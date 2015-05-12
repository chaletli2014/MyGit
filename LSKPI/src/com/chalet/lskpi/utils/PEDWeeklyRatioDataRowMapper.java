package com.chalet.lskpi.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.chalet.lskpi.model.WeeklyRatioData;

/**
 * @author Chalet
 * @version 创建时间：2013年12月20日 上午12:13:48
 * 类说明
 */

public class PEDWeeklyRatioDataRowMapper implements RowMapper<WeeklyRatioData>{
    public WeeklyRatioData mapRow(ResultSet rs, int arg1) throws SQLException {
        WeeklyRatioData weeklyRatioData = new WeeklyRatioData();
        
        weeklyRatioData.setUserCode(rs.getString("userCode"));
        weeklyRatioData.setName(rs.getString("name"));
        
        weeklyRatioData.setInRate(rs.getDouble("inRate"));
        weeklyRatioData.setWhRate(rs.getDouble("whRate"));
        weeklyRatioData.setAverageDose(rs.getDouble("averageDose"));
        weeklyRatioData.setPnum(rs.getInt("pnum"));
        weeklyRatioData.setLsnum(rs.getInt("lsnum"));
        
        weeklyRatioData.setInRateRatio(rs.getDouble("inRateRatio"));
        weeklyRatioData.setWhRateRatio(rs.getDouble("whRateRatio"));
        weeklyRatioData.setAverageDoseRatio(rs.getDouble("averageDoseRatio"));
        weeklyRatioData.setPatNumRatio(rs.getDouble("pnumRatio"));
        weeklyRatioData.setLsNumRatio(rs.getDouble("lsnumRatio"));
        
        weeklyRatioData.setHmgRate(rs.getDouble("hmgRate"));
        weeklyRatioData.setHmgRateRatio(rs.getDouble("hmgRateRatio"));
        weeklyRatioData.setOmgRate(rs.getDouble("omgRate"));
        weeklyRatioData.setOmgRateRatio(rs.getDouble("omgRateRatio"));
        weeklyRatioData.setTmgRate(rs.getDouble("tmgRate"));
        weeklyRatioData.setTmgRateRatio(rs.getDouble("tmgRateRatio"));
        weeklyRatioData.setFmgRate(rs.getDouble("fmgRate"));
        weeklyRatioData.setFmgRateRatio(rs.getDouble("fmgRateRatio"));
        
        weeklyRatioData.setWhbwnum(rs.getInt("whbwnum"));
        weeklyRatioData.setWhbwNumRatio(rs.getDouble("whbwNumRatio"));
        weeklyRatioData.setBlRate(rs.getDouble("blRate"));
        weeklyRatioData.setBlRateRatio(rs.getDouble("blRateRatio"));
        return weeklyRatioData;
    }
}