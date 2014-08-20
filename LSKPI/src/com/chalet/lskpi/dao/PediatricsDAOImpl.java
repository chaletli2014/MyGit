package com.chalet.lskpi.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.chalet.lskpi.mapper.PediatricsMobileRowMapper;
import com.chalet.lskpi.mapper.TopAndBottomRSMDataRowMapper;
import com.chalet.lskpi.model.DailyReportData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobilePEDDailyData;
import com.chalet.lskpi.model.PediatricsData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserCode;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyRatioData;
import com.chalet.lskpi.utils.DailyReportDataRowMapper;
import com.chalet.lskpi.utils.DataBean;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.PEDWeeklyRatioDataRowMapper;
import com.chalet.lskpi.utils.ReportProcessDataRowMapper;
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
        .append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
        .append("       select least(count(1),3) as inNum, h1.dsmCode, h1.rsmRegion ")
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
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDataRowMapper());
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
    	.append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
    	.append("       select least(count(1),3) as inNum, h1.dsmCode, h1.rsmRegion ")
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
    	return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{startDate,startDate,telephone}, new ReportProcessDataRowMapper());
    }
    
    @Override
    public ReportProcessData getSalesSelfReportProcessPEDData(String telephone) throws Exception {
        StringBuffer sb = new StringBuffer("");
        sb.append("select count(1) as hosNum, ")
        .append("( select IFNULL(sum(inNum),0) as validInNum from ( ")
        .append("       select least(count(1),3) as inNum, h1.code as hosCode, h1.dsmCode, h1.rsmRegion ")
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
        return dataBean.getJdbcTemplate().queryForObject(sb.toString(), new Object[]{new Timestamp(startDate.getTime()),new Timestamp(startDate.getTime()),telephone}, new ReportProcessDataRowMapper());
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
	    
	    sb.append("insert into tbl_pediatrics_data_weekly ")
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
	    .append("now()  ")
	    .append("from ( ")
	    .append("	SELECT ")
	    .append("	h.code,	")
	    .append("	count_hos.inNum, ")
	    .append("	(sum(pd.pnum)/count_hos.inNum)*5 as pnum, ")
	    .append("	(sum(pd.whnum)/count_hos.inNum)*5 as whnum, ")
	    .append("	(sum(pd.lsnum)/count_hos.inNum)*5 as lsnum, ")
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
	    	.append("		and h.isPedAssessed='1' ") 
	    	.append("		and u.level='RSM' ") 
	    	.append("		group by u.region ") 
	    	.append("	) hosNumTemp, ") 
	    	.append("		( ") 
	    	.append("		select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from (")
	    	.append("			select IFNULL(count(1),0) as inNum, h.rsmRegion ")
	    	.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
	    	.append("			where pd.hospitalName = h.name  ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(pd.createdate)")
	    	.append("			and h.isPedAssessed='1' ")
	    	.append("			group by h.rsmRegion ")
	    	.append("		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region ")
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
	    	.append("		and h.isPedAssessed='1' ")
	    	.append("		and u.level='RSM' ")
	    	.append("		group by u.region ")
	    	.append("	) hosNumTemp, ")
	    	.append("	( select IFNULL(inNum1.inNum,0) as inNum, u.region as rsmRegion, u.name from ( ")
	    	.append("			select IFNULL(count(1),0) as inNum, h.rsmRegion ")
	    	.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
	    	.append("			where pd.hospitalName = h.name ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(pd.createdate)")
	    	.append("			and h.isPedAssessed='1' ")
	    	.append("			group by h.rsmRegion ")
	    	.append("		) inNum1 right join tbl_userinfo u on inNum1.rsmRegion = u.region ")
	    	.append("		where u.level='RSM' ")
	    	.append("	) inNumTemp ")
	    	.append("	where hosNumTemp.rsmRegion = inNumTemp.rsmRegion ")
	    	.append("	order by inNumTemp.inNum/hosNumTemp.hosNum desc ")
	    	.append("	limit 1	")
	    	.append(") inRateMaxT, ")
	    	.append("( 	select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMin,pNumTemp.name as whRateMinUser ")
	    	.append("	from ( ")
	    	.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from ( ")
	    	.append("				select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion ")
	    	.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
	    	.append("				where pd.hospitalName = h.name ")
	    	.append("				and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
	    	.append("				and h.isPedAssessed='1' ")
	    	.append("				group by h.rsmRegion ")
	    	.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
	    	.append("			where u.level='RSM' ")
	    	.append("		) pNumTemp, ")
	    	.append("		( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
	    	.append("			select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion ")
	    	.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
	    	.append("			where pd.hospitalName = h.name ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
	    	.append("			and h.isPedAssessed='1' ")
	    	.append("			group by h.rsmRegion ")
	    	.append("			) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
	    	.append("			where u.level='RSM' ")
	    	.append("		) lsNumTemp")
	    	.append("		where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
	    	.append("		order by lsNumTemp.lsNum/pNumTemp.pNum ")
	    	.append("		limit 1	")
	    	.append(") whRateMinT,")
	    	.append("( 	select IFNULL(lsNumTemp.lsNum/pNumTemp.pNum,0) as whRateMax,pNumTemp.name as whRateMaxUser ")
	    	.append("	from ( ")
	    	.append("			select IFNULL(pNum1.pNum,0) as pNum, u.region as rsmRegion, u.name from (")
	    	.append("				select IFNULL(sum(pd.pnum),0) as pNum, h.rsmRegion ")
	    	.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
	    	.append("				where pd.hospitalName = h.name ")
	    	.append("				and TO_DAYS(?) = TO_DAYS(pd.createdate)")
	    	.append("				and h.isPedAssessed='1' ")
	    	.append("				group by h.rsmRegion ")
	    	.append("			) pNum1 right join tbl_userinfo u on pNum1.rsmRegion = u.region ")
	    	.append("			where u.level='RSM' ")
	    	.append("		) pNumTemp, ")
	    	.append("		( select IFNULL(lsNum1.lsNum,0) as lsNum, u.region as rsmRegion, u.name from ( ")
	    	.append("				select IFNULL(sum(pd.lsnum),0) as lsNum, h.rsmRegion ")
	    	.append("				from tbl_pediatrics_data pd, tbl_hospital h ")
	    	.append("				where pd.hospitalName = h.name ")
	    	.append("				and TO_DAYS(?) = TO_DAYS(pd.createdate)")
	    	.append("				and h.isPedAssessed='1' ")
	    	.append("				group by h.rsmRegion ")
	    	.append("			) lsNum1 right join tbl_userinfo u on lsNum1.rsmRegion = u.region ")
	    	.append("			where u.level='RSM' ")
	    	.append("		) lsNumTemp ")
	    	.append("		where pNumTemp.rsmRegion = lsNumTemp.rsmRegion ")
	    	.append("		order by lsNumTemp.lsNum/pNumTemp.pNum desc ")
	    	.append("		limit 1	")
	    	.append(") whRateMaxT,")
	    	.append("( ")
	    	.append("	select IFNULL(av1.averageDose,0) as averageDoseMin, u.name as averageDoseMinUser from ")
	    	.append("		( ")
	    	.append("			select IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose, h.rsmRegion")
	    	.append("			from tbl_pediatrics_data pd, tbl_hospital h ")
	    	.append("			where pd.hospitalName = h.name ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
	    	.append("			and h.isPedAssessed='1' ")
	    	.append("			group by h.rsmRegion ")
	    	.append("		) av1 right join tbl_userinfo u on u.region = av1.rsmRegion ")
	    	.append("		where u.level='RSM' ")
	    	.append("		order by av1.averageDose")
	    	.append("		limit 1	")
	    	.append(") averageDoseMinT,")
	    	.append("( ")
	    	.append("	select IFNULL(av2.averageDose,0) as averageDoseMax, u.name as averageDoseMaxUser from ")
	    	.append("		( ")
	    	.append("			select IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose, h.rsmRegion")
	    	.append("			from tbl_pediatrics_data pd, tbl_hospital h")
	    	.append("			where pd.hospitalName = h.name ")
	    	.append("			and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
	    	.append("			and h.isPedAssessed='1' ")
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
		Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
		sb.append("select u.name,u.userCode,")
			.append(" ( select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.isPedAssessed='1' ) hosNum,")
			.append(" count(1) as inNum, ")
			.append(" IFNULL(sum(pd.pnum),0) as pnum, ")
			.append(" IFNULL(sum(pd.whnum),0) as whnum, ")
			.append(" IFNULL(sum(pd.lsnum),0) as lsnum, ")
			.append(" IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose ")
			.append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
		    .append(" where pd.hospitalName = h1.name ")
    		.append(" and h1.rsmRegion = u.region ")
    		.append(" and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
    		.append(" and h1.isPedAssessed='1' ")
    		.append(" and u.level='RSM' ")
		    .append(" group by u.region ");
		
		return dataBean.getJdbcTemplate().query(sb.toString(), new Object[]{paramDate},new DailyReportDataRowMapper());
	}
	
	@Override
	public PediatricsData getPediatricsDataByHospital(String hospitalName)
			throws Exception {
		return dataBean.getJdbcTemplate().queryForObject("select pd.*,h.code as hospitalCode,h.dsmName,h.isPedAssessed from tbl_pediatrics_data pd, tbl_hospital h where pd.hospitalName=? and DATE_FORMAT(pd.createdate,'%Y-%m-%d') = curdate() and pd.hospitalName = h.name", new Object[]{hospitalName}, new PediatricsRowMapper());
	}

	@Override
	public PediatricsData getPediatricsDataByHospitalAndDate(
			String hospitalName, Date createdate) throws Exception {
		return dataBean.getJdbcTemplate().queryForObject("select pd.*,h.code as hospitalCode,h.dsmName,h.isPedAssessed from tbl_pediatrics_data pd, tbl_hospital h where pd.hospitalName=? and DATE_FORMAT(pd.createdate,'%Y-%m-%d') = DATE_FORMAT(?,'%Y-%m-%d') and pd.hospitalName = h.name", new Object[]{hospitalName,new Timestamp(createdate.getTime())}, new PediatricsRowMapper());
	}
	
	@Override
	public List<PediatricsData> getPediatricsDataByDate(Date createdatebegin, Date createdateend) throws Exception {
		String sql = "select pd.*,h.code as hospitalCode,h.dsmName,h.isPedAssessed from tbl_pediatrics_data pd, tbl_hospital h where DATE_FORMAT(pd.createdate,'%Y-%m-%d') between DATE_FORMAT(?,'%Y-%m-%d') and DATE_FORMAT(?,'%Y-%m-%d') and pd.hospitalName = h.name order by createdate desc";
		return dataBean.getJdbcTemplate().query(sql, new Object[]{new Timestamp(createdatebegin.getTime()),new Timestamp(createdateend.getTime())},new PediatricsRowMapper());
	}

	@Override
	public PediatricsData getPediatricsDataById(int id) throws Exception {
		return dataBean.getJdbcTemplate().queryForObject("select pd.*,h.code as hospitalCode,h.dsmName,h.isPedAssessed from tbl_pediatrics_data pd, tbl_hospital h where pd.id=? and pd.hospitalName = h.name", new Object[]{id}, new PediatricsRowMapper());
	}

    @Override
    public List<MobilePEDDailyData> getDailyPEDData4RSMByRegion(String region) throws Exception {
        StringBuffer mobilePEDDailySQL = new StringBuffer();
        
        Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
        
        mobilePEDDailySQL.append("select ui.region as name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isPedAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
        .append(" from ( ")
        .append(" select u.region as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
        .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
        .append(" and h1.isPedAssessed='1' ")
        .append(" and u.level='RSM' ")
        .append(" and u.regionCenter = ? ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSM' ")
        .append(" and ui.regionCenter = ? ")
        .append(" order by ui.region");
        return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,region,region},new PediatricsMobileRowMapper());
    }

    public MobilePEDDailyData getDailyPEDData4CountoryMobile() throws Exception {
        StringBuffer mobilePEDDailySQL = new StringBuffer();
        
        Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
        
        mobilePEDDailySQL.append("select '全国' as name,null as userCode,")
            .append(" '' as regionCenterCN, ")
            .append(" (select count(1) from tbl_hospital h where h.isPedAssessed='1' ) hosNum, ")
            .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
            .append(" from ( ")
            .append("   select ped.* from tbl_pediatrics_data ped, tbl_hospital h ")
            .append("   where ped.hospitalName = h.name ")
            .append("   and TO_DAYS(ped.createdate) = TO_DAYS(?) ")
            .append("   and h.isPedAssessed='1' ")
            .append(" ) pd ");
        return dataBean.getJdbcTemplate().queryForObject(mobilePEDDailySQL.toString(), new Object[]{paramDate},new PediatricsMobileRowMapper());
    }
	
	public List<MobilePEDDailyData> getDailyPEDData4DSMMobile(String telephone) throws Exception {
	    StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
	    Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
	    
	    mobilePEDDailySQL.append("select ui.name, ui.userCode,")
	    .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.dsmCode = ui.userCode and h.rsmRegion = ui.region and h.isPedAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
        .append(" from ( ")
        .append(" select u.name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
        .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and h1.dsmCode = u.userCode ")
        .append(" and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
        .append(" and h1.isPedAssessed='1' ")
        .append(" and u.level='DSM' ")
        .append(" and u.region = ( select region from tbl_userinfo where telephone=? ) ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='DSM' ")
        .append(" and ui.region = ( select region from tbl_userinfo where telephone=? )");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,telephone,telephone},new PediatricsMobileRowMapper());
    }
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDData4RSMMobile(String telephone)	throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
        Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
        mobilePEDDailySQL.append("select ui.region as name, ui.userCode,")
        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
        .append(" ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isPedAssessed='1' ) hosNum, ")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
        .append(" from ( ")
        .append(" select u.region as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
        .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
        .append(" and h1.isPedAssessed='1' ")
        .append(" and u.level='RSM' ")
        .append(" and u.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSM' ")
        .append(" and ui.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
        .append(" order by ui.region");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,telephone,telephone},new PediatricsMobileRowMapper());
	}

	@Override
	public List<MobilePEDDailyData> getDailyPEDData4RSDMobile() throws Exception {
		StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
        Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
		
	    mobilePEDDailySQL.append("select ( select distinct property_value from tbl_property where property_name = ui.regionCenter ) as name,ui.userCode,")
	        .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
	        .append(" ( select count(1) from tbl_hospital h where h.region = ui.regionCenter and h.isPedAssessed='1' ) hosNum, ")
    	    .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
    	    .append(" from ( ")
    	    .append(" select u.regionCenter as name, u.userCode,")
    	    .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
    	    .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
    	    .append(" where pd.hospitalName = h1.name ")
    	    .append(" and h1.region = u.regionCenter ")
    	    .append(" and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
    	    .append(" and h1.isPedAssessed='1' ")
    	    .append(" and u.level='RSD' ")
    	    .append(" group by u.regionCenter ")
    	    .append(" ) dailyData ")
    	    .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
    	    .append(" where ui.level='RSD' ")
    	    .append(" order by ui.regionCenter");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate}, new PediatricsMobileRowMapper());
	}
	
	public List<MobilePEDDailyData> getDailyPEDChildData4DSMMobile(String telephone) throws Exception {
	    StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
	    Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
	    
	    mobilePEDDailySQL.append("select ui.name,ui.userCode,")
	    .append(" (select distinct property_value from tbl_property where property_name=ui.regionCenter ) as regionCenterCN, ")
	    .append(" ( select count(1) from tbl_hospital h where h.saleCode = ui.userCode and h.rsmRegion = ui.region and h.dsmCode = ui.superior and h.isPedAssessed='1') hosNum, ")
	    .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
	    .append(" from ( ")
	    .append(" select u.name,u.userCode,")
	    .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
	    .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
	    .append(" and h1.rsmRegion = u.region ")
	    .append(" and h1.dsmCode = u.superior ")
	    .append(" and h1.saleCode = u.userCode ")
	    .append(" and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
	    .append(" and h1.isPedAssessed='1' ")
	    .append(" and u.level='REP' ")
	    .append(" and u.superior = ( select userCode from tbl_userinfo where telephone=? ) ")
	    .append(" group by u.userCode ")
	    .append(" ) dailyData ")
	    .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
	    .append(" where ui.level='REP' ")
	    .append(" and ui.superior = ( select userCode from tbl_userinfo where telephone=? )");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,telephone,telephone},new PediatricsMobileRowMapper());
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDChildData4RSMMobile(String telephone)	throws Exception {
	    return this.getDailyPEDData4DSMMobile(telephone);
	    /**
	     * 
	    StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
	    Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
	    
	    mobilePEDDailySQL.append("select ui.name, ui.userCode,")
	    .append(" ( select count(1) from tbl_hospital h where h.dsmCode = ui.userCode and h.rsmRegion = ui.region and h.isPedAssessed='1' ) hosNum, ")
	    .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
	    .append(" from ( ")
        .append(" select u.name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
        .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and h1.dsmCode = u.userCode ")
        .append(" and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
        .append(" and h1.isPedAssessed='1' ")
        .append(" and u.level='DSM' ")
        .append(" and u.region = ( select region from tbl_userinfo where telephone=? ) ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='DSM' ")
        .append(" and ui.region = ( select region from tbl_userinfo where telephone=? )");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,telephone,telephone},new PediatricsMobileRowMapper());
	     */
	}
	
	@Override
	public List<MobilePEDDailyData> getDailyPEDChildData4RSDMobile(String telephone) throws Exception {
	    return this.getDailyPEDData4RSMMobile(telephone);
	    /**
	     * 
	    StringBuffer mobilePEDDailySQL = new StringBuffer();
	    
	    Date date = new Date();
	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
	    
	    mobilePEDDailySQL.append("select ui.region as name, ui.userCode,")
	    .append(" ( select count(1) from tbl_hospital h where h.rsmRegion = ui.region and h.isPedAssessed='1' ) hosNum, ")
	    .append(LsAttributes.SQL_DAILYREPORT_SELECTION_ALIAS_PED)
	    .append(" from ( ")
        .append(" select u.region as name,u.userCode,")
        .append(LsAttributes.SQL_DAILYREPORT_SELECTION_PED)
	    .append(" from tbl_userinfo u, tbl_pediatrics_data pd, tbl_hospital h1 ")
        .append(" where pd.hospitalName = h1.name ")
        .append(" and h1.rsmRegion = u.region ")
        .append(" and TO_DAYS(?) = TO_DAYS(pd.createdate) ")
        .append(" and h1.isPedAssessed='1' ")
        .append(" and u.level='RSM' ")
        .append(" and u.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? ) ")
        .append(" group by u.userCode ")
        .append(" ) dailyData ")
        .append(" right join tbl_userinfo ui on ui.userCode = dailyData.userCode ")
        .append(" where ui.level='RSM' ")
        .append(" and ui.regionCenter = ( select regionCenter from tbl_userinfo where telephone=? )");
	    return dataBean.getJdbcTemplate().query(mobilePEDDailySQL.toString(), new Object[]{paramDate,telephone,telephone}, new PediatricsMobileRowMapper());
	     */
	}
	
	@Override
	public void insert(final PediatricsData pediatricsData, final UserInfo operator, final Hospital hospital) throws Exception {
		logger.info(">>PediatricsDAOImpl insert");
		
		final String sql = "insert into tbl_pediatrics_data values(null,NOW(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		dataBean.getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(
					Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, pediatricsData.getHospitalName());
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
				return ps;
			}
		}, keyHolder);
		logger.info("returned id is "+keyHolder.getKey().intValue());
	}
	
	@Override
	public void insert(final PediatricsData pediatricsData, final String dsmCode) throws Exception {
	    logger.info("insert data - daily upload");
	    
	    final String sql = "insert into tbl_pediatrics_data values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)";
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
	
	class PediatricsRowMapper implements RowMapper<PediatricsData>{

        public PediatricsData mapRow(ResultSet rs, int arg1) throws SQLException {
        	PediatricsData pediatricsData = new PediatricsData();
        	pediatricsData.setDataId(rs.getInt("id"));
        	pediatricsData.setCreatedate(rs.getTimestamp("createdate"));
        	pediatricsData.setHospitalCode(rs.getString("hospitalCode"));
        	pediatricsData.setHospitalName(rs.getString("hospitalName"));
        	pediatricsData.setPnum(rs.getInt("pnum"));
        	pediatricsData.setWhnum(rs.getInt("whnum"));
        	pediatricsData.setLsnum(rs.getInt("lsnum"));
        	pediatricsData.setSalesETMSCode(rs.getString("etmsCode"));
        	pediatricsData.setSalesName(rs.getString("operatorName"));
        	pediatricsData.setRegion(rs.getString("region"));
        	pediatricsData.setRsmRegion(rs.getString("rsmRegion"));
        	pediatricsData.setHqd(rs.getDouble("hqd"));
        	pediatricsData.setHbid(rs.getDouble("hbid"));
        	pediatricsData.setOqd(rs.getDouble("oqd"));
        	pediatricsData.setObid(rs.getDouble("obid"));
        	pediatricsData.setTqd(rs.getDouble("tqd"));
        	pediatricsData.setTbid(rs.getDouble("tbid"));
        	pediatricsData.setRecipeType(rs.getString("recipeType"));
        	pediatricsData.setDsmName(rs.getString("dsmName"));
        	pediatricsData.setIsPedAssessed(rs.getString("isPedAssessed"));
            return pediatricsData;
        }
        
    }
	
	public DataBean getDataBean() {
        return dataBean;
    }
    
    public void setDataBean(DataBean dataBean) {
        this.dataBean = dataBean;
    }
}
