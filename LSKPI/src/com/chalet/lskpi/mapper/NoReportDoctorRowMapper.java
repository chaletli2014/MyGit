package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.chalet.lskpi.model.HomeWeeklyNoReportDr;

public class NoReportDoctorRowMapper implements RowMapper<HomeWeeklyNoReportDr> {

    public HomeWeeklyNoReportDr mapRow(ResultSet rs, int arg1) throws SQLException {
    	HomeWeeklyNoReportDr dr = new HomeWeeklyNoReportDr();
        dr.setRsd(rs.getString("region"));
        dr.setRsm(rs.getString("rsmRegion"));
        dr.setDsm(rs.getString("dsmName"));
        dr.setSalesCode(rs.getString("salesCode"));
        dr.setRep(rs.getString("salesName"));
        dr.setHospitalCode(rs.getString("hospitalCode"));
        dr.setHospitalName(rs.getString("hospitalName"));
        dr.setDoctorCode(rs.getString("doctorCode"));
        dr.setDoctorId(rs.getString("doctorId"));
        dr.setDoctorName(rs.getString("doctorName"));
        return dr;
    }

}
