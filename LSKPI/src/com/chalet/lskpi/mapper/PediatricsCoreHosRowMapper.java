package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobilePEDDailyData;

public class PediatricsCoreHosRowMapper implements RowMapper<MobilePEDDailyData>{
    
    public MobilePEDDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobilePEDDailyData mobilePEDDailyData = new MobilePEDDailyData();
        mobilePEDDailyData.setUserCode(rs.getString("userCode"));
        if( rs.getDouble("coreHosNum") == 0 ){
        	mobilePEDDailyData.setCoreInRate(0);
        }else{
        	mobilePEDDailyData.setCoreInRate(rs.getDouble("coreInNum")/rs.getDouble("coreHosNum"));
        }
        return mobilePEDDailyData;
    }
}
