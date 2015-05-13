package com.chalet.lskpi.utils;

import java.util.Date;

public class LsAttributes {

    public static final String COLLECT_RESPIROLOGY_MESSAGE = "collectRespirologyMessage";
    public static final String COLLECT_PEDIATRICS_MESSAGE = "collectPediatricsMessage";
    public static final String COLLECT_MONTHLYDATA_MESSAGE = "collectMonthlyDataMessage";
    public static final String COLLECT_HOMEDATA_MESSAGE = "collectHomeDataMessage";
    public static final String COLLECT_CHESTSURGERY_MESSAGE = "collectChestSurgeryMessage";
    public static final String WEEKLY_PDF_REPORT_MESSAGE = "weeklyReportMessage";
    public static final String WEEKLY_PDF_REFRESH_MESSAGE = "weeklyPDFRefreshMessage";
    public static final String WEEKLY_HOS_REFRESH_MESSAGE = "weeklyHosRefreshMessage";
    public static final String UPLOAD_FILE_MESSAGE = "uploadFileMessage";
    public static final String CURRENT_OPERATOR_OBJECT = "operatorObj";
    public static final String CURRENT_OPERATOR = "operator";
    public static final String CURRENT_DATE = "currentDate";
    /**
     * 提交成功.
     */
    public static final String RETURNED_MESSAGE_0 = "提交成功";
    /**
     * 提交失败，系统错误.
     */
    public static final String RETURNED_MESSAGE_1 = "提交失败，系统错误！";
    
    /**
     * 获取数据失败，系统错误.
     */
    public static final String RETURNED_MESSAGE_2 = "获取数据失败，系统错误！";
    /**
     * 很抱歉，您没有权限查看或使用该功能.
     */
    public static final String RETURNED_MESSAGE_3 = "很抱歉，您没有权限查看或使用该功能！";
    /**
     * 很抱歉，用来接收周报的邮箱地址不合法，请修正后重试.
     */
    public static final String RETURNED_MESSAGE_4 = "很抱歉，用来接收周报的邮箱地址不合法，请修正后重试！";
    /**
     * 导出周报的层级不能为空.
     */
    public static final String RETURNED_MESSAGE_5 = "导出周报的层级不能为空！";
    /**
     * 导出周报的日期不能为空.
     */
    public static final String RETURNED_MESSAGE_6 = "导出周报的日期不能为空！";
    /**
     * 无法获取医院信息，请重新选择医院.
     */
    public static final String RETURNED_MESSAGE_7 = "无法获取医院信息，请重新选择医院！";
    /**
     * 医院和医生姓名不能为空.
     */
    public static final String RETURNED_MESSAGE_8 = "医院和医生姓名不能为空！";
    /**
     * 获取医生信息失败.
     */
    public static final String RETURNED_MESSAGE_9 = "获取医生信息失败！";
    /**
     * 未查到相关数据.
     */
    public static final String RETURNED_MESSAGE_10 = "未查到相关数据！";
    /**
     * 关联销售不能为空.
     */
    public static final String RETURNED_MESSAGE_11 = "关联销售不能为空！";
    /**
     * 家庭雾化录入周期为周一到周三，目前无法录入.
     */
    public static final String RETURNED_MESSAGE_12 = "家庭雾化录入周期为周一到周三，目前无法录入！";
    /**
     * 医生重新绑定到当前用户成功。
     */
    public static final String RETURNED_MESSAGE_13 = "医生重新绑定成功！";
    /**
     * 医生添加成功。
     */
    public static final String RETURNED_MESSAGE_14 = "医生添加成功！";
    /**
     * 操作已提交，请勿重复操作！
     */
    public static final String RETURNED_MESSAGE_15 = "操作已提交，请勿重复操作！";
    
    /**
     * 获取用户信息失败，请重新登录APP.
     */
    public static final String NO_USER_FOUND = "获取用户信息失败，请重新登录APP";
    /**
     * 获取用户信息失败，请重新登录.
     */
    public static final String NO_USER_FOUND_WEB = "获取用户信息失败，请重新登录";
    /**
     * 周报未生成，请联系管理员.
     */
    public static final String NO_WEEKLY_PDF_FOUND = "周报未生成，请联系管理员";
    /**
     * 发送周报邮件失败，请联系管理员.
     */
    public static final String SEND_WEEKLY_PDF_ERROR = "发送周报邮件失败，请联系管理员";
    /**
     * 上周周报已发送，请注意查收.
     */
    public static final String WEEKLY_PDF_SEND = "上周周报已发送，请注意查收";
    public static final String WEEKLY_PDF_REFRESHED = "周报已刷新";
    public static final String WEEKLY_HOS_DATA_REFRESHED = "数据已刷新";
    public static final String JSP_VERIFY_MESSAGE = "message";
    
    public static final String MESSAGE_AREA_ID = "messageareaid";
    
    public static final String VALID_DATA_NUM = "validDataNum";
    public static final String INVALID_DATA = "invalidData";
    public static final String EXISTS_DATA = "existsData";
    
    
    public static final String MOBILE_DAILY_REPORT_DATA = "mobileDailyReportData";
    public static final String MOBILE_DAILY_REPORT_CHILD_DATA = "mobileDailyReportChildData";
    public static final String MOBILE_DAILY_REPORT_PARENT_DATA = "mobileDailyReportParentData";
    public static final String MOBILE_DAILY_REPORT_CENTRAL_DATA = "mobileDailyReportCentralData";
    public static final String MOBILE_DAILY_REPORT_EAST1_DATA = "mobileDailyReportEast1Data";
    public static final String MOBILE_DAILY_REPORT_EAST2_DATA = "mobileDailyReportEast2Data";
    public static final String MOBILE_DAILY_REPORT_NORTH_DATA = "mobileDailyReportNorthData";
    public static final String MOBILE_DAILY_REPORT_SOUTH_DATA = "mobileDailyReportSouthData";
    public static final String MOBILE_DAILY_REPORT_WEST_DATA = "mobileDailyReportWestData";
    public static final String MOBILE_DAILY_REPORT_ALL_RSM_DATA = "mobileDailyReportAllRSMData";
    
    public static final String USER_LEVEL_REP = "REP";
    public static final String USER_LEVEL_DSM = "DSM";
    public static final String USER_LEVEL_RSM = "RSM";
    public static final String USER_LEVEL_RSD = "RSD";
    public static final String USER_LEVEL_BM = "BM";
    public static final String USER_LEVEL_BU_HEAD = "BU Head";
    public static final String USER_LEVEL_MD = "MD";
    public static final String USER_LEVEL_SARTON = "Sarton";
    
    
    public static final String BR_NAME_SOUTH = "South GRA";
    public static final String BR_NAME_SOUTH_ZH = "南区";
    public static final String BR_NAME_NORTH = "North GRA";
    public static final String BR_NAME_NORTH_ZH = "北区";
    public static final String BR_NAME_CENTRAL = "Central GRA";
    public static final String BR_NAME_CENTRAL_ZH = "中区";
    public static final String BR_NAME_EAST1 = "East1 GRA";
    public static final String BR_NAME_EAST1_ZH = "东一区";
    public static final String BR_NAME_EAST2 = "East2 GRA";
    public static final String BR_NAME_EAST2_ZH = "东二区";
    public static final String BR_NAME_WEST = "West GRA";
    public static final String BR_NAME_WEST_ZH = "西区";
    
    public static final String DEPARTMENT_PED = "PED";
    public static final String DEPARTMENT_RES = "RES";
    public static final String DEPARTMENT_CHE = "CHE";
    
    public static final String WEB_LOGIN_MESSAGE = "web_login_message";
    public static final String WEB_LOGIN_USER = "web_login_user";
    public static final String WEB_LOGIN_MESSAGE_NO_USER = "用户名或密码错误";
    
    public static final String DAILYREPORTTITLE_1 = "所属 [";
    public static final String DAILYREPORTTITLE_2 = "] ";
    public static final String DAILYREPORTTITLE_3 = "日报数据表";
    public static final String DAILYREPORTTITLE_4 = "全国";
    public static final String DAILYREPORTTITLE_REP = "销售";
    public static final String DAILYREPORTTITLE_3_2 = "上周数据环比";
    public static final String DAILYREPORTTITLE_SPLIT_1 = "[";
    public static final String DAILYREPORTTITLE_SPLIT_2 = "]";
    
    public static final String MONTHLYREPORTTITLE = "销售袋数月报";
    public static final String MONTHLY12TITLE = "销售袋数报告";
    public static final String MONTHLYCOLLECTIONDATEERROR="销售袋数采集为次月1号到10号，目前无法录入";
    
    public static final String HOMEWEEKLYREPORTTITLE = "家庭雾化周报";
    
    public static final String DATAQUERY_PROCESS_DATA="processData";
    public static final String DATAQUERY_PROCESS_DATA_DETAIL="processDataDetail";
    public static final String DATAQUERY_PROCESS_CHILD_DATA="processChildData";
    public static final String WEEKLY_RATIO_DATA = "weeklyRatioData";
    
    public static final String CURRENT_USER_LOWER_USERS = "lowerUsers";
    
    public static final String HOSPITAL_SEARCH_KEYWORD = "hospitalKeyword";
    public static final String HOSPITAL_SEARCH_RESULT = "searchedHospitals";
    public static final String HOSPITAL_SPLITCHAT_1 = ",";
    public static final String HOSPITAL_SPLITCHAT_2 = " ";
    
    public static final String VACANT_USER_CODE="#N/A";
    
    public static final String COLLECT_HOMEDATA_DATAID = "dataId";
    
    public static final int ASSESSED_HOS_NUM_PED = 920;
    public static final int ASSESSED_HOS_NUM_RES = 398;
    public static final int ASSESSED_HOS_NUM_CHE = 304;
    
    public static final String lastWeekDuration = DateUtils.getWeeklyDurationYYYYMMDD(new Date());
    public static final String last2WeekDuration = DateUtils.getWeeklyDurationYYYYMMDD(new Date(new Date().getTime() - 7*24*60*60*1000));
    
    public static final String DRAGON_TYPE_EMERGING="Emerging";
    public static final String DRAGON_TYPE_CORE="Core";
    
    
    public static final StringBuffer SQL_WEEKLY_PED_RATIO_DATA_SELECT_PED 
    	= new StringBuffer("select lastweekdata.pnum ")
    	    .append(" , ROUND((lastweekdata.pnum - last2weekdata.pnum) / last2weekdata.pnum,2) as pnumRatio ")
    	    .append(" , lastweekdata.lsnum ")
    	    .append(" , ROUND((lastweekdata.lsnum - last2weekdata.lsnum) / last2weekdata.lsnum,2) as lsnumRatio ")
    	    .append(" , lastweekdata.inRate ")
    	    .append(" , ROUND((lastweekdata.inRate - last2weekdata.inRate),2) as inRateRatio ")
    	    .append(" , lastweekdata.whRate ")
    	    .append(" , ROUND((lastweekdata.whRate - last2weekdata.whRate),2) as whRateRatio ")
    	    .append(" , lastweekdata.averageDose ")
    	    .append(" , ROUND((lastweekdata.averageDose - last2weekdata.averageDose ) / last2weekdata.averageDose,2) as averageDoseRatio ")
    	    .append(" , lastweekdata.hmgRate as hmgRate ")
    	    .append(" , ROUND((lastweekdata.hmgRate - last2weekdata.hmgRate ),2) as hmgRateRatio ")
    	    .append(" , lastweekdata.omgRate as omgRate ")
    	    .append(" , ROUND((lastweekdata.omgRate - last2weekdata.omgRate ),2) as omgRateRatio ")
    	    .append(" , lastweekdata.tmgRate as tmgRate ")
    	    .append(" , ROUND((lastweekdata.tmgRate - last2weekdata.tmgRate ),2) as tmgRateRatio ")
    	    .append(" , lastweekdata.fmgRate as fmgRate ")
    	    .append(" , ROUND((lastweekdata.fmgRate - last2weekdata.fmgRate ),2) as fmgRateRatio ")
    	    .append(" , lastweekdata.whbwnum ")
    	    .append(" , ROUND((lastweekdata.whbwnum - last2weekdata.whbwnum) / last2weekdata.whbwnum,2) as whbwNumRatio ")
    	    .append(" , lastweekdata.blRate ")
    	    .append(" , ROUND((lastweekdata.blRate - last2weekdata.blRate),2) as blRateRatio ");
    
