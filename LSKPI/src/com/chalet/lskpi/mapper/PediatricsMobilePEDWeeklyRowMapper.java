package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobilePEDDailyData;


public class PediatricsMobilePEDWeeklyRowMapper implements RowMapper<MobilePEDDailyData>{
    
    public MobilePEDDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobilePEDDailyData mobilePEDDailyData = new MobilePEDDailyData();
        mobilePEDDailyData.setUserName(rs.getString("name"));
        mobilePEDDailyData.setHosNum(rs.getInt("hosNum"));
        mobilePEDDailyData.setInNum(rs.getInt("inNum"));
        mobilePEDDailyData.setPatNum(rs.getInt("pNum"));
        mobilePEDDailyData.setWhNum(rs.getInt("whNum"));
        mobilePEDDailyData.setLsNum(rs.getInt("lsNum"));
       // mobilePEDDailyData.setInRate(rs.getDouble("inRate"));
        mobilePEDDailyData.setWhRate(rs.getDouble("whRate"));
        mobilePEDDailyData.setAverageDose(rs.getDouble("averageDose"));
        mobilePEDDailyData.setHmgRate(rs.getDouble("hmgRate"));
        mobilePEDDailyData.setOmgRate(rs.getDouble("omgRate"));
        mobilePEDDailyData.setTmgRate(rs.getDouble("tmgRate"));
        mobilePEDDailyData.setFmgRate(rs.getDouble("fmgRate"));
        mobilePEDDailyData.setRegionCenterCN(rs.getString("regionCenterCN"));
        mobilePEDDailyData.setRegion(rs.getString("region"));
        mobilePEDDailyData.setWhbwnum(rs.getInt("whbwnum"));
        mobilePEDDailyData.setBlRate(rs.getDouble("blRate"));
        mobilePEDDailyData.setWhdays(rs.getDouble("whdays_emerging"));
        if( rs.getInt("hosNum") != 0 ){
        	mobilePEDDailyData.setInRate(rs.getDouble("inNum")/rs.getDouble("hosNum"));
        }else{
        	mobilePEDDailyData.setInRate(0);
        }
        return mobilePEDDailyData;
    }
}
