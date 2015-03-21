package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MonthlyRatioData;

public class MonthlyRatioDataRowMapper implements RowMapper<MonthlyRatioData>{
	
	private Logger logger = Logger.getLogger(MonthlyRatioDataRowMapper.class);
			
    @Override
    public MonthlyRatioData mapRow(ResultSet rs, int i) throws SQLException {
        MonthlyRatioData monthlyRatioData = new MonthlyRatioData();
        monthlyRatioData.setSaleName(rs.getString("saleName"));
        monthlyRatioData.setDsmName(rs.getString("dsmName"));
        monthlyRatioData.setRsmRegion(rs.getString("rsmRegion"));
        monthlyRatioData.setRegion(rs.getString("region"));
        
        monthlyRatioData.setPedemernum(rs.getInt("pedEmernum"));
        monthlyRatioData.setPedroomnum(rs.getInt("pedroomnum"));
        monthlyRatioData.setResnum(rs.getInt("resnum"));
        //月报分拆
        monthlyRatioData.setPedEmerDrugStore(rs.getDouble("ped_emer_drugstore"));
        monthlyRatioData.setPedEmerDrugStoreRate(rs.getDouble("pedEmerDtRate"));
        monthlyRatioData.setPedEmerDrugStoreRatio(rs.getDouble("pedEmerDtRatio"));
        monthlyRatioData.setPedEmerDrugStoreRateRatio(rs.getDouble("pedEmerDtRateRatio"));

        monthlyRatioData.setPedEmerWh(rs.getDouble("ped_emer_wh"));
        monthlyRatioData.setPedEmerWhRate(rs.getDouble("pedEmerWhRate"));
        monthlyRatioData.setPedEmerWhRatio(rs.getDouble("pedEmerWhRatio"));
        monthlyRatioData.setPedEmerWhRateRatio(rs.getDouble("pedEmerWhRateRatio"));
        
        monthlyRatioData.setPedRoomDrugStore(rs.getDouble("ped_room_drugstore"));
        monthlyRatioData.setPedRoomDrugStoreRate(rs.getDouble("pedRoomDtRate"));
        monthlyRatioData.setPedRoomDrugStoreRatio(rs.getDouble("pedRoomDtRatio"));
        monthlyRatioData.setPedRoomDrugStoreRateRatio(rs.getDouble("pedRoomDtRateRatio"));
        
        monthlyRatioData.setPedRoomDrugStoreWh(rs.getDouble("ped_room_drugstore_wh"));
        monthlyRatioData.setPedRoomDrugStoreWhRate(rs.getDouble("pedRoomDtWhRate"));
        monthlyRatioData.setPedRoomDrugStoreWhRatio(rs.getDouble("pedRoomDtWhRatio"));
        monthlyRatioData.setPedRoomDrugStoreWhRateRatio(rs.getDouble("pedRoomDtWhRateRatio"));
        
        monthlyRatioData.setHomeWh(rs.getDouble("home_wh"));
        monthlyRatioData.setHomeWhRate(rs.getDouble("homeWhRate"));
        monthlyRatioData.setHomeWhRatio(rs.getDouble("homeWhRatio"));
        monthlyRatioData.setHomeWhRateRatio(rs.getDouble("homeWhRateRatio"));
        
        monthlyRatioData.setResClinic(rs.getDouble("res_clinic"));
        monthlyRatioData.setResClinicRate(rs.getDouble("resClinicRate"));
        monthlyRatioData.setResClinicRatio(rs.getDouble("resClinicRatio"));
        monthlyRatioData.setResClinicRateRatio(rs.getDouble("resClinicRateRatio"));
        
        monthlyRatioData.setResRoom(rs.getDouble("res_room"));
        monthlyRatioData.setResRoomRate(rs.getDouble("resRoomRate"));
        monthlyRatioData.setResRoomRatio(rs.getDouble("resRoomRatio"));
        monthlyRatioData.setResRoomRateRatio(rs.getDouble("resRoomRateRatio"));
        
        
        monthlyRatioData.setOthernum(rs.getInt("othernum"));
        monthlyRatioData.setTotalnum(rs.getInt("totalnum"));
        
        monthlyRatioData.setPedemernumrate(rs.getDouble("pedemernumrate"));
        monthlyRatioData.setPedroomnumrate(rs.getDouble("pedroomnumrate"));
        monthlyRatioData.setResnumrate(rs.getDouble("resnumrate"));
        monthlyRatioData.setOthernumrate(rs.getDouble("othernumrate"));
        
        monthlyRatioData.setPedemernumratio(rs.getDouble("pedemernumratio"));
        monthlyRatioData.setPedroomnumratio(rs.getDouble("pedroomnumratio"));
        monthlyRatioData.setResnumratio(rs.getDouble("resnumratio"));
        monthlyRatioData.setOthernumratio(rs.getDouble("othernumratio"));
        monthlyRatioData.setTotalnumratio(rs.getDouble("totalnumratio"));
        
        monthlyRatioData.setPedemernumrateratio(rs.getDouble("pedemernumrateratio"));
        monthlyRatioData.setPedroomnumrateratio(rs.getDouble("pedroomnumrateratio"));
        monthlyRatioData.setResnumrateratio(rs.getDouble("resnumrateratio"));
        monthlyRatioData.setOthernumrateratio(rs.getDouble("othernumrateratio"));
        
        try{
        	if(rs.getInt("hosnum") >= 0 ){
        		monthlyRatioData.setHosnum(rs.getInt("hosnum"));
        	}
    		monthlyRatioData.setHosnumratio(rs.getDouble("hosnumratio"));
        }catch(Exception e){
        	logger.warn("there is no column called hosnum");
        }
        try{
        	if(rs.getInt("innum") >= 0 ){
        		monthlyRatioData.setInnum(rs.getInt("innum"));
        	}
    		monthlyRatioData.setInnumratio(rs.getDouble("innumratio"));
        }catch(Exception e){
        	logger.warn("there is no column called innum");
        }
        try{
            monthlyRatioData.setInrate(rs.getDouble("inrate"));
            monthlyRatioData.setInrateratio(rs.getDouble("inrateratio"));
        }catch(Exception e){
            logger.warn("there is no column called inrate");
        }
        
        return monthlyRatioData;
    }
    
}