    public static final StringBuffer SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED 
    	= new StringBuffer(" 	IFNULL(sum(lastweek.pnum),0) as pnum, ")
				    .append("	IFNULL(sum(lastweek.lsnum),0) as lsnum, ")
				    .append("	IFNULL(sum(lastweek.lsnum),0) / IFNULL(sum(lastweek.pnum),0) as whRate, ")
				    .append("	IFNULL(sum(least(lastweek.innum,3)),0) / (count(1)*3) as inRate, ")
				    .append("	IFNULL(sum(lastweek.averageDose*lastweek.lsnum)/sum(lastweek.lsnum),0) as averageDose, ")
				    .append("	IFNULL(sum(lastweek.hmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as hmgRate, ")
				    .append("	IFNULL(sum(lastweek.omgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as omgRate, ")
				    .append("	IFNULL(sum(lastweek.tmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as tmgRate, ")
				    .append("	IFNULL(sum(lastweek.fmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as fmgRate, ")
				    .append("	IFNULL(sum(lastweek.whbwnum),0) as whbwnum, ")
				    .append("	IFNULL(sum(lastweek.whbwnum) / ")
				    .append("			sum( ")
				    .append("	 		case when lastweek.whbwnum != 0 then lastweek.lsnum ")
				    .append("	 			else 0 ")
				    .append("	 		end )")
				    .append("	,0) as blRate ")
				    .append("	from tbl_pediatrics_data_weekly lastweek, tbl_hospital h ")
				    .append("   where lastweek.hospitalCode = h.code ")
    				.append("   and lastweek.duration = '").append(lastWeekDuration).append("' ");
    
    public static final StringBuffer SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED 
		= new StringBuffer("	IFNULL(sum(last2week.pnum),0) as pnum, ")
				    .append("	IFNULL(sum(last2week.lsnum),0) as lsnum, ")
				    .append("	IFNULL(sum(last2week.lsnum),0) / IFNULL(sum(last2week.pnum),0) as whRate, ")
				    .append("	IFNULL(sum(least(last2week.innum,3)),0) / (count(1)*3) as inRate, ")
				    .append("	IFNULL(sum(last2week.averageDose*last2week.lsnum)/sum(last2week.lsnum),0) as averageDose, ")
				    .append("	IFNULL(sum(last2week.hmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as hmgRate, ")
				    .append("	IFNULL(sum(last2week.omgRate*last2week.lsnum)/sum(last2week.lsnum),0) as omgRate, ")
				    .append("	IFNULL(sum(last2week.tmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as tmgRate, ")
				    .append("	IFNULL(sum(last2week.fmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as fmgRate, ")
				    .append("	IFNULL(sum(last2week.whbwnum),0) as whbwnum, ")
				    .append("	IFNULL(sum(last2week.whbwnum) / ")
				    .append("			sum( ")
				    .append("	 		case when last2week.whbwnum != 0 then last2week.lsnum ")
				    .append("	 			else 0 ")
				    .append("	 		end )")
				    .append("	,0) as blRate ")
				    .append("	from tbl_pediatrics_data_weekly last2week, tbl_hospital h ")
				    .append("   where last2week.hospitalCode = h.code ")
    				.append("   and last2week.duration = '").append(last2WeekDuration).append("' ");
    
    public static final StringBuffer SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_PED 
        = new StringBuffer(" 	IFNULL(lastweek.pnum,0) as pnum, ")
                    .append("	IFNULL(lastweek.lsnum,0) as lsnum, ")
                    .append("	IFNULL(IFNULL(lastweek.lsnum,0) / IFNULL(lastweek.pnum,0),0) as whRate, ")
                    .append("	IFNULL(least(lastweek.innum,3),0) / 3 as inRate, ")
                    .append("	IFNULL(lastweek.averageDose,0) as averageDose, ")
                    .append("	IFNULL(lastweek.hmgRate,0) as hmgRate, ")
                    .append("	IFNULL(lastweek.omgRate,0) as omgRate, ")
                    .append("	IFNULL(lastweek.tmgRate,0) as tmgRate, ")
                    .append("	IFNULL(lastweek.fmgRate,0) as fmgRate, ")
                    .append("	IFNULL(lastweek.whbwnum,0) as whbwnum ")
                    .append("	from ( select * from tbl_pediatrics_data_weekly  where duration = '").append(lastWeekDuration).append("' ) lastweek ");
    
    public static final StringBuffer SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_PED 
        = new StringBuffer("	IFNULL(last2week.pnum,0) as pnum, ")
                    .append("	IFNULL(last2week.lsnum,0) as lsnum, ")
                    .append("	IFNULL(last2week.lsnum,0) / IFNULL(last2week.pnum,0) as whRate, ")
                    .append("	IFNULL(least(last2week.innum,3),0) / 3 as inRate, ")
                    .append("	IFNULL(last2week.averageDose,0) as averageDose, ")
                    .append("	IFNULL(last2week.hmgRate,0) as hmgRate, ")
                    .append("	IFNULL(last2week.omgRate,0) as omgRate, ")
                    .append("	IFNULL(last2week.tmgRate,0) as tmgRate, ")
                    .append("	IFNULL(last2week.fmgRate,0) as fmgRate, ")
                    .append("	IFNULL(last2week.whbwnum,0) as whbwnum ")
                    .append("	from ( select * from tbl_pediatrics_data_weekly where duration = '").append(last2WeekDuration).append("' ) last2week ");
    
    public static final StringBuffer SQL_WEEKLY_PED_RATIO_DATA_SELECT_RES 
		    = new StringBuffer("select lastweekdata.pnum ")
				    .append(" , ROUND((lastweekdata.pnum - last2weekdata.pnum) / last2weekdata.pnum,2) as pnumRatio ")
				    .append(" , lastweekdata.lsnum ")
				    .append(" , ROUND((lastweekdata.lsnum - last2weekdata.lsnum) / last2weekdata.lsnum,2) as lsnumRatio ")
				    .append(" , lastweekdata.inRate ")
				    .append(" , ROUND((lastweekdata.inRate - last2weekdata.inRate),2) as inRateRatio ")
				    .append(" , lastweekdata.whRate ")
				    .append(" , ROUND((lastweekdata.whRate - last2weekdata.whRate),2) as whRateRatio ")
				    .append(" , lastweekdata.averageDose ")
				    .append(" , ROUND((lastweekdata.averageDose - last2weekdata.averageDose ) / last2weekdata.averageDose,2) as averageDoseRatio ")
				    .append(" , lastweekdata.omgRate as omgRate ")
				    .append(" , ROUND((lastweekdata.omgRate - last2weekdata.omgRate ),2) as omgRateRatio ")
				    .append(" , lastweekdata.tmgRate as tmgRate ")
				    .append(" , ROUND((lastweekdata.tmgRate - last2weekdata.tmgRate ),2) as tmgRateRatio ")
				    .append(" , lastweekdata.thmgRate as thmgRate ")
				    .append(" , ROUND((lastweekdata.thmgRate - last2weekdata.thmgRate ),2) as thmgRateRatio ")
				    .append(" , lastweekdata.fmgRate as fmgRate ")
				    .append(" , ROUND((lastweekdata.fmgRate - last2weekdata.fmgRate ),2) as fmgRateRatio ")
				    .append(" , lastweekdata.smgRate as smgRate ")
				    .append(" , ROUND((lastweekdata.smgRate - last2weekdata.smgRate ),2) as smgRateRatio ")
				    .append(" , lastweekdata.emgRate as emgRate ")
				    .append(" , ROUND((lastweekdata.emgRate - last2weekdata.emgRate ),2) as emgRateRatio ");
    
    public static final StringBuffer SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES 
		    = new StringBuffer(" 	IFNULL(sum(lastweek.pnum),0) as pnum, ")
				    .append("	IFNULL(sum(lastweek.lsnum),0) as lsnum, ")
				    .append("	IFNULL(sum(lastweek.lsnum),0) / IFNULL(sum(lastweek.pnum),0) as whRate, ")
				    .append("	IFNULL(sum(least(lastweek.innum,3)),0) / (count(1)*3) as inRate, ")
				    .append("	IFNULL(sum(lastweek.averageDose*lastweek.lsnum)/sum(lastweek.lsnum),0) as averageDose, ")
				    .append("	IFNULL(sum(lastweek.omgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as omgRate, ")
				    .append("	IFNULL(sum(lastweek.tmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as tmgRate, ")
				    .append("	IFNULL(sum(lastweek.thmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as thmgRate, ")
				    .append("	IFNULL(sum(lastweek.fmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as fmgRate, ")
				    .append("	IFNULL(sum(lastweek.smgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as smgRate, ")
				    .append("	IFNULL(sum(lastweek.emgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as emgRate ")
				    .append("	from tbl_respirology_data_weekly lastweek, tbl_hospital h ")
				    .append("   where lastweek.hospitalCode = h.code ")
				    .append("	and lastweek.duration = '").append(lastWeekDuration).append("' ")
				    .append(" 	and h.isResAssessed='1' ");
    public static final StringBuffer SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES 
		    = new StringBuffer("	IFNULL(sum(last2week.pnum),0) as pnum, ")
				    .append("	IFNULL(sum(last2week.lsnum),0) as lsnum, ")
				    .append("	IFNULL(sum(last2week.lsnum),0) / IFNULL(sum(last2week.pnum),0) as whRate, ")
				    .append("	IFNULL(sum(least(last2week.innum,3)),0) / (count(1)*3) as inRate, ")
				    .append("	IFNULL(sum(last2week.averageDose*last2week.lsnum)/sum(last2week.lsnum),0) as averageDose, ")
				    .append("	IFNULL(sum(last2week.omgRate*last2week.lsnum)/sum(last2week.lsnum),0) as omgRate, ")
				    .append("	IFNULL(sum(last2week.tmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as tmgRate, ")
				    .append("	IFNULL(sum(last2week.thmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as thmgRate, ")
				    .append("	IFNULL(sum(last2week.fmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as fmgRate, ")
				    .append("	IFNULL(sum(last2week.smgRate*last2week.lsnum)/sum(last2week.lsnum),0) as smgRate, ")
				    .append("	IFNULL(sum(last2week.emgRate*last2week.lsnum)/sum(last2week.lsnum),0) as emgRate ")
				    .append("	from tbl_respirology_data_weekly last2week, tbl_hospital h ")
				    .append("   where last2week.hospitalCode = h.code ")
				    .append("	and last2week.duration = '").append(last2WeekDuration).append("' ")
				    .append(" 	and h.isResAssessed='1' ");
    
    public static final StringBuffer SQL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE 
            = new StringBuffer(" 	IFNULL(sum(lastweek.pnum),0) as pnum, ")
            .append("	IFNULL(sum(lastweek.lsnum),0) as lsnum, ")
            .append("	IFNULL(sum(lastweek.lsnum),0) / IFNULL(sum(lastweek.pnum),0) as whRate, ")
            .append("	IFNULL(sum(least(lastweek.innum,3)),0) / (count(1)*3) as inRate, ")
            .append("	IFNULL(sum(lastweek.averageDose*lastweek.lsnum)/sum(lastweek.lsnum),0) as averageDose, ")
            .append("	IFNULL(sum(lastweek.omgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as omgRate, ")
            .append("	IFNULL(sum(lastweek.tmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as tmgRate, ")
            .append("	IFNULL(sum(lastweek.thmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as thmgRate, ")
            .append("	IFNULL(sum(lastweek.fmgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as fmgRate, ")
            .append("	IFNULL(sum(lastweek.smgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as smgRate, ")
            .append("	IFNULL(sum(lastweek.emgRate*lastweek.lsnum)/sum(lastweek.lsnum),0) as emgRate ")
            .append("	from ( select * from tbl_chestSurgery_data_weekly where duration = '").append(lastWeekDuration).append("' ) lastweek, tbl_hospital h ")
            .append("   where lastweek.hospitalCode = h.code ");
    public static final StringBuffer SQL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE 
            = new StringBuffer("	IFNULL(sum(last2week.pnum),0) as pnum, ")
            .append("	IFNULL(sum(last2week.lsnum),0) as lsnum, ")
            .append("	IFNULL(sum(last2week.lsnum),0) / IFNULL(sum(last2week.pnum),0) as whRate, ")
            .append("	IFNULL(sum(least(last2week.innum,3)),0) / (count(1)*3) as inRate, ")
            .append("	IFNULL(sum(last2week.averageDose*last2week.lsnum)/sum(last2week.lsnum),0) as averageDose, ")
            .append("	IFNULL(sum(last2week.omgRate*last2week.lsnum)/sum(last2week.lsnum),0) as omgRate, ")
            .append("	IFNULL(sum(last2week.tmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as tmgRate, ")
            .append("	IFNULL(sum(last2week.thmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as thmgRate, ")
            .append("	IFNULL(sum(last2week.fmgRate*last2week.lsnum)/sum(last2week.lsnum),0) as fmgRate, ")
            .append("	IFNULL(sum(last2week.smgRate*last2week.lsnum)/sum(last2week.lsnum),0) as smgRate, ")
            .append("	IFNULL(sum(last2week.emgRate*last2week.lsnum)/sum(last2week.lsnum),0) as emgRate ")
            .append("	from ( select * from tbl_chestSurgery_data_weekly where duration = '").append(last2WeekDuration).append("' ) last2week, tbl_hospital h ")
            .append("   where last2week.hospitalCode = h.code ");
    
