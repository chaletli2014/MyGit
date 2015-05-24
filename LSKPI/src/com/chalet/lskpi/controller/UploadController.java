package com.chalet.lskpi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.DDIData;
import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MonthlyData;
import com.chalet.lskpi.model.PediatricsData;
import com.chalet.lskpi.model.RespirologyData;
import com.chalet.lskpi.model.UserCode;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.service.ChestSurgeryService;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.service.PediatricsService;
import com.chalet.lskpi.service.RespirologyService;
import com.chalet.lskpi.service.UploadService;
import com.chalet.lskpi.service.UserService;
import com.chalet.lskpi.utils.ExcelUtils;
import com.chalet.lskpi.utils.LsAttributes;

/**
 * @author Chalet
 * @version 创建时间：2013年12月18日 下午10:24:54
 * 类说明
 */

@Controller
public class UploadController {

	private Logger logger = Logger.getLogger(UploadController.class);
	
    @Autowired
    @Qualifier("userService")
    private UserService userService;
    
    @Autowired
    @Qualifier("hospitalService")
    private HospitalService hospitalService;
    
    @Autowired
    @Qualifier("respirologyService")
    private RespirologyService respirologyService;
    
    @Autowired
    @Qualifier("chestSurgeryService")
    private ChestSurgeryService chestSurgeryService;
    
    @Autowired
    @Qualifier("pediatricsService")
    private PediatricsService pediatricsService;
    
    @Autowired
    @Qualifier("uploadService")
    private UploadService uploadService;

    @RequestMapping("/doUploadAllData")
    public String doUploadAllData(HttpServletRequest request){
        try{
            List<String> regionHeaders = new ArrayList<String>();
            regionHeaders.add("BR Name");//user:Region Center / hospital:Region
            regionHeaders.add("DIST NAME");//user:大区  / hospital:rsmRegion
            regionHeaders.add("BR CNName");//BR Name 的中文名称
            
            List<String> hospitalHeaders = new ArrayList<String>();
            hospitalHeaders.add("Hospital Code");//Sub Institution
            hospitalHeaders.add("Name");//
            hospitalHeaders.add("Province");
            hospitalHeaders.add("City");
            hospitalHeaders.add("Hospital Level");
            hospitalHeaders.add("Dragon Type");
            hospitalHeaders.add("是否在呼吸科名单中（在=1，不在=0）");
            hospitalHeaders.add("是否在儿科名单中（在=1，不在=0）");
            hospitalHeaders.add("是否为月报袋数统计中（在=1，不在=0）");
            hospitalHeaders.add("是否在胸外科名单中（在=1，不在=0）");
            hospitalHeaders.add("是否在胸外科Top 100名单中（在=1，不在=0）");
            hospitalHeaders.add("是否为儿科日报考核名单中（在=1，不在=0）");
            hospitalHeaders.add("是否在呼吸科RE2名单中（在=1，不在=0）");
            
            List<String> repHeaders = new ArrayList<String>();
            repHeaders.add("Rep Code");
            repHeaders.add("Rep Name");
            repHeaders.add("Rep Tel");
            repHeaders.add("是否为负责销售（是=1，否=0）");
            repHeaders.add("Rep Email");
            
            List<String> dsmHeaders = new ArrayList<String>();
            dsmHeaders.add("DSM Code");
            dsmHeaders.add("DSM Name");
            dsmHeaders.add("DSM Tel");
            dsmHeaders.add("DSM Email");
            
            List<String> rsmHeaders = new ArrayList<String>();
            rsmHeaders.add("RSM Code");
            rsmHeaders.add("RSM Name");
            rsmHeaders.add("RSM Tel");
            rsmHeaders.add("RSM Email");
            
            List<String> rsdHeaders = new ArrayList<String>();
            rsdHeaders.add("RSD Code");
            rsdHeaders.add("RSD Name");
            rsdHeaders.add("RSD Tel");
            rsdHeaders.add("RSD Email");
            
            long begin = System.currentTimeMillis();
            Map<String, List> allInfos = ExcelUtils.getAllInfosFromFile(loadFile(request), regionHeaders, hospitalHeaders, repHeaders, dsmHeaders, rsmHeaders, rsdHeaders);
            long end = System.currentTimeMillis();
            logger.info("all item size is " + allInfos.size() + ", spend time " + (end - begin) + " ms");
            
            uploadService.uploadAllData(allInfos);
            
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
        }catch(Exception e){
            logger.error("fail to upload the file,",e);
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
        }
        request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadAllResult_div");
        return "redirect:showUploadData";
    }
    
