package com.chalet.lskpi.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.chalet.lskpi.dao.PediatricsDAO;
import com.chalet.lskpi.model.DailyReportData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobilePEDDailyData;
import com.chalet.lskpi.model.PediatricsData;
import com.chalet.lskpi.model.RateElement;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyRatioData;
import com.chalet.lskpi.utils.LsAttributes;

/**
 * @author Chalet
 * @version 创建时间：2013年11月27日 下午11:42:40
 * 类说明
 */

@Service("pediatricsService")
public class PediatricsServiceImpl implements PediatricsService {

	@Autowired
	@Qualifier("pediatricsDAO")
	private PediatricsDAO pediatricsDAO;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("hospitalService")
	private HospitalService hospitalService;
	
	private Logger logger = Logger.getLogger(PediatricsServiceImpl.class);
	

    public MobilePEDDailyData getDailyPEDParentData4Mobile(String telephone, String level) throws Exception {
        MobilePEDDailyData mpd = new MobilePEDDailyData();
        
        switch (level) {
            case LsAttributes.USER_LEVEL_BM:
                mpd = pediatricsDAO.getDailyPEDData4CountoryMobile();
                logger.info(String.format("end to get the ped daily data of the countory, current telephone is %s", telephone));
                List<RateElement> rates = new ArrayList<RateElement>();
                RateElement re1 = new RateElement("ped");
                re1.setRateType(1);
                re1.setRateNum(mpd.getHmgRate());
                
                RateElement re2 = new RateElement("ped");
                re2.setRateType(2);
                re2.setRateNum(mpd.getOmgRate());
                
                RateElement re3 = new RateElement("ped");
                re3.setRateType(3);
                re3.setRateNum(mpd.getTmgRate());
                
                RateElement re4 = new RateElement("ped");
                re4.setRateType(4);
                re4.setRateNum(mpd.getFmgRate());
                
                rates.add(re1);
                rates.add(re2);
                rates.add(re3);
                rates.add(re4);
                
                java.util.Collections.sort(rates, new RateElementComparator());
                
                mpd.setFirstRate(rates.get(0));
                mpd.setSecondRate(rates.get(1));
                mpd.setInRate(mpd.getHosNum()==0?0:(double)mpd.getInNum()/mpd.getHosNum());
                mpd.setWhRate(mpd.getPatNum()==0?0:(double)mpd.getLsNum()/mpd.getPatNum());
                break;
            default:
                mpd = null;
                break;
        }
        return mpd;
    }

    public ReportProcessData getSalesSelfReportProcessPEDData(String telephone) throws Exception {
        try{
            return pediatricsDAO.getSalesSelfReportProcessPEDData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return new ReportProcessData();
        } catch(Exception e){
            logger.error(String.format("fail to get the REP report process data by telephone - %s" , telephone),e);
            return new ReportProcessData();
        }
    }
    
    public List<ReportProcessDataDetail> getSalesSelfReportProcessPEDDetailData(String telephone) throws Exception {
        try{
            return pediatricsDAO.getSalesSelfReportProcessPEDDetailData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no detail record found by the telephone - %s", telephone));
            return new ArrayList<ReportProcessDataDetail>();
        } catch(Exception e){
            logger.error(String.format("fail to get the sales report process detail data by telephone - %s" , telephone),e);
            return new ArrayList<ReportProcessDataDetail>();
        }
    }
    
    public ReportProcessData getDSMSelfReportProcessPEDData(String telephone) throws Exception {
        try{
            return pediatricsDAO.getDSMSelfReportProcessPEDData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no record found by the telephone - %s", telephone));
            return new ReportProcessData();
        } catch(Exception e){
            logger.error(String.format("fail to get the DSM report process data by telephone - %s" , telephone),e);
            return new ReportProcessData();
        }
    }

    public List<ReportProcessDataDetail> getDSMSelfReportProcessPEDDetailData(String telephone) throws Exception {
        try{
            return pediatricsDAO.getDSMSelfReportProcessPEDDetailData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no detail record found by the telephone - %s", telephone));
            return new ArrayList<ReportProcessDataDetail>();
        } catch(Exception e){
            logger.error(String.format("fail to get the DSM report process detail data by telephone - %s" , telephone),e);
            return new ArrayList<ReportProcessDataDetail>();
        }
    }
    
