package com.chalet.lskpi.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * yyyy.MM.dd
	 */
    private static SimpleDateFormat formatter_1 = new SimpleDateFormat("yyyy.MM.dd");
    
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
    
    public static String getLastThursDay(){
        Date date = new Date();
        return getLastThursDay(date);
    }
    
    public static String getLastThursDay(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date lastThursDay = getLastThursDay_Date(date);
        return formatter.format(lastThursDay);
    }
    
    public static Date getLastThursDay_Date(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        // 0 sunday
        if( date.getDay() == 4 ){
            return date;
        }else if( date.getDay() < 4 ){
            return new Date(date.getTime() - (date.getDay()+3)*24*60*60*1000);
        }else{
            return new Date(date.getTime() - (date.getDay()-4)*24*60*60*1000);
        }
    }
    
    public static String getThursDayOfParamDate(Date date){
        
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        // 0 sunday
        if( date.getDay() >= 4 ){
            return formatter.format(new Date(date.getTime() + (11-date.getDay())*24*60*60*1000));
        }else{
            return formatter.format(new Date(date.getTime() + (4-date.getDay())*24*60*60*1000));
        }
    }
    
    public static String getWeeklyDuration(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        StringBuffer sb = new StringBuffer("");
        if( date.getDay() <= 3 ){
            sb.append(formatter.format(new Date(date.getTime() - (date.getDay()+3)*24*60*60*1000)))
            .append("-")
            .append(formatter.format(new Date(date.getTime() + (3-date.getDay())*24*60*60*1000)));
        }else{
            sb.append(formatter.format(new Date(date.getTime() - (date.getDay()-4)*24*60*60*1000)))
            .append("-")
            .append(formatter.format(new Date(date.getTime() + (10-date.getDay())*24*60*60*1000)));
        }
        return sb.toString();
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
    
    public static Date getLastThursDayOfDataFormat(){
    	Date date = new Date();
    	Date returnDate = new Date();
    	// 0 sunday
    	if( date.getDay() == 4 ){
    		returnDate =  date;
    	}else if( date.getDay() < 4 ){
    		returnDate =  new Date(date.getTime() - (date.getDay()+3)*24*60*60*1000);
    	}else if( date.getDay() > 4){
    		returnDate =  new Date(date.getTime() - (date.getDay()-4)*24*60*60*1000);
    	}else{
    		returnDate =  date;
    	}
    	
    	return new Date(returnDate.getYear(),returnDate.getMonth(),returnDate.getDate());
    }
    
    public static Date getGenerateWeeklyReportDate(Date refreshDate){
        Date endDate = refreshDate;
        if( refreshDate.getDay() <= 3 ){
            endDate = new Date(refreshDate.getTime() + (3-refreshDate.getDay()) * 24 * 60 * 60 * 1000);
        }else {
            endDate = new Date(refreshDate.getTime() + (10-refreshDate.getDay()) * 24 * 60 * 60 * 1000);
        }
        return new Date(endDate.getYear(),endDate.getMonth(),endDate.getDate());
    }
    
    public static Date getGenerateWeeklyReportDate(){
    	Date date = new Date();
    	Date returnDate = new Date();
    	// 0 sunday
    	if( date.getDay() < 4 ){
    		returnDate = new Date(date.getTime() - (date.getDay()+4)*24*60*60*1000);
    	}else if( date.getDay() >= 4){
    		returnDate = new Date(date.getTime() - (date.getDay()-3)*24*60*60*1000);
    	}
    	
    	return new Date(returnDate.getYear(),returnDate.getMonth(),returnDate.getDate());
    }
    
    public static Date getTheBeginDateOfCurrentWeek(){
    	Date date = new Date();
    	Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //1 - Sunday 2-Monday
        Date beginDate = date;
        if( dayInWeek >= 5 ){
            beginDate = new Date(date.getTime() - (dayInWeek-5) * 24 * 60 * 60 * 1000);
        }else {
            beginDate = new Date(date.getTime() - (dayInWeek+2) * 24 * 60 * 60 * 1000);
        }
        beginDate = new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
        return beginDate;
    }
    
    public static String getTheBeginDateOfRefreshDate(Date refreshDate){
        // 0 sunday
        Date beginDate = refreshDate;
        if( refreshDate.getDay() <= 3 ){
            beginDate = new Date(refreshDate.getTime() - (refreshDate.getDay()+3) * 24 * 60 * 60 * 1000);
        }else {
            beginDate = new Date(refreshDate.getTime() - (refreshDate.getDay()-4) * 24 * 60 * 60 * 1000);
        }
        beginDate = new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
        return formatter_1.format(beginDate);
    }
    
    public static String getTheEndDateOfRefreshDate(Date refreshDate){
        // 0 sunday
        Date endDate = getGenerateWeeklyReportDate(refreshDate);
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
    
    public static String getMonthInRateBeginDuration(Date chooseDate){
    	Date beginDateInMonth = new Date(chooseDate.getYear(),chooseDate.getMonth(),1);
    	Date beginDate = null;
    	if( beginDateInMonth.getDay() <= 4 ){
            beginDate = new Date(beginDateInMonth.getTime() + (4-beginDateInMonth.getDay()) * 24 * 60 * 60 * 1000);
        }else {
            beginDate = new Date(beginDateInMonth.getTime() + (11-beginDateInMonth.getDay()) * 24 * 60 * 60 * 1000);
        }
        beginDate = new Date(beginDate.getYear(),beginDate.getMonth(),beginDate.getDate());
        Date endDate = new Date(beginDate.getTime()+(6 * 24 * 60 * 60 * 1000));
        return formatter_1.format(beginDate)+"-"+formatter_1.format(endDate);
    }
    
    public static String getMonthInRateEndDuration(Date chooseDate){
    	Date endDateInMonth = new Date(chooseDate.getYear(),chooseDate.getMonth()+1,1);
    	endDateInMonth = new Date(endDateInMonth.getTime() - 1* 24 * 60 * 60 * 1000);
    	
    	Date endThursDay = null;
    	if( endDateInMonth.getDay() < 4 ){
    		endThursDay = new Date(endDateInMonth.getTime() - (endDateInMonth.getDay()+3) * 24 * 60 * 60 * 1000);
        }else {
        	endThursDay = new Date(endDateInMonth.getTime() - (endDateInMonth.getDay()-4) * 24 * 60 * 60 * 1000);
        }
    	endThursDay = new Date(endThursDay.getYear(),endThursDay.getMonth(),endThursDay.getDate());
    	Date endWenDay = new Date(endThursDay.getTime()+(6 * 24 * 60 * 60 * 1000));
        return formatter_1.format(endThursDay)+"-"+formatter_1.format(endWenDay);
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
    
    public static Date getHomeWeeklyReportBegionDate(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        //1 - Sunday 2-Monday
        if( dayInWeek < 5 && dayInWeek >= 2 ){
            date = new Date(date.getTime() - 7 * 24 * 60 * 60 * 1000 );
        }
        
        return getHomeCollectionBegionDate(date);
    }
    
    public static void main(String[] args){
        System.out.println(getHomeWeeklyReportBegionDate());
    }
}
