package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.HospitalSalesQueryObj;

public class HospitalSalesQueryRowMapper implements RowMapper<HospitalSalesQueryObj> {

    public HospitalSalesQueryObj mapRow(ResultSet rs, int arg1) throws SQLException {
        HospitalSalesQueryObj hso = new HospitalSalesQueryObj();
        hso.setHosCode(rs.getString("hospitalCode"));
        hso.setHosName(rs.getString("hospitalName"));
        hso.setPnum(rs.getInt("pnum"));
        hso.setLsnum(rs.getInt("lsnum"));
        hso.setWhRate(rs.getDouble("whRate"));
        hso.setAveDose(rs.getDouble("averageDose"));
        hso.setPnumRatio(rs.getDouble("pnumRatio"));
        hso.setLsnumRatio(rs.getDouble("lsnumRatio"));
        hso.setWhRateRatio(rs.getDouble("whRateRatio"));
        hso.setAveDoseRatio(rs.getDouble("averageDoseRatio"));
        return hso;
    }

}
