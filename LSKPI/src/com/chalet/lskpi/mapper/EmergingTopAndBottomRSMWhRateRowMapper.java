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

public class EmergingTopAndBottomRSMWhRateRowMapper implements RowMapper<TopAndBottomRSMData> {

	@Override
	public TopAndBottomRSMData mapRow(ResultSet rs, int arg1)
			throws SQLException {
		TopAndBottomRSMData rsmData = new TopAndBottomRSMData();
		rsmData.setTopEmergingWhRate(rs.getDouble("whRateMax"));
		rsmData.setTopEmergingWhRateRSMName(rs.getString("whRateMaxUser"));
		rsmData.setBottomEmergingWhRate(rs.getDouble("whRateMin"));
		rsmData.setBottomEmergingWhRateRSMName(rs.getString("whRateMinUser"));
		return rsmData;
	}
}
