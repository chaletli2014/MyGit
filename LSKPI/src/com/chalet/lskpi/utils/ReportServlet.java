package com.chalet.lskpi.utils;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.chalet.lskpi.service.ChestSurgeryService;
import com.chalet.lskpi.service.HomeService;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.service.PediatricsService;
import com.chalet.lskpi.service.RespirologyService;
import com.chalet.lskpi.service.UserService;

/**
 * @author Chalet
 * @version 创建时间：2013年12月5日 上午12:06:58
 * 类说明
 */

public class ReportServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;  
    
    private ReportThread myThread1;
    private PDFReportThread pdfThread;
    private ThreadChecker threadChecker;
    private Logger logger = Logger.getLogger(ReportServlet.class);
    
    public ReportServlet(){  
    }  
  
    public void init(){
    	
        String basePath = getServletContext().getRealPath("/");
        String contextPath = CustomizedProperty.getContextProperty("host", "http://localhost:8080");
        
        logger.info("init the report servlet, basePath is " + basePath + ",contextPath is " + contextPath);
        UserService userService = (UserService)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("userService");
        PediatricsService pediatricsService = (PediatricsService)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("pediatricsService");
        RespirologyService respirologyService = (RespirologyService)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("respirologyService");
        ChestSurgeryService chestSurgeryService = (ChestSurgeryService)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("chestSurgeryService");
        HospitalService hospitalService = (HospitalService)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("hospitalService");
        HomeService homeService = (HomeService)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("homeService");
        
        if (myThread1 == null) {
            try{
                myThread1 = new ReportThread(basePath,userService,pediatricsService,respirologyService,chestSurgeryService,hospitalService,homeService,contextPath);
                myThread1.setName("reportThread");
                myThread1.start();
                
                threadChecker = new ThreadChecker(basePath,userService,pediatricsService,respirologyService,chestSurgeryService,hospitalService,homeService,contextPath,myThread1);
                threadChecker.setName("threadChecker");
                threadChecker.start();
            }catch(Exception e){
                logger.error("fail to init the thread,",e);
            }
        }
        if (pdfThread == null) {
        	try{
        		pdfThread = new PDFReportThread(basePath,userService,pediatricsService,respirologyService,chestSurgeryService,hospitalService,homeService,contextPath);
        		pdfThread.setName("PDFReportThread");
        		pdfThread.start();
        	}catch(Exception e){
        		logger.error("fail to init the pdf thread,",e);
        	}
        }
    }  
  
    public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)  
        throws ServletException, IOException{  
    	
    }
  
    public void destory(){  
        if (myThread1 != null && myThread1.isInterrupted()) {  
            myThread1.interrupt();  
        }  
        if (pdfThread != null && pdfThread.isInterrupted()) {  
        	pdfThread.interrupt();  
        }  
    }
}