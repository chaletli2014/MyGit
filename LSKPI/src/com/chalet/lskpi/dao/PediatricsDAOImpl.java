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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.CoreTopAndBottomRSMDataRowMapper;
import com.chalet.lskpi.mapper.CoreTopAndBottomRSMWhRateRowMapper;
import com.chalet.lskpi.mapper.EmergingTopAndBottomRSMWhRateRowMapper;
import com.chalet.lskpi.mapper.MonthlyStatisticsCoreDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyStatisticsDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyStatisticsEmergingDataRowMapper;
import com.chalet.lskpi.mapper.PediatricsCoreHosRowMapper;
import com.chalet.lskpi.mapper.PediatricsEmergingHosRowMapper;
import com.chalet.lskpi.mapper.PediatricsMobileRowMapper;
import com.chalet.lskpi.mapper.PediatricsRoomRowMapper;
import com.chalet.lskpi.mapper.PediatricsRowMapper;
import com.chalet.lskpi.mapper.PediatricsWhPortRowMapper;
import com.chalet.lskpi.mapper.PediatricsWithPortNumRowMapper;
import com.chalet.lskpi.mapper.ReportProcessPEDDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomAverageDoseRSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomBlRateRSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomEmergingNum1RSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomEmergingNum3RSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomEmergingNum4RSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomInRateRSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRoomAverageDoseRSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRoomBlRateRSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRoomNum1RSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRoomNum3RSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRoomNum4RSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRoomWhDaysRSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRoomWhRateRSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomWhDaysRSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomWhPortRateRSMDataRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomWhRateRSMDataRowMapper;
import com.chalet.lskpi.model.DailyReportData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobilePEDDailyData;
import com.chalet.lskpi.model.MonthlyStatisticsData;
import com.chalet.lskpi.model.PediatricsData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserCode;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyRatioData;
import com.chalet.lskpi.utils.CustomizedProperty;
import com.chalet.lskpi.utils.DailyReportDataRowMapper;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.PEDWeeklyRatioDataRowMapper;
import com.chalet.lskpi.utils.ReportProcessDetailDataRowMapper;

/**
 * @author Chalet
 * @version 创建时间：2013年11月27日 下午11:29:42
 * 类说明
 */

@Repository("pediatricsDAO")
public class PediatricsDAOImpl implements PediatricsDAO {

	private Logger logger = Logger.getLogger(PediatricsDAOImpl.class);
	
	@Autowired
	@Qualifier("dataBean")
	private DataBean dataBean;
	
