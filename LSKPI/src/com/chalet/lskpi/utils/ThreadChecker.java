package com.chalet.lskpi.utils;

import org.apache.log4j.Logger;

import com.chalet.lskpi.service.ChestSurgeryService;
import com.chalet.lskpi.service.HomeService;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.service.PediatricsService;
import com.chalet.lskpi.service.RespirologyService;
import com.chalet.lskpi.service.UserService;

public class ThreadChecker extends Thread {
    private ReportThread thread1 = null;
    
    private String basePath = "";
    private String contextPath = "";
    private UserService userService;
    private PediatricsService pediatricsService;
    private RespirologyService respirologyService;
    private ChestSurgeryService chestSurgeryService;
    private HospitalService hospitalService;
    private HomeService homeService;
    
    private long timeDuration = Long.parseLong(CustomizedProperty.getContextProperty("restart_report_thread_timeout", "600000"));
    
    private Logger logger = Logger.getLogger(ReportThread.class);
    
    
    public ThreadChecker(){
        
    }
    public ThreadChecker(String basePath, UserService userService, PediatricsService pediatricsService, RespirologyService respirologyService, ChestSurgeryService chestSurgeryService, HospitalService hospitalService, HomeService homeService, String contextPath, ReportThread thread1){
        this.thread1 = thread1;
        this.basePath = basePath;
        this.userService = userService;
        this.pediatricsService = pediatricsService;
        this.respirologyService = respirologyService;
        this.chestSurgeryService = chestSurgeryService;
        this.hospitalService = hospitalService;
        this.homeService = homeService;
        this.contextPath = contextPath;
    }
    public void run() {
        while (!this.isInterrupted()) {
            try{
                logger.info(String.format("-------------------------------the status report thread is %s--------------------------------", thread1.getState().name()));
                Thread.sleep(timeDuration);
                long lastTaskTime = thread1.getTaskTime();
                long nowTime = System.currentTimeMillis();
                logger.info(String.format("------the time past %s after the latest task, lastTaskTime is %s", nowTime-lastTaskTime,lastTaskTime));
                if( !thread1.isAlive() 
                        || ((nowTime-lastTaskTime) > timeDuration && lastTaskTime != 0 ) ){
                    logger.warn(String.format("report thread is not alive, it is %s, restart it", thread1.getState().name()));
                    thread1.interrupt();
                    
                    thread1 = new ReportThread(basePath,userService,pediatricsService,respirologyService,chestSurgeryService,hospitalService,homeService,contextPath);
                    thread1.setRestart(true);
                    thread1.start();
                }
            }catch(Exception e){
                logger.error("fail to run the threadchecker",e);
                this.interrupt();
            }
        }
        
    }
}
