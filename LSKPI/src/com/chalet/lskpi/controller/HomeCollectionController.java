package com.chalet.lskpi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.service.HomeService;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.service.UserService;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.LsKPIModelAndView;
import com.chalet.lskpi.utils.StringUtils;

@Controller
public class HomeCollectionController extends BaseController{

    @Autowired
    @Qualifier("hospitalService")
    private HospitalService hospitalService;
    
    @Autowired
    @Qualifier("homeService")
    private HomeService homeService;
    
    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @RequestMapping("/homecollection")
    public ModelAndView homecollection(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("collect home data, current user's telephone is %s", currentUserTel));
        if( !super.isCurrentUserValid(currentUser, currentUserTel, view) ){
            return view;
        }
        view.setViewName("homecollectionmenu");
        return view;
    }
    
    @RequestMapping("/doctormaintenance")
    public ModelAndView doctormaintenance(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            view.setViewName("index");
            return view;
        }
        
        try {
            List<Hospital> hospitals = new ArrayList<Hospital>();
            hospitals = hospitalService.getHospitalsOfHomeCollectionByUserTel(currentUserTel);
            view.addObject("hospitals", hospitals);
            
            String selectedHospitalCode = request.getParameter("selectedHospital");
            List<Doctor> existedDoctors = new ArrayList<Doctor>();
            if( (null != selectedHospitalCode && !"".equalsIgnoreCase(selectedHospitalCode)) ){
                logger.info("get the respirology data of the selected hospital - " + selectedHospitalCode);
                view.addObject("selectedHospitalCode", selectedHospitalCode);
            }
            
            existedDoctors = hospitalService.getDoctorsOfCurrentUser(currentUser);
            view.addObject("existedDoctors", existedDoctors);
            if( null != existedDoctors){
            	logger.info(String.format("existedDoctors size is %s", existedDoctors.size()));
            }else{
            	logger.info(String.format("there is no doctors found of user %s", currentUserTel));
            }
            
            //get the sales under current user.
            List<UserInfo> salesList = userService.getSalesOfCurrentUser(currentUser);
            view.addObject("salesList", salesList);
            
            String message = "";
            if( null != request.getSession().getAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE) ){
                message = (String)request.getSession().getAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE);
            }
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, message);
            view.addObject("currentUser", currentUser);
            request.getSession().removeAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE);
            view.setViewName("doctormaintenance");
        } catch (Exception e) {
            logger.error("fail to init the doctor maintenance page,",e);
        }
        
        return view;
    }
    
    @RequestMapping("/doAddDoctor")
    public String doAddDoctor(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("do add new doctor, current user's telephone is %s", currentUserTel));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser || 
                ! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                        || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()))){
            return "redirect:index";
        }
        
        String doctorname = request.getParameter("doctorname");
        String hospitalCode = request.getParameter("hospital");
        String salesCode = "";
        if( currentUser.getLevel().equalsIgnoreCase(LsAttributes.USER_LEVEL_REP) ){
            salesCode = currentUser.getUserCode();
        }
        logger.info(String.format("doing the doctor data persistence, doctorname is %s, hospitalCode is %s, salesCode is %s"
                , doctorname,hospitalCode,salesCode));
        
        if( null == doctorname || "".equalsIgnoreCase(doctorname) 
        		|| null == hospitalCode || "".equalsIgnoreCase(hospitalCode)){
        	request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_8);
        	return "redirect:doctormaintenance";
        }
        
        try{
            int drCount = hospitalService.getExistedDrNumByHospitalCode(hospitalCode, doctorname);
            if( drCount > 0 ){
                doctorname += "("+drCount+")";
            }
            int totalDrCount = hospitalService.getTotalDrNumOfHospital(hospitalCode);
            Doctor doctor = new Doctor();
            doctor.setName(doctorname);
            doctor.setHospitalCode(hospitalCode);
            doctor.setCode(""+(totalDrCount+1));
            doctor.setSalesCode(salesCode);
            
            hospitalService.insertDoctor(doctor);
            logger.info(String.format("user %s add new doctor successfully!", currentUserTel));
        }catch(Exception e){
            logger.error("fail to add doctor,"+e.getMessage());
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
        }
        return "redirect:doctormaintenance";
    }
    
    @RequestMapping("/doEditDoctor")
    public String doEditDoctor(HttpServletRequest request){
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	logger.info(String.format("do edit doctor, current user's telephone is %s", currentUserTel));
    	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            return "redirect:doctormaintenance";
        }
        
        if( !(LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel())) ){
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            return "redirect:doctormaintenance";
        }
    	
    	String dataId = request.getParameter("dataId");
    	if( null == dataId || "".equalsIgnoreCase(dataId) ){
    		request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_9);
        	return "redirect:doctormaintenance";
    	}
    	int doctorId = Integer.parseInt(dataId);
    	String doctorname = request.getParameter("doctorname");
    	String defaultDoctorName = request.getParameter("doctorname_h");
    	String hospitalcode = request.getParameter("hospitalcode");
    	
    	logger.info(String.format("edit the doctor data, dataId is %s, doctorname is %s, hospitalcode is %s"
    			, dataId,doctorname,hospitalcode));
    	
    	if( null == doctorname || "".equalsIgnoreCase(doctorname) ){
    		request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_8);
    		return "redirect:doctormaintenance";
    	}
    	
    	try{
    	    if( defaultDoctorName.equalsIgnoreCase(doctorname) ){
    	        logger.info(String.format("the doctor name %s is no changed, no need to update", defaultDoctorName));
    	    }else{
    	        int drCount = hospitalService.getExistedDrNumByHospitalCodeExcludeSelf(doctorId,hospitalcode, doctorname);
    	        if( drCount > 0 ){
    	            doctorname += "("+drCount+")";
    	        }
    	        Doctor doctor = new Doctor();
    	        doctor.setName(doctorname);
    	        doctor.setId(doctorId);
    	        
    	        hospitalService.updateDoctor(doctor);
    	        logger.info(String.format("user %s update doctor successfully!", currentUserTel));
    	    }
    	}catch(Exception e){
    		logger.error("fail to edit doctor,"+e.getMessage());
    		request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
    	}
    	return "redirect:doctormaintenance";
    }
    
    @RequestMapping("/doEditDoctorRelationship")
    public String doEditDoctorRelationship(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("do edit doctor, current user's telephone is %s", currentUserTel));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            return "redirect:doctormaintenance";
        }
        
        if( !(LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel())) ){
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            return "redirect:doctormaintenance";
        }
        
        String dataId = request.getParameter("dataId");
        if( null == dataId || "".equalsIgnoreCase(dataId) ){
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_9);
            return "redirect:doctormaintenance";
        }
        int doctorId = Integer.parseInt(dataId);
        String relatedSales = request.getParameter("relatedSales");
        String defaultRelatedSales = request.getParameter("edit_relatedSales");
        
        logger.info(String.format("edit the doctor data, dataId is %s, relatedSales is %s"
                , dataId,relatedSales));
        
