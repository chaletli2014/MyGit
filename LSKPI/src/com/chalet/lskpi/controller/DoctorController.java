package com.chalet.lskpi.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.service.HomeService;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.LsKPIModelAndView;

@Controller
public class DoctorController extends BaseController{

	Logger logger = Logger.getLogger(DataQueryController.class);
	
    @Autowired
    @Qualifier("hospitalService")
    private HospitalService hospitalService;
    
    @Autowired
    @Qualifier("homeService")
    private HomeService homeService;
	
    @RequestMapping(value = "/checkIfDoctorExsits", method = RequestMethod.GET)  
    @ResponseBody
    public Map<String, Object> checkIfDoctorExsits(HttpServletRequest request){
    	String hospitalCode = request.getParameter("hospitalCode");
    	String doctorName = request.getParameter("doctorName");
    	logger.info(String.format("check if doctor exists - HospitalCode is %s, doctorName is %s" , hospitalCode,doctorName));
    	
    	int drNum = 0;
		try {
			drNum = hospitalService.getExistedDrNumByHospitalCode(hospitalCode, doctorName);
		} catch (Exception e) {
			logger.error("can not get existed doctor number by hospitalCode and doctorName,", e); 
		}
    	
        Map<String, Object> modelMap = new HashMap<String, Object>(1);  
        modelMap.put("count",drNum);
        return modelMap;
    }
    
    @RequestMapping("/doRelateDoctor")
    public String doRelateDoctor(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        logger.info(String.format("do relate the existing doctor to current user, current user's telephone is %s", currentUserTel));
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser || 
                ! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                        || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()))){
            return "redirect:index";
        }
        try{
        	String doctorName = request.getParameter("relatedDoctorName");
        	String hospitalCode = request.getParameter("relatedHospitalCode");
        	
        	Doctor dr = homeService.getDoctorByDoctorNameAndHospital(doctorName, hospitalCode);
        	
        	hospitalService.updateDoctorRelationship(dr.getId(), currentUser.getUserCode());
        	request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_13);
        }catch(Exception e){
            logger.error("fail to relate the doctor to current user,"+e.getMessage());
            request.getSession().setAttribute(LsAttributes.COLLECT_HOMEDATA_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
        }
        return "redirect:doctormaintenance";
    }
}
