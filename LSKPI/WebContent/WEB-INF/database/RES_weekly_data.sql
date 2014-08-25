truncate table tbl_respirology_data_weekly;

insert into tbl_respirology_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-25', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-25', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-08-25', Interval 7 day)),Month(DATE_SUB('2014-08-25', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-08-25', Interval 7 day) and '2014-08-25' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-08-25', Interval 7 day) and '2014-08-25' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-18', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-18', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-08-18', Interval 7 day)),Month(DATE_SUB('2014-08-18', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-08-18', Interval 7 day) and '2014-08-18' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-08-18', Interval 7 day) and '2014-08-18' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-11', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-11', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-08-11', Interval 7 day)),Month(DATE_SUB('2014-08-11', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-08-11', Interval 7 day) and '2014-08-11' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-08-11', Interval 7 day) and '2014-08-11' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-04', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-04', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-08-04', Interval 7 day)),Month(DATE_SUB('2014-08-04', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-08-04', Interval 7 day) and '2014-08-04' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-08-04', Interval 7 day) and '2014-08-04' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-28', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-28', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-07-28', Interval 7 day)),Month(DATE_SUB('2014-07-28', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-07-28', Interval 7 day) and '2014-07-28' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-07-28', Interval 7 day) and '2014-07-28' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-21', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-21', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-07-21', Interval 7 day)),Month(DATE_SUB('2014-07-21', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-07-21', Interval 7 day) and '2014-07-21' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-07-21', Interval 7 day) and '2014-07-21' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-14', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-14', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-07-14', Interval 7 day)),Month(DATE_SUB('2014-07-14', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-07-14', Interval 7 day) and '2014-07-14' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-07-14', Interval 7 day) and '2014-07-14' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-07', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-07', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-07-07', Interval 7 day)),Month(DATE_SUB('2014-07-07', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-07-07', Interval 7 day) and '2014-07-07' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-07-07', Interval 7 day) and '2014-07-07' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-30', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-30', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-06-30', Interval 7 day)),Month(DATE_SUB('2014-06-30', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-06-30', Interval 7 day) and '2014-06-30' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-06-30', Interval 7 day) and '2014-06-30' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-23', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-23', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-06-23', Interval 7 day)),Month(DATE_SUB('2014-06-23', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-06-23', Interval 7 day) and '2014-06-23' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-06-23', Interval 7 day) and '2014-06-23' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-16', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-16', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-06-16', Interval 7 day)),Month(DATE_SUB('2014-06-16', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-06-16', Interval 7 day) and '2014-06-16' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-06-16', Interval 7 day) and '2014-06-16' 
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
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-09', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-09', Interval 1 day),'%Y.%m.%d')) as duration, 
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
now(),Year(DATE_SUB('2014-06-09', Interval 7 day)),Month(DATE_SUB('2014-06-09', Interval 7 day))  
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
        WHERE rd.createdate between DATE_SUB('2014-06-09', Interval 7 day) and '2014-06-09' 
        and rd.hospitalName = h.name 
        and h.isResAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE rd.createdate between DATE_SUB('2014-06-09', Interval 7 day) and '2014-06-09' 
    and rd.hospitalName = h.name 
    and h.code = count_hos.code
    and h.isResAssessed='1' 
    GROUP BY h.code
) rd_data 
right join tbl_hospital h on rd_data.code = h.code 
where h.isResAssessed='1';