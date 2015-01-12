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
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.CommonMapRowMapper;
import com.chalet.lskpi.mapper.HospitalRowMapper;
import com.chalet.lskpi.mapper.HospitalSalesQueryRowMapper;
import com.chalet.lskpi.mapper.KPIHospitalRowMapper;
import com.chalet.lskpi.mapper.Monthly12DataRowMapper;
import com.chalet.lskpi.mapper.MonthlyCollectionDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyStatisticsDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyRatioDataRowMapper;
import com.chalet.lskpi.mapper.UserInfoRowMapper;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.HospitalSalesQueryObj;
import com.chalet.lskpi.model.HospitalSalesQueryParam;
import com.chalet.lskpi.model.KPIHospital4Export;
import com.chalet.lskpi.model.Monthly12Data;
import com.chalet.lskpi.model.MonthlyData;
import com.chalet.lskpi.model.MonthlyStatisticsData;
import com.chalet.lskpi.model.MonthlyRatioData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyDataOfHospital;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.DateUtils;
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
	
    public List<Hospital> getHospitalsOfHomeCollectionByPSRTel(String telephone) throws Exception {
        List<Hospital> hospitals = new ArrayList<Hospital>();
        StringBuffer sql = new StringBuffer("")
        .append(" select h.id,h.code ")
        .append(" , h.name ")
        .append(" , h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode,h.portNum ")
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
            .append(" , h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode,h.portNum ")
            .append(" from tbl_hospital h, tbl_userinfo ui ")
            .append(" where h.dsmCode = ui.userCode and ui.telephone = ? ")
            .append(" order by h.name asc ");
        hospitals = dataBean.getJdbcTemplate().query(sql.toString(), new Object[]{telephone}, new HospitalRowMapper());
        return hospitals;
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
		String insertSQL = "insert into tbl_hospital(id,name,city,province,region,rsmRegion,level,code,dsmCode,dsmName,saleName,dragonType,isResAssessed,isPedAssessed,saleCode,isMonthlyAssessed,isChestSurgeryAssessed,isTop100,portNum) values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
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
				ps.setInt(18, hospitals.get(i).getPortNum());
			}
			
			@Override
			public int getBatchSize() {
				return hospitals.size();
			}
		});
	}
	
	@Override
	public void insertMonthlyData(final MonthlyData monthlyData) throws Exception {
	    final String insertSQL = "insert into tbl_month_data(id,pedEmernum,pedroomnum,resnum,other,operatorName,operatorCode,hospitalCode,dsmCode,rsmRegion,region,createdate,updatedate,countMonth) values(null,?,?,?,?,?,?,?,?,?,?,?,now(),?)";
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
                }if( month == 0 ){
                	ps.setString(12, (createCal.get(Calendar.YEAR)-1)+"-12");
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
            sb.append(", case when h.isPedAssessed='1' and h.dragonType='").append(LsAttributes.DRAGON_TYPE_CORE).append("' then concat('* ',h.name) ")
            .append(" when h.isPedAssessed='1' and h.dragonType='").append(LsAttributes.DRAGON_TYPE_EMERGING).append("' then concat('** ',h.name) ")
            .append(" else h.name ")
            .append(" end name ");
        }else if( LsAttributes.DEPARTMENT_RES.equalsIgnoreCase(department) ){
            sb.append(", case when h.isResAssessed='1' then concat('* ',h.name) else h.name end name ");
        }else if( LsAttributes.DEPARTMENT_CHE.equalsIgnoreCase(department) ){
            sb.append(", case when h.isChestSurgeryAssessed='1' then concat('* ',h.name) else h.name end name");
        }
        
        sb.append(" ,h.portNum ");
        
        sb.append(" from tbl_userinfo u, tbl_hospital h ")
            .append(" where u.userCode = h.dsmCode and u.telephone = ? ");
        
        if( LsAttributes.DEPARTMENT_PED.equalsIgnoreCase(department) ){
            sb.append(" order by h.isPedAssessed desc, name asc");
        }else if( LsAttributes.DEPARTMENT_RES.equalsIgnoreCase(department) ){
            sb.append(" order by h.isResAssessed desc, name asc");
        }else if( LsAttributes.DEPARTMENT_CHE.equalsIgnoreCase(department) ){
            sb.append(" order by h.isChestSurgeryAssessed desc, name asc");
        }
        
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{telephone}, new HospitalRowMapper());
	}
	
	@Override
	public List<Hospital> getMonthlyHospitalsByUserTel(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer();
	    sb.append("select h.id,h.code ")
	    	.append(", case when h.isMonthlyAssessed='1' then concat('* ',h.name) else h.name end name")
	    	.append(", h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode,h.portNum ")
	        .append(" from tbl_hospital h, tbl_userinfo ui, tbl_hos_user hu ")
	        .append(" where ui.userCode = hu.userCode and hu.hosCode = h.code and ui.telephone = ? order by h.isMonthlyAssessed desc, h.name asc");
	    return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{telephone}, new HospitalRowMapper());
	}
	
	@Override
	public List<Hospital> getMonthlyHospitalsByDSMTel(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer();
	    sb.append("select h.id,h.code ")
    		.append(", case when h.isMonthlyAssessed='1' then concat('* ',h.name) else h.name end name")
    		.append(", h.city,h.province,h.region,h.rsmRegion,h.saleCode,h.saleName,h.dsmCode,h.portNum ")
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
            sb.append(", case when h.isPedAssessed='1' and h.dragonType='").append(LsAttributes.DRAGON_TYPE_CORE).append("' then concat('* ',h.name) ")
            .append(" when h.isPedAssessed='1' and h.dragonType='").append(LsAttributes.DRAGON_TYPE_EMERGING).append("' then concat('** ',h.name) ")
            .append(" else h.name ")
            .append(" end name ");
        }else if( LsAttributes.DEPARTMENT_RES.equalsIgnoreCase(department) ){
            sb.append(", case when h.isResAssessed='1' then concat('* ',h.name) else h.name end name ");
        }else if( LsAttributes.DEPARTMENT_CHE.equalsIgnoreCase(department) ){
            sb.append(", case when h.isChestSurgeryAssessed='1' then concat('* ',h.name) else h.name end name ");
        }
        
        sb.append(" , h.portNum ");
        
        sb.append(" from tbl_userinfo u, tbl_hos_user hu, tbl_hospital h ")
            .append(" where u.userCode = hu.userCode and hu.hosCode = h.code and u.telephone = ? ");
        
        if( LsAttributes.DEPARTMENT_PED.equalsIgnoreCase(department) ){
            sb.append(" order by h.isPedAssessed desc, name asc");
        }else if( LsAttributes.DEPARTMENT_RES.equalsIgnoreCase(department) ){
            sb.append(" order by h.isResAssessed desc, name asc");
        }else if( LsAttributes.DEPARTMENT_CHE.equalsIgnoreCase(department) ){
            sb.append(" order by h.isChestSurgeryAssessed desc, name asc");
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
	    }else if( "2".equalsIgnoreCase(queryParam.getDepartment()) ){
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
	    }else if( "3".equalsIgnoreCase(queryParam.getDepartment()) ){
	        hosSalesSQL.append(LsAttributes.SQL_WEEKLY_HOS_SALES_DATA)
	        .append(" from ( ")
	        .append("   select hospitalCode, ( select name from tbl_hospital where code = hospitalCode ) as hospitalName, ")
	        .append(LsAttributes.SQL_WEEKLY_HOS_SALES_DATA_LASTWEEK_SELECT_CHE)
	        .append(") lastweekdata, ")
	        .append("( ")
	        .append("   select hospitalCode, ")
	        .append(LsAttributes.SQL_WEEKLY_HOS_SALES_DATA_LAST2WEEK_SELECT_CHE)
	        .append(") last2weekdata ")
	        .append("where lastweekdata.hospitalCode = last2weekdata.hospitalCode ");
	    }
        return dataBean.getJdbcTemplate().query(hosSalesSQL.toString(), new HospitalSalesQueryRowMapper());
    }
	
	@Override
	public List<MonthlyStatisticsData> getMonthlyInRateData(String beginDuraion,
			String endDuraion, String level) throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
	    if( null != level && "RSM".equalsIgnoreCase(level) ){
	    	inRateSQL.append("select pedData.duration,pedData.region,pedData.rsmRegion,resData.inRate as resInRate,pedData.inRate as pedInRate ")
            .append(" from ( ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_SELECTION)
            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSM_CONDITION)
            .append(") resData ")
            .append(",( ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_SELECTION)
            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSM_CONDITION)
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
            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSD_CONDITION)
            .append(") resData ")
            .append(",( ")
            .append(LsAttributes.SQL_MONTHLY_INRATE_SELECTION)
            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSD_CONDITION)
            .append(") pedData ")
            .append(" where resData.duration = pedData.duration ")
            .append(" and resData.region = pedData.region ")
            .append(" order by region asc, duration desc");
	    }
        return dataBean.getJdbcTemplate().query(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion,beginDuraion,endDuraion},new MonthlyStatisticsDataRowMapper());
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
    public void delete() throws Exception {
        dataBean.getJdbcTemplate().update("delete from tbl_hospital");
    }
    
	public int getLastWeeklyData() throws Exception {
        Timestamp lastThursDay = new Timestamp(DateUtils.getGenerateWeeklyReportDate().getTime());
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) from tbl_hospital_data_weekly where duration = CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d'))");
        return dataBean.getJdbcTemplate().queryForInt(sb.toString(), lastThursDay,lastThursDay);
    }
    
    @Override
    public void generateWeeklyDataOfHospital() throws Exception {
        Date lastweekDay = DateUtils.getGenerateWeeklyReportDate();
        this.generateWeeklyPEDDataOfHospital(lastweekDay);
    }
    
    @Override
    public int removeOldWeeklyHosData(String duration) throws Exception{
        String sql = "delete from tbl_hospital_data_weekly where duration=?";
        return dataBean.getJdbcTemplate().update(sql, new Object[] { duration });
    }
	
	@Override
	public void generateWeeklyPEDDataOfHospital(Date refreshDate) throws Exception {
	    Timestamp lastweekDay = new Timestamp(refreshDate.getTime());
        StringBuffer sb = new StringBuffer();
        
        sb.append("insert into tbl_hospital_data_weekly(id,duration,hospitalCode,pedPNum,pedLsNum,pedAverageDose,updatedate) ")
            .append(" select ")
            .append(" null,")
            .append(" CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d')) as duration, ")
            .append(" pdw.hospitalCode, ")
            .append(" pdw.pnum, ")
            .append(" pdw.lsnum, ")
            .append(" pdw.averageDose, ")
            .append(" now()  ")
            .append(" from tbl_pediatrics_data_weekly pdw ")
            .append(" where pdw.duration = CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d')) ");
        int result = dataBean.getJdbcTemplate().update(sb.toString(), new Object[]{lastweekDay,lastweekDay,lastweekDay,lastweekDay});
        logger.info(String.format("finish to generate the hospital weekly data of ped, the result is %s", result));
	}
	
    public int getWeeklyDataIDOfHospital(String duration, String hospitalCode) throws Exception {
        StringBuffer sb = new StringBuffer();
        int id = 0;
        sb.append(" select id ")
            .append(" from tbl_hospital_data_weekly hdw ")
            .append(" where hdw.duration = ? ")
            .append(" and hdw.hospitalCode=?");
        try{
            id = dataBean.getJdbcTemplate().queryForInt(sb.toString(), new Object[]{duration,hospitalCode});
        }catch(Exception e){
            logger.warn("fail to get the hospital weekly data ID,"+e.getMessage());
        }
        return id;
    }

    public void insertHospitalWeeklyRESData(WeeklyDataOfHospital weeklyData) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into tbl_hospital_data_weekly(id,duration,hospitalCode,resPNum,resLsNum,resAverageDose,updatedate) ")
            .append(" values ( null,?,?,?,?,?,now())");
        dataBean.getJdbcTemplate().update(sb.toString(), new Object[]{weeklyData.getDuration()
            ,weeklyData.getHospitalCode()
            ,weeklyData.getPnum()
            ,weeklyData.getLsnum()
            ,weeklyData.getAverageDose()});
    }

    public void updateHospitalWeeklyRESData(WeeklyDataOfHospital weeklyData, int hosWeeklyDataId) throws Exception {
        StringBuffer sql = new StringBuffer("update tbl_hospital_data_weekly set ")
        .append("updatedate=NOW()")
        .append(", resPNum=? ")
        .append(", resLsNum=? ")
        .append(", resAverageDose=? ")
        .append(" where id=? ");
        
        dataBean.getJdbcTemplate().update(sql.toString(), new Object[]{weeklyData.getPnum(),weeklyData.getLsnum(),weeklyData.getAverageDose(),hosWeeklyDataId});
    }

    public void insertHospitalWeeklyCHEData(WeeklyDataOfHospital weeklyData) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into tbl_hospital_data_weekly(id,duration,hospitalCode,chePNum,cheLsNum,cheAverageDose,updatedate) ")
            .append(" values ( null,?,?,?,?,?,now())");
        dataBean.getJdbcTemplate().update(sb.toString(), new Object[]{weeklyData.getDuration()
            ,weeklyData.getHospitalCode()
            ,weeklyData.getPnum()
            ,weeklyData.getLsnum()
            ,weeklyData.getAverageDose()});
    }

    public void updateHospitalWeeklyCHEData(WeeklyDataOfHospital weeklyData, int hosWeeklyDataId) throws Exception {
        StringBuffer sql = new StringBuffer("update tbl_hospital_data_weekly set ")
        .append("updatedate=NOW()")
        .append(", chePNum=? ")
        .append(", cheLsNum=? ")
        .append(", cheAverageDose=? ")
        .append(" where id=? ");
        
        dataBean.getJdbcTemplate().update(sql.toString(), new Object[]{weeklyData.getPnum(),weeklyData.getLsnum(),weeklyData.getAverageDose(),hosWeeklyDataId});
    }

    public int deleteOldHospitalWeeklyData(String duration) throws Exception {
        String sql = "delete from tbl_hospital_data_weekly where duration=?";
        return dataBean.getJdbcTemplate().update(sql, new Object[] { duration });
    }

	@Override
	public List<KPIHospital4Export> getKPIHospitalOfRes()
			throws Exception {
		StringBuffer sb = new StringBuffer();
	    sb.append(LsAttributes.SQL_KPI_HOS_SELECTION)
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION_SALES)
	        .append(LsAttributes.SQL_KPI_HOS_CONDITION_WITH_SALES)
	        .append(" and h.isResAssessed='1' ")
	        .append(" union all ")
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION)
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION_WITHOUT_SALES)
	        .append(LsAttributes.SQL_KPI_HOS_CONDITION_WITHOUT_SALES)
	        .append(" and h.isResAssessed='1' ")
	        .append(" order by region, rsmRegion ");
        return dataBean.getJdbcTemplate().query(sb.toString(), new KPIHospitalRowMapper());
	}
	
	@Override
	public List<KPIHospital4Export> getKPIHospitalOfPed()
			throws Exception {
		StringBuffer sb = new StringBuffer();
	    sb.append(LsAttributes.SQL_KPI_HOS_SELECTION)
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION_SALES)
	        .append(LsAttributes.SQL_KPI_HOS_CONDITION_WITH_SALES)
	        .append(" and h.isPedAssessed='1' ")
	        .append(" union all ")
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION)
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION_WITHOUT_SALES)
	        .append(LsAttributes.SQL_KPI_HOS_CONDITION_WITHOUT_SALES)
	        .append(" and h.isPedAssessed='1' ")
	        .append(" order by region, rsmRegion ");
        return dataBean.getJdbcTemplate().query(sb.toString(), new KPIHospitalRowMapper());
	}
	
	@Override
	public List<KPIHospital4Export> getKPIHospitalOfChe()
			throws Exception {
		StringBuffer sb = new StringBuffer();
	    sb.append(LsAttributes.SQL_KPI_HOS_SELECTION)
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION_SALES)
	        .append(LsAttributes.SQL_KPI_HOS_CONDITION_WITH_SALES)
	        .append(" and h.isChestSurgeryAssessed='1' ")
	        .append(" union all ")
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION)
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION_WITHOUT_SALES)
	        .append(LsAttributes.SQL_KPI_HOS_CONDITION_WITHOUT_SALES)
	        .append(" and h.isChestSurgeryAssessed='1' ")
	        .append(" order by region, rsmRegion ");
        return dataBean.getJdbcTemplate().query(sb.toString(), new KPIHospitalRowMapper());
	}
	
	@Override
	public List<KPIHospital4Export> getKPIHospitalOfMonth()
			throws Exception {
		StringBuffer sb = new StringBuffer();
	    sb.append(LsAttributes.SQL_KPI_HOS_SELECTION)
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION_SALES)
	        .append(LsAttributes.SQL_KPI_HOS_CONDITION_WITH_SALES)
	        .append(" and h.isMonthlyAssessed='1' ")
	        .append(" union all ")
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION)
	        .append(LsAttributes.SQL_KPI_HOS_SELECTION_WITHOUT_SALES)
	        .append(LsAttributes.SQL_KPI_HOS_CONDITION_WITHOUT_SALES)
	        .append(" and h.isMonthlyAssessed='1' ")
	        .append(" order by region, rsmRegion ");
        return dataBean.getJdbcTemplate().query(sb.toString(), new KPIHospitalRowMapper());
	}
	

    public List<Map<String, Integer>> getKPIHosNumMap(String department) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(" select rsmRegion,count(1) as numCount ")
          .append(" from tbl_hospital ");
        switch(department){
            case LsAttributes.DEPARTMENT_RES:
                sb.append(" where isResAssessed='1' ")
                .append(" group by rsmRegion ")
                .append(" union all ")
                .append(" select '全国' as rsmRegion, count(1) as numCount ")
                .append(" from tbl_hospital ")
                .append(" where isResAssessed='1' ");
                break;
            case LsAttributes.DEPARTMENT_PED:
                break;
            case LsAttributes.DEPARTMENT_CHE:
                break;
        }
        return dataBean.getJdbcTemplate().query(sb.toString(), new CommonMapRowMapper());
    }

    public List<Map<String, Integer>> getKPISalesNumMap(String department) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append(" select rsmRegion,count(1) as numCount ")
          .append(" from ( ")
          .append("     select distinct h.rsmRegion, hu.userCode ")
          .append("     from tbl_hospital h, tbl_hos_user hu ");
        switch(department){
            case LsAttributes.DEPARTMENT_RES:
                sb.append(LsAttributes.SQL_MONTH_WEEKLY_REPORT_SALES_NUM_CONDITION)
                .append(" and h.isResAssessed='1' ")
                .append(" ) temp ")
                .append(" group by rsmRegion ") 
                .append(" union all ")
                .append(" select '全国' as rsmRegion, count(1) as numCount ")
                .append(" from ( ")
                .append("     select distinct h.rsmRegion, hu.userCode ")
                .append("     from tbl_hospital h, tbl_hos_user hu ")
                .append(LsAttributes.SQL_MONTH_WEEKLY_REPORT_SALES_NUM_CONDITION)
                .append(" and h.isResAssessed='1'")
                .append(" ) temp ");
                break;
            case LsAttributes.DEPARTMENT_PED:
                break;
            case LsAttributes.DEPARTMENT_CHE:
                break;
        }
        return dataBean.getJdbcTemplate().query(sb.toString(), new CommonMapRowMapper());
    }
	
	public DataBean getDataBean() {
		return dataBean;
	}
	public void setDataBean(DataBean dataBean) {
		this.dataBean = dataBean;
	}

	@Override
	public void updatePortNum(final Hospital hospitalWithPortNum)
			throws Exception {
		StringBuffer sql = new StringBuffer("update tbl_hospital set ")
        .append(" portNum=? ")
        .append(" where code=? ");
        
        dataBean.getJdbcTemplate().update(sql.toString(), new Object[]{hospitalWithPortNum.getPortNum(),hospitalWithPortNum.getCode()});
	}

}
