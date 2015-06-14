package com.chalet.lskpi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * yyyy.MM.dd
	 */
    private static SimpleDateFormat formatter_1 = new SimpleDateFormat("yyyy.MM.dd");
    
    public static Date parseString2Date(String dateStr) throws ParseException{
    	return formatter_1.parse(dateStr);
    }
    
    public static String parseDate2String(Date date) throws ParseException{
    	return formatter_1.format(date);
    }
    
    /**
     * 返回昨天的日期，如果遇到周一和周日，则返回上周五
     * @param date date
     * @return Date
     */
    public static Date populateParamDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //1 - Sunday 2-Monday
        Date paramDate = date;
        if( dayInWeek == 1 ){
            paramDate = new Date(date.getTime() - 2 * 24 * 60 * 60 * 1000);
        }else if( dayInWeek == 2 ){
            paramDate = new Date(date.getTime() - 3 * 24 * 60 * 60 * 1000);
        }else{
            paramDate = new Date(date.getTime() - 1 * 24 * 60 * 60 * 1000);
        }
        paramDate = new Date(paramDate.getYear(),paramDate.getMonth(),paramDate.getDate());
        return paramDate;
    }
    
    public static String getDirectoryNameOfLastDuration(){
        Date date = new Date();
        return getDirectoryNameOfLastDuration(date);
    }
    
    /**
     * 调整周期为周一到周日，本方法返回参数所在周期的周一， 格式为：MM-dd-yyyy
     * @param date
     * @return 本方法返回参数所在周期的周一
     */
    public static String getDirectoryNameOfLastDuration(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date lastThursDay = getDirectoryNameOfLastDuration_Date(date);
        return formatter.format(lastThursDay);
    }
    
    /**
     * 调整周期为周一到周日，本方法返回参数所在周期的周一
     * @param date
     * @return 本方法返回参数所在周期的周一
     */
    public static Date getDirectoryNameOfLastDuration_Date(Date refreshDate){
        // 0 sunday
        Date beginDate = refreshDate;
        if( refreshDate.getDay() == 0 ){
            beginDate = new Date(refreshDate.getTime() - 6 * 24 * 60 * 60 * 1000);
        }else {
            beginDate = new Date(refreshDate.getTime() - (refreshDate.getDay()-1) * 24 * 60 * 60 * 1000);
        }
        beginDate = new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
        return beginDate;
    }
    
    /**
     * 调整周期为周一到周日，本方法返回参数日期所在周期的目录名字，即参数日期所在周的下周一
     * @param date
     * @return 返回参数日期所在周期的目录名字，即参数日期所在周的下周一
     */
    public static String getDirectoryNameOfCurrentDuration(Date date){
        
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        return formatter.format(new Date(getDirectoryNameOfLastDuration_Date(date).getTime() + 7*24*60*60*1000));
    }
    
    /**
     * 获取参数日期所在周的duration
     * @param date
     * @return 获取参数日期所在周的duration
     */
    public static String getWeeklyDuration(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        StringBuffer sb = new StringBuffer("");
        if( date.getDay() == 0 ){
            sb.append(formatter.format(new Date(date.getTime() - 6*24*60*60*1000)))
            .append("-")
            .append(formatter.format(date));
        }else{
            sb.append(formatter.format(new Date(date.getTime() - (date.getDay()-1)*24*60*60*1000)))
            .append("-")
            .append(formatter.format(new Date(date.getTime() + (7-date.getDay())*24*60*60*1000)));
        }
        return sb.toString();
    }
    
    /**
     * 获取参数日期所在周的上周的duration
     * @param date
     * @return
     */
    public static String getWeeklyDurationYYYYMMDD(Date date){
    	StringBuffer sb = new StringBuffer("");
    	if( date.getDay() == 0 ){
    		sb.append(formatter_1.format(new Date(date.getTime() - 13*24*60*60*1000)))
    		.append("-")
    		.append(formatter_1.format(new Date(date.getTime() - 7*24*60*60*1000)));
    	}else{
    		sb.append(formatter_1.format(new Date(date.getTime() - (date.getDay()+6)*24*60*60*1000)))
    		.append("-")
    		.append(formatter_1.format(new Date(date.getTime() - date.getDay()*24*60*60*1000)));
    	}
    	return sb.toString();
    }
    
    /**
     * 获取参数日期所在周的上周的duration
     * @param date
     * @return
     */
    public static String getWeeklyDurationStr(Date date, String formater){
    	if( null == formater || "".equalsIgnoreCase(formater) ){
    		return getWeeklyDurationYYYYMMDD(date);
    	}else{
    		SimpleDateFormat sdf = new SimpleDateFormat(formater);
    		
    		StringBuffer sb = new StringBuffer("");
    		if( date.getDay() == 0 ){
    			sb.append(sdf.format(new Date(date.getTime() - 13*24*60*60*1000)))
    			.append("-")
    			.append(sdf.format(new Date(date.getTime() - 7*24*60*60*1000)));
    		}else{
    			sb.append(sdf.format(new Date(date.getTime() - (date.getDay()+6)*24*60*60*1000)))
    			.append("-")
    			.append(sdf.format(new Date(date.getTime() - date.getDay()*24*60*60*1000)));
    		}
    		return sb.toString();
    	}
    }
    
    public static String getLastMonthForTitle(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        return formatter.format(cal.getTime());
    }
    
    public static String getLastMonth(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MONTH, -1);
        return formatter.format(cal.getTime());
    }
    
    public static String getLast2Month(){
    	SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.add(Calendar.MONTH, -2);
    	return formatter.format(cal.getTime());
    }
    
    public static String getYesterDay(){
        Date date = populateParamDate(new Date());
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        return formatter.format(date);
    }
    
    public static String getYesterDayForDailyReportTitle(){
    	Date date = populateParamDate(new Date());
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	return formatter.format(date);
    }
    
    /**
     * 修正周期为周一到周天，则本方法新的返回值应该是refreshDate所在周的周天
     * @param refreshDate
     * @return Date 参数refreshDate所在周的周天
     */
    public static Date getGenerateWeeklyReportDate(Date refreshDate){
        Date endDate = refreshDate;
        if( refreshDate.getDay() > 0 ){
            endDate = new Date(refreshDate.getTime() + (7-refreshDate.getDay()) * 24 * 60 * 60 * 1000);
        }
        return new Date(endDate.getYear(),endDate.getMonth(),endDate.getDate());
    }
    
    /**
     * 修正周期为周一到周天
     * @return 返回上周天
     */
    public static Date getGenerateWeeklyReportDate(){
    	Date date = new Date();
    	Date returnDate = new Date();
    	// 0 sunday
    	if( date.getDay() == 0 ){
    		returnDate = new Date(date.getTime() - 7*24*60*60*1000);
    	}else{
    		returnDate = new Date(date.getTime() - date.getDay()*24*60*60*1000);
    	}
    	return new Date(returnDate.getYear(),returnDate.getMonth(),returnDate.getDate());
    }
    
    /**
     * 修正周期为周一到周天
     * @return 返回本周一
     */
    public static Date getTheBeginDateOfCurrentWeek(){
    	Date date = new Date();
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //1 - Sunday 2-Monday
        if( dayInWeek ==1 ){
            calendar.add(Calendar.DAY_OF_MONTH, -6);
        }else {
            calendar.add(Calendar.DAY_OF_MONTH, -(dayInWeek-2));
        }
        
        Date beginDate = calendar.getTime();
        return new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
    }
    
    /**
     * 修正周期为周一到周天
     * @param date 源日期
     * @param days 加减天数
     * @return 返回下周一
     */
    public static Date getDateByParam(Date date, int days){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
    	
    	Date beginDate = calendar.getTime();
    	return new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
    }
    
    /**
     * 修正新的统计周期，起始日期为参数所在周的周一
     * @param refreshDate
     * @return 参数 refreshDate所在周的周一
     */
    public static String getTheBeginDateOfRefreshDate(Date refreshDate){
        // 0 sunday
        Date beginDate = refreshDate;
        if( refreshDate.getDay() == 0 ){
            beginDate = new Date(refreshDate.getTime() - 6 * 24 * 60 * 60 * 1000);
        }else {
            beginDate = new Date(refreshDate.getTime() - (refreshDate.getDay()-1) * 24 * 60 * 60 * 1000);
        }
        beginDate = new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
        return formatter_1.format(beginDate);
    }
    
    /**
     * 修正新的统计周期，起始日期为参数所在周的周天
     * @param refreshDate
     * @return 参数 refreshDate所在周的周天
     */
    public static String getTheEndDateOfRefreshDate(Date refreshDate){
        // 0 sunday
        Date endDate = getGenerateWeeklyReportDate(refreshDate);
        return formatter_1.format(endDate);
    }
    
    /**
     * 修正新的统计周期，起始日期为参数所在周的周天
     * @param refreshDate
     * @return 参数 refreshDate所在周的周天
     */
    public static String getTheEndDateOfRefreshDate4ReportTable(Date refreshDate){
    	// 0 sunday
    	Date endDate = getGenerateWeeklyReportDate(refreshDate);
    	endDate = new Date(endDate.getTime() + 1 * 24 * 60 * 60 * 1000);
    	return formatter_1.format(endDate);
    }
    
    public static String getMonthInCN(Date chooseDate){
        String monthInCN = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(chooseDate);
        int month = cal.get(Calendar.MONTH)+1;
        switch(month){
        case 1:
        	monthInCN = "一月";
        	break;
        case 2:
        	monthInCN = "二月";
        	break;
        case 3:
        	monthInCN = "三月";
        	break;
        case 4:
        	monthInCN = "四月";
        	break;
        case 5:
        	monthInCN = "五月";
        	break;
        case 6:
        	monthInCN = "六月";
        	break;
        case 7:
        	monthInCN = "七月";
        	break;
        case 8:
        	monthInCN = "八月";
        	break;
        case 9:
        	monthInCN = "九月";
        	break;
        case 10:
        	monthInCN = "十月";
        	break;
        case 11:
        	monthInCN = "十一月";
        	break;
        case 12:
        	monthInCN = "十二月";
        	break;
        }
        return monthInCN;
    }
    
    /**
     * 调整周期为周一到周日，获取参数日期所在月份的第一个周期区段
     * @param chooseDate
     * @return 参数日期所在月份的第一个周期区段
     */
    public static String getMonthInRateBeginDuration(Date chooseDate){
    	Date beginDateInMonth = new Date(chooseDate.getYear(),chooseDate.getMonth(),1);
    	Date beginDate = null;
    	if( beginDateInMonth.getDay() == 1 ){
            beginDate = beginDateInMonth;
        }else if( beginDateInMonth.getDay() == 0 ){
            beginDate = new Date(beginDateInMonth.getTime() + 1 * 24 * 60 * 60 * 1000);
        }else {
            beginDate = new Date(beginDateInMonth.getTime() + (8-beginDateInMonth.getDay()) * 24 * 60 * 60 * 1000);
        }
        beginDate = new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
        Date endDate = new Date(beginDate.getTime()+(6 * 24 * 60 * 60 * 1000));
        return formatter_1.format(beginDate)+"-"+formatter_1.format(endDate);
    }
    
    /**
     * 调整周期为周一到周日，获取参数日期所在月份的最后一个周期区段
     * @param chooseDate
     * @return 参数日期所在月份的最后一个周期区段
     */
    public static String getMonthInRateEndDuration(Date chooseDate){
    	Date endDateInMonth = new Date(chooseDate.getYear(),chooseDate.getMonth()+1,1);
    	endDateInMonth = new Date(endDateInMonth.getTime() - 1* 24 * 60 * 60 * 1000);
    	
    	Date startDayInEndDuration = null;
    	if( endDateInMonth.getDay() == 0 ){
    	    startDayInEndDuration = new Date(endDateInMonth.getTime() - 6 * 24 * 60 * 60 * 1000);
        }else {
            startDayInEndDuration = new Date(endDateInMonth.getTime() - (endDateInMonth.getDay()-1) * 24 * 60 * 60 * 1000);
        }
    	startDayInEndDuration = new Date(startDayInEndDuration.getYear(),startDayInEndDuration.getMonth(),startDayInEndDuration.getDate());
    	Date endDayInEndDuration = new Date(startDayInEndDuration.getTime()+(6 * 24 * 60 * 60 * 1000));
        return formatter_1.format(startDayInEndDuration)+"-"+formatter_1.format(endDayInEndDuration);
    }
    
    public static Date getHomeCollectionBegionDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //1 - Sunday 2-Monday
        Date beginDate = date;
        if( dayInWeek >= 2 ){
            beginDate = new Date(date.getTime() - (dayInWeek-2+7) * 24 * 60 * 60 * 1000 );
        }else {
            beginDate = new Date(date.getTime() - 13 * 24 * 60 * 60 * 1000);
        }
        beginDate = new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
        return beginDate;
    }
    
    public static Date getExportHomeWeeklyBegionDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //1 - Sunday 2-Monday
        Date beginDate = date;
        if( dayInWeek >= 2 ){
            beginDate = new Date(date.getTime() - (dayInWeek-2) * 24 * 60 * 60 * 1000 );
        }else {
            beginDate = new Date(date.getTime() - 6 * 24 * 60 * 60 * 1000);
        }
        beginDate = new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
        return beginDate;
    }
    
    public static Date getHomeWeeklyReportBegionDate(){
        return getHomeWeeklyReportBegionDate(new Date());
    }
    
    public static Date getHomeWeeklyReportBegionDate(Date reportDate){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(reportDate);
    	int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
//    	1 - Sunday 2-Monday
    	if( dayInWeek < 5 && dayInWeek >= 2 ){
    		reportDate = new Date(reportDate.getTime() - 7 * 24 * 60 * 60 * 1000 );
    	}
    	
    	return getHomeCollectionBegionDate(reportDate);
    }
    
    public static String getEndDurationByStartDate(String startDate) throws ParseException{
    	Date startDate_d = formatter_1.parse(startDate);
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(startDate_d);
    	cal.add(Calendar.DAY_OF_YEAR,-7*11);
    	Date last12StartDate = cal.getTime();
    	
    	Date last12EndDate = new Date(last12StartDate.getTime() + 6 * 24 * 60 * 60 * 1000);
    	return formatter_1.format(last12StartDate)+"-"+formatter_1.format(last12EndDate);
    }
    
    public static String populateDuration( Date beginDate, Date endDate ){
    	return new StringBuffer(formatter_1.format(beginDate)).append("-").append(formatter_1.format(endDate)).toString();
    }
    
    public static String getAutoHome12WeeksBeginDuration(){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.add(Calendar.DATE, -14);
    	
    	return getHome12WeeksBeginDuration(cal.getTime());
    }
    
    public static String getAutoHome12WeeksEndDuration() throws ParseException{
    	String beginDuration = getAutoHome12WeeksBeginDuration();
    	
    	Date startDate_d = formatter_1.parse(beginDuration.substring(0, 10));
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(startDate_d);
    	cal.add(Calendar.DAY_OF_YEAR,-7*11);
    	
    	Date last12StartDate = cal.getTime();
    	cal.add(Calendar.DATE, 6);
    	Date last12EndDate = cal.getTime();
    	return formatter_1.format(last12StartDate)+"-"+formatter_1.format(last12EndDate);
    }
    
    public static String getThursdayHome12WeeksBeginDuration(){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(new Date());
    	cal.add(Calendar.DATE, -7);
    	
    	return getHome12WeeksBeginDuration(cal.getTime());
    }
    
    public static String getThursdayHome12WeeksEndDuration() throws ParseException{
    	String beginDuration = getThursdayHome12WeeksBeginDuration();
    	
    	Date startDate_d = formatter_1.parse(beginDuration.substring(0, 10));
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(startDate_d);
    	cal.add(Calendar.DAY_OF_YEAR,-7*11);
    	
    	Date last12StartDate = cal.getTime();
    	cal.add(Calendar.DATE, 6);
    	Date last12EndDate = cal.getTime();
    	return formatter_1.format(last12StartDate)+"-"+formatter_1.format(last12EndDate);
    }
    
    public static String getThursdayHome12WeeksEndDuration(String startDuration) throws ParseException{
    	
    	Date startDate_d = formatter_1.parse(startDuration.substring(0, 10));
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(startDate_d);
    	cal.add(Calendar.DAY_OF_YEAR,-7*11);
    	
    	Date last12StartDate = cal.getTime();
    	cal.add(Calendar.DATE, 6);
    	Date last12EndDate = cal.getTime();
    	return formatter_1.format(last12StartDate)+"-"+formatter_1.format(last12EndDate);
    }
    
    public static String getThursdayHomeLast2WeekEndDuration(String startDuration) throws ParseException{
    	
    	Date startDate_d = formatter_1.parse(startDuration.substring(0, 10));
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(startDate_d);
    	cal.add(Calendar.DAY_OF_YEAR,-7);
    	
    	Date last2StartDate = cal.getTime();
    	cal.add(Calendar.DATE, 6);
    	Date last2EndDate = cal.getTime();
    	return formatter_1.format(last2StartDate)+"-"+formatter_1.format(last2EndDate);
    }
    
    public static String getHome12WeeksBeginDuration(Date date){
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.setFirstDayOfWeek(Calendar.MONDAY);
    	cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    	Date beginDate = cal.getTime();
    	
    	cal.add(Calendar.DATE, 6);
    	Date endDate = cal.getTime();
    	return formatter_1.format(beginDate)+"-"+formatter_1.format(endDate);
    }
    public static String getHome12WeeksEndDuration(Date date) throws ParseException{
    	String beginDuration = getHome12WeeksBeginDuration(date);
    	
    	Date startDate_d = formatter_1.parse(beginDuration.substring(0, 10));
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(startDate_d);
    	cal.add(Calendar.DAY_OF_YEAR,-7*11);
    	
    	Date last12StartDate = cal.getTime();
    	cal.add(Calendar.DATE, 6);
    	Date last12EndDate = cal.getTime();
    	return formatter_1.format(last12StartDate)+"-"+formatter_1.format(last12EndDate);
    }
    
    public static void main(String[] args){
		try {
			Date test = new Date();
			test = new Date(test.getTime() - 0 * 24 * 60 * 60 * 1000);
			System.out.println(test);
            System.out.println(getHome12WeeksBeginDuration(test));
            System.out.println(getHome12WeeksEndDuration(test));
            System.out.println(getAutoHome12WeeksBeginDuration());
            System.out.println(getAutoHome12WeeksEndDuration());
            System.out.println(getHomeWeeklyReportBegionDate());
            System.out.println(getHomeCollectionBegionDate(new Date()));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
