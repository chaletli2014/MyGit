package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobilePEDDailyData;

public class PediatricsWhPortRowMapper implements RowMapper<MobilePEDDailyData>{
    
    public MobilePEDDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobilePEDDailyData mobilePEDDailyData = new MobilePEDDailyData();
        mobilePEDDailyData.setUserCode(rs.getString("userCode"));
        if( rs.getDouble("portNum") == 0 ){
        	mobilePEDDailyData.setWhPortRate(0);
        }else{
        	mobilePEDDailyData.setWhPortRate(rs.getDouble("lsnum")/rs.getDouble("portNum"));
        }
        return mobilePEDDailyData;
    }
}
