package com.chalet.lskpi.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.MonthlyData;
import com.chalet.lskpi.model.PediatricsData;
import com.chalet.lskpi.model.ReportFileObject;
import com.chalet.lskpi.model.RespirologyData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WebUserInfo;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.service.PediatricsService;
import com.chalet.lskpi.service.RespirologyService;
import com.chalet.lskpi.service.UserService;
import com.chalet.lskpi.utils.BrowserUtils;
import com.chalet.lskpi.utils.CustomizedProperty;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.LsKPIModelAndView;
import com.chalet.lskpi.utils.StringUtils;

@Controller
public class IndexController extends BaseController{
    
    private Logger logger = Logger.getLogger(IndexController.class);
    
    @Autowired
    @Qualifier("respirologyService")
    private RespirologyService respirologyService;
    
    @Autowired
    @Qualifier("pediatricsService")
    private PediatricsService pediatricsService;
    
    @Autowired
    @Qualifier("userService")
    private UserService userService;
    
    @Autowired
    @Qualifier("hospitalService")
    private HospitalService hospitalService;
    
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        verifyCurrentUser(request,view);
        
        if( !BrowserUtils.isMoblie(request.getHeader("User-Agent")) ){
            String paramDate = request.getParameter(LsAttributes.CURRENT_DATE);
            String sessionDate = (String)request.getSession(true).getAttribute(LsAttributes.CURRENT_DATE);
            
            if( ( null == paramDate || "".equalsIgnoreCase(paramDate) ) 
                    && ( null == sessionDate || "".equalsIgnoreCase(sessionDate) ) ){
                view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
                view.setViewName("index");
                return view;
            }
            
            if( null == paramDate || "".equalsIgnoreCase(paramDate) ){
                paramDate = sessionDate;
            }else{
                paramDate = StringUtils.decode(paramDate);
            }
            
            logger.info("decode the parameter date, paramDate is " + paramDate);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String currentDate = formatter.format(new Date());
            
            
            if( !currentDate.equalsIgnoreCase(paramDate) ){
                view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
                view.setViewName("index");
                return view;
            }else{
                request.getSession(true).setAttribute(LsAttributes.CURRENT_DATE, paramDate);
            }
            
        }
        