    @RequestMapping("/doUploaddailyRESData")
    public String doUploaddailyRESData(HttpServletRequest request){
    	logger.info("upload the daily res data..");
        try{
            if( null == request.getSession().getAttribute(LsAttributes.WEB_LOGIN_USER) ){
                return "redirect:login";
            }
            
            removeSessionAttribute(request);
            
            List<String> dataHeaders = new ArrayList<String>();
            dataHeaders.add("录入日期");
            dataHeaders.add("医院编号");
            dataHeaders.add("医院名称");
            dataHeaders.add("当日目标科室病房病人数");
            dataHeaders.add("当日病房内AECOPD人数");
            dataHeaders.add("当日雾化病人数");
            dataHeaders.add("当日雾化令舒病人数");
            dataHeaders.add("销售代表ETMSCode");
            dataHeaders.add("销售代表姓名");
            dataHeaders.add("所属Region");
            dataHeaders.add("所属RSM Region");
            dataHeaders.add("1mg QD");
            dataHeaders.add("2mg QD");
            dataHeaders.add("1mg TID");
            dataHeaders.add("2mg BID");
            dataHeaders.add("2mg TID");
            dataHeaders.add("3mg BID");
            dataHeaders.add("4mg BID");
//            dataHeaders.add("该医院主要处方方式");
            
            long begin = System.currentTimeMillis();
            Map<String,List<RespirologyData>> resDatas = ExcelUtils.getdailyRESDataFromFile(loadFile(request), dataHeaders);
            
            List<RespirologyData> validResData = resDatas.get("validResData");
            List<RespirologyData> invalidResData = resDatas.get("invalidResData");
            long end = System.currentTimeMillis();
            logger.info("valid ResData size is " + validResData.size() + ", invalid ResData size is "+invalidResData.size()+", spend time " + (end - begin) + " ms");
            
//            List<RespirologyData> existsResData = new ArrayList<RespirologyData>();
            int validNum = validResData.size();
            for( RespirologyData data : validResData ){
            	try{
	                RespirologyData respirologyData = respirologyService.getRespirologyDataByHospitalAndDate(data.getHospitalName(), data.getCreatedate());
	                if( respirologyData != null ){
	                    if( respirologyData.getDataId() != 0 ){
	                        logger.info("upload res data, the data for " + data.getHospitalName() + "|" + data.getCreatedate() + ", is already exists, then update it which id is " + respirologyData.getDataId());
	                        data.setDataId(respirologyData.getDataId());
	                        respirologyService.update(data, null);
	                    }else{
	                        logger.error(String.format("the res data of %s is invalid", data.getHospitalName()));
	                        validNum--;
	                        invalidResData.add(data);
	                    }
	                }else{
	                    logger.info("insert the new data of respirology");
	                        respirologyService.insert(data);
	                }
            	}catch(Exception e){
            		logger.error("fail to insert the data of hospital " + data.getHospitalName() ,e);
            		validNum--;
            		invalidResData.add(data);
            	}
            }
            
            request.getSession().setAttribute(LsAttributes.INVALID_DATA, invalidResData);
//            request.getSession().setAttribute(LsAttributes.EXISTS_DATA, existsResData);
            request.getSession().setAttribute(LsAttributes.VALID_DATA_NUM, validNum);
//            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
        }catch(Exception e){
            logger.error("fail to upload the file,",e);
            request.getSession().setAttribute(LsAttributes.VALID_DATA_NUM, 0);
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
        }
        request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadRESResult_div");
        return "redirect:showUploadData";
    }
    
