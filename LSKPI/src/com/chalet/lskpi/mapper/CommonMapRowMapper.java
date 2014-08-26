package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class CommonMapRowMapper implements RowMapper<Map<String, Integer>> {

    public Map<String, Integer> mapRow(ResultSet rs, int arg1) throws SQLException {
        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        resultMap.put(rs.getString("rsmRegion"), rs.getInt("numCount"));
        return resultMap;
    }

}
