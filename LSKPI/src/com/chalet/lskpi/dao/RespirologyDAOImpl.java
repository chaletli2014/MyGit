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
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.MonthlyStatisticsCoreDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyStatisticsDataRowMapper;
import com.chalet.lskpi.mapper.MonthlyStatisticsEmergingDataRowMapper;
import com.chalet.lskpi.mapper.ResLastWeekDataRowMapper;
import com.chalet.lskpi.mapper.RespirologyMobileRowMapper;
import com.chalet.lskpi.mapper.RespirologyMonthDataRowMapper;
import com.chalet.lskpi.mapper.RespirologyRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRSMDataRowMapper;
import com.chalet.lskpi.mapper.WeeklyHospitalDataRowMapper;
import com.chalet.lskpi.model.DailyReportData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobileRESDailyData;
import com.chalet.lskpi.model.MonthlyStatisticsData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.RespirologyData;
import com.chalet.lskpi.model.RespirologyLastWeekData;
import com.chalet.lskpi.model.RespirologyMonthDBData;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserCode;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyDataOfHospital;
import com.chalet.lskpi.model.WeeklyRatioData;
import com.chalet.lskpi.utils.DailyReportDataRowMapper;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.RESWeeklyRatioDataRowMapper;
import com.chalet.lskpi.utils.ReportProcessDataRowMapper;
import com.chalet.lskpi.utils.ReportProcessDetailDataRowMapper;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:54:20
 * 类说明
 */

@Repository("respirologyDAO")
public class RespirologyDAOImpl implements RespirologyDAO {

	private Logger logger = Logger.getLogger(RespirologyDAOImpl.class);
	
	@Autowired
	@Qualifier("dataBean")
	private DataBean dataBean;
	
	@Override
	public void updateRESUserCodes(final List<UserCode> userCodes) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("update tbl_respirology_data set etmsCode=? where etmsCode=?");
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
		sb.append("update tbl_respirology_data set dsmCode=? where dsmCode=?");
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

    public WeeklyRatioData getHospitalWeeklyRESData4Mobile(String hospitalCode) throws Exception {
        StringBuffer mobileRESWeeklySQL = new StringBuffer();
        mobileRESWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
        .append(" , '' as userCode ")
        .append(" , lastweekdata.hospitalName as name ")
        .append(" from ( ")
        .append("   select hospitalCode, hospitalName, ")
        .append(LsAttributes.SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES)
        .append("   where hospitalCode=? ")
        .append(") lastweekdata, ")
        .append("( ")
        .append("   select hospitalCode, hospitalName, ")
        .append(LsAttributes.SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES)
        .append("   where hospitalCode=? ")
        .append(") last2weekdata ");
        return dataBean.getJdbcTemplate().queryForObject(mobileRESWeeklySQL.toString(),new Object[]{hospitalCode,hospitalCode},new RESWeeklyRatioDataRowMapper());
    }
	
	@Override
	public WeeklyRatioData getLowerWeeklyRESData4REPMobile(UserInfo currentUser,String lowerUserCode)
			throws Exception {
		StringBuffer mobileRESWeeklySQL = new StringBuffer();
		mobileRESWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
    	.append(" , lastweekdata.saleCode as userCode ")
    	.append(" , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.saleCode and u.superior = lastweekdata.dsmCode and u.level='REP'),'vacant') as name ")
    	.append(" from ( ")
    	.append("   select h.dsmCode, h.saleCode, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES)
    	.append("	group by h.dsmCode, h.saleCode ")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.dsmCode, h.saleCode, ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES)
	    .append("	group by h.dsmCode, h.saleCode ")
	    .append(") last2weekdata ")
        .append("where lastweekdata.dsmCode = last2weekdata.dsmCode ")
        .append("and lastweekdata.saleCode = last2weekdata.saleCode ")
        .append("and lastweekdata.saleCode = ?")
        .append("and lastweekdata.dsmCode = ?");
        return dataBean.getJdbcTemplate().queryForObject(mobileRESWeeklySQL.toString(),new Object[]{lowerUserCode,currentUser.getUserCode()},new RESWeeklyRatioDataRowMapper());
	}

	@Override
	public WeeklyRatioData getLowerWeeklyRESData4DSMMobile(UserInfo currentUser,String lowerUserCode)
			throws Exception {
		StringBuffer mobileRESWeeklySQL = new StringBuffer();
		mobileRESWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
    	.append(" , lastweekdata.dsmCode as userCode ")
    	.append(" , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.dsmCode and u.region = lastweekdata.rsmRegion and u.level='DSM' ),'vacant') as name ")
    	.append(" from ( ")
    	.append("   select h.dsmCode, h.rsmRegion, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES)
    	.append("	group by h.rsmRegion, h.dsmCode ")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.dsmCode, h.rsmRegion, ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES)
	    .append("	group by h.rsmRegion, h.dsmCode ")
	    .append(") last2weekdata ")
        .append("where lastweekdata.dsmCode = last2weekdata.dsmCode ")
        .append("and lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.dsmCode = ?")
        .append("and lastweekdata.rsmRegion = ?");
        return dataBean.getJdbcTemplate().queryForObject(mobileRESWeeklySQL.toString(),new Object[]{lowerUserCode,currentUser.getRegion()},new RESWeeklyRatioDataRowMapper());
	}

	@Override
	public WeeklyRatioData getLowerWeeklyRESData4RSMMobile(UserInfo currentUser,String lowerUserCode)
			throws Exception {
		StringBuffer mobileRESWeeklySQL = new StringBuffer();
		mobileRESWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
    	.append(" , lastweekdata.rsmRegion as userCode ")
    	.append(" , IFNULL((select u.name from tbl_userinfo u where u.level='RSM' and u.region = lastweekdata.rsmRegion ),'vacant') as name ")
    	.append(" from ( ")
    	.append("   select h.region, h.rsmRegion, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES)
    	.append("	group by h.region, h.rsmRegion ")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.region, h.rsmRegion, ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES)
	    .append("	group by h.region, h.rsmRegion ")
	    .append(") last2weekdata ")
        .append("where lastweekdata.region = last2weekdata.region ")
        .append("and lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.rsmRegion = (select region from tbl_userinfo where userCode=?)");
        return dataBean.getJdbcTemplate().queryForObject(mobileRESWeeklySQL.toString(),new Object[]{lowerUserCode},new RESWeeklyRatioDataRowMapper());
	}

    public List<WeeklyRatioData> getWeeklyRESData4DSMMobile(String telephone) throws Exception {
    	StringBuffer mobileRESWeeklySQL = new StringBuffer();
        mobileRESWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
    	.append(" , lastweekdata.dsmCode as userCode , lastweekdata.rsmRegion ")
    	.append(" , IFNULL((select u.name from tbl_userinfo u where u.userCode = lastweekdata.dsmCode and u.region = lastweekdata.rsmRegion and u.level='DSM' ),'vacant') as name ")
    	.append(" from ( ")
    	.append("   select h.dsmCode, h.rsmRegion, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES)
    	.append("	group by h.rsmRegion, h.dsmCode ")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.dsmCode, h.rsmRegion, ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES)
	    .append("	group by h.rsmRegion, h.dsmCode ")
	    .append(") last2weekdata ")
        .append("where lastweekdata.dsmCode = last2weekdata.dsmCode ")
        .append("and lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.rsmRegion = (select region from tbl_userinfo where telephone=?)");
        return dataBean.getJdbcTemplate().query(mobileRESWeeklySQL.toString(),new Object[]{telephone},new RESWeeklyRatioDataRowMapper());
    }

    public List<WeeklyRatioData> getWeeklyRESData4RSMMobile(String telephone) throws Exception {
    	StringBuffer mobileRESWeeklySQL = new StringBuffer();
    	mobileRESWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
    	.append(" , lastweekdata.rsmRegion as userCode, lastweekdata.region ")
    	.append(" , IFNULL((select u.name from tbl_userinfo u where u.level='RSM' and u.region = lastweekdata.rsmRegion ),'vacant') as name ")
    	.append(" from ( ")
    	.append("   select h.rsmRegion, h.region, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES)
    	.append("	group by h.rsmRegion")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.rsmRegion, h.region, ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES)
	    .append("	group by h.rsmRegion")
	    .append(") last2weekdata ")
        .append("where lastweekdata.rsmRegion = last2weekdata.rsmRegion ")
        .append("and lastweekdata.region = (select regionCenter from tbl_userinfo where telephone=?)");
       return dataBean.getJdbcTemplate().query(mobileRESWeeklySQL.toString(),new Object[]{telephone},new RESWeeklyRatioDataRowMapper());
    }

