package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MonthlyStatisticsData;

public class MonthlyStatisticsCoreDataRowMapper implements RowMapper<MonthlyStatisticsData>{
    @Override
    public MonthlyStatisticsData mapRow(ResultSet rs, int i) throws SQLException {
    	MonthlyStatisticsData monthlyStatisticsData = new MonthlyStatisticsData();
    	monthlyStatisticsData.setRsd(rs.getString("region"));
    	monthlyStatisticsData.setRsm(rs.getString("rsmRegion"));
    	monthlyStatisticsData.setDsmCode(rs.getString("dsmCode"));
    	monthlyStatisticsData.setDsmName(rs.getString("dsmName"));
    	monthlyStatisticsData.setCoreInRate(rs.getDouble("inRate"));
    	monthlyStatisticsData.setCoreWhRate(rs.getDouble("whRate"));
        return monthlyStatisticsData;
    }
    
}
