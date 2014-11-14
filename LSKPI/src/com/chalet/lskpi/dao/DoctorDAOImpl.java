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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.DoctorRowMapper;
import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.utils.DataBean;

@Repository("doctorDAO")
public class DoctorDAOImpl implements DoctorDAO {

    @Autowired
    @Qualifier("dataBean")
    private DataBean dataBean;
    
    private Logger logger = Logger.getLogger(DoctorDAOImpl.class);
    
    public int getTotalDrNumOfHospital(String hospitalCode) throws Exception {
        StringBuffer sql = new StringBuffer("")
        .append(" select count(1) ")
        .append(" from tbl_doctor ")
        .append(" where hospitalCode=?");
        return dataBean.getJdbcTemplate().queryForInt(sql.toString(), new Object[]{hospitalCode});
    }
    
    public int getTotalRemovedDrNumOfHospital(String hospitalCode) throws Exception {
        StringBuffer sql = new StringBuffer("")
        .append(" select count(1) ")
        .append(" from tbl_doctor_history ")
        .append(" where hospitalCode=?");
        return dataBean.getJdbcTemplate().queryForInt(sql.toString(), new Object[]{hospitalCode});
    }
    
    public int getExistedDrNumByHospitalCode(String hospitalCode, String drName) throws Exception {
        StringBuffer sql = new StringBuffer("")
        .append(" select count(1) ")
        .append(" from tbl_doctor ")
        .append(" where hospitalCode=? and ( name = ? or name like ?)");
        return dataBean.getJdbcTemplate().queryForInt(sql.toString(), new Object[]{hospitalCode,drName,drName+"(%)"});
    }
    
    public int getExistedDrNumByHospitalCodeExcludeSelf(long dataId, String hospitalCode, String drName) throws Exception {
        StringBuffer sql = new StringBuffer("")
        .append(" select count(1) ")
        .append(" from tbl_doctor ")
        .append(" where hospitalCode=? and ( name = ? or name like ?) and id != ?");
        return dataBean.getJdbcTemplate().queryForInt(sql.toString(), new Object[]{hospitalCode,drName,drName+"(%)",dataId});
    }

