package com.chalet.lskpi.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobilePEDDailyData;
import com.chalet.lskpi.model.MonthlyStatisticsData;
import com.chalet.lskpi.model.PediatricsData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyRatioData;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:50:47
 * 类说明
 */

public interface PediatricsService {

	public PediatricsData getPediatricsDataByHospital(String hospitalCode) throws Exception;
	public PediatricsData getPediatricsRoomDataByHospital(String hospitalCode) throws Exception;
	public PediatricsData getPediatricsDataByHospitalAndDate(String hospitalName, Date createdate) throws Exception;
	public List<PediatricsData> getPediatricsDataByDate(Date createdatebegin, Date createdateend) throws Exception;
	public PediatricsData getPediatricsDataById(int id) throws Exception;
	public void insert(PediatricsData pediatricsData, UserInfo operator, Hospital hospital) throws Exception;
	public void insert(PediatricsData pediatricsData) throws Exception;
	public void update(PediatricsData pediatricsData, UserInfo operator) throws Exception;
	public void insertRoomData(PediatricsData pediatricsData, UserInfo operator, Hospital hospital) throws Exception;
	public void updateRoomData(PediatricsData pediatricsData, UserInfo operator) throws Exception;
	
	public MobilePEDDailyData getDailyPEDParentData4Mobile(String telephone, String level, String hospitalShownFlag)throws Exception;
	public List<MobilePEDDailyData> getDailyPEDData4Mobile( String telephone, UserInfo currentUser, String hospitalShownFlag) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDChildData4Mobile( String telephone, UserInfo currentUser, String hospitalShownFlag) throws Exception;
	
	public TopAndBottomRSMData getTopAndBottomInRateRSMData(String telephone, String hospitalShownFlag) throws Exception;
	public TopAndBottomRSMData getTopAndBottomWhRateRSMData(String telephone, String hospitalShownFlag) throws Exception;
	public TopAndBottomRSMData getTopAndBottomAverageRSMData(String telephone, String hospitalShownFlag) throws Exception;
	
	public TopAndBottomRSMData getTopAndBottomRSMData(Timestamp paramDate, String hospitalShownFlag) throws Exception;
	
	public ReportProcessData getSalesSelfReportProcessPEDData(String telephone) throws Exception;
    public List<ReportProcessDataDetail> getSalesSelfReportProcessPEDDetailData(String telephone) throws Exception;
    
    public ReportProcessData getDSMSelfReportProcessPEDData(String telephone) throws Exception;
    public List<ReportProcessDataDetail> getDSMSelfReportProcessPEDDetailData(String telephone) throws Exception;
    
    public ReportProcessData getRSMSelfReportProcessPEDData(String telephone) throws Exception;
    public List<ReportProcessDataDetail> getRSMSelfReportProcessPEDDetailData(String telephone) throws Exception;
    
    public List<WeeklyRatioData> getWeeklyPEDData4Mobile( String telephone ) throws Exception;
    public WeeklyRatioData getWeeklyPEDCountoryData4Mobile() throws Exception;
    public WeeklyRatioData getHospitalWeeklyPEDData4Mobile( String hospitalCode ) throws Exception;
    public WeeklyRatioData getLowerWeeklyPEDData4Mobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    
    public void generateWeeklyPEDDataOfHospital() throws Exception;
    public int removeOldWeeklyPEDData(String duration) throws Exception;
    public void generateWeeklyPEDDataOfHospital(Date refreshDate) throws Exception;
    public boolean hasLastWeeklyPEDData() throws Exception;
    
    public List<MobilePEDDailyData> getDailyPEDData4MobileByRegion(Timestamp paramDate, String region, String hospitalShownFlag) throws Exception;
    
    public List<MonthlyStatisticsData> getMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public MonthlyStatisticsData getMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
    public MonthlyStatisticsData getCoreOrEmergingMonthlyStatisticsCountryData(String beginDuraion, String endDuraion, String dragonType) throws Exception;
    public Map<String,MonthlyStatisticsData> getCoreOrEmergingMonthlyStatisticsData(String beginDuraion, String endDuraion, String level, String dragonType) throws Exception;
}
