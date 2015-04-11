package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MonthlyStatisticsData;

public class MonthlyStatisticsCoreDataRowMapper implements RowMapper<MonthlyStatisticsData>{
	Logger logger = Logger.getLogger(MonthlyStatisticsCoreDataRowMapper.class);
	
    @Override
    public MonthlyStatisticsData mapRow(ResultSet rs, int i) throws SQLException {
    	MonthlyStatisticsData monthlyStatisticsData = new MonthlyStatisticsData();
    	monthlyStatisticsData.setRsd(rs.getString("region"));
    	monthlyStatisticsData.setRsm(rs.getString("rsmRegion"));
    	monthlyStatisticsData.setDsmCode(rs.getString("dsmCode"));
    	monthlyStatisticsData.setDsmName(rs.getString("dsmName"));
    	monthlyStatisticsData.setCoreInRate(rs.getDouble("inRate"));
    	monthlyStatisticsData.setCoreWhRate(rs.getDouble("whRate"));
    	try{
    		monthlyStatisticsData.setCoreAverageDose(rs.getDouble("averageDose"));
    		if( rs.getDouble("level2Num") == 0 ){
    			monthlyStatisticsData.setCoreAverageDose2(-1);
    		}else{
    			monthlyStatisticsData.setCoreAverageDose2(rs.getDouble("averageDose2"));
    		}
    		if( rs.getDouble("level3Num") == 0 ){
    			monthlyStatisticsData.setCoreAverageDose3(-1);
    		}else{
    			monthlyStatisticsData.setCoreAverageDose3(rs.getDouble("averageDose3"));
    		}
    	}catch(Exception e){
    		logger.warn(String.format("fail to populate MonthlyStatisticsCoreDataRowMapper, %s", e.getMessage()));
    	}
        return monthlyStatisticsData;
    }
    
}