    @RequestMapping("/doUploaddailyCHEData")
    public String doUploaddailyCHEData(HttpServletRequest request){
        logger.info("upload the daily chest surgery data..");
        try{
            if( null == request.getSession().getAttribute(LsAttributes.WEB_LOGIN_USER) ){
                return "redirect:login";
            }
            
            removeSessionAttribute(request);
            
            List<String> dataHeaders = new ArrayList<String>();
            dataHeaders.add("录入日期");
            dataHeaders.add("医院编号");
            dataHeaders.add("医院名称");
            dataHeaders.add("当日病房病人人数");
            dataHeaders.add("当日病房内合并COPD或哮喘的手术病人数");
            dataHeaders.add("当日雾化人数");
            dataHeaders.add("当日雾化令舒病人数");
            dataHeaders.add("1mg QD");
            dataHeaders.add("2mg QD");
            dataHeaders.add("1mg TID");
            dataHeaders.add("2mg BID");
            dataHeaders.add("2mg TID");
            dataHeaders.add("3mg BID");
            dataHeaders.add("4mg BID");
            
            long begin = System.currentTimeMillis();
            Map<String,List<ChestSurgeryData>> cheDatas = ExcelUtils.getdailyCHEDataFromFile(loadFile(request), dataHeaders);
            
            List<ChestSurgeryData> validData = cheDatas.get("validData");
            List<ChestSurgeryData> invalidData = cheDatas.get("invalidData");
            long end = System.currentTimeMillis();
            logger.info("valid ResData size is " + validData.size() + ", invalid ResData size is "+invalidData.size()+", spend time " + (end - begin) + " ms");
            
            int validNum = validData.size();
            for( ChestSurgeryData data : validData ){
                try{
                    ChestSurgeryData chestSurgeryData = chestSurgeryService.getChestSurgeryDataByHospitalAndDate(data.getHospitalCode(), data.getCreatedate());
                    if( chestSurgeryData != null ){
                        if( chestSurgeryData.getDataId() != 0 ){
                            logger.info("upload chest surgery data, the data for " + data.getHospitalName() + "|" + data.getCreatedate() + ", is already exists, then update it which id is " + chestSurgeryData.getDataId());
                            data.setDataId(chestSurgeryData.getDataId());
                            chestSurgeryService.update(data, null);
                        }else{
                            logger.error(String.format("the res data of %s is invalid", data.getHospitalName()));
                            validNum--;
                            invalidData.add(data);
                        }
                    }else{
                        logger.info("insert the new data of chest surgery");
                        chestSurgeryService.insert(data);
                    }
                }catch(Exception e){
                    logger.error("fail to insert the data of hospital " + data.getHospitalName() ,e);
                    validNum--;
                    invalidData.add(data);
                }
            }
            
            request.getSession().setAttribute(LsAttributes.INVALID_DATA, invalidData);
            request.getSession().setAttribute(LsAttributes.VALID_DATA_NUM, validNum);
        }catch(Exception e){
            logger.error("fail to upload the file,",e);
            request.getSession().setAttribute(LsAttributes.VALID_DATA_NUM, 0);
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
        }
        request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadCHEResult_div");
        return "redirect:showUploadData";
    }
    
