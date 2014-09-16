package com.chalet.lskpi.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.chalet.lskpi.comparator.HospitalSalesDataComparator;
import com.chalet.lskpi.dao.ChestSurgeryDAO;
import com.chalet.lskpi.dao.DoctorDAO;
import com.chalet.lskpi.dao.HospitalDAO;
import com.chalet.lskpi.dao.RespirologyDAO;
import com.chalet.lskpi.dao.UserDAO;
import com.chalet.lskpi.exception.CustomrizedExceptioin;
import com.chalet.lskpi.model.Doctor;
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
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午5:14:49
 * 类说明
 */

@Service("hospitalService")
public class HospitalServiceImpl implements HospitalService {

	@Autowired
	@Qualifier("hospitalDAO")
	private HospitalDAO hospitalDAO;
	
	@Autowired
	@Qualifier("userDAO")
	private UserDAO userDAO;
	
	@Autowired
	@Qualifier("doctorDAO")
	private DoctorDAO doctorDAO;
	
	@Autowired
	@Qualifier("respirologyDAO")
	private RespirologyDAO respirologyDAO;
	
	@Autowired
	@Qualifier("chestSurgeryDAO")
	private ChestSurgeryDAO chestSurgeryDAO;
	
	private Logger logger = Logger.getLogger(HospitalServiceImpl.class);
	
	
	public int getTotalDrNumOfHospital(String hospitalCode) throws Exception {
		return doctorDAO.getTotalDrNumOfHospital(hospitalCode)+doctorDAO.getTotalRemovedDrNumOfHospital(hospitalCode);
	}
	
	public int getExistedDrNumByHospitalCode(String hospitalCode, String drName) throws Exception {
	    return doctorDAO.getExistedDrNumByHospitalCode(hospitalCode,drName);
	}

    public int getExistedDrNumByHospitalCodeExcludeSelf(long dataId, String hospitalCode, String drName) throws Exception {
        return doctorDAO.getExistedDrNumByHospitalCodeExcludeSelf(dataId, hospitalCode, drName);
    }

    public void insertDoctor(Doctor doctor) throws Exception {
        doctorDAO.insertDoctor(doctor);
    }
    
    public void updateDoctorRelationship(int doctorId, String salesCode) throws Exception {
        doctorDAO.updateDoctorRelationship(doctorId, salesCode);
    }

    public void updateDoctor(Doctor doctor) throws Exception {
        doctorDAO.updateDoctor(doctor);
    }

    public void deleteDoctor(Doctor doctor) throws Exception {
        Date beginDate = DateUtils.getHomeCollectionBegionDate(new Date());
        Date endDate = new Date(beginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        boolean drHasData = doctorDAO.drHasLastWeekData(doctor.getId(), beginDate,endDate);
        if( drHasData ){
            logger.warn(String.format("doctor %s has data in last week, could not be deleted",doctor.getId()));
            throw new CustomrizedExceptioin("该医生已经填入上周数据，本周不能立即删除");
        }else{
            logger.info(String.format("start to delete doctor, backup doctor firstly, doctorId is %s", doctor.getId()));
            doctorDAO.backupDoctor(doctor);
            logger.info("start to delete the doctor");
            doctorDAO.deleteDoctor(doctor);
            logger.info("delete done.");
        }
    }
	
	public List<Doctor> getDoctorsOfCurrentUser( UserInfo currentUser ) throws Exception{
	    List<Doctor> doctors = new ArrayList<Doctor>();
	    switch(currentUser.getLevel()){
	        case LsAttributes.USER_LEVEL_DSM:
	            doctors =  doctorDAO.getDoctorsByDsmCode(currentUser.getUserCode());
	            break;
	        case LsAttributes.USER_LEVEL_REP:
	            doctors = doctorDAO.getDoctorsBySalesCode(currentUser.getUserCode());
	            break;
	    }
	    return doctors;
    }

    @Override
    public List<MonthlyData> getMonthlyDataByDate(Date startDate, Date endDate)
            throws Exception {
        return hospitalDAO.getMonthlyDataByDate(startDate, endDate);
    }
    
    public List<Monthly12Data> get12MontlyDataOfUser(UserInfo user) throws Exception {
        List<Monthly12Data> monthlyDatas = new ArrayList<Monthly12Data>();
        switch(user.getLevel()){
            case LsAttributes.USER_LEVEL_BM:
                break;
            case LsAttributes.USER_LEVEL_RSD:
                monthlyDatas = hospitalDAO.getRSD12MontlyDataByRegionCenter(user.getRegionCenter());
                break;
            case LsAttributes.USER_LEVEL_RSM:
                monthlyDatas = hospitalDAO.getRSM12MontlyDataByRegion(user.getRegion());
                break;
            case LsAttributes.USER_LEVEL_DSM:
                monthlyDatas = hospitalDAO.getDSM12MontlyDataByDSMCode(user.getUserCode());
                break;
        }
        return monthlyDatas;
    }
    
    public List<Monthly12Data> get12MontlyDataByCountory() throws Exception {
    	return hospitalDAO.get12MontlyDataByCountory();
    }
	
	public List<Hospital> getAllHospitals() throws Exception{
	    return hospitalDAO.getAllHospitals();
	}
	
	@Override
	public Hospital getHospitalByName(String hospitalName) throws Exception {
		return hospitalDAO.getHospitalByName(hospitalName);
	}
	
	@Override
	public Hospital getHospitalByCode(String hospitalCode) throws Exception {
	    return hospitalDAO.getHospitalByCode(hospitalCode);
	}

	@Override
	public List<Hospital> getHospitalsByUserTel(String telephone, String department) throws Exception {
	    UserInfo user = userDAO.getUserInfoByTel(telephone);
	    if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(user.getLevel()) ){
	        return hospitalDAO.getHospitalsByUserTel(telephone, department);
	    }else if ( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(user.getLevel()) ){
	        return hospitalDAO.getHospitalsByDSMTel(telephone, department);
	    }
	    return new ArrayList<Hospital>();
	}
	
