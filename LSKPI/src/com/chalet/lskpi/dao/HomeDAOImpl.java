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

import com.chalet.lskpi.mapper.HomeDataExportRowMapper;
import com.chalet.lskpi.mapper.HomeDataRowMapper;
import com.chalet.lskpi.mapper.HomeWeeklyDataRowMapper;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.utils.DataBean;
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
        sql.append(" select hd.id, hd.createdate, h.region, h.rsmRegion, h.code as hospitalCode, h.name as hospitalName, d.name as drName ")
        .append(" , hd.salenum, hd.asthmanum, hd.ltenum, hd.lsnum, hd.efnum, hd.ftnum, hd.lttnum ")
        .append(" , IFNULL((select name from tbl_userinfo u where u.userCode = h.dsmCode and u.level='DSM' ),'vacant') as dsmName ")
        .append(" , IFNULL((select name from tbl_userinfo u where u.userCode = d.salesCode and u.level='REP' and d.salesCode is not null ),'vacant') as salesName ")
        .append(" from tbl_home_data hd, tbl_doctor d, tbl_hospital h")
        .append(" where hd.doctorid = d.id ")
        .append(" and d.hospitalCode = h.code ")
        .append(" and hd.createdate between ? and ? ")
        .append(" union all ")
        .append(" select hd.id, hd.createdate, h.region, h.rsmRegion, h.code as hospitalCode, h.name as hospitalName, dh.drName ")
        .append(" , hd.salenum, hd.asthmanum, hd.ltenum, hd.lsnum, hd.efnum, hd.ftnum, hd.lttnum ")
        .append(" , IFNULL((select name from tbl_userinfo u where u.userCode = h.dsmCode and u.level='DSM' ),'vacant') as dsmName ")
        .append(" , IFNULL((select name from tbl_userinfo u where u.userCode = dh.salesCode and u.level='REP' and dh.salesCode is not null ),'vacant') as salesName ")
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
        
        final String sql = "insert into tbl_home_data values(null,?,?,?,?,?,?,?,?,date_sub(NOW(),interval 7 day),NOW())";
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
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.name ")
        .append(", ( select count(1) from tbl_doctor d2 ")
        .append("   where d2.salesCode = ui.userCode ")
        .append("   and d2.createdate between ? and ? ")
        .append("   ) as newDrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2 ")
        .append("   where d2.salesCode = ui.userCode ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", u.name")
        .append(", u.userCode ")
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
            new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , dsmCode
            , region
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , dsmCode
            , region}, new HomeWeeklyDataRowMapper());
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfDSM(String region,Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.name ")
        .append(", ( select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.dsmCode = ui.userCode ")
        .append("   and d2.createdate between ? and ? ")
        .append("   ) as newDrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.dsmCode = ui.userCode ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.dsmCode ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where ( hd.doctorId in ( select d.id from tbl_doctor d where d.hospitalCode = h.code )")
        .append("   or hd.doctorId in ( select dh.doctorId from tbl_doctor_history dh where dh.hospitalCode = h.code ) )")
        .append(" and h.rsmRegion = ? ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region, h.rsmRegion, h.dsmCode ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.userCode = homeData.dsmCode ")
        .append(" where ui.region = ? and ui.level='DSM' ");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{
            new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , region
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , region}, new HomeWeeklyDataRowMapper());
    }
    
    public HomeWeeklyData getHomeWeeklyDataOfSingleDSM(String dsmCode, String region,Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.name ")
        .append(", ( select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.dsmCode = ui.userCode ")
        .append("   and d2.createdate between ? and ? ")
        .append("   ) as newDrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.dsmCode = ui.userCode ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.dsmCode ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where ( hd.doctorId in ( select d.id from tbl_doctor d where d.hospitalCode = h.code )")
        .append("   or hd.doctorId in ( select dh.doctorId from tbl_doctor_history dh where dh.hospitalCode = h.code ) )")
        .append(" and h.dsmCode = ? ")
        .append(" and h.rsmRegion = ? ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region, h.rsmRegion, h.dsmCode ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.userCode = homeData.dsmCode ")
        .append(" where ui.userCode = ? and ui.region = ? and ui.level='DSM' ");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{
            new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , dsmCode
            , region
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , dsmCode
            , region}, new HomeWeeklyDataRowMapper());
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfRSM(String regionCenter,Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.region as name ")
        .append(", ( select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.rsmRegion = ui.region ")
        .append("   and d2.createdate between ? and ? ")
        .append("   ) as newDrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.rsmRegion = ui.region ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.rsmRegion")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where ( hd.doctorId in ( select d.id from tbl_doctor d where d.hospitalCode = h.code )")
        .append("   or hd.doctorId in ( select dh.doctorId from tbl_doctor_history dh where dh.hospitalCode = h.code ) )")
        .append(" and h.region = ? ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region, h.rsmRegion ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.region = homeData.rsmRegion ")
        .append(" where ui.regionCenter = ? and ui.level='RSM' ");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{
            new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , regionCenter
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , regionCenter}, new HomeWeeklyDataRowMapper());
    }
    
    public HomeWeeklyData getHomeWeeklyDataOfSingleRSM(String region,Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.region as name ")
        .append(", ( select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.rsmRegion = ui.region ")
        .append("   and d2.createdate between ? and ? ")
        .append("   ) as newDrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.rsmRegion = ui.region ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.rsmRegion")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where ( hd.doctorId in ( select d.id from tbl_doctor d where d.hospitalCode = h.code )")
        .append("   or hd.doctorId in ( select dh.doctorId from tbl_doctor_history dh where dh.hospitalCode = h.code ) )")
        .append(" and h.rsmRegion = ? ")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region, h.rsmRegion ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.region = homeData.rsmRegion ")
        .append(" where ui.region = ? and ui.level='RSM' ");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{
            new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , region
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , region}, new HomeWeeklyDataRowMapper());
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfRSD(Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.regionCenter as name ")
        .append(", ( select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.region = ui.regionCenter ")
        .append("   and d2.createdate between ? and ? ")
        .append("   ) as newDrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.region = ui.regionCenter ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.region")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where ( hd.doctorId in ( select d.id from tbl_doctor d where d.hospitalCode = h.code )")
        .append("   or hd.doctorId in ( select dh.doctorId from tbl_doctor_history dh where dh.hospitalCode = h.code ) )")
        .append(" and hd.createdate between ? and ? ") 
        .append(" group by h.region ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.regionCenter = homeData.region ")
        .append(" where ui.level='RSD' ");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{
            new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())}, new HomeWeeklyDataRowMapper());
    }
    
    public HomeWeeklyData getHomeWeeklyDataOfSingleRSD(String regionCenter, Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SELECTION)
        .append(", ui.regionCenter as name ")
        .append(", ( select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.region = ui.regionCenter ")
        .append("   and d2.createdate between ? and ? ")
        .append("   ) as newDrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2, tbl_hospital h2 ")
        .append("   where d2.hospitalCode = h2.code ")
        .append("   and h2.region = ui.regionCenter ")
        .append("   ) as totalDrNum ")
        .append("from ( ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", h.region")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_3_FROM)
        .append(" where ( hd.doctorId in ( select d.id from tbl_doctor d where d.hospitalCode = h.code )")
        .append("   or hd.doctorId in ( select dh.doctorId from tbl_doctor_history dh where dh.hospitalCode = h.code ) )")
        .append(" and hd.createdate between ? and ? ") 
        .append(" and h.region = ? ")
        .append(" group by h.region ")
        .append(") homeData")
        .append(" right join tbl_userinfo ui on ui.regionCenter = homeData.region ")
        .append(" where ui.level='RSD' and ui.regionCenter = ?");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{
            new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , regionCenter
            , regionCenter}, new HomeWeeklyDataRowMapper());
    }
    
    public HomeWeeklyData getHomeWeeklyDataOfCountory(Date beginDate, Date endDate) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(LsAttributes.SQL_HOME_WEEKLY_DATA_SUB_SELECTION)
        .append(", '全国' as name ")
        .append(", ( select count(1) from tbl_doctor d2 ")
        .append("   where d2.createdate between ? and ? ")
        .append("   ) as newDrNum")
        .append(", (")
        .append("   select count(1) from tbl_doctor d2 ")
        .append("   ) as totalDrNum ")
        .append(LsAttributes.SQL_HOME_WEEKLY_DATA_FROM_HOME_ONLY)
        .append(" where hd.createdate between ? and ? ");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{
            new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())
            , new Timestamp(beginDate.getTime())
            , new Timestamp(endDate.getTime())}, new HomeWeeklyDataRowMapper());
    }
}
