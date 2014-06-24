package com.chalet.lskpi.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.ReportProcessDataDetail;

/**
 * @author Chalet
 * @version 创建时间：2013年12月20日 上午12:13:48
 * 类说明
 */

public class ReportProcessDetailDataRowMapper implements RowMapper<ReportProcessDataDetail>{
    public ReportProcessDataDetail mapRow(ResultSet rs, int arg1) throws SQLException {
        ReportProcessDataDetail reportProcessDataDetail = new ReportProcessDataDetail();
    	reportProcessDataDetail.setHospitalName(rs.getString("hospitalName"));
    	reportProcessDataDetail.setSaleName(rs.getString("salesName"));
    	reportProcessDataDetail.setInNum(rs.getInt("inNum"));
    	reportProcessDataDetail.setIsAssessed(rs.getString("isAssessed"));
        return reportProcessDataDetail;
    }
}