    	view.setViewName("index");
        return view;
    }
    
    @RequestMapping("/collectData")
    public ModelAndView collectData(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( !super.isCurrentUserValid(currentUser, currentUserTel, view, false) ){
        	return view;
        }
        
        view.setViewName("department");
        return view;
    }
    
    @RequestMapping("/collectmonthlydata")
    public ModelAndView collectmonthlydata(HttpServletRequest request){
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	logger.info(String.format("collect monthly data, current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
    	if( !super.isCurrentUserValid(currentUser, currentUserTel, view, true) ){
    		return view;
    	}
    	
    	int monthlyCollectionDate = Integer.parseInt(CustomizedProperty.getContextProperty("monthly_collection_date", "10"));
    	Calendar today = Calendar.getInstance();
    	today.setTime(new Date());
        
    	int dayInMonth = today.get(Calendar.DAY_OF_MONTH);
    	int curMonth = today.get(Calendar.MONTH) + 1;
    	logger.info(String.format("current user tel is %s, current month is %s, dayInMonth is %s", currentUserTel,curMonth,dayInMonth));
    	if( dayInMonth > monthlyCollectionDate ){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.MONTHLYCOLLECTIONDATEERROR);
    		view.setViewName("department");
            return view;
    	}
    	
    	try {
    	    List<Hospital> monthlyHospitals = hospitalService.getMonthlyHospitalsByUserTel(currentUserTel);
			view.addObject("hospitals", monthlyHospitals);
			
			String hospitalCode = request.getParameter("hospitalCode");
			MonthlyData existedData = new MonthlyData();
			if( (null != hospitalCode && !"".equalsIgnoreCase(hospitalCode)) ){
			    logger.info(String.format("get the monly data of the selected hospital - %s", hospitalCode));
			    existedData = hospitalService.getMonthlyData(hospitalCode, new Date());
			    view.addObject("hospitalCode", hospitalCode);
			}
			view.addObject("existedData", existedData);
			
			String message = "";
			if( null != request.getSession().getAttribute(LsAttributes.COLLECT_MONTHLYDATA_MESSAGE) ){
				message = (String)request.getSession().getAttribute(LsAttributes.COLLECT_MONTHLYDATA_MESSAGE);
			}
			view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, message);
			request.getSession().removeAttribute(LsAttributes.COLLECT_MONTHLYDATA_MESSAGE);
		} catch (Exception e) {
			logger.error("fail to init the monthly collection page,",e);
		}
    	
    	view.setViewName("collectMonthlyData");
    	return view;
    }
    
    @RequestMapping("/doCollectMonth")
    public String doCollectMonth(HttpServletRequest request){
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	try{
    	    UserInfo operator = (UserInfo)request.getSession(true).getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        	logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,operator));
        	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == operator ){
        	    request.getSession().setAttribute(LsAttributes.COLLECT_MONTHLYDATA_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
        		return "redirect:collectmonthlydata";
        	}
        	if(!( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(operator.getLevel()) 
        	                || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(operator.getLevel()))){
        	    request.getSession().setAttribute(LsAttributes.COLLECT_MONTHLYDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
        	    return "redirect:collectmonthlydata";
        	}
        	
            String dataId = request.getParameter("dataId");
            String hospitalCode = request.getParameter("hospitalCode");
            logger.info(String.format("doing the monthly data persistence, dataId is %s, hospitalCode is %s", dataId,hospitalCode));
            
            UserInfo primarySales = hospitalService.getPrimarySalesOfHospital(hospitalCode);
            
            //儿科急门诊
            double pedemernum = StringUtils.getDoubleFromString(request.getParameter("pedemernum"));
            //儿科病房
            double pedroomnum = StringUtils.getDoubleFromString(request.getParameter("pedroomnum"));
            //呼吸科
            double resnum = StringUtils.getDoubleFromString(request.getParameter("resnum"));
            //外科及其它科
            double othernum = StringUtils.getDoubleFromString(request.getParameter("othernum"));
            
        	/**
        	 * 儿科门急诊药房
        	 */
            double pedEmerDrugStore = StringUtils.getDoubleFromString(request.getParameter("pedEmerDrugStore"));
        	/**
        	 * 儿科门急诊雾化室
        	 */
            double pedEmerWh = StringUtils.getDoubleFromString(request.getParameter("pedEmerWh"));
        	/**
        	 * 儿科病房药房
        	 */
            double pedRoomDrugStore = StringUtils.getDoubleFromString(request.getParameter("pedRoomDrugStore"));
        	/**
        	 * 儿科病房药房雾化
        	 */
            double pedRoomDrugStoreWh = StringUtils.getDoubleFromString(request.getParameter("pedRoomDrugStoreWh"));
            /**
        	 * 家庭雾化
        	 */
            double homeWh = StringUtils.getDoubleFromString(request.getParameter("homeWh"));
        	/**
        	 * 呼吸科门诊
        	 */
            double resClinic = StringUtils.getDoubleFromString(request.getParameter("resClinic"));
        	/**
        	 * 呼吸科病房
        	 */
            double resRoom = StringUtils.getDoubleFromString(request.getParameter("resRoom"));
            
            logger.info(String.format("pedEmerDrugStore = %s,pedEmerWh=%s,pedRoomDrugStore=%s,pedRoomDrugStoreWh=%s,resClinic=%s,resRoom=%s",pedEmerDrugStore,pedEmerWh,pedRoomDrugStore,pedRoomDrugStoreWh,resClinic,resRoom));
            
            logger.info(String.format("get the monly data of the selected hospital - %s when collecting", hospitalCode));
            MonthlyData existedData = hospitalService.getMonthlyData(hospitalCode, new Date());
            if( (null == dataId || "".equalsIgnoreCase(dataId)) && null != existedData ){
                dataId = String.valueOf(existedData.getId());
                logger.info(String.format("the monthly data is found which id is %s", dataId));
            }
            
            //new data
            if( null == dataId || "".equalsIgnoreCase(dataId) ){
                MonthlyData monthlyData = new MonthlyData();
                
//                monthlyData.setPedemernum(pedemernum);
//                monthlyData.setPedroomnum(pedroomnum);
//                monthlyData.setResnum(resnum);
                monthlyData.setOthernum(othernum);
                
                monthlyData.setPedEmerDrugStore(pedEmerDrugStore);
                monthlyData.setPedEmerWh(pedEmerWh);
                monthlyData.setPedRoomDrugStore(pedRoomDrugStore);
                monthlyData.setPedRoomDrugStoreWh(pedRoomDrugStoreWh);
                monthlyData.setHomeWh(homeWh);
                monthlyData.setResClinic(resClinic);
                monthlyData.setResRoom(resRoom);
                
                monthlyData.setOperatorName(operator.getName());
                if( null != primarySales ){
                    monthlyData.setOperatorCode(primarySales.getUserCode());
                }else{
                    monthlyData.setOperatorCode(operator.getUserCode());
                }
                monthlyData.setHospitalCode(hospitalCode);
                if( null != primarySales ){
                    monthlyData.setDsmCode(primarySales.getSuperior());
                }else{
                    monthlyData.setDsmCode(operator.getSuperior());
                }
                monthlyData.setRsmRegion(operator.getRegion());
                monthlyData.setRegion(operator.getRegionCenter());
                monthlyData.setCreateDate(new Date());
                
                logger.info("insert the data of monthly");
                hospitalService.insertMonthlyData(monthlyData);
            }else{
                //update the current data
                MonthlyData monthlyData = hospitalService.getMonthlyDataById(Integer.parseInt(dataId));
                if( null == monthlyData ){
                    request.getSession().setAttribute(LsAttributes.COLLECT_MONTHLYDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
                }else{
//                    monthlyData.setPedemernum(pedemernum);
//                    monthlyData.setPedroomnum(pedroomnum);
//                    monthlyData.setResnum(resnum);
                	monthlyData.setPedEmerDrugStore(pedEmerDrugStore);
                	monthlyData.setPedEmerWh(pedEmerWh);
                	monthlyData.setPedRoomDrugStore(pedRoomDrugStore);
                	monthlyData.setPedRoomDrugStoreWh(pedRoomDrugStoreWh);
                	monthlyData.setHomeWh(homeWh);
                	monthlyData.setResClinic(resClinic);
                	monthlyData.setResRoom(resRoom);
                    monthlyData.setOthernum(othernum);
                    monthlyData.setOperatorName(operator.getName());
                    
                    logger.info("update the data of monthly");
                    hospitalService.updateMonthlyData(monthlyData);
                }
            }
        
            request.getSession().setAttribute(LsAttributes.COLLECT_MONTHLYDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
    	}catch(Exception e){
    	    logger.error("fail to collect monthly data "+e.getMessage(),e);
    	    request.getSession().setAttribute(LsAttributes.COLLECT_MONTHLYDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
    	}
    
    	return "redirect:collectmonthlydata";
    }
    
    @RequestMapping("/respirology")
    public ModelAndView respirology(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String operator_telephone = verifyCurrentUser(request,view);
        
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( !super.isCurrentUserValid(currentUser, operator_telephone, view, true) ){
        	return view;
        }
        
        try {
            List<Hospital> hospitals = new ArrayList<Hospital>();
			hospitals = hospitalService.getHospitalsByUserTel(operator_telephone,LsAttributes.DEPARTMENT_RES);
			view.addObject("hospitals", hospitals);
			/**
			 * 
			List<String> recipeTypes = new ArrayList<String>();
			for( int i = 1; i <=7; i++ ){
				recipeTypes.add(i+"");
			}
			view.addObject("recipeTypes", recipeTypes);
			 */
			
			String selectedHospital = request.getParameter("selectedHospital");
			String hospitalName = "";
			RespirologyData existedData = new RespirologyData();
			if( (null != selectedHospital && !"".equalsIgnoreCase(selectedHospital)) ){
			    if( selectedHospital.indexOf("*") == 0 ){
			        hospitalName = selectedHospital.substring(2).trim();
	            }else{
	                hospitalName = selectedHospital;
	            }
			    logger.info("get the respirology data of the selected hospital - " + hospitalName);
			    existedData = respirologyService.getRespirologyDataByHospital(hospitalName);
			    if( existedData != null ){
			    	logger.info(String.format("init res existedData is %s", existedData.getHospitalName()));
			    }
			    view.addObject("selectedHospital", selectedHospital);
			    
			    Hospital selectedHos = hospitalService.getHospitalByName(hospitalName);
			    view.addObject("selectedHos", selectedHos);
			}
			view.addObject("existedData", existedData);
			
			String message = "";
			if( null != request.getSession().getAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE) ){
				message = (String)request.getSession().getAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE);
			}
			view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, message);
			request.getSession().removeAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE);
			view.setViewName("respirologyform");
		} catch (Exception e) {
			logger.error("fail to init the respirology page,",e);
		}
        
        return view;
    }
    
    @RequestMapping("/collectRespirology")
    public String collectRespirology(HttpServletRequest request){
    	String operator_telephone = (String)request.getSession(true).getAttribute(LsAttributes.CURRENT_OPERATOR);
    	logger.info("collectRespirology, user from session is " + operator_telephone);
        try{
        	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
            if( null == operator_telephone || "".equalsIgnoreCase(operator_telephone) || null == currentUser ){
                request.getSession().setAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            	return "redirect:respirology";
            }
            if(! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                            || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()))){
                request.getSession().setAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
                return "redirect:respirology";
            }
        	
            String dataId = request.getParameter("dataId");
            String hospitalName = request.getParameter("hospital");
            
            if( null == hospitalName || "".equalsIgnoreCase(hospitalName) ){
            	request.getSession().setAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE, LsAttributes.RETURNED_MESSAGE_7);
            	return "redirect:respirology";
            }
            
            if( hospitalName.indexOf("*") == 0 ){
            	hospitalName = hospitalName.substring(2).trim();
            }
            logger.info(String.format("doing the data persistence, dataId is %s, hospital is %s", dataId,hospitalName));
            
            RespirologyData existedData = respirologyService.getRespirologyDataByHospital(hospitalName);
            if( null != existedData ){
            	logger.info(String.format("check the res data again when user %s collecting, the data id is %s", operator_telephone, existedData.getDataId()));
            }
            if( ( null == dataId || "".equalsIgnoreCase(dataId) || "0".equalsIgnoreCase(dataId) ) 
                    && null != existedData){
                dataId = String.valueOf(existedData.getDataId());
                logger.info(String.format("the res data is found which id is %s", dataId));
            }
            
            //new data
            if( null == dataId || "".equalsIgnoreCase(dataId) ){
                RespirologyData respirologyData = new RespirologyData();
                populateRespirologyData(request,respirologyData);
                
                Hospital hospital = hospitalService.getHospitalByName(hospitalName);
                
                logger.info("insert the data of respirology");
                respirologyService.insert(respirologyData, currentUser, hospital);
            }else{
                //update the current data
                RespirologyData dbRespirologyData = respirologyService.getRespirologyDataById(Integer.parseInt(dataId));
                if( null == dbRespirologyData ){
                    request.getSession().setAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
                }else{
                    populateRespirologyData(request,dbRespirologyData);
                    
                    logger.info("update the data of respirology");
                    respirologyService.update(dbRespirologyData, currentUser);
                }
            }
            
            request.getSession().setAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
        }catch(Exception e){
            logger.error("fail to collect respirology "+e.getMessage(),e);
            request.getSession().setAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
        }
        
        return "redirect:respirology";
    }
    
    @RequestMapping("/pediatrics")
    public ModelAndView pediatrics(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String operator_telephone = verifyCurrentUser(request,view);
        
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( !super.isCurrentUserValid(currentUser, operator_telephone, view, true) ){
        	return view;
        }
        
        List<Hospital> hospitals = new ArrayList<Hospital>();
        try{
            hospitals = hospitalService.getHospitalsByUserTel(operator_telephone,LsAttributes.DEPARTMENT_PED);
            
            List<String> recipeTypes = new ArrayList<String>();
            for( int i = 1; i <=7; i++ ){
                recipeTypes.add(i+"");
            }
            
            view.addObject("recipeTypes", recipeTypes);
            view.addObject("hospitals", hospitals);
            
            String selectedHospital = request.getParameter("selectedHospital");
            PediatricsData existedData = new PediatricsData();
            if( (null != selectedHospital && !"".equalsIgnoreCase(selectedHospital)) ){
            	
            	logger.info("get the pediatrics data of the selected hospital - " + selectedHospital);
                existedData = pediatricsService.getPediatricsDataByHospital(selectedHospital);
                
                if( existedData != null ){
			    	logger.info(String.format("init ped existedData is %s", existedData.getHospitalName()));
			    }
                view.addObject("selectedHospital", selectedHospital);
                
                //if existedData is null, then the data is not set, the port num should be shown as the one of hospital.
                if( existedData == null ){
                	Hospital hospital = hospitalService.getHospitalByCode(selectedHospital);
                	existedData = new PediatricsData();
                	existedData.setPortNum(hospital.getPortNum());
                }
            }
            
            view.addObject("existedData", existedData);
            
            String message = "";
            if( null != request.getSession().getAttribute(LsAttributes.COLLECT_PEDIATRICS_MESSAGE) ){
                message = (String)request.getSession().getAttribute(LsAttributes.COLLECT_PEDIATRICS_MESSAGE);
            }
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, message);
            request.getSession().removeAttribute(LsAttributes.COLLECT_PEDIATRICS_MESSAGE);
            
        }catch(Exception e){
            logger.info("fail to init the pediatrics,",e);
        }
        view.setViewName("pediatricsform");
        return view;
    }
    
    @RequestMapping("/collectPediatrics")
    public String collectPediatrics(HttpServletRequest request){
    	String operator_telephone = (String)request.getSession(true).getAttribute(LsAttributes.CURRENT_OPERATOR);
    	logger.info("collectPediatrics, user = "+operator_telephone);
        try{
        	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
            if( null == operator_telephone || "".equalsIgnoreCase(operator_telephone) || null == currentUser ){
                request.getSession().setAttribute(LsAttributes.COLLECT_PEDIATRICS_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            	return "redirect:pediatrics";
            }
            if( ! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                            || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()))){
                request.getSession().setAttribute(LsAttributes.COLLECT_PEDIATRICS_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
                return "redirect:pediatrics";
            }
            
            String dataId = request.getParameter("dataId");
            String hospitalCode = request.getParameter("hospital");
            
            if( null == hospitalCode || "".equalsIgnoreCase(hospitalCode) ){
            	request.getSession().setAttribute(LsAttributes.COLLECT_PEDIATRICS_MESSAGE, LsAttributes.RETURNED_MESSAGE_7);
            	return "redirect:pediatrics";
            }
            
            logger.info(String.format("doing the data persistence, dataId is %s, hospital is %s", dataId,hospitalCode));
            
            PediatricsData existedData = pediatricsService.getPediatricsDataByHospital(hospitalCode);
            if( null != existedData ){
            	logger.info(String.format("check the ped data again when user %s collecting, the data id is %s", operator_telephone, existedData.getDataId()));
            }
            if( ( null == dataId || "".equalsIgnoreCase(dataId) || "0".equalsIgnoreCase(dataId) ) 
                    && null != existedData){
                dataId = String.valueOf(existedData.getDataId());
                logger.info(String.format("the res data is found which id is %s", dataId));
            }
            
            //new data
            if( null == dataId || "".equalsIgnoreCase(dataId) || "0".equalsIgnoreCase(dataId) ){
                PediatricsData pediatricsData = new PediatricsData();
                populatePediatricsData(request,pediatricsData);
                
                Hospital hospital = hospitalService.getHospitalByCode(hospitalCode);
                
                logger.info("insert the data of pediatrics");
                pediatricsService.insert(pediatricsData, currentUser, hospital);
            }else{
                //update the current data
                PediatricsData dbPediatricsData = pediatricsService.getPediatricsDataById(Integer.parseInt(dataId));
                if( null == dbPediatricsData ){
                    request.getSession().setAttribute(LsAttributes.COLLECT_RESPIROLOGY_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
                }else{
                    populatePediatricsData(request,dbPediatricsData);
                    
                    logger.info("update the data of pediatrics");
                    pediatricsService.update(dbPediatricsData, currentUser);
                }
            }
            
            request.getSession().setAttribute(LsAttributes.COLLECT_PEDIATRICS_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
        }catch(Exception e){
            logger.error("fail to collect pediatrics "+e.getMessage(),e);
            request.getSession().setAttribute(LsAttributes.COLLECT_PEDIATRICS_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
        }
        
        return "redirect:pediatrics";
    }
    
    @RequestMapping("/showUploadData")
    public ModelAndView showUploadData(HttpServletRequest request){
        ModelAndView view = new ModelAndView();
        initUploadDataUsers(view);
        
        if( null != request.getSession(true).getAttribute(LsAttributes.WEB_LOGIN_USER)){
        	view.addObject(LsAttributes.WEB_LOGIN_USER, (WebUserInfo)request.getSession(true).getAttribute(LsAttributes.WEB_LOGIN_USER));
        }
        view.setViewName("uploadData");

        //upload daily res data message
        int validDataMessage = 0;
        if( null != request.getSession().getAttribute(LsAttributes.VALID_DATA_NUM) ){
        	validDataMessage = (Integer)request.getSession().getAttribute(LsAttributes.VALID_DATA_NUM);
        	view.addObject(LsAttributes.VALID_DATA_NUM, validDataMessage);
        	request.getSession().removeAttribute(LsAttributes.VALID_DATA_NUM);
        }
        
        List<RespirologyData> inValidData = new ArrayList<RespirologyData>();
        if( null != request.getSession().getAttribute(LsAttributes.INVALID_DATA) ){
        	inValidData = (List<RespirologyData>)request.getSession().getAttribute(LsAttributes.INVALID_DATA);
        	view.addObject(LsAttributes.INVALID_DATA, inValidData);
        	request.getSession().removeAttribute(LsAttributes.INVALID_DATA);
        }
        
        List<RespirologyData> existsData = new ArrayList<RespirologyData>();
        if( null != request.getSession().getAttribute(LsAttributes.EXISTS_DATA) ){
        	existsData = (List<RespirologyData>)request.getSession().getAttribute(LsAttributes.EXISTS_DATA);
        	view.addObject(LsAttributes.EXISTS_DATA, existsData);
        	request.getSession().removeAttribute(LsAttributes.EXISTS_DATA);
        }

        this.populateFileInfo(view, request);

        return view;
    }
    
    @RequestMapping("/showWebUploadData")
    public ModelAndView showWebUploadData(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        
        String operator_telephone = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( null == operator_telephone 
                || "".equalsIgnoreCase(operator_telephone) 
                || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            view.setViewName("index");
            return view;
        }
        
//        if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
//            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
//            view.setViewName("index");
//            return view;
//        }
        
        view.setViewName("uploadData");
        initUploadDataUsers(view);
        
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
        String localPath = request.getRealPath("/");
        initKPISource(view, basePath, localPath);
        
        if( null != request.getSession().getAttribute(LsAttributes.WEEKLY_PDF_REFRESH_MESSAGE) ){
            view.addObject(LsAttributes.WEEKLY_PDF_REFRESH_MESSAGE, (String)request.getSession().getAttribute(LsAttributes.WEEKLY_PDF_REFRESH_MESSAGE));
            request.getSession().removeAttribute(LsAttributes.WEEKLY_PDF_REFRESH_MESSAGE);
        }
        
        if( null != request.getSession().getAttribute(LsAttributes.WEEKLY_HOS_REFRESH_MESSAGE) ){
            view.addObject(LsAttributes.WEEKLY_HOS_REFRESH_MESSAGE, (String)request.getSession().getAttribute(LsAttributes.WEEKLY_HOS_REFRESH_MESSAGE));
            request.getSession().removeAttribute(LsAttributes.WEEKLY_HOS_REFRESH_MESSAGE);
        }
        
        if( null != request.getSession().getAttribute("reportFiles") ){
        	view.addObject("reportFiles", request.getSession().getAttribute("reportFiles"));
        	request.getSession().removeAttribute("reportFiles");
        }

        this.populateFileInfo(view, request);
        populateQueryParams(view, request);
        
        return view;
    }
    
    private void populateQueryParams(ModelAndView view, HttpServletRequest request){
    	if( null != request.getSession().getAttribute("chooseDate_weekly") ){
    		view.addObject("chooseDate_weekly", request.getSession().getAttribute("chooseDate_weekly"));
        	request.getSession().removeAttribute("chooseDate_weekly");
    	}
    	if( null != request.getSession().getAttribute("selectedRSD") ){
    		view.addObject("selectedRSD", request.getSession().getAttribute("selectedRSD"));
    		request.getSession().removeAttribute("selectedRSD");
    	}
    	if( null != request.getSession().getAttribute("selectedRSM") ){
    		view.addObject("selectedRSM", request.getSession().getAttribute("selectedRSM"));
    		request.getSession().removeAttribute("selectedRSM");
    	}
    	if( null != request.getSession().getAttribute("selectedDSM") ){
    		view.addObject("selectedDSM", request.getSession().getAttribute("selectedDSM"));
    		request.getSession().removeAttribute("selectedDSM");
    	}
    	if( null != request.getSession().getAttribute("department") ){
    		view.addObject("department", request.getSession().getAttribute("department"));
    		request.getSession().removeAttribute("department");
    	}
    }
    
    private void populateFileInfo(ModelAndView view, HttpServletRequest request){
        
        if( null != request.getSession().getAttribute(LsAttributes.UPLOAD_FILE_MESSAGE) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, (String)request.getSession().getAttribute(LsAttributes.UPLOAD_FILE_MESSAGE));
            request.getSession().removeAttribute(LsAttributes.UPLOAD_FILE_MESSAGE);
        }
        if( null != request.getSession().getAttribute(LsAttributes.MESSAGE_AREA_ID) ){
        	view.addObject(LsAttributes.MESSAGE_AREA_ID, (String)request.getSession().getAttribute(LsAttributes.MESSAGE_AREA_ID));
        	request.getSession().removeAttribute(LsAttributes.MESSAGE_AREA_ID);
        }
        
    	if( null != request.getSession().getAttribute("dataFile") ){
        	view.addObject("dataFile",(String)request.getSession().getAttribute("dataFile"));
        	request.getSession().removeAttribute("dataFile");
        }
    	
        if( null != request.getSession().getAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE) ){
            view.addObject(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, (String)request.getSession().getAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE));
            request.getSession().removeAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE);
        }
        
        if( null != request.getSession().getAttribute("dsmFileName") ){
            view.addObject("dsmFileName", request.getSession().getAttribute("dsmFileName"));
            request.getSession().removeAttribute("dsmFileName");
        }
        
        if( null != request.getSession().getAttribute("dsmDataFile") ){
            view.addObject("dsmDataFile", request.getSession().getAttribute("dsmDataFile"));
            request.getSession().removeAttribute("dsmDataFile");
        }
        
        if( null != request.getSession().getAttribute("rsmFileName") ){
            view.addObject("rsmFileName", request.getSession().getAttribute("rsmFileName"));
            request.getSession().removeAttribute("rsmFileName");
        }
        
        if( null != request.getSession().getAttribute("rsmDataFile") ){
            view.addObject("rsmDataFile", request.getSession().getAttribute("rsmDataFile"));
            request.getSession().removeAttribute("rsmDataFile");
        }
        if( null != request.getSession().getAttribute("monthlyDataFile") ){
            view.addObject("monthlyDataFile", request.getSession().getAttribute("monthlyDataFile"));
            request.getSession().removeAttribute("monthlyDataFile");
        }
        if( null != request.getSession().getAttribute("homeDataFile") ){
            view.addObject("homeDataFile", request.getSession().getAttribute("homeDataFile"));
            request.getSession().removeAttribute("homeDataFile");
        }
        if( null != request.getSession().getAttribute("doctorDataFile") ){
            view.addObject("doctorDataFile", request.getSession().getAttribute("doctorDataFile"));
            request.getSession().removeAttribute("doctorDataFile");
        }
        if( null != request.getSession().getAttribute("monthlyInRateDataFile") ){
        	view.addObject("monthlyInRateDataFile", request.getSession().getAttribute("monthlyInRateDataFile"));
        	request.getSession().removeAttribute("monthlyInRateDataFile");
        }
        if( null != request.getSession().getAttribute("monthlyInRateDataFileName") ){
        	view.addObject("monthlyInRateDataFileName", request.getSession().getAttribute("monthlyInRateDataFileName"));
        	request.getSession().removeAttribute("monthlyInRateDataFileName");
        }
        
        if( null != request.getSession().getAttribute("monthlyCollectionDataFile") ){
        	view.addObject("monthlyCollectionDataFile", request.getSession().getAttribute("monthlyCollectionDataFile"));
        	request.getSession().removeAttribute("monthlyCollectionDataFile");
        }
        if( null != request.getSession().getAttribute("monthlyCollectionDataFileName") ){
        	view.addObject("monthlyCollectionDataFileName", request.getSession().getAttribute("monthlyCollectionDataFileName"));
        	request.getSession().removeAttribute("monthlyCollectionDataFileName");
        }
        if( null != request.getSession().getAttribute("resMonthDataFile") ){
        	view.addObject("resMonthDataFile", request.getSession().getAttribute("resMonthDataFile"));
        	request.getSession().removeAttribute("resMonthDataFile");
        }
        if( null != request.getSession().getAttribute("resMonthDataFileName") ){
        	view.addObject("resMonthDataFileName", request.getSession().getAttribute("resMonthDataFileName"));
        	request.getSession().removeAttribute("resMonthDataFileName");
        }

    }
    
    private void initUploadDataUsers(ModelAndView view){
        
        List<UserInfo> rsdList = new ArrayList<UserInfo>();
        List<UserInfo> rsmList = new ArrayList<UserInfo>();
        List<UserInfo> dsmList = new ArrayList<UserInfo>();
        
        try{
            rsdList = userService.getUserInfoByLevel(LsAttributes.USER_LEVEL_RSD);
            rsmList = userService.getUserInfoByLevel(LsAttributes.USER_LEVEL_RSM);
            dsmList = userService.getUserInfoByLevel(LsAttributes.USER_LEVEL_DSM);
        }catch(Exception e){
            logger.error("fail to get the rsm users,",e);
        }
        view.addObject("rsdlist", rsdList);
        view.addObject("rsmlist", rsmList);
        view.addObject("dsmlist", dsmList);
    }
    
    private void initKPISource(ModelAndView view, String basePath, String localPath){
        List<List<ReportFileObject>> allSourceFileList = new ArrayList<List<ReportFileObject>>();
        
    	List<ReportFileObject> bowuList = new ArrayList<ReportFileObject>();
    	List<ReportFileObject> pedList = new ArrayList<ReportFileObject>();
    	List<ReportFileObject> resList = new ArrayList<ReportFileObject>();
    	List<ReportFileObject> surgeryList = new ArrayList<ReportFileObject>();
    	List<ReportFileObject> systemDescList = new ArrayList<ReportFileObject>();
    	
    	StringBuffer bowulocalFile = new StringBuffer(localPath);
        StringBuffer bowuremoteFile = new StringBuffer(basePath);
        readFiles(bowulocalFile.append("KPISource/bowu/").toString(),bowuremoteFile.append("KPISource/bowu/").toString(),bowuList);
        
        StringBuffer pedlocalFile = new StringBuffer(localPath);
        StringBuffer pedremoteFile = new StringBuffer(basePath);
        readFiles(pedlocalFile.append("KPISource/ped/").toString(),pedremoteFile.append("KPISource/ped/").toString(),pedList);
        
        StringBuffer reslocalFile = new StringBuffer(localPath);
        StringBuffer resremoteFile = new StringBuffer(basePath);
        readFiles(reslocalFile.append("KPISource/res/").toString(),resremoteFile.append("KPISource/res/").toString(),resList);
        
        StringBuffer surgerylocalFile = new StringBuffer(localPath);
        StringBuffer surgeryremoteFile = new StringBuffer(basePath);
        readFiles(surgerylocalFile.append("KPISource/surgery/").toString(),surgeryremoteFile.append("KPISource/surgery/").toString(),surgeryList);
        
        StringBuffer systemDesclocalFile = new StringBuffer(localPath);
        StringBuffer systemDescremoteFile = new StringBuffer(basePath);
        readFiles(systemDesclocalFile.append("KPISource/systemDesc/").toString(),systemDescremoteFile.append("KPISource/systemDesc/").toString(),systemDescList);
    	
        if( null != bowuList && bowuList.size() > 0){
            bowuList.get(0).setFolderName_en("bowu");
            bowuList.get(0).setFolderName_cn("博雾资料");
            allSourceFileList.add(bowuList);
        }
        
        if( null != pedList && pedList.size() > 0){
            pedList.get(0).setFolderName_en("ped");
            pedList.get(0).setFolderName_cn("儿科资料");
            allSourceFileList.add(pedList);
        }
        
        if( null != resList && resList.size() > 0){
            resList.get(0).setFolderName_en("res");
            resList.get(0).setFolderName_cn("呼吸科资料");
            allSourceFileList.add(resList);
        }
        
        if( null != surgeryList && surgeryList.size() > 0){
            surgeryList.get(0).setFolderName_en("surgery");
            surgeryList.get(0).setFolderName_cn("外科资料");
            allSourceFileList.add(surgeryList);
        }
        
        if( null != systemDescList && systemDescList.size() > 0){
            systemDescList.get(0).setFolderName_en("systemDesc");
            systemDescList.get(0).setFolderName_cn("KPI系统说明");
            allSourceFileList.add(systemDescList);
        }
        
        view.addObject("allSourceFileList", allSourceFileList);
    }
    
    private void readFiles(String localfilepath, String remotefilepath, List<ReportFileObject> fileList){
    	File file = new File(localfilepath);
    	if (!file.isDirectory()) {
            logger.warn(String.format("the file is not a directory, the file name is %s", file.getName()));
	    } else if (file.isDirectory()) {
	    	String[] filelist = file.list();
	        for (int i = 0; i < filelist.length; i++) {
	        	File readfile = new File(localfilepath + filelist[i]);
	            if (!readfile.isDirectory()) {
	            	ReportFileObject fileObj = new ReportFileObject();
	            	fileObj.setFileName(filelist[i]);
	            	fileObj.setFilePath(remotefilepath+filelist[i]);
	            	fileList.add(fileObj);
	            } else if (readfile.isDirectory()) {
	            	logger.warn(String.format("the readfile is a directory, the file name is %s", readfile.getName()));
	            }
	        }
	    }
    }
    
    private void populateRespirologyData(HttpServletRequest request,RespirologyData respirologyData){
        String hospitalName = request.getParameter("hospital");
        if( hospitalName.indexOf("*") == 0 ){
        	hospitalName = hospitalName.substring(2).trim();
        }
        //当日病房病人人数
        int pnum = StringUtils.getIntegerFromString(request.getParameter("pnum"));
        //当日病房内AECOPD病人数
        int aenum = StringUtils.getIntegerFromString(request.getParameter("aenum"));
        //当日雾化人数
        int whnum = StringUtils.getIntegerFromString(request.getParameter("whnum"));
        //当日雾化令舒病人数
        int lsnum = StringUtils.getIntegerFromString(request.getParameter("lsnum"));
        //1mg QD
        double oqd = StringUtils.getDoubleFromString(request.getParameter("oqd"));
        //2mg QD
        double tqd = StringUtils.getDoubleFromString(request.getParameter("tqd"));
        //1mg TID
        double otid = StringUtils.getDoubleFromString(request.getParameter("otid"));
        //2mg BID
        double tbid = StringUtils.getDoubleFromString(request.getParameter("tbid"));
        //2mg TID
        double ttid = StringUtils.getDoubleFromString(request.getParameter("ttid"));
        //3mg BID
        double thbid = StringUtils.getDoubleFromString(request.getParameter("thbid"));
        //4mg BID
        double fbid = StringUtils.getDoubleFromString(request.getParameter("fbid"));
//        //该医院主要处方方式
//        String recipeType = request.getParameter("recipeType");
        
        /**
         * 信必可
         */
        int xbknum = StringUtils.getIntegerFromString(request.getParameter("xbknum"));
        int xbk1num = StringUtils.getIntegerFromString(request.getParameter("xbk1num"));
        int xbk2num = StringUtils.getIntegerFromString(request.getParameter("xbk2num"));
        int xbk3num = StringUtils.getIntegerFromString(request.getParameter("xbk3num"));
        
        respirologyData.setHospitalName(hospitalName);
        respirologyData.setPnum(pnum);
        respirologyData.setAenum(aenum);
        respirologyData.setWhnum(whnum);
        respirologyData.setLsnum(lsnum);
        respirologyData.setOqd(oqd);
        respirologyData.setTqd(tqd);
        respirologyData.setOtid(otid);
        respirologyData.setTbid(tbid);
        respirologyData.setTtid(ttid);
        respirologyData.setThbid(thbid);
        respirologyData.setFbid(fbid);
//        respirologyData.setRecipeType(recipeType);
        respirologyData.setXbknum(xbknum);
        respirologyData.setXbk1num(xbk1num);
        respirologyData.setXbk2num(xbk2num);
        respirologyData.setXbk3num(xbk3num);
    }
    
    private void populatePediatricsData(HttpServletRequest request,PediatricsData pediatricsData){
        String hospitalCode = request.getParameter("hospital");
        
        //当日病房病人人数
        int pnum = StringUtils.getIntegerFromString(request.getParameter("pnum"));
        //当日雾化人数
        int whnum = StringUtils.getIntegerFromString(request.getParameter("whnum"));
        //当日雾化令舒病人数
        int lsnum = StringUtils.getIntegerFromString(request.getParameter("lsnum"));
        //0.5mg QD
        double hqd = StringUtils.getDoubleFromString(request.getParameter("hqd"));
        //0.5mg BID
        double hbid = StringUtils.getDoubleFromString(request.getParameter("hbid"));
        //1mg QD
        double oqd = StringUtils.getDoubleFromString(request.getParameter("oqd"));
        //1mg BID
        double obid = StringUtils.getDoubleFromString(request.getParameter("obid"));
        //2mg QD
        double tqd = StringUtils.getDoubleFromString(request.getParameter("tqd"));
        //2mg BID
        double tbid = StringUtils.getDoubleFromString(request.getParameter("tbid"));
        //该医院主要处方方式
        String recipeType = request.getParameter("recipeType");
        
        pediatricsData.setHospitalCode(hospitalCode);
        pediatricsData.setPnum(pnum);
        pediatricsData.setWhnum(whnum);
        pediatricsData.setLsnum(lsnum);
        pediatricsData.setHqd(hqd);
        pediatricsData.setHbid(hbid);
        pediatricsData.setOqd(oqd);
        pediatricsData.setObid(obid);
        pediatricsData.setTqd(tqd);
        pediatricsData.setTbid(tbid);
        pediatricsData.setRecipeType(recipeType);
    }
}
