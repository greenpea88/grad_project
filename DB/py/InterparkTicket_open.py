#!/usr/bin/env python
# coding: utf-8

# ## 인터파크티켓 티켓오픈공지 크롤링

# In[ ]:


from selenium import webdriver
import datetime
import time

path="C:/Users/YYJ/Desktop/Programming/Python/Crawling/chromedriver.exe"
driver=webdriver.Chrome(path)


# In[ ]:


import re

driver.get("http://ticket.interpark.com/webzine/paper/TPNoticeList.asp")
driver.switch_to.frame('iFrmNotice')
driver.find_element_by_class_name('array').find_elements_by_tag_name('a')[1].click()

link=[]
page=driver.find_element_by_class_name('total').find_element_by_tag_name('strong').text

for pg in range(0, int(page)) :
    for tr in driver.find_element_by_tag_name('tbody').find_elements_by_tag_name('tr') :
        link.append(tr.find_element_by_class_name('subject').find_element_by_tag_name('a').get_attribute('href'))
    if pg!=int(page)-1:
        driver.find_element_by_class_name('no').find_element_by_tag_name('a').click()

open_info=[]
    
for l in link :
    open_data={'Title':'', 'DateTime':''}
    driver.get(l)
    d1=driver.find_element_by_class_name('detail_top').find_element_by_class_name('open').text
    d=re.findall("\d+", d1)
    print (d)
    if d1.split(' ', 4)[3] == "오전":
        open_data['DateTime']=datetime.datetime(int(d[0]), int(d[1]), int(d[2]), int(d[3]))
    else :
        open_data['DateTime']=datetime.datetime(int(d[0]), int(d[1]), int(d[2]), int(d[3])+12)
    try:
        open_data['Title']=driver.find_element_by_class_name('comment').find_element_by_tag_name('strong').text
    except NoSuchElementException:
        open_data['Title']=driver.find_element_by_class_name('comment').find_element_by_tag_name('p').text
    open_info.append(open_data)
    
print(open_info)


# In[ ]:


## MySql DB에 데이터 넣기

import pymysql

conn=pymysql.connect(host='localhost', user='root', password='asdf1234', db='grad_test', charset='utf8mb4')
try:
    with conn.cursor() as cursor:
        sql_select_show_id='SELECT id FROM shows WHERE title=%s'
        sql_update_ticketopen='UPDATE shows SET ticket_open=%s, registered_time=%s WHERE id=%s'
        sql_new_show_ticketopen='INSERT ignore INTO shows (title, ticket_open, registered_time) VALUES (%s, %s, %s)'
        
        for r in open_info:
            cursor.execute(sql_select_show_id, r['Title'])
            show_id=cursor.fetchone()
            if show_id==None :
                # 공연 제목이 다르게 입력되어 있어 찾지 못하거나 기존에 등록되어 있는 공연이 아닐 경우 수기로 입력해주어야 할 듯
                print (r['Title'])
#                 cursor.execute(sql_new_show_ticketopen, (r['Title'], r['DateTime'], datetime.datetime.now()))
            else :
                cursor.execute(sql_update_ticketopen, (r['DateTime'], datetime.datetime.now(), show_id))
                conn.commit()
                
        # 티켓오픈날짜 지난 것 지워주기
        for s in shows:
            sql_update_ticketopen_ended='UPDATE shows SET ticket_open="0000-00-00 00:00:00", registered_time=now() WHERE ticket_open!="0000-00-00 00:00:00" AND ticket_open<=now()'
            cursor.execute(sql_update_ticketopen_ended)
        conn.commit()
            
finally:
    conn.close()


# In[ ]:




