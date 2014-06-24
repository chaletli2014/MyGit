delete from tbl_pediatrics_data_weekly where duration='2013.12.26-2014.01.01';
delete from tbl_pediatrics_data_weekly where duration='2014.01.02-2014.01.08';
delete from tbl_pediatrics_data_weekly where duration='2014.01.09-2014.01.15';
delete from tbl_pediatrics_data_weekly where duration='2014.01.16-2014.01.22';
delete from tbl_pediatrics_data_weekly where duration='2014.01.23-2014.01.29';
delete from tbl_pediatrics_data_weekly where duration='2014.01.30-2014.02.05';
delete from tbl_pediatrics_data_weekly where duration='2014.02.06-2014.02.12';
delete from tbl_pediatrics_data_weekly where duration='2014.02.13-2014.02.19';
delete from tbl_pediatrics_data_weekly where duration='2014.02.20-2014.02.26';
delete from tbl_pediatrics_data_weekly where duration='2014.02.27-2014.03.05';
delete from tbl_pediatrics_data_weekly where duration='2014.03.06-2014.03.12';
delete from tbl_pediatrics_data_weekly where duration='2014.03.13-2014.03.19';
delete from tbl_pediatrics_data_weekly where duration='2014.03.20-2014.03.26';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-02', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-02', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-01-02', Interval 7 day) and '2014-01-02' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-01-02', Interval 7 day) and '2014-01-02' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-09', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-09', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-01-09', Interval 7 day) and '2014-01-09' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-01-09', Interval 7 day) and '2014-01-09' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-16', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-16', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-01-16', Interval 7 day) and '2014-01-16' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-01-16', Interval 7 day) and '2014-01-16' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-23', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-23', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-01-23', Interval 7 day) and '2014-01-23' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-01-23', Interval 7 day) and '2014-01-23' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-30', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-30', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-01-30', Interval 7 day) and '2014-01-30' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-01-30', Interval 7 day) and '2014-01-30' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-02-06', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-02-06', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-02-06', Interval 7 day) and '2014-02-06' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-02-06', Interval 7 day) and '2014-02-06' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-02-13', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-02-13', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-02-13', Interval 7 day) and '2014-02-13' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-02-13', Interval 7 day) and '2014-02-13' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-02-20', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-02-20', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-02-20', Interval 7 day) and '2014-02-20' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-02-20', Interval 7 day) and '2014-02-20' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-02-27', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-02-27', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-02-27', Interval 7 day) and '2014-02-27' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-02-27', Interval 7 day) and '2014-02-27' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-03-06', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-03-06', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-03-06', Interval 7 day) and '2014-03-06' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-03-06', Interval 7 day) and '2014-03-06' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-03-13', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-03-13', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-03-13', Interval 7 day) and '2014-03-13' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-03-13', Interval 7 day) and '2014-03-13' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-03-20', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-03-20', Interval 1 day),'%Y.%m.%d')) as duration, 
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
		WHERE pd.createdate between DATE_SUB('2014-03-20', Interval 7 day) and '2014-03-20' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-03-20', Interval 7 day) and '2014-03-20' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';