package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.HomeData;


public class HomeDataExportRowMapper implements RowMapper<HomeData> {

    public HomeData mapRow(ResultSet rs, int arg1) throws SQLException {
        HomeData homeData = new HomeData();
        homeData.setId(rs.getInt("id"));
        homeData.setRegion(rs.getString("region"));
        homeData.setRsmRegion(rs.getString("rsmRegion"));
        homeData.setDsmName(rs.getString("dsmName"));
        homeData.setSalesName(rs.getString("salesName"));
        homeData.setHospitalCode(rs.getString("hospitalCode"));
        homeData.setHospitalName(rs.getString("hospitalName"));
        homeData.setDrName(rs.getString("drName"));
        homeData.setDoctorId(rs.getInt("doctorId"));
        homeData.setSalenum(rs.getInt("salenum"));
        homeData.setAsthmanum(rs.getInt("asthmanum"));
        homeData.setLtenum(rs.getInt("ltenum"));
        homeData.setLsnum(rs.getInt("lsnum"));
        homeData.setEfnum(rs.getInt("efnum"));
        homeData.setFtnum(rs.getInt("ftnum"));
        homeData.setLttnum(rs.getInt("lttnum"));
        homeData.setCreateDate(rs.getTimestamp("createdate"));
        return homeData;
    }

}
