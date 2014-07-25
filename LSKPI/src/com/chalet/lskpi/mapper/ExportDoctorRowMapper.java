package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.ExportDoctor;


public class ExportDoctorRowMapper implements RowMapper<ExportDoctor> {

    public ExportDoctor mapRow(ResultSet rs, int arg1) throws SQLException {
        ExportDoctor dr = new ExportDoctor();
        dr.setRegion(rs.getString("region"));
        dr.setRsmRegion(rs.getString("rsmRegion"));
        dr.setDsmName(rs.getString("dsmName"));
        dr.setSalesCode(rs.getString("salesCode"));
        dr.setSalesName(rs.getString("salesName"));
        dr.setHospitalCode(rs.getString("hospitalCode"));
        dr.setHospitalName(rs.getString("hospitalName"));
        dr.setDoctorCode(rs.getString("hospitalCode")+rs.getString("doctorCode"));
        dr.setDoctorName(rs.getString("doctorName"));
        dr.setId(rs.getInt("id"));
        return dr;
    }

}