    public static final StringBuffer SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_RES 
            = new StringBuffer(" 	IFNULL(lastweek.pnum,0) as pnum, ")
                    .append("	IFNULL(lastweek.lsnum,0) as lsnum, ")
                    .append("	IFNULL(IFNULL(lastweek.lsnum,0) / IFNULL(lastweek.pnum,0),0) as whRate, ")
                    .append("	IFNULL(least(lastweek.innum,3),0) / 3 as inRate, ")
                    .append("	IFNULL(lastweek.averageDose,0) as averageDose, ")
                    .append("	IFNULL(lastweek.omgRate,0) as omgRate, ")
                    .append("	IFNULL(lastweek.tmgRate,0) as tmgRate, ")
                    .append("	IFNULL(lastweek.thmgRate,0) as thmgRate, ")
                    .append("	IFNULL(lastweek.fmgRate,0) as fmgRate, ")
                    .append("	IFNULL(lastweek.smgRate,0) as smgRate, ")
                    .append("	IFNULL(lastweek.emgRate,0) as emgRate ")
                    .append("	from tbl_respirology_data_weekly lastweek ");
    
    public static final StringBuffer SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_RES 
            = new StringBuffer("	IFNULL(last2week.pnum,0) as pnum, ")
                    .append("	IFNULL(last2week.lsnum,0) as lsnum, ")
                    .append("	IFNULL(IFNULL(last2week.lsnum,0) / IFNULL(last2week.pnum,0),0) as whRate, ")
                    .append("	IFNULL(least(last2week.innum,3),0) / 3 as inRate, ")
                    .append("	IFNULL(last2week.averageDose,0) as averageDose, ")
                    .append("	IFNULL(last2week.omgRate,0) as omgRate, ")
                    .append("	IFNULL(last2week.tmgRate,0) as tmgRate, ")
                    .append("	IFNULL(last2week.thmgRate,0) as thmgRate, ")
                    .append("	IFNULL(last2week.fmgRate,0) as fmgRate, ")
                    .append("	IFNULL(last2week.smgRate,0) as smgRate, ")
                    .append("	IFNULL(last2week.emgRate,0) as emgRate ")
                    .append("	from tbl_respirology_data_weekly last2week ");
    
    public static final StringBuffer SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LASTWEEK_SELECT_CHE 
            = new StringBuffer(" 	IFNULL(lastweek.pnum,0) as pnum, ")
            .append("	IFNULL(lastweek.lsnum,0) as lsnum, ")
            .append("	IFNULL(IFNULL(lastweek.lsnum,0) / IFNULL(lastweek.pnum,0),0) as whRate, ")
            .append("	IFNULL(least(lastweek.innum,3),0) / 3 as inRate, ")
            .append("	IFNULL(lastweek.averageDose,0) as averageDose, ")
            .append("	IFNULL(lastweek.omgRate,0) as omgRate, ")
            .append("	IFNULL(lastweek.tmgRate,0) as tmgRate, ")
            .append("	IFNULL(lastweek.thmgRate,0) as thmgRate, ")
            .append("	IFNULL(lastweek.fmgRate,0) as fmgRate, ")
            .append("	IFNULL(lastweek.smgRate,0) as smgRate, ")
            .append("	IFNULL(lastweek.emgRate,0) as emgRate ")
            .append("	from ( select * from tbl_chestSurgery_data_weekly where duration = '").append(lastWeekDuration).append("' ) lastweek ");
    
