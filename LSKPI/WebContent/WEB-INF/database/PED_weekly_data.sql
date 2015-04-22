truncate table tbl_pediatrics_data_weekly;

insert into tbl_pediatrics_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-09-08', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-09-08', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-09-08', Interval 7 day) and '2014-09-08' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-09-08', Interval 7 day) and '2014-09-08' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-09-01', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-09-01', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-09-01', Interval 7 day) and '2014-09-01' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-09-01', Interval 7 day) and '2014-09-01' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-25', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-25', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-08-25', Interval 7 day) and '2014-08-25' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-08-25', Interval 7 day) and '2014-08-25' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-18', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-18', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-08-18', Interval 7 day) and '2014-08-18' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-08-18', Interval 7 day) and '2014-08-18' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-11', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-11', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-08-11', Interval 7 day) and '2014-08-11' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-08-11', Interval 7 day) and '2014-08-11' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-04', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-04', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-08-04', Interval 7 day) and '2014-08-04' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-08-04', Interval 7 day) and '2014-08-04' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-28', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-28', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-07-28', Interval 7 day) and '2014-07-28' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-07-28', Interval 7 day) and '2014-07-28' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-21', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-21', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-07-21', Interval 7 day) and '2014-07-21' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-07-21', Interval 7 day) and '2014-07-21' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-14', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-14', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-07-14', Interval 7 day) and '2014-07-14' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-07-14', Interval 7 day) and '2014-07-14' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-07', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-07', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-07-07', Interval 7 day) and '2014-07-07' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-07-07', Interval 7 day) and '2014-07-07' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-30', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-30', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-06-30', Interval 7 day) and '2014-06-30' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-06-30', Interval 7 day) and '2014-06-30' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-23', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-23', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-06-23', Interval 7 day) and '2014-06-23' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-06-23', Interval 7 day) and '2014-06-23' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-16', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-16', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-06-16', Interval 7 day) and '2014-06-16' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-06-16', Interval 7 day) and '2014-06-16' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-09', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-09', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),
h.portNum 
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
		WHERE pd.createdate between DATE_SUB('2014-06-09', Interval 7 day) and '2014-06-09' 
		and pd.hospitalName = h.name 
		and h.isPedAssessed='1' 
		GROUP BY h.code
	) count_hos 
    WHERE pd.createdate between DATE_SUB('2014-06-09', Interval 7 day) and '2014-06-09' 
	and pd.hospitalName = h.name 
	and h.code = count_hos.code
	and h.isPedAssessed='1' 
    GROUP BY h.code
) pd_data 
right join tbl_hospital h on pd_data.code = h.code 
where h.isPedAssessed='1';