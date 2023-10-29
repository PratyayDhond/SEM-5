


create table trainhalts
 (id varchar(5) , 
 seqno integer , 
 stcode varchar(10), 
 timein  varchar(5) ,
 timeout varchar(5) , 
 primary key (id,seqno) ); 


create table track
 (stcode1 varchar(5) ,
 stcode2 varchar(5), 
distance integer ,
 primary key (stcode1,stcode2) );

create table station
 (stcode varchar(5),
 name varchar(20),
 primary key (stcode));

create table train
 (id varchar(5) ,
 name varchar(20),
 primary key (id) );


