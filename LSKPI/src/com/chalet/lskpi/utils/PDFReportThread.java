package com.chalet.lskpi.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.service.ChestSurgeryService;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.service.PediatricsService;
import com.chalet.lskpi.service.RespirologyService;
import com.chalet.lskpi.service.UserService;

public class PDFReportThread extends Thread {
    private String basePath = "";
    private String contextPath = "";
    private UserService userService;
    private PediatricsService pediatricsService;
    private RespirologyService respirologyService;
    private ChestSurgeryService chestSurgeryService;
    private HospitalService hospitalService;
    private boolean isRestart = false;
    private long taskTime = 0;
    
    private Logger logger = Logger.getLogger(PDFReportThread.class);
    
    
    public PDFReportThread(){
        
    }
    public PDFReportThread(String basePath, UserService userService
    		, PediatricsService pediatricsService
    		, RespirologyService respirologyService
    		, ChestSurgeryService chestSurgeryService
    		, HospitalService hospitalService
    		, String contextPath){
        this.basePath = basePath;
        this.userService = userService;
        this.pediatricsService = pediatricsService;
        this.respirologyService = respirologyService;
        this.chestSurgeryService = chestSurgeryService;
        this.hospitalService = hospitalService;
        this.contextPath = contextPath;
    }
    public void run() {  
        
        boolean emailIsSend = false;
        
        while (!this.isInterrupted()) {
            //check report time
            Date now = new Date();
            String reportGenerateDate = DateUtils.getDirectoryNameOfLastDuration();
            //daily report start
            try {
                //0-Sunday
                int dayInWeek = now.getDay();
                int hour = now.getHours();
                if( hour == Integer.parseInt(CustomizedProperty.getContextProperty("report_generate_time", "2"))
                        || isRestart ){
                    logger.info("console : now is " + hour + ", begin to generate PDF report");
                    
                    List<UserInfo> bmUserInfos = userService.getUserInfoByLevel("BM");
                    List<UserInfo> rsdUserInfos = userService.getUserInfoByLevel("RSD");
                    List<UserInfo> rsmUserInfos = userService.getUserInfoByLevel("RSM");
                    List<UserInfo> dsmUserInfos = userService.getUserInfoByLevel("DSM");
                    logger.info(String.format("dsm size is %s, rsm size is %s, rsd size is %s, bm size is %s",dsmUserInfos.size(),rsmUserInfos.size(),rsdUserInfos.size(),bmUserInfos.size()));
                    
                    checkAndCreateFileFolder(basePath + "weeklyReport/"+reportGenerateDate);
                    
                    List<UserInfo> reportUserInfos = new ArrayList<UserInfo>();
                    reportUserInfos.addAll(bmUserInfos);
                    reportUserInfos.addAll(rsdUserInfos);
                    reportUserInfos.addAll(rsmUserInfos);
                    reportUserInfos.addAll(dsmUserInfos);
                    
                    BirtReportUtils html = new BirtReportUtils();
                    
                    if( dayInWeek == Integer.parseInt(CustomizedProperty.getContextProperty("weekly_report_day", "1")) ){
                        logger.info("today is Thursday, generate the last week data first");
                        
                        if( !pediatricsService.hasLastWeeklyPEDData() ){
                            pediatricsService.generateWeeklyPEDDataOfHospital();
                        }else{
                            logger.info(" the data of PED in last week is already generated");
                        }
                        logger.info(" the data of PED in last week is populated");
                        
                        if( !respirologyService.hasLastWeeklyRESData() ){
                            respirologyService.generateWeeklyRESDataOfHospital();
                        }else{
                            logger.info(" the data of RES in last week is already generated");
                        }
                        logger.info(" the data of RES in last week is populated");
                        
                        if( !chestSurgeryService.hasLastWeeklyData() ){
                            chestSurgeryService.generateWeeklyDataOfHospital();
                        }else{
                            logger.info(" the data of chest surgery in last week is already generated");
                        }
                        logger.info(" the data of chest surgery in last week is populated");
                        
                        if( !hospitalService.hasLastWeeklyData() ){
                        	hospitalService.generateWeeklyDataOfHospital(DateUtils.getGenerateWeeklyReportDate());
                        }else{
                        	logger.info(" the data of hospital in last week is already generated");
                        }
                        logger.info(" the data of hospital in last week is populated");
                        
                        this.taskTime = System.currentTimeMillis();
                        Date refreshDate = DateUtils.getGenerateWeeklyReportDate();
                        String startDate = DateUtils.getTheBeginDateOfRefreshDate(refreshDate);
                        String endDate = DateUtils.getTheEndDateOfRefreshDate(refreshDate);
                        String lastRefreshThursday = DateUtils.getDirectoryNameOfCurrentDuration(refreshDate);
                        logger.info(String.format("start to refresh the pdf weekly report, lastThursday is %s, start date is %s, end date is %s", reportGenerateDate, startDate, endDate));
                        
                        boolean isFirstRefresh = true;
                        List<String> regionList = userService.getAllRegionName();
                        
                        html.startPlatform();
                        for( UserInfo user : reportUserInfos ){
                            String telephone = user.getTelephone();
                            if( telephone != null && !"#N/A".equalsIgnoreCase(telephone) ){
                                logger.info(String.format("the mobile is %s",telephone));
                                ReportUtils.createWeeklyPDFReport(html, user, telephone, startDate, endDate, basePath, contextPath, lastRefreshThursday, user.getEmail(),isFirstRefresh,true,regionList);
                                this.taskTime = System.currentTimeMillis();
                            }else{
                                logger.error(String.format("the telephone number for the user %s is not found", user.getName()));
                            }
                            isFirstRefresh = false;
                        }
                        html.stopPlatform();
                    }else{
                        logger.info(String.format("current day in week is %s, no need to generate the html weekly report", dayInWeek));
                    }
                    
                    this.taskTime = 0;
                    logger.info("end to generate the pdf weekly report");
                }
                
                //if current hour is not 8, then reset the flag to false
                if( hour != Integer.parseInt(CustomizedProperty.getContextProperty("email_send_time", "8")) && emailIsSend){
                    emailIsSend = false;
                }
                
                Thread.sleep(60000*30);
            } catch (Exception e) {  
                logger.error("fail to send the report,",e);
                this.interrupt();
            }  finally{
                isRestart = false;
            }
        }
    }
    
    private void checkAndCreateFileFolder(String filePath){
        File file = new File(filePath);
        if( !file.exists() ){
            logger.info("filePath " + filePath + " is not found, create it automaticlly");
            file.mkdirs();
        }
    }
    public boolean isRestart() {
        return isRestart;
    }
    public void setRestart(boolean isRestart) {
        this.isRestart = isRestart;
    }
    public long getTaskTime() {
        return taskTime;
    }
    public void setTaskTime(long taskTime) {
        this.taskTime = taskTime;
    }
}
