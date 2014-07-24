package com.chalet.lskpi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
import com.chalet.lskpi.mapper.HospitalRowMapper;
import com.chalet.lskpi.mapper.HospitalSalesQueryRowMapper;
import com.chalet.lskpi.mapper.Monthly12DataRowMapper;
import com.chalet.lskpi.mapper.MonthlyCollectionDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyInRateDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyRatioDataRowMapper;
import com.chalet.lskpi.mapper.UserInfoRowMapper;
import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.HospitalSalesQueryObj;
import com.chalet.lskpi.model.HospitalSalesQueryParam;
import com.chalet.lskpi.model.Monthly12Data;
import com.chalet.lskpi.model.MonthlyData;
import com.chalet.lskpi.model.MonthlyInRateData;
import com.chalet.lskpi.model.MonthlyRatioData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.LsAttributes;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午5:07:36
 * 类说明
 */

@Repository("hospitalDAO")
public class HospitalDAOImpl implements HospitalDAO {

	@Autowired
	@Qualifier("dataBean")
	private DataBean dataBean;
	
	private Logger logger = Logger.getLogger(HospitalDAOImpl.class);
	
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

    public void insertDoctor(final Doctor doctor) throws Exception {
        logger.info(">>HospitalDAOImpl insertDoctor");
        
        final String sql = "insert into tbl_doctor values(null,?,LPAD(?,4,'0'),?,?,NOW(),NOW())";
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
    }
    
    public void insertDoctors(final List<Doctor> doctors) throws Exception {
        logger.info(">>HospitalDAOImpl insertDoctors when uploading doctor");
        String insertSQL = "insert into tbl_doctor values(null,?,?,?,?,now(),now())";
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
	   
    public List<Hospital> getHospitalsOfHomeCollectionByPSRTel(String telephone) throws Exception {
        List<Hospital> hospitals = new ArrayList<Hospital>();
        StringBuffer sql = new StringBuffer("")
        .append(" select h.id,h.code ")
        .append(" , h.name ")
        .append(" , h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode ")
        .append(" from tbl_userinfo u, tbl_hos_user hu, tbl_hospital h ")
        .append(" where u.userCode = hu.userCode and hu.hosCode = h.code and u.telephone = ? ");
        hospitals = dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{telephone}, new HospitalRowMapper());
        return hospitals;
    }
    
    public List<Hospital> getHospitalsOfHomeCollectionByDSMTel(String telephone) throws Exception {
        List<Hospital> hospitals = new ArrayList<Hospital>();
        StringBuffer sql = new StringBuffer();
        sql.append(" select h.id,h.code ")
            .append(" , h.name")
            .append(" , h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode ")
            .append(" from tbl_hospital h, tbl_userinfo ui ")
            .append(" where h.dsmCode = ui.userCode and ui.telephone = ? ")
            .append(" order by h.name asc ");
        hospitals = dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{telephone}, new HospitalRowMapper());
        return hospitals;
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
	
