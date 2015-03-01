set GLOBAL max_connections=800
show variables like 'max_connections';

----------------------report---------------------
select * from tbl_pediatrics_data_weekly where duration <= '2013.12.26-2014.01.01' order by duration desc limit 0,11040;


set profiling=1;
show profiles;




--'2013.12.26-2014.01.01'

select pdw.duration, 
pdw.region, 
IFNULL(sum(pdw.pnum),0) as pnum 
from tbl_pediatrics_data_weekly pdw 
where duration <= CONCAT(DATE_FORMAT(?,'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d')) 
group by pdw.duration,pdw.region 
order by pdw.duration desc 
limit 0,72;


-----------------------------------------------------------

update tbl_respirology_data rd set etmsCode=(select h.saleCode from tbl_hospital h where h.name = rd.hospitalName) where rd.etmsCode is null;
update tbl_pediatrics_data pd set etmsCode=(select h.saleCode from tbl_hospital h where h.name = pd.hospitalName) where pd.etmsCode is null;

delete from tbl_pediatrics_data_weekly where duration='2014.01.10-2014.01.16';
delete from tbl_respirology_data_weekly where duration='2014.01.10-2014.01.16';

select * from tbl_userinfo where telephone in (
	select distinct telephone from tbl_userinfo 
	where telephone != '#N/A'
	group by telephone
	having count(1)>1
)
order by telephone;

select * from tbl_userinfo where userCode in (
    select distinct userCode from tbl_userinfo 
    where userCode != '#N/A' 
    and userCode != '2000003' 
    and userCode != '2000002'
    group by userCode
    having count(1)>1
) 
and level in ('REP','DSM','RSM','RSD') 
order by regionCenter, region, superior, userCode;

select * from tbl_userinfo where superior in (
    select distinct superior from tbl_userinfo 
    where userCode = '2000003'
    group by region, superior
    having count(1)>1
) 
and userCode = '2000003'
order by regionCenter, region, superior, userCode;

select * from tbl_userinfo where region in (
    select distinct userCode from tbl_userinfo 
    where userCode = '2000002'
    group by region 
    having count(1)>1
) 
and userCode = '2000002' 
order by regionCenter, region, superior, userCode;

select * from tbl_respirology_data 
group by date_format(createdate,'%Y-%m-%d'), hospitalName 
having count(1) > 1;

select * from tbl_pediatrics_data 
group by date_format(createdate,'%Y-%m-%d'), hospitalName 
having count(1) > 1;

select * from tbl_chestSurgery_data 
group by date_format(createdate,'%Y-%m-%d'), hospitalCode 
having count(1) > 1;

select rd.* from tbl_respirology_data rd, tbl_userinfo u 
where date_format(rd.createdate,'%Y-%m-%d') = '2014-05-09' 
and rd.etmsCode=u.userCode 
and u.telephone in ('15889967386','13299265923');

select pd.* from tbl_pediatrics_data pd, tbl_userinfo u 
where date_format(pd.createdate,'%Y-%m-%d') = '2014-05-09' 
and pd.etmsCode=u.userCode 
and u.telephone in ('15889967386','13299265923');

select count(1) from tbl_respirology_data where date_format(createdate,'%Y-%m-%d') = date_format('2014-01-25','%Y-%m-%d');
select count(1) from tbl_pediatrics_data where date_format(createdate,'%Y-%m-%d') = date_format('2014-01-25','%Y-%m-%d');

select * from tbl_respirology_data 
where hospitalName = '保定市252医院' 
and date_format(createdate,'%Y-%m-%d') = date_format('2014-06-10','%Y-%m-%d');

select * from tbl_pediatrics_data 
where hospitalName = '广州医科大学附属第二医院' 
and date_format(createdate,'%Y-%m-%d') = date_format('2014-06-10','%Y-%m-%d');

select * from tbl_respirology_data 
where (dsmCode='#N/A' or etmsCode='#N/A')
and date_format(createdate,'%Y-%m-%d') = date_format('2014-01-24','%Y-%m-%d');

select * from tbl_pediatrics_data 
where (dsmCode='#N/A' or etmsCode='#N/A')
and date_format(createdate,'%Y-%m-%d') = date_format('2014-01-24','%Y-%m-%d');

========================================================================================================================
--查找医生表中同一家医院code重复的医生
select d.* from tbl_doctor d
where concat(d.hospitalCode,d.code) in (
	select concat(hospitalCode,code) from tbl_doctor
	group by hospitalCode,code 
	having count(1)>1
) 
order by hospitalCode;

--查询重复医生所在医院的所有医生
select * from tbl_doctor where hospitalCode in (
	select d.hospitalCode from tbl_doctor d
	where concat(d.hospitalCode,d.code) in (
		select concat(hospitalCode,code) from tbl_doctor
		group by hospitalCode,code 
		having count(1)>1
	) 
)
order by hospitalCode;

--
select * from tbl_doctor
where hospitalCode='CQCQ006H';

select * from tbl_doctor_weekly 
where hospitalCode='ZJHZ015H' 
and duration ='2014.10.20-2014.10.26';

select * from tbl_doctor_weekly 
group by duration,hospitalCode,code 
having count(1) > 1

(210,2153,4098,6048,18468,26658) | 0001 | CQCQ006H --> doctorId 1853 --> 0002
update tbl_doctor_weekly set code='0002' where id in (210,2153,4098,6048,18468,26658);

(326,2269,4214,6164,18581,26770) | 0001 | FJFZ004H --> doctorId 943 --> 0002
update tbl_doctor_weekly set code='0002' where id in (326,2269,4214,6164,18581,26770);

(307,2250,4195,6145,18562,26751) | 0001 | FJFZ016H --> 0003
update tbl_doctor_weekly set code='0003' where id in (307,2250,4195,6145,18562,26751);
 -----------------
								  396 | 董李   | 0001 | FJFZ016H	
								| 398 | 林娟   | 0002 | FJFZ016H	
								| 399 | 唐素萍  | 0003 | FJFZ016H
 -----------------
(812,2755,4700,6650,19064,27247) | 0001 | GDSS001H --> 0003
update tbl_doctor_weekly set code='0003' where id in (812,2755,4700,6650,19064,27247);
 
								 1049 | 李焕琼 | 0001 | GDSS001H
								| 1053 | 黄如章 | 0002 | GDSS001H
								| 1054 | 钟斌才 | 0003 | GDSS001H
------------------
(1144,3087,5032,6982,19392,27573)	| 0001 | HUCS002H --> doctorId 1808 --> 0004
update tbl_doctor_weekly set code='0004' where id in (1144,3087,5032,6982,19392,27573);

(125,2068,4013,5963,18383,26573)	| 0001 | JSCZ001H --> doctorId 190 --> 0002
update tbl_doctor_weekly set code='0002' where id in (125,2068,4013,5963,18383,26573);

(661,2604,4549,6499,18914,27097)		| 0001 | SX03920N --> doctorId 848 --> 0002
update tbl_doctor_weekly set code='0002' where id in (661,2604,4549,6499,18914,27097);

(583,2526,4471,6421,18838,27021)		| 0001 | ZJHZ015H --> doctorId 1780 --> 0012
update tbl_doctor_weekly set code='0012' where id in (583,2526,4471,6421,18838,27021);

(584,2527,4472,6422,18839,27022)		| 0002 | ZJHZ015H --> doctorId 1781 --> 0013
update tbl_doctor_weekly set code='0013' where id in (584,2527,4472,6422,18839,27022);


update tbl_doctor_weekly dw,tbl_doctor d set dw.doctorName=d.name, dw.doctorId=d.id 
where dw.hospitalCode = d.hospitalCode and dw.code = d.code;

update tbl_doctor_weekly dw,tbl_doctor_history dh set dw.doctorName=dh.drName, dw.doctorId=dh.doctorId 
where dw.hospitalCode = dh.hospitalCode and dw.code = dh.drCode and dw.doctorId is null;


select h.region
,h.rsmRegion
,(select u.name from tbl_userinfo u where u.userCode = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region) as dsmName
,case when dw.salesCode is null or dw.salesCode = '' then 'N/A' else dw.salesCode end as salesCode 
,IFNULL((select u.name from tbl_userinfo u where u.userCode = dw.salesCode and u.superior = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region),'N/A') as salesName
,dw.hospitalCode
,h.name
,concat(dw.hospitalCode,dw.code) as doctorCode
,dw.doctorId
,dw.doctorName 
from tbl_doctor_weekly dw, tbl_hospital h 
where not exists(
	select 1 
	from tbl_home_data hd 
	where hd.createdate between '2014.10.13' and '2014.10.20' 
	and hd.doctorId = dw.doctorId 
) 
and dw.hospitalCode = h.code 
and dw.duration = '2014.10.13-2014.10.19' 
order by region,rsmRegion,dsmName,salesName,doctorName;


=======================================================================================================================================
-----------------------------------------------------salesPEDDaily-------------------------------------------------


-----------------------------------------------------------------------------------

-----------------------------------------------------PED Daily APP report-----------------------------------------
--18501622299 zhangyu
select u.name,u.userCode,
( 
	select count(1) 
	from tbl_hospital h 
	where h.dsmName = u.name 
) hosNum,  
( 
	select count(1) 
	from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and h1.dsmName = u.name 
	and TO_DAYS(NOW()) - TO_DAYS(pd.createdate) = 1
) inNum,  
( 
	select IFNULL(sum(pd.pnum),0) 
	from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and h1.dsmName = u.name 
	and TO_DAYS(NOW()) - TO_DAYS(pd.createdate) = 1 
) pnum,  
( 
	select IFNULL(sum(pd.whnum),0) 
	from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and h1.dsmName = u.name 
	and TO_DAYS(NOW()) - TO_DAYS(pd.createdate) = 1 
) whnum,  
( 
	select IFNULL(sum(pd.lsnum),0) 
	from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and h1.dsmName = u.name 
	and TO_DAYS(NOW()) - TO_DAYS(pd.createdate) = 1 
) lsnum,  
( 
	select IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 )  
    from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and h1.dsmName = u.name 
	and TO_DAYS(NOW()) - TO_DAYS(pd.createdate) = 1  
) averageDose, 
( 
	select IFNULL( sum( IFNULL(pd.hqd,0)/100*IFNULL(pd.whnum,0) ) / sum( IFNULL(pd.whnum,0) ),0 )  
    from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and h1.dsmName = u.name 
	and TO_DAYS(NOW()) - TO_DAYS(pd.createdate) = 1  
) hmgRate, 
( 
	select IFNULL( sum( IFNULL(pd.hbid,0)/100*IFNULL(pd.whnum,0) + IFNULL(pd.oqd,0)/100*IFNULL(pd.whnum,0) ) / sum( IFNULL(pd.whnum,0) ),0 )   
    from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and h1.dsmName = u.name 
	and TO_DAYS(NOW()) - TO_DAYS(pd.createdate) = 1  
) omgRate, 
( 
	select IFNULL( sum( IFNULL(pd.obid,0)/100*IFNULL(pd.whnum,0) + IFNULL(pd.tqd,0)/100*IFNULL(pd.whnum,0) ) / sum( IFNULL(pd.whnum,0) ),0 )   
    from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and h1.dsmName = u.name 
	and TO_DAYS(NOW()) - TO_DAYS(pd.createdate) = 1  
) tmgRate, 
( 
	select IFNULL( sum( IFNULL(pd.tbid,0)/100*IFNULL(pd.whnum,0) ) / sum( IFNULL(pd.whnum,0) ),0 )    
    from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and h1.dsmName = u.name 
	and TO_DAYS(NOW()) - TO_DAYS(pd.createdate) = 1  
) fmgRate  
from tbl_userinfo u 
where u.level='DSM'
and u.region = ( 
	select region from tbl_userinfo where telephone='18501622299' 
) ;

-------------------------------------------------------------

