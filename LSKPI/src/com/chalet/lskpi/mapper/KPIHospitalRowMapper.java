package com.chalet.lskpi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.chalet.lskpi.model.KPIHospital4Export;

public class KPIHospitalRowMapper implements RowMapper<KPIHospital4Export>{

    public KPIHospital4Export mapRow(ResultSet rs, int i) throws SQLException {
        KPIHospital4Export dbHospital = new KPIHospital4Export();
        dbHospital.setProvince(rs.getString("province"));
        dbHospital.setCity(rs.getString("city"));
        dbHospital.setCode(rs.getString("code"));
        dbHospital.setName(rs.getString("name"));
        dbHospital.setDragonType(rs.getString("dragonType"));
        dbHospital.setLevel(rs.getString("level"));
        dbHospital.setBrCNName(rs.getString("brCNName"));
        dbHospital.setRegion(rs.getString("region"));
        dbHospital.setRsdCode(rs.getString("rsdCode"));
        dbHospital.setRsdName(rs.getString("rsdName"));
        dbHospital.setRsdTel(rs.getString("rsdTel"));
        dbHospital.setRsdEmail(rs.getString("rsdEmail"));
        dbHospital.setRsmRegion(rs.getString("rsmRegion"));
        dbHospital.setRsmCode(rs.getString("rsmCode"));
        dbHospital.setRsmName(rs.getString("rsmName"));
        dbHospital.setRsmTel(rs.getString("rsmTel"));
        dbHospital.setRsmEmail(rs.getString("rsmEmail"));
        dbHospital.setDsmCode(rs.getString("dsmCode"));
        dbHospital.setDsmName(rs.getString("dsmName"));
        dbHospital.setDsmTel(rs.getString("dsmTel"));
        dbHospital.setDsmEmail(rs.getString("dsmEmail"));
        dbHospital.setIsMainSales(rs.getString("isMainSales"));
        dbHospital.setSalesCode(rs.getString("salesCode"));
        dbHospital.setSalesName(rs.getString("salesName"));
        dbHospital.setSalesTel(rs.getString("salesTel"));
        dbHospital.setSalesEmail(rs.getString("salesEmail"));
        dbHospital.setIsTop100(rs.getString("isTop100"));
        dbHospital.setPortNum(rs.getInt("portNum"));
        return dbHospital;
    }

}
