package com.chalet.lskpi.service;

import java.util.List;

import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.UserInfo;

public interface HomeService {

    public HomeData getHomeDataByDoctorId(String doctorId) throws Exception;
    public HomeData getHomeDataById(int dataId) throws Exception;
    public void insert(HomeData homeData,String doctorId) throws Exception;
    public void update(HomeData homeData) throws Exception;
    
    public List<HomeWeeklyData> getHomeWeeklyDataOfCurrentUser(UserInfo currentUser) throws Exception;
}