----------------------------------------getTopAndBottomRSMData-----------------------
select inRateMinT.inRateMin, 
inRateMinT.inRateMinUser,
inRateMaxT.inRateMax, 
inRateMaxT.inRateMaxUser, 
whRateMinT.whRateMin,
whRateMinT.whRateMinUser,
whRateMaxT.whRateMax,
whRateMaxT.whRateMaxUser,
averageDoseMinT.averageDoseMin,
averageDoseMinT.averageDoseMinUser,
averageDoseMaxT.averageDoseMax,
averageDoseMaxT.averageDoseMaxUser
from 
(
	select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMin,hosNumTemp.name as inRateMinUser
	from 
	( 
		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name
		from tbl_hospital h, tbl_userinfo u
		where h.isPedAssessed='1' 
		and h.rsmRegion = u.region 
		and u.level='RSM' 
		group by u.region 
	) hosNumTemp, 
	(
		select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from (
			select IFNULL(count(1),0) as inNum, h.rsmRegion 
			from tbl_pediatrics_data pd, tbl_hospital h 
			where pd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(pd.createdate)
			and h.isPedAssessed='1' 		
			group by h.rsmRegion 
		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region 
		where u.level='RSM'
	) inNumTemp
	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion 
	order by inNumTemp.inNum/hosNumTemp.hosNum
	limit 1	
 ) inRateMinT,
 (
	select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMax,hosNumTemp.name as inRateMaxUser
	from 
	( 
		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name
		from tbl_hospital h, tbl_userinfo u
		where h.isPedAssessed='1' 
		and h.rsmRegion = u.region 
		and u.level='RSM' 
		group by u.region 
	) hosNumTemp, 
	(
		select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from (
			select IFNULL(count(1),0) as inNum, h.rsmRegion 
			from tbl_pediatrics_data pd, tbl_hospital h 
			where pd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(pd.createdate)
			and h.isPedAssessed='1' 		
			group by h.rsmRegion 
		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region 
		where u.level='RSM'
	) inNumTemp
	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion 
	order by inNumTemp.inNum/hosNumTemp.hosNum desc
	limit 1	
 ) inRateMaxT ,
 (
	select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMin,pNumTemp.name as whRateMinUser
	from 
	( 
		select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (
			select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion 
			from tbl_pediatrics_data pd, tbl_hospital h 
			where pd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(pd.createdate)
			and h.isPedAssessed='1' 		
			group by h.rsmRegion 
		) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region 
		where u.level='RSM' 
	) pNumTemp, 
	(
		select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from (
			select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion 
			from tbl_pediatrics_data pd, tbl_hospital h 
			where pd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(pd.createdate)
			and h.isPedAssessed='1' 		
			group by h.rsmRegion 
		) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region 
		where u.level='RSM'
	) lsNumTemp
	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion 
	order by lsNumTemp.lsNum/pNumTemp.pNum
	limit 1	
 ) whRateMinT,
 (
	select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMax,pNumTemp.name as whRateMaxUser
	from 
	( 
		select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (
			select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion 
			from tbl_pediatrics_data pd, tbl_hospital h 
			where pd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(pd.createdate)
			and h.isPedAssessed='1' 		
			group by h.rsmRegion 
		) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region 
		where u.level='RSM' 
	) pNumTemp, 
	(
		select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from (
			select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion 
			from tbl_pediatrics_data pd, tbl_hospital h 
			where pd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(pd.createdate)
			and h.isPedAssessed='1' 		
			group by h.rsmRegion 
		) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region 
		where u.level='RSM'
	) lsNumTemp
	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion 
	order by lsNumTemp.lsNum/pNumTemp.pNum desc
	limit 1	
 ) whRateMaxT,
(
	select IFNULL(av1.averageDose,0) as averageDoseMin, u.name as averageDoseMinUser from 
	(
		select IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose, h.rsmRegion
		from tbl_pediatrics_data pd, tbl_hospital h
		where pd.hospitalName = h.name 
		and TO_DAYS(?) = TO_DAYS(pd.createdate) 
		and h.isPedAssessed='1' 
		group by h.rsmRegion 
	) av1 right join tbl_userinfo u on av1.rsmRegion = u.region
	where u.level='RSM' 
	order by av1.averageDose
	limit 1	
) averageDoseMinT,
(
	select IFNULL(av2.averageDose,0) as averageDoseMax, u.name as averageDoseMaxUser  from 
	(
		select IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose, h.rsmRegion
		from tbl_pediatrics_data pd, tbl_hospital h
		where pd.hospitalName = h.name 
		and TO_DAYS(?) = TO_DAYS(pd.createdate) 
		and h.isPedAssessed='1' 
		group by h.rsmRegion 
	) av2 right join tbl_userinfo u on av2.rsmRegion = u.region
	where u.level='RSM' 
	order by av2.averageDose desc
	limit 1	
 ) averageDoseMaxT
 
------------------------------------------------------------------------------------------------------

 select inRateMinT.inRateMin, 
inRateMinT.inRateMinUser,
inRateMaxT.inRateMax, 
inRateMaxT.inRateMaxUser, 
whRateMinT.whRateMin,
whRateMinT.whRateMinUser,
whRateMaxT.whRateMax,
whRateMaxT.whRateMaxUser,
averageDoseMinT.averageDoseMin,
averageDoseMinT.averageDoseMinUser,
averageDoseMaxT.averageDoseMax,
averageDoseMaxT.averageDoseMaxUser
from 
(
	select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMin,hosNumTemp.name as inRateMinUser
	from 
	( 
		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name
		from tbl_hospital h, tbl_userinfo u
		where h.isResAssessed='1' 
		and h.rsmRegion = u.region 
		and u.level='RSM' 
		group by u.region 
	) hosNumTemp, 
	(
		select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from (
			select IFNULL(count(1),0) as inNum, h.rsmRegion 
			from tbl_respirology_data rd, tbl_hospital h 
			where rd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(rd.createdate)
			and h.isResAssessed='1' 		
			group by h.rsmRegion 
		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region 
		where u.level='RSM'
	) inNumTemp
	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion 
	order by inNumTemp.inNum/hosNumTemp.hosNum
	limit 1	
 ) inRateMinT,
 (
	select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMax,hosNumTemp.name as inRateMaxUser
	from 
	( 
		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name
		from tbl_hospital h, tbl_userinfo u
		where h.isResAssessed='1' 
		and h.rsmRegion = u.region 
		and u.level='RSM' 
		group by u.region 
	) hosNumTemp, 
	(
		select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from (
			select IFNULL(count(1),0) as inNum, h.rsmRegion 
			from tbl_respirology_data rd, tbl_hospital h 
			where rd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(rd.createdate)
			and h.isResAssessed='1' 		
			group by h.rsmRegion 
		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region 
		where u.level='RSM'
	) inNumTemp
	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion 
	order by inNumTemp.inNum/hosNumTemp.hosNum desc
	limit 1	
 ) inRateMaxT ,
 (
	select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMin,pNumTemp.name as whRateMinUser
	from 
	( 
		select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (
			select IFNULL(sum(rd.pnum),0) as pNum, h.rsmRegion 
			from tbl_respirology_data rd, tbl_hospital h 
			where rd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(rd.createdate)
			and h.isResAssessed='1' 		
			group by h.rsmRegion 
		) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region 
		where u.level='RSM' 
	) pNumTemp, 
	(
		select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from (
			select IFNULL(sum(rd.lsnum),0) as lsNum, h.rsmRegion 
			from tbl_respirology_data rd, tbl_hospital h 
			where rd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(rd.createdate)
			and h.isResAssessed='1' 		
			group by h.rsmRegion 
		) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region 
		where u.level='RSM'
	) lsNumTemp
	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion 
	order by lsNumTemp.lsNum/pNumTemp.pNum
	limit 1	
 ) whRateMinT,
 (
	select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMax,pNumTemp.name as whRateMaxUser
	from 
	( 
		select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (
			select IFNULL(sum(rd.pnum),0) as pNum, h.rsmRegion 
			from tbl_respirology_data rd, tbl_hospital h 
			where rd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(rd.createdate)
			and h.isResAssessed='1' 		
			group by h.rsmRegion 
		) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region 
		where u.level='RSM' 
	) pNumTemp, 
	(
		select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from (
			select IFNULL(sum(rd.lsnum),0) as lsNum, h.rsmRegion 
			from tbl_respirology_data rd, tbl_hospital h 
			where rd.hospitalName = h.name  
			and TO_DAYS(?) = TO_DAYS(rd.createdate)
			and h.isResAssessed='1' 		
			group by h.rsmRegion 
		) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region 
		where u.level='RSM'
	) lsNumTemp
	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion 
	order by lsNumTemp.lsNum/pNumTemp.pNum desc
	limit 1	
 ) whRateMaxT,
(
	select IFNULL(av1.averageDose,0) as averageDoseMin, u.name as averageDoseMinUser from 
	(
		select IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) as averageDose, h.rsmRegion
		from tbl_respirology_data rd, tbl_hospital h
		where rd.hospitalName = h.name 
		and TO_DAYS(?) = TO_DAYS(rd.createdate) 
		and h.isResAssessed='1' 
		group by h.rsmRegion 
	) av1 right join tbl_userinfo u on av1.rsmRegion = u.region
	where u.level='RSM' 
	order by av1.averageDose
	limit 1	
) averageDoseMinT,
(
	select IFNULL(av2.averageDose,0) as averageDoseMax, u.name as averageDoseMaxUser  from 
	(
		select IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) as averageDose, h.rsmRegion
		from tbl_respirology_data rd, tbl_hospital h
		where rd.hospitalName = h.name 
		and TO_DAYS(?) = TO_DAYS(rd.createdate) 
		and h.isResAssessed='1' 
		group by h.rsmRegion 
	) av2 right join tbl_userinfo u on av2.rsmRegion = u.region
	where u.level='RSM' 
	order by av2.averageDose desc
	limit 1	
 ) averageDoseMaxT
 
----------------------------------------------------------------------------------------
insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2013-12-25', Interval 6 day),'%m.%d'), '-',DATE_FORMAT('2013-12-25','%m.%d')) as duration, 
h.name,
h.code,
pd_data.inNum,
pd_data.pnum,
pd_data.whnum,
pd_data.lsnum,
pd_data.averageDose,
pd_data.hmgRate,
pd_data.omgRate,
pd_data.tmgRate,
pd_data.fmgRate,
h.saleCode,
h.dsmCode,
h.rsmRegion,
h.region,
now() 
from (
SELECT 
	h.code,	
	count_hos.inNum,
	(sum(pd.pnum)/count_hos.inNum)*5 as pnum,
	(sum(pd.whnum)/count_hos.inNum)*5 as whnum,
	(sum(pd.lsnum)/count_hos.inNum)*5 as lsnum,
	IFNULL( 
				sum( 
					( 
						( 0.5*IFNULL(pd.hqd,0) 
						+ 0.5*2*IFNULL(pd.hbid,0) 
						+ 1*1*IFNULL(pd.oqd,0) 
						+ 1*2*IFNULL(pd.obid,0) 
						+ 2*1*IFNULL(pd.tqd,0) 
						+ 2*2*IFNULL(pd.tbid,0) 
						) / 100 
					) * IFNULL(pd.lsnum,0) 
				) / IFNULL(sum(pd.lsnum),0)
			,0 ) averageDose, 
	IFNULL(
		sum(IFNULL(pd.hqd,0)*pd.lsnum/100)/sum(pd.lsnum),0
	) hmgRate,
	IFNULL(
		sum((IFNULL(pd.hbid,0)*pd.lsnum + IFNULL(pd.oqd,0)*pd.lsnum)/100)/sum(pd.lsnum),0
	) omgRate,
	IFNULL(
		sum((IFNULL(pd.obid,0)*pd.lsnum + IFNULL(pd.tqd,0)*pd.lsnum)/100)/sum(pd.lsnum),0
	) tmgRate,
	IFNULL(
		sum(IFNULL(pd.tbid,0)*pd.lsnum/100)/sum(pd.lsnum),0
	) fmgRate 
    FROM tbl_pediatrics_data pd, tbl_hospital h, 
	(
		select count(1) as inNum, h.code 
		from tbl_pediatrics_data pd, tbl_hospital h
		WHERE pd.createdate between DATE_SUB('2013-12-25', Interval 6 day) and '2013-12-25' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2013-12-25', Interval 6 day) and '2013-12-25' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

--------------------------------------------------------------

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2013-10-09', Interval 6 day),'%m.%d'), '-',DATE_FORMAT('2013-10-09','%m.%d')) as duration, 
h.name,
h.code,
rd_data.inNum,
rd_data.pnum,
rd_data.whnum,
rd_data.aenum,
rd_data.lsnum,
rd_data.averageDose,
rd_data.omgRate,
rd_data.tmgRate,
rd_data.thmgRate,
rd_data.fmgRate,
rd_data.smgRate,
rd_data.emgRate,
h.saleCode,
h.dsmCode,
h.rsmRegion,
h.region,
now() 
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(rd.pnum)/count_hos.inNum)*5 as pnum,
    (sum(rd.aenum)/count_hos.inNum)*5 as aenum,
    (sum(rd.whnum)/count_hos.inNum)*5 as whnum,
    (sum(rd.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(rd.oqd,0) 
                        + 2*1*IFNULL(rd.tqd,0) 
                        + 1*3*IFNULL(rd.otid,0) 
                        + 2*2*IFNULL(rd.tbid,0) 
                        + 2*3*IFNULL(rd.ttid,0) 
                        + 3*2*IFNULL(rd.thbid,0) 
                        + 4*2*IFNULL(rd.fbid,0) 
                        ) / 100  
                    ) * IFNULL(rd.lsnum,0) 
                ) / IFNULL(sum(rd.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(rd.oqd,0)*rd.lsnum/100)/sum(rd.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(rd.tqd,0)*rd.lsnum/100)/sum(rd.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(rd.otid,0)*rd.lsnum/100)/sum(rd.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(rd.tbid,0)*rd.lsnum/100)/sum(rd.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(rd.ttid,0)*rd.lsnum + IFNULL(rd.thbid,0)*rd.lsnum)/100)/sum(rd.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(rd.fbid,0)*rd.lsnum/100)/sum(rd.lsnum),0
    ) emgRate 
    FROM tbl_respirology_data rd, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_respirology_data rd, tbl_hospital h
        WHERE rd.createdate between DATE_SUB('2013-10-09', Interval 6 day) and '2013-10-09' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2013-10-09', Interval 6 day) and '2013-10-09' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';
  
------------------------------------------------------------周报RSD--------------------------------------------------
select 
u.name,
u.userCode,
u.regionCenter,
u.region,u.level, 
count(1) as hosNum,
(
    select sum(inNum) from (
        select least(count(1),3) as inNum, h1.region, pd.hospitalName 
        from tbl_pediatrics_data pd, tbl_hospital h1 
        where pd.hospitalName = h1.name 
        and pd.createdate between DATE_SUB('2014-01-01', Interval 6 day) and '2014-01-01' 
        and h1.isPedAssessed='1' group by pd.hospitalName 
        ) inNumTemp 
        where inNumTemp.region = u.regionCenter 
) inNum,
IFNULL(sum(pdw.pnum),0) as pnum, 
IFNULL(sum(pdw.whnum),0) whnum, 
IFNULL(sum(pdw.lsnum),0) as lsnum, 
IFNULL(sum(pdw.averageDose*pdw.lsnum)/sum(pdw.lsnum),0) as averageDose,      
IFNULL(sum(pdw.hmgRate*pdw.lsnum)/sum(pdw.lsnum),0) as hmgRate, 
IFNULL(sum(pdw.omgRate*pdw.lsnum)/sum(pdw.lsnum),0) as omgRate, 
IFNULL(sum(pdw.tmgRate*pdw.lsnum)/sum(pdw.lsnum),0) as tmgRate, 
IFNULL(sum(pdw.fmgRate*pdw.lsnum)/sum(pdw.lsnum),0) as fmgRate 
from tbl_pediatrics_data_weekly pdw, tbl_userinfo u 
where pdw.region = u.regionCenter 
and pdw.duration = CONCAT(DATE_FORMAT(DATE_SUB('2014-01-01', Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT('2014-01-01','%Y.%m.%d')) 
and u.level='RSD'
group by pdw.region;
------------------------------------------------------------------------------------------------------------------
select 
u.name,
u.userCode,
u.regionCenter,
u.region,u.level, 
count(1) as hosNum,
(
    select sum(inNum) from (
        select least(count(1),3) as inNum, h1.region, rd.hospitalName 
        from tbl_respirology_data rd, tbl_hospital h1 
        where rd.hospitalName = h1.name 
        and rd.createdate between DATE_SUB('2014-01-01', Interval 6 day) and '2014-01-01' 
        and h1.isResAssessed='1' group by rd.hospitalName 
        ) inNumTemp 
        where inNumTemp.region = u.regionCenter 
) inNum,
IFNULL(sum(rdw.pnum),0) as pnum, 
IFNULL(sum(rdw.whnum),0) whnum, 
IFNULL(sum(rdw.lsnum),0) as lsnum, 
IFNULL(sum(rdw.averageDose*rdw.lsnum)/sum(rdw.lsnum),0) as averageDose,      
IFNULL(sum(rdw.omgRate*rdw.lsnum)/sum(rdw.lsnum),0) as omgRate, 
IFNULL(sum(rdw.tmgRate*rdw.lsnum)/sum(rdw.lsnum),0) as tmgRate, 
IFNULL(sum(rdw.thmgRate*rdw.lsnum)/sum(rdw.lsnum),0) as thmgRate, 
IFNULL(sum(rdw.fmgRate*rdw.lsnum)/sum(rdw.lsnum),0) as fmgRate, 
IFNULL(sum(rdw.smgRate*rdw.lsnum)/sum(rdw.lsnum),0) as smgRate, 
IFNULL(sum(rdw.emgRate*rdw.lsnum)/sum(rdw.lsnum),0) as emgRate 
from tbl_respirology_data_weekly rdw, tbl_userinfo u 
where rdw.region = u.regionCenter 
and rdw.duration = CONCAT(DATE_FORMAT(DATE_SUB('2014-01-01', Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT('2014-01-01','%Y.%m.%d')) 
and u.level='RSD'
group by rdw.region;
----------------------------------
select least(count(1),3) as inNum, h1.region, rd.hospitalName, h1.isResAssessed, rd.createdate 
from tbl_respirology_data rd, tbl_hospital h1 
where rd.hospitalName = h1.name 
and rd.createdate between DATE_SUB('2014-01-01', Interval 6 day) and '2014-01-01' 
and h1.isResAssessed='1' 
and h1.region = 'South GRA'
group by rd.hospitalName 
order by rd.createdate;
-----------------------------------------------周报RSM----------------------------------------------------------------
select 
pdw.rsmRegion, 
pdw.region, 
count(1) as hosNum, 
(
    select sum(inNum) from (
        select least(count(1),3) as inNum, h1.rsmRegion, pd.hospitalName 
        from tbl_pediatrics_data pd, tbl_hospital h1 
        where pd.hospitalName = h1.name 
        and pd.createdate between DATE_SUB(?, Interval 6 day) and ? 
        and h1.isPedAssessed='1' group by pd.hospitalName 
        ) inNumTemp 
        where inNumTemp.rsmRegion = pdw.rsmRegion 
) inNum,
IFNULL(sum(pdw.pnum),0) as pnum, 
IFNULL(sum(pdw.whnum),0) whnum, 
IFNULL(sum(pdw.lsnum),0) as lsnum, 
IFNULL(sum(pdw.averageDose*pdw.lsnum)/sum(pdw.lsnum),0) as averageDose,      
IFNULL(sum(pdw.hmgRate*pdw.lsnum)/sum(pdw.lsnum),0) as hmgRate, 
IFNULL(sum(pdw.omgRate*pdw.lsnum)/sum(pdw.lsnum),0) as omgRate, 
IFNULL(sum(pdw.tmgRate*pdw.lsnum)/sum(pdw.lsnum),0) as tmgRate, 
IFNULL(sum(pdw.fmgRate*pdw.lsnum)/sum(pdw.lsnum),0) as fmgRate 
from tbl_pediatrics_data_weekly pdw 
where pdw.region = ( select regionCenter from tbl_userinfo where telephone=?)
and pdw.duration = CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%m.%d'), '-',DATE_FORMAT(?,'%m.%d')) 
group by pdw.rsmRegion;
----------------------------------------
--上报率
select sum(inNum) from (
	select least(count(1),3) as inNum, h1.rsmRegion 
	from tbl_pediatrics_data pd, tbl_hospital h1 
	where pd.hospitalName = h1.name 
	and TO_DAYS(?) = TO_DAYS(pd.createdate)
	and h1.isPedAssessed='1' 
) inNumTemp 
where inNumTemp.region = u.region 


--------------------------------------PED 上周数据环比-------------------------
select lastweekdata.dsmCode,
lastweekdata.rsmRegion,
lastweekdata.pnum,
ROUND((lastweekdata.pnum - last2weekdata.pnum) / last2weekdata.pnum,2) as pnumRatio,
lastweekdata.lsnum,
ROUND((lastweekdata.lsnum - last2weekdata.lsnum) / last2weekdata.lsnum,2) as lsnumRatio,
lastweekdata.inRate,
ROUND((lastweekdata.inRate - last2weekdata.inRate) / last2weekdata.inRate,2) as inRateRatio,
lastweekdata.whRate,
ROUND((lastweekdata.whRate - last2weekdata.whRate) / last2weekdata.whRate,2) as whRateRatio,
lastweekdata.averageDose,
ROUND((lastweekdata.averageDose - last2weekdata.averageDose ) / last2weekdata.averageDose,2) as averageDoseRatio,
lastweekdata.hmgRate as hmgRate,
ROUND((lastweekdata.hmgRate - last2weekdata.hmgRate ) / last2weekdata.hmgRate,2) as hmgRateRatio,
lastweekdata.omgRate as omgRate,
ROUND((lastweekdata.omgRate - last2weekdata.omgRate ) / last2weekdata.omgRate,2) as omgRateRatio,
lastweekdata.tmgRate as tmgRate,
ROUND((lastweekdata.tmgRate - last2weekdata.tmgRate ) / last2weekdata.tmgRate,2) as tmgRateRatio, 
lastweekdata.fmgRate as fmgRate,
ROUND((lastweekdata.fmgRate - last2weekdata.fmgRate ) / last2weekdata.fmgRate,2) as fmgRateRatio 
from 
(
    select dsmCode, rsmRegion, 
    IFNULL(sum(lastweek.pnum),0) as pnum,  
    IFNULL(sum(lastweek.lsnum),0) as lsnum,
    IFNULL(sum(lastweek.lsnum),0) / IFNULL(sum(lastweek.pnum),0) as whRate, 
    IFNULL(sum(least(lastweek.innum,3)),0) / (count(1)*3) as inRate, 
    IFNULL(sum(lastweek.averageDose*lastweek.lsnum)/sum(lastweek.lsnum),0) as averageDose, 
    IFNULL(sum(lastweek.hmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as hmgRate, 
    IFNULL(sum(lastweek.omgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as omgRate, 
    IFNULL(sum(lastweek.tmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as tmgRate, 
    IFNULL(sum(lastweek.fmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as fmgRate 
    from 
    (
        select *
        from tbl_pediatrics_data_weekly 
        order by duration desc  
        limit 0,920 
    ) lastweek 
    group by rsmRegion,dsmCode 
) lastweekdata, 
(
    select dsmCode, rsmRegion, 
    IFNULL(sum(last2week.pnum),0) as pnum,  
    IFNULL(sum(last2week.lsnum),0) as lsnum, 
    IFNULL(sum(last2week.lsnum),0) / IFNULL(sum(last2week.pnum),0) as whRate, 
    IFNULL(sum(least(last2week.innum,3)),0) / (count(1)*3) as inRate, 
    IFNULL(sum(last2week.averageDose*last2week.lsnum)/sum(last2week.lsnum),0) as averageDose, 
    IFNULL(sum(last2week.hmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as hmgRate, 
    IFNULL(sum(last2week.omgRate*last2week.lsnum)/sum(last2week.lsnum),0) as omgRate, 
    IFNULL(sum(last2week.tmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as tmgRate, 
    IFNULL(sum(last2week.fmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as fmgRate 
    from 
    (
        select *
        from tbl_pediatrics_data_weekly 
        order by duration desc  
        limit 920,920 
    ) last2week 
    group by rsmRegion,dsmCode 
) last2weekdata 
where lastweekdata.dsmCode = last2weekdata.dsmCode
and lastweekdata.rsmRegion = last2weekdata.rsmRegion
and lastweekdata.rsmRegion = (select region from tbl_userinfo where telephone='13958896176')


---------------------------------------
 --   pedEmernum      int,
 --   pedroomnum      int,
 --   resnum          int,
 --   other           int,
 --   operatorName    varchar(20),
 --   operatorCode    varchar(20),
 --   hospitalCode    varchar(20),
 --   dsmCode         varchar(20),
 --   rsmRegion       varchar(20),
 --   region          varchar(20),
select u.name, u.userCode 
, u.region 
, monthlyData.pedEmernum 
, monthlyData.pedroomnum 
, monthlyData.resnum 
, monthlyData.othernum 
, monthlyData.totalnum 
from (
	select dsmCode 
	,rsmRegion 
	,sum(pedEmernum) as pedEmernum
	, sum(pedroomnum) as pedroomnum
	, sum(resnum) as resnum
	, sum(other) as othernum
	, sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum 
	from tbl_month_data
	where (YEAR(createdate)+MONTH(createdate)) = YEAR(DATE_SUB(now(), INTERVAL 1 MONTH))+MONTH(DATE_SUB(now(), INTERVAL 1 MONTH))
	and rsmRegion=(select region from tbl_userinfo where telephone='13888757118')
	group by dsmCode 
) monthlyData 
right join tbl_userinfo u on u.userCode = monthlyData.dsmCode 
where u.region = (select region from tbl_userinfo where telephone='13888757118') 
and u.level='DSM'

select IFNULL(sum(pedEmernum),0) as pedEmernum
, IFNULL(sum(pedroomnum),0) as pedroomnum
, IFNULL(sum(resnum),0) as resnum
, IFNULL(sum(other),0) as othernum 
from tbl_month_data
where (YEAR(createdate)+MONTH(createdate)) = YEAR(DATE_SUB(now(), INTERVAL 1 MONTH))+MONTH(DATE_SUB(now(), INTERVAL 1 MONTH))
and dsmCode = (select userCode from tbl_userinfo where telephone='15887174075')


select sum(pedEmernum) as pedEmernum
, sum(pedroomnum) as pedroomnum
, sum(resnum) as resnum
, sum(other) as othernum
, sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum 
from tbl_month_data
where (YEAR(createdate)+MONTH(createdate)) = YEAR(DATE_SUB(now(), INTERVAL 1 MONTH))+MONTH(DATE_SUB(now(), INTERVAL 1 MONTH))
and region=(select regionCenter from tbl_userinfo where telephone=?)
group by rsmRegion

select sum(pedEmernum) as pedEmernum
, sum(pedroomnum) as pedroomnum
, sum(resnum) as resnum
, sum(other) as othernum
, sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum  
from tbl_month_data
where (YEAR(createdate)+MONTH(createdate)) = YEAR(DATE_SUB(now(), INTERVAL 1 MONTH))+MONTH(DATE_SUB(now(), INTERVAL 1 MONTH))
group by region

-----------------------------------------------------------
--医院折线图

--TJTJ007H(RES)   YNKM019H(PED)  YNKM001H(Both)

select pd.duration
, IFNULL(pd.lsnum/pd.pnum,0) as pedWhRate 
, IFNULL(pd.averageDose,0) as pedAverageDose 
, IFNULL(pd.pnum,0) as pedPnum 
, IFNULL(pd.lsnum,0) as pedLsnum 
, IFNULL(rd.lsnum/rd.pnum,0) as resWhRate 
, IFNULL(rd.averageDose,0) as resAverageDose 
, IFNULL(rd.pnum,0) as resPnum 
, IFNULL(rd.lsnum,0) as resLsnum 
from (
    select * from tbl_pediatrics_data_weekly 
    order by duration desc 
    limit 0,11040 
) pd
left join (
    select * from tbl_respirology_data_weekly 
    order by duration desc 
    limit 0,4776 
) rd
on pd.hospitalCode = rd.hospitalCode and pd.duration = rd.duration
where pd.hospitalCode='YNKM019H'
union 
select rd.duration
, IFNULL(pd.lsnum/pd.pnum,0) as pedWhRate 
, IFNULL(pd.averageDose,0) as pedAverageDose 
, IFNULL(pd.pnum,0) as pedPnum 
, IFNULL(pd.lsnum,0) as pedLsnum 
, IFNULL(rd.lsnum/rd.pnum,0) as resWhRate 
, IFNULL(rd.averageDose,0) as resAverageDose 
, IFNULL(rd.pnum,0) as resPnum 
, IFNULL(rd.lsnum,0) as resLsnum 
from (
    select * from tbl_pediatrics_data_weekly 
    order by duration desc 
    limit 0,11040 
) pd
right join (
    select * from tbl_respirology_data_weekly 
    order by duration desc 
    limit 0,4776 
) rd
on pd.hospitalCode = rd.hospitalCode and pd.duration = rd.duration
where rd.hospitalCode='YNKM019H' 
order by duration desc;

------------------------------------------------------------------------------
select * 
from tbl_hos_user where hosCode in (
select hu.hosCode 
from tbl_hospital h, tbl_hos_user hu, tbl_userinfo u
where hu.hosCode = h.code 
and hu.userCode = u.userCode 
and h.isResAssessed='1'
group by hu.hosCode 
having count(1) > 1
)

--getSalesSelfReportProcessRESData
--| 温州市医学院附属第二医院                                | ZJWZ016H | 7022917  | 刘叶           | 刘叶           | 18606640630 |
--| 温州市医学院附属第二医院                                | ZJWZ016H | 7022917  | 刘叶           | 黄象甜         | 13057947254 |

--GDGZ141H
-- 84 | 赵娟   | 13802411683 | NULL     | 7011851  | NULL | South GRA    | GD RE  
-- 86 | 张丽霞 | 13570914930 | NULL     | 7021137  | NULL | South GRA    | GD RE   
select count(1) as hosNum, 
( select IFNULL(sum(inNum),0) as validInNum from (
    	select least(count(1),3) as inNum, h1.code as hosCode, h1.dsmCode, h1.rsmRegion 
    	from tbl_respirology_data rd, tbl_hospital h1 
        where rd.hospitalName = h1.name 
        and rd.createdate between '2014-01-16' and DATE_ADD('2014-01-16', Interval 6 day) 
        and h1.isResAssessed='1' 
        group by rd.hospitalName 
    ) inNumTemp, tbl_hos_user hu 
    where inNumTemp.rsmRegion = u.region 
    and inNumTemp.hosCode = hu.hosCode 
    and hu.userCode = u.userCode 
    and inNumTemp.dsmCode = u.superior 
) as validInNum 
from tbl_userinfo u, tbl_hospital h, tbl_hos_user hu  
where h.dsmCode = u.superior 
and h.rsmRegion = u.region 
and h.code = hu.hosCode 
and hu.userCode = u.userCode 
and h.isResAssessed='1' 
and telephone = '13570914930' ;

select count(1) as hosNum, 
( select IFNULL(sum(inNum),0) as validInNum from (
    select least(count(1),3) as inNum, h1.code as hosCode, h1.dsmCode, h1.rsmRegion 
    from tbl_pediatrics_data pd, tbl_hospital h1 
        where pd.hospitalName = h1.name 
        and pd.createdate between '2014-01-16' and DATE_ADD('2014-01-16', Interval 6 day) 
        and h1.isPedAssessed='1' 
        group by pd.hospitalName 
    ) inNumTemp, tbl_hos_user hu 
    where inNumTemp.rsmRegion = u.region 
    and inNumTemp.hosCode = hu.userCode 
    and hu.userCode = u.userCode 
    and inNumTemp.dsmCode = u.superior 
) as validInNum 
from tbl_userinfo u, tbl_hospital h, tbl_hos_user hu  
where h.dsmCode = u.superior 
and h.rsmRegion = u.region 
and h.code = hu.hosCode 
and hu.userCode = u.userCode 
and h.isPedAssessed='1' 
and telephone = '13802411683' ;

-----getDSMSelfReportProcessPEDData--
select count(1) as hosNum,
( select IFNULL(sum(inNum),0) as validInNum from ( 
               select least(count(1),3) as inNum, h1.dsmCode, h1.rsmRegion 
               from tbl_pediatrics_data pd, tbl_hospital h1 
               where pd.hospitalName = h1.name 
               and pd.createdate between '2014-01-24' and DATE_ADD('2014-01-24', Interval 6 day) 
               and h1.isPedAssessed='1' 
               group by pd.hospitalName 
           ) inNumTemp 
           where inNumTemp.rsmRegion = u.region 
           and inNumTemp.dsmCode = u.userCode 
) as validInNum 
from tbl_userinfo u, tbl_hospital h 
where h.dsmCode = u.userCode 
and h.rsmRegion = u.region 
and h.isPedAssessed='1' 
and telephone = '18630180929' ;
------------getDSMSelfReportProcessPEDDetailData-------------
select h.name as hospitalName,
( select IFNULL( 
               ( select count(1) 
               from tbl_pediatrics_data pd 
               where pd.hospitalName = h.name 
               and pd.createdate between '2014-01-24' and DATE_ADD('2014-01-24', Interval 6 day) 
               group by pd.hospitalName 
           ),0) ) as inNum, 
        ( select ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion  and ui.level='REP') as salesName, 
         h.isPedAssessed as isAssessed 
        from tbl_userinfo u, tbl_hospital h 
        where h.dsmCode = u.userCode 
        and h.rsmRegion = u.region 
        and telephone = '18630180929' ;
        
 select ui.name 
 from tbl_userinfo ui 
 where ui.userCode = '8206153'
 and ui.superior = '7003977'
 and ui.region = 'HeB GRA'
 and u.level='REP';
 
  ---------getRSD12MontlyDataByTel---------
 select DATE_FORMAT(md.createdate,'%Y-%m') as dataMonth 
  , ( select count(1) from tbl_hospital h where h.region = u.regionCenter and h.isMonthlyAssessed='1' ) as hosNum
 , count(1) as innum 
 , sum(md.pedEmernum) as pedEmernum
 , sum(md.pedroomnum) as pedroomnum
 , sum(md.resnum) as resnum
 , sum(md.other) as other
 , sum( md.pedEmernum + md.pedroomnum + md.resnum + md.other ) as totalnum
 from tbl_userinfo u, tbl_month_data md, tbl_hospital h
 where u.regionCenter = md.region 
 and md.hospitalCode = h.code 
 and h.isMonthlyAssessed='1' 
 and u.telephone='13801370734' 
group by dataMonth 
order by dataMonth desc
limit 0,12 

-----------------------------------------------

-----------------------------getRSM12MontlyDataByTel --------------------------
 select DATE_FORMAT(md.createdate,'%Y-%m') as dataMonth 
  , ( select count(1) from tbl_hospital h where h.region = u.regionCenter and h.isMonthlyAssessed='1' ) as hosNum
 , count(1) as innum 
 , sum(md.pedEmernum) as pedEmernum
 , sum(md.pedroomnum) as pedroomnum
 , sum(md.resnum) as resnum
 , sum(md.other) as other
 , sum( md.pedEmernum + md.pedroomnum + md.resnum + md.other ) as totalnum
 from tbl_userinfo u, tbl_month_data md, tbl_hospital h
 where u.regionCenter = md.region 
 and md.hospitalCode = h.code 
 and h.isMonthlyAssessed='1' 
 and u.telephone='13801370734' 
group by dataMonth 
order by dataMonth desc
limit 0,12 
------------------------------------------------------------
 
 ----------getMonthlyDataOfRSD--------------
 select IFNULL(lastMonthData.pedEmernum,0) as pedEmernum 
, IFNULL(lastMonthData.pedroomnum,0) as pedroomnum 
, IFNULL(lastMonthData.resnum,0) as resnum 
, IFNULL(lastMonthData.othernum,0) as othernum 
, ROUND(IFNULL(lastMonthData.pedEmernum/lastMonthData.totalnum,0),2) as pedemernumrate 
, ROUND(IFNULL(lastMonthData.pedroomnum/lastMonthData.totalnum,0),2) as pedroomnumrate 
, ROUND(IFNULL(lastMonthData.resnum/lastMonthData.totalnum,0),2) as resnumrate 
, ROUND(IFNULL(lastMonthData.othernum/lastMonthData.totalnum,0),2) as othernumrate 
, ROUND(IFNULL((lastMonthData.pedEmernum-last2MonthData.pedEmernum)/last2MonthData.pedEmernum,0),2) as pedemernumratio 
, ROUND(IFNULL((lastMonthData.pedroomnum-last2MonthData.pedroomnum)/last2MonthData.pedroomnum,0),2) as pedroomnumratio 
, ROUND(IFNULL((lastMonthData.resnum-last2MonthData.resnum)/last2MonthData.resnum,0),2) as resnumratio 
, ROUND(IFNULL((lastMonthData.othernum-last2MonthData.othernum)/last2MonthData.othernum,0),2) as othernumratio 
, ROUND(IFNULL(lastMonthData.pedEmernum/lastMonthData.totalnum - last2MonthData.pedEmernum/last2MonthData.totalnum,0),2) as pedemernumrateratio 
, ROUND(IFNULL(lastMonthData.pedroomnum/lastMonthData.totalnum - last2MonthData.pedroomnum/last2MonthData.totalnum,0),2) as pedroomnumrateratio 
, ROUND(IFNULL(lastMonthData.resnum/lastMonthData.totalnum - last2MonthData.resnum/last2MonthData.totalnum,0),2) as resnumrateratio 
, ROUND(IFNULL(lastMonthData.othernum/lastMonthData.totalnum - last2MonthData.othernum/last2MonthData.totalnum,0),2) as othernumrateratio 
, IFNULL(lastMonthData.totalnum,0) as totalnum 
, ROUND(IFNULL((lastMonthData.totalnum - last2MonthData.totalnum)/last2MonthData.totalnum,0),2) as totalnumratio 
, IFNULL(lastMonthData.innum,0) as innum 
, ROUND(IFNULL((lastMonthData.innum-last2MonthData.innum)/last2MonthData.innum,0),2) as innumratio 
, IFNULL(lastMonthData.hosnum,0) as hosnum 
, ROUND(IFNULL((lastMonthData.hosnum-last2MonthData.hosnum)/last2MonthData.hosnum,0),2) as hosnumratio 
, ROUND(IFNULL(lastMonthData.innum,0)/IFNULL(lastMonthData.hosnum,0),2) as inrate 
, ROUND(IFNULL(lastMonthData.innum/lastMonthData.hosnum - last2MonthData.innum/last2MonthData.hosnum,0),2) as inrateratio 
, '' as saleName, '' as dsmName, '' as rsmRegion, lastMonthData.region 
from (
    select lastMonth.pedEmernum ,lastMonth.pedroomnum ,lastMonth.resnum ,lastMonth.othernum ,lastMonth.totalnum 
    , lastMonth.innum as innum 
    , (select count(1) from tbl_hospital h where h.region = u.regionCenter and h.isMonthlyAssessed='1') as hosnum 
    , u.region as rsmRegion , u.regionCenter as region 
        from (
           select h.rsmRegion 
           , h.region 
           , sum(pedEmernum) as pedEmernum 
           , sum(pedroomnum) as pedroomnum 
           , sum(resnum) as resnum 
           , sum(other) as othernum 
           , sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum 
           , count(1) as innum 
           from tbl_month_data md, tbl_hospital h 
            where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 MONTH),'%Y-%m') 
           and md.hospitalCode = h.code and h.isMonthlyAssessed='1' 
           group by h.region 
       ) lastMonth 
       right join tbl_userinfo u on u.regionCenter = lastMonth.region 
       where u.level='RSD' 
) lastMonthData 
inner join ( 
    select last2Month.pedEmernum ,last2Month.pedroomnum ,last2Month.resnum ,last2Month.othernum ,last2Month.totalnum 
    , last2Month.innum as innum 
    , (select count(1) from tbl_hospital h where h.region = u.regionCenter and h.isMonthlyAssessed='1') as hosnum 
    , u.region as rsmRegion , u.regionCenter as region 
        from (
           select h.rsmRegion 
           , h.region 
           , sum(pedEmernum) as pedEmernum 
           , sum(pedroomnum) as pedroomnum 
           , sum(resnum) as resnum 
           , sum(other) as othernum 
           , sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum 
           , count(1) as innum 
           from tbl_month_data md, tbl_hospital h 
            where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m') 
           and md.hospitalCode = h.code and h.isMonthlyAssessed='1' 
           group by h.region 
       ) last2Month 
       right join tbl_userinfo u on u.regionCenter = last2Month.region 
       where u.level='RSD' 
) last2MonthData on lastMonthData.region = last2MonthData.region;

--------getMonthlyDataOfRSMByRegion---------
select IFNULL(lastMonthData.pedEmernum,0) as pedEmernum 
	, IFNULL(lastMonthData.pedroomnum,0) as pedroomnum 
	, IFNULL(lastMonthData.resnum,0) as resnum 
	, IFNULL(lastMonthData.othernum,0) as othernum 
	, ROUND(IFNULL(lastMonthData.pedEmernum/lastMonthData.totalnum,0),2) as pedemernumrate 
	, ROUND(IFNULL(lastMonthData.pedroomnum/lastMonthData.totalnum,0),2) as pedroomnumrate 
	, ROUND(IFNULL(lastMonthData.resnum/lastMonthData.totalnum,0),2) as resnumrate 
	, ROUND(IFNULL(lastMonthData.othernum/lastMonthData.totalnum,0),2) as othernumrate 
	, ROUND(IFNULL((lastMonthData.pedEmernum-last2MonthData.pedEmernum)/last2MonthData.pedEmernum,0),2) as pedemernumratio 
	, ROUND(IFNULL((lastMonthData.pedroomnum-last2MonthData.pedroomnum)/last2MonthData.pedroomnum,0),2) as pedroomnumratio 
	, ROUND(IFNULL((lastMonthData.resnum-last2MonthData.resnum)/last2MonthData.resnum,0),2) as resnumratio 
	, ROUND(IFNULL((lastMonthData.othernum-last2MonthData.othernum)/last2MonthData.othernum,0),2) as othernumratio 
	, ROUND(IFNULL(lastMonthData.pedEmernum/lastMonthData.totalnum - last2MonthData.pedEmernum/last2MonthData.totalnum,0),2) as pedemernumrateratio 
	, ROUND(IFNULL(lastMonthData.pedroomnum/lastMonthData.totalnum - last2MonthData.pedroomnum/last2MonthData.totalnum,0),2) as pedroomnumrateratio 
	, ROUND(IFNULL(lastMonthData.resnum/lastMonthData.totalnum - last2MonthData.resnum/last2MonthData.totalnum,0),2) as resnumrateratio 
	, ROUND(IFNULL(lastMonthData.othernum/lastMonthData.totalnum - last2MonthData.othernum/last2MonthData.totalnum,0),2) as othernumrateratio
	, '' as saleName, '' as dsmName, lastMonthData.rsmRegion, lastMonthData.region 
	, lastMonthData.innum as innum, lastMonthData.hosnum as hosnum 
	from (
		select lastMonth.pedEmernum ,lastMonth.pedroomnum ,lastMonth.resnum ,lastMonth.othernum ,lastMonth.totalnum 
				, lastMonth.innum as innum, lastMonth.hosnum as hosnum, u.region as rsmRegion , u.regionCenter as region 
		from (
			select rsmRegion 
                   , region 
                   , sum(pedEmernum) as pedEmernum 
                   , sum(pedroomnum) as pedroomnum 
                   , sum(resnum) as resnum 
                   , sum(other) as othernum 
                   , sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum 
                   , count(1) as innum 
			       , (select count(1) from tbl_hospital h where h.rsmRegion = md.rsmRegion and (h.isResAssessed='1' or h.isPedAssessed='1')) as hosnum 
			from tbl_month_data md 
			where (YEAR(createdate)+MONTH(createdate)) = YEAR(DATE_SUB(now(), INTERVAL 1 MONTH))+MONTH(DATE_SUB(now(), INTERVAL 1 MONTH)) 
			and region='West GRA' 
			group by rsmRegion 
		) lastMonth 
		right join tbl_userinfo u on u.region = lastMonth.rsmRegion 
		where u.regionCenter = 'West GRA' 
		and u.level='RSM'
	) lastMonthData 
	inner join ( 
		select last2Month.pedEmernum ,last2Month.pedroomnum ,last2Month.resnum 
		,last2Month.othernum ,last2Month.totalnum , last2Month.innum as innum, last2Month.hosnum as hosnum
		, u.region as rsmRegion , u.regionCenter as region 
		from (
			select rsmRegion 
                   , region 
                   , sum(pedEmernum) as pedEmernum 
                   , sum(pedroomnum) as pedroomnum 
                   , sum(resnum) as resnum 
                   , sum(other) as othernum 
                   , sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum 
                   , count(1) as innum 
			       , (select count(1) from tbl_hospital h where h.rsmRegion = md.rsmRegion and (h.isResAssessed='1' or h.isPedAssessed='1')) as hosnum 
			from tbl_month_data md 
			where (YEAR(createdate)+MONTH(createdate)) = YEAR(DATE_SUB(now(), INTERVAL 2 MONTH))+MONTH(DATE_SUB(now(), INTERVAL 2 MONTH)) 
			and region='West GRA' 
			group by rsmRegion 
		) last2Month 
		right join tbl_userinfo u on u.region = last2Month.rsmRegion 
		where u.regionCenter = 'West GRA' 
		and u.level='RSM'
	) last2MonthData on lastMonthData.rsmRegion = last2MonthData.rsmRegion;
	
-------------getMonthlyHospitalsByUserTel-----------
select h.id,h.code 
, case when h.isMonthlyAssessed='1' then concat('* ',h.name) else h.name end name
,h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.dsmCode 
from tbl_hospital h, tbl_userinfo ui, tbl_hos_user hu 
where ui.userCode = hu.userCode 
and hu.hosCode = h.code 
and ui.telephone = '18645358210' 
order by h.isMonthlyAssessed desc, h.name asc 

--------------getMonthlyDataOfCountory----------------


-----------------------------getWeeklyRESData4DSMMobile--------------------
 select lastweekdata.pnum 
 , ROUND((lastweekdata.pnum - last2weekdata.pnum) / last2weekdata.pnum,2) as pnumRatio 
 , lastweekdata.lsnum 
 , ROUND((lastweekdata.lsnum - last2weekdata.lsnum) / last2weekdata.lsnum,2) as lsnumRatio 
 , lastweekdata.inRate 
 , ROUND((lastweekdata.inRate - last2weekdata.inRate),2) as inRateRatio 
 , lastweekdata.whRate 
 , ROUND((lastweekdata.whRate - last2weekdata.whRate),2) as whRateRatio 
 , lastweekdata.averageDose 
 , ROUND((lastweekdata.averageDose - last2weekdata.averageDose ) / last2weekdata.averageDose,2) as averageDoseRatio 
 , lastweekdata.omgRate as omgRate 
 , ROUND((lastweekdata.omgRate - last2weekdata.omgRate ),2) as omgRateRatio 
 , lastweekdata.tmgRate as tmgRate 
 , ROUND((lastweekdata.tmgRate - last2weekdata.tmgRate ),2) as tmgRateRatio 
 , lastweekdata.thmgRate as thmgRate 
 , ROUND((lastweekdata.thmgRate - last2weekdata.thmgRate ),2) as thmgRateRatio 
 , lastweekdata.fmgRate as fmgRate 
 , ROUND((lastweekdata.fmgRate - last2weekdata.fmgRate ),2) as fmgRateRatio 
 , lastweekdata.smgRate as smgRate 
 , ROUND((lastweekdata.smgRate - last2weekdata.smgRate ),2) as smgRateRatio 
 , lastweekdata.emgRate as emgRate 
 , ROUND((lastweekdata.emgRate - last2weekdata.emgRate ),2) as emgRateRatio 
 , lastweekdata.dsmCode as userCode , lastweekdata.rsmRegion 
 , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.dsmCode and u.region = lastweekdata.rsmRegion and u.level='DSM' ),'vacant') as name 
   from (
    select dsmCode, rsmRegion,
        IFNULL(sum(lastweek.pnum),0) as pnum, 
            IFNULL(sum(lastweek.lsnum),0) as lsnum, 
            IFNULL(sum(lastweek.lsnum),0) / IFNULL(sum(lastweek.pnum),0) as whRate, 
            IFNULL(sum(least(lastweek.innum,3)),0) / (count(1)*3) as inRate, 
            IFNULL(sum(lastweek.averageDose*lastweek.lsnum)/sum(lastweek.lsnum),0) as averageDose, 
            IFNULL(sum(lastweek.omgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as omgRate, 
            IFNULL(sum(lastweek.tmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as tmgRate, 
            IFNULL(sum(lastweek.thmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as thmgRate, 
            IFNULL(sum(lastweek.fmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as fmgRate, 
            IFNULL(sum(lastweek.smgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as smgRate, 
            IFNULL(sum(lastweek.emgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as emgRate 
            from ( select * from tbl_respirology_data_weekly order by duration desc limit 0,398 ) lastweek
    group by rsmRegion,dsmCode
    ) lastweekdata,
    (
        select dsmCode, rsmRegion,
            IFNULL(sum(last2week.pnum),0) as pnum, 
            IFNULL(sum(last2week.lsnum),0) as lsnum, 
            IFNULL(sum(last2week.lsnum),0) / IFNULL(sum(last2week.pnum),0) as whRate, 
            IFNULL(sum(least(last2week.innum,3)),0) / (count(1)*3) as inRate, 
            IFNULL(sum(last2week.averageDose*last2week.lsnum)/sum(last2week.lsnum),0) as averageDose, 
            IFNULL(sum(last2week.omgRate*last2week.lsnum)/sum(last2week.lsnum),0) as omgRate, 
            IFNULL(sum(last2week.tmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as tmgRate, 
            IFNULL(sum(last2week.thmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as thmgRate, 
            IFNULL(sum(last2week.fmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as fmgRate, 
            IFNULL(sum(last2week.smgRate*last2week.lsnum)/sum(last2week.lsnum),0) as smgRate, 
            IFNULL(sum(last2week.emgRate*last2week.lsnum)/sum(last2week.lsnum),0) as emgRate 
            from ( select * from tbl_respirology_data_weekly order by duration desc limit 398,398 ) last2week 
                group by rsmRegion,dsmCode 
    ) last2weekdata 
    where lastweekdata.dsmCode = last2weekdata.dsmCode 
    and lastweekdata.rsmRegion = last2weekdata.rsmRegion 
    and lastweekdata.rsmRegion = (select region from tbl_userinfo where telephone='13957418198');

    
---------------getDailyPEDData4CountoryMobile-----------------------------------------------
select '全国' as name, 
null as userCode, 
( select count(1) from tbl_hospital h where h.isPedAssessed='1' ) hosNum,  
count(1) as inNum,  
IFNULL(sum(tmp.pnum),0) as pnum, 
IFNULL(sum(tmp.whnum),0) as whnum, 
IFNULL(sum(tmp.lsnum),0) as lsnum, 
IFNULL( sum( ( ( 0.5*IFNULL(tmp.hqd,0) + 0.5*2*IFNULL(tmp.hbid,0) + 1*1*IFNULL(tmp.oqd,0) + 1*2*IFNULL(tmp.obid,0) + 2*1*IFNULL(tmp.tqd,0) + 2*2*IFNULL(tmp.tbid,0) ) / 100 ) * IFNULL(tmp.lsnum,0) ) / IFNULL(sum(tmp.lsnum),0),0 ) as averageDose, 
IFNULL( sum( IFNULL(tmp.hqd,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as hmgRate, 
IFNULL( sum( IFNULL(tmp.hbid,0)/100*IFNULL(tmp.lsnum,0) + IFNULL(tmp.oqd,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as omgRate, 
IFNULL( sum( IFNULL(tmp.obid,0)/100*IFNULL(tmp.lsnum,0) + IFNULL(tmp.tqd,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as tmgRate, 
IFNULL( sum( IFNULL(tmp.tbid,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as fmgRate 
from ( 
    select pd.* from tbl_pediatrics_data pd, tbl_hospital h 
    where pd.hospitalName = h.name 
    and TO_DAYS(pd.createdate) = TO_DAYS('2014-06-10')     
    and h.isPedAssessed='1'
) tmp;

----------------getDailyPEDData4RSMByRegion----------------------------------------------
 select 
 ui.region as name, ui.userCode, 
 ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isPedAssessed='1' ) hosNum, 
 IFNULL(dailyData.inNum,0) as inNum, 
 IFNULL(dailyData.pnum,0) as pnum, 
 IFNULL(dailyData.whnum,0) as whnum, 
 IFNULL(dailyData.lsnum,0) as lsnum, 
 IFNULL(dailyData.averageDose,0) as averageDose, 
 IFNULL(dailyData.hmgRate,0) as hmgRate, 
 IFNULL(dailyData.omgRate,0) as omgRate, 
 IFNULL(dailyData.tmgRate,0) as tmgRate, 
 IFNULL(dailyData.fmgRate,0) as fmgRate 
 from ( 
    select u.region as name,u.userCode, 
     count(1) as inNum, 
     IFNULL(sum(pd.pnum),0) as pnum, 
     IFNULL(sum(pd.whnum),0) as whnum, 
     IFNULL(sum(pd.lsnum),0) as lsnum, 
     IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose,
     IFNULL( sum( IFNULL(pd.hqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as hmgRate,
     IFNULL( sum( IFNULL(pd.hbid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.oqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as omgRate,
     IFNULL( sum( IFNULL(pd.obid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.tqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as tmgRate,
     IFNULL( sum( IFNULL(pd.tbid,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as fmgRate 
     from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 
     where pd.hospitalName = h1.name 
     and pd.rsmRegion = u.region 
     and TO_DAYS('2014-04-18') = TO_DAYS(pd.createdate) 
     and h1.isPedAssessed='1' 
     and u.level='RSM' 
     and u.regionCenter = 'Central GRA' 
     group by u.userCode 
 ) dailyData right join tbl_userinfo ui on ui.userCode = dailyData.userCode 
 where ui.level='RSM' 
 and ui.regionCenter = 'Central GRA';
  
----------------------------getAllRSMDataByTelephone------------------------------------
  select u.name,u.userCode,
 ( select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.isPedAssessed='1' ) hosNum,
 count(1) as inNum, 
 IFNULL(sum(pd.pnum),0) as pnum, 
 IFNULL(sum(pd.whnum),0) as whnum, 
 IFNULL(sum(pd.lsnum),0) as lsnum, 
 IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose 
 from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 
 where pd.hospitalName = h1.name 
 and h1.rsmRegion = u.region 
 and TO_DAYS('2014-04-15') = TO_DAYS(pd.createdate) 
 and h1.isPedAssessed='1' 
 and u.level='RSM' 
 group by u.region;
 -------------------------------getDailyPEDData4DSMMobile-------------------------------------
select ui.name, ui.userCode,
 ( select count(1) from tbl_hospital h where h.dsmCode = ui.userCode and h.rsmRegion = ui.region and h.isPedAssessed='1' ) hosNum, 
 IFNULL(dailyData.inNum,0) as inNum,  
 IFNULL(dailyData.pnum,0) as pnum,  
 IFNULL(dailyData.whnum,0) as whnum,  
 IFNULL(dailyData.lsnum,0) as lsnum,  
 IFNULL(dailyData.averageDose,0) as averageDose,  
 IFNULL(dailyData.hmgRate,0) as hmgRate,  
 IFNULL(dailyData.omgRate,0) as omgRate,  
 IFNULL(dailyData.tmgRate,0) as tmgRate,  
 IFNULL(dailyData.fmgRate,0) as fmgRate 
 from ( 
 select u.name,u.userCode,
 count(1) as inNum, 
 IFNULL(sum(pd.pnum),0) as pnum, 
 IFNULL(sum(pd.whnum),0) as whnum, 
 IFNULL(sum(pd.lsnum),0) as lsnum, 
 IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose,
 IFNULL( sum( IFNULL(pd.hqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as hmgRate,
 IFNULL( sum( IFNULL(pd.hbid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.oqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as omgRate,
 IFNULL( sum( IFNULL(pd.obid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.tqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as tmgRate,
 IFNULL( sum( IFNULL(pd.tbid,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as fmgRate 
 from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 
 where pd.hospitalName = h1.name 
 and h1.rsmRegion = u.region 
 and h1.dsmCode = u.userCode 
 and TO_DAYS('2014-05-10') = TO_DAYS(pd.createdate) 
 and h1.isPedAssessed='1' 
 and u.level='DSM' 
 and u.region = ( select region from tbl_userinfo where telephone='18645110989' ) 
 group by u.userCode 
 ) dailyData 
 right join tbl_userinfo ui on ui.userCode = dailyData.userCode 
 where ui.level='DSM' 
 and ui.region = ( select region from tbl_userinfo where telephone='18645110989' );
 
 ------------------------------------getDailyPEDData4RSDMobile----------------------------------------
select 
 ui.regionCenter as name, ui.userCode, 
 ( select count(1) from tbl_hospital h where h.region = ui.regionCenter and h.isPedAssessed='1' ) hosNum, 
 IFNULL(dailyData.inNum,0) as inNum, 
 IFNULL(dailyData.pnum,0) as pnum, 
 IFNULL(dailyData.whnum,0) as whnum, 
 IFNULL(dailyData.lsnum,0) as lsnum, 
 IFNULL(dailyData.averageDose,0) as averageDose, 
 IFNULL(dailyData.hmgRate,0) as hmgRate, 
 IFNULL(dailyData.omgRate,0) as omgRate, 
 IFNULL(dailyData.tmgRate,0) as tmgRate, 
 IFNULL(dailyData.fmgRate,0) as fmgRate 
 from ( 
     select u.regionCenter as name,u.userCode,   
     count(1) as inNum, 
     IFNULL(sum(pd.pnum),0) as pnum, 
     IFNULL(sum(pd.whnum),0) as whnum, 
     IFNULL(sum(pd.lsnum),0) as lsnum, 
     IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose,
     IFNULL( sum( IFNULL(pd.hqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as hmgRate,
     IFNULL( sum( IFNULL(pd.hbid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.oqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as omgRate,
     IFNULL( sum( IFNULL(pd.obid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.tqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as tmgRate,
     IFNULL( sum( IFNULL(pd.tbid,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as fmgRate 
     from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 
     where pd.hospitalName = h1.name 
     and h1.region = u.regionCenter 
     and TO_DAYS('2014-06-10') = TO_DAYS(pd.createdate) 
     and h1.isPedAssessed='1' 
     and u.level='RSD' 
     group by u.regionCenter 
     order by u.regionCenter
 ) dailyData right join tbl_userinfo ui on ui.userCode = dailyData.userCode 
 where ui.level='RSD' 
 order by ui.regionCenter;
 
 ------------------------------------getDailyPEDChildData4DSMMobile----------------------------------------
 
 select 
 ui.name, ui.userCode, 
 ( select count(1) from tbl_hospital h where h.saleCode = ui.userCode and h.rsmRegion = ui.region and h.dsmCode = ui.superior and h.isPedAssessed='1') hosNum, 
 IFNULL(dailyData.inNum,0) as inNum, 
 IFNULL(dailyData.pnum,0) as pnum, 
 IFNULL(dailyData.whnum,0) as whnum, 
 IFNULL(dailyData.lsnum,0) as lsnum, 
 IFNULL(dailyData.averageDose,0) as averageDose, 
 IFNULL(dailyData.hmgRate,0) as hmgRate, 
 IFNULL(dailyData.omgRate,0) as omgRate, 
 IFNULL(dailyData.tmgRate,0) as tmgRate, 
 IFNULL(dailyData.fmgRate,0) as fmgRate 
 from ( 
 select u.name,u.userCode, 
 count(1) as inNum, 
 IFNULL(sum(pd.pnum),0) as pnum, 
 IFNULL(sum(pd.whnum),0) as whnum, 
 IFNULL(sum(pd.lsnum),0) as lsnum, 
 IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose,
 IFNULL( sum( IFNULL(pd.hqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as hmgRate,
 IFNULL( sum( IFNULL(pd.hbid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.oqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as omgRate,
 IFNULL( sum( IFNULL(pd.obid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.tqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as tmgRate,
 IFNULL( sum( IFNULL(pd.tbid,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as fmgRate 
 from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 
 where pd.hospitalName = h1.name 
 and pd.rsmRegion = u.region 
 and pd.dsmCode = u.superior 
 and h1.saleCode = u.userCode 
 and TO_DAYS('2014-04-15') = TO_DAYS(pd.createdate) 
 and h1.isPedAssessed='1' 
 and u.level='REP' 
 and u.superior = ( select userCode from tbl_userinfo where telephone='13600486683' ) 
 group by u.userCode 
 ) dailyData 
 right join tbl_userinfo ui on ui.userCode = dailyData.userCode 
 where ui.level='REP' 
 and ui.superior = ( select userCode from tbl_userinfo where telephone='13600486683' );
 
 ------------------------------------getDailyPEDChildData4RSDMobile------getDailyPEDData4RSMMobile----------------------------------
 select 
 ui.region as name, ui.userCode, 
 ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isPedAssessed='1' ) hosNum, 
 IFNULL(dailyData.inNum,0) as inNum, 
 IFNULL(dailyData.pnum,0) as pnum, 
 IFNULL(dailyData.whnum,0) as whnum, 
 IFNULL(dailyData.lsnum,0) as lsnum, 
 IFNULL(dailyData.averageDose,0) as averageDose, 
 IFNULL(dailyData.hmgRate,0) as hmgRate, 
 IFNULL(dailyData.omgRate,0) as omgRate, 
 IFNULL(dailyData.tmgRate,0) as tmgRate, 
 IFNULL(dailyData.fmgRate,0) as fmgRate 
 from ( 
    select u.region as name,u.userCode, 
     count(1) as inNum, 
     IFNULL(sum(pd.pnum),0) as pnum, 
     IFNULL(sum(pd.whnum),0) as whnum, 
     IFNULL(sum(pd.lsnum),0) as lsnum, 
     IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose,
     IFNULL( sum( IFNULL(pd.hqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as hmgRate,
     IFNULL( sum( IFNULL(pd.hbid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.oqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as omgRate,
     IFNULL( sum( IFNULL(pd.obid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.tqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as tmgRate,
     IFNULL( sum( IFNULL(pd.tbid,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as fmgRate 
     from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 
     where pd.hospitalName = h1.name 
     and pd.rsmRegion = u.region 
     and TO_DAYS('2014-04-18') = TO_DAYS(pd.createdate) 
     and h1.isPedAssessed='1' 
     and u.level='RSM' 
     and u.regionCenter = (select regionCenter from tbl_userinfo where telephone='18686868896') 
     group by u.userCode 
 ) dailyData right join tbl_userinfo ui on ui.userCode = dailyData.userCode 
 where ui.level='RSM' 
 and ui.regionCenter = (select regionCenter from tbl_userinfo where telephone='18686868896');
 
-----------------------------------------------getDailyRESData4CountoryMobile---------------------------
select '全国' as name,null as userCode,
 ( select count(1) from tbl_hospital h where h.isResAssessed='1' ) hosNum,
count(1) as inNum,
IFNULL(sum(tmp.pnum),0) as pNum, 
IFNULL(sum(tmp.whnum),0) as whNum, 
IFNULL(sum(tmp.lsnum),0) as lsNum, 
IFNULL( sum( ( ( 1*IFNULL(tmp.oqd,0) + 2*1*IFNULL(tmp.tqd,0) + 1*3*IFNULL(tmp.otid,0) + 2*2*IFNULL(tmp.tbid,0) + 2*3*IFNULL(tmp.ttid,0) + 3*2*IFNULL(tmp.thbid,0) + 4*2*IFNULL(tmp.fbid,0) ) / 100 ) * IFNULL(tmp.lsnum,0) ) / IFNULL(sum(tmp.lsnum),0),0 ) as averageDose,
IFNULL( sum( IFNULL(tmp.oqd,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as omgRate,
IFNULL( sum( IFNULL(tmp.tqd,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as tmgRate,
IFNULL( sum( IFNULL(tmp.otid,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as thmgRate,
IFNULL( sum( IFNULL(tmp.tbid,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as fmgRate,
IFNULL( sum( IFNULL(tmp.ttid,0)/100*IFNULL(tmp.lsnum,0) + IFNULL(tmp.thbid,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as smgRate,
IFNULL( sum( IFNULL(tmp.fbid,0)/100*IFNULL(tmp.lsnum,0) ) / sum( IFNULL(tmp.lsnum,0) ),0 ) as emgRate 
from (
    select rd.* from tbl_respirology_data rd, tbl_hospital h 
    where rd.hospitalName = h.name 
    and TO_DAYS('2014-04-10') = TO_DAYS(rd.createdate)  
    and h.isResAssessed='1'
) tmp;

-----------------------------------------------------------------------------------------------------

----------------------------------------------------getDailyRESData4DSMMobile------------------------------
select ui.name, ui.userCode,
 ( select count(1) from tbl_hospital h where h.dsmCode = ui.userCode and h.rsmRegion = ui.region and h.isResAssessed='1' ) hosNum, 
 IFNULL(dailyData.inNum,0) as inNum,  
 IFNULL(dailyData.pnum,0) as pnum,  
 IFNULL(dailyData.whnum,0) as whnum,  
 IFNULL(dailyData.lsnum,0) as lsnum,  
 IFNULL(dailyData.averageDose,0) as averageDose,  
 IFNULL(dailyData.omgRate,0) as omgRate,  
 IFNULL(dailyData.tmgRate,0) as tmgRate,  
 IFNULL(dailyData.thmgRate,0) as thmgRate,  
 IFNULL(dailyData.fmgRate,0) as fmgRate, 
 IFNULL(dailyData.smgRate,0) as smgRate, 
 IFNULL(dailyData.emgRate,0) as emgRate 
 from ( 
 select u.name,u.userCode,
 count(1) as inNum, 
 IFNULL(sum(rd.pnum),0) as pnum, 
 IFNULL(sum(rd.whnum),0) as whnum, 
 IFNULL(sum(rd.lsnum),0) as lsnum, 
 IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) as averageDose,
 IFNULL( sum( IFNULL(rd.oqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as omgRate,
 IFNULL( sum( IFNULL(rd.tqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as tmgRate,
 IFNULL( sum( IFNULL(rd.otid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as thmgRate,
 IFNULL( sum( IFNULL(rd.tbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as fmgRate,
 IFNULL( sum( IFNULL(rd.ttid,0)/100*IFNULL(rd.lsnum,0) + IFNULL(rd.thbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as smgRate,
 IFNULL( sum( IFNULL(rd.fbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as emgRate 
 from tbl_userinfo u, tbl_respirology_data rd, tbl_hospital h1 
 where rd.hospitalName = h1.name 
 and h1.rsmRegion = u.region 
 and h1.dsmCode = u.userCode 
 and TO_DAYS('2014-04-10') = TO_DAYS(rd.createdate) 
 and h1.isResAssessed='1' 
 and u.level='DSM' 
 and u.region = ( select region from tbl_userinfo where telephone='18645110989' ) 
 group by u.userCode 
 ) dailyData 
 right join tbl_userinfo ui on ui.userCode = dailyData.userCode 
 where ui.level='DSM' 
 and ui.region = ( select region from tbl_userinfo where telephone='18645110989' );
-----------------------------------------------------------------------------------------------------

 ------------------------------------getDailyRESChildData4RSDMobile------getDailyRESData4RSMMobile----------------------------------
 select ui.region as name, ui.userCode,
 ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isResAssessed='1' ) hosNum, 
 IFNULL(dailyData.inNum,0) as inNum,  
 IFNULL(dailyData.pnum,0) as pnum,  
 IFNULL(dailyData.whnum,0) as whnum,  
 IFNULL(dailyData.lsnum,0) as lsnum,  
 IFNULL(dailyData.averageDose,0) as averageDose,  
 IFNULL(dailyData.omgRate,0) as omgRate,  
 IFNULL(dailyData.tmgRate,0) as tmgRate,  
 IFNULL(dailyData.thmgRate,0) as thmgRate,  
 IFNULL(dailyData.fmgRate,0) as fmgRate, 
 IFNULL(dailyData.smgRate,0) as smgRate, 
 IFNULL(dailyData.emgRate,0) as emgRate 
 from ( 
 select u.region as name,u.userCode,
 count(1) as inNum, 
 IFNULL(sum(rd.pnum),0) as pnum, 
 IFNULL(sum(rd.whnum),0) as whnum, 
 IFNULL(sum(rd.lsnum),0) as lsnum, 
 IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) as averageDose,
 IFNULL( sum( IFNULL(rd.oqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as omgRate,
 IFNULL( sum( IFNULL(rd.tqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as tmgRate,
 IFNULL( sum( IFNULL(rd.otid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as thmgRate,
 IFNULL( sum( IFNULL(rd.tbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as fmgRate,
 IFNULL( sum( IFNULL(rd.ttid,0)/100*IFNULL(rd.lsnum,0) + IFNULL(rd.thbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as smgRate,
 IFNULL( sum( IFNULL(rd.fbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as emgRate 
 from tbl_userinfo u, tbl_respirology_data rd, tbl_hospital h1 
 where rd.hospitalName = h1.name 
 and h1.rsmRegion = u.region 
 and TO_DAYS('2014-04-10') = TO_DAYS(rd.createdate) 
 and h1.isResAssessed='1' 
 and u.level='RSM' 
 and u.regionCenter = ( select regionCenter from tbl_userinfo where telephone='18686868896' ) 
 group by u.userCode 
 ) dailyData 
 right join tbl_userinfo ui on ui.userCode = dailyData.userCode 
 where ui.level='RSM' 
 and ui.regionCenter = ( select regionCenter from tbl_userinfo where telephone='18686868896' ) 
 order by ui.region;
 
 --------------------------------------------------------------------------------------------------
 --------------------------------------------getDailyRESData4RSDMobile------------------------------------------------------
select ui.regionCenter as name, ui.userCode,
 ( select count(1) from tbl_hospital h where h.region = ui.regionCenter and h.isResAssessed='1' ) hosNum, 
 IFNULL(dailyData.inNum,0) as inNum,  
 IFNULL(dailyData.pnum,0) as pnum,  
 IFNULL(dailyData.whnum,0) as whnum,  
 IFNULL(dailyData.lsnum,0) as lsnum,  
 IFNULL(dailyData.averageDose,0) as averageDose,  
 IFNULL(dailyData.omgRate,0) as omgRate,  
 IFNULL(dailyData.tmgRate,0) as tmgRate,  
 IFNULL(dailyData.thmgRate,0) as thmgRate,  
 IFNULL(dailyData.fmgRate,0) as fmgRate, 
 IFNULL(dailyData.smgRate,0) as smgRate, 
 IFNULL(dailyData.emgRate,0) as emgRate 
 from ( 
 select u.regionCenter as name,u.userCode,
 count(1) as inNum, 
 IFNULL(sum(rd.pnum),0) as pnum, 
 IFNULL(sum(rd.whnum),0) as whnum, 
 IFNULL(sum(rd.lsnum),0) as lsnum, 
 IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) as averageDose,
 IFNULL( sum( IFNULL(rd.oqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as omgRate,
 IFNULL( sum( IFNULL(rd.tqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as tmgRate,
 IFNULL( sum( IFNULL(rd.otid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as thmgRate,
 IFNULL( sum( IFNULL(rd.tbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as fmgRate,
 IFNULL( sum( IFNULL(rd.ttid,0)/100*IFNULL(rd.lsnum,0) + IFNULL(rd.thbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as smgRate,
 IFNULL( sum( IFNULL(rd.fbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as emgRate 
 from tbl_userinfo u, tbl_respirology_data rd, tbl_hospital h1 
 where rd.hospitalName = h1.name 
 and h1.region = u.regionCenter 
 and TO_DAYS('2014-04-10') = TO_DAYS(rd.createdate) 
 and h1.isResAssessed='1' 
 and u.level='RSD' 
 group by u.regionCenter 
 ) dailyData 
 right join tbl_userinfo ui on ui.userCode = dailyData.userCode 
 where ui.level='RSD' 
 order by ui.regionCenter;
 
 -------------------------------------getWeeklyPEDData4DSMMobile--------------------------------
 select IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.dsmCode and u.region = lastweekdata.rsmRegion and u.level='DSM'),'vacant') as name 
 , lastweekdata.dsmCode as userCode , lastweekdata.rsmRegion 
 ,lastweekdata.pnum 
 , ROUND((lastweekdata.pnum - last2weekdata.pnum) / last2weekdata.pnum,2) as pnumRatio 
 , lastweekdata.lsnum 
 , ROUND((lastweekdata.lsnum - last2weekdata.lsnum) / last2weekdata.lsnum,2) as lsnumRatio 
 , lastweekdata.inRate 
 , ROUND((lastweekdata.inRate - last2weekdata.inRate),2) as inRateRatio 
 , lastweekdata.whRate 
 , ROUND((lastweekdata.whRate - last2weekdata.whRate),2) as whRateRatio 
 , lastweekdata.averageDose 
 , ROUND((lastweekdata.averageDose - last2weekdata.averageDose ) / last2weekdata.averageDose,2) as averageDoseRatio 
 , lastweekdata.hmgRate as hmgRate 
 , ROUND((lastweekdata.hmgRate - last2weekdata.hmgRate ),2) as hmgRateRatio 
 , lastweekdata.omgRate as omgRate 
 , ROUND((lastweekdata.omgRate - last2weekdata.omgRate ),2) as omgRateRatio 
 , lastweekdata.tmgRate as tmgRate 
 , ROUND((lastweekdata.tmgRate - last2weekdata.tmgRate ),2) as tmgRateRatio 
 , lastweekdata.fmgRate as fmgRate 
 , ROUND((lastweekdata.fmgRate - last2weekdata.fmgRate ),2) as fmgRateRatio 
 from ( 
   select dsmCode, rsmRegion, 
   IFNULL(sum(lastweek.pnum),0) as pnum, 
    IFNULL(sum(lastweek.lsnum),0) as lsnum, 
    IFNULL(sum(lastweek.lsnum),0) / IFNULL(sum(lastweek.pnum),0) as whRate, 
    IFNULL(sum(least(lastweek.innum,3)),0) / (count(1)*3) as inRate, 
    IFNULL(sum(lastweek.averageDose*lastweek.lsnum)/sum(lastweek.lsnum),0) as averageDose, 
    IFNULL(sum(lastweek.hmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as hmgRate, 
    IFNULL(sum(lastweek.omgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as omgRate, 
    IFNULL(sum(lastweek.tmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as tmgRate, 
    IFNULL(sum(lastweek.fmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as fmgRate 
    from ( select * from tbl_pediatrics_data_weekly order by duration desc limit 0,920 ) lastweek
    group by rsmRegion,dsmCode 
) lastweekdata, 
( 
    select dsmCode, rsmRegion, 
    IFNULL(sum(last2week.pnum),0) as pnum, 
    IFNULL(sum(last2week.lsnum),0) as lsnum, 
    IFNULL(sum(last2week.lsnum),0) / IFNULL(sum(last2week.pnum),0) as whRate, 
    IFNULL(sum(least(last2week.innum,3)),0) / (count(1)*3) as inRate, 
    IFNULL(sum(last2week.averageDose*last2week.lsnum)/sum(last2week.lsnum),0) as averageDose, 
    IFNULL(sum(last2week.hmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as hmgRate, 
    IFNULL(sum(last2week.omgRate*last2week.lsnum)/sum(last2week.lsnum),0) as omgRate, 
    IFNULL(sum(last2week.tmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as tmgRate, 
    IFNULL(sum(last2week.fmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as fmgRate 
    from ( select * from tbl_pediatrics_data_weekly order by duration desc limit 920,920 ) last2week 
    group by rsmRegion,dsmCode 
) last2weekdata 
where lastweekdata.dsmCode = last2weekdata.dsmCode 
and lastweekdata.rsmRegion = last2weekdata.rsmRegion 
and lastweekdata.rsmRegion = (select region from tbl_userinfo where telephone='18645110989');



-----------------------------------------------------

select 
    duration
    , rsmRegion 
    , IFNULL((select u.name from tbl_userinfo u where u.userCode = dsmCode and u.region = rsmRegion and u.level='DSM'),'vacant') as name
    , dsmCode 
    , IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate 
    from tbl_pediatrics_data_weekly 
    where duration between '2014.01.02-2014.01.08' and '2014.04.10-2014.04.16' 
    group by rsmRegion, dsmCode, duration 
    order by rsmRegion asc, dsmCode asc, duration desc;
    
select 
    pedData.duration 
    , pedData.region 
    , pedData.rsmRegion 
	, resData.inRate as resInRate
	, pedData.inRate as pedInRate
from ( 
		select 
		duration,
		region,
		rsmRegion,
		IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate 
		from tbl_respirology_data_weekly 
		where duration between '2014.04.03-2014.04.09' and '2014.04.24-2014.04.30' 
		group by region, rsmRegion, duration 
	) resData
	,( 
		select 
		duration,
		region,
		rsmRegion,
		IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate 
		from tbl_pediatrics_data_weekly 
		where duration between '2014.04.03-2014.04.09' and '2014.04.24-2014.04.30' 
		group by region, rsmRegion, duration 		
	) pedData 
where resData.duration = pedData.duration 
and resData.region = pedData.region 
and resData.rsmRegion = pedData.rsmRegion 
order by region asc, rsmRegion asc, duration desc;

--------------------------getDSMSelfReportProcessRESDetailData---------------------------
select h.name as hospitalName, 
( select IFNULL((select count(1) 
                from tbl_respirology_data rd 
                where rd.hospitalName = h.name 
                and date_format(rd.createdate,'%Y-%m-%d') between '2014-06-05' and '2014-06-12' 
                group by rd.hospitalName    
                ),0) 
) as inNum, 
( select ui.name 
    from tbl_userinfo ui 
    where ui.userCode = h.saleCode 
    and ui.superior = h.dsmCode 
    and ui.region = h.rsmRegion 
    and ui.level='REP' 
) as salesName,  
h.isResAssessed as isAssessed 
from tbl_userinfo u, tbl_hospital h 
where h.dsmCode = u.userCode 
and h.rsmRegion = u.region 
and telephone = 18089768818;
------------------------------------getMonthlyInRateData-----------------------------------
select pedData.duration,pedData.region,pedData.rsmRegion,resData.inRate as resInRate,pedData.inRate as pedInRate  
  from (  
    select duration, h.region, h.rsmRegion, IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate
    from tbl_respirology_data_weekly rd, tbl_hospital h  
    where duration between '2014.05.01-2014.05.07' and '2014.05.29-2014.06.04' 
    and rd.hospitalCode = h.code 
    group by h.region, h.rsmRegion, duration 
 ) resData  
 ,(  
    select duration, h.region, h.rsmRegion, IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate
    from tbl_pediatrics_data_weekly pd, tbl_hospital h  
    where duration between '2014.05.01-2014.05.07' and '2014.05.29-2014.06.04' 
    and pd.hospitalCode = h.code 
    group by h.region, h.rsmRegion, duration 
 ) pedData  
  where resData.duration = pedData.duration  
  and resData.region = pedData.region  
  and resData.rsmRegion = pedData.rsmRegion  
  order by region asc, rsmRegion asc, duration desc ;
  
----------------------------------getAllDoctors -----------------------------------------
select h.region, h.rsmRegion 
, case when h.dsmCode is null then '#N/A' else (
    select u.name from tbl_userinfo u 
    where u.region = h.rsmRegion 
    and u.regionCenter = h.region 
    and u.userCode=h.dsmCode 
    and u.level='DSM') end dsmName 
, case when d.salesCode is null or d.salesCode='' then '#N/A' else (
    select u.name from tbl_userinfo u 
    where u.region = h.rsmRegion 
    and u.regionCenter = h.region 
    and u.superior = h.dsmCode 
    and u.userCode=d.salesCode 
    and u.level='REP') end salesName
, case when d.salesCode is null or d.salesCode='' then '#N/A' else d.salesCode end salesCode 
, h.code as hospitalCode, h.name as hospitalName
, d.code as doctorCode,  d.name as doctorName 
from tbl_doctor d, tbl_hospital h 
where d.hospitalCode = h.code 
order by h.region, h.rsmRegion, h.code, d.code;

----------------------------------------------------------------------------------------------


insert into tbl_chestSurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-04-24', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-04-24', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
cd_data.inNum,
cd_data.pnum,
cd_data.whnum,
cd_data.risknum,
cd_data.lsnum,
cd_data.averageDose,
cd_data.omgRate,
cd_data.tmgRate,
cd_data.thmgRate,
cd_data.fmgRate,
cd_data.smgRate,
cd_data.emgRate,
month(DATE_SUB('2014-04-24', Interval 7 day)),
now() 
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(cd.pnum)/count_hos.inNum)*5 as pnum,
    (sum(cd.risknum)/count_hos.inNum)*5 as risknum,
    (sum(cd.whnum)/count_hos.inNum)*5 as whnum,
    (sum(cd.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(cd.oqd,0) 
                        + 2*1*IFNULL(cd.tqd,0) 
                        + 1*3*IFNULL(cd.otid,0) 
                        + 2*2*IFNULL(cd.tbid,0) 
                        + 2*3*IFNULL(cd.ttid,0) 
                        + 3*2*IFNULL(cd.thbid,0) 
                        + 4*2*IFNULL(cd.fbid,0) 
                        ) / 100  
                    ) * IFNULL(cd.lsnum,0) 
                ) / IFNULL(sum(cd.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(cd.oqd,0)*cd.lsnum/100)/sum(cd.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(cd.tqd,0)*cd.lsnum/100)/sum(cd.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(cd.otid,0)*cd.lsnum/100)/sum(cd.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(cd.tbid,0)*cd.lsnum/100)/sum(cd.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(cd.ttid,0)*cd.lsnum + IFNULL(cd.thbid,0)*cd.lsnum)/100)/sum(cd.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(cd.fbid,0)*cd.lsnum/100)/sum(cd.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data cd, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data cd, tbl_hospital h
        WHERE cd.createdate between DATE_SUB('2014-04-24', Interval 7 day) and '2014-04-24' 
        and cd.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE cd.createdate between DATE_SUB('2014-04-24', Interval 7 day) and '2014-04-24' 
    and cd.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) cd_data 
right join tbl_hospital h on cd_data.code = h.code 
where h.isChestSurgeryAssessed='1';

----------------------------getLowerWeeklyRESData4REPMobile-------------------------------
select lastweekdata.pnum 
 , ROUND((lastweekdata.pnum - last2weekdata.pnum) / last2weekdata.pnum,2) as pnumRatio 
 , lastweekdata.lsnum 
 , ROUND((lastweekdata.lsnum - last2weekdata.lsnum) / last2weekdata.lsnum,2) as lsnumRatio 
 , lastweekdata.inRate 
 , ROUND((lastweekdata.inRate - last2weekdata.inRate),2) as inRateRatio 
 , lastweekdata.whRate 
 , ROUND((lastweekdata.whRate - last2weekdata.whRate),2) as whRateRatio 
 , lastweekdata.averageDose 
 , ROUND((lastweekdata.averageDose - last2weekdata.averageDose ) / last2weekdata.averageDose,2) as averageDoseRatio 
 , lastweekdata.omgRate as omgRate 
 , ROUND((lastweekdata.omgRate - last2weekdata.omgRate ),2) as omgRateRatio 
 , lastweekdata.tmgRate as tmgRate 
 , ROUND((lastweekdata.tmgRate - last2weekdata.tmgRate ),2) as tmgRateRatio 
 , lastweekdata.thmgRate as thmgRate 
 , ROUND((lastweekdata.thmgRate - last2weekdata.thmgRate ),2) as thmgRateRatio 
 , lastweekdata.fmgRate as fmgRate 
 , ROUND((lastweekdata.fmgRate - last2weekdata.fmgRate ),2) as fmgRateRatio 
 , lastweekdata.smgRate as smgRate 
 , ROUND((lastweekdata.smgRate - last2weekdata.smgRate ),2) as smgRateRatio 
 , lastweekdata.emgRate as emgRate 
 , ROUND((lastweekdata.emgRate - last2weekdata.emgRate ),2) as emgRateRatio 
 , lastweekdata.saleCode as userCode 
 , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.saleCode and u.superior = lastweekdata.dsmCode and u.level='REP'),'vacant') as name 
 from ( 
   select h.dsmCode, h.saleCode, 
    IFNULL(sum(lastweek.pnum),0) as pnum, 
    IFNULL(sum(lastweek.lsnum),0) as lsnum, 
    IFNULL(sum(lastweek.lsnum),0) / IFNULL(sum(lastweek.pnum),0) as whRate, 
    IFNULL(sum(least(lastweek.innum,3)),0) / (count(1)*3) as inRate, 
    IFNULL(sum(lastweek.averageDose*lastweek.lsnum)/sum(lastweek.lsnum),0) as averageDose, 
    IFNULL(sum(lastweek.omgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as omgRate, 
    IFNULL(sum(lastweek.tmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as tmgRate, 
    IFNULL(sum(lastweek.thmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as thmgRate, 
    IFNULL(sum(lastweek.fmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as fmgRate, 
    IFNULL(sum(lastweek.smgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as smgRate, 
    IFNULL(sum(lastweek.emgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as emgRate 
    from ( select * from tbl_respirology_data_weekly order by duration desc limit 0,398 ) lastweek, tbl_hospital h 
   where lastweek.hospitalCode = h.code ;
    group by h.dsmCode, h.saleCode 
 )lastweekdata, 
( 
    select h.dsmCode, h.saleCode, 
    IFNULL(sum(last2week.pnum),0) as pnum, 
    IFNULL(sum(last2week.lsnum),0) as lsnum, 
    IFNULL(sum(last2week.lsnum),0) / IFNULL(sum(last2week.pnum),0) as whRate, 
    IFNULL(sum(least(last2week.innum,3)),0) / (count(1)*3) as inRate, 
    IFNULL(sum(last2week.averageDose*last2week.lsnum)/sum(last2week.lsnum),0) as averageDose, 
    IFNULL(sum(last2week.omgRate*last2week.lsnum)/sum(last2week.lsnum),0) as omgRate, 
    IFNULL(sum(last2week.tmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as tmgRate, 
    IFNULL(sum(last2week.thmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as thmgRate, 
    IFNULL(sum(last2week.fmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as fmgRate, 
    IFNULL(sum(last2week.smgRate*last2week.lsnum)/sum(last2week.lsnum),0) as smgRate, 
    IFNULL(sum(last2week.emgRate*last2week.lsnum)/sum(last2week.lsnum),0) as emgRate 
    from ( select * from tbl_respirology_data_weekly order by duration desc limit 398,398 ) last2week, tbl_hospital h 
   where last2week.hospitalCode = h.code ;
    group by h.dsmCode, h.saleCode 
) last2weekdata 
where lastweekdata.dsmCode = last2weekdata.dsmCode 
and lastweekdata.saleCode = last2weekdata.saleCode 
and lastweekdata.saleCode = ?
and lastweekdata.dsmCode = ?;

--------------------------------------------getHomeWeeklyDataOfRSM-------------------------

 select homeData.newWhNum 
 , homeData.cureRate  
 , homeData.lsnum  
 , homeData.lsRate  
 , homeData.reachRate 
 , ui.region as name 
 , homeData.reportNum 
 , (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN 
 , (    select count(1) 
        from tbl_doctor d2, tbl_hospital h2    
        where d2.hospitalCode = h2.code    
        and h2.rsmRegion = ui.region    
        and d2.createdate between '2014-07-14' and '2014-07-21'    
    ) as newDrNum
, (   select count(1) 
        from tbl_doctor d2, tbl_hospital h2    
        where d2.hospitalCode = h2.code    
        and h2.rsmRegion = ui.region    
    ) as totalDrNum 
from (  
    select sum(hd.salenum) as newWhNum  
    , sum(hd.ltenum)/sum(hd.asthmanum) as cureRate  
    , sum(hd.lsnum) as lsnum  , sum(hd.lsnum)/sum(hd.asthmanum) as lsRate  
    , sum(hd.lttnum)/sum(hd.lsnum) as reachRate 
    , h.rsmRegion, count(1) as reportNum  
    from tbl_home_data hd, tbl_hospital h  
    where hd.hospitalCode = h.code  and h.region = 'North2 GRA'  
    and hd.createdate between '2014-07-14' and '2014-07-21'  
    group by h.region, h.rsmRegion 
) homeData 
right join tbl_userinfo ui on ui.region = homeData.rsmRegion  
where ui.regionCenter = 'North2 GRA' and ui.level='RSM';


-----------------------------------------------------------------------------------------------
 select h.province, h.city, h.code, h.name, h.dragonType, h.level, h.isTop100 
 , ( select distinct property_value from tbl_property where property_name = h.region ) as brCNName 
 , h.region 
 , rsd.userCode as rsdCode 
 , rsd.name as rsdName 
 , rsd.telephone as rsdTel 
 , rsd.email as rsdEmail 
 , h.rsmRegion 
 , rsm.userCode as rsmCode 
 , rsm.name as rsmName 
 , rsm.telephone as rsmTel 
 , rsm.email as rsmEmail 
 , ( select userCode from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmCode 
 , ( select name from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmName  
 , ( select telephone from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmTel 
 , ( select email from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmEmail 
 , hu.isPrimary as isMainSales 
 , sales.userCode as salesCode 
 , sales.name as salesName 
 , sales.telephone as salesTel 
 , sales.email as salesEmail 
 from tbl_hospital h, tbl_hos_user hu, tbl_userinfo sales, tbl_userinfo rsd, tbl_userinfo rsm 
 where h.code = hu.hosCode 
 and hu.userCode = sales.userCode 
 and h.region = rsd.regionCenter 
 and rsd.level='RSD' 
 and h.rsmRegion = rsm.region 
 and rsm.level='RSM' 
 and hu.userCode !='2000003' 
 and h.isPedAssessed='1' 
 and hu.isPrimary ='1' 
 union all
  select h.province, h.city, h.code, h.name, h.dragonType, h.level, h.isTop100 
 , ( select distinct property_value from tbl_property where property_name = h.region ) as brCNName 
 , h.region 
 , rsd.userCode as rsdCode 
 , rsd.name as rsdName 
 , rsd.telephone as rsdTel 
 , rsd.email as rsdEmail 
 , h.rsmRegion 
 , rsm.userCode as rsmCode 
 , rsm.name as rsmName 
 , rsm.telephone as rsmTel 
 , rsm.email as rsmEmail 
 , ( select userCode from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmCode 
 , ( select name from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmName  
 , ( select telephone from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmTel 
 , ( select email from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmEmail 
 , hu.isPrimary as isMainSales 
 , '2000003' as salesCode 
 , '' as salesName 
 , '' as salesTel 
 , '' as salesEmail 
 from tbl_hospital h, tbl_hos_user hu, tbl_userinfo rsd, tbl_userinfo rsm 
 where h.code = hu.hosCode 
 and h.region = rsd.regionCenter 
 and rsd.level='RSD' 
 and h.rsmRegion = rsm.region 
 and rsm.level='RSM' 
 and h.isPedAssessed='1'
 and hu.userCode ='2000003' 
 and hu.isPrimary ='1' 
 order by region, rsmRegion;
 
-----------------------------------------getRESMonthReportDBData-----------------------------------------
 select h.rsmRegion  
 ,(select distinct name from tbl_userinfo u where u.regionCenter = h.region and u.region = h.rsmRegion and u.level='RSM') as rsmName  
 ,date_MM  ,date_YYYY  
 ,'' as duration  
 ,sum(pnum) as pnum  
 ,sum(lsnum) as lsnum  
 ,sum(aenum) as aenum  
 ,IFNULL(sum(least(innum,3))/(count(1)*3),0) as inRate  
 ,IFNULL(sum(rdw.lsnum)/sum(rdw.pnum),0) as whRate  
 ,IFNULL(sum(rdw.averageDose*rdw.lsnum)/sum(rdw.lsnum),0) as averageDose  
 ,(     
    select temp2.weekNum    
    from (       
        select count(1) as weekNum,date_YYYY,date_MM        
        from (           
            select distinct rdw2.date_YYYY,rdw2.date_MM,rdw2.duration            
            from tbl_respirology_data_weekly rdw2, tbl_hospital h2            
            where rdw2.hospitalCode = h2.code            
            and ( (rdw2.date_YYYY > 2013 and rdw2.date_MM > 3)  or ( rdw2.date_YYYY > 2014 ))        
        ) temp        
        group by temp.date_YYYY,temp.date_MM    
    ) temp2    
    where rdw.date_YYYY = temp2.date_YYYY 
    and rdw.date_MM = temp2.date_MM  
) as weeklyCount  
,(  
    select sum(resnum) 
    from tbl_month_data md, tbl_hospital h1         
    where md.hospitalCode = h1.code and h1.rsmRegion = h.rsmRegion          
    and md.countMonth = concat(date_YYYY,'-',LPAD(date_MM, 2, 0)) 
) as resMonthNum  
from tbl_respirology_data_weekly rdw, tbl_hospital h  
where h.code = rdw.hospitalCode  
and ( (rdw.date_YYYY > 2013 and rdw.date_MM > 3)  or ( rdw.date_YYYY > 2014 ))  
and rdw.duration < '2014.08.07-2014.08.13'  
group by date_YYYY,date_MM,h.rsmRegion  
order by h.rsmRegion, date_YYYY, date_MM;


-------------getMonthlyDataOfREPByDSMCode----------------
select IFNULL(lastMonthData.pedEmernum,0) as pedEmernum 
, IFNULL(lastMonthData.pedroomnum,0) as pedroomnum 
, IFNULL(lastMonthData.resnum,0) as resnum 
, IFNULL(lastMonthData.othernum,0) as othernum 
, ROUND(IFNULL(lastMonthData.pedEmernum/lastMonthData.totalnum,0),2) as pedemernumrate 
, ROUND(IFNULL(lastMonthData.pedroomnum/lastMonthData.totalnum,0),2) as pedroomnumrate 
, ROUND(IFNULL(lastMonthData.resnum/lastMonthData.totalnum,0),2) as resnumrate 
, ROUND(IFNULL(lastMonthData.othernum/lastMonthData.totalnum,0),2) as othernumrate 
, ROUND(IFNULL((lastMonthData.pedEmernum-last2MonthData.pedEmernum)/last2MonthData.pedEmernum,0),2) as pedemernumratio 
, ROUND(IFNULL((lastMonthData.pedroomnum-last2MonthData.pedroomnum)/last2MonthData.pedroomnum,0),2) as pedroomnumratio 
, ROUND(IFNULL((lastMonthData.resnum-last2MonthData.resnum)/last2MonthData.resnum,0),2) as resnumratio 
, ROUND(IFNULL((lastMonthData.othernum-last2MonthData.othernum)/last2MonthData.othernum,0),2) as othernumratio 
, ROUND(IFNULL(lastMonthData.pedEmernum/lastMonthData.totalnum - last2MonthData.pedEmernum/last2MonthData.totalnum,0),2) as pedemernumrateratio 
, ROUND(IFNULL(lastMonthData.pedroomnum/lastMonthData.totalnum - last2MonthData.pedroomnum/last2MonthData.totalnum,0),2) as pedroomnumrateratio 
, ROUND(IFNULL(lastMonthData.resnum/lastMonthData.totalnum - last2MonthData.resnum/last2MonthData.totalnum,0),2) as resnumrateratio 
, ROUND(IFNULL(lastMonthData.othernum/lastMonthData.totalnum - last2MonthData.othernum/last2MonthData.totalnum,0),2) as othernumrateratio 
, IFNULL(lastMonthData.totalnum,0) as totalnum 
, ROUND(IFNULL((lastMonthData.totalnum - last2MonthData.totalnum)/last2MonthData.totalnum,0),2) as totalnumratio 
, IFNULL(lastMonthData.innum,0) as innum 
, ROUND(IFNULL((lastMonthData.innum-last2MonthData.innum)/last2MonthData.innum,0),2) as innumratio 
, IFNULL(lastMonthData.hosnum,0) as hosnum 
, ROUND(IFNULL((lastMonthData.hosnum-last2MonthData.hosnum)/last2MonthData.hosnum,0),2) as hosnumratio 
, ROUND(IFNULL(lastMonthData.innum,0)/IFNULL(lastMonthData.hosnum,0),2) as inrate 
, ROUND(IFNULL(lastMonthData.innum/lastMonthData.hosnum - last2MonthData.innum/last2MonthData.hosnum,0),2) as inrateratio 
, lastMonthData.saleName, '' as dsmName, lastMonthData.rsmRegion
, ( select distinct property_value from tbl_property where property_name = lastMonthData.region ) as region 
 from (
	select lastMonth.pedEmernum ,lastMonth.pedroomnum ,lastMonth.resnum ,lastMonth.othernum ,lastMonth.totalnum 
	, lastMonth.innum as innum 
	, (select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.dsmCode = u.superior and h.saleCode = u.userCode and h.isMonthlyAssessed='1') as hosnum 
	, u.userCode as saleCode, u.superior as dsmCode, u.name as saleName , u.region as rsmRegion, u.regionCenter as region 
	    from (
	       select h.saleCode as saleCode 
	       , h.dsmCode 
	       , h.rsmRegion 
	       , h.region 
	       , sum(pedEmernum) as pedEmernum 
	       , sum(pedroomnum) as pedroomnum 
	       , sum(resnum) as resnum 
	       , sum(other) as othernum 
	       , sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum 
	       , count(1) as innum 
	       from tbl_month_data md, tbl_hospital h 
			where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 MONTH),'%Y-%m') 
	       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' 
	       and h.dsmCode='5202081' 
	       group by h.dsmCode, h.saleCode 
	   ) lastMonth 
	   right join tbl_userinfo u on u.userCode = lastMonth.saleCode and u.superior = lastMonth.dsmCode
	   where u.superior = '5202081' 
	   and u.level='REP' 
) lastMonthData 
inner join ( 
	select last2Month.pedEmernum ,last2Month.pedroomnum ,last2Month.resnum ,last2Month.othernum ,last2Month.totalnum 
	, last2Month.innum as innum 
	, (select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.dsmCode = u.superior and h.saleCode = u.userCode and h.isMonthlyAssessed='1') as hosnum 
	, u.userCode as saleCode, u.superior as dsmCode, u.name as saleName , u.region as rsmRegion, u.regionCenter as region 
	    from (
	       select h.saleCode as saleCode 
	       , h.dsmCode 
	       , h.rsmRegion 
	       , h.region 
	       , sum(pedEmernum) as pedEmernum 
	       , sum(pedroomnum) as pedroomnum 
	       , sum(resnum) as resnum 
	       , sum(other) as othernum 
	       , sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum 
	       , count(1) as innum 
	       from tbl_month_data md, tbl_hospital h 
			where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m') 
	       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' 
	       and h.dsmCode='5202081' 
	       group by h.dsmCode, h.saleCode 
	   ) last2Month 
	   right join tbl_userinfo u on u.userCode = last2Month.saleCode and u.superior = last2Month.dsmCode
	   where u.superior = '5202081' 
	   and u.level='REP' 
) last2MonthData on lastMonthData.saleCode = last2MonthData.saleCode 
 and lastMonthData.dsmCode = last2MonthData.dsmCode and lastMonthData.rsmRegion = last2MonthData.rsmRegion;
 
 
 
 
 
 
update tbl_pediatrics_data_weekly 
set portNum = 10 
where region='South GRA' and duration='2014.09.29-2014.10.05';


update tbl_pediatrics_data_weekly 
set portNum = 10 
where region !='South GRA' and duration='2014.10.06-2014.10.12';