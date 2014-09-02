package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.RespirologyLastWeekData;

public class ResLastWeekDataRowMapper implements RowMapper<RespirologyLastWeekData>{

    public RespirologyLastWeekData mapRow(ResultSet rs, int arg1) throws SQLException {
        RespirologyLastWeekData respirologyData = new RespirologyLastWeekData();
        respirologyData.setLsnum(rs.getInt("lsnum"));
        respirologyData.setAenum(rs.getInt("aenum"));
        respirologyData.setDuration(rs.getString("duration"));
        return respirologyData;
    }
}