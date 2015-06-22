package com.chalet.lskpi.utils;

import java.io.File;
import java.util.Calendar;
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
            String lastThursday = DateUtils.getDirectoryNameOfCurrentDuration(new Date(refreshDate.getTime() + 7 * 24 * 60 * 60 * 1000));
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
                    lastThursday = DateUtils.getDirectoryNameOfCurrentDuration(new Date(refreshDate.getTime() + 7 * 24 * 60 * 60 * 1000));
                    createWeeklyHomePDFReport(html, user, telephone, startDate, endDate, lastThursday, basePath, contextPath, checkFileExists, isFirstRefresh, false);
                    lastThursday = DateUtils.getDirectoryNameOfCurrentDuration(refreshDate);
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
        String weeklyPDFPEDEmergingReportFileName = new StringBuilder(basePath).append("weeklyReport/")
        		.append(lastThursday)
        		.append("/儿科门急诊周报-")
        		.append(fileSubName)
        		.append("-")
        		.append(lastThursday)
        		.append(".pdf").toString();
        
        String weeklyPDFPEDRoomReportFileName = new StringBuilder(basePath).append("weeklyReport/")
		        .append(lastThursday)
		        .append("/儿科住院周报-")
		        .append(fileSubName)
		        .append("-")
		        .append(lastThursday)
		        .append(".pdf").toString();
        
        String weeklyPDFRESReportFileName = new StringBuilder(basePath).append("weeklyReport/")
        		.append(lastThursday)
        		.append("/呼吸科周报-")
        		.append(fileSubName)
        		.append("-")
        		.append(lastThursday)
        		.append(".pdf").toString();
        
        String weeklyPDFCHEReportFileName = new StringBuilder(basePath).append("weeklyReport/")
        		.append(lastThursday)
        		.append("/胸外科周报-")
        		.append(fileSubName)
        		.append("-")
        		.append(lastThursday)
        		.append(".pdf").toString();
        
        switch(userLevel){
            case LsAttributes.USER_LEVEL_RSD:
              //RSD
            	if( !new File(weeklyPDFPEDEmergingReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDEmergingReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportRSD.rptdesign",telephone,startDate,endDate,weeklyPDFPEDEmergingReportFileName,"pdf","","","");
            		logger.info("the weekly report for RSD is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            	
            	if( !new File(weeklyPDFPEDRoomReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDRoomReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDRoomReportRSD.rptdesign",telephone,startDate,endDate,weeklyPDFPEDRoomReportFileName,"pdf","","","");
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
            	if( !new File(weeklyPDFPEDEmergingReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDEmergingReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportRSM.rptdesign",telephone,startDate,endDate,weeklyPDFPEDEmergingReportFileName,"pdf","","","");
            		logger.info("the weekly report for RSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            	
            	if( !new File(weeklyPDFPEDRoomReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDRoomReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDRoomReportRSM.rptdesign",telephone,startDate,endDate,weeklyPDFPEDRoomReportFileName,"pdf","","","");
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
            	if( !new File(weeklyPDFPEDEmergingReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDEmergingReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportDSM.rptdesign",telephone,startDate,endDate,weeklyPDFPEDEmergingReportFileName,"pdf","","","");
            		logger.info("the weekly report for DSM is done.");
            	}else{
            		logger.info(String.format(LOG_MESSAGE, fileSubName));
            	}
            
            	if( !new File(weeklyPDFPEDRoomReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDRoomReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDRoomReportDSM.rptdesign",telephone,startDate,endDate,weeklyPDFPEDRoomReportFileName,"pdf","","","");
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
            	if( !new File(weeklyPDFPEDEmergingReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFPEDEmergingReportFileName).exists()) ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportREP.rptdesign",telephone,startDate,endDate,weeklyPDFPEDEmergingReportFileName,"pdf","","","");
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
            	if( !new File(weeklyPDFPEDEmergingReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDReportBU.rptdesign",telephone,startDate,endDate,weeklyPDFPEDEmergingReportFileName,"pdf","","","");
            		logger.info("the ped weekly report for BU is done.");
            	}else{
            		logger.info("The ped weekly report for BU is already generated, no need to do again.");
            	}
            	
            	if( !new File(weeklyPDFPEDRoomReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
            		html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyPEDRoomReportBU.rptdesign",telephone,startDate,endDate,weeklyPDFPEDRoomReportFileName,"pdf","","","");
            		logger.info("the ped weekly report for BU is done.");
            	}else{
            		logger.info("The ped weekly report for BU is already generated, no need to do again.");
            	}
                
                if( !new File(weeklyPDFRESReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
                    html.runRefreshReport( basePath + "reportDesigns/refresh_weeklyRESReportBU.rptdesign",telephone,startDate,endDate,weeklyPDFRESReportFileName,"pdf","","","");
                    logger.info("the res weekly res report for BU is done.");
                }else{
                    logger.info("The res weekly report for BU is already generated, no need to do again.");
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
    public static void createWeeklyHomePDFReport(BirtReportUtils html, UserInfo user,String telephone,String startDate, String endDate, String directoryName, String basePath, String contextPath, boolean checkFileExists, boolean isFirstRefresh, boolean isScheduledFresh) throws Exception{
    	String userLevel = user.getLevel();
    	String fileSubName = StringUtils.getFileSubName(user);
    	
    	String startDuration = "";
    	String endDuration = "";
    	
    	String lastWeek2Duration = "";
    			
    	if( isScheduledFresh ){
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(new Date());
        	cal.add(Calendar.DAY_OF_YEAR,7);
        	
    		directoryName = DateUtils.getDirectoryNameOfLastDuration(cal.getTime());
        	startDuration = DateUtils.getThursdayHome12WeeksBeginDuration();
    		endDuration = DateUtils.getThursdayHome12WeeksEndDuration();
    		
            lastWeek2Duration = DateUtils.getThursdayHomeLast2WeekEndDuration(startDuration);
            
            cal.add(Calendar.DAY_OF_YEAR,-14);
            startDate = DateUtils.getTheBeginDateOfRefreshDate(cal.getTime());
            endDate = DateUtils.getTheEndDateOfRefreshDate4ReportTable(cal.getTime());
    		logger.info(String.format("this is the scheduled refresh, directoryName is %s,startDuration is %s, endDuration is %s, lastWeek2Duration is %s ",directoryName,startDuration,endDuration,lastWeek2Duration));
    	}else{
    		startDuration = startDate+"-"+endDate;
    		endDuration = DateUtils.getThursdayHome12WeeksEndDuration(startDuration);
    		
    		lastWeek2Duration = DateUtils.getThursdayHomeLast2WeekEndDuration(startDuration);
    		
    		Date end_date = DateUtils.parseString2Date(endDate);
        	Calendar cal = Calendar.getInstance();
        	cal.setTime(end_date);
        	cal.add(Calendar.DAY_OF_YEAR,1);
    		endDate = DateUtils.parseDate2String(cal.getTime());
    		
    		logger.info(String.format("this is the manual refresh, directoryName is %s,startDuration is %s, endDuration is %s, lastWeek2Duration is %s",directoryName,startDuration,endDuration,lastWeek2Duration));
    	}
    	
    	String homeFileNamePre = basePath + "weeklyReport/"+directoryName+"/家庭雾化周报-"+fileSubName+"-"+directoryName;
    	
    	String weeklyPDFHomeReportFileName = homeFileNamePre+".pdf";
    	
    	switch(userLevel){
    	case LsAttributes.USER_LEVEL_RSD:
    		//RSD
    		if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForRSD.rptdesign",telephone,startDuration,endDuration,startDate, endDate, lastWeek2Duration, weeklyPDFHomeReportFileName);
    			logger.info("the weekly home report for RSD is done.");
    		}else{
    			logger.info(String.format(LOG_MESSAGE, fileSubName));
    		}
    		
    		break;
    	case LsAttributes.USER_LEVEL_RSM:
    		//RSM
    		if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForRSM.rptdesign",telephone,startDuration,endDuration,startDate, endDate, lastWeek2Duration, weeklyPDFHomeReportFileName);
    			logger.info("the weekly home report for RSM is done.");
    		}else{
    			logger.info(String.format(LOG_MESSAGE, fileSubName));
    		}
    		break;
    	case LsAttributes.USER_LEVEL_DSM:
    		//DSM
    		if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForDSM.rptdesign",telephone,startDuration,endDuration,startDate, endDate, lastWeek2Duration, weeklyPDFHomeReportFileName);
    			logger.info("the weekly home report for DSM is done.");
    		}else{
    			logger.info(String.format(LOG_MESSAGE, fileSubName));
    		}
    		break;
    	case LsAttributes.USER_LEVEL_REP:
    		//REP
    		if( !new File(weeklyPDFHomeReportFileName).exists() || ( !checkFileExists && new File(weeklyPDFHomeReportFileName).exists()) ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForREP.rptdesign",telephone,startDuration,endDuration,startDate, endDate, lastWeek2Duration, weeklyPDFHomeReportFileName);
    			logger.info("the weekly home report for REP is done.");
    		}else{
    			logger.info(String.format(LOG_MESSAGE, fileSubName));
    		}
    		break;
    	case LsAttributes.USER_LEVEL_BM:
    		if( !new File(weeklyPDFHomeReportFileName).exists() || (isFirstRefresh && !checkFileExists)  ){
    			html.runHomePDFReport( basePath + "reportDesigns/weeklyHomePDFReportForBU.rptdesign",telephone,startDuration,endDuration,startDate, endDate, lastWeek2Duration, weeklyPDFHomeReportFileName);
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
