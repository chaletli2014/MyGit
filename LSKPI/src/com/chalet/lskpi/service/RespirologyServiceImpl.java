package com.chalet.lskpi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.chalet.lskpi.dao.RespirologyDAO;
import com.chalet.lskpi.model.DailyReportData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobileRESDailyData;
import com.chalet.lskpi.model.RateElement;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.RespirologyData;
import com.chalet.lskpi.model.RespirologyExportData;
import com.chalet.lskpi.model.RespirologyMonthDBData;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyRatioData;
import com.chalet.lskpi.service.PediatricsServiceImpl.DailyReportDataAverageComparator;
import com.chalet.lskpi.service.PediatricsServiceImpl.DailyReportDataInRateComparator;
import com.chalet.lskpi.service.PediatricsServiceImpl.DailyReportDataWhRateComparator;
import com.chalet.lskpi.service.PediatricsServiceImpl.RateElementComparator;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:53:17
 * 类说明
 */

@Service("respirologyService")
public class RespirologyServiceImpl implements RespirologyService {

	@Autowired
	@Qualifier("respirologyDAO")
	private RespirologyDAO respirologyDAO;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("hospitalService")
	private HospitalService hospitalService;
	
	private Logger logger = Logger.getLogger(RespirologyServiceImpl.class);
    

    public MobileRESDailyData getDailyRESParentData4Mobile(String telephone, String level) throws Exception {
        MobileRESDailyData mrd = new MobileRESDailyData();
        
        switch (level) {
            case LsAttributes.USER_LEVEL_BM:
                mrd = respirologyDAO.getDailyRESData4CountoryMobile();
                logger.info(String.format("end to get the res daily data of the countory, current telephone is %s", telephone));
                List<RateElement> rates = new ArrayList<RateElement>();
                RateElement re1 = new RateElement("res");
                re1.setRateType(1);
                re1.setRateNum(mrd.getOmgRate());
                
                RateElement re2 = new RateElement("res");
                re2.setRateType(2);
                re2.setRateNum(mrd.getTmgRate());
                
                RateElement re3 = new RateElement("res");
                re3.setRateType(3);
                re3.setRateNum(mrd.getThmgRate());
                
                RateElement re4 = new RateElement("res");
                re4.setRateType(4);
                re4.setRateNum(mrd.getFmgRate());
                
                RateElement re6 = new RateElement("res");
                re6.setRateType(6);
                re6.setRateNum(mrd.getSmgRate());
                
                RateElement re8 = new RateElement("res");
                re8.setRateType(8);
                re8.setRateNum(mrd.getEmgRate());
                
                rates.add(re1);
                rates.add(re2);
                rates.add(re3);
                rates.add(re4);
                rates.add(re6);
                rates.add(re8);
                
                java.util.Collections.sort(rates, new RateElementComparator());
                
                mrd.setFirstRate(rates.get(0));
                mrd.setSecondRate(rates.get(1));
                mrd.setInRate(mrd.getHosNum()==0?0:(double)mrd.getInNum()/mrd.getHosNum());
                mrd.setWhRate(mrd.getPatNum()==0?0:(double)mrd.getLsNum()/mrd.getPatNum());
                break;
            default:
                mrd = null;
                break;
        }
        return mrd;
    }
	
	@Override
	public RespirologyData getRespirologyDataByHospital(String hospitalName) throws Exception {
	    try{
	        return respirologyDAO.getRespirologyDataByHospital(hospitalName);
	    } catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(Exception e){
	        logger.error("fail to get the respirology data by hospital - " + hospitalName,e);
	        return null;
	    }
	}
	
	@Override
	public RespirologyData getRespirologyDataByHospitalAndDate(String hospitalName, Date createdate) throws Exception {
	    try{
	        return respirologyDAO.getRespirologyDataByHospitalAndDate(hospitalName, createdate);
	    } catch(EmptyResultDataAccessException erd){
	        logger.info("there is no record found.");
	        return null;
	    } catch(IncorrectResultSizeDataAccessException ire){
	        logger.error(ire.getMessage());
	        RespirologyData defaultData = new RespirologyData();
	        defaultData.setDataId(0);
	        return defaultData;
	    }catch(Exception e){
	        logger.error("fail to get the respirology data by hospital - " + hospitalName,e);
	        RespirologyData defaultData = new RespirologyData();
	        defaultData.setDataId(0);
	        return defaultData;
	    }
	}
	
	public RespirologyData getRespirologyDataById(int id) throws Exception {
	    try{
            return respirologyDAO.getRespirologyDataById(id);
        }catch(Exception e){
            logger.error("fail to get the respirology data by ID - " + id,e);
            return null;
        }
	}

