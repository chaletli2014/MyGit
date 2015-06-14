package com.chalet.lskpi.service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.chalet.lskpi.dao.DoctorDAO;
import com.chalet.lskpi.dao.HomeDAO;
import com.chalet.lskpi.exception.CustomrizedExceptioin;
import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.DoctorToBeDeleted;
import com.chalet.lskpi.model.ExportDoctor;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.HomeWeeklyNoReportDr;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;

@Service("homeService")
public class HomeServiceImpl implements HomeService {

    @Autowired
    @Qualifier("homeDAO")
    private HomeDAO homeDAO;
    
	@Autowired
	@Qualifier("doctorDAO")
	private DoctorDAO doctorDAO;
    
    Logger logger = Logger.getLogger(HomeServiceImpl.class);
    
    public HomeData getHomeDataByDoctorId(String doctorId) throws Exception {
        try{
            Date beginDate = DateUtils.getHomeCollectionBegionDate(new Date());
            Date endDate = new Date(beginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
            return homeDAO.getHomeDataByDoctorId(doctorId,beginDate,endDate);
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(Exception e){
            logger.error("fail to get the home data by doctorId - " + doctorId,e);
            return null;
        }
    }
    
    public List<HomeData> getHomeDataByDate(Date startDate, Date endDate) throws Exception {
        try{
            endDate = new Date(endDate.getTime() + 1 * 24 * 60 * 60 * 1000);
            return homeDAO.getHomeDataByDate(startDate, endDate);
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return new ArrayList<HomeData>();
        } catch(Exception e){
            logger.error("fail to get the home data by Date,",e);
            return new ArrayList<HomeData>();
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
        Date beginDate = DateUtils.getHomeWeeklyReportBegionDate();
        Date endDate = new Date(beginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        
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
    	Date beginDate = DateUtils.getHomeWeeklyReportBegionDate();
    	Date endDate = new Date(beginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
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
    	Date beginDate = DateUtils.getHomeWeeklyReportBegionDate();
    	Date endDate = new Date(beginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
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
    
    @Override
    public List<HomeWeeklyData> getPedHomeWeeklyDataOfLowerUser(UserInfo currentUser, String pedType) throws Exception {
        List<HomeWeeklyData> homeWeeklyData = new ArrayList<HomeWeeklyData>();
        /**
    	 * endDate 本周一
    	 */
    	Date endDate = DateUtils.getTheBeginDateOfCurrentWeek();
    	/**
    	 * beginDate 上周一
    	 */
    	Date beginDate = DateUtils.getDateByParam(endDate, -7);
        switch(currentUser.getLevel()){
            case LsAttributes.USER_LEVEL_BM:
                break;
            case LsAttributes.USER_LEVEL_RSD:
                homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfRSM(pedType, currentUser.getRegionCenter(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_RSM:
                homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfDSM(pedType, currentUser.getRegion(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_DSM:
                homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfSales(pedType, currentUser.getUserCode(),currentUser.getRegion(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_REP:
                break;
        }
        return homeWeeklyData;
    }
    
    @Override
    public HomeWeeklyData getPedHomeWeeklyDataOfUpperUser(UserInfo currentUser, String pedType) throws Exception {
        HomeWeeklyData homeWeeklyData = new HomeWeeklyData();
        /**
    	 * endDate 本周一
    	 */
    	Date endDate = DateUtils.getTheBeginDateOfCurrentWeek();
    	/**
    	 * beginDate 上周一
    	 */
    	Date beginDate = DateUtils.getDateByParam(endDate, -7);
        switch(currentUser.getLevel()){
            case LsAttributes.USER_LEVEL_BM:
                homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfCountory(pedType, beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_RSD:
                homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfCountory(pedType, beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_RSM:
                homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfSingleRSD(pedType, currentUser.getRegionCenter(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_DSM:
                homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfSingleRSM(pedType, currentUser.getRegion(),beginDate, endDate);
                break;
            case LsAttributes.USER_LEVEL_REP:
                homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfSingleDSM(pedType, currentUser.getSuperior(),currentUser.getRegion(),beginDate, endDate);
                break;
        }
        return homeWeeklyData;
    }
    
    @Override
    public List<HomeWeeklyData> getPedHomeWeeklyDataOfCurrentUser(UserInfo currentUser, String pedType) throws Exception {
    	List<HomeWeeklyData> homeWeeklyData = new ArrayList<HomeWeeklyData>();
    	/**
    	 * endDate 本周一
    	 */
    	Date endDate = DateUtils.getTheBeginDateOfCurrentWeek();
    	/**
    	 * beginDate 上周一
    	 */
    	Date beginDate = DateUtils.getDateByParam(endDate, -7);
    	
    	switch(currentUser.getLevel()){
    	case LsAttributes.USER_LEVEL_BM:
    		homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfRSD(pedType, beginDate, endDate);
    		break;
    	case LsAttributes.USER_LEVEL_RSD:
    		homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfRSD(pedType, beginDate, endDate);
    		break;
    	case LsAttributes.USER_LEVEL_RSM:
    		homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfRSM(pedType, currentUser.getRegionCenter(),beginDate, endDate);
    		break;
    	case LsAttributes.USER_LEVEL_DSM:
    		homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfDSM(pedType, currentUser.getRegion(),beginDate, endDate);
    		break;
    	case LsAttributes.USER_LEVEL_REP:
    		homeWeeklyData = homeDAO.getPedHomeWeeklyDataOfSales(pedType, currentUser.getSuperior(),currentUser.getRegion(),beginDate, endDate);
    		break;
    	}
    	return homeWeeklyData;
    }
    
	@Override
	public List<HomeWeeklyData> getPedHomeWeeklyDataByRegion(String regionCenter, String pedType) throws Exception {
		List<HomeWeeklyData> rsmHomeWeeklyData = new ArrayList<HomeWeeklyData>();
		/**
    	 * endDate 本周一
    	 */
    	Date endDate = DateUtils.getTheBeginDateOfCurrentWeek();
    	/**
    	 * beginDate 上周一
    	 */
    	Date beginDate = DateUtils.getDateByParam(endDate, -7);
        
        rsmHomeWeeklyData = homeDAO.getPedHomeWeeklyDataOfRSM(pedType, regionCenter,beginDate, endDate);
		return rsmHomeWeeklyData;
	}

    public List<ExportDoctor> getAllDoctors() throws Exception {
        return homeDAO.getAllDoctors();
    }

	@Override
	public List<HomeWeeklyData> getWeeklyDataByRegion(String regionCenter)
			throws Exception {
		List<HomeWeeklyData> rsmHomeWeeklyData = new ArrayList<HomeWeeklyData>();
        Date beginDate = DateUtils.getHomeWeeklyReportBegionDate();
        Date endDate = new Date(beginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        
        rsmHomeWeeklyData = homeDAO.getHomeWeeklyDataOfRSM(regionCenter,beginDate, endDate);
		return rsmHomeWeeklyData;
	}
	
	@Override
	public List<HomeWeeklyData> getWeeklyDataByRegion(String regionCenter, Date beginDate)
	        throws Exception {
	    List<HomeWeeklyData> rsmHomeWeeklyData = new ArrayList<HomeWeeklyData>();
	    Date endDate = new Date(beginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
	    
	    rsmHomeWeeklyData = homeDAO.getHomeWeeklyDataOfRSM(regionCenter,beginDate, endDate);
	    return rsmHomeWeeklyData;
	}
	
	@Override
	public List<HomeWeeklyNoReportDr> getWeeklyNoReportDr(Date beginDate)
			throws Exception {
		List<HomeWeeklyNoReportDr> homeWeeklyNoReportDr = new ArrayList<HomeWeeklyNoReportDr>();
		Date endDate = new Date(beginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
		
		String duration = DateUtils.populateDuration(beginDate, new Date(endDate.getTime() - 1 * 24 * 60 * 60 * 1000));
		try{
			homeWeeklyNoReportDr = homeDAO.getWeeklyNoReportDr(beginDate, endDate, duration);
		}catch(EmptyResultDataAccessException erd){
			return new ArrayList<HomeWeeklyNoReportDr>();
		}
		
		return homeWeeklyNoReportDr;
	}

    public ReportProcessData getSalesSelfReportProcess(String telephone) throws Exception {
        try{
            return homeDAO.getSalesSelfReportProcess(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return new ReportProcessData();
        } catch(Exception e){
            logger.error(String.format("fail to get the home report process data by telephone - %s" , telephone),e);
            return new ReportProcessData();
        }
    }

    public ReportProcessData getDSMSelfReportProcess(String telephone) throws Exception {
        try{
            return homeDAO.getDSMSelfReportProcess(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no record found by the telephone - %s", telephone));
            return new ReportProcessData();
        } catch(Exception e){
            logger.error(String.format("fail to get the DSM report home process data by telephone - %s" , telephone),e);
            return new ReportProcessData();
        }
    }

    public ReportProcessData getRSMSelfReportProcess(String telephone) throws Exception {
        try{
            return homeDAO.getRSMSelfReportProcess(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no record found by the telephone - %s", telephone));
            return new ReportProcessData();
        } catch(Exception e){
            logger.error(String.format("fail to get the RSM report process data by telephone - %s" , telephone),e);
            return new ReportProcessData();
        }
    }

	@Override
	public void backupDoctors(int dayInWeek) throws Exception {
        String duration = "";
        if( dayInWeek > 3 || dayInWeek == 0 ){
			Date beginDate = DateUtils.getHomeCollectionBegionDate(new Date());
	        Date endDate = new Date(beginDate.getTime() + 6 * 24 * 60 * 60 * 1000);
	        duration = DateUtils.populateDuration(beginDate, endDate);
		}else if( dayInWeek == 1 ){
			Date beginDate = DateUtils.getHomeWeeklyReportBegionDate();
	        Date endDate = new Date(beginDate.getTime() + 6 * 24 * 60 * 60 * 1000);
	        duration = DateUtils.populateDuration(beginDate, endDate);
		}
		logger.info(String.format("backup doctors, duration is %s, day in week is %s", duration,dayInWeek));
		homeDAO.backupDoctors(duration);
	}
	
	public boolean isAlreadyBackup() throws Exception{
		Date beginDate = DateUtils.getHomeWeeklyReportBegionDate();
        Date endDate = new Date(beginDate.getTime() + 6 * 24 * 60 * 60 * 1000);
        String duration = DateUtils.populateDuration(beginDate, endDate);
        
        return homeDAO.isAlreadyBackup(duration);
	}
	
	public synchronized void removeOldDoctors(int dayInWeek) throws Exception{
		String duration = "";
		if( dayInWeek > 3 || dayInWeek == 0 ){
			Date beginDate = DateUtils.getHomeCollectionBegionDate(new Date());
	        Date endDate = new Date(beginDate.getTime() + 6 * 24 * 60 * 60 * 1000);
	        duration = DateUtils.populateDuration(beginDate, endDate);
		}else if( dayInWeek == 1 ){
			Date beginDate = DateUtils.getHomeWeeklyReportBegionDate();
	        Date endDate = new Date(beginDate.getTime() + 6 * 24 * 60 * 60 * 1000);
	        duration = DateUtils.populateDuration(beginDate, endDate);
		}
		logger.info(String.format("remove old doctors, duration is %s, day in week is %s", duration,dayInWeek));
		homeDAO.removeOldDoctors(duration);
		
		logger.info(String.format("begin to backup the doctors, duration is %s, day in week is %s", duration,dayInWeek));
		this.backupDoctors(dayInWeek);
	}

	@Override
	public List<DoctorToBeDeleted> getAllDoctorsToBeDeleted() throws Exception {
		try{
			List<DoctorToBeDeleted> doctors = doctorDAO.getAllDoctorsToBeDeleted();
			if( doctors == null ){
				doctors = Collections.emptyList();
			}
			return doctors;
		}catch(EmptyResultDataAccessException erd){
			return Collections.emptyList();
		}
	}

	@Override
	public void storeToBeDeletedDoctor(DoctorToBeDeleted doctor, String currentUserTel)
			throws Exception {
		Date beginDate = DateUtils.getHomeCollectionBegionDate(new Date());
        Date endDate = new Date(beginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        boolean drHasData = doctorDAO.drHasLastWeekData(doctor.getDrId(), beginDate,endDate);
        if( drHasData ){
            logger.warn(String.format("doctor %s has data in last week, could not be deleted",doctor.getDrId()));
            throw new CustomrizedExceptioin("该医生已经填入上周数据，本周不能立即删除");
        }else{
        	doctorDAO.storeToBeDeletedDoctor(doctor,currentUserTel);
        }
	}

	@Override
	public String getDeleteReasonByDrId(int drId) throws Exception {
		try{
			return doctorDAO.getDeleteReasonByDrId(drId);
		}catch(EmptyResultDataAccessException erd){
			return "UNKNOWN";
		}
	}

	@Override
	public void rejectRemovingDoctor(Doctor doctor, String currentUserTel)
			throws Exception {
		doctorDAO.updateApprovalStatus(doctor, currentUserTel, "2");
	}

	@Override
	public Doctor getDoctorByDoctorNameAndHospital(String doctorName,String hospitalCode) throws Exception {
		return doctorDAO.getDoctorByDoctorNameAndHospital(doctorName, hospitalCode);
	}
}
