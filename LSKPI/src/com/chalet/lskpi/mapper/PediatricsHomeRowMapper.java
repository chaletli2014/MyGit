package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobilePEDDailyData;

public class PediatricsHomeRowMapper implements RowMapper<MobilePEDDailyData>{
    
    public MobilePEDDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobilePEDDailyData mobilePEDDailyData = new MobilePEDDailyData();
        mobilePEDDailyData.setUserName(rs.getString("name"));
        mobilePEDDailyData.setHosNum(rs.getInt("hosNum"));
        mobilePEDDailyData.setInNum(rs.getInt("inNum"));
        mobilePEDDailyData.setRegionCenterCN(rs.getString("regionCenterCN"));
        mobilePEDDailyData.setRegion(rs.getString("region"));
        
        mobilePEDDailyData.setHomeWhEmergingNum1(rs.getInt("homeWhNum1"));
        mobilePEDDailyData.setHomeWhEmergingAverDays(rs.getDouble("averDays")); 
        mobilePEDDailyData.setHomeWhEmergingNum4(rs.getInt("homeWhNum4"));
        
        if( rs.getInt("hosNum") != 0 ){
        	mobilePEDDailyData.setInRate(rs.getDouble("inNum")/rs.getDouble("hosNum"));
        }else{
        	mobilePEDDailyData.setInRate(0);
        }
        
        return mobilePEDDailyData;
    }
}