    @RequestMapping("/doUploaddailyPEDData")
    public String doUploaddailyPEDData(HttpServletRequest request){
    	logger.info("upload the daily ped data..");
    	try{
    	    if( null == request.getSession().getAttribute(LsAttributes.WEB_LOGIN_USER) ){
                return "redirect:login";
            }
    	    
    	    removeSessionAttribute(request);
    	    
    		List<String> dataHeaders = new ArrayList<String>();
    		dataHeaders.add("录入日期");
    		dataHeaders.add("医院编号");
    		dataHeaders.add("医院名称");
    		dataHeaders.add("当日门诊人次");
    		dataHeaders.add("当日雾化人次");
    		dataHeaders.add("当日雾化令舒人次");
    		dataHeaders.add("雾化端口数量");
    		dataHeaders.add("销售代表ETMSCode");
    		dataHeaders.add("销售代表姓名");
    		dataHeaders.add("所属Region");
    		dataHeaders.add("所属RSM Region");
    		dataHeaders.add("0.5mg QD");
    		dataHeaders.add("0.5mg BID");
    		dataHeaders.add("1mg QD");
    		dataHeaders.add("1mg BID");
    		dataHeaders.add("2mg QD");
    		dataHeaders.add("2mg BID");
    		dataHeaders.add("门急诊雾化天数1天占比");
    		dataHeaders.add("门急诊雾化天数2天占比");
    		dataHeaders.add("门急诊雾化天数3天占比");
    		dataHeaders.add("门急诊雾化天数4天占比");
    		dataHeaders.add("门急诊雾化天数5天占比");
    		dataHeaders.add("门急诊雾化天数6天占比");
    		dataHeaders.add("门急诊雾化天数7天及以上占比");
    		dataHeaders.add("门急诊赠卖泵数量");
    		dataHeaders.add("门急诊带药人数");
    		dataHeaders.add("门急诊平均带药天数");
    		dataHeaders.add("门急诊总带药支数");
    		dataHeaders.add("病房雾化天数1天占比");
    		dataHeaders.add("病房雾化天数2天占比");
    		dataHeaders.add("病房雾化天数3天占比");
    		dataHeaders.add("病房雾化天数4天占比");
    		dataHeaders.add("病房雾化天数5天占比");
    		dataHeaders.add("病房雾化天数6天占比");
    		dataHeaders.add("病房雾化天数7天占比");
    		dataHeaders.add("病房雾化天数8天占比");
    		dataHeaders.add("病房雾化天数9天占比");
    		dataHeaders.add("病房雾化天数10天及以上占比");
    		dataHeaders.add("病房赠卖泵数量");
    		dataHeaders.add("病房带药人数");
    		dataHeaders.add("病房平均带药天数");
    		dataHeaders.add("病房总带药支数");   		
    		dataHeaders.add("该医院主要处方方式");
    		
    		long begin = System.currentTimeMillis();
    		Map<String,List<PediatricsData>> pedDatas = ExcelUtils.getdailyPEDDataFromFile(loadFile(request), dataHeaders);
    		
    		List<PediatricsData> validPedData = pedDatas.get("validPedData");
    		List<PediatricsData> invalidPedData = pedDatas.get("invalidPedData");
    		long end = System.currentTimeMillis();
    		logger.info("valid PedData size is " + validPedData.size() + ", invalid PedData size is "+invalidPedData.size()+", spend time " + (end - begin) + " ms");
    		
//    		List<PediatricsData> existsPedData = new ArrayList<PediatricsData>();
    		int validNum = validPedData.size();
    		for( PediatricsData data : validPedData ){
    			try{
	    			PediatricsData pediatricsData = pediatricsService.getPediatricsDataByHospitalAndDate(data.getHospitalName(), data.getCreatedate());
	    			if( pediatricsData != null ){
	    			    if( pediatricsData.getDataId() != 0 ){
	    			        logger.info("upload ped data, the data for " + data.getHospitalName() + "|" + data.getCreatedate() + ", is already exists, then update it which id is " + pediatricsData.getDataId());
	    			        //existsPedData.add(data);
	    			        data.setDataId(pediatricsData.getDataId());
	    			        pediatricsService.update(data, null);
	    			    }else{
	    			        logger.error(String.format("the ped data of %s is invalid", data.getHospitalName()));
                            validNum--;
                            invalidPedData.add(data);
	    			    }
	    			}else{
	    				logger.info("insert the new data of pediatrics");
	    				pediatricsService.insert(data);
	    			}
    			}catch(Exception e){
    				logger.error("fail to insert the data of hospital " + data.getHospitalName() ,e);
    				validNum--;
    				invalidPedData.add(data);
    			}
    		}
    		
    		request.getSession().setAttribute(LsAttributes.INVALID_DATA, invalidPedData);
//    		request.getSession().setAttribute(LsAttributes.EXISTS_DATA, existsPedData);
    		request.getSession().setAttribute(LsAttributes.VALID_DATA_NUM, validNum);
//    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
    	}catch(Exception e){
    		logger.error("fail to upload the file,",e);
    		request.getSession().setAttribute(LsAttributes.VALID_DATA_NUM, 0);
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
    	}
    	request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadPEDResult_div");
    	return "redirect:showUploadData";
    }
    
