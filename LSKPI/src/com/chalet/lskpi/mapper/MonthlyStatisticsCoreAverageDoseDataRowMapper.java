package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MonthlyStatisticsData;

public class MonthlyStatisticsCoreAverageDoseDataRowMapper implements RowMapper<MonthlyStatisticsData>{
    @Override
    public MonthlyStatisticsData mapRow(ResultSet rs, int i) throws SQLException {
    	MonthlyStatisticsData monthlyStatisticsData = new MonthlyStatisticsData();
    	monthlyStatisticsData.setRsd(rs.getString("region"));
    	monthlyStatisticsData.setRsm(rs.getString("rsmRegion"));
    	monthlyStatisticsData.setDsmCode(rs.getString("dsmCode"));
    	monthlyStatisticsData.setDsmName(rs.getString("dsmName"));
    	monthlyStatisticsData.setAverageDose(rs.getDouble("averageDose"));
    	
    	double aD1mgRate = -1;
    	double aD2mgRate = -1;
    	double aD3mgRate = -1;
    	double aD4mgRate = -1;
    	double aD6mgRate = -1;
    	double aD8mgRate = -1;
    	double aD4mgUpRate = -1;
    	
    	double averageDose2 = -1;
    	if(rs.getDouble("level2Num") != 0){
    		averageDose2 = rs.getDouble("averageDose2");
    		aD1mgRate = rs.getDouble("level21mgRate");
    		aD2mgRate = rs.getDouble("level22mgRate");
    		aD3mgRate = rs.getDouble("level23mgRate");
    		aD4mgRate = rs.getDouble("level24mgRate");
    		aD6mgRate = rs.getDouble("level26mgRate");
    		aD8mgRate = rs.getDouble("level28mgRate");
    		aD4mgUpRate = rs.getDouble("level24mgUpRate");
    	}
    	
    	monthlyStatisticsData.setCoreAverageDose2(averageDose2);
    	monthlyStatisticsData.setCoreLevel2AD1mgRate(aD1mgRate);
		monthlyStatisticsData.setCoreLevel2AD2mgRate(aD2mgRate);
		monthlyStatisticsData.setCoreLevel2AD3mgRate(aD3mgRate);
		monthlyStatisticsData.setCoreLevel2AD4mgRate(aD4mgRate);
		monthlyStatisticsData.setCoreLevel2AD6mgRate(aD6mgRate);
		monthlyStatisticsData.setCoreLevel2AD8mgRate(aD8mgRate);
		monthlyStatisticsData.setCoreLevel2AD4mgUpRate(aD4mgUpRate);
		
		double averageDose3 = -1;
    	if( rs.getDouble("level3Num") == 0 ){
    		aD1mgRate = -1;
        	aD2mgRate = -1;
        	aD3mgRate = -1;
        	aD4mgRate = -1;
        	aD6mgRate = -1;
        	aD8mgRate = -1;
        	aD4mgUpRate = -1;
    	}else{
    		averageDose3 = rs.getDouble("averageDose3");
    		aD1mgRate = rs.getDouble("level31mgRate");
    		aD2mgRate = rs.getDouble("level32mgRate");
    		aD3mgRate = rs.getDouble("level33mgRate");
    		aD4mgRate = rs.getDouble("level34mgRate");
    		aD6mgRate = rs.getDouble("level36mgRate");
    		aD8mgRate = rs.getDouble("level38mgRate");
    		aD4mgUpRate = rs.getDouble("level34mgUpRate");
    	}
    	
    	monthlyStatisticsData.setCoreAverageDose3(averageDose3);
    	monthlyStatisticsData.setCoreLevel3AD1mgRate(aD1mgRate);
		monthlyStatisticsData.setCoreLevel3AD2mgRate(aD2mgRate);
		monthlyStatisticsData.setCoreLevel3AD3mgRate(aD3mgRate);
		monthlyStatisticsData.setCoreLevel3AD4mgRate(aD4mgRate);
		monthlyStatisticsData.setCoreLevel3AD6mgRate(aD6mgRate);
		monthlyStatisticsData.setCoreLevel3AD8mgRate(aD8mgRate);
		monthlyStatisticsData.setCoreLevel3AD4mgUpRate(aD4mgUpRate);
    	
        return monthlyStatisticsData;
    }
    
}
