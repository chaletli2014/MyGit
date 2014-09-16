package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.PediatricsData;

public class PediatricsRowMapper implements RowMapper<PediatricsData>{

    public PediatricsData mapRow(ResultSet rs, int arg1) throws SQLException {
    	PediatricsData pediatricsData = new PediatricsData();
    	pediatricsData.setDataId(rs.getInt("id"));
    	pediatricsData.setCreatedate(rs.getTimestamp("createdate"));
    	pediatricsData.setHospitalCode(rs.getString("hospitalCode"));
    	pediatricsData.setHospitalName(rs.getString("hospitalName"));
    	pediatricsData.setPnum(rs.getInt("pnum"));
    	pediatricsData.setWhnum(rs.getInt("whnum"));
    	pediatricsData.setLsnum(rs.getInt("lsnum"));
    	pediatricsData.setSalesETMSCode(rs.getString("etmsCode"));
    	pediatricsData.setSalesName(rs.getString("operatorName"));
    	pediatricsData.setRegion(rs.getString("region"));
    	pediatricsData.setRsmRegion(rs.getString("rsmRegion"));
    	pediatricsData.setHqd(rs.getDouble("hqd"));
    	pediatricsData.setHbid(rs.getDouble("hbid"));
    	pediatricsData.setOqd(rs.getDouble("oqd"));
    	pediatricsData.setObid(rs.getDouble("obid"));
    	pediatricsData.setTqd(rs.getDouble("tqd"));
    	pediatricsData.setTbid(rs.getDouble("tbid"));
    	pediatricsData.setRecipeType(rs.getString("recipeType"));
    	pediatricsData.setDsmName(rs.getString("dsmName"));
    	pediatricsData.setIsPedAssessed(rs.getString("isPedAssessed"));
    	pediatricsData.setDragonType(rs.getString("dragonType"));
        return pediatricsData;
    }
    
}
