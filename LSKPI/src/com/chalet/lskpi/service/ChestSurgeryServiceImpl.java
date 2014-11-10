package com.chalet.lskpi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.chalet.lskpi.dao.ChestSurgeryDAO;
import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MobileCHEDailyData;
import com.chalet.lskpi.model.RateElement;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyRatioData;
import com.chalet.lskpi.service.PediatricsServiceImpl.RateElementComparator;
import com.chalet.lskpi.utils.LsAttributes;

@Service("chestSurgeryService")
public class ChestSurgeryServiceImpl implements ChestSurgeryService {
    
    @Autowired
    @Qualifier("chestSurgeryDAO")
    private ChestSurgeryDAO chestSurgeryDAO;
    
    @Autowired
    @Qualifier("userService")
    private UserService userService;
    
    @Autowired
    @Qualifier("hospitalService")
    private HospitalService hospitalService;
    
    private Logger logger = Logger.getLogger(ChestSurgeryServiceImpl.class);

    public ChestSurgeryData getChestSurgeryDataByHospital(String hospitalCode) throws Exception {
        try{
            return chestSurgeryDAO.getChestSurgeryDataByHospital(hospitalCode);
        } catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(Exception e){
            logger.error("fail to get the chest surgery data by hospital - " + hospitalCode,e);
            return null;
        }
    }

    public List<ChestSurgeryData> getChestSurgeryDataByDate(Date createdatebegin, Date createdateend) throws Exception {
        try{
            return chestSurgeryDAO.getChestSurgeryDataByDate(createdatebegin, createdateend);
        } catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(IncorrectResultSizeDataAccessException ire){
            logger.error(ire.getMessage());
            return new ArrayList<ChestSurgeryData>();
        }catch(Exception e){
            logger.error(String.format("fail to get the chest surgery data by data from %s to %s", createdatebegin,createdateend),e);
            return new ArrayList<ChestSurgeryData>();
        }
    }

    public ChestSurgeryData getChestSurgeryDataByHospitalAndDate(String hospitalCode, Date createdate) throws Exception {
        try{
            return chestSurgeryDAO.getChestSurgeryDataByHospitalAndDate(hospitalCode, createdate);
        } catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(IncorrectResultSizeDataAccessException ire){
            logger.error(ire.getMessage());
            ChestSurgeryData chestData = new ChestSurgeryData();
            chestData.setDataId(0);
            return chestData;
        }catch(Exception e){
            logger.error(String.format("fail to get the chest surgery data by hospital - ", hospitalCode),e);
            ChestSurgeryData chestData = new ChestSurgeryData();
            chestData.setDataId(0);
            return chestData;
        }
    }

    public ChestSurgeryData getChestSurgeryDataById(int id) throws Exception {
        try{
            return chestSurgeryDAO.getChestSurgeryDataById(id);
        }catch(Exception e){
            logger.error(String.format("fail to get the chest surgery data by ID - ", id),e);
            return null;
        }
    }

    public void insert(ChestSurgeryData chestSurgeryData, UserInfo operator, Hospital hospital) throws Exception {
        chestSurgeryDAO.insert(chestSurgeryData, operator, hospital);
    }

    public void insert(ChestSurgeryData chestSurgeryData) throws Exception {
        String dsmCode = "";
        try{
            UserInfo primarySales = hospitalService.getPrimarySalesOfHospital(chestSurgeryData.getHospitalCode());
            if( null != primarySales ){
                chestSurgeryData.setSalesCode(primarySales.getUserCode());
                dsmCode = (primarySales.getSuperior()==null||"".equalsIgnoreCase(primarySales.getSuperior()))?primarySales.getUserCode():primarySales.getSuperior();
            }
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no user found whose code is " + chestSurgeryData.getSalesCode());
        }
        chestSurgeryDAO.insert(chestSurgeryData,dsmCode);
    }

    public void update(ChestSurgeryData chestSurgeryData, UserInfo operator) throws Exception {
        chestSurgeryDAO.update(chestSurgeryData);
    }

