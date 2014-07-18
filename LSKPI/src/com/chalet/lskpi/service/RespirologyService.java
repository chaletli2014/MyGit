package com.chalet.lskpi.service;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobileRESDailyData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.RespirologyData;
import com.chalet.lskpi.model.RespirologyExportData;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyRatioData;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:50:47
 * 类说明
 */

public interface RespirologyService {

	public RespirologyData getRespirologyDataByHospital(String hospitalName) throws Exception;
	public List<RespirologyData> getRespirologyDataByDate(Date createdatebegin, Date createdateend) throws Exception;
	public RespirologyData getRespirologyDataByHospitalAndDate(String hospitalName, Date createdate) throws Exception;
	public RespirologyData getRespirologyDataById(int id) throws Exception;
	public void insert(RespirologyData respirologyData, UserInfo operator, Hospital hospital) throws Exception;
	public void insert(RespirologyData respirologyData) throws Exception;
	public void update(RespirologyData respirologyData, UserInfo operator) throws Exception;
	
	public MobileRESDailyData getDailyRESParentData4Mobile(String telephone, String level)throws Exception;
	public List<MobileRESDailyData> getDailyRESData4Mobile( String telephone, UserInfo currentUser ) throws Exception;
	public List<MobileRESDailyData> getDailyRESChildData4Mobile( String telephone, UserInfo currentUser ) throws Exception;
	
	public TopAndBottomRSMData getTopAndBottomInRateRSMData(String telephone) throws Exception;
	public TopAndBottomRSMData getTopAndBottomWhRateRSMData(String telephone) throws Exception;
	public TopAndBottomRSMData getTopAndBottomAverageRSMData(String telephone) throws Exception;
	
	public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception;
	
	public List<ReportProcessData> getReportProcessRESDataByUserTel( String telephone ) throws Exception;
	
	public ReportProcessData getSalesSelfReportProcessRESData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getSalesSelfReportProcessRESDetailData(String telephone) throws Exception;
	
	public ReportProcessData getDSMSelfReportProcessRESData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getDSMSelfReportProcessRESDetailData(String telephone) throws Exception;
	
	public ReportProcessData getRSMSelfReportProcessRESData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getRSMSelfReportProcessRESDetailData(String telephone) throws Exception;
	
	public List<WeeklyRatioData> getWeeklyRESData4Mobile( String telephone ) throws Exception;
	public WeeklyRatioData getWeeklyRESCountoryData4Mobile() throws Exception;
	public WeeklyRatioData getHospitalWeeklyRESData4Mobile( String hospitalCode ) throws Exception;
	public WeeklyRatioData getLowerWeeklyRESData4Mobile( UserInfo currentUser, String lowerUserCode ) throws Exception;

	public int removeOldWeeklyRESData(String duration) throws Exception;
	public void generateWeeklyRESDataOfHospital() throws Exception;
    public void generateWeeklyRESDataOfHospital(Date refreshDate) throws Exception;
    
    public boolean hasLastWeeklyRESData() throws Exception;
    
    public List<MobileRESDailyData> getDailyRESData4MobileByRegion(String region) throws Exception;
    
    public List<RespirologyExportData> getResMonthExportData() throws Exception;
}
