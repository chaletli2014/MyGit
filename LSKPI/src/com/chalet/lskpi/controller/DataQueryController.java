package com.chalet.lskpi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.HospitalSalesQueryObj;
import com.chalet.lskpi.model.HospitalSalesQueryParam;
import com.chalet.lskpi.model.Monthly12Data;
import com.chalet.lskpi.model.MonthlyRatioData;
import com.chalet.lskpi.model.ReportProcessData;
import com.chalet.lskpi.model.ReportProcessDataDetail;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WeeklyRatioData;
import com.chalet.lskpi.service.ChestSurgeryService;
import com.chalet.lskpi.service.HomeService;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.service.PediatricsService;
import com.chalet.lskpi.service.RespirologyService;
import com.chalet.lskpi.service.UserService;
import com.chalet.lskpi.utils.BrowserUtils;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.LsKPIModelAndView;

/**
 * @author Chalet
 * @version 创建时间：2014年1月2日 下午9:17:11
 * 类说明
 */

@Controller
public class DataQueryController extends BaseController{

	Logger logger = Logger.getLogger(DataQueryController.class);
    
    @Autowired
    @Qualifier("respirologyService")
    private RespirologyService respirologyService;
    
    @Autowired
    @Qualifier("pediatricsService")
    private PediatricsService pediatricsService;
    
    @Autowired
    @Qualifier("chestSurgeryService")
    private ChestSurgeryService chestSurgeryService;
    
    @Autowired
    @Qualifier("userService")
    private UserService userService;
    
    @Autowired
    @Qualifier("hospitalService")
    private HospitalService hospitalService;
    
    @Autowired
    @Qualifier("homeService")
    private HomeService homeService;
	
    @RequestMapping("/dataQuery")
    public ModelAndView dataQuery(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
        	view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
        	view.setViewName("index");
        	return view;
        }
        
