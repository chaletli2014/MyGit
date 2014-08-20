package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.ChestSurgeryData;

public class ChestSurgeryRowMapper implements RowMapper<ChestSurgeryData>{

    public ChestSurgeryData mapRow(ResultSet rs, int arg1) throws SQLException {
        ChestSurgeryData chestSurgeryData = new ChestSurgeryData();
        chestSurgeryData.setDataId(rs.getInt("id"));
        chestSurgeryData.setCreatedate(rs.getTimestamp("createdate"));
        chestSurgeryData.setHospitalCode(rs.getString("hospitalCode"));
        chestSurgeryData.setHospitalName(rs.getString("hospitalName"));
        chestSurgeryData.setPnum(rs.getInt("pnum"));
        chestSurgeryData.setRisknum(rs.getInt("risknum"));
        chestSurgeryData.setWhnum(rs.getInt("whnum"));
        chestSurgeryData.setLsnum(rs.getInt("lsnum"));
        chestSurgeryData.setSalesCode(rs.getString("salesCode"));
        chestSurgeryData.setSalesName(rs.getString("salesName"));
        chestSurgeryData.setRegion(rs.getString("region"));
        chestSurgeryData.setRsmRegion(rs.getString("rsmRegion"));
        chestSurgeryData.setOqd(rs.getDouble("oqd"));
        chestSurgeryData.setTqd(rs.getDouble("tqd"));
        chestSurgeryData.setOtid(rs.getDouble("otid"));
        chestSurgeryData.setTbid(rs.getDouble("tbid"));
        chestSurgeryData.setTtid(rs.getDouble("ttid"));
        chestSurgeryData.setThbid(rs.getDouble("thbid"));
        chestSurgeryData.setFbid(rs.getDouble("fbid"));
        chestSurgeryData.setDsmName(rs.getString("dsmName"));
        chestSurgeryData.setIsChestSurgeryAssessed(rs.getString("isChestSurgeryAssessed"));
        return chestSurgeryData;
    }

}