    @Cacheable(value="getWeeklyRESData4RSDMobile")
    public List<WeeklyRatioData> getWeeklyRESData4RSDMobile() throws Exception {
    	StringBuffer mobileRESWeeklySQL = new StringBuffer();
    	mobileRESWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
    	.append(" , lastweekdata.region as userCode")
    	.append(" , (select distinct property_value from tbl_property where property_name=lastweekdata.region ) as name")
    	.append(" from ( ")
    	.append("   select h.region, ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES)
    	.append("	group by h.region")
	    .append(") lastweekdata, ")
	    .append("( ")
	    .append(" 	select h.region , ")
	    .append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES)
	    .append("	group by h.region")
	    .append(") last2weekdata ")
        .append("where lastweekdata.region = last2weekdata.region  order by lastweekdata.region");
       return dataBean.getJdbcTemplate().query(mobileRESWeeklySQL.toString(),new RESWeeklyRatioDataRowMapper());
    }
    
    public WeeklyRatioData getHospitalWeeklyRESData4Mobile() throws Exception {
    	StringBuffer mobileRESWeeklySQL = new StringBuffer();
    	mobileRESWeeklySQL.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES)
    	.append(" , '' as userCode")
    	.append(" , '' as name ")
    	.append(" from ( ")
    	.append("   select ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES)
    	.append(") lastweekdata, ")
    	.append("( ")
    	.append(" 	select ")
    	.append(LsAttributes.SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES)
    	.append(") last2weekdata ");
    	return dataBean.getJdbcTemplate().queryForObject(mobileRESWeeklySQL.toString(),new RESWeeklyRatioDataRowMapper());
    }
	
	public List<ReportProcessDataDetail> getSalesSelfReportProcessRESDetailData(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer("");
	    sb.append("select h.name as hospitalName, ")
	    .append("( select IFNULL( ")
	    .append("       ( select count(1) ")
	    .append("       from tbl_respirology_data rd ")
	    .append("       where rd.hospitalName = h.name ")
	    .append("       and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
	    .append("       group by rd.hospitalName ")
	    .append("   ),0) ) as inNum, ")
	    .append("( select ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion and ui.level='REP' ) as salesName, ")
	    .append(" h.isResAssessed as isAssessed ")
	    .append("from tbl_userinfo u, tbl_hospital h, tbl_hos_user hu ")
	    .append("where hu.userCode = u.userCode ")
	    .append("and hu.hosCode = h.code ")
	    .append("and h.dsmCode = u.superior ")
	    .append("and h.rsmRegion = u.region ")
	    .append("and telephone = ? ");
	    
	    Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
	    return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDetailDataRowMapper());
	}
	
