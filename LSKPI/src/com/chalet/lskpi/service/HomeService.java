package com.chalet.lskpi.service;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.ExportDoctor;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
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
    
    public List<ExportDoctor> getAllDoctors() throws Exception;
    public List<HomeWeeklyData> getWeeklyDataByRegion(String regionCenter) throws Exception;
}
