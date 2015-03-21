package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MonthlyData;

public class MonthlyDataRowMapper implements RowMapper<MonthlyData>{
    @Override
    public MonthlyData mapRow(ResultSet rs, int i) throws SQLException {
        MonthlyData monthlyData = new MonthlyData();
        monthlyData.setId(rs.getInt("id"));
        monthlyData.setPedemernum(rs.getDouble("pedEmernum"));
        monthlyData.setPedroomnum(rs.getDouble("pedroomnum"));
        monthlyData.setResnum(rs.getDouble("resnum"));
        monthlyData.setOthernum(rs.getDouble("other"));
        monthlyData.setOperatorName(rs.getString("operatorName"));
        monthlyData.setOperatorCode(rs.getString("operatorCode"));
        monthlyData.setHospitalCode(rs.getString("hospitalCode"));
        monthlyData.setHospitalName(rs.getString("hospitalName"));
        monthlyData.setDsmName(rs.getString("dsmName"));
        monthlyData.setRsmRegion(rs.getString("rsmRegion"));
        monthlyData.setRegion(rs.getString("region"));
        monthlyData.setCreateDate(rs.getTimestamp("createdate"));
        monthlyData.setPedEmerDrugStore(rs.getDouble("ped_emer_drugstore"));
        monthlyData.setPedEmerWh(rs.getDouble("ped_emer_wh"));
        monthlyData.setPedRoomDrugStore(rs.getDouble("ped_room_drugstore"));
        monthlyData.setPedRoomDrugStoreWh(rs.getDouble("ped_room_drugstore_wh"));
        monthlyData.setResClinic(rs.getDouble("res_clinic"));
        monthlyData.setResRoom(rs.getDouble("res_room"));
        monthlyData.setHomeWh(rs.getDouble("home_wh"));
        return monthlyData;
    }
    
}