    public ReportProcessData getRSMSelfReportProcessPEDData(String telephone) throws Exception {
    	try{
    		return pediatricsDAO.getRSMSelfReportProcessPEDData(telephone);
    	}catch(EmptyResultDataAccessException erd){
    		logger.info(String.format("there is no record found by the telephone - %s", telephone));
    		return new ReportProcessData();
    	} catch(Exception e){
    		logger.error(String.format("fail to get the RSM report process data by telephone - %s" , telephone),e);
    		return new ReportProcessData();
    	}
    }
    
    public List<ReportProcessDataDetail> getRSMSelfReportProcessPEDDetailData(String telephone) throws Exception {
    	try{
    		return pediatricsDAO.getRSMSelfReportProcessPEDDetailData(telephone);
    	}catch(EmptyResultDataAccessException erd){
    		logger.info(String.format("there is no detail record found by the telephone - %s", telephone));
    		return new ArrayList<ReportProcessDataDetail>();
    	} catch(Exception e){
    		logger.error(String.format("fail to get the RSM report process detail data by telephone - %s" , telephone),e);
    		return new ArrayList<ReportProcessDataDetail>();
    	}
    }
	
	@Override
	public PediatricsData getPediatricsDataByHospital(String hospitalCode)
			throws Exception {
		try{
	        return pediatricsDAO.getPediatricsDataByHospital(hospitalCode);
	    } catch(EmptyResultDataAccessException erd){
	        logger.info("there is no record found.");
	        return null;
	    } catch(Exception e){
	        logger.error("fail to get the pediatrics data by hospital - " + hospitalCode,e);
	        return null;
	    }
	}

	@Override
	public PediatricsData getPediatricsDataById(int id) throws Exception {
		try{
            return pediatricsDAO.getPediatricsDataById(id);
        }catch(Exception e){
            logger.error("fail to get the pediatrics data by ID - " + id,e);
            return null;
        }
	}

	@Override
	public void insert(PediatricsData pediatricsData, UserInfo operator, Hospital hospital) throws Exception {
		pediatricsDAO.insert(pediatricsData, operator, hospital);
	}

    @Override
    public void insert(PediatricsData pediatricsData) throws Exception {
    	String dsmCode = "";
        try{
        	UserInfo primarySales = hospitalService.getPrimarySalesOfHospital(pediatricsData.getHospitalCode());
        	if( null != primarySales ){
        	    pediatricsData.setSalesETMSCode(primarySales.getUserCode());
        	    dsmCode = (primarySales.getSuperior()==null||"".equalsIgnoreCase(primarySales.getSuperior()))?primarySales.getUserCode():primarySales.getSuperior();
        	}
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no user found whose code is " + pediatricsData.getSalesETMSCode());
        }
        pediatricsDAO.insert(pediatricsData, dsmCode);
    }

	@Override
	public void update(PediatricsData pediatricsData, UserInfo operator)
			throws Exception {
		pediatricsDAO.update(pediatricsData, operator);
	}

