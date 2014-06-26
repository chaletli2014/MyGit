package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.UserInfo;

public interface HomeDAO {

    public HomeData getHomeDataByDoctorId(String doctorId, Date startDate, Date endDate) throws Exception;
    public HomeData getHomeDataById(int dataId) throws Exception;
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
}
