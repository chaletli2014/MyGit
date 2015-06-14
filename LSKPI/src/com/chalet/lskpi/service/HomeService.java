package com.chalet.lskpi.service;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.DoctorToBeDeleted;
import com.chalet.lskpi.model.ExportDoctor;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.HomeWeeklyNoReportDr;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.UserInfo;

public interface HomeService {

    public HomeData getHomeDataByDoctorId(String doctorId) throws Exception;
    public List<HomeData> getHomeDataByDate(Date startDate, Date endDate) throws Exception;
    public HomeData getHomeDataById(int dataId) throws Exception;
    public void insert(HomeData homeData,String doctorId) throws Exception;
    public void update(HomeData homeData) throws Exception;
    
    public List<HomeWeeklyData> getHomeWeeklyDataOfCurrentUser(UserInfo currentUser) throws Exception;
    public List<HomeWeeklyData> getHomeWeeklyDataOfLowerUser(UserInfo currentUser) throws Exception;
    public HomeWeeklyData getHomeWeeklyDataOfUpperUser(UserInfo currentUser) throws Exception;
    
    /**
     * 获取当前用户的儿科家庭雾化周报
     * @param currentUser
     * @param pedType 儿科科室，门急诊还是病房
     * @return
     * @throws Exception
     */
    public List<HomeWeeklyData> getPedHomeWeeklyDataOfCurrentUser(UserInfo currentUser, String pedType) throws Exception;
    
    /**
     * 获取当前用户下级的儿科家庭雾化周报
     * @param currentUser
     * @param pedType 儿科科室，门急诊还是病房
     * @return
     * @throws Exception
     */
    public List<HomeWeeklyData> getPedHomeWeeklyDataOfLowerUser(UserInfo currentUser, String pedType) throws Exception;
    
    /**
     * 获取当前用户上级的儿科家庭雾化周报
     * @param currentUser
     * @param pedType 儿科科室，门急诊还是病房
     * @return
     * @throws Exception
     */
    public HomeWeeklyData getPedHomeWeeklyDataOfUpperUser(UserInfo currentUser, String pedType) throws Exception;
    
    /**
     * 获取RSD下所有RSM的儿科家庭雾化周报
     * @param regionCenter
     * @return
     * @throws Exception
     */
    public List<HomeWeeklyData> getPedHomeWeeklyDataByRegion(String regionCenter, String pedType) throws Exception;
    
    public List<ExportDoctor> getAllDoctors() throws Exception;
    public List<HomeWeeklyData> getWeeklyDataByRegion(String regionCenter) throws Exception;
    public List<HomeWeeklyData> getWeeklyDataByRegion(String regionCenter, Date beginDate) throws Exception;
    public List<HomeWeeklyNoReportDr> getWeeklyNoReportDr(Date beginDate) throws Exception;
    
    public ReportProcessData getSalesSelfReportProcess(String telephone) throws Exception;
    public ReportProcessData getDSMSelfReportProcess(String telephone) throws Exception;
    public ReportProcessData getRSMSelfReportProcess(String telephone) throws Exception;
    
    public void backupDoctors(int dayInWeek) throws Exception;
    public boolean isAlreadyBackup() throws Exception;
    public void removeOldDoctors(int dayinweek) throws Exception;
    
    public List<DoctorToBeDeleted> getAllDoctorsToBeDeleted() throws Exception;
    public void storeToBeDeletedDoctor(DoctorToBeDeleted doctor,String currentUserTel) throws Exception;
    public String getDeleteReasonByDrId(int drId) throws Exception;
    public void rejectRemovingDoctor(Doctor doctor, String currentUserTel) throws Exception;
    
    public Doctor getDoctorByDoctorNameAndHospital(String doctorName,String hospitalCode) throws Exception;
}
