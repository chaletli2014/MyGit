package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MonthlyStatisticsData;

public class MonthlyStatisticsDataRowMapper implements RowMapper<MonthlyStatisticsData>{
    @Override
    public MonthlyStatisticsData mapRow(ResultSet rs, int i) throws SQLException {
    	MonthlyStatisticsData monthlyStatisticsData = new MonthlyStatisticsData();
    	monthlyStatisticsData.setRsd(rs.getString("region"));
    	monthlyStatisticsData.setRsm(rs.getString("rsmRegion"));
    	monthlyStatisticsData.setDsmCode(rs.getString("dsmCode"));
    	monthlyStatisticsData.setDsmName(rs.getString("dsmName"));
    	monthlyStatisticsData.setInRate(rs.getDouble("inRate"));
    	monthlyStatisticsData.setWhRate(rs.getDouble("whRate"));
    	monthlyStatisticsData.setPnum(rs.getDouble("pnum"));
    	monthlyStatisticsData.setLsnum(rs.getDouble("lsnum"));
    	monthlyStatisticsData.setAenum(rs.getDouble("aenum"));
    	monthlyStatisticsData.setRisknum(rs.getDouble("risknum"));
    	monthlyStatisticsData.setAverageDose(rs.getDouble("averageDose"));;
        return monthlyStatisticsData;
    }
    
}