    @RequestMapping("/doUploadMonthlyData")
    public String doUploadMonthlyData(HttpServletRequest request){
    	logger.info("upload the monthly data..");
    	try{
    		if( null == request.getSession().getAttribute(LsAttributes.WEB_LOGIN_USER) ){
    			return "redirect:login";
    		}
    		
    		removeSessionAttribute(request);
    		
    		List<String> dataHeaders = new ArrayList<String>();
    		dataHeaders.add("录入日期");
    		dataHeaders.add("医院编号");
    		dataHeaders.add("儿科门急诊");
    		dataHeaders.add("儿科病房");
    		dataHeaders.add("呼吸科");
    		dataHeaders.add("其他科室");
    		
    		long begin = System.currentTimeMillis();
    		Map<String,List<MonthlyData>> monthlyDatas = ExcelUtils.getMonthlyDataFromFile(loadFile(request), dataHeaders);
    		
    		List<MonthlyData> validData = monthlyDatas.get("validData");
    		List<MonthlyData> invalidData = monthlyDatas.get("invalidData");
    		long end = System.currentTimeMillis();
    		logger.info("valid monthly data size is " + validData.size() + ", spend time " + (end - begin) + " ms");
    		
    		int validNum = validData.size();
    		for( MonthlyData data : validData ){
    			try{
    				MonthlyData monthlyData = hospitalService.getMonthlyData(data.getHospitalCode(), data.getCreateDate());
    				if( monthlyData != null ){
    					logger.info("upload monthly data, the data for " + data.getHospitalName() + "|" + data.getCreateDate() + ", is already exists, then update it which id is " + monthlyData.getId());
    					data.setId(monthlyData.getId());
    					hospitalService.updateMonthlyData(data);
    				}else{
    					logger.info("insert the new monthly data");
    					Hospital hospital = hospitalService.getHospitalByCode(data.getHospitalCode());
    					data.setOperatorName(hospital.getSaleName());
    					data.setOperatorCode(hospital.getSaleCode());
    					data.setDsmCode(hospital.getDsmCode());
    					data.setRsmRegion(hospital.getRsmRegion());
    					data.setRegion(hospital.getRegion());
    					hospitalService.insertMonthlyData(data);
    				}
    			}catch(Exception e){
    				logger.error("fail to insert the data of hospital " + data.getHospitalName() ,e);
    				validNum--;
    				invalidData.add(data);
    			}
    		}
    		
    		request.getSession().setAttribute(LsAttributes.INVALID_DATA, invalidData);
    		request.getSession().setAttribute(LsAttributes.VALID_DATA_NUM, validNum);
    	}catch(Exception e){
    		logger.error("fail to upload the file,",e);
    		request.getSession().setAttribute(LsAttributes.VALID_DATA_NUM, 0);
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
    	}
    	request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadMonthlyResult_div");
    	return "redirect:showUploadData";
    }
    