	@Override
	public List<Hospital> getHospitalsOfHomeCollectionByUserTel(String telephone) throws Exception {
	    UserInfo user = userDAO.getUserInfoByTel(telephone);
	    if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(user.getLevel()) ){
	        return hospitalDAO.getHospitalsOfHomeCollectionByPSRTel(telephone);
	    }else if ( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(user.getLevel()) ){
	        return hospitalDAO.getHospitalsOfHomeCollectionByDSMTel(telephone);
	    }
	    return new ArrayList<Hospital>();
	}
	
	@Override
	public List<Hospital> getMonthlyHospitalsByUserTel(String telephone) throws Exception {
	    UserInfo user = userDAO.getUserInfoByTel(telephone);
	    if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(user.getLevel()) ){
	        return hospitalDAO.getMonthlyHospitalsByUserTel(telephone);
	    }else if ( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(user.getLevel()) ){
	        return hospitalDAO.getMonthlyHospitalsByDSMTel(telephone);
	    }
	    return new ArrayList<Hospital>();
	}

	@Override
	public void insert(List<Hospital> hospitals) throws Exception {
		hospitalDAO.insert(hospitals);
	}
	
	@Override
	public void insertMonthlyData(MonthlyData monthlyData) throws Exception {
	    hospitalDAO.insertMonthlyData(monthlyData);
	}

	@Override
	public void delete() throws Exception {
		hospitalDAO.delete();
	}

	@Override
	public MonthlyData getMonthlyData(String hospitalCode, Date date) throws Exception {
	    try{
	        return hospitalDAO.getMonthlyData(hospitalCode, date);
	    }catch(EmptyResultDataAccessException erd){
            logger.info("there is no monthly record found.");
            return null;
        } catch(Exception e){
            logger.error("fail to get the monthly data by hospitalCode - " + hospitalCode,e);
            return null;
        }
	}
	
	@Override
	public MonthlyData getMonthlyDataById(int id) throws Exception {
	    return hospitalDAO.getMonthlyDataById(id);
	}

    public void updateMonthlyData(MonthlyData monthlyData) throws Exception {
        hospitalDAO.updateMonthlyData(monthlyData);
    }

	@Override
	public List<Hospital> getHospitalsByKeyword(String keyword)
			throws Exception {
		String[] keywords = null;
		if( keyword.indexOf(LsAttributes.HOSPITAL_SPLITCHAT_1) > 0 ){
			keywords = keyword.split(LsAttributes.HOSPITAL_SPLITCHAT_1);
		}else if( keyword.indexOf(LsAttributes.HOSPITAL_SPLITCHAT_2) > 0 ){
			keywords = keyword.split(LsAttributes.HOSPITAL_SPLITCHAT_2);
		}
		
		StringBuffer searchingStr = new StringBuffer("%");
		if( null == keywords || keywords.length == 0){
			searchingStr.append(keyword).append("%");
		}else{
			for( String keywordItem : keywords){
				if( keywordItem != null && !"".equalsIgnoreCase(keywordItem) ){
					searchingStr.append(keywordItem).append("%");
				}
			}
		}
		return hospitalDAO.getHospitalsByKeywords(searchingStr.toString());
	}

    public UserInfo getPrimarySalesOfHospital(String hospitalCode) throws Exception {
        try{
            return hospitalDAO.getPrimarySalesOfHospital(hospitalCode);
        }catch(IncorrectResultSizeDataAccessException e){
            logger.error(String.format("fail to get the primary sales of hospital %s, incorrect result size", hospitalCode));
            return null;
        }
    }

    public List<MonthlyRatioData> getMonthlyRatioData(UserInfo currentUser) throws Exception {
        List<MonthlyRatioData> monthlyRatioData = new ArrayList<MonthlyRatioData>();
        switch(currentUser.getLevel()){
            case LsAttributes.USER_LEVEL_BM:
                monthlyRatioData = hospitalDAO.getMonthlyDataOfRSD();
                break;
            case LsAttributes.USER_LEVEL_RSD:
                monthlyRatioData = hospitalDAO.getMonthlyDataOfRSD();
                break;
            case LsAttributes.USER_LEVEL_RSM:
                monthlyRatioData = hospitalDAO.getMonthlyDataOfRSMByRegion(currentUser.getRegionCenter());
                break;
            case LsAttributes.USER_LEVEL_DSM:
                monthlyRatioData = hospitalDAO.getMonthlyDataOfDSMByRsmRegion(currentUser.getRegion());
                break;
        }
        
        return monthlyRatioData;
    }
    
    public List<MonthlyRatioData> getChildMonthlyRatioData(UserInfo currentUser) throws Exception {
    	List<MonthlyRatioData> monthlyRatioData = new ArrayList<MonthlyRatioData>();
    	switch(currentUser.getLevel()){
    	case LsAttributes.USER_LEVEL_RSD:
    		monthlyRatioData = hospitalDAO.getMonthlyDataOfRSMByRegion(currentUser.getRegionCenter());
    		break;
    	case LsAttributes.USER_LEVEL_RSM:
    		monthlyRatioData = hospitalDAO.getMonthlyDataOfDSMByRsmRegion(currentUser.getRegion());
    		break;
    	case LsAttributes.USER_LEVEL_DSM:
    		monthlyRatioData = hospitalDAO.getMonthlyDataOfREPByDSMCode(currentUser.getUserCode());
    		break;
    	}
    	
    	return monthlyRatioData;
    }
    
    public MonthlyRatioData getUpperUserMonthlyRatioData(UserInfo currentUser) throws Exception {
        MonthlyRatioData monthlyRatioData = new MonthlyRatioData();
        switch(currentUser.getLevel()){
            case LsAttributes.USER_LEVEL_BM:
                monthlyRatioData = hospitalDAO.getMonthlyDataOfCountory();
                break;
            case LsAttributes.USER_LEVEL_RSD:
                monthlyRatioData = hospitalDAO.getMonthlyDataOfCountory();
                break;
            case LsAttributes.USER_LEVEL_RSM:
                monthlyRatioData = hospitalDAO.getMonthlyDataOfSingleRsd(currentUser.getRegionCenter());
                break;
            case LsAttributes.USER_LEVEL_DSM:
                monthlyRatioData = hospitalDAO.getMonthlyDataOfSingleRsm(currentUser.getRegion());
                break;
        }
        
        return monthlyRatioData;
    }

    public List<HospitalSalesQueryObj> getHospitalSalesList(HospitalSalesQueryParam queryParam) throws Exception {
        List<HospitalSalesQueryObj> dbData = new ArrayList<HospitalSalesQueryObj>();
        dbData = hospitalDAO.getHospitalSalesList(queryParam);
        Collections.sort(dbData, new HospitalSalesDataComparator(queryParam));
        return dbData;
    }

	@Override
	public Map<String, MonthlyInRateData> getMonthlyInRateData(String beginDuraion, String endDuraion, String level)
			throws Exception {
		List<MonthlyInRateData> dbInRateData = hospitalDAO.getMonthlyInRateData(beginDuraion, endDuraion, level);
		logger.info(String.format("finish to get monthly inRate during %s and %s", beginDuraion,endDuraion));
		
		Map<String, MonthlyInRateData> tempInRateResult = new HashMap<String, MonthlyInRateData>();
		Map<String, List<MonthlyInRateData>> tempInRate = new HashMap<String, List<MonthlyInRateData>>();
		String user = "";
		for( MonthlyInRateData inRate : dbInRateData ){
			if( "RSM".equalsIgnoreCase(level) ){
				user = inRate.getRsm();
			}else{
				user = inRate.getRsd();
			}
			if( tempInRate.containsKey(user) ){
				tempInRate.get(user).add(inRate);
			}else{
				List<MonthlyInRateData> inRateList = new ArrayList<MonthlyInRateData>();
				inRateList.add(inRate);
				tempInRate.put(user, inRateList);
			}
		}
		
		Set<String> users = tempInRate.keySet();
		Iterator<String> userIterator = users.iterator();
		
		String userName = "";
		while(userIterator.hasNext()){
			double resInRate = 0;
			double pedInRate = 0;
			userName = userIterator.next();
			List<MonthlyInRateData> inRateList = tempInRate.get(userName);
			for( MonthlyInRateData inRateData : inRateList ){
				resInRate = resInRate + inRateData.getResInRate();
				pedInRate = pedInRate + inRateData.getPedInRate();
			}
			pedInRate = pedInRate/inRateList.size();
			resInRate = resInRate/inRateList.size();
			
			MonthlyInRateData inRateResult = new MonthlyInRateData();
			inRateResult.setPedInRate(pedInRate);
			inRateResult.setResInRate(resInRate);
			tempInRateResult.put(userName, inRateResult);
		}
		return tempInRateResult;
	}

	@Override
	public List<MonthlyRatioData> getMonthlyCollectionData(Date chooseDate) throws Exception {
		return hospitalDAO.getMonthlyCollectionData(chooseDate);
	}

	@Override
	public MonthlyRatioData getMonthlyCollectionSumData(Date chooseDate)
			throws Exception {
		return hospitalDAO.getMonthlyCollectionSumData(chooseDate);
	}

	@Override
	public Doctor getDoctorById(int doctorId) throws Exception {
		try{
			return doctorDAO.getDoctorById(doctorId);
		}catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no doctor found. whose id is %s", doctorId));
            return null;
        } catch(Exception e){
            logger.error("fail to get the doctor by doctorId - " + doctorId,e);
            return null;
        }
	}
	
	public boolean hasLastWeeklyData() throws Exception {
        int count = hospitalDAO.getLastWeeklyData();
        logger.info("the last week hospital data size is " + count);
        return count>0;
    }
	

    public int deleteOldHospitalWeeklyData(String duration) throws Exception {
        return hospitalDAO.deleteOldHospitalWeeklyData(duration);
    }
	
	public void generateWeeklyDataOfHospital() throws Exception {
		hospitalDAO.generateWeeklyDataOfHospital();
    }
    
    public void generateWeeklyDataOfHospital(Date refreshDate) throws Exception {
        logger.info("start to generate the ped hospital data");
    	hospitalDAO.generateWeeklyPEDDataOfHospital(refreshDate);
    	logger.info("finish to generate the ped data");
    	
    	List<WeeklyDataOfHospital> resWeeklyHosData = respirologyDAO.getWeeklyDataOfHospital(refreshDate);
    	for( WeeklyDataOfHospital data : resWeeklyHosData ){
    	    int hosWeeklyDataId = hospitalDAO.getWeeklyDataIDOfHospital(data.getDuration(), data.getHospitalCode());
    	    if( hosWeeklyDataId > 0 ){
    	        hospitalDAO.updateHospitalWeeklyRESData(data, hosWeeklyDataId);
    	    }else{
    	        hospitalDAO.insertHospitalWeeklyRESData(data);
    	    }
    	}
    	
    	logger.info("finish to populate the res data");
    	
    	List<WeeklyDataOfHospital> cheWeeklyHosData = chestSurgeryDAO.getWeeklyDataOfHospital(refreshDate);
    	
    	for( WeeklyDataOfHospital data : cheWeeklyHosData ){
            int hosWeeklyDataId = hospitalDAO.getWeeklyDataIDOfHospital(data.getDuration(), data.getHospitalCode());
            if( hosWeeklyDataId > 0 ){
                hospitalDAO.updateHospitalWeeklyCHEData(data, hosWeeklyDataId);
            }else{
                hospitalDAO.insertHospitalWeeklyCHEData(data);
            }
        }
    	
    	logger.info("finish to populate the che data");
    }

	@Override
	public List<KPIHospital4Export> getKPIHospitalByDepartment(String department)
			throws Exception {
		List<KPIHospital4Export> dbKPIHos = new ArrayList<KPIHospital4Export>();
		
		switch(department){
		case "1":
			dbKPIHos = hospitalDAO.getKPIHospitalOfRes();
			break;
		case "2":
			dbKPIHos = hospitalDAO.getKPIHospitalOfPed();
			break;
		case "3":
			dbKPIHos = hospitalDAO.getKPIHospitalOfChe();
			break;
		case "4":
			dbKPIHos = hospitalDAO.getKPIHospitalOfMonth();
			break;
		}
		return dbKPIHos;
	}

    public List<Map<String, Integer>> getKPIHosNumMap(String department) throws Exception {
        return hospitalDAO.getKPIHosNumMap(department);
    }

    public List<Map<String, Integer>> getKPISalesNumMap(String department) throws Exception {
        return hospitalDAO.getKPISalesNumMap(department);
    }

	@Override
	public void uploadPortNumData(Hospital hospitalWithPortNum)
			throws Exception {
		hospitalDAO.updatePortNum(hospitalWithPortNum);
	}
}
