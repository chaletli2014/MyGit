package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.Monthly12Data;

public class Monthly12DataRowMapper implements RowMapper<Monthly12Data>{
    @Override
    public Monthly12Data mapRow(ResultSet rs, int i) throws SQLException {
        Monthly12Data monthly12Data = new Monthly12Data();
        monthly12Data.setDataMonth(rs.getString("dataMonth"));
        monthly12Data.setHosNum(rs.getInt("hosNum"));
        monthly12Data.setPedemernum(rs.getInt("pedEmernum"));
        monthly12Data.setPedroomnum(rs.getInt("pedroomnum"));
        monthly12Data.setResnum(rs.getInt("resnum"));
        monthly12Data.setOthernum(rs.getInt("other"));
        monthly12Data.setTotalnum(rs.getInt("totalnum"));
        monthly12Data.setInNum(rs.getInt("innum"));
        
        monthly12Data.setPedEmerDrugStore(rs.getDouble("ped_emer_drugstore"));
        monthly12Data.setPedEmerWh(rs.getDouble("ped_emer_wh"));
        monthly12Data.setPedRoomDrugStore(rs.getDouble("ped_room_drugstore"));
        monthly12Data.setPedRoomDrugStoreWh(rs.getDouble("ped_room_drugstore_wh"));
        monthly12Data.setResClinic(rs.getDouble("res_clinic"));
        monthly12Data.setResRoom(rs.getDouble("res_room"));
        monthly12Data.setHomeWh(rs.getDouble("home_wh"));
        return monthly12Data;
    }
    
}