    @RequestMapping("/doUploadDoctorData")
    public String doUploadDoctorData(HttpServletRequest request){
        logger.info("upload the doctor data..");
        try{
            if( null == request.getSession().getAttribute(LsAttributes.WEB_LOGIN_USER) ){
                return "redirect:login";
            }
            
            removeSessionAttribute(request);
            
            List<String> dataHeaders = new ArrayList<String>();
            dataHeaders.add("目标医院CODE修正");
            dataHeaders.add("目标医生");
            dataHeaders.add("最新PSR CODE");
            
            long begin = System.currentTimeMillis();
            List<Doctor> doctors = ExcelUtils.getDoctorDataFromFile(loadFile(request), dataHeaders);
            long end = System.currentTimeMillis();
            logger.info("doctor data size is " + doctors.size() + ", spend time " + (end - begin) + " ms");
            List<Doctor> duplicateDoctors = uploadService.uploadDoctorData(doctors);
            
            if( null != duplicateDoctors && !duplicateDoctors.isEmpty()){
            	request.getSession().setAttribute(LsAttributes.INVALID_DATA, duplicateDoctors);
            }
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
        }catch(Exception e){
            logger.error("fail to upload the file,",e);
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
        }
        request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadDoctorResult_div");
        return "redirect:showUploadData";
    }
    
    @RequestMapping("/doUploadPortNumData")
    public String doUploadPortNumData(HttpServletRequest request){
    	logger.info("upload the port num data..");
    	try{
    		if( null == request.getSession().getAttribute(LsAttributes.WEB_LOGIN_USER) ){
    			return "redirect:login";
    		}
    		
    		removeSessionAttribute(request);
    		
    		List<String> dataHeaders = new ArrayList<String>();
    		dataHeaders.add("Hospital Code");
    		dataHeaders.add("雾化端口数量");
    		
    		long begin = System.currentTimeMillis();
    		List<Hospital> portNums = ExcelUtils.getPortNumDataFromFile(loadFile(request), dataHeaders);
    		long end = System.currentTimeMillis();
    		logger.info("port num data size is " + portNums.size() + ", spend time " + (end - begin) + " ms");
    		
    		for( Hospital hospital : portNums ){
    			try{
    				hospitalService.uploadPortNumData(hospital);
    			}catch(Exception e){
    				logger.error(String.format("fail to update the port nums for the hospital %s,", hospital.getCode()),e);
    			}
    		}
    		
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
    	}catch(Exception e){
    		logger.error("fail to upload the file,",e);
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
    	}
    	request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadPortNumResult_div");
    	return "redirect:showUploadData";
    }
    
    @RequestMapping("/doUploadDDI")
    public String doUploadDDI(HttpServletRequest request){
    	logger.info("upload the DDI data..");
    	try{
    		if( null == request.getSession().getAttribute(LsAttributes.WEB_LOGIN_USER) ){
    			return "redirect:login";
    		}
    		
    		logger.info("refresh the message info firstly");
    		request.getSession().removeAttribute(LsAttributes.UPLOAD_FILE_MESSAGE);
    		logger.info("refresh the message info done.");
    		
    		List<String> dataHeaders = new ArrayList<String>();
    		List<String> regions = userService.getAllRegionName();
    		for( String regionCenter : regions ){
    		    dataHeaders.add(regionCenter);
    		}
    		
    		long begin = System.currentTimeMillis();
    		List<DDIData> ddiDatas = ExcelUtils.getDDIDataFromFile(loadFile(request), dataHeaders);
    		long end = System.currentTimeMillis();
    		logger.info("ddi data size is " + ddiDatas.size() +", spend time " + (end - begin) + " ms");
    		logger.info("delete old data");
    		userService.deleteDDI();
    		
			logger.info("insert the data of ddi");
			userService.insertDDI(ddiDatas);
			
    	}catch(Exception e){
    		logger.error("fail to upload the file,",e);
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
    	}
    	request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadDDIResult_div");
    	return "redirect:showUploadData";
    }
    
