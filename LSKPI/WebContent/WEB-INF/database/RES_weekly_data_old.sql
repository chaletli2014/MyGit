insert into tbl_respirology_data_weekly(id,duration,hospitalName,hospitalCode  
  ,innum,pnum,aenum,whnum,lsnum   
  ,xbknum,xbk1num,xbk2num,xbk3num   
  ,averageDose,omgRate,tmgRate,thmgRate,fmgRate,smgRate,emgRate 
  ,saleCode,dsmCode,rsmRegion,region,updatedate,date_YYYY,date_MM ) 
 select  
 null, 
  CONCAT(DATE_FORMAT(DATE_SUB( '2015-04-19', Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT( '2015-04-19','%Y.%m.%d')) as duration,  
 h.name,  
 h.code,  
 IFNULL(rd_data.innum,0),  
 IFNULL(rd_data.pnum,0), 
 IFNULL(rd_data.aenum,0), 
 IFNULL(rd_data.whnum,0),  
 IFNULL(rd_data.lsnum,0),  
 IFNULL(rd_data.xbknum,0),  
 IFNULL(rd_data.xbk1num,0),  
 IFNULL(rd_data.xbk2num,0),  
 IFNULL(rd_data.xbk3num,0),
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
 now(),   
 Year(DATE_SUB( '2015-04-19', Interval 6 day)),   
 Month(DATE_SUB( '2015-04-19', Interval 6 day))   
 from (  
    SELECT  
    h.code,  
    count_hos.inNum,  
    IFNULL((sum(rd.pnum)/count_hos.inNum)*5,0) as pnum,  
    IFNULL((sum(rd.aenum)/count_hos.inNum)*5,0) as aenum,  
    IFNULL((sum(rd.whnum)/count_hos.inNum)*5,0) as whnum,  
    IFNULL((sum(rd.lsnum)/count_hos.inNum)*5,0) as lsnum,  
    IFNULL((sum(rd.xbknum)/count_hos.inNum)*5,0) as xbknum,  
    IFNULL((sum(rd.xbk1num)/count_hos.inNum)*5,0) as xbk1num,  
    IFNULL((sum(rd.xbk2num)/count_hos.inNum)*5,0) as xbk2num,  
    IFNULL((sum(rd.xbk3num)/count_hos.inNum)*5,0) as xbk3num,  
    IFNULL(  
        sum(  
            ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 )* IFNULL(rd.lsnum,0)  
        ) / IFNULL(sum(rd.lsnum),0) ,0 ) averageDose,  
    IFNULL( sum(IFNULL(rd.oqd,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) omgRate,  
    IFNULL( sum(IFNULL(rd.tqd,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) tmgRate,  
    IFNULL( sum(IFNULL(rd.otid,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) thmgRate,  
    IFNULL( sum(IFNULL(rd.tbid,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) fmgRate,  
    IFNULL( sum((IFNULL(rd.ttid,0)*rd.lsnum + IFNULL(rd.thbid,0)*rd.lsnum)/100)/sum(rd.lsnum),0 ) smgRate,  
    IFNULL( sum(IFNULL(rd.fbid,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) emgRate  
    FROM tbl_respirology_data rd, tbl_hospital h,  
    (    
        select count(1) as inNum, h.code  
        from tbl_respirology_data rd, tbl_hospital h  
        WHERE rd.createdate between DATE_SUB( '2015-04-19', Interval 6 day) and DATE_ADD( '2015-04-19', Interval 1 day)  
        and rd.hospitalName = h.name  
        and (h.isResAssessed='1' or h.isRe2='1')  
        GROUP BY h.code  
    ) count_hos  
    WHERE rd.createdate between DATE_SUB( '2015-04-19', Interval 6 day) and DATE_ADD( '2015-04-19', Interval 1 day)  
    and rd.hospitalName = h.name  
    and h.code = count_hos.code 
    and (h.isResAssessed='1' or h.isRe2='1')  
    GROUP BY h.code  
 ) rd_data  
 right join tbl_hospital h on rd_data.code = h.code  
 where h.isResAssessed='1' or h.isRe2='1'  ;