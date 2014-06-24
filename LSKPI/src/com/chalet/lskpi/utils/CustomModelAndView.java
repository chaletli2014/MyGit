package com.chalet.lskpi.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

/**
 * @author Chalet
 * @version 创建时间：2014年3月23日 下午9:25:38
 * 类说明
 */

public class CustomModelAndView extends ModelAndView{

	private HttpServletRequest request;
	
	public CustomModelAndView(HttpServletRequest request){
		this.request = request;
	}
	
	@Override
	public void setViewName(String viewName){
		String requestHeader = request.getHeader("user-agent");
		boolean isFromMobile = BrowserUtils.isMoblie(requestHeader);
		if( isFromMobile ){
			super.setViewName(viewName);
		}else{
			super.setViewName("/web/"+viewName);
		}
	}
}