    @RequestMapping("/doUploadUserData")
    public String doUploadUserData(HttpServletRequest request){
    	try{
    		List<String> headers = new ArrayList<String>();
    		headers.add("User Code");
    		headers.add("Name");
    		headers.add("BU");
    		headers.add("Region Center");
    		headers.add("大区");
    		headers.add("Team Code");
    		headers.add("Team");
    		headers.add("层级");
    		headers.add("Tel");
    		
    		logger.info("remove the old user infos firstly");
    		userService.delete();
    		
    		long begin = System.currentTimeMillis();
    		List<UserInfo> users = ExcelUtils.getUserInfosFromFile(loadFile(request), headers);
    		long end = System.currentTimeMillis();
    		logger.info("user size is " + users.size() + ", spend time " + (end - begin) + " ms");
    		userService.insert(users);
    		long finish = System.currentTimeMillis();
    		logger.info("time spent to insert into DB is " + (finish-end) + " ms");
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
    	}catch(Exception e){
    		logger.error("fail to upload the file,",e);
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
    	}
    	return "redirect:showUploadData";
    }
    
    
    @RequestMapping("/doUploadData")
    public String doUploadData(HttpServletRequest request){
        try{
            List<String> headers = new ArrayList<String>();
            headers.add("Sub Institution");
            headers.add("Name");
            headers.add("Province");
            headers.add("City");
            headers.add("DSM_CODE");
            headers.add("DSM_NAME");
            headers.add("大区name");//RSM
            headers.add("代表姓名");//sales
            headers.add("Region");
            
            logger.info("remove the old hospital infos firstly");
            hospitalService.delete();
            
            long begin = System.currentTimeMillis();
            List<Hospital> hospitals = ExcelUtils.getHospitalsFromFile(loadFile(request), headers);
            long end = System.currentTimeMillis();
    		logger.info("hospital size is " + hospitals.size() + ", spend time " + (end - begin) + " ms");
    		
    		hospitalService.insert(hospitals);
    		long finish = System.currentTimeMillis();
    		logger.info("time spent to insert into DB is " + (finish-end) + " ms");
    		
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
        }catch(Exception e){
            logger.error("fail to upload the file,",e);
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
        }
        return "redirect:showUploadData";
    }
    
    private String loadFile(HttpServletRequest request){
    	String savePath =request.getSession().getServletContext().getRealPath("/")+"uploadfiles\\";  
    	String fileName = "";
    	try{
    		request.setCharacterEncoding("UTF-8");
    		File f1 = new File(savePath);
    		if (!f1.exists()) {
    			f1.mkdirs();  
    		}  
    		DiskFileItemFactory fac = new DiskFileItemFactory();
    		ServletFileUpload upload = new ServletFileUpload(fac);
    		
    		upload.setHeaderEncoding("utf-8");  
    		List fileList = null;
    		try {  
    			fileList = upload.parseRequest(request);  
    		} catch (FileUploadException ex) {
    			ex.printStackTrace();
    		}
    		
    		FileItem item=(FileItem)fileList.get(0);
    		
    		if( item.isFormField() ){
    			logger.info(item.getFieldName());
    			logger.info(item.getString());
    		}else{
    			fileName = item.getName();
    			if( fileName.indexOf("\\") > 0 ){
    			    fileName = fileName.substring(fileName.lastIndexOf("\\")+1, fileName.length());
    			}
    			FileOutputStream fos = new FileOutputStream(savePath+fileName);
    			InputStream in = item.getInputStream();
    			byte buffer[] = new byte[1024];
    			int len = 0;
    			while((len=in.read(buffer))>0){
    				fos.write(buffer,0,len);
    			}
    			in.close();
    			fos.close();
    		}
    	}catch(Exception e){
    		logger.error("fail to load the file");
    	}
    	logger.info(String.format("loadFile... the file name is %s", fileName));
    	return savePath+fileName;
    }

