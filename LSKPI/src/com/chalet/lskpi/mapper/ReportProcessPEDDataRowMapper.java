package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.ReportProcessData;

/**
 * @author Chalet
 * @version 创建时间：2013年12月20日 上午12:13:48
 * 类说明
 */

public class ReportProcessPEDDataRowMapper implements RowMapper<ReportProcessData>{
    public ReportProcessData mapRow(ResultSet rs, int arg1) throws SQLException {
    	ReportProcessData reportProcessData = new ReportProcessData();
    	reportProcessData.setHosNum(rs.getInt("hosNum"));
    	reportProcessData.setValidInNum(rs.getInt("validInNum"));
    	if( rs.getInt("hosNum") == 0 ){
    	    reportProcessData.setCurrentInRate(0);
    	}else{
    	    Double inNum = rs.getDouble("validInNum");
    	    Double hosNum_count = rs.getDouble("hosNum_count");
    	    
	        reportProcessData.setCurrentInRate(inNum/hosNum_count);
    	}
        return reportProcessData;
    }
}