package com.chalet.lskpi.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.service.PediatricsService;
import com.chalet.lskpi.service.RespirologyService;
import com.chalet.lskpi.service.UserService;

public class ReportThread extends Thread {
    private String basePath = "";
    private String contextPath = "";
    private UserService userService;
    private PediatricsService pediatricsService;
    private RespirologyService respirologyService;
    private HospitalService hospitalService;
    private boolean isRestart = false;
    private long taskTime = 0;
    
    private Logger logger = Logger.getLogger(ReportThread.class);
    
    
    public ReportThread(){
        
    }
    public ReportThread(String basePath, UserService userService, PediatricsService pediatricsService, RespirologyService respirologyService, HospitalService hospitalService, String contextPath){
        this.basePath = basePath;
        this.userService = userService;
        this.pediatricsService = pediatricsService;
        this.respirologyService = respirologyService;
        this.hospitalService = hospitalService;
        this.contextPath = contextPath;
    }
    public void run() {  
        
        boolean emailIsSend = false;
        
        while (!this.isInterrupted()) {
            //check report time
            Date now = new Date();
            String yesterday = DateUtils.getYesterDay();
            String lastThursday = DateUtils.getLastThursDay();
            String lastMonth = DateUtils.getLastMonth();
            //daily report start
            try {
                //0-Sunday
                int dayInWeek = now.getDay();
                int hour = now.getHours();
                int dayInMonth = now.getDate();
                logger.info("current hour is " + hour);
                if( hour == Integer.parseInt(CustomizedProperty.getContextProperty("report_generate_time", "2"))
                        || isRestart ){
                    logger.info("console : now is " + hour + ", begin to generate report");
                    
                    List<UserInfo> bmUserInfos = userService.getUserInfoByLevel("BM");
                    List<UserInfo> rsdUserInfos = userService.getUserInfoByLevel("RSD");
                    List<UserInfo> rsmUserInfos = userService.getUserInfoByLevel("RSM");
                    List<UserInfo> dsmUserInfos = userService.getUserInfoByLevel("DSM");
                    List<UserInfo> repUserInfos = userService.getUserInfoByLevel("REP");
                    logger.info(String.format("dsm size is %s, rsm size is %s, rsd size is %s, bm size is %s",dsmUserInfos.size(),rsmUserInfos.size(),rsdUserInfos.size(),bmUserInfos.size()));
                    
                    
                    checkAndCreateFileFolder(basePath + "pedDailyReport/"+yesterday);
                    checkAndCreateFileFolder(basePath + "resDailyReport/"+yesterday);
                    
                    //the below 4 report are for the export handler
                    checkAndCreateFileFolder(basePath + "pedAllRSMDailyReport/"+yesterday);
                    checkAndCreateFileFolder(basePath + "resAllRSMDailyReport/"+yesterday);
                    checkAndCreateFileFolder(basePath + "pedAllDSMDailyReport/"+yesterday);
                    checkAndCreateFileFolder(basePath + "resAllDSMDailyReport/"+yesterday);
                    //------
                    
                    checkAndCreateFileFolder(basePath + "weeklyReport/"+lastThursday);
                    
                    checkAndCreateFileFolder(basePath + "weeklyHTMLReport/"+lastThursday);
                    checkAndCreateFileFolder(basePath + "lowerWeeklyReport/"+lastThursday);
                    checkAndCreateFileFolder(basePath + "hospitalHTMLReport/"+lastThursday);
                    checkAndCreateFileFolder(basePath + "monthlyHTMLReport/"+lastMonth);
                    
                    checkAndCreateFileFolder(basePath + "weeklyHTMLReportForWeb/"+lastThursday);
                    checkAndCreateFileFolder(basePath + "lowerWeeklyReportForWeb/"+lastThursday);
                    checkAndCreateFileFolder(basePath + "hospitalHTMLReportForWeb/"+lastThursday);
                    checkAndCreateFileFolder(basePath + "monthlyHTMLReportForWeb/"+lastMonth);
                    
                    List<UserInfo> reportUserInfos = new ArrayList<UserInfo>();
                    reportUserInfos.addAll(bmUserInfos);
                    reportUserInfos.addAll(rsdUserInfos);
                    reportUserInfos.addAll(rsmUserInfos);
                    reportUserInfos.addAll(dsmUserInfos);
                    
                    List<UserInfo> lowerUserInfos4Report = new ArrayList<UserInfo>();
                    lowerUserInfos4Report.addAll(rsmUserInfos);
                    lowerUserInfos4Report.addAll(dsmUserInfos);
                    lowerUserInfos4Report.addAll(repUserInfos);
                    
                    BirtReportUtils html = new BirtReportUtils();
                    int email_send_flag = Integer.parseInt(CustomizedProperty.getContextProperty("email_send_flag", "0"));
                    
                    /**
                     * 
                    logger.info("begin to generate all RSM daily report");
                    html.startPlatform();
                    createAllRSMDailyReport(html,basePath, contextPath, email_send_flag, yesterday);
                    html.stopPlatform();
                    logger.info("end to generate all RSM daily report");
                    
                    logger.info("begin to generate all DSM daily report");
                    html.startPlatform();
                    createAllDSMDailyReport(html,basePath, contextPath, email_send_flag, yesterday);
                    html.stopPlatform();
                    logger.info("end to generate all DSM daily report");
                    
                     */
                    
                    if( dayInWeek == Integer.parseInt(CustomizedProperty.getContextProperty("weekly_report_day", "4")) ){
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
                        
                        
                        logger.info("start to generate the html weekly report");
                        this.taskTime = System.currentTimeMillis();
                        html.startHtmlPlatform();
                        for( UserInfo user : reportUserInfos ){
                            String telephone = user.getTelephone();
                            if( telephone != null && !"#N/A".equalsIgnoreCase(telephone) ){
                                logger.info(String.format("the mobile is %s",telephone));
                                createHTMLWeeklyReport(html, user.getLevel(), telephone, basePath, contextPath, lastThursday);
                                this.taskTime = System.currentTimeMillis();
                                
                                createHTMLWeeklyReportForWeb(html, user.getLevel(), telephone, basePath, contextPath, lastThursday);
                                this.taskTime = System.currentTimeMillis();
                            }else{
                                logger.error(String.format("the telephone number for the user %s is not found", user.getName()));
                            }
                        }
                        logger.info("end to generate the html weekly report");
                        
                        logger.info("start to generate the html weekly report of lower user");
                        for( UserInfo user : lowerUserInfos4Report ){
                            String userCode = user.getUserCode();
                            if( userCode != null && !"#N/A".equalsIgnoreCase(userCode) ){
                                logger.info(String.format("the code of the lower user is %s",userCode));
                                createHTMLWeeklyReportOfLowerUser(html, user.getLevel(), userCode, basePath, contextPath, lastThursday);
                                this.taskTime = System.currentTimeMillis();
                                
                                createHTMLWeeklyReportOfLowerUserForWeb(html, user.getLevel(), userCode, basePath, contextPath, lastThursday);
                                this.taskTime = System.currentTimeMillis();
                            }else{
                                logger.error(String.format("the userCode of the user %s is not found or the user is vacant", user.getName()));
                            }
                        }
                        logger.info("end to generate the html weekly report of lower user");
                        
                        logger.info("start to generate the html hospital report");
                        List<Hospital> hospitals = hospitalService.getAllHospitals();
                        if( null != hospitals ){
                            logger.info(String.format("hospital size is %s", hospitals.size()));
                        }
                        for( Hospital hospital : hospitals ){
                            createHTMLWeeklyReportOfHospital(html, hospital.getCode(), basePath, contextPath, lastThursday);
                            this.taskTime = System.currentTimeMillis();
                            
                            createHTMLWeeklyReportOfHospitalForWeb(html, hospital.getCode(), basePath, contextPath, lastThursday);
                            this.taskTime = System.currentTimeMillis();
                        }
                        html.stopPlatform();
                        
                        logger.info("start to generate the pdf weekly report");
                        Date refreshDate = DateUtils.getGenerateWeeklyReportDate();
                        String lastRefreshThursday = DateUtils.getThursDayOfParamDate(refreshDate);
                        String startDate = DateUtils.getTheBeginDateOfRefreshDate(refreshDate);
                        String endDate = DateUtils.getTheEndDateOfRefreshDate(refreshDate);
                        logger.info(String.format("start to refresh the pdf weekly report, lastThursday is %s, start date is %s, end date is %s", lastThursday, startDate, endDate));
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
                        logger.info("end to refresh the pdf weekly report");
//                        html.startPlatform();
//                        boolean tmpSender = false;
//                        for( UserInfo user : reportUserInfos ){
//                            String telephone = user.getTelephone();
//                            if( telephone != null && !"#N/A".equalsIgnoreCase(telephone) ){
//                                logger.info(String.format("the mobile is %s",telephone));
//                                createWeeklyPDFReport(html, user, telephone, basePath, contextPath, email_send_flag, lastThursday, user.getEmail());
//                                this.taskTime = System.currentTimeMillis();
//                            }else{
//                                logger.error(String.format("the telephone number for the user %s is not found", user.getName()));
//                            }
//                            
//                            //TODO temp sender
//                            if( ( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(user.getLevel())
//                                    || ( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(user.getLevel())
//                                            && !tmpSender)
//                                            )
//                                    && Integer.parseInt(CustomizedProperty.getContextProperty("weekly_email_send_flag_tmp", "0")) == 1 ){
//                                // the BM only need to send once since all the reports for BM is same.
//                                if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(user.getLevel()) ){
//                                    tmpSender = true;
//                                }
//                                sendWeeklyReport2User(CustomizedProperty.getContextProperty("lskpi_to", "0"), user, lastThursday, user.getTelephone());
//                            }
//                        }
//                        html.stopPlatform();
                        logger.info("end to generate the pdf weekly report");
                    }else{
                        logger.info(String.format("current day in week is %s, no need to generate the html weekly report", dayInWeek));
                    }
                    
                    if( dayInMonth == Integer.parseInt(CustomizedProperty.getContextProperty("monthly_report_day", "11"))){
                        logger.info("start to generate the html monthly report");
                        html.startHtmlPlatform();
                        for( UserInfo user : reportUserInfos ){
                            String telephone = user.getTelephone();
                            if( telephone != null && !"#N/A".equalsIgnoreCase(telephone) ){
                                logger.info(String.format("start to generate the monthly html report for mobile %s",telephone));
                                createHTMLMonthlyReport(html, user.getLevel(), telephone, basePath, contextPath, lastMonth);
                                this.taskTime = System.currentTimeMillis();
                                
                                createHTMLMonthlyReportForWeb(html, user.getLevel(), telephone, basePath, contextPath, lastMonth);
                                this.taskTime = System.currentTimeMillis();
                            }else{
                                logger.error(String.format("the telephone number for the user %s is not found", user.getName()));
                            }
                        }
                        html.stopPlatform();
                        logger.info("end to generate the html monthly report");
                    }else{
                        logger.info(String.format("current day in month is %s, no need to generate the html monthly report", dayInMonth));
                    }
                    
                    if( 1 == Integer.parseInt(CustomizedProperty.getContextProperty("generate_daily_report", "0")) ){
                        html.startPlatform();
                        for( UserInfo user : reportUserInfos ){
                            if( !LsAttributes.USER_LEVEL_BU_HEAD.equalsIgnoreCase(user.getRealLevel()) 
                                    && !LsAttributes.USER_LEVEL_MD.equalsIgnoreCase(user.getRealLevel()) 
                                    && !LsAttributes.USER_LEVEL_SARTON.equalsIgnoreCase(user.getRealLevel()) ){
                                String telephone = user.getTelephone();
                                if( telephone != null && !"#N/A".equalsIgnoreCase(telephone) ){
                                    logger.info(String.format("start to generate the daily report for mobile %s",telephone));
                                    createDailyReport(html, user, telephone, basePath, contextPath, email_send_flag, yesterday);
                                    this.taskTime = System.currentTimeMillis();
                                }else{
                                    logger.error(String.format("the telephone number for the user %s is not found", user.getName()));
                                }
                            }else{
                                logger.info(String.format("the level of the current user %s is %s,no need to generate the report", user.getTelephone(),user.getRealLevel()));
                            }
                        }
                        html.stopPlatform();
                    }else{
                        logger.info("the flag of generate_daily_report is 0, no need to generate the report");
                    }
                    this.taskTime = 0;
                    logger.info("Finished");
                }
                
                
                if( hour == Integer.parseInt(CustomizedProperty.getContextProperty("email_send_time", "8")) && !emailIsSend){
                    try{
                        List<UserInfo> rsdUserInfos = userService.getUserInfoByLevel("RSD");
                        List<UserInfo> rsmUserInfos = userService.getUserInfoByLevel("RSM");
                        List<UserInfo> dsmUserInfos = userService.getUserInfoByLevel("DSM");
                        List<UserInfo> bmUserInfos = userService.getUserInfoByLevel("BM");
                        logger.info(String.format("dsm size is %s, rsm size is %s, rsd size is %s, bm size is %s",dsmUserInfos.size(),rsmUserInfos.size(),rsdUserInfos.size(),bmUserInfos.size()));
                        
                        List<UserInfo> emailUserInfos = new ArrayList<UserInfo>();
                        emailUserInfos.addAll(rsdUserInfos);
                        emailUserInfos.addAll(rsmUserInfos);
                        emailUserInfos.addAll(dsmUserInfos);
                        emailUserInfos.addAll(bmUserInfos);
                        
                        int email_send_flag = Integer.parseInt(CustomizedProperty.getContextProperty("email_send_flag", "0"));
                        
                        for( UserInfo user : emailUserInfos ){
                            try{
                                if( !LsAttributes.USER_LEVEL_BU_HEAD.equalsIgnoreCase(user.getRealLevel()) 
                                        && !LsAttributes.USER_LEVEL_MD.equalsIgnoreCase(user.getRealLevel()) 
                                        && !LsAttributes.USER_LEVEL_SARTON.equalsIgnoreCase(user.getRealLevel()) ){
                                    //TODO temp, need to remove
//                                  if( Integer.parseInt(CustomizedProperty.getContextProperty("email_send_flag_tmp", "0")) == 1 ){
//                                      if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(user.getLevel()) ){
//                                          logger.info(String.format("send the daily report of user %s to mac and gu", user.getTelephone()));
//                                          sendDailyReport2User(CustomizedProperty.getContextProperty("lskpi_to", "0"), user, yesterday, user.getTelephone());
//                                      }
//                                  }
//                                    if( 1 == email_send_flag ){
//                                        sendDailyReport2User(user.getEmail(), user, yesterday, user.getTelephone());
//                                        logger.info(String.format("the daily report email is sent to user %s, level is %s", user.getTelephone(),user.getRealLevel()));
//                                    }else{
//                                        logger.info(String.format("the email send flag is %s, no need to send the daily email",email_send_flag));
//                                    }
                                }else{
                                    logger.info(String.format("the level of the current user %s is %s,no need to send the report", user.getTelephone(),user.getRealLevel()));
                                }
                                if( 1 == email_send_flag ){
                                    List<String> allRegions = userService.getAllRegionName();
                                    sendWeeklyReport2User(user.getEmail(), user, lastThursday, user.getTelephone(),allRegions);
                                    logger.info(String.format("the weekly report email is sent to user %s, level is %s", user.getTelephone(),user.getRealLevel()));
                                }else{
                                    logger.info(String.format("the email send flag is %s, no need to send the weekly email",email_send_flag));
                                }
                            }catch(Exception e){
                                logger.error(String.format("fail to send the email to user %s", user.getTelephone()),e);
                            }
                        }
                    }catch(Exception e){
                        logger.error("fail to send the email,",e);
                    }
                    
                    emailIsSend = true;
                }else{
                    logger.info(String.format("current hour is %s, the email is already sent? %s", hour, emailIsSend));
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
    
    private void sendDailyReport2User( String email, UserInfo user, String yesterday, String telephone){
        String fileSubName = StringUtils.getFileSubName(user);
        String userLevel = user.getLevel();
        String dailyPEDReportName = basePath + "pedDailyReport/"+yesterday+"/儿科日报-"+fileSubName+"-"+yesterday+".xlsx";
        String dailyRESReportName = basePath + "resDailyReport/"+yesterday+"/呼吸科日报-"+fileSubName+"-"+yesterday+".xlsx";
        StringBuffer pedSubject = new StringBuffer(" - ").append(userLevel).append(" 儿科日报推送");
        StringBuffer resSubject = new StringBuffer(" - ").append(userLevel).append(" 呼吸科日报推送");
        try{
            EmailUtils.sendMessage(dailyPEDReportName,email,pedSubject.toString(),"");
            EmailUtils.sendMessage(dailyRESReportName,email,resSubject.toString(),"");
        }catch(Exception e){
            logger.error(String.format("fail to send the daily report email to user %s,email is %s,", telephone,email),e);
        }
    }
    
    private void sendWeeklyReport2User( String email, UserInfo user, String lastThursday, String telephone, List<String> allRegions){
        String userLevel = user.getLevel();
        String fileSubName = StringUtils.getFileSubName(user);
        
        String pedFileNamePre = basePath + "weeklyReport/"+lastThursday+"/儿科周报-"+fileSubName+"-"+lastThursday;
        String resFileNamePre = basePath + "weeklyReport/"+lastThursday+"/呼吸科周报-"+fileSubName+"-"+lastThursday;
        
        String weeklyPDFPEDReportFileName = pedFileNamePre+".pdf";
        String weeklyPDFRESReportFileName = resFileNamePre+".pdf";
        
        List<String> weeklyPDFPEDReportFileNameList = new ArrayList<String>();
        List<String> weeklyPDFRESReportFileNameList = new ArrayList<String>();
        for( String regionCenter : allRegions ){
            weeklyPDFPEDReportFileNameList.add(pedFileNamePre+"_"+regionCenter+".pdf");
        }
        for( String regionCenter : allRegions ){
            weeklyPDFRESReportFileNameList.add(resFileNamePre+"_"+regionCenter+".pdf");
        }
        
        StringBuffer weeklyPDFSubject = new StringBuffer(" - ").append(userLevel).append(" 周报推送");
        try{
            List<String> filePaths = new ArrayList<String>();
            filePaths.add(weeklyPDFPEDReportFileName);
            if(LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(user.getLevel())){
                filePaths.addAll(weeklyPDFPEDReportFileNameList);
            }
            
            filePaths.add(weeklyPDFRESReportFileName);
            if(LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(user.getLevel())){
                filePaths.addAll(weeklyPDFRESReportFileNameList);
            }
            EmailUtils.sendMessage(filePaths,email,weeklyPDFSubject.toString(),"");
        }catch(Exception e){
            logger.error(String.format("fail to send the weekly report email to user %s, email is %s ,", telephone,email),e);
        }
    }
    
    private void createDailyReport(BirtReportUtils html, UserInfo user,String telephone, String basePath, String contextPath, int email_send_flag, String yesterday) throws Exception{
        String fileSubName = StringUtils.getFileSubName(user);
        String dailyPEDReportName = basePath + "pedDailyReport/"+yesterday+"/儿科日报-"+fileSubName+"-"+yesterday+".xlsx";
        String dailyRESReportName = basePath + "resDailyReport/"+yesterday+"/呼吸科日报-"+fileSubName+"-"+yesterday+".xlsx";
        switch(user.getLevel()){
            case LsAttributes.USER_LEVEL_RSD:
              //RSD report -- start
                if( !new File(dailyPEDReportName).exists() ){
                    html.runReport( basePath + "reportDesigns/rsdPEDDaily.rptdesign",telephone,"","",dailyPEDReportName,"","","");
                    logger.info("the PED report to RSD is done.");
                }else{
                    logger.info("The ped report for rsd is already generated, no need to do again.");
                }
                
                if( !new File(dailyRESReportName).exists() ){
                    html.runReport( basePath + "reportDesigns/rsdRESDaily.rptdesign",telephone,"","",dailyRESReportName,"","","");
                    logger.info("the RES report to RSD is done.");
                }else{
                    logger.info("The res report for rsd is already generated, no need to do again.");
                }
                //RSD report -- end
                break;
            case LsAttributes.USER_LEVEL_RSM:
              //RSM report -- start
                if( !new File(dailyPEDReportName).exists() ){
                    html.runReport( basePath + "reportDesigns/rsmPEDDaily.rptdesign",telephone,"","",dailyPEDReportName,"","","");
                    logger.info("the PED report to RSM is done.");
                }else{
                    logger.info("The ped report for rsm is already generated, no need to do again.");
                }
                
                if( !new File(dailyRESReportName).exists() ){
                    html.runReport( basePath + "reportDesigns/rsmRESDaily.rptdesign",telephone,"","",dailyRESReportName,"","","");
                    logger.info("the RES report to RSM is done.");
                }else{
                    logger.info("The res report for rsm is already generated, no need to do again.");
                }
                //RSM report -- end
                break;
            case LsAttributes.USER_LEVEL_DSM:
              //DSM report -- start
                if( !new File(dailyPEDReportName).exists() ){
                    html.runReport( basePath + "reportDesigns/dsmPEDDaily.rptdesign",telephone,"","",dailyPEDReportName,"","","");
                    logger.info("tye PED report to DSM is done.");
                }else{
                    logger.info("The ped report for dsm is already generated, no need to do again.");
                }
                
                if( !new File(dailyRESReportName).exists() ){
                    html.runReport( basePath + "reportDesigns/dsmRESDaily.rptdesign",telephone,"","",dailyRESReportName,"","","");
                    logger.info("the RES report to DSM is done.");
                }else{
                    logger.info("The res report for dsm is already generated, no need to do again.");
                }
                //DSM report -- end
                break;
            case LsAttributes.USER_LEVEL_BM:
              //BU Head report -- start
                if( !new File(dailyPEDReportName).exists() ){
                    html.runReport( basePath + "reportDesigns/buHeadPEDDaily.rptdesign",telephone,"","",dailyPEDReportName,"","","");
                    logger.info("the PED report to Bu Head is done.");
                }else{
                    logger.info("The ped report for Bu Head is already generated, no need to do again.");
                }
                
                if( !new File(dailyRESReportName).exists() ){
                    html.runReport( basePath + "reportDesigns/buHeadRESDaily.rptdesign",telephone,"","",dailyRESReportName,"","","");
                    logger.info("the RES report to Bu Head is done.");
                }else{
                    logger.info("The res report for Bu Head is already generated, no need to do again.");
                }
                //BU Head report -- end
                break;
        }
      //Sales report -- start
//      if( !new File(pedReportSalesFullName).exists() ){
//          html.runReport( basePath + "reportDesigns/salesPEDDaily.rptdesign","13598013601",pedReportSalesFullName);
//          logger.info("PED to sales report done.");
//      }else{
//          logger.info("The ped report for sales is already generated, no need to do again.");
//      }
//      EmailUtils.sendMessage(pedReportSalesFullName,""," - Sales 儿科日报推送","");
//      logger.info("send mail to sales end...");
//      
//      if( !new File(resReportSalesFullName).exists() ){
//          html.runReport( basePath + "reportDesigns/salesRESDaily.rptdesign","13598013601",resReportSalesFullName);
//          logger.info("RES to Sales report done.");
//      }else{
//          logger.info("The res report for Sales is already generated, no need to do again.");
//      }
//      EmailUtils.sendMessage(resReportSalesFullName,""," - Sales 呼吸科日报推送","");
//      logger.info("send mail to DSM end...");
        //Sales report -- end
    }
    
    private void createAllRSMDailyReport(BirtReportUtils html, String basePath, String contextPath, int email_send_flag, String yesterday ) throws Exception{
        String dailyPEDReportName = basePath + "pedAllRSMDailyReport/"+yesterday+"/儿科日报-RSM-"+yesterday+".xlsx";
        String dailyRESReportName = basePath + "resAllRSMDailyReport/"+yesterday+"/呼吸科日报-RSM-"+yesterday+".xlsx";
        
        if( !new File(dailyPEDReportName).exists() ){
            html.runReport( basePath + "reportDesigns/exportRSMPEDDaily.rptdesign","","","",dailyPEDReportName,"","","");
            logger.info("the PED report of all RSM is done.");
        }else{
            logger.info("The ped report of all RSM is already generated, no need to do again.");
        }
        
        if( !new File(dailyRESReportName).exists() ){
            html.runReport( basePath + "reportDesigns/exportRSMRESDaily.rptdesign","","","",dailyRESReportName,"","","");
            logger.info("the RES report of all RSM is done.");
        }else{
            logger.info("The res report of all RSM is already generated, no need to do again.");
        }
    }
    
    private void createAllDSMDailyReport(BirtReportUtils html, String basePath, String contextPath, int email_send_flag, String yesterday ) throws Exception{
        String dailyPEDReportName = basePath + "pedAllDSMDailyReport/"+yesterday+"/儿科日报-DSM-"+yesterday+".xlsx";
        String dailyRESReportName = basePath + "resAllDSMDailyReport/"+yesterday+"/呼吸科日报-DSM-"+yesterday+".xlsx";
        
        if( !new File(dailyPEDReportName).exists() ){
            html.runReport( basePath + "reportDesigns/exportDSMPEDDaily.rptdesign","","","",dailyPEDReportName,"","","");
            logger.info("the PED report of all DSM is done.");
        }else{
            logger.info("The PED report of all DSM is already generated, no need to do again.");
        }
        
        if( !new File(dailyRESReportName).exists() ){
            html.runReport( basePath + "reportDesigns/exportDSMRESDaily.rptdesign","","","",dailyRESReportName,"","","");
            logger.info("the RES report of all DSM is done.");
        }else{
            logger.info("The RES report of all DSM is already generated, no need to do again.");
        }
    }
    
    private void createHTMLWeeklyReport(BirtReportUtils html, String userLevel,String telephone, String basePath, String contextPath, String lastThursday){
        String weeklyHtmlPEDReportFileName = basePath + "weeklyHTMLReport/"+lastThursday+"/weeklyPEDReport-"+userLevel+"-"+telephone+"-"+DateUtils.getLastThursDay()+".html";
        String weeklyHtmlRESReportFileName = basePath + "weeklyHTMLReport/"+lastThursday+"/weeklyRESReport-"+userLevel+"-"+telephone+"-"+DateUtils.getLastThursDay()+".html";
        
        switch(userLevel){
            case LsAttributes.USER_LEVEL_RSD:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForMobile.rptdesign",telephone,"","",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html PED report to RSD is done.");
                }else{
                    logger.info("The weekly html ped report for RSD is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForMobile.rptdesign",telephone,"","",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html RES report to RSD is done.");
                }else{
                    logger.info("The weekly html res report for RSD is already generated, no need to do again.");
                }
                break;
            
            case LsAttributes.USER_LEVEL_RSM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForMobileRSM.rptdesign",telephone,"","",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html PED report to RSM is done.");
                }else{
                    logger.info("The weekly html ped report for RSM is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForMobileRSM.rptdesign",telephone,"","",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html RES report to RSM is done.");
                }else{
                    logger.info("The weekly html res report for RSM is already generated, no need to do again.");
                }
                break;
                
            case LsAttributes.USER_LEVEL_DSM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForMobileDSM.rptdesign",telephone,"","",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html PED report to DSM is done.");
                }else{
                    logger.info("The weekly html ped report for DSM is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForMobileDSM.rptdesign",telephone,"","",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html RES report to DSM is done.");
                }else{
                    logger.info("The weekly html res report for DSM is already generated, no need to do again.");
                }
                break;
//            case LsAttributes.USER_LEVEL_REP:
//                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
//                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForMobileREP.rptdesign",telephone,weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
//                    logger.info("the weekly html PED report to REP is done.");
//                }else{
//                    logger.info("The weekly html ped report for REP is already generated, no need to do again.");
//                }
//                
//                if( !new File(weeklyHtmlRESReportFileName).exists() ){
//                    html.runReport( basePath + "reportDesigns/weeklyRESReportForMobileREP.rptdesign",telephone,weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
//                    logger.info("the weekly html RES report to REP is done.");
//                }else{
//                    logger.info("The weekly html res report for REP is already generated, no need to do again.");
//                }
//                break;
            case LsAttributes.USER_LEVEL_BM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForMobileBU.rptdesign",telephone,"","",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html PED report to BU Head is done.");
                }else{
                    logger.info("The weekly html ped report for BU Head is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForMobileBU.rptdesign",telephone,"","",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html RES report to BU Head is done.");
                }else{
                    logger.info("The weekly html res report for BU Head is already generated, no need to do again.");
                }
                break;
            default:
                logger.info(String.format("the level of the user is %s, no need to generate the report", userLevel));
                break;
        }
    }
    
