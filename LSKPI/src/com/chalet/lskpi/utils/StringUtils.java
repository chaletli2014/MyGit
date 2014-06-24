package com.chalet.lskpi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.chalet.lskpi.model.UserInfo;

/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:38:55
 * 类说明
 */

public class StringUtils {
	
	private static Logger logger = Logger.getLogger(StringUtils.class);

	public static int getIntegerFromString(String value){
		int result = 0;
		if( null != value && !"".equalsIgnoreCase(value) ){
			try{
				result = Integer.parseInt(value);
			}catch(Exception e){
				logger.error("fail to convert string to int,",e);
			}
		}
		return result;
	}
	
	public static double getDoubleFromString(String value){
	    double result = 0;
	    if( null != value && !"".equalsIgnoreCase(value) ){
	        try{
	            result = Double.parseDouble(value);
	        }catch(Exception e){
	            logger.error("fail to convert string to int,",e);
	        }
	    }
	    return result;
	}
	
	public static String getTheZHValueOfRegionCenter(String regionCenter){
	    
	    if( LsAttributes.BR_NAME_CENTRAL.equalsIgnoreCase(regionCenter) ){
            return LsAttributes.BR_NAME_CENTRAL_ZH;
        }else if( LsAttributes.BR_NAME_EAST1.equalsIgnoreCase(regionCenter) ){
            return LsAttributes.BR_NAME_EAST1_ZH;
        }else if( LsAttributes.BR_NAME_EAST2.equalsIgnoreCase(regionCenter) ){
            return LsAttributes.BR_NAME_EAST2_ZH;
        }else if( LsAttributes.BR_NAME_NORTH.equalsIgnoreCase(regionCenter) ){
            return LsAttributes.BR_NAME_NORTH_ZH;
        }else if( LsAttributes.BR_NAME_SOUTH.equalsIgnoreCase(regionCenter) ){
            return LsAttributes.BR_NAME_SOUTH_ZH;
        }else if( LsAttributes.BR_NAME_WEST.equalsIgnoreCase(regionCenter) ){
            return LsAttributes.BR_NAME_WEST_ZH;
        }else{
            return regionCenter;
        }
	}
	
	public static String getFileSubName(UserInfo user){
		String userLevel = user.getLevel();
        switch(userLevel){
        	case LsAttributes.USER_LEVEL_RSD:
        		return userLevel+"-"+user.getRegionCenter();
        	case LsAttributes.USER_LEVEL_RSM:
        		return userLevel+"-"+user.getRegion();
        	case LsAttributes.USER_LEVEL_DSM:
        		return userLevel+"-"+user.getName();
        	case LsAttributes.USER_LEVEL_BM:
        		return user.getLevel();
        	default:
        		return user.getLevel()+"-"+user.getTelephone();
        }
	}
	
	public static boolean isEmail(String strEmail) {
	    Pattern p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	    Matcher m = p.matcher(strEmail);
	    return m.find();
	}
	
	public static String decode(String content){
        String result = "";
        for( int i = 0; i < content.length(); i++ ){
            int codePoint = Character.codePointAt(content, i);
            result += Character.toChars(codePoint-2)[0];
        }
        return result;
    }
}
