package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MonthlyStatisticsData;

public class MonthlyStatisticsEmergingDataRowMapper implements RowMapper<MonthlyStatisticsData>{
	Logger logger = Logger.getLogger(MonthlyStatisticsEmergingDataRowMapper.class);
	
    @Override
    public MonthlyStatisticsData mapRow(ResultSet rs, int i) throws SQLException {
    	MonthlyStatisticsData monthlyStatisticsData = new MonthlyStatisticsData();
    	monthlyStatisticsData.setRsd(rs.getString("region"));
    	monthlyStatisticsData.setRsm(rs.getString("rsmRegion"));
    	monthlyStatisticsData.setDsmCode(rs.getString("dsmCode"));
    	monthlyStatisticsData.setDsmName(rs.getString("dsmName"));
    	monthlyStatisticsData.setEmergingInRate(rs.getDouble("inRate"));
    	monthlyStatisticsData.setEmergingWhRate(rs.getDouble("whRate"));
    	try{
    		monthlyStatisticsData.setEmergingAverageDose(rs.getDouble("averageDose"));
    	}catch(Exception e){
    		logger.warn(String.format("fail to populate MonthlyStatisticsEmergingDataRowMapper, %s", e.getMessage()));
    	}
        return monthlyStatisticsData;
    }
    
}
