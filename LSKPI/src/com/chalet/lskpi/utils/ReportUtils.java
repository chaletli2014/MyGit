package com.chalet.lskpi.utils;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.chalet.lskpi.model.UserInfo;

public class ReportUtils {
    
    private static Logger logger = Logger.getLogger(ReportUtils.class);
    
    private static String LOG_MESSAGE = "the weekly report is already exists for %s,no need to generate";
    
    public static void refreshWeeklyPDFReport(List<UserInfo> reportUserInfos, String basePath, String contextPath, Date refreshDate){
    	refreshWeeklyPDFReport(reportUserInfos, basePath, contextPath, refreshDate, false);
    }
    
    public static void refreshWeeklyPDFReport(List<UserInfo> reportUserInfos, String basePath, String contextPath, Date refreshDate, boolean checkFileExists){
        try{
            String lastThursday = DateUtils.getThursDayOfParamDate(refreshDate);
            String startDate = DateUtils.getTheBeginDateOfRefreshDate(refreshDate);
            String endDate = DateUtils.getTheEndDateOfRefreshDate(refreshDate);
            logger.info(String.format("start to refresh the pdf weekly report, lastThursday is %s, start date is %s, end date is %s", lastThursday, startDate, endDate));
            
            BirtReportUtils html = new BirtReportUtils();
            boolean isFirstRefresh = true;
            html.startPlatform();
            for( UserInfo user : reportUserInfos ){
                String telephone = user.getTelephone();
                if( telephone != null && !"#N/A".equalsIgnoreCase(telephone) ){
                    logger.info(String.format("the mobile is %s",telephone));
                    createWeeklyPDFReport(html, user, telephone, startDate, endDate, basePath, contextPath, lastThursday, user.getEmail(),isFirstRefresh,checkFileExists);
                }else{
                    logger.error(String.format("the telephone number for the user %s is not found", user.getName()));
                }
                isFirstRefresh = false;
            }
            html.stopPlatform();
            logger.info("end to refresh the pdf weekly report");
        }catch(Exception e){
            logger.error("fail to refresh the pdf weekly report,",e);
        }
    }