    private void createHTMLWeeklyReportForWeb(BirtReportUtils html, String userLevel,String telephone, String basePath, String contextPath, String lastThursday){
        String weeklyHtmlPEDReportFileName = basePath + "weeklyHTMLReportForWeb/"+lastThursday+"/weeklyPEDReport-"+userLevel+"-"+telephone+"-"+DateUtils.getLastThursDay()+".html";
        String weeklyHtmlRESReportFileName = basePath + "weeklyHTMLReportForWeb/"+lastThursday+"/weeklyRESReport-"+userLevel+"-"+telephone+"-"+DateUtils.getLastThursDay()+".html";
        
        switch(userLevel){
            case LsAttributes.USER_LEVEL_RSD:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForWeb.rptdesign",telephone,"","",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html PED report to RSD is done.");
                }else{
                    logger.info("The web weekly html ped report for RSD is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForWeb.rptdesign",telephone,"","",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html RES report to RSD is done.");
                }else{
                    logger.info("The web weekly html res report for RSD is already generated, no need to do again.");
                }
                break;
                
            case LsAttributes.USER_LEVEL_RSM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForWebRSM.rptdesign",telephone,"","",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html PED report to RSM is done.");
                }else{
                    logger.info("The web weekly html ped report for RSM is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForWebRSM.rptdesign",telephone,"","",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html RES report to RSM is done.");
                }else{
                    logger.info("The web weekly html res report for RSM is already generated, no need to do again.");
                }
                break;
                
            case LsAttributes.USER_LEVEL_DSM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForWebDSM.rptdesign",telephone,"","",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html PED report to DSM is done.");
                }else{
                    logger.info("The web weekly html ped report for DSM is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForWebDSM.rptdesign",telephone,"","",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html RES report to DSM is done.");
                }else{
                    logger.info("The web weekly html res report for DSM is already generated, no need to do again.");
                }
                break;
//            case LsAttributes.USER_LEVEL_REP:
//                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
//                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForWebREP.rptdesign",telephone,weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
//                    logger.info("the weekly html PED report to REP is done.");
//                }else{
//                    logger.info("The weekly html ped report for REP is already generated, no need to do again.");
//                }
//                
//                if( !new File(weeklyHtmlRESReportFileName).exists() ){
//                    html.runReport( basePath + "reportDesigns/weeklyRESReportForWebREP.rptdesign",telephone,weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
//                    logger.info("the weekly html RES report to REP is done.");
//                }else{
//                    logger.info("The weekly html res report for REP is already generated, no need to do again.");
//                }
//                break;
            case LsAttributes.USER_LEVEL_BM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForWebBU.rptdesign",telephone,"","",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html PED report to BU Head is done.");
                }else{
                    logger.info("The web weekly html ped report for BU Head is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForWebBU.rptdesign",telephone,"","",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html RES report to BU Head is done.");
                }else{
                    logger.info("The web weekly html res report for BU Head is already generated, no need to do again.");
                }
                break;
            default:
                logger.info(String.format("the level of the user is %s, no need to generate the report", userLevel));
                break;
        }
    }
    
    private void createHTMLWeeklyReportOfLowerUser(BirtReportUtils html, String userLevel,String userCode, String basePath, String contextPath, String lastThursday){
        String weeklyHtmlPEDReportFileName = basePath + "lowerWeeklyReport/"+lastThursday+"/lowerWeeklyPEDReport-"+userLevel+"-"+userCode+"-"+DateUtils.getLastThursDay()+".html";
        String weeklyHtmlRESReportFileName = basePath + "lowerWeeklyReport/"+lastThursday+"/lowerWeeklyRESReport-"+userLevel+"-"+userCode+"-"+DateUtils.getLastThursDay()+".html";
        
        switch(userLevel){
            case LsAttributes.USER_LEVEL_RSM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForSingleRSM.rptdesign","",userCode,"",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the lower weekly html PED report to RSM is done.");
                }else{
                    logger.info("The lower weekly html ped report for RSM is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForSingleRSM.rptdesign","",userCode,"",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the lower weekly html RES report to RSM is done.");
                }else{
                    logger.info("The lower weekly html res report for RSM is already generated, no need to do again.");
                }
                break;
                
            case LsAttributes.USER_LEVEL_DSM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForSingleDSM.rptdesign","",userCode,"",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the lower weekly html PED report to DSM is done.");
                }else{
                    logger.info("The lower weekly html ped report for DSM is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForSingleDSM.rptdesign","",userCode,"",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the lower weekly html RES report to DSM is done.");
                }else{
                    logger.info("The lower weekly html res report for DSM is already generated, no need to do again.");
                }
                break;
            case LsAttributes.USER_LEVEL_REP:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForSingleREP.rptdesign","",userCode,"",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the lower weekly html PED report to REP is done.");
                }else{
                    logger.info("The lower weekly html ped report for REP is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForSingleREP.rptdesign","",userCode,"",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the lower weekly html RES report to REP is done.");
                }else{
                    logger.info("The lower weekly html res report for REP is already generated, no need to do again.");
                }
                break;
            default:
                logger.info(String.format("the level of the user is %s, no need to generate the report", userLevel));
                break;
                
        }
    }
    
    private void createHTMLWeeklyReportOfLowerUserForWeb(BirtReportUtils html, String userLevel,String userCode, String basePath, String contextPath, String lastThursday){
        String weeklyHtmlPEDReportFileName = basePath + "lowerWeeklyReportForWeb/"+lastThursday+"/lowerWeeklyPEDReport-"+userLevel+"-"+userCode+"-"+DateUtils.getLastThursDay()+".html";
        String weeklyHtmlRESReportFileName = basePath + "lowerWeeklyReportForWeb/"+lastThursday+"/lowerWeeklyRESReport-"+userLevel+"-"+userCode+"-"+DateUtils.getLastThursDay()+".html";
        
        switch(userLevel){
            case LsAttributes.USER_LEVEL_RSM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForWebSingleRSM.rptdesign","",userCode,"",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the Web lower weekly html PED report to RSM is done.");
                }else{
                    logger.info("The Web lower weekly html ped report for RSM is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForWebSingleRSM.rptdesign","",userCode,"",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the Web lower weekly html RES report to RSM is done.");
                }else{
                    logger.info("The Web lower weekly html res report for RSM is already generated, no need to do again.");
                }
                break;
                
            case LsAttributes.USER_LEVEL_DSM:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForWebSingleDSM.rptdesign","",userCode,"",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the Web lower weekly html PED report to DSM is done.");
                }else{
                    logger.info("The Web lower weekly html ped report for DSM is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForWebSingleDSM.rptdesign","",userCode,"",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the Web lower weekly html RES report to DSM is done.");
                }else{
                    logger.info("The Web lower weekly html res report for DSM is already generated, no need to do again.");
                }
                break;
            case LsAttributes.USER_LEVEL_REP:
                if( !new File(weeklyHtmlPEDReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyPEDReportForWebSingleREP.rptdesign","",userCode,"",weeklyHtmlPEDReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the Web lower weekly html PED report to REP is done.");
                }else{
                    logger.info("The Web lower weekly html ped report for REP is already generated, no need to do again.");
                }
                
                if( !new File(weeklyHtmlRESReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/weeklyRESReportForWebSingleREP.rptdesign","",userCode,"",weeklyHtmlRESReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the Web lower weekly html RES report to REP is done.");
                }else{
                    logger.info("The Web lower weekly html res report for REP is already generated, no need to do again.");
                }
                break;
            default:
                logger.info(String.format("the level of the user is %s, no need to generate the report", userLevel));
                break;
                
        }
    }

    private void createHTMLMonthlyReport(BirtReportUtils html, String userLevel,String telephone, String basePath, String contextPath, String lastMonth){
        String monthlyHtmlReportFileName = basePath + "monthlyHTMLReport/"+lastMonth+"/monthlyReport-"+userLevel+"-"+telephone+"-"+DateUtils.getLastMonth()+".html";
        
        switch(userLevel){
            case LsAttributes.USER_LEVEL_RSD:
                if( !new File(monthlyHtmlReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/monthlyReportRSD.rptdesign",telephone,"","",monthlyHtmlReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html PED report to RSD is done.");
                }else{
                    logger.info("The weekly html ped report for RSD is already generated, no need to do again.");
                }
                break;
            
            case LsAttributes.USER_LEVEL_RSM:
                if( !new File(monthlyHtmlReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/monthlyReportRSM.rptdesign",telephone,"","",monthlyHtmlReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html PED report to RSM is done.");
                }else{
                    logger.info("The weekly html ped report for RSM is already generated, no need to do again.");
                }
                break;
                
            case LsAttributes.USER_LEVEL_DSM:
                if( !new File(monthlyHtmlReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/monthlyReportDSM.rptdesign",telephone,"","",monthlyHtmlReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html PED report to DSM is done.");
                }else{
                    logger.info("The weekly html ped report for DSM is already generated, no need to do again.");
                }
                break;
            case LsAttributes.USER_LEVEL_BM:
                if( !new File(monthlyHtmlReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/monthlyReportBU.rptdesign","","","",monthlyHtmlReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the weekly html PED report to BU Head is done.");
                }else{
                    logger.info("The weekly html ped report for BU Head is already generated, no need to do again.");
                }
                break;
            default:
                logger.info(String.format("the level of the user is %s, no need to generate the report", userLevel));
                break;
        }
    }
    
    private void createHTMLMonthlyReportForWeb(BirtReportUtils html, String userLevel,String telephone, String basePath, String contextPath, String lastMonth){
        String monthlyHtmlReportFileName = basePath + "monthlyHTMLReportForWeb/"+lastMonth+"/monthlyReport-"+userLevel+"-"+telephone+"-"+DateUtils.getLastMonth()+".html";
        
        switch(userLevel){
            case LsAttributes.USER_LEVEL_RSD:
                if( !new File(monthlyHtmlReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/monthlyReportForWebRSD.rptdesign",telephone,"","",monthlyHtmlReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html PED report to RSD is done.");
                }else{
                    logger.info("The web weekly html ped report for RSD is already generated, no need to do again.");
                }
                break;
                
            case LsAttributes.USER_LEVEL_RSM:
                if( !new File(monthlyHtmlReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/monthlyReportForWebRSM.rptdesign",telephone,"","",monthlyHtmlReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html PED report to RSM is done.");
                }else{
                    logger.info("The web weekly html ped report for RSM is already generated, no need to do again.");
                }
                break;
                
            case LsAttributes.USER_LEVEL_DSM:
                if( !new File(monthlyHtmlReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/monthlyReportForWebDSM.rptdesign",telephone,"","",monthlyHtmlReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html PED report to DSM is done.");
                }else{
                    logger.info("The web weekly html ped report for DSM is already generated, no need to do again.");
                }
                break;
            case LsAttributes.USER_LEVEL_BM:
                if( !new File(monthlyHtmlReportFileName).exists() ){
                    html.runReport( basePath + "reportDesigns/monthlyReportForWebBU.rptdesign","","","",monthlyHtmlReportFileName,"html",basePath+"/reportImages",contextPath+"/reportImages");
                    logger.info("the web weekly html PED report to BU Head is done.");
                }else{
                    logger.info("The web weekly html ped report for BU Head is already generated, no need to do again.");
                }
                break;
            default:
                logger.info(String.format("the level of the user is %s, no need to generate the report", userLevel));
                break;
        }
    }
    
    private void createHTMLWeeklyReportOfHospital(BirtReportUtils html, String hospitalCode, String basePath, String contextPath, String lastThursday){
        String hospitalHTMLReport = basePath + "hospitalHTMLReport/"+lastThursday+"/hospitalReport-"+hospitalCode+"-"+DateUtils.getLastThursDay()+".html";
        
        if( !new File(hospitalHTMLReport).exists() ){
            html.runReport( basePath + "reportDesigns/hospitalReport.rptdesign","","",hospitalCode,hospitalHTMLReport,"html",basePath+"/reportImages",contextPath+"/reportImages");
            logger.info("the weekly html hospital report is done.");
        }else{
            logger.info("The weekly html hospital report is already generated, no need to do again.");
        }
    }
    
    private void createHTMLWeeklyReportOfHospitalForWeb(BirtReportUtils html, String hospitalCode, String basePath, String contextPath, String lastThursday){
        String hospitalHTMLReport = basePath + "hospitalHTMLReportForWeb/"+lastThursday+"/hospitalReport-"+hospitalCode+"-"+DateUtils.getLastThursDay()+".html";
        
        if( !new File(hospitalHTMLReport).exists() ){
            html.runReport( basePath + "reportDesigns/hospitalReportForWeb.rptdesign","","",hospitalCode,hospitalHTMLReport,"html",basePath+"/reportImages",contextPath+"/reportImages");
            logger.info("the web weekly html hospital report is done.");
        }else{
            logger.info("The web weekly html hospital report is already generated, no need to do again.");
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