    public List<Monthly12Data> getRSD12MontlyDataByRegionCenter(String regionCenter) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(" select md.countMonth as dataMonth")
        .append(" , ( select count(1) from tbl_hospital h where h.region = ? and h.isMonthlyAssessed='1' ) as hosNum")
        .append(" , count(1) as innum ")
        .append(LsAttributes.SQL_MONTHLY_12_SELECT)
        .append(" from tbl_month_data md, tbl_hospital h")
        .append(" where md.hospitalCode = h.code ")
        .append(" and h.isMonthlyAssessed='1' ")
        .append(" and h.region=? ")
        .append(LsAttributes.SQL_MONTHLY_12_GROUP);
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{regionCenter,regionCenter}, new Monthly12DataRowMapper());
    }

    public List<Monthly12Data> getRSM12MontlyDataByRegion(String region) throws Exception {
    	StringBuffer sb = new StringBuffer();
        sb.append(" select md.countMonth as dataMonth")
            .append(" , ( select count(1) from tbl_hospital h where h.rsmRegion = ? and h.isMonthlyAssessed='1' ) as hosNum")
            .append(" , count(1) as innum ")
            .append(LsAttributes.SQL_MONTHLY_12_SELECT)
            .append(" from tbl_month_data md, tbl_hospital h")
            .append(" where md.hospitalCode = h.code ")
            .append(" and h.isMonthlyAssessed='1' ")
            .append(" and h.rsmRegion=? ")
            .append(LsAttributes.SQL_MONTHLY_12_GROUP);
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{region,region}, new Monthly12DataRowMapper());
    }

    public List<Monthly12Data> getDSM12MontlyDataByDSMCode(String dsmCode) throws Exception {
    	StringBuffer sb = new StringBuffer();
        sb.append(" select md.countMonth as dataMonth")
            .append(" , ( select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.dsmCode = u.userCode and h.isMonthlyAssessed='1' ) as hosNum")
            .append(" , count(1) as innum ")
            .append(LsAttributes.SQL_MONTHLY_12_SELECT)
            .append(" from tbl_userinfo u, tbl_month_data md, tbl_hospital h")
            .append(" where u.region = h.rsmRegion ")
            .append(" and u.userCode = h.dsmCode ")
            .append(" and md.hospitalCode = h.code ")
            .append(" and h.isMonthlyAssessed='1' ")
            .append(" and u.userCode=? ")
            .append(LsAttributes.SQL_MONTHLY_12_GROUP);
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{dsmCode}, new Monthly12DataRowMapper());
    }
    
    public List<Monthly12Data> get12MontlyDataByCountory() throws Exception {
    	StringBuffer sb = new StringBuffer();
    	sb.append(" select md.countMonth as dataMonth")
    	.append(" , ( select count(1) from tbl_hospital h where h.isMonthlyAssessed='1' ) as hosNum")
    	.append(" , count(1) as innum ")
    	.append(LsAttributes.SQL_MONTHLY_12_SELECT)
    	.append(" from tbl_month_data md, tbl_hospital h")
    	.append(" where md.hospitalCode = h.code ")
    	.append(" and h.isMonthlyAssessed='1' ")
    	.append(LsAttributes.SQL_MONTHLY_12_GROUP);
    	return dataBean.getJdbcTemplate().query(sb.toString(), new Monthly12DataRowMapper());
    }

	@Override
	public List<MonthlyData> getMonthlyDataByDate(Date startDate, Date endDate)
			throws Exception {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select md.id, md.pedEmernum, md.pedroomnum, md.resnum, md.other, md.operatorName, md.operatorCode, md.hospitalCode,  h.region, h.rsmRegion, md.createdate ")
			.append(" ,h.dsmName, h.name as hospitalName ")
			.append(" from tbl_month_data md, tbl_hospital h")
			.append(" where md.hospitalCode = h.code ")
			.append(" and DATE_FORMAT(md.createdate,'%Y-%m-%d') between DATE_FORMAT(?,'%Y-%m-%d') and DATE_FORMAT(?,'%Y-%m-%d') ")
			.append(" order by md.createdate desc");
        return dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{new Timestamp(startDate.getTime()),new Timestamp(endDate.getTime())},new MonthlyDataRowMapper());
	}
	
	public List<Hospital> getAllHospitals() throws Exception{
	    String searchSQL = "select * from tbl_hospital where isPedAssessed='1' or isResAssessed='1'";
        return dataBean.getJdbcTemplate().query(searchSQL, new HospitalRowMapper());
	}
	
	public List<MonthlyRatioData> getMonthlyDataOfREPByDSMCode(String dsmCode) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(LsAttributes.SQL_MONTHLY_RATIO_SELECT)
		.append(", lastMonthData.saleName, '' as dsmName, lastMonthData.rsmRegion, ( select distinct property_value from tbl_property where property_name = lastMonthData.region ) as region ")
		.append(" from (")
		.append(LsAttributes.SQL_MONTHLY_RATIO_LASTMONTH_SELECT_REP)
		.append(") lastMonthData ")
		.append("inner join ( ")
		.append(LsAttributes.SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_REP)
		.append(") last2MonthData on lastMonthData.saleCode = last2MonthData.saleCode ")
		.append(" and lastMonthData.dsmCode = last2MonthData.dsmCode and lastMonthData.rsmRegion = last2MonthData.rsmRegion");
		return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{dsmCode,dsmCode,dsmCode,dsmCode}, new MonthlyRatioDataRowMapper());
	}
	
	public List<MonthlyRatioData> getMonthlyDataOfDSMByRsmRegion(String rsmRegion) throws Exception {
	    StringBuffer sb = new StringBuffer();
	    sb.append(LsAttributes.SQL_MONTHLY_RATIO_SELECT)
	        .append(", '' as saleName, lastMonthData.dsmName, lastMonthData.rsmRegion, ( select distinct property_value from tbl_property where property_name = lastMonthData.region ) as region ")
	        .append(" from (")
	        .append(LsAttributes.SQL_MONTHLY_RATIO_LASTMONTH_SELECT_DSM)
	        .append(") lastMonthData ")
	        .append("inner join ( ")
	        .append(LsAttributes.SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_DSM)
	        .append(") last2MonthData on lastMonthData.dsmName = last2MonthData.dsmName and lastMonthData.rsmRegion = last2MonthData.rsmRegion");
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{rsmRegion,rsmRegion,rsmRegion,rsmRegion}, new MonthlyRatioDataRowMapper());
    }
	
	public MonthlyRatioData getMonthlyDataOfSingleRsm(String rsmRegion) throws Exception{
	    StringBuffer sb = new StringBuffer();
        sb.append(LsAttributes.SQL_MONTHLY_RATIO_SELECT)
            .append(", '' as saleName, '' as dsmName, lastMonthData.rsmRegion, ( select distinct property_value from tbl_property where property_name = lastMonthData.region ) as region ")
            .append(" from (")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LASTMONTH_SELECT_BELONG_RSM)
            .append(") lastMonthData ")
            .append("inner join ( ")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_BELONG_RSM)
            .append(") last2MonthData on lastMonthData.rsmRegion = last2MonthData.rsmRegion");
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{rsmRegion,rsmRegion,rsmRegion,rsmRegion}, new MonthlyRatioDataRowMapper());
	}

    public List<MonthlyRatioData> getMonthlyDataOfRSMByRegion(String region) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(LsAttributes.SQL_MONTHLY_RATIO_SELECT)
            .append(", '' as saleName, '' as dsmName, lastMonthData.rsmRegion, ( select distinct property_value from tbl_property where property_name = lastMonthData.region ) as region ")
            .append(" from (")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LASTMONTH_SELECT_RSM)
            .append(") lastMonthData ")
            .append("inner join ( ")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_RSM)
            .append(") last2MonthData on lastMonthData.rsmRegion = last2MonthData.rsmRegion");
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{region,region,region,region}, new MonthlyRatioDataRowMapper());
    }
    public MonthlyRatioData getMonthlyDataOfSingleRsd(String region) throws Exception{
        StringBuffer sb = new StringBuffer();
        sb.append(LsAttributes.SQL_MONTHLY_RATIO_SELECT)
            .append(", '' as saleName, '' as dsmName, '' as rsmRegion, ( select distinct property_value from tbl_property where property_name = lastMonthData.region ) as region ")
            .append(" from (")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LASTMONTH_SELECT_BELONG_RSD)
            .append(") lastMonthData ")
            .append("inner join ( ")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_BELONG_RSD)
            .append(") last2MonthData on lastMonthData.region = last2MonthData.region");
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{region,region,region,region}, new MonthlyRatioDataRowMapper());
    }

    public List<MonthlyRatioData> getMonthlyDataOfRSD() throws Exception {
    	StringBuffer sb = new StringBuffer();
        sb.append(LsAttributes.SQL_MONTHLY_RATIO_SELECT)
            .append(", '' as saleName, '' as dsmName, '' as rsmRegion, ( select distinct property_value from tbl_property where property_name = lastMonthData.region ) as region ")
            .append(" from ( ")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LASTMONTH_SELECT_RSD)
            .append(") lastMonthData ")
            .append(" inner join ( ")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_RSD)
            .append(") last2MonthData on lastMonthData.region = last2MonthData.region ")
            .append(" order by region ");
        return dataBean.getJdbcTemplate().query(sb.toString(), new MonthlyRatioDataRowMapper());
    }
    public MonthlyRatioData getMonthlyDataOfCountory() throws Exception{
        StringBuffer sb = new StringBuffer();
        sb.append(LsAttributes.SQL_MONTHLY_RATIO_SELECT)
        	.append(", '' as saleName, '' as dsmName, '' as rsmRegion, '' as region ")
            .append(" from (")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LASTMONTH_SELECT_BELONG_COUNTORY)
            .append(") lastMonthData ")
            .append("inner join ( ")
            .append(LsAttributes.SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_BELONG_COUNTORY)
            .append(") last2MonthData ");
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new MonthlyRatioDataRowMapper());
    }

    public List<MonthlyRatioData> getMonthlyDataOfBUByTel(String telephone) throws Exception {
        StringBuffer sb = new StringBuffer();
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{telephone}, new MonthlyRatioDataRowMapper());
    }

	@Override
	public MonthlyData getMonthlyData(String hospitalCode, Date date)
			throws Exception {
	    Calendar paramDate = Calendar.getInstance();
	    paramDate.setTime(date);
	    int month = paramDate.get(Calendar.MONTH);
	    String param_month = paramDate.get(Calendar.YEAR)+"-"+month;
	    if( month < 10 ){
	    	param_month = paramDate.get(Calendar.YEAR)+"-0"+month;
	    }
	    
		logger.info(String.format("get the monthly data, month is %s, hospitalCode is %s", month, hospitalCode));
		StringBuffer sql = new StringBuffer("");
		sql.append(" select md.id, md.pedEmernum, md.pedroomnum, md.resnum, md.other, md.operatorName, md.operatorCode, md.hospitalCode,  h.region, h.rsmRegion, md.createdate ")
			.append(" ,h.dsmName, h.name as hospitalName ")
			.append(" from tbl_month_data md, tbl_hospital h")
			.append(" where md.hospitalCode = h.code ")
			.append(" and md.hospitalCode = ? ")
			.append(" and md.countMonth = ?");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{hospitalCode,param_month}, new MonthlyDataRowMapper());
	}
	
	@Override
	public MonthlyData getMonthlyDataById( int id )
	        throws Exception {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select md.id, md.pedEmernum, md.pedroomnum, md.resnum, md.other, md.operatorName, md.operatorCode, md.hospitalCode,  h.region, h.rsmRegion, md.createdate ")
			.append(" ,h.dsmName, h.name as hospitalName ")
			.append(" from tbl_month_data md, tbl_hospital h")
			.append(" where  md.hospitalCode = h.code")
			.append(" and md.id = ?");
        return dataBean.getJdbcTemplate().queryForObject(sql.toString(), new Object[]{id}, new MonthlyDataRowMapper());
	}

    public void updateMonthlyData(MonthlyData monthlyData) throws Exception {
        StringBuffer sql = new StringBuffer("update tbl_month_data set ")
        .append("updatedate=NOW()")
        .append(", pedEmernum=? ")
        .append(", pedroomnum=? ")
        .append(", resnum=? ")
        .append(", other=? ")
        .append(", operatorName=? ")
        .append(" where id=? ");
        
        List<Object> paramList = new ArrayList<Object>();
        paramList.add(monthlyData.getPedemernum());
        paramList.add(monthlyData.getPedroomnum());
        paramList.add(monthlyData.getResnum());
        paramList.add(monthlyData.getOthernum());
        paramList.add(monthlyData.getOperatorName());
        paramList.add(monthlyData.getId());
        
        dataBean.getJdbcTemplate().update(sql.toString(), paramList.toArray());
    }

	@Override
	public List<Hospital> getHospitalsByKeywords(String keywords)
			throws Exception {
		String searchSQL = "select * from tbl_hospital where name like '"+keywords+"' order by isResAssessed desc, isPedAssessed desc, isChestSurgeryAssessed desc,name asc";
		logger.info("searchSQL is " + searchSQL);
		return dataBean.getJdbcTemplate().query(searchSQL, new HospitalRowMapper());
	}
	
	@Override
	public void insert(final List<Hospital> hospitals) throws Exception {
		String insertSQL = "insert into tbl_hospital values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		dataBean.getJdbcTemplate().batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, hospitals.get(i).getName());
				ps.setString(2, hospitals.get(i).getCity());
				ps.setString(3, hospitals.get(i).getProvince());
				ps.setString(4, hospitals.get(i).getRegion());
				ps.setString(5, hospitals.get(i).getRsmRegion());
				ps.setString(6, hospitals.get(i).getLevel());
				ps.setString(7, hospitals.get(i).getCode());
				ps.setString(8, hospitals.get(i).getDsmCode());
				ps.setString(9, hospitals.get(i).getDsmName());
				ps.setString(10, hospitals.get(i).getSaleName());
				ps.setString(11, hospitals.get(i).getDragonType());
				ps.setString(12, hospitals.get(i).getIsResAssessed());
				ps.setString(13, hospitals.get(i).getIsPedAssessed());
				ps.setString(14, hospitals.get(i).getSaleCode());
				ps.setString(15, hospitals.get(i).getIsMonthlyAssessed());
				ps.setString(16, hospitals.get(i).getIsChestSurgeryAssessed());
				ps.setString(17, hospitals.get(i).getIsTop100());
			}
			
			@Override
			public int getBatchSize() {
				return hospitals.size();
			}
		});
	}
	
	@Override
	public void insertMonthlyData(final MonthlyData monthlyData) throws Exception {
	    final String insertSQL = "insert into tbl_month_data values(null,?,?,?,?,?,?,?,?,?,?,?,now(),?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(insertSQL,Statement.RETURN_GENERATED_KEYS);
                ps.setDouble(1, monthlyData.getPedemernum());
                ps.setDouble(2, monthlyData.getPedroomnum());
                ps.setDouble(3, monthlyData.getResnum());
                ps.setDouble(4, monthlyData.getOthernum());
                ps.setString(5, monthlyData.getOperatorName());
                ps.setString(6, monthlyData.getOperatorCode());
                ps.setString(7, monthlyData.getHospitalCode());
                ps.setString(8, monthlyData.getDsmCode());
                ps.setString(9, monthlyData.getRsmRegion());
                ps.setString(10, monthlyData.getRegion());
                ps.setTimestamp(11, new Timestamp(monthlyData.getCreateDate().getTime()));
                
                Calendar createCal = Calendar.getInstance();
                createCal.setTime(monthlyData.getCreateDate());
                int month = createCal.get(Calendar.MONTH);
                
                if( month > 9 ){
                    ps.setString(12, createCal.get(Calendar.YEAR)+"-"+month);
                }else{
                    ps.setString(12, createCal.get(Calendar.YEAR)+"-0"+month);
                }
                return ps;
            }
        }, keyHolder);
        logger.info("returned id is "+keyHolder.getKey().intValue());
	}
	
	@Override
	public Hospital getHospitalByName(String hospitalName) throws Exception {
		Hospital hospital = new Hospital();
		String sql = "select * from tbl_hospital where name = ?";
		hospital = dataBean.getJdbcTemplate().queryForObject(sql, new Object[]{hospitalName}, new HospitalRowMapper());
		return hospital;
	}
	
	@Override
	public Hospital getHospitalByCode(String hospitalCode) throws Exception {
	    Hospital hospital = new Hospital();
	    String sql = "select * from tbl_hospital where code = ?";
	    hospital = dataBean.getJdbcTemplate().queryForObject(sql, new Object[]{hospitalCode}, new HospitalRowMapper());
	    return hospital;
	}
	
	@Override
	public List<Hospital> getHospitalsByDSMTel(String telephone, String department) throws Exception {
	    
	    StringBuffer sb = new StringBuffer();
        sb.append("select h.id,h.code ")
            .append(", h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode ");
        
        if( LsAttributes.DEPARTMENT_PED.equalsIgnoreCase(department) ){
            sb.append(", case when h.isPedAssessed='1' then concat('* ',h.name) else h.name end name ");
        }else if( LsAttributes.DEPARTMENT_RES.equalsIgnoreCase(department) ){
            sb.append(", case when h.isResAssessed='1' then concat('* ',h.name) else h.name end name ");
        }else if( LsAttributes.DEPARTMENT_CHE.equalsIgnoreCase(department) ){
            sb.append(", case when h.isChestSurgeryAssessed='1' then concat('* ',h.name) else h.name end name");
        }
        
        sb.append(" from tbl_userinfo u, tbl_hospital h ")
            .append(" where u.userCode = h.dsmCode and u.telephone = ? ");
        
        if( LsAttributes.DEPARTMENT_PED.equalsIgnoreCase(department) ){
            sb.append(" order by h.isPedAssessed desc, h.name asc");
        }else if( LsAttributes.DEPARTMENT_RES.equalsIgnoreCase(department) ){
            sb.append(" order by h.isResAssessed desc, h.name asc");
        }else if( LsAttributes.DEPARTMENT_CHE.equalsIgnoreCase(department) ){
            sb.append(" order by h.isChestSurgeryAssessed desc, h.name asc");
        }
        
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{telephone}, new HospitalRowMapper());
	}
	
	@Override
	public List<Hospital> getMonthlyHospitalsByUserTel(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer();
	    sb.append("select h.id,h.code ")
	    	.append(", case when h.isMonthlyAssessed='1' then concat('* ',h.name) else h.name end name")
	    	.append(", h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode ")
	        .append(" from tbl_hospital h, tbl_userinfo ui, tbl_hos_user hu ")
	        .append(" where ui.userCode = hu.userCode and hu.hosCode = h.code and ui.telephone = ? order by h.isMonthlyAssessed desc, h.name asc");
	    return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{telephone}, new HospitalRowMapper());
	}
	
	@Override
	public List<Hospital> getMonthlyHospitalsByDSMTel(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer();
	    sb.append("select h.id,h.code ")
    		.append(", case when h.isMonthlyAssessed='1' then concat('* ',h.name) else h.name end name")
    		.append(", h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode ")
            .append(" from tbl_hospital h, tbl_userinfo ui ")
            .append(" where h.dsmCode = ui.userCode and ui.telephone = ? order by h.isMonthlyAssessed desc, h.name asc");
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{telephone}, new HospitalRowMapper());
	}
	
	@Override
	public List<Hospital> getHospitalsByUserTel(String telephone, String department) throws Exception {
	    
	    StringBuffer sb = new StringBuffer();
        sb.append("select h.id,h.code ")
            .append(", h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode ");
        
        if( LsAttributes.DEPARTMENT_PED.equalsIgnoreCase(department) ){
            sb.append(", case when h.isPedAssessed='1' then concat('* ',h.name) else h.name end name ");
        }else if( LsAttributes.DEPARTMENT_RES.equalsIgnoreCase(department) ){
            sb.append(", case when h.isResAssessed='1' then concat('* ',h.name) else h.name end name ");
        }else if( LsAttributes.DEPARTMENT_CHE.equalsIgnoreCase(department) ){
            sb.append(", case when h.isChestSurgeryAssessed='1' then concat('* ',h.name) else h.name end name ");
        }
        
        sb.append(" from tbl_userinfo u, tbl_hos_user hu, tbl_hospital h ")
            .append(" where u.userCode = hu.userCode and hu.hosCode = h.code and u.telephone = ? ");
        
        if( LsAttributes.DEPARTMENT_PED.equalsIgnoreCase(department) ){
            sb.append(" order by h.isPedAssessed desc, h.name asc");
        }else if( LsAttributes.DEPARTMENT_RES.equalsIgnoreCase(department) ){
            sb.append(" order by h.isResAssessed desc, h.name asc");
        }else if( LsAttributes.DEPARTMENT_CHE.equalsIgnoreCase(department) ){
            sb.append(" order by h.isChestSurgeryAssessed desc, h.name asc");
        }
        
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{telephone}, new HospitalRowMapper());
	}
	
	public UserInfo getPrimarySalesOfHospital(String hospitalCode) throws Exception {
	    UserInfo primarySales = new UserInfo();
        String sql = "select ui.*, (select distinct property_value from tbl_property where property_name=ui.regionCenter) as regionCenterCN from tbl_hospital h, tbl_userinfo ui where h.code = ? and h.saleCode = ui.userCode and h.dsmCode = ui.superior";
        primarySales = dataBean.getJdbcTemplate().queryForObject(sql, new Object[]{hospitalCode}, new UserInfoRowMapper());
        return primarySales;
	}
	
	public List<HospitalSalesQueryObj> getHospitalSalesList(HospitalSalesQueryParam queryParam) throws Exception {
	    StringBuffer hosSalesSQL = new StringBuffer();
	    if( null != queryParam && "1".equalsIgnoreCase(queryParam.getDepartment()) ){
	        hosSalesSQL.append(LsAttributes.SQL_WEEKLY_HOS_SALES_DATA)
            .append(" from ( ")
            .append("   select hospitalCode, ( select name from tbl_hospital where code = hospitalCode ) as hospitalName, ")
            .append(LsAttributes.SQL_WEEKLY_HOS_SALES_DATA_LASTWEEK_SELECT_RES)
            .append(") lastweekdata, ")
            .append("( ")
            .append("   select hospitalCode, ")
            .append(LsAttributes.SQL_WEEKLY_HOS_SALES_DATA_LAST2WEEK_SELECT_RES)
            .append(") last2weekdata ")
            .append(" where lastweekdata.hospitalCode = last2weekdata.hospitalCode ");
	    }else{
	        hosSalesSQL.append(LsAttributes.SQL_WEEKLY_HOS_SALES_DATA)
	        .append(" from ( ")
	        .append("   select hospitalCode, ( select name from tbl_hospital where code = hospitalCode ) as hospitalName, ")
	        .append(LsAttributes.SQL_WEEKLY_HOS_SALES_DATA_LASTWEEK_SELECT_PED)
	        .append(") lastweekdata, ")
	        .append("( ")
	        .append("   select hospitalCode, ")
	        .append(LsAttributes.SQL_WEEKLY_HOS_SALES_DATA_LAST2WEEK_SELECT_PED)
	        .append(") last2weekdata ")
	        .append("where lastweekdata.hospitalCode = last2weekdata.hospitalCode ");
	    }
        return dataBean.getJdbcTemplate().query(hosSalesSQL.toString(), new HospitalSalesQueryRowMapper());
    }
	
	@Override
	public List<MonthlyInRateData> getMonthlyInRateData(String beginDuraion,
			String endDuraion, String level) throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
	    if( null != level && "RSM".equalsIgnoreCase(level) ){
	    	inRateSQL.append("select pedData.duration,pedData.region,pedData.rsmRegion,resData.inRate as resInRate,pedData.inRate as pedInRate ")
            .append(" from ( ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_SELECTION)
            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_RSM_CONDITION)
            .append(") resData ")
            .append(",( ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_SELECTION)
            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_RSM_CONDITION)
            .append(") pedData ")
            .append(" where resData.duration = pedData.duration ")
            .append(" and resData.region = pedData.region ")
            .append(" and resData.rsmRegion = pedData.rsmRegion ")
            .append(" order by region asc, rsmRegion asc, duration desc");
	    }else{
	    	inRateSQL.append("select pedData.duration,pedData.region,'' as rsmRegion,resData.inRate as resInRate,pedData.inRate as pedInRate ")
            .append(" from ( ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_SELECTION)
            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_RSD_CONDITION)
            .append(") resData ")
            .append(",( ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_SELECTION)
            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_RSD_CONDITION)
            .append(") pedData ")
            .append(" where resData.duration = pedData.duration ")
            .append(" and resData.region = pedData.region ")
            .append(" order by region asc, duration desc");
	    }
        return dataBean.getJdbcTemplate().query(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion,beginDuraion,endDuraion},new MonthlyInRateDataRowMapper());
	}
	
	@Override
	public List<MonthlyRatioData> getMonthlyCollectionData(Date chooseDate)
			throws Exception {
		StringBuffer sb = new StringBuffer();
	    sb.append("select chooseMonth.pedEmernum ,chooseMonth.pedroomnum ,chooseMonth.resnum ,chooseMonth.othernum ,chooseMonth.totalnum ")
	        .append(", ROUND(IFNULL(chooseMonth.pedEmernum/chooseMonth.totalnum,0),2) as pedemernumrate ")
            .append(", ROUND(IFNULL(chooseMonth.pedroomnum/chooseMonth.totalnum,0),2) as pedroomnumrate ")
            .append(", ROUND(IFNULL(chooseMonth.resnum/chooseMonth.totalnum,0),2) as resnumrate ")
            .append(", ROUND(IFNULL(chooseMonth.othernum/chooseMonth.totalnum,0),2) as othernumrate ")
	    	.append(", IFNULL(chooseMonth.innum,0) as innum ")
	    	.append(", (select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.isMonthlyAssessed='1') as hosnum " )
        	.append(", u.region as rsmRegion , u.regionCenter as region " )
            .append("    from (")
            .append("       select h.rsmRegion ")
            .append("       , h.region ")
            .append("       , sum(pedEmernum) as pedEmernum ")
            .append("       , sum(pedroomnum) as pedroomnum ")
            .append("       , sum(resnum) as resnum ")
            .append("       , sum(other) as othernum ")
            .append("       , sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum ")
            .append("       , count(1) as innum ")
            .append("       from tbl_month_data md, tbl_hospital h ")
            .append("		where md.countMonth = DATE_FORMAT(?,'%Y-%m') " )
            .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
            .append("       group by h.rsmRegion ")
            .append("   ) chooseMonth ")
            .append("   right join tbl_userinfo u on u.region = chooseMonth.rsmRegion ")
            .append("   where u.level='RSM' ");
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{chooseDate}, new MonthlyCollectionDataRowMapper());
	}
	
	@Override
	public MonthlyRatioData getMonthlyCollectionSumData(Date chooseDate)
			throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("select chooseMonth.pedEmernum ,chooseMonth.pedroomnum ,chooseMonth.resnum ,chooseMonth.othernum ,chooseMonth.totalnum ")
        .append(", ROUND(IFNULL(chooseMonth.pedEmernum/chooseMonth.totalnum,0),2) as pedemernumrate ")
        .append(", ROUND(IFNULL(chooseMonth.pedroomnum/chooseMonth.totalnum,0),2) as pedroomnumrate ")
        .append(", ROUND(IFNULL(chooseMonth.resnum/chooseMonth.totalnum,0),2) as resnumrate ")
        .append(", ROUND(IFNULL(chooseMonth.othernum/chooseMonth.totalnum,0),2) as othernumrate ")
    	.append(", IFNULL(chooseMonth.innum,0) as innum ")
    	.append(", (select count(1) from tbl_hospital h where h.isMonthlyAssessed='1') as hosnum " )
    	.append(", '' as rsmRegion , '' as region " )
        .append("    from (")
        .append("       select h.rsmRegion ")
        .append("       , h.region ")
        .append("       , sum(pedEmernum) as pedEmernum ")
        .append("       , sum(pedroomnum) as pedroomnum ")
        .append("       , sum(resnum) as resnum ")
        .append("       , sum(other) as othernum ")
        .append("       , sum(pedEmernum)+sum(pedroomnum)+sum(resnum)+sum(other) as totalnum ")
        .append("       , count(1) as innum ")
        .append("       from tbl_month_data md, tbl_hospital h ")
        .append("		where md.countMonth = DATE_FORMAT(?,'%Y-%m') " )
        .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
        .append("   ) chooseMonth ");
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{chooseDate}, new MonthlyCollectionDataRowMapper());
	}
	
	@Override
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

    @Override
    public void delete() throws Exception {
        dataBean.getJdbcTemplate().update("delete from tbl_hospital");
    }
	
	public DataBean getDataBean() {
		return dataBean;
	}
	public void setDataBean(DataBean dataBean) {
		this.dataBean = dataBean;
	}
}
