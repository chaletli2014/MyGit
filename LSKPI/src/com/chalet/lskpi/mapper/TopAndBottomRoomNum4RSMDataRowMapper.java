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

public class TopAndBottomRoomNum4RSMDataRowMapper implements RowMapper<TopAndBottomRSMData> {

	@Override
	public TopAndBottomRSMData mapRow(ResultSet rs, int arg1)
			throws SQLException {
		TopAndBottomRSMData rsmData = new TopAndBottomRSMData();
		rsmData.setTopRoomWhNum4(rs.getDouble("homeRoomNum4Max"));
		rsmData.setTopRoomWhNum4RSMName(rs.getString("homeRoomNum4MaxUser"));
		rsmData.setBottomRoomWhNum4(rs.getDouble("homeRoomNum4Min"));
		rsmData.setBottomRoomWhNum4RSMName(rs.getString("homeRoomNum4MinUser"));
		return rsmData;
	}


}