    public static final StringBuffer SQL_HOSPITAL_WEEKLY_PED_RATIO_DATA_LAST2WEEK_SELECT_CHE 
            = new StringBuffer("	IFNULL(last2week.pnum,0) as pnum, ")
            .append("	IFNULL(last2week.lsnum,0) as lsnum, ")
            .append("	IFNULL(IFNULL(last2week.lsnum,0) / IFNULL(last2week.pnum,0),0) as whRate, ")
            .append("	IFNULL(least(last2week.innum,3),0) / 3 as inRate, ")
            .append("	IFNULL(last2week.averageDose,0) as averageDose, ")
            .append("	IFNULL(last2week.omgRate,0) as omgRate, ")
            .append("	IFNULL(last2week.tmgRate,0) as tmgRate, ")
            .append("	IFNULL(last2week.thmgRate,0) as thmgRate, ")
            .append("	IFNULL(last2week.fmgRate,0) as fmgRate, ")
            .append("	IFNULL(last2week.smgRate,0) as smgRate, ")
            .append("	IFNULL(last2week.emgRate,0) as emgRate ")
            .append("	from ( select * from tbl_chestSurgery_data_weekly where duration = '").append(last2WeekDuration).append("' ) last2week ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_SELECT
            = new StringBuffer( "select IFNULL(lastMonthData.pedEmernum,0) as pedEmernum " )
                    .append(", IFNULL(lastMonthData.pedroomnum,0) as pedroomnum ")
                    .append(", IFNULL(lastMonthData.resnum,0) as resnum ")
                    .append(", IFNULL(lastMonthData.othernum,0) as othernum ")
                    .append(", ROUND(IFNULL(lastMonthData.pedEmernum/lastMonthData.totalnum,0),2) as pedemernumrate ")
                    .append(", ROUND(IFNULL(lastMonthData.pedroomnum/lastMonthData.totalnum,0),2) as pedroomnumrate ")
                    .append(", ROUND(IFNULL(lastMonthData.resnum/lastMonthData.totalnum,0),2) as resnumrate ")
                    .append(", ROUND(IFNULL(lastMonthData.othernum/lastMonthData.totalnum,0),2) as othernumrate ")
                    .append(", ROUND(IFNULL((lastMonthData.pedEmernum-last2MonthData.pedEmernum)/last2MonthData.pedEmernum,0),2) as pedemernumratio ")
                    .append(", ROUND(IFNULL((lastMonthData.pedroomnum-last2MonthData.pedroomnum)/last2MonthData.pedroomnum,0),2) as pedroomnumratio ")
                    .append(", ROUND(IFNULL((lastMonthData.resnum-last2MonthData.resnum)/last2MonthData.resnum,0),2) as resnumratio ")
                    .append(", ROUND(IFNULL((lastMonthData.othernum-last2MonthData.othernum)/last2MonthData.othernum,0),2) as othernumratio ")
                    .append(", ROUND(IFNULL(lastMonthData.pedEmernum/lastMonthData.totalnum - last2MonthData.pedEmernum/last2MonthData.totalnum,0),2) as pedemernumrateratio ")
                    .append(", ROUND(IFNULL(lastMonthData.pedroomnum/lastMonthData.totalnum - last2MonthData.pedroomnum/last2MonthData.totalnum,0),2) as pedroomnumrateratio ")
                    .append(", ROUND(IFNULL(lastMonthData.resnum/lastMonthData.totalnum - last2MonthData.resnum/last2MonthData.totalnum,0),2) as resnumrateratio ")
                    .append(", ROUND(IFNULL(lastMonthData.othernum/lastMonthData.totalnum - last2MonthData.othernum/last2MonthData.totalnum,0),2) as othernumrateratio ")
                    .append(", IFNULL(lastMonthData.totalnum,0) as totalnum ")
                    .append(", ROUND(IFNULL((lastMonthData.totalnum - last2MonthData.totalnum)/last2MonthData.totalnum,0),2) as totalnumratio ")
                    .append(", IFNULL(lastMonthData.innum,0) as innum ")
                    .append(", ROUND(IFNULL((lastMonthData.innum-last2MonthData.innum)/last2MonthData.innum,0),2) as innumratio ")
    				.append(", IFNULL(lastMonthData.hosnum,0) as hosnum ")
    				.append(", ROUND(IFNULL((lastMonthData.hosnum-last2MonthData.hosnum)/last2MonthData.hosnum,0),2) as hosnumratio ")
    				.append(", ROUND(IFNULL(lastMonthData.innum,0)/IFNULL(lastMonthData.hosnum,0),2) as inrate ")
    				.append(", ROUND(IFNULL(lastMonthData.innum/lastMonthData.hosnum - last2MonthData.innum/last2MonthData.hosnum,0),2) as inrateratio ")
    				
    				.append(", IFNULL(lastMonthData.ped_emer_drugstore,0) as ped_emer_drugstore ")
                    .append(", IFNULL(lastMonthData.ped_emer_wh,0) as ped_emer_wh ")
                    .append(", IFNULL(lastMonthData.ped_room_drugstore,0) as ped_room_drugstore ")
                    .append(", IFNULL(lastMonthData.ped_room_drugstore_wh,0) as ped_room_drugstore_wh ")
                    .append(", IFNULL(lastMonthData.res_clinic,0) as res_clinic ")
                    .append(", IFNULL(lastMonthData.res_room,0) as res_room ")
                    .append(", IFNULL(lastMonthData.home_wh,0) as home_wh ")
                    //比例
                    .append(", ROUND(IFNULL(lastMonthData.ped_emer_drugstore/lastMonthData.totalnum,0),2) as pedEmerDtRate ")
                    .append(", ROUND(IFNULL(lastMonthData.ped_emer_wh/lastMonthData.totalnum,0),2) as pedEmerWhRate ")
                    .append(", ROUND(IFNULL(lastMonthData.ped_room_drugstore/lastMonthData.totalnum,0),2) as pedRoomDtRate ")
                    .append(", ROUND(IFNULL(lastMonthData.ped_room_drugstore_wh/lastMonthData.totalnum,0),2) as pedRoomDtWhRate ")
                    .append(", ROUND(IFNULL(lastMonthData.res_clinic/lastMonthData.totalnum,0),2) as resClinicRate ")
                    .append(", ROUND(IFNULL(lastMonthData.res_room/lastMonthData.totalnum,0),2) as resRoomRate ")
                    .append(", ROUND(IFNULL(lastMonthData.home_wh/lastMonthData.totalnum,0),2) as homeWhRate ")
                    //环比
                    .append(", ROUND(IFNULL((lastMonthData.ped_emer_drugstore-last2MonthData.ped_emer_drugstore)/last2MonthData.ped_emer_drugstore,0),2) as pedEmerDtRatio ")
                    .append(", ROUND(IFNULL((lastMonthData.ped_emer_wh-last2MonthData.ped_emer_wh)/last2MonthData.ped_emer_wh,0),2) as pedEmerWhRatio ")
                    .append(", ROUND(IFNULL((lastMonthData.ped_room_drugstore-last2MonthData.ped_room_drugstore)/last2MonthData.ped_room_drugstore,0),2) as pedRoomDtRatio ")
                    .append(", ROUND(IFNULL((lastMonthData.ped_room_drugstore_wh-last2MonthData.ped_room_drugstore_wh)/last2MonthData.ped_room_drugstore_wh,0),2) as pedRoomDtWhRatio ")
                    .append(", ROUND(IFNULL((lastMonthData.res_clinic-last2MonthData.res_clinic)/last2MonthData.res_clinic,0),2) as resClinicRatio ")
                    .append(", ROUND(IFNULL((lastMonthData.res_room-last2MonthData.res_room)/last2MonthData.res_room,0),2) as resRoomRatio ")
                    .append(", ROUND(IFNULL((lastMonthData.home_wh-last2MonthData.home_wh)/last2MonthData.home_wh,0),2) as homeWhRatio ")
                    //比例环比
                    .append(", ROUND(IFNULL(lastMonthData.ped_emer_drugstore/lastMonthData.totalnum - last2MonthData.ped_emer_drugstore/last2MonthData.totalnum,0),2) as pedEmerDtRateRatio ")
                    .append(", ROUND(IFNULL(lastMonthData.ped_emer_wh/lastMonthData.totalnum - last2MonthData.ped_emer_wh/last2MonthData.totalnum,0),2) as pedEmerWhRateRatio ")
                    .append(", ROUND(IFNULL(lastMonthData.ped_room_drugstore/lastMonthData.totalnum - last2MonthData.ped_room_drugstore/last2MonthData.totalnum,0),2) as pedRoomDtRateRatio ")
                    .append(", ROUND(IFNULL(lastMonthData.ped_room_drugstore_wh/lastMonthData.totalnum - last2MonthData.ped_room_drugstore_wh/last2MonthData.totalnum,0),2) as pedRoomDtWhRateRatio ")
                    .append(", ROUND(IFNULL(lastMonthData.res_clinic/lastMonthData.totalnum - last2MonthData.res_clinic/last2MonthData.totalnum,0),2) as resClinicRateRatio ")
                    .append(", ROUND(IFNULL(lastMonthData.res_room/lastMonthData.totalnum - last2MonthData.res_room/last2MonthData.totalnum,0),2) as resRoomRateRatio ")
    				.append(", ROUND(IFNULL(lastMonthData.home_wh/lastMonthData.totalnum - last2MonthData.home_wh/last2MonthData.totalnum,0),2) as homeWhRateRatio ");
    
    private static final StringBuffer SQL_MONTHLY_NEW_COLUMN_LAST_SELECT_1 
    		= new StringBuffer(", lastMonth.ped_emer_drugstore ")
    				.append(", lastMonth.ped_emer_wh ")
    				.append(", lastMonth.ped_room_drugstore ")
    				.append(", lastMonth.ped_room_drugstore_wh ")
    				.append(", lastMonth.res_clinic ")
    				.append(", lastMonth.res_room ")
    				.append(", lastMonth.home_wh ");
    private static final StringBuffer SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_1 
		    = new StringBuffer(", last2Month.ped_emer_drugstore ")
				    .append(", last2Month.ped_emer_wh ")
				    .append(", last2Month.ped_room_drugstore ")
				    .append(", last2Month.ped_room_drugstore_wh ")
				    .append(", last2Month.res_clinic ")
				    .append(", last2Month.res_room ")
				    .append(", last2Month.home_wh ");
    
    public static final StringBuffer SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner 
    		= new StringBuffer("       , IFNULL(sum(ped_emer_drugstore),0) as ped_emer_drugstore ")
			    .append("       , IFNULL(sum(ped_emer_wh),0) as ped_emer_wh ")
			    .append("       , IFNULL(sum(ped_room_drugstore),0) as ped_room_drugstore ")
			    .append("       , IFNULL(sum(ped_room_drugstore_wh),0) as ped_room_drugstore_wh ")
			    .append("       , IFNULL(sum(res_clinic),0) as res_clinic ")
			    .append("       , IFNULL(sum(res_room),0) as res_room ")
			    .append("       , IFNULL(sum(home_wh),0) as home_wh ")
			    .append("       , sum(IFNULL(ped_emer_wh,0)+IFNULL(ped_room_drugstore_wh,0)+IFNULL(home_wh,0)+IFNULL(res_clinic,0)+IFNULL(res_room,0)+IFNULL(other,0)) as totalnum");
    
    public static final StringBuffer SQL_MONTHLY_NEW_COLUMN_SELECT
		    = new StringBuffer(", ped_emer_drugstore ")
				.append(", ped_emer_wh ")
				.append(", ped_room_drugstore ")
				.append(", ped_room_drugstore_wh ")
				.append(", res_clinic ")
				.append(", res_room ")
				.append(", home_wh ");
    
    
    public static final StringBuffer SQL_MONTHLY_NEW_COLUMN_RATE_SELECT
		    = new StringBuffer(", ROUND(IFNULL(chooseMonth.ped_emer_drugstore/chooseMonth.totalnum,0),2) as pedEmerDtRate ")
			    .append(", ROUND(IFNULL(chooseMonth.ped_emer_wh/chooseMonth.totalnum,0),2) as pedEmerWhRate ")
			    .append(", ROUND(IFNULL(chooseMonth.ped_room_drugstore/chooseMonth.totalnum,0),2) as pedRoomDtRate ")
			    .append(", ROUND(IFNULL(chooseMonth.ped_room_drugstore_wh/chooseMonth.totalnum,0),2) as pedRoomDtWhRate ")
			    .append(", ROUND(IFNULL(chooseMonth.res_clinic/chooseMonth.totalnum,0),2) as resClinicRate ")
			    .append(", ROUND(IFNULL(chooseMonth.res_room/chooseMonth.totalnum,0),2) as resRoomRate ")
			    .append(", ROUND(IFNULL(chooseMonth.home_wh/chooseMonth.totalnum,0),2) as homeWhRate ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LASTMONTH_SELECT_REP
    		= new StringBuffer( "select lastMonth.pedEmernum ,lastMonth.pedroomnum ,lastMonth.resnum ,lastMonth.othernum ,lastMonth.totalnum " )
				    .append(", lastMonth.innum as innum ")
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST_SELECT_1)
					.append(", (select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.dsmCode = u.superior and h.saleCode = u.userCode and h.isMonthlyAssessed='1') as hosnum " )
					.append(", u.userCode as saleCode, u.superior as dsmCode, u.name as saleName , u.region as rsmRegion, u.regionCenter as region " )
				    .append("    from (")
				    .append("       select h.saleCode as saleCode ")
				    .append("       , h.dsmCode ")
				    .append("       , h.rsmRegion ")
				    .append("       , h.region ")
				    .append("       , sum(pedEmernum) as pedEmernum ")
				    .append("       , sum(pedroomnum) as pedroomnum ")
				    .append("       , sum(resnum) as resnum ")
				    .append("       , sum(other) as othernum ")
				    //月报拆分
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
				    .append("       , count(1) as innum ")
				    .append("       from tbl_month_data md, tbl_hospital h ")
				    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 MONTH),'%Y-%m') " )
				    .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
				    .append("       and h.dsmCode=? ")
				    .append("       group by h.dsmCode, h.saleCode ")
				    .append("   ) lastMonth ")
				    .append("   right join tbl_userinfo u on u.userCode = lastMonth.saleCode and u.superior = lastMonth.dsmCode")
				    .append("   where u.superior = ? ")
				    .append("   and u.level='REP' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_REP
    		= new StringBuffer( "select last2Month.pedEmernum ,last2Month.pedroomnum ,last2Month.resnum ,last2Month.othernum ,last2Month.totalnum " )
				    .append(", last2Month.innum as innum ")
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_1)
					.append(", (select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.dsmCode = u.superior and h.saleCode = u.userCode and h.isMonthlyAssessed='1') as hosnum " )
					.append(", u.userCode as saleCode, u.superior as dsmCode, u.name as saleName , u.region as rsmRegion, u.regionCenter as region " )
				    .append("    from (")
				    .append("       select h.saleCode as saleCode ")
				    .append("       , h.dsmCode ")
				    .append("       , h.rsmRegion ")
				    .append("       , h.region ")
				    .append("       , sum(pedEmernum) as pedEmernum ")
				    .append("       , sum(pedroomnum) as pedroomnum ")
				    .append("       , sum(resnum) as resnum ")
				    .append("       , sum(other) as othernum ")
				    //月报拆分
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
				    .append("       , count(1) as innum ")
				    .append("       from tbl_month_data md, tbl_hospital h ")
				    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m') " )
				    .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
				    .append("       and h.dsmCode=? ")
				    .append("       group by h.dsmCode, h.saleCode ")
				    .append("   ) last2Month ")
				    .append("   right join tbl_userinfo u on u.userCode = last2Month.saleCode and u.superior = last2Month.dsmCode")
				    .append("   where u.superior = ? ")
				    .append("   and u.level='REP' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LASTMONTH_SELECT_DSM
            = new StringBuffer( "select lastMonth.pedEmernum ,lastMonth.pedroomnum ,lastMonth.resnum ,lastMonth.othernum ,lastMonth.totalnum " )
				    .append(", lastMonth.innum as innum ")
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST_SELECT_1)
					.append(", (select count(1) from tbl_hospital h where h.dsmCode = u.userCode and h.rsmRegion = u.region and h.isMonthlyAssessed='1') as hosnum " )
					.append(", u.region as rsmRegion, u.regionCenter as region, u.name as dsmName " )
				    .append("    from (")
                    .append("       select h.dsmCode ")
                    .append("       , ( select ui.name from tbl_userinfo ui where ui.userCode = h.dsmCode and ui.region = h.rsmRegion and ui.level='DSM' ) as dsmName ")
                    .append("       , h.rsmRegion ")
                    .append("       , h.region ")
                    .append("       , sum(pedEmernum) as pedEmernum ")
                    .append("       , sum(pedroomnum) as pedroomnum ")
                    .append("       , sum(resnum) as resnum ")
                    .append("       , sum(other) as othernum ")
                    //月报拆分
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
                    .append("       , count(1) as innum ")
                    .append("       from tbl_month_data md, tbl_hospital h ")
                    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 MONTH),'%Y-%m') " )
				    .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
                    .append("       and h.rsmRegion=? ")
                    .append("       group by h.dsmCode ")
                    .append("   ) lastMonth ")
                    .append("   right join tbl_userinfo u on u.userCode = lastMonth.dsmCode ")
                    .append("   where u.region = ? ")
                    .append("   and u.level='DSM' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_DSM
            = new StringBuffer( "select last2Month.pedEmernum ,last2Month.pedroomnum ,last2Month.resnum ,last2Month.othernum ,last2Month.totalnum " )
				    .append(", last2Month.innum as innum ")
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_1)
					.append(", (select count(1) from tbl_hospital h where h.dsmCode = u.userCode and h.rsmRegion = u.region and h.isMonthlyAssessed='1') as hosnum " )
					.append(", u.region as rsmRegion, u.regionCenter as region, u.name as dsmName " )
				    .append("    from (")
                    .append("       select h.dsmCode ")
                    .append("       , ( select ui.name from tbl_userinfo ui where ui.userCode = h.dsmCode and ui.region = h.rsmRegion and ui.level='DSM' ) as dsmName ")
                    .append("       , h.rsmRegion ")
                    .append("       , h.region ")
                    .append("       , sum(pedEmernum) as pedEmernum ")
                    .append("       , sum(pedroomnum) as pedroomnum ")
                    .append("       , sum(resnum) as resnum ")
                    .append("       , sum(other) as othernum ")
                    //月报拆分
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
                    .append("       , count(1) as innum ")
                    .append("       from tbl_month_data md, tbl_hospital h ")
                    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m') " )
				    .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
                    .append("       and h.rsmRegion=? ")
                    .append("       group by h.dsmCode ")
                    .append("   ) last2Month ")
                    .append("   right join tbl_userinfo u on u.userCode = last2Month.dsmCode ")
                    .append("   where u.region = ? ")
                    .append("   and u.level='DSM' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LASTMONTH_SELECT_BELONG_RSM
            = new StringBuffer( "select lastMonth.pedEmernum ,lastMonth.pedroomnum ,lastMonth.resnum ,lastMonth.othernum ,lastMonth.totalnum " )
				    .append(", lastMonth.innum as innum ")
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST_SELECT_1)
					.append(", (select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.isMonthlyAssessed='1') as hosnum " )
					.append(", u.region as rsmRegion , u.regionCenter as region " )
				    .append("    from (")
                    .append("       select h.rsmRegion ")
                    .append("       , h.region ")
                    .append("       , sum(pedEmernum) as pedEmernum ")
                    .append("       , sum(pedroomnum) as pedroomnum ")
                    .append("       , sum(resnum) as resnum ")
                    .append("       , sum(other) as othernum ")
                    //月报拆分
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
                    .append("       , count(1) as innum ")
                    .append("       from tbl_month_data md, tbl_hospital h ")
                    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 MONTH),'%Y-%m') " )
				    .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
                    .append("       and h.rsmRegion=? ")
                    .append("       group by h.rsmRegion ")
                    .append("   ) lastMonth ")
                    .append("   right join tbl_userinfo u on u.region = lastMonth.rsmRegion ")
                    .append("   where u.region =? ")
                    .append("   and u.level='RSM' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_BELONG_RSM
    = new StringBuffer( "select last2Month.pedEmernum ,last2Month.pedroomnum ,last2Month.resnum ,last2Month.othernum ,last2Month.totalnum " )
		    .append(", last2Month.innum as innum ")
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_1)
			.append(", (select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.isMonthlyAssessed='1') as hosnum " )
			.append(", u.region as rsmRegion , u.regionCenter as region " )
		    .append("    from (")
            .append("       select h.rsmRegion ")
            .append("       , h.region ")
            .append("       , sum(pedEmernum) as pedEmernum ")
            .append("       , sum(pedroomnum) as pedroomnum ")
            .append("       , sum(resnum) as resnum ")
            .append("       , sum(other) as othernum ")
            //月报拆分
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
            .append("       , count(1) as innum ")
            .append("       from tbl_month_data md, tbl_hospital h ")
            .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m') " )
            .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
            .append("       and h.rsmRegion=? ")
            .append("       group by h.rsmRegion ")
            .append("   ) last2Month ")
            .append("   right join tbl_userinfo u on u.region = last2Month.rsmRegion ")
            .append("   where u.region = ? ")
            .append("   and u.level='RSM' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LASTMONTH_SELECT_RSM
        = new StringBuffer( "select lastMonth.pedEmernum ,lastMonth.pedroomnum ,lastMonth.resnum ,lastMonth.othernum ,lastMonth.totalnum ")
        	.append(", lastMonth.innum as innum ")
        	.append(SQL_MONTHLY_NEW_COLUMN_LAST_SELECT_1)
        	.append(", (select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.isMonthlyAssessed='1') as hosnum " )
        	.append(", u.region as rsmRegion , u.regionCenter as region " )
            .append("    from (")
            .append("       select h.rsmRegion ")
            .append("       , h.region ")
            .append("       , sum(pedEmernum) as pedEmernum ")
            .append("       , sum(pedroomnum) as pedroomnum ")
            .append("       , sum(resnum) as resnum ")
            .append("       , sum(other) as othernum ")
            //月报拆分
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
            .append("       , count(1) as innum ")
            .append("       from tbl_month_data md, tbl_hospital h ")
            .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 MONTH),'%Y-%m') " )
            .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
            .append("       and h.region=? ")
            .append("       group by h.rsmRegion ")
            .append("   ) lastMonth ")
            .append("   right join tbl_userinfo u on u.region = lastMonth.rsmRegion ")
            .append("   where u.regionCenter = ? ")
            .append("   and u.level='RSM' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_RSM
        = new StringBuffer( "select last2Month.pedEmernum ,last2Month.pedroomnum ,last2Month.resnum ,last2Month.othernum ,last2Month.totalnum " )
		    .append(", last2Month.innum as innum ")
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_1)
			.append(", (select count(1) from tbl_hospital h where h.rsmRegion = u.region and h.isMonthlyAssessed='1') as hosnum " )
			.append(", u.region as rsmRegion , u.regionCenter as region " )
    		.append("    from (")
            .append("       select h.rsmRegion ")
            .append("       , h.region ")
            .append("       , sum(pedEmernum) as pedEmernum ")
            .append("       , sum(pedroomnum) as pedroomnum ")
            .append("       , sum(resnum) as resnum ")
            .append("       , sum(other) as othernum ")
            //月报拆分
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
            .append("       , count(1) as innum ")
            .append("       from tbl_month_data md, tbl_hospital h ")
            .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m') " )
            .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
            .append("       and h.region=? ")
            .append("       group by h.rsmRegion ")
            .append("   ) last2Month ")
            .append("   right join tbl_userinfo u on u.region = last2Month.rsmRegion ")
            .append("   where u.regionCenter = ? ")
            .append("   and u.level='RSM' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LASTMONTH_SELECT_BELONG_RSD
            = new StringBuffer( "select lastMonth.pedEmernum ,lastMonth.pedroomnum ,lastMonth.resnum ,lastMonth.othernum ,lastMonth.totalnum " )
				    .append(", lastMonth.innum as innum ")
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST_SELECT_1)
					.append(", (select count(1) from tbl_hospital h where h.region = u.regionCenter and h.isMonthlyAssessed='1') as hosnum " )
					.append(", u.region as rsmRegion , u.regionCenter as region " )
				    .append("    from (")
                    .append("       select h.rsmRegion ")
                    .append("       , h.region ")
                    .append("       , sum(pedEmernum) as pedEmernum ")
                    .append("       , sum(pedroomnum) as pedroomnum ")
                    .append("       , sum(resnum) as resnum ")
                    .append("       , sum(other) as othernum ")
                    //月报拆分
				    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
                    .append("       , count(1) as innum ")
                    .append("       from tbl_month_data md, tbl_hospital h ")
                    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 MONTH),'%Y-%m') " )
				    .append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
                    .append("       and h.region=? ")
                    .append("       group by h.region ")
                    .append("   ) lastMonth ")
                    .append("   right join tbl_userinfo u on u.regionCenter = lastMonth.region ")
                    .append("   where u.regionCenter =? ")
                    .append("   and u.level='RSD' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_BELONG_RSD
    = new StringBuffer( "select last2Month.pedEmernum ,last2Month.pedroomnum ,last2Month.resnum ,last2Month.othernum ,last2Month.totalnum " )
		    .append(", last2Month.innum as innum ")
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_1)
			.append(", (select count(1) from tbl_hospital h where h.region = u.regionCenter and h.isMonthlyAssessed='1') as hosnum " )
			.append(", u.region as rsmRegion , u.regionCenter as region " )
		    .append("    from (")
            .append("       select h.rsmRegion ")
            .append("       , h.region ")
            .append("       , sum(pedEmernum) as pedEmernum ")
            .append("       , sum(pedroomnum) as pedroomnum ")
            .append("       , sum(resnum) as resnum ")
            .append("       , sum(other) as othernum ")
            //月报拆分
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
            .append("       , count(1) as innum ")
            .append("       from tbl_month_data md, tbl_hospital h ")
            .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m') " )
			.append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
            .append("       and h.region=? ")
            .append("       group by h.region ")
            .append("   ) last2Month ")
            .append("   right join tbl_userinfo u on u.regionCenter = last2Month.region ")
            .append("   where u.regionCenter = ? ")
            .append("   and u.level='RSD' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LASTMONTH_SELECT_RSD
    	= new StringBuffer( "select lastMonth.pedEmernum ,lastMonth.pedroomnum ,lastMonth.resnum ,lastMonth.othernum ,lastMonth.totalnum " )
		    .append(", lastMonth.innum as innum ")
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST_SELECT_1)
			.append(", (select count(1) from tbl_hospital h where h.region = u.regionCenter and h.isMonthlyAssessed='1') as hosnum " )
			.append(", u.region as rsmRegion , u.regionCenter as region " )
		    .append("    from (")
		    .append("       select h.rsmRegion ")
		    .append("       , h.region ")
		    .append("       , sum(pedEmernum) as pedEmernum ")
		    .append("       , sum(pedroomnum) as pedroomnum ")
		    .append("       , sum(resnum) as resnum ")
		    .append("       , sum(other) as othernum ")
		    //月报拆分
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
		    .append("       , count(1) as innum ")
		    .append("       from tbl_month_data md, tbl_hospital h ")
		    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 MONTH),'%Y-%m') " )
			.append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
		    .append("       group by h.region ")
		    .append("   ) lastMonth ")
		    .append("   right join tbl_userinfo u on u.regionCenter = lastMonth.region ")
		    .append("   where u.level='RSD' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_RSD
    	= new StringBuffer( "select last2Month.pedEmernum ,last2Month.pedroomnum ,last2Month.resnum ,last2Month.othernum ,last2Month.totalnum " )
		    .append(", last2Month.innum as innum ")
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_1)
			.append(", (select count(1) from tbl_hospital h where h.region = u.regionCenter and h.isMonthlyAssessed='1') as hosnum " )
			.append(", u.region as rsmRegion , u.regionCenter as region " )
		    .append("    from (")
		    .append("       select h.rsmRegion ")
		    .append("       , h.region ")
		    .append("       , sum(pedEmernum) as pedEmernum ")
		    .append("       , sum(pedroomnum) as pedroomnum ")
		    .append("       , sum(resnum) as resnum ")
		    .append("       , sum(other) as othernum ")
		    //月报拆分
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
		    .append("       , count(1) as innum ")
		    .append("       from tbl_month_data md, tbl_hospital h ")
		    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m') " )
			.append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ")
		    .append("       group by h.region ")
		    .append("   ) last2Month ")
		    .append("   right join tbl_userinfo u on u.regionCenter = last2Month.region ")
		    .append("   where u.level='RSD' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LASTMONTH_SELECT_BELONG_COUNTORY
    	= new StringBuffer(" select IFNULL(sum(pedEmernum),0) as pedEmernum ")
		    .append("       , IFNULL(sum(pedroomnum),0) as pedroomnum ")
		    .append("       , IFNULL(sum(resnum),0) as resnum ")
		    .append("       , IFNULL(sum(other),0) as othernum ")
		    //月报拆分
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
		    .append("       , count(1) as innum ")
			.append("       , (select count(1) from tbl_hospital h where h.isMonthlyAssessed='1') as hosnum ")
		    .append("       from tbl_month_data md, tbl_hospital h ")
		    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 1 MONTH),'%Y-%m') " )
			.append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ");
    
    public static final StringBuffer SQL_MONTHLY_RATIO_LAST2MONTH_SELECT_BELONG_COUNTORY
    	= new StringBuffer(" select IFNULL(sum(pedEmernum),0) as pedEmernum ")
		    .append("       , IFNULL(sum(pedroomnum),0) as pedroomnum ")
		    .append("       , IFNULL(sum(resnum),0) as resnum ")
		    .append("       , IFNULL(sum(other),0) as othernum ")
		    //月报拆分
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner)
		    .append("       , count(1) as innum ")
			.append("       , (select count(1) from tbl_hospital h where h.isMonthlyAssessed='1') as hosnum ")
		    .append("       from tbl_month_data md, tbl_hospital h ")
		    .append("		where md.countMonth = DATE_FORMAT(DATE_SUB(now(), INTERVAL 2 MONTH),'%Y-%m') " )
			.append("       and md.hospitalCode = h.code and h.isMonthlyAssessed='1' ");
    
    public static final StringBuffer SQL_MONTHLY_12_SELECT
    	= new StringBuffer(" , sum(md.pedEmernum) as pedEmernum ")
		    .append(" , sum(md.pedroomnum) as pedroomnum")
		    .append(" , sum(md.resnum) as resnum")
		    .append(" , sum(md.other) as other")
		    //月报拆分
		    .append(SQL_MONTHLY_NEW_COLUMN_LAST2_SELECT_inner);
    
    public static final StringBuffer SQL_MONTHLY_12_GROUP
    	= new StringBuffer(" group by dataMonth ")
    		.append(" order by dataMonth desc")
    		.append(" limit 0,12 ");
    
    public static final StringBuffer SQL_WEEKLY_HOS_SALES_DATA 
        = new StringBuffer("select lastweekdata.hospitalCode, lastweekdata.hospitalName ")
            .append(" , lastweekdata.pnum  ")
            .append(" , ROUND((lastweekdata.pnum - last2weekdata.pnum) / last2weekdata.pnum,2) as pnumRatio ")
            .append(" , lastweekdata.lsnum ")
            .append(" , ROUND((lastweekdata.lsnum - last2weekdata.lsnum) / last2weekdata.lsnum,2) as lsnumRatio ")
            .append(" , lastweekdata.whRate ")
            .append(" , ROUND((lastweekdata.whRate - last2weekdata.whRate),2) as whRateRatio ")
            .append(" , lastweekdata.averageDose ")
            .append(" , ROUND((lastweekdata.averageDose - last2weekdata.averageDose ) / last2weekdata.averageDose,2) as averageDoseRatio ");
    
    public static final StringBuffer SQL_WEEKLY_HOS_SALES_DATA_LASTWEEK_SELECT_PED 
        = new StringBuffer("    IFNULL(lastweek.pnum,0) as pnum, ")
            .append("   IFNULL(lastweek.lsnum,0) as lsnum, ")
            .append("   ROUND(IFNULL(lastweek.lsnum,0) / IFNULL(lastweek.pnum,0),2) as whRate, ")
            .append("   IFNULL(lastweek.averageDose,0) as averageDose ")
            .append("   from ( select * from tbl_pediatrics_data_weekly where duration = '").append(lastWeekDuration).append("' ) lastweek ");

    public static final StringBuffer SQL_WEEKLY_HOS_SALES_DATA_LAST2WEEK_SELECT_PED 
        = new StringBuffer("    IFNULL(last2week.pnum,0) as pnum, ")
            .append("   IFNULL(last2week.lsnum,0) as lsnum, ")
            .append("   ROUND(IFNULL(last2week.lsnum,0) / IFNULL(last2week.pnum,0),2) as whRate, ")
            .append("   IFNULL(last2week.averageDose,0) as averageDose ")
            .append("   from ( select * from tbl_pediatrics_data_weekly where duration = '").append(last2WeekDuration).append("' ) last2week ");
    
    public static final StringBuffer SQL_WEEKLY_HOS_SALES_DATA_LASTWEEK_SELECT_RES 
        = new StringBuffer("    IFNULL(lastweek.pnum,0) as pnum, ")
            .append("   IFNULL(lastweek.lsnum,0) as lsnum, ")
            .append("   ROUND(IFNULL(lastweek.lsnum,0) / IFNULL(lastweek.pnum,0),2) as whRate, ")
            .append("   IFNULL(lastweek.averageDose,0) as averageDose ")
            .append("   from tbl_respirology_data_weekly lastweek, tbl_hospital h ")
            .append("   where duration = '").append(lastWeekDuration).append("' )")
            .append("	and lastweek.hospitalCode = h.code and h.isResAssessed='1'");
    
    public static final StringBuffer SQL_WEEKLY_HOS_SALES_DATA_LAST2WEEK_SELECT_RES 
        = new StringBuffer("    IFNULL(last2week.pnum,0) as pnum, ")
            .append("   IFNULL(last2week.lsnum,0) as lsnum, ")
            .append("   ROUND(IFNULL(last2week.lsnum,0) / IFNULL(last2week.pnum,0),2) as whRate, ")
            .append("   IFNULL(last2week.averageDose,0) as averageDose ")
            .append("   from tbl_respirology_data_weekly last2week, tbl_hospital h ")
            .append("   where duration = '").append(last2WeekDuration).append("' )")
            .append("	and last2week.hospitalCode = h.code and h.isResAssessed='1'");
    
    public static final StringBuffer SQL_WEEKLY_HOS_SALES_DATA_LASTWEEK_SELECT_CHE 
    = new StringBuffer("    IFNULL(lastweek.pnum,0) as pnum, ")
    .append("   IFNULL(lastweek.lsnum,0) as lsnum, ")
    .append("   ROUND(IFNULL(lastweek.lsnum,0) / IFNULL(lastweek.pnum,0),2) as whRate, ")
    .append("   IFNULL(lastweek.averageDose,0) as averageDose ")
    .append("   from ( select * from tbl_chestSurgery_data_weekly where duration = '").append(lastWeekDuration).append("' ) lastweek ");
    
    public static final StringBuffer SQL_WEEKLY_HOS_SALES_DATA_LAST2WEEK_SELECT_CHE 
    = new StringBuffer("    IFNULL(last2week.pnum,0) as pnum, ")
    .append("   IFNULL(last2week.lsnum,0) as lsnum, ")
    .append("   ROUND(IFNULL(last2week.lsnum,0) / IFNULL(last2week.pnum,0),2) as whRate, ")
    .append("   IFNULL(last2week.averageDose,0) as averageDose ")
    .append("   from ( select * from tbl_chestSurgery_data_weekly where duration = '").append(last2WeekDuration).append("' ) last2week ");
    
    public static final StringBuffer SQL_DAILYREPORT_SELECTION_PED
        = new StringBuffer("")
            .append(" count(1) as inNum, ")
            .append(" IFNULL(sum(pd.pnum),0) as pnum, ")
            .append(" IFNULL(sum(pd.whnum),0) as whnum, ")
            .append(" IFNULL(sum(pd.lsnum),0) as lsnum, ")
            .append(" IFNULL(sum(pd.portNum),0) as portNum, ")
            .append(" IFNULL( sum( ( ( 0.5*IFNULL(pd.hqd,0) + 0.5*2*IFNULL(pd.hbid,0) + 1*1*IFNULL(pd.oqd,0) + 1*2*IFNULL(pd.obid,0) + 2*1*IFNULL(pd.tqd,0) + 2*2*IFNULL(pd.tbid,0) ) / 100 ) * IFNULL(pd.lsnum,0) ) / IFNULL(sum(pd.lsnum),0),0 ) as averageDose,")
            .append(" IFNULL( sum( IFNULL(pd.hqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as hmgRate,")
            .append(" IFNULL( sum( IFNULL(pd.hbid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.oqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as omgRate,")
            .append(" IFNULL( sum( IFNULL(pd.obid,0)/100*IFNULL(pd.lsnum,0) + IFNULL(pd.tqd,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as tmgRate,")
            .append(" IFNULL( sum( IFNULL(pd.tbid,0)/100*IFNULL(pd.lsnum,0) ) / sum( IFNULL(pd.lsnum,0) ),0 ) as fmgRate, ")
            .append(" IFNULL(sum(pd.whbwnum),0) as whbwnum, ")
            .append(" IFNULL(sum(pd.whbwnum) / ")
		    .append("			sum( ")
		    .append("	 		case when pd.whbwnum != 0 then pd.lsnum ")
		    .append("	 			else 0 ")
		    .append("	 		end )")
		    .append(" ,0) as blRate ");
            
    
    public static final StringBuffer SQL_DAILYREPORT_SELECTION_ALIAS_PED
        = new StringBuffer("")
            .append(" IFNULL(dailyData.inNum,0) as inNum,  ")
            .append(" IFNULL(dailyData.pnum,0) as pnum,  ")
            .append(" IFNULL(dailyData.whnum,0) as whnum,  ")
            .append(" IFNULL(dailyData.lsnum,0) as lsnum,  ")
            .append(" IFNULL(dailyData.portNum,0) as portNum,  ")
            .append(" IFNULL(dailyData.averageDose,0) as averageDose,  ")
            .append(" IFNULL(dailyData.hmgRate,0) as hmgRate,  ")
            .append(" IFNULL(dailyData.omgRate,0) as omgRate,  ")
            .append(" IFNULL(dailyData.tmgRate,0) as tmgRate,  ")
            .append(" IFNULL(dailyData.fmgRate,0) as fmgRate, ")
            .append(" IFNULL(dailyData.whbwnum,0) as whbwnum, ")
            .append(" IFNULL(dailyData.blRate,0) as blRate ");
    
    public static final StringBuffer SQL_DAILYREPORT_SELECTION_RES
            = new StringBuffer("")
            .append(" count(1) as inNum, ")
            .append(" IFNULL(sum(rd.pnum),0) as pnum, ")
            .append(" IFNULL(sum(rd.whnum),0) as whnum, ")
            .append(" IFNULL(sum(rd.lsnum),0) as lsnum, ")
            .append(" IFNULL( sum( ( ( 1*IFNULL(rd.oqd,0) + 2*1*IFNULL(rd.tqd,0) + 1*3*IFNULL(rd.otid,0) + 2*2*IFNULL(rd.tbid,0) + 2*3*IFNULL(rd.ttid,0) + 3*2*IFNULL(rd.thbid,0) + 4*2*IFNULL(rd.fbid,0) ) / 100 ) * IFNULL(rd.lsnum,0) ) / IFNULL(sum(rd.lsnum),0),0 ) as averageDose,")
            .append(" IFNULL( sum( IFNULL(rd.oqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as omgRate,")
            .append(" IFNULL( sum( IFNULL(rd.tqd,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as tmgRate,")
            .append(" IFNULL( sum( IFNULL(rd.otid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as thmgRate,")
            .append(" IFNULL( sum( IFNULL(rd.tbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as fmgRate,")
            .append(" IFNULL( sum( IFNULL(rd.ttid,0)/100*IFNULL(rd.lsnum,0) + IFNULL(rd.thbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as smgRate,")
            .append(" IFNULL( sum( IFNULL(rd.fbid,0)/100*IFNULL(rd.lsnum,0) ) / sum( IFNULL(rd.lsnum,0) ),0 ) as emgRate ");
    
    public static final StringBuffer SQL_DAILYREPORT_SELECTION_ALIAS_RES
            = new StringBuffer("")
            .append(" IFNULL(dailyData.inNum,0) as inNum,  ")
            .append(" IFNULL(dailyData.pnum,0) as pnum,  ")
            .append(" IFNULL(dailyData.whnum,0) as whnum,  ")
            .append(" IFNULL(dailyData.lsnum,0) as lsnum,  ")
            .append(" IFNULL(dailyData.averageDose,0) as averageDose,  ")
            .append(" IFNULL(dailyData.omgRate,0) as omgRate,  ")
            .append(" IFNULL(dailyData.tmgRate,0) as tmgRate,  ")
            .append(" IFNULL(dailyData.thmgRate,0) as thmgRate,  ")
            .append(" IFNULL(dailyData.fmgRate,0) as fmgRate, ")
            .append(" IFNULL(dailyData.smgRate,0) as smgRate,  ")
            .append(" IFNULL(dailyData.emgRate,0) as emgRate ");
    
    public static final StringBuffer SQL_DAILYREPORT_SELECTION_CHE
            = new StringBuffer("")
            .append(" count(1) as inNum, ")
            .append(" IFNULL(sum(cd.pnum),0) as pnum, ")
            .append(" IFNULL(sum(cd.whnum),0) as whnum, ")
            .append(" IFNULL(sum(cd.lsnum),0) as lsnum, ")
            .append(" IFNULL( sum( ( ( 1*IFNULL(cd.oqd,0) + 2*1*IFNULL(cd.tqd,0) + 1*3*IFNULL(cd.otid,0) + 2*2*IFNULL(cd.tbid,0) + 2*3*IFNULL(cd.ttid,0) + 3*2*IFNULL(cd.thbid,0) + 4*2*IFNULL(cd.fbid,0) ) / 100 ) * IFNULL(cd.lsnum,0) ) / IFNULL(sum(cd.lsnum),0),0 ) as averageDose,")
            .append(" IFNULL( sum( IFNULL(cd.oqd,0)/100*IFNULL(cd.lsnum,0) ) / sum( IFNULL(cd.lsnum,0) ),0 ) as omgRate,")
            .append(" IFNULL( sum( IFNULL(cd.tqd,0)/100*IFNULL(cd.lsnum,0) ) / sum( IFNULL(cd.lsnum,0) ),0 ) as tmgRate,")
            .append(" IFNULL( sum( IFNULL(cd.otid,0)/100*IFNULL(cd.lsnum,0) ) / sum( IFNULL(cd.lsnum,0) ),0 ) as thmgRate,")
            .append(" IFNULL( sum( IFNULL(cd.tbid,0)/100*IFNULL(cd.lsnum,0) ) / sum( IFNULL(cd.lsnum,0) ),0 ) as fmgRate,")
            .append(" IFNULL( sum( IFNULL(cd.ttid,0)/100*IFNULL(cd.lsnum,0) + IFNULL(cd.thbid,0)/100*IFNULL(cd.lsnum,0) ) / sum( IFNULL(cd.lsnum,0) ),0 ) as smgRate,")
            .append(" IFNULL( sum( IFNULL(cd.fbid,0)/100*IFNULL(cd.lsnum,0) ) / sum( IFNULL(cd.lsnum,0) ),0 ) as emgRate ");
    
    public static final StringBuffer SQL_DAILYREPORT_SELECTION_ALIAS_CHE
            = new StringBuffer("")
            .append(" IFNULL(dailyData.inNum,0) as inNum,  ")
            .append(" IFNULL(dailyData.pnum,0) as pnum,  ")
            .append(" IFNULL(dailyData.whnum,0) as whnum,  ")
            .append(" IFNULL(dailyData.lsnum,0) as lsnum,  ")
            .append(" IFNULL(dailyData.averageDose,0) as averageDose,  ")
            .append(" IFNULL(dailyData.omgRate,0) as omgRate,  ")
            .append(" IFNULL(dailyData.tmgRate,0) as tmgRate,  ")
            .append(" IFNULL(dailyData.thmgRate,0) as thmgRate,  ")
            .append(" IFNULL(dailyData.fmgRate,0) as fmgRate, ")
            .append(" IFNULL(dailyData.smgRate,0) as smgRate,  ")
            .append(" IFNULL(dailyData.emgRate,0) as emgRate ");
    
    public static final StringBuffer SQL_MONTHLY_INRATE_SELECTION
		    = new StringBuffer(" select duration, h.region, h.rsmRegion , IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate ");
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_SELECTION
    	= new StringBuffer(" , IFNULL(round(sum(lsnum)/sum(pnum),4),0) as whRate ")
		    .append(" , IFNULL(sum(pnum),0) as pnum ")
		    .append(" , IFNULL(sum(lsnum),0) as lsnum ")
		    .append(" , IFNULL(round(sum(averageDose*lsnum)/sum(lsnum),4),0) as averageDose ");
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION
	    = new StringBuffer(", IFNULL(sum(least(innum,3)),0) / (count(1)*3) as inRate ")
		.append(", IFNULL(round(sum(lsnum)/sum(pnum),4),0) as whRate ");
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_CORE_EMERGING_SELECTION_RES
	    = new StringBuffer("")
	    .append(" ,IFNULL( sum(  ")
	    .append("   case ")
	    .append("       when h.dragonType='Emerging' then least(innum*3,3) ")
	    .append("       when h.dragonType='Core' then least(innum,3) ")
	    .append(" 	end")
	    .append(" ) ")
	    .append(" / (count(1)*3),0 ) as inRate ")
	    .append(", IFNULL(round(sum(lsnum)/sum(pnum),4),0) as whRate ")
	    .append(" , IFNULL(round(sum(averageDose*lsnum)/sum(lsnum),4),0) as averageDose ")
	    .append(" ,IFNULL( round( sum(  ")
	    .append("   case ")
	    .append("       when h.level != '3' then averageDose*lsnum ")
	    .append("       else 0 ")
	    .append(" 	end")
	    .append(" ) ")
	    .append(" / sum(")
	    .append("   case ")
	    .append("       when h.level != '3' then lsnum ")
	    .append("       else 0 ")
	    .append(" 	end")
	    .append(" ),4),0) as averageDose2 ")
	    .append(", IFNULL(sum( case when h.level != '3' then 1 else 0 end ),0) as level2Num ")
	    .append(", IFNULL(sum( case when h.level = '3' then 1 else 0 end ),0) as level3Num ")
	    .append(" ,IFNULL( round( sum(  ")
	    .append("   case ")
	    .append("       when h.level = '3' then averageDose*lsnum ")
	    .append("       else 0 ")
	    .append(" 	end")
	    .append(" ) ")
	    .append(" / sum(")
	    .append("   case ")
	    .append("       when h.level = '3' then lsnum ")
	    .append("       else 0 ")
	    .append(" 	end")
	    .append(" ),4),0) as averageDose3 ");
    

    public static final StringBuffer SQL_MONTHLY_STATISTICS_CORE_AVERAGEDOSE_SELECTION_RES
	    = new StringBuffer("")
	    .append(" ,IFNULL( round( sum(  ")
	    .append("   case ")
	    .append("       when h.level != '3' then averageDose*lsnum ")
	    .append("       else 0 ")
	    .append(" 	end")
	    .append(" ) ")
	    .append(" / sum(")
	    .append("   case ")
	    .append("       when h.level != '3' then lsnum ")
	    .append("       else 0 ")
	    .append(" 	end")
	    .append(" ),4),0) as averageDose2 ")
	    .append(getLevel2AverageDoseSQL("omgRate","level21mgRate"))
	    .append(getLevel2AverageDoseSQL("tmgRate","level22mgRate"))
	    .append(getLevel2AverageDoseSQL("thmgRate","level23mgRate"))
	    .append(getLevel2AverageDoseSQL("fmgRate","level24mgRate"))
	    .append(getLevel2AverageDoseSQL("smgRate","level26mgRate"))
	    .append(getLevel2AverageDoseSQL("emgRate","level28mgRate"))
	    .append(getLevel2AverageDoseSQL("(emgRate+smgRate+fmgRate)","level24mgUpRate"))
	    .append(" ,IFNULL( round( sum(  ")
	    .append("   case ")
	    .append("       when h.level = '3' then averageDose*lsnum ")
	    .append("       else 0 ")
	    .append(" 	end")
	    .append(" ) ")
	    .append(" / sum(")
	    .append("   case ")
	    .append("       when h.level = '3' then lsnum ")
	    .append("       else 0 ")
	    .append(" 	end")
	    .append(" ),4),0) as averageDose3 ")
	    .append(getLevel3AverageDoseSQL("omgRate","level31mgRate"))
	    .append(getLevel3AverageDoseSQL("tmgRate","level32mgRate"))
	    .append(getLevel3AverageDoseSQL("thmgRate","level33mgRate"))
	    .append(getLevel3AverageDoseSQL("fmgRate","level34mgRate"))
	    .append(getLevel3AverageDoseSQL("smgRate","level36mgRate"))
	    .append(getLevel3AverageDoseSQL("emgRate","level38mgRate"))
	    .append(getLevel3AverageDoseSQL("(emgRate+smgRate+fmgRate)","level34mgUpRate"))
	    .append(", IFNULL(sum( case when h.level != '3' then 1 else 0 end ),0) as level2Num ")
	    .append(", IFNULL(sum( case when h.level = '3' then 1 else 0 end ),0) as level3Num ")
	    .append(" , IFNULL(round(sum(averageDose*lsnum)/sum(lsnum),4),0) as averageDose ");
    
    private static StringBuffer getLevel2AverageDoseSQL(String rateName, String aliasName){
    	 return new StringBuffer("")
 	    .append(" ,IFNULL( round( sum(  ")
 	    .append("   case ")
 	    .append("       when h.level != '3' then ").append(rateName).append("*lsnum ")
 	    .append("       else 0 ")
 	    .append(" 	end")
 	    .append(" ) ")
 	    .append(" / sum(")
 	    .append("   case ")
 	    .append("       when h.level != '3' then lsnum ")
 	    .append("       else 0 ")
 	    .append(" 	end")
 	    .append(" ),4),0) as ")
 	    .append(aliasName);
    }
    
    private static StringBuffer getLevel3AverageDoseSQL(String rateName, String aliasName){
    	return new StringBuffer("")
    	.append(" ,IFNULL( round( sum(  ")
    	.append("   case ")
    	.append("       when h.level = '3' then ").append(rateName).append("*lsnum ")
    	.append("       else 0 ")
    	.append(" 	end")
    	.append(" ) ")
    	.append(" / sum(")
    	.append("   case ")
    	.append("       when h.level = '3' then lsnum ")
    	.append("       else 0 ")
    	.append(" 	end")
    	.append(" ),4),0) as ")
    	.append(aliasName);
    }
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_DSM_CONDITION
    	= new StringBuffer(" where duration between ? and ? and hospitalCode = h.code ");
    public static final StringBuffer SQL_MONTHLY_STATISTICS_DSM_GROUP
		= new StringBuffer(" group by h.region, h.rsmRegion, h.dsmCode");
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_RSM_CONDITION
	    = new StringBuffer(" where duration between ? and ? and hospitalCode = h.code ");
    public static final StringBuffer SQL_MONTHLY_STATISTICS_RSM_GROUP
	    = new StringBuffer(" group by h.region, h.rsmRegion");
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_RSD_CONDITION
	    = new StringBuffer(" where duration between ? and ? and hospitalCode = h.code ");
    public static final StringBuffer SQL_MONTHLY_STATISTICS_RSD_GROUP
	    = new StringBuffer(" group by h.region");
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_CORE_DSM_CONDITION
	    = new StringBuffer(" where duration between ? and ? and hospitalCode = h.code and h.dragonType='Core' ");
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_CORE_RSM_CONDITION
	    = new StringBuffer(" where duration between ? and ? and hospitalCode = h.code and h.dragonType='Core' ");
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_CORE_RSD_CONDITION
	    = new StringBuffer(" where duration between ? and ? and hospitalCode = h.code and h.dragonType='Core' ");
    
    public static final StringBuffer SQL_MONTHLY_STATISTICS_EMERGING_DSM_CONDITION
	    = new StringBuffer(" where duration between ? and ? and hospitalCode = h.code and h.dragonType='Emerging' ");
    public static final StringBuffer SQL_MONTHLY_STATISTICS_EMERGING_RSM_CONDITION
	    = new StringBuffer(" where duration between ? and ? and hospitalCode = h.code and h.dragonType='Emerging' ");
    public static final StringBuffer SQL_MONTHLY_STATISTICS_EMERGING_RSD_CONDITION
	    = new StringBuffer(" where duration between ? and ? and hospitalCode = h.code and h.dragonType='Emerging' ");
    
    public static final StringBuffer SQL_HOME_WEEKLY_DATA_SELECTION
            = new StringBuffer("")
            .append(" select ")
            .append(" homeData.newWhNum")
            .append(" , homeData.cureRate ")
            .append(" , homeData.lsnum ")
            .append(" , homeData.lsRate ")
            .append(" , homeData.reachRate ");
    
    public static final StringBuffer SQL_HOME_WEEKLY_DATA_SUB_SELECTION
            = new StringBuffer("")
            .append(" select sum(hd.salenum) as newWhNum ")
            .append(" , sum(hd.ltenum)/sum(hd.asthmanum) as cureRate ")
            .append(" , sum(hd.lsnum) as lsnum ")
            .append(" , sum(hd.lsnum)/sum(hd.ltenum) as lsRate ")
            .append(" , IFNULL(sum(hd.lttnum),0) as reachRate ");
    
    public static final StringBuffer SQL_HOME_WEEKLY_DATA_SUB_FROM
    = new StringBuffer(" from tbl_home_data hd, tbl_userinfo u ");
    
    public static final StringBuffer SQL_HOME_WEEKLY_DATA_SUB_3_FROM
    = new StringBuffer(" from tbl_home_data hd, tbl_hospital h ");
    
    public static final StringBuffer SQL_HOME_WEEKLY_DATA_FROM_HOME_ONLY
    = new StringBuffer(" from tbl_home_data hd ");
    
    
    public static final StringBuffer SQL_HOSPITAL_WEEKLY_DATA_SELECTION
        = new StringBuffer(" select duration, hospitalCode, pnum, lsnum, averageDose ");
    
    public static final StringBuffer SQL_KPI_HOS_SELECTION
    	= new StringBuffer(" select h.province, h.city, h.code, h.name, h.dragonType, h.level, h.isTop100 ")
    		.append(" , ( select distinct property_value from tbl_property where property_name = h.region ) as brCNName ")
    		.append(" , h.region ")
    		.append(" , h.portNum ")
    		.append(" , rsd.userCode as rsdCode ")
    		.append(" , rsd.name as rsdName ")
    		.append(" , rsd.telephone as rsdTel ")
    		.append(" , rsd.email as rsdEmail ")
    		.append(" , h.rsmRegion ")
    		.append(" , rsm.userCode as rsmCode ")
    		.append(" , rsm.name as rsmName ")
    		.append(" , rsm.telephone as rsmTel ")
    		.append(" , rsm.email as rsmEmail ")
    		.append(" , ( select userCode from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmCode ")
    		.append(" , ( select name from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmName  ")
    		.append(" , ( select telephone from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmTel ")
    		.append(" , ( select email from tbl_userinfo u where u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM' ) as dsmEmail ");

    public static final StringBuffer SQL_KPI_HOS_CONDITION_WITH_SALES
    	= new StringBuffer(" from tbl_hospital h, tbl_hos_user hu, tbl_userinfo sales, tbl_userinfo rsd, tbl_userinfo rsm ")
    		.append(" where h.code = hu.hosCode ")
    		.append(" and hu.userCode = sales.userCode ")
    		.append(" and h.region = rsd.regionCenter ")
    		.append(" and rsd.level='RSD' ")
    		.append(" and h.rsmRegion = rsm.region ")
    		.append(" and rsm.level='RSM' ")
    		.append(" and hu.userCode !='2000003' ");
    
    public static final StringBuffer SQL_KPI_HOS_CONDITION_WITHOUT_SALES
	    = new StringBuffer(" from tbl_hospital h, tbl_hos_user hu, tbl_userinfo rsd, tbl_userinfo rsm ")
		    .append(" where h.code = hu.hosCode ")
		    .append(" and h.region = rsd.regionCenter ")
		    .append(" and rsd.level='RSD' ")
		    .append(" and h.rsmRegion = rsm.region ")
		    .append(" and rsm.level='RSM' ")
		    .append(" and hu.userCode ='2000003' ");
    		 
    public static final StringBuffer SQL_KPI_HOS_SELECTION_SALES
    	= new StringBuffer(" , hu.isPrimary as isMainSales ")
    		.append(" , sales.userCode as salesCode ")
    		.append(" , sales.name as salesName ")
    		.append(" , sales.telephone as salesTel ")
    		.append(" , sales.email as salesEmail ");
    
    public static final StringBuffer SQL_KPI_HOS_SELECTION_WITHOUT_SALES
    	= new StringBuffer(" , hu.isPrimary as isMainSales ")
		    .append(" , '2000003' as salesCode ")
		    .append(" , 'Vacant' as salesName ")
		    .append(" , '#N/A' as salesTel ")
		    .append(" , '#N/A' as salesEmail ");
    
    public static final StringBuffer SQL_MONTH_WEEKLY_REPORT_SALES_NUM_CONDITION = new StringBuffer("")
            .append(" where  h.code = hu.hosCode ")
            .append(" and h.saleCode != '2000003'")
            .append(" and hu.userCode != '2000003'")
            .append(" and h.saleCode != '#N/A' ");
    
    public static final StringBuffer SQL_REPORT_PROCESS_RES_INNUM4RATE = new StringBuffer("")
            .append("       case ")
            .append("           when h1.dragonType='Emerging' then least(count(1)*3,3) ")
            .append("           when h1.dragonType='Core' then least(count(1),3) ")
            .append("       end  as inNum");
    
    /**
     * isDailyReport = '1'
     */
    public static final StringBuffer SQL_WHERE_CONDITION_DAILYREPORT_HOSPITAL = new StringBuffer("isDailyReport='1' ");
    /**
     * isPedAssessed='1'
     */
    public static final StringBuffer SQL_WHERE_CONDITION_PED_COLLECTION_HOSPITAL = new StringBuffer("isPedAssessed='1' ");
    
    public static final StringBuffer SQL_SELECTION_HOSPITAL = new StringBuffer("")
    		.append(", h.city, h.province, h.region, h.rsmRegion, h.saleCode, h.saleName, h.dsmCode, h.portNum, h.isRe2, h.isWHBW");
    
    /**
     * 呼吸科周周报---
     */
    
    private static final StringBuffer SQL_MONTHWEEKLYREPORT_COMMON_SELECTION_1 = new StringBuffer("")
		    .append(" ,date_MM ")
		    .append(" ,date_YYYY ")
		    .append(" ,'' as duration ")
		    .append(" ,IFNULL(sum(pnum),0) as pnum ")
		    .append(" ,IFNULL(sum(lsnum),0) as lsnum ")
		    .append(" ,IFNULL(sum(aenum),0) as aenum ")
		    .append(" ,IFNULL(sum(xbknum),0) as xbknum ")
		    .append(" ,IFNULL( sum(  ")
		    .append("   case ")
		    .append("       when h.dragonType='Emerging' then least(innum*3,3) ")
		    .append("       when h.dragonType='Core' then least(innum,3) ")
		    .append(" 	end")
		    .append(" ) ")
		    .append(" / (count(1)*3),0 ) as inRate ")
		    .append(" ,IFNULL(sum(rdw.lsnum)/sum(rdw.pnum),0) as whRate ")
		    .append(" ,IFNULL(sum(rdw.averageDose*rdw.lsnum)/sum(rdw.lsnum),0) as averageDose ")
		    .append(" ,(  ")
	        .append("   select temp2.weekNum ")
	        .append("   from (")
	        .append("       select count(1) as weekNum,date_YYYY,date_MM ")
	        .append("       from (")
	        .append("           select distinct rdw2.date_YYYY,rdw2.date_MM,rdw2.duration ")
	        .append("           from tbl_respirology_data_weekly rdw2, tbl_hospital h2 ")
	        .append("           where rdw2.hospitalCode = h2.code ");
    
    private static final StringBuffer SQL_MONTHWEEKLYREPORT_COMMON_SELECTION_2 = new StringBuffer("")
		    .append("       ) temp ")
		    .append("       group by temp.date_YYYY,temp.date_MM ")
		    .append("   ) temp2 ")
		    .append("   where rdw.date_YYYY = temp2.date_YYYY and rdw.date_MM = temp2.date_MM ")
		    .append(" ) as weeklyCount ");
    
    private static final StringBuffer getMonthWeeklyCommonSeletion(String level){
    	StringBuffer sb = new StringBuffer("");
    	switch(level){
    	case LsAttributes.USER_LEVEL_RSM:
    		sb.append(" select h.rsmRegion as title");
    	    sb.append(" ,(select distinct name from tbl_userinfo u where u.regionCenter = h.region and u.region = h.rsmRegion and u.level='RSM') as name ");
    	    break;
    	case LsAttributes.USER_LEVEL_RSD:
    		sb.append(" select h.region as title");
    	    sb.append(" ,(select distinct name from tbl_userinfo u where u.regionCenter = h.region and u.level='RSD') as name ");
    	    break;
    	case LsAttributes.USER_LEVEL_DSM:
    		sb.append(" select concat(h.rsmRegion,'-',h.dsmCode,'-',(select distinct name from tbl_userinfo u where u.regionCenter = h.region and u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM')) as title");
    	    sb.append(" ,(select distinct name from tbl_userinfo u where u.regionCenter = h.region and u.region = h.rsmRegion and u.userCode = h.dsmCode and u.level='DSM') as name ");
    	    break;
    	}
    	return sb;
    }
    
    private static final StringBuffer SQL_MONTHWEEKLYREPORT_COMMON_COUNTRY_SELECTION = new StringBuffer("")
    		.append(" select '全国' as title ")
    		.append(" ,'全国' as name ");
    
    public static final StringBuffer getSQLMonthWeeklyReportRes(String isRe2 , String level){
    	StringBuffer sb = getMonthWeeklyCommonSeletion(level);
	        
	    sb.append(SQL_MONTHWEEKLYREPORT_COMMON_SELECTION_1);
	    if( "1".equals(isRe2) ){
	    	sb.append("     and ( (rdw2.date_YYYY >= 2015 and rdw2.date_MM >= 4)  or ( rdw2.date_YYYY >= 2016 )) ");
    		sb.append("		and h2.isRe2 = '1' ");
    	}else{
    		sb.append("     and ( (rdw2.date_YYYY > 2013 and rdw2.date_MM > 3)  or ( rdw2.date_YYYY > 2014 )) ");
    		sb.append(" 	and h2.isResAssessed='1' ");
    	}
	    sb.append(SQL_MONTHWEEKLYREPORT_COMMON_SELECTION_2);
	    
	    sb.append(" ,(    select sum(res_room) from tbl_month_data md, tbl_hospital h1 ");
	    sb.append("       where md.hospitalCode = h1.code ");
	    switch(level){
    	case LsAttributes.USER_LEVEL_RSM:
    		sb.append(" and h1.rsmRegion = h.rsmRegion ");
    	    break;
    	case LsAttributes.USER_LEVEL_RSD:
    		sb.append(" and h1.region = h.region ");
    	    break;
    	case LsAttributes.USER_LEVEL_DSM:
    		sb.append(" and h1.rsmRegion = h.rsmRegion and h1.dsmCode = h.dsmCode ");
    	    break;
    	}
	    sb.append("       and md.countMonth = concat(date_YYYY,'-',LPAD(date_MM, 2, 0)) ");
        if( "1".equals(isRe2) ){
    		sb.append("   and h1.isRe2 = '1' ");
    	}else{
    		sb.append("	  and h1.isResAssessed = '1' ");
    	}
	    sb.append(") as resMonthNum ");
	    
	    sb.append(" from tbl_respirology_data_weekly rdw, tbl_hospital h ");
	    sb.append(" where h.code = rdw.hospitalCode ");
	    
	    if( "1".equals(isRe2) ){
	    	sb.append(" and ( (rdw.date_YYYY >= 2015 and rdw.date_MM >= 4)  or ( rdw.date_YYYY >= 2016 )) ")
	        .append(" and rdw.duration < ? ")
    		.append(" and h.isRe2 = '1' ");
    	}else{
    		sb.append(" and ( (rdw.date_YYYY > 2013 and rdw.date_MM > 3)  or ( rdw.date_YYYY > 2014 )) ")
	        .append(" and rdw.duration < ? ")
	        .append("and h.isResAssessed='1' ");
    	}
	    
	    switch(level){
    	case LsAttributes.USER_LEVEL_RSM:
    		sb.append(" group by date_YYYY,date_MM,h.rsmRegion ");
    	    sb.append(" order by h.rsmRegion, date_YYYY, date_MM");
    	    break;
    	case LsAttributes.USER_LEVEL_RSD:
    		sb.append(" group by date_YYYY,date_MM,h.region ");
    	    sb.append(" order by h.region, date_YYYY, date_MM");
    	    break;
    	case LsAttributes.USER_LEVEL_DSM:
    		sb.append(" group by date_YYYY,date_MM,h.region,h.rsmRegion,h.dsmCode ");
    	    sb.append(" order by h.region,h.rsmRegion,h.dsmCode, date_YYYY, date_MM");
    	    break;
    	}
    	
    	return sb;
    }
    
    public static final StringBuffer getSQLMonthWeeklyReportResCountry( String isRe2){
    	StringBuffer sb = new StringBuffer(SQL_MONTHWEEKLYREPORT_COMMON_COUNTRY_SELECTION);
    	
	    sb.append(SQL_MONTHWEEKLYREPORT_COMMON_SELECTION_1);
    	
    	if( "1".equals(isRe2) ){
	    	sb.append("     and ( (rdw2.date_YYYY >= 2015 and rdw2.date_MM >= 4)  or ( rdw2.date_YYYY >= 2016 )) ");
    		sb.append("		and h2.isRe2 = '1' ");
    	}else{
    		sb.append("     and ( (rdw2.date_YYYY > 2013 and rdw2.date_MM > 3)  or ( rdw2.date_YYYY > 2014 )) ");
    		sb.append(" 	and h2.isResAssessed='1' ");
    	}
    	
	    sb.append(SQL_MONTHWEEKLYREPORT_COMMON_SELECTION_2)
	        .append(" ,(    select sum(res_room) from tbl_month_data md, tbl_hospital h1 ")
	        .append("		where md.hospitalCode = h1.code ")
	        .append("       and md.countMonth = concat(date_YYYY,'-',LPAD(date_MM, 2, 0)) ");
        if( "1".equals(isRe2) ){
    		sb.append("		and h1.isRe2 = '1' ");
    	}else{
    		sb.append("		and h1.isResAssessed = '1' ");
    	}
	    sb.append(") as resMonthNum ")
	        .append(" from tbl_respirology_data_weekly rdw, tbl_hospital h ")
	        .append(" where h.code = rdw.hospitalCode ");
	    
        if( "1".equals(isRe2) ){
        	sb.append(" and ( (rdw.date_YYYY >= 2015 and rdw.date_MM >= 4)  or ( rdw.date_YYYY >= 2016 )) ")
        	.append(" and rdw.duration < ? ")
    		.append(" and h.isRe2 = '1' ");
    	}else{
    		sb.append(" and ( (rdw.date_YYYY > 2013 and rdw.date_MM > 3)  or ( rdw.date_YYYY > 2014 )) ")
        	.append(" and rdw.duration < ? ")
        	.append(" and h.isResAssessed='1' ");
    	}
        
	    sb.append(" group by date_YYYY,date_MM ")
	        .append(" order by date_YYYY, date_MM");
    	return sb;
    }
    
    private static final StringBuffer SQL_MONTH_WEEKLY_REPORT_WEEKLY_RES_COMMON_SELECTION = new StringBuffer()
	    .append(" ,date_MM ")
	    .append(" ,date_YYYY ")
	    .append(" , rdw.duration ")
	    .append(" ,IFNULL(sum(pnum),0) as pnum ")
	    .append(" ,IFNULL(sum(lsnum),0) as lsnum ")
	    .append(" ,IFNULL(sum(aenum),0) as aenum ")
	    .append(" ,IFNULL(sum(xbknum),0) as xbknum ")
	    .append(" ,IFNULL( sum(  ")
	    .append("   case ")
	    .append("       when h.dragonType='Emerging' then least(innum*3,3) ")
	    .append("       when h.dragonType='Core' then least(innum,3) ")
	    .append(" 	end")
	    .append(" ) ")
	    .append(" / (count(1)*3),0 ) as inRate ")
	    .append(" ,IFNULL(sum(rdw.lsnum)/sum(rdw.pnum),0) as whRate ")
	    .append(" ,IFNULL(sum(rdw.averageDose*rdw.lsnum)/sum(rdw.lsnum),0) as averageDose ")
	    .append(" ,'1' as weeklyCount ")
	    .append(" ,'1' as resMonthNum ");
    
    private static final StringBuffer SQL_MONTH_WEEKLY_REPORT_WEEKLY_RES_COMMON_CONDITION = new StringBuffer()
	    .append(" from tbl_respirology_data_weekly rdw, tbl_hospital h ")
	    .append(" where rdw.hospitalCode = h.code ")
	    .append(" and rdw.duration >= ? ");
    
    public static final StringBuffer getSQLMonthWeeklyReportWeeklyRes( String isRe2, String level){
    	StringBuffer sb = getMonthWeeklyCommonSeletion(level);
    	
        sb.append(SQL_MONTH_WEEKLY_REPORT_WEEKLY_RES_COMMON_SELECTION);
        sb.append(SQL_MONTH_WEEKLY_REPORT_WEEKLY_RES_COMMON_CONDITION);
        if( "1".equals(isRe2) ){
        	sb.append(" and rdw.duration>='2015.04.13-2015.04.19' ");
    		sb.append(" and h.isRe2 = '1' ");
    	}else{
    		sb.append(" and h.isResAssessed='1' ");
    	}
        switch(level){
    	case LsAttributes.USER_LEVEL_RSM:
    		sb.append(" group by h.rsmRegion, duration");
            sb.append(" order by h.rsmRegion, duration");
    	    break;
    	case LsAttributes.USER_LEVEL_RSD:
    		sb.append(" group by h.region, duration");
            sb.append(" order by h.region, duration");
    	    break;
    	case LsAttributes.USER_LEVEL_DSM:
    		sb.append(" group by h.region,h.rsmRegion,h.dsmCode, duration");
            sb.append(" order by h.region,h.rsmRegion,h.dsmCode, duration");
    	    break;
    	}
    	
    	return sb;
    }
    
    public static final StringBuffer getSQLMonthWeeklyReportWeeklyResCountry(String isRe2){
    	StringBuffer sb = new StringBuffer(SQL_MONTHWEEKLYREPORT_COMMON_COUNTRY_SELECTION);
	    sb.append(SQL_MONTH_WEEKLY_REPORT_WEEKLY_RES_COMMON_SELECTION);
		sb.append(SQL_MONTH_WEEKLY_REPORT_WEEKLY_RES_COMMON_CONDITION);
	    if( "1".equals(isRe2) ){
	    	sb.append(" and rdw.duration>='2015.04.13-2015.04.19' ");
    		sb.append(" and h.isRe2 = '1' ");
    	}else{
    		sb.append(" and h.isResAssessed='1' ");
    	}
		sb.append(" group by duration ")
		    .append(" order by duration");
	    return sb;
    }
    
    /**
     * ---呼吸科周周报
     */
}
