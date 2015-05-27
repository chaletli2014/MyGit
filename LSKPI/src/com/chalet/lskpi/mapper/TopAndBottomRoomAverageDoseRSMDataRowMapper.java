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

public class TopAndBottomRoomAverageDoseRSMDataRowMapper implements RowMapper<TopAndBottomRSMData> {

	@Override
	public TopAndBottomRSMData mapRow(ResultSet rs, int arg1)
			throws SQLException {
		TopAndBottomRSMData rsmData = new TopAndBottomRSMData();
		rsmData.setTopRoomAverageDose(rs.getDouble("averageDoseMax"));
		rsmData.setTopRoomAverageDoseRSMName(rs.getString("averageDoseMaxUser"));
		rsmData.setBottomRoomAverageDose(rs.getDouble("averageDoseMin"));
		rsmData.setBottomRoomAverageDoseRSMName(rs.getString("averageDoseMinUser"));
		return rsmData;
	}


}
