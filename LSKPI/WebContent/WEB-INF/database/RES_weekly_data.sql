delete from tbl_respirology_data_weekly where duration='2013.12.26-2014.01.01';
delete from tbl_respirology_data_weekly where duration='2014.01.02-2014.01.08';
delete from tbl_respirology_data_weekly where duration='2014.01.09-2014.01.15';
delete from tbl_respirology_data_weekly where duration='2014.01.16-2014.01.22';
delete from tbl_respirology_data_weekly where duration='2014.01.23-2014.01.29';
delete from tbl_respirology_data_weekly where duration='2014.01.30-2014.02.05';
delete from tbl_respirology_data_weekly where duration='2014.02.06-2014.02.12';
delete from tbl_respirology_data_weekly where duration='2014.02.13-2014.02.19';
delete from tbl_respirology_data_weekly where duration='2014.02.20-2014.02.26';
delete from tbl_respirology_data_weekly where duration='2014.02.27-2014.03.05';
delete from tbl_respirology_data_weekly where duration='2014.03.06-2014.03.12';
delete from tbl_respirology_data_weekly where duration='2014.03.13-2014.03.19';
delete from tbl_respirology_data_weekly where duration='2014.03.20-2014.03.26';
delete from tbl_respirology_data_weekly where duration='2014.03.27-2014.04.02';
delete from tbl_respirology_data_weekly where duration='2014.04.03-2014.04.09';
delete from tbl_respirology_data_weekly where duration='2014.04.10-2014.04.16';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-02', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-02', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-01-02', Interval 7 day) and '2014-01-02' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-01-02', Interval 7 day) and '2014-01-02' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-09', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-09', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-01-09', Interval 7 day) and '2014-01-09' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-01-09', Interval 7 day) and '2014-01-09' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-16', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-16', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-01-16', Interval 7 day) and '2014-01-16' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-01-16', Interval 7 day) and '2014-01-16' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-23', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-23', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-01-23', Interval 7 day) and '2014-01-23' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-01-23', Interval 7 day) and '2014-01-23' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-01-30', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-01-30', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-01-30', Interval 7 day) and '2014-01-30' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-01-30', Interval 7 day) and '2014-01-30' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-02-06', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-02-06', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-02-06', Interval 7 day) and '2014-02-06' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-02-06', Interval 7 day) and '2014-02-06' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-02-13', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-02-13', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-02-13', Interval 7 day) and '2014-02-13' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-02-13', Interval 7 day) and '2014-02-13' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-02-20', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-02-20', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-02-20', Interval 7 day) and '2014-02-20' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-02-20', Interval 7 day) and '2014-02-20' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-02-27', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-02-27', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-02-27', Interval 7 day) and '2014-02-27' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-02-27', Interval 7 day) and '2014-02-27' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-03-06', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-03-06', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-03-06', Interval 7 day) and '2014-03-06' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-03-06', Interval 7 day) and '2014-03-06' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-03-13', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-03-13', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-03-13', Interval 7 day) and '2014-03-13' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-03-13', Interval 7 day) and '2014-03-13' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-03-20', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-03-20', Interval 1 day),'%Y.%m.%d')) as duration, 
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
        WHERE rd.createdate between DATE_SUB('2014-03-20', Interval 7 day) and '2014-03-20' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-03-20', Interval 7 day) and '2014-03-20' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';