	@Override
	public void insert(RespirologyData respirologyData, UserInfo operator, Hospital hospital) throws Exception {
		respirologyDAO.insert(respirologyData, operator, hospital);
	}
	
	@Override
	public void insert(RespirologyData respirologyData) throws Exception {
		String dsmCode = "";
		try{
			UserInfo primarySales = hospitalService.getPrimarySalesOfHospital(respirologyData.getHospitalCode());
			if( null != primarySales ){
			    respirologyData.setSalesETMSCode(primarySales.getUserCode());
			    dsmCode = (primarySales.getSuperior()==null||"".equalsIgnoreCase(primarySales.getSuperior()))?primarySales.getUserCode():primarySales.getSuperior();
			}
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no user found whose code is " + respirologyData.getSalesETMSCode());
        }
	    respirologyDAO.insert(respirologyData,dsmCode);
	}

	@Override
	public void update(RespirologyData respirologyData, UserInfo operator) throws Exception {
    	respirologyDAO.update(respirologyData, operator);
	}

	@Override
	public List<RespirologyData> getRespirologyDataByDate(Date createdatebegin, Date createdateend)	throws Exception {
	    try{
	        return respirologyDAO.getRespirologyDataByDate(createdatebegin,createdateend);
	    }catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return new ArrayList<RespirologyData>();
        } catch(Exception e){
            logger.error("fail to get the Respirology data by date - " + createdatebegin,e);
            return new ArrayList<RespirologyData>();
        }
	}
	
	public List<MobileRESDailyData> getDailyRESData4MobileByRegion(String region) throws Exception {
    	List<MobileRESDailyData> resDatas = new ArrayList<MobileRESDailyData>();
		resDatas = respirologyDAO.getDailyRESData4RSMByRegion(region);
    	
    	for( MobileRESDailyData resDailyData : resDatas ){
            List<RateElement> rates = new ArrayList<RateElement>();
        	RateElement re1 = new RateElement("res");
        	re1.setRateType(1);
        	re1.setRateNum(resDailyData.getOmgRate());
        	
        	RateElement re2 = new RateElement("res");
        	re2.setRateType(2);
        	re2.setRateNum(resDailyData.getTmgRate());
        	
        	RateElement re3 = new RateElement("res");
        	re3.setRateType(3);
        	re3.setRateNum(resDailyData.getThmgRate());
        	
        	RateElement re4 = new RateElement("res");
        	re4.setRateType(4);
        	re4.setRateNum(resDailyData.getFmgRate());
        	
        	RateElement re6 = new RateElement("res");
        	re6.setRateType(6);
        	re6.setRateNum(resDailyData.getSmgRate());
        	
        	RateElement re8 = new RateElement("res");
        	re8.setRateType(8);
        	re8.setRateNum(resDailyData.getEmgRate());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            rates.add(re6);
            rates.add(re8);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            resDailyData.setFirstRate(rates.get(0));
            resDailyData.setSecondRate(rates.get(1));
            resDailyData.setInRate(resDailyData.getHosNum()==0?0:(double)resDailyData.getInNum()/resDailyData.getHosNum());
            resDailyData.setWhRate(resDailyData.getPatNum()==0?0:(double)resDailyData.getLsNum()/resDailyData.getPatNum());
    	}
        return resDatas;
	}

	@Override
	public List<MobileRESDailyData> getDailyRESData4Mobile(String telephone, UserInfo currentUser) throws Exception {
    	List<MobileRESDailyData> resDatas = new ArrayList<MobileRESDailyData>();
    	if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
    		resDatas = respirologyDAO.getDailyRESData4DSMMobile(telephone);
    	}else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
    		resDatas = respirologyDAO.getDailyRESData4RSMMobile(telephone);
    	}else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
    			|| LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())){
    		resDatas = respirologyDAO.getDailyRESData4RSDMobile();
    	}
    	logger.info(String.format("end to get the res daily data...current telephone is %s", telephone));
    	List<MobileRESDailyData> orderedResData = new ArrayList<MobileRESDailyData>();
    	List<MobileRESDailyData> leftResData = new ArrayList<MobileRESDailyData>();
    	
    	for( MobileRESDailyData resDailyData : resDatas ){
            List<RateElement> rates = new ArrayList<RateElement>();
        	RateElement re1 = new RateElement("res");
        	re1.setRateType(1);
        	re1.setRateNum(resDailyData.getOmgRate());
        	
        	RateElement re2 = new RateElement("res");
        	re2.setRateType(2);
        	re2.setRateNum(resDailyData.getTmgRate());
        	
        	RateElement re3 = new RateElement("res");
        	re3.setRateType(3);
        	re3.setRateNum(resDailyData.getThmgRate());
        	
        	RateElement re4 = new RateElement("res");
        	re4.setRateType(4);
        	re4.setRateNum(resDailyData.getFmgRate());
        	
        	RateElement re6 = new RateElement("res");
        	re6.setRateType(6);
        	re6.setRateNum(resDailyData.getSmgRate());
        	
        	RateElement re8 = new RateElement("res");
        	re8.setRateType(8);
        	re8.setRateNum(resDailyData.getEmgRate());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            rates.add(re6);
            rates.add(re8);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            resDailyData.setFirstRate(rates.get(0));
            resDailyData.setSecondRate(rates.get(1));
            resDailyData.setInRate(resDailyData.getHosNum()==0?0:(double)resDailyData.getInNum()/resDailyData.getHosNum());
            resDailyData.setWhRate(resDailyData.getPatNum()==0?0:(double)resDailyData.getLsNum()/resDailyData.getPatNum());
        
            if( resDailyData.getHosNum() != 0 ){
                if( null != currentUser && null != resDailyData.getUserCode() 
                        && resDailyData.getUserCode().equalsIgnoreCase(currentUser.getUserCode()) ){
                    orderedResData.add(0,resDailyData);
                }else{
                    leftResData.add(resDailyData);
                }
            }
    	}
    	
    	if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
    		orderedResData.addAll(leftResData);
    	}else{
    		orderedResData.addAll(1,leftResData);
    	}
    	logger.info(String.format("end to populate the res daily data...current telephone is %s", telephone));
        return orderedResData;
	}
	
	@Override
	public List<MobileRESDailyData> getDailyRESChildData4Mobile(String telephone, UserInfo currentUser) throws Exception {
	    List<MobileRESDailyData> resDatas = new ArrayList<MobileRESDailyData>();
	    List<MobileRESDailyData> filteredResDatas = new ArrayList<MobileRESDailyData>();
	    if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
	        resDatas = respirologyDAO.getDailyRESChildData4DSMMobile(telephone);
	    }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
	        resDatas = respirologyDAO.getDailyRESChildData4RSMMobile(telephone);
	    }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) ){
	        resDatas = respirologyDAO.getDailyRESChildData4RSDMobile(telephone);
	    }
	    
	    for( MobileRESDailyData resDailyData : resDatas ){
	        List<RateElement> rates = new ArrayList<RateElement>();
	        RateElement re1 = new RateElement("res");
	        re1.setRateType(1);
	        re1.setRateNum(resDailyData.getOmgRate());
	        
	        RateElement re2 = new RateElement("res");
	        re2.setRateType(2);
	        re2.setRateNum(resDailyData.getTmgRate());
	        
	        RateElement re3 = new RateElement("res");
	        re3.setRateType(3);
	        re3.setRateNum(resDailyData.getThmgRate());
	        
	        RateElement re4 = new RateElement("res");
	        re4.setRateType(4);
	        re4.setRateNum(resDailyData.getFmgRate());
	        
	        RateElement re6 = new RateElement("res");
	        re6.setRateType(6);
	        re6.setRateNum(resDailyData.getSmgRate());
	        
	        RateElement re8 = new RateElement("res");
	        re8.setRateType(8);
	        re8.setRateNum(resDailyData.getEmgRate());
	        
	        rates.add(re1);
	        rates.add(re2);
	        rates.add(re3);
	        rates.add(re4);
	        rates.add(re6);
	        rates.add(re8);
	        
	        java.util.Collections.sort(rates, new RateElementComparator());
	        
	        resDailyData.setFirstRate(rates.get(0));
	        resDailyData.setSecondRate(rates.get(1));
	        resDailyData.setInRate(resDailyData.getHosNum()==0?0:(double)resDailyData.getInNum()/resDailyData.getHosNum());
	        resDailyData.setWhRate(resDailyData.getPatNum()==0?0:(double)resDailyData.getLsNum()/resDailyData.getPatNum());
	    
	        if( resDailyData.getHosNum() != 0 ){
	            filteredResDatas.add(resDailyData);
	        }
	    }
	    
	    return filteredResDatas;
	}
	

    @Override
    public List<WeeklyRatioData> getWeeklyRESData4Mobile(String telephone) throws Exception {
        UserInfo userInfo = userService.getUserInfoByTel(telephone);
        List<WeeklyRatioData> resDatas = new ArrayList<WeeklyRatioData>();
        if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(userInfo.getLevel()) ){
            resDatas = respirologyDAO.getWeeklyRESData4DSMMobile(telephone);
        }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(userInfo.getLevel()) ){
            resDatas = respirologyDAO.getWeeklyRESData4RSMMobile(telephone);
        }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(userInfo.getLevel()) 
        		|| LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(userInfo.getLevel())){
            resDatas = respirologyDAO.getWeeklyRESData4RSDMobile();
        }
        
        List<WeeklyRatioData> orderedResData = new ArrayList<WeeklyRatioData>();
        List<WeeklyRatioData> leftResData = new ArrayList<WeeklyRatioData>();
        
        for( WeeklyRatioData resWeeklyData : resDatas ){
            List<RateElement> rates = new ArrayList<RateElement>();
            RateElement re1 = new RateElement("res");
            re1.setRateType(1);
            re1.setRateNum(resWeeklyData.getOmgRate());
            re1.setRateRatio(resWeeklyData.getOmgRateRatio());
            
            RateElement re2 = new RateElement("res");
            re2.setRateType(2);
            re2.setRateNum(resWeeklyData.getTmgRate());
            re2.setRateRatio(resWeeklyData.getTmgRateRatio());
            
            RateElement re3 = new RateElement("res");
            re3.setRateType(3);
            re3.setRateNum(resWeeklyData.getThmgRate());
            re3.setRateRatio(resWeeklyData.getThmgRateRatio());
            
            RateElement re4 = new RateElement("res");
            re4.setRateType(4);
            re4.setRateNum(resWeeklyData.getFmgRate());
            re4.setRateRatio(resWeeklyData.getFmgRateRatio());
            
            RateElement re6 = new RateElement("res");
            re6.setRateType(6);
            re6.setRateNum(resWeeklyData.getSmgRate());
            re6.setRateRatio(resWeeklyData.getSmgRateRatio());
            
            RateElement re8 = new RateElement("res");
            re8.setRateType(8);
            re8.setRateNum(resWeeklyData.getEmgRate());
            re8.setRateRatio(resWeeklyData.getEmgRateRatio());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            rates.add(re6);
            rates.add(re8);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            resWeeklyData.setFirstRate(rates.get(0));
            resWeeklyData.setSecondRate(rates.get(1));
        
            boolean isSelf = false;
            if( null != userInfo && null != resWeeklyData.getUserCode() ){
            	if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(userInfo.getLevel())
            			&& resWeeklyData.getUserCode().equalsIgnoreCase(userInfo.getUserCode())){
            		isSelf = true;
            	}
            	if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(userInfo.getLevel())
            			&& resWeeklyData.getUserCode().equalsIgnoreCase(userInfo.getRegion())){
            		isSelf = true;
            	}
            	if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(userInfo.getLevel())
            			&& resWeeklyData.getUserCode().equalsIgnoreCase(userInfo.getRegionCenter())){
            		isSelf = true;
            	}
            }
            
            if( isSelf ){
            	orderedResData.add(0,resWeeklyData);
            }else{
                leftResData.add(resWeeklyData);
            }
        }
        if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(userInfo.getLevel()) ){
        	orderedResData.addAll(leftResData);
        }else{
        	orderedResData.addAll(1,leftResData);
        }
        
        return orderedResData;
    }
	
	@Override
	public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception {
		return respirologyDAO.getTopAndBottomRSMData();
	}

    @Override
    public TopAndBottomRSMData getTopAndBottomInRateRSMData(String telephone) throws Exception {
    	List<DailyReportData> allRSMData = respirologyDAO.getAllRSMDataByTelephone();
    	java.util.Collections.sort(allRSMData, new DailyReportDataInRateComparator());
    	
    	TopAndBottomRSMData topAndBottomRSMInRateData = new TopAndBottomRSMData();
    	topAndBottomRSMInRateData.setTopRSMName(allRSMData.get(0).getRsmName());
    	topAndBottomRSMInRateData.setTopRSMRate(allRSMData.get(0).getInRate());
    	topAndBottomRSMInRateData.setBottomRSMName(allRSMData.get(allRSMData.size()-1).getRsmName());
    	topAndBottomRSMInRateData.setBottomRSMRate(allRSMData.get(allRSMData.size()-1).getInRate());
    	
    	return topAndBottomRSMInRateData;
    }
    
    @Override
    public TopAndBottomRSMData getTopAndBottomWhRateRSMData(String telephone) throws Exception {
    	List<DailyReportData> allRSMData = respirologyDAO.getAllRSMDataByTelephone();
    	java.util.Collections.sort(allRSMData, new DailyReportDataWhRateComparator());
    	
    	TopAndBottomRSMData topAndBottomRSMWhRateData = new TopAndBottomRSMData();
    	topAndBottomRSMWhRateData.setTopRSMName(allRSMData.get(0).getRsmName());
    	topAndBottomRSMWhRateData.setTopRSMRate(allRSMData.get(0).getWhRate());
    	topAndBottomRSMWhRateData.setBottomRSMName(allRSMData.get(allRSMData.size()-1).getRsmName());
    	topAndBottomRSMWhRateData.setBottomRSMRate(allRSMData.get(allRSMData.size()-1).getWhRate());
    	
    	return topAndBottomRSMWhRateData;
    }
    
    @Override
    public TopAndBottomRSMData getTopAndBottomAverageRSMData(String telephone) throws Exception {
    	List<DailyReportData> allRSMData = respirologyDAO.getAllRSMDataByTelephone();
    	java.util.Collections.sort(allRSMData, new DailyReportDataAverageComparator());
    	
    	TopAndBottomRSMData topAndBottomRSMAverageData = new TopAndBottomRSMData();
    	topAndBottomRSMAverageData.setTopRSMName(allRSMData.get(0).getRsmName());
    	topAndBottomRSMAverageData.setTopRSMAverageDose(allRSMData.get(0).getAverageDose());
    	topAndBottomRSMAverageData.setBottomRSMName(allRSMData.get(allRSMData.size()-1).getRsmName());
    	topAndBottomRSMAverageData.setBottomRSMAverageDose(allRSMData.get(allRSMData.size()-1).getAverageDose());
    	
    	return topAndBottomRSMAverageData;
    }

	@Override
	public List<ReportProcessData> getReportProcessRESDataByUserTel(String telephone)
			throws Exception {
		return respirologyDAO.getReportProcessRESDataByUserTel(telephone);
	}

    public ReportProcessData getSalesSelfReportProcessRESData(String telephone) throws Exception {
        try{
            return respirologyDAO.getSalesSelfReportProcessRESData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return new ReportProcessData();
        } catch(Exception e){
            logger.error(String.format("fail to get the REP report process data by telephone - %s" , telephone),e);
            return new ReportProcessData();
        }
    }
    
    public List<ReportProcessDataDetail> getSalesSelfReportProcessRESDetailData(String telephone) throws Exception {
        try{
            return respirologyDAO.getSalesSelfReportProcessRESDetailData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no detail record found by the telephone - %s", telephone));
            return new ArrayList<ReportProcessDataDetail>();
        } catch(Exception e){
            logger.error(String.format("fail to get the sales report process detail data by telephone - %s" , telephone),e);
            return new ArrayList<ReportProcessDataDetail>();
        }
    }
    
    public ReportProcessData getDSMSelfReportProcessRESData(String telephone) throws Exception {
        try{
            return respirologyDAO.getDSMSelfReportProcessRESData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no record found by the telephone - %s", telephone));
            return new ReportProcessData();
        } catch(Exception e){
            logger.error(String.format("fail to get the DSM report process data by telephone - %s" , telephone),e);
            return new ReportProcessData();
        }
    }

    public List<ReportProcessDataDetail> getDSMSelfReportProcessRESDetailData(String telephone) throws Exception {
        try{
            return respirologyDAO.getDSMSelfReportProcessRESDetailData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no detail record found by the telephone - %s", telephone));
            return new ArrayList<ReportProcessDataDetail>();
        } catch(Exception e){
            logger.error(String.format("fail to get the DSM report process detail data by telephone - %s" , telephone),e);
            return new ArrayList<ReportProcessDataDetail>();
        }
    }
    
    public ReportProcessData getRSMSelfReportProcessRESData(String telephone) throws Exception {
    	try{
    		return respirologyDAO.getRSMSelfReportProcessRESData(telephone);
    	}catch(EmptyResultDataAccessException erd){
    		logger.info(String.format("there is no record found by the telephone - %s", telephone));
    		return new ReportProcessData();
    	} catch(Exception e){
    		logger.error(String.format("fail to get the RSM report process data by telephone - %s" , telephone),e);
    		return new ReportProcessData();
    	}
    }
    
    public List<ReportProcessDataDetail> getRSMSelfReportProcessRESDetailData(String telephone) throws Exception {
    	try{
    		return respirologyDAO.getRSMSelfReportProcessRESDetailData(telephone);
    	}catch(EmptyResultDataAccessException erd){
    		logger.info(String.format("there is no detail record found by the telephone - %s", telephone));
    		return new ArrayList<ReportProcessDataDetail>();
    	} catch(Exception e){
    		logger.error(String.format("fail to get the RSM report process detail data by telephone - %s" , telephone),e);
    		return new ArrayList<ReportProcessDataDetail>();
    	}
    }

	@Override
	public WeeklyRatioData getLowerWeeklyRESData4Mobile(UserInfo currentUser, String lowerUserCode) throws Exception {
		WeeklyRatioData resData = new WeeklyRatioData();
		try{
			if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
	            resData = respirologyDAO.getLowerWeeklyRESData4REPMobile(currentUser,lowerUserCode);
	        }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
	            resData = respirologyDAO.getLowerWeeklyRESData4DSMMobile(currentUser,lowerUserCode);
	        }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
            		|| LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())){
	            resData = respirologyDAO.getLowerWeeklyRESData4RSMMobile(currentUser,lowerUserCode);
	        }
			
			List<RateElement> rates = new ArrayList<RateElement>();
            RateElement re1 = new RateElement("res");
            re1.setRateType(1);
            re1.setRateNum(resData.getOmgRate());
            re1.setRateRatio(resData.getOmgRateRatio());
            
            RateElement re2 = new RateElement("res");
            re2.setRateType(2);
            re2.setRateNum(resData.getTmgRate());
            re2.setRateRatio(resData.getTmgRateRatio());
            
            RateElement re3 = new RateElement("res");
            re3.setRateType(3);
            re3.setRateNum(resData.getThmgRate());
            re3.setRateRatio(resData.getThmgRateRatio());
            
            RateElement re4 = new RateElement("res");
            re4.setRateType(4);
            re4.setRateNum(resData.getFmgRate());
            re4.setRateRatio(resData.getFmgRateRatio());
            
            RateElement re6 = new RateElement("res");
            re6.setRateType(6);
            re6.setRateNum(resData.getSmgRate());
            re6.setRateRatio(resData.getSmgRateRatio());
            
            RateElement re8 = new RateElement("res");
            re8.setRateType(8);
            re8.setRateNum(resData.getEmgRate());
            re8.setRateRatio(resData.getEmgRateRatio());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            rates.add(re6);
            rates.add(re8);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            resData.setFirstRate(rates.get(0));
            resData.setSecondRate(rates.get(1));
		}catch(Exception e){
			logger.error("fail to get the lower weekly res data,",e);
		}
		return resData;
	}
	
	public WeeklyRatioData getHospitalWeeklyRESData4Mobile(String hospitalCode) throws Exception {
        WeeklyRatioData hospitalData = new WeeklyRatioData();
        try{
            hospitalData = respirologyDAO.getHospitalWeeklyRESData4Mobile(hospitalCode);
            
            List<RateElement> rates = new ArrayList<RateElement>();
            RateElement re1 = new RateElement("res");
            re1.setRateType(1);
            re1.setRateNum(hospitalData.getOmgRate());
            re1.setRateRatio(hospitalData.getOmgRateRatio());
            
            RateElement re2 = new RateElement("res");
            re2.setRateType(2);
            re2.setRateNum(hospitalData.getTmgRate());
            re2.setRateRatio(hospitalData.getTmgRateRatio());
            
            RateElement re3 = new RateElement("res");
            re3.setRateType(3);
            re3.setRateNum(hospitalData.getThmgRate());
            re3.setRateRatio(hospitalData.getThmgRateRatio());
            
            RateElement re4 = new RateElement("res");
            re4.setRateType(4);
            re4.setRateNum(hospitalData.getFmgRate());
            re4.setRateRatio(hospitalData.getFmgRateRatio());
            
            RateElement re6 = new RateElement("res");
            re6.setRateType(6);
            re6.setRateNum(hospitalData.getSmgRate());
            re6.setRateRatio(hospitalData.getSmgRateRatio());
            
            RateElement re8 = new RateElement("res");
            re8.setRateType(8);
            re8.setRateNum(hospitalData.getEmgRate());
            re8.setRateRatio(hospitalData.getEmgRateRatio());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            rates.add(re6);
            rates.add(re8);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            hospitalData.setFirstRate(rates.get(0));
            hospitalData.setSecondRate(rates.get(1));
        }catch(EmptyResultDataAccessException e){
            logger.info(String.format("there is no record found by the hospitalCode %s", hospitalCode));
        }catch(Exception e){
            logger.error("fail to get the hospital weekly ratio data,",e);
        }
        return hospitalData;
    }
	
	public WeeklyRatioData getWeeklyRESCountoryData4Mobile() throws Exception {
		WeeklyRatioData weeklyRatioData = new WeeklyRatioData();
		try{
			weeklyRatioData = respirologyDAO.getHospitalWeeklyRESData4Mobile();
			
			List<RateElement> rates = new ArrayList<RateElement>();
			RateElement re1 = new RateElement("res");
			re1.setRateType(1);
			re1.setRateNum(weeklyRatioData.getOmgRate());
			re1.setRateRatio(weeklyRatioData.getOmgRateRatio());
			
			RateElement re2 = new RateElement("res");
			re2.setRateType(2);
			re2.setRateNum(weeklyRatioData.getTmgRate());
			re2.setRateRatio(weeklyRatioData.getTmgRateRatio());
			
			RateElement re3 = new RateElement("res");
			re3.setRateType(3);
			re3.setRateNum(weeklyRatioData.getThmgRate());
			re3.setRateRatio(weeklyRatioData.getThmgRateRatio());
			
			RateElement re4 = new RateElement("res");
			re4.setRateType(4);
			re4.setRateNum(weeklyRatioData.getFmgRate());
			re4.setRateRatio(weeklyRatioData.getFmgRateRatio());
			
			RateElement re6 = new RateElement("res");
			re6.setRateType(6);
			re6.setRateNum(weeklyRatioData.getSmgRate());
			re6.setRateRatio(weeklyRatioData.getSmgRateRatio());
			
			RateElement re8 = new RateElement("res");
			re8.setRateType(8);
			re8.setRateNum(weeklyRatioData.getEmgRate());
			re8.setRateRatio(weeklyRatioData.getEmgRateRatio());
			
			rates.add(re1);
			rates.add(re2);
			rates.add(re3);
			rates.add(re4);
			rates.add(re6);
			rates.add(re8);
			
			java.util.Collections.sort(rates, new RateElementComparator());
			
			weeklyRatioData.setFirstRate(rates.get(0));
			weeklyRatioData.setSecondRate(rates.get(1));
		}catch(EmptyResultDataAccessException e){
			logger.info(String.format("there is no record found by the countory"));
		}catch(Exception e){
			logger.error("fail to get the countory weekly ratio data,",e);
		}
		return weeklyRatioData;
	}
	
    public int removeOldWeeklyRESData(String duration) throws Exception{
        return respirologyDAO.removeOldWeeklyRESData(duration);
    }
	
    public void generateWeeklyRESDataOfHospital() throws Exception {
        respirologyDAO.generateWeeklyRESDataOfHospital();
    }
    
    public void generateWeeklyRESDataOfHospital(Date refreshDate) throws Exception {
        respirologyDAO.generateWeeklyRESDataOfHospital(refreshDate);
    }

    public boolean hasLastWeeklyRESData() throws Exception {
        int count = respirologyDAO.getLastWeeklyRESData();
        logger.info("the last week res data size is " + count);
        return count>0;
    }

    public List<RespirologyExportData> getResMonthExportData() throws Exception {
    	Date today = new Date();
    	String beginDuration = DateUtils.getMonthInRateBeginDuration(today);
    	String endDuration = DateUtils.getMonthInRateEndDuration(today);
    	logger.info(String.format("get res month export data, in this month, beginDuration is %s,endDuration is %s ", beginDuration,endDuration));
    	
    	String latestDurationInDB = respirologyDAO.getLatestDuration();
    	int beginCompare = latestDurationInDB.compareTo(beginDuration);
    	int endCompare = latestDurationInDB.compareTo(endDuration);
    	logger.info(String.format("beginCompare is %s, endCompare is %s", beginCompare,endCompare));
    	
    	String lastMonthDuration = beginDuration;
    	String lastWeekDuration = "";
    	//DB里最新的duration在本月的开始和结束之间
    	if( beginCompare >= 0 && endCompare < 0 ){
    		lastWeekDuration = beginDuration;
    	}
    	
    	List<RespirologyMonthDBData> monthDBData = respirologyDAO.getRESMonthReportDBData(lastMonthDuration);
    	List<RespirologyMonthDBData> monthWeeklyDBData = new ArrayList<RespirologyMonthDBData>();
    	if( null != lastWeekDuration && !"".equalsIgnoreCase(lastWeekDuration) ){
    		monthWeeklyDBData = respirologyDAO.getRESMonthReportWeeklyDBData(lastWeekDuration);
    	}
    	
        List<RespirologyExportData> exportData = new ArrayList<RespirologyExportData>();
        
        Set<String> allRSM = new LinkedHashSet<String>();
        for( RespirologyMonthDBData resData : monthDBData ){
            allRSM.add(resData.getRsmRegion());
        }
        
        Map<String, Double> pNumMap = new LinkedHashMap<String, Double>();
        Map<String, Double> lsNumMap = new LinkedHashMap<String, Double>();
        Map<String, Double> whRateMap = new LinkedHashMap<String, Double>();
        Map<String, Double> inRateMap = new LinkedHashMap<String, Double>();
        Map<String, Double> aeNumMap = new LinkedHashMap<String, Double>();
        Map<String, Double> averageDoseMap = new LinkedHashMap<String, Double>();
        Map<String, Double> whDaysMap = new LinkedHashMap<String, Double>();
        
        for( String rsmRegion : allRSM ){
            RespirologyExportData rsmData = new RespirologyExportData();
            pNumMap = new LinkedHashMap<String, Double>();
            lsNumMap = new LinkedHashMap<String, Double>();
            aeNumMap = new LinkedHashMap<String, Double>();
            inRateMap = new LinkedHashMap<String, Double>();
            whRateMap = new LinkedHashMap<String, Double>();
            averageDoseMap = new LinkedHashMap<String, Double>();
            whDaysMap = new LinkedHashMap<String, Double>();
            
            rsmData.setRsmRegion(rsmRegion);
            
            for( RespirologyMonthDBData resData : monthDBData ){
                if( resData.getRsmRegion().equalsIgnoreCase(rsmRegion) ){
                    
                    rsmData.setRsmName(resData.getRsmName());
                    
                    pNumMap.put(resData.getDataYear()+"年"+resData.getDataMonth()+"月", resData.getPnum()/resData.getWeeklyCount());
                    lsNumMap.put(resData.getDataYear()+"年"+resData.getDataMonth()+"月", resData.getLsnum()/resData.getWeeklyCount());
                    aeNumMap.put(resData.getDataYear()+"年"+resData.getDataMonth()+"月", resData.getAenum()/resData.getWeeklyCount());
                    inRateMap.put(resData.getDataYear()+"年"+resData.getDataMonth()+"月", resData.getInRate());
                    whRateMap.put(resData.getDataYear()+"年"+resData.getDataMonth()+"月", resData.getWhRate());
                    averageDoseMap.put(resData.getDataYear()+"年"+resData.getDataMonth()+"月", resData.getAverageDose());
                    whDaysMap.put(resData.getDataYear()+"年"+resData.getDataMonth()+"月", resData.getWhDays());
                }
            }
            
            if( null != monthWeeklyDBData && monthWeeklyDBData.size() > 0 ){
            	for( RespirologyMonthDBData resData : monthWeeklyDBData ){
                    if( resData.getRsmRegion().equalsIgnoreCase(rsmRegion) ){
                        rsmData.setRsmName(resData.getRsmName());
                        
                        pNumMap.put(resData.getDuration().substring(5, 11)+resData.getDuration().substring(16), resData.getPnum());
                        lsNumMap.put(resData.getDuration().substring(5, 11)+resData.getDuration().substring(16), resData.getLsnum());
                        aeNumMap.put(resData.getDuration().substring(5, 11)+resData.getDuration().substring(16), resData.getAenum());
                        inRateMap.put(resData.getDuration().substring(5, 11)+resData.getDuration().substring(16), resData.getInRate());
                        whRateMap.put(resData.getDuration().substring(5, 11)+resData.getDuration().substring(16), resData.getWhRate());
                        averageDoseMap.put(resData.getDuration().substring(5, 11)+resData.getDuration().substring(16), resData.getAverageDose());
                    }
                }
            }
            
            List<String> durations = new ArrayList<String>(inRateMap.keySet());
            List<Double> values = new ArrayList<Double>(inRateMap.values());
            inRateMap.put(durations.get(durations.size()-3)+"到"+durations.get(durations.size()-2), values.get(values.size()-2)-values.get(values.size()-3));
            inRateMap.put(durations.get(durations.size()-2)+"到"+durations.get(durations.size()-1), values.get(values.size()-1)-values.get(values.size()-2));
            
            durations = new ArrayList<String>(whRateMap.keySet());
            values = new ArrayList<Double>(whRateMap.values());
            whRateMap.put(durations.get(durations.size()-3)+"到"+durations.get(durations.size()-2), values.get(values.size()-2)-values.get(values.size()-3));
            whRateMap.put(durations.get(durations.size()-2)+"到"+durations.get(durations.size()-1), values.get(values.size()-1)-values.get(values.size()-2));
            
            durations = new ArrayList<String>(lsNumMap.keySet());
            values = new ArrayList<Double>(lsNumMap.values());
            lsNumMap.put(durations.get(durations.size()-3)+"到"+durations.get(durations.size()-2), (values.get(values.size()-2)-values.get(values.size()-3))/values.get(values.size()-3));
            lsNumMap.put(durations.get(durations.size()-2)+"到"+durations.get(durations.size()-1), (values.get(values.size()-1)-values.get(values.size()-2))/values.get(values.size()-2));
            
            rsmData.setpNumMap(pNumMap);
            rsmData.setLsNumMap(lsNumMap);
            rsmData.setAeNumMap(aeNumMap);
            rsmData.setInRateMap(inRateMap);
            rsmData.setWhRateMap(whRateMap);
            rsmData.setAverageDoseMap(averageDoseMap);
            rsmData.setWhDaysMap(whDaysMap);
            
            exportData.add(rsmData);
        }
        
        return exportData;
    }
}
