package com.chalet.lskpi.service;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobileCHEDailyData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;

public interface ChestSurgeryService {

    public ChestSurgeryData getChestSurgeryDataByHospital(String hospitalCode) throws Exception;
    public List<ChestSurgeryData> getChestSurgeryDataByDate(Date createdatebegin, Date createdateend) throws Exception;
    public ChestSurgeryData getChestSurgeryDataByHospitalAndDate(String hospitalCode, Date createdate) throws Exception;
    public ChestSurgeryData getChestSurgeryDataById(int id) throws Exception;
    public void insert(ChestSurgeryData chestSurgeryData, UserInfo operator, Hospital hospital) throws Exception;
    public void insert(ChestSurgeryData chestSurgeryData) throws Exception;
    public void update(ChestSurgeryData chestSurgeryData, UserInfo operator) throws Exception;
    
    public MobileCHEDailyData getDailyCHEParentData4Mobile(String telephone, String level)throws Exception;
    public List<MobileCHEDailyData> getDailyCHEData4Mobile( String telephone, UserInfo currentUser ) throws Exception;
    public List<MobileCHEDailyData> getDailyCHEChildData4Mobile( String telephone, UserInfo currentUser ) throws Exception;
    
    public List<MobileCHEDailyData> getDailyCHEData4MobileByRegionCenter(String regionCenter) throws Exception;
    
    public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception;
    

	public ReportProcessData getSalesSelfReportProcessData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getSalesSelfReportProcessDetailData(String telephone) throws Exception;
	
	public ReportProcessData getDSMSelfReportProcessData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getDSMSelfReportProcessDetailData(String telephone) throws Exception;
	
	public ReportProcessData getRSMSelfReportProcessData(String telephone) throws Exception;
	public List<ReportProcessDataDetail> getRSMSelfReportProcessDetailData(String telephone) throws Exception;
}