    public List<ReportProcessDataDetail> getDSMSelfReportProcessRESDetailData(String telephone) throws Exception {
        StringBuffer sb = new StringBuffer("");
        sb.append("select h.name as hospitalName, ")
        .append("( select IFNULL( ")
        .append("       ( select count(1) ")
        .append("       from tbl_respirology_data rd ")
        .append("       where rd.hospitalName = h.name ")
        .append("       and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       group by rd.hospitalName ")
        .append("   ),0) ) as inNum, ")
        .append("( select ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion and ui.level='REP' ) as salesName, ")
        .append(" h.isResAssessed as isAssessed ")
        .append("from tbl_userinfo u, tbl_hospital h ")
        .append("where h.dsmCode = u.userCode ")
        .append("and h.rsmRegion = u.region ")
        .append("and telephone = ? ");
        
        Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDetailDataRowMapper());
    }
	
	@Override
	public ReportProcessData getDSMSelfReportProcessRESData(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer("");
	    sb.append("select count(1) as hosNum, ")
	    .append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
	    .append("		select least(count(1),3) as inNum, h1.dsmCode, h1.rsmRegion ")
	    .append("		from tbl_respirology_data rd, tbl_hospital h1 ")
	    .append("		where rd.hospitalName = h1.name ")
	    .append("		and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
	    .append("		and h1.isResAssessed='1' ")
	    .append("		group by rd.hospitalName ")
	    .append("	) inNumTemp ")
	    .append("	where u.region = inNumTemp.rsmRegion ")
	    .append("	and u.userCode = inNumTemp.dsmCode ")
	    .append(") as validInNum ")
	    .append(",( select IFNULL(sum(inNum),0) from ( ")
        .append("       select ")
        .append(LsAttributes.SQL_REPORT_PROCESS_RES_INNUM4RATE)
        .append("       , h1.dsmCode")
        .append("       , h1.rsmRegion ")
        .append("       from tbl_respirology_data rd, tbl_hospital h1 ")
        .append("       where rd.hospitalName = h1.name ")
        .append("       and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       and h1.isResAssessed='1' ")
        .append("       group by rd.hospitalName ")
        .append("   ) inNumTemp ")
        .append("   where u.region = inNumTemp.rsmRegion ")
        .append("   and u.userCode = inNumTemp.dsmCode ")
        .append(") as inNumForRate ")
	    .append("from tbl_userinfo u, tbl_hospital h ")
	    .append("where h.dsmCode = u.userCode ")
	    .append("and h.rsmRegion = u.region ")
	    .append("and h.isResAssessed='1' ")
	    .append("and telephone = ? ");
	    
	    Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
	    return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{startDate,startDate,startDate,startDate,telephone}, new ReportProcessDataRowMapper());
	}
	
	public List<ReportProcessDataDetail> getRSMSelfReportProcessRESDetailData(String telephone) throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("select h.name as hospitalName, ")
		.append("( select IFNULL( ")
		.append("       ( select count(1) ")
		.append("       from tbl_respirology_data rd ")
		.append("       where rd.hospitalName = h.name ")
		.append("       and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
		.append("       group by rd.hospitalName ")
		.append("   ),0) ) as inNum, ")
		.append("( select ui.name from tbl_userinfo ui where ui.userCode = h.saleCode and ui.superior = h.dsmCode and ui.region = h.rsmRegion and ui.level='REP' ) as salesName, ")
		.append(" h.isResAssessed as isAssessed ")
		.append("from tbl_userinfo u, tbl_hospital h ")
		.append("where h.rsmRegion = u.region ")
		.append("and h.isResAssessed = '1' ")
		.append("and telephone = ? ");
		
		Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
		return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDetailDataRowMapper());
	}
	
	@Override
	public ReportProcessData getRSMSelfReportProcessRESData(String telephone) throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("select count(1) as hosNum, ")
		.append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
		.append("		select least(count(1),3) as inNum, h1.dsmCode, h1.rsmRegion ")
		.append("		from tbl_respirology_data rd, tbl_hospital h1 ")
		.append("		where rd.hospitalName = h1.name ")
		.append("		and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
		.append("		and h1.isResAssessed='1' ")
		.append("		group by rd.hospitalName ")
		.append("	) inNumTemp ")
		.append("	where u.region = inNumTemp.rsmRegion ")
		.append(") as validInNum ")
		.append(",( select IFNULL(sum(inNum),0) from ( ")
        .append("       select ")
        .append(LsAttributes.SQL_REPORT_PROCESS_RES_INNUM4RATE)
        .append("       , h1.dsmCode")
        .append("       , h1.rsmRegion ")
        .append("       from tbl_respirology_data rd, tbl_hospital h1 ")
        .append("       where rd.hospitalName = h1.name ")
        .append("       and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       and h1.isResAssessed='1' ")
        .append("       group by rd.hospitalName ")
        .append("   ) inNumTemp ")
        .append("   where u.region = inNumTemp.rsmRegion ")
        .append(") as inNumForRate ")
		.append("from tbl_userinfo u, tbl_hospital h ")
		.append("where h.rsmRegion = u.region ")
		.append("and h.isResAssessed='1' ")
		.append("and telephone = ? ");
		
		Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
		return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{startDate,startDate,startDate,startDate,telephone}, new ReportProcessDataRowMapper());
	}
	
	@Override
	public List<ReportProcessData> getDSMReportProcessRESDataOfRSM(String rsmRegion) throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("select count(1) as hosNum, ")
		.append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
		.append("		select least(count(1),3) as inNum, h1.dsmCode, h1.rsmRegion ")
		.append("		from tbl_respirology_data rd, tbl_hospital h1 ")
		.append("		where rd.hospitalName = h1.name ")
		.append("		and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
		.append("		and h1.isResAssessed='1' ")
		.append("		group by rd.hospitalName ")
		.append("	) inNumTemp ")
		.append("	where u.region = inNumTemp.rsmRegion ")
		.append(") as validInNum ")
		.append(",( select IFNULL(sum(inNum),0) from ( ")
        .append("       select ")
        .append(LsAttributes.SQL_REPORT_PROCESS_RES_INNUM4RATE)
        .append("       , h1.dsmCode")
        .append("       , h1.rsmRegion ")
        .append("       from tbl_respirology_data rd, tbl_hospital h1 ")
        .append("       where rd.hospitalName = h1.name ")
        .append("       and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       and h1.isResAssessed='1' ")
        .append("       group by rd.hospitalName ")
        .append("   ) inNumTemp ")
        .append("   where u.region = inNumTemp.rsmRegion ")
        .append(") as inNumForRate ")
		.append("from tbl_userinfo u, tbl_hospital h ")
		.append("where h.rsmRegion = u.region ")
		.append("and h.rsmRegion=? ")
		.append("and h.isResAssessed='1' ");
		
		Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
		return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,startDate,startDate,rsmRegion}, new ReportProcessDataRowMapper());
	}
	
	@Override
	public ReportProcessData getSalesSelfReportProcessRESData(String telephone) throws Exception {
	    StringBuffer sb = new StringBuffer("");
	    sb.append("select count(1) as hosNum, ")
	    .append("( select IFNULL(sum(inNum),0) from ( ")
	    .append("		select least(count(1),3) as inNum, h1.code as hosCode, h1.dsmCode, h1.rsmRegion ")
	    .append("		from tbl_respirology_data rd, tbl_hospital h1 ")
	    .append("		where rd.hospitalName = h1.name ")
	    .append("		and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
	    .append("		and h1.isResAssessed='1' ")
	    .append("		group by rd.hospitalName ")
	    .append("	) inNumTemp, tbl_hos_user hu ")
	    .append("	where u.region = inNumTemp.rsmRegion ")
	    .append("	and hu.hosCode = inNumTemp.hosCode ")
	    .append("	and hu.userCode = u.userCode ")
	    .append("	and u.superior = inNumTemp.dsmCode ")
	    .append(") as validInNum ")
	    .append(",( select IFNULL(sum(inNum),0) from ( ")
        .append("       select ")
        .append(LsAttributes.SQL_REPORT_PROCESS_RES_INNUM4RATE)
        .append("       , h1.code as hosCode")
        .append("       , h1.dsmCode")
        .append("       , h1.rsmRegion ")
        .append("       from tbl_respirology_data rd, tbl_hospital h1 ")
        .append("       where rd.hospitalName = h1.name ")
        .append("       and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
        .append("       and h1.isResAssessed='1' ")
        .append("       group by rd.hospitalName ")
        .append("   ) inNumTemp, tbl_hos_user hu ")
        .append("   where u.region = inNumTemp.rsmRegion ")
        .append("   and hu.hosCode = inNumTemp.hosCode ")
        .append("   and hu.userCode = u.userCode ")
        .append("   and u.superior = inNumTemp.dsmCode ")
        .append(") as inNumForRate ")
	    .append(" from tbl_userinfo u, tbl_hospital h, tbl_hos_user hu ")
	    .append(" where h.dsmCode = u.superior ")
	    .append(" and h.rsmRegion = u.region ")
	    .append(" and h.code = hu.hosCode ")
	    .append(" and hu.userCode = u.userCode ")
	    .append(" and h.isResAssessed='1' ")
	    .append(" and telephone = ? ");
	    
	    Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
	    return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{startDate,startDate,startDate,startDate,telephone}, new ReportProcessDataRowMapper());
	}

	@Override
	public List<ReportProcessData> getReportProcessRESDataByUserTel(String telephone)
			throws Exception {
		StringBuffer sb = new StringBuffer("");
		sb.append("select ")
			.append("u.name, u.telephone, ")
			.append("( 	select count(1) from tbl_hospital h ")
			.append(" 	where h.saleCode = u.userCode ")
			.append("   and h.dsmCode = u.superior ")
			.append("   and h.rsmRegion = u.region ")
			.append("   and h.isResAssessed='1' ")
			.append(") hosNum, ")
			.append("( select sum(inNum) from ( ")
			.append("		select least(count(1),3) as inNum, h1.saleCode, h1.dsmCode, h1.rsmRegion ")
			.append("		from tbl_respirology_data rd, tbl_hospital h1 ")
			.append("		where rd.hospitalName = h1.name ")
			.append("		and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
			.append("		and h1.isResAssessed='1' ")
			.append("		group by rd.hospitalName ")
			.append("	) inNumTemp")
			.append("	where u.region = inNumTemp.rsmRegion ")
			.append("	and inNumTemp.saleCode = u.userCode ")
			.append("	and inNumTemp.dsmCode = u.superior ")
			.append(") validInNum ")
			.append(",( select IFNULL(sum(inNum),0) from ( ")
            .append("       select ")
            .append(LsAttributes.SQL_REPORT_PROCESS_RES_INNUM4RATE)
            .append("       , h1.saleCode")
            .append("       , h1.dsmCode")
            .append("       , h1.rsmRegion ")
            .append("       from tbl_respirology_data rd, tbl_hospital h1 ")
            .append("       where rd.hospitalName = h1.name ")
            .append("       and rd.createdate between ? and DATE_ADD(?, Interval 7 day) ")
            .append("       and h1.isResAssessed='1' ")
            .append("       group by rd.hospitalName ")
            .append("   ) inNumTemp ")
            .append("   where u.region = inNumTemp.rsmRegion ")
            .append("   and u.userCode = inNumTemp.saleCode ")
            .append("   and u.superior = inNumTemp.dsmCode ")
            .append(") as inNumForRate ")
			.append("from tbl_userinfo u ")
			.append("where u.level='REP' ")
			.append("and u.superior = ( select superior from tbl_userinfo where telephone = ? )");
		
		Timestamp startDate = new Timestamp(DateUtils.getTheBeginDateOfCurrentWeek().getTime());
		return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{startDate,startDate,startDate,startDate,telephone}, new ReportProcessDataRowMapper());
	}
	
	public int getLastWeeklyRESData() throws Exception {
        Timestamp lastThursDay = new Timestamp(DateUtils.getGenerateWeeklyReportDate().getTime());
        StringBuffer sb = new StringBuffer();
        sb.append(" select count(1) from tbl_respirology_data_weekly where duration = CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d'))");
        return dataBean.getJdbcTemplate().queryForInt(sb.toString(), lastThursDay,lastThursDay);
    }
	
    @Override
    public void generateWeeklyRESDataOfHospital() throws Exception {
        Date lastweekDay = DateUtils.getGenerateWeeklyReportDate();
        this.generateWeeklyRESDataOfHospital(lastweekDay);
    }
    
    @Override
    public int removeOldWeeklyRESData(String duration) throws Exception{
        String sql = "delete from tbl_respirology_data_weekly where duration=?";
        return dataBean.getJdbcTemplate().update(sql, new Object[] { duration });
    }
	
	@Override
	public void generateWeeklyRESDataOfHospital(Date refreshDate) throws Exception {
	    Timestamp lastweekDay = new Timestamp(refreshDate.getTime());
        StringBuffer sb = new StringBuffer();
        
        sb.append("insert into tbl_respirology_data_weekly(id,duration,hospitalName,hospitalCode,innum,pnum,aenum,whnum,lsnum,averageDose,omgRate,tmgRate,thmgRate,fmgRate,smgRate,emgRate,saleCode,dsmCode,rsmRegion,region,updatedate,date_YYYY,date_MM) ")
            .append("select ")
            .append("null,")
            .append(" CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d')) as duration, ")
            .append("h.name, ")
            .append("h.code, ")
            .append("rd_data.innum, ")
            .append("rd_data.pnum, ")
            .append("rd_data.aenum, ")
            .append("rd_data.whnum, ")
            .append("rd_data.lsnum, ")
            .append("rd_data.averageDose, ")
            .append("rd_data.omgRate, ")
            .append("rd_data.tmgRate, ")
            .append("rd_data.thmgRate, ")
            .append("rd_data.fmgRate, ")
            .append("rd_data.smgRate, ")
            .append("rd_data.emgRate, ")
            .append("h.saleCode, ")
            .append("h.dsmCode, ")
            .append("h.rsmRegion, ")
            .append("h.region, ")
            .append("now(),  ")
            .append("Year(DATE_SUB(?, Interval 6 day)),  ")
            .append("Month(DATE_SUB(?, Interval 6 day))  ")
            .append("from ( ")
            .append("   SELECT ")
            .append("   h.code, ")
            .append("   count_hos.inNum, ")
            .append("   (sum(rd.pnum)/count_hos.inNum)*5 as pnum, ")
            .append("   (sum(rd.aenum)/count_hos.inNum)*5 as aenum, ")
            .append("   (sum(rd.whnum)/count_hos.inNum)*5 as whnum, ")
            .append("   (sum(rd.lsnum)/count_hos.inNum)*5 as lsnum, ")
            .append("   IFNULL( ")
            .append("       sum( ")
            .append("           ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 )* IFNULL(rd.lsnum,0) ")
            .append("       ) / IFNULL(sum(rd.lsnum),0) ,0 ) averageDose, ")
            .append("   IFNULL( sum(IFNULL(rd.oqd,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) omgRate, ")
            .append("   IFNULL( sum(IFNULL(rd.tqd,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) tmgRate, ")
            .append("   IFNULL( sum(IFNULL(rd.otid,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) thmgRate, ")
            .append("   IFNULL( sum(IFNULL(rd.tbid,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) fmgRate, ")
            .append("   IFNULL( sum((IFNULL(rd.ttid,0)*rd.lsnum + IFNULL(rd.thbid,0)*rd.lsnum)/100)/sum(rd.lsnum),0 ) smgRate, ")
            .append("   IFNULL( sum(IFNULL(rd.fbid,0)*rd.lsnum/100)/sum(rd.lsnum),0 ) emgRate ")
            .append("   FROM tbl_respirology_data rd, tbl_hospital h, ")
            .append("   (   ")
            .append("       select count(1) as inNum, h.code ")
            .append("       from tbl_respirology_data rd, tbl_hospital h ")
            .append("       WHERE rd.createdate between DATE_SUB(?, Interval 6 day) and DATE_ADD(?, Interval 1 day) ")
            .append("       and rd.hospitalName = h.name ")
            .append("       and h.isResAssessed='1' ")
            .append("       GROUP BY h.code ")
            .append("   ) count_hos ")
            .append("   WHERE rd.createdate between DATE_SUB(?, Interval 6 day) and DATE_ADD(?, Interval 1 day) ")
            .append("   and rd.hospitalName = h.name ")
            .append("   and h.code = count_hos.code")
            .append("   and h.isResAssessed='1' ")
            .append("   GROUP BY h.code ")
            .append(") rd_data ")
            .append("right join tbl_hospital h on rd_data.code = h.code ")
            .append("where h.isResAssessed='1'");
        int result = dataBean.getJdbcTemplate().update(sb.toString(), new Object[]{lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay,lastweekDay});
        logger.info(String.format("finish to generate the res weekly data, the result is %s", result));
	}
	
	@Override
	public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception {
		StringBuffer sb = new StringBuffer();
		Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
	    sb.append("select inRateMinT.inRateMin, ")
	    	.append(" inRateMinT.inRateMinUser, ")
	    	.append(" inRateMaxT.inRateMax, ")
	    	.append(" inRateMaxT.inRateMaxUser, ")
	    	.append(" whRateMinT.whRateMin, ")
	    	.append(" whRateMinT.whRateMinUser, ")
	    	.append(" whRateMaxT.whRateMax, ")
	    	.append(" whRateMaxT.whRateMaxUser, ")
	    	.append(" averageDoseMinT.averageDoseMin, ")
	    	.append(" averageDoseMinT.averageDoseMinUser, ")
	    	.append(" averageDoseMaxT.averageDoseMax, ")
	    	.append(" averageDoseMaxT.averageDoseMaxUser ")
	    	.append(" from ") 
	    	.append(" ( select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMin,hosNumTemp.name as inRateMinUser ") 
	    	.append("	from ( ") 
	    	.append("		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name ") 
	    	.append("		from tbl_hospital h, tbl_userinfo u ") 
	    	.append("		where h.rsmRegion = u.region ") 
	    	.append("		and h.isResAssessed='1' ") 
	    	.append("		and u.level='RSM' ") 
	    	.append("		group by u.region ") 
	    	.append("	) hosNumTemp, ") 
	    	.append("		( ") 
	    	.append("		select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from (")
	    	.append("			select IFNULL(count(1),0) as inNum, h.rsmRegion ")
	    	.append("			from tbl_respirology_data rd, tbl_hospital h ")
	    	.append("			where rd.hospitalName = h.name  ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(rd.createdate)")
	    	.append("			and h.isResAssessed='1' ")
	    	.append("			group by h.rsmRegion ")
	    	.append("		) inNum1 right join tbl_userinfo u on u.region = inNum1.rsmRegion ")
	    	.append("		where u.level='RSM' ")
	    	.append("	) inNumTemp")
	    	.append("	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion ")
	    	.append("	order by inNumTemp.inNum/hosNumTemp.hosNum ")
	    	.append("	limit 1	")
	    	.append(") inRateMinT,")
	    	.append("( 	select (inNumTemp.inNum/hosNumTemp.hosNum) as inRateMax,hosNumTemp.name as inRateMaxUser ")
	    	.append("	from ( ")
	    	.append("		select IFNULL(count(1),0) as hosNum, h.rsmRegion, u.name ")
	    	.append("		from tbl_hospital h, tbl_userinfo u ")
	    	.append("		where h.rsmRegion = u.region ")
	    	.append("		and h.isResAssessed='1' ")
	    	.append("		and u.level='RSM' ")
	    	.append("		group by u.region ")
	    	.append("	) hosNumTemp, ")
	    	.append("	( select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from ( ")
	    	.append("			select IFNULL(count(1),0) as inNum, h.rsmRegion ")
	    	.append("			from tbl_respirology_data rd, tbl_hospital h ")
	    	.append("			where rd.hospitalName = h.name ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(rd.createdate)")
	    	.append("			and h.isResAssessed='1' ")
	    	.append("			group by h.rsmRegion ")
	    	.append("		) inNum1 right join tbl_userinfo u on u.region = inNum1.rsmRegion ")
	    	.append("		where u.level='RSM' ")
	    	.append("	) inNumTemp ")
	    	.append("	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion ")
	    	.append("	order by inNumTemp.inNum/hosNumTemp.hosNum desc ")
	    	.append("	limit 1	")
	    	.append(") inRateMaxT, ")
	    	.append("( 	select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMin,pNumTemp.name as whRateMinUser ")
	    	.append("	from ( ")
	    	.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from ( ")
	    	.append("				select IFNULL(sum(rd.pnum),0) as pNum, h.rsmRegion ")
	    	.append("				from tbl_respirology_data rd, tbl_hospital h ")
	    	.append("				where rd.hospitalName = h.name ")
	    	.append("				and TO_DAYS(?) = TO_DAYS(rd.createdate) ")
	    	.append("				and h.isResAssessed='1' ")
	    	.append("				group by h.rsmRegion ")
	    	.append("			) pNum1 right join tbl_userinfo u on u.region = pNum1.rsmRegion ")
	    	.append("			where u.level='RSM' ")
	    	.append("		) pNumTemp, ")
	    	.append("		( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
	    	.append("			select IFNULL(sum(rd.lsnum),0) as lsNum, h.rsmRegion ")
	    	.append("			from tbl_respirology_data rd, tbl_hospital h ")
	    	.append("			where rd.hospitalName = h.name ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(rd.createdate) ")
	    	.append("			and h.isResAssessed='1' ")
	    	.append("			group by h.rsmRegion ")
	    	.append("			) lsNum1 right join tbl_userinfo u on u.region = lsNum1.rsmRegion ")
	    	.append("			where u.level='RSM' ")
	    	.append("		) lsNumTemp")
	    	.append("		where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
	    	.append("		order by lsNumTemp.lsNum/pNumTemp.pNum ")
	    	.append("		limit 1	")
	    	.append(") whRateMinT,")
	    	.append("( 	select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMax,pNumTemp.name as whRateMaxUser ")
	    	.append("	from ( ")
	    	.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (")
	    	.append("				select IFNULL(sum(rd.pnum),0) as pNum, h.rsmRegion ")
	    	.append("				from tbl_respirology_data rd, tbl_hospital h ")
	    	.append("				where rd.hospitalName = h.name ")
	    	.append("				and TO_DAYS(?) = TO_DAYS(rd.createdate)")
	    	.append("				and h.isResAssessed='1' ")
	    	.append("				group by h.rsmRegion ")
	    	.append("			) pNum1 right join tbl_userinfo u on u.region = pNum1.rsmRegion ")
	    	.append("			where u.level='RSM' ")
	    	.append("		) pNumTemp, ")
	    	.append("		( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
	    	.append("				select IFNULL(sum(rd.lsnum),0) as lsNum, h.rsmRegion ")
	    	.append("				from tbl_respirology_data rd, tbl_hospital h ")
	    	.append("				where rd.hospitalName = h.name ")
	    	.append("				and TO_DAYS(?) = TO_DAYS(rd.createdate)")
	    	.append("				and h.isResAssessed='1' ")
	    	.append("				group by h.rsmRegion ")
	    	.append("			) lsNum1 right join tbl_userinfo u on u.region = lsNum1.rsmRegion ")
	    	.append("			where u.level='RSM' ")
	    	.append("		) lsNumTemp ")
	    	.append("		where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
	    	.append("		order by lsNumTemp.lsNum/pNumTemp.pNum desc ")
	    	.append("		limit 1	")
	    	.append(") whRateMaxT,")
	    	.append("( ")
	    	.append("	select IFNULL(av1.averageDose,0) as averageDoseMin, u.name as averageDoseMinUser from ")
	    	.append("		( ")
	    	.append("			select IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) as averageDose, h.rsmRegion")
	    	.append("			from tbl_respirology_data rd, tbl_hospital h ")
	    	.append("			where rd.hospitalName = h.name ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(rd.createdate) ")
	    	.append("			and h.isResAssessed='1' ")
	    	.append("			group by h.rsmRegion ")
	    	.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
	    	.append("		where u.level='RSM' ")
	    	.append("		order by av1.averageDose")
	    	.append("		limit 1	")
	    	.append(") averageDoseMinT,")
	    	.append("( ")
	    	.append("	select IFNULL(av2.averageDose,0) as averageDoseMax, u.name as averageDoseMaxUser from ")
	    	.append("		( ")
	    	.append("			select IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) as averageDose, h.rsmRegion")
	    	.append("			from tbl_respirology_data rd, tbl_hospital h")
	    	.append("			where rd.hospitalName = h.name ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(rd.createdate) ")
	    	.append("			and h.isResAssessed='1' ")
	    	.append("			group by h.rsmRegion ")
	    	.append("		) av2 right join tbl_userinfo u on u.region = av2.rsmRegion ")
	    	.append("		where u.level='RSM' ")
	    	.append("		order by av2.averageDose desc ")
	    	.append("		limit 1	")
	    	.append(") averageDoseMaxT");
	    return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate},new TopAndBottomRSMDataRowMapper());
	}
	
	@Override
	public List<DailyReportData> getAllRSMDataByTelephone() throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select u.name,u.userCode,")
			.append(" ( select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.isResAssessed='1' ) hosNum,")
			.append(" ( select count(1) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name ")
			.append(" 		and h1.rsmRegion = u.region and TO_DAYS(NOW()) - TO_DAYS(rd.createdate) = 1 and h1.isResAssessed='1' ")
			.append(" ) inNum, ")
			.append(" ( select IFNULL(sum(rd.pnum),0) from tbl_respirology_data rd, tbl_hospital h1 ")
			.append(" 	where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(NOW()) - TO_DAYS(rd.createdate) = 1 and h1.isResAssessed='1' ")
			.append(" ) pnum, ")
			.append(" ( select IFNULL(sum(rd.whnum),0) from tbl_respirology_data rd, tbl_hospital h1 ")
			.append("	where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(NOW()) - TO_DAYS(rd.createdate) = 1 and h1.isResAssessed='1' ")
			.append(" ) whnum, ")
			.append(" ( select IFNULL(sum(rd.lsnum),0) from tbl_respirology_data rd, tbl_hospital h1 ")
			.append(" 	where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(NOW()) - TO_DAYS(rd.createdate) = 1 and h1.isResAssessed='1' ")
			.append(" ) lsnum, ")
			.append(" ( select IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) ")
			.append("	from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(NOW()) - TO_DAYS(rd.createdate) = 1 and h1.isResAssessed='1' ")
			.append(" ) averageDose ")
			.append(" from tbl_userinfo u where u.level='RSM'  order by u.name");
		
		return dataBean.getJdbcTemplate().query(sb.toString(), new DailyReportDataRowMapper());
	}
	
	@Override
	public RespirologyData getRespirologyDataByHospital(final String hospitalName) throws Exception {
		return dataBean.getJdbcTemplate().queryForObject("select rd.*,h.code as hospitalCode,h.dsmName,h.isResAssessed, h.dragonType from tbl_respirology_data rd, tbl_hospital h where rd.hospitalName=? and DATE_FORMAT(rd.createdate,'%Y-%m-%d') = curdate() and rd.hospitalName = h.name", new Object[]{hospitalName}, new RespirologyRowMapper());
	}
	
	@Override
	public List<RespirologyData> getRespirologyDataByDate(Date createdatebegin, Date createdateend) throws Exception {
		String sql = "select rd.*,h.code as hospitalCode,h.dsmName,h.isResAssessed, h.dragonType from tbl_respirology_data rd, tbl_hospital h where DATE_FORMAT(createdate,'%Y-%m-%d') between DATE_FORMAT(?,'%Y-%m-%d') and DATE_FORMAT(?,'%Y-%m-%d') and rd.hospitalName = h.name order by createdate desc";
		return dataBean.getJdbcTemplate().query(sql, new Object[]{new Timestamp(createdatebegin.getTime()),new Timestamp(createdateend.getTime())},new RespirologyRowMapper());
	}
	
	@Override
	public RespirologyData getRespirologyDataByHospitalAndDate(final String hospitalName, final Date createdate) throws Exception {
	    return dataBean.getJdbcTemplate().queryForObject("select rd.*,h.code as hospitalCode,h.dsmName,h.isResAssessed, h.dragonType from tbl_respirology_data rd, tbl_hospital h where rd.hospitalName=? and DATE_FORMAT(rd.createdate,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') and rd.hospitalName = h.name", new Object[]{hospitalName,new Timestamp(createdate.getTime())}, new RespirologyRowMapper());
	}
	
	public RespirologyData getRespirologyDataById(int id) throws Exception {
	    return dataBean.getJdbcTemplate().queryForObject("select rd.*,h.code as hospitalCode,h.dsmName,h.isResAssessed, h.dragonType from tbl_respirology_data rd, tbl_hospital h where rd.id=? and rd.hospitalName = h.name", new Object[]{id}, new RespirologyRowMapper());
	}

    public List<MobileRESDailyData> getDailyRESData4RSMByRegion(String region) throws Exception {
        StringBuffer mobileRESDailySQL = new StringBuffer();
        
        Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
        
        mobileRESDailySQL.append("select ui.region as name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isResAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_RES)
        .append(" from ( ")
        .append(" select u.region as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_RES)
        .append(" from tbl_userinfo u, tbl_respirology_data rd, tbl_hospital h1 ")
        .append(" where rd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and TO_DAYS(?) = TO_DAYS(rd.createdate) ")
        .append(" and h1.isResAssessed='1' ")
        .append(" and u.level='RSM' ")
        .append(" and u.regionCenter = ? ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSM' ")
        .append(" and ui.regionCenter = ?")
        .append(" order by ui.region ");
        return dataBean.getJdbcTemplate().query(mobileRESDailySQL.toString(), new Object[]{paramDate,region,region},new RespirologyMobileRowMapper());
    }
    
    @Cacheable(value="getDailyRESData4CountoryMobile")
    public MobileRESDailyData getDailyRESData4CountoryMobile() throws Exception {
        StringBuffer mobileRESDailySQL = new StringBuffer();
        
        Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
        
        mobileRESDailySQL.append("select '全国' as name,null as userCode,")
            .append(" '' as regionCenterCN, ")
            .append(" ( select count(1) from tbl_hospital h where h.isResAssessed='1' ) hosNum,")
            .append(LsAttributes.SQL_DAILYREPORT_SELECTION_RES)
            .append(" from ( ")
            .append("   select rd.* from tbl_respirology_data rd, tbl_hospital h ")
            .append("   where rd.hospitalName = h.name ")
            .append("   and TO_DAYS(rd.createdate) = TO_DAYS(?) ")
            .append("   and h.isResAssessed='1' ")
            .append(" ) rd ");
        return dataBean.getJdbcTemplate().queryForObject(mobileRESDailySQL.toString(), new Object[]{paramDate},new RespirologyMobileRowMapper());
    }

	@Override
	public List<MobileRESDailyData> getDailyRESData4DSMMobile(String telephone) throws Exception {
		StringBuffer mobileRESDailySQL = new StringBuffer();
		
		Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
        mobileRESDailySQL.append("select ui.name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.dsmCode = ui.userCode and h.rsmRegion = ui.region and h.isResAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_RES)
        .append(" from ( ")
        .append(" select u.name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_RES)
        .append(" from tbl_userinfo u, tbl_respirology_data rd, tbl_hospital h1 ")
        .append(" where rd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and h1.dsmCode = u.userCode ")
        .append(" and TO_DAYS(?) = TO_DAYS(rd.createdate) ")
        .append(" and h1.isResAssessed='1' ")
        .append(" and u.level='DSM' ")
        .append(" and u.region = ( select region from tbl_userinfo where telephone=? ) ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='DSM' ")
        .append(" and ui.region = ( select region from tbl_userinfo where telephone=? )");
		return dataBean.getJdbcTemplate().query(mobileRESDailySQL.toString(), new Object[]{paramDate,telephone,telephone},new RespirologyMobileRowMapper());
	}

	@Override
	public List<MobileRESDailyData> getDailyRESData4RSMMobile(String telephone) throws Exception {
		StringBuffer mobileRESDailySQL = new StringBuffer();
		
		Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
        mobileRESDailySQL.append("select ui.region as name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isResAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_RES)
        .append(" from ( ")
        .append(" select u.region as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_RES)
        .append(" from tbl_userinfo u, tbl_respirology_data rd, tbl_hospital h1 ")
        .append(" where rd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and TO_DAYS(?) = TO_DAYS(rd.createdate) ")
        .append(" and h1.isResAssessed='1' ")
        .append(" and u.level='RSM' ")
        .append(" and u.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSM' ")
        .append(" and ui.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? )")
        .append(" order by ui.region ");
		return dataBean.getJdbcTemplate().query(mobileRESDailySQL.toString(), new Object[]{paramDate,telephone,telephone},new RespirologyMobileRowMapper());
	}

	@Override
	@Cacheable(value="getDailyRESData4RSDMobile")
	public List<MobileRESDailyData> getDailyRESData4RSDMobile() throws Exception {
		StringBuffer mobileRESDailySQL = new StringBuffer();
		
		Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
        mobileRESDailySQL.append("select ( select distinct property_value from tbl_property where property_name = ui.regionCenter ) as name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.region = ui.regionCenter and h.isResAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_RES)
        .append(" from ( ")
        .append(" select u.regionCenter as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_RES)
        .append(" from tbl_userinfo u, tbl_respirology_data rd, tbl_hospital h1 ")
        .append(" where rd.hospitalName = h1.name ")
        .append(" and h1.region = u.regionCenter ")
        .append(" and TO_DAYS(?) = TO_DAYS(rd.createdate) ")
        .append(" and h1.isResAssessed='1' ")
        .append(" and u.level='RSD' ")
        .append(" group by u.regionCenter ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSD' ")
        .append(" order by ui.regionCenter ");
		return dataBean.getJdbcTemplate().query(mobileRESDailySQL.toString(), new Object[]{paramDate}, new RespirologyMobileRowMapper());
	}
	
	@Override
	public List<MobileRESDailyData> getDailyRESChildData4DSMMobile(String telephone) throws Exception {
	    StringBuffer mobileRESDailySQL = new StringBuffer();
	    
	    Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
	    
	    mobileRESDailySQL.append("select ui.name, ui.userCode,")
	    .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.saleCode = ui.userCode and h.rsmRegion = ui.region and h.dsmCode = ui.superior and h.isResAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_RES)
        .append(" from ( ")
        .append(" select u.name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_RES)
        .append(" from tbl_userinfo u, tbl_respirology_data rd, tbl_hospital h1 ")
        .append(" where rd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and h1.dsmCode = u.superior ")
        .append(" and h1.saleCode = u.userCode ")
        .append(" and TO_DAYS(?) = TO_DAYS(rd.createdate) ")
        .append(" and h1.isResAssessed='1' ")
        .append(" and u.level='REP' ")
        .append(" and u.superior = ( select userCode from tbl_userinfo where telephone=? ) ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='REP' ")
        .append(" and ui.superior = ( select userCode from tbl_userinfo where telephone=? )");
	    return dataBean.getJdbcTemplate().query(mobileRESDailySQL.toString(), new Object[]{paramDate,telephone,telephone},new RespirologyMobileRowMapper());
	}
	
	@Override
	public List<MobileRESDailyData> getDailyRESChildData4RSMMobile(String telephone) throws Exception {
	    return this.getDailyRESData4DSMMobile(telephone);
	    /**
	     * 
	    StringBuffer mobileRESDailySQL = new StringBuffer();
	    
	    Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
	    
	    mobileRESDailySQL.append("select u.name as name,u.userCode, ")
	    .append(" ( select count(1) from tbl_hospital h where h.dsmCode = u.userCode and h.rsmRegion = u.region and h.isResAssessed='1' ) hosNum,")
	    .append(" ( select count(1) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') inNum, ")
	    .append(" ( select IFNULL(sum(rd.pnum),0) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') pNum, ")
	    .append(" ( select IFNULL(sum(rd.whnum),0) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') whNum, ")
	    .append(" ( select IFNULL(sum(rd.lsnum),0) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') lsNum, ")
	    .append(" ( select IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) ")
	    .append("   from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1' ")
	    .append(" ) averageDose,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.oqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') omgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.tqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') tmgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.otid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') thmgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.tbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') fmgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.ttid,0)/100*IFNULL(rd.lsnum,0) + IFNULL(rd.thbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') smgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.fbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 )  from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and h1.dsmCode = u.userCode and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') emgRate ")
	    .append(" from tbl_userinfo u where u.level='DSM'and u.region = ( select region from tbl_userinfo where telephone=? ) ");
	    return dataBean.getJdbcTemplate().query(mobileRESDailySQL.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,telephone},new RespirologyMobileRowMapper());
	     */
	}
	
	@Override
	public List<MobileRESDailyData> getDailyRESChildData4RSDMobile(String telephone) throws Exception {
	    return this.getDailyRESData4RSMMobile(telephone);
	    /**
	     * 
	    StringBuffer mobileRESDailySQL = new StringBuffer();
	    
	    Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
	    
	    mobileRESDailySQL.append("select u.region as name,u.userCode, ")
	    .append(" ( select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.isResAssessed='1' ) hosNum,")
	    .append(" ( select count(1) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') inNum, ")
	    .append(" ( select IFNULL(sum(rd.pnum),0) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') pNum, ")
	    .append(" ( select IFNULL(sum(rd.whnum),0) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') whNum, ")
	    .append(" ( select IFNULL(sum(rd.lsnum),0) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') lsNum, ")
	    .append(" ( select IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) ")
	    .append("   from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1' ")
	    .append(" ) averageDose,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.oqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') omgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.tqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') tmgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.otid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') thmgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.tbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') fmgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.ttid,0)/100*IFNULL(rd.lsnum,0) + IFNULL(rd.thbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') smgRate,")
	    .append(" ( select IFNULL( sum( IFNULL(rd.fbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 )  from tbl_respirology_data rd, tbl_hospital h1 where rd.hospitalName = h1.name and h1.rsmRegion = u.region and TO_DAYS(?) = TO_DAYS(rd.createdate)  and h1.isResAssessed='1') emgRate ")
	    .append(" from tbl_userinfo u where u.level='RSM' and u.regionCenter = (select regionCenter from tbl_userinfo where telephone=?)");
	    return dataBean.getJdbcTemplate().query(mobileRESDailySQL.toString(), new Object[]{paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,paramDate,telephone}, new RespirologyMobileRowMapper());
	     */
	}

	@Override
	public void insert(final RespirologyData respirologyData, final UserInfo operator, final Hospital hospital) throws Exception {
		logger.info(">>RespirologyDAOImpl insert");
		
		final String sql = "insert into tbl_respirology_data(id,createdate,hospitalName,pnum,aenum,whnum,lsnum,etmsCode,operatorName,region,rsmRegion,oqd,tqd,otid,tbid,ttid,thbid,fbid,recipeType,updatedate,dsmCode) values(null,NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, respirologyData.getHospitalName());
				ps.setInt(2, respirologyData.getPnum());
				ps.setInt(3, respirologyData.getAenum());
				ps.setInt(4, respirologyData.getWhnum());
				ps.setInt(5, respirologyData.getLsnum());
				ps.setString(6, hospital.getSaleCode());
				ps.setString(7, operator.getName());
				ps.setString(8, hospital.getRegion());
				ps.setString(9, hospital.getRsmRegion());
				ps.setDouble(10, respirologyData.getOqd());
				ps.setDouble(11, respirologyData.getTqd());
				ps.setDouble(12, respirologyData.getOtid());
				ps.setDouble(13, respirologyData.getTbid());
				ps.setDouble(14, respirologyData.getTtid());
				ps.setDouble(15, respirologyData.getThbid());
				ps.setDouble(16, respirologyData.getFbid());
				ps.setString(17, respirologyData.getRecipeType());
				ps.setString(18, (operator.getSuperior()==null||"".equalsIgnoreCase(operator.getSuperior()))?operator.getUserCode():operator.getSuperior());
				return ps;
			}
		}, keyHolder);
		logger.info("returned id is "+keyHolder.getKey().intValue());
	}
	
	@Override
	public void insert(final RespirologyData respirologyData, final String dsmCode) throws Exception {
	    logger.info(">>RespirologyDAOImpl insert - upload daily data");
	    
	    final String sql = "insert into tbl_respirology_data(id,createdate,hospitalName,pnum,aenum,whnum,lsnum,etmsCode,operatorName,region,rsmRegion,oqd,tqd,otid,tbid,ttid,thbid,fbid,recipeType,updatedate,dsmCode) values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)";
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
	        @Override
	        public PreparedStatement createPreparedStatement(
	                Connection connection) throws SQLException {
	            PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
	            ps.setTimestamp(1, new Timestamp(respirologyData.getCreatedate().getTime()));
	            ps.setString(2, respirologyData.getHospitalName());
	            ps.setInt(3, respirologyData.getPnum());
	            ps.setInt(4, respirologyData.getAenum());
	            ps.setInt(5, respirologyData.getWhnum());
	            ps.setInt(6, respirologyData.getLsnum());
	            ps.setString(7, respirologyData.getSalesETMSCode());
	            ps.setString(8, respirologyData.getSalesName());
	            ps.setString(9, respirologyData.getRegion());
	            ps.setString(10, respirologyData.getRsmRegion());
	            ps.setDouble(11, respirologyData.getOqd());
	            ps.setDouble(12, respirologyData.getTqd());
	            ps.setDouble(13, respirologyData.getOtid());
	            ps.setDouble(14, respirologyData.getTbid());
	            ps.setDouble(15, respirologyData.getTtid());
	            ps.setDouble(16, respirologyData.getThbid());
	            ps.setDouble(17, respirologyData.getFbid());
	            ps.setString(18, respirologyData.getRecipeType());
	            ps.setString(19, dsmCode);
	            return ps;
	        }
	    }, keyHolder);
	    logger.info("returned id is "+keyHolder.getKey().intValue());
	}

	@Override
	public void update(final RespirologyData respirologyData, final UserInfo operator) throws Exception {
	    StringBuffer sql = new StringBuffer("update tbl_respirology_data set ");
	    sql.append("updatedate=NOW()");
	    sql.append(", pnum=? ");
	    sql.append(", aenum=? ");
	    sql.append(", whnum=? ");
	    sql.append(", lsnum=? ");
	    sql.append(", oqd=? ");
	    sql.append(", tqd=? ");
	    sql.append(", otid=? ");
	    sql.append(", tbid=? ");
	    sql.append(", ttid=? ");
	    sql.append(", thbid=? ");
	    sql.append(", fbid=? ");
	    sql.append(", recipeType=? ");
	    
	    List<Object> paramList = new ArrayList<Object>();
	    paramList.add(respirologyData.getPnum());
	    paramList.add(respirologyData.getAenum());
	    paramList.add(respirologyData.getWhnum());
	    paramList.add(respirologyData.getLsnum());
	    paramList.add(respirologyData.getOqd());
	    paramList.add(respirologyData.getTqd());
	    paramList.add(respirologyData.getOtid());
	    paramList.add(respirologyData.getTbid());
	    paramList.add(respirologyData.getTtid());
	    paramList.add(respirologyData.getThbid());
	    paramList.add(respirologyData.getFbid());
	    paramList.add(respirologyData.getRecipeType());
	    paramList.toArray();
	    
	    if( null == operator ){
	    	logger.info("using web to update the data, no need to update the sales info");
	    	paramList.add(respirologyData.getDataId());
	    }else{
//	    	sql.append(", etmsCode=? ");
	    	sql.append(", operatorName=? ");
	    	
//	    	paramList.add(operator.getUserCode());
	    	paramList.add(operator.getName());
	    	paramList.add(respirologyData.getDataId());
	    }
	    sql.append(" where id=? ");
	    
		dataBean.getJdbcTemplate().update(sql.toString(), paramList.toArray());
	}
	
	public List<RespirologyMonthDBData> getRESMonthReportDBData(String lastMonthDuration) throws Exception {
        StringBuffer resMonthSQL = new StringBuffer(LsAttributes.SQL_MONTH_WEEKLY_REPORT_RES);
       return dataBean.getJdbcTemplate().query(resMonthSQL.toString(),new Object[]{lastMonthDuration},new RespirologyMonthDataRowMapper());
    }
	
	public List<RespirologyMonthDBData> getRESMonthReportDBDataOfCountry(String lastMonthDuration) throws Exception {
	    StringBuffer resMonthSQL = new StringBuffer(LsAttributes.SQL_MONTH_WEEKLY_REPORT_RES_COUNTRY);
	    return dataBean.getJdbcTemplate().query(resMonthSQL.toString(),new Object[]{lastMonthDuration},new RespirologyMonthDataRowMapper());
	}
	
	public List<RespirologyMonthDBData> getRESMonthReportWeeklyDBData(String lastWeekDuration) throws Exception {
		StringBuffer resMonthSQL = new StringBuffer(LsAttributes.SQL_MONTH_WEEKLY_REPORT_WEEKLY_RES);
		return dataBean.getJdbcTemplate().query(resMonthSQL.toString(),new Object[]{lastWeekDuration},new RespirologyMonthDataRowMapper());
	}
	
	public List<RespirologyMonthDBData> getRESMonthReportWeeklyDBDataOfCountry(String lastWeekDuration) throws Exception {
	    StringBuffer resMonthSQL = new StringBuffer(LsAttributes.SQL_MONTH_WEEKLY_REPORT_WEEKLY_RES_COUNTRY);
	    return dataBean.getJdbcTemplate().query(resMonthSQL.toString(),new Object[]{lastWeekDuration},new RespirologyMonthDataRowMapper());
	}
	
	@Override
	public String getLatestDuration() throws Exception {
		return dataBean.getJdbcTemplate().queryForObject("select distinct duration from tbl_respirology_data_weekly order by duration desc limit 1", String.class);
	}
	

    public List<WeeklyDataOfHospital> getWeeklyDataOfHospital(Date refreshDate) throws Exception {
        Timestamp lastweekDay = new Timestamp(refreshDate.getTime());
        StringBuffer sb = new StringBuffer();
        sb.append(LsAttributes.SQL_HOSPITAL_WEEKLY_DATA_SELECTION)
          .append(" from tbl_respirology_data_weekly ")
          .append(" where duration = CONCAT(DATE_FORMAT(DATE_SUB(?, Interval 6 day),'%Y.%m.%d'), '-',DATE_FORMAT(?,'%Y.%m.%d'))");
        return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{lastweekDay,lastweekDay}, new WeeklyHospitalDataRowMapper());
    }
    

    public RespirologyLastWeekData getLastWeekDataOfRSM(String rsmRegion) throws Exception {
        StringBuffer mobileRESWeeklySQL = new StringBuffer();
        String lastWeekDuration = DateUtils.getWeeklyDurationYYYYMMDD(new Date());
        
        mobileRESWeeklySQL.append("select duration, IFNULL(sum(lastweek.lsnum),0) as lsnum ")
        .append(" , IFNULL(sum(lastweek.aenum),0) as aenum ");
        
        if( !"全国".equalsIgnoreCase(rsmRegion) ){
            mobileRESWeeklySQL.append(" from tbl_respirology_data_weekly lastweek, tbl_hospital h ")
            .append(" where lastweek.hospitalCode=h.code ")
            .append(" and h.rsmRegion=? ")
            .append(" and duration=? ");
            return dataBean.getJdbcTemplate().queryForObject(mobileRESWeeklySQL.toString(),new Object[]{rsmRegion,lastWeekDuration},new ResLastWeekDataRowMapper());
        }else{
            mobileRESWeeklySQL.append(" from tbl_respirology_data_weekly lastweek ")
            .append(" where duration=? ");
            return dataBean.getJdbcTemplate().queryForObject(mobileRESWeeklySQL.toString(),new Object[]{lastWeekDuration},new ResLastWeekDataRowMapper());
        }
        
    }
    

	@Override
	public List<MonthlyStatisticsData> getMonthlyStatisticsData(
			String beginDuraion, String endDuraion, String level)
			throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
		switch(level){
			case LsAttributes.USER_LEVEL_RSD:
				inRateSQL.append(" select h.region, '' as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(" ,IFNULL( sum(  ")
			    .append("   case ")
			    .append("       when h.dragonType='Emerging' then least(innum*3,3) ")
			    .append("       when h.dragonType='Core' then least(innum,3) ")
			    .append(" 	end")
			    .append(" ) ")
			    .append(" / (count(1)*3),0 ) as inRate ")
	            .append(", IFNULL(sum(aenum),0) as aenum ")
	            .append(", 0 as risknum ")
				.append(LsAttributes.SQL_MONTHLY_STATISTICS_SELECTION)
	            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSD_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_RSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(" ,IFNULL( sum(  ")
			    .append("   case ")
			    .append("       when h.dragonType='Emerging' then least(innum*3,3) ")
			    .append("       when h.dragonType='Core' then least(innum,3) ")
			    .append(" 	end")
			    .append(" ) ")
			    .append(" / (count(1)*3),0 ) as inRate ")
	            .append(", IFNULL(sum(aenum),0) as aenum ")
	            .append(", 0 as risknum ")
				.append(LsAttributes.SQL_MONTHLY_STATISTICS_SELECTION)
	            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_RSM_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_DSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, h.dsmCode as dsmCode ")
	            .append(", (select distinct name from tbl_userinfo u where u.userCode = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region ) as dsmName")
	            .append(" ,IFNULL( sum(  ")
			    .append("   case ")
			    .append("       when h.dragonType='Emerging' then least(innum*3,3) ")
			    .append("       when h.dragonType='Core' then least(innum,3) ")
			    .append(" 	end")
			    .append(" ) ")
			    .append(" / (count(1)*3),0 ) as inRate ")
				.append(", IFNULL(sum(aenum),0) as aenum ")
	            .append(", 0 as risknum ")
				.append(LsAttributes.SQL_MONTHLY_STATISTICS_SELECTION)
	            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
		        .append(LsAttributes.SQL_MONTHLY_STATISTICS_DSM_CONDITION);
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
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION_RES)
	            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_RSD_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_RSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION_RES)
	            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_RSM_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_DSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, h.dsmCode as dsmCode ")
	            .append(", (select distinct name from tbl_userinfo u where u.userCode = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region ) as dsmName")
				.append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION_RES)
	            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
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
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION_RES)
	            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_EMERGING_RSD_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_RSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, '' as dsmCode, '' as dsmName ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION_RES)
	            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_EMERGING_RSM_CONDITION);
				break;
			case LsAttributes.USER_LEVEL_DSM:
				inRateSQL.append(" select h.region, h.rsmRegion as rsmRegion, h.dsmCode as dsmCode ")
	            .append(", (select distinct name from tbl_userinfo u where u.userCode = h.dsmCode and u.region = h.rsmRegion and u.regionCenter = h.region ) as dsmName")
	            .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION_RES)
	            .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
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
        .append(" ,IFNULL( sum(  ")
	    .append("   case ")
	    .append("       when h.dragonType='Emerging' then least(innum*3,3) ")
	    .append("       when h.dragonType='Core' then least(innum,3) ")
	    .append(" 	end")
	    .append(" ) ")
	    .append(" / (count(1)*3),0 ) as inRate ")
        .append(", IFNULL(sum(aenum),0) as aenum ")
        .append(", 0 as risknum ")
		.append(LsAttributes.SQL_MONTHLY_STATISTICS_SELECTION)
        .append(" from tbl_respirology_data_weekly, tbl_hospital h ")
        .append(" where duration between ? and ? and hospitalCode = h.code ");
        return dataBean.getJdbcTemplate().queryForObject(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion},new MonthlyStatisticsDataRowMapper());
	}

	@Override
	public MonthlyStatisticsData getCoreMonthlyStatisticsCountryData(
			String beginDuraion, String endDuraion) throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
		inRateSQL.append(" select '' as region, '' as rsmRegion, '' as dsmCode, '' as dsmName ")
        .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION_RES)
        .append(" from tbl_respirology_data_weekly, tbl_hospital h  ")
        .append(" where duration between ? and ? and hospitalCode = h.code and h.dragonType='Core' ");
        return dataBean.getJdbcTemplate().queryForObject(inRateSQL.toString(), new Object[]{beginDuraion,endDuraion},new MonthlyStatisticsCoreDataRowMapper());
	}

	@Override
	public MonthlyStatisticsData getEmergingMonthlyStatisticsCountryData(
			String beginDuraion, String endDuraion) throws Exception {
		StringBuffer inRateSQL = new StringBuffer("");
		inRateSQL.append(" select '' as region, '' as rsmRegion, '' as dsmCode, '' as dsmName ")
        .append(LsAttributes.SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION_RES)
        .append(" from tbl_respirology_data_weekly, tbl_hospital h  ")
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
