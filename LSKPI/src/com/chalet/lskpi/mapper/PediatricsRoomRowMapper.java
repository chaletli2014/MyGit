package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.PediatricsData;

public class PediatricsRoomRowMapper implements RowMapper<PediatricsData>{

    public PediatricsData mapRow(ResultSet rs, int arg1) throws SQLException {
    	PediatricsData pediatricsData = new PediatricsData();
    	pediatricsData.setDataId(rs.getInt("id"));
    	pediatricsData.setCreatedate(rs.getTimestamp("createdate"));
    	pediatricsData.setHospitalCode(rs.getString("hospitalCode"));
    	pediatricsData.setHospitalName(rs.getString("hospitalName"));
    	pediatricsData.setPnum(rs.getInt("pnum_room"));
    	pediatricsData.setWhnum(rs.getInt("whnum_room"));
    	pediatricsData.setLsnum(rs.getInt("lsnum_room"));
    	pediatricsData.setSalesETMSCode(rs.getString("etmsCode"));
    	pediatricsData.setSalesName(rs.getString("operatorName"));
    	pediatricsData.setRegion(rs.getString("region"));
    	pediatricsData.setRsmRegion(rs.getString("rsmRegion"));
    	pediatricsData.setHqd(rs.getDouble("hqd_room"));
    	pediatricsData.setHbid(rs.getDouble("hbid_room"));
    	pediatricsData.setOqd(rs.getDouble("oqd_room"));
    	pediatricsData.setObid(rs.getDouble("obid_room"));
    	pediatricsData.setTqd(rs.getDouble("tqd_room"));
    	pediatricsData.setTbid(rs.getDouble("tbid_room"));
    	pediatricsData.setRecipeType(rs.getString("recipeType_room"));
    	pediatricsData.setDsmName(rs.getString("dsmName"));
    	pediatricsData.setIsPedAssessed(rs.getString("isPedAssessed"));
    	pediatricsData.setDragonType(rs.getString("dragonType"));
    	pediatricsData.setPortNum(rs.getInt("portNum"));
    	pediatricsData.setWhbwnum(rs.getInt("whbwnum_room"));
    	
    	pediatricsData.setWhdaysRoom1Rate(rs.getDouble("whdays_room_1"));
    	pediatricsData.setWhdaysRoom2Rate(rs.getDouble("whdays_room_2"));
    	pediatricsData.setWhdaysRoom3Rate(rs.getDouble("whdays_room_3"));
    	pediatricsData.setWhdaysRoom4Rate(rs.getDouble("whdays_room_4"));
    	pediatricsData.setWhdaysRoom5Rate(rs.getDouble("whdays_room_5"));
    	pediatricsData.setWhdaysRoom6Rate(rs.getDouble("whdays_room_6"));
    	pediatricsData.setWhdaysRoom7Rate(rs.getDouble("whdays_room_7"));
    	pediatricsData.setWhdaysRoom8Rate(rs.getDouble("whdays_room_8"));
    	pediatricsData.setWhdaysRoom9Rate(rs.getDouble("whdays_room_9"));
    	pediatricsData.setWhdaysRoom10Rate(rs.getDouble("whdays_room_10"));
    	pediatricsData.setHomeWhRoomNum1(rs.getInt("home_wh_room_num1"));
    	pediatricsData.setHomeWhRoomNum2(rs.getInt("home_wh_room_num2"));
    	pediatricsData.setHomeWhRoomNum3(rs.getInt("home_wh_room_num3"));
    	pediatricsData.setHomeWhRoomNum4(rs.getInt("home_wh_room_num4"));
        return pediatricsData;
    }
}
