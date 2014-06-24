package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.UserInfo;

public interface HomeDAO {

    public HomeData getHomeDataByDoctorId(String doctorId) throws Exception;
    public HomeData getHomeDataById(int dataId) throws Exception;
    public void insert(HomeData homeData,String doctorId) throws Exception;
    public void update(HomeData homeData) throws Exception;
    
    public List<HomeWeeklyData> getHomeWeeklyDataOfSales(UserInfo currentUser,Date lastWednesday) throws Exception;
    public List<HomeWeeklyData> getHomeWeeklyDataOfDSM(UserInfo currentUser,Date lastWednesday) throws Exception;
    public List<HomeWeeklyData> getHomeWeeklyDataOfRSM(UserInfo currentUser,Date lastWednesday) throws Exception;
    public List<HomeWeeklyData> getHomeWeeklyDataOfRSD(Date lastWednesday) throws Exception;
}
