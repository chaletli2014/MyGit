package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.RespirologyData;

public class RespirologyRowMapper implements RowMapper<RespirologyData>{

    public RespirologyData mapRow(ResultSet rs, int arg1) throws SQLException {
        RespirologyData respirologyData = new RespirologyData();
        respirologyData.setDataId(rs.getInt("id"));
        respirologyData.setCreatedate(rs.getTimestamp("createdate"));
        respirologyData.setHospitalCode(rs.getString("hospitalCode"));
        respirologyData.setHospitalName(rs.getString("hospitalName"));
        respirologyData.setPnum(rs.getInt("pnum"));
        respirologyData.setAenum(rs.getInt("aenum"));
        respirologyData.setWhnum(rs.getInt("whnum"));
        respirologyData.setLsnum(rs.getInt("lsnum"));
        respirologyData.setSalesETMSCode(rs.getString("etmsCode"));
        respirologyData.setSalesName(rs.getString("operatorName"));
        respirologyData.setRegion(rs.getString("region"));
        respirologyData.setRsmRegion(rs.getString("rsmRegion"));
        respirologyData.setOqd(rs.getDouble("oqd"));
        respirologyData.setTqd(rs.getDouble("tqd"));
        respirologyData.setOtid(rs.getDouble("otid"));
        respirologyData.setTbid(rs.getDouble("tbid"));
        respirologyData.setTtid(rs.getDouble("ttid"));
        respirologyData.setThbid(rs.getDouble("thbid"));
        respirologyData.setFbid(rs.getDouble("fbid"));
        respirologyData.setRecipeType(rs.getString("recipeType"));
        respirologyData.setDsmName(rs.getString("dsmName"));
        respirologyData.setIsResAssessed(rs.getString("isResAssessed"));
        respirologyData.setDragonType(rs.getString("dragonType"));
        respirologyData.setXbknum(rs.getInt("xbknum"));
        respirologyData.setXbk1num(rs.getInt("xbk1num"));
        respirologyData.setXbk2num(rs.getInt("xbk2num"));
        respirologyData.setXbk3num(rs.getInt("xbk3num"));
        return respirologyData;
    }
}