package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.TopAndBottomRSMData;

/**
 * @author Chalet
 * @version 创建时间：2013年12月28日 下午11:23:08
 * 类说明
 */

public class TopAndBottomEmergingNum3RSMDataRowMapper implements RowMapper<TopAndBottomRSMData> {

	@Override
	public TopAndBottomRSMData mapRow(ResultSet rs, int arg1)
			throws SQLException {
		TopAndBottomRSMData rsmData = new TopAndBottomRSMData();
		rsmData.setTopEmergingWhNum3(rs.getDouble("homeAverNum3Max"));
		rsmData.setTopEmergingWhNum3RSMName(rs.getString("homeAverNum3MaxUser"));
		rsmData.setBottomEmergingWhNum3(rs.getDouble("homeAverNum3Min"));
		rsmData.setBottomEmergingWhNum3RSMName(rs.getString("homeAverNum3MinUser"));
		return rsmData;
	}


}
