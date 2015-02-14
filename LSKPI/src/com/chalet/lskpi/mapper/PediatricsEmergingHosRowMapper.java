package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobilePEDDailyData;

public class PediatricsEmergingHosRowMapper implements RowMapper<MobilePEDDailyData>{
    
    public MobilePEDDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobilePEDDailyData mobilePEDDailyData = new MobilePEDDailyData();
        mobilePEDDailyData.setUserCode(rs.getString("userCode"));
        if( rs.getDouble("emergingPNum") == 0 ){
        	mobilePEDDailyData.setEmergingWhRate(0);
        }else{
        	mobilePEDDailyData.setEmergingWhRate(rs.getDouble("emergingLsNum")/rs.getDouble("emergingPNum"));
        }
        return mobilePEDDailyData;
    }
}
