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

public class TopAndBottomInRateRSMDataRowMapper implements RowMapper<TopAndBottomRSMData> {

	@Override
	public TopAndBottomRSMData mapRow(ResultSet rs, int arg1)
			throws SQLException {
		TopAndBottomRSMData rsmData = new TopAndBottomRSMData();
		rsmData.setTopInRate(rs.getDouble("inRateMax"));
		rsmData.setTopInRateRSMName(rs.getString("inRateMaxUser"));
		rsmData.setBottomInRate(rs.getDouble("inRateMin"));
		rsmData.setBottomInRateRSMName(rs.getString("inRateMinUser"));
		return rsmData;
	}


}
