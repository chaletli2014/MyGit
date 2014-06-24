package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.chalet.lskpi.model.MonthlyRatioData;

/**
 * @author Chalet
 * @version 创建时间：2014年5月20日 下午10:30:46
 * 类说明
 */

public class MonthlyCollectionDataRowMapper implements RowMapper<MonthlyRatioData> {

	@Override
	public MonthlyRatioData mapRow(ResultSet rs, int arg1)
			throws SQLException {
		MonthlyRatioData monthlyRatioData = new MonthlyRatioData();
        monthlyRatioData.setRsmRegion(rs.getString("rsmRegion"));
        
        monthlyRatioData.setPedemernum(rs.getInt("pedEmernum"));
        monthlyRatioData.setPedroomnum(rs.getInt("pedroomnum"));
        monthlyRatioData.setResnum(rs.getInt("resnum"));
        monthlyRatioData.setOthernum(rs.getInt("othernum"));
        monthlyRatioData.setTotalnum(rs.getInt("totalnum"));
        
        monthlyRatioData.setPedemernumrate(rs.getDouble("pedemernumrate"));
        monthlyRatioData.setPedroomnumrate(rs.getDouble("pedroomnumrate"));
        monthlyRatioData.setResnumrate(rs.getDouble("resnumrate"));
        monthlyRatioData.setOthernumrate(rs.getDouble("othernumrate"));
        
		monthlyRatioData.setHosnum(rs.getInt("hosnum"));
        monthlyRatioData.setInnum(rs.getInt("innum"));
        monthlyRatioData.setInrate(rs.getDouble("innum")/rs.getInt("hosnum"));
        return monthlyRatioData;
	}

}
