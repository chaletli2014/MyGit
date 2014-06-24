package com.chalet.lskpi.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.DailyReportData;

/**
 * @author Chalet
 * @version 创建时间：2013年12月20日 上午12:13:48
 * 类说明
 */

public class DailyReportDataRowMapper implements RowMapper<DailyReportData>{
    public DailyReportData mapRow(ResultSet rs, int arg1) throws SQLException {
    	DailyReportData dailyReportData = new DailyReportData();
    	dailyReportData.setRsmName(rs.getString("name"));
    	dailyReportData.setRsmCode(rs.getString("userCode"));
    	dailyReportData.setInRate(rs.getDouble("inNum")/rs.getDouble("hosNum"));
    	dailyReportData.setWhRate(rs.getDouble("pnum")==0?0:rs.getDouble("lsNum")/rs.getDouble("pnum"));
    	dailyReportData.setAverageDose(rs.getDouble("averageDose"));
        return dailyReportData;
    }
}