    public int insertDoctor(final Doctor doctor) throws Exception {
        logger.info(">>HospitalDAOImpl insertDoctor");
        
        final String sql = "insert into tbl_doctor(id,name,code,hospitalCode,salesCode,createdate,modifydate) values(null,?,LPAD(?,4,'0'),?,?,date_sub(NOW(),interval 7 day),NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, doctor.getName());
                ps.setString(2, doctor.getCode());
                ps.setString(3, doctor.getHospitalCode());
                ps.setString(4, doctor.getSalesCode());
                return ps;
            }
        }, keyHolder);
        logger.info("insertDoctor,returned id is "+keyHolder.getKey().intValue());
        return keyHolder.getKey().intValue();
    }
    
    public void insertDoctors(final List<Doctor> doctors) throws Exception {
        logger.info(">>HospitalDAOImpl insertDoctors when uploading doctor");
        String insertSQL = "insert into tbl_doctor(id,name,code,hospitalCode,salesCode,createdate,modifydate) values(null,?,?,?,?,date_sub(NOW(),interval 7 day),now())";
        dataBean.getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
            
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, doctors.get(i).getName());
                ps.setString(2, doctors.get(i).getCode());
                ps.setString(3, doctors.get(i).getHospitalCode());
                ps.setString(4, doctors.get(i).getSalesCode());
            }
            
            @Override
            public int getBatchSize() {
                return doctors.size();
            }
        });
    }
    
    public void updateDoctorRelationship(int doctorId, String salesCode) throws Exception {
        StringBuffer sql = new StringBuffer("update tbl_doctor set ");
        sql.append("modifydate=NOW()");
        sql.append(", salesCode=? ");
        sql.append(" where id=? ");
        dataBean.getJdbcTemplate().update(sql.toString(), new Object[]{salesCode,doctorId});
    }

    public void updateDoctor(Doctor doctor) throws Exception {
        StringBuffer sql = new StringBuffer("update tbl_doctor set ");
        sql.append("modifydate=NOW()");
        sql.append(", name=? ");
        sql.append(" where id=? ");
        dataBean.getJdbcTemplate().update(sql.toString(), new Object[]{doctor.getName(),doctor.getId()});
    }
    
    public void backupDoctor(final Doctor doctor) throws Exception {
        final StringBuffer sql = new StringBuffer("")
        .append("insert into tbl_doctor_history(drName,drCode,doctorId,hospitalCode,salesCode,reason,createdate,modifydate)")
        .append(" select name as drName, code as drCode, id as doctorId,hospitalCode,salesCode,?,now(),now() from tbl_doctor ")
        .append(" where id=?");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql.toString(),Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, doctor.getReason());
                ps.setInt(2, doctor.getId());
                return ps;
            }
        }, keyHolder);
        logger.info("backupDoctor,returned id is "+keyHolder.getKey().intValue());
    }
    
    public void deleteDoctor(Doctor doctor) throws Exception {
        String sql = "delete from tbl_doctor where id=?";
        dataBean.getJdbcTemplate().update(sql, new Object[]{doctor.getId()});
    }
    
    public void cleanDoctor() throws Exception {
        dataBean.getJdbcTemplate().update("delete from tbl_doctor");
    }

    public Doctor getDoctorById(int doctorId) throws Exception {
        StringBuffer sb = new StringBuffer("");
        sb.append("select d.id, d.name as drName, d.code as drCode, d.hospitalCode,")
        .append(" h.name as hospitalName, ")
        .append(" d.salesCode, ")
        .append(" ( select distinct name from tbl_userinfo u where u.userCode = d.salesCode and u.superior = h.dsmCode and u.region = h.rsmRegion ) as salesName")
        .append(" from tbl_doctor d, tbl_hospital h ")
        .append(" where d.hospitalCode = h.code and d.id=? ");
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{doctorId},new DoctorRowMapper());
    }
    

    public List<Doctor> getDoctorsBySalesCode(String salesCode) throws Exception {
        List<Doctor> doctors = new ArrayList<Doctor>();
        StringBuffer sql = new StringBuffer("")
        .append(" select d.id ")
        .append(" , d.name as drName ")
        .append(" , d.code as drCode ")
        .append(" , h.code as hospitalCode ")
        .append(" , h.name as hospitalName ")
        .append(" , u.userCode as salesCode ")
        .append(" , u.name as salesName ")
        .append(" from tbl_userinfo u, tbl_doctor d, tbl_hospital h ")
        .append(" where u.superior = h.dsmCode and u.userCode = d.salesCode and d.hospitalCode = h.code and u.userCode = ?");
        doctors = dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{salesCode}, new DoctorRowMapper());
        return doctors;
    }
    
    public List<Doctor> getDoctorsByDsmCode(String dsmCode) throws Exception {
        List<Doctor> doctors = new ArrayList<Doctor>();
        StringBuffer sql = new StringBuffer("")
        .append(" select d.id ")
        .append(" , d.name as drName ")
        .append(" , d.code as drCode ")
        .append(" , h.code as hospitalCode ")
        .append(" , h.name as hospitalName ")
        .append(" , case when d.salesCode is null or d.salesCode='' or d.salesCode='#N/A' then 'Vacant' else d.salesCode end salesCode ")
        .append(" , case when d.salesCode is null or d.salesCode='' or d.salesCode='#N/A' then 'Vacant' else ")
        .append(" (select distinct u.name from tbl_userinfo u where u.region = h.rsmRegion and u.superior = h.dsmCode and u.userCode=d.salesCode) end salesName ")
        .append(" from tbl_doctor d, tbl_hospital h ")
        .append(" where d.hospitalCode = h.code and h.dsmCode=? ");
        doctors = dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{dsmCode}, new DoctorRowMapper());
        return doctors;
    }
    
    public List<Doctor> getDoctorsByRegion(String region) throws Exception {
        List<Doctor> doctors = new ArrayList<Doctor>();
        StringBuffer sql = new StringBuffer("")
        .append(" select d.id ")
        .append(" , d.name as drName ")
        .append(" , d.code as drCode ")
        .append(" , h.code as hospitalCode ")
        .append(" , h.name as hospitalName ")
        .append(" , case when d.salesCode is null or d.salesCode='' then 'Vacant' else d.salesCode end salesCode ")
        .append(" , case when d.salesCode is null or d.salesCode='' then 'Vacant' else ")
        .append(" (select distinct u.name from tbl_userinfo u where u.userCode=d.salesCode) end salesName ")
        .append(" from tbl_doctor d, tbl_hospital h ")
        .append(" where d.hospitalCode = h.code and h.rsmRegion=? ");
        doctors = dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{region}, new DoctorRowMapper());
        return doctors;
    }
    
    public List<Doctor> getDoctorsByRegionCenter(String regionCenter) throws Exception {
        List<Doctor> doctors = new ArrayList<Doctor>();
        StringBuffer sql = new StringBuffer("")
        .append(" select d.id ")
        .append(" , d.name as drName ")
        .append(" , d.code as drCode ")
        .append(" , h.code as hospitalCode ")
        .append(" , h.name as hospitalName ")
        .append(" , case when d.salesCode is null or d.salesCode='' then 'Vacant' else d.salesCode end salesCode ")
        .append(" , case when d.salesCode is null or d.salesCode='' then 'Vacant' else (select distinct u.name from tbl_userinfo u where u.userCode=d.salesCode) end salesName ")
        .append(" from tbl_doctor d, tbl_hospital h ")
        .append(" where d.hospitalCode = h.code and h.region=? ");
        doctors = dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{regionCenter}, new DoctorRowMapper());
        return doctors;
    }
    
    public boolean drHasLastWeekData(int doctorId, Date beginDate, Date endDate) throws Exception {
        int count = dataBean.getJdbcTemplate().queryForInt("select count(1) from tbl_home_data where doctorId=? and createdate between ? and ?", new Object[]{doctorId,new Timestamp(beginDate.getTime()),new Timestamp(endDate.getTime())});
        return count>0;
    }

    public DataBean getDataBean() {
        return dataBean;
    }

    public void setDataBean(DataBean dataBean) {
        this.dataBean = dataBean;
    }
}
