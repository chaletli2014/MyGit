package com.chalet.lskpi.service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.chalet.lskpi.dao.HomeDAO;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;

@Service("homeService")
public class HomeServiceImpl implements HomeService {

    @Autowired
    @Qualifier("homeDAO")
    private HomeDAO homeDAO;
    
    Logger logger = Logger.getLogger(HomeServiceImpl.class);
    
    public HomeData getHomeDataByDoctorId(String doctorId) throws Exception {
        try{
            Date beginDate = DateUtils.getTheBeginDateOfCurrentWeek();
            Date endDate = DateUtils.getGenerateWeeklyReportDate(new Date());
            endDate = new Date(endDate.getTime() + 1 * 24 * 60 * 60 * 1000);
            return homeDAO.getHomeDataByDoctorId(doctorId,beginDate,endDate);
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(Exception e){
            logger.error("fail to get the home data by doctorId - " + doctorId,e);
            return null;
        }
    }

    public HomeData getHomeDataById(int dataId) throws Exception {
        try{
            return homeDAO.getHomeDataById(dataId);
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(Exception e){
            logger.error("fail to get the home data by dataId - " + dataId,e);
            return null;
        }
    }

    public void insert(HomeData homeData, String doctorId) throws Exception {
        homeDAO.insert(homeData, doctorId);
        
    }

    public void update(HomeData homeData) throws Exception {
        homeDAO.update(homeData);
    }

    public List<HomeWeeklyData> getHomeWeeklyDataOfCurrentUser(UserInfo currentUser) throws Exception {
        List<HomeWeeklyData> homeWeeklyData = new ArrayList<HomeWeeklyData>();
        Date lastWednesday = DateUtils.getGenerateWeeklyReportDate();
        Date beginDate = new Date(lastWednesday.getTime() - 6* 24 * 60 * 60 * 1000);
        Date endDate = new Date(lastWednesday.getTime() + 1 * 24 * 60 * 60 * 1000);
        switch(currentUser.getLevel()){
            case LsAttributes.USER_LEVEL_BM:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfRSD(beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_RSD:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfRSD(beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_RSM:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfRSM(currentUser.getRegionCenter(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_DSM:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfDSM(currentUser.getRegion(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_REP:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfSales(currentUser.getSuperior(),currentUser.getRegion(),beginDate, endDate);
                break;
        }
        return homeWeeklyData;
    }
    
    public List<HomeWeeklyData> getHomeWeeklyDataOfLowerUser(UserInfo currentUser) throws Exception {
        List<HomeWeeklyData> homeWeeklyData = new ArrayList<HomeWeeklyData>();
        Date lastWednesday = DateUtils.getGenerateWeeklyReportDate();
        Date beginDate = new Date(lastWednesday.getTime() - 6* 24 * 60 * 60 * 1000);
        Date endDate = new Date(lastWednesday.getTime() + 1 * 24 * 60 * 60 * 1000);
        switch(currentUser.getLevel()){
            case LsAttributes.USER_LEVEL_BM:
                break;
            case LsAttributes.USER_LEVEL_RSD:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfRSM(currentUser.getRegionCenter(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_RSM:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfDSM(currentUser.getRegion(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_DSM:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfSales(currentUser.getUserCode(),currentUser.getRegion(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_REP:
                break;
        }
        return homeWeeklyData;
    }
    
    public HomeWeeklyData getHomeWeeklyDataOfUpperUser(UserInfo currentUser) throws Exception {
        HomeWeeklyData homeWeeklyData = new HomeWeeklyData();
        Date lastWednesday = DateUtils.getGenerateWeeklyReportDate();
        Date beginDate = new Date(lastWednesday.getTime() - 6* 24 * 60 * 60 * 1000);
        Date endDate = new Date(lastWednesday.getTime() + 1 * 24 * 60 * 60 * 1000);
        switch(currentUser.getLevel()){
            case LsAttributes.USER_LEVEL_BM:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfCountory(beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_RSD:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfCountory(beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_RSM:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfSingleRSD(currentUser.getRegionCenter(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_DSM:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfSingleRSM(currentUser.getRegion(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_REP:
                homeWeeklyData = homeDAO.getHomeWeeklyDataOfSingleDSM(currentUser.getSuperior(),currentUser.getRegion(),beginDate, endDate);
                break;
        }
        return homeWeeklyData;
    }

}