    public MobileCHEDailyData getDailyCHEParentData4Mobile(String telephone, String level) throws Exception {
        MobileCHEDailyData mcd = new MobileCHEDailyData();
        
        switch (level) {
            case LsAttributes.USER_LEVEL_BM:
                mcd = chestSurgeryDAO.getDailyCHEData4CountoryMobile();
                logger.info(String.format("end to get the chest surgery daily data of the country, current telephone is %s", telephone));
                mcd.setInRate(mcd.getHosNum()==0?0:(double)mcd.getInNum()/mcd.getHosNum());
                mcd.setWhRate(mcd.getPatNum()==0?0:(double)mcd.getLsNum()/mcd.getPatNum());
                break;
            default:
                mcd = null;
                break;
        }
        return mcd;
    }

    public List<MobileCHEDailyData> getDailyCHEData4Mobile(String telephone, UserInfo currentUser) throws Exception {
        List<MobileCHEDailyData> cheDatas = new ArrayList<MobileCHEDailyData>();
        if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
            cheDatas = chestSurgeryDAO.getDailyCHEData4DSMMobile(currentUser.getRegion());
        }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
            cheDatas = chestSurgeryDAO.getDailyCHEData4RSMMobile(currentUser.getRegionCenter());
        }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
                || LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())){
            cheDatas = chestSurgeryDAO.getDailyCHEData4RSDMobile();
        }
        logger.info(String.format("end to get the chest surgery daily data...current telephone is %s", telephone));
        List<MobileCHEDailyData> orderedCheData = new ArrayList<MobileCHEDailyData>();
        List<MobileCHEDailyData> leftCheData = new ArrayList<MobileCHEDailyData>();
        
        for( MobileCHEDailyData cheDailyData : cheDatas ){
            
            cheDailyData.setInRate(cheDailyData.getHosNum()==0?0:(double)cheDailyData.getInNum()/cheDailyData.getHosNum());
            cheDailyData.setWhRate(cheDailyData.getPatNum()==0?0:(double)cheDailyData.getLsNum()/cheDailyData.getPatNum());
        
            if( cheDailyData.getHosNum() != 0 ){
                if( null != currentUser && null != cheDailyData.getUserCode() 
                        && cheDailyData.getUserCode().equalsIgnoreCase(currentUser.getUserCode()) ){
                    orderedCheData.add(0,cheDailyData);
                }else{
                    leftCheData.add(cheDailyData);
                }
            }
        }
        
        if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            orderedCheData.addAll(leftCheData);
        }else{
            orderedCheData.addAll(1,leftCheData);
        }
        logger.info(String.format("end to populate the chest surgery daily data...current telephone is %s", telephone));
        return orderedCheData;
    }

    public List<MobileCHEDailyData> getDailyCHEChildData4Mobile(String telephone, UserInfo currentUser)
            throws Exception {
        List<MobileCHEDailyData> cheDatas = new ArrayList<MobileCHEDailyData>();
        List<MobileCHEDailyData> filteredCheDatas = new ArrayList<MobileCHEDailyData>();
        if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
            cheDatas = chestSurgeryDAO.getChildDailyCHEData4DSMMobile(currentUser.getUserCode());
        }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
            cheDatas = chestSurgeryDAO.getDailyCHEData4DSMMobile(currentUser.getRegion());
        }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) ){
            cheDatas = chestSurgeryDAO.getDailyCHEData4RSMMobile(currentUser.getRegionCenter());
        }
        
        for( MobileCHEDailyData cheDailyData : cheDatas ){
            cheDailyData.setInRate(cheDailyData.getHosNum()==0?0:(double)cheDailyData.getInNum()/cheDailyData.getHosNum());
            cheDailyData.setWhRate(cheDailyData.getPatNum()==0?0:(double)cheDailyData.getLsNum()/cheDailyData.getPatNum());
        
            if( cheDailyData.getHosNum() != 0 ){
                filteredCheDatas.add(cheDailyData);
            }
        }
        
        return filteredCheDatas;
    }

    @Cacheable(value="getDailyCHEData4MobileByRegionCenter")
    public List<MobileCHEDailyData> getDailyCHEData4MobileByRegionCenter(String regionCenter) throws Exception {
        List<MobileCHEDailyData> cheDatas = new ArrayList<MobileCHEDailyData>();
        cheDatas = chestSurgeryDAO.getDailyCHEData4RSMByRegionCenter(regionCenter);
        
        for( MobileCHEDailyData cheDailyData : cheDatas ){
            cheDailyData.setInRate(cheDailyData.getHosNum()==0?0:(double)cheDailyData.getInNum()/cheDailyData.getHosNum());
            cheDailyData.setWhRate(cheDailyData.getPatNum()==0?0:(double)cheDailyData.getLsNum()/cheDailyData.getPatNum());
        }
        return cheDatas;
    }

    @Cacheable(value="getTopAndBottomRSMData_CHE")
    public TopAndBottomRSMData getTopAndBottomRSMData() throws Exception {
        return chestSurgeryDAO.getTopAndBottomRSMData();
    }
    

    public ReportProcessData getSalesSelfReportProcessData(String telephone) throws Exception {
        try{
            return chestSurgeryDAO.getSalesSelfReportProcessData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return new ReportProcessData();
        } catch(Exception e){
            logger.error(String.format("fail to get the REP report process data by telephone - %s" , telephone),e);
            return new ReportProcessData();
        }
    }
    
    public List<ReportProcessDataDetail> getSalesSelfReportProcessDetailData(String telephone) throws Exception {
        try{
            return chestSurgeryDAO.getSalesSelfReportProcessDetailData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no detail record found by the telephone - %s", telephone));
            return new ArrayList<ReportProcessDataDetail>();
        } catch(Exception e){
            logger.error(String.format("fail to get the sales report process detail data by telephone - %s" , telephone),e);
            return new ArrayList<ReportProcessDataDetail>();
        }
    }
    
    public ReportProcessData getDSMSelfReportProcessData(String telephone) throws Exception {
        try{
            return chestSurgeryDAO.getDSMSelfReportProcessData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no record found by the telephone - %s", telephone));
            return new ReportProcessData();
        } catch(Exception e){
            logger.error(String.format("fail to get the DSM report process data by telephone - %s" , telephone),e);
            return new ReportProcessData();
        }
    }

    public List<ReportProcessDataDetail> getDSMSelfReportProcessDetailData(String telephone) throws Exception {
        try{
            return chestSurgeryDAO.getDSMSelfReportProcessDetailData(telephone);
        }catch(EmptyResultDataAccessException erd){
            logger.info(String.format("there is no detail record found by the telephone - %s", telephone));
            return new ArrayList<ReportProcessDataDetail>();
        } catch(Exception e){
            logger.error(String.format("fail to get the DSM report process detail data by telephone - %s" , telephone),e);
            return new ArrayList<ReportProcessDataDetail>();
        }
    }
    
    public ReportProcessData getRSMSelfReportProcessData(String telephone) throws Exception {
    	try{
    		return chestSurgeryDAO.getRSMSelfReportProcessData(telephone);
    	}catch(EmptyResultDataAccessException erd){
    		logger.info(String.format("there is no record found by the telephone - %s", telephone));
    		return new ReportProcessData();
    	} catch(Exception e){
    		logger.error(String.format("fail to get the RSM report process data by telephone - %s" , telephone),e);
    		return new ReportProcessData();
    	}
    }
    
    public List<ReportProcessDataDetail> getRSMSelfReportProcessDetailData(String telephone) throws Exception {
    	try{
    		return chestSurgeryDAO.getRSMSelfReportProcessDetailData(telephone);
    	}catch(EmptyResultDataAccessException erd){
    		logger.info(String.format("there is no detail record found by the telephone - %s", telephone));
    		return new ArrayList<ReportProcessDataDetail>();
    	} catch(Exception e){
    		logger.error(String.format("fail to get the RSM report process detail data by telephone - %s" , telephone),e);
    		return new ArrayList<ReportProcessDataDetail>();
    	}
    }
    
    @Override
    public List<WeeklyRatioData> getWeeklyData4Mobile(UserInfo currentUser) throws Exception {
        List<WeeklyRatioData> resDatas = new ArrayList<WeeklyRatioData>();
        if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
            resDatas = chestSurgeryDAO.getWeeklyData4DSMMobile(currentUser.getTelephone());
        }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
            resDatas = chestSurgeryDAO.getWeeklyData4RSMMobile(currentUser.getTelephone());
        }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
                || LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())){
            resDatas = chestSurgeryDAO.getWeeklyData4RSDMobile();
        }
        
        List<WeeklyRatioData> orderedResData = new ArrayList<WeeklyRatioData>();
        List<WeeklyRatioData> leftResData = new ArrayList<WeeklyRatioData>();
        
        if( null != resDatas && resDatas.size() > 0 ){
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
        		if( null != currentUser && null != resWeeklyData.getUserCode() ){
        			if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel())
        					&& resWeeklyData.getUserCode().equalsIgnoreCase(currentUser.getUserCode())){
        				isSelf = true;
        			}
        			if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel())
        					&& resWeeklyData.getUserCode().equalsIgnoreCase(currentUser.getRegion())){
        				isSelf = true;
        			}
        			if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel())
        					&& resWeeklyData.getUserCode().equalsIgnoreCase(currentUser.getRegionCenter())){
        				isSelf = true;
        			}
        		}
        		
        		if( isSelf ){
        			orderedResData.add(0,resWeeklyData);
        		}else{
        			leftResData.add(resWeeklyData);
        		}
        	}
        	if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
        		orderedResData.addAll(leftResData);
        	}else{
        		orderedResData.addAll(1,leftResData);
        	}
        }
        
        return orderedResData;
    }

    @Override
    public WeeklyRatioData getLowerWeeklyData4Mobile(UserInfo currentUser, String lowerUserCode) throws Exception {
        WeeklyRatioData resData = new WeeklyRatioData();
        try{
            if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
                resData = chestSurgeryDAO.getLowerWeeklyData4REPMobile(currentUser,lowerUserCode);
            }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
                resData = chestSurgeryDAO.getLowerWeeklyData4DSMMobile(currentUser,lowerUserCode);
            }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
                    || LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())){
                resData = chestSurgeryDAO.getLowerWeeklyData4RSMMobile(currentUser,lowerUserCode);
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
    
    public WeeklyRatioData getHospitalWeeklyData4Mobile(String hospitalCode) throws Exception {
        WeeklyRatioData hospitalData = new WeeklyRatioData();
        try{
            hospitalData = chestSurgeryDAO.getHospitalWeeklyData4Mobile(hospitalCode);
            
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
    
    @Cacheable(value="getWeeklyCountoryData4Mobile")
    public WeeklyRatioData getWeeklyCountoryData4Mobile() throws Exception {
        WeeklyRatioData weeklyRatioData = new WeeklyRatioData();
        try{
            weeklyRatioData = chestSurgeryDAO.getHospitalWeeklyData4Mobile();
            
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
            logger.info(String.format("there is no record found by the country"));
        }catch(Exception e){
            logger.error("fail to get the country weekly ratio data,",e);
        }
        return weeklyRatioData;
    }
    

    public synchronized int removeOldWeeklyData(String duration) throws Exception{
        return chestSurgeryDAO.removeOldWeeklyData(duration);
    }
    
    public synchronized void generateWeeklyDataOfHospital() throws Exception {
        chestSurgeryDAO.generateWeeklyDataOfHospital();
    }
    
    public synchronized void generateWeeklyDataOfHospital(Date refreshDate) throws Exception {
        chestSurgeryDAO.generateWeeklyDataOfHospital(refreshDate);
    }

    public synchronized boolean hasLastWeeklyData() throws Exception {
        int count = chestSurgeryDAO.getLastWeeklyData();
        logger.info("the last week chest surgery data size is " + count);
        if( !(count>0) ){
        	this.generateWeeklyDataOfHospital();
        }
        return count>0;
    }
}
