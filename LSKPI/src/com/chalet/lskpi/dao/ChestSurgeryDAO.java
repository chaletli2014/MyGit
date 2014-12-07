package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.MobileCHEDailyData;
import com.chalet.lskpi.model.MonthlyStatisticsData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyDataOfHospital;
import com.chalet.lskpi.model.WeeklyRatioData;

public interface ChestSurgeryDAO {

    public ChestSurgeryData getChestSurgeryDataByHospital(String hospitalCode) throws Exception;
    public List<ChestSurgeryData> getChestSurgeryDataByDate(Date createdatebegin, Date createdateend) throws Exception;
    public ChestSurgeryData getChestSurgeryDataByHospitalAndDate(String hospitalCode, Date createdate) throws Exception;
    public ChestSurgeryData getChestSurgeryDataById(int id) throws Exception;
    public void insert(ChestSurgeryData chestSurgeryData, UserInfo operator, Hospital hospital) throws Exception;
    public void insert(ChestSurgeryData chestSurgeryData,String dsmCode) throws Exception;
    public void update(ChestSurgeryData chestSurgeryData) throws Exception;
    
    public MobileCHEDailyData getDailyCHEData4CountoryMobile()throws Exception;
    public List<MobileCHEDailyData> getChildDailyCHEData4DSMMobile( String dsmCode ) throws Exception;
    public List<MobileCHEDailyData> getDailyCHEData4DSMMobile( String region ) throws Exception;
    public List<MobileCHEDailyData> getDailyCHEData4RSMMobile( String regionCenter ) throws Exception;
    public List<MobileCHEDailyData> getDailyCHEData4RSDMobile() throws Exception;
    
    public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception;
    
    public List<MobileCHEDailyData> getDailyCHEData4RSMByRegionCenter(String region) throws Exception;
    
    public ReportProcessData getSalesSelfReportProcessData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getSalesSelfReportProcessDetailData(String telephone) throws Exception;
	
	public ReportProcessData getDSMSelfReportProcessData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getDSMSelfReportProcessDetailData(String telephone) throws Exception;
	
	public ReportProcessData getRSMSelfReportProcessData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getRSMSelfReportProcessDetailData(String telephone) throws Exception;
	
	
	public List<WeeklyRatioData> getWeeklyData4DSMMobile( String telephone ) throws Exception;
    public List<WeeklyRatioData> getWeeklyData4RSMMobile( String telephone ) throws Exception;
    public List<WeeklyRatioData> getWeeklyData4RSDMobile() throws Exception;
    
    public WeeklyRatioData getLowerWeeklyData4REPMobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    public WeeklyRatioData getLowerWeeklyData4DSMMobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    public WeeklyRatioData getLowerWeeklyData4RSMMobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    
    public WeeklyRatioData getHospitalWeeklyData4Mobile( String hospitalCode ) throws Exception;
    public WeeklyRatioData getHospitalWeeklyData4Mobile() throws Exception;
    
    
    public int removeOldWeeklyData(String duration) throws Exception;
    public void generateWeeklyDataOfHospital() throws Exception;
    public void generateWeeklyDataOfHospital(Date refreshDate) throws Exception;
    public int getLastWeeklyData() throws Exception;
    public List<WeeklyDataOfHospital> getWeeklyDataOfHospital(Date refreshDate) throws Exception;
    
    public List<MonthlyStatisticsData> getMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public List<MonthlyStatisticsData> getCoreMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public List<MonthlyStatisticsData> getEmergingMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public MonthlyStatisticsData getMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
    public MonthlyStatisticsData getCoreMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
    public MonthlyStatisticsData getEmergingMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
}
