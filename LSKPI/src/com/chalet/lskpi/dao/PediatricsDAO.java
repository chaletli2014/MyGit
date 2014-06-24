package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;

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

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:53:37
 * 类说明
 */

public interface PediatricsDAO {

    public PediatricsData getPediatricsDataByHospital(String hospitalName) throws Exception;
    public PediatricsData getPediatricsDataByHospitalAndDate(String hospitalName, Date createdate) throws Exception;
    public List<PediatricsData> getPediatricsDataByDate(Date createdatebegin, Date createdateend) throws Exception;
    public PediatricsData getPediatricsDataById(int id) throws Exception;
	public void insert(PediatricsData pediatricsData, UserInfo operator, Hospital hospital) throws Exception;
	public void insert(PediatricsData pediatricsData, String dsmCode) throws Exception;
	public void update(PediatricsData pediatricsData, UserInfo operator) throws Exception;
	
	public MobilePEDDailyData getDailyPEDData4CountoryMobile()throws Exception;
	
	public List<MobilePEDDailyData> getDailyPEDData4DSMMobile( String telephone ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDData4RSMMobile( String telephone ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDData4RSDMobile() throws Exception;
	public List<MobilePEDDailyData> getDailyPEDChildData4DSMMobile( String telephone ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDChildData4RSMMobile( String telephone ) throws Exception;
	public List<MobilePEDDailyData> getDailyPEDChildData4RSDMobile( String telephone ) throws Exception;
	
	public List<DailyReportData> getAllRSMDataByTelephone() throws Exception;
	
	public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception;
	
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
    
    public List<MobilePEDDailyData> getDailyPEDData4RSMByRegion(String region) throws Exception;
}
