package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.RespirologyMonthDBData;


public class RespirologyMonthDataRowMapper implements RowMapper<RespirologyMonthDBData> {

    public RespirologyMonthDBData mapRow(ResultSet rs, int arg1) throws SQLException {
        RespirologyMonthDBData monthData = new RespirologyMonthDBData();
        monthData.setRsmRegion(rs.getString("rsmRegion"));
        monthData.setRsmName(rs.getString("rsmName"));
        monthData.setDataMonth(rs.getString("date_MM"));
        monthData.setDataYear(rs.getString("date_YYYY"));
        monthData.setPnum(rs.getDouble("pnum"));
        monthData.setLsnum(rs.getDouble("lsnum"));
        monthData.setAenum(rs.getDouble("aenum"));
        monthData.setInRate(rs.getDouble("inRate"));
        monthData.setWhRate(rs.getDouble("whRate"));
        monthData.setAverageDose(rs.getDouble("averageDose"));
        monthData.setWeeklyCount(rs.getInt("weeklyCount"));
        return monthData;
    }

}
