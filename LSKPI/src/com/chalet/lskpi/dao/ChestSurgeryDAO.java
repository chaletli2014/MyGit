package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.MobileCHEDailyData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;

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
}