//        if( null == relatedSales || "".equalsIgnoreCase(relatedSales) ){
//            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_11);
//            return "redirect:doctormaintenance";
//        }
        
        try{
            if( defaultRelatedSales.equalsIgnoreCase(relatedSales) ){
                logger.info(String.format("the sales %s is no changed, no need to update", defaultRelatedSales));
            }else{
                
                hospitalService.updateDoctorRelationship(doctorId, relatedSales);
                logger.info(String.format("user %s update doctor %s relationship successfully, the new salesCode is %s!", currentUserTel,doctorId,relatedSales));
            }
        }catch(Exception e){
            logger.error("fail to edit doctor relationship,",e);
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
        }
        return "redirect:doctormaintenance";
    }
    
    @RequestMapping("/doDeleteDoctor")
    public String doDeleteDoctor(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("do delete doctor, current user's telephone is %s", currentUserTel));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            return "redirect:doctormaintenance";
        }
        
        if( !(LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) 
                || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel())) ){
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            return "redirect:doctormaintenance";
        }
        
        String dataId = request.getParameter("dataId");
        if( null == dataId || "".equalsIgnoreCase(dataId) ){
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_9);
            return "redirect:doctormaintenance";
        }
        
        String reason = request.getParameter("reason");
        String reason_other = request.getParameter("reason_other");
        if( null != reason && "other".equalsIgnoreCase(reason) ){
            reason = reason_other;
        }
        
        if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()) 
                && (null == reason||"".equalsIgnoreCase(reason)) ){
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_DATAID, dataId);
            return "redirect:deletereason";
        }
        
        int doctorId = Integer.parseInt(dataId);
        
        logger.info(String.format("delete the doctor data, dataId is %s", dataId));
        try{
            Doctor doctor = new Doctor();
            doctor.setId(doctorId);
            doctor.setReason(reason);
            
            hospitalService.deleteDoctor(doctor);
            logger.info(String.format("user %s delete doctor successfully!", currentUserTel));
        }catch(Exception e){
            logger.error("fail to delete doctor,"+e.getMessage());
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
        }
        return "redirect:doctormaintenance";
    }
    
    @RequestMapping("/deletereason")
    public ModelAndView deletereason(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        logger.info(String.format("the reason for deleting doctor, current user's telephone is %s", currentUserTel));
        String dataId = "";
        if( null != request.getSession().getAttribute(LsAttributes.COLLECT_HOMEDATA_DATAID) ){
            dataId = (String)request.getSession().getAttribute(LsAttributes.COLLECT_HOMEDATA_DATAID);
        }
        view.addObject("dataId", dataId);
        view.setViewName("deletereason");
        return view;
    }
    
    @RequestMapping("/collecthomedata")
    public ModelAndView collecthomedata(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( !super.isCurrentUserValid(currentUser, currentUserTel, view) ){
            return view;
        }
        
        List<Hospital> hospitals = new ArrayList<Hospital>();
        List<Doctor> existedDoctors = new ArrayList<Doctor>();
        try{
            hospitals = hospitalService.getHospitalsOfHomeCollectionByUserTel(currentUserTel);
            view.addObject("hospitals", hospitals);
            
            existedDoctors = hospitalService.getDoctorsOfCurrentUser(currentUser);
            view.addObject("doctors", existedDoctors);
            
            String selectedDoctor = request.getParameter("selectedDoctor");
            HomeData existedData = new HomeData();
            if( (null != selectedDoctor && !"".equalsIgnoreCase(selectedDoctor)) ){
                logger.info("get the home data of the selected doctor - " + selectedDoctor);
                view.addObject("selectedDoctor", selectedDoctor);
                
                existedData = homeService.getHomeDataByDoctorId(selectedDoctor);
            }
            view.addObject("existedData", existedData);
            
            String message = "";
            if( null != request.getSession().getAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE) ){
                message = (String)request.getSession().getAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE);
            }
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, message);
            request.getSession().removeAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE);
            
        }catch(Exception e){
            logger.info("fail to init the home data,",e);
        }
        
        view.setViewName("homeform");
        return view;
    }
    

    @RequestMapping("/docollecthomedata")
    public String docollecthomedata(HttpServletRequest request){
        String operator_telephone = (String)request.getSession(true).getAttribute(LsAttributes.CURRENT_OPERATOR);
        logger.info("docollecthomedata, user = "+operator_telephone);
        try{
            UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
            if( null == operator_telephone || "".equalsIgnoreCase(operator_telephone) || null == currentUser || 
                    ! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                            || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()))){
                return "redirect:index";
            }
            
            String dataId = request.getParameter("dataId");
            String doctorId = request.getParameter("selectedDoctor");
            
            if( null == doctorId || "".equalsIgnoreCase(doctorId) ){
                request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_9);
                return "redirect:collecthomedata";
            }
            
            logger.info(String.format("doing the data persistence, dataId is %s, doctorId is %s", dataId,doctorId));
            
            HomeData existedData = homeService.getHomeDataByDoctorId(doctorId);
            if( null != existedData ){
                logger.info(String.format("check the home data again when user %s collecting, the data id is %s", operator_telephone, existedData.getId()));
            }
            if( ( null == dataId || "".equalsIgnoreCase(dataId) ) 
                    && null != existedData){
                dataId = String.valueOf(existedData.getId());
                logger.info(String.format("the home data is found which id is %s", dataId));
            }
            
            //new data
            if( null == dataId || "".equalsIgnoreCase(dataId) ){
                HomeData homeData = new HomeData();
                populateHomeData(request, homeData);
                
                logger.info("insert the data of home");
                homeService.insert(homeData,doctorId);
            }else{
                //update the current data
                HomeData dbHomeData = homeService.getHomeDataById(Integer.parseInt(dataId));
                if( null == dbHomeData ){
                    request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_10);
                }else{
                    populateHomeData(request,dbHomeData);
                    
                    logger.info("update the data of home");
                    homeService.update(dbHomeData);
                }
            }
            
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
        }catch(Exception e){
            logger.error("fail to collect pediatrics "+e.getMessage(),e);
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
        }
        
        return "redirect:collecthomedata";
    }
    
    private void populateHomeData(HttpServletRequest request, HomeData homeData){
      //卖/赠泵数量
        int salenum = StringUtils.getIntegerFromString(request.getParameter("salenum"));
        //哮喘*患者人次.
        int asthmanum = StringUtils.getIntegerFromString(request.getParameter("asthmanum"));
        //处方>=8天的哮喘持续期病人次
        int ltenum = StringUtils.getIntegerFromString(request.getParameter("ltenum"));
        //持续期病人中推荐使用令舒的人次
        int lsnum = StringUtils.getIntegerFromString(request.getParameter("lsnum"));
        //8<=DOT<15天，病人次.
        int efnum = StringUtils.getIntegerFromString(request.getParameter("efnum"));
        //15<=DOT<30天，病人次.
        int ftnum = StringUtils.getIntegerFromString(request.getParameter("ftnum"));
        //DOT>=30天,病人次.
        int lttnum = StringUtils.getIntegerFromString(request.getParameter("lttnum"));
        
        homeData.setSalenum(salenum);
        homeData.setAsthmanum(asthmanum);
        homeData.setLtenum(ltenum);
        homeData.setLsnum(lsnum);
        homeData.setEfnum(efnum);
        homeData.setFtnum(ftnum);
        homeData.setLttnum(lttnum);
    }

}
