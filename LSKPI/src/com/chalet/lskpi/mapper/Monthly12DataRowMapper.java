package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.Monthly12Data;

public class Monthly12DataRowMapper implements RowMapper<Monthly12Data>{
    @Override
    public Monthly12Data mapRow(ResultSet rs, int i) throws SQLException {
        Monthly12Data monthly12Data = new Monthly12Data();
        monthly12Data.setDataMonth(rs.getString("dataMonth"));
        monthly12Data.setHosNum(rs.getInt("hosNum"));
        monthly12Data.setPedemernum(rs.getInt("pedEmernum"));
        monthly12Data.setPedroomnum(rs.getInt("pedroomnum"));
        monthly12Data.setResnum(rs.getInt("resnum"));
        monthly12Data.setOthernum(rs.getInt("other"));
        monthly12Data.setTotalnum(rs.getInt("totalnum"));
        monthly12Data.setInNum(rs.getInt("innum"));
        return monthly12Data;
    }
    
}