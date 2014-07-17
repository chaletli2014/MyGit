package com.chalet.lskpi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.chalet.lskpi.model.ExportDoctor;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.MobileCHEDailyData;
import com.chalet.lskpi.model.MobilePEDDailyData;
import com.chalet.lskpi.model.MobileRESDailyData;
import com.chalet.lskpi.model.MonthlyData;
import com.chalet.lskpi.model.MonthlyInRateData;
import com.chalet.lskpi.model.MonthlyRatioData;
import com.chalet.lskpi.model.PediatricsData;
import com.chalet.lskpi.model.ReportFileObject;
import com.chalet.lskpi.model.RespirologyData;
import com.chalet.lskpi.model.RespirologyExportData;
import com.chalet.lskpi.model.TopAndBottomRSMData;
import com.chalet.lskpi.model.UserInfo;
import com.chalet.lskpi.model.WebUserInfo;
import com.chalet.lskpi.service.ChestSurgeryService;
import com.chalet.lskpi.service.HomeService;
import com.chalet.lskpi.service.HospitalService;
import com.chalet.lskpi.service.PediatricsService;
import com.chalet.lskpi.service.RespirologyService;
import com.chalet.lskpi.service.UserService;
import com.chalet.lskpi.utils.BirtReportUtils;
import com.chalet.lskpi.utils.BrowserUtils;
import com.chalet.lskpi.utils.CustomizedProperty;
import com.chalet.lskpi.utils.DateUtils;
import com.chalet.lskpi.utils.EmailUtils;
import com.chalet.lskpi.utils.FileUtils;
import com.chalet.lskpi.utils.LsAttributes;
import com.chalet.lskpi.utils.LsKPIModelAndView;
import com.chalet.lskpi.utils.ReportUtils;
import com.chalet.lskpi.utils.StringUtils;

@Controller
public class ReportController extends BaseController{
    private Logger logger = Logger.getLogger(ReportController.class);
	
    @Autowired
    @Qualifier("respirologyService")
    private RespirologyService respirologyService;
    
    @Autowired
    @Qualifier("pediatricsService")
    private PediatricsService pediatricsService;
    
    @Autowired
    @Qualifier("hospitalService")
    private HospitalService hospitalService;
    
    @Autowired
    @Qualifier("userService")
    private UserService userService;
    
    @Autowired
    @Qualifier("homeService")
    private HomeService homeService;
    
    @Autowired
    @Qualifier("chestSurgeryService")
    private ChestSurgeryService chestSurgeryService;
    
    private String populateRecipeTypeValue(String recipeTypeValue){
        String returnValue = recipeTypeValue;
        if( null != recipeTypeValue ){
            if( "1".equalsIgnoreCase(recipeTypeValue) || "1.0".equalsIgnoreCase(recipeTypeValue) ){
                returnValue = "一次门急诊处方1天的雾化量";
            }else if( "2".equalsIgnoreCase(recipeTypeValue) || "2.0".equalsIgnoreCase(recipeTypeValue) ){
                returnValue = "一次门急诊处方2天的雾化量";
            }else if( "3".equalsIgnoreCase(recipeTypeValue) || "3.0".equalsIgnoreCase(recipeTypeValue) ){
                returnValue = "一次门急诊处方3天的雾化量";
            }else if( "4".equalsIgnoreCase(recipeTypeValue) || "4.0".equalsIgnoreCase(recipeTypeValue) ){
                returnValue = "一次门急诊处方4天的雾化量";
            }else if( "5".equalsIgnoreCase(recipeTypeValue) || "5.0".equalsIgnoreCase(recipeTypeValue) ){
                returnValue = "一次门急诊处方5天的雾化量";
            }else if( "6".equalsIgnoreCase(recipeTypeValue) || "6.0".equalsIgnoreCase(recipeTypeValue) ){
                returnValue = "一次门急诊处方6天的雾化量";
            }else if( "7".equalsIgnoreCase(recipeTypeValue) || "7.0".equalsIgnoreCase(recipeTypeValue) ){
                returnValue = "一次门急诊处方7天的雾化量";
            }
         }
        return returnValue;
    }
    
