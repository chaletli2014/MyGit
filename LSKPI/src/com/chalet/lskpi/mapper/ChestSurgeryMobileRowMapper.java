package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobileCHEDailyData;

public class ChestSurgeryMobileRowMapper implements RowMapper<MobileCHEDailyData>{

    public MobileCHEDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobileCHEDailyData mobileCHEDailyData = new MobileCHEDailyData();
        mobileCHEDailyData.setUserName(rs.getString("name"));
        mobileCHEDailyData.setUserCode(rs.getString("userCode"));
        mobileCHEDailyData.setHosNum(rs.getInt("hosNum"));
        mobileCHEDailyData.setInNum(rs.getInt("inNum"));
        mobileCHEDailyData.setPatNum(rs.getInt("pNum"));
        mobileCHEDailyData.setWhNum(rs.getInt("whNum"));
        mobileCHEDailyData.setLsNum(rs.getInt("lsNum"));
        mobileCHEDailyData.setAverageDose(rs.getDouble("averageDose"));
        mobileCHEDailyData.setOmgRate(rs.getDouble("omgRate"));
        mobileCHEDailyData.setTmgRate(rs.getDouble("tmgRate"));
        mobileCHEDailyData.setThmgRate(rs.getDouble("thmgRate"));
        mobileCHEDailyData.setFmgRate(rs.getDouble("fmgRate"));
        mobileCHEDailyData.setSmgRate(rs.getDouble("smgRate"));
        mobileCHEDailyData.setEmgRate(rs.getDouble("emgRate"));
        mobileCHEDailyData.setRegionCenterCN(rs.getString("regionCenterCN"));
        return mobileCHEDailyData;
    }

}
