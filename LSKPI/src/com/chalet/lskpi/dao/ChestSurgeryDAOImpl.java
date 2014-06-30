package com.chalet.lskpi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.ChestSurgeryRowMapper;
import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.utils.DataBean;

@Repository("chestSurgeryDAO")
public class ChestSurgeryDAOImpl implements ChestSurgeryDAO {

    private Logger logger = Logger.getLogger(ChestSurgeryDAOImpl.class);
    
    @Autowired
    @Qualifier("dataBean")
    private DataBean dataBean;
    
    public ChestSurgeryData getChestSurgeryDataByHospital(String hospitalCode) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode")
        .append(" , (select name from tbl_userinfo u where u.userCode = h.saleCode and u.level='REP') as salesName ")
        .append(" , h.region, h.rsmRegion ")
        .append(" from tbl_chestSurgery_data cd, tbl_hospital h ")
        .append(" where cd.hospitalCode=? ")
        .append(" and DATE_FORMAT(cd.createdate,'%Y-%m-%d') = curdate() ")
        .append(" and cd.hospitalCode = h.code");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{hospitalCode}, new ChestSurgeryRowMapper());
    }

    public List<ChestSurgeryData> getChestSurgeryDataByDate(Date createdatebegin, Date createdateend) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode ")
        .append(" , (select name from tbl_userinfo u where u.userCode = h.saleCode and u.level='REP') as salesName ")
        .append(" , h.region, h.rsmRegion ")
        .append(" from tbl_chestSurgery_data cd, tbl_hospital h ")
        .append(" where cd.createdate between ? and ? ")
        .append(" and cd.hospitalCode = h.code ")
        .append(" order by cd.createdate desc");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{new Timestamp(createdatebegin.getTime()),new Timestamp(createdateend.getTime())},new ChestSurgeryRowMapper());
    }

    public ChestSurgeryData getChestSurgeryDataByHospitalAndDate(String hospitalCode, Date createdate) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode ")
        .append(" , (select name from tbl_userinfo u where u.userCode = h.saleCode and u.level='REP') as salesName ")
        .append(" , h.region, h.rsmRegion ")
        .append(" from tbl_chestSurgery_data cd, tbl_hospital h ")
        .append(" where cd.hospitalCode = ? ")
        .append(" and DATE_FORMAT(cd.createdate,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') ")
        .append(" and cd.hospitalCode = h.code ")
        .append(" order by cd.createdate desc");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{hospitalCode,new Timestamp(createdate.getTime())}, new ChestSurgeryRowMapper());
    }

    public ChestSurgeryData getChestSurgeryDataById(int id) throws Exception {
        StringBuffer sql = new StringBuffer("");
        sql.append(" select cd.* ")
        .append(" , h.code as hospitalCode, h.name as hospitalName, h.dsmName, h.saleCode as salesCode ")
        .append(" , (select name from tbl_userinfo u where u.userCode = h.saleCode and u.level='REP') as salesName ")
        .append(" , h.region, h.rsmRegion ")
        .append(" from tbl_chestSurgery_data cd, tbl_hospital h ")
        .append(" where cd.id = ? ")
        .append(" and cd.hospitalCode = h.code ");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{id}, new ChestSurgeryRowMapper());
    }

    public void insert(final ChestSurgeryData chestSurgeryData, UserInfo operator, Hospital hospital) throws Exception {
        logger.info(">>ChestSurgeryDAOImpl insert");
        
        final String sql = "insert into tbl_chestSurgery_data values(null,NOW(),?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, chestSurgeryData.getHospitalCode());
                ps.setInt(2, chestSurgeryData.getPnum());
                ps.setInt(3, chestSurgeryData.getRisknum());
                ps.setInt(4, chestSurgeryData.getWhnum());
                ps.setInt(5, chestSurgeryData.getLsnum());
                ps.setDouble(6, chestSurgeryData.getOqd());
                ps.setDouble(7, chestSurgeryData.getTqd());
                ps.setDouble(8, chestSurgeryData.getOtid());
                ps.setDouble(9, chestSurgeryData.getTbid());
                ps.setDouble(10, chestSurgeryData.getTtid());
                ps.setDouble(11, chestSurgeryData.getThbid());
                ps.setDouble(12, chestSurgeryData.getFbid());
                return ps;
            }
        }, keyHolder);
        logger.info("insert chest surgery returned id is "+keyHolder.getKey().intValue());
    }

    public void insert(final ChestSurgeryData chestSurgeryData, String dsmCode) throws Exception {
        logger.info(">>ChestSurgeryDAOImpl insert - upload daily data");
        
        final String sql = "insert into tbl_chestSurgery_data values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setTimestamp(1, new Timestamp(chestSurgeryData.getCreatedate().getTime()));
                ps.setInt(2, chestSurgeryData.getPnum());
                ps.setInt(3, chestSurgeryData.getRisknum());
                ps.setInt(4, chestSurgeryData.getWhnum());
                ps.setInt(5, chestSurgeryData.getLsnum());
                ps.setDouble(6, chestSurgeryData.getOqd());
                ps.setDouble(7, chestSurgeryData.getTqd());
                ps.setDouble(8, chestSurgeryData.getOtid());
                ps.setDouble(9, chestSurgeryData.getTbid());
                ps.setDouble(10, chestSurgeryData.getTtid());
                ps.setDouble(11, chestSurgeryData.getThbid());
                ps.setDouble(12, chestSurgeryData.getFbid());
                return ps;
            }
        }, keyHolder);
        logger.info("upload daily data, returned id is "+keyHolder.getKey().intValue());
    }

    public void update(ChestSurgeryData chestSurgeryData) throws Exception {
        StringBuffer sql = new StringBuffer("update tbl_chestSurgery_data set ");
        sql.append("updatedate=NOW()");
        sql.append(", pnum=? ");
        sql.append(", risknum=? ");
        sql.append(", whnum=? ");
        sql.append(", lsnum=? ");
        sql.append(", oqd=? ");
        sql.append(", tqd=? ");
        sql.append(", otid=? ");
        sql.append(", tbid=? ");
        sql.append(", ttid=? ");
        sql.append(", thbid=? ");
        sql.append(", fbid=? ");
        sql.append(" where id=? ");
        
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(chestSurgeryData.getPnum());
        paramList.add(chestSurgeryData.getRisknum());
        paramList.add(chestSurgeryData.getWhnum());
        paramList.add(chestSurgeryData.getLsnum());
        paramList.add(chestSurgeryData.getOqd());
        paramList.add(chestSurgeryData.getTqd());
        paramList.add(chestSurgeryData.getOtid());
        paramList.add(chestSurgeryData.getTbid());
        paramList.add(chestSurgeryData.getTtid());
        paramList.add(chestSurgeryData.getThbid());
        paramList.add(chestSurgeryData.getFbid());
        paramList.add(chestSurgeryData.getDataId());
        
        dataBean.getJdbcTemplate().update(sql.toString(), paramList.toArray());
    }

}
