package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobilePEDDailyData;

public class PediatricsMobileRowMapper implements RowMapper<MobilePEDDailyData>{
    
    public MobilePEDDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobilePEDDailyData mobilePEDDailyData = new MobilePEDDailyData();
        mobilePEDDailyData.setUserName(rs.getString("name"));
        mobilePEDDailyData.setUserCode(rs.getString("userCode"));
        mobilePEDDailyData.setHosNum(rs.getInt("hosNum"));
        mobilePEDDailyData.setInNum(rs.getInt("inNum"));
        mobilePEDDailyData.setPatNum(rs.getInt("pNum"));
        mobilePEDDailyData.setWhNum(rs.getInt("whNum"));
        mobilePEDDailyData.setLsNum(rs.getInt("lsNum"));
        mobilePEDDailyData.setAverageDose(rs.getDouble("averageDose"));
        mobilePEDDailyData.setHmgRate(rs.getDouble("hmgRate"));
        mobilePEDDailyData.setOmgRate(rs.getDouble("omgRate"));
        mobilePEDDailyData.setTmgRate(rs.getDouble("tmgRate"));
        mobilePEDDailyData.setFmgRate(rs.getDouble("fmgRate"));
        mobilePEDDailyData.setRegionCenterCN(rs.getString("regionCenterCN"));
        mobilePEDDailyData.setWhbwnum(rs.getInt("whbwnum"));
        mobilePEDDailyData.setBlRate(rs.getDouble("blRate"));
        mobilePEDDailyData.setWhdays(rs.getDouble("whdays_emerging"));
        
        mobilePEDDailyData.setPatNumRoom(rs.getInt("pNum_room"));
        mobilePEDDailyData.setWhNumRoom(rs.getInt("whNum_room"));
        mobilePEDDailyData.setLsNumRoom(rs.getInt("lsNum_room"));
        mobilePEDDailyData.setAverageDoseRoom(rs.getDouble("averageDose_room"));
        mobilePEDDailyData.setWhbwnumRoom(rs.getInt("whbwnum_room"));
        mobilePEDDailyData.setBlRateRoom(rs.getDouble("blRate_room"));
        mobilePEDDailyData.setWhdaysRoom(rs.getDouble("whdays_room"));
        
        mobilePEDDailyData.setHomeWhEmergingNum1(rs.getInt("home_wh_emerging_num1"));
        mobilePEDDailyData.setHomeWhEmergingNum2(rs.getInt("home_wh_emerging_num2"));
        mobilePEDDailyData.setHomeWhEmergingNum3(rs.getInt("home_wh_emerging_num3"));
        mobilePEDDailyData.setHomeWhEmergingNum4(rs.getInt("home_wh_emerging_num4"));
        mobilePEDDailyData.setHomeWhRoomNum1(rs.getInt("home_wh_room_num1"));
        mobilePEDDailyData.setHomeWhRoomNum2(rs.getInt("home_wh_room_num2"));
        mobilePEDDailyData.setHomeWhRoomNum3(rs.getInt("home_wh_room_num3"));
        mobilePEDDailyData.setHomeWhRoomNum4(rs.getInt("home_wh_room_num4"));
        
        return mobilePEDDailyData;
    }
}
