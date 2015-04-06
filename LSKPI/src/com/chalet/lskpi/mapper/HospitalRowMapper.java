package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.Hospital;

public class HospitalRowMapper implements RowMapper<Hospital>{

    public Hospital mapRow(ResultSet rs, int i) throws SQLException {
        Hospital dbHospital = new Hospital();
        dbHospital.setId(rs.getInt("id"));
        dbHospital.setCode(rs.getString("code"));
        dbHospital.setName(rs.getString("name"));
        dbHospital.setCity(rs.getString("city"));
        dbHospital.setProvince(rs.getString("province"));
        dbHospital.setRegion(rs.getString("region"));
        dbHospital.setRsmRegion(rs.getString("rsmRegion"));
        dbHospital.setSaleCode(rs.getString("saleCode"));
        dbHospital.setDsmCode(rs.getString("dsmCode"));
        dbHospital.setSaleName(rs.getString("saleName"));
        dbHospital.setPortNum(rs.getInt("portNum"));
        dbHospital.setIsRe2(rs.getString("isRe2"));
        return dbHospital;
    }

}