    public static void createWeeklyPDFReport(BirtReportUtils html, UserInfo user,String telephone, String startDate, String endDate, String basePath, String contextPath, String lastThursday, String email, boolean isFirstRefresh, boolean checkFileExists) throws Exception{
        String userLevel = user.getLevel();
        String fileSubName = StringUtils.getFileSubName(user);
        String pedFileNamePre = basePath + "weeklyReport/"+lastThursday+"/儿科周报-"+fileSubName+"-"+lastThursday;
        String resFileNamePre = basePath + "weeklyReport/"+lastThursday+"/呼吸科周报-"+fileSubName+"-"+lastThursday;
        
        String weeklyPDFPEDReportFileName = pedFileNamePre+".pdf";
        String weeklyPDFRESReportFileName = resFileNamePre+".pdf";
        
        switch(userLevel){
            case LsAttributes.USER_LEVEL_RSD:
              //RSD
            	if( !new File(weeklyPDFPEDReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportRSD.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","");
            		logger.info("the weekly report for RSD is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            	
            	if( !new File(weeklyPDFRESReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFRESReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportRSD.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","");
            		logger.info("the weekly res report for RSD is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
                
                break;
            case LsAttributes.USER_LEVEL_RSM:
              //RSM
            	if( !new File(weeklyPDFPEDReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportRSM.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","");
            		logger.info("the weekly report for RSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            
            	if( !new File(weeklyPDFRESReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFRESReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportRSM.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","");
            		logger.info("the weekly res report for RSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
                break;
            case LsAttributes.USER_LEVEL_DSM:
              //DSM
            	if( !new File(weeklyPDFPEDReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportDSM.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","");
            		logger.info("the weekly report for DSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            
            	if( !new File(weeklyPDFRESReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFRESReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportDSM.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","");
            		logger.info("the weekly res report for DSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
                break;
            case LsAttributes.USER_LEVEL_REP:
              //REP
            	if( !new File(weeklyPDFPEDReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportREP.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","");
            		logger.info("the weekly report for REP is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            
            	if( !new File(weeklyPDFRESReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFRESReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportREP.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","");
            		logger.info("the weekly res report for REP is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
                break;
            case LsAttributes.USER_LEVEL_BM:
                String weeklyPDFPEDReportFileName_central = pedFileNamePre+"_central.pdf";
                String weeklyPDFPEDReportFileName_east1 = pedFileNamePre+"_east1.pdf";
                String weeklyPDFPEDReportFileName_east2 = pedFileNamePre+"_east2.pdf";
                String weeklyPDFPEDReportFileName_north = pedFileNamePre+"_north.pdf";
                String weeklyPDFPEDReportFileName_south = pedFileNamePre+"_south.pdf";
                String weeklyPDFPEDReportFileName_west = pedFileNamePre+"_west.pdf";
                
                String weeklyPDFRESReportFileName_central = resFileNamePre+"_central.pdf";
                String weeklyPDFRESReportFileName_east1 = resFileNamePre+"_east1.pdf";
                String weeklyPDFRESReportFileName_east2 = resFileNamePre+"_east2.pdf";
                String weeklyPDFRESReportFileName_north = resFileNamePre+"_north.pdf";
                String weeklyPDFRESReportFileName_south = resFileNamePre+"_south.pdf";
                String weeklyPDFRESReportFileName_west = resFileNamePre+"_west.pdf";
                
                if( !new File(weeklyPDFPEDReportFileName_central).exists() || (isFirstRefresh && !checkFileExists) ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBUCentral.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName_central,"pdf","","");
                    logger.info("the ped weekly report for BU Central is done.");
                }else{
                    logger.info("The ped weekly report for BU Central is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFPEDReportFileName_east1).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBUEast1.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName_east1,"pdf","","");
                    logger.info("the ped weekly report for BU east1 is done.");
                }else{
                    logger.info("The ped weekly report for BU east1 is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFPEDReportFileName_east2).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBUEast2.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName_east2,"pdf","","");
                    logger.info("the ped weekly report for BU east2 is done.");
                }else{
                    logger.info("The ped weekly report for BU east2 is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFPEDReportFileName_north).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBUNorth.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName_north,"pdf","","");
                    logger.info("the ped weekly report for BU north is done.");
                }else{
                    logger.info("The ped weekly report for BU north is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFPEDReportFileName_south).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBUSouth.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName_south,"pdf","","");
                    logger.info("the ped weekly report for BU south is done.");
                }else{
                    logger.info("The ped weekly report for BU south is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFPEDReportFileName_west).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBUWest.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName_west,"pdf","","");
                    logger.info("the ped weekly report for BU west is done.");
                }else{
                    logger.info("The ped weekly report for BU west is already generated, no need to do again.");
                }
                
                if( !new File(weeklyPDFRESReportFileName_central).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBUCentral.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName_central,"pdf","","");
                    logger.info("the res weekly res report for BU Central is done.");
                }else{
                    logger.info("The res weekly res report for BU Central is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFRESReportFileName_east1).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBUEast1.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName_east1,"pdf","","");
                    logger.info("the res weekly report for BU east1 is done.");
                }else{
                    logger.info("The res weekly report for BU east1 is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFRESReportFileName_east2).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBUEast2.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName_east2,"pdf","","");
                    logger.info("the res weekly report for BU east2 is done.");
                }else{
                    logger.info("The res weekly report for BU east2 is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFRESReportFileName_north).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBUNorth.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName_north,"pdf","","");
                    logger.info("the res weekly report for BU north is done.");
                }else{
                    logger.info("The res weekly report for BU north is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFRESReportFileName_south).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBUSouth.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName_south,"pdf","","");
                    logger.info("the res weekly report for BU south is done.");
                }else{
                    logger.info("The res weekly report for BU south is already generated, no need to do again.");
                }
                if( !new File(weeklyPDFRESReportFileName_west).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBUWest.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName_west,"pdf","","");
                    logger.info("the res weekly report for BU west is done.");
                }else{
                    logger.info("The res weekly report for BU west is already generated, no need to do again.");
                }
                
                if( !new File(weeklyPDFRESReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBU.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","");
                    logger.info("the res weekly res report for BU is done.");
                }else{
                    logger.info("The res weekly report for BU is already generated, no need to do again.");
                }
                
                if( !new File(weeklyPDFPEDReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBU.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","");
                    logger.info("the ped weekly report for BU is done.");
                }else{
                    logger.info("The ped weekly report for BU is already generated, no need to do again.");
                }
                break;
            default:
                logger.info(String.format("the level of the user is %s, no need to generate the report", userLevel));
                break;
        }
    }
}