    @RequestMapping("/doDownloadDailyData")
    public String doDownloadDailyData(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	logger.info("download the daily res data..");
    	FileOutputStream fOut = null;
    	String fileName = null;
    	String fromWeb = request.getParameter("fromWeb");
        try{
            String chooseDate = request.getParameter("chooseDate");
            String chooseDate_end = request.getParameter("chooseDate_end");
            
            SimpleDateFormat exportdateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            if( null == chooseDate || "".equalsIgnoreCase(chooseDate) || null == chooseDate_end || "".equalsIgnoreCase(chooseDate_end) ){
            	logger.error(String.format("the choose date is %s, the choose end date is %s", chooseDate,chooseDate_end));
            }else{
            	SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
            	Date chooseDate_d = simpledateformat.parse(chooseDate);
            	Date chooseDate_end_d = simpledateformat.parse(chooseDate_end);
            	
            	String department = request.getParameter("department");
            	logger.info(String.format("begin to get the res data of department %s, from %s to %s", department,chooseDate,chooseDate_end));
            	if( "1".equalsIgnoreCase(department) ){
            		List<RespirologyData> dbResData = respirologyService.getRespirologyDataByDate(chooseDate_d,chooseDate_end_d);
            		
            		File resDir = new File(request.getRealPath("/") + "dailyResReport/");
            		if( !resDir.exists() ){
            			resDir.mkdir();
            		}
            		fileName = "dailyResReport/呼吸科原始数据-"+simpledateformat.format(chooseDate_d) + ".xls";
            		File tmpFile = new File(request.getRealPath("/") + fileName);
            		if( !tmpFile.exists() ){
            			tmpFile.createNewFile();
            		}
            		
            		fOut = new FileOutputStream(tmpFile);
            		
            		HSSFWorkbook workbook = new HSSFWorkbook();
            		workbook.createSheet("日报");
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int currentRowNum = 0;
                    
                    //build the header
                    HSSFRow row = sheet.createRow(currentRowNum++);
                    row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("编号");
                    row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("录入日期");
                    row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("医院编号");
                    row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("医院名称");
                    row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("当日病房病人人数");
                    row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("当日病房内AECOPD病人数");
                    row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("当日雾化病人数");
                    row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("当日雾化令舒病人数");
                    row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表ETMSCode");
                    row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表姓名");
                    row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue("所属DSM");
                    row.createCell(11, XSSFCell.CELL_TYPE_STRING).setCellValue("所属Region");
                    row.createCell(12, XSSFCell.CELL_TYPE_STRING).setCellValue("所属RSM Region");
                    row.createCell(13, XSSFCell.CELL_TYPE_STRING).setCellValue("1mg QD");
                    row.createCell(14, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg QD");
                    row.createCell(15, XSSFCell.CELL_TYPE_STRING).setCellValue("1mg TID");
                    row.createCell(16, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg BID");
                    row.createCell(17, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg TID");
                    row.createCell(18, XSSFCell.CELL_TYPE_STRING).setCellValue("3mg BID");
                    row.createCell(19, XSSFCell.CELL_TYPE_STRING).setCellValue("4mg BID");
//                    row.createCell(19, XSSFCell.CELL_TYPE_STRING).setCellValue("该医院主要处方方式");
                    
                    for( RespirologyData resData : dbResData ){
                    	row = sheet.createRow(currentRowNum++);
                    	row.createCell(0, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(currentRowNum-1);
                        row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue(exportdateformat.format(resData.getCreatedate()));
                        row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getHospitalCode());
                        row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getHospitalName());
                        row.createCell(4, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getPnum());
                        row.createCell(5, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getAenum());
                        row.createCell(6, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getWhnum());
                        row.createCell(7, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getLsnum());
                        row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getSalesETMSCode());
                        row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getSalesName());
                        row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getDsmName());
                        row.createCell(11, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getRegion());
                        row.createCell(12, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getRsmRegion());
                        row.createCell(13, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getOqd());
                        row.createCell(14, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getTqd());
                        row.createCell(15, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getOtid());
                        row.createCell(16, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getTbid());
                        row.createCell(17, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getTtid());
                        row.createCell(18, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getThbid());
                        row.createCell(19, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getFbid());
//                        row.createCell(19, XSSFCell.CELL_TYPE_STRING).setCellValue(populateRecipeTypeValue(resData.getRecipeType()));
                    }
                    workbook.write(fOut);
            	}else if( "2".equalsIgnoreCase(department) ){
            		
            		List<PediatricsData> dbPedData = pediatricsService.getPediatricsDataByDate(chooseDate_d,chooseDate_end_d);
            		
            		File pedDir = new File(request.getRealPath("/") + "dailyPedReport/");
            		if( !pedDir.exists() ){
            			pedDir.mkdir();
            		}
            		fileName = "dailyPedReport/儿科原始数据-"+simpledateformat.format(chooseDate_d) + ".xls";
            		File tmpFile = new File(request.getRealPath("/") + fileName);
            		if( !tmpFile.exists() ){
            			tmpFile.createNewFile();
            		}
            		
            		fOut = new FileOutputStream(tmpFile);
            		
            		HSSFWorkbook workbook = new HSSFWorkbook();
            		workbook.createSheet("日报");
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int currentRowNum = 0;
                    
                    //build the header
                    HSSFRow row = sheet.createRow(currentRowNum++);
                    row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("编号");
                    row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("录入日期");
                    row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("医院编号");
                    row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("医院名称");
                    row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("当日门诊人次");
                    row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("当日雾化人次");
                    row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("当日雾化令舒人次");
                    row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表ETMSCode");
                    row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表姓名");
                    row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue("所属DSM");
                    row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue("所属Region");
                    row.createCell(11, XSSFCell.CELL_TYPE_STRING).setCellValue("所属RSM Region");
                    row.createCell(12, XSSFCell.CELL_TYPE_STRING).setCellValue("0.5mg QD");
                    row.createCell(13, XSSFCell.CELL_TYPE_STRING).setCellValue("0.5mg BID");
                    row.createCell(14, XSSFCell.CELL_TYPE_STRING).setCellValue("1mg QD");
                    row.createCell(15, XSSFCell.CELL_TYPE_STRING).setCellValue("1mg BID");
                    row.createCell(16, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg QD");
                    row.createCell(17, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg BID");
                    row.createCell(18, XSSFCell.CELL_TYPE_STRING).setCellValue("该医院主要处方方式");
                    
                    for( PediatricsData pedData : dbPedData ){
                    	row = sheet.createRow(currentRowNum++);
                    	row.createCell(0, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(currentRowNum-1);
                        row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue(exportdateformat.format(pedData.getCreatedate()));
                        row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getHospitalCode());
                        row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getHospitalName());
                        row.createCell(4, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getPnum());
                        row.createCell(5, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getWhnum());
                        row.createCell(6, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getLsnum());
                        row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getSalesETMSCode());
                        row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getSalesName());
                        row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getDsmName());
                        row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getRegion());
                        row.createCell(11, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getRsmRegion());
                        row.createCell(12, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHqd());
                        row.createCell(13, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHbid());
                        row.createCell(14, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getOqd());
                        row.createCell(15, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getObid());
                        row.createCell(16, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getTqd());
                        row.createCell(17, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getTbid());
                        row.createCell(18, XSSFCell.CELL_TYPE_STRING).setCellValue(populateRecipeTypeValue(pedData.getRecipeType()));
                    }
                    workbook.write(fOut);
            	}
            }
        }catch(Exception e){
            logger.error("fail to download the file,",e);
        }finally{
            if( fOut != null ){
                fOut.close();
            }
        }
        request.getSession().setAttribute("dataFile", fileName);
        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    
    @RequestMapping("/doDownloadMonthlyData")
    public String doDownloadMonthlyData(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String fromWeb = request.getParameter("fromWeb");
        logger.info(String.format("download the monthly data.. is from web ? ", fromWeb));
        FileOutputStream fOut = null;
        String fileName = null;
        try{
        	String chooseDate = request.getParameter("chooseDate_monthly");
        	String chooseDate_end = request.getParameter("chooseDate_monthly_end");
        	
            if( null == chooseDate || "".equalsIgnoreCase(chooseDate) ){
                logger.error(String.format("the choose date is %s", chooseDate));
            }else{
                SimpleDateFormat exportdateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            	SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
            	Date chooseDate_d = simpledateformat.parse(chooseDate);
            	Date chooseDate_end_d = simpledateformat.parse(chooseDate_end);
                logger.info(String.format("begin to get the monthly data in %s", chooseDate));
                    
                List<MonthlyData> dbMonthlyData = hospitalService.getMonthlyDataByDate(chooseDate_d,chooseDate_end_d);
                    
                    File pedDir = new File(request.getRealPath("/") + "monthlyData/");
                    if( !pedDir.exists() ){
                        pedDir.mkdir();
                    }
                    fileName = "monthlyData/每月采集数据-"+simpledateformat.format(chooseDate_d) + "-" + simpledateformat.format(chooseDate_end_d) + ".xls";
                    File tmpFile = new File(request.getRealPath("/") + fileName);
                    if( !tmpFile.exists() ){
                        tmpFile.createNewFile();
                    }
                    
                    fOut = new FileOutputStream(tmpFile);
                    
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    workbook.createSheet("每月采集数据");
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int currentRowNum = 0;
                    
                    //build the header
                    HSSFRow row = sheet.createRow(currentRowNum++);
                    row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("编号");
                    row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("录入月份");
                    row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("医院编号");
                    row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("医院名称");
                    row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("儿科门急诊");
                    row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("儿科病房");
                    row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("呼吸科");
                    row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("其他科室");
                    row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表姓名");
                    row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue("所属DSM");
                    row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue("所属RSM Region");
                    row.createCell(11, XSSFCell.CELL_TYPE_STRING).setCellValue("所属Region");
                    for( MonthlyData monthlyData : dbMonthlyData ){
                        row = sheet.createRow(currentRowNum++);
                        row.createCell(0, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(currentRowNum-1);
                        row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue(exportdateformat.format(monthlyData.getCreateDate()));
                        row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getHospitalCode());
                        row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getHospitalName());
                        row.createCell(4, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getPedemernum());
                        row.createCell(5, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getPedroomnum());
                        row.createCell(6, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getResnum());
                        row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getOthernum());
                        row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getOperatorName());
                        row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getDsmName());
                        row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getRsmRegion());
                        row.createCell(11, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getRegion());
                    }
                    workbook.write(fOut);
                }
        }catch(Exception e){
            logger.error("fail to download the file,",e);
        }finally{
            if( fOut != null ){
                fOut.close();
            }
        }
        request.getSession().setAttribute("monthlyDataFile", fileName);
        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    
    @RequestMapping("/doDownloadHomeData")
    public String doDownloadHomeData(HttpServletRequest request, HttpServletResponse response) throws IOException{
        logger.info("download the home data..");
        FileOutputStream fOut = null;
        String fileName = null;
        String fromWeb = request.getParameter("fromWeb");
        try{
            String chooseDate = request.getParameter("chooseDate");
            String chooseDate_end = request.getParameter("chooseDate_end");
            
            SimpleDateFormat exportdateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            if( null == chooseDate || "".equalsIgnoreCase(chooseDate) || null == chooseDate_end || "".equalsIgnoreCase(chooseDate_end) ){
                logger.error(String.format("the choose date is %s, the choose end date is %s", chooseDate,chooseDate_end));
            }else{
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
                Date chooseDate_d = simpledateformat.parse(chooseDate);
                Date chooseDate_end_d = simpledateformat.parse(chooseDate_end);
                
                logger.info(String.format("begin to get the home data from %s to %s", chooseDate,chooseDate_end));
                List<HomeData> homeDataList = homeService.getHomeDataByDate(chooseDate_d, chooseDate_end_d);
                
                File homeDir = new File(request.getRealPath("/") + "homeData/");
                if( !homeDir.exists() ){
                    homeDir.mkdir();
                }
                
                fileName = new StringBuffer("homeData/家庭雾化原始数据-")
                .append(simpledateformat.format(chooseDate_d))
                .append("-")
                .append(simpledateformat.format(chooseDate_end_d))
                .append(".xls")
                .toString();
                
                File tmpFile = new File(request.getRealPath("/") + fileName);
                if( !tmpFile.exists() ){
                    tmpFile.createNewFile();
                }
                
                fOut = new FileOutputStream(tmpFile);
                
                HSSFWorkbook workbook = new HSSFWorkbook();
                workbook.createSheet("家庭雾化数据");
                HSSFSheet sheet = workbook.getSheetAt(0);
                int currentRowNum = 0;
                
                HSSFCellStyle topStyle=workbook.createCellStyle();
                topStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                topStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                topStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                topStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                topStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                topStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle top2Style=workbook.createCellStyle();
                top2Style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                top2Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                top2Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                top2Style.setLeftBorderColor(HSSFColor.BLACK.index);
                top2Style.setRightBorderColor(HSSFColor.BLACK.index);
                
                //build the header
                HSSFRow row = sheet.createRow(currentRowNum++);
                row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                
                row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("区域信息");
                row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                sheet.addMergedRegion(new Region(0, (short)1, 0, (short)4));
                row.getCell(1).setCellStyle(topStyle);
                
                row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("医生信息");
                row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                sheet.addMergedRegion(new Region(0, (short)5, 0, (short)7));
                row.getCell(5).setCellStyle(topStyle);
                
                row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                
                row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue("持续期治疗");
                row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                row.createCell(11, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                sheet.addMergedRegion(new Region(0, (short)9, 0, (short)11));
                row.getCell(9).setCellStyle(topStyle);
                
                row.createCell(12, XSSFCell.CELL_TYPE_STRING).setCellValue("持续期令舒治疗天数（DOT)");
                row.createCell(13, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                row.createCell(14, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                sheet.addMergedRegion(new Region(0, (short)12, 0, (short)14));
                row.getCell(12).setCellStyle(topStyle);
                
                row = sheet.createRow(currentRowNum++);
                
                HSSFCell dateCell = row.createCell(0, XSSFCell.CELL_TYPE_STRING);
                dateCell.setCellValue("录入日期");
                dateCell.setCellStyle(top2Style);
                
                HSSFCell rsdCell = row.createCell(1, XSSFCell.CELL_TYPE_STRING);
                rsdCell.setCellValue("RSD");
                rsdCell.setCellStyle(top2Style);
                
                HSSFCell rsmCell = row.createCell(2, XSSFCell.CELL_TYPE_STRING);
                rsmCell.setCellValue("RSM");
                rsmCell.setCellStyle(top2Style);
                
                HSSFCell dsmCell = row.createCell(3, XSSFCell.CELL_TYPE_STRING);
                dsmCell.setCellValue("DSM");
                dsmCell.setCellStyle(top2Style);
                
                HSSFCell psrCell = row.createCell(4, XSSFCell.CELL_TYPE_STRING);
                psrCell.setCellValue("PSR");
                psrCell.setCellStyle(top2Style);
                
                HSSFCell hosCodeCell = row.createCell(5, XSSFCell.CELL_TYPE_STRING);
                hosCodeCell.setCellValue("目标医院CODE");
                hosCodeCell.setCellStyle(top2Style);
                
                HSSFCell hosNameCell = row.createCell(6, XSSFCell.CELL_TYPE_STRING);
                hosNameCell.setCellValue("目标医院名称");
                hosNameCell.setCellStyle(top2Style);
                
                HSSFCell drNameCell = row.createCell(7, XSSFCell.CELL_TYPE_STRING);
                drNameCell.setCellValue("目标医生");
                drNameCell.setCellStyle(top2Style);
                
                HSSFCell saleNumCell = row.createCell(8, XSSFCell.CELL_TYPE_STRING);
                saleNumCell.setCellValue("卖/赠泵数量");
                saleNumCell.setCellStyle(top2Style);
                
                HSSFCell num1Cell = row.createCell(9, XSSFCell.CELL_TYPE_STRING);
                num1Cell.setCellValue("哮喘*患者人次");
                num1Cell.setCellStyle(top2Style);
                
                HSSFCell num2Cell = row.createCell(10, XSSFCell.CELL_TYPE_STRING);
                num2Cell.setCellValue("处方>=8天的哮喘持续期病人次");
                num2Cell.setCellStyle(top2Style);
                
                HSSFCell num3Cell = row.createCell(11, XSSFCell.CELL_TYPE_STRING);
                num3Cell.setCellValue("持续期病人中推荐使用令舒的人次");
                num3Cell.setCellStyle(top2Style);
                
                HSSFCell num4Cell = row.createCell(12, XSSFCell.CELL_TYPE_STRING);
                num4Cell.setCellValue("8<=DOT<15天，病人次");
                num4Cell.setCellStyle(top2Style);
                
                HSSFCell num5Cell = row.createCell(13, XSSFCell.CELL_TYPE_STRING);
                num5Cell.setCellValue("15<=DOT<30天，病人次");
                num5Cell.setCellStyle(top2Style);
                
                HSSFCell num6Cell = row.createCell(14, XSSFCell.CELL_TYPE_STRING);
                num6Cell.setCellValue("DOT>=30天,病人次");
                num6Cell.setCellStyle(top2Style);
                
                int dateColumnWidth = 15;
                int userColumnWidth = 12;
                int hosColumnWidth = 14;
                int numColumnWidth = 14;
                
                sheet.setColumnWidth(0, dateColumnWidth*256);
                sheet.setColumnWidth(1, userColumnWidth*256);
                sheet.setColumnWidth(2, userColumnWidth*256);
                sheet.setColumnWidth(3, userColumnWidth*256);
                sheet.setColumnWidth(4, userColumnWidth*256);
                sheet.setColumnWidth(5, hosColumnWidth*256);
                sheet.setColumnWidth(6, 18*256);//hospital name
                sheet.setColumnWidth(7, hosColumnWidth*256);
                sheet.setColumnWidth(8, numColumnWidth*256);
                sheet.setColumnWidth(9, numColumnWidth*256);
                sheet.setColumnWidth(10, numColumnWidth*256);
                sheet.setColumnWidth(11, numColumnWidth*256);
                sheet.setColumnWidth(12, numColumnWidth*256);
                sheet.setColumnWidth(13, numColumnWidth*256);
                sheet.setColumnWidth(14, numColumnWidth*256);
                
                HSSFCellStyle numberCellStyle = workbook.createCellStyle();
                numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                
                for( HomeData homeData : homeDataList ){
                    row = sheet.createRow(currentRowNum++);
                    row.createCell(0, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(exportdateformat.format(homeData.getCreateDate()));
                    row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue(homeData.getRegion());
                    row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue(homeData.getRsmRegion());
                    row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue(homeData.getDsmName());
                    row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue(homeData.getSalesName());
                    row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue(homeData.getHospitalCode());
                    row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue(homeData.getHospitalName());
                    row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue(homeData.getDrName());
                    
                    HSSFCell value1Cell = row.createCell(8, XSSFCell.CELL_TYPE_NUMERIC);
                    value1Cell.setCellValue(homeData.getSalenum());
                    value1Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value2Cell = row.createCell(9, XSSFCell.CELL_TYPE_NUMERIC);
                    value2Cell.setCellValue(homeData.getAsthmanum());
                    value2Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value3Cell = row.createCell(10, XSSFCell.CELL_TYPE_NUMERIC);
                    value3Cell.setCellValue(homeData.getLtenum());
                    value3Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value4Cell = row.createCell(11, XSSFCell.CELL_TYPE_NUMERIC);
                    value4Cell.setCellValue(homeData.getLsnum());
                    value4Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value5Cell = row.createCell(12, XSSFCell.CELL_TYPE_NUMERIC);
                    value5Cell.setCellValue(homeData.getEfnum());
                    value5Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value6Cell = row.createCell(13, XSSFCell.CELL_TYPE_NUMERIC);
                    value6Cell.setCellValue(homeData.getFtnum());
                    value6Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value7Cell = row.createCell(14, XSSFCell.CELL_TYPE_NUMERIC);
                    value7Cell.setCellValue(homeData.getLttnum());
                    value7Cell.setCellStyle(numberCellStyle);
                }
                workbook.write(fOut);
            }
        }catch(Exception e){
            logger.error("fail to export the home data file,",e);
        }finally{
            if( fOut != null ){
                fOut.close();
            }
        }
        request.getSession().setAttribute("homeDataFile", fileName);
        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    
    @RequestMapping("/doDownloadWeeklyHomeData")
    public String doDownloadWeeklyHomeData(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	logger.info("download the home weekly data..");
    	FileOutputStream fOut = null;
    	String fileName = null;
    	String fromWeb = request.getParameter("fromWeb");
    	try{
    		String chooseDate = request.getParameter("chooseDate");
    		
    		if( null == chooseDate || "".equalsIgnoreCase(chooseDate) ){
    			logger.error(String.format("the choose date is %s", chooseDate));
    		}else{
    			SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
    			Date chooseDate_d = simpledateformat.parse(chooseDate);
    			
    			logger.info(String.format("begin to get the weekly home data from %s", chooseDate));
    			
    			Date reportBeginDate = DateUtils.getExportHomeWeeklyBegionDate(chooseDate_d);
    			Date reportEndDate = new Date(reportBeginDate.getTime() + 7 * 24 * 60 * 60 * 1000);
    			
    			List<String> allRegionCenters = userService.getAllRegionName();
                List<HomeWeeklyData> allRSMData = new ArrayList<HomeWeeklyData>();
                
                for( String regionCenter : allRegionCenters ){
                    List<HomeWeeklyData> rsmData = homeService.getWeeklyDataByRegion(regionCenter,reportBeginDate);
                    logger.info(String.format("get weekly home data of %s RSM end...", regionCenter));
                    allRSMData.addAll(rsmData);
                }
    			
    			File homeDir = new File(request.getRealPath("/") + "homeData/");
    			if( !homeDir.exists() ){
    				homeDir.mkdir();
    			}
    			
    			fileName = new StringBuffer("homeData/家庭雾化周报-")
    			.append(simpledateformat.format(reportBeginDate))
    			.append("-")
    			.append(simpledateformat.format(reportEndDate))
    			.append(".xls")
    			.toString();
    			
    			File tmpFile = new File(request.getRealPath("/") + fileName);
    			if( !tmpFile.exists() ){
    				tmpFile.createNewFile();
    			}
    			
    			fOut = new FileOutputStream(tmpFile);
    			
    			HSSFWorkbook workbook = new HSSFWorkbook();
    			workbook.createSheet("家庭雾化数据");
    			HSSFSheet sheet = workbook.getSheetAt(0);
    			int currentRowNum = 0;
    			
    			HSSFCellStyle topStyle=workbook.createCellStyle();
    			topStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    			topStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    			topStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    			topStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    			topStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    			topStyle.setRightBorderColor(HSSFColor.BLACK.index);
    			
    			HSSFCellStyle top2Style=workbook.createCellStyle();
    			top2Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    			top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    			top2Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    			top2Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
    			top2Style.setLeftBorderColor(HSSFColor.BLACK.index);
    			top2Style.setRightBorderColor(HSSFColor.BLACK.index);
    			
    			HSSFCellStyle valueStyle = workbook.createCellStyle();
    			valueStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    			valueStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    			valueStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    			valueStyle.setRightBorderColor(HSSFColor.BLACK.index);
    			
    			//build the header
    			HSSFRow row = sheet.createRow(currentRowNum++);
    			row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("层级情况");
    			row.getCell(0).setCellStyle(topStyle);
    			
    			row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("医生情况");
    			row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			sheet.addMergedRegion(new Region(0, (short)1, 0, (short)2));
    			row.getCell(1).setCellStyle(topStyle);
    			
    			row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("处方情况");
    			row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			sheet.addMergedRegion(new Region(0, (short)3, 0, (short)7));
    			row.getCell(3).setCellStyle(topStyle);
    			
    			row = sheet.createRow(currentRowNum++);
    			
    			HSSFCell dateCell = row.createCell(0, XSSFCell.CELL_TYPE_STRING);
    			dateCell.setCellValue("名称");
    			dateCell.setCellStyle(top2Style);
    			
    			HSSFCell rsdCell = row.createCell(1, XSSFCell.CELL_TYPE_STRING);
    			rsdCell.setCellValue("总目标医生数");
    			rsdCell.setCellStyle(top2Style);
    			
    			HSSFCell rsmCell = row.createCell(2, XSSFCell.CELL_TYPE_STRING);
    			rsmCell.setCellValue("上周新增医生数");
    			rsmCell.setCellStyle(top2Style);
    			
    			HSSFCell dsmCell = row.createCell(3, XSSFCell.CELL_TYPE_STRING);
    			dsmCell.setCellValue("上周家庭雾化新病人次量");
    			dsmCell.setCellStyle(top2Style);
    			
    			HSSFCell psrCell = row.createCell(4, XSSFCell.CELL_TYPE_STRING);
    			psrCell.setCellValue("持续期治疗率");
    			psrCell.setCellStyle(top2Style);
    			
    			HSSFCell hosCodeCell = row.createCell(5, XSSFCell.CELL_TYPE_STRING);
    			hosCodeCell.setCellValue("推荐使用令舒的人次");
    			hosCodeCell.setCellStyle(top2Style);
    			
    			HSSFCell hosNameCell = row.createCell(6, XSSFCell.CELL_TYPE_STRING);
    			hosNameCell.setCellValue("持续期令舒比例");
    			hosNameCell.setCellStyle(top2Style);
    			
    			HSSFCell drNameCell = row.createCell(7, XSSFCell.CELL_TYPE_STRING);
    			drNameCell.setCellValue("家庭雾化疗程达标率（DOT>=30天）");
    			drNameCell.setCellStyle(top2Style);
    			
    			int dateColumnWidth = 15;
    			
    			sheet.setColumnWidth(0, dateColumnWidth*256);
    			sheet.setColumnWidth(1, dateColumnWidth*256);
    			sheet.setColumnWidth(2, dateColumnWidth*256);
    			sheet.setColumnWidth(3, dateColumnWidth*256);
    			sheet.setColumnWidth(4, dateColumnWidth*256);
    			sheet.setColumnWidth(5, dateColumnWidth*256);
    			sheet.setColumnWidth(6, dateColumnWidth*256);
    			sheet.setColumnWidth(7, dateColumnWidth*256);
    			
    			HSSFCellStyle numberCellStyle = workbook.createCellStyle();
    			numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
    			numberCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    			numberCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    			numberCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    			numberCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
    			
    			HSSFCellStyle percentCellStyle = workbook.createCellStyle();
    			percentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
    			percentCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    			percentCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    			percentCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    			percentCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
    			
    			for( HomeWeeklyData rsmDate : allRSMData ){
    			    row = sheet.createRow(currentRowNum++);
    			    
    			    HSSFCell value1Cell = row.createCell(0, XSSFCell.CELL_TYPE_STRING);
                    value1Cell.setCellValue(rsmDate.getUserName());
                    value1Cell.setCellStyle(valueStyle);
                    
                    HSSFCell value2Cell = row.createCell(1, XSSFCell.CELL_TYPE_NUMERIC);
                    value2Cell.setCellValue(rsmDate.getTotalDrNum());
                    value2Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value3Cell = row.createCell(2, XSSFCell.CELL_TYPE_NUMERIC);
                    value3Cell.setCellValue(rsmDate.getNewDrNum());
                    value3Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value4Cell = row.createCell(3, XSSFCell.CELL_TYPE_NUMERIC);
                    value4Cell.setCellValue(rsmDate.getNewWhNum());
                    value4Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value5Cell = row.createCell(4, XSSFCell.CELL_TYPE_NUMERIC);
                    value5Cell.setCellValue(rsmDate.getCureRate());
                    value5Cell.setCellStyle(percentCellStyle);
                    
                    HSSFCell value6Cell = row.createCell(5, XSSFCell.CELL_TYPE_NUMERIC);
                    value6Cell.setCellValue(rsmDate.getLsnum());
                    value6Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value7Cell = row.createCell(6, XSSFCell.CELL_TYPE_NUMERIC);
                    value7Cell.setCellValue(rsmDate.getLsRate());
                    value7Cell.setCellStyle(percentCellStyle);
                    
                    HSSFCell value8Cell = row.createCell(7, XSSFCell.CELL_TYPE_NUMERIC);
                    value8Cell.setCellValue(rsmDate.getReachRate());
                    value8Cell.setCellStyle(percentCellStyle);
    			}
    			
    			workbook.write(fOut);
    		}
    	}catch(Exception e){
    		logger.error("fail to export the weekly home data file,",e);
    	}finally{
    		if( fOut != null ){
    			fOut.close();
    		}
    	}
    	request.getSession().setAttribute("weeklyHomeDataFile", fileName);
    	if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
    		return "redirect:showWebUploadData";
    	}else{
    		return "redirect:showUploadData";
    	}
    }
    
    @RequestMapping("/doDownloadDoctorData")
    public String doDownloadDoctorData(HttpServletRequest request, HttpServletResponse response) throws IOException{
        logger.info("download the doctor data..");
        FileOutputStream fOut = null;
        String fileName = null;
        String fromWeb = request.getParameter("fromWeb");
        try{
            List<ExportDoctor> doctorList = homeService.getAllDoctors();
            
            File homeDir = new File(request.getRealPath("/") + "homeData/");
            if( !homeDir.exists() ){
                homeDir.mkdir();
            }
            
            fileName = new StringBuffer("homeData/家庭雾化KPI医生名单.xls").toString();
            
            File tmpFile = new File(request.getRealPath("/") + fileName);
            if( !tmpFile.exists() ){
                tmpFile.createNewFile();
            }
            
            fOut = new FileOutputStream(tmpFile);
            
            HSSFWorkbook workbook = new HSSFWorkbook();
            workbook.createSheet("家庭雾化医生名单");
            HSSFSheet sheet = workbook.getSheetAt(0);
            int currentRowNum = 0;
            
            HSSFFont font = workbook.createFont();
            font.setColor(HSSFColor.WHITE.index);
            
            HSSFCellStyle topStyle=workbook.createCellStyle();
            topStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            topStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            topStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            topStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
            topStyle.setLeftBorderColor(HSSFColor.BLACK.index);
            topStyle.setRightBorderColor(HSSFColor.BLACK.index);
            topStyle.setFillForegroundColor(HSSFColor.BLUE.index);
            topStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            topStyle.setFont(font);
            
            HSSFCellStyle top2Style=workbook.createCellStyle();
            top2Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            top2Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            top2Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            top2Style.setLeftBorderColor(HSSFColor.BLACK.index);
            top2Style.setRightBorderColor(HSSFColor.BLACK.index);
            top2Style.setFillForegroundColor(HSSFColor.VIOLET.index);
            top2Style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            top2Style.setFont(font);
            
            //build the header
            HSSFRow row = sheet.createRow(currentRowNum++);
            row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("区域");
            row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("大区");
            row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("DSM");
            row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("PSR Code");
            row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("PSR");
            row.getCell(0).setCellStyle(topStyle);
            row.getCell(1).setCellStyle(topStyle);
            row.getCell(2).setCellStyle(topStyle);
            row.getCell(3).setCellStyle(topStyle);
            row.getCell(4).setCellStyle(topStyle);
            
            row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("目标医院Code");
            row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("目标医院名称");
            row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("目标医生Code");
            row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("目标医生");
            row.getCell(5).setCellStyle(top2Style);
            row.getCell(6).setCellStyle(top2Style);
            row.getCell(7).setCellStyle(top2Style);
            row.getCell(8).setCellStyle(top2Style);
            
            int userColumnWidth = 12;
            
            sheet.setColumnWidth(0, userColumnWidth*256);
            sheet.setColumnWidth(1, userColumnWidth*256);
            sheet.setColumnWidth(2, userColumnWidth*256);
            sheet.setColumnWidth(3, userColumnWidth*256);
            sheet.setColumnWidth(4, userColumnWidth*256);
            sheet.setColumnWidth(5, userColumnWidth*256);
            sheet.setColumnWidth(6, 26*256);//hospital name
            sheet.setColumnWidth(7, 18*256);//doctor code
            sheet.setColumnWidth(8, userColumnWidth*256);
            
            for( ExportDoctor doctor : doctorList ){
                row = sheet.createRow(currentRowNum++);
                row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getRegion());
                row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getRsmRegion());
                row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getDsmName());
                row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getSalesCode());
                row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getSalesName());
                row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getHospitalCode());
                row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getHospitalName());
                row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getDoctorCode());
                row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getDoctorName());
            }
            
            workbook.write(fOut);
        }catch(Exception e){
            logger.error("fail to export the doctor data file,",e);
        }finally{
            if( fOut != null ){
                fOut.close();
            }
        }
        request.getSession().setAttribute("doctorDataFile", fileName);
        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    
    @RequestMapping("/dailyReport")
    public ModelAndView dailyReport(HttpServletRequest request){
        logger.info("daily report department");
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        
        logger.info(String.format("daily report: current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser 
                || LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel())){
        	view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
        	view.setViewName("index");
        	return view;
        }
        
        view.setViewName("dailyReportDepartment");
        return view;
    }
    
    @RequestMapping("/peddailyreport")
    public ModelAndView pedDailyReport(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( null == currentUser 
        		|| LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
        	view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
        	view.setViewName("index");
        	return view;
        }
        
        try{
            String telephone = (String)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR);
            logger.info("daily RES report, the current user is " + telephone);
            
            List<MobilePEDDailyData> mobilePEDData = pediatricsService.getDailyPEDData4Mobile(telephone,currentUser);
            logger.info("get daily ped data for mobile end...");
            if( !LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            	List<MobilePEDDailyData> mobilePEDChildData = pediatricsService.getDailyPEDChildData4Mobile(telephone);
            	logger.info("get daily ped child data for mobile end...");
            	
            	view.addObject(LsAttributes.MOBILE_DAILY_REPORT_CHILD_DATA, mobilePEDChildData);
            }else{
                MobilePEDDailyData mpd = pediatricsService.getDailyPEDParentData4Mobile(telephone, currentUser.getLevel());
                logger.info("get daily ped parent data for mobile end...");
                view.addObject(LsAttributes.MOBILE_DAILY_REPORT_PARENT_DATA, mpd);
                
                List<String> allRegionCenters = userService.getAllRegionName();
                List<List<MobilePEDDailyData>> allRSMMobilePEDData = new ArrayList<List<MobilePEDDailyData>>();
                for( String regionCenter : allRegionCenters ){
                    List<MobilePEDDailyData> mobilePEDRSMData = pediatricsService.getDailyPEDData4MobileByRegion(regionCenter);
                    logger.info(String.format("get daily ped data of %s RSM end...", regionCenter));
                    allRSMMobilePEDData.add(mobilePEDRSMData);
                }
                
                view.addObject(LsAttributes.MOBILE_DAILY_REPORT_ALL_RSM_DATA, allRSMMobilePEDData);
                populateDailyReportTitle4AllRSM(view);
                logger.info("populate the title for all rsm end...");
            }
            
            view.addObject(LsAttributes.MOBILE_DAILY_REPORT_DATA, mobilePEDData);
            view.addObject(LsAttributes.CURRENT_OPERATOR_OBJECT,currentUser);
            
            //set the top and bottom data
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())
                    || LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel())
                    || LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel())){
                TopAndBottomRSMData rsmData = pediatricsService.getTopAndBottomRSMData();
                view.addObject("rsmData", rsmData);
                logger.info("get the top and bottom rsm data end...");
            }
            
            populateDailyReportTitle(currentUser,view,LsAttributes.DAILYREPORTTITLE_3);
            logger.info("populate the title end");
        }catch(Exception e){
            logger.error("fail to get the daily ped report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        view.setViewName("pedDailyReport");
        return view;
    }
    
    @RequestMapping("/resdailyreport")
    public ModelAndView resDailyReport(HttpServletRequest request){
        logger.info("daily res report");
        ModelAndView view = new LsKPIModelAndView(request);
        verifyCurrentUser(request,view);
        
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( null == currentUser 
        		|| LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
        	view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
        	view.setViewName("index");
        	return view;
        }
        
        try{
            String telephone = (String)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR);
            logger.info("daily res report, the current user is " + telephone);
            
            List<MobileRESDailyData> mobileRESData = respirologyService.getDailyRESData4Mobile(telephone,currentUser);
            logger.info("get daily res data for mobile end...");
            if( !LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            	List<MobileRESDailyData> mobileRESChildData = respirologyService.getDailyRESChildData4Mobile(telephone,currentUser);
            	logger.info("get daily res child data for mobile end...");
            	view.addObject(LsAttributes.MOBILE_DAILY_REPORT_CHILD_DATA, mobileRESChildData);
            }else{
                MobileRESDailyData mrd = respirologyService.getDailyRESParentData4Mobile(telephone, currentUser.getLevel());
                logger.info("get daily res parent data for mobile end...");
                view.addObject(LsAttributes.MOBILE_DAILY_REPORT_PARENT_DATA, mrd);
                
                List<String> allRegionCenters = userService.getAllRegionName();
                List<List<MobileRESDailyData>> mobileRESAllRSMData = new ArrayList<List<MobileRESDailyData>>();
                
                for( String regionCenter : allRegionCenters ){
                    List<MobileRESDailyData> mobileRESRSMData = respirologyService.getDailyRESData4MobileByRegion(regionCenter);
                    logger.info(String.format("get daily res data of %s RSM end...", regionCenter));
                    mobileRESAllRSMData.add(mobileRESRSMData);
                }
                
                view.addObject(LsAttributes.MOBILE_DAILY_REPORT_ALL_RSM_DATA, mobileRESAllRSMData);
                populateDailyReportTitle4AllRSM(view);
            }
            
            view.addObject(LsAttributes.MOBILE_DAILY_REPORT_DATA, mobileRESData);
            view.addObject(LsAttributes.CURRENT_OPERATOR_OBJECT,currentUser);
            
            //set the top and bottom data
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())
                    || LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel())
                    || LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel())){
                TopAndBottomRSMData rsmData = respirologyService.getTopAndBottomRSMData();
                view.addObject("rsmData", rsmData);
            }
            
            populateDailyReportTitle(currentUser,view,LsAttributes.DAILYREPORTTITLE_3);
            logger.info("populate the title end");
        }catch(Exception e){
            logger.error("fail to get the daily res report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        view.setViewName("resDailyReport");
        return view;
    }
    
    @RequestMapping("/chestSurgeryDailyReport")
    public ModelAndView chestSurgeryDailyReport(HttpServletRequest request){
        logger.info("daily chest surgery report");
        ModelAndView view = new LsKPIModelAndView(request);
        verifyCurrentUser(request,view);
        
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( null == currentUser 
                || LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("index");
            return view;
        }
        
        try{
            String telephone = (String)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR);
            logger.info("daily chest surgery report, the current user is " + telephone);
            
            List<MobileCHEDailyData> mobileCHEData = chestSurgeryService.getDailyCHEData4Mobile(telephone,currentUser);
            logger.info("get daily chest surgery data for mobile end...");
            if( !LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
                List<MobileCHEDailyData> mobileCHEChildData = chestSurgeryService.getDailyCHEChildData4Mobile(telephone,currentUser);
                logger.info("get daily chest surgery child data for mobile end...");
                view.addObject(LsAttributes.MOBILE_DAILY_REPORT_CHILD_DATA, mobileCHEChildData);
            }else{
                MobileCHEDailyData mrd = chestSurgeryService.getDailyCHEParentData4Mobile(telephone, currentUser.getLevel());
                logger.info("get daily chest surgery parent data for mobile end...");
                view.addObject(LsAttributes.MOBILE_DAILY_REPORT_PARENT_DATA, mrd);
                
                List<String> allRegionCenters = userService.getAllRegionName();
                List<List<MobileCHEDailyData>> mobileAllRSMData = new ArrayList<List<MobileCHEDailyData>>();
                
                for( String regionCenter : allRegionCenters ){
                    List<MobileCHEDailyData> mobileRSMData = chestSurgeryService.getDailyCHEData4MobileByRegionCenter(regionCenter);
                    logger.info(String.format("get daily chest surgery data of %s RSM end...", regionCenter));
                    mobileAllRSMData.add(mobileRSMData);
                }
                
                view.addObject(LsAttributes.MOBILE_DAILY_REPORT_ALL_RSM_DATA, mobileAllRSMData);
                populateDailyReportTitle4AllRSM(view);
            }
            
            view.addObject(LsAttributes.MOBILE_DAILY_REPORT_DATA, mobileCHEData);
            view.addObject(LsAttributes.CURRENT_OPERATOR_OBJECT,currentUser);
            
            //set the top and bottom data
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel())
                    || LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(currentUser.getLevel())
                    || LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(currentUser.getLevel())){
                TopAndBottomRSMData rsmData = chestSurgeryService.getTopAndBottomRSMData();
                logger.info("get daily top and bottom rsm data end...");
                view.addObject("rsmData", rsmData);
            }
            
            populateDailyReportTitle(currentUser,view,LsAttributes.DAILYREPORTTITLE_3);
            logger.info("populate the title end");
        }catch(Exception e){
            logger.error("fail to get the daily chest surgery report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
        view.setViewName("chestSurgeryDailyReport");
        return view;
    }

    @RequestMapping("/weeklyreport")
    public ModelAndView weeklyReport(HttpServletRequest request){
        logger.info("weekly report department");
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        
        logger.info(String.format("weekly report: current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
        	view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
        	view.setViewName("index");
        	return view;
        }
        
        view.setViewName("weeklyReportDepartment");
        return view;
    }
    

    @RequestMapping("/homeReport")
    public ModelAndView homeReport(HttpServletRequest request){
        logger.info("home data weekly report");
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        
        logger.info(String.format("home data weekly report: current user's telephone is %s", currentUserTel));
        
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            view.setViewName("index");
            return view;
        }
        
        try{
            List<HomeWeeklyData> homeWeeklyDataList = homeService.getHomeWeeklyDataOfCurrentUser(currentUser);
            view.addObject("homeWeeklyDataList", homeWeeklyDataList);
            logger.info(String.format("end to get the home weekly data of user %s", currentUser.getTelephone()));
            
            List<HomeWeeklyData> lowerHomeWeeklyDataList = homeService.getHomeWeeklyDataOfLowerUser(currentUser);
            view.addObject("lowerHomeWeeklyDataList", lowerHomeWeeklyDataList);
            logger.info(String.format("end to get the lower home weekly data of user %s", currentUser.getTelephone()));
            
            HomeWeeklyData upperHomeWeeklyData = homeService.getHomeWeeklyDataOfUpperUser(currentUser);
            view.addObject("upperHomeWeeklyData", upperHomeWeeklyData);
            logger.info(String.format("end to get the upper home weekly data of user %s", currentUser.getTelephone()));
            
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            	List<String> allRegionCenters = userService.getAllRegionName();
                List<List<HomeWeeklyData>> allRSMData = new ArrayList<List<HomeWeeklyData>>();
                
                for( String regionCenter : allRegionCenters ){
                    List<HomeWeeklyData> rsmData = homeService.getWeeklyDataByRegion(regionCenter);
                    logger.info(String.format("get weekly home data of %s RSM end...", regionCenter));
                    allRSMData.add(rsmData);
                }
                
                view.addObject("allRSMHomeWeeklyData", allRSMData);
                populateHomeWeeklyReportTitle4AllRSM(view);
            }
            
            view.addObject("currentUser", currentUser);
            String dsmName = "";
            if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
                dsmName = userService.getUserInfoByUserCode(currentUser.getSuperior()).getName();
            }
            populateHomeWeeklyReportTitle(currentUser, view, LsAttributes.HOMEWEEKLYREPORTTITLE, dsmName);
        }catch(Exception e){
            logger.error("fail to get the home weekly data,",e);
        }
        
        view.setViewName("homeCollectionReport");
        return view;
    }
    
    @RequestMapping("/monthlyDataReport")
    public ModelAndView monthlyDataReport(HttpServletRequest request){
        logger.info("monthly data report");
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        
        logger.info(String.format("weekly report: current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
        
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            view.setViewName("weeklyReportDepartment");
            return view;
        }
        if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("weeklyReportDepartment");
            return view;
        }
        
        try{
            List<MonthlyRatioData> monthlyRatioList = hospitalService.getMonthlyRatioData(currentUser);
            MonthlyRatioData superiorMonthlyRatio = hospitalService.getUpperUserMonthlyRatioData(currentUser);
            view.addObject("monthlyRatioList", monthlyRatioList);
            view.addObject("superiorMonthlyRatio", superiorMonthlyRatio);
            view.addObject("currentUser", currentUser);
            populateDailyReportTitle(currentUser, view, LsAttributes.MONTHLYREPORTTITLE);
        }catch(Exception e){
            logger.error("fail to get the monthly data in query,",e);
        }

        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
        String localPath = request.getRealPath("/");
        
        StringBuffer localReportFile = new StringBuffer(localPath);
        StringBuffer remoteReportFile = new StringBuffer(basePath);
        
        String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"monthlyHTMLReport");
        
        remoteReportFile.append(directory).append(DateUtils.getLastMonth()).append("/")
        .append("monthlyReport-")
        .append(currentUser.getLevel())
        .append("-")
        .append(currentUserTel)
        .append("-")
        .append(DateUtils.getLastMonth())
        .append(".html");
        
        localReportFile.append(directory).append(DateUtils.getLastMonth()).append("/")
            .append("monthlyReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(currentUserTel)
            .append("-")
            .append(DateUtils.getLastMonth())
            .append(".html");
        
        File reportfile = new File(localReportFile.toString());
        if( reportfile.exists() ){
            view.addObject("monthlyReportFile", remoteReportFile.toString());
        }else{
            view.addObject("monthlyReportFile", basePath+"jsp/weeklyReport_404.html");
        }
        
        view.setViewName("monthlyCollectionReport");
        return view;
    }
    

    @RequestMapping("/pedWeeklyreport")
    public ModelAndView pedWeeklyreport(HttpServletRequest request){
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
            view.setViewName("weeklyReportDepartment");
            return view;
        }
        if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("weeklyReportDepartment");
            return view;
        }
        
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
        String localPath = request.getRealPath("/");
        
        StringBuffer localpedReportFile = new StringBuffer(localPath);
        StringBuffer remotepedReportFile = new StringBuffer(basePath);
        
        String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"weeklyHTMLReport");
        
        remotepedReportFile.append(directory).append(DateUtils.getLastThursDay()).append("/")
        .append("weeklyPEDReport-")
        .append(currentUser.getLevel())
        .append("-")
        .append(currentUserTel)
        .append("-")
        .append(DateUtils.getLastThursDay())
        .append(".html");
        
        localpedReportFile.append(directory).append(DateUtils.getLastThursDay()).append("/")
	        .append("weeklyPEDReport-")
	        .append(currentUser.getLevel())
	        .append("-")
	        .append(currentUserTel)
	        .append("-")
	        .append(DateUtils.getLastThursDay())
	        .append(".html");
        logger.info("pedreport = "+localpedReportFile.toString());
        File reportfile = new File(localpedReportFile.toString());
        if( reportfile.exists() ){
        	view.addObject("pedReportFile", remotepedReportFile.toString());
        }else{
        	view.addObject("pedReportFile", basePath+"jsp/weeklyReport_404.html");
        }
        
        
        view.setViewName("pedWeeklyReport");
        return view;
    }
    
    @RequestMapping("/resWeeklyreport")
    public ModelAndView resWeeklyreport(HttpServletRequest request){
        logger.info("weekly res report");
        ModelAndView view = new LsKPIModelAndView(request);
        String currentUserTel = verifyCurrentUser(request,view);
        
        UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
        	view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
        	view.setViewName("weeklyReportDepartment");
        	return view;
        }
        if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            view.setViewName("weeklyReportDepartment");
            return view;
        }
        
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
        String localPath = request.getRealPath("/");
        StringBuffer localResReportFile = new StringBuffer(localPath);
        StringBuffer remoteResReportFile = new StringBuffer(basePath);
        
        String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"weeklyHTMLReport");
        
        remoteResReportFile.append(directory).append(DateUtils.getLastThursDay()).append("/")
        .append("weeklyRESReport-")
        .append(currentUser.getLevel())
        .append("-")
        .append(currentUserTel)
        .append("-")
        .append(DateUtils.getLastThursDay())
        .append(".html");
        
        localResReportFile.append(directory).append(DateUtils.getLastThursDay()).append("/")
	        .append("weeklyRESReport-")
	        .append(currentUser.getLevel())
	        .append("-")
	        .append(currentUserTel)
	        .append("-")
	        .append(DateUtils.getLastThursDay())
	        .append(".html");
        
        logger.info("localResReportFile = "+localResReportFile.toString());
        logger.info("remoteResReportFile = "+localResReportFile.toString());
        
        File reportfile = new File(localResReportFile.toString());
        if( reportfile.exists() ){
        	view.addObject("resReportFile", remoteResReportFile.toString());
        }else{
        	view.addObject("resReportFile", basePath+"jsp/weeklyReport_404.html");
        }
        view.setViewName("resWeeklyReport");
        return view;
    }
    
    
    
    @RequestMapping("/doDownloadDailyDSMReport")
    public String doDownloadDailyDSMReport(HttpServletRequest request){
    	logger.info("download the all dsm daily data..");
        String fileName = null;
        String fromWeb = request.getParameter("fromWeb");
        try{
        	String chooseDate = request.getParameter("chooseDate");
        	String department = request.getParameter("department");
        	
            if( null == chooseDate || "".equalsIgnoreCase(chooseDate) ){
                logger.error(String.format("the choose date is %s", chooseDate));
            }else{
                logger.info(String.format("begin to get the all dsm daily data in %s,department is %s", chooseDate,department));
                    
                String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
                String localPath = request.getRealPath("/");
                StringBuffer remoteDailyReportFile = new StringBuffer(basePath);
                StringBuffer localDailyReportFile = new StringBuffer(localPath);
                long systemTime = System.currentTimeMillis();
                
                if( "1".equalsIgnoreCase(department) ){
                	remoteDailyReportFile.append("resAllDSMDailyReport/").append(chooseDate).append("/")
                	.append("呼吸科日报-DSM-").append(chooseDate).append("-").append(systemTime).append(".xlsx");
                	localDailyReportFile.append("resAllDSMDailyReport/").append(chooseDate).append("/")
                	.append("呼吸科日报-DSM-").append(chooseDate).append("-").append(systemTime).append(".xlsx");
                }else{
                	remoteDailyReportFile.append("pedAllDSMDailyReport/").append(chooseDate).append("/")
                	.append("儿科日报-DSM-").append(chooseDate).append("-").append(systemTime).append(".xlsx");
                	localDailyReportFile.append("pedAllDSMDailyReport/").append(chooseDate).append("/")
                	.append("儿科日报-DSM-").append(chooseDate).append("-").append(systemTime).append(".xlsx");
                }
                
                File dailyReportFile = new File(localDailyReportFile.toString());
                
                if( !dailyReportFile.exists() ){
                	BirtReportUtils html = new BirtReportUtils();
                	logger.info("begin to generate all DSM daily report");
                	html.startPlatform();
                	createAllDSMDailyReport(html, localPath, chooseDate, systemTime, department);
                	html.stopPlatform();
                	logger.info("end to generate all DSM daily report");
                	
                	if( !dailyReportFile.exists() ){
                		logger.error("fail to generate the daily report to export, no file is found");
                		fileName = "";
                	}else{
                		fileName = remoteDailyReportFile.toString();
                	}
                }else{
                	fileName = remoteDailyReportFile.toString();
                }
            }
        }catch(Exception e){
            logger.error("fail to download the all daily report file,",e);
        }finally{
        }
        request.getSession().setAttribute("dsmFileName", fileName.substring(fileName.lastIndexOf("/")+1));
        request.getSession().setAttribute("dsmDataFile", fileName);
        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    
    @RequestMapping("/doDownloadDailyRSMReport")
    public String doDownloadDailyRSMReport(HttpServletRequest request){
    	logger.info("download the all rsm daily data..");
    	String fileName = null;
    	String fromWeb = request.getParameter("fromWeb");
    	try{
    		String chooseDate = request.getParameter("chooseDate");
    		String department = request.getParameter("department");
    		
    		if( null == chooseDate || "".equalsIgnoreCase(chooseDate) ){
    			logger.error(String.format("the choose date is %s", chooseDate));
    		}else{
    			logger.info(String.format("begin to get the all rsm daily data in %s,department is %s", chooseDate,department));
    			
    			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
    			String localPath = request.getRealPath("/");
    			StringBuffer remoteDailyReportFile = new StringBuffer(basePath);
    			StringBuffer localDailyReportFile = new StringBuffer(localPath);
    			long systemTime = System.currentTimeMillis();
    			
    			if( "1".equalsIgnoreCase(department) ){
    				remoteDailyReportFile.append("resAllRSMDailyReport/").append(chooseDate).append("/")
    				.append("呼吸科日报-RSM-").append(chooseDate).append("-").append(systemTime).append(".xlsx");
    				localDailyReportFile.append("resAllRSMDailyReport/").append(chooseDate).append("/")
    				.append("呼吸科日报-RSM-").append(chooseDate).append("-").append(systemTime).append(".xlsx");
    			}else{
    				remoteDailyReportFile.append("pedAllRSMDailyReport/").append(chooseDate).append("/")
    				.append("儿科日报-RSM-").append(chooseDate).append("-").append(systemTime).append(".xlsx");
    				localDailyReportFile.append("pedAllRSMDailyReport/").append(chooseDate).append("/")
    				.append("儿科日报-RSM-").append(chooseDate).append("-").append(systemTime).append(".xlsx");
    			}
    			
    			File dailyReportFile = new File(localDailyReportFile.toString());
    			
    			if( !dailyReportFile.exists() ){
    				BirtReportUtils html = new BirtReportUtils();
    				logger.info("begin to generate all RSM daily report");
    				html.startPlatform();
    				createAllRSMDailyReport(html, localPath, chooseDate,systemTime, department);
    				html.stopPlatform();
    				logger.info("end to generate all RSM daily report");
    				
    				if( !dailyReportFile.exists() ){
    					logger.error("fail to generate the daily report to export, no file is found");
    					fileName = "";
    				}else{
    					fileName = remoteDailyReportFile.toString();
    				}
    			}else{
    				fileName = remoteDailyReportFile.toString();
    			}
    		}
    	}catch(Exception e){
    		logger.error("fail to download the all daily report file,",e);
    	}finally{
    	}
    	request.getSession().setAttribute("rsmFileName", fileName.substring(fileName.lastIndexOf("/")+1));
    	request.getSession().setAttribute("rsmDataFile", fileName);
    	if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    
    @RequestMapping("/doDownloadWeeklyData")
    public String doDownloadWeeklyData(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	logger.info("download the weekly data..");
    	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		String localPath = request.getRealPath("/");
		StringBuffer remoteWeeklyReportFile = new StringBuffer(basePath).append("weeklyReport2Download/");
		StringBuffer weeklyReportFile2Download = new StringBuffer(localPath).append("weeklyReport2Download/");
		StringBuffer localWeeklyReportFile = new StringBuffer(localPath).append("weeklyReport/");
		
		String fromWeb = request.getParameter("fromWeb");
		String emailto = request.getParameter("emailto");
		String eventtype = request.getParameter("eventtype");
		
		UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
        
		WebUserInfo webUser = (WebUserInfo)request.getSession().getAttribute(LsAttributes.WEB_LOGIN_USER);
		
		if( null == webUser 
				&& (null == currentUser 
                	|| LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel())) ){
            request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.RETURNED_MESSAGE_3);
            if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
                return "redirect:showWebUploadData";
            }else{
                return "redirect:showUploadData";
            }
        }
		
        if( null == eventtype || "".equalsIgnoreCase(eventtype) ){
            eventtype = "download";
        }
        
        if( (null == emailto || "".equalsIgnoreCase(emailto)) && null == webUser ){
            emailto = currentUser.getEmail();
        }
        
        if(  "email".equalsIgnoreCase(eventtype) && ( null == emailto || "".equalsIgnoreCase(emailto) || !StringUtils.isEmail(emailto) ) ){
            request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.RETURNED_MESSAGE_4);
            if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
                return "redirect:showWebUploadData";
            }else{
                return "redirect:showUploadData";
            }
        }
        
    	try{
    	    String chooseDate_weekly_h = request.getParameter("chooseDate_weekly_h");
    	    String chooseDate_weekly = request.getParameter("chooseDate_weekly");
    	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	    
    	    if( null == chooseDate_weekly || "".equalsIgnoreCase(chooseDate_weekly) ){
    	        chooseDate_weekly = chooseDate_weekly_h;
    	    }
    	    
    	    Date chooseDate_d = formatter.parse(chooseDate_weekly);
    	    
    	    String selectedRSD = request.getParameter("rsdSelect");
    		String selectedRSM = request.getParameter("rsmSelect");
    		String selectedDSM = request.getParameter("dsmSelect");
    		String department = request.getParameter("department");
    		
    		request.getSession().setAttribute("chooseDate_weekly", chooseDate_weekly);
    		request.getSession().setAttribute("selectedRSD", selectedRSD);
    		request.getSession().setAttribute("selectedRSM", selectedRSM);
    		request.getSession().setAttribute("selectedDSM", selectedDSM);
    		request.getSession().setAttribute("department", department);
    		
    		if( null == chooseDate_weekly || "".equalsIgnoreCase(chooseDate_weekly) ){
		        logger.error("the choose date should not be empty during downloading the weekly report");
		        request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.RETURNED_MESSAGE_6);
		        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
		            return "redirect:showWebUploadData";
		        }else{
		            return "redirect:showUploadData";
		        }
    		}
    		
    		if( ( null == selectedRSD || "".equalsIgnoreCase(selectedRSD) ) 
    		        && ( null == selectedRSM || "".equalsIgnoreCase(selectedRSM) )
    		        && ( null == selectedDSM || "".equalsIgnoreCase(selectedDSM) ) ){
    			logger.error("there is no user selected during downloading the weekly report");
    			request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.RETURNED_MESSAGE_5);
                if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
                    return "redirect:showWebUploadData";
                }else{
                    return "redirect:showUploadData";
                }
    		}

			logger.info(String.format("begin to get the weekly pdf report selectedRSD is %s, selectedRSM is %s,selectedDSM is %s, department is %s, chooseDate_weekly is %s", selectedRSD, selectedRSM,selectedDSM,department,chooseDate_weekly));
			List<ReportFileObject> reportFiles = new ArrayList<ReportFileObject>();
			
			if( null != selectedDSM && !"".equalsIgnoreCase(selectedDSM) ){
			    UserInfo dsm = userService.getUserInfoByTel(selectedDSM);
                
                if( "1".equalsIgnoreCase(department) ){
                    populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "呼吸科周报-DSM-", dsm.getName());
                }else{
                    populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "儿科周报-DSM-", dsm.getName());
                }
			}else if( null != selectedRSM && !"".equalsIgnoreCase(selectedRSM) ){
			    
			    if( "1".equalsIgnoreCase(department) ){
			        populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "呼吸科周报-RSM-", selectedRSM);
                }else{
                    populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "儿科周报-RSM-", selectedRSM);
                }
			}else if( null != selectedRSD && !"0".equalsIgnoreCase(selectedRSD) ){
			    if( "1".equalsIgnoreCase(department) ){
			        populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "呼吸科周报-RSD-", selectedRSD);
                }else{
                    populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "儿科周报-RSD-", selectedRSD);
                }
			}else{
			  //the whole country is selected.
			    List<String> filePaths = new ArrayList<String>();
			    try{
			        if( "1".equalsIgnoreCase(department) ){
			            populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "呼吸科周报", "");
			            populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "呼吸科周报", "_central");
			            populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "呼吸科周报", "_east1");
			            populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "呼吸科周报", "_east2");
			            populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "呼吸科周报", "_north");
			            populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "呼吸科周报", "_south");
			            populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "呼吸科周报", "_west");
			        }else{
                        populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "儿科周报", "");
                        populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "儿科周报", "_central");
                        populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "儿科周报", "_east1");
                        populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "儿科周报", "_east2");
                        populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "儿科周报", "_north");
                        populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "儿科周报", "_south");
                        populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "儿科周报", "_west");
			        }
			    }catch(Exception e){
			        logger.error("fail to generate the daily report to export, no file is found");
			        
			        request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.NO_WEEKLY_PDF_FOUND);
			        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
			            return "redirect:showWebUploadData";
			        }else{
			            return "redirect:showUploadData";
			        }
			    }
			    
			    try{
			    	if( "download".equalsIgnoreCase(eventtype) ){
			    		request.getSession().setAttribute("reportFiles", reportFiles);
			    	}else{
			    		EmailUtils.sendMessage(filePaths,emailto,"周报推送","");
			    		request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.WEEKLY_PDF_SEND);
			    	}
	            }catch(Exception e){
	                logger.error(String.format("fail to sent the weekly report to %s,",emailto),e);
	                request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.SEND_WEEKLY_PDF_ERROR);
	            }
			    
			    if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
			        return "redirect:showWebUploadData";
			    }else{
			        return "redirect:showUploadData";
			    }
			}
			
			File dailyReportFile = new File(localWeeklyReportFile.toString());
	        File targetReportFile = new File(weeklyReportFile2Download.toString());
	        
	        if( !dailyReportFile.exists() ){
	            logger.error("fail to generate the daily report to export, no file is found");
	            
	            request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.NO_WEEKLY_PDF_FOUND);
	            if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
	                return "redirect:showWebUploadData";
	            }else{
	                return "redirect:showUploadData";
	            }
	        }else{
	            FileUtils.copySourceFile2TargetFile(dailyReportFile, targetReportFile);
	            
	            ReportFileObject rfo = new ReportFileObject();
	            rfo.setFileName(remoteWeeklyReportFile.toString().substring(remoteWeeklyReportFile.toString().lastIndexOf("/")+1));
	            rfo.setFilePath(remoteWeeklyReportFile.toString());
	            
	            reportFiles.add(rfo);
	            
	            try{
	            	if( "download".equalsIgnoreCase(eventtype) ){
	            		request.getSession().setAttribute("reportFiles", reportFiles);
	            	}else{
	            		EmailUtils.sendMessage(weeklyReportFile2Download.toString(),emailto,"周报推送","");
	            		request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.WEEKLY_PDF_SEND);
	            	}
	            }catch(Exception e){
	                logger.error(String.format("fail to sent the weekly report to %s,",emailto),e);
	                request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.SEND_WEEKLY_PDF_ERROR);
	            }
	        }
			
    	}catch(Exception e){
    		logger.error("fail to download the all daily report file,",e);
    	}finally{
    	}
    	
    	if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    
    @RequestMapping("/refreshWeeklyPDFReport")
    public String refreshWeeklyPDFReport(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String refreshDate_s = request.getParameter("refreshDate");
        logger.info(String.format("start to refresh the PDF weekly report, refresh date is %s", refreshDate_s));
        
        try{
            String basePath = request.getRealPath("/");
            String contextPath = CustomizedProperty.getContextProperty("host", "http://localhost:8080");
            
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            Date refreshDate = formatter.parse(refreshDate_s);
            
            List<UserInfo> reportUserInfos = new ArrayList<UserInfo>();
            
            List<UserInfo> bmUserInfos = userService.getUserInfoByLevel("BM");
            List<UserInfo> rsdUserInfos = userService.getUserInfoByLevel("RSD");
            List<UserInfo> rsmUserInfos = userService.getUserInfoByLevel("RSM");
            List<UserInfo> dsmUserInfos = userService.getUserInfoByLevel("DSM");
            
            reportUserInfos.addAll(bmUserInfos);
            reportUserInfos.addAll(rsdUserInfos);
            reportUserInfos.addAll(rsmUserInfos);
            reportUserInfos.addAll(dsmUserInfos);
            
            String duration = DateUtils.getTheBeginDateOfRefreshDate(refreshDate)+"-"+DateUtils.getTheEndDateOfRefreshDate(refreshDate);
            logger.info(String.format("start to remove the old weekly data, the duration is %s", duration));
            
            int rowNumP = pediatricsService.removeOldWeeklyPEDData(duration);
            logger.info(String.format("PED:the number of rows affected is %s", rowNumP));
            int rowNumR = respirologyService.removeOldWeeklyRESData(duration);
            logger.info(String.format("RES:the number of rows affected is %s", rowNumR));
            logger.info(String.format("remove old weekly data done, start to generate the weekly data, the refresh date is %s", refreshDate));
            
            Date weeklyRefreshDate = DateUtils.getGenerateWeeklyReportDate(refreshDate);
            pediatricsService.generateWeeklyPEDDataOfHospital(weeklyRefreshDate);
            respirologyService.generateWeeklyRESDataOfHospital(weeklyRefreshDate);
            logger.info("generate the latest weekly data done, start to refresh the weekly pdf report.");
            
            List<String> regionList = userService.getAllRegionName();
            
            ReportUtils.refreshWeeklyPDFReport(reportUserInfos, basePath, contextPath, refreshDate, regionList);
        }catch(Exception e){
            logger.error("fail to refresh the weekly pdf report,",e);
        }
        
        logger.info("end to refresh the weekly pdf report");
        request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REFRESH_MESSAGE, LsAttributes.WEEKLY_PDF_REFRESHED);
        
        return "redirect:showWebUploadData";
    }

    @RequestMapping("/doDownloadMonthlyInRateData")
    public String doDownloadMonthlyInRateData(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	logger.info("download the monthly inrate data..");
        String fileName = null;
        String fromWeb = request.getParameter("fromWeb");
        try{
        	String chooseDate = request.getParameter("chooseDate_monthlyInRate");
        	String level = request.getParameter("level");
        	
            if( null == chooseDate || "".equalsIgnoreCase(chooseDate) ){
                logger.error(String.format("the choose date is %s", chooseDate));
            }else{
                logger.info(String.format("begin to get the inrate data in %s,level is %s", chooseDate,level));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date inRateDate = formatter.parse(chooseDate);
                String monthName = DateUtils.getMonthInCN(inRateDate);
                
                String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
                String localPath = request.getRealPath("/");
                StringBuffer remoteReportFile = new StringBuffer(basePath);
                StringBuffer localReportFile = new StringBuffer(localPath);
                long systemTime = System.currentTimeMillis();
                
                remoteReportFile.append("monthlyInRate/")
            	.append(monthName).append(level).append("上报率-").append(systemTime).append(".xls");
                localReportFile.append("monthlyInRate/")
            	.append(monthName).append(level).append("上报率-").append(systemTime).append(".xls");
                
                fileName = remoteReportFile.toString();
                
                FileOutputStream fOut = null;
                File inRateDir = new File(localPath + "monthlyInRate/");
                if( !inRateDir.exists() ){
                	inRateDir.mkdir();
                }
                
                File tmpFile = new File(localReportFile.toString());

                try{
                    if( !tmpFile.exists() ){
                        tmpFile.createNewFile();
                    }
                    
                    fOut = new FileOutputStream(tmpFile);
                    
                    HSSFWorkbook workbook = new HSSFWorkbook();
                    String title = monthName+level+"平均上报率";
                    workbook.createSheet(title);
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int currentRowNum = 0;
                    
                    HSSFCellStyle percentCellStyle = workbook.createCellStyle();
                    percentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
                    percentCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    percentCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    percentCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                    percentCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                    
                    HSSFRow row = sheet.createRow(currentRowNum++);
                    row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue(title);
                    row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                    row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                    row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                    sheet.addMergedRegion(new Region(0, (short)0, 0, (short)3));
                    
                    HSSFFont top1FontStyle = workbook.createFont();
                    top1FontStyle.setColor(HSSFColor.BLACK.index);
                    top1FontStyle.setFontName("楷体");
                    top1FontStyle.setFontHeightInPoints((short)10);
                    top1FontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    
                    HSSFCellStyle topStyle=workbook.createCellStyle();
                    topStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    topStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    topStyle.setFont(top1FontStyle);
                    topStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    topStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    topStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                    topStyle.setRightBorderColor(HSSFColor.BLACK.index);
                    
                    row.getCell(0).setCellStyle(topStyle);
                    
                    HSSFCellStyle top2Style=workbook.createCellStyle();
                    top2Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    top2Style.setFont(top1FontStyle);
                    top2Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    top2Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    top2Style.setLeftBorderColor(HSSFColor.BLACK.index);
                    top2Style.setRightBorderColor(HSSFColor.BLACK.index);
                    
                    //build the header
                    row = sheet.createRow(currentRowNum++);
                    HSSFCell userCell = row.createCell(0, XSSFCell.CELL_TYPE_STRING);
                    userCell.setCellValue(level+"名单");
                    userCell.setCellStyle(top2Style);
                    
                    HSSFCell resTitle = row.createCell(1, XSSFCell.CELL_TYPE_STRING);
                    resTitle.setCellValue("呼吸科");
                    resTitle.setCellStyle(top2Style);
                    
                    HSSFCell pedTitle = row.createCell(2, XSSFCell.CELL_TYPE_STRING);
                    pedTitle.setCellValue("儿科");
                    pedTitle.setCellStyle(top2Style);
                    
                    HSSFCell aveTitle = row.createCell(3, XSSFCell.CELL_TYPE_STRING);
                    aveTitle.setCellValue("平均");
                    aveTitle.setCellStyle(top2Style);
                    
                    String beginDuraion = DateUtils.getMonthInRateBeginDuration(inRateDate);
                    String endDuraion = DateUtils.getMonthInRateEndDuration(inRateDate);
                    
                    logger.info(String.format("begin to get monthly inRate during %s and %s", beginDuraion,endDuraion));
                    
                    Map<String, MonthlyInRateData> monthlyInRates = hospitalService.getMonthlyInRateData(beginDuraion,endDuraion,level);
                    logger.info("get monthly inRate data end...");
                    
                    Set<String> levelUsers = monthlyInRates.keySet();
                    Iterator<String> userIterator = levelUsers.iterator();
                    
                    while(userIterator.hasNext()){
                    	String user = userIterator.next();
                    	MonthlyInRateData inRateData = monthlyInRates.get(user);
                    	
                    	row = sheet.createRow(currentRowNum++);
                    	row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue(user);
                    	
                    	HSSFCell resRateCell = row.createCell(1, XSSFCell.CELL_TYPE_NUMERIC);
                    	resRateCell.setCellValue(inRateData.getResInRate());
                    	resRateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell pedRateCell = row.createCell(2, XSSFCell.CELL_TYPE_NUMERIC);
                        pedRateCell.setCellValue(inRateData.getPedInRate());
                        pedRateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell aveRateCell = row.createCell(3, XSSFCell.CELL_TYPE_NUMERIC);
                        aveRateCell.setCellValue((inRateData.getPedInRate()+inRateData.getResInRate())/2);
                        aveRateCell.setCellStyle(percentCellStyle);
                        
                    }
                    logger.info("begin to write the export file.");
                    workbook.write(fOut);
                }catch(Exception e){
                    logger.error("fail to generate the file,",e);
                }finally{
                    if( fOut != null ){
                        fOut.close();
                    }
                }
                request.getSession().setAttribute("monthlyInRateDataFileName", fileName.substring(fileName.lastIndexOf("/")+1));
                request.getSession().setAttribute("monthlyInRateDataFile", fileName);
            }
        }catch(Exception e){
            logger.error("fail to download the monthly inrate report file,",e);
        }finally{
        }
        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    
    @RequestMapping("/doDownloadMonthlyCollectionData")
    public String doDownloadMonthlyCollectionData(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	logger.info("download the monthly collection data..");
    	String fileName = null;
    	String fromWeb = request.getParameter("fromWeb");
    	try{
    		String chooseDate = request.getParameter("chooseDate_monthlyCollection");
    		
    		if( null == chooseDate || "".equalsIgnoreCase(chooseDate) ){
    			logger.error(String.format("the choose date is %s", chooseDate));
    		}else{
    			logger.info(String.format("begin to get the collection data in %s", chooseDate));
    			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    			Date collectionDate = formatter.parse(chooseDate);
    			String monthName = DateUtils.getMonthInCN(collectionDate);
    			
    			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
    			String localPath = request.getRealPath("/");
    			StringBuffer remoteReportFile = new StringBuffer(basePath);
    			StringBuffer localReportFile = new StringBuffer(localPath);
    			long systemTime = System.currentTimeMillis();
    			
    			remoteReportFile.append("monthlyCollection/")
    			.append(monthName).append("袋数采集统计表-").append(systemTime).append(".xls");
    			localReportFile.append("monthlyCollection/")
    			.append(monthName).append("袋数采集统计表-").append(systemTime).append(".xls");
    			
    			fileName = remoteReportFile.toString();
    			
    			FileOutputStream fOut = null;
    			File inRateDir = new File(localPath + "monthlyCollection/");
    			if( !inRateDir.exists() ){
    				inRateDir.mkdir();
    			}
    			
    			File tmpFile = new File(localReportFile.toString());
    			
    			try{
    				if( !tmpFile.exists() ){
    					tmpFile.createNewFile();
    				}
    				
    				fOut = new FileOutputStream(tmpFile);
    				
    				HSSFWorkbook workbook = new HSSFWorkbook();
    				String title = monthName+"袋数采集统计";
    				workbook.createSheet(title);
    				HSSFSheet sheet = workbook.getSheetAt(0);
    				int currentRowNum = 0;
    				
    				HSSFPalette palette = workbook.getCustomPalette();
    				/** blue*/
                    palette.setColorAtIndex((short)63, (byte) (83), (byte) (142), (byte) (213));
                    /** light blue*/
                    palette.setColorAtIndex((short)62, (byte) (197), (byte) (217), (byte) (241));
                    
                    HSSFFont top1FontStyle = workbook.createFont();
    				top1FontStyle.setColor(HSSFColor.WHITE.index);
    				top1FontStyle.setFontName("楷体");
    				top1FontStyle.setFontHeightInPoints((short)11);
    				top1FontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    				
                    HSSFFont englishFont = workbook.createFont();
                    englishFont.setColor(HSSFColor.BLACK.index);
                    englishFont.setFontName("Times New Roman");
                    englishFont.setFontHeightInPoints((short)11);
    				
    				HSSFCellStyle percentCellStyle = workbook.createCellStyle();
    				percentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
    				percentCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    				percentCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    				percentCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    				percentCellStyle.setLeftBorderColor(HSSFColor.BLUE.index);
    				percentCellStyle.setRightBorderColor(HSSFColor.BLUE.index);
    				percentCellStyle.setBottomBorderColor(HSSFColor.BLUE.index);
    				percentCellStyle.setFont(englishFont);
    				percentCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    				
    				HSSFCellStyle evenPercentCellStyle = workbook.createCellStyle();
    				evenPercentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
    				evenPercentCellStyle.setFillForegroundColor((short)62);
    				evenPercentCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    				evenPercentCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    				evenPercentCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    				evenPercentCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    				evenPercentCellStyle.setLeftBorderColor(HSSFColor.BLUE.index);
    				evenPercentCellStyle.setRightBorderColor(HSSFColor.BLUE.index);
    				evenPercentCellStyle.setBottomBorderColor(HSSFColor.BLUE.index);
    				evenPercentCellStyle.setFont(englishFont);
    				evenPercentCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    				
    				HSSFCellStyle numCellStyle = workbook.createCellStyle();
    				numCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
    				numCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    				numCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    				numCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    				numCellStyle.setLeftBorderColor(HSSFColor.BLUE.index);
    				numCellStyle.setRightBorderColor(HSSFColor.BLUE.index);
    				numCellStyle.setBottomBorderColor(HSSFColor.BLUE.index);
    				numCellStyle.setFont(englishFont);
    				numCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    				
    				HSSFCellStyle evenNumCellStyle = workbook.createCellStyle();
    				evenNumCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
    				evenNumCellStyle.setFillForegroundColor((short)62);
    				evenNumCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    				evenNumCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    				evenNumCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    				evenNumCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
    				evenNumCellStyle.setLeftBorderColor(HSSFColor.BLUE.index);
    				evenNumCellStyle.setRightBorderColor(HSSFColor.BLUE.index);
    				evenNumCellStyle.setBottomBorderColor(HSSFColor.BLUE.index);
    				evenNumCellStyle.setFont(englishFont);
    				evenNumCellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    				
    				HSSFCellStyle topStyle=workbook.createCellStyle();
    				topStyle.setFillForegroundColor((short)63);
                    topStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
    				topStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
    				topStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
    				topStyle.setFont(top1FontStyle);
    				topStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
    				topStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
    				topStyle.setLeftBorderColor(HSSFColor.BLACK.index);
    				topStyle.setRightBorderColor(HSSFColor.BLACK.index);
    				
    				//build the header
    				HSSFRow row = sheet.createRow(currentRowNum++);
    				HSSFCell userCell = row.createCell(0, XSSFCell.CELL_TYPE_STRING);
    				userCell.setCellValue("RSM");
    				userCell.setCellStyle(topStyle);
    				
    				HSSFCell pedEmerNumTitle = row.createCell(1, XSSFCell.CELL_TYPE_STRING);
    				pedEmerNumTitle.setCellValue("儿科门急诊");
    				pedEmerNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell pedEmerRateTitle = row.createCell(2, XSSFCell.CELL_TYPE_STRING);
    				pedEmerRateTitle.setCellValue("儿科门急诊占比");
    				pedEmerRateTitle.setCellStyle(topStyle);
    				
    				HSSFCell pedRoomNumTitle = row.createCell(3, XSSFCell.CELL_TYPE_STRING);
    				pedRoomNumTitle.setCellValue("儿科病房");
    				pedRoomNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell pedRoomRateTitle = row.createCell(4, XSSFCell.CELL_TYPE_STRING);
    				pedRoomRateTitle.setCellValue("儿科病房占比");
    				pedRoomRateTitle.setCellStyle(topStyle);
    				
    				HSSFCell resNumTitle = row.createCell(5, XSSFCell.CELL_TYPE_STRING);
    				resNumTitle.setCellValue("呼吸科");
    				resNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell resRateTitle = row.createCell(6, XSSFCell.CELL_TYPE_STRING);
    				resRateTitle.setCellValue("呼吸科占比");
    				resRateTitle.setCellStyle(topStyle);
    				
    				HSSFCell otherNumTitle = row.createCell(7, XSSFCell.CELL_TYPE_STRING);
    				otherNumTitle.setCellValue("其他科室");
    				otherNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell otherRateTitle = row.createCell(8, XSSFCell.CELL_TYPE_STRING);
    				otherRateTitle.setCellValue("其他科室占比");
    				otherRateTitle.setCellStyle(topStyle);
    				
    				HSSFCell sumNumTitle = row.createCell(9, XSSFCell.CELL_TYPE_STRING);
    				sumNumTitle.setCellValue("总袋数");
    				sumNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell inHosNumTitle = row.createCell(10, XSSFCell.CELL_TYPE_STRING);
    				inHosNumTitle.setCellValue("上报医院数量");
    				inHosNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell hosNumTitle = row.createCell(11, XSSFCell.CELL_TYPE_STRING);
    				hosNumTitle.setCellValue("负责医院数量");
    				hosNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell inRateTitle = row.createCell(12, XSSFCell.CELL_TYPE_STRING);
    				inRateTitle.setCellValue("上报率");
    				inRateTitle.setCellStyle(topStyle);
    				
    				for( int i = 0; i < 13; i++ ){
    					sheet.setColumnWidth(i, 16*256);
    				}
    				
    				List<MonthlyRatioData> monthlyCollectionData = hospitalService.getMonthlyCollectionData(collectionDate);
    				
    				for( MonthlyRatioData data : monthlyCollectionData ){
    					
    					row = sheet.createRow(currentRowNum++);
    					HSSFCell rsmCell = row.createCell(0, XSSFCell.CELL_TYPE_STRING);
    					rsmCell.setCellValue(data.getRsmRegion());
    					if( currentRowNum%2 == 0 ){
    						rsmCell.setCellStyle(evenNumCellStyle);
    					}else{
    						rsmCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell pedEmerCell = row.createCell(1, XSSFCell.CELL_TYPE_NUMERIC);
    					pedEmerCell.setCellValue(data.getPedemernum());
    					if( currentRowNum%2 == 0 ){
    						pedEmerCell.setCellStyle(evenNumCellStyle);
    					}else{
    						pedEmerCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell pedEmerRateCell = row.createCell(2, XSSFCell.CELL_TYPE_NUMERIC);
    					pedEmerRateCell.setCellValue(data.getPedemernumrate());
    					if( currentRowNum%2 == 0 ){
    						pedEmerRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						pedEmerRateCell.setCellStyle(percentCellStyle);
    					}
    					
    					HSSFCell pedRoomCell = row.createCell(3, XSSFCell.CELL_TYPE_NUMERIC);
    					pedRoomCell.setCellValue(data.getPedroomnum());
    					if( currentRowNum%2 == 0 ){
    						pedRoomCell.setCellStyle(evenNumCellStyle);
    					}else{
    						pedRoomCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell pedRoomRateCell = row.createCell(4, XSSFCell.CELL_TYPE_NUMERIC);
    					pedRoomRateCell.setCellValue(data.getPedroomnumrate());
    					if( currentRowNum%2 == 0 ){
    						pedRoomRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						pedRoomRateCell.setCellStyle(percentCellStyle);
    					}
    					
    					HSSFCell resCell = row.createCell(5, XSSFCell.CELL_TYPE_NUMERIC);
    					resCell.setCellValue(data.getResnum());
    					if( currentRowNum%2 == 0 ){
    						resCell.setCellStyle(evenNumCellStyle);
    					}else{
    						resCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell resRateCell = row.createCell(6, XSSFCell.CELL_TYPE_NUMERIC);
    					resRateCell.setCellValue(data.getResnumrate());
    					if( currentRowNum%2 == 0 ){
    						resRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						resRateCell.setCellStyle(percentCellStyle);
    					}
    					
    					HSSFCell otherCell = row.createCell(7, XSSFCell.CELL_TYPE_NUMERIC);
    					otherCell.setCellValue(data.getOthernum());
    					if( currentRowNum%2 == 0 ){
    						otherCell.setCellStyle(evenNumCellStyle);
    					}else{
    						otherCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell otherRateCell = row.createCell(8, XSSFCell.CELL_TYPE_NUMERIC);
    					otherRateCell.setCellValue(data.getOthernumrate());
    					if( currentRowNum%2 == 0 ){
    						otherRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						otherRateCell.setCellStyle(percentCellStyle);
    					}

    					HSSFCell sumCell = row.createCell(9, XSSFCell.CELL_TYPE_NUMERIC);
    					sumCell.setCellValue(data.getTotalnum());
    					if( currentRowNum%2 == 0 ){
    						sumCell.setCellStyle(evenNumCellStyle);
    					}else{
    						sumCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell inHosNumCell = row.createCell(10, XSSFCell.CELL_TYPE_NUMERIC);
    					inHosNumCell.setCellValue(data.getInnum());
    					if( currentRowNum%2 == 0 ){
    						inHosNumCell.setCellStyle(evenNumCellStyle);
    					}else{
    						inHosNumCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell hosNumCell = row.createCell(11, XSSFCell.CELL_TYPE_NUMERIC);
    					hosNumCell.setCellValue(data.getHosnum());
    					if( currentRowNum%2 == 0 ){
    						hosNumCell.setCellStyle(evenNumCellStyle);
    					}else{
    						hosNumCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell inRateCell = row.createCell(12, XSSFCell.CELL_TYPE_NUMERIC);
    					inRateCell.setCellValue(data.getInrate());
    					if( currentRowNum%2 == 0 ){
    						inRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						inRateCell.setCellStyle(percentCellStyle);
    					}
    				}
    				
    				MonthlyRatioData monthlySumData = hospitalService.getMonthlyCollectionSumData(collectionDate);
    				int rsmDataCount = monthlyCollectionData.size();
    				row = sheet.createRow(currentRowNum++);
					HSSFCell rsmCell = row.createCell(0, XSSFCell.CELL_TYPE_STRING);
					rsmCell.setCellValue("总计");
					if( rsmDataCount%2 == 0 ){
						rsmCell.setCellStyle(evenNumCellStyle);
					}else{
						rsmCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell pedEmerCell = row.createCell(1, XSSFCell.CELL_TYPE_NUMERIC);
					pedEmerCell.setCellValue(monthlySumData.getPedemernum());
					if( rsmDataCount%2 == 0 ){
						pedEmerCell.setCellStyle(evenNumCellStyle);
					}else{
						pedEmerCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell pedEmerRateCell = row.createCell(2, XSSFCell.CELL_TYPE_NUMERIC);
					pedEmerRateCell.setCellValue(monthlySumData.getPedemernumrate());
					if( rsmDataCount%2 == 0 ){
						pedEmerRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						pedEmerRateCell.setCellStyle(percentCellStyle);
					}
					
					HSSFCell pedRoomCell = row.createCell(3, XSSFCell.CELL_TYPE_NUMERIC);
					pedRoomCell.setCellValue(monthlySumData.getPedroomnum());
					if( rsmDataCount%2 == 0 ){
						pedRoomCell.setCellStyle(evenNumCellStyle);
					}else{
						pedRoomCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell pedRoomRateCell = row.createCell(4, XSSFCell.CELL_TYPE_NUMERIC);
					pedRoomRateCell.setCellValue(monthlySumData.getPedroomnumrate());
					if( rsmDataCount%2 == 0 ){
						pedRoomRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						pedRoomRateCell.setCellStyle(percentCellStyle);
					}
					
					HSSFCell resCell = row.createCell(5, XSSFCell.CELL_TYPE_NUMERIC);
					resCell.setCellValue(monthlySumData.getResnum());
					if( rsmDataCount%2 == 0 ){
						resCell.setCellStyle(evenNumCellStyle);
					}else{
						resCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell resRateCell = row.createCell(6, XSSFCell.CELL_TYPE_NUMERIC);
					resRateCell.setCellValue(monthlySumData.getResnumrate());
					if( rsmDataCount%2 == 0 ){
						resRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						resRateCell.setCellStyle(percentCellStyle);
					}
					
					HSSFCell otherCell = row.createCell(7, XSSFCell.CELL_TYPE_NUMERIC);
					otherCell.setCellValue(monthlySumData.getOthernum());
					if( rsmDataCount%2 == 0 ){
						otherCell.setCellStyle(evenNumCellStyle);
					}else{
						otherCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell otherRateCell = row.createCell(8, XSSFCell.CELL_TYPE_NUMERIC);
					otherRateCell.setCellValue(monthlySumData.getOthernumrate());
					if( rsmDataCount%2 == 0 ){
						otherRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						otherRateCell.setCellStyle(percentCellStyle);
					}

					HSSFCell sumCell = row.createCell(9, XSSFCell.CELL_TYPE_NUMERIC);
					sumCell.setCellValue(monthlySumData.getTotalnum());
					if( rsmDataCount%2 == 0 ){
						sumCell.setCellStyle(evenNumCellStyle);
					}else{
						sumCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell inHosNumCell = row.createCell(10, XSSFCell.CELL_TYPE_NUMERIC);
					inHosNumCell.setCellValue(monthlySumData.getInnum());
					if( rsmDataCount%2 == 0 ){
						inHosNumCell.setCellStyle(evenNumCellStyle);
					}else{
						inHosNumCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell hosNumCell = row.createCell(11, XSSFCell.CELL_TYPE_NUMERIC);
					hosNumCell.setCellValue(monthlySumData.getHosnum());
					if( rsmDataCount%2 == 0 ){
						hosNumCell.setCellStyle(evenNumCellStyle);
					}else{
						hosNumCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell inRateCell = row.createCell(12, XSSFCell.CELL_TYPE_NUMERIC);
					inRateCell.setCellValue(monthlySumData.getInrate());
					if( rsmDataCount%2 == 0 ){
						inRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						inRateCell.setCellStyle(percentCellStyle);
					}
    				
    				workbook.write(fOut);
    			}catch(Exception e){
    				logger.error("fail to generate the file,",e);
    			}finally{
    				if( fOut != null ){
    					fOut.close();
    				}
    			}
    			request.getSession().setAttribute("monthlyCollectionDataFileName", fileName.substring(fileName.lastIndexOf("/")+1));
    			request.getSession().setAttribute("monthlyCollectionDataFile", fileName);
    		}
    	}catch(Exception e){
    		logger.error("fail to download the monthly Collection report file,",e);
    	}finally{
    	}
    	if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
    		return "redirect:showWebUploadData";
    	}else{
    		return "redirect:showUploadData";
    	}
    }
    

    @RequestMapping("/doDownloadResMonthData")
    public String doDownloadResMonthData(HttpServletRequest request, HttpServletResponse response) throws IOException{
        logger.info("download the res month data..");
        FileOutputStream fOut = null;
        String fileName = null;
        String fromWeb = request.getParameter("fromWeb");
        try{
            
                SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
                
                File resMonthData = new File(request.getRealPath("/") + "resMonthData/");
                if( !resMonthData.exists() ){
                    resMonthData.mkdir();
                }
                
                fileName = "resMonthData/呼吸科上报数据汇总.xls";
                
                File tmpFile = new File(request.getRealPath("/") + fileName);
                if( !tmpFile.exists() ){
                    tmpFile.createNewFile();
                }
                
                fOut = new FileOutputStream(tmpFile);
                
                HSSFWorkbook workbook = new HSSFWorkbook();
                workbook.createSheet("呼吸科上报数据汇总");
                HSSFSheet sheet = workbook.getSheetAt(0);
                int currentRowNum = 0;
                
                HSSFCellStyle topStyle=workbook.createCellStyle();
                topStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                topStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                topStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                topStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                topStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                topStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle top2Style=workbook.createCellStyle();
                top2Style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                top2Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                top2Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                top2Style.setLeftBorderColor(HSSFColor.BLACK.index);
                top2Style.setRightBorderColor(HSSFColor.BLACK.index);
                
                //build the header
                HSSFRow row = sheet.createRow(currentRowNum++);
                
                List<RespirologyExportData> resExportData = new ArrayList<RespirologyExportData>();
                
                RespirologyExportData test = new RespirologyExportData();
                test.setRsmName("张小伟");
                test.setRsmRegion("BJ RE Core");
                
                Map<String, Double> testMap = new LinkedHashMap<String, Double>();
                testMap.put("4月", 1471D);
                testMap.put("5月", 1465D);
                testMap.put("6月", 1287D);
                testMap.put("4月到5月", 0D);
                testMap.put("5月到6月", -0.12);
                test.setLsNumMap(testMap);
                
                testMap = new LinkedHashMap<String, Double>();
                testMap.put("4月", 0.31);
                testMap.put("5月", 0.31);
                testMap.put("6月", 0.26);
                testMap.put("4月到5月", 0D);
                testMap.put("5月到6月", -0.05);
                test.setWhRateMap(testMap);
                
                testMap = new LinkedHashMap<String, Double>();
                testMap.put("4月", 2.1);
                testMap.put("5月", 2.5);
                testMap.put("6月", 2.1);
                test.setWhDaysMap(testMap);

                testMap = new LinkedHashMap<String, Double>();
                testMap.put("4月", 0.996);
                testMap.put("5月", 0.994);
                testMap.put("6月", 1.00);
                testMap.put("4月到5月", -0.002);
                testMap.put("5月到6月", 0.006);
                test.setInRateMap(testMap);
                
                resExportData.add(test);
                
                if( null != resExportData && resExportData.size() > 0 ){
                	
                	int columnCount = 0;
                	row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("呼吸科指标");
                	row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                	sheet.addMergedRegion(new Region(0, (short)0, 0, (short)1));
                	row.getCell(0).setCellStyle(topStyle);
                	
                	RespirologyExportData resData = resExportData.get(0);
                	
                	Map<String, Double> lsNumMap = resData.getLsNumMap();
                	Iterator<String> lsNumIte = lsNumMap.keySet().iterator();
                	
                	Map<String, Double> whRateMap = resData.getWhRateMap();
                	Iterator<String> whRateIte = whRateMap.keySet().iterator();
                	
                	Map<String, Double> whDaysMap = resData.getWhDaysMap();
                	Iterator<String> whDaysIte = whDaysMap.keySet().iterator();
                	
                	Map<String, Double> inRateMap = resData.getInRateMap();
                	Iterator<String> inRateIte = inRateMap.keySet().iterator();
                	
                	int i = 0;
                	while( lsNumIte.hasNext() ){
                		lsNumIte.next();
                		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING).setCellValue("雾化令舒人数(周平均)");
                		i++;
                	}
                	columnCount += lsNumMap.size();
                	
                	i = 0;
                	while( whRateIte.hasNext() ){
                		whRateIte.next();
                		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING).setCellValue("雾化率");
                		i++;
                	}
                	columnCount += whRateMap.size();
                	
                	i = 0;
                	Iterator<String> daysIte = whDaysMap.keySet().iterator();
                	while( daysIte.hasNext() ){
                		daysIte.next();
                		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING).setCellValue("雾化天数");
                		i++;
                	}
                	columnCount += whDaysMap.size();
                	
                	i = 0;
                	while( inRateIte.hasNext() ){
                		inRateIte.next();
                		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING).setCellValue("上报率");
                		i++;
                	}
                	columnCount += inRateMap.size();
                	
                	
                	row = sheet.createRow(currentRowNum++);
                	columnCount = 0;
                	
                	HSSFCell rsmRegionTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                	rsmRegionTitleCell.setCellValue("区域");
                	rsmRegionTitleCell.setCellStyle(top2Style);
                	
                	HSSFCell rsmNameTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                	rsmNameTitleCell.setCellValue("RSM");
                	rsmNameTitleCell.setCellStyle(top2Style);
                	
                	i = 0;
                	lsNumIte = lsNumMap.keySet().iterator();
                	while( lsNumIte.hasNext() ){
                		String monthName = lsNumIte.next();
                		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING).setCellValue(monthName);
                		i++;
                	}
                	columnCount += lsNumMap.size();
                	
                	i = 0;
                	whRateIte = whRateMap.keySet().iterator();
                	while( whRateIte.hasNext() ){
                		String monthName = whRateIte.next();
                		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING).setCellValue(monthName);
                		i++;
                	}
                	columnCount += whRateMap.size();
                	
                	i = 0;
                	whDaysIte = whDaysMap.keySet().iterator();
                	while( whDaysIte.hasNext() ){
                		String monthName = whDaysIte.next();
                		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING).setCellValue(monthName);
                		i++;
                	}
                	columnCount += whDaysMap.size();
                	
                	i = 0;
                	inRateIte = inRateMap.keySet().iterator();
                	while( inRateIte.hasNext() ){
                		String monthName = inRateIte.next();
                		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING).setCellValue(monthName);
                		i++;
                	}
                	columnCount += inRateMap.size();
                	
                	HSSFCellStyle numberCellStyle = workbook.createCellStyle();
                	numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                	
                	for( RespirologyExportData res : resExportData ){
                		
                		row = sheet.createRow(currentRowNum++);
                    	columnCount = 0;
                    	
                    	HSSFCell rsmRegionValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    	rsmRegionValueCell.setCellValue(res.getRsmRegion());
                    	rsmRegionValueCell.setCellStyle(top2Style);
                    	
                    	HSSFCell rsmNameValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    	rsmNameValueCell.setCellValue(res.getRsmName());
                    	rsmNameValueCell.setCellStyle(top2Style);
                    	
                    	lsNumMap = res.getLsNumMap();
                    	lsNumIte = lsNumMap.keySet().iterator();
                    	whRateMap = res.getWhRateMap();
                    	whRateIte = whRateMap.keySet().iterator();
                    	whDaysMap = res.getWhDaysMap();
                    	whDaysIte = whDaysMap.keySet().iterator();
                    	inRateMap = res.getInRateMap();
                    	inRateIte = inRateMap.keySet().iterator();
                    	
                    	i = 0;
                    	String columnName;
                    	while( lsNumIte.hasNext() ){
                    		columnName = lsNumIte.next();
                    		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(lsNumMap.get(columnName));
                    		i++;
                    	}
                    	columnCount += lsNumMap.size();
                    	
                    	i = 0;
                    	while( whRateIte.hasNext() ){
                    		columnName = whRateIte.next();
                    		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(whRateMap.get(columnName));
                    		i++;
                    	}
                    	columnCount += whRateMap.size();
                    	
                    	i = 0;
                    	while( whDaysIte.hasNext() ){
                    		columnName = whDaysIte.next();
                    		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(whDaysMap.get(columnName));
                    		i++;
                    	}
                    	columnCount += whDaysMap.size();
                    	
                    	i = 0;
                    	while( inRateIte.hasNext() ){
                    		columnName = inRateIte.next();
                    		row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(inRateMap.get(columnName));
                    		i++;
                    	}
                    	columnCount += inRateMap.size();
                	}
                }
                
                workbook.write(fOut);
        }catch(Exception e){
            logger.error("fail to export the res month data file,",e);
        }finally{
            if( fOut != null ){
                fOut.close();
            }
        }
        
        request.getSession().setAttribute("resMonthDataFileName", fileName.substring(fileName.lastIndexOf("/")+1));
        request.getSession().setAttribute("resMonthDataFile", fileName);
        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    
    private void populateWeeklyReportFile(StringBuffer remoteWeeklyReportFile, StringBuffer weeklyReportFile2Download, StringBuffer localWeeklyReportFile, Date chooseDate_d, String fileNamePre, String fileSubName){
        weeklyReportFile2Download.append(DateUtils.getThursDayOfParamDate(chooseDate_d)).append("/")
        .append(fileNamePre)
        .append(fileSubName)
        .append("-")
        .append(DateUtils.getWeeklyDuration(chooseDate_d))
        .append(".pdf");
        
        localWeeklyReportFile.append(DateUtils.getThursDayOfParamDate(chooseDate_d)).append("/")
        .append(fileNamePre)
        .append(fileSubName)
        .append("-")
        .append(DateUtils.getThursDayOfParamDate(chooseDate_d))
        .append(".pdf");
        
        remoteWeeklyReportFile.append(DateUtils.getThursDayOfParamDate(chooseDate_d)).append("/")
        .append(fileNamePre)
        .append(fileSubName)
        .append("-")
        .append(DateUtils.getWeeklyDuration(chooseDate_d))
        .append(".pdf");
    }
    
    private void populateWeeklyReportAttachedFiles(List<String> filePaths, List<ReportFileObject> reportFiles, String localPath, String basePath, Date chooseDate_d
            , StringBuffer weeklyReportFile2Download, StringBuffer localWeeklyReportFile, StringBuffer remoteWeeklyReportFile
            , String department, String rsmRegion) throws Exception{
    	remoteWeeklyReportFile = new StringBuffer(basePath).append("weeklyReport2Download/");
        weeklyReportFile2Download = new StringBuffer(localPath).append("weeklyReport2Download/");
        localWeeklyReportFile = new StringBuffer(localPath).append("weeklyReport/");
        
        remoteWeeklyReportFile.append(DateUtils.getThursDayOfParamDate(chooseDate_d)).append("/")
        .append(department)
        .append("-BM-")
        .append(DateUtils.getWeeklyDuration(chooseDate_d))
        .append(rsmRegion)
        .append(".pdf");
        
        weeklyReportFile2Download.append(DateUtils.getThursDayOfParamDate(chooseDate_d)).append("/")
        .append(department)
        .append("-BM-")
        .append(DateUtils.getWeeklyDuration(chooseDate_d))
        .append(rsmRegion)
        .append(".pdf");
        
        localWeeklyReportFile.append(DateUtils.getThursDayOfParamDate(chooseDate_d)).append("/")
        .append(department)
        .append("-BM-")
        .append(DateUtils.getThursDayOfParamDate(chooseDate_d))
        .append(rsmRegion)
        .append(".pdf");
        
        ReportFileObject rfo = new ReportFileObject();
        rfo.setFileName(remoteWeeklyReportFile.toString().substring(remoteWeeklyReportFile.toString().lastIndexOf("/")+1));
        rfo.setFilePath(remoteWeeklyReportFile.toString());
        
        reportFiles.add(rfo);
        
        File dailyReportFile = new File(localWeeklyReportFile.toString());
        File targetReportFile = new File(weeklyReportFile2Download.toString());
        
        if( !dailyReportFile.exists() ){
            throw new Exception("no weekly report is found");
        }else{
            FileUtils.copySourceFile2TargetFile(dailyReportFile, targetReportFile);
        }
        
        filePaths.add(weeklyReportFile2Download.toString());
    }
}
