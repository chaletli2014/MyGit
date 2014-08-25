package com.chalet.lskpi.utils;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.chalet.lskpi.model.UserInfo;

public class ReportUtils {
    
    private static Logger logger = Logger.getLogger(ReportUtils.class);
    
    private static String LOG_MESSAGE = "the weekly report is already exists for %s,no need to generate";
    
    public static void refreshWeeklyPDFReport(List<UserInfo> reportUserInfos, String basePath, String contextPath, Date refreshDate, List<String> regionList){
    	refreshWeeklyPDFReport(reportUserInfos, basePath, contextPath, refreshDate, false, regionList);
    }
    
    public static void refreshWeeklyPDFReport(List<UserInfo> reportUserInfos, String basePath, String contextPath, Date refreshDate, boolean checkFileExists, List<String> regionList){
        try{
            String lastThursday = DateUtils.getDirectoryNameOfCurrentDuration(refreshDate);
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
                    createWeeklyPDFReport(html, user, telephone, startDate, endDate, basePath, contextPath, lastThursday, user.getEmail(),isFirstRefresh,checkFileExists, regionList);
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

    public static void createWeeklyPDFReport(BirtReportUtils html, UserInfo user,String telephone, String startDate, String endDate, String basePath, String contextPath, String lastThursday, String email, boolean isFirstRefresh, boolean checkFileExists, List<String> regionList) throws Exception{
        String userLevel = user.getLevel();
        String fileSubName = StringUtils.getFileSubName(user);
        String pedFileNamePre = basePath + "weeklyReport/"+lastThursday+"/儿科周报-"+fileSubName+"-"+lastThursday;
        String resFileNamePre = basePath + "weeklyReport/"+lastThursday+"/呼吸科周报-"+fileSubName+"-"+lastThursday;
        String cheFileNamePre = basePath + "weeklyReport/"+lastThursday+"/胸外科周报-"+fileSubName+"-"+lastThursday;
        
        String weeklyPDFPEDReportFileName = pedFileNamePre+".pdf";
        String weeklyPDFRESReportFileName = resFileNamePre+".pdf";
        String weeklyPDFCHEReportFileName = cheFileNamePre+".pdf";
        
        switch(userLevel){
            case LsAttributes.USER_LEVEL_RSD:
              //RSD
            	if( !new File(weeklyPDFPEDReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportRSD.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","","");
            		logger.info("the weekly report for RSD is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            	
            	if( !new File(weeklyPDFRESReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFRESReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportRSD.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","","");
            		logger.info("the weekly res report for RSD is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            	
            	if( !new File(weeklyPDFCHEReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFCHEReportFileName).exists()) ){
            	    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyCHEReportRSD.rptdesign",telephone,startDate,endDate,weeklyPDFCHEReportFileName,"pdf","","","");
            	    logger.info("the weekly chest surgery report for RSD is done.");
            	}else{
            	    logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            	
                break;
            case LsAttributes.USER_LEVEL_RSM:
              //RSM
            	if( !new File(weeklyPDFPEDReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportRSM.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","","");
            		logger.info("the weekly report for RSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            
            	if( !new File(weeklyPDFRESReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFRESReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportRSM.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","","");
            		logger.info("the weekly res report for RSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            	
                if( !new File(weeklyPDFCHEReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFCHEReportFileName).exists()) ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyCHEReportRSM.rptdesign",telephone,startDate,endDate,weeklyPDFCHEReportFileName,"pdf","","","");
                    logger.info("the weekly chest surgery report for RSM is done.");
                }else{
                    logger.info(String.format(LOG_MESSAGE, fileSubName));
                }
                break;
            case LsAttributes.USER_LEVEL_DSM:
              //DSM
            	if( !new File(weeklyPDFPEDReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportDSM.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","","");
            		logger.info("the weekly report for DSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            
            	if( !new File(weeklyPDFRESReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFRESReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportDSM.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","","");
            		logger.info("the weekly res report for DSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            	
            	if( !new File(weeklyPDFCHEReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFCHEReportFileName).exists()) ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyCHEReportDSM.rptdesign",telephone,startDate,endDate,weeklyPDFCHEReportFileName,"pdf","","","");
                    logger.info("the weekly chest surgery report for DSM is done.");
                }else{
                    logger.info(String.format(LOG_MESSAGE, fileSubName));
                }
                break;
            case LsAttributes.USER_LEVEL_REP:
              //REP
            	if( !new File(weeklyPDFPEDReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportREP.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","","");
            		logger.info("the weekly report for REP is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            
            	if( !new File(weeklyPDFRESReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFRESReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportREP.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","","");
            		logger.info("the weekly res report for REP is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
                break;
            case LsAttributes.USER_LEVEL_BM:
                for( String region : regionList ){
                    String weeklyPDFPEDSingleRSDReportFileName = new StringBuffer(pedFileNamePre).append("_").append(region).append(".pdf").toString();
                    String weeklyPDFRESSingleRSDReportFileName = new StringBuffer(resFileNamePre).append("_").append(region).append(".pdf").toString();
                    
                    if( !new File(weeklyPDFPEDSingleRSDReportFileName).exists() || (isFirstRefresh && !checkFileExists) ){
                        html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBUSingleRSD.rptdesign",telephone,startDate,endDate,weeklyPDFPEDSingleRSDReportFileName,"pdf","","",region);
                        logger.info(String.format("the ped weekly report for BU %s is done.", region));
                    }
                    
                    if( !new File(weeklyPDFRESSingleRSDReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
                        html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBUSingleRSD.rptdesign",telephone,startDate,endDate,weeklyPDFRESSingleRSDReportFileName,"pdf","","",region);
                        logger.info(String.format("the res weekly report for BU %s is done.", region));
                    }
                    
                }
                
                if( !new File(weeklyPDFRESReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBU.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","","");
                    logger.info("the res weekly res report for BU is done.");
                }else{
                    logger.info("The res weekly report for BU is already generated, no need to do again.");
                }
                
                if( !new File(weeklyPDFPEDReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBU.rptdesign",telephone,startDate,endDate,weeklyPDFPEDReportFileName,"pdf","","","");
                    logger.info("the ped weekly report for BU is done.");
                }else{
                    logger.info("The ped weekly report for BU is already generated, no need to do again.");
                }
                
                if( !new File(weeklyPDFCHEReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyCHEReportBU.rptdesign",telephone,startDate,endDate,weeklyPDFCHEReportFileName,"pdf","","","");
                    logger.info("the chest surgery weekly report for BU is done.");
                }else{
                    logger.info("The chest surgery weekly report for BU is already generated, no need to do again.");
                }
                break;
            default:
                logger.info(String.format("the level of the user is %s, no need to generate the report", userLevel));
                break;
        }
    }
}
