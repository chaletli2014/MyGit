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
        String homeFileNamePre = basePath + "weeklyReport/"+lastThursday+"/家庭雾化周报-"+fileSubName+"-"+lastThursday;
        
        String weeklyPDFPEDReportFileName = pedFileNamePre+".pdf";
        String weeklyPDFRESReportFileName = resFileNamePre+".pdf";
        String weeklyPDFCHEReportFileName = cheFileNamePre+".pdf";
        String weeklyPDFHomeReportFileName = homeFileNamePre+".pdf";
        
        String startDuration = startDate+"-"+endDate;
		String endDuration = DateUtils.getThursdayHome12WeeksEndDuration(startDuration);
        
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
            	
            	if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
            		html.runHomeReport( basePath + "reportDesigns/weeklyHomePDFReportForRSD.rptdesign",telephone,startDuration,endDuration,weeklyPDFHomeReportFileName,"pdf","","");
            		logger.info("the weekly home report for RSD is done.");
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
                
                if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
                	html.runHomeReport( basePath + "reportDesigns/weeklyHomePDFReportForRSM.rptdesign",telephone,startDuration,endDuration,weeklyPDFHomeReportFileName,"pdf","","");
                	logger.info("the weekly home report for RSM is done.");
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
            	
            	if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
            		html.runHomeReport( basePath + "reportDesigns/weeklyHomePDFReportForDSM.rptdesign",telephone,startDuration,endDuration,weeklyPDFHomeReportFileName,"pdf","","");
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
                
                if( !new File(weeklyPDFHomeReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
                	html.runHomeReport( basePath + "reportDesigns/weeklyHomePDFReportForBU.rptdesign",telephone,startDuration,endDuration,weeklyPDFHomeReportFileName,"pdf","","");
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
    public static void createWeeklyHomePDFReport(BirtReportUtils html, UserInfo user,String telephone,String basePath, String contextPath, boolean checkFileExists, boolean isFirstRefresh) throws Exception{
    	String userLevel = user.getLevel();
    	String fileSubName = StringUtils.getFileSubName(user);
    	Date now = new Date();
    	
    	String reportGenerateDate = DateUtils.getDirectoryNameOfLastDuration(new Date(now.getTime()+ 7 * 24 * 60 * 60 * 1000));
    	String startDuration = DateUtils.getThursdayHome12WeeksBeginDuration();
		String endDuration = DateUtils.getThursdayHome12WeeksEndDuration();
    	
    	String homeFileNamePre = basePath + "weeklyReport/"+reportGenerateDate+"/家庭雾化周报-"+fileSubName+"-"+reportGenerateDate;
    	
    	String weeklyPDFHomeReportFileName = homeFileNamePre+".pdf";
    	
    	switch(userLevel){
    	case LsAttributes.USER_LEVEL_RSD:
    		//RSD
    		if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForRSD.rptdesign",telephone,startDuration,endDuration,weeklyPDFHomeReportFileName);
    			logger.info("the weekly home report for RSD is done.");
    		}else{
    			logger.info(String.format(LOG_MESSAGE, fileSubName));
    		}
    		
    		break;
    	case LsAttributes.USER_LEVEL_RSM:
    		//RSM
    		if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForRSM.rptdesign",telephone,startDuration,endDuration,weeklyPDFHomeReportFileName);
    			logger.info("the weekly home report for RSM is done.");
    		}else{
    			logger.info(String.format(LOG_MESSAGE, fileSubName));
    		}
    		break;
    	case LsAttributes.USER_LEVEL_DSM:
    		//DSM
    		if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForDSM.rptdesign",telephone,startDuration,endDuration,weeklyPDFHomeReportFileName);
    			logger.info("the weekly home report for DSM is done.");
    		}else{
    			logger.info(String.format(LOG_MESSAGE, fileSubName));
    		}
    		break;
    	case LsAttributes.USER_LEVEL_REP:
    		//REP
    		if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForREP.rptdesign",telephone,startDuration,endDuration,weeklyPDFHomeReportFileName);
    			logger.info("the weekly home report for REP is done.");
    		}else{
    			logger.info(String.format(LOG_MESSAGE, fileSubName));
    		}
    		break;
    	case LsAttributes.USER_LEVEL_BM:
    		if( !new File(weeklyPDFHomeReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForBU.rptdesign",telephone,startDuration,endDuration,weeklyPDFHomeReportFileName);
    			logger.info("the home weekly report for BU is done.");
    		}else{
    			logger.info("The home weekly report for BU is already generated, no need to do again.");
    		}
    		break;
    	default:
    		logger.info(String.format("the level of the user is %s, no need to generate the report", userLevel));
    		break;
    	}
    }
}
