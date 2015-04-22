package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.RespirologyMonthDBData;


public class RespirologyMonthDataRowMapper implements RowMapper<RespirologyMonthDBData> {

    public RespirologyMonthDBData mapRow(ResultSet rs, int arg1) throws SQLException {
        RespirologyMonthDBData monthData = new RespirologyMonthDBData();
        monthData.setRsmRegion(rs.getString("title"));
        monthData.setRsmName(rs.getString("name"));
        monthData.setDataMonth(rs.getString("date_MM"));
        monthData.setDataYear(rs.getString("date_YYYY"));
        monthData.setDuration(rs.getString("duration"));
        monthData.setPnum(rs.getDouble("pnum"));
        monthData.setLsnum(rs.getDouble("lsnum"));
        monthData.setAenum(rs.getDouble("aenum"));
        monthData.setInRate(rs.getDouble("inRate"));
        monthData.setWhRate(rs.getDouble("whRate"));
        monthData.setAverageDose(rs.getDouble("averageDose"));
        monthData.setWeeklyCount(rs.getInt("weeklyCount"));
        if( 0 == rs.getDouble("resMonthNum") || 0 == rs.getDouble("lsnum") ){
        	monthData.setWhDays(0);
        }else{
        	monthData.setWhDays(((rs.getDouble("resMonthNum")/rs.getInt("weeklyCount"))*5)/rs.getDouble("averageDose")/(rs.getDouble("lsnum")/rs.getInt("weeklyCount")));
        }
        monthData.setXbknum(rs.getDouble("xbknum"));
        return monthData;
    }

}
