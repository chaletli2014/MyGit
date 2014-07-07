package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobileCHEDailyData;
import com.chalet.lskpi.utils.LsAttributes;

public class ChestSurgeryMobileRowMapper implements RowMapper<MobileCHEDailyData>{

    public MobileCHEDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobileCHEDailyData mobileCHEDailyData = new MobileCHEDailyData();
        String userName = rs.getString("name");
        if( LsAttributes.BR_NAME_CENTRAL.equalsIgnoreCase(userName) ){
            userName = LsAttributes.BR_NAME_CENTRAL_ZH;
        }else if( LsAttributes.BR_NAME_EAST1.equalsIgnoreCase(userName) ){
            userName = LsAttributes.BR_NAME_EAST1_ZH;
        }else if( LsAttributes.BR_NAME_EAST2.equalsIgnoreCase(userName) ){
            userName = LsAttributes.BR_NAME_EAST2_ZH;
        }else if( LsAttributes.BR_NAME_NORTH.equalsIgnoreCase(userName) ){
            userName = LsAttributes.BR_NAME_NORTH_ZH;
        }else if( LsAttributes.BR_NAME_SOUTH.equalsIgnoreCase(userName) ){
            userName = LsAttributes.BR_NAME_SOUTH_ZH;
        }else if( LsAttributes.BR_NAME_WEST.equalsIgnoreCase(userName) ){
            userName = LsAttributes.BR_NAME_WEST_ZH;
        }
        
        mobileCHEDailyData.setUserName(userName);
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
        return mobileCHEDailyData;
    }

}
