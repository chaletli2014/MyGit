package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MonthlyInRateData;

public class MonthlyInRateDataRowMapper implements RowMapper<MonthlyInRateData>{
    @Override
    public MonthlyInRateData mapRow(ResultSet rs, int i) throws SQLException {
    	MonthlyInRateData monthlyInRateData = new MonthlyInRateData();
    	monthlyInRateData.setDuration(rs.getString("duration"));
    	monthlyInRateData.setRsd(rs.getString("region"));
    	monthlyInRateData.setRsm(rs.getString("rsmRegion"));
    	monthlyInRateData.setPedInRate(rs.getDouble("pedInRate"));
    	monthlyInRateData.setResInRate(rs.getDouble("resInRate"));
        return monthlyInRateData;
    }
    
}
