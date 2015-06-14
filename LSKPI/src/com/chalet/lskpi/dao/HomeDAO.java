package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.ExportDoctor;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.HomeWeeklyNoReportDr;
import com.chalet.lskpi.model.ReportProcessData;

public interface HomeDAO {

    public HomeData getHomeDataByDoctorId(String doctorId, Date startDate, Date endDate) throws Exception;
    public HomeData getHomeDataById(int dataId) throws Exception;
    public List<HomeData> getHomeDataByDate(Date startDate, Date endDate) throws Exception;
    public void insert(HomeData homeData,String doctorId) throws Exception;
    public void update(HomeData homeData) throws Exception;
    
    public List<HomeWeeklyData> getHomeWeeklyDataOfSales(String dsmCode, String region,Date beginDate, Date endDate) throws Exception;
    public List<HomeWeeklyData> getHomeWeeklyDataOfDSM(String region, Date beginDate, Date endDate) throws Exception;
    public List<HomeWeeklyData> getHomeWeeklyDataOfRSM(String regionCenter, Date beginDate, Date endDate) throws Exception;
    public List<HomeWeeklyData> getHomeWeeklyDataOfRSD(Date beginDate, Date endDate) throws Exception;
    
    public HomeWeeklyData getHomeWeeklyDataOfCountory(Date beginDate, Date endDate) throws Exception;
    public HomeWeeklyData getHomeWeeklyDataOfSingleRSD(String regionCenter, Date beginDate, Date endDate) throws Exception;
    public HomeWeeklyData getHomeWeeklyDataOfSingleRSM(String region, Date beginDate, Date endDate) throws Exception;
    public HomeWeeklyData getHomeWeeklyDataOfSingleDSM(String dsmCode, String region, Date beginDate, Date endDate) throws Exception;
    
    /**
     * 获取儿科门急诊家庭雾化周报数据
     * @param dsmCode
     * @param region
     * @param beginDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public List<HomeWeeklyData> getPedHomeWeeklyDataOfSales(String pedType, String dsmCode, String region,Date beginDate, Date endDate) throws Exception;
    public List<HomeWeeklyData> getPedHomeWeeklyDataOfDSM(String pedType, String region, Date beginDate, Date endDate) throws Exception;
    public List<HomeWeeklyData> getPedHomeWeeklyDataOfRSM(String pedType, String regionCenter, Date beginDate, Date endDate) throws Exception;
    public List<HomeWeeklyData> getPedHomeWeeklyDataOfRSD(String pedType, Date beginDate, Date endDate) throws Exception;
    
    public HomeWeeklyData getPedHomeWeeklyDataOfCountory(String pedType, Date beginDate, Date endDate) throws Exception;
    public HomeWeeklyData getPedHomeWeeklyDataOfSingleRSD(String pedType, String regionCenter, Date beginDate, Date endDate) throws Exception;
    public HomeWeeklyData getPedHomeWeeklyDataOfSingleRSM(String pedType, String region, Date beginDate, Date endDate) throws Exception;
    public HomeWeeklyData getPedHomeWeeklyDataOfSingleDSM(String pedType, String dsmCode, String region, Date beginDate, Date endDate) throws Exception;
    
    
    public List<HomeWeeklyNoReportDr> getWeeklyNoReportDr(Date beginDate, Date endDate, String duration) throws Exception;
    
    public List<ExportDoctor> getAllDoctors() throws Exception;
    
    
    public ReportProcessData getSalesSelfReportProcess(String telephone) throws Exception;
    public ReportProcessData getDSMSelfReportProcess(String telephone) throws Exception;
    public ReportProcessData getRSMSelfReportProcess(String telephone) throws Exception;
    
    public void backupDoctors(String duration) throws Exception;
    public boolean isAlreadyBackup(String duration) throws Exception;
    public void removeOldDoctors(String duration) throws Exception;
}
