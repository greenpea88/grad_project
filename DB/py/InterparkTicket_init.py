#!/usr/bin/env python
# coding: utf-8

# ## 인터파크티켓 크롤링
# #### 공연 개별 상세페이지에서 정보 가져오기

# In[ ]:


from selenium import webdriver
import datetime
import time

path="/Users/mocha/chromedriver"
driver=webdriver.Chrome(path)


# In[ ]:


def get_data(p_type):
    
    p_info={'Type':'', 'Title':'', 'TicketOpen':'', 'Venue':'', 'StartDate':'', 'EndDate':'', 'Time':'',
           'RunningTime':'', 'Price':'', 'BuyTicket':'인터파크', 'PosterSrc':'', 'RegisteredTime':'', 'Performer':''}
    performer_info=[]
    performer={'Name':'', 'ImgSrc':''}
    
#     #RegisteredTime
#     p_info['RegisteredTime']=datetime.datetime.now()
    
    # Type
    p_info['Type']=p_type
    
    # Title
    p_info['Title']=driver.find_element_by_id('IDGoodsName').text
    
    elem=driver.find_element_by_id('TabA')
    
    #PosterSrc
    p_info['PosterSrc']=elem.find_element_by_tag_name('img').get_attribute('src')
    
    # Price
    try:
        for tr in elem.find_elements_by_tag_name('tr'):
            td=tr.find_elements_by_tag_name('td')
            p_info['Price']=p_info['Price']+td[0].text+' '+td[2].text+', '
        p_info['Price']=p_info['Price'][:-2]
    except IndexError:
        pass
    
    # Venue, StartDate, EndDate, Performer
    for dl in elem.find_elements_by_tag_name('dl'):
        info_type=dl.find_element_by_tag_name('dt')
        info_data=dl.find_element_by_tag_name('dd')
        
        if info_type.text=='장소':
            p_info['Venue']=info_data.text
        elif info_type.text=='기간':
            if '~' in info_data.text:
                p_info['StartDate']=info_data.text.split(' ', 3)[0]       # split('기준', '횟수')
                tmp=info_data.text.split(' ', 3)[2]
                if tmp!='오픈런':
                    p_info['EndDate']=tmp
            else:
                p_info['StartDate']=p_info['EndDate']=info_data.text.split(' ', 2)[0]
        elif info_type.text=='출연':
            driver.find_element_by_id('aTabC').click()
            driver.switch_to.frame('ifrTabC')
            for i in driver.find_elements_by_class_name('Actor'):
                performer['Name']=i.find_element_by_tag_name('h3').text
                performer['ImgSrc']=i.find_element_by_tag_name('img').get_attribute('src')
                performer_copy=performer.copy()
                performer_info.append(performer_copy)
            p_info['Performer']=performer_info
#             print(performer_info)
#                 performer.append(i.find_element_by_tag_name('h3').text)
        else:
            pass
        
    driver.switch_to.default_content()

    # Time, RunningTime
    elem=driver.find_element_by_id('divTabArea')
    for data in elem.find_elements_by_class_name('Data'):
        info_type=data.find_element_by_tag_name('h4')
        info_data=data.find_element_by_class_name('Data_infoarea')
        
        if info_type.text=='공연시간 정보':
            p_info['Time']=info_data.text
        elif info_type.text=='상품관련정보':
            for tr in info_data.find_element_by_tag_name('table').find_elements_by_tag_name('tr'):
                for th in tr.find_elements_by_tag_name('th'):
                    if th.text=='공연시간':
                        p_info['RunningTime']=tr.find_element_by_tag_name('td').text.split('분', 2)[0]
                    else:
                        pass
        else:
            pass
    
    print(p_info)
    
    return p_info


# In[ ]:


def get_link():
    
    link=[]
    p_table=driver.find_element_by_xpath('/html/body/table/tbody/tr[2]/td[3]/div/div/div[2]/div/table/tbody')

    for row in p_table.find_elements_by_tag_name('tr'):
        link.append(row.find_element_by_tag_name('a').get_attribute('href'))
        
    return link


# In[ ]:


# p_type={'Mus':'1', 'Liv':'2', 'Dra':'3', 'Cla':'4', 'Eve&SubCa=Eve_T':'5', 'Fam':'6'}
p_type={'Cla':'4'}
p_data=[]       # 공연정보 리스트

for key in p_type:
    
    # [장르 - 전체보기 - 상품명순] 페이지 접근
    driver.get("http://ticket.interpark.com/TPGoodsList.asp?Ca="+key+"&Sort=4")
    time.sleep(0.5)
    
    # 각 공연의 상세페이지 주소 p_link에 가져오기
    p_link=get_link()
    
    # p_link의 주소로 상세페이지에 접근하여 데이터 가져오기
    for link in p_link:
        
        driver.get(link)
        time.sleep(0.5)
            
        # 상세페이지에서 얻은 공연정보 데이터를 p_data에 추가하기
        p_data.append(get_data(p_type[key]))
    
    print(p_data)


# In[ ]:


## MySql DB에 데이터 넣기

import pymysql

conn=pymysql.connect(host='localhost', user='root', password='qwerty1234', db='grad_test', charset='utf8mb4')
try:
    with conn.cursor() as cursor:
        sql_show='INSERT ignore INTO shows (type, title, venue, start_date, end_date, time, ticket_open, price, running_time, buy_ticket, poster_src, registered_time) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)'
        sql_performer_exist='SELECT count(*) FROM performer WHERE name=%s'
        sql_performer_write='INSERT ignore INTO performer (name, img_src) VALUES (%s, %s)'
        sql_show_performer='INSERT INTO show_performer (performer_id, show_id) VALUES (%s, %s)'
        sql_select_show_id='SELECT id FROM shows WHERE title=%s'
        sql_select_performer_id='SELECT id FROM performer WHERE name=%s'
        for r in p_data:
            cursor.execute(sql_show, (r['Type'], r['Title'], r['Venue'], r['StartDate'], r['EndDate'], r['Time'], r['TicketOpen'], r['Price'], r['RunningTime'], r['BuyTicket'], r['PosterSrc'], datetime.datetime.now()))
            conn.commit()
            cursor.execute(sql_select_show_id, r['Title'])
            show_id=cursor.fetchone()
            for s in r['Performer']:
                cursor.execute(sql_select_performer_id, s['Name'])
                performer_id=cursor.fetchone()
                if performer_id==None :
                    cursor.execute(sql_performer_write, (s['Name'], s['ImgSrc']))
                    conn.commit()
                    cursor.execute(sql_select_performer_id, s['Name'])
                    performer_id=cursor.fetchone()
                cursor.execute(sql_show_performer, (performer_id, show_id))
                conn.commit()
                
finally:
    conn.close()


# In[ ]:




