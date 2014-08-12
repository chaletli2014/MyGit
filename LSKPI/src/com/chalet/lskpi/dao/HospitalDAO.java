package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.HospitalSalesQueryObj;
import com.chalet.lskpi.model.HospitalSalesQueryParam;
import com.chalet.lskpi.model.KPIHospital4Export;
import com.chalet.lskpi.model.Monthly12Data;
import com.chalet.lskpi.model.MonthlyData;
import com.chalet.lskpi.model.MonthlyInRateData;
import com.chalet.lskpi.model.MonthlyRatioData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyDataOfHospital;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午5:05:56
 * 类说明
 */

public interface HospitalDAO {

	public Hospital getHospitalByName(String hospitalName) throws Exception;
	
	public Hospital getHospitalByCode(String hospitalCode) throws Exception;
	
	public List<Hospital> getHospitalsByUserTel(String telephone, String department) throws Exception;
	
	public List<Hospital> getMonthlyHospitalsByUserTel(String telephone) throws Exception;
	
	public List<Hospital> getMonthlyHospitalsByDSMTel(String telephone) throws Exception;
	
	public List<Hospital> getHospitalsByDSMTel(String telephone, String department) throws Exception;
	
	public void insert(List<Hospital> hospitals) throws Exception;
	
	public void insertMonthlyData( MonthlyData monthlyData ) throws Exception;
	
	public void delete() throws Exception;
	
	public MonthlyData getMonthlyData( String hospitalCode , Date date) throws Exception;
	
	public MonthlyData getMonthlyDataById( int id ) throws Exception;
	
	public void updateMonthlyData( MonthlyData monthlyData ) throws Exception;
	
	public List<Hospital> getHospitalsByKeywords( String keywords ) throws Exception;
	
	public UserInfo getPrimarySalesOfHospital(String hospitalCode) throws Exception;
	
	public List<MonthlyRatioData> getMonthlyDataOfDSMByRsmRegion(String rsmRegion) throws Exception;
	public List<MonthlyRatioData> getMonthlyDataOfRSMByRegion(String region) throws Exception;
    public List<MonthlyRatioData> getMonthlyDataOfRSD() throws Exception;
    public List<MonthlyRatioData> getMonthlyDataOfREPByDSMCode(String dsmCode) throws Exception;
    
    public MonthlyRatioData getMonthlyDataOfSingleRsm(String rsmRegion) throws Exception;
    public MonthlyRatioData getMonthlyDataOfSingleRsd(String region) throws Exception;
    public MonthlyRatioData getMonthlyDataOfCountory() throws Exception;
    
    public List<Hospital> getAllHospitals() throws Exception;
    
    public List<MonthlyData> getMonthlyDataByDate(Date startDate, Date endDate) throws Exception;
    
    public List<Monthly12Data> getRSD12MontlyDataByRegionCenter(String regionCenter) throws Exception;
    public List<Monthly12Data> getRSM12MontlyDataByRegion(String region) throws Exception;
    public List<Monthly12Data> getDSM12MontlyDataByDSMCode(String dsmCode) throws Exception;
    
    public List<Monthly12Data> get12MontlyDataByCountory() throws Exception;
    
    public List<HospitalSalesQueryObj> getHospitalSalesList(HospitalSalesQueryParam queryParam) throws Exception;
    
    public List<MonthlyInRateData> getMonthlyInRateData(String beginDuraion, String endDuraion, String level) throws Exception;
    
    public List<MonthlyRatioData> getMonthlyCollectionData(Date chooseDate) throws Exception;
    
    public MonthlyRatioData getMonthlyCollectionSumData(Date chooseDate) throws Exception;
    
    public List<Hospital> getHospitalsOfHomeCollectionByPSRTel(String telephone) throws Exception;
    public List<Hospital> getHospitalsOfHomeCollectionByDSMTel(String telephone) throws Exception;
    
    public int removeOldWeeklyHosData(String duration) throws Exception;
	public void generateWeeklyDataOfHospital() throws Exception;
	public void generateWeeklyPEDDataOfHospital(Date refreshDate) throws Exception;
	public int getLastWeeklyData() throws Exception;
	
	public int deleteOldHospitalWeeklyData(String duration) throws Exception;
	
	public int getWeeklyDataIDOfHospital(String duration, String hospitalCode) throws Exception;
	
	public void insertHospitalWeeklyRESData(WeeklyDataOfHospital weeklyData) throws Exception;
	public void updateHospitalWeeklyRESData(WeeklyDataOfHospital weeklyData, int hosWeeklyDataId) throws Exception;
	
	public void insertHospitalWeeklyCHEData(WeeklyDataOfHospital weeklyData) throws Exception;
	public void updateHospitalWeeklyCHEData(WeeklyDataOfHospital weeklyData, int hosWeeklyDataId) throws Exception;
	
	public List<KPIHospital4Export> getKPIHospitalOfPed() throws Exception;
	public List<KPIHospital4Export> getKPIHospitalOfRes() throws Exception;
	public List<KPIHospital4Export> getKPIHospitalOfChe() throws Exception;
	public List<KPIHospital4Export> getKPIHospitalOfMonth() throws Exception;
}