	@Override
	public List<PediatricsData> getPediatricsDataByDate(Date createdatebegin, Date createdateend) throws Exception {
		try{
			return pediatricsDAO.getPediatricsDataByDate(createdatebegin,createdateend);
		}catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return new ArrayList<PediatricsData>();
        } catch(Exception e){
	        logger.error("fail to get the Pediatrics data by date - " + createdatebegin,e);
	        return new ArrayList<PediatricsData>();
	    }
	}

	@Override
	public PediatricsData getPediatricsDataByHospitalAndDate(String hospitalName, Date createdate) throws Exception {
		try{
			return pediatricsDAO.getPediatricsDataByHospitalAndDate(hospitalName, createdate);
		}catch(EmptyResultDataAccessException erd){
	        logger.info("there is no record found.");
	        return null;
	    } catch(IncorrectResultSizeDataAccessException ire){
	        logger.error(ire.getMessage());
	        PediatricsData defaultData = new PediatricsData();
	        defaultData.setDataId(0);
	        return defaultData;
	    }catch(Exception e){
	        logger.error("fail to get the pediatrics data by hospital - " + hospitalName,e);
	        PediatricsData defaultData = new PediatricsData();
            defaultData.setDataId(0);
            return defaultData;
	    }
	}

	public List<MobilePEDDailyData> getDailyPEDData4MobileByRegion(String region) throws Exception{
    	List<MobilePEDDailyData> pedDatas = new ArrayList<MobilePEDDailyData>();
    	
		pedDatas = pediatricsDAO.getDailyPEDData4RSMByRegion(region);
        
        for( MobilePEDDailyData pedDailyData : pedDatas ){
            List<RateElement> rates = new ArrayList<RateElement>();
        	RateElement re1 = new RateElement("ped");
        	re1.setRateType(1);
        	re1.setRateNum(pedDailyData.getHmgRate());
        	
        	RateElement re2 = new RateElement("ped");
        	re2.setRateType(2);
        	re2.setRateNum(pedDailyData.getOmgRate());
        	
        	RateElement re3 = new RateElement("ped");
        	re3.setRateType(3);
        	re3.setRateNum(pedDailyData.getTmgRate());
        	
        	RateElement re4 = new RateElement("ped");
        	re4.setRateType(4);
        	re4.setRateNum(pedDailyData.getFmgRate());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            pedDailyData.setFirstRate(rates.get(0));
            pedDailyData.setSecondRate(rates.get(1));
            pedDailyData.setInRate(pedDailyData.getHosNum()==0?0:(double)pedDailyData.getInNum()/pedDailyData.getHosNum());
            pedDailyData.setWhRate(pedDailyData.getPatNum()==0?0:(double)pedDailyData.getLsNum()/pedDailyData.getPatNum());
        }
        
        return pedDatas;
	}
	
    public List<MobilePEDDailyData> getDailyPEDData4Mobile(String telephone, UserInfo currentUser) throws Exception {
    	List<MobilePEDDailyData> pedDatas = new ArrayList<MobilePEDDailyData>();
    	if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
    		pedDatas = pediatricsDAO.getDailyPEDData4DSMMobile(telephone);
    	}else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
    		pedDatas = pediatricsDAO.getDailyPEDData4RSMMobile(telephone);
    	}else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
    			|| LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
    		pedDatas = pediatricsDAO.getDailyPEDData4RSDMobile();
    	}
    	logger.info(String.format("end to get the ped daily data...current telephone is %s", telephone));
    	List<MobilePEDDailyData> orderedPedData = new ArrayList<MobilePEDDailyData>();
    	List<MobilePEDDailyData> leftPedData = new ArrayList<MobilePEDDailyData>();
        
        for( MobilePEDDailyData pedDailyData : pedDatas ){
            List<RateElement> rates = new ArrayList<RateElement>();
        	RateElement re1 = new RateElement("ped");
        	re1.setRateType(1);
        	re1.setRateNum(pedDailyData.getHmgRate());
        	
        	RateElement re2 = new RateElement("ped");
        	re2.setRateType(2);
        	re2.setRateNum(pedDailyData.getOmgRate());
        	
        	RateElement re3 = new RateElement("ped");
        	re3.setRateType(3);
        	re3.setRateNum(pedDailyData.getTmgRate());
        	
        	RateElement re4 = new RateElement("ped");
        	re4.setRateType(4);
        	re4.setRateNum(pedDailyData.getFmgRate());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            pedDailyData.setFirstRate(rates.get(0));
            pedDailyData.setSecondRate(rates.get(1));
            pedDailyData.setInRate(pedDailyData.getHosNum()==0?0:(double)pedDailyData.getInNum()/pedDailyData.getHosNum());
            pedDailyData.setWhRate(pedDailyData.getPatNum()==0?0:(double)pedDailyData.getLsNum()/pedDailyData.getPatNum());
        
            if( pedDailyData.getHosNum() != 0 ){
                if( null != currentUser && null != pedDailyData.getUserCode() 
                        && pedDailyData.getUserCode().equalsIgnoreCase(currentUser.getUserCode()) ){
                    orderedPedData.add(0,pedDailyData);
                }else{
                    leftPedData.add(pedDailyData);
                }
            }
            
        }
        
        if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
        	orderedPedData.addAll(leftPedData);
        }else{
        	orderedPedData.addAll(1,leftPedData);
        }
        logger.info(String.format("end to populate the ped daily data...current telephone is %s", telephone));
        return orderedPedData;
    }
    
    public List<MobilePEDDailyData> getDailyPEDChildData4Mobile(String telephone) throws Exception {
        UserInfo userInfo = userService.getUserInfoByTel(telephone);
        List<MobilePEDDailyData> pedDatas = new ArrayList<MobilePEDDailyData>();
        
        List<MobilePEDDailyData> filteredPedDatas = new ArrayList<MobilePEDDailyData>();
        
        if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(userInfo.getLevel()) ){
            pedDatas = pediatricsDAO.getDailyPEDChildData4DSMMobile(telephone);
        }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(userInfo.getLevel()) ){
            pedDatas = pediatricsDAO.getDailyPEDChildData4RSMMobile(telephone);
        }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(userInfo.getLevel()) ){
            pedDatas = pediatricsDAO.getDailyPEDChildData4RSDMobile(telephone);
        }
        
        for( MobilePEDDailyData pedDailyData : pedDatas ){
            List<RateElement> rates = new ArrayList<RateElement>();
            RateElement re1 = new RateElement("ped");
            re1.setRateType(1);
            re1.setRateNum(pedDailyData.getHmgRate());
            
            RateElement re2 = new RateElement("ped");
            re2.setRateType(2);
            re2.setRateNum(pedDailyData.getOmgRate());
            
            RateElement re3 = new RateElement("ped");
            re3.setRateType(3);
            re3.setRateNum(pedDailyData.getTmgRate());
            
            RateElement re4 = new RateElement("ped");
            re4.setRateType(4);
            re4.setRateNum(pedDailyData.getFmgRate());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            pedDailyData.setFirstRate(rates.get(0));
            pedDailyData.setSecondRate(rates.get(1));
            pedDailyData.setInRate(pedDailyData.getHosNum()==0?0:(double)pedDailyData.getInNum()/pedDailyData.getHosNum());
            pedDailyData.setWhRate(pedDailyData.getPatNum()==0?0:(double)pedDailyData.getLsNum()/pedDailyData.getPatNum());
            
            if( pedDailyData.getHosNum() != 0 ){
                filteredPedDatas.add(pedDailyData);
            }
            
        }
        
        return filteredPedDatas;
    }
    
	@Override
	public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception {
		return pediatricsDAO.getTopAndBottomRSMData();
	}
    
    @Override
    public TopAndBottomRSMData getTopAndBottomInRateRSMData(String telephone) throws Exception {
    	List<DailyReportData> allRSMData = pediatricsDAO.getAllRSMDataByTelephone();
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
    	List<DailyReportData> allRSMData = pediatricsDAO.getAllRSMDataByTelephone();
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
    	List<DailyReportData> allRSMData = pediatricsDAO.getAllRSMDataByTelephone();
    	java.util.Collections.sort(allRSMData, new DailyReportDataAverageComparator());
    	
    	TopAndBottomRSMData topAndBottomRSMAverageData = new TopAndBottomRSMData();
    	topAndBottomRSMAverageData.setTopRSMName(allRSMData.get(0).getRsmName());
    	topAndBottomRSMAverageData.setTopRSMAverageDose(allRSMData.get(0).getAverageDose());
    	topAndBottomRSMAverageData.setBottomRSMName(allRSMData.get(allRSMData.size()-1).getRsmName());
    	topAndBottomRSMAverageData.setBottomRSMAverageDose(allRSMData.get(allRSMData.size()-1).getAverageDose());
    	
    	return topAndBottomRSMAverageData;
    }
    
    public List<WeeklyRatioData> getWeeklyPEDData4Mobile(String telephone) throws Exception {
        UserInfo userInfo = userService.getUserInfoByTel(telephone);
        List<WeeklyRatioData> pedDatas = new ArrayList<WeeklyRatioData>();
        if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(userInfo.getLevel()) ){
            pedDatas = pediatricsDAO.getWeeklyPEDData4DSMMobile(telephone);
        }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(userInfo.getLevel()) ){
            pedDatas = pediatricsDAO.getWeeklyPEDData4RSMMobile(telephone);
        }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(userInfo.getLevel())  
    			|| LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(userInfo.getLevel()) ){
            pedDatas = pediatricsDAO.getWeeklyPEDData4RSDMobile();
        }
        
        List<WeeklyRatioData> orderedPedData = new ArrayList<WeeklyRatioData>();
        List<WeeklyRatioData> leftPedData = new ArrayList<WeeklyRatioData>();
        
        for( WeeklyRatioData pedWeeklyData : pedDatas ){
            List<RateElement> rates = new ArrayList<RateElement>();
            RateElement re1 = new RateElement("ped");
            re1.setRateType(1);
            re1.setRateNum(pedWeeklyData.getHmgRate());
            re1.setRateRatio(pedWeeklyData.getHmgRateRatio());
            
            RateElement re2 = new RateElement("ped");
            re2.setRateType(2);
            re2.setRateNum(pedWeeklyData.getOmgRate());
            re2.setRateRatio(pedWeeklyData.getOmgRateRatio());
            
            RateElement re3 = new RateElement("ped");
            re3.setRateType(3);
            re3.setRateNum(pedWeeklyData.getTmgRate());
            re3.setRateRatio(pedWeeklyData.getTmgRateRatio());
            
            RateElement re4 = new RateElement("ped");
            re4.setRateType(4);
            re4.setRateNum(pedWeeklyData.getFmgRate());
            re4.setRateRatio(pedWeeklyData.getFmgRateRatio());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            pedWeeklyData.setFirstRate(rates.get(0));
            pedWeeklyData.setSecondRate(rates.get(1));
        
            boolean isSelf = false;
            if( null != userInfo && null != pedWeeklyData.getUserCode() ){
            	if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(userInfo.getLevel())
            			&& pedWeeklyData.getUserCode().equalsIgnoreCase(userInfo.getUserCode())){
            		isSelf = true;
            	}
            	if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(userInfo.getLevel())
            			&& pedWeeklyData.getUserCode().equalsIgnoreCase(userInfo.getRegion())){
            		isSelf = true;
            	}
            	if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(userInfo.getLevel())
            			&& pedWeeklyData.getUserCode().equalsIgnoreCase(userInfo.getRegionCenter())){
            		isSelf = true;
            	}
            }
            
            if( isSelf ){
            	orderedPedData.add(0,pedWeeklyData);
            }else{
                leftPedData.add(pedWeeklyData);
            }
        }
        
        if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(userInfo.getLevel()) ){
        	orderedPedData.addAll(leftPedData);
        }else{
        	orderedPedData.addAll(1,leftPedData);
        }
        
        return orderedPedData;
    }
    
    public WeeklyRatioData getHospitalWeeklyPEDData4Mobile(String hospitalCode) throws Exception {
        WeeklyRatioData hospitalWeeklyRatioData = new WeeklyRatioData();
        try{
            hospitalWeeklyRatioData = pediatricsDAO.getHospitalWeeklyPEDData4Mobile(hospitalCode);
            
            List<RateElement> rates = new ArrayList<RateElement>();
            RateElement re1 = new RateElement("ped");
            re1.setRateType(1);
            re1.setRateNum(hospitalWeeklyRatioData.getHmgRate());
            re1.setRateRatio(hospitalWeeklyRatioData.getHmgRateRatio());
            
            RateElement re2 = new RateElement("ped");
            re2.setRateType(2);
            re2.setRateNum(hospitalWeeklyRatioData.getOmgRate());
            re2.setRateRatio(hospitalWeeklyRatioData.getOmgRateRatio());
            
            RateElement re3 = new RateElement("ped");
            re3.setRateType(3);
            re3.setRateNum(hospitalWeeklyRatioData.getTmgRate());
            re3.setRateRatio(hospitalWeeklyRatioData.getTmgRateRatio());
            
            RateElement re4 = new RateElement("ped");
            re4.setRateType(4);
            re4.setRateNum(hospitalWeeklyRatioData.getFmgRate());
            re4.setRateRatio(hospitalWeeklyRatioData.getFmgRateRatio());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            hospitalWeeklyRatioData.setFirstRate(rates.get(0));
            hospitalWeeklyRatioData.setSecondRate(rates.get(1));
        }catch(EmptyResultDataAccessException e){
            logger.info(String.format("there is no record found by the hospitalCode %s", hospitalCode));
        }catch(Exception e){
            logger.error("fail to get the hospital weekly ratio data,",e);
        }
            
        return hospitalWeeklyRatioData;
    }
    public WeeklyRatioData getWeeklyPEDCountoryData4Mobile() throws Exception {
    	WeeklyRatioData weeklyRatioData = new WeeklyRatioData();
    	try{
    		weeklyRatioData = pediatricsDAO.getWeeklyPEDCountoryData4Mobile();
    		
    		List<RateElement> rates = new ArrayList<RateElement>();
    		RateElement re1 = new RateElement("ped");
    		re1.setRateType(1);
    		re1.setRateNum(weeklyRatioData.getHmgRate());
    		re1.setRateRatio(weeklyRatioData.getHmgRateRatio());
    		
    		RateElement re2 = new RateElement("ped");
    		re2.setRateType(2);
    		re2.setRateNum(weeklyRatioData.getOmgRate());
    		re2.setRateRatio(weeklyRatioData.getOmgRateRatio());
    		
    		RateElement re3 = new RateElement("ped");
    		re3.setRateType(3);
    		re3.setRateNum(weeklyRatioData.getTmgRate());
    		re3.setRateRatio(weeklyRatioData.getTmgRateRatio());
    		
    		RateElement re4 = new RateElement("ped");
    		re4.setRateType(4);
    		re4.setRateNum(weeklyRatioData.getFmgRate());
    		re4.setRateRatio(weeklyRatioData.getFmgRateRatio());
    		
    		rates.add(re1);
    		rates.add(re2);
    		rates.add(re3);
    		rates.add(re4);
    		
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
    

	@Override
	public WeeklyRatioData getLowerWeeklyPEDData4Mobile(UserInfo currentUser, String lowerUserCode) throws Exception {
		WeeklyRatioData pedData = new WeeklyRatioData();
        try{
        	if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
        		pedData = pediatricsDAO.getLowerWeeklyPEDData4REPMobile(currentUser,lowerUserCode);
        	}else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
        		pedData = pediatricsDAO.getLowerWeeklyPEDData4DSMMobile(currentUser,lowerUserCode);
        	}else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
        			|| LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())){
        		pedData = pediatricsDAO.getLowerWeeklyPEDData4RSMMobile(currentUser,lowerUserCode);
        	}
        	
        	List<RateElement> rates = new ArrayList<RateElement>();
            RateElement re1 = new RateElement("ped");
            re1.setRateType(1);
            re1.setRateNum(pedData.getHmgRate());
            re1.setRateRatio(pedData.getHmgRateRatio());
            
            RateElement re2 = new RateElement("ped");
            re2.setRateType(2);
            re2.setRateNum(pedData.getOmgRate());
            re2.setRateRatio(pedData.getOmgRateRatio());
            
            RateElement re3 = new RateElement("ped");
            re3.setRateType(3);
            re3.setRateNum(pedData.getTmgRate());
            re3.setRateRatio(pedData.getTmgRateRatio());
            
            RateElement re4 = new RateElement("ped");
            re4.setRateType(4);
            re4.setRateNum(pedData.getFmgRate());
            re4.setRateRatio(pedData.getFmgRateRatio());
            
            rates.add(re1);
            rates.add(re2);
            rates.add(re3);
            rates.add(re4);
            
            java.util.Collections.sort(rates, new RateElementComparator());
            
            pedData.setFirstRate(rates.get(0));
            pedData.setSecondRate(rates.get(1));
        }catch(Exception e){
        	logger.error("fail to get the lower weekly ped data,",e);
        }
        return pedData;
	}
	
    public void generateWeeklyPEDDataOfHospital() throws Exception {
        pediatricsDAO.generateWeeklyPEDDataOfHospital();
    }
    
    public int removeOldWeeklyPEDData(String duration) throws Exception{
        return pediatricsDAO.removeOldWeeklyPEDData(duration);
    }
    
    public void generateWeeklyPEDDataOfHospital(Date refreshDate) throws Exception {
        pediatricsDAO.generateWeeklyPEDDataOfHospital(refreshDate);
    }

    public boolean hasLastWeeklyPEDData() throws Exception {
        int count = pediatricsDAO.getLastWeeklyPEDData();
        logger.info("the last week ped data size is " + count);
        return count>0;
    }
    
    static class RateElementComparator implements Comparator<RateElement>{

		@Override
		public int compare(RateElement o1, RateElement o2) {
			Double o1value = o1.getRateNum();
			Double o2value = o2.getRateNum();
			if( o1value.compareTo(o2value) != 0 ){
				return o2value.compareTo(o1value);
			}else{
				Integer o1type = o1.getRateType();
				Integer o2type = o2.getRateType();
				return o2type.compareTo(o1type);
			}
		}
    }
    
    static class DailyReportDataInRateComparator implements Comparator<DailyReportData>{

		@Override
		public int compare(DailyReportData o1, DailyReportData o2) {
			Double o1value = o1.getInRate();
			Double o2value = o2.getInRate();
			return o2value.compareTo(o1value);
		}
    }
    
    static class DailyReportDataWhRateComparator implements Comparator<DailyReportData>{
    	
    	@Override
    	public int compare(DailyReportData o1, DailyReportData o2) {
    		Double o1value = o1.getWhRate();
    		Double o2value = o2.getWhRate();
    		return o2value.compareTo(o1value);
    	}
    }
    
    static class DailyReportDataAverageComparator implements Comparator<DailyReportData>{
    	
    	@Override
    	public int compare(DailyReportData o1, DailyReportData o2) {
    		Double o1value = o1.getAverageDose();
    		Double o2value = o2.getAverageDose();
			return o2value.compareTo(o1value);
    	}
    }
    
    public static void main(String[] args){
    	/**
    	 * 
    	List<RateElement> rates = new ArrayList<RateElement>();
    	RateElement re1 = new RateElement();
    	re1.setRateType(1);
    	re1.setRateNum(0.30);
    	
    	RateElement re2 = new RateElement();
    	re2.setRateType(2);
    	re2.setRateNum(0.10);
    	
    	RateElement re3 = new RateElement();
    	re3.setRateType(3);
    	re3.setRateNum(0.30);
    	
    	RateElement re4 = new RateElement();
    	re4.setRateType(4);
    	re4.setRateNum(0.30);
        
        rates.add(re1);
        rates.add(re2);
        rates.add(re3);
        rates.add(re4);
        
        for( RateElement re : rates ){
        	System.out.println(re.getRateType()+"--"+re.getRateNum());
        }
        
        java.util.Collections.sort(rates, new RateElementComparator());
        System.out.println("------------------split--------------------");
        for( RateElement re : rates ){
        	System.out.println(re.getRateType()+"--"+re.getRateNum());
        }
    	 */
    	List<DailyReportData> allRSMData = new ArrayList<DailyReportData>();
    	DailyReportData d1 = new DailyReportData();
    	d1.setRsmName("a");
    	d1.setInRate(0.54);
    	d1.setWhRate(0.11);
    	d1.setAverageDose(1.35f);
    	
    	DailyReportData d2 = new DailyReportData();
    	d2.setRsmName("b");
    	d2.setInRate(0.90);
    	d2.setWhRate(0.33);
    	d2.setAverageDose(1.8f);
    	
    	DailyReportData d3 = new DailyReportData();
    	d3.setRsmName("c");
    	d3.setInRate(0.30);
    	d3.setWhRate(0.12);
    	d3.setAverageDose(3.3f);
    	
    	allRSMData.add(d1);
    	allRSMData.add(d2);
    	allRSMData.add(d3);
    	
    	java.util.Collections.sort(allRSMData, new DailyReportDataInRateComparator());
    	System.out.println(allRSMData.get(0).getRsmName());
    	System.out.println(allRSMData.get(2).getRsmName());
    	System.out.println("-------------------------wh--------");
    	java.util.Collections.sort(allRSMData, new DailyReportDataWhRateComparator());
    	System.out.println(allRSMData.get(0).getRsmName());
    	System.out.println(allRSMData.get(2).getRsmName());
    	System.out.println("-------------------------average--------");
    	java.util.Collections.sort(allRSMData, new DailyReportDataAverageComparator());
    	System.out.println(allRSMData.get(0).getRsmName());
    	System.out.println(allRSMData.get(2).getRsmName());
    }
}
