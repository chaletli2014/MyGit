package com.chalet.lskpi.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:53:37
 * 类说明
 */

public interface PediatricsDAO {

    public PediatricsData getPediatricsDataByHospital(String hospitalCode) throws Exception;
    public PediatricsData getPediatricsRoomDataByHospital(String hospitalCode) throws Exception;
    /**
     * 获取当周参数医院对应的儿科的最新一条数据
     * @param hospitalCode
     * @return
     * @throws Exception
     */
    public PediatricsData getPediatricsHomeDataByHospital(String hospitalCode) throws Exception;
    /**
     * 获取当周参数医院对应的儿科已经录入过的家庭雾化数据
     * @param hospitalCode
     * @return
     * @throws Exception
     */
    public PediatricsData getExistsPediatricsHomeDataByHospital(String hospitalCode) throws Exception;
    public PediatricsData getPediatricsDataByHospitalAndDate(String hospitalName, Date createdate) throws Exception;
    public List<PediatricsData> getPediatricsDataByDate(Date createdatebegin, Date createdateend) throws Exception;
    public PediatricsData getPediatricsDataById(int id) throws Exception;
	public void insert(PediatricsData pediatricsData, UserInfo operator, Hospital hospital) throws Exception;
	public void insert(PediatricsData pediatricsData, String dsmCode) throws Exception;
	public void update(PediatricsData pediatricsData, UserInfo operator) throws Exception;
	public void insertRoomData(PediatricsData pediatricsData, UserInfo operator, Hospital hospital) throws Exception;
	public void updateRoomData(PediatricsData pediatricsData, UserInfo operator) throws Exception;
	public void updateHomeData(PediatricsData pediatricsData,UserInfo operator)throws Exception;
	
	public MobilePEDDailyData getDailyPEDData4CountoryMobile(Timestamp paramDate, String hospitalShownFlag)throws Exception;
	public List<MobilePEDDailyData> getDailyPEDData4RSMByRegion(String region, String hospitalShownFlag) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDData4DSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDData4RSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDData4RSDMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDChildData4DSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDChildData4RSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDChildData4RSDMobile( String telephone, String hospitalShownFlag ) throws Exception;
	
	public MobilePEDDailyData getDailyPEDWhPortData4CountoryMobile(Timestamp paramDate, String hospitalShownFlag)throws Exception;
	public List<MobilePEDDailyData> getDailyPEDWhPortData4RSMByRegion(String region, String hospitalShownFlag) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDWhPortData4DSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDWhPortData4RSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDWhPortData4RSDMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDWhPortChildData4DSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDWhPortChildData4RSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDWhPortChildData4RSDMobile( String telephone, String hospitalShownFlag ) throws Exception;
	
	public MobilePEDDailyData getDailyCorePEDData4CountoryMobile(Timestamp paramDate, String hospitalShownFlag)throws Exception;
	public List<MobilePEDDailyData> getDailyCorePEDData4RSMByRegion(String region, String hospitalShownFlag) throws Exception;
	public List<MobilePEDDailyData> getDailyCorePEDData4DSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyCorePEDData4RSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyCorePEDData4RSDMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception;
	public List<MobilePEDDailyData> getDailyCorePEDChildData4DSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyCorePEDChildData4RSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyCorePEDChildData4RSDMobile( String telephone, String hospitalShownFlag ) throws Exception;
	
	public MobilePEDDailyData getDailyEmergingPEDData4CountoryMobile(Timestamp paramDate, String hospitalShownFlag)throws Exception;
	public List<MobilePEDDailyData> getDailyEmergingPEDData4RSMByRegion(String region, String hospitalShownFlag) throws Exception;
	
	public List<MobilePEDDailyData> getDailyEmergingPEDData4DSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyEmergingPEDData4RSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyEmergingPEDData4RSDMobile(Timestamp paramDate, String hospitalShownFlag) throws Exception;
	
	public List<MobilePEDDailyData> getDailyEmergingPEDChildData4DSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyEmergingPEDChildData4RSMMobile( String telephone, String hospitalShownFlag ) throws Exception;
	public List<MobilePEDDailyData> getDailyEmergingPEDChildData4RSDMobile( String telephone, String hospitalShownFlag ) throws Exception;
	
