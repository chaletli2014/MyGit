package com.chalet.lskpi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.service.ChestSurgeryService;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.LsKPIModelAndView;
import com.chalet.lskpi.utils.StringUtils;

@Controller
public class ChestSurgeryController extends BaseController{

    @Autowired
    @Qualifier("hospitalService")
    private HospitalService hospitalService;
    
    @Autowired
    @Qualifier("chestSurgeryService")
    private ChestSurgeryService chestSurgeryService;

    @RequestMapping("/chestSurgery")
    public ModelAndView respirology(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String operator_telephone = verifyCurrentUser(request,view);
        
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( !super.isCurrentUserValid(currentUser, operator_telephone, view) ){
            return view;
        }
        
        try {
            List<Hospital> hospitals = new ArrayList<Hospital>();
            hospitals = hospitalService.getHospitalsByUserTel(operator_telephone,LsAttributes.DEPARTMENT_CHE);
            view.addObject("hospitals", hospitals);
            
            String selectedHospital = request.getParameter("selectedHospital");
            ChestSurgeryData existedData = new ChestSurgeryData();
            if( (null != selectedHospital && !"".equalsIgnoreCase(selectedHospital)) ){
                logger.info("get the chest surgery data of the selected hospital - " + selectedHospital);
                existedData = chestSurgeryService.getChestSurgeryDataByHospital(selectedHospital);
                if( existedData != null ){
                    logger.info(String.format("init chest surgery existedData is %s", existedData.getHospitalName()));
                }
                view.addObject("selectedHospital", selectedHospital);
            }
            view.addObject("existedData", existedData);
            
            String message = "";
            if( null != request.getSession().getAttribute(LsAttributes.COLLECT_CHESTSURGERY_MESSAGE) ){
                message = (String)request.getSession().getAttribute(LsAttributes.COLLECT_CHESTSURGERY_MESSAGE);
            }
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, message);
            request.getSession().removeAttribute(LsAttributes.COLLECT_CHESTSURGERY_MESSAGE);
            view.setViewName("chestSurgeryform");
        } catch (Exception e) {
            logger.error("fail to init the chest surgery page,",e);
        }
        
        return view;
    }
    
    @RequestMapping("/collectChestSurgery")
    public String collectChestSurgery(HttpServletRequest request){
        String operator_telephone = (String)request.getSession(true).getAttribute(LsAttributes.CURRENT_OPERATOR);
        logger.info("collectChestSurgery, user from session is " + operator_telephone);
        try{
            UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
            if( null == operator_telephone || "".equalsIgnoreCase(operator_telephone) || null == currentUser || 
                    ! ( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) 
                            || LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(currentUser.getLevel()))){
                return "redirect:index";
            }
            
            String dataId = request.getParameter("dataId");
            String hospitalCode = request.getParameter("hospital");
            
            if( null == hospitalCode || "".equalsIgnoreCase(hospitalCode) ){
                request.getSession().setAttribute(LsAttributes.COLLECT_CHESTSURGERY_MESSAGE, LsAttributes.RETURNED_MESSAGE_7);
                return "redirect:chestSurgery";
            }
            
            logger.info(String.format("doing the chest surgery data persistence, dataId is %s, hospital is %s", dataId,hospitalCode));
            
            ChestSurgeryData existedData = chestSurgeryService.getChestSurgeryDataByHospital(hospitalCode);
            if( null != existedData ){
                logger.info(String.format("check the chest surgery data again when user %s is collecting, the data id is %s", operator_telephone, existedData.getDataId()));
            }
            if( ( null == dataId || "".equalsIgnoreCase(dataId) ) 
                    && null != existedData){
                dataId = String.valueOf(existedData.getDataId());
                logger.info(String.format("the chest surgery data is found which id is %s", dataId));
            }
            
            //new data
            if( null == dataId || "".equalsIgnoreCase(dataId) ){
                ChestSurgeryData chestSurgeryData = new ChestSurgeryData();
                populateChestSurgeryData(request,chestSurgeryData);
                
                Hospital hospital = hospitalService.getHospitalByCode(hospitalCode);
                
                logger.info("insert the data of chest surgery");
                chestSurgeryService.insert(chestSurgeryData, currentUser, hospital);
            }else{
                //update the current data
                ChestSurgeryData dbChestSurgeryData = chestSurgeryService.getChestSurgeryDataById(Integer.parseInt(dataId));
                if( null == dbChestSurgeryData ){
                    request.getSession().setAttribute(LsAttributes.COLLECT_CHESTSURGERY_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
                }else{
                    populateChestSurgeryData(request,dbChestSurgeryData);
                    
                    logger.info("update the data of chest surgery");
                    chestSurgeryService.update(dbChestSurgeryData, currentUser);
                }
            }
            
            request.getSession().setAttribute(LsAttributes.COLLECT_CHESTSURGERY_MESSAGE, LsAttributes.RETURNED_MESSAGE_0);
        }catch(Exception e){
            logger.error("fail to collect chest surgery "+e.getMessage(),e);
            request.getSession().setAttribute(LsAttributes.COLLECT_CHESTSURGERY_MESSAGE, LsAttributes.RETURNED_MESSAGE_1);
        }
        
        return "redirect:chestSurgery";
    }
    
    private void populateChestSurgeryData(HttpServletRequest request,ChestSurgeryData chestSurgeryData){
        String hospitalCode = request.getParameter("hospital");
        //当日病房病人人数
        int pnum = StringUtils.getIntegerFromString(request.getParameter("pnum"));
        //当日病房内高危因素病人人数
        int risknum = StringUtils.getIntegerFromString(request.getParameter("risknum"));
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
        
        chestSurgeryData.setHospitalCode(hospitalCode);
        chestSurgeryData.setPnum(pnum);
        chestSurgeryData.setRisknum(risknum);
        chestSurgeryData.setWhnum(whnum);
        chestSurgeryData.setLsnum(lsnum);
        chestSurgeryData.setOqd(oqd);
        chestSurgeryData.setTqd(tqd);
        chestSurgeryData.setOtid(otid);
        chestSurgeryData.setTbid(tbid);
        chestSurgeryData.setTtid(ttid);
        chestSurgeryData.setThbid(thbid);
        chestSurgeryData.setFbid(fbid);
    }
}
