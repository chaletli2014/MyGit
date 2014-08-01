package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.chalet.lskpi.model.WeeklyDataOfHospital;

public class WeeklyHospitalDataRowMapper implements RowMapper<WeeklyDataOfHospital>{

    public WeeklyDataOfHospital mapRow(ResultSet rs, int arg1) throws SQLException {
        WeeklyDataOfHospital hosData = new WeeklyDataOfHospital();
        hosData.setDuration(rs.getString("duration"));
        hosData.setHospitalCode(rs.getString("hospitalCode"));
        hosData.setPnum(rs.getDouble("pnum"));
        hosData.setLsnum(rs.getDouble("lsnum"));
        hosData.setAverageDose(rs.getDouble("averageDose"));
        return hosData;
    }

}
