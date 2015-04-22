package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:53:37
 * 类说明
 */

public interface RespirologyDAO {

    public RespirologyData getRespirologyDataByHospital(String hospitalName) throws Exception;
    public List<RespirologyData> getRespirologyDataByDate(Date createdatebegin, Date createdateend) throws Exception;
    public RespirologyData getRespirologyDataByHospitalAndDate(String hospitalName, Date createdate) throws Exception;
    public RespirologyData getRespirologyDataById(int id) throws Exception;
	public void insert(RespirologyData respirologyData, UserInfo operator, Hospital hospital) throws Exception;
	public void insert(RespirologyData respirologyData,String dsmCode) throws Exception;
	public void update(RespirologyData respirologyData, UserInfo operator) throws Exception;
	
	public MobileRESDailyData getDailyRESData4CountoryMobile()throws Exception;
	
	public List<MobileRESDailyData> getDailyRESData4DSMMobile( String telephone ) throws Exception;
	public List<MobileRESDailyData> getDailyRESData4RSMMobile( String telephone ) throws Exception;
	public List<MobileRESDailyData> getDailyRESData4RSDMobile() throws Exception;
	
	public List<MobileRESDailyData> getDailyRESChildData4DSMMobile( String telephone ) throws Exception;
	public List<MobileRESDailyData> getDailyRESChildData4RSMMobile( String telephone ) throws Exception;
	public List<MobileRESDailyData> getDailyRESChildData4RSDMobile( String telephone ) throws Exception;
	
	public List<DailyReportData> getAllRSMDataByTelephone() throws Exception;
	
	public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception;
	
	public int removeOldWeeklyRESData(String duration) throws Exception;
	public void generateWeeklyRESDataOfHospital() throws Exception;
	public void generateWeeklyRESDataOfHospital(Date refreshDate) throws Exception;
	public int getLastWeeklyRESData() throws Exception;
	
	public List<ReportProcessData> getReportProcessRESDataByUserTel( String telephone ) throws Exception;
	
	public ReportProcessData getSalesSelfReportProcessRESData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getSalesSelfReportProcessRESDetailData(String telephone) throws Exception;
	
	public ReportProcessData getDSMSelfReportProcessRESData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getDSMSelfReportProcessRESDetailData(String telephone) throws Exception;
	
	public ReportProcessData getRSMSelfReportProcessRESData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getRSMSelfReportProcessRESDetailData(String telephone) throws Exception;
	
	public List<ReportProcessData> getDSMReportProcessRESDataOfRSM(String rsmRegion) throws Exception;
	
	public List<WeeklyRatioData> getWeeklyRESData4DSMMobile( String telephone ) throws Exception;
    public List<WeeklyRatioData> getWeeklyRESData4RSMMobile( String telephone ) throws Exception;
    public List<WeeklyRatioData> getWeeklyRESData4RSDMobile() throws Exception;
    
    public WeeklyRatioData getLowerWeeklyRESData4REPMobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    public WeeklyRatioData getLowerWeeklyRESData4DSMMobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    public WeeklyRatioData getLowerWeeklyRESData4RSMMobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    
    public WeeklyRatioData getHospitalWeeklyRESData4Mobile( String hospitalCode ) throws Exception;
    public WeeklyRatioData getHospitalWeeklyRESData4Mobile() throws Exception;
    
    public void updateRESUserCodes(List<UserCode> userCodes) throws Exception;
    
    public List<MobileRESDailyData> getDailyRESData4RSMByRegion(String region) throws Exception;
    
    public List<RespirologyMonthDBData> getRESMonthReportDBData(String lastMonthDuration, String isRe2) throws Exception;
    public List<RespirologyMonthDBData> getRESMonthReportDBDataOfCountry(String lastMonthDuration, String isRe2) throws Exception;
    public List<RespirologyMonthDBData> getRESMonthReportWeeklyDBData(String lastWeekDuration, String isRe2) throws Exception;
    public List<RespirologyMonthDBData> getRESMonthReportWeeklyDBDataOfCountry(String lastWeekDuration, String isRe2) throws Exception;
    

    public List<RespirologyMonthDBData> getRESMonthReportRSDData(String lastMonthDuration, String isRe2) throws Exception;
    public List<RespirologyMonthDBData> getRESMonthReportRSDDataOfCountry(String lastMonthDuration, String isRe2) throws Exception;
    public List<RespirologyMonthDBData> getRESMonthReportWeeklyRSDData(String lastWeekDuration, String isRe2) throws Exception;
    public List<RespirologyMonthDBData> getRESMonthReportWeeklyRSDDataOfCountry(String lastWeekDuration, String isRe2) throws Exception;
    

    public List<RespirologyMonthDBData> getRESMonthReportDSMData(String lastMonthDuration, String isRe2) throws Exception;
    public List<RespirologyMonthDBData> getRESMonthReportDSMDataOfCountry(String lastMonthDuration, String isRe2) throws Exception;
    public List<RespirologyMonthDBData> getRESMonthReportWeeklyDSMData(String lastWeekDuration, String isRe2) throws Exception;
    public List<RespirologyMonthDBData> getRESMonthReportWeeklyDSMDataOfCountry(String lastWeekDuration, String isRe2) throws Exception;
    
    
    public String getLatestDuration() throws Exception;
    
    public List<WeeklyDataOfHospital> getWeeklyDataOfHospital(Date refreshDate) throws Exception;
    
    public RespirologyLastWeekData getLastWeekData(String rsmRegion, String rsmName, String isRe2, String level) throws Exception;
    
    public List<MonthlyStatisticsData> getMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public List<MonthlyStatisticsData> getCoreMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public List<MonthlyStatisticsData> getEmergingMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public MonthlyStatisticsData getMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
    public MonthlyStatisticsData getCoreMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
    public MonthlyStatisticsData getEmergingMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
    
    public List<MonthlyStatisticsData> getCoreAverageDoseMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public MonthlyStatisticsData getCoreAverageDoseMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
}
