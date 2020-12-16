#!/usr/bin/env python
# coding: utf-8

# ## Selenium을 이용한 크롤링
# #### 멜론티켓

# In[ ]:


from selenium import webdriver
import datetime
import time

path="/Users/mocha/chromedriver"
driver=webdriver.Chrome(path)


# In[ ]:


def get_data(p_type):
    
    p_info={'Type':'', 'Title':'', 'TicketOpen':'', 'Venue':'', 'StartDate':'', 'EndDate':'', 'Time':'',
           'RunningTime':'', 'Price':'', 'BuyTicket':'멜론', 'PosterSrc':'', 'RegisteredTime':'', 'Performer':''}
#     p_info={'Type':'', 'Title':'', 'Subtitle':'', 'Venue':'', 'StartDate':'', 'EndDate':'', 'Performer':''}
    performer=[]
    
    # Type
    p_info['Type']=p_type
    
    # Title
    p_info['Title']=driver.find_element_by_xpath('//*[@id="conts"]/div/div[1]/div[1]/div[2]/p[2]').text
    
#     # Subtitle
#     p_info['Subtitle']=driver.find_element_by_xpath('//*[@id="conts"]/div/div[1]/div[1]/div[2]/p[2]').text

    # RunningTime
    p_info['RunningTime']=driver.find_element_by_xpath('//*[@id="conts"]/div/div[1]/div[1]/div[2]/div[2]/dl[1]/dd[2]').text
    
    # PosterSrc
    p_info['PosterSrc']=driver.find_element_by_xpath('//*[@id="conts"]/div/div[1]/div[1]/div[1]/img').get_attribute('src')
    
    # Time
    try:
        p_info['Time']=driver.find_element_by_xpath('//*[@id="conts"]/div/div[3]/div[1]/div[2]/p[2]/span').text.split(') ', 2)[1]
    except:
        p_info['Time']=driver.find_element_by_xpath('//*[@id="conts"]/div/div[3]/div[1]/div[1]').text[4:]
        
    # Price
    try:
        p_info['Price']=driver.find_element_by_xpath('//*[@id="conts"]/div/div[3]/div[1]/div[3]/ul/li').text
        p_info['Price'].replace('\n', ' ')
    except:
        p_info['Price']=driver.find_element_by_class_name('list_seat').text
    
    # Venue
    try:
        p_info['Venue']=driver.find_element_by_xpath('//*[@id="performanceHallBtn"]/span[1]').text
    except:
        p_info['Venue']=' '
    
    # Date
    temp_date=driver.find_element_by_xpath('//*[@id="periodInfo"]').text
    if '-' in temp_date:
        p_info['StartDate']=temp_date.split(' ', 2)[0]
        p_info['EndDate']=temp_date.split(' ', 2)[2]

    # Performer
    li_artist=driver.find_elements_by_class_name('list_artist')
    for i in li_artist:
        if i.find_element_by_tag_name('li') != 'no_artist':
            performer.append(i.find_element_by_class_name('singer').text)
    p_info['Performer']=performer
    
    return(p_info)


# In[ ]:


def get_link():
    
    link=[]
    temp_li=driver.find_element_by_xpath('//*[@id="perf_poster"]')
    
    try:
        for p_block in temp_li.find_elements_by_tag_name('li'):
            link.append(p_block.find_element_by_tag_name('a').get_attribute('href'))
    except:
        pass
            
    return link


# In[ ]:


plays=[]       # 전체 공연정보 리스트
p_data=[]       # 장르별 공연정보 리스트
# plays_type=['GENRE_CON', 'GENRE_ART', 'GENRE_CLA', 'GENRE_EXH']
p_type={'GENRE_CLA':'4'}

for key in p_type:
    
    # 장르에 따른 '전체보기 - 상품명순' 페이지 접근
    driver.get("https://ticket.melon.com/concert/index.htm?genreType="+key)
    time.sleep(1)
    
    # 최신순으로 정렬
    driver.find_element_by_xpath('//*[@id="sort_lately_admin"]/a').click()
    
    # 각 공연의 상세페이지로 접근하기 위해 클릭할 웹요소 p_link에 가져오기
    p_link=get_link()
    
    for link in p_link:
        
        # 상세페이지 링크에 접근
        driver.get(link)
        time.sleep(0.5)
            
        # 공연정보 데이터를 p_data에 가져오기
        p_data.append(get_data(p_type[key]))
        
        # 전체 공연 리스트에 p_data 추가하기 (이중리스트)
#         plays.append(p_data)
        
        print(p_data)


# In[ ]:


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
                if(tmp_buy.find('멜론')==-1):
                    tmp_buy=tmp_buy+', '+r['BuyTicket']
                cursor.execute(sql_update_show_buy, (tmp_buy, show_id))
                conn.commit()
                
finally:
    conn.close()


# In[ ]:




