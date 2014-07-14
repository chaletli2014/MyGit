package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.MobilePEDDailyData;

public class PediatricsMobileRowMapper implements RowMapper<MobilePEDDailyData>{
    
    public MobilePEDDailyData mapRow(ResultSet rs, int arg1) throws SQLException {
        MobilePEDDailyData mobilePEDDailyData = new MobilePEDDailyData();
        String userName = rs.getString("name");
//      if( LsAttributes.BR_NAME_CENTRAL.equalsIgnoreCase(userName) ){
//          userName = LsAttributes.BR_NAME_CENTRAL_ZH;
//      }else if( LsAttributes.BR_NAME_EAST1.equalsIgnoreCase(userName) ){
//          userName = LsAttributes.BR_NAME_EAST1_ZH;
//      }else if( LsAttributes.BR_NAME_EAST2.equalsIgnoreCase(userName) ){
//          userName = LsAttributes.BR_NAME_EAST2_ZH;
//      }else if( LsAttributes.BR_NAME_NORTH.equalsIgnoreCase(userName) ){
//          userName = LsAttributes.BR_NAME_NORTH_ZH;
//      }else if( LsAttributes.BR_NAME_SOUTH.equalsIgnoreCase(userName) ){
//          userName = LsAttributes.BR_NAME_SOUTH_ZH;
//      }else if( LsAttributes.BR_NAME_WEST.equalsIgnoreCase(userName) ){
//          userName = LsAttributes.BR_NAME_WEST_ZH;
//      }
        
        mobilePEDDailyData.setUserName(userName);
        mobilePEDDailyData.setUserCode(rs.getString("userCode"));
        mobilePEDDailyData.setHosNum(rs.getInt("hosNum"));
        mobilePEDDailyData.setInNum(rs.getInt("inNum"));
        mobilePEDDailyData.setPatNum(rs.getInt("pNum"));
        mobilePEDDailyData.setWhNum(rs.getInt("whNum"));
        mobilePEDDailyData.setLsNum(rs.getInt("lsNum"));
        mobilePEDDailyData.setAverageDose(rs.getDouble("averageDose"));
        mobilePEDDailyData.setHmgRate(rs.getDouble("hmgRate"));
        mobilePEDDailyData.setOmgRate(rs.getDouble("omgRate"));
        mobilePEDDailyData.setTmgRate(rs.getDouble("tmgRate"));
        mobilePEDDailyData.setFmgRate(rs.getDouble("fmgRate"));
        mobilePEDDailyData.setRegionCenterCN(rs.getString("regionCenterCN"));
        return mobilePEDDailyData;
    }
}