        view.setViewName("dataQueryMenu");
        return view;
    }
    
    @RequestMapping("/reportProcess")
    public ModelAndView reportProcess(HttpServletRequest request){
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
    	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser  || 
        		! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
        				|| LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel())
        				|| LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ) ){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
    		view.setViewName("index");
    		return view;
    	}
    	view.setViewName("reportProcessDepartment");
    	return view;
    }

    @RequestMapping("/resprocess")
    public ModelAndView resprocess(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("ped report process, current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser  || 
                ! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                        || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel())
                        || LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        
        ReportProcessData processData = new ReportProcessData();
        try{
            if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
                processData = respirologyService.getSalesSelfReportProcessRESData(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
                processData = respirologyService.getDSMSelfReportProcessRESData(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
            	logger.info(String.format("start to get rsm self res process, user tel is %s", currentUser.getTelephone()));
                processData = respirologyService.getRSMSelfReportProcessRESData(currentUserTel);
                List<ReportProcessData> dsmProcessDataList = new ArrayList<ReportProcessData>();
                logger.info(String.format("start to get dsm res process of current rsm, user tel is %s", currentUser.getTelephone()));
                List<UserInfo> dsms = userService.getLowerUserOfCurrentUser(currentUser, null);
                for( UserInfo user : dsms ){
                	ReportProcessData dsmProcessData = respirologyService.getDSMSelfReportProcessRESData(user.getTelephone());
                	dsmProcessData.setName(user.getName());
                	dsmProcessDataList.add(dsmProcessData);
                }
                logger.info(String.format("end to get dsm res process of current rsm, user tel is %s", currentUser.getTelephone()));
                view.addObject(LsAttributes.DATAQUERY_PROCESS_CHILD_DATA, dsmProcessDataList);
            }
            view.addObject(LsAttributes.DATAQUERY_PROCESS_DATA, processData);
            view.addObject("currentUser", currentUser);
        }catch(Exception e){
            logger.error("fail to get the process data,",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        
        List<ReportProcessDataDetail> processDataDetail = new ArrayList<ReportProcessDataDetail>();
        try{
            if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
                processDataDetail = respirologyService.getSalesSelfReportProcessRESDetailData(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
                processDataDetail = respirologyService.getDSMSelfReportProcessRESDetailData(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
                processDataDetail = respirologyService.getRSMSelfReportProcessRESDetailData(currentUserTel);
            }
            view.addObject(LsAttributes.DATAQUERY_PROCESS_DATA_DETAIL, processDataDetail);
        }catch(Exception e){
            logger.error("fail to get the process data,",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        
        view.setViewName("reportProcessRES");
        return view;
    }
    
    @RequestMapping("/cheprocess")
    public ModelAndView cheprocess(HttpServletRequest request){
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	logger.info(String.format("che report process, current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
    	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
    		view.setViewName("index");
    		return view;
    	} 
    	if( ! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
    					|| LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel())
    					|| LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ) ){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
    		view.setViewName("index");
    		return view;
    	}
    	
    	ReportProcessData processData = new ReportProcessData();
    	try{
    		if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
    			processData = chestSurgeryService.getSalesSelfReportProcessData(currentUserTel);
    		}else if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
    			processData = chestSurgeryService.getDSMSelfReportProcessData(currentUserTel);
    		}else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
    			logger.info(String.format("start to get rsm self chest surgery process, user tel is %s", currentUser.getTelephone()));
    			processData = chestSurgeryService.getRSMSelfReportProcessData(currentUserTel);
    			List<ReportProcessData> dsmProcessDataList = new ArrayList<ReportProcessData>();
    			logger.info(String.format("start to get dsm chest surgery process of current rsm, user tel is %s", currentUser.getTelephone()));
    			List<UserInfo> dsms = userService.getLowerUserOfCurrentUser(currentUser, null);
    			for( UserInfo user : dsms ){
    				ReportProcessData dsmProcessData = chestSurgeryService.getDSMSelfReportProcessData(user.getTelephone());
    				dsmProcessData.setName(user.getName());
    				dsmProcessDataList.add(dsmProcessData);
    			}
    			logger.info(String.format("end to get dsm chest surgery process of current rsm, user tel is %s", currentUser.getTelephone()));
    			view.addObject(LsAttributes.DATAQUERY_PROCESS_CHILD_DATA, dsmProcessDataList);
    		}
    		view.addObject(LsAttributes.DATAQUERY_PROCESS_DATA, processData);
    		view.addObject("currentUser", currentUser);
    	}catch(Exception e){
    		logger.error("fail to get the chest surgery process data,",e);
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
    	}
    	
    	List<ReportProcessDataDetail> processDataDetail = new ArrayList<ReportProcessDataDetail>();
    	try{
    		if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
    			processDataDetail = chestSurgeryService.getSalesSelfReportProcessDetailData(currentUserTel);
    		}else if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
    			processDataDetail = chestSurgeryService.getDSMSelfReportProcessDetailData(currentUserTel);
    		}else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
    			processDataDetail = chestSurgeryService.getRSMSelfReportProcessDetailData(currentUserTel);
    		}
    		view.addObject(LsAttributes.DATAQUERY_PROCESS_DATA_DETAIL, processDataDetail);
    	}catch(Exception e){
    		logger.error("fail to get the chest surgery process data,",e);
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
    	}
    	
    	view.setViewName("reportProcessCHE");
    	return view;
    }
    
    @RequestMapping("/pedprocess")
    public ModelAndView pedprocess(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("ped report process, current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser  || 
                ! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                        || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel())
                        || LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        
        ReportProcessData processData = new ReportProcessData();
        try{
            if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
                processData = pediatricsService.getSalesSelfReportProcessPEDData(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
                processData = pediatricsService.getDSMSelfReportProcessPEDData(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
            	logger.info(String.format("start to get rsm self ped process, user tel is %s", currentUser.getTelephone()));
                processData = pediatricsService.getRSMSelfReportProcessPEDData(currentUserTel);
                
                List<ReportProcessData> dsmProcessDataList = new ArrayList<ReportProcessData>();
                logger.info(String.format("start to get dsm ped process of current rsm, user tel is %s", currentUser.getTelephone()));
                List<UserInfo> dsms = userService.getLowerUserOfCurrentUser(currentUser, null);
                for( UserInfo user : dsms ){
                	ReportProcessData dsmProcessData = pediatricsService.getDSMSelfReportProcessPEDData(user.getTelephone());
                	dsmProcessData.setName(user.getName());
                	dsmProcessDataList.add(dsmProcessData);
                }
                logger.info(String.format("end to get dsm ped process of current rsm, user tel is %s", currentUser.getTelephone()));
                view.addObject(LsAttributes.DATAQUERY_PROCESS_CHILD_DATA, dsmProcessDataList);
            }
            view.addObject(LsAttributes.DATAQUERY_PROCESS_DATA, processData);
            view.addObject("currentUser", currentUser);
        }catch(Exception e){
            logger.error("fail to get the process data,",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        
        List<ReportProcessDataDetail> processDataDetail = new ArrayList<ReportProcessDataDetail>();
        try{
            if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
                processDataDetail = pediatricsService.getSalesSelfReportProcessPEDDetailData(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
                processDataDetail = pediatricsService.getDSMSelfReportProcessPEDDetailData(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
                processDataDetail = pediatricsService.getRSMSelfReportProcessPEDDetailData(currentUserTel);
            }
            view.addObject(LsAttributes.DATAQUERY_PROCESS_DATA_DETAIL, processDataDetail);
        }catch(Exception e){
            logger.error("fail to get the process data,",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        
        view.setViewName("reportProcessPED");
        return view;
    }
    
    @RequestMapping("/homeprocess")
    public ModelAndView homeprocess(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("home report process, current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            view.setViewName("index");
            return view;
        }
        
        if( ! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                        || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel())
                        || LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        
        ReportProcessData processData = new ReportProcessData();
        try{
            if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
                processData = homeService.getSalesSelfReportProcess(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
                processData = homeService.getDSMSelfReportProcess(currentUserTel);
            }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
                logger.info(String.format("start to get rsm self ped process, user tel is %s", currentUser.getTelephone()));
                processData = homeService.getRSMSelfReportProcess(currentUserTel);
                
                List<ReportProcessData> dsmProcessDataList = new ArrayList<ReportProcessData>();
                logger.info(String.format("start to get dsm ped process of current rsm, user tel is %s", currentUser.getTelephone()));
                List<UserInfo> dsms = userService.getLowerUserOfCurrentUser(currentUser, null);
                for( UserInfo user : dsms ){
                    ReportProcessData dsmProcessData = homeService.getDSMSelfReportProcess(user.getTelephone());
                    dsmProcessData.setName(user.getName());
                    dsmProcessDataList.add(dsmProcessData);
                }
                logger.info(String.format("end to get dsm home process of current rsm, user tel is %s", currentUser.getTelephone()));
                view.addObject(LsAttributes.DATAQUERY_PROCESS_CHILD_DATA, dsmProcessDataList);
            }
            view.addObject(LsAttributes.DATAQUERY_PROCESS_DATA, processData);
            view.addObject("currentUser", currentUser);
        }catch(Exception e){
            logger.error("fail to get the home process data,",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        
        view.setViewName("reportProcessHome");
        return view;
    }
    
    @RequestMapping("/monthlyQuery")
    public ModelAndView monthlyQuery(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        try{
            List<MonthlyRatioData> monthlyRatioList = hospitalService.getMonthlyRatioData(currentUser);
            List<MonthlyRatioData> childMonthlyRatioList = hospitalService.getChildMonthlyRatioData(currentUser);
            MonthlyRatioData superiorMonthlyRatio = hospitalService.getUpperUserMonthlyRatioData(currentUser);
            
            List<Monthly12Data> monthly12Datas = hospitalService.get12MontlyDataOfUser(currentUser);
            logger.info(String.format("get the 12 monthly data during monthly query, current user is %s", currentUser.getTelephone()));
            
            List<UserInfo> lowerUsers = userService.getLowerUserOfCurrentUser4MonthData(currentUser);
            view.addObject(LsAttributes.CURRENT_USER_LOWER_USERS, lowerUsers);
            logger.info("get lower users of current user end...");
            
            List<UserInfo> rsmUserlist = new ArrayList<UserInfo>();
            List<UserInfo> rsdUserlist = new ArrayList<UserInfo>();
            try{
            	rsmUserlist = userService.getUserInfoByLevel(LsAttributes.USER_LEVEL_RSM);
            	rsdUserlist = userService.getUserInfoByLevel(LsAttributes.USER_LEVEL_RSD);
            }catch(Exception e){
            	logger.error("fail to get the rsm users,",e);
            }
            view.addObject("rsmUserlist", rsmUserlist);
            view.addObject("rsdUserlist", rsdUserlist);
            
            view.addObject("monthlyRatioList", monthlyRatioList);
            view.addObject("childMonthlyRatioList", childMonthlyRatioList);
            view.addObject("superiorMonthlyRatio", superiorMonthlyRatio);
            view.addObject("monthly12Datas", monthly12Datas);
            view.addObject("currentUser", currentUser);
            populateDailyReportTitle(currentUser, view, LsAttributes.MONTHLYREPORTTITLE);
            populateMonthlyDataTitle(currentUser, view, LsAttributes.MONTHLY12TITLE);
        }catch(Exception e){
            logger.error("fail to get the monthly data in query,",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        
        view.setViewName("monthlyQuery");
        return view;
    }
    
    @RequestMapping("/showLowerMonthlyData")
    public ModelAndView showLowerMonthlyData(HttpServletRequest request){
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	logger.info(String.format("showLower12MonthlyData, current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
    	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
    		view.setViewName("index");
    		return view;
    	}
    	
    	try{
    		String lowerUserCode = request.getParameter("lowUser");
    		String regionCenter = request.getParameter("rsdSelect");
    		String region = request.getParameter("rsmSelect");
    		logger.info(String.format("get the lower monthly data which code is %s, regionCenter is %s, region is %s", lowerUserCode,regionCenter,region));
    		
    		UserInfo lowerUser = null;
    		if( null != lowerUserCode && !"".equalsIgnoreCase(lowerUserCode) ){
    			lowerUser = userService.getUserInfoByUserCode(lowerUserCode);
    		}
    		
    		if( null != regionCenter && !"".equalsIgnoreCase(regionCenter) ){
    			if( null != region && !"".equalsIgnoreCase(region) ){
    				lowerUser = userService.getUserInfoByRegion(region);
    			}else if( !"0".equalsIgnoreCase(regionCenter) ){
    				lowerUser = userService.getUserInfoByRegionCenter(regionCenter);
    			}
    		}
    		
    		List<Monthly12Data> monthly12Datas = new ArrayList<Monthly12Data>();
    		if( null == lowerUser ){
    			monthly12Datas = hospitalService.get12MontlyDataByCountory();
    		}else{
    			monthly12Datas = hospitalService.get12MontlyDataOfUser(lowerUser);
    		}
    		if( null == lowerUser ){
    			logger.info("get the whole country 12 monthly data");
    			StringBuffer monthlyDataTitle = new StringBuffer();
    			monthlyDataTitle.append(LsAttributes.DAILYREPORTTITLE_1)
        		.append("全国")
        		.append(LsAttributes.DAILYREPORTTITLE_2)
        		.append(" ")
        		.append(LsAttributes.MONTHLY12TITLE);
    			view.addObject("monthlyDataTitle", monthlyDataTitle.toString());
    		}else{
    			logger.info(String.format("get the lower 12 monthly data during monthly query, lower user is %s", lowerUser.getTelephone()));
    			populateMonthlyDataTitle(lowerUser, view, LsAttributes.MONTHLY12TITLE);
    		}
            view.addObject("monthly12Datas", monthly12Datas);
            logger.info("get lower 12 monthly data for mobile end...");
            
        }catch(Exception e){
            logger.error("fail to get the lower weekly ped ratio report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
    	view.setViewName("monthlyQuery_lower_12month");
    	return view;
    }
    
    @RequestMapping("/ratioLastweek")
    public ModelAndView ratioLastweek(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        view.setViewName("ratioLastweekDepartment");
        return view;
    }

    @RequestMapping("/ratioLastweekPed")
    public ModelAndView ratioLastweekPed(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        
        if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        try{
            List<WeeklyRatioData> mobilePEDWeeklyRatioData = pediatricsService.getWeeklyPEDData4Mobile(currentUserTel);
            view.addObject(LsAttributes.WEEKLY_RATIO_DATA, mobilePEDWeeklyRatioData);
            logger.info("get weekly ratio ped data for mobile end...");
            
            List<UserInfo> lowerUsers = userService.getLowerUserOfCurrentUser(currentUser,LsAttributes.DEPARTMENT_PED);
            view.addObject(LsAttributes.CURRENT_USER_LOWER_USERS, lowerUsers);
            logger.info("get lower users of current user end...");
            
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            	WeeklyRatioData countryRatioData = pediatricsService.getWeeklyPEDCountoryData4Mobile();
            	view.addObject("countryRatioData", countryRatioData);
            }
            
            populateDailyReportTitle(currentUser, view, LsAttributes.DAILYREPORTTITLE_3_2);
        }catch(Exception e){
            logger.error("fail to get the weekly ped ratio report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        view.setViewName("ratioLastweekPED");
        return view;
    }
    
    @RequestMapping("/ratioLastweekRes")
    public ModelAndView ratioLastweekRes(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        
        if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        
        try{
            List<WeeklyRatioData> mobileRESWeeklyRatioData = respirologyService.getWeeklyRESData4Mobile(currentUserTel);
            view.addObject(LsAttributes.WEEKLY_RATIO_DATA, mobileRESWeeklyRatioData);
            logger.info("get weekly ratio res data for mobile end...");
            
            List<UserInfo> lowerUsers = userService.getLowerUserOfCurrentUser(currentUser,LsAttributes.DEPARTMENT_RES);
            view.addObject(LsAttributes.CURRENT_USER_LOWER_USERS, lowerUsers);
            logger.info("get lower users of current user end...");
            
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            	WeeklyRatioData countryRatioData = respirologyService.getWeeklyRESCountoryData4Mobile();
            	view.addObject("countryRatioData", countryRatioData);
            }
            
            populateDailyReportTitle(currentUser, view, LsAttributes.DAILYREPORTTITLE_3_2);
        }catch(Exception e){
            logger.error("fail to get the weekly res ratio report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        view.setViewName("ratioLastweekRES");
        return view;
    }
    
    @RequestMapping("/ratioLastweekChe")
    public ModelAndView ratioLastweekChe(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        
        if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        
        try{
            List<WeeklyRatioData> mobileRESWeeklyRatioData = chestSurgeryService.getWeeklyData4Mobile(currentUser);
            view.addObject(LsAttributes.WEEKLY_RATIO_DATA, mobileRESWeeklyRatioData);
            logger.info("get weekly ratio chest surgery data for mobile end...");
            
            List<UserInfo> lowerUsers = userService.getLowerUserOfCurrentUser(currentUser,LsAttributes.DEPARTMENT_RES);
            view.addObject(LsAttributes.CURRENT_USER_LOWER_USERS, lowerUsers);
            logger.info("get lower users of current user end...");
            
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
                WeeklyRatioData countryRatioData = chestSurgeryService.getWeeklyCountoryData4Mobile();
                view.addObject("countryRatioData", countryRatioData);
            }
            
            populateDailyReportTitle(currentUser, view, LsAttributes.DAILYREPORTTITLE_3_2);
        }catch(Exception e){
            logger.error("fail to get the weekly chest surgery ratio report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        view.setViewName("ratioLastweekCHE");
        return view;
    }
    
    @RequestMapping("/showLowerPEDReport")
    public ModelAndView showLowerPEDReport(HttpServletRequest request){
    	logger.info("show lower PED report");
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	
    	logger.info(String.format("lower PED report: current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
    	
    	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser 
    			|| LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel())){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
    		view.setViewName("index");
    		return view;
    	}
    	
    	try{
    		String lowerUserCode = request.getParameter("lowUser");
    		logger.info(String.format("get the lower ped report which code is %s", lowerUserCode));
            WeeklyRatioData lowerPEDRatio = pediatricsService.getLowerWeeklyPEDData4Mobile(currentUser, lowerUserCode);
            view.addObject(LsAttributes.WEEKLY_RATIO_DATA, lowerPEDRatio);
            logger.info("get lower weekly ratio ped data for mobile end...");
            
            String lowerUserLevel = "";
            if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
                lowerUserLevel = "REP";
            }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
                lowerUserLevel = "DSM";
            }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
            		|| LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())){
                lowerUserLevel = "RSM";
            }
            
            String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
            String localPath = request.getRealPath("/");
            
            StringBuffer localpedReportFile = new StringBuffer(localPath);
            StringBuffer remotepedReportFile = new StringBuffer(basePath);
            
            String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"lowerWeeklyReport");
            
            remotepedReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("lowerWeeklyPEDReport-")
            .append(lowerUserLevel)
            .append("-")
            .append(lowerUserCode)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localpedReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
                .append("lowerWeeklyPEDReport-")
                .append(lowerUserLevel)
                .append("-")
                .append(lowerUserCode)
                .append("-")
                .append(DateUtils.getDirectoryNameOfLastDuration())
                .append(".html");
            File reportfile = new File(localpedReportFile.toString());
            if( reportfile.exists() ){
                view.addObject("pedReportFile", remotepedReportFile.toString());
            }else{
                view.addObject("pedReportFile", basePath+"jsp/weeklyReport_404.html");
            }
            
            populateDailyReportTitle(currentUser, view, LsAttributes.DAILYREPORTTITLE_3_2);
        }catch(Exception e){
            logger.error("fail to get the lower weekly ped ratio report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
    	
    	view.setViewName("lowerPEDReport");
    	return view;
    }
    
    @RequestMapping("/showLowerRESReport")
    public ModelAndView showLowerRESReport(HttpServletRequest request){
    	logger.info("show lower RES report");
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	
    	logger.info(String.format("lower RES report: current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
    	
    	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser 
    			|| LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel())){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
    		view.setViewName("index");
    		return view;
    	}
    	
    	try{
    		String lowerUserCode = request.getParameter("lowUser");
    		logger.info(String.format("get the lower res report which code is %s", lowerUserCode));
            WeeklyRatioData lowerRESRatio = respirologyService.getLowerWeeklyRESData4Mobile(currentUser, lowerUserCode);
            view.addObject(LsAttributes.WEEKLY_RATIO_DATA, lowerRESRatio);
            logger.info("get lower weekly ratio res data for mobile end...");
            
            String lowerUserLevel = "";
            if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
                lowerUserLevel = "REP";
            }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
                lowerUserLevel = "DSM";
            }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
            		|| LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())){
                lowerUserLevel = "RSM";
            }
            
            String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
            String localPath = request.getRealPath("/");
            StringBuffer localResReportFile = new StringBuffer(localPath);
            StringBuffer remoteResReportFile = new StringBuffer(basePath);
            
            String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"lowerWeeklyReport");
            
            remoteResReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("lowerWeeklyRESReport-")
            .append(lowerUserLevel)
            .append("-")
            .append(lowerUserCode)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localResReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
                .append("lowerWeeklyRESReport-")
                .append(lowerUserLevel)
                .append("-")
                .append(lowerUserCode)
                .append("-")
                .append(DateUtils.getDirectoryNameOfLastDuration())
                .append(".html");
            
            File reportfile = new File(localResReportFile.toString());
            if( reportfile.exists() ){
                view.addObject("resReportFile", remoteResReportFile.toString());
            }else{
                view.addObject("resReportFile", basePath+"jsp/weeklyReport_404.html");
            }
            
            populateDailyReportTitle(currentUser, view, LsAttributes.DAILYREPORTTITLE_3_2);
        }catch(Exception e){
            logger.error("fail to get the lower weekly res ratio report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
    	
    	view.setViewName("lowerRESReport");
    	return view;
    }
    
    @RequestMapping("/showLowerCHEReport")
    public ModelAndView showLowerCHEReport(HttpServletRequest request){
        logger.info("show lower CHE report");
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        
        logger.info(String.format("lower CHE report: current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser 
                || LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel())){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        
        try{
            String lowerUserCode = request.getParameter("lowUser");
            logger.info(String.format("get the lower chest surgery report which user code is %s", lowerUserCode));
            WeeklyRatioData lowerRatio = chestSurgeryService.getLowerWeeklyData4Mobile(currentUser, lowerUserCode);
            view.addObject(LsAttributes.WEEKLY_RATIO_DATA, lowerRatio);
            logger.info("get lower weekly ratio chest surgery data for mobile end...");
            
            String lowerUserLevel = "";
            if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) ){
                lowerUserLevel = "REP";
            }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel()) ){
                lowerUserLevel = "DSM";
            }else if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel()) 
                    || LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())){
                lowerUserLevel = "RSM";
            }
            
            String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
            String localPath = request.getRealPath("/");
            StringBuffer localReportFile = new StringBuffer(localPath);
            StringBuffer remoteReportFile = new StringBuffer(basePath);
            
            String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"lowerWeeklyReport");
            
            remoteReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("lowerWeeklyCHEReport-")
            .append(lowerUserLevel)
            .append("-")
            .append(lowerUserCode)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("lowerWeeklyCHEReport-")
            .append(lowerUserLevel)
            .append("-")
            .append(lowerUserCode)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            File reportfile = new File(localReportFile.toString());
            if( reportfile.exists() ){
                view.addObject("cheReportFile", remoteReportFile.toString());
            }else{
                view.addObject("cheReportFile", basePath+"jsp/weeklyReport_404.html");
            }
            
            populateDailyReportTitle(currentUser, view, LsAttributes.DAILYREPORTTITLE_3_2);
        }catch(Exception e){
            logger.error("fail to get the lower weekly chest surgery ratio report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        
        view.setViewName("lowerCHEReport");
        return view;
    }
    
    @RequestMapping("/hospitalQuery")
    public ModelAndView hospitalQuery(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        view.setViewName("hospitalQuery");
        return view;
    }
    
    @RequestMapping("/doHospitalSearch")
    public ModelAndView doHospitalSearch(HttpServletRequest request){
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
    	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
    		view.setViewName("index");
    		return view;
    	}
    	try{
    	    String hospitalKeyword = request.getParameter("hospitalKeyword");
    		logger.info(String.format("the keyword of the hospital is %s", hospitalKeyword));
    		view.addObject(LsAttributes.HOSPITAL_SEARCH_KEYWORD, hospitalKeyword);
    		
    		List<Hospital> hospitals = hospitalService.getHospitalsByKeyword(hospitalKeyword);
    		view.addObject(LsAttributes.HOSPITAL_SEARCH_RESULT, hospitals);
    	}catch(Exception e){
    		logger.error("fail to search the hospital,",e);
    	}
    	
    	view.setViewName("hospitalQuery");
    	return view;
    }
    
    @RequestMapping("/hospitalSalesQuery")
    public ModelAndView hospitalSalesQuery(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        view.setViewName("hospitalSalesQuery");
        return view;
    }
    
    @RequestMapping("/doHospitalSalesQuery")
    public ModelAndView doHospitalSalesQuery(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        
        String orderParam = request.getParameter("orderParam");
        String order = request.getParameter("order");
        String orderType = request.getParameter("orderType");
        String department = request.getParameter("department");
        
        HospitalSalesQueryParam queryParam = new HospitalSalesQueryParam();
        queryParam.setOrderParam(orderParam);
        queryParam.setOrder(order);
        queryParam.setOrderType(orderType);
        queryParam.setDepartment(department);
        List<HospitalSalesQueryObj> hosSalesData = new ArrayList<HospitalSalesQueryObj>();
        try{
            hosSalesData = hospitalService.getHospitalSalesList(queryParam);
        }catch(Exception e){
            logger.error("fail to get the hos sales data,",e);
        }
        
        view.addObject("orderParam", orderParam);
        view.addObject("order", order);
        view.addObject("orderType", orderType);
        view.addObject("department", department);
        view.addObject("hosSalesData", hosSalesData);
        
        view.setViewName("hospitalSalesQuery");
        return view;
    }
    
    @RequestMapping("/downloadHosSalesData")
    public ModelAndView downloadHosSalesData(HttpServletRequest request) throws IOException{
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        
        String orderParam = request.getParameter("orderParam");
        String order = request.getParameter("order");
        String orderType = request.getParameter("orderType");
        String department = request.getParameter("department");
        
        HospitalSalesQueryParam queryParam = new HospitalSalesQueryParam();
        queryParam.setOrderParam(orderParam);
        queryParam.setOrder(order);
        queryParam.setOrderType(orderType);
        queryParam.setDepartment(department);
        List<HospitalSalesQueryObj> hosSalesData = new ArrayList<HospitalSalesQueryObj>();
        try{
            hosSalesData = hospitalService.getHospitalSalesList(queryParam);
        }catch(Exception e){
            logger.error("fail to get the hos sales data,",e);
        }
        
        FileOutputStream fOut = null;
        String fileName = null;
        
        File pedDir = new File(request.getRealPath("/") + "hosSalesData/");
        if( !pedDir.exists() ){
            pedDir.mkdir();
        }
        
        String fileSubName = "";
        if( null != department && "1".equalsIgnoreCase(department) ){
            fileSubName="医院呼吸科销售数据";
        }else{
            fileSubName="医院儿科销售数据";
        }
        
        fileName = "hosSalesData/" + fileSubName + "-" + System.currentTimeMillis() + ".xls";
        File tmpFile = new File(request.getRealPath("/") + fileName);

        try{
            if( !tmpFile.exists() ){
                tmpFile.createNewFile();
            }
            
            fOut = new FileOutputStream(tmpFile);
            
            HSSFWorkbook workbook = new HSSFWorkbook();
            workbook.createSheet(fileSubName);
            HSSFSheet sheet = workbook.getSheetAt(0);
            int currentRowNum = 0;
            
            HSSFCellStyle percentCellStyle = workbook.createCellStyle();
            percentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
            HSSFCellStyle numberCellStyle = workbook.createCellStyle();
            numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
            
            //build the header
            HSSFRow row = sheet.createRow(currentRowNum++);
            row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("医院名称");
            row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("医院编号");
            row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("总人数");
            row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("雾化令舒人数");
            row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("雾化率");
            row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("平均剂量");
            row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("总人数增长率");
            row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("雾化令舒人数增长率");
            row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("雾化率增长率");
            row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue("平均剂量增长率");
            for( HospitalSalesQueryObj hso : hosSalesData ){
                row = sheet.createRow(currentRowNum++);
                row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue(hso.getHosName());
                row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue(hso.getHosCode());
                
                HSSFCell pnumCell = row.createCell(2, XSSFCell.CELL_TYPE_NUMERIC);
                pnumCell.setCellValue(hso.getPnum());
                pnumCell.setCellStyle(numberCellStyle);
                
                HSSFCell lsnumCell = row.createCell(3, XSSFCell.CELL_TYPE_NUMERIC);
                lsnumCell.setCellValue(hso.getLsnum());
                lsnumCell.setCellStyle(numberCellStyle);
                
                
                HSSFCell whRateCell = row.createCell(4, XSSFCell.CELL_TYPE_NUMERIC);
                whRateCell.setCellValue(hso.getWhRate());
                whRateCell.setCellStyle(percentCellStyle);
                
                java.text.DecimalFormat d_f = new java.text.DecimalFormat("#.00");
                String ave_Dose = d_f.format(hso.getAveDose());
                
                row.createCell(5, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(Double.parseDouble(ave_Dose));
                
                HSSFCell pnumRatioCell = row.createCell(6, XSSFCell.CELL_TYPE_NUMERIC);
                pnumRatioCell.setCellValue(hso.getPnumRatio());
                pnumRatioCell.setCellStyle(percentCellStyle);
                
                HSSFCell lsnumRatioCell = row.createCell(7, XSSFCell.CELL_TYPE_NUMERIC);
                lsnumRatioCell.setCellValue(hso.getLsnumRatio());
                lsnumRatioCell.setCellStyle(percentCellStyle);
                
                HSSFCell whRateRatioCell = row.createCell(8, XSSFCell.CELL_TYPE_NUMERIC);
                whRateRatioCell.setCellValue(hso.getWhRateRatio());
                whRateRatioCell.setCellStyle(percentCellStyle);
                
                HSSFCell aveDoseRatioCell = row.createCell(9, XSSFCell.CELL_TYPE_NUMERIC);
                aveDoseRatioCell.setCellValue(hso.getAveDoseRatio());
                aveDoseRatioCell.setCellStyle(percentCellStyle);
            }
            workbook.write(fOut);
        }catch(Exception e){
            logger.error("fail to generate the file,",e);
        }finally{
            if( fOut != null ){
                fOut.close();
            }
        }
        
        view.addObject("orderParam", orderParam);
        view.addObject("order", order);
        view.addObject("orderType", orderType);
        view.addObject("department", department);
        view.addObject("hosSalesData", hosSalesData);
        view.addObject("hosSalesDataFile", fileName);
        
        view.setViewName("hospitalSalesQuery");
        return view;
    }
    
    @RequestMapping("/hospitalRatio")
    public ModelAndView hospitalRatio(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND);
            view.setViewName("index");
            return view;
        }
        try{
            String hospitalCode = request.getParameter("hospitalCode");
            logger.info(String.format("the code of the hospital is %s", hospitalCode));
            String hospitalKeyword = request.getParameter("hospitalKeyword");
            logger.info(String.format("the keyword of the hospital is %s", hospitalKeyword));
            
            WeeklyRatioData pedWeeklyRatioData = pediatricsService.getHospitalWeeklyPEDData4Mobile(hospitalCode);
            WeeklyRatioData resWeeklyRatioData = respirologyService.getHospitalWeeklyRESData4Mobile(hospitalCode);
            WeeklyRatioData cheWeeklyRatioData = chestSurgeryService.getHospitalWeeklyData4Mobile(hospitalCode);
            
            view.addObject("pedRatioData", pedWeeklyRatioData);
            view.addObject("resRatioData", resWeeklyRatioData);
            view.addObject("cheRatioData", cheWeeklyRatioData);
            view.addObject("hospitalKeyword", hospitalKeyword);
            view.addObject("selectedHospitalName", hospitalService.getHospitalByCode(hospitalCode).getName());
            
            String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
            String localPath = request.getRealPath("/");
            StringBuffer localResReportFile = new StringBuffer(localPath);
            StringBuffer remoteResReportFile = new StringBuffer(basePath);
            
            String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"hospitalHTMLReport");
            
            remoteResReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("hospitalReport-")
            .append(hospitalCode)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localResReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
                .append("hospitalReport-")
                .append(hospitalCode)
                .append("-")
                .append(DateUtils.getDirectoryNameOfLastDuration())
                .append(".html");
            
            File reportfile = new File(localResReportFile.toString());
            if( reportfile.exists() ){
                view.addObject("hospitalReportFile", remoteResReportFile.toString());
            }else{
                view.addObject("hospitalReportFile", basePath+"jsp/weeklyHospitalReport_404.html");
            }
            
            String fromSalesQuery = request.getParameter("fromSalesQuery");
            view.addObject("fromSalesQuery", fromSalesQuery);
            if(null != fromSalesQuery && "Y".equalsIgnoreCase(fromSalesQuery)){
                String orderParam = request.getParameter("orderParam");
                String orderType = request.getParameter("orderType");
                String order = request.getParameter("order");
                String department = request.getParameter("department");
                
                view.addObject("orderParam", orderParam);
                view.addObject("orderType", orderType);
                view.addObject("order", order);
                view.addObject("department", department);
            }
            
        }catch(Exception e){
            logger.error("fail to get the hospital ratio,",e);
        }
        
        view.setViewName("hospitalRatioLastweek");
        return view;
    }
}
