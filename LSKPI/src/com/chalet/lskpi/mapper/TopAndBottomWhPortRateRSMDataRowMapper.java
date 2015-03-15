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

public class TopAndBottomWhPortRateRSMDataRowMapper implements RowMapper<TopAndBottomRSMData> {

	@Override
	public TopAndBottomRSMData mapRow(ResultSet rs, int arg1)
			throws SQLException {
		TopAndBottomRSMData rsmData = new TopAndBottomRSMData();
		rsmData.setTopWhPortRate(rs.getDouble("whPortRateMax"));
		rsmData.setTopWhPortRateRSMName(rs.getString("whPortRateMaxUser"));
		rsmData.setBottomWhPortRate(rs.getDouble("whPortRateMin"));
		rsmData.setBottomWhPortRateRSMName(rs.getString("whPortRateMinUser"));
		return rsmData;
	}


}
