package com.chalet.lskpi.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


public class FileDownloadFilter implements Filter{

	Logger logger = Logger.getLogger(FileDownloadFilter.class);
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        String currentURL = request.getRequestURI();

        String fileName = currentURL.substring(currentURL.lastIndexOf("/")+1);
        String directoryPath = currentURL.substring(0,currentURL.lastIndexOf("/"));
        
        String directoryName = directoryPath.substring(directoryPath.lastIndexOf("/")+1);
        
        logger.info(String.format("file download filter, currentURL is %s, fileName is %s, directory is %s", currentURL,fileName,directoryName));
        
        
        filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
