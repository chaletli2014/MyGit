package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobileRESDailyData;

public class RespirologyMobileRowMapper implements RowMapper<MobileRESDailyData>{
    
    public MobileRESDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobileRESDailyData mobileRESDailyData = new MobileRESDailyData();
        mobileRESDailyData.setUserName(rs.getString("name"));
        mobileRESDailyData.setUserCode(rs.getString("userCode"));
        mobileRESDailyData.setHosNum(rs.getInt("hosNum"));
        mobileRESDailyData.setInNum(rs.getInt("inNum"));
        mobileRESDailyData.setPatNum(rs.getInt("pNum"));
        mobileRESDailyData.setWhNum(rs.getInt("whNum"));
        mobileRESDailyData.setLsNum(rs.getInt("lsNum"));
        mobileRESDailyData.setAverageDose(rs.getDouble("averageDose"));
        mobileRESDailyData.setOmgRate(rs.getDouble("omgRate"));
        mobileRESDailyData.setTmgRate(rs.getDouble("tmgRate"));
        mobileRESDailyData.setThmgRate(rs.getDouble("thmgRate"));
        mobileRESDailyData.setFmgRate(rs.getDouble("fmgRate"));
        mobileRESDailyData.setSmgRate(rs.getDouble("smgRate"));
        mobileRESDailyData.setEmgRate(rs.getDouble("emgRate"));
        mobileRESDailyData.setRegionCenterCN(rs.getString("regionCenterCN"));
        return mobileRESDailyData;
    }
}