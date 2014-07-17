package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.HomeWeeklyData;

public class HomeWeeklyDataRowMapper implements RowMapper<HomeWeeklyData> {

    public HomeWeeklyData mapRow(ResultSet rs, int arg1) throws SQLException {
        HomeWeeklyData weeklyData = new HomeWeeklyData();
        weeklyData.setUserName(rs.getString("name"));
        weeklyData.setNewDrNum(rs.getInt("newDrNum"));
        weeklyData.setTotalDrNum(rs.getInt("totalDrNum"));
        weeklyData.setNewWhNum(rs.getDouble("newWhNum"));
        weeklyData.setCureRate(rs.getDouble("cureRate"));
        weeklyData.setLsnum(rs.getDouble("lsnum"));
        weeklyData.setLsRate(rs.getDouble("lsRate"));
        weeklyData.setReachRate(rs.getDouble("reachRate"));
        weeklyData.setRegionCenterCN(rs.getString("regionCenterCN"));
        weeklyData.setReportNum(rs.getInt("reportNum"));
        return weeklyData;
    }

}
