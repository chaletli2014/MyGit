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