	public List<DailyReportData> getAllRSMDataByTelephone(String hospitalShownFlag) throws Exception;
	
	public TopAndBottomRSMData getTopAndBottomInRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	public TopAndBottomRSMData getTopAndBottomWhRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	public TopAndBottomRSMData getTopAndBottomAverageDoseRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	public TopAndBottomRSMData getCoreTopAndBottomRSMData(String hospitalShownFlag) throws Exception;
	public TopAndBottomRSMData getCoreTopAndBottomRSMWhRateData(String hospitalShownFlag) throws Exception;
	
	/**
	 * 获取全国最大最小RSM Room医院平均剂量指标
	 * getRoomTopAndBottomAverageDoseRSMData
	 * @param rsmData rsmData
	 * @param hospitalShownFlag hospitalShownFlag
	 * @param paramDate paramDate
	 * @return TopAndBottomRSMData
	 * @throws Exception Exception
	 */
	public TopAndBottomRSMData getRoomTopAndBottomAverageDoseRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	
	/**
	 * 获取全国最大最小RSM Room天数指标
	 * getRoomTopAndBottomWhDaysRSMData
	 * @param rsmData rsmData
	 * @param hospitalShownFlag hospitalShownFlag
	 * @param paramDate paramDate
	 * @return TopAndBottomRSMData
	 * @throws Exception Exception
	 */
	public TopAndBottomRSMData getRoomTopAndBottomWhDaysRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	
	/**
	 * 获取全国最大最小RSM Room博令人次比指标
	 * getRoomTopAndBottomBlRateRSMData
	 * @param rsmData rsmData
	 * @param hospitalShownFlag hospitalShownFlag
	 * @param paramDate paramDate
	 * @return TopAndBottomRSMData
	 * @throws Exception Exception
	 */
	public TopAndBottomRSMData getRoomTopAndBottomBlRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	/**
	 * 获取全国最大最小RSM Room赠卖泵数量指标
	 * getRoomTopAndBottomHomeWhNum1RSMData
	 * @param rsmData rsmData
	 * @param hospitalShownFlag hospitalShownFlag
	 * @param paramDate paramDate
	 * @return TopAndBottomRSMData
	 * @throws Exception Exception
	 */
	public TopAndBottomRSMData getRoomTopAndBottomHomeWhNum1RSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	/**
	 * 获取全国最大最小RSM Room平均带药天数指标
	 * getRoomTopAndBottomAverDaysRSMData
	 * @param rsmData rsmData
	 * @param hospitalShownFlag hospitalShownFlag
	 * @param paramDate paramDate
	 * @return TopAndBottomRSMData
	 * @throws Exception Exception
	 */
	public TopAndBottomRSMData getRoomTopAndBottomAverDaysRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	/**
	 * 获取全国最大最小RSM Room总带药支数指标
	 * getRoomTopAndBottomHomeWhNum4RSMData
	 * @param rsmData rsmData
	 * @param hospitalShownFlag hospitalShownFlag
	 * @param paramDate paramDate
	 * @return TopAndBottomRSMData
	 * @throws Exception Exception
	 */
	public TopAndBottomRSMData getRoomTopAndBottomHomeWhNum4RSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	
	/**
	 * 获取全国最大最小RSM Emerging医院雾化率指标
	 * @return TopAndBottomRSMData
	 * @param hospitalShownFlag 判断取所有KPI还是重点KPI
	 * @throws Exception
	 */
	public TopAndBottomRSMData getEmergingTopAndBottomRSMWhRateData(String hospitalShownFlag) throws Exception;
	
