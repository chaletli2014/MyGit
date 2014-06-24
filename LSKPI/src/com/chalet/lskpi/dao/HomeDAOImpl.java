package com.chalet.lskpi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.HomeDataRowMapper;
import com.chalet.lskpi.mapper.HomeWeeklyDataRowMapper;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.DateUtils;

@Repository("homeDAO")
public class HomeDAOImpl implements HomeDAO {
    
    @Autowired
    @Qualifier("dataBean")
    private DataBean dataBean;
    
    private Logger logger = Logger.getLogger(HomeDAOImpl.class);
    
    public HomeData getHomeDataByDoctorId(String doctorId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select hd.id, hd.doctorId, hd.salenum, hd.asthmanum, hd.ltenum, hd.lsnum, hd.efnum, hd.ftnum, hd.lttnum ")
        .append(" from tbl_home_data hd ")
        .append(" where hd.doctorId = ? ")
        .append(" and DATE_FORMAT(hd.createdate,'%Y-%m-%d') = curdate()");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{doctorId}, new HomeDataRowMapper());
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
        
        final String sql = "insert into tbl_home_data values(null,?,?,?,?,?,?,?,?,NOW(),NOW())";
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

    public List<HomeWeeklyData> getHomeWeeklyDataOfSales(UserInfo currentUser,Date lastWednesday) throws Exception {
        StringBuffer sql = new StringBuffer();
        String duration = DateUtils.getTheBeginDateOfRefreshDate(lastWednesday)+"-"+DateUtils.getTheEndDateOfRefreshDate(lastWednesday);
        sql.append(" select hd.id, hd.doctorId, hd.salenum, hd.asthmanum, hd.ltenum, hd.lsnum, hd.efnum, hd.ftnum, hd.lttnum ")
        .append(" from tbl_home_data_weekly hdw, tbl_userinfo u, tbl_doctor d, tbl_doctor_history dh, tbl_hospital h ")
        .append(" where ")
        .append(" and duration = ?");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{currentUser.getSuperior(),duration}, new HomeWeeklyDataRowMapper());
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfDSM(UserInfo currentUser,Date lastWednesday) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfRSM(UserInfo currentUser,Date lastWednesday) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfRSD(Date lastWednesday) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
