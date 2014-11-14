package com.chalet.lskpi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.ExportDoctorRowMapper;
import com.chalet.lskpi.mapper.HomeDataExportRowMapper;
import com.chalet.lskpi.mapper.HomeDataRowMapper;
import com.chalet.lskpi.mapper.HomeWeeklyDataRowMapper;
import com.chalet.lskpi.mapper.NoReportDoctorRowMapper;
import com.chalet.lskpi.model.ExportDoctor;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.HomeWeeklyNoReportDr;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;

@Repository("homeDAO")
public class HomeDAOImpl implements HomeDAO {
    
    @Autowired
    @Qualifier("dataBean")
    private DataBean dataBean;
    
    private Logger logger = Logger.getLogger(HomeDAOImpl.class);
    
    public HomeData getHomeDataByDoctorId(String doctorId, Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select hd.id, hd.doctorId, hd.salenum, hd.asthmanum, hd.ltenum, hd.lsnum, hd.efnum, hd.ftnum, hd.lttnum ")
        .append(" from tbl_home_data hd ")
        .append(" where hd.doctorId = ? ")
        .append(" and hd.createdate between ? and ?");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{doctorId,new Timestamp(beginDate.getTime()),new Timestamp(endDate.getTime())}, new HomeDataRowMapper());
    }
    
    public List<HomeData> getHomeDataByDate(Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select hd.id, hd.createdate, h.region, h.rsmRegion, h.code as hospitalCode, h.name as hospitalName, d.name as drName, d.id as doctorId ")
        .append(" , hd.salenum, hd.asthmanum, hd.ltenum, hd.lsnum, hd.efnum, hd.ftnum, hd.lttnum ")
        .append(" , IFNULL((select distinct name from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ),'vacant') as dsmName ")
        .append(" , IFNULL((select distinct name from tbl_userinfo u where u.region = h.rsmRegion and u.superior = h.dsmCode and u.userCode = d.salesCode and u.level='REP' and d.salesCode is not null ),'vacant') as salesName ")
        .append(" from tbl_home_data hd, tbl_doctor d, tbl_hospital h")
        .append(" where hd.doctorid = d.id ")
        .append(" and d.hospitalCode = h.code ")
        .append(" and hd.createdate between ? and ? ")
        .append(" union all ")
        .append(" select hd.id, hd.createdate, h.region, h.rsmRegion, h.code as hospitalCode, h.name as hospitalName, dh.drName, dh.doctorId ")
        .append(" , hd.salenum, hd.asthmanum, hd.ltenum, hd.lsnum, hd.efnum, hd.ftnum, hd.lttnum ")
        .append(" , IFNULL((select distinct name from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ),'vacant') as dsmName ")
        .append(" , IFNULL((select distinct name from tbl_userinfo u where u.region = h.rsmRegion and u.superior = h.dsmCode and u.userCode = dh.salesCode and u.level='REP' and dh.salesCode is not null ),'vacant') as salesName ")
        .append(" from tbl_home_data hd, tbl_doctor_history dh, tbl_hospital h")
        .append(" where hd.doctorid = dh.doctorId ")
        .append(" and dh.hospitalCode = h.code ")
        .append(" and hd.createdate between ? and ? ")
        .append(" order by createdate asc");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{new Timestamp(beginDate.getTime()),new Timestamp(endDate.getTime()),new Timestamp(beginDate.getTime()),new Timestamp(endDate.getTime())}, new HomeDataExportRowMapper());
    }

    public HomeData getHomeDataById(int dataId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select hd.id, hd.doctorId, hd.salenum, hd.asthmanum, hd.ltenum, hd.lsnum, hd.efnum, hd.ftnum, hd.lttnum ")
        .append(" from tbl_home_data hd ")
        .append(" where hd.id=? ");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{dataId}, new HomeDataRowMapper());
    }

    public void insert(final HomeData homeData, final String doctorId) throws Exception {
        logger.info("insert home data");
        
        final String sql = "insert into tbl_home_data(id,doctorId,salenum,asthmanum,ltenum,lsnum,efnum,ftnum,lttnum,createdate,updatedate,hospitalCode,duration) values(null,?,?,?,?,?,?,?,?,date_sub(NOW(),interval 7 day),NOW(),?,concat( DATE_FORMAT( ADDDATE(createdate,-WEEKDAY(NOW())),'%Y.%m.%d'),'-', DATE_FORMAT(ADDDATE(createdate,6-WEEKDAY(NOW())),'%Y.%m.%d')))";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, Integer.parseInt(doctorId));
                ps.setInt(2, homeData.getSalenum());
                ps.setInt(3, homeData.getAsthmanum());
                ps.setInt(4, homeData.getLtenum());
                ps.setInt(5, homeData.getLsnum());
                ps.setInt(6, homeData.getEfnum());
                ps.setInt(7, homeData.getFtnum());
                ps.setInt(8, homeData.getLttnum());
                ps.setString(9, homeData.getHospitalCode());
                return ps;
            }
        }, keyHolder);
        logger.info("returned home data id is "+keyHolder.getKey().intValue());
        
    }

    public void update(HomeData homeData) throws Exception {
        StringBuffer sql = new StringBuffer("update tbl_home_data set ");
        sql.append("updatedate=NOW()");
        sql.append(", salenum=? ");
        sql.append(", asthmanum=? ");
        sql.append(", ltenum=? ");
        sql.append(", lsnum=? ");
        sql.append(", efnum=? ");
        sql.append(", ftnum=? ");
        sql.append(", lttnum=? ");
        sql.append(" where id=? ");
        dataBean.getJdbcTemplate().update(sql.toString(), new Object[]{homeData.getSalenum(),
            homeData.getAsthmanum(),
            homeData.getLtenum(),
            homeData.getLsnum(),
            homeData.getEfnum(),
            homeData.getFtnum(),
            homeData.getLttnum(),
            homeData.getId()});
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfSales(String dsmCode, String region, Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        String lastWeekDuration = DateUtils.populateDuration(beginDate, new Date(endDate.getTime() - 1 * 24 * 60 * 60 * 1000));
        String lastWeek2Duration = DateUtils.populateDuration(new Date(beginDate.getTime() - 7 * 24 * 60 * 60 * 1000), new Date(endDate.getTime() - 8 * 24 * 60 * 60 * 1000));
        logger.info(String.format("get the sales home weekly data,lastDuration is %s, last2Duration is %s", lastWeekDuration,lastWeek2Duration));
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.name ")
        .append(", '' as regionCenterCN ")
        .append(", homeData.reportNum ")
        .append(", ( select count(1) from tbl_doctor_weekly dw1 ")
        .append("   where dw1.salesCode = ui.userCode ")
        .append("   and dw1.duration = ? ")
        .append("   ) as lastWeekDrNum")
        .append(", ( select count(1) from tbl_doctor_weekly dw2 ")
        .append("   where dw2.salesCode = ui.userCode ")
        .append("   and dw2.duration = ? ")
        .append("   ) as lastWeek2DrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2 ")
        .append("   where d2.salesCode = ui.userCode ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", u.name")
        .append(", u.userCode ")
        .append(", count(1) as reportNum ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_FROM)
        .append(" where ( hd.doctorId in ( select d.id from tbl_doctor d where d.salesCode = u.userCode )")
        .append("   or hd.doctorId in ( select dh.doctorId from tbl_doctor_history dh where dh.salesCode = u.userCode ))")
        .append(" and u.superior = ? ")
        .append(" and u.region= ? ")
        .append(" and u.level='REP' ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by u.userCode ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.userCode = homeData.userCode ")
        .append(" where ui.superior = ? and ui.region= ? and ui.level='REP' ");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{
        	lastWeekDuration
        	,lastWeek2Duration
            , dsmCode
            , region
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , dsmCode
            , region}, new HomeWeeklyDataRowMapper());
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfDSM(String region,Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        
        String lastWeekDuration = DateUtils.populateDuration(beginDate, new Date(endDate.getTime() - 1 * 24 * 60 * 60 * 1000));
        String lastWeek2Duration = DateUtils.populateDuration(new Date(beginDate.getTime() - 7 * 24 * 60 * 60 * 1000), new Date(endDate.getTime() - 8 * 24 * 60 * 60 * 1000));
        logger.info(String.format("get the dsm home weekly data,lastDuration is %s, last2Duration is %s", lastWeekDuration,lastWeek2Duration));
        
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.name ")
        .append(", homeData.reportNum ")
        .append(", '' as regionCenterCN ")
        .append(", ( select count(1) from tbl_doctor_weekly dw1, tbl_hospital h1 ")
        .append("   where dw1.hospitalCode = h1.code ")
        .append("   and h1.dsmCode = ui.userCode ")
        .append("   and dw1.duration = ? ")
        .append("   ) as lastWeekDrNum")
        .append(", ( select count(1) from tbl_doctor_weekly dw2, tbl_hospital h2 ")
        .append("   where dw2.hospitalCode = h2.code ")
        .append("   and h2.dsmCode = ui.userCode ")
        .append("   and dw2.duration = ? ")
        .append("   ) as lastWeek2DrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.dsmCode = ui.userCode ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.dsmCode ")
        .append(", count(1) as reportNum ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where hd.hospitalCode = h.code ")
        .append(" and h.rsmRegion = ? ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region, h.rsmRegion, h.dsmCode ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.userCode = homeData.dsmCode ")
        .append(" where ui.region = ? and ui.level='DSM' ");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{
            lastWeekDuration
            ,lastWeek2Duration
            , region
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , region}, new HomeWeeklyDataRowMapper());
    }
    
    public HomeWeeklyData getHomeWeeklyDataOfSingleDSM(String dsmCode, String region,Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        String lastWeekDuration = DateUtils.populateDuration(beginDate, new Date(endDate.getTime() - 1 * 24 * 60 * 60 * 1000));
        String lastWeek2Duration = DateUtils.populateDuration(new Date(beginDate.getTime() - 7 * 24 * 60 * 60 * 1000), new Date(endDate.getTime() - 8 * 24 * 60 * 60 * 1000));
        logger.info(String.format("get the single dsm home weekly data,lastDuration is %s, last2Duration is %s", lastWeekDuration,lastWeek2Duration));
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.name ")
        .append(", '' as regionCenterCN ")
        .append(", homeData.reportNum ")
        .append(", ( select count(1) from tbl_doctor_weekly dw1, tbl_hospital h1 ")
        .append("   where dw1.hospitalCode = h1.code ")
        .append("   and h1.dsmCode = ui.userCode ")
        .append("   and dw1.duration = ? ")
        .append("   ) as lastWeekDrNum")
        .append(", ( select count(1) from tbl_doctor_weekly dw2, tbl_hospital h2 ")
        .append("   where dw2.hospitalCode = h2.code ")
        .append("   and h2.dsmCode = ui.userCode ")
        .append("   and dw2.duration = ? ")
        .append("   ) as lastWeek2DrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.dsmCode = ui.userCode ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.dsmCode ")
        .append(", count(1) as reportNum ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where hd.hospitalCode = h.code ")
        .append(" and h.dsmCode = ? ")
        .append(" and h.rsmRegion = ? ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region, h.rsmRegion, h.dsmCode ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.userCode = homeData.dsmCode ")
        .append(" where ui.userCode = ? and ui.region = ? and ui.level='DSM' ");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{
            lastWeekDuration
            , lastWeek2Duration
            , dsmCode
            , region
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , dsmCode
            , region}, new HomeWeeklyDataRowMapper());
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfRSM(String regionCenter,Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        String lastWeekDuration = DateUtils.populateDuration(beginDate, new Date(endDate.getTime() - 1 * 24 * 60 * 60 * 1000));
        String lastWeek2Duration = DateUtils.populateDuration(new Date(beginDate.getTime() - 7 * 24 * 60 * 60 * 1000), new Date(endDate.getTime() - 8 * 24 * 60 * 60 * 1000));
        logger.info(String.format("get the rsm home weekly data,lastDuration is %s, last2Duration is %s", lastWeekDuration,lastWeek2Duration));
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.region as name ")
        .append(", homeData.reportNum ")
        .append(", (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN ")
        .append(", ( select count(1) from tbl_doctor_weekly dw1, tbl_hospital h1 ")
        .append("   where dw1.hospitalCode = h1.code ")
        .append("   and h1.rsmRegion = ui.region ")
        .append("   and dw1.duration = ? ")
        .append("   ) as lastWeekDrNum")
        .append(", ( select count(1) from tbl_doctor_weekly dw2, tbl_hospital h2 ")
        .append("   where dw2.hospitalCode = h2.code ")
        .append("   and h2.rsmRegion = ui.region ")
        .append("   and dw2.duration = ? ")
        .append("   ) as lastWeek2DrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.rsmRegion = ui.region ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.rsmRegion")
        .append(", count(1) as reportNum ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where hd.hospitalCode = h.code ")
        .append(" and h.region = ? ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region, h.rsmRegion ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.region = homeData.rsmRegion ")
        .append(" where ui.regionCenter = ? and ui.level='RSM' ");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{
            lastWeekDuration
            , lastWeek2Duration
            , regionCenter
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , regionCenter}, new HomeWeeklyDataRowMapper());
    }
    
    public HomeWeeklyData getHomeWeeklyDataOfSingleRSM(String region,Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        String lastWeekDuration = DateUtils.populateDuration(beginDate, new Date(endDate.getTime() - 1 * 24 * 60 * 60 * 1000));
        String lastWeek2Duration = DateUtils.populateDuration(new Date(beginDate.getTime() - 7 * 24 * 60 * 60 * 1000), new Date(endDate.getTime() - 8 * 24 * 60 * 60 * 1000));
        logger.info(String.format("get the single rsm home weekly data,lastDuration is %s, last2Duration is %s", lastWeekDuration,lastWeek2Duration));
        
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.region as name ")
        .append(", '' as regionCenterCN ")
        .append(", homeData.reportNum ")
        .append(", ( select count(1) from tbl_doctor_weekly dw1, tbl_hospital h1 ")
        .append("   where dw1.hospitalCode = h1.code ")
        .append("   and h1.rsmRegion = ui.region ")
        .append("   and dw1.duration = ? ")
        .append("   ) as lastWeekDrNum")
        .append(", ( select count(1) from tbl_doctor_weekly dw2, tbl_hospital h2 ")
        .append("   where dw2.hospitalCode = h2.code ")
        .append("   and h2.rsmRegion = ui.region ")
        .append("   and dw2.duration = ? ")
        .append("   ) as lastWeek2DrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.rsmRegion = ui.region ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.rsmRegion")
        .append(", count(1) as reportNum ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where hd.hospitalCode = h.code ")
        .append(" and h.rsmRegion = ? ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region, h.rsmRegion ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.region = homeData.rsmRegion ")
        .append(" where ui.region = ? and ui.level='RSM' ");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{
            lastWeekDuration
            , lastWeek2Duration
            , region
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , region}, new HomeWeeklyDataRowMapper());
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfRSD(Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        String lastWeekDuration = DateUtils.populateDuration(beginDate, new Date(endDate.getTime() - 1 * 24 * 60 * 60 * 1000));
        String lastWeek2Duration = DateUtils.populateDuration(new Date(beginDate.getTime() - 7 * 24 * 60 * 60 * 1000), new Date(endDate.getTime() - 8 * 24 * 60 * 60 * 1000));
        logger.info(String.format("get the single rsm home weekly data,lastDuration is %s, last2Duration is %s", lastWeekDuration,lastWeek2Duration));
        
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as name ")
        .append(", '' as regionCenterCN ")
        .append(", homeData.reportNum ")
        .append(", ( select count(1) from tbl_doctor_weekly dw1, tbl_hospital h1 ")
        .append("   where dw1.hospitalCode = h1.code ")
        .append("   and h1.region = ui.regionCenter ")
        .append("   and dw1.duration = ? ")
        .append("   ) as lastWeekDrNum")
        .append(", ( select count(1) from tbl_doctor_weekly dw2, tbl_hospital h2 ")
        .append("   where dw2.hospitalCode = h2.code ")
        .append("   and h2.region = ui.regionCenter ")
        .append("   and dw2.duration = ? ")
        .append("   ) as lastWeek2DrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.region = ui.regionCenter ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.region")
        .append(", count(1) as reportNum ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where hd.hospitalCode = h.code ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.regionCenter = homeData.region ")
        .append(" where ui.level='RSD' ");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{
            lastWeekDuration
            , lastWeek2Duration
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())}, new HomeWeeklyDataRowMapper());
    }
    
    public HomeWeeklyData getHomeWeeklyDataOfSingleRSD(String regionCenter, Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        String lastWeekDuration = DateUtils.populateDuration(beginDate, new Date(endDate.getTime() - 1 * 24 * 60 * 60 * 1000));
        String lastWeek2Duration = DateUtils.populateDuration(new Date(beginDate.getTime() - 7 * 24 * 60 * 60 * 1000), new Date(endDate.getTime() - 8 * 24 * 60 * 60 * 1000));
        logger.info(String.format("get the single rsm home weekly data,lastDuration is %s, last2Duration is %s", lastWeekDuration,lastWeek2Duration));
        
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", (select property_value from tbl_property where property_name=ui.regionCenter ) as name ")
        .append(", '' as regionCenterCN ")
        .append(", homeData.reportNum ")
        .append(", ( select count(1) from tbl_doctor_weekly dw1, tbl_hospital h1 ")
        .append("   where dw1.hospitalCode = h1.code ")
        .append("   and h1.region = ui.regionCenter ")
        .append("   and dw1.duration = ? ")
        .append("   ) as lastWeekDrNum")
        .append(", ( select count(1) from tbl_doctor_weekly dw2, tbl_hospital h2 ")
        .append("   where dw2.hospitalCode = h2.code ")
        .append("   and h2.region = ui.regionCenter ")
        .append("   and dw2.duration = ? ")
        .append("   ) as lastWeek2DrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.region = ui.regionCenter ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.region")
        .append(", count(1) as reportNum ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where hd.hospitalCode = h.code ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" and h.region = ? ")
        .append(" group by h.region ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.regionCenter = homeData.region ")
        .append(" where ui.level='RSD' and ui.regionCenter = ?");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{
            lastWeekDuration
            , lastWeek2Duration
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , regionCenter
            , regionCenter}, new HomeWeeklyDataRowMapper());
    }
    
    public HomeWeeklyData getHomeWeeklyDataOfCountory(Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        String lastWeekDuration = DateUtils.populateDuration(beginDate, new Date(endDate.getTime() - 1 * 24 * 60 * 60 * 1000));
        String lastWeek2Duration = DateUtils.populateDuration(new Date(beginDate.getTime() - 7 * 24 * 60 * 60 * 1000), new Date(endDate.getTime() - 8 * 24 * 60 * 60 * 1000));
        logger.info(String.format("get the single rsm home weekly data,lastDuration is %s, last2Duration is %s", lastWeekDuration,lastWeek2Duration));
        
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", '全国' as name ")
        .append(", count(1) as reportNum ")
        .append(", '' as regionCenterCN ")
        .append(", ( select count(1) from tbl_doctor_weekly dw1 ")
        .append("   where dw1.duration = ? ")
        .append("   ) as lastWeekDrNum")
        .append(", ( select count(1) from tbl_doctor_weekly dw2 ")
        .append("   where dw2.duration = ? ")
        .append("   ) as lastWeek2DrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2 ")
        .append("   ) as totalDrNum ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_FROM_HOME_ONLY)
        .append(" where hd.createdate between ? and ? ");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{
            lastWeekDuration
            , lastWeek2Duration
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())}, new HomeWeeklyDataRowMapper());
    }
    
    public List<HomeWeeklyNoReportDr> getWeeklyNoReportDr(Date beginDate, Date endDate, String duration) throws Exception {
    	StringBuffer sql = new StringBuffer();
    	logger.info(String.format("get the non-reported doctors in the week from %s to %s", beginDate,endDate));
    	
    	sql.append(" select h.region ")
    	.append(",h.rsmRegion ")
    	.append(",(select u.name from tbl_userinfo u where u.userCode = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region) as dsmName ")
    	.append(",case when dw.salesCode is null or dw.salesCode = '' then 'N/A' else dw.salesCode end as salesCode ")
    	.append(",IFNULL((select u.name from tbl_userinfo u where u.userCode = dw.salesCode and u.superior = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region),'N/A') as salesName ")
    	.append(",dw.hospitalCode ")
    	.append(",h.name as hospitalName")
    	.append(",concat(dw.hospitalCode,dw.code) as doctorCode ")
    	.append(",dw.doctorId ")
    	.append(",dw.doctorName ")
    	.append("from tbl_doctor_weekly dw, tbl_hospital h ")
    	.append("where not exists( ")
    	.append("	select 1 ")
    	.append("	from tbl_home_data hd ")
    	.append("	where hd.createdate between ? and ? ")
    	.append("	and hd.doctorId = dw.doctorId ")
    	.append(") ")
    	.append("and dw.hospitalCode = h.code ")
    	.append("and dw.duration = ? ")
    	.append("order by region,rsmRegion,dsmName,salesName,doctorName ");
    	return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{
    		new Timestamp(beginDate.getTime())
    		, new Timestamp(endDate.getTime())
    		,duration}, new NoReportDoctorRowMapper());
    }

    public List<ExportDoctor> getAllDoctors() throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select h.region, h.rsmRegion  ")
        .append(" , h.code as hospitalCode, h.name as hospitalName ")
        .append(" , d.code as doctorCode,  d.name as doctorName, d.id ")
        .append(" , case when h.dsmCode is null then '#N/A' else ( ")
        .append("       select u.name from tbl_userinfo u ")
        .append("       where u.region = h.rsmRegion ")
        .append("       and u.regionCenter = h.region and u.userCode=h.dsmCode and u.level='DSM') ")
        .append("   end dsmName ")
        .append(" , case when d.salesCode is null or d.salesCode='' then '#N/A' else ( ")
        .append("       select u.name from tbl_userinfo u ")
        .append("       where u.region = h.rsmRegion ")
        .append("       and u.regionCenter = h.region and u.superior = h.dsmCode and u.userCode=d.salesCode and u.level='REP') ")
        .append("   end salesName ")
        .append(" , case when d.salesCode is null or d.salesCode='' then '#N/A' else d.salesCode end salesCode ")
        .append(" from tbl_doctor d, tbl_hospital h  ")
        .append(" where d.hospitalCode = h.code  ")
        .append(" order by h.region, h.rsmRegion, h.code, d.code ");
        return dataBean.getJdbcTemplate().query(sql.toString(), new ExportDoctorRowMapper());
    }

    public ReportProcessData getSalesSelfReportProcess(String telephone) throws Exception {
        StringBuffer drNumSQL = new StringBuffer("");
        drNumSQL.append("select count(1) as drNum ")
        .append(" from tbl_userinfo u, tbl_doctor d ")
        .append(" where d.salesCode = u.userCode ")
        .append(" and u.telephone = ? ");
        
        int drNum = dataBean.getJdbcTemplate().queryForInt(drNumSQL.toString(), telephone);
        
        Date startDate = DateUtils.getHomeCollectionBegionDate(new Date());
        Date endDate = new Date(startDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        StringBuffer inNumSQL = new StringBuffer("");
        inNumSQL.append("select count(1) as hosNum ")
        .append(" from tbl_userinfo u, tbl_doctor d, tbl_home_data hd ")
        .append(" where d.salesCode = u.userCode ")
        .append(" and hd.doctorId = d.id ")
        .append(" and hd.createdate between ? and ? ")
        .append(" and u.telephone = ? ");
        
        int inNum = dataBean.getJdbcTemplate().queryForInt(inNumSQL.toString(), new Object[]{new Timestamp(startDate.getTime()),new Timestamp(endDate.getTime()),telephone});
        
        ReportProcessData homeProcess = new ReportProcessData();
        homeProcess.setHosNum(drNum);
        homeProcess.setValidInNum(inNum);
        if( drNum == 0 ){
            homeProcess.setCurrentInRate(0);
        }else{
            homeProcess.setCurrentInRate((double)inNum/(double)drNum);
        }
        return homeProcess;
    }

    public ReportProcessData getDSMSelfReportProcess(String telephone) throws Exception {
        StringBuffer drNumSQL = new StringBuffer("");
        drNumSQL.append("select count(1) as drNum ")
        .append(" from tbl_userinfo u, tbl_hospital h, tbl_doctor d ")
        .append(" where h.dsmCode = u.userCode ")
        .append(" and d.hospitalCode = h.code ")
        .append(" and u.telephone = ? ");
        
        int drNum = dataBean.getJdbcTemplate().queryForInt(drNumSQL.toString(), telephone);
        
        Date startDate = DateUtils.getHomeCollectionBegionDate(new Date());
        Date endDate = new Date(startDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        StringBuffer inNumSQL = new StringBuffer("");
        inNumSQL.append("select count(1) as hosNum ")
        .append(" from tbl_userinfo u, tbl_hospital h, tbl_home_data hd ")
        .append(" where h.dsmCode = u.userCode ")
        .append(" and hd.hospitalCode = h.code ")
        .append(" and hd.createdate between ? and ? ")
        .append(" and u.telephone = ? ");
        
        int inNum = dataBean.getJdbcTemplate().queryForInt(inNumSQL.toString(), new Object[]{new Timestamp(startDate.getTime()),new Timestamp(endDate.getTime()),telephone});
        
        ReportProcessData homeProcess = new ReportProcessData();
        homeProcess.setHosNum(drNum);
        homeProcess.setValidInNum(inNum);
        if( drNum == 0 ){
            homeProcess.setCurrentInRate(0);
        }else{
            homeProcess.setCurrentInRate((double)inNum/(double)drNum);
        }
        return homeProcess;
    }

    public ReportProcessData getRSMSelfReportProcess(String telephone) throws Exception {
        StringBuffer drNumSQL = new StringBuffer("");
        drNumSQL.append("select count(1) as drNum ")
        .append(" from tbl_userinfo u, tbl_hospital h, tbl_doctor d ")
        .append(" where h.rsmRegion = u.region ")
        .append(" and d.hospitalCode = h.code ")
        .append(" and u.telephone = ? ");
        
        int drNum = dataBean.getJdbcTemplate().queryForInt(drNumSQL.toString(), telephone);
        
        Date startDate = DateUtils.getHomeCollectionBegionDate(new Date());
        Date endDate = new Date(startDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        StringBuffer inNumSQL = new StringBuffer("");
        inNumSQL.append("select count(1) as hosNum ")
        .append(" from tbl_userinfo u, tbl_hospital h, tbl_home_data hd ")
        .append(" where h.rsmRegion = u.region ")
        .append(" and hd.hospitalCode = h.code ")
        .append(" and hd.createdate between ? and ? ")
        .append(" and u.telephone = ? ");
        
        int inNum = dataBean.getJdbcTemplate().queryForInt(inNumSQL.toString(), new Object[]{new Timestamp(startDate.getTime()),new Timestamp(endDate.getTime()),telephone});
        
        ReportProcessData homeProcess = new ReportProcessData();
        homeProcess.setHosNum(drNum);
        homeProcess.setValidInNum(inNum);
        if( drNum == 0 ){
            homeProcess.setCurrentInRate(0);
        }else{
            homeProcess.setCurrentInRate((double)inNum/(double)drNum);
        }
        return homeProcess;
    }

	@Override
	public void backupDoctors(String duration) throws Exception {
        
        StringBuffer backupSQL = new StringBuffer("");
        backupSQL.append("insert into tbl_doctor_weekly(id,duration,code,hospitalCode,salesCode,createdate,modifydate,doctorId,doctorName) ")
        .append(" select ")
        .append(" null,")
        .append(" ?, ")
        .append(" code, ")
        .append(" hospitalCode, ")
        .append(" salesCode, ")
        .append(" createdate, ")
        .append(" modifydate, ")
        .append(" id, ")
        .append(" name ")
        .append(" from tbl_doctor ");
        
		int rowNum = dataBean.getJdbcTemplate().update(backupSQL.toString(), duration);
		logger.info(String.format("backup doctor done, number is %s", rowNum));
	}

	@Override
	public boolean isAlreadyBackup(String duration) throws Exception {
		StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) from tbl_doctor_weekly where duration = ?");
        int rowNum = dataBean.getJdbcTemplate().queryForInt(sb.toString(), duration);
        return rowNum>0;
	}
	
	@Override
	public void removeOldDoctors(String duration) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" delete from tbl_doctor_weekly where duration = ?");
		int rowNum = dataBean.getJdbcTemplate().update(sb.toString(), duration);
		logger.info(String.format("remove old doctor done, number is %s", rowNum));
	}
}
