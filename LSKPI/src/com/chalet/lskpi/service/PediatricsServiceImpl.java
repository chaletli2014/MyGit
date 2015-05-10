package com.chalet.lskpi.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chalet.lskpi.comparator.DailyReportDataAverageComparator;
import com.chalet.lskpi.comparator.DailyReportDataInRateComparator;
import com.chalet.lskpi.comparator.DailyReportDataWhRateComparator;
import com.chalet.lskpi.comparator.RateElementComparator;
import com.chalet.lskpi.dao.PediatricsDAO;
import com.chalet.lskpi.model.DailyReportData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobilePEDDailyData;
import com.chalet.lskpi.model.MonthlyStatisticsData;
import com.chalet.lskpi.model.PediatricsData;
import com.chalet.lskpi.model.RateElement;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyRatioData;
import com.chalet.lskpi.utils.DateUtils;
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
	

    public MobilePEDDailyData getDailyPEDParentData4Mobile(String telephone, String level, String hospitalShownFlag) throws Exception {
        MobilePEDDailyData mpd = new MobilePEDDailyData();
        
        Date date = new Date();
        Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
        
        switch (level) {
            case LsAttributes.USER_LEVEL_BM:
            	
            	MobilePEDDailyData pedCoreData = new MobilePEDDailyData();
        		pedCoreData = pediatricsDAO.getDailyCorePEDData4CountoryMobile(paramDate,hospitalShownFlag);
        		
        		MobilePEDDailyData pedEmergingData = new MobilePEDDailyData();
        		pedEmergingData = pediatricsDAO.getDailyEmergingPEDData4CountoryMobile(paramDate,hospitalShownFlag);
        		
        		MobilePEDDailyData pedWhPortData = new MobilePEDDailyData();
        		pedWhPortData = pediatricsDAO.getDailyPEDWhPortData4CountoryMobile(paramDate,hospitalShownFlag);
            	
                mpd = pediatricsDAO.getDailyPEDData4CountoryMobile(paramDate,hospitalShownFlag);
                logger.info(String.format("end to get the ped daily data of the country, current telephone is %s", telephone));
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
                
                
                mpd.setCoreInRate(pedCoreData.getCoreInRate());
                mpd.setCoreWhRate(pedCoreData.getCoreWhRate());
                mpd.setWhPortRate(pedWhPortData.getWhPortRate());
                mpd.setEmergingWhRate(pedEmergingData.getEmergingWhRate());
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
	@Transactional
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

	@Cacheable(value="getDailyPEDData4MobileByRegion")
	public List<MobilePEDDailyData> getDailyPEDData4MobileByRegion(Timestamp paramDate,String region, String hospitalShownFlag) throws Exception{
    	List<MobilePEDDailyData> pedDatas = new ArrayList<MobilePEDDailyData>();
		pedDatas = pediatricsDAO.getDailyPEDData4RSMByRegion(region,hospitalShownFlag);
		
		List<MobilePEDDailyData> pedCoreData = new ArrayList<MobilePEDDailyData>();
		Map<String,Double> pedCoreInRateMap = new HashMap<String,Double>();
		Map<String,Double> pedCoreWhRateMap = new HashMap<String,Double>();
		
		List<MobilePEDDailyData> pedEmergingData = new ArrayList<MobilePEDDailyData>();
		Map<String,Double> pedEmergingWhRateMap = new HashMap<String,Double>();
		
		pedCoreData = pediatricsDAO.getDailyCorePEDData4RSMByRegion(region,hospitalShownFlag);
		for( MobilePEDDailyData pedCore : pedCoreData ){
			pedCoreInRateMap.put(pedCore.getUserCode(), pedCore.getCoreInRate());
			pedCoreWhRateMap.put(pedCore.getUserCode(), pedCore.getCoreWhRate());
		}
		
		pedEmergingData = pediatricsDAO.getDailyEmergingPEDData4RSMByRegion(region,hospitalShownFlag);
		for( MobilePEDDailyData pedEmerging : pedEmergingData ){
			pedEmergingWhRateMap.put(pedEmerging.getUserCode(), pedEmerging.getEmergingWhRate());
		}
		
		List<MobilePEDDailyData> pedWhPortData = new ArrayList<MobilePEDDailyData>();
		Map<String,Double> pedWhPortMap = new HashMap<String,Double>();
		
		pedWhPortData = pediatricsDAO.getDailyPEDWhPortData4RSMByRegion(region,hospitalShownFlag);
		for( MobilePEDDailyData pedWhPort : pedWhPortData ){
			pedWhPortMap.put(pedWhPort.getUserCode(), pedWhPort.getWhPortRate());
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
            
            pedDailyData.setCoreInRate(pedCoreInRateMap.get(pedDailyData.getUserCode()));
            pedDailyData.setCoreWhRate(pedCoreWhRateMap.get(pedDailyData.getUserCode()));
            pedDailyData.setEmergingWhRate(pedEmergingWhRateMap.get(pedDailyData.getUserCode()));
            pedDailyData.setWhPortRate(pedWhPortMap.get(pedDailyData.getUserCode()));
        }
        
        return pedDatas;
	}
	
    public List<MobilePEDDailyData> getDailyPEDData4Mobile(String telephone, UserInfo currentUser, String hospitalShownFlag) throws Exception {
    	List<MobilePEDDailyData> pedDatas = new ArrayList<MobilePEDDailyData>();
    	
		List<MobilePEDDailyData> pedCoreData = new ArrayList<MobilePEDDailyData>();
		Map<String,Double> pedCoreInRateMap = new HashMap<String,Double>();
		Map<String,Double> pedCoreWhRateMap = new HashMap<String,Double>();
		
		List<MobilePEDDailyData> pedEmergingData = new ArrayList<MobilePEDDailyData>();
		Map<String,Double> pedEmergingWhRateMap = new HashMap<String,Double>();
		
		List<MobilePEDDailyData> pedWhPortData = new ArrayList<MobilePEDDailyData>();
		Map<String,Double> pedWhPortMap = new HashMap<String,Double>();
		
		Date date = new Date();
		Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
    	
    	if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
    		pedDatas = pediatricsDAO.getDailyPEDData4DSMMobile(telephone,hospitalShownFlag);
    		pedCoreData = pediatricsDAO.getDailyCorePEDData4DSMMobile(telephone,hospitalShownFlag);
    		pedEmergingData = pediatricsDAO.getDailyEmergingPEDData4DSMMobile(telephone,hospitalShownFlag);
    		pedWhPortData = pediatricsDAO.getDailyPEDWhPortData4DSMMobile(telephone,hospitalShownFlag);
    	}else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
    		pedDatas = pediatricsDAO.getDailyPEDData4RSMMobile(telephone,hospitalShownFlag);
    		pedCoreData = pediatricsDAO.getDailyCorePEDData4RSMMobile(telephone,hospitalShownFlag);
    		pedEmergingData = pediatricsDAO.getDailyEmergingPEDData4RSMMobile(telephone,hospitalShownFlag);
    		pedWhPortData = pediatricsDAO.getDailyPEDWhPortData4RSMMobile(telephone,hospitalShownFlag);
    	}else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
    			|| LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
    		pedDatas = pediatricsDAO.getDailyPEDData4RSDMobile(paramDate,hospitalShownFlag);
    		pedCoreData = pediatricsDAO.getDailyCorePEDData4RSDMobile(paramDate,hospitalShownFlag);
    		pedEmergingData = pediatricsDAO.getDailyEmergingPEDData4RSDMobile(paramDate,hospitalShownFlag);
    		pedWhPortData = pediatricsDAO.getDailyPEDWhPortData4RSDMobile(paramDate,hospitalShownFlag);
    	}
    	
    	for( MobilePEDDailyData pedCore : pedCoreData ){
			pedCoreInRateMap.put(pedCore.getUserCode(), pedCore.getCoreInRate());
			pedCoreWhRateMap.put(pedCore.getUserCode(), pedCore.getCoreWhRate());
		}
    	
    	for( MobilePEDDailyData pedEmerging : pedEmergingData ){
    		pedEmergingWhRateMap.put(pedEmerging.getUserCode(), pedEmerging.getEmergingWhRate());
    	}
    	
		for( MobilePEDDailyData pedWhPort : pedWhPortData ){
			pedWhPortMap.put(pedWhPort.getUserCode(), pedWhPort.getWhPortRate());
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
        
            pedDailyData.setCoreInRate(pedCoreInRateMap.get(pedDailyData.getUserCode()));
            pedDailyData.setCoreWhRate(pedCoreWhRateMap.get(pedDailyData.getUserCode()));
            pedDailyData.setEmergingWhRate(pedEmergingWhRateMap.get(pedDailyData.getUserCode()));
            pedDailyData.setWhPortRate(pedWhPortMap.get(pedDailyData.getUserCode()));
            
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
    
    public List<MobilePEDDailyData> getDailyPEDChildData4Mobile(String telephone, UserInfo currentUser, String hospitalShownFlag) throws Exception {
        List<MobilePEDDailyData> pedDatas = new ArrayList<MobilePEDDailyData>();
        
        List<MobilePEDDailyData> filteredPedDatas = new ArrayList<MobilePEDDailyData>();
        
        List<MobilePEDDailyData> pedCoreData = new ArrayList<MobilePEDDailyData>();
		Map<String,Double> pedCoreInRateMap = new HashMap<String,Double>();
		Map<String,Double> pedCoreWhRateMap = new HashMap<String,Double>();
		
		List<MobilePEDDailyData> pedEmergingData = new ArrayList<MobilePEDDailyData>();
		Map<String,Double> pedEmergingWhRateMap = new HashMap<String,Double>();
		
		List<MobilePEDDailyData> pedWhPortData = new ArrayList<MobilePEDDailyData>();
		Map<String,Double> pedWhPortMap = new HashMap<String,Double>();
        
        if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
            pedDatas = pediatricsDAO.getDailyPEDChildData4DSMMobile(telephone,hospitalShownFlag);
            pedCoreData = pediatricsDAO.getDailyCorePEDChildData4DSMMobile(telephone,hospitalShownFlag);
            pedEmergingData = pediatricsDAO.getDailyEmergingPEDChildData4DSMMobile(telephone,hospitalShownFlag);
            pedWhPortData = pediatricsDAO.getDailyPEDWhPortChildData4DSMMobile(telephone,hospitalShownFlag);
        }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
            pedDatas = pediatricsDAO.getDailyPEDChildData4RSMMobile(telephone,hospitalShownFlag);
            pedCoreData = pediatricsDAO.getDailyCorePEDChildData4RSMMobile(telephone,hospitalShownFlag);
            pedEmergingData = pediatricsDAO.getDailyEmergingPEDChildData4RSMMobile(telephone,hospitalShownFlag);
            pedWhPortData = pediatricsDAO.getDailyPEDWhPortChildData4RSMMobile(telephone,hospitalShownFlag);
        }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) ){
            pedDatas = pediatricsDAO.getDailyPEDChildData4RSDMobile(telephone,hospitalShownFlag);
            pedCoreData = pediatricsDAO.getDailyCorePEDChildData4RSDMobile(telephone,hospitalShownFlag);
            pedEmergingData = pediatricsDAO.getDailyEmergingPEDChildData4RSDMobile(telephone,hospitalShownFlag);
            pedWhPortData = pediatricsDAO.getDailyPEDWhPortChildData4RSDMobile(telephone,hospitalShownFlag);
        }
        
        for( MobilePEDDailyData pedCore : pedCoreData ){
			pedCoreInRateMap.put(pedCore.getUserCode(), pedCore.getCoreInRate());
			pedCoreWhRateMap.put(pedCore.getUserCode(), pedCore.getCoreWhRate());
		}
        
        for( MobilePEDDailyData pedEmerging : pedEmergingData ){
        	pedEmergingWhRateMap.put(pedEmerging.getUserCode(), pedEmerging.getEmergingWhRate());
        }
    	
		for( MobilePEDDailyData pedWhPort : pedWhPortData ){
			pedWhPortMap.put(pedWhPort.getUserCode(), pedWhPort.getWhPortRate());
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
            
            pedDailyData.setCoreInRate(pedCoreInRateMap.get(pedDailyData.getUserCode()));
            pedDailyData.setCoreWhRate(pedCoreWhRateMap.get(pedDailyData.getUserCode()));
            pedDailyData.setEmergingWhRate(pedEmergingWhRateMap.get(pedDailyData.getUserCode()));
            pedDailyData.setWhPortRate(pedWhPortMap.get(pedDailyData.getUserCode()));
            
            if( pedDailyData.getHosNum() != 0 ){
                filteredPedDatas.add(pedDailyData);
            }
            
        }
        return filteredPedDatas;
    }
    
	@Override
	@Cacheable(value="getTopAndBottomRSMData")
	public TopAndBottomRSMData getTopAndBottomRSMData(Timestamp paramDate, String hospitalShownFlag) throws Exception {
		TopAndBottomRSMData topAndBottomRSMData = new TopAndBottomRSMData();
		
		topAndBottomRSMData = pediatricsDAO.getTopAndBottomInRateRSMData(topAndBottomRSMData,hospitalShownFlag);
		logger.info("inRate end");
		topAndBottomRSMData = pediatricsDAO.getTopAndBottomWhRateRSMData(topAndBottomRSMData,hospitalShownFlag);
		logger.info("whRate end");
		topAndBottomRSMData = pediatricsDAO.getTopAndBottomAverageDoseRSMData(topAndBottomRSMData,hospitalShownFlag);
		logger.info("averageDose end");
		topAndBottomRSMData = pediatricsDAO.getTopAndBottomWhPortRateRSMData(topAndBottomRSMData,hospitalShownFlag);
		logger.info("whPortRate end");
		
		TopAndBottomRSMData coreRSMData = pediatricsDAO.getCoreTopAndBottomRSMData(hospitalShownFlag);
		logger.info("core end");
		TopAndBottomRSMData coreWhRateData = pediatricsDAO.getCoreTopAndBottomRSMWhRateData(hospitalShownFlag);
		logger.info("coreWhRate end");
		
		TopAndBottomRSMData emergingWhRateData = pediatricsDAO.getEmergingTopAndBottomRSMWhRateData(hospitalShownFlag);
		logger.info("emergingWhRate end");
		
		topAndBottomRSMData.setCoreBottomInRate(coreRSMData.getCoreBottomInRate());
		topAndBottomRSMData.setCoreBottomInRateRSMName(coreRSMData.getCoreBottomInRateRSMName());
		topAndBottomRSMData.setCoreTopInRate(coreRSMData.getCoreTopInRate());
		topAndBottomRSMData.setCoreTopInRateRSMName(coreRSMData.getCoreTopInRateRSMName());
		
		topAndBottomRSMData.setCoreBottomWhRate(coreWhRateData.getCoreBottomWhRate());
		topAndBottomRSMData.setCoreBottomWhRateRSMName(coreWhRateData.getCoreBottomWhRateRSMName());
		topAndBottomRSMData.setCoreTopWhRate(coreWhRateData.getCoreTopWhRate());
		topAndBottomRSMData.setCoreTopWhRateRSMName(coreWhRateData.getCoreTopWhRateRSMName());
		
		topAndBottomRSMData.setTopEmergingWhRate(emergingWhRateData.getTopEmergingWhRate());
		topAndBottomRSMData.setTopEmergingWhRateRSMName(emergingWhRateData.getTopEmergingWhRateRSMName());
		topAndBottomRSMData.setBottomEmergingWhRate(emergingWhRateData.getBottomEmergingWhRate());
		topAndBottomRSMData.setBottomEmergingWhRateRSMName(emergingWhRateData.getBottomEmergingWhRateRSMName());
		
		
		return topAndBottomRSMData;
	}
    
    @Override
    public TopAndBottomRSMData getTopAndBottomInRateRSMData(String telephone, String hospitalShownFlag) throws Exception {
    	List<DailyReportData> allRSMData = pediatricsDAO.getAllRSMDataByTelephone(hospitalShownFlag);
    	java.util.Collections.sort(allRSMData, new DailyReportDataInRateComparator());
    	
    	TopAndBottomRSMData topAndBottomRSMInRateData = new TopAndBottomRSMData();
    	topAndBottomRSMInRateData.setTopRSMName(allRSMData.get(0).getRsmName());
    	topAndBottomRSMInRateData.setTopRSMRate(allRSMData.get(0).getInRate());
    	topAndBottomRSMInRateData.setBottomRSMName(allRSMData.get(allRSMData.size()-1).getRsmName());
    	topAndBottomRSMInRateData.setBottomRSMRate(allRSMData.get(allRSMData.size()-1).getInRate());
    	
    	return topAndBottomRSMInRateData;
    }
    
    @Override
    public TopAndBottomRSMData getTopAndBottomWhRateRSMData(String telephone, String hospitalShownFlag) throws Exception {
    	List<DailyReportData> allRSMData = pediatricsDAO.getAllRSMDataByTelephone(hospitalShownFlag);
    	java.util.Collections.sort(allRSMData, new DailyReportDataWhRateComparator());
    	
    	TopAndBottomRSMData topAndBottomRSMWhRateData = new TopAndBottomRSMData();
    	topAndBottomRSMWhRateData.setTopRSMName(allRSMData.get(0).getRsmName());
    	topAndBottomRSMWhRateData.setTopRSMRate(allRSMData.get(0).getWhRate());
    	topAndBottomRSMWhRateData.setBottomRSMName(allRSMData.get(allRSMData.size()-1).getRsmName());
    	topAndBottomRSMWhRateData.setBottomRSMRate(allRSMData.get(allRSMData.size()-1).getWhRate());
    	
    	return topAndBottomRSMWhRateData;
    }
    
    @Override
    public TopAndBottomRSMData getTopAndBottomAverageRSMData(String telephone, String hospitalShownFlag) throws Exception {
    	List<DailyReportData> allRSMData = pediatricsDAO.getAllRSMDataByTelephone(hospitalShownFlag);
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
    		logger.info(String.format("there is no record found by the country"));
    	}catch(Exception e){
    		logger.error("fail to get the country weekly ratio data,",e);
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
	
    public synchronized void generateWeeklyPEDDataOfHospital() throws Exception {
        pediatricsDAO.generateWeeklyPEDDataOfHospital();
    }
    
    public synchronized int removeOldWeeklyPEDData(String duration) throws Exception{
        return pediatricsDAO.removeOldWeeklyPEDData(duration);
    }
    
    public synchronized void generateWeeklyPEDDataOfHospital(Date refreshDate) throws Exception {
        pediatricsDAO.generateWeeklyPEDDataOfHospital(refreshDate);
    }

    public synchronized boolean hasLastWeeklyPEDData() throws Exception {
        int count = pediatricsDAO.getLastWeeklyPEDData();
        logger.info("the last week ped data size is " + count);
        if( !(count>0) ){
        	this.generateWeeklyPEDDataOfHospital();
        }
        return count>0;
    }
    
	@Override
	public List<MonthlyStatisticsData> getMonthlyStatisticsData(
			String beginDuraion, String endDuraion, String level)
			throws Exception {
		try{
			return pediatricsDAO.getMonthlyStatisticsData(beginDuraion, endDuraion, level);
		}catch(EmptyResultDataAccessException erd){
			return Collections.emptyList();
		}
	}

	@Override
	public Map<String, MonthlyStatisticsData> getCoreOrEmergingMonthlyStatisticsData(
			String beginDuraion, String endDuraion, String level, String dragonType)
			throws Exception {
		List<MonthlyStatisticsData> dbStatisticsData = new ArrayList<MonthlyStatisticsData>();
		try{
			if( "Core".equalsIgnoreCase(dragonType) ){
				dbStatisticsData = pediatricsDAO.getCoreMonthlyStatisticsData(beginDuraion, endDuraion, level);
			}else{
				dbStatisticsData = pediatricsDAO.getEmergingMonthlyStatisticsData(beginDuraion, endDuraion, level);
			}
			
			Map<String, MonthlyStatisticsData> statisticsDataMap = new HashMap<String, MonthlyStatisticsData>();
			
			if( null != dbStatisticsData && !dbStatisticsData.isEmpty() ){
				for( MonthlyStatisticsData data : dbStatisticsData ){
					switch(level){
						case LsAttributes.USER_LEVEL_DSM:
							statisticsDataMap.put(data.getRsd()+data.getRsm()+data.getDsmCode(), data);
							break;
						case LsAttributes.USER_LEVEL_RSM:
							statisticsDataMap.put(data.getRsm(), data);
							break;
						case LsAttributes.USER_LEVEL_RSD:
							statisticsDataMap.put(data.getRsd(), data);
							break;
					}
				}
			}
			return statisticsDataMap;
		}catch(EmptyResultDataAccessException erd){
			return Collections.emptyMap();
		}
	}

	@Override
	public MonthlyStatisticsData getMonthlyStatisticsCountryData(
			String beginDuraion, String endDuraion) throws Exception {
		try{
			return pediatricsDAO.getMonthlyStatisticsCountryData(beginDuraion, endDuraion);
		}catch(EmptyResultDataAccessException erd){
			return new MonthlyStatisticsData();
		}
	}
	
	@Override
	public MonthlyStatisticsData getCoreOrEmergingMonthlyStatisticsCountryData(
			String beginDuraion, String endDuraion, String dragonType) throws Exception {
		try{
			if( "Core".equalsIgnoreCase(dragonType) ){
				return pediatricsDAO.getCoreMonthlyStatisticsCountryData(beginDuraion, endDuraion);
			}else{
				return pediatricsDAO.getEmergingMonthlyStatisticsCountryData(beginDuraion, endDuraion);
			}
		}catch(EmptyResultDataAccessException erd){
			return new MonthlyStatisticsData();
		}
	}
}
