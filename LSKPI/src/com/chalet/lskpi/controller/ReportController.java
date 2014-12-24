package com.chalet.lskpi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
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

import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.ExportDoctor;
import com.chalet.lskpi.model.HomeData;
import com.chalet.lskpi.model.HomeWeeklyData;
import com.chalet.lskpi.model.HomeWeeklyNoReportDr;
import com.chalet.lskpi.model.KPIHospital4Export;
import com.chalet.lskpi.model.MobileCHEDailyData;
import com.chalet.lskpi.model.MobilePEDDailyData;
import com.chalet.lskpi.model.MobileRESDailyData;
import com.chalet.lskpi.model.MonthlyData;
import com.chalet.lskpi.model.MonthlyRatioData;
import com.chalet.lskpi.model.MonthlyStatisticsData;
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
import com.ibm.icu.util.Calendar;

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
    
    @RequestMapping("/doDownloadKPIHosData")
    public String doDownloadKPIHosData(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	logger.info("download the KPI Hospital..");
    	FileOutputStream fOut = null;
    	String fileName = null;
    	String fromWeb = request.getParameter("fromWeb");
    	try{
			String department = request.getParameter("department");
			logger.info(String.format("begin to get the KPI Hospital of department %s", department));
			List<KPIHospital4Export> dbHosData = hospitalService.getKPIHospitalByDepartment(department);
			
			File resDir = new File(request.getRealPath("/") + "kpiHosData/");
			if( !resDir.exists() ){
				resDir.mkdir();
			}
			
			File tmpFile = null;
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			if( "1".equalsIgnoreCase(department) ){
				fileName = "kpiHosData/呼吸科KPI医院列表.xls";
				tmpFile = new File(request.getRealPath("/") + fileName);
				if( !tmpFile.exists() ){
					tmpFile.createNewFile();
				}
				workbook.createSheet("呼吸科KPI医院列表");
			}else if( "2".equalsIgnoreCase(department) ){
				fileName = "kpiHosData/儿科KPI医院列表.xls";
				tmpFile = new File(request.getRealPath("/") + fileName);
				if( !tmpFile.exists() ){
					tmpFile.createNewFile();
				}
				workbook.createSheet("儿科KPI医院列表");
			}else if( "3".equalsIgnoreCase(department) ){
				fileName = "kpiHosData/胸外科KPI医院列表.xls";
				tmpFile = new File(request.getRealPath("/") + fileName);
				if( !tmpFile.exists() ){
					tmpFile.createNewFile();
				}
				workbook.createSheet("胸外科KPI医院列表");
			}else if( "4".equalsIgnoreCase(department) ){
				fileName = "kpiHosData/每月袋数KPI医院列表.xls";
				tmpFile = new File(request.getRealPath("/") + fileName);
				if( !tmpFile.exists() ){
					tmpFile.createNewFile();
				}
				workbook.createSheet("每月袋数KPI医院列表");
			}
				
			fOut = new FileOutputStream(tmpFile);
				
			HSSFSheet sheet = workbook.getSheetAt(0);
			int currentRowNum = 0;
				
			//build the header
			HSSFRow row = sheet.createRow(currentRowNum++);
			int columnNum = 0;
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Province");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("City");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Hospital Code");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Name");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Dragon Type");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Hospital Level");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("BR CNName");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("BR Name");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("RSD Code");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("RSD Name");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("RSD Tel");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("RSD Email");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("DIST NAME");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("RSM Code");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("RSM Name");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("RSM Tel");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("RSM Email");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("DSM Code");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("DSM Name");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("DSM Tel");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("DSM Email");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("是否为负责销售(是=1，否=0)");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Rep Code");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Rep Name");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Rep Tel");
			row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Rep Email");
			if( null != department && "3".equalsIgnoreCase(department) ){
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("isTop100");
			}
			if( null != department && "2".equalsIgnoreCase(department) ){
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("雾化端口数量");
			}
			
			for( KPIHospital4Export kpiHos : dbHosData ){
				row = sheet.createRow(currentRowNum++);
				columnNum = 0;
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getProvince());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getCity());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getCode());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getName());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getDragonType());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getLevel());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getBrCNName());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRegion());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRsdCode());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRsdName());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRsdTel());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRsdEmail());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRsmRegion());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRsmCode());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRsmName());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRsmTel());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getRsmEmail());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getDsmCode());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getDsmName());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getDsmTel());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getDsmEmail());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getIsMainSales());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getSalesCode());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getSalesName());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getSalesTel());
				row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getSalesEmail());
				if( null != department && "3".equalsIgnoreCase(department) ){
					row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getIsTop100());
				}
				if( null != department && "2".equalsIgnoreCase(department) ){
					row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(kpiHos.getPortNum());
				}
			}
			workbook.write(fOut);
    	}catch(Exception e){
    		logger.error("fail to download the KPI hospital file,",e);
    	}finally{
    		if( fOut != null ){
    			fOut.close();
    		}
    	}
    	request.getSession().setAttribute("kpiHosFile", fileName);
    	if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
    		return "redirect:showWebUploadData";
    	}else{
    		return "redirect:showUploadData";
    	}
    }
    @RequestMapping("/doDownloadDailyData")
    public String doDownloadDailyData(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	logger.info("download the daily data..");
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
            	logger.info(String.format("begin to get the data of department %s, from %s to %s", department,chooseDate,chooseDate_end));
            	
            	HSSFWorkbook workbook = new HSSFWorkbook();
            	
            	HSSFCellStyle percentCellStyle = workbook.createCellStyle();
                percentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
            	
            	if( "1".equalsIgnoreCase(department) ){
            		List<RespirologyData> dbResData = respirologyService.getRespirologyDataByDate(chooseDate_d,chooseDate_end_d);
            		
            		File resDir = new File(request.getRealPath("/") + "dailyResReport/");
            		if( !resDir.exists() ){
            			resDir.mkdir();
            		}
            		fileName = new StringBuffer("dailyResReport/呼吸科原始数据-")
                    .append(simpledateformat.format(chooseDate_d))
                    .append("-")
                    .append(simpledateformat.format(chooseDate_end_d))
                    .append(".xls").toString();
            		File tmpFile = new File(request.getRealPath("/") + fileName);
            		if( !tmpFile.exists() ){
            			tmpFile.createNewFile();
            		}
            		
            		fOut = new FileOutputStream(tmpFile);
            		
            		workbook.createSheet("原始数据");
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int currentRowNum = 0;
                    
                    //build the header
                    HSSFRow row = sheet.createRow(currentRowNum++);
                    row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("编号");
                    row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("录入日期");
                    row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("医院编号");
                    row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("医院名称");
                    row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("当日目标科室病房病人数");
                    row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("当日病房内AECOPD人数");
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
                    row.createCell(20, XSSFCell.CELL_TYPE_STRING).setCellValue("是否为KPI医院（在=1，不在=0）");
                    row.createCell(21, XSSFCell.CELL_TYPE_STRING).setCellValue("Dragon Type");
                    
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
                        
                        HSSFCell oqdCell = row.createCell(13, XSSFCell.CELL_TYPE_NUMERIC);
                        oqdCell.setCellValue(resData.getOqd()/100);
                        oqdCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell tqdCell = row.createCell(14, XSSFCell.CELL_TYPE_NUMERIC);
                        tqdCell.setCellValue(resData.getTqd()/100);
                        tqdCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell otidCell = row.createCell(15, XSSFCell.CELL_TYPE_NUMERIC);
                        otidCell.setCellValue(resData.getOtid()/100);
                        otidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell tbidCell = row.createCell(16, XSSFCell.CELL_TYPE_NUMERIC);
                        tbidCell.setCellValue(resData.getTbid()/100);
                        tbidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell ttidCell = row.createCell(17, XSSFCell.CELL_TYPE_NUMERIC);
                        ttidCell.setCellValue(resData.getTtid()/100);
                        ttidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell thbidCell = row.createCell(18, XSSFCell.CELL_TYPE_NUMERIC);
                        thbidCell.setCellValue(resData.getThbid()/100);
                        thbidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell fbidCell = row.createCell(19, XSSFCell.CELL_TYPE_NUMERIC);
                        fbidCell.setCellValue(resData.getFbid()/100);
                        fbidCell.setCellStyle(percentCellStyle);
                        
                        row.createCell(20, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getIsResAssessed());
                        row.createCell(21, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getDragonType());
                    }
                    workbook.write(fOut);
            	}else if( "2".equalsIgnoreCase(department) ){
            		
            		List<PediatricsData> dbPedData = pediatricsService.getPediatricsDataByDate(chooseDate_d,chooseDate_end_d);
            		
            		File pedDir = new File(request.getRealPath("/") + "dailyPedReport/");
            		if( !pedDir.exists() ){
            			pedDir.mkdir();
            		}
            		fileName = new StringBuffer("dailyPedReport/儿科原始数据-")
                            .append(simpledateformat.format(chooseDate_d))
                            .append("-")
                            .append(simpledateformat.format(chooseDate_end_d))
                            .append(".xls").toString();
            		File tmpFile = new File(request.getRealPath("/") + fileName);
            		if( !tmpFile.exists() ){
            			tmpFile.createNewFile();
            		}
            		
            		fOut = new FileOutputStream(tmpFile);
            		
            		workbook.createSheet("原始数据");
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int currentRowNum = 0;
                    
                    //build the header
                    HSSFRow row = sheet.createRow(currentRowNum++);
                    int columnNum = 0;
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("编号");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("录入日期");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("医院编号");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("医院名称");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("当日门诊人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("当日雾化人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("当日雾化令舒人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("雾化端口数量");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表ETMSCode");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表姓名");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("所属DSM");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("所属Region");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("所属RSM Region");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("0.5mg QD");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("0.5mg BID");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("1mg QD");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("1mg BID");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg QD");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg BID");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("该医院主要处方方式");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("是否为KPI医院（在=1，不在=0）");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("Dragon Type");
                    
                    for( PediatricsData pedData : dbPedData ){
                    	row = sheet.createRow(currentRowNum++);
                    	
                    	int dataColumnNum = 0;
                    	row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(currentRowNum-1);
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(exportdateformat.format(pedData.getCreatedate()));
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getHospitalCode());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getHospitalName());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getPnum());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getWhnum());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getLsnum());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getPortNum());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getSalesETMSCode());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getSalesName());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getDsmName());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getRegion());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getRsmRegion());
                        
                        HSSFCell hqdCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        hqdCell.setCellValue(pedData.getHqd()/100);
                        hqdCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell hbidCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        hbidCell.setCellValue(pedData.getHbid()/100);
                        hbidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell oqdCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        oqdCell.setCellValue(pedData.getOqd()/100);
                        oqdCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell obidCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        obidCell.setCellValue(pedData.getObid()/100);
                        obidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell tqdCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        tqdCell.setCellValue(pedData.getTqd()/100);
                        tqdCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell tbidCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        tbidCell.setCellValue(pedData.getTbid()/100);
                        tbidCell.setCellStyle(percentCellStyle);
                        
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(populateRecipeTypeValue(pedData.getRecipeType()));
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getIsPedAssessed());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue(pedData.getDragonType());
                    }
                    workbook.write(fOut);
            	}else if( "3".equalsIgnoreCase(department) ){
            	    List<ChestSurgeryData> dbChestSurgeryData = chestSurgeryService.getChestSurgeryDataByDate(chooseDate_d,chooseDate_end_d);
            	    
            	    File cheDir = new File(request.getRealPath("/") + "dailyCheReport/");
                    if( !cheDir.exists() ){
                        cheDir.mkdir();
                    }
                    fileName = new StringBuffer("dailyCheReport/胸外科原始数据-")
                            .append(simpledateformat.format(chooseDate_d))
                            .append("-")
                            .append(simpledateformat.format(chooseDate_end_d))
                            .append(".xls").toString();
                    File tmpFile = new File(request.getRealPath("/") + fileName);
                    if( !tmpFile.exists() ){
                        tmpFile.createNewFile();
                    }
                    
                    fOut = new FileOutputStream(tmpFile);
                    
                    workbook.createSheet("原始数据");
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int currentRowNum = 0;
                    
                    //build the header
                    HSSFRow row = sheet.createRow(currentRowNum++);
                    row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("编号");
                    row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("录入日期");
                    row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("医院编号");
                    row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("医院名称");
                    row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("当日病房病人人数");
                    row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("当日病房内合并COPD或哮喘的手术病人数");
                    row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("当日雾化人数");
                    row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("当日雾化令舒病人数");
                    row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("所属Region");
                    row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue("所属RSM Region");
                    row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue("所属DSM");
                    row.createCell(11, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表Code");
                    row.createCell(12, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表姓名");
                    row.createCell(13, XSSFCell.CELL_TYPE_STRING).setCellValue("1mg QD");
                    row.createCell(14, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg QD");
                    row.createCell(15, XSSFCell.CELL_TYPE_STRING).setCellValue("1mg TID");
                    row.createCell(16, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg BID");
                    row.createCell(17, XSSFCell.CELL_TYPE_STRING).setCellValue("2mg TID");
                    row.createCell(18, XSSFCell.CELL_TYPE_STRING).setCellValue("3mg BID");
                    row.createCell(19, XSSFCell.CELL_TYPE_STRING).setCellValue("4mg BID");
                    row.createCell(20, XSSFCell.CELL_TYPE_STRING).setCellValue("是否为KPI医院（在=1，不在=0）");
                    
                    for( ChestSurgeryData cheData : dbChestSurgeryData ){
                        row = sheet.createRow(currentRowNum++);
                        row.createCell(0, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(currentRowNum-1);
                        row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue(exportdateformat.format(cheData.getCreatedate()));
                        row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue(cheData.getHospitalCode());
                        row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue(cheData.getHospitalName());
                        row.createCell(4, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(cheData.getPnum());
                        row.createCell(5, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(cheData.getRisknum());
                        row.createCell(6, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(cheData.getWhnum());
                        row.createCell(7, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(cheData.getLsnum());
                        row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue(cheData.getRegion());
                        row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue(cheData.getRsmRegion());
                        row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue(cheData.getDsmName());
                        row.createCell(11, XSSFCell.CELL_TYPE_STRING).setCellValue(cheData.getSalesCode());
                        row.createCell(12, XSSFCell.CELL_TYPE_STRING).setCellValue(cheData.getSalesName());
                        
                        HSSFCell oqdCell = row.createCell(13, XSSFCell.CELL_TYPE_NUMERIC);
                        oqdCell.setCellValue(cheData.getOqd()/100);
                        oqdCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell tqdCell = row.createCell(14, XSSFCell.CELL_TYPE_NUMERIC);
                        tqdCell.setCellValue(cheData.getTqd()/100);
                        tqdCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell otidCell = row.createCell(15, XSSFCell.CELL_TYPE_NUMERIC);
                        otidCell.setCellValue(cheData.getOtid()/100);
                        otidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell tbidCell = row.createCell(16, XSSFCell.CELL_TYPE_NUMERIC);
                        tbidCell.setCellValue(cheData.getTbid()/100);
                        tbidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell ttidCell = row.createCell(17, XSSFCell.CELL_TYPE_NUMERIC);
                        ttidCell.setCellValue(cheData.getTtid()/100);
                        ttidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell thbidCell = row.createCell(18, XSSFCell.CELL_TYPE_NUMERIC);
                        thbidCell.setCellValue(cheData.getThbid()/100);
                        thbidCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell fbidCell = row.createCell(19, XSSFCell.CELL_TYPE_NUMERIC);
                        fbidCell.setCellValue(cheData.getFbid()/100);
                        fbidCell.setCellStyle(percentCellStyle);
                        
                        row.createCell(20, XSSFCell.CELL_TYPE_STRING).setCellValue(cheData.getIsChestSurgeryAssessed());
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
                row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                sheet.addMergedRegion(new Region(0, (short)5, 0, (short)8));
                row.getCell(5).setCellStyle(topStyle);
                
                row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                
                row.createCell(10, XSSFCell.CELL_TYPE_STRING).setCellValue("维持期治疗");
                row.createCell(11, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                row.createCell(12, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                sheet.addMergedRegion(new Region(0, (short)10, 0, (short)12));
                row.getCell(10).setCellStyle(topStyle);
                
                row.createCell(13, XSSFCell.CELL_TYPE_STRING).setCellValue("维持期令舒治疗天数（DOT)");
                row.createCell(14, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                row.createCell(15, XSSFCell.CELL_TYPE_STRING).setCellValue("");
                sheet.addMergedRegion(new Region(0, (short)13, 0, (short)15));
                row.getCell(13).setCellStyle(topStyle);
                
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
                psrCell.setCellValue("销售代表");
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
                
                HSSFCell drIdCell = row.createCell(8, XSSFCell.CELL_TYPE_STRING);
                drIdCell.setCellValue("目标医生ID");
                drIdCell.setCellStyle(top2Style);
                
                HSSFCell saleNumCell = row.createCell(9, XSSFCell.CELL_TYPE_STRING);
                saleNumCell.setCellValue("每周新病人人次");
                saleNumCell.setCellStyle(top2Style);
                
                HSSFCell num1Cell = row.createCell(10, XSSFCell.CELL_TYPE_STRING);
                num1Cell.setCellValue("哮喘*患者人次");
                num1Cell.setCellStyle(top2Style);
                
                HSSFCell num2Cell = row.createCell(11, XSSFCell.CELL_TYPE_STRING);
                num2Cell.setCellValue("处方>=8天的哮喘维持期病人次");
                num2Cell.setCellStyle(top2Style);
                
                HSSFCell num3Cell = row.createCell(12, XSSFCell.CELL_TYPE_STRING);
                num3Cell.setCellValue("维持期使用令舒的人次");
                num3Cell.setCellStyle(top2Style);
                
                HSSFCell num4Cell = row.createCell(13, XSSFCell.CELL_TYPE_STRING);
                num4Cell.setCellValue("8<=DOT<15天，病人次");
                num4Cell.setCellStyle(top2Style);
                
                HSSFCell num5Cell = row.createCell(14, XSSFCell.CELL_TYPE_STRING);
                num5Cell.setCellValue("15<=DOT<30天，病人次");
                num5Cell.setCellStyle(top2Style);
                
                HSSFCell num6Cell = row.createCell(15, XSSFCell.CELL_TYPE_STRING);
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
                sheet.setColumnWidth(15, numColumnWidth*256);
                
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
                    row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue(homeData.getDoctorId());
                    
                    HSSFCell value1Cell = row.createCell(9, XSSFCell.CELL_TYPE_NUMERIC);
                    value1Cell.setCellValue(homeData.getSalenum());
                    value1Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value2Cell = row.createCell(10, XSSFCell.CELL_TYPE_NUMERIC);
                    value2Cell.setCellValue(homeData.getAsthmanum());
                    value2Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value3Cell = row.createCell(11, XSSFCell.CELL_TYPE_NUMERIC);
                    value3Cell.setCellValue(homeData.getLtenum());
                    value3Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value4Cell = row.createCell(12, XSSFCell.CELL_TYPE_NUMERIC);
                    value4Cell.setCellValue(homeData.getLsnum());
                    value4Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value5Cell = row.createCell(13, XSSFCell.CELL_TYPE_NUMERIC);
                    value5Cell.setCellValue(homeData.getEfnum());
                    value5Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value6Cell = row.createCell(14, XSSFCell.CELL_TYPE_NUMERIC);
                    value6Cell.setCellValue(homeData.getFtnum());
                    value6Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value7Cell = row.createCell(15, XSSFCell.CELL_TYPE_NUMERIC);
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
    			Date reportEndDate = new Date(reportBeginDate.getTime() + 6 * 24 * 60 * 60 * 1000);
    			
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
    			row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			sheet.addMergedRegion(new Region(0, (short)1, 0, (short)4));
    			row.getCell(1).setCellStyle(topStyle);
    			
    			row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("处方情况");
    			row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue("");
    			sheet.addMergedRegion(new Region(0, (short)5, 0, (short)9));
    			row.getCell(5).setCellStyle(topStyle);
    			
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
    			
    			HSSFCell reportNumCell = row.createCell(3, XSSFCell.CELL_TYPE_STRING);
    			reportNumCell.setCellValue("上周上报医生数");
    			reportNumCell.setCellStyle(top2Style);
    			
    			HSSFCell inRateCell = row.createCell(4, XSSFCell.CELL_TYPE_STRING);
    			inRateCell.setCellValue("上报率");
    			inRateCell.setCellStyle(top2Style);
    			
    			HSSFCell dsmCell = row.createCell(5, XSSFCell.CELL_TYPE_STRING);
    			dsmCell.setCellValue("每周新病人人次");
    			dsmCell.setCellStyle(top2Style);
    			
    			HSSFCell psrCell = row.createCell(6, XSSFCell.CELL_TYPE_STRING);
    			psrCell.setCellValue("维持期治疗率");
    			psrCell.setCellStyle(top2Style);
    			
    			HSSFCell hosCodeCell = row.createCell(7, XSSFCell.CELL_TYPE_STRING);
    			hosCodeCell.setCellValue("维持期使用令舒的人次");
    			hosCodeCell.setCellStyle(top2Style);
    			
    			HSSFCell hosNameCell = row.createCell(8, XSSFCell.CELL_TYPE_STRING);
    			hosNameCell.setCellValue("维持期令舒比例");
    			hosNameCell.setCellStyle(top2Style);
    			
    			HSSFCell drNameCell = row.createCell(9, XSSFCell.CELL_TYPE_STRING);
    			drNameCell.setCellValue("家庭雾化疗程达标人次（DOT>=30天）");
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
    			sheet.setColumnWidth(8, dateColumnWidth*256);
    			sheet.setColumnWidth(9, dateColumnWidth*256);
    			
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
                    
                    Calendar aCalendar = Calendar.getInstance();
                    aCalendar.setTime(new Date());
                    int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
                    aCalendar.setTime(reportBeginDate);
                    int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
                    
                    if( ((day1 - day2 > 6) && (day1 - day2 < 10)) || day1 - day2 < 0 ){
                    	value3Cell.setCellValue("N/A");
                    }else{
                    	value3Cell.setCellValue(rsmDate.getNewDrNum());
                    }
                    value3Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell reportValueCell = row.createCell(3, XSSFCell.CELL_TYPE_NUMERIC);
                    reportValueCell.setCellValue(rsmDate.getReportNum());
                    reportValueCell.setCellStyle(numberCellStyle);
                    
                    HSSFCell inRateValueCell = row.createCell(4, XSSFCell.CELL_TYPE_NUMERIC);
//                    inRateValueCell.setCellValue((double)rsmDate.getReportNum()/(double)rsmDate.getTotalDrNum());
                    inRateValueCell.setCellValue(rsmDate.getInRate());
                    inRateValueCell.setCellStyle(percentCellStyle);
                    
                    HSSFCell value4Cell = row.createCell(5, XSSFCell.CELL_TYPE_NUMERIC);
                    value4Cell.setCellValue(rsmDate.getNewWhNum());
                    value4Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value5Cell = row.createCell(6, XSSFCell.CELL_TYPE_NUMERIC);
                    value5Cell.setCellValue(rsmDate.getCureRate());
                    value5Cell.setCellStyle(percentCellStyle);
                    
                    HSSFCell value6Cell = row.createCell(7, XSSFCell.CELL_TYPE_NUMERIC);
                    value6Cell.setCellValue(rsmDate.getLsnum());
                    value6Cell.setCellStyle(numberCellStyle);
                    
                    HSSFCell value7Cell = row.createCell(8, XSSFCell.CELL_TYPE_NUMERIC);
                    value7Cell.setCellValue(rsmDate.getLsRate());
                    value7Cell.setCellStyle(percentCellStyle);
                    
                    HSSFCell value8Cell = row.createCell(9, XSSFCell.CELL_TYPE_NUMERIC);
                    value8Cell.setCellValue(rsmDate.getReachRate());
                    value8Cell.setCellStyle(numberCellStyle);
    			}
    			
    			workbook.createSheet("未上报家庭雾化医生名单");
    			HSSFSheet sheet2 = workbook.getSheetAt(1);
    			currentRowNum = 0;
    			
    			HSSFRow doctorRow = sheet2.createRow(currentRowNum++);
    			
                HSSFFont font = workbook.createFont();
                font.setColor(HSSFColor.WHITE.index);
                
                HSSFCellStyle noReportDrTop1Style=workbook.createCellStyle();
                noReportDrTop1Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                noReportDrTop1Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                noReportDrTop1Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                noReportDrTop1Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                noReportDrTop1Style.setLeftBorderColor(HSSFColor.BLACK.index);
                noReportDrTop1Style.setRightBorderColor(HSSFColor.BLACK.index);
                noReportDrTop1Style.setFillForegroundColor(HSSFColor.BLUE.index);
                noReportDrTop1Style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                noReportDrTop1Style.setFont(font);
                
                HSSFCellStyle noReportDrTop2Style=workbook.createCellStyle();
                noReportDrTop2Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                noReportDrTop2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                noReportDrTop2Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                noReportDrTop2Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                noReportDrTop2Style.setLeftBorderColor(HSSFColor.BLACK.index);
                noReportDrTop2Style.setRightBorderColor(HSSFColor.BLACK.index);
                noReportDrTop2Style.setFillForegroundColor(HSSFColor.VIOLET.index);
                noReportDrTop2Style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                noReportDrTop2Style.setFont(font);
    			
    			int columnNum = 0;
    			sheet2.setColumnWidth(columnNum, dateColumnWidth*256);
    			HSSFCell doctor_rsdCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_rsdCell.setCellValue("区域");
    			doctor_rsdCell.setCellStyle(noReportDrTop1Style);
    			
    			sheet2.setColumnWidth(columnNum, dateColumnWidth*256);
    			HSSFCell doctor_rsmCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_rsmCell.setCellValue("大区");
    			doctor_rsmCell.setCellStyle(noReportDrTop1Style);
    			
    			sheet2.setColumnWidth(columnNum, dateColumnWidth*256);
    			HSSFCell doctor_dsmCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_dsmCell.setCellValue("DSM");
    			doctor_dsmCell.setCellStyle(noReportDrTop1Style);
    			
    			sheet2.setColumnWidth(columnNum, dateColumnWidth*256);
    			HSSFCell doctor_salesCodeCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_salesCodeCell.setCellValue("销售Code");
    			doctor_salesCodeCell.setCellStyle(noReportDrTop1Style);
    			
    			sheet2.setColumnWidth(columnNum, dateColumnWidth*256);
    			HSSFCell doctor_repCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_repCell.setCellValue("销售代表");
    			doctor_repCell.setCellStyle(noReportDrTop1Style);
    			
    			sheet2.setColumnWidth(columnNum, dateColumnWidth*256);
    			HSSFCell doctor_hospitalCodeCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_hospitalCodeCell.setCellValue("目标医院Code");
    			doctor_hospitalCodeCell.setCellStyle(noReportDrTop2Style);
    			
    			sheet2.setColumnWidth(columnNum, 18*256);
    			HSSFCell doctor_hospitalNameCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_hospitalNameCell.setCellValue("目标医院名称");
    			doctor_hospitalNameCell.setCellStyle(noReportDrTop2Style);
    			
    			sheet2.setColumnWidth(columnNum, dateColumnWidth*256);
    			HSSFCell doctor_doctorCodeCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_doctorCodeCell.setCellValue("目标医生Code");
    			doctor_doctorCodeCell.setCellStyle(noReportDrTop2Style);
    			
    			sheet2.setColumnWidth(columnNum, dateColumnWidth*256);
    			HSSFCell doctor_doctorIdCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_doctorIdCell.setCellValue("目标医生ID");
    			doctor_doctorIdCell.setCellStyle(noReportDrTop2Style);
    			
    			sheet2.setColumnWidth(columnNum, dateColumnWidth*256);
    			HSSFCell doctor_doctorNameCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			doctor_doctorNameCell.setCellValue("目标医生");
    			doctor_doctorNameCell.setCellStyle(noReportDrTop2Style);
    			
    			List<HomeWeeklyNoReportDr> noReportDrList = homeService.getWeeklyNoReportDr(reportBeginDate);
    			
    			for( HomeWeeklyNoReportDr noReportDr : noReportDrList ){
    				doctorRow = sheet2.createRow(currentRowNum++);
    			    columnNum = 0;
    			 
    			    HSSFCell rsdValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    rsdValueCell.setCellValue(noReportDr.getRsd());
    			    
    			    HSSFCell rsmValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    rsmValueCell.setCellValue(noReportDr.getRsm());
    			    
    			    HSSFCell dsmValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    dsmValueCell.setCellValue(noReportDr.getDsm());
    			    
    			    HSSFCell repCodeValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    repCodeValueCell.setCellValue(noReportDr.getSalesCode());
    			    
    			    HSSFCell repValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    repValueCell.setCellValue(noReportDr.getRep());
    			    
    			    HSSFCell hospitalCodeValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    hospitalCodeValueCell.setCellValue(noReportDr.getHospitalCode());
    			    
    			    HSSFCell hospitalNameValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    hospitalNameValueCell.setCellValue(noReportDr.getHospitalName());
    			    
    			    HSSFCell drCodeValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    drCodeValueCell.setCellValue(noReportDr.getDoctorCode());
    			    
    			    HSSFCell drIdValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    drIdValueCell.setCellValue(noReportDr.getDoctorId());
    			    
    			    HSSFCell drNameValueCell = doctorRow.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
    			    drNameValueCell.setCellValue(noReportDr.getDoctorName());
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
            row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("销售Code");
            row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表");
            row.getCell(0).setCellStyle(topStyle);
            row.getCell(1).setCellStyle(topStyle);
            row.getCell(2).setCellStyle(topStyle);
            row.getCell(3).setCellStyle(topStyle);
            row.getCell(4).setCellStyle(topStyle);
            
            row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("目标医院Code");
            row.createCell(6, XSSFCell.CELL_TYPE_STRING).setCellValue("目标医院名称");
            row.createCell(7, XSSFCell.CELL_TYPE_STRING).setCellValue("目标医生Code");
            row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue("目标医生ID");
            row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue("目标医生");
            row.getCell(5).setCellStyle(top2Style);
            row.getCell(6).setCellStyle(top2Style);
            row.getCell(7).setCellStyle(top2Style);
            row.getCell(8).setCellStyle(top2Style);
            row.getCell(9).setCellStyle(top2Style);
            
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
            sheet.setColumnWidth(9, userColumnWidth*256);
            
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
                row.createCell(8, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getId());
                row.createCell(9, XSSFCell.CELL_TYPE_STRING).setCellValue(doctor.getDoctorName());
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
            logger.info("daily PED report, the current user is " + telephone);
            
            List<MobilePEDDailyData> mobilePEDData = pediatricsService.getDailyPEDData4Mobile(telephone,currentUser);
            logger.info("get daily ped data for mobile end...");
            if( !LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            	List<MobilePEDDailyData> mobilePEDChildData = pediatricsService.getDailyPEDChildData4Mobile(telephone,currentUser);
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
            
            logger.info(String.format("begin to get the last 12 weeks report, current user is %s", currentUser.getTelephone()));
            //add the last 12 weekly report
            String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
            String localPath = request.getRealPath("/");
            StringBuffer localReportFile = new StringBuffer(localPath);
            StringBuffer remoteReportFile = new StringBuffer(basePath);
            
            String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"weeklyHTMLReport");
            String reportGenerateDate = DateUtils.getDirectoryNameOfLastDuration();
            
            Date now = new Date();
            if( now.getDay() > 3 || now.getDay() == 0 ){
				reportGenerateDate = DateUtils.getDirectoryNameOfLastDuration(new Date(now.getTime()+ 7 * 24 * 60 * 60 * 1000));
			}
            
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
                remoteReportFile.append(directory).append(reportGenerateDate).append("/")
                .append("weeklyHomeReport-")
                .append(currentUser.getLevel())
                .append(".html");
                
                localReportFile.append(directory).append(reportGenerateDate).append("/")
                .append("weeklyHomeReport-")
                .append(currentUser.getLevel())
                .append(".html");
            }else{
                remoteReportFile.append(directory).append(reportGenerateDate).append("/")
                .append("weeklyHomeReport-")
                .append(currentUser.getLevel())
                .append("-")
                .append(currentUserTel)
                .append(".html");
                
                localReportFile.append(directory).append(reportGenerateDate).append("/")
                .append("weeklyHomeReport-")
                .append(currentUser.getLevel())
                .append("-")
                .append(currentUserTel)
                .append(".html");
            }
            
            File reportfile = new File(localReportFile.toString());
            if( reportfile.exists() ){
                view.addObject("reportFile", remoteReportFile.toString());
            }else{
                view.addObject("reportFile", basePath+"jsp/weeklyReport_404.html");
            }
            
        }catch(Exception e){
            logger.error("fail to get the last 12 home weekly data,",e);
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
        	logger.info("can not get the monthly report of last month, then get the last 2 month report.");
        	
        	localReportFile = new StringBuffer(localPath);
            remoteReportFile = new StringBuffer(basePath);
            
        	remoteReportFile.append(directory).append(DateUtils.getLast2Month()).append("/")
            .append("monthlyReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(currentUserTel)
            .append("-")
            .append(DateUtils.getLast2Month())
            .append(".html");
            
            localReportFile.append(directory).append(DateUtils.getLast2Month()).append("/")
                .append("monthlyReport-")
                .append(currentUser.getLevel())
                .append("-")
                .append(currentUserTel)
                .append("-")
                .append(DateUtils.getLast2Month())
                .append(".html");
        	
            reportfile = new File(localReportFile.toString());
            
            if( reportfile.exists() ){
                view.addObject("monthlyReportFile", remoteReportFile.toString());
            }else{
            	view.addObject("monthlyReportFile", basePath+"jsp/weeklyReport_404.html");
            }
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
        
        if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            remotepedReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyPEDReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localpedReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyPEDReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
        }else{
            remotepedReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyPEDReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(currentUserTel)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localpedReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyPEDReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(currentUserTel)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
        }
        
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
        
        if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            remoteResReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyRESReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localResReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyRESReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
        }else{
            remoteResReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyRESReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(currentUserTel)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localResReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyRESReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(currentUserTel)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
        }
        
        
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
    
    @RequestMapping("/cheWeeklyreport")
    public ModelAndView cheWeeklyreport(HttpServletRequest request){
        logger.info("weekly chest surgery report");
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
        StringBuffer localReportFile = new StringBuffer(localPath);
        StringBuffer remoteReportFile = new StringBuffer(basePath);
        
        String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"weeklyHTMLReport");
        
        if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            remoteReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyCHEReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyCHEReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
        }else{
            remoteReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyCHEReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(currentUserTel)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
            
            localReportFile.append(directory).append(DateUtils.getDirectoryNameOfLastDuration()).append("/")
            .append("weeklyCHEReport-")
            .append(currentUser.getLevel())
            .append("-")
            .append(currentUserTel)
            .append("-")
            .append(DateUtils.getDirectoryNameOfLastDuration())
            .append(".html");
        }
        
        File reportfile = new File(localReportFile.toString());
        if( reportfile.exists() ){
            view.addObject("reportFile", remoteReportFile.toString());
        }else{
            view.addObject("reportFile", basePath+"jsp/weeklyReport_404.html");
        }
        view.setViewName("cheWeeklyReport");
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
			
			String directoryName = DateUtils.getDirectoryNameOfCurrentDuration(chooseDate_d);
			if( null != selectedDSM && !"".equalsIgnoreCase(selectedDSM) ){
			    UserInfo dsm = userService.getUserInfoByTel(selectedDSM);
                
			    switch(department){
				    case "1":
				    	populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "呼吸科周报-DSM-", dsm.getName(),directoryName);
				    	break;
				    case "2":
				    	populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "儿科周报-DSM-", dsm.getName(),directoryName);
				    	break;
				    case "3":
				    	populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "胸外科周报-DSM-", dsm.getName(),directoryName);
				    	break;
				    case "4":
				    	directoryName = DateUtils.getDirectoryNameOfCurrentDuration(new Date(chooseDate_d.getTime() + 7*24*60*60*1000));
				    	populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "家庭雾化周报-DSM-", dsm.getName(),directoryName);
				    	break;
			    	default:
			    }
			}else if( null != selectedRSM && !"".equalsIgnoreCase(selectedRSM) ){
				switch(department){
					case "1":
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "呼吸科周报-RSM-", selectedRSM,directoryName);
						break;
					case "2":
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "儿科周报-RSM-", selectedRSM,directoryName);
						break;
					case "3":
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "胸外科周报-RSM-", selectedRSM,directoryName);
						break;
					case "4":
						directoryName = DateUtils.getDirectoryNameOfCurrentDuration(new Date(chooseDate_d.getTime() + 7*24*60*60*1000));
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "家庭雾化周报-RSM-", selectedRSM,directoryName);
						break;
					default:
				}
			}else if( null != selectedRSD && !"0".equalsIgnoreCase(selectedRSD) ){
				switch(department){
					case "1":
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "呼吸科周报-RSD-", selectedRSD,directoryName);
						break;
					case "2":
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "儿科周报-RSD-", selectedRSD,directoryName);
						break;
					case "3":
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "胸外科周报-RSD-", selectedRSD,directoryName);
						break;
					case "4":
						directoryName = DateUtils.getDirectoryNameOfCurrentDuration(new Date(chooseDate_d.getTime() + 7*24*60*60*1000));
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "家庭雾化周报-RSD-", selectedRSD,directoryName);
						break;
					default:
				}
			}else{
			  //the whole country is selected.
			    List<String> filePaths = new ArrayList<String>();
			    try{
			    	switch(department){
						case "1":
							populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "呼吸科周报", "",directoryName);
							break;
						case "2":
							populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "儿科周报", "",directoryName);
							break;
						case "3":
							populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "胸外科周报", "",directoryName);
							break;
						case "4":
							directoryName = DateUtils.getDirectoryNameOfCurrentDuration(new Date(chooseDate_d.getTime() + 7*24*60*60*1000));
							populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath, chooseDate_d, weeklyReportFile2Download, localWeeklyReportFile, remoteWeeklyReportFile, "家庭雾化周报", "",directoryName);
							break;
						default:
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
            int rowNumC = chestSurgeryService.removeOldWeeklyData(duration);
            logger.info(String.format("CHE:the number of rows affected is %s", rowNumC));
            logger.info(String.format("remove old weekly data done, start to generate the weekly data, the refresh date is %s", refreshDate));
            
            Date weeklyRefreshDate = DateUtils.getGenerateWeeklyReportDate(refreshDate);
            pediatricsService.generateWeeklyPEDDataOfHospital(weeklyRefreshDate);
            respirologyService.generateWeeklyRESDataOfHospital(weeklyRefreshDate);
            chestSurgeryService.generateWeeklyDataOfHospital(weeklyRefreshDate);
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
    

    @RequestMapping("/refreshWeeklyHospitalData")
    public String refreshWeeklyHospitalData(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String refreshDate_s = request.getParameter("refreshDate");
        logger.info(String.format("start to refresh the weekly hospital Data, refresh date is %s", refreshDate_s));
        
        try{
            
            SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
            Date refreshDate = formatter.parse(refreshDate_s);
            
            String duration = DateUtils.getTheBeginDateOfRefreshDate(refreshDate)+"-"+DateUtils.getTheEndDateOfRefreshDate(refreshDate);
            logger.info(String.format("start to remove the old weekly data, the duration is %s", duration));
            
            int rowNumP = hospitalService.deleteOldHospitalWeeklyData(duration);
            logger.info(String.format("hospital weekly data : the number of rows affected is %s", rowNumP));
            
            logger.info(String.format("remove old weekly data done, start to generate the weekly data, the duration is %s", duration));
            
            Date weeklyRefreshDate = DateUtils.getGenerateWeeklyReportDate(refreshDate);
            hospitalService.generateWeeklyDataOfHospital(weeklyRefreshDate);
            
            request.getSession().setAttribute(LsAttributes.WEEKLY_HOS_REFRESH_MESSAGE, LsAttributes.WEEKLY_HOS_DATA_REFRESHED);
            
        }catch(Exception e){
            logger.error("fail to refresh the weekly hospital data,",e);
        }
        
        logger.info("end to refresh the weekly hospital data");
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
        	String department = request.getParameter("department");
        	
            if( null == chooseDate || "".equalsIgnoreCase(chooseDate) ){
                logger.error("there is no date chose.");
            }else{
                logger.info(String.format("begin to get the monthly statistical data in %s,level is %s, department is %s", chooseDate,level,department));
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date inRateDate = formatter.parse(chooseDate);
                String monthName = DateUtils.getMonthInCN(inRateDate);
                
                String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
                String localPath = request.getRealPath("/");
                StringBuffer remoteReportFile = new StringBuffer(basePath);
                StringBuffer localReportFile = new StringBuffer(localPath);
                long systemTime = System.currentTimeMillis();
                
                String departmentName = "";
                switch(department){
	                case "1":
	                	departmentName = "呼吸科";
	                	break;
	                case "2":
	                	departmentName = "儿科";
	                	break;
	                case "3":
	                	departmentName = "胸外科";
	                	break;
                }
                
                remoteReportFile.append("monthlyInRate/")
            	.append(monthName).append(departmentName).append(level).append("汇总数据-").append(systemTime).append(".xls");
                localReportFile.append("monthlyInRate/")
            	.append(monthName).append(departmentName).append(level).append("汇总数据-").append(systemTime).append(".xls");
                
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
                    String title = monthName+level+"汇总数据";
                    workbook.createSheet(title);
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    int currentRowNum = 0;
                    
                    HSSFCellStyle percentCellStyle = workbook.createCellStyle();
                    percentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
                    percentCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    percentCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    percentCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                    percentCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                    
                    HSSFCellStyle numberCellStyle = workbook.createCellStyle();
                    numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    numberCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    numberCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    numberCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                    numberCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                    
                    HSSFCellStyle averageDoseCellStyle = workbook.createCellStyle();
                    averageDoseCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                    averageDoseCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    averageDoseCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                    
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
                    int columnNum = 0;
                    HSSFCell userCell = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    userCell.setCellValue(level+"名单");
                    userCell.setCellStyle(top2Style);
                    sheet.setColumnWidth(columnNum-1, 15*256);
                    
                    if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(level) ){
                    	HSSFCell rsmTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    	rsmTitle.setCellValue("RSM");
                    	rsmTitle.setCellStyle(top2Style);
                    }
                    
                    HSSFCell inRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    inRateTitle.setCellValue("上报率");
                    inRateTitle.setCellStyle(top2Style);
                    
                    HSSFCell coreInRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    coreInRateTitle.setCellValue("Core医院上报率");
                    coreInRateTitle.setCellStyle(top2Style);
                    sheet.setColumnWidth(columnNum-1, 20*256);
                    
                    HSSFCell emergingInRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    emergingInRateTitle.setCellValue("Emerging医院上报率");
                    emergingInRateTitle.setCellStyle(top2Style);
                    sheet.setColumnWidth(columnNum-1, 20*256);
                    
                    HSSFCell whRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    whRateTitle.setCellValue("雾化率");
                    whRateTitle.setCellStyle(top2Style);
                    
                    HSSFCell coreWhRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    coreWhRateTitle.setCellValue("Core医院雾化率");
                    coreWhRateTitle.setCellStyle(top2Style);
                    sheet.setColumnWidth(columnNum-1, 20*256);
                    
                    HSSFCell emergingWhRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    emergingWhRateTitle.setCellValue("Emerging医院雾化率");
                    emergingWhRateTitle.setCellStyle(top2Style);
                    sheet.setColumnWidth(columnNum-1, 20*256);
                    
                    switch(department){
	                    case "1":
	                    	HSSFCell pnumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	                    	pnumTitle.setCellValue("病房病人数");
	                    	pnumTitle.setCellStyle(top2Style);
	                    	
	                    	HSSFCell aenumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	                    	aenumTitle.setCellValue("病房内AECOPD人数");
	                    	aenumTitle.setCellStyle(top2Style);
	                    	sheet.setColumnWidth(columnNum-1, 15*256);
	                    	break;
	                    case "3":
	                    	HSSFCell chenumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	                    	chenumTitle.setCellValue("病房病人数");
	                    	chenumTitle.setCellStyle(top2Style);
	                    	sheet.setColumnWidth(columnNum-1, 10*256);
	                    	
	                    	HSSFCell risknumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	                    	risknumTitle.setCellValue("病房内合并COPD或哮喘的手术病人数");
	                    	risknumTitle.setCellStyle(top2Style);
	                    	sheet.setColumnWidth(columnNum-1, 20*256);
	                    	break;
	                    case "2":
	                    	HSSFCell pednumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	                    	pednumTitle.setCellValue("门诊人次");
	                    	pednumTitle.setCellStyle(top2Style);
	                    	break;
                    	default:
	                    		break;
                    }
                    
                    HSSFCell lsnumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    lsnumTitle.setCellValue("雾化令舒人数");
                    lsnumTitle.setCellStyle(top2Style);
                    sheet.setColumnWidth(columnNum-1, 10*256);
                    
                    HSSFCell averageDoseTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
                    averageDoseTitle.setCellValue("平均计量");
                    averageDoseTitle.setCellStyle(top2Style);
                    
                    String beginDuraion = DateUtils.getMonthInRateBeginDuration(inRateDate);
                    String endDuraion = DateUtils.getMonthInRateEndDuration(inRateDate);
                    
                    logger.info(String.format("begin to get monthly inRate during %s and %s", beginDuraion,endDuraion));
                    List<MonthlyStatisticsData> monthlyStatistics = new ArrayList<MonthlyStatisticsData>();
                    Map<String,MonthlyStatisticsData> coreMonthlyStatistics = new HashMap<String,MonthlyStatisticsData>();
                    Map<String,MonthlyStatisticsData> emergingMonthlyStatistics = new HashMap<String,MonthlyStatisticsData>();
                    MonthlyStatisticsData monthlyCountryStatistics = new MonthlyStatisticsData();
                    MonthlyStatisticsData coreMonthlyCountryStatistics = new MonthlyStatisticsData();
                    MonthlyStatisticsData emergingMonthlyCountryStatistics = new MonthlyStatisticsData();
                    
                    switch(department){
	                    case "1":
	                    	//呼吸科
	                    	monthlyStatistics = respirologyService.getMonthlyStatisticsData(beginDuraion,endDuraion,level);
	                    	coreMonthlyStatistics = respirologyService.getCoreOrEmergingMonthlyStatisticsData(beginDuraion,endDuraion,level,"Core");
	                    	emergingMonthlyStatistics = respirologyService.getCoreOrEmergingMonthlyStatisticsData(beginDuraion,endDuraion,level,"Emerging");
	                    	
	                    	monthlyCountryStatistics = respirologyService.getMonthlyStatisticsCountryData(beginDuraion, endDuraion);
	                    	coreMonthlyCountryStatistics = respirologyService.getCoreOrEmergingMonthlyStatisticsCountryData(beginDuraion, endDuraion,"Core");
	                    	emergingMonthlyCountryStatistics = respirologyService.getCoreOrEmergingMonthlyStatisticsCountryData(beginDuraion, endDuraion,"Emerging");
	                    	break;
	                    case "2":
	                    	//儿科
	                    	coreMonthlyStatistics = pediatricsService.getCoreOrEmergingMonthlyStatisticsData(beginDuraion,endDuraion,level,"Core");
	                    	emergingMonthlyStatistics = pediatricsService.getCoreOrEmergingMonthlyStatisticsData(beginDuraion,endDuraion,level,"Emerging");
	                    	monthlyStatistics = pediatricsService.getMonthlyStatisticsData(beginDuraion,endDuraion,level);
	                    	
	                    	monthlyCountryStatistics = pediatricsService.getMonthlyStatisticsCountryData(beginDuraion, endDuraion);
	                    	coreMonthlyCountryStatistics = pediatricsService.getCoreOrEmergingMonthlyStatisticsCountryData(beginDuraion, endDuraion,"Core");
	                    	emergingMonthlyCountryStatistics = pediatricsService.getCoreOrEmergingMonthlyStatisticsCountryData(beginDuraion, endDuraion,"Emerging");
	                    	break;
	                    case "3":
	                    	//胸外科
	                    	coreMonthlyStatistics = chestSurgeryService.getCoreOrEmergingMonthlyStatisticsData(beginDuraion,endDuraion,level,"Core");
	                    	emergingMonthlyStatistics = chestSurgeryService.getCoreOrEmergingMonthlyStatisticsData(beginDuraion,endDuraion,level,"Emerging");
	                    	monthlyStatistics = chestSurgeryService.getMonthlyStatisticsData(beginDuraion,endDuraion,level);
	                    	
	                    	monthlyCountryStatistics = chestSurgeryService.getMonthlyStatisticsCountryData(beginDuraion, endDuraion);
	                    	coreMonthlyCountryStatistics = chestSurgeryService.getCoreOrEmergingMonthlyStatisticsCountryData(beginDuraion, endDuraion,"Core");
	                    	emergingMonthlyCountryStatistics = chestSurgeryService.getCoreOrEmergingMonthlyStatisticsCountryData(beginDuraion, endDuraion,"Emerging");
	                    	break;
                    }
                    logger.info("get monthly statistics data end...");
                    
                    
                    monthlyStatistics.add(monthlyCountryStatistics);
                    
                    for( MonthlyStatisticsData sData : monthlyStatistics){
                    	
                    	row = sheet.createRow(currentRowNum++);
                    	int column = 0;
                    	String name = "";
                    	switch(level){
	                    	case LsAttributes.USER_LEVEL_RSD:
	                    		name = sData.getRsd();
	                    		row.createCell(column++, XSSFCell.CELL_TYPE_STRING).setCellValue(name==null||"".equalsIgnoreCase(name)?"全国":name);
	                    		break;
	                    	case LsAttributes.USER_LEVEL_RSM:
	                    		name = sData.getRsm();
	                    		row.createCell(column++, XSSFCell.CELL_TYPE_STRING).setCellValue(name==null||"".equalsIgnoreCase(name)?"全国":name);
	                    		break;
	                    	case LsAttributes.USER_LEVEL_DSM:
	                    		name = sData.getRsd()+sData.getRsm()+sData.getDsmCode();
	                    		row.createCell(column++, XSSFCell.CELL_TYPE_STRING).setCellValue(name==null||"".equalsIgnoreCase(name)?"全国":sData.getDsmName());
	                    		break;
                    	}
                    	
                    	if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(level) ){
                    		row.createCell(column++, XSSFCell.CELL_TYPE_STRING).setCellValue(sData.getRsm());
                    	}
                    	
                    	HSSFCell inRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
                    	inRateCell.setCellValue(sData.getInRate());
                    	inRateCell.setCellStyle(percentCellStyle);
                    	
                    	HSSFCell coreRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
                    	if( null != coreMonthlyStatistics && null != coreMonthlyStatistics.get(name) ){
                    		coreRateCell.setCellValue(coreMonthlyStatistics.get(name).getCoreInRate());
                    	}else if(null==name||"".equalsIgnoreCase(name)){
                    		coreRateCell.setCellValue(coreMonthlyCountryStatistics.getCoreInRate());
                    	}else{
                    		coreRateCell.setCellValue(0);
                    	}
                    	coreRateCell.setCellStyle(percentCellStyle);
                    	
                    	HSSFCell emergingRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
                    	if( null != emergingMonthlyStatistics && null != emergingMonthlyStatistics.get(name) ){
                    		emergingRateCell.setCellValue(emergingMonthlyStatistics.get(name).getEmergingInRate());
                    	}else if(null==name||"".equalsIgnoreCase(name)){
                    		emergingRateCell.setCellValue(emergingMonthlyCountryStatistics.getEmergingInRate());
                    	}else{
                    		emergingRateCell.setCellValue(0);
                    	}
                    	emergingRateCell.setCellStyle(percentCellStyle);
                    	
                    	HSSFCell whRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
                    	whRateCell.setCellValue(sData.getWhRate());
                    	whRateCell.setCellStyle(percentCellStyle);
                    	
                    	HSSFCell coreWhRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
                    	if( null != coreMonthlyStatistics && null != coreMonthlyStatistics.get(name) ){
                    		coreWhRateCell.setCellValue(coreMonthlyStatistics.get(name).getCoreWhRate());
                    	}else if(null==name||"".equalsIgnoreCase(name)){
                    		coreWhRateCell.setCellValue(coreMonthlyCountryStatistics.getCoreWhRate());
                    	}else{
                    		coreWhRateCell.setCellValue(0);
                    	}
                    	coreWhRateCell.setCellStyle(percentCellStyle);
                    	
                    	HSSFCell emergingwhRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
                    	if( null != emergingMonthlyStatistics && null != emergingMonthlyStatistics.get(name) ){
                    		emergingwhRateCell.setCellValue(emergingMonthlyStatistics.get(name).getEmergingWhRate());
                    	}else if(null==name||"".equalsIgnoreCase(name)){
                    		emergingwhRateCell.setCellValue(emergingMonthlyCountryStatistics.getEmergingWhRate());
                    	}else{
                    		emergingwhRateCell.setCellValue(0);
                    	}
                    	emergingwhRateCell.setCellStyle(percentCellStyle);
                    	
                    	HSSFCell pnumCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
                    	pnumCell.setCellValue(sData.getPnum());
                    	pnumCell.setCellStyle(numberCellStyle);
                    	
                    	switch(department){
		                    case "1":
		                    	HSSFCell aenumCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
		                    	aenumCell.setCellValue(sData.getAenum());
		                    	aenumCell.setCellStyle(numberCellStyle);
		                    	break;
		                    case "3":
		                    	HSSFCell risknumCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
		                    	risknumCell.setCellValue(sData.getRisknum());
		                    	risknumCell.setCellStyle(numberCellStyle);
		                    	break;
	                    	default:
	                    		break;
	                    }
                        
                        HSSFCell lsnumCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
                        lsnumCell.setCellValue(sData.getLsnum());
                        lsnumCell.setCellStyle(numberCellStyle);
                        
                        HSSFCell averageDoseCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
                        averageDoseCell.setCellValue(sData.getAverageDose());
                        averageDoseCell.setCellStyle(averageDoseCellStyle);
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
                
                HSSFCellStyle top1Style=workbook.createCellStyle();
                top1Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                top1Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                top1Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                top1Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                top1Style.setLeftBorderColor(HSSFColor.BLACK.index);
                top1Style.setRightBorderColor(HSSFColor.BLACK.index);
                top1Style.setWrapText(true);
                
                HSSFCellStyle rsmTitleStyle=workbook.createCellStyle();
                rsmTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                rsmTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                rsmTitleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                rsmTitleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                rsmTitleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                rsmTitleStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle rsmTitleBorderStyle=workbook.createCellStyle();
                rsmTitleBorderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                rsmTitleBorderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                rsmTitleBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                rsmTitleBorderStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle rsmValueStyle=workbook.createCellStyle();
                rsmValueStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                rsmValueStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                rsmValueStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                rsmValueStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                rsmValueStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                rsmValueStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle rsmValueBorderStyle=workbook.createCellStyle();
                rsmValueBorderStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
                rsmValueBorderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                rsmValueBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                rsmValueBorderStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle top2Style=workbook.createCellStyle();
                top2Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                top2Style.setBorderRight(HSSFCellStyle.BORDER_THICK);
                top2Style.setRightBorderColor(HSSFColor.BLACK.index);
                top2Style.setWrapText(true);
                
                HSSFCellStyle numberCellStyle = workbook.createCellStyle();
                numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                numberCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                numberCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                numberCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                numberCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                numberCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                numberCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle numberCellRightBorderStyle = workbook.createCellStyle();
                numberCellRightBorderStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                numberCellRightBorderStyle.setRightBorderColor(HSSFColor.BLACK.index);
                numberCellRightBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                numberCellRightBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                numberCellRightBorderStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle percentCellStyle = workbook.createCellStyle();
                percentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
                percentCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                percentCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                percentCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                percentCellStyle.setRightBorderColor(HSSFColor.BLACK.index);

                HSSFCellStyle percentCellRightBorderStyle = workbook.createCellStyle();
                percentCellRightBorderStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
                percentCellRightBorderStyle.setRightBorderColor(HSSFColor.BLACK.index);
                percentCellRightBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                percentCellRightBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                percentCellRightBorderStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle averageDoseCellStyle = workbook.createCellStyle();
                averageDoseCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                averageDoseCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                averageDoseCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                averageDoseCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                averageDoseCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle averageDoseRightCellStyle = workbook.createCellStyle();
                averageDoseRightCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                averageDoseRightCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                averageDoseRightCellStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                averageDoseRightCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                averageDoseRightCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                
                workbook.createSheet("上周呼吸科KPI医院原始数据");
                HSSFSheet sheet = workbook.getSheetAt(0);
                int currentRowNum = 0;
                Date lastWeekEndDate = DateUtils.getGenerateWeeklyReportDate();
                Date lastWeekStartDate = new Date(lastWeekEndDate.getTime() - 6*24*60*60*1000);
                
                List<RespirologyData> dbResData = respirologyService.getRespirologyDataByDate(lastWeekStartDate,lastWeekEndDate);
                
                SimpleDateFormat exportdateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                
                //build the header
                HSSFRow row = sheet.createRow(currentRowNum++);
                
                row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue("编号");
                row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("录入日期");
                row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("医院编号");
                row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("医院名称");
                row.createCell(4, XSSFCell.CELL_TYPE_STRING).setCellValue("当日目标科室病房病人数");
                row.createCell(5, XSSFCell.CELL_TYPE_STRING).setCellValue("当日病房内AECOPD人数");
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
                row.createCell(20, XSSFCell.CELL_TYPE_STRING).setCellValue("是否为KPI医院（在=1，不在=0）");
                
                for( RespirologyData resData : dbResData ){
                    if( "1".equalsIgnoreCase(resData.getIsResAssessed()) ){
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
                        row.createCell(20, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getIsResAssessed());
                    }
                }
                
                workbook.createSheet("分析总表");
                sheet = workbook.getSheetAt(1);
                currentRowNum = 0;
                
                //build the header
                row = sheet.createRow(currentRowNum++);
                
                List<RespirologyExportData> resExportData = respirologyService.getResMonthExportData();
                
                if( null != resExportData && resExportData.size() > 0 ){
                	
                	int columnCount = 0;
                	int i = 0;
                	
                	RespirologyExportData resData = resExportData.get(0);
                	
                	Map<String, Double> pNumMap = resData.getpNumMap();
                	Iterator<String> pNumIte = pNumMap.keySet().iterator();
                	
                	Map<String, Double> lsNumMap = resData.getLsNumMap();
                	Iterator<String> lsNumIte = lsNumMap.keySet().iterator();
                	
                	Map<String, Double> aeNumMap = resData.getAeNumMap();
                	Iterator<String> aeNumIte = aeNumMap.keySet().iterator();
                	
                	Map<String, Double> inRateMap = resData.getInRateMap();
                	Iterator<String> inRateIte = inRateMap.keySet().iterator();
                	
                	Map<String, Double> whRateMap = resData.getWhRateMap();
                	Iterator<String> whRateIte = whRateMap.keySet().iterator();
                	
                	Map<String, Double> averageDoseMap = resData.getAverageDoseMap();
                	Iterator<String> averageDoseIte = averageDoseMap.keySet().iterator();
                	
                	Map<String, Double> whDaysMap = resData.getWhDaysMap();
                	Iterator<String> whDaysIte = whDaysMap.keySet().iterator();
                	
                	Map<String, Double> dValueMap = resData.getdValueMap();
                	Iterator<String> dValueIte = dValueMap.keySet().iterator();
                	
                	HSSFCell rsmRegionTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                	rsmRegionTitleCell.setCellValue("区域");
                	rsmRegionTitleCell.setCellStyle(rsmTitleStyle);
                	
                	HSSFCell rsmNameTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                	rsmNameTitleCell.setCellValue("RSM");
                	rsmNameTitleCell.setCellStyle(rsmTitleBorderStyle);
                	
                	HSSFCell hosNumTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                	hosNumTitleCell.setCellValue("医院家数");
                	hosNumTitleCell.setCellStyle(rsmTitleBorderStyle);
                	
                	HSSFCell salesNumTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                	salesNumTitleCell.setCellValue("代表数");
                	salesNumTitleCell.setCellStyle(rsmTitleBorderStyle);
                	
                	i = 0;
                    inRateIte = inRateMap.keySet().iterator();
                    while( inRateIte.hasNext() ){
                        String monthName = inRateIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue(monthName+"上报率");
                        if( i == inRateMap.size()-1 ){
                            titleCell.setCellStyle(top2Style);
                        }else{
                            titleCell.setCellStyle(top1Style);
                        }
                        i++;
                    }
                    columnCount += inRateMap.size();
                	
                	i = 0;
                	pNumIte = pNumMap.keySet().iterator();
                	while( pNumIte.hasNext() ){
                	    String monthName = pNumIte.next();
                	    HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                	    titleCell.setCellValue(new HSSFRichTextString(monthName+"周平均\r\n呼吸科住院\r\n人数"));
                	    if( i == pNumMap.size()-1 ){
                	    	titleCell.setCellStyle(top2Style);
                	    }else{
                	    	titleCell.setCellStyle(top1Style);
                	    }
                	    i++;
                	}
                	columnCount += pNumMap.size();
                	
                	i = 0;
                	aeNumIte = aeNumMap.keySet().iterator();
                	while( aeNumIte.hasNext() ){
                	    String monthName = aeNumIte.next();
                	    HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                	    titleCell.setCellValue(new HSSFRichTextString(monthName+"周平均\r\nAECOPD\r\n人数"));
                	    if( i == aeNumMap.size()-1 ){
                	    	titleCell.setCellStyle(top2Style);
                	    }else{
                	    	titleCell.setCellStyle(top1Style);
                	    }
                	    i++;
                	}
                	columnCount += aeNumMap.size();
                	
                	i = 0;
                    lsNumIte = lsNumMap.keySet().iterator();
                    while( lsNumIte.hasNext() ){
                        String monthName = lsNumIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue(new HSSFRichTextString(monthName+"周平均\r\n呼吸科雾化令舒\r\n人数"));
                        if( i == lsNumMap.size()-1 ){
                            titleCell.setCellStyle(top2Style);
                        }else{
                            titleCell.setCellStyle(top1Style);
                        }
                        i++;
                    }
                    columnCount += lsNumMap.size();
                    
                	i = 0;
                	whRateIte = whRateMap.keySet().iterator();
                	while( whRateIte.hasNext() ){
                		String monthName = whRateIte.next();
                		HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                		titleCell.setCellValue(monthName+"雾化率");
                		if( i == whRateMap.size()-1 ){
                	    	titleCell.setCellStyle(top2Style);
                	    }else{
                	    	titleCell.setCellStyle(top1Style);
                	    }
                		i++;
                	}
                	columnCount += whRateMap.size();
                	
                	i = 0;
                	averageDoseIte = averageDoseMap.keySet().iterator();
                	while( averageDoseIte.hasNext() ){
                	    String monthName = averageDoseIte.next();
                	    HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                	    titleCell.setCellValue(monthName+"平均剂量");
                	    if( i == averageDoseMap.size()-1 ){
                	    	titleCell.setCellStyle(top2Style);
                	    }else{
                	    	titleCell.setCellStyle(top1Style);
                	    }
                	    i++;
                	}
                	columnCount += averageDoseMap.size();
                	
                	if( null != whDaysMap && whDaysMap.size() > 0 ){
                	    i = 0;
                	    whDaysIte = whDaysMap.keySet().iterator();
                	    while( whDaysIte.hasNext() ){
                	        String monthName = whDaysIte.next();
                	        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                	        titleCell.setCellValue(monthName+"天数");
                	        if( i == whDaysMap.size()-1 ){
                	            titleCell.setCellStyle(top2Style);
                	        }else{
                	            titleCell.setCellStyle(top1Style);
                	        }
                	        i++;
                	    }
                	    columnCount += whDaysMap.size();
                	}
                	
                	i = 0;
                	dValueIte = dValueMap.keySet().iterator();
                    while( dValueIte.hasNext() ){
                        String monthName = dValueIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue(monthName+"雾化令舒人数-AE人数");
                        if( i == dValueMap.size()-1 ){
                            titleCell.setCellStyle(top2Style);
                        }else{
                            titleCell.setCellStyle(top1Style);
                        }
                        i++;
                    }
                    columnCount += dValueMap.size();
                	
                	
                	for( int columnNum = 1; columnNum < columnCount; columnNum++ ){
                		sheet.setColumnWidth(columnNum, 20*256);
                	}
                	
                	for( RespirologyExportData res : resExportData ){
                		
                		row = sheet.createRow(currentRowNum++);
                    	columnCount = 0;
                    	
                    	HSSFCell rsmRegionValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    	rsmRegionValueCell.setCellValue(res.getRsmRegion());
                    	rsmRegionValueCell.setCellStyle(rsmValueStyle);
                    	
                    	HSSFCell rsmNameValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    	rsmNameValueCell.setCellValue(res.getRsmName());
                    	rsmNameValueCell.setCellStyle(rsmValueBorderStyle);
                    	
                    	HSSFCell hosNumValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
                    	hosNumValueCell.setCellValue(res.getHosNum());
                    	hosNumValueCell.setCellStyle(numberCellRightBorderStyle);
                    	
                    	HSSFCell salesNumValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
                    	salesNumValueCell.setCellValue(res.getSalesNum());
                    	salesNumValueCell.setCellStyle(numberCellRightBorderStyle);
                    	
                    	pNumMap = res.getpNumMap();
                    	pNumIte = pNumMap.keySet().iterator();
                    	
                    	lsNumMap = res.getLsNumMap();
                    	lsNumIte = lsNumMap.keySet().iterator();
                    	
                    	aeNumMap = res.getAeNumMap();
                    	aeNumIte = aeNumMap.keySet().iterator();
                    	
                    	inRateMap = res.getInRateMap();
                    	inRateIte = inRateMap.keySet().iterator();
                    	
                    	whRateMap = res.getWhRateMap();
                    	whRateIte = whRateMap.keySet().iterator();
                    	
                    	averageDoseMap = res.getAverageDoseMap();
                    	averageDoseIte = averageDoseMap.keySet().iterator();
                    	
                    	whDaysMap = res.getWhDaysMap();
                    	whDaysIte = whDaysMap.keySet().iterator();
                    	
                    	dValueMap = res.getdValueMap();
                    	dValueIte = dValueMap.keySet().iterator();
                    	
                    	i = 0;
                    	String columnName;
                        while( inRateIte.hasNext() ){
                            columnName = inRateIte.next();
                            HSSFCell inRateValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                            inRateValueCell.setCellValue(inRateMap.get(columnName));
                            if( i == inRateMap.size()-1 ){
                                inRateValueCell.setCellStyle(percentCellRightBorderStyle);
                            }else{
                                inRateValueCell.setCellStyle(percentCellStyle);
                            }
                            i++;
                        }
                        columnCount += inRateMap.size();
                    	
                    	i = 0;
                    	while( pNumIte.hasNext() ){
                    	    columnName = pNumIte.next();
                    	    HSSFCell pNumValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                    	    pNumValueCell.setCellValue(pNumMap.get(columnName));
                    	    if( i == pNumMap.size()-1 ){
                    	    	pNumValueCell.setCellStyle(numberCellRightBorderStyle);
                    	    }else{
                    	    	pNumValueCell.setCellStyle(numberCellStyle);
                    	    }
                    	    i++;
                    	}
                    	columnCount += pNumMap.size();
                    	
                    	i = 0;
                    	while( aeNumIte.hasNext() ){
                    	    columnName = aeNumIte.next();
                    	    HSSFCell aeNumValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                    	    aeNumValueCell.setCellValue(aeNumMap.get(columnName));
                    	    if( i == aeNumMap.size()-1 ){
                    	    	aeNumValueCell.setCellStyle(numberCellRightBorderStyle);
                    	    }else{
                    	    	aeNumValueCell.setCellStyle(numberCellStyle);
                    	    }
                    	    i++;
                    	}
                    	columnCount += aeNumMap.size();
                    	
                    	i = 0;
                        while( lsNumIte.hasNext() ){
                            columnName = lsNumIte.next();
                            HSSFCell lsNumValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                            lsNumValueCell.setCellValue(lsNumMap.get(columnName));
                            
                            if( lsNumMap.size() > 3 ){
                                if( i == lsNumMap.size()-1 ){
                                    lsNumValueCell.setCellStyle(percentCellRightBorderStyle);
                                }else if( i == lsNumMap.size()-2 ){
                                    lsNumValueCell.setCellStyle(percentCellStyle);
                                }else{
                                    lsNumValueCell.setCellStyle(numberCellStyle);
                                }
                            }else if( lsNumMap.size() == 3 ){
                                if( i == 2 ){
                                    lsNumValueCell.setCellStyle(percentCellRightBorderStyle);
                                }else if( i == 1 ){
                                    lsNumValueCell.setCellStyle(numberCellStyle);
                                }else{
                                    lsNumValueCell.setCellStyle(numberCellStyle);
                                }
                            }else{
                                lsNumValueCell.setCellStyle(numberCellRightBorderStyle);
                            }
                            
                            i++;
                        }
                        columnCount += lsNumMap.size();
                        
                    	i = 0;
                    	while( whRateIte.hasNext() ){
                    		columnName = whRateIte.next();
                    		HSSFCell whRateValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                    		whRateValueCell.setCellValue(whRateMap.get(columnName));
                    		if( i == whRateMap.size()-1 ){
                    			whRateValueCell.setCellStyle(percentCellRightBorderStyle);
                    	    }else{
                    	    	whRateValueCell.setCellStyle(percentCellStyle);
                    	    }
                    		i++;
                    	}
                    	columnCount += whRateMap.size();
                    	
                    	i = 0;
                    	while( averageDoseIte.hasNext() ){
                    	    columnName = averageDoseIte.next();
                    	    HSSFCell averageDoseValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                    	    averageDoseValueCell.setCellValue(averageDoseMap.get(columnName));
                    	    if( i == averageDoseMap.size()-1 ){
                    	    	averageDoseValueCell.setCellStyle(averageDoseRightCellStyle);
                    	    }else{
                    	    	averageDoseValueCell.setCellStyle(averageDoseCellStyle);
                    	    }
                    	    i++;
                    	}
                    	columnCount += averageDoseMap.size();
                    	
                    	if( null != whDaysMap && whDaysMap.size() > 0 ){
                    	    i = 0;
                    	    while( whDaysIte.hasNext() ){
                    	        columnName = whDaysIte.next();
                    	        HSSFCell daysValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                    	        daysValueCell.setCellValue(whDaysMap.get(columnName));
                    	        if( i == whDaysMap.size()-1 ){
                    	            daysValueCell.setCellStyle(averageDoseRightCellStyle);
                    	        }else{
                    	            daysValueCell.setCellStyle(averageDoseCellStyle);
                    	        }
                    	        i++;
                    	    }
                    	    columnCount += whDaysMap.size();
                    	}
                    	
                    	i = 0;
                    	dValueIte = dValueMap.keySet().iterator();
                        while( dValueIte.hasNext() ){
                            columnName = dValueIte.next();
                            HSSFCell dValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                            dValueCell.setCellValue(dValueMap.get(columnName));
                            if( dValueMap.size() > 3 ){
                                if( i == dValueMap.size()-1 ){
                                    dValueCell.setCellStyle(percentCellRightBorderStyle);
                                }else if( i == dValueMap.size()-2 ){
                                    dValueCell.setCellStyle(percentCellStyle);
                                }else{
                                    dValueCell.setCellStyle(numberCellStyle);
                                }
                            }else if( dValueMap.size() == 3 ){
                                if( i == 2 ){
                                    dValueCell.setCellStyle(percentCellRightBorderStyle);
                                }else if( i == 1 ){
                                    dValueCell.setCellStyle(numberCellStyle);
                                }else{
                                    dValueCell.setCellStyle(numberCellStyle);
                                }
                            }else{
                                dValueCell.setCellStyle(numberCellRightBorderStyle);
                            }
                            i++;
                        }
                        columnCount += dValueMap.size();

                	}
                }
                
                
                workbook.createSheet("令舒呼吸科周周报");
                sheet = workbook.getSheetAt(2);
                currentRowNum = 1;
                
                HSSFCellStyle month_week_top1Style=workbook.createCellStyle();
                month_week_top1Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                month_week_top1Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                month_week_top1Style.setBorderTop(HSSFCellStyle.BORDER_THICK);
                month_week_top1Style.setTopBorderColor(HSSFColor.BLACK.index);
                month_week_top1Style.setBorderRight(HSSFCellStyle.BORDER_THICK);
                month_week_top1Style.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_top1Style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                month_week_top1Style.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_top1Style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                month_week_top1Style.setLeftBorderColor(HSSFColor.BLACK.index);
                month_week_top1Style.setWrapText(true);
                
                HSSFCellStyle month_week_top2Style=workbook.createCellStyle();
                month_week_top2Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                month_week_top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                month_week_top2Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                month_week_top2Style.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_top2Style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                month_week_top2Style.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_top2Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                month_week_top2Style.setLeftBorderColor(HSSFColor.BLACK.index);
                month_week_top1Style.setWrapText(true);
                
                HSSFCellStyle month_week_top2LeftStyle=workbook.createCellStyle();
                month_week_top2LeftStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                month_week_top2LeftStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                month_week_top2LeftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                month_week_top2LeftStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_top2LeftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                month_week_top2LeftStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_top2LeftStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                month_week_top2LeftStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                month_week_top1Style.setWrapText(true);
                
                HSSFCellStyle month_week_top2RightStyle=workbook.createCellStyle();
                month_week_top2RightStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                month_week_top2RightStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                month_week_top2RightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                month_week_top2RightStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_top2RightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                month_week_top2RightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_top2RightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                month_week_top2RightStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                month_week_top1Style.setWrapText(true);
                
                HSSFCellStyle month_week_valueLeftStyle=workbook.createCellStyle();
                month_week_valueLeftStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                month_week_valueLeftStyle.setTopBorderColor(HSSFColor.BLACK.index);
                month_week_valueLeftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                month_week_valueLeftStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_valueLeftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                month_week_valueLeftStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_valueLeftStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                month_week_valueLeftStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle month_week_valueRightStyle=workbook.createCellStyle();
                month_week_valueRightStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                month_week_valueRightStyle.setTopBorderColor(HSSFColor.BLACK.index);
                month_week_valueRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                month_week_valueRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_valueRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                month_week_valueRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_valueRightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                month_week_valueRightStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle month_week_valueStyle=workbook.createCellStyle();
                month_week_valueStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                month_week_valueStyle.setTopBorderColor(HSSFColor.BLACK.index);
                month_week_valueStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                month_week_valueStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_valueStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                month_week_valueStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_valueStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                month_week_valueStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle month_week_valueBottomStyle=workbook.createCellStyle();
                month_week_valueBottomStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                month_week_valueBottomStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_valueBottomStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                month_week_valueBottomStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle month_week_valueBottomRightStyle=workbook.createCellStyle();
                month_week_valueBottomRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                month_week_valueBottomRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_valueBottomRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                month_week_valueBottomRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle month_week_valueBottomLeftStyle=workbook.createCellStyle();
                month_week_valueBottomLeftStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                month_week_valueBottomLeftStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_valueBottomLeftStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                month_week_valueBottomLeftStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                month_week_valueBottomLeftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                month_week_valueBottomLeftStyle.setRightBorderColor(HSSFColor.BLACK.index);
                
                HSSFCellStyle month_week_numberBottomStyle=workbook.createCellStyle();
                month_week_numberBottomStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                month_week_numberBottomStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_numberBottomStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                month_week_numberBottomStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_numberBottomStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                
                HSSFCellStyle month_week_numberBottomRightStyle=workbook.createCellStyle();
                month_week_numberBottomRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                month_week_numberBottomRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_numberBottomRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                month_week_numberBottomRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_numberBottomRightStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                
                HSSFCellStyle month_week_percentBottomStyle=workbook.createCellStyle();
                month_week_percentBottomStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                month_week_percentBottomStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_percentBottomStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                month_week_percentBottomStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_percentBottomStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
                
                HSSFCellStyle month_week_percentBottomRightStyle=workbook.createCellStyle();
                month_week_percentBottomRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                month_week_percentBottomRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_percentBottomRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                month_week_percentBottomRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_percentBottomRightStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
                
                HSSFCellStyle month_week_averageDoseBottomStyle=workbook.createCellStyle();
                month_week_averageDoseBottomStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                month_week_averageDoseBottomStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_averageDoseBottomStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                month_week_averageDoseBottomStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_averageDoseBottomStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                
                HSSFCellStyle month_week_averageDoseBottomRightStyle=workbook.createCellStyle();
                month_week_averageDoseBottomRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                month_week_averageDoseBottomRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                month_week_averageDoseBottomRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
                month_week_averageDoseBottomRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
                month_week_averageDoseBottomRightStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                
                //build the header
                row = sheet.createRow(currentRowNum++);
                
                if( null != resExportData && resExportData.size() > 0 ){
                    
                    int columnCount = 1;
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("呼吸科指标");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("");

                    sheet.addMergedRegion(new Region(1, (short)1, 1, (short)2));
                    row.getCell(1).setCellStyle(month_week_top1Style);
                    row.getCell(2).setCellStyle(month_week_top1Style);
                    
                    HSSFCell hosNumTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    hosNumTitleCell.setCellValue("医院家数");
                    hosNumTitleCell.setCellStyle(month_week_top1Style);
                    
                    HSSFCell salesNumTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    salesNumTitleCell.setCellValue("代表数");
                    salesNumTitleCell.setCellStyle(month_week_top1Style);
                    
                    RespirologyExportData resData = resExportData.get(0);
                    
                    Map<String, Double> lsNumMap = resData.getLsNumMap();
                    Iterator<String> lsNumIte = lsNumMap.keySet().iterator();
                    
                    Map<String, Double> whRateMap = resData.getWhRateMap();
                    Iterator<String> whRateIte = whRateMap.keySet().iterator();
                    
                    Map<String, Double> whDaysMap = resData.getWhDaysMap();
                    Iterator<String> whDaysIte = whDaysMap.keySet().iterator();
                    
                    Map<String, Double> inRateMap = resData.getInRateMap();
                    Iterator<String> inRateIte = inRateMap.keySet().iterator();
                    
                    Map<String, Double> currentWeekAENumMap = resData.getCurrentWeekAENum();
                    Iterator<String> currentWeekAENumIte = currentWeekAENumMap.keySet().iterator();
                    
                    Map<String, Double> currentWeekLsAERateMap = resData.getCurrentWeekLsAERate();
                    Iterator<String> currentWeekLsAERateIte = currentWeekLsAERateMap.keySet().iterator();
                    
                    
                    int i = 0;
                    int inRateStartCount = columnCount;
                    while( inRateIte.hasNext() ){
                        inRateIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue("1.上报率");
                        titleCell.setCellStyle(month_week_top1Style);
                        i++;
                    }
                    columnCount += inRateMap.size();
                    
                    sheet.addMergedRegion(new Region(1, (short)inRateStartCount, 1, (short)(columnCount-1)));
                    row.getCell(inRateStartCount).setCellStyle(month_week_top1Style);
                    
                    i = 0;
                    int lsNumStartCount = columnCount;
                    while( lsNumIte.hasNext() ){
                        lsNumIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue("2.雾化令舒人数(周平均)");
                        titleCell.setCellStyle(month_week_top1Style);
                        i++;
                    }
                    columnCount += lsNumMap.size();
                    
                    sheet.addMergedRegion(new Region(1, (short)lsNumStartCount, 1, (short)(columnCount-1)));
                    row.getCell(lsNumStartCount).setCellStyle(month_week_top1Style);
                    
                    i = 0;
                    int whRateStartCount = columnCount;
                    while( whRateIte.hasNext() ){
                        whRateIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue("3.雾化率");
                        titleCell.setCellStyle(month_week_top1Style);
                        i++;
                    }
                    columnCount += whRateMap.size();
                    
                    sheet.addMergedRegion(new Region(1, (short)whRateStartCount, 1, (short)(columnCount-1)));
                    row.getCell(whRateStartCount).setCellStyle(month_week_top1Style);
                    
                    int lsAERateStartCount = columnCount;
                    HSSFCell lsAERateTitleCell = row.createCell(columnCount, XSSFCell.CELL_TYPE_STRING);
                    lsAERateTitleCell.setCellValue("4.当周雾化令舒人数/AE人数");
                    lsAERateTitleCell.setCellStyle(month_week_top1Style);
                    columnCount++;
                    
                    HSSFCell lsAERateTitle2Cell = row.createCell(columnCount, XSSFCell.CELL_TYPE_STRING);
                    lsAERateTitle2Cell.setCellValue("4.当周雾化令舒人数/AE人数");
                    lsAERateTitle2Cell.setCellStyle(month_week_top1Style);
                    columnCount++;
                    
                    sheet.addMergedRegion(new Region(1, (short)lsAERateStartCount, 1, (short)(lsAERateStartCount+1)));
                    row.getCell(lsAERateStartCount).setCellStyle(month_week_top1Style);
                    
                    if( null != whDaysMap && whDaysMap.size() > 0 ){
                        i = 0;
                        int daysStartCount = columnCount;
                        Iterator<String> daysIte = whDaysMap.keySet().iterator();
                        while( daysIte.hasNext() ){
                            daysIte.next();
                            HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                            titleCell.setCellValue("5.雾化天数");
                            i++;
                        }
                        columnCount += whDaysMap.size();
                        
                        sheet.addMergedRegion(new Region(1, (short)daysStartCount, 1, (short)(columnCount-1)));
                        row.getCell(daysStartCount).setCellStyle(month_week_top1Style);
                    }
                    
                    
                    row = sheet.createRow(currentRowNum++);
                    columnCount = 1;
                    
                    HSSFCell rsmRegionTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    rsmRegionTitleCell.setCellValue("区域");
                    rsmRegionTitleCell.setCellStyle(month_week_top2LeftStyle);
                    
                    HSSFCell rsmNameTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    rsmNameTitleCell.setCellValue("RSM");
                    rsmNameTitleCell.setCellStyle(month_week_top2RightStyle);
                    
                    HSSFCell hosNumTitle2Cell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    hosNumTitle2Cell.setCellValue("");
                    hosNumTitle2Cell.setCellStyle(month_week_top2RightStyle);
                    
                    HSSFCell salesNumTitle2Cell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                    salesNumTitle2Cell.setCellValue("");
                    salesNumTitle2Cell.setCellStyle(month_week_top2RightStyle);
                    
                    i = 0;
                    inRateIte = inRateMap.keySet().iterator();
                    while( inRateIte.hasNext() ){
                        String monthName = inRateIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue(monthName);
                        if( i == inRateMap.size()-1 ){
                            titleCell.setCellStyle(month_week_top2RightStyle);
                        }else{
                            titleCell.setCellStyle(month_week_top2Style);
                        }
                        i++;
                    }
                    columnCount += inRateMap.size();
                    
                    i = 0;
                    lsNumIte = lsNumMap.keySet().iterator();
                    while( lsNumIte.hasNext() ){
                        String monthName = lsNumIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue(monthName);
                        if( i == lsNumMap.size()-1 ){
                        	titleCell.setCellStyle(month_week_top2RightStyle);
                        }else{
                        	titleCell.setCellStyle(month_week_top2Style);
                        }
                        i++;
                    }
                    columnCount += lsNumMap.size();
                    
                    i = 0;
                    whRateIte = whRateMap.keySet().iterator();
                    while( whRateIte.hasNext() ){
                        String monthName = whRateIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue(monthName);
                        if( i == whRateMap.size()-1 ){
                        	titleCell.setCellStyle(month_week_top2RightStyle);
                        }else{
                        	titleCell.setCellStyle(month_week_top2Style);
                        }
                        i++;
                    }
                    columnCount += whRateMap.size();
                    
                    i = 0;
                    currentWeekAENumIte = currentWeekAENumMap.keySet().iterator();
                    while(currentWeekAENumIte.hasNext()){
                        String monthName = currentWeekAENumIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue(monthName);
                        titleCell.setCellStyle(month_week_top2Style);
                        i++;
                    }
                    columnCount += currentWeekAENumMap.size();
                    
                    i = 0;
                    currentWeekLsAERateIte = currentWeekLsAERateMap.keySet().iterator();
                    while(currentWeekLsAERateIte.hasNext()){
                        String monthName = currentWeekLsAERateIte.next();
                        HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                        titleCell.setCellValue(monthName);
                        titleCell.setCellStyle(month_week_top2RightStyle);
                        i++;
                    }
                    columnCount += currentWeekLsAERateMap.size();
                    
                    if( null != whDaysMap && whDaysMap.size() > 0 ){
                        i = 0;
                        whDaysIte = whDaysMap.keySet().iterator();
                        while( whDaysIte.hasNext() ){
                            String monthName = whDaysIte.next();
                            HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                            titleCell.setCellValue(monthName);
                            if( i == whDaysMap.size()-1 ){
                                titleCell.setCellStyle(month_week_top2RightStyle);
                            }else{
                                titleCell.setCellStyle(month_week_top2Style);
                            }
                            i++;
                        }
                        columnCount += whDaysMap.size();
                    }
                    
                    sheet.setColumnWidth(0, 2*256);
                    for( int columnNum = 1; columnNum < columnCount; columnNum++ ){
                		sheet.setColumnWidth(columnNum, 16*256);
                	}
                    
                    int resExportDataCount = 0;
                    
                    for( RespirologyExportData res : resExportData ){
                        
                        row = sheet.createRow(currentRowNum++);
                        columnCount = 1;
                        
                        HSSFCell rsmRegionValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                        rsmRegionValueCell.setCellValue(res.getRsmRegion());
                        if( resExportDataCount == resExportData.size()-1 ){
                            rsmRegionValueCell.setCellStyle(month_week_valueBottomLeftStyle);
                        }else{
                            rsmRegionValueCell.setCellStyle(month_week_valueLeftStyle);
                        }
                        
                        HSSFCell rsmNameValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
                        rsmNameValueCell.setCellValue(res.getRsmName());
                        if( resExportDataCount == resExportData.size()-1 ){
                            rsmNameValueCell.setCellStyle(month_week_valueBottomRightStyle);
                        }else{
                            rsmNameValueCell.setCellStyle(month_week_valueRightStyle);
                        }
                        
                        HSSFCell hosNumValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
                        hosNumValueCell.setCellValue(res.getHosNum());
                        if( resExportDataCount == resExportData.size()-1 ){
                            hosNumValueCell.setCellStyle(month_week_numberBottomRightStyle);
                        }else{
                            hosNumValueCell.setCellStyle(numberCellRightBorderStyle);
                        }
                        
                        HSSFCell salesNumValueCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
                        salesNumValueCell.setCellValue(res.getSalesNum());
                        if( resExportDataCount == resExportData.size()-1 ){
                            salesNumValueCell.setCellStyle(month_week_numberBottomRightStyle);
                        }else{
                            salesNumValueCell.setCellStyle(numberCellRightBorderStyle);
                        }
                        
                        lsNumMap = res.getLsNumMap();
                        lsNumIte = lsNumMap.keySet().iterator();
                        whRateMap = res.getWhRateMap();
                        whRateIte = whRateMap.keySet().iterator();
                        whDaysMap = res.getWhDaysMap();
                        whDaysIte = whDaysMap.keySet().iterator();
                        inRateMap = res.getInRateMap();
                        inRateIte = inRateMap.keySet().iterator();
                        
                        currentWeekAENumMap = res.getCurrentWeekAENum();
                        currentWeekAENumIte = currentWeekAENumMap.keySet().iterator();
                        
                        currentWeekLsAERateMap = res.getCurrentWeekLsAERate();
                        currentWeekLsAERateIte = currentWeekLsAERateMap.keySet().iterator();
                        
                        i = 0;
                        String columnName;
                        while( inRateIte.hasNext() ){
                            columnName = inRateIte.next();
                            HSSFCell inRateValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                            inRateValueCell.setCellValue(inRateMap.get(columnName));
                            if( i == inRateMap.size() - 1 ){
                                if( resExportDataCount == resExportData.size()-1 ){
                                    inRateValueCell.setCellStyle(month_week_percentBottomRightStyle);
                                }else{
                                    inRateValueCell.setCellStyle(percentCellRightBorderStyle);
                                }
                            }else{
                                if( resExportDataCount == resExportData.size()-1 ){
                                    inRateValueCell.setCellStyle(month_week_percentBottomStyle);
                                }else{
                                    inRateValueCell.setCellStyle(percentCellStyle);
                                }
                            }
                            i++;
                        }
                        columnCount += inRateMap.size();
                        
                        i = 0;
                        while( lsNumIte.hasNext() ){
                            columnName = lsNumIte.next();
                            HSSFCell lsNumValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                            lsNumValueCell.setCellValue(lsNumMap.get(columnName));
                            if( lsNumMap.size() > 3 ){
                                if( i == lsNumMap.size()-1 ){
                                    if( resExportDataCount == resExportData.size()-1 ){
                                        lsNumValueCell.setCellStyle(month_week_percentBottomRightStyle);
                                    }else{
                                        lsNumValueCell.setCellStyle(percentCellRightBorderStyle);
                                    }
                                }else if( i == lsNumMap.size()-2 ){
                                    if( resExportDataCount == resExportData.size()-1 ){
                                        lsNumValueCell.setCellStyle(month_week_percentBottomStyle);
                                    }else{
                                        lsNumValueCell.setCellStyle(percentCellStyle);
                                    }
                                }else{
                                    if( resExportDataCount == resExportData.size()-1 ){
                                        lsNumValueCell.setCellStyle(month_week_numberBottomStyle);
                                    }else{
                                        lsNumValueCell.setCellStyle(numberCellStyle);
                                    }
                                }
                            }else if( lsNumMap.size() == 3 ){
                                if( i == 2 ){
                                    if( resExportDataCount == resExportData.size()-1 ){
                                        lsNumValueCell.setCellStyle(month_week_percentBottomRightStyle);
                                    }else{
                                        lsNumValueCell.setCellStyle(percentCellRightBorderStyle);
                                    }
                                }else{
                                    if( resExportDataCount == resExportData.size()-1 ){
                                        lsNumValueCell.setCellStyle(month_week_numberBottomStyle);
                                    }else{
                                        lsNumValueCell.setCellStyle(numberCellStyle);
                                    }
                                }
                            }else{
                                if( resExportDataCount == resExportData.size()-1 ){
                                    lsNumValueCell.setCellStyle(month_week_numberBottomRightStyle);
                                }else{
                                    lsNumValueCell.setCellStyle(numberCellStyle);
                                }
                            }
                            i++;
                        }
                        columnCount += lsNumMap.size();
                        
                        i = 0;
                        while( whRateIte.hasNext() ){
                            columnName = whRateIte.next();
                            HSSFCell whRateValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                            whRateValueCell.setCellValue(whRateMap.get(columnName));
                            if( i == whRateMap.size() -1 ){
                                if( resExportDataCount == resExportData.size()-1 ){
                                    whRateValueCell.setCellStyle(month_week_percentBottomRightStyle);
                                }else{
                                    whRateValueCell.setCellStyle(percentCellRightBorderStyle);
                                }
                            }else{
                                if( resExportDataCount == resExportData.size()-1 ){
                                    whRateValueCell.setCellStyle(month_week_percentBottomStyle);
                                }else{
                                    whRateValueCell.setCellStyle(percentCellStyle);
                                }
                            }
                            i++;
                        }
                        columnCount += whRateMap.size();
                        
                        i = 0;
                        while( currentWeekAENumIte.hasNext() ){
                            columnName = currentWeekAENumIte.next();
                            HSSFCell currentWeekAENumValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                            currentWeekAENumValueCell.setCellValue(currentWeekAENumMap.get(columnName));
                            if( resExportDataCount == resExportData.size()-1 ){
                                currentWeekAENumValueCell.setCellStyle(month_week_numberBottomStyle);
                            }else{
                                currentWeekAENumValueCell.setCellStyle(numberCellStyle);
                            }
                            i++;
                        }
                        columnCount += currentWeekAENumMap.size();
                        
                        i = 0;
                        while( currentWeekLsAERateIte.hasNext() ){
                            columnName = currentWeekLsAERateIte.next();
                            HSSFCell currentWeekLsAERateValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                            currentWeekLsAERateValueCell.setCellValue(currentWeekLsAERateMap.get(columnName));
                            if( i == currentWeekLsAERateMap.size() -1 ){
                                if( resExportDataCount == resExportData.size()-1 ){
                                    currentWeekLsAERateValueCell.setCellStyle(month_week_percentBottomRightStyle);
                                }else{
                                    currentWeekLsAERateValueCell.setCellStyle(percentCellRightBorderStyle);
                                }
                            }else{
                                if( resExportDataCount == resExportData.size()-1 ){
                                    currentWeekLsAERateValueCell.setCellStyle(month_week_percentBottomStyle);
                                }else{
                                    currentWeekLsAERateValueCell.setCellStyle(percentCellStyle);
                                }
                            }
                            i++;
                        }
                        columnCount += currentWeekLsAERateMap.size();
                        
                        if( null != whDaysMap && whDaysMap.size() > 0 ){
                            i = 0;
                            while( whDaysIte.hasNext() ){
                                columnName = whDaysIte.next();
                                HSSFCell daysValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                                daysValueCell.setCellValue(whDaysMap.get(columnName));
                                if( i == whDaysMap.size() -1 ){
                                    if( resExportDataCount == resExportData.size()-1 ){
                                        daysValueCell.setCellStyle(month_week_averageDoseBottomRightStyle);
                                    }else{
                                        daysValueCell.setCellStyle(averageDoseRightCellStyle);
                                    }
                                }else{
                                    if( resExportDataCount == resExportData.size()-1 ){
                                        daysValueCell.setCellStyle(month_week_averageDoseBottomStyle);
                                    }else{
                                        daysValueCell.setCellStyle(averageDoseCellStyle);
                                    }
                                }
                                i++;
                            }
                            columnCount += whDaysMap.size();
                        }
                        
                        resExportDataCount++;
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
    
    private void populateWeeklyReportFile(StringBuffer remoteWeeklyReportFile, StringBuffer weeklyReportFile2Download, StringBuffer localWeeklyReportFile, Date chooseDate_d, String fileNamePre, String fileSubName, String directoryName){
        weeklyReportFile2Download.append(directoryName).append("/")
        .append(fileNamePre)
        .append(fileSubName)
        .append("-")
        .append(DateUtils.getWeeklyDuration(chooseDate_d))
        .append(".pdf");
        
        localWeeklyReportFile.append(directoryName).append("/")
        .append(fileNamePre)
        .append(fileSubName)
        .append("-")
        .append(directoryName)
        .append(".pdf");
        
        remoteWeeklyReportFile.append(directoryName).append("/")
        .append(fileNamePre)
        .append(fileSubName)
        .append("-")
        .append(DateUtils.getWeeklyDuration(chooseDate_d))
        .append(".pdf");
    }
    
    private void populateWeeklyReportAttachedFiles(List<String> filePaths, List<ReportFileObject> reportFiles, String localPath, String basePath, Date chooseDate_d
            , StringBuffer weeklyReportFile2Download, StringBuffer localWeeklyReportFile, StringBuffer remoteWeeklyReportFile
            , String department, String rsmRegion, String directoryName) throws Exception{
    	remoteWeeklyReportFile = new StringBuffer(basePath).append("weeklyReport2Download/");
        weeklyReportFile2Download = new StringBuffer(localPath).append("weeklyReport2Download/");
        localWeeklyReportFile = new StringBuffer(localPath).append("weeklyReport/");
        
        remoteWeeklyReportFile.append(directoryName).append("/")
        .append(department)
        .append("-BM-")
        .append(DateUtils.getWeeklyDuration(chooseDate_d))
        .append(rsmRegion)
        .append(".pdf");
        
        weeklyReportFile2Download.append(directoryName).append("/")
        .append(department)
        .append("-BM-")
        .append(DateUtils.getWeeklyDuration(chooseDate_d))
        .append(rsmRegion)
        .append(".pdf");
        
        localWeeklyReportFile.append(directoryName).append("/")
        .append(department)
        .append("-BM-")
        .append(directoryName)
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
