package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.Doctor;


public class DoctorRowMapper implements RowMapper<Doctor> {

    public Doctor mapRow(ResultSet rs, int arg1) throws SQLException {
        Doctor dr = new Doctor();
        dr.setId(rs.getInt("id"));
        dr.setName(rs.getString("drName"));
        dr.setCode(rs.getString("drCode"));
        dr.setHospitalCode(rs.getString("hospitalCode"));
        dr.setHospitalName(rs.getString("hospitalName"));
        dr.setSalesCode(rs.getString("salesCode"));
        dr.setSalesName(rs.getString("salesName"));
        return dr;
    }

}