	@Override
	public void updatePEDUserCodes(final List<UserCode> userCodes) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("update tbl_pediatrics_data set etmsCode=? where etmsCode=?");
		dataBean.getJdbcTemplate().batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, userCodes.get(i).getNewCode());
				ps.setString(2, userCodes.get(i).getOldCode());
			}
			
			@Override
			public int getBatchSize() {
				return userCodes.size();
			}
		});
		logger.info("update the sales code end, start to refresh the dsm code");
		sb = new StringBuffer();
		sb.append("update tbl_pediatrics_data set dsmCode=? where dsmCode=?");
		dataBean.getJdbcTemplate().batchUpdate(sb.toString(), new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, userCodes.get(i).getNewCode());
				ps.setString(2, userCodes.get(i).getOldCode());
			}
			
			@Override
			public int getBatchSize() {
				return userCodes.size();
			}
		});
	}
	
    public WeeklyRatioData getHospitalWeeklyPEDData4Mobile(String hospitalCode) throws Exception {
        StringBuffer mobilePEDWeeklySQL = new StringBuffer();
        mobilePEDWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_PED)
        .append(" , '' as userCode ")
        .append(" , lastweekdata.hospitalName as name ")
        .append(" from ( ")
        .append("   select hospitalCode, hospitalName, ")
        .append(LsAttributes.SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED)
        .append("   where hospitalCode=? ")
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select hospitalCode, hospitalName, ")
        .append(LsAttributes.SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED)
        .append("   where hospitalCode=? ")
        .append(") last2weekdata ");
        return dataBean.getJdbcTemplate().queryForObject(mobilePEDWeeklySQL.toString(),new Object[]{hospitalCode,hospitalCode},new PEDWeeklyRatioDataRowMapper());
    }
	
	@Override
	public WeeklyRatioData getLowerWeeklyPEDData4REPMobile(UserInfo currentUser,String lowerUserCode)
			throws Exception {
		StringBuffer mobilePEDWeeklySQL = new StringBuffer();
        mobilePEDWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_PED)
    	.append(" , lastweekdata.saleCode as userCode ")
    	.append(" , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.saleCode and u.superior = lastweekdata.dsmCode and u.level='REP'),'vacant') as name ")
    	.append(" from ( ")
    	.append("   select h.dsmCode, h.saleCode, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED)
    	.append("	group by h.dsmCode, h.saleCode ")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.dsmCode, h.saleCode, ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED)
	    .append("	group by h.dsmCode, h.saleCode ")
	    .append(") last2weekdata ")
        .append("where lastweekdata.dsmCode = last2weekdata.dsmCode ")
        .append("and lastweekdata.saleCode = last2weekdata.saleCode ")
        .append("and lastweekdata.saleCode = ?")
        .append("and lastweekdata.dsmCode = ?");
        return dataBean.getJdbcTemplate().queryForObject(mobilePEDWeeklySQL.toString(),new Object[]{lowerUserCode,currentUser.getUserCode()},new PEDWeeklyRatioDataRowMapper());
	}

	@Override
	public WeeklyRatioData getLowerWeeklyPEDData4DSMMobile(UserInfo currentUser,String lowerUserCode)
			throws Exception {
		StringBuffer mobilePEDWeeklySQL = new StringBuffer();
        mobilePEDWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_PED)
    	.append(" , lastweekdata.dsmCode as userCode ")
    	.append(" , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.dsmCode and u.region = lastweekdata.rsmRegion  and u.level='DSM'),'vacant') as name ")
    	.append(" from ( ")
    	.append("   select h.dsmCode, h.rsmRegion, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED)
    	.append("	group by h.rsmRegion, h.dsmCode ")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.dsmCode, h.rsmRegion, ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED)
	    .append("	group by h.rsmRegion, h.dsmCode ")
	    .append(") last2weekdata ")
        .append("where lastweekdata.dsmCode = last2weekdata.dsmCode ")
        .append("and lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.dsmCode = ?")
        .append("and lastweekdata.rsmRegion = ?");
        return dataBean.getJdbcTemplate().queryForObject(mobilePEDWeeklySQL.toString(),new Object[]{lowerUserCode,currentUser.getRegion()},new PEDWeeklyRatioDataRowMapper());
	}

	@Override
	public WeeklyRatioData getLowerWeeklyPEDData4RSMMobile(UserInfo currentUser,String lowerUserCode)
			throws Exception {
		StringBuffer mobilePEDWeeklySQL = new StringBuffer();
        mobilePEDWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_PED)
    	.append(" , lastweekdata.rsmRegion as userCode ")
    	.append(" , IFNULL((select u.name from tbl_userinfo u where u.level='RSM' and u.region = lastweekdata.rsmRegion ),'vacant') as name ")
    	.append(" from ( ")
    	.append("   select h.region, h.rsmRegion, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED)
    	.append("	group by h.region, h.rsmRegion ")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.region, h.rsmRegion, ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED)
	    .append("	group by h.region, h.rsmRegion ")
	    .append(") last2weekdata ")
        .append("where lastweekdata.region = last2weekdata.region ")
        .append("and lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.rsmRegion = (select region from tbl_userinfo where userCode=?)");
        return dataBean.getJdbcTemplate().queryForObject(mobilePEDWeeklySQL.toString(),new Object[]{lowerUserCode},new PEDWeeklyRatioDataRowMapper());
	}

    public List<WeeklyRatioData> getWeeklyPEDData4DSMMobile(String telephone) throws Exception {
        StringBuffer mobilePEDWeeklySQL = new StringBuffer();
        mobilePEDWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_PED)
    	.append(" , lastweekdata.dsmCode as userCode , lastweekdata.rsmRegion ")
    	.append(" , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.dsmCode and u.region = lastweekdata.rsmRegion and u.level='DSM'),'vacant') as name ")
    	.append(" from ( ")
    	.append("   select h.dsmCode, h.rsmRegion, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED)
    	.append("	group by h.rsmRegion, h.dsmCode ")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.dsmCode, h.rsmRegion, ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED)
	    .append("	group by h.rsmRegion, h.dsmCode ")
	    .append(") last2weekdata ")
        .append("where lastweekdata.dsmCode = last2weekdata.dsmCode ")
        .append("and lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.rsmRegion = (select region from tbl_userinfo where telephone=?)");
        return dataBean.getJdbcTemplate().query(mobilePEDWeeklySQL.toString(),new Object[]{telephone},new PEDWeeklyRatioDataRowMapper());
    }

    public List<WeeklyRatioData> getWeeklyPEDData4RSMMobile(String telephone) throws Exception {
    	StringBuffer mobilePEDWeeklySQL = new StringBuffer();
    	 mobilePEDWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_PED)
     	.append(" , lastweekdata.rsmRegion as userCode, lastweekdata.region ")
     	.append(" , IFNULL((select u.name from tbl_userinfo u where u.level='RSM' and u.region = lastweekdata.rsmRegion ),'vacant') as name ")
     	.append(" from ( ")
     	.append("   select h.rsmRegion, h.region, ")
     	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED)
     	.append("	group by h.rsmRegion")
 	    .append(") lastweekdata, ")
 	    .append("( ")
 	    .append(" 	select h.rsmRegion, h.region, ")
 	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED)
 	    .append("	group by h.rsmRegion")
 	    .append(") last2weekdata ")
         .append("where lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
         .append("and lastweekdata.region = (select regionCenter from tbl_userinfo where telephone=?)");
        return dataBean.getJdbcTemplate().query(mobilePEDWeeklySQL.toString(),new Object[]{telephone},new PEDWeeklyRatioDataRowMapper());
    }

    public List<WeeklyRatioData> getWeeklyPEDData4RSDMobile() throws Exception {
    	StringBuffer mobilePEDWeeklySQL = new StringBuffer();
   	 	mobilePEDWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_PED)
    	.append(" , lastweekdata.region as userCode")
    	.append(" , (select distinct property_value from tbl_property where property_name=lastweekdata.region ) as name ")
    	.append(" from ( ")
    	.append("   select h.region, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED)
    	.append("	group by h.region")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.region , ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED)
	    .append("	group by h.region")
	    .append(") last2weekdata ")
        .append("where lastweekdata.region = last2weekdata.region  order by lastweekdata.region");
       return dataBean.getJdbcTemplate().query(mobilePEDWeeklySQL.toString(),new PEDWeeklyRatioDataRowMapper());
    }
    
    public WeeklyRatioData getWeeklyPEDCountoryData4Mobile() throws Exception {
    	StringBuffer mobilePEDWeeklySQL = new StringBuffer();
    	mobilePEDWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_PED)
    	.append(" , '' as userCode")
    	.append(" , '' as name ")
    	.append(" from ( ")
    	.append("   select ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED)
    	.append(") lastweekdata, ")
    	.append("( ")
    	.append(" 	select ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED)
    	.append(") last2weekdata ");
    	return dataBean.getJdbcTemplate().queryForObject(mobilePEDWeeklySQL.toString(),new PEDWeeklyRatioDataRowMapper());
    }

    public List<ReportProcessDataDetail> getSalesSelfReportProcessPEDDetailData(String telephone) throws Exception {
        StringBuffer sb = new StringBuffer("");
        sb.append("select h.name as hospitalName, ")
        .append("( select IFNULL( ")
        .append("       ( select count(1) ")
        .append("       from tbl_pediatrics_data pd ")
        .append("       where pd.hospitalName = h.name ")
        .append("       and pd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       group by pd.hospitalName ")
        .append("   ),0) ) as inNum, ")
        .append("( select ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion and ui.level='REP') as salesName, ")
        .append(" h.isPedAssessed as isAssessed ")
        .append("from tbl_userinfo u, tbl_hospital h, tbl_hos_user hu ")
        .append("where hu.userCode = u.userCode ")
	    .append("and hu.hosCode = h.code ")
        .append("and h.dsmCode = u.superior ")
        .append("and h.rsmRegion = u.region ")
        .append("and telephone = ? ");
        
        Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDetailDataRowMapper());
    }
    
    public List<ReportProcessDataDetail> getDSMSelfReportProcessPEDDetailData(String telephone) throws Exception {
        StringBuffer sb = new StringBuffer("");
        sb.append("select h.name as hospitalName, ")
        .append("( select IFNULL( ")
        .append("       ( select count(1) ")
        .append("       from tbl_pediatrics_data pd ")
        .append("       where pd.hospitalName = h.name ")
        .append("       and pd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       group by pd.hospitalName ")
        .append("   ),0) ) as inNum, ")
        .append("( select ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion  and ui.level='REP') as salesName, ")
        .append(" h.isPedAssessed as isAssessed ")
        .append("from tbl_userinfo u, tbl_hospital h ")
        .append("where h.dsmCode = u.userCode ")
        .append("and h.rsmRegion = u.region ")
        .append("and telephone = ? ");
        
        Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDetailDataRowMapper());
    }
    
    @Override
    public ReportProcessData getDSMSelfReportProcessPEDData(String telephone) throws Exception {
        StringBuffer sb = new StringBuffer("");
        sb.append("select count(1) as hosNum, ")
        .append("sum( ") 
        .append("	case ") 
		.append("	when h.dragonType='Core' then 3 ") 
		.append("	when h.dragonType='Emerging' then 1 ") 
        .append("	end ") 
        .append(" ) as hosNum_count, ") 
        .append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
        .append("       select ")
        .append(" 			case ")
        .append("			when h1.dragonType='Core' then least(count(1),3) ")
		.append("			when h1.dragonType='Emerging' then least(count(1),1) ")
		.append("			end as inNum ")
        .append("       , h1.dsmCode, h1.rsmRegion ")
        .append("       from tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append("       where pd.hospitalName = h1.name ")
        .append("       and pd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       and h1.isPedAssessed='1' ")
        .append("       group by pd.hospitalName ")
        .append("   ) inNumTemp ")
        .append("   where inNumTemp.rsmRegion = u.region ")
        .append("   and inNumTemp.dsmCode = u.userCode ")
        .append(") as validInNum ")
        .append("from tbl_userinfo u, tbl_hospital h ")
        .append("where h.dsmCode = u.userCode ")
        .append("and h.rsmRegion = u.region ")
        .append("and h.isPedAssessed='1' ")
        .append("and telephone = ? ");
        
        Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessPEDDataRowMapper());
    }
    
    public List<ReportProcessDataDetail> getRSMSelfReportProcessPEDDetailData(String telephone) throws Exception {
    	StringBuffer sb = new StringBuffer("");
    	sb.append("select h.name as hospitalName, ")
    	.append("( select IFNULL( ")
    	.append("       ( select count(1) ")
    	.append("       from tbl_pediatrics_data pd ")
    	.append("       where pd.hospitalName = h.name ")
    	.append("       and pd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
    	.append("       group by pd.hospitalName ")
    	.append("   ),0) ) as inNum, ")
    	.append("( select ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion  and ui.level='REP') as salesName, ")
    	.append(" h.isPedAssessed as isAssessed ")
    	.append("from tbl_userinfo u, tbl_hospital h ")
    	.append("where h.rsmRegion = u.region ")
    	.append("and h.isPedAssessed = '1' ")
    	.append("and telephone = ? ");
    	
    	Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
    	return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDetailDataRowMapper());
    }
    
    @Override
    public ReportProcessData getRSMSelfReportProcessPEDData(String telephone) throws Exception {
    	StringBuffer sb = new StringBuffer("");
    	sb.append("select count(1) as hosNum, ")
        .append("sum( ") 
        .append("	case ") 
		.append("	when h.dragonType='Core' then 3 ") 
		.append("	when h.dragonType='Emerging' then 1 ") 
        .append("	end ") 
        .append(" ) as hosNum_count, ") 
    	.append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
    	.append("       select ")
        .append(" 			case ")
        .append("			when h1.dragonType='Core' then least(count(1),3) ")
		.append("			when h1.dragonType='Emerging' then least(count(1),1) ")
		.append("			end as inNum ")
    	.append("       , h1.dsmCode, h1.rsmRegion ")
    	.append("       from tbl_pediatrics_data pd, tbl_hospital h1 ")
    	.append("       where pd.hospitalName = h1.name ")
    	.append("       and pd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
    	.append("       and h1.isPedAssessed='1' ")
    	.append("       group by pd.hospitalName ")
    	.append("   ) inNumTemp ")
    	.append("   where inNumTemp.rsmRegion = u.region ")
    	.append(") as validInNum ")
    	.append("from tbl_userinfo u, tbl_hospital h ")
    	.append("where h.rsmRegion = u.region ")
    	.append("and h.isPedAssessed='1' ")
    	.append("and telephone = ? ");
    	
    	Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
    	return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessPEDDataRowMapper());
    }
    
    @Override
    public ReportProcessData getSalesSelfReportProcessPEDData(String telephone) throws Exception {
        StringBuffer sb = new StringBuffer("");
        sb.append("select count(1) as hosNum, ")
        .append("sum( ") 
        .append("	case ") 
		.append("	when h.dragonType='Core' then 3 ") 
		.append("	when h.dragonType='Emerging' then 1 ") 
        .append("	end ") 
        .append(" ) as hosNum_count, ") 
        .append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
        .append("       select ")
        .append(" 			case ")
        .append("			when h1.dragonType='Core' then least(count(1),3) ")
		.append("			when h1.dragonType='Emerging' then least(count(1),1) ")
		.append("			end as inNum ")
        .append("       , h1.code as hosCode,  h1.dsmCode, h1.rsmRegion ")
        .append("       from tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append("       where pd.hospitalName = h1.name ")
        .append("       and pd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       and h1.isPedAssessed='1' ")
        .append("       group by pd.hospitalName ")
        .append("   ) inNumTemp, tbl_hos_user hu ")
        .append("   where inNumTemp.rsmRegion = u.region ")
        .append("   and inNumTemp.hosCode = hu.hosCode ")
        .append("   and hu.userCode = u.userCode ")
        .append("   and inNumTemp.dsmCode = u.superior ")
        .append(") as validInNum ")
        .append("from tbl_userinfo u, tbl_hospital h, tbl_hos_user hu ")
        .append("where h.dsmCode = u.superior ")
        .append("and h.rsmRegion = u.region ")
        .append("and h.code = hu.hosCode ")
	    .append("and hu.userCode = u.userCode ")
        .append("and h.isPedAssessed='1' ")
        .append("and telephone = ? ");
        logger.info("getSalesSelfReportProcessPEDData telephone= "+telephone);
        Date startDate = DateUtils.getTheBeginDateOfCurrentWeek();
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{new Timestamp(startDate.getTime()),new Timestamp(startDate.getTime()),telephone}, new ReportProcessPEDDataRowMapper());
    }

    public int getLastWeeklyPEDData() throws Exception {
        Timestamp lastThursDay = new Timestamp(DateUtils.getGenerateWeeklyReportDate().getTime());
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) from tbl_pediatrics_data_weekly where duration = CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d'))");
        return dataBean.getJdbcTemplate().queryForInt(sb.toString(), lastThursDay,lastThursDay);
    }
    
	@Override
	public void generateWeeklyPEDDataOfHospital() throws Exception {
		Date lastweekDay = DateUtils.getGenerateWeeklyReportDate();
		this.generateWeeklyPEDDataOfHospital(lastweekDay);
	}
	
	@Override
	public int removeOldWeeklyPEDData(String duration) throws Exception{
	    String sql = "delete from tbl_pediatrics_data_weekly where duration=?";
	    return dataBean.getJdbcTemplate().update(sql, new Object[] { duration });
	}
	
	@Override
	public void generateWeeklyPEDDataOfHospital(Date refreshDate) throws Exception {
	    Timestamp lastweekDay = new Timestamp(refreshDate.getTime());
	    StringBuffer sb = new StringBuffer();
	    
	    sb.append("insert into tbl_pediatrics_data_weekly(id,duration,hospitalName,hospitalCode,innum,pnum,whnum,lsnum,averageDose,hmgRate,omgRate,tmgRate,fmgRate,saleCode,dsmCode,rsmRegion,region,updatedate,portNum) ")
	    .append("select ")
	    .append("null,")
	    .append(" CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d')) as duration, ")
	    .append("h.name, ")
	    .append("h.code, ")
	    .append("pd_data.innum, ")
	    .append("pd_data.pnum, ")
	    .append("pd_data.whnum, ")
	    .append("pd_data.lsnum, ")
	    .append("pd_data.averageDose, ")
	    .append("pd_data.hmgRate, ")
	    .append("pd_data.omgRate, ")
	    .append("pd_data.tmgRate, ")
	    .append("pd_data.fmgRate, ")
	    .append("h.saleCode, ")
	    .append("h.dsmCode, ")
	    .append("h.rsmRegion, ")
	    .append("h.region, ")
	    .append("now(),  ")
	    .append("pd_data.portNum ")
	    .append("from ( ")
	    .append("	SELECT ")
	    .append("	h.code,	")
	    .append("	count_hos.inNum, ")
	    .append("	(sum(pd.pnum)/count_hos.inNum)*5 as pnum, ")
	    .append("	(sum(pd.whnum)/count_hos.inNum)*5 as whnum, ")
	    .append("	(sum(pd.lsnum)/count_hos.inNum)*5 as lsnum, ")
	    .append("	(sum(pd.portNum)/count_hos.inNum)*5 as portNum, ")
	    .append("	IFNULL( ")
	    .append("		sum( ")
	    .append("			( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 )* IFNULL(pd.lsnum,0) ")
	    .append("		) / IFNULL(sum(pd.lsnum),0) ,0 ) averageDose, ")
	    .append("	IFNULL( sum(IFNULL(pd.hqd,0)*pd.lsnum/100)/sum(pd.lsnum),0) hmgRate,")
	    .append("	IFNULL( sum((IFNULL(pd.hbid,0)*pd.lsnum + IFNULL(pd.oqd,0)*pd.lsnum)/100)/sum(pd.lsnum),0 ) omgRate, ")
	    .append("	IFNULL( sum((IFNULL(pd.obid,0)*pd.lsnum + IFNULL(pd.tqd,0)*pd.lsnum)/100)/sum(pd.lsnum),0 ) tmgRate, ")
	    .append("	IFNULL( sum(IFNULL(pd.tbid,0)*pd.lsnum/100)/sum(pd.lsnum),0 ) fmgRate ")
	    .append("	FROM tbl_pediatrics_data pd, tbl_hospital h, ")
	    .append("	(	")
	    .append("		select count(1) as inNum, h.code ")
	    .append("		from tbl_pediatrics_data pd, tbl_hospital h ")
	    .append("		WHERE pd.createdate between DATE_SUB(?, Interval 6 day) and DATE_ADD(?, Interval 1 day) ")
	    .append("		and pd.hospitalName = h.name ")
	    .append("		and h.isPedAssessed='1' ")
	    .append("		GROUP BY h.code ")
	    .append("	) count_hos ")
	    .append("	WHERE pd.createdate between DATE_SUB(?, Interval 6 day) and DATE_ADD(?, Interval 1 day) ")
	    .append("	and pd.hospitalName = h.name ")
	    .append("	and h.code = count_hos.code")
	    .append("	and h.isPedAssessed='1' ")
	    .append("	GROUP BY h.code ")
	    .append(") pd_data ")
	    .append("right join tbl_hospital h on pd_data.code = h.code ")
	    .append("where h.isPedAssessed='1'");
	    int result = dataBean.getJdbcTemplate().update(sb.toString(), new Object[]{lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay});
	    logger.info(String.format("finish to generate the ped weekly data, the result is %s", result));
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomInRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
	    try{
	    	maxSB.append(" select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMax,hosNumTemp.name as inRateMaxUser,0 as inRateMin,'' as inRateMinUser ")
	    	.append("	from ( ")
	    	.append("		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name ")
	    	.append("		from tbl_hospital h, tbl_userinfo u ")
	    	.append("		where h.rsmRegion = u.region ")
	    	.append("		and h.").append(hospitalShownFlag)
	    	.append("		and u.level='RSM' ")
	    	.append("		group by u.region ")
	    	.append("	) hosNumTemp, ")
	    	.append("	( select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from ( ")
	    	.append("			select IFNULL(count(1),0) as inNum, h.rsmRegion ")
	    	.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
	    	.append("			where pd.hospitalName = h.name ")
	    	.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
	    	.append("			and h.").append(hospitalShownFlag)
	    	.append("			group by h.rsmRegion ")
	    	.append("		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region ")
	    	.append("		where u.level='RSM' and u.name != 'Vacant' ")
	    	.append("	) inNumTemp ")
	    	.append("	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion ")
	    	.append("	order by inNumTemp.inNum/hosNumTemp.hosNum desc ")
	    	.append("	limit 1	");
	    	dbMaxData = dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomInRateRSMDataRowMapper());
	    	
	    	minSB.append("select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMin,hosNumTemp.name as inRateMinUser,0 as inRateMax,'' as inRateMaxUser  ") 
	    	.append("	from ( ") 
	    	.append("		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name ") 
	    	.append("		from tbl_hospital h, tbl_userinfo u ") 
	    	.append("		where h.rsmRegion = u.region ") 
	    	.append("		and h.").append(hospitalShownFlag)
	    	.append("		and u.level='RSM' ") 
	    	.append("		group by u.region ") 
	    	.append("	) hosNumTemp, ") 
	    	.append("		( ") 
	    	.append("		select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from (")
	    	.append("			select IFNULL(count(1),0) as inNum, h.rsmRegion ")
	    	.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
	    	.append("			where pd.hospitalName = h.name  ")
	    	.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
	    	.append("			and h.").append(hospitalShownFlag)
	    	.append("			group by h.rsmRegion ")
	    	.append("		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region ")
	    	.append("		where u.level='RSM' and u.name != 'Vacant' ")
	    	.append("	) inNumTemp")
	    	.append("	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion ")
	    	.append("	order by inNumTemp.inNum/hosNumTemp.hosNum ")
	    	.append("	limit 1	");
	    	dbMinData = dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomInRateRSMDataRowMapper());
	    }catch(EmptyResultDataAccessException ee){
	    	logger.warn("getTopAndBottomInRateRSMData,data is empty");
	    }
	    
	    rsmData.setTopInRate(dbMaxData.getTopInRate());
	    rsmData.setTopInRateRSMName(dbMaxData.getTopInRateRSMName());
	    rsmData.setBottomInRate(dbMinData.getBottomInRate());
	    rsmData.setBottomInRateRSMName(dbMinData.getBottomInRateRSMName());
	    
	    return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomWhRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append(" select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMax,pNumTemp.name as whRateMaxUser, 0 as whRateMin,''as whRateMinUser ")
			.append("	from ( ")
			.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (")
			.append("				select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("				and h.").append(hospitalShownFlag)
			.append("				group by h.rsmRegion ")
			.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' and u.name != 'Vacant' ")
			.append("		) pNumTemp, ")
			.append("	( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
			.append("			select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("	) lsNumTemp ")
			.append("	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
			.append("	order by lsNumTemp.lsNum/pNumTemp.pNum desc ")
			.append("	limit 1	");
			dbMaxData = dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate},new TopAndBottomWhRateRSMDataRowMapper());
			
			minSB.append(" select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMin,pNumTemp.name as whRateMinUser, 0 as whRateMax,''as whRateMaxUser ")
			.append("	from ( ")
			.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from ( ")
			.append("				select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("				and h.").append(hospitalShownFlag)
			.append("				group by h.rsmRegion ")
			.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' and u.name != 'Vacant' ")
			.append("		) pNumTemp, ")
			.append("		( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
			.append("			select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("			) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' and u.name != 'Vacant' ")
			.append("		) lsNumTemp")
			.append("		where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
			.append("		order by lsNumTemp.lsNum/pNumTemp.pNum ")
			.append("		limit 1	");
			dbMinData = dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate},new TopAndBottomWhRateRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
	    	logger.warn("getTopAndBottomWhRateRSMData,data is empty");
	    }
		
	    rsmData.setTopWhRate(dbMaxData.getTopWhRate());
	    rsmData.setTopWhRateRSMName(dbMaxData.getTopWhRateRSMName());
	    rsmData.setBottomWhRate(dbMinData.getBottomWhRate());
	    rsmData.setBottomWhRateRSMName(dbMinData.getBottomWhRateRSMName());
	    return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getRoomTopAndBottomRSMWhRateData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append(" select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMax,pNumTemp.name as whRateMaxUser, 0 as whRateMin,''as whRateMinUser ")
			.append("	from ( ")
			.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (")
			.append("				select IFNULL(sum(pd.pnum_room),0) as pNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("				and h.").append(hospitalShownFlag)
			.append("				group by h.rsmRegion ")
			.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' and u.name != 'Vacant' ")
			.append("		) pNumTemp, ")
			.append("	( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
			.append("			select IFNULL(sum(pd.lsnum_room),0) as lsNum, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("	) lsNumTemp ")
			.append("	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
			.append("	order by lsNumTemp.lsNum/pNumTemp.pNum desc ")
			.append("	limit 1	");
			dbMaxData = dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate},new TopAndBottomRoomWhRateRSMDataRowMapper());
			
			minSB.append(" select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMin,pNumTemp.name as whRateMinUser, 0 as whRateMax,''as whRateMaxUser ")
			.append("	from ( ")
			.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from ( ")
			.append("				select IFNULL(sum(pd.pnum_room),0) as pNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("				and h.").append(hospitalShownFlag)
			.append("				group by h.rsmRegion ")
			.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' and u.name != 'Vacant' ")
			.append("		) pNumTemp, ")
			.append("		( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
			.append("			select IFNULL(sum(pd.lsnum_room),0) as lsNum, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("			) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' and u.name != 'Vacant' ")
			.append("		) lsNumTemp")
			.append("		where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
			.append("		order by lsNumTemp.lsNum/pNumTemp.pNum ")
			.append("		limit 1	");
			dbMinData = dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate},new TopAndBottomRoomWhRateRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getRoomTopAndBottomRSMWhRateData,data is empty");
		}
		
		rsmData.setTopRoomWhRate(dbMaxData.getTopRoomWhRate());
		rsmData.setTopRoomWhRateRSMName(dbMaxData.getTopRoomWhRateRSMName());
		rsmData.setBottomRoomWhRate(dbMinData.getBottomRoomWhRate());
		rsmData.setBottomRoomWhRateRSMName(dbMinData.getBottomRoomWhRateRSMName());
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomAverageDoseRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		try{
			maxSB.append("select IFNULL(av2.averageDose,0) as averageDoseMax, u.name as averageDoseMaxUser, 0 as averageDoseMin, '' as averageDoseMinUser from ")
			.append("		( ")
			.append("			select IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.averageDose desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomAverageDoseRSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.averageDose,0) as averageDoseMin, u.name as averageDoseMinUser, 0 as averageDoseMax, '' as averageDoseMaxUser from ")
			.append("		( ")
			.append("			select IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.averageDose")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomAverageDoseRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
	    	logger.warn("getTopAndBottomAverageDoseRSMData,data is empty");
	    }
		
		rsmData.setTopAverageDose(dbMaxData.getTopAverageDose());
	    rsmData.setTopAvRSMName(dbMaxData.getTopAvRSMName());
	    rsmData.setBottomAverageDose(dbMinData.getBottomAverageDose());
	    rsmData.setBottomAvRSMName(dbMinData.getBottomAvRSMName());
	    
	    return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getRoomTopAndBottomAverageDoseRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		try{
			maxSB.append("select IFNULL(av2.averageDose,0) as averageDoseMax, u.name as averageDoseMaxUser, 0 as averageDoseMin, '' as averageDoseMinUser from ")
			.append("		( ")
			.append("			select IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd_room,0) + 0.5*2*IFNULL(pd.hbid_room,0) + 1*1*IFNULL(pd.oqd_room,0) + 1*2*IFNULL(pd.obid_room,0) + 2*1*IFNULL(pd.tqd_room,0) + 2*2*IFNULL(pd.tbid_room,0) ) ")
			.append("			/ 100 ) * IFNULL(pd.lsnum_room,0) ) / IFNULL(sum(pd.lsnum_room),0),0 ) as averageDose, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.averageDose desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomAverageDoseRSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.averageDose,0) as averageDoseMin, u.name as averageDoseMinUser, 0 as averageDoseMax, '' as averageDoseMaxUser from ")
			.append("		( ")
			.append("			select IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd_room,0) + 0.5*2*IFNULL(pd.hbid_room,0) + 1*1*IFNULL(pd.oqd_room,0) + 1*2*IFNULL(pd.obid_room,0) + 2*1*IFNULL(pd.tqd_room,0) + 2*2*IFNULL(pd.tbid_room,0) ) ")
			.append("			/ 100 ) * IFNULL(pd.lsnum_room,0) ) / IFNULL(sum(pd.lsnum_room),0),0 ) as averageDose, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.averageDose")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomAverageDoseRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getRoomTopAndBottomAverageDoseRSMData,data is empty");
		}
		
		rsmData.setTopRoomAverageDose(dbMaxData.getTopRoomAverageDose());
		rsmData.setTopRoomAverageDoseRSMName(dbMaxData.getTopRoomAverageDoseRSMName());
		rsmData.setBottomRoomAverageDose(dbMinData.getBottomRoomAverageDose());
		rsmData.setBottomRoomAverageDoseRSMName(dbMinData.getBottomRoomAverageDoseRSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomWhPortRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		int portRateBase = Integer.parseInt(CustomizedProperty.getContextProperty("portRateBase", "24"));

		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.whPortRate,0) as whPortRateMax, u.name as whPortRateMaxUser, 0 as whPortRateMin, '' as whPortRateMinUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.lsnum),0)/(IFNULL(sum(pd.portNum),0)*?) as whPortRate, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			and pd.portNum != 0 ")
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.whPortRate desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{portRateBase,paramDate,paramDate},new TopAndBottomWhPortRateRSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.whPortRate,0) as whPortRateMin, u.name as whPortRateMinUser, 0 as whPortRateMax, '' as whPortRateMaxUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.lsnum),0)/(IFNULL(sum(pd.portNum),0)*?) as whPortRate, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and pd.portNum != 0 ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.whPortRate")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{portRateBase,paramDate,paramDate},new TopAndBottomWhPortRateRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
	    	logger.warn("getTopAndBottomWhPortRateRSMData,data is empty");
	    }
		
		rsmData.setTopWhPortRate(dbMaxData.getTopWhPortRate());
		rsmData.setTopWhPortRateRSMName(dbMaxData.getTopWhPortRateRSMName());
		rsmData.setBottomWhPortRate(dbMinData.getBottomWhPortRate());
		rsmData.setBottomWhPortRateRSMName(dbMinData.getBottomWhPortRateRSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomWhDaysRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.whdays_emerging,0) as whDaysMax, u.name as whDaysMaxUser, 0 as whDaysMin, '' as whDaysMinUser from ")
			.append("		( ")
			.append(" 			select IFNULL(sum( ( ( 1*IFNULL(pd.whdays_emerging_1,0) + 2*IFNULL(pd.whdays_emerging_2,0) ")
		    .append("  				+ 3*IFNULL(pd.whdays_emerging_3,0) + 4*IFNULL(pd.whdays_emerging_4,0) ")
		    .append("  				+ 5*IFNULL(pd.whdays_emerging_5,0) + 6*IFNULL(pd.whdays_emerging_6,0) ")
		    .append("  				+ 7*IFNULL(pd.whdays_emerging_7,0) ")
		    .append("				) / 100) * IFNULL(pd.lsnum,0)) / IFNULL(sum(pd.lsnum),0),0 ) as whdays_emerging ")
			.append("			, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.whdays_emerging desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomWhDaysRSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.whdays_emerging,0) as whDaysMin, u.name as whDaysMinUser, 0 as whDaysMax, '' as whDaysMaxUser from ")
			.append("		( ")
			.append(" 			select IFNULL(sum( ( ( 1*IFNULL(pd.whdays_emerging_1,0) + 2*IFNULL(pd.whdays_emerging_2,0) ")
		    .append("  				+ 3*IFNULL(pd.whdays_emerging_3,0) + 4*IFNULL(pd.whdays_emerging_4,0) ")
		    .append("  				+ 5*IFNULL(pd.whdays_emerging_5,0) + 6*IFNULL(pd.whdays_emerging_6,0) ")
		    .append("  				+ 7*IFNULL(pd.whdays_emerging_7,0) ")
		    .append("				) / 100) * IFNULL(pd.lsnum,0)) / IFNULL(sum(pd.lsnum),0),0 ) as whdays_emerging ")
			.append("			, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.whdays_emerging")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomWhDaysRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getTopAndBottomWhDaysRSMData,data is empty");
		}
		
		rsmData.setTopEmergingWhDays(dbMaxData.getTopEmergingWhDays());
		rsmData.setTopEmergingWhDaysRSMName(dbMaxData.getTopEmergingWhDaysRSMName());
		rsmData.setBottomEmergingWhDays(dbMinData.getBottomEmergingWhDays());
		rsmData.setBottomEmergingWhDaysRSMName(dbMinData.getBottomEmergingWhDaysRSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getRoomTopAndBottomWhDaysRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.whdays_room,0) as whDaysMax, u.name as whDaysMaxUser, 0 as whDaysMin, '' as whDaysMinUser from ")
			.append("		( ")
			.append(" 			select IFNULL(sum( ( ( 1*IFNULL(pd.whdays_room_1,0) + 2*IFNULL(pd.whdays_room_2,0) ")
			.append("  				+ 3*IFNULL(pd.whdays_room_3,0) + 4*IFNULL(pd.whdays_room_4,0) ")
			.append("  				+ 5*IFNULL(pd.whdays_room_5,0) + 6*IFNULL(pd.whdays_room_6,0) ")
			.append("  				+ 7*IFNULL(pd.whdays_room_7,0) ")
			.append("				) / 100) * IFNULL(pd.lsnum_room,0)) / IFNULL(sum(pd.lsnum_room),0),0 ) as whdays_room ")
			.append("			, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.whdays_room desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomWhDaysRSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.whdays_room,0) as whDaysMin, u.name as whDaysMinUser, 0 as whDaysMax, '' as whDaysMaxUser from ")
			.append("		( ")
			.append(" 			select IFNULL(sum( ( ( 1*IFNULL(pd.whdays_room_1,0) + 2*IFNULL(pd.whdays_room_2,0) ")
			.append("  				+ 3*IFNULL(pd.whdays_room_3,0) + 4*IFNULL(pd.whdays_room_4,0) ")
			.append("  				+ 5*IFNULL(pd.whdays_room_5,0) + 6*IFNULL(pd.whdays_room_6,0) ")
			.append("  				+ 7*IFNULL(pd.whdays_room_7,0) ")
			.append("				) / 100) * IFNULL(pd.lsnum_room,0)) / IFNULL(sum(pd.lsnum_room),0),0 ) as whdays_room ")
			.append("			, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.whdays_room ")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomWhDaysRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getRoomTopAndBottomWhDaysRSMData,data is empty");
		}
		
		rsmData.setTopRoomWhDays(dbMaxData.getTopRoomWhDays());
		rsmData.setTopRoomWhDaysRSMName(dbMaxData.getTopRoomWhDaysRSMName());
		rsmData.setBottomRoomWhDays(dbMinData.getBottomRoomWhDays());
		rsmData.setBottomRoomWhDaysRSMName(dbMinData.getBottomRoomWhDaysRSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomBlRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.blRate,0) as blRateMax, u.name as blRateMaxUser, 0 as blRateMin, '' as blRateMinUser from ")
			.append("		( ")
			.append("			select IFNULL(IFNULL(sum(pd.whbwnum),0)/IFNULL(sum(pd.lsnum),0),0) as blRate, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			and pd.whbwnum != 0 ")
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.blRate desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomBlRateRSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.blRate,0) as blRateMin, u.name as blRateMinUser, 0 as blRateMax, '' as blRateMaxUser from ")
			.append("		( ")
			.append("			select IFNULL(IFNULL(sum(pd.whbwnum),0)/IFNULL(sum(pd.lsnum),0),0) as blRate, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and pd.whbwnum != 0 ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.blRate")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomBlRateRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getTopAndBottomBlRateRSMData,data is empty");
		}
		
		rsmData.setTopEmergingBlRate(dbMaxData.getTopEmergingBlRate());
		rsmData.setTopEmergingBlRateRSMName(dbMaxData.getTopEmergingBlRateRSMName());
		rsmData.setBottomEmergingBlRate(dbMinData.getBottomEmergingBlRate());
		rsmData.setBottomEmergingBlRateRSMName(dbMinData.getBottomEmergingBlRateRSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getRoomTopAndBottomBlRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.blRate,0) as blRateMax, u.name as blRateMaxUser, 0 as blRateMin, '' as blRateMinUser from ")
			.append("		( ")
			.append("			select IFNULL(IFNULL(sum(pd.whbwnum_room),0)/IFNULL(sum(pd.lsnum_room),0),0) as blRate, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			and pd.whbwnum_room != 0 ")
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.blRate desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomBlRateRSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.blRate,0) as blRateMin, u.name as blRateMinUser, 0 as blRateMax, '' as blRateMaxUser from ")
			.append("		( ")
			.append("			select IFNULL(IFNULL(sum(pd.whbwnum_room),0)/IFNULL(sum(pd.lsnum_room),0),0) as blRate, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and pd.whbwnum_room != 0 ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.blRate")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomBlRateRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getRoomTopAndBottomBlRateRSMData,data is empty");
		}
		
		rsmData.setTopRoomBlRate(dbMaxData.getTopRoomBlRate());
		rsmData.setTopRoomBlRateRSMName(dbMaxData.getTopRoomBlRateRSMName());
		rsmData.setBottomRoomBlRate(dbMinData.getBottomRoomBlRate());
		rsmData.setBottomRoomBlRateRSMName(dbMinData.getBottomRoomBlRateRSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomHomeWhNum1RSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.homeEmergingNum1,0) as homeEmergingNum1Max, u.name as homeEmergingNum1MaxUser, 0 as homeEmergingNum1Min, '' as homeEmergingNum1MinUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.home_wh_emerging_num1),0) as homeEmergingNum1, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.homeEmergingNum1 desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomEmergingNum1RSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.homeEmergingNum1,0) as homeEmergingNum1Min, u.name as homeEmergingNum1MinUser, 0 as homeEmergingNum1Max, '' as homeEmergingNum1MaxUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.home_wh_emerging_num1),0) as homeEmergingNum1, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.homeEmergingNum1")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomEmergingNum1RSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getTopAndBottomHomeWhNum1RSMData,data is empty");
		}
		
		rsmData.setTopEmergingWhNum1(dbMaxData.getTopEmergingWhNum1());
		rsmData.setTopEmergingWhNum1RSMName(dbMaxData.getTopEmergingWhNum1RSMName());
		rsmData.setBottomEmergingWhNum1(dbMinData.getBottomEmergingWhNum1());
		rsmData.setBottomEmergingWhNum1RSMName(dbMinData.getBottomEmergingWhNum1RSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getRoomTopAndBottomHomeWhNum1RSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.homeRoomNum1,0) as homeRoomNum1Max, u.name as homeRoomNum1MaxUser, 0 as homeRoomNum1Min, '' as homeRoomNum1MinUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.home_wh_room_num1),0) as homeRoomNum1, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.homeRoomNum1 desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomNum1RSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.homeRoomNum1,0) as homeRoomNum1Min, u.name as homeRoomNum1MinUser, 0 as homeRoomNum1Max, '' as homeRoomNum1MaxUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.home_wh_room_num1),0) as homeRoomNum1, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.homeRoomNum1")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomNum1RSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getRoomTopAndBottomHomeWhNum1RSMData,data is empty");
		}
		
		rsmData.setTopRoomWhNum1(dbMaxData.getTopRoomWhNum1());
		rsmData.setTopRoomWhNum1RSMName(dbMaxData.getTopRoomWhNum1RSMName());
		rsmData.setBottomRoomWhNum1(dbMinData.getBottomRoomWhNum1());
		rsmData.setBottomRoomWhNum1RSMName(dbMinData.getBottomRoomWhNum1RSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomAverDaysRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.homeAverNum3,0) as homeAverNum3Max, u.name as homeAverNum3MaxUser, 0 as homeAverNum3Min, '' as homeAverNum3MinUser from ")
			.append("		( ")
			.append("			select IFNULL( sum(pd.home_wh_emerging_num3) ")
			.append(" 			 		/ sum(")
			.append(" 					case when home_wh_emerging_num3 != 0 then 1 ")
			.append(" 					else 0 ")
			.append("					end ), 0) as homeAverNum3, ")
			.append(" 			h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.homeAverNum3 desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomEmergingNum3RSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.homeAverNum3,0) as homeAverNum3Min, u.name as homeAverNum3MinUser, 0 as homeAverNum3Max, '' as homeAverNum3MaxUser from ")
			.append("		( ")
			.append("			select IFNULL( sum(pd.home_wh_emerging_num3) ")
			.append(" 			 		/ sum(")
			.append(" 					case when home_wh_emerging_num3 != 0 then 1 ")
			.append(" 					else 0 ")
			.append("					end ), 0) as homeAverNum3, ")
			.append(" 			h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.homeAverNum3")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomEmergingNum3RSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getTopAndBottomAverDaysRSMData,data is empty");
		}
		
		rsmData.setTopEmergingWhNum3(dbMaxData.getTopEmergingWhNum3());
		rsmData.setTopEmergingWhNum3RSMName(dbMaxData.getTopEmergingWhNum3RSMName());
		rsmData.setBottomEmergingWhNum3(dbMinData.getBottomEmergingWhNum3());
		rsmData.setBottomEmergingWhNum3RSMName(dbMinData.getBottomEmergingWhNum3RSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getRoomTopAndBottomAverDaysRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.homeAverNum3,0) as homeAverNum3Max, u.name as homeAverNum3MaxUser, 0 as homeAverNum3Min, '' as homeAverNum3MinUser from ")
			.append("		( ")
			.append("			select IFNULL( sum(pd.home_wh_room_num3) ")
			.append(" 			 		/ sum(")
			.append(" 					case when home_wh_room_num3 != 0 then 1 ")
			.append(" 					else 0 ")
			.append("					end ), 0) as homeAverNum3, ")
			.append(" 			h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.homeAverNum3 desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomNum3RSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.homeAverNum3,0) as homeAverNum3Min, u.name as homeAverNum3MinUser, 0 as homeAverNum3Max, '' as homeAverNum3MaxUser from ")
			.append("		( ")
			.append("			select IFNULL( sum(pd.home_wh_room_num3) ")
			.append(" 			 		/ sum(")
			.append(" 					case when home_wh_room_num3 != 0 then 1 ")
			.append(" 					else 0 ")
			.append("					end ), 0) as homeAverNum3, ")
			.append(" 			h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.homeAverNum3")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomNum3RSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getRoomTopAndBottomAverDaysRSMData,data is empty");
		}
		
		rsmData.setTopRoomWhNum3(dbMaxData.getTopRoomWhNum3());
		rsmData.setTopRoomWhNum3RSMName(dbMaxData.getTopRoomWhNum3RSMName());
		rsmData.setBottomRoomWhNum3(dbMinData.getBottomRoomWhNum3());
		rsmData.setBottomRoomWhNum3RSMName(dbMinData.getBottomRoomWhNum3RSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomHomeWhNum4RSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.homeEmergingNum4,0) as homeEmergingNum4Max, u.name as homeEmergingNum4MaxUser, 0 as homeEmergingNum4Min, '' as homeEmergingNum4MinUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.home_wh_emerging_num4),0) as homeEmergingNum4, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.homeEmergingNum4 desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomEmergingNum4RSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.homeEmergingNum4,0) as homeEmergingNum4Min, u.name as homeEmergingNum4MinUser, 0 as homeEmergingNum4Max, '' as homeEmergingNum4MaxUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.home_wh_emerging_num4),0) as homeEmergingNum4, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.homeEmergingNum4")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomEmergingNum4RSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getTopAndBottomHomeWhNum4RSMData,data is empty");
		}
		
		rsmData.setTopEmergingWhNum4(dbMaxData.getTopEmergingWhNum4());
		rsmData.setTopEmergingWhNum4RSMName(dbMaxData.getTopEmergingWhNum4RSMName());
		rsmData.setBottomEmergingWhNum4(dbMinData.getBottomEmergingWhNum4());
		rsmData.setBottomEmergingWhNum4RSMName(dbMinData.getBottomEmergingWhNum4RSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getRoomTopAndBottomHomeWhNum4RSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			maxSB.append("select IFNULL(av2.homeRoomNum4,0) as homeRoomNum4Max, u.name as homeRoomNum4MaxUser, 0 as homeRoomNum4Min, '' as homeRoomNum4MinUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.home_wh_room_num4),0) as homeRoomNum4, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av2.homeRoomNum4 desc ")
			.append("		limit 1	");
			dbMaxData =  dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomNum4RSMDataRowMapper());
			
			minSB.append("select IFNULL(av1.homeRoomNum4,0) as homeRoomNum4Min, u.name as homeRoomNum4MinUser, 0 as homeRoomNum4Max, '' as homeRoomNum4MaxUser from ")
			.append("		( ")
			.append("			select IFNULL(sum(pd.home_wh_room_num4),0) as homeRoomNum4, h.rsmRegion")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			group by h.rsmRegion ")
			.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
			.append("		where u.level='RSM' and u.name != 'Vacant' ")
			.append("		order by av1.homeRoomNum4")
			.append("		limit 1	");
			dbMinData =  dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new TopAndBottomRoomNum4RSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
			logger.warn("getTopAndBottomHomeWhNum4RSMData,data is empty");
		}
		
		rsmData.setTopRoomWhNum4(dbMaxData.getTopRoomWhNum4());
		rsmData.setTopRoomWhNum4RSMName(dbMaxData.getTopRoomWhNum4RSMName());
		rsmData.setBottomRoomWhNum4(dbMinData.getBottomRoomWhNum4());
		rsmData.setBottomRoomWhNum4RSMName(dbMinData.getBottomRoomWhNum4RSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getCoreTopAndBottomRSMData(String hospitalShownFlag) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			minSB.append("select  (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMin,hosNumTemp.name as inRateMinUser, 0 as inRateMax, '' as inRateMaxUser ") 
			.append("	from ( ") 
			.append("		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name ") 
			.append("		from tbl_hospital h, tbl_userinfo u ") 
			.append("		where h.rsmRegion = u.region ") 
			.append("		and h.").append(hospitalShownFlag)
			.append("		and h.dragonType='Core' ")
			.append("		and u.level='RSM' ") 
			.append("		group by u.region ") 
			.append("	) hosNumTemp, ") 
			.append("	( ") 
			.append("		select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from (")
			.append("			select IFNULL(count(1),0) as inNum, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name  ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("			and h.").append(hospitalShownFlag)
			.append("			and h.dragonType='Core' ")
			.append("			group by h.rsmRegion ")
			.append("		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region ")
			.append("		where u.level='RSM' ")
			.append("	) inNumTemp")
			.append("	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion ")
			.append("	order by inNumTemp.inNum/hosNumTemp.hosNum ")
			.append("	limit 1	");
			dbMinData = dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate},new CoreTopAndBottomRSMDataRowMapper());
			
			maxSB.append(" select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMax,hosNumTemp.name as inRateMaxUser, 0 as inRateMin, '' as inRateMinUser ")
			.append("	from ( ")
			.append("		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name ")
			.append("		from tbl_hospital h, tbl_userinfo u ")
			.append("		where h.rsmRegion = u.region ")
			.append("		and h.").append(hospitalShownFlag)
			.append("		and h.dragonType='Core' ")
			.append("		and u.level='RSM' ")
			.append("		group by u.region ")
			.append("	) hosNumTemp, ")
			.append("	( select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from ( ")
			.append("			select IFNULL(count(1),0) as inNum, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("			and h.").append(hospitalShownFlag)
			.append("			and h.dragonType='Core' ")
			.append("			group by h.rsmRegion ")
			.append("		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region ")
			.append("		where u.level='RSM' ")
			.append("	) inNumTemp ")
			.append("	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion ")
			.append("	order by inNumTemp.inNum/hosNumTemp.hosNum desc ")
			.append("	limit 1	");
			dbMaxData = dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate},new CoreTopAndBottomRSMDataRowMapper());
		}catch(EmptyResultDataAccessException ee){
	    	logger.warn("getCoreTopAndBottomRSMData,data is empty");
	    }
		
		TopAndBottomRSMData rsmData = new TopAndBottomRSMData();
		rsmData.setCoreTopInRate(dbMaxData.getCoreTopInRate());
		rsmData.setCoreTopInRateRSMName(dbMaxData.getCoreTopInRateRSMName());
		rsmData.setCoreBottomInRate(dbMinData.getCoreBottomInRate());
		rsmData.setCoreBottomInRateRSMName(dbMinData.getCoreBottomInRateRSMName());
		
		return rsmData;
	}
	
	@Override
	public TopAndBottomRSMData getCoreTopAndBottomRSMWhRateData(String hospitalShownFlag) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			minSB.append("select  IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMin,pNumTemp.name as whRateMinUser, 0 as whRateMax, '' as whRateMaxUser ")
			.append("	from ( ")
			.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from ( ")
			.append("				select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("				and h.").append(hospitalShownFlag)
			.append("				and h.dragonType='Core' ")
			.append("				group by h.rsmRegion ")
			.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' ")
			.append("	) pNumTemp, ")
			.append("	( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
			.append("			select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			and h.dragonType='Core' ")
			.append("			group by h.rsmRegion ")
			.append("			) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' ")
			.append("	) lsNumTemp")
			.append("	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
			.append("	order by lsNumTemp.lsNum/pNumTemp.pNum ")
			.append("	limit 1	");
			
			dbMinData = dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate},new CoreTopAndBottomRSMWhRateRowMapper());
			
			maxSB.append("  select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMax,pNumTemp.name as whRateMaxUser, 0 as whRateMin, '' as whRateMinUser ")
			.append("	from ( ")
			.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (")
			.append("				select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("				and h.").append(hospitalShownFlag)
			.append("				and h.dragonType='Core' ")
			.append("				group by h.rsmRegion ")
			.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' ")
			.append("	) pNumTemp, ")
			.append("	( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
			.append("				select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("				and h.").append(hospitalShownFlag)
			.append("				and h.dragonType='Core' ")
			.append("				group by h.rsmRegion ")
			.append("			) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' ")
			.append("	) lsNumTemp ")
			.append("	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
			.append("	order by lsNumTemp.lsNum/pNumTemp.pNum desc ")
			.append("	limit 1	");
			dbMaxData = dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate},new CoreTopAndBottomRSMWhRateRowMapper());
		}catch(EmptyResultDataAccessException ee){
	    	logger.warn("getCoreTopAndBottomRSMWhRateData,data is empty");
	    }

		TopAndBottomRSMData rsmData = new TopAndBottomRSMData();
		rsmData.setCoreTopWhRate(dbMaxData.getCoreTopWhRate());
		rsmData.setCoreTopWhRateRSMName(dbMaxData.getCoreTopWhRateRSMName());
		rsmData.setCoreBottomWhRate(dbMinData.getCoreBottomWhRate());
		rsmData.setCoreBottomWhRateRSMName(dbMinData.getCoreBottomWhRateRSMName());
		
		return rsmData;
	}
	

	@Override
	public TopAndBottomRSMData getEmergingTopAndBottomRSMWhRateData(String hospitalShownFlag) throws Exception {
		StringBuffer maxSB = new StringBuffer();
		StringBuffer minSB = new StringBuffer();
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		TopAndBottomRSMData dbMaxData = new TopAndBottomRSMData();
		TopAndBottomRSMData dbMinData = new TopAndBottomRSMData();
		
		try{
			minSB.append("select  IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMin,pNumTemp.name as whRateMinUser, 0 as whRateMax, '' as whRateMaxUser ")
			.append("	from ( ")
			.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from ( ")
			.append("				select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("				and h.").append(hospitalShownFlag)
			.append("				and h.dragonType='Emerging' ")
			.append("				group by h.rsmRegion ")
			.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' ")
			.append("	) pNumTemp, ")
			.append("	( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
			.append("			select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion ")
			.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("			where pd.hospitalName = h.name ")
			.append("			and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
			.append("			and h.").append(hospitalShownFlag)
			.append("			and h.dragonType='Emerging' ")
			.append("			group by h.rsmRegion ")
			.append("			) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' ")
			.append("	) lsNumTemp")
			.append("	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
			.append("	order by lsNumTemp.lsNum/pNumTemp.pNum ")
			.append("	limit 1	");
			dbMinData = dataBean.getJdbcTemplate().queryForObject(minSB.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate},new EmergingTopAndBottomRSMWhRateRowMapper());
			
			maxSB.append(" select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMax,pNumTemp.name as whRateMaxUser, 0 as whRateMin, '' as whRateMinUser ")
			.append("	from ( ")
			.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (")
			.append("				select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("				and h.").append(hospitalShownFlag)
			.append("				and h.dragonType='Emerging' ")
			.append("				group by h.rsmRegion ")
			.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' ")
			.append("	) pNumTemp, ")
			.append("	( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
			.append("				select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion ")
			.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
			.append("				where pd.hospitalName = h.name ")
			.append("				and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY)")
			.append("				and h.").append(hospitalShownFlag)
			.append("				and h.dragonType='Emerging' ")
			.append("				group by h.rsmRegion ")
			.append("			) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
			.append("			where u.level='RSM' ")
			.append("	) lsNumTemp ")
			.append("	where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
			.append("	order by lsNumTemp.lsNum/pNumTemp.pNum desc ")
			.append("	limit 1	");
			dbMaxData = dataBean.getJdbcTemplate().queryForObject(maxSB.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate},new EmergingTopAndBottomRSMWhRateRowMapper());
		}catch(EmptyResultDataAccessException ee){
	    	logger.warn("getEmergingTopAndBottomRSMWhRateData,data is empty");
	    }

		TopAndBottomRSMData rsmData = new TopAndBottomRSMData();
		rsmData.setTopEmergingWhRate(dbMaxData.getTopEmergingWhRate());
		rsmData.setTopEmergingWhRateRSMName(dbMaxData.getTopEmergingWhRateRSMName());
		rsmData.setBottomEmergingWhRate(dbMinData.getBottomEmergingWhRate());
		rsmData.setBottomEmergingWhRateRSMName(dbMinData.getBottomEmergingWhRateRSMName());
		
		return rsmData;
	}
	
	@Override
	public List<DailyReportData> getAllRSMDataByTelephone(String hospitalShownFlag) throws Exception {
		StringBuffer sb = new StringBuffer();
		Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		sb.append("select u.name,u.userCode,")
			.append(" ( select count(1) from tbl_hospital h ")
			.append("	where h.rsmRegion = u.region ")
			.append("	and h.").append(hospitalShownFlag)
			.append(" ) hosNum,")
			.append(" count(1) as inNum, ")
			.append(" IFNULL(sum(pd.pnum),0) as pnum, ")
			.append(" IFNULL(sum(pd.whnum),0) as whnum, ")
			.append(" IFNULL(sum(pd.lsnum),0) as lsnum, ")
			.append(" IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose ")
			.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		    .append(" where pd.hospitalName = h1.name ")
    		.append(" and h1.rsmRegion = u.region ")
    		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
    		.append(" and h1.").append(hospitalShownFlag)
    		.append(" and u.level='RSM' ")
		    .append(" group by u.region ");
		
		return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{paramDate,paramDate},new DailyReportDataRowMapper());
	}
	
	@Override
	public PediatricsData getPediatricsDataByHospital(String hospitalCode)
			throws Exception {
		return dataBean.getJdbcTemplate().queryForObject("select pd.*,h.code as hospitalCode,h.dsmName,h.isPedAssessed,h.dragonType from tbl_pediatrics_data pd, tbl_hospital h where h.code=? and DATE_FORMAT(pd.createdate,'%Y-%m-%d') = curdate() and pd.hospitalName = h.name", new Object[]{hospitalCode}, new PediatricsRowMapper());
	}
	
	@Override
	public PediatricsData getPediatricsRoomDataByHospital(String hospitalCode)
			throws Exception {
		return dataBean.getJdbcTemplate().queryForObject("select pd.*,h.code as hospitalCode,h.dsmName,h.isPedAssessed,h.dragonType from tbl_pediatrics_data pd, tbl_hospital h where h.code=? and DATE_FORMAT(pd.createdate,'%Y-%m-%d') = curdate() and pd.hospitalName = h.name", new Object[]{hospitalCode}, new PediatricsRoomRowMapper());
	}

	@Override
	public PediatricsData getPediatricsDataByHospitalAndDate(
			String hospitalName, Date createdate) throws Exception {
		return dataBean.getJdbcTemplate().queryForObject("select pd.*,h.code as hospitalCode,h.dsmName,h.isPedAssessed,h.dragonType from tbl_pediatrics_data pd, tbl_hospital h where pd.hospitalName=? and DATE_FORMAT(pd.createdate,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') and pd.hospitalName = h.name", new Object[]{hospitalName,new Timestamp(createdate.getTime())}, new PediatricsRowMapper());
	}
	
	@Override
	public List<PediatricsData> getPediatricsDataByDate(Date createdatebegin, Date createdateend) throws Exception {
		String sql = "select pd.*,h.code as hospitalCode,h.dsmName,h.isPedAssessed,h.dragonType,pd.portNum from tbl_pediatrics_data pd, tbl_hospital h where DATE_FORMAT(pd.createdate,'%Y-%m-%d') between DATE_FORMAT(?,'%Y-%m-%d') and DATE_FORMAT(?,'%Y-%m-%d') and pd.hospitalName = h.name order by createdate desc";
		return dataBean.getJdbcTemplate().query(sql, new Object[]{new Timestamp(createdatebegin.getTime()),new Timestamp(createdateend.getTime())},new PediatricsWithPortNumRowMapper());
	}

	@Override
	public PediatricsData getPediatricsDataById(int id) throws Exception {
		return dataBean.getJdbcTemplate().queryForObject("select pd.*,h.code as hospitalCode,h.dsmName,h.isPedAssessed,h.dragonType from tbl_pediatrics_data pd, tbl_hospital h where pd.id=? and pd.hospitalName = h.name", new Object[]{id}, new PediatricsRowMapper());
	}

    @Override
    public List<MobilePEDDailyData> getDailyPEDData4RSMByRegion(String region, String hospitalShownFlag) throws Exception {
        StringBuffer mobilePEDDailySQL = new StringBuffer();
        
        Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
        
        mobilePEDDailySQL.append("select ui.region as name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h ")
        .append("	where h.rsmRegion = ui.region ")
        .append("	and h.").append(hospitalShownFlag)
        .append(" ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
        .append(" from ( ")
        .append(" select u.region as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
        .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
        .append(" and h1.").append(hospitalShownFlag)
        .append(" and u.level='RSM' ")
        .append(" and u.regionCenter = ? ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSM' ")
        .append(" and ui.regionCenter = ? ")
        .append(" order by ui.region");
        return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,region,region},new PediatricsMobileRowMapper());
    }

    @Cacheable(value="getDailyPEDData4CountoryMobile")
    public MobilePEDDailyData getDailyPEDData4CountoryMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception {
        StringBuffer mobilePEDDailySQL = new StringBuffer();
        
        mobilePEDDailySQL.append("select '全国' as name,null as userCode,")
            .append(" '' as regionCenterCN, ")
            .append(" (select count(1) from tbl_hospital h ")
            .append("	where h.").append(hospitalShownFlag)
            .append(" ) hosNum, ")
            .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
            .append(" from ( ")
            .append("   select ped.* from tbl_pediatrics_data ped, tbl_hospital h ")
            .append("   where ped.hospitalName = h.name ")
            .append("   and ped.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
            .append("   and h.").append(hospitalShownFlag)
            .append(" ) pd ");
        return dataBean.getJdbcTemplate().queryForObject(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate},new PediatricsMobileRowMapper());
    }
	
	public List<MobilePEDDailyData> getDailyPEDData4DSMMobile(String telephone, String hospitalShownFlag) throws Exception {
	    StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
	    Date date = DateUtils.populateParamDate(new Date());
	    Timestamp paramDate = new Timestamp(date.getTime());
	    
	    mobilePEDDailySQL.append("select ui.name, ui.userCode,")
	    .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h ")
        .append("	where h.dsmCode = ui.userCode ")
        .append("	and h.rsmRegion = ui.region ")
        .append("	and h.").append(hospitalShownFlag)
        .append(" ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
        .append(" from ( ")
        .append(" select u.name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
        .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and h1.dsmCode = u.userCode ")
        .append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
        .append(" and h1.").append(hospitalShownFlag)
        .append(" and u.level='DSM' ")
        .append(" and u.region = ( select region from tbl_userinfo where telephone=? ) ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='DSM' ")
        .append(" and ui.region = ( select region from tbl_userinfo where telephone=? )");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsMobileRowMapper());
    }
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDData4RSMMobile(String telephone, String hospitalShownFlag)	throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
        Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
        mobilePEDDailySQL.append("select ui.region as name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h ")
        .append("	where h.rsmRegion = ui.region ")
        .append("	and h.").append(hospitalShownFlag)
        .append(" ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
        .append(" from ( ")
        .append(" select u.region as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
        .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
        .append(" and h1.").append(hospitalShownFlag)
        .append(" and u.level='RSM' ")
        .append(" and u.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSM' ")
        .append(" and ui.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
        .append(" order by ui.region");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsMobileRowMapper());
	}

	@Override
	@Cacheable(value="getDailyPEDData4RSDMobile")
	public List<MobilePEDDailyData> getDailyPEDData4RSDMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
	    mobilePEDDailySQL.append("select ( select distinct property_value from tbl_property where property_name = ui.regionCenter ) as name,ui.userCode,")
	        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
	        .append(" ( select count(1) from tbl_hospital h ")
    	    .append("	where h.region = ui.regionCenter ")
    	    .append("	and h.").append(hospitalShownFlag)
    	    .append(" ) hosNum, ")
	        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
    	    .append(" from ( ")
    	    .append(" select u.regionCenter as name, u.userCode,")
    	    .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
    	    .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
    	    .append(" where pd.hospitalName = h1.name ")
    	    .append(" and h1.region = u.regionCenter ")
    	    .append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
    	    .append(" and h1.").append(hospitalShownFlag)
    	    .append(" and u.level='RSD' ")
    	    .append(" group by u.regionCenter ")
    	    .append(" ) dailyData ")
    	    .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
    	    .append(" where ui.level='RSD' ")
    	    .append(" order by ui.regionCenter");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate}, new PediatricsMobileRowMapper());
	}
	
	public List<MobilePEDDailyData> getDailyPEDChildData4DSMMobile(String telephone, String hospitalShownFlag) throws Exception {
	    StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
	    Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
	    
	    mobilePEDDailySQL.append("select ui.name,ui.userCode,")
	    .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
	    .append(" ( select count(1) from tbl_hospital h ")
	    .append(" 	where h.saleCode = ui.userCode ")
	    .append("	and h.rsmRegion = ui.region ")
	    .append("	and h.dsmCode = ui.superior ")
	    .append("	and h.").append(hospitalShownFlag)
	    .append(" ) hosNum,")
	    .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
	    .append(" from ( ")
	    .append(" select u.name,u.userCode,")
	    .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
	    .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
	    .append(" and h1.rsmRegion = u.region ")
	    .append(" and h1.dsmCode = u.superior ")
	    .append(" and h1.saleCode = u.userCode ")
	    .append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
	    .append(" and h1.").append(hospitalShownFlag)
	    .append(" and u.level='REP' ")
	    .append(" and u.superior = ( select userCode from tbl_userinfo where telephone=? ) ")
	    .append(" group by u.userCode ")
	    .append(" ) dailyData ")
	    .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
	    .append(" where ui.level='REP' ")
	    .append(" and ui.superior = ( select userCode from tbl_userinfo where telephone=? )");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsMobileRowMapper());
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDChildData4RSMMobile(String telephone, String hospitalShownFlag)throws Exception {
	    return this.getDailyPEDData4DSMMobile(telephone,hospitalShownFlag);
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDChildData4RSDMobile(String telephone, String hospitalShownFlag) throws Exception {
	    return this.getDailyPEDData4RSMMobile(telephone,hospitalShownFlag);
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDWhPortData4RSMByRegion(String region, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode,")
	    .append(" IFNULL(dailyData.lsnum,0) as lsnum,  ")
        .append(" IFNULL(dailyData.portNum,0) as portNum  ")
		.append(" from ( ")
		.append(" select u.userCode,")
        .append(" IFNULL(sum(pd.lsnum),0) as lsnum, ")
        .append(" IFNULL(sum(pd.portNum),0) as portNum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and pd.portNum != 0 ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and u.level='RSM' ")
		.append(" and u.regionCenter = ? ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='RSM' ")
		.append(" and ui.regionCenter = ? ")
		.append(" order by ui.region");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,region,region},new PediatricsWhPortRowMapper());
	}
	
	@Cacheable(value="getDailyPEDWhPortData4CountoryMobile")
	public MobilePEDDailyData getDailyPEDWhPortData4CountoryMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		mobilePEDDailySQL.append("select null as userCode,")
		.append(" IFNULL(sum(pd.lsnum),0) as lsnum, ")
        .append(" IFNULL(sum(pd.portNum),0) as portNum ")
		.append(" from tbl_pediatrics_data pd, tbl_hospital h ")
		.append(" where pd.hospitalName = h.name ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and pd.portNum != 0 ")
		.append(" and h.").append(hospitalShownFlag);
		return dataBean.getJdbcTemplate().queryForObject(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate},new PediatricsWhPortRowMapper());
	}
	
	public List<MobilePEDDailyData> getDailyPEDWhPortData4DSMMobile(String telephone, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode,")
	    .append(" IFNULL(dailyData.lsnum,0) as lsnum,  ")
        .append(" IFNULL(dailyData.portNum,0) as portNum  ")
		.append(" from ( ")
		.append(" select u.userCode,")
        .append(" IFNULL(sum(pd.lsnum),0) as lsnum, ")
        .append(" IFNULL(sum(pd.portNum),0) as portNum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and h1.dsmCode = u.userCode ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and pd.portNum != 0 ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and u.level='DSM' ")
		.append(" and u.region = ( select region from tbl_userinfo where telephone=? ) ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='DSM' ")
		.append(" and ui.region = ( select region from tbl_userinfo where telephone=? )");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsWhPortRowMapper());
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDWhPortData4RSMMobile(String telephone, String hospitalShownFlag)	throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode,")
	    .append(" IFNULL(dailyData.lsnum,0) as lsnum,  ")
        .append(" IFNULL(dailyData.portNum,0) as portNum  ")
		.append(" from ( ")
		.append(" select u.userCode,")
        .append(" IFNULL(sum(pd.lsnum),0) as lsnum, ")
        .append(" IFNULL(sum(pd.portNum),0) as portNum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and pd.portNum != 0 ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and u.level='RSM' ")
		.append(" and u.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='RSM' ")
		.append(" and ui.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
		.append(" order by ui.region");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsWhPortRowMapper());
	}
	
	@Override
	@Cacheable(value="getDailyPEDWhPortData4RSDMobile")
	public List<MobilePEDDailyData> getDailyPEDWhPortData4RSDMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		mobilePEDDailySQL.append("select ui.userCode,")
	    .append(" IFNULL(dailyData.lsnum,0) as lsnum,  ")
        .append(" IFNULL(dailyData.portNum,0) as portNum  ")
		.append(" from ( ")
		.append(" select u.userCode,")
        .append(" IFNULL(sum(pd.lsnum),0) as lsnum, ")
        .append(" IFNULL(sum(pd.portNum),0) as portNum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.region = u.regionCenter ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and pd.portNum != 0 ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and u.level='RSD' ")
		.append(" group by u.regionCenter ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='RSD' ")
		.append(" order by ui.regionCenter");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate}, new PediatricsWhPortRowMapper());
	}
	
	public List<MobilePEDDailyData> getDailyPEDWhPortChildData4DSMMobile(String telephone, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode,")
	    .append(" IFNULL(dailyData.lsnum,0) as lsnum,  ")
        .append(" IFNULL(dailyData.portNum,0) as portNum  ")
		.append(" from ( ")
		.append(" select u.userCode,")
        .append(" IFNULL(sum(pd.lsnum),0) as lsnum, ")
        .append(" IFNULL(sum(pd.portNum),0) as portNum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and h1.dsmCode = u.superior ")
		.append(" and h1.saleCode = u.userCode ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and pd.portNum != 0 ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and u.level='REP' ")
		.append(" and u.superior = ( select userCode from tbl_userinfo where telephone=? ) ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='REP' ")
		.append(" and ui.superior = ( select userCode from tbl_userinfo where telephone=? )");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsWhPortRowMapper());
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDWhPortChildData4RSMMobile(String telephone, String hospitalShownFlag )	throws Exception {
		return this.getDailyPEDWhPortData4DSMMobile(telephone,hospitalShownFlag);
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDWhPortChildData4RSDMobile(String telephone, String hospitalShownFlag) throws Exception {
		return this.getDailyPEDWhPortData4RSMMobile(telephone,hospitalShownFlag);
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyCorePEDData4RSMByRegion(String region, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode,")
		.append(" ( select count(1) from tbl_hospital h ")
		.append("	where h.rsmRegion = ui.region ")
		.append("	and h.").append(hospitalShownFlag)
		.append("	and h.dragonType='Core' ")
		.append(" ) coreHosNum, ")
		.append(" IFNULL(dailyData.inNum,0) as coreInNum ")
		.append(" ,IFNULL(dailyData.pnum,0) as corePNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as coreLsNum ")
		.append(" from ( ")
		.append(" select u.userCode,")
		.append(" count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
        .append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Core' ")
		.append(" and u.level='RSM' ")
		.append(" and u.regionCenter = ? ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='RSM' ")
		.append(" and ui.regionCenter = ? ")
		.append(" order by ui.region");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,region,region},new PediatricsCoreHosRowMapper());
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyEmergingPEDData4RSMByRegion(String region, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode ")
		.append(" ,IFNULL(dailyData.pnum,0) as emergingPNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as emergingLsNum ")
		.append(" from ( ")
		.append(" select u.userCode,")
		.append(" count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
		.append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Emerging' ")
		.append(" and u.level='RSM' ")
		.append(" and u.regionCenter = ? ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='RSM' ")
		.append(" and ui.regionCenter = ? ")
		.append(" order by ui.region");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,region,region},new PediatricsEmergingHosRowMapper());
	}
	
	@Cacheable(value="getDailyCorePEDData4CountoryMobile")
	public MobilePEDDailyData getDailyCorePEDData4CountoryMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		mobilePEDDailySQL.append("select null as userCode,")
		.append(" (select count(1) from tbl_hospital h ")
		.append("	where h.").append(hospitalShownFlag)
		.append("	and h.dragonType='Core' ")
		.append(" ) coreHosNum, ")
		.append(" pd.coreInNum ")
		.append(" ,IFNULL(pd.pnum,0) as corePNum ")
		.append(" ,IFNULL(pd.lsnum,0) as coreLsNum ")
		.append(" from ( ")
		.append("   select count(1) as coreInNum,IFNULL(sum(ped.pnum),0) as pnum,IFNULL(sum(ped.lsnum),0) as lsnum ")
		.append(" 	from tbl_pediatrics_data ped, tbl_hospital h ")
		.append("   where ped.hospitalName = h.name ")
		.append("   and ped.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append("   and h.").append(hospitalShownFlag)
		.append(" 	and h.dragonType='Core' ")
		.append(" ) pd ");
		return dataBean.getJdbcTemplate().queryForObject(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate},new PediatricsCoreHosRowMapper());
	}
	
	@Cacheable(value="getDailyEmergingPEDData4CountoryMobile")
	public MobilePEDDailyData getDailyEmergingPEDData4CountoryMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		mobilePEDDailySQL.append("select '' as userCode, IFNULL(sum(ped.pnum),0) as emergingPNum,IFNULL(sum(ped.lsnum),0) as emergingLsNum ")
		.append("   from tbl_pediatrics_data ped, tbl_hospital h ")
		.append("   where ped.hospitalName = h.name ")
		.append("   and ped.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append("   and h.").append(hospitalShownFlag)
		.append(" 	and h.dragonType='Emerging' ");
		return dataBean.getJdbcTemplate().queryForObject(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate},new PediatricsEmergingHosRowMapper());
	}
	
	public List<MobilePEDDailyData> getDailyCorePEDData4DSMMobile(String telephone, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode,")
		.append(" ( select count(1) from tbl_hospital h ")
		.append("	where h.dsmCode = ui.userCode ")
		.append("	and h.rsmRegion = ui.region ")
		.append("	and h.").append(hospitalShownFlag)
		.append("	and h.dragonType='Core' ")
		.append(" ) coreHosNum, ")
		.append(" IFNULL(dailyData.inNum,0) as coreInNum ")
		.append(" ,IFNULL(dailyData.pnum,0) as corePNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as coreLsNum ")
		.append(" from ( ")
		.append(" select u.name,u.userCode,")
		.append(" count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
        .append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and h1.dsmCode = u.userCode ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Core' ")
		.append(" and u.level='DSM' ")
		.append(" and u.region = ( select region from tbl_userinfo where telephone=? ) ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='DSM' ")
		.append(" and ui.region = ( select region from tbl_userinfo where telephone=? )");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsCoreHosRowMapper());
	}
	
	public List<MobilePEDDailyData> getDailyEmergingPEDData4DSMMobile(String telephone, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode ")
		.append(" ,IFNULL(dailyData.pnum,0) as emergingPNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as emergingLsNum ")
		.append(" from ( ")
		.append(" select u.name,u.userCode,")
		.append(" count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
		.append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and h1.dsmCode = u.userCode ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Emerging' ")
		.append(" and u.level='DSM' ")
		.append(" and u.region = ( select region from tbl_userinfo where telephone=? ) ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='DSM' ")
		.append(" and ui.region = ( select region from tbl_userinfo where telephone=? )");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsEmergingHosRowMapper());
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyCorePEDData4RSMMobile(String telephone, String hospitalShownFlag)	throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode,")
		.append(" ( select count(1) from tbl_hospital h ")
		.append("	where h.rsmRegion = ui.region ")
		.append("	and h.").append(hospitalShownFlag)
		.append("	and h.dragonType='Core' ")
		.append(" ) coreHosNum, ")
		.append(" IFNULL(dailyData.inNum,0) as coreInNum ")
		.append(" ,IFNULL(dailyData.pnum,0) as corePNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as coreLsNum ")
		.append(" from ( ")
		.append(" select u.userCode,")
		.append(" count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
        .append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Core' ")
		.append(" and u.level='RSM' ")
		.append(" and u.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='RSM' ")
		.append(" and ui.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
		.append(" order by ui.region");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsCoreHosRowMapper());
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyEmergingPEDData4RSMMobile(String telephone, String hospitalShownFlag)	throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode ")
		.append(" ,IFNULL(dailyData.pnum,0) as emergingPNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as emergingLsNum ")
		.append(" from ( ")
		.append(" select u.userCode,")
		.append(" count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
		.append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Emerging' ")
		.append(" and u.level='RSM' ")
		.append(" and u.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='RSM' ")
		.append(" and ui.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
		.append(" order by ui.region");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsEmergingHosRowMapper());
	}
	
	@Override
	@Cacheable(value="getDailyCorePEDData4RSDMobile")
	public List<MobilePEDDailyData> getDailyCorePEDData4RSDMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		mobilePEDDailySQL.append("select ui.userCode,")
		.append(" ( select count(1) from tbl_hospital h ")
		.append("	where h.region = ui.regionCenter ")
		.append("	and h.").append(hospitalShownFlag)
		.append("	and h.dragonType='Core' ")
		.append(" ) coreHosNum, ")
		.append(" IFNULL(dailyData.inNum,0) as coreInNum ")
		.append(" ,IFNULL(dailyData.pnum,0) as corePNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as coreLsNum ")
		.append(" from ( ")
		.append(" select u.userCode,")
		.append(" count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
        .append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.region = u.regionCenter ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Core' ")
		.append(" and u.level='RSD' ")
		.append(" group by u.regionCenter ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='RSD' ")
		.append(" order by ui.regionCenter");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate}, new PediatricsCoreHosRowMapper());
	}
	
	@Override
	@Cacheable(value="getDailyEmergingPEDData4RSDMobile")
	public List<MobilePEDDailyData> getDailyEmergingPEDData4RSDMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		mobilePEDDailySQL.append("select ui.userCode ")
		.append(" ,IFNULL(dailyData.pnum,0) as emergingPNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as emergingLsNum ")
		.append(" from ( ")
		.append(" select u.userCode,")
		.append(" count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
		.append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.region = u.regionCenter ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Emerging' ")
		.append(" and u.level='RSD' ")
		.append(" group by u.regionCenter ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='RSD' ")
		.append(" order by ui.regionCenter");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate}, new PediatricsEmergingHosRowMapper());
	}
	
	public List<MobilePEDDailyData> getDailyCorePEDChildData4DSMMobile(String telephone, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode,")
		.append(" ( select count(1) from tbl_hospital h ")
		.append("	where h.saleCode = ui.userCode ")
		.append("	and h.rsmRegion = ui.region ")
		.append("	and h.dsmCode = ui.superior ")
		.append("	and h.").append(hospitalShownFlag)
		.append("	and h.dragonType='Core' ")
		.append(" ) coreHosNum, ")
		.append(" IFNULL(dailyData.inNum,0) as coreInNum ")
		.append(" ,IFNULL(dailyData.pnum,0) as corePNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as coreLsNum ")
		.append(" from ( ")
		.append(" select u.userCode ")
		.append(" ,count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
        .append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and h1.dsmCode = u.superior ")
		.append(" and h1.saleCode = u.userCode ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Core' ")
		.append(" and u.level='REP' ")
		.append(" and u.superior = ( select userCode from tbl_userinfo where telephone=? ) ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='REP' ")
		.append(" and ui.superior = ( select userCode from tbl_userinfo where telephone=? )");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsCoreHosRowMapper());
	}
	
	public List<MobilePEDDailyData> getDailyEmergingPEDChildData4DSMMobile(String telephone, String hospitalShownFlag) throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		mobilePEDDailySQL.append("select ui.userCode ")
		.append(" ,IFNULL(dailyData.pnum,0) as emergingPNum ")
		.append(" ,IFNULL(dailyData.lsnum,0) as emergingLsNum ")
		.append(" from ( ")
		.append(" select u.userCode ")
		.append(" ,count(1) as inNum ")
		.append(" ,IFNULL(sum(pd.pnum),0) as pnum ")
		.append(" ,IFNULL(sum(pd.lsnum),0) as lsnum ")
		.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		.append(" where pd.hospitalName = h1.name ")
		.append(" and h1.rsmRegion = u.region ")
		.append(" and h1.dsmCode = u.superior ")
		.append(" and h1.saleCode = u.userCode ")
		.append(" and pd.createdate between ? and DATE_ADD(?, INTERVAL 1 DAY) ")
		.append(" and h1.").append(hospitalShownFlag)
		.append(" and h1.dragonType='Emerging' ")
		.append(" and u.level='REP' ")
		.append(" and u.superior = ( select userCode from tbl_userinfo where telephone=? ) ")
		.append(" group by u.userCode ")
		.append(" ) dailyData ")
		.append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
		.append(" where ui.level='REP' ")
		.append(" and ui.superior = ( select userCode from tbl_userinfo where telephone=? )");
		return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,paramDate,telephone,telephone},new PediatricsEmergingHosRowMapper());
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyCorePEDChildData4RSMMobile(String telephone, String hospitalShownFlag) throws Exception {
		return this.getDailyCorePEDData4DSMMobile(telephone,hospitalShownFlag);
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyEmergingPEDChildData4RSMMobile(String telephone, String hospitalShownFlag) throws Exception {
		return this.getDailyEmergingPEDData4DSMMobile(telephone,hospitalShownFlag);
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyCorePEDChildData4RSDMobile(String telephone, String hospitalShownFlag) throws Exception {
		return this.getDailyCorePEDData4RSMMobile(telephone,hospitalShownFlag);
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyEmergingPEDChildData4RSDMobile(String telephone, String hospitalShownFlag) throws Exception {
		return this.getDailyEmergingPEDData4RSMMobile(telephone,hospitalShownFlag);
	}
	
	@Override
	public void insert(final PediatricsData pediatricsData, final UserInfo operator, final Hospital hospital) throws Exception {
		logger.info(">>PediatricsDAOImpl insert");
		final String sql = new StringBuilder("insert into tbl_pediatrics_data(")
			.append("id,createdate,hospitalName,pnum,whnum,lsnum,etmsCode,operatorName,region,rsmRegion,")
			.append("hqd,hbid,oqd,obid,tqd,tbid,recipeType,updatedate,dsmCode,portNum,whbwnum,")
			.append("whdays_emerging_1,whdays_emerging_2,whdays_emerging_3,whdays_emerging_4,whdays_emerging_5,whdays_emerging_6,whdays_emerging_7,")
			.append("home_wh_emerging_num1,home_wh_emerging_num2,home_wh_emerging_num3,home_wh_emerging_num4 ")
			.append(") values( ")
			.append("null,NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?)").toString();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, hospital.getName());
				ps.setInt(2, pediatricsData.getPnum());
				ps.setInt(3, pediatricsData.getWhnum());
				ps.setInt(4, pediatricsData.getLsnum());
				ps.setString(5, hospital.getSaleCode());
				ps.setString(6, operator.getName());
				ps.setString(7, hospital.getRegion());
				ps.setString(8, hospital.getRsmRegion());
				ps.setDouble(9, pediatricsData.getHqd());
				ps.setDouble(10, pediatricsData.getHbid());
				ps.setDouble(11, pediatricsData.getOqd());
				ps.setDouble(12, pediatricsData.getObid());
				ps.setDouble(13, pediatricsData.getTqd());
				ps.setDouble(14, pediatricsData.getTbid());
				ps.setString(15, pediatricsData.getRecipeType());
				ps.setString(16, (operator.getSuperior()==null||"".equalsIgnoreCase(operator.getSuperior()))?operator.getUserCode():operator.getSuperior());
				ps.setInt(17, hospital.getPortNum());
				ps.setInt(18, pediatricsData.getWhbwnum());
				ps.setDouble(19, pediatricsData.getWhdaysEmerging1Rate());
				ps.setDouble(20, pediatricsData.getWhdaysEmerging2Rate());
				ps.setDouble(21, pediatricsData.getWhdaysEmerging3Rate());
				ps.setDouble(22, pediatricsData.getWhdaysEmerging4Rate());
				ps.setDouble(23, pediatricsData.getWhdaysEmerging5Rate());
				ps.setDouble(24, pediatricsData.getWhdaysEmerging6Rate());
				ps.setDouble(25, pediatricsData.getWhdaysEmerging7Rate());
				ps.setInt(26, pediatricsData.getHomeWhEmergingNum1());
				ps.setInt(27, pediatricsData.getHomeWhEmergingNum2());
				ps.setInt(28, pediatricsData.getHomeWhEmergingNum3());
				ps.setInt(29, pediatricsData.getHomeWhEmergingNum4());
				return ps;
			}
		}, keyHolder);
		logger.info("returned id is "+keyHolder.getKey().intValue());
	}
	
	@Override
	public void insertRoomData(final PediatricsData pediatricsData, final UserInfo operator, final Hospital hospital) throws Exception {
		logger.info(">>PediatricsDAOImpl insert");
		final String sql = new StringBuilder("insert into tbl_pediatrics_data(")
		.append("id,createdate,hospitalName,pnum_room,whnum_room,lsnum_room,etmsCode,operatorName,region,rsmRegion,")
		.append("hqd_room,hbid_room,oqd_room,obid_room,tqd_room,tbid_room,recipeType,updatedate,dsmCode,portNum,whbwnum_room,")
		.append("whdays_room_1,whdays_room_2,whdays_room_3,whdays_room_4,whdays_room_5,whdays_room_6,whdays_room_7,whdays_room_8,whdays_room_9,whdays_room_10,")
		.append("home_wh_room_num1,home_wh_room_num2,home_wh_room_num3,home_wh_room_num4 ")
		.append(") values( ")
		.append("null,NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)").toString();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, hospital.getName());
				ps.setInt(2, pediatricsData.getPnum());
				ps.setInt(3, pediatricsData.getWhnum());
				ps.setInt(4, pediatricsData.getLsnum());
				ps.setString(5, hospital.getSaleCode());
				ps.setString(6, operator.getName());
				ps.setString(7, hospital.getRegion());
				ps.setString(8, hospital.getRsmRegion());
				ps.setDouble(9, pediatricsData.getHqd());
				ps.setDouble(10, pediatricsData.getHbid());
				ps.setDouble(11, pediatricsData.getOqd());
				ps.setDouble(12, pediatricsData.getObid());
				ps.setDouble(13, pediatricsData.getTqd());
				ps.setDouble(14, pediatricsData.getTbid());
				ps.setString(15, pediatricsData.getRecipeType());
				ps.setString(16, (operator.getSuperior()==null||"".equalsIgnoreCase(operator.getSuperior()))?operator.getUserCode():operator.getSuperior());
				ps.setInt(17, hospital.getPortNum());
				ps.setInt(18, pediatricsData.getWhbwnum());
				ps.setDouble(19, pediatricsData.getWhdaysRoom1Rate());
				ps.setDouble(20, pediatricsData.getWhdaysRoom2Rate());
				ps.setDouble(21, pediatricsData.getWhdaysRoom3Rate());
				ps.setDouble(22, pediatricsData.getWhdaysRoom4Rate());
				ps.setDouble(23, pediatricsData.getWhdaysRoom5Rate());
				ps.setDouble(24, pediatricsData.getWhdaysRoom6Rate());
				ps.setDouble(25, pediatricsData.getWhdaysRoom7Rate());
				ps.setDouble(26, pediatricsData.getWhdaysRoom8Rate());
				ps.setDouble(27, pediatricsData.getWhdaysRoom9Rate());
				ps.setDouble(28, pediatricsData.getWhdaysRoom10Rate());
				ps.setInt(29, pediatricsData.getHomeWhRoomNum1());
				ps.setInt(30, pediatricsData.getHomeWhRoomNum2());
				ps.setInt(31, pediatricsData.getHomeWhRoomNum3());
				ps.setInt(32, pediatricsData.getHomeWhRoomNum4());
				return ps;
			}
		}, keyHolder);
		logger.info("returned id is "+keyHolder.getKey().intValue());
	}
	
	@Override
	public void insert(final PediatricsData pediatricsData, final String dsmCode) throws Exception {
	    logger.info("insert data - daily upload");
	    
	    final String sql = new StringBuilder("insert into tbl_pediatrics_data(")
		.append("id,createdate,hospitalName,pnum,whnum,lsnum,etmsCode,operatorName,region,rsmRegion,")
		.append("hqd,hbid,oqd,obid,tqd,tbid,recipeType,updatedate,dsmCode,portNum,whbwnum,")
		.append("whdays_emerging_1,whdays_emerging_2,whdays_emerging_3,whdays_emerging_4,whdays_emerging_5,whdays_emerging_6,whdays_emerging_7,")
		.append("home_wh_emerging_num1,home_wh_emerging_num2,home_wh_emerging_num3,home_wh_emerging_num4 ")
		.append(") values( ")
		.append("null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?)").toString();
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
	        @Override
	        public PreparedStatement createPreparedStatement(
	                Connection connection) throws SQLException {
	            PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	            ps.setTimestamp(1, new Timestamp(pediatricsData.getCreatedate().getTime()));
	            ps.setString(2, pediatricsData.getHospitalName());
	            ps.setInt(3, pediatricsData.getPnum());
				ps.setInt(4, pediatricsData.getWhnum());
				ps.setInt(5, pediatricsData.getLsnum());
				ps.setString(6, pediatricsData.getSalesETMSCode());
				ps.setString(7, pediatricsData.getSalesName());
				ps.setString(8, pediatricsData.getRegion());
				ps.setString(9, pediatricsData.getRsmRegion());
				ps.setDouble(10, pediatricsData.getHqd());
				ps.setDouble(11, pediatricsData.getHbid());
				ps.setDouble(12, pediatricsData.getOqd());
				ps.setDouble(13, pediatricsData.getObid());
				ps.setDouble(14, pediatricsData.getTqd());
				ps.setDouble(15, pediatricsData.getTbid());
				ps.setString(16, pediatricsData.getRecipeType());
				ps.setString(17, dsmCode);
				ps.setInt(18, pediatricsData.getPortNum());
				ps.setInt(19, pediatricsData.getWhbwnum());
				ps.setDouble(20, pediatricsData.getWhdaysEmerging1Rate());
				ps.setDouble(21, pediatricsData.getWhdaysEmerging2Rate());
				ps.setDouble(22, pediatricsData.getWhdaysEmerging3Rate());
				ps.setDouble(23, pediatricsData.getWhdaysEmerging4Rate());
				ps.setDouble(24, pediatricsData.getWhdaysEmerging5Rate());
				ps.setDouble(25, pediatricsData.getWhdaysEmerging6Rate());
				ps.setDouble(26, pediatricsData.getWhdaysEmerging7Rate());
				ps.setInt(27, pediatricsData.getHomeWhEmergingNum1());
				ps.setInt(28, pediatricsData.getHomeWhEmergingNum2());
				ps.setInt(29, pediatricsData.getHomeWhEmergingNum3());
				ps.setInt(30, pediatricsData.getHomeWhEmergingNum4());
	            return ps;
	        }
	    }, keyHolder);
	    logger.info("returned id is "+keyHolder.getKey().intValue());
	}

	@Override
	public void update(PediatricsData pediatricsData, UserInfo operator) throws Exception {
		StringBuffer sql = new StringBuffer("update tbl_pediatrics_data set ");
	    sql.append("updatedate=NOW()");
	    sql.append(", pnum=? ");
	    sql.append(", whnum=? ");
	    sql.append(", lsnum=? ");
	    sql.append(", hqd=? ");
	    sql.append(", hbid=? ");
	    sql.append(", oqd=? ");
	    sql.append(", obid=? ");
	    sql.append(", tqd=? ");
	    sql.append(", tbid=? ");
	    sql.append(", recipeType=? ");
	    sql.append(", whbwnum=? ");
	    sql.append(", whdays_emerging_1=? ");
	    sql.append(", whdays_emerging_2=? ");
	    sql.append(", whdays_emerging_3=? ");
	    sql.append(", whdays_emerging_4=? ");
	    sql.append(", whdays_emerging_5=? ");
	    sql.append(", whdays_emerging_6=? ");
	    sql.append(", whdays_emerging_7=? ");
	    sql.append(", home_wh_emerging_num1=? ");
	    sql.append(", home_wh_emerging_num2=? ");
	    sql.append(", home_wh_emerging_num3=? ");
	    sql.append(", home_wh_emerging_num4=? ");
	    
	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(pediatricsData.getPnum());
	    paramList.add(pediatricsData.getWhnum());
	    paramList.add(pediatricsData.getLsnum());
	    paramList.add(pediatricsData.getHqd());
	    paramList.add(pediatricsData.getHbid());
	    paramList.add(pediatricsData.getOqd());
	    paramList.add(pediatricsData.getObid());
	    paramList.add(pediatricsData.getTqd());
	    paramList.add(pediatricsData.getTbid());
	    paramList.add(pediatricsData.getRecipeType());
	    paramList.add(pediatricsData.getWhbwnum());
	    paramList.add(pediatricsData.getWhdaysEmerging1Rate());
	    paramList.add(pediatricsData.getWhdaysEmerging2Rate());
	    paramList.add(pediatricsData.getWhdaysEmerging3Rate());
	    paramList.add(pediatricsData.getWhdaysEmerging4Rate());
	    paramList.add(pediatricsData.getWhdaysEmerging5Rate());
	    paramList.add(pediatricsData.getWhdaysEmerging6Rate());
	    paramList.add(pediatricsData.getWhdaysEmerging7Rate());
	    paramList.add(pediatricsData.getHomeWhEmergingNum1());
	    paramList.add(pediatricsData.getHomeWhEmergingNum2());
	    paramList.add(pediatricsData.getHomeWhEmergingNum3());
	    paramList.add(pediatricsData.getHomeWhEmergingNum4());

	    if( null == operator ){
	    	logger.info("using web to update the data, no need to update the sales info");
	    	paramList.add(pediatricsData.getDataId());
	    }else{
//	    	sql.append(", etmsCode=? ");
	    	sql.append(", operatorName=? ");
	    	
//	    	paramList.add(operator.getUserCode());
	    	paramList.add(operator.getName());
	    	paramList.add(pediatricsData.getDataId());
	    }
	    
	    sql.append(" where id=? ");
		dataBean.getJdbcTemplate().update(sql.toString(), paramList.toArray());
	}
	
	@Override
	public void updateRoomData(PediatricsData pediatricsData, UserInfo operator) throws Exception {
		StringBuffer sql = new StringBuffer("update tbl_pediatrics_data set ");
		sql.append("updatedate=NOW()");
		sql.append(", pnum_room=? ");
		sql.append(", whnum_room=? ");
		sql.append(", lsnum_room=? ");
		sql.append(", hqd_room=? ");
		sql.append(", hbid_room=? ");
		sql.append(", oqd_room=? ");
		sql.append(", obid_room=? ");
		sql.append(", tqd_room=? ");
		sql.append(", tbid_room=? ");
		sql.append(", recipeType=? ");
		sql.append(", whbwnum_room=? ");
		sql.append(", whdays_room_1=? ");
		sql.append(", whdays_room_2=? ");
		sql.append(", whdays_room_3=? ");
		sql.append(", whdays_room_4=? ");
		sql.append(", whdays_room_5=? ");
		sql.append(", whdays_room_6=? ");
		sql.append(", whdays_room_7=? ");
		sql.append(", whdays_room_8=? ");
		sql.append(", whdays_room_9=? ");
		sql.append(", whdays_room_10=? ");
		sql.append(", home_wh_room_num1=? ");
		sql.append(", home_wh_room_num2=? ");
		sql.append(", home_wh_room_num3=? ");
		sql.append(", home_wh_room_num4=? ");
		
		List<Object> paramList = new ArrayList<Object>();
		paramList.add(pediatricsData.getPnum());
		paramList.add(pediatricsData.getWhnum());
		paramList.add(pediatricsData.getLsnum());
		paramList.add(pediatricsData.getHqd());
		paramList.add(pediatricsData.getHbid());
		paramList.add(pediatricsData.getOqd());
		paramList.add(pediatricsData.getObid());
		paramList.add(pediatricsData.getTqd());
		paramList.add(pediatricsData.getTbid());
		paramList.add(pediatricsData.getRecipeType());
		paramList.add(pediatricsData.getWhbwnum());
		paramList.add(pediatricsData.getWhdaysRoom1Rate());
		paramList.add(pediatricsData.getWhdaysRoom2Rate());
		paramList.add(pediatricsData.getWhdaysRoom3Rate());
		paramList.add(pediatricsData.getWhdaysRoom4Rate());
		paramList.add(pediatricsData.getWhdaysRoom5Rate());
		paramList.add(pediatricsData.getWhdaysRoom6Rate());
		paramList.add(pediatricsData.getWhdaysRoom7Rate());
		paramList.add(pediatricsData.getWhdaysRoom8Rate());
		paramList.add(pediatricsData.getWhdaysRoom9Rate());
		paramList.add(pediatricsData.getWhdaysRoom10Rate());
		paramList.add(pediatricsData.getHomeWhRoomNum1());
		paramList.add(pediatricsData.getHomeWhRoomNum2());
		paramList.add(pediatricsData.getHomeWhRoomNum3());
		paramList.add(pediatricsData.getHomeWhRoomNum4());
		
		if( null == operator ){
			logger.info("using web to update the data, no need to update the sales info");
			paramList.add(pediatricsData.getDataId());
		}else{
//	    	sql.append(", etmsCode=? ");
			sql.append(", operatorName=? ");
			
//	    	paramList.add(operator.getUserCode());
			paramList.add(operator.getName());
			paramList.add(pediatricsData.getDataId());
		}
		
		sql.append(" where id=? ");
		dataBean.getJdbcTemplate().update(sql.toString(), paramList.toArray());
	}
	
	@Override
	public List<MonthlyStatisticsData> getMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
		switch(level){
			case LsAttributes.USER_LEVEL_RSD:
				inRateSQL.append(" select h.region, '' as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(", IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate ")
	            .append(", 0 as aenum ")
	            .append(", 0 as risknum ")
				.append(LsAttributes.SQL_MONTHLY_STATISTICS_SELECTION)
	            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSD_CONDITION)
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSD_GROUP);
				break;
			case LsAttributes.USER_LEVEL_RSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(", IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate ")
	            .append(", 0 as aenum ")
	            .append(", 0 as risknum ")
				.append(LsAttributes.SQL_MONTHLY_STATISTICS_SELECTION)
	            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSM_CONDITION)
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSM_GROUP);
				break;
			case LsAttributes.USER_LEVEL_DSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, h.dsmCode as dsmCode ")
	            .append(", (select distinct name from tbl_userinfo u where u.userCode = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region ) as dsmName")
				.append(", IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate ")
				.append(", 0 as aenum ")
	            .append(", 0 as risknum ")
				.append(LsAttributes.SQL_MONTHLY_STATISTICS_SELECTION)
	            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
		        .append(LsAttributes.SQL_MONTHLY_STATISTICS_DSM_CONDITION)
		        .append(LsAttributes.SQL_MONTHLY_STATISTICS_DSM_GROUP);
				break;
		}
        return dataBean.getJdbcTemplate().query(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion},new MonthlyStatisticsDataRowMapper());
	}

	@Override
	public List<MonthlyStatisticsData> getCoreMonthlyStatisticsData(
			String beginDuraion, String endDuraion, String level)
			throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
		switch(level){
			case LsAttributes.USER_LEVEL_RSD:
				inRateSQL.append(" select h.region, '' as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION)
	            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_RSD_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_RSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION)
	            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_RSM_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_DSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, h.dsmCode as dsmCode ")
	            .append(", (select distinct name from tbl_userinfo u where u.userCode = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region ) as dsmName")
				.append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION)
	            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
		        .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_DSM_CONDITION);
				break;
		}
        return dataBean.getJdbcTemplate().query(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion},new MonthlyStatisticsCoreDataRowMapper());
	}

	@Override
	public List<MonthlyStatisticsData> getEmergingMonthlyStatisticsData(
			String beginDuraion, String endDuraion, String level)
			throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
		switch(level){
			case LsAttributes.USER_LEVEL_RSD:
				inRateSQL.append(" select h.region, '' as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION)
	            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_EMERGING_RSD_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_RSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION)
	            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_EMERGING_RSM_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_DSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, h.dsmCode as dsmCode ")
	            .append(", (select distinct name from tbl_userinfo u where u.userCode = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region ) as dsmName")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION)
	            .append(" from tbl_pediatrics_data_weekly, tbl_hospital h ")
		        .append(LsAttributes.SQL_MONTHLY_STATISTICS_EMERGING_DSM_CONDITION);
				break;
		}
        return dataBean.getJdbcTemplate().query(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion},new MonthlyStatisticsEmergingDataRowMapper());
	}

	@Override
	public MonthlyStatisticsData getMonthlyStatisticsCountryData(
			String beginDuraion, String endDuraion) throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
		inRateSQL.append(" select '' as region, '' as rsmRegion, '' as dsmCode, '' as dsmName ")
        .append(", IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate ")
        .append(", 0 as aenum ")
        .append(", 0 as risknum ")
		.append(LsAttributes.SQL_MONTHLY_STATISTICS_SELECTION)
        .append(" from tbl_pediatrics_data_weekly ")
        .append(" where duration between ? and ? ");
        return dataBean.getJdbcTemplate().queryForObject(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion},new MonthlyStatisticsDataRowMapper());
	}

	@Override
	public MonthlyStatisticsData getCoreMonthlyStatisticsCountryData(
			String beginDuraion, String endDuraion) throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
		inRateSQL.append(" select '' as region, '' as rsmRegion, '' as dsmCode, '' as dsmName ")
        .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION)
        .append(" from tbl_pediatrics_data_weekly, tbl_hospital h  ")
        .append(" where duration between ? and ? and hospitalCode = h.code and h.dragonType='Core' ");
        return dataBean.getJdbcTemplate().queryForObject(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion},new MonthlyStatisticsCoreDataRowMapper());
	}
	
	@Override
	public MonthlyStatisticsData getEmergingMonthlyStatisticsCountryData(
			String beginDuraion, String endDuraion) throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
		inRateSQL.append(" select '' as region, '' as rsmRegion, '' as dsmCode, '' as dsmName ")
        .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION)
        .append(" from tbl_pediatrics_data_weekly, tbl_hospital h  ")
        .append(" where duration between ? and ? and hospitalCode = h.code and h.dragonType='Emerging' ");
        return dataBean.getJdbcTemplate().queryForObject(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion},new MonthlyStatisticsEmergingDataRowMapper());
	}
	
	public DataBean getDataBean() {
        return dataBean;
    }
    
    public void setDataBean(DataBean dataBean) {
        this.dataBean = dataBean;
    }
}
