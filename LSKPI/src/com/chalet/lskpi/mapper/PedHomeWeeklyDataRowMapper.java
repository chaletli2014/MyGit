package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.HomeWeeklyData;

public class PedHomeWeeklyDataRowMapper implements RowMapper<HomeWeeklyData> {

    public HomeWeeklyData mapRow(ResultSet rs, int arg1) throws SQLException {
        HomeWeeklyData weeklyData = new HomeWeeklyData();
        weeklyData.setUserName(rs.getString("name"));
        weeklyData.setRegionCenterCN(rs.getString("regionCenterCN"));
        if( rs.getDouble("hosNum") != 0 ){
        	weeklyData.setInRate(rs.getDouble("inNum")/rs.getDouble("hosNum"));
        }else{
        	weeklyData.setInRate(0);
        }
        weeklyData.setHomeWhNum1(rs.getInt("homeWhNum1"));
        weeklyData.setAverDays(rs.getDouble("averDays"));
        weeklyData.setHomeWhNum4(rs.getInt("homeWhNum4"));
        weeklyData.setInNum(rs.getInt("inNum"));
        return weeklyData;
    }

}
