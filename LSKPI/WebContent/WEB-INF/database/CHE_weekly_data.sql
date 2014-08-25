truncate table tbl_chestsurgery_data_weekly;

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-25', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-25', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-08-25', Interval 7 day)),Month(DATE_SUB('2014-08-25', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-08-25', Interval 7 day) and '2014-08-25' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-08-25', Interval 7 day) and '2014-08-25' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-18', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-18', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-08-18', Interval 7 day)),Month(DATE_SUB('2014-08-18', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-08-18', Interval 7 day) and '2014-08-18' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-08-18', Interval 7 day) and '2014-08-18' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-11', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-11', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-08-11', Interval 7 day)),Month(DATE_SUB('2014-08-11', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-08-11', Interval 7 day) and '2014-08-11' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-08-11', Interval 7 day) and '2014-08-11' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-08-04', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-08-04', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-08-04', Interval 7 day)),Month(DATE_SUB('2014-08-04', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-08-04', Interval 7 day) and '2014-08-04' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-08-04', Interval 7 day) and '2014-08-04' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-28', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-28', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-07-28', Interval 7 day)),Month(DATE_SUB('2014-07-28', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-07-28', Interval 7 day) and '2014-07-28' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-07-28', Interval 7 day) and '2014-07-28' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-21', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-21', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-07-21', Interval 7 day)),Month(DATE_SUB('2014-07-21', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-07-21', Interval 7 day) and '2014-07-21' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-07-21', Interval 7 day) and '2014-07-21' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-14', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-14', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-07-14', Interval 7 day)),Month(DATE_SUB('2014-07-14', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-07-14', Interval 7 day) and '2014-07-14' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-07-14', Interval 7 day) and '2014-07-14' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-07-07', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-07-07', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-07-07', Interval 7 day)),Month(DATE_SUB('2014-07-07', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-07-07', Interval 7 day) and '2014-07-07' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-07-07', Interval 7 day) and '2014-07-07' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-30', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-30', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-06-30', Interval 7 day)),Month(DATE_SUB('2014-06-30', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-06-30', Interval 7 day) and '2014-06-30' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-06-30', Interval 7 day) and '2014-06-30' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-23', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-23', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-06-23', Interval 7 day)),Month(DATE_SUB('2014-06-23', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-06-23', Interval 7 day) and '2014-06-23' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-06-23', Interval 7 day) and '2014-06-23' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-16', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-16', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-06-16', Interval 7 day)),Month(DATE_SUB('2014-06-16', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-06-16', Interval 7 day) and '2014-06-16' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-06-16', Interval 7 day) and '2014-06-16' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';

insert into tbl_chestsurgery_data_weekly 
select 
null,
CONCAT(DATE_FORMAT(DATE_SUB('2014-06-09', Interval 7 day),'%Y.%m.%d'), '-',DATE_FORMAT(DATE_SUB('2014-06-09', Interval 1 day),'%Y.%m.%d')) as duration, 
h.name,
h.code,
che_data.inNum,
che_data.pnum,
che_data.risknum,
che_data.whnum,
che_data.lsnum,
che_data.averageDose,
che_data.omgRate,
che_data.tmgRate,
che_data.thmgRate,
che_data.fmgRate,
che_data.smgRate,
che_data.emgRate,
now(),Year(DATE_SUB('2014-06-09', Interval 7 day)),Month(DATE_SUB('2014-06-09', Interval 7 day))  
from (
SELECT 
    h.code, 
    count_hos.inNum,
    (sum(che.pnum)/count_hos.inNum)*5 as pnum,
    (sum(che.risknum)/count_hos.inNum)*5 as risknum,
    (sum(che.whnum)/count_hos.inNum)*5 as whnum,
    (sum(che.lsnum)/count_hos.inNum)*5 as lsnum,
    IFNULL( 
                sum( 
                    ( 
                        ( 1*IFNULL(che.oqd,0) 
                        + 2*1*IFNULL(che.tqd,0) 
                        + 1*3*IFNULL(che.otid,0) 
                        + 2*2*IFNULL(che.tbid,0) 
                        + 2*3*IFNULL(che.ttid,0) 
                        + 3*2*IFNULL(che.thbid,0) 
                        + 4*2*IFNULL(che.fbid,0) 
                        ) / 100  
                    ) * IFNULL(che.lsnum,0) 
                ) / IFNULL(sum(che.lsnum),0)
            ,0 ) averageDose, 
    IFNULL(
        sum(IFNULL(che.oqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) omgRate,
    IFNULL(
        sum(IFNULL(che.tqd,0)*che.lsnum/100)/sum(che.lsnum),0
    ) tmgRate,
    IFNULL(
        sum(IFNULL(che.otid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) thmgRate,
    IFNULL(
        sum(IFNULL(che.tbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) fmgRate, 
    IFNULL(
        sum((IFNULL(che.ttid,0)*che.lsnum + IFNULL(che.thbid,0)*che.lsnum)/100)/sum(che.lsnum),0
    ) smgRate, 
    IFNULL(
        sum(IFNULL(che.fbid,0)*che.lsnum/100)/sum(che.lsnum),0
    ) emgRate 
    FROM tbl_chestSurgery_data che, tbl_hospital h, 
    (
        select count(1) as inNum, h.code 
        from tbl_chestSurgery_data che, tbl_hospital h
        WHERE che.createdate between DATE_SUB('2014-06-09', Interval 7 day) and '2014-06-09' 
        and che.hospitalCode = h.code 
        and h.isChestSurgeryAssessed='1' 
        GROUP BY h.code
    ) count_hos 
    WHERE che.createdate between DATE_SUB('2014-06-09', Interval 7 day) and '2014-06-09' 
    and che.hospitalCode = h.code 
    and h.code = count_hos.code
    and h.isChestSurgeryAssessed='1' 
    GROUP BY h.code
) che_data 
right join tbl_hospital h on che_data.code = h.code 
where h.isChestSurgeryAssessed='1';