	/**
	 * 获取全国最大最小RSM 病房雾化率指标
	 * @return TopAndBottomRSMData
	 * @param hospitalShownFlag 判断取所有KPI还是重点KPI
	 * @throws Exception
	 */
	public TopAndBottomRSMData getRoomTopAndBottomRSMWhRateData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	
	/**
	 * 获取全国最大最小RSM 雾化端口使用率指标
	 * @param rsmData
	 * @return TopAndBottomRSMData
	 * @throws Exception
	 */
	public TopAndBottomRSMData getTopAndBottomWhPortRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag, Timestamp paramDate) throws Exception;
	
	/**
	 * 获取全国最大最小RSM 天数
	 * @param rsmData rsmData
	 * @return TopAndBottomRSMData
	 * @throws Exception
	 */
	public TopAndBottomRSMData getTopAndBottomWhDaysRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception;
	
	/**
	 * 获取全国最大最小RSM 博令人次比指标
	 * @param rsmData rsmData
	 * @return TopAndBottomRSMData
	 * @throws Exception
	 */
	public TopAndBottomRSMData getTopAndBottomBlRateRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception;
	/**
	 * 获取全国最大最小RSM 赠卖泵数量
	 * @param rsmData rsmData
	 * @return TopAndBottomRSMData
	 * @throws Exception
	 */
	public TopAndBottomRSMData getTopAndBottomHomeWhNum1RSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception;
	
	/**
	 * 获取全国最大最小RSM 平均带药天数
	 * @param rsmData rsmData
	 * @return TopAndBottomRSMData
	 * @throws Exception
	 */
	public TopAndBottomRSMData getTopAndBottomAverDaysRSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception;
	
	/**
	 * 获取全国最大最小RSM 总带药支数
	 * @param rsmData rsmData
	 * @return TopAndBottomRSMData
	 * @throws Exception
	 */
	public TopAndBottomRSMData getTopAndBottomHomeWhNum4RSMData(TopAndBottomRSMData rsmData, String hospitalShownFlag) throws Exception;
	
	public void generateWeeklyPEDDataOfHospital() throws Exception;
	public int removeOldWeeklyPEDData(String duration) throws Exception;
	public void generateWeeklyPEDDataOfHospital(Date refreshDate) throws Exception;
	public int getLastWeeklyPEDData() throws Exception;
	
	public ReportProcessData getSalesSelfReportProcessPEDData(String telephone) throws Exception;
    public List<ReportProcessDataDetail> getSalesSelfReportProcessPEDDetailData(String telephone) throws Exception;
    
    public ReportProcessData getDSMSelfReportProcessPEDData(String telephone) throws Exception;
    public List<ReportProcessDataDetail> getDSMSelfReportProcessPEDDetailData(String telephone) throws Exception;
    
    public ReportProcessData getRSMSelfReportProcessPEDData(String telephone) throws Exception;
    public List<ReportProcessDataDetail> getRSMSelfReportProcessPEDDetailData(String telephone) throws Exception;
    
    public List<WeeklyRatioData> getWeeklyPEDData4DSMMobile( String telephone ) throws Exception;
    public List<WeeklyRatioData> getWeeklyPEDData4RSMMobile( String telephone ) throws Exception;
    public List<WeeklyRatioData> getWeeklyPEDData4RSDMobile() throws Exception;
    
    public WeeklyRatioData getLowerWeeklyPEDData4REPMobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    public WeeklyRatioData getLowerWeeklyPEDData4DSMMobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    public WeeklyRatioData getLowerWeeklyPEDData4RSMMobile( UserInfo currentUser, String lowerUserCode ) throws Exception;
    
    public WeeklyRatioData getHospitalWeeklyPEDData4Mobile( String hospitalCode ) throws Exception;
    public WeeklyRatioData getWeeklyPEDCountoryData4Mobile() throws Exception;
    
    public void updatePEDUserCodes(List<UserCode> userCodes) throws Exception;
    
    public List<MonthlyStatisticsData> getMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public List<MonthlyStatisticsData> getCoreMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public List<MonthlyStatisticsData> getEmergingMonthlyStatisticsData(String beginDuraion, String endDuraion, String level) throws Exception;
    public MonthlyStatisticsData getMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
    public MonthlyStatisticsData getCoreMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
    public MonthlyStatisticsData getEmergingMonthlyStatisticsCountryData(String beginDuraion, String endDuraion) throws Exception;
    /**
	 * 查询儿科门急诊周数据
	 */
    public List<MobilePEDDailyData> getWeeklyPediatricsEmergingDatas(String duration,String level) throws Exception;
    /**
	 * 查询儿科家庭雾化周数据
	 */
    public List<MobilePEDDailyData> getWeeklyPEDHomeDatas(Date beginDate, Date endDate,String level,String department) throws Exception;
    /**
	 * 查询儿科病房周数据
	 */
    public List<MobilePEDDailyData> getWeeklyPEDRoomDatas(Date beginDate, Date endDate,String level) throws Exception;
    
}