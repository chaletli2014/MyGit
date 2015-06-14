package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.HomeWeeklyData;

public class PedRoomHomeWeeklyDataRowMapper implements RowMapper<HomeWeeklyData> {

    public HomeWeeklyData mapRow(ResultSet rs, int arg1) throws SQLException {
        HomeWeeklyData weeklyData = new HomeWeeklyData();
        weeklyData.setUserName(rs.getString("name"));
        weeklyData.setRegionCenterCN(rs.getString("regionCenterCN"));
        if( rs.getDouble("lastWeekDrNum") != 0 ){
        	weeklyData.setInRate(rs.getDouble("reportNum")/rs.getDouble("lastWeekDrNum"));
        }else{
        	weeklyData.setInRate(0);
        }
        weeklyData.setHomeWhNum1(rs.getInt("home_wh_room_num1"));
        weeklyData.setAverDays(rs.getDouble("averDays"));
        weeklyData.setHomeWhNum4(rs.getInt("home_wh_room_num4"));
        return weeklyData;
    }

}
