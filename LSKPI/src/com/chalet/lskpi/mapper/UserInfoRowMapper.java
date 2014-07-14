package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.utils.LsAttributes;

public class UserInfoRowMapper implements RowMapper<UserInfo>{
    @Override
    public UserInfo mapRow(ResultSet rs, int i) throws SQLException {
        UserInfo dbUser = new UserInfo();
        dbUser.setId(rs.getInt("id"));
        dbUser.setName(rs.getString("name"));
        dbUser.setTelephone(rs.getString("telephone"));
        dbUser.setEtmsCode(rs.getString("etmsCode"));
        dbUser.setRegion(rs.getString("region"));
        dbUser.setRegionCenter(rs.getString("regionCenter"));
        if( null != rs.getString("level") 
                && !LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(rs.getString("level"))
                && !LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(rs.getString("level"))
                && !LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(rs.getString("level"))
                && !LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(rs.getString("level"))){
            dbUser.setLevel(LsAttributes.USER_LEVEL_BM);
        }else{
            dbUser.setLevel(rs.getString("level"));
        }
        dbUser.setRealLevel(rs.getString("level"));
        dbUser.setSuperior(rs.getString("superior"));
        dbUser.setUserCode(rs.getString("userCode"));
        dbUser.setEmail(rs.getString("email"));
        dbUser.setRegionCenterCN(rs.getString("regionCenterCN"));
        return dbUser;
    }
    
}
