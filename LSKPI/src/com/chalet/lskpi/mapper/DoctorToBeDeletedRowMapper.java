package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.chalet.lskpi.model.DoctorToBeDeleted;


public class DoctorToBeDeletedRowMapper implements RowMapper<DoctorToBeDeleted> {

    public DoctorToBeDeleted mapRow(ResultSet rs, int arg1) throws SQLException {
    	DoctorToBeDeleted dr = new DoctorToBeDeleted();
        dr.setDrId(rs.getInt("drId"));
        dr.setDrName(rs.getString("drName"));
        dr.setHospitalCode(rs.getString("hospitalCode"));
        dr.setHospitalName(rs.getString("hospitalName"));
        dr.setDeleteReason(rs.getString("deleteReason"));
        return dr;
    }

}
