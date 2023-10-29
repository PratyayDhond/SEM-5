drop table trainhalts;
drop table train;
drop table track;
drop table station;

--This table contains one row for every halt of a train. 
-- id     : id of the train
-- seqno  : the halt number. Assume that the starting station has seqno as 0
-- stcode : station code of this halt 
-- timein : time at which the train arrives at this station. (will be null for the starting station of a train)
-- timeout: time at which the train departs this station. (will be null for the terminating station of a train)
-- If a train passes through a station without stopping, then there will be an entry with timein = timeout.
create table trainhalts
	(id varchar(5) , 
	seqno integer , 
	stcode varchar(10), 
	timein  varchar(5) ,
	timeout varchar(5) , 
	primary key (id,seqno) ); 

-- This table stores the distances between directly connected stations stcode1 and stcode2.
-- Assume that this represents a directed track. i.e, for two stations A and B, there will be 
-- an entry corresponding to (A, B, distance) and another for (B,A, distance).
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