    @RequestMapping("/doUploadBMUserData")
    public String doUploadBMUserData(HttpServletRequest request){
    	try{
    		List<String> headers = new ArrayList<String>();
    		headers.add("User Code");
    		headers.add("Name");
    		headers.add("Type");
    		headers.add("Tel");
    		headers.add("E-mail");
    		
    		long begin = System.currentTimeMillis();
    		List<UserInfo> users = ExcelUtils.getBMUserInfosFromFile(loadFile(request), headers);
    		long end = System.currentTimeMillis();
    		logger.info(String.format("user size is %s, spend time %s ms", users==null?0:users.size(),(end - begin)));
    		
    		logger.info("delete the old BM users first");
    		userService.deleteBMUsers();
    		
    		logger.info("begin to insert the new BM users");
    		userService.insertBMUsers(users);
    		long finish = System.currentTimeMillis();
    		logger.info("time spent to insert into DB is " + (finish-end) + " ms");
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
    	}catch(Exception e){
    		logger.error("fail to upload the file,",e);
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
    	}
    	request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadBMUserResult_div");
    	return "redirect:showUploadData";
    }
    
    @RequestMapping("/doUploadUserCode")
    public String doUploadUserCode(HttpServletRequest request){
    	try{
    		List<String> headers = new ArrayList<String>();
    		headers.add("老Code");
    		headers.add("新Code");
    		
    		long begin = System.currentTimeMillis();
    		List<UserCode> userCodes = ExcelUtils.getUserCodesFromFile(loadFile(request), headers);
    		long end = System.currentTimeMillis();
    		logger.info("userCodes size is " + userCodes.size() + ", spend time " + (end - begin) + " ms");
    		userService.updateUserCodes(userCodes);
    		long finish = System.currentTimeMillis();
    		logger.info("time spent to update the DB is " + (finish-end) + " ms");
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
    	}catch(Exception e){
    		logger.error("fail to upload the file,",e);
    		request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
    	}
    	request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadUserCodeResult_div");
    	return "redirect:showUploadData";
    }
    
    /**
     * 上传
     * @param request
     * @return
     */
    @RequestMapping("/doUploadWhbwHospital")
    public String doUploadWhbwHospital(HttpServletRequest request){
        logger.info("upload the whbw hospitals..");
        List<String> dataHeaders = new ArrayList<String>();
        try{
            if( null == request.getSession().getAttribute(LsAttributes.WEB_LOGIN_USER) ){
                return "redirect:login";
            }
            
            removeSessionAttribute(request);
            
            dataHeaders.add("Hospital Code");
            
            long begin = System.currentTimeMillis();
            List<Hospital> whbwHospitals = ExcelUtils.getWhbwHospitalFromFile(loadFile(request), dataHeaders);
            long end = System.currentTimeMillis();
            logger.info("whbwHospitals size is " + whbwHospitals.size() + ", spend time " + (end - begin) + " ms");
            uploadService.uploadWhbwHospital(whbwHospitals);
            
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
        }catch(Exception e){
            logger.error("fail to upload the file,",e);
            request.getSession().setAttribute(LsAttributes.UPLOAD_FILE_MESSAGE, (null==e.getMessage()||"".equalsIgnoreCase(e.getMessage()))?LsAttributes.RETURNED_MESSAGE_1:e.getMessage());
        }finally{
        	dataHeaders = null;
        }
        request.getSession().setAttribute(LsAttributes.MESSAGE_AREA_ID, "uploadWhbwHosResult_div");
        return "redirect:showUploadData";
    }
    
    private void removeSessionAttribute( HttpServletRequest request){
    	logger.info("refresh the message info firstly");
        request.getSession().removeAttribute(LsAttributes.INVALID_DATA);
        request.getSession().removeAttribute(LsAttributes.EXISTS_DATA);
        request.getSession().removeAttribute(LsAttributes.VALID_DATA_NUM);
        request.getSession().removeAttribute(LsAttributes.UPLOAD_FILE_MESSAGE);
        logger.info("refresh the message info done.");
    }
}
