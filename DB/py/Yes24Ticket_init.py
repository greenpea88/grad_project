#!/usr/bin/env python
# coding: utf-8

# ## 예스24티켓 크롤링
# #### 공연 개별 상세페이지에서 정보 가져오기

# In[1]:


from selenium import webdriver
import datetime
import time

path="/Users/mocha/chromedriver"
driver=webdriver.Chrome(path)


# In[2]:


def get_data(p_type):
    
    p_info={'Type':'', 'Title':'', 'TicketOpen':'', 'Venue':'', 'StartDate':'', 'EndDate':'', 'Time':'',
           'RunningTime':'', 'Price':'', 'BuyTicket':'예스24', 'PosterSrc':'', 'RegisteredTime':'', 'Performer':''}
    performer=[]
    
#     p_info={'Type':'', 'Title':'', 'Subtitle':'', 'Venue':'', 'StartDate':'', 'EndDate':'', 'Performer':''}
#     performer=[]
    
    # Type
    p_info['Type']=p_type
    
    # Title
    p_info['Title']=driver.find_element_by_class_name('rn-big-title').text
    
    # RunningTime
    div=driver.find_element_by_class_name('rn-product-area1')
    dd=div.find_elements_by_tag_name('dd')
    p_info['RunningTime']=dd[1].text.split('분', 2)[0]
    p_info['RunningTime']=p_info['RunningTime'][1:]
    p_info['RunningTime']=p_info['RunningTime'].replace("총 ", "")
    
    # PosterSrc
    try:
        p_info['PosterSrc']=driver.find_element_by_xpath('//*[@id="mainForm"]/div[10]/div/div[1]/div[1]/div[1]/img').get_attribute('src')
    except:
        p_info['PosterSrc']=driver.find_element_by_xpath('//*[@id="mainForm"]/div[9]/div/div[1]/div[1]/div/img').get_attribute('src')
    
    # Price
    dd = driver.find_element_by_class_name('rn-product-price')
    try:
        for li in dd.find_elements_by_tag_name('li'):
            p_info['Price']=p_info['Price']+li.text+', '
        p_info['Price']=p_info['Price'][:-2]
    except IndexError:
        pass
    
    # Date
    p_info['StartDate']=driver.find_element_by_class_name('ps-date').text.split(' ', 2)[0]
    p_info['EndDate']=driver.find_element_by_class_name('ps-date').text.split(' ', 2)[2]
    
    # Time
    try:
        p_info['Time']=driver.find_element_by_xpath('//*[@id="mainForm"]/div[10]/div/div[1]/div[2]/div[2]/dl/dd[1]').text
    except:
        p_info['Time']=driver.find_element_by_xpath('//*[@id="mainForm"]/div[9]/div/div[1]/div[2]/div[2]/dl/dd[1]').text
    if(p_info['Time'].find('시')==-1):
        p_info['Time']=' '
    p_info['Time'].replace('\n', ' ')
    
    # Venue
    try:
        p_info['Venue']=driver.find_element_by_class_name('ps-location').text
    except:
        p_info['Venue']=driver.find_element_by_class_name('ps-location2').text
    
    # Performer
    for i in driver.find_elements_by_class_name('rn-product-peole'):
        performer.append(i.text)
    p_info['Performer']=performer
    
    return p_info


# In[3]:


def get_link():
    
    link=[]
    
    # 무한 스크롤
    last_height=driver.execute_script("return document.body.scrollHeight;")
    while True:
        # Scroll down to bottom
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        # Wait to load page
        time.sleep(0.5)
        # Calculate new scroll height and compare with last scroll height
        new_height=driver.execute_script("return document.body.scrollHeight")
        if new_height==last_height:
            break
        last_height=new_height
    
    # 상세페이지로 접근하는 웹요소를 찾아 url에 입력할 번호만 파싱
    for row in driver.find_elements_by_class_name('ms-list-imgs'):
        for col in row.find_elements_by_tag_name('a'):
            s=col.get_attribute('onclick')
            s1=s.split('(')[1]
            s2=s1.split(')')[0]
            link.append(s2)
    
    return link


# In[7]:


# p_type={'15456':'콘서트', '15457':'뮤지컬', '15458':'연극', '15459':'클래식', '15460':'전시'}
p_type={'15459':'4'}
p_data=[]       # 공연정보 리스트

for key in p_type:
    
    # [장르 - 전체보기 - 신상품순] 페이지 접근
    driver.get("http://ticket.yes24.com/New/Genre/GenreList.aspx?genretype=1&genre="+key)
    time.sleep(0.5)
    driver.find_element_by_xpath('/html/body/section/p[1]/span[2]/a').click()
    
    # 각 공연의 상세페이지로 접근하기 위해 클릭할 웹요소 p_link에 가져오기
    p_link=get_link()
    
    # p_link의 주소로 상세페이지에 접근하여 데이터 가져오기
    for link in p_link:
        
        driver.get('http://ticket.yes24.com/Perf/'+link)
        time.sleep(0.5)
            
        # 상세페이지에서 얻은 공연정보 데이터를 p_data에 추가하기
        p_data.append(get_data(p_type[key]))
        
    print(p_data)


# In[1]:


## MySql DB에 데이터 넣기

import pymysql

conn=pymysql.connect(host='localhost', user='root', password='qwerty1234', db='grad_test', charset='utf8mb4')
try:
    with conn.cursor() as cursor:
        sql_show='INSERT ignore INTO shows (type, title, venue, start_date, end_date, time, ticket_open, price, running_time, buy_ticket, poster_src, registered_time) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)'
        sql_performer_exist='SELECT count(*) FROM performer WHERE name=%s'
        sql_performer_write='INSERT ignore INTO performer (name) VALUES (%s)'
        sql_show_performer='INSERT INTO show_performer (performer_id, show_id) VALUES (%s, %s)'
        sql_select_show_id='SELECT id FROM shows WHERE title=%s'
        sql_select_show_buy='SELECT buy_ticket FROM shows WHERE id=%s'
        sql_select_performer_id='SELECT id FROM performer WHERE name=%s'
        sql_update_show_buy='UPDATE shows SET buy_ticket=%s WHERE id=%s'
        for r in p_data:
            cursor.execute(sql_select_show_id, r['Title'])
            show_id=cursor.fetchone()
            if show_id==None:
                cursor.execute(sql_show, (r['Type'], r['Title'], r['Venue'], r['StartDate'], r['EndDate'], r['Time'], r['TicketOpen'], r['Price'], r['RunningTime'], r['BuyTicket'], r['PosterSrc'], datetime.datetime.now()))
                conn.commit()
                cursor.execute(sql_select_show_id, r['Title'])
                show_id=cursor.fetchone()
                for s in r['Performer']:
                    cursor.execute(sql_select_performer_id, s)
                    performer_id=cursor.fetchone()
                    if performer_id==None :
                        cursor.execute(sql_performer_write, (s))
                        conn.commit()
                        cursor.execute(sql_select_performer_id, s)
                        performer_id=cursor.fetchone()
                    cursor.execute(sql_show_performer, (performer_id, show_id))
                    conn.commit()
            else:
                cursor.execute(sql_select_show_buy, show_id)
                tmp_buy=cursor.fetchone()[0]
                if(tmp_buy.find('예스24')==-1):
                    tmp_buy=tmp_buy+', '+r['BuyTicket']
                cursor.execute(sql_update_show_buy, (tmp_buy, show_id))
                conn.commit()
                
finally:
    conn.close()


# In[ ]:




