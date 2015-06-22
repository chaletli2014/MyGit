package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.PediatricsData;

public class PediatricsWithPortNumRowMapper implements RowMapper<PediatricsData>{

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
    	pediatricsData.setPortNum(rs.getInt("portNum"));
    	pediatricsData.setWhbwnum(rs.getInt("whbwnum"));
    	pediatricsData.setHomeWhEmergingNum1(rs.getInt("home_wh_emerging_num1"));
    	pediatricsData.setHomeWhEmergingNum2(rs.getInt("home_wh_emerging_num2"));
    	pediatricsData.setHomeWhEmergingNum3(rs.getInt("home_wh_emerging_num3"));
    	pediatricsData.setHomeWhEmergingNum4(rs.getInt("home_wh_emerging_num4"));
    	pediatricsData.setLttEmergingNum(rs.getInt("lttEmergingNum"));
    	pediatricsData.setPnum_room(rs.getInt("pnum_room"));
    	pediatricsData.setLsnum_room(rs.getInt("lsnum_room"));
    	pediatricsData.setWhbwnum_room(rs.getInt("whbwnum_room"));
    	pediatricsData.setWhnum_room(rs.getInt("whnum_room"));
    	pediatricsData.setHqd_room(rs.getDouble("hqd_room"));
    	pediatricsData.setHbid_room(rs.getDouble("hbid_room"));
    	pediatricsData.setOqd_room(rs.getDouble("oqd_room"));
    	pediatricsData.setObid_room(rs.getDouble("obid_room"));
    	pediatricsData.setTqd_room(rs.getDouble("tqd_room"));
    	pediatricsData.setTbid_room(rs.getDouble("tbid_room"));
    	pediatricsData.setHomeWhRoomNum1(rs.getInt("home_wh_room_num1"));
    	pediatricsData.setHomeWhRoomNum2(rs.getInt("home_wh_room_num2"));
    	pediatricsData.setHomeWhRoomNum3(rs.getInt("home_wh_room_num3"));
    	pediatricsData.setHomeWhRoomNum4(rs.getInt("home_wh_room_num4"));
    	pediatricsData.setLttRoomNum(rs.getInt("lttRoomNum"));
    	pediatricsData.setWhdaysEmerging1Rate(rs.getDouble("whdays_emerging_1"));
    	pediatricsData.setWhdaysEmerging2Rate(rs.getDouble("whdays_emerging_2"));
    	pediatricsData.setWhdaysEmerging3Rate(rs.getDouble("whdays_emerging_3"));
    	pediatricsData.setWhdaysEmerging4Rate(rs.getDouble("whdays_emerging_4"));
    	pediatricsData.setWhdaysEmerging5Rate(rs.getDouble("whdays_emerging_5"));
    	pediatricsData.setWhdaysEmerging6Rate(rs.getDouble("whdays_emerging_6"));
    	pediatricsData.setWhdaysEmerging7Rate(rs.getDouble("whdays_emerging_7"));
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
        return pediatricsData;
    }
    
}

