package com.chalet.lskpi.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
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
    
    /**
     * 医院KPI医院列表下载
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
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
    
    /**
     * 下载各科室原始数据
     * @param request request 
     * @param response response 
     * @return 下载文件
     * @throws IOException
     */
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
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("当日门急诊门诊人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("当日门急诊雾化人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("当日门急诊雾化令舒人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊雾化博雾人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊雾化端口数量");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表ETMSCode");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表姓名");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("所属DSM");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("所属Region");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("所属RSM Region");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊 0.5mg QD");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊 0.5mg BID");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊 1mg QD");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊 1mg BID");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊 2mg QD");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊 2mg BID"); 
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊雾化天数1天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊雾化天数2天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊雾化天数3天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊雾化天数4天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊雾化天数5天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊雾化天数6天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊雾化天数7天及以上占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊赠卖泵数量");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊带药人数");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊平均带药天数");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("门急诊总带药支数");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("当日住院门诊人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("当日住院雾化人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("当日住院雾化令舒人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化博雾人次");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院 0.5mg QD");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院 0.5mg BID");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院 1mg QD");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院 1mg BID");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院 2mg QD");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院 2mg BID"); 
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数1天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数2天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数3天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数4天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数5天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数6天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数7天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数8天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数9天占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院雾化天数10天及以上占比");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院赠卖泵数量");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院带药人数");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院平均带药天数");
                    row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING).setCellValue("住院总带药支数");   
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
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getWhbwnum());
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
                        
                        HSSFCell whdaysEmerging1RateCell  = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysEmerging1RateCell.setCellValue(pedData.getWhdaysEmerging1Rate()/100);
                        whdaysEmerging1RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysEmerging2RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysEmerging2RateCell.setCellValue(pedData.getWhdaysEmerging2Rate()/100);
                        whdaysEmerging2RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysEmerging3RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysEmerging3RateCell.setCellValue(pedData.getWhdaysEmerging3Rate()/100);
                        whdaysEmerging3RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysEmerging4RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysEmerging4RateCell.setCellValue(pedData.getWhdaysEmerging4Rate()/100);
                        whdaysEmerging4RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysEmerging5RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysEmerging5RateCell.setCellValue(pedData.getWhdaysEmerging5Rate()/100);
                        whdaysEmerging5RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysEmerging6RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysEmerging6RateCell.setCellValue(pedData.getWhdaysEmerging6Rate()/100);
                        whdaysEmerging6RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysEmerging7RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysEmerging7RateCell.setCellValue(pedData.getWhdaysEmerging7Rate()/100);
                        whdaysEmerging7RateCell.setCellStyle(percentCellStyle);
                        
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHomeWhEmergingNum1());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHomeWhEmergingNum2());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHomeWhEmergingNum3());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHomeWhEmergingNum4());
                        
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getPnum_room());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getWhnum_room());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getLsnum_room());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getWhbwnum_room());
                        
                        HSSFCell hqd_roomCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        hqd_roomCell.setCellValue(pedData.getHqd_room()/100);
                        hqd_roomCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell hbid_roomCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        hbid_roomCell.setCellValue(pedData.getHbid_room()/100);
                        hbid_roomCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell oqd_roomCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        oqd_roomCell.setCellValue(pedData.getOqd_room()/100);
                        oqd_roomCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell obid_roomCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        obid_roomCell.setCellValue(pedData.getObid_room()/100);
                        obid_roomCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell tqd_roomCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        tqd_roomCell.setCellValue(pedData.getTqd_room()/100);
                        tqd_roomCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell tbid_roomCell = row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        tbid_roomCell.setCellValue(pedData.getTbid_room()/100);
                        tbid_roomCell.setCellStyle(percentCellStyle);

                        HSSFCell whdaysRoom1RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom1RateCell.setCellValue(pedData.getWhdaysRoom1Rate()/100);
                        whdaysRoom1RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysRoom2RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom2RateCell.setCellValue(pedData.getWhdaysRoom2Rate()/100);
                        whdaysRoom2RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysRoom3RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom3RateCell.setCellValue(pedData.getWhdaysRoom3Rate()/100);
                        whdaysRoom3RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysRoom4RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom4RateCell.setCellValue(pedData.getWhdaysRoom4Rate()/100);
                        whdaysRoom4RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysRoom5RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom5RateCell.setCellValue(pedData.getWhdaysRoom5Rate()/100);
                        whdaysRoom5RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysRoom6RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom6RateCell.setCellValue(pedData.getWhdaysRoom6Rate()/100);
                        whdaysRoom6RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysRoom7RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom7RateCell.setCellValue(pedData.getWhdaysRoom7Rate()/100);
                        whdaysRoom7RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysRoom8RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom8RateCell.setCellValue(pedData.getWhdaysRoom8Rate()/100);
                        whdaysRoom8RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysRoom9RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom9RateCell.setCellValue(pedData.getWhdaysRoom9Rate()/100);
                        whdaysRoom9RateCell.setCellStyle(percentCellStyle);
                        
                        HSSFCell whdaysRoom10RateCell  =row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC);
                        whdaysRoom10RateCell.setCellValue(pedData.getWhdaysRoom10Rate()/100);
                        whdaysRoom10RateCell.setCellStyle(percentCellStyle);
                        
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHomeWhRoomNum1());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHomeWhRoomNum2());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHomeWhRoomNum3());
                        row.createCell(dataColumnNum++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(pedData.getHomeWhRoomNum4());
                        
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
                    int columnCount = 0;
                    
                    //build the header
                    HSSFRow row = sheet.createRow(currentRowNum++);
                    
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("编号");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("录入月份");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("医院编号");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("医院名称");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("儿科病房");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("儿科门急诊");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("家庭雾化");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("呼吸科病房");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("呼吸科门急诊");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("其他科室");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("销售代表姓名");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("所属DSM");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("所属RSM Region");
                    row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue("所属Region");
                    for( MonthlyData monthlyData : dbMonthlyData ){
                        row = sheet.createRow(currentRowNum++);
                        columnCount = 0;
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(currentRowNum-1);
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue(exportdateformat.format(monthlyData.getCreateDate()));
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getHospitalCode());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getHospitalName());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getPedRoomDrugStoreWh());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getPedEmerWh());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getHomeWh());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getResRoom());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getResClinic());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getOthernum());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getOperatorName());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getDsmName());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING).setCellValue(monthlyData.getRsmRegion());
                        row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(monthlyData.getRegion());
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
        	Date date = new Date();
    	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
    	    
            String telephone = (String)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR);
//            String hospitalShownFlag = (String)request.getParameter("hospitalShownFlag");
            
            String hospitalShownFlag = "all";
            if( "all".equalsIgnoreCase(hospitalShownFlag) ){
            	hospitalShownFlag = LsAttributes.SQL_WHERE_CONDITION_PED_COLLECTION_HOSPITAL.toString();
            }else{
            	hospitalShownFlag = LsAttributes.SQL_WHERE_CONDITION_DAILYREPORT_HOSPITAL.toString();
            }
            logger.info(String.format("daily PED report, the current user is %s, hospitalShownFlag is %s", telephone,hospitalShownFlag));
            
            String pedType = (String)request.getParameter("pedType");
            
            if( "e".equalsIgnoreCase(pedType) ){
            	view.setViewName("pedDailyReport");
            }else if("r".equalsIgnoreCase(pedType)){
            	view.setViewName("pedRoomDailyReport");
            }
            
            List<MobilePEDDailyData> mobilePEDData = pediatricsService.getDailyPEDData4Mobile(telephone,currentUser,hospitalShownFlag);
            logger.info("get daily ped data for mobile end...");
            if( !LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            	List<MobilePEDDailyData> mobilePEDChildData = pediatricsService.getDailyPEDChildData4Mobile(telephone,currentUser,hospitalShownFlag);
            	logger.info("get daily ped child data for mobile end...");
            	
            	view.addObject(LsAttributes.MOBILE_DAILY_REPORT_CHILD_DATA, mobilePEDChildData);
            }else{
                MobilePEDDailyData mpd = pediatricsService.getDailyPEDParentData4Mobile(telephone, currentUser.getLevel(),hospitalShownFlag);
                logger.info("get daily ped parent data for mobile end...");
                view.addObject(LsAttributes.MOBILE_DAILY_REPORT_PARENT_DATA, mpd);
                
                List<String> allRegionCenters = userService.getAllRegionName();
                List<List<MobilePEDDailyData>> allRSMMobilePEDData = new ArrayList<List<MobilePEDDailyData>>();
                for( String regionCenter : allRegionCenters ){
                    List<MobilePEDDailyData> mobilePEDRSMData = pediatricsService.getDailyPEDData4MobileByRegion(paramDate,regionCenter,hospitalShownFlag);
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
            	
                TopAndBottomRSMData rsmData = pediatricsService.getTopAndBottomRSMData(paramDate,hospitalShownFlag, pedType);
                view.addObject("rsmData", rsmData);
                logger.info("get the top and bottom rsm data end...");
            }
            
            populateDailyReportTitle(currentUser,view,LsAttributes.DAILYREPORTTITLE_3);
            logger.info("populate the title end");
        }catch(Exception e){
            logger.error("fail to get the daily ped report data",e);
            view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.RETURNED_MESSAGE_2);
        }
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
            	
            	Date date = new Date();
        	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
        	    
                TopAndBottomRSMData rsmData = respirologyService.getTopAndBottomRSMData(paramDate);
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
            	
            	Date date = new Date();
        	    Timestamp paramDate = new Timestamp(DateUtils.populateParamDate(date).getTime());
            	
                TopAndBottomRSMData rsmData = chestSurgeryService.getTopAndBottomRSMData(paramDate);
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
    
    @RequestMapping("/weeklypeddepartment")
    public ModelAndView weeklypeddepartment(HttpServletRequest request){
    	logger.info("weekly ped report department");
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	
    	logger.info(String.format("weekly report: current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
    	
    	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
    		view.setViewName("index");
    		return view;
    	}
    	
    	view.setViewName("weeklyPedReportDepartment");
    	return view;
    }
    
    /**
     * 家庭雾化周报(儿科门急诊，病房，医生)
     * @param request
     * @return
     */
    @RequestMapping("/homeReportDepartment")
    public ModelAndView homeReportDepartment(HttpServletRequest request){
    	logger.info("weekly home report department");
    	ModelAndView view = new LsKPIModelAndView(request);
    	String currentUserTel = verifyCurrentUser(request,view);
    	UserInfo currentUser = (UserInfo)request.getSession().getAttribute(LsAttributes.CURRENT_OPERATOR_OBJECT);
    	
    	logger.info(String.format("weekly report: current user's telephone is %s, the user in session is %s", currentUserTel,currentUser));
    	
    	if( null == currentUserTel || "".equalsIgnoreCase(currentUserTel) || null == currentUser ){
    		view.addObject(LsAttributes.JSP_VERIFY_MESSAGE, LsAttributes.NO_USER_FOUND_WEB);
    		view.setViewName("index");
    		return view;
    	}
    	
    	view.setViewName("weeklyHomeReportDepartment");
    	return view;
    }
    
    /**
     * 家庭雾化周报(医生)
     * @param request
     * @return
     */
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
                populateHomeWeeklyReportTitle4AllRSM(view,"");
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
    
    /**
     * 家庭雾化周报(儿科门急诊，病房)
     * @param request
     * @return
     */
    @RequestMapping("/pedHomeReport")
    public ModelAndView pedHomeReport(HttpServletRequest request){
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
        
        String pedType = request.getParameter("pedType");
        
        try{
            List<HomeWeeklyData> homeWeeklyDataList = homeService.getPedHomeWeeklyDataOfCurrentUser(currentUser,pedType);
            view.addObject("homeWeeklyDataList", homeWeeklyDataList);
            logger.info(String.format("end to get the home weekly data of user %s", currentUser.getTelephone()));
            
            List<HomeWeeklyData> lowerHomeWeeklyDataList = homeService.getPedHomeWeeklyDataOfLowerUser(currentUser, pedType);
            view.addObject("lowerHomeWeeklyDataList", lowerHomeWeeklyDataList);
            logger.info(String.format("end to get the lower home weekly data of user %s", currentUser.getTelephone()));
            
            HomeWeeklyData upperHomeWeeklyData = homeService.getPedHomeWeeklyDataOfUpperUser(currentUser, pedType);
            view.addObject("upperHomeWeeklyData", upperHomeWeeklyData);
            logger.info(String.format("end to get the upper home weekly data of user %s", currentUser.getTelephone()));
            
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            	List<String> allRegionCenters = userService.getAllRegionName();
                List<List<HomeWeeklyData>> allRSMData = new ArrayList<List<HomeWeeklyData>>();
                
                for( String regionCenter : allRegionCenters ){
                    List<HomeWeeklyData> rsmData = homeService.getPedHomeWeeklyDataByRegion(regionCenter, pedType);
                    logger.info(String.format("get weekly home data of %s RSM end...", regionCenter));
                    allRSMData.add(rsmData);
                }
                
                view.addObject("allRSMHomeWeeklyData", allRSMData);
                populateHomeWeeklyReportTitle4AllRSM(view, pedType);
            }
            
            view.addObject("currentUser", currentUser);
            String dsmName = "";
            if( LsAttributes.USER_LEVEL_REP.equalsIgnoreCase(currentUser.getLevel()) ){
                dsmName = userService.getUserInfoByUserCode(currentUser.getSuperior()).getName();
            }
            
            String homeWeeklyReportTitle = "";
            if( "emerging".equalsIgnoreCase(pedType) ){
            	homeWeeklyReportTitle = LsAttributes.HOMEWEEKLYREPORTTITLE_EMERGING+LsAttributes.HOMEWEEKLYREPORTTITLE;
            }else{
            	homeWeeklyReportTitle = LsAttributes.HOMEWEEKLYREPORTTITLE_ROOM+LsAttributes.HOMEWEEKLYREPORTTITLE;
            }
            populateHomeWeeklyReportTitle(currentUser, view, homeWeeklyReportTitle, dsmName);
            
            logger.info(String.format("begin to get the last 12 weeks report, current user is %s", currentUser.getTelephone()));
            //add the last 12 weekly report
            String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
            String localPath = request.getRealPath("/");
            StringBuffer localReportFile = new StringBuffer(localPath);
            StringBuffer remoteReportFile = new StringBuffer(basePath);
            
            String directory = BrowserUtils.getDirectory(request.getHeader("User-Agent"),"weeklyHTMLReport");
            String reportGenerateDate = DateUtils.getDirectoryNameOfLastDuration();
            
            //weeklyPEDEmergingHomeReport-"+userLevel+"-"+telephone+"-"+DateUtils.getDirectoryNameOfLastDuration()+".html";
            String reportName = "";
            if( "emerging".equalsIgnoreCase(pedType) ){
            	reportName = "weeklyPEDEmergingHomeReport-";
            }else{
            	reportName = "weeklyPEDRoomHomeReport-";
            }
            if( LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
                remoteReportFile.append(directory).append(reportGenerateDate).append("/")
                .append(reportName)
                .append(currentUser.getLevel())
                .append("-")
                .append(reportGenerateDate)
                .append(".html");
                
                localReportFile.append(directory).append(reportGenerateDate).append("/")
                .append(reportName)
                .append(currentUser.getLevel())
                .append("-")
                .append(reportGenerateDate)
                .append(".html");
            }else{
                remoteReportFile.append(directory).append(reportGenerateDate).append("/")
                .append(reportName)
                .append(currentUser.getLevel())
                .append("-")
                .append(currentUserTel)
                .append("-")
                .append(reportGenerateDate)
                .append(".html");
                
                localReportFile.append(directory).append(reportGenerateDate).append("/")
                .append(reportName)
                .append(currentUser.getLevel())
                .append("-")
                .append(currentUserTel)
                .append("-")
                .append(reportGenerateDate)
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
        
        view.setViewName("pedHomeCollectionReport");
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
        .append(currentUser.getLevel());
        
        if( !LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
        	remoteReportFile.append("-")
        	.append(currentUserTel);
        }
        
        remoteReportFile.append("-")
        .append(DateUtils.getLastMonth())
        .append(".html");
        
        localReportFile.append(directory).append(DateUtils.getLastMonth()).append("/")
            .append("monthlyReport-")
            .append(currentUser.getLevel());
        
        if( !LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
        	localReportFile.append("-")
        	.append(currentUserTel);
        }
        
        localReportFile.append("-")
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
            .append(currentUser.getLevel());
        	
        	if( !LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
        		remoteReportFile.append("-")
            	.append(currentUserTel);
            }
        	
        	remoteReportFile.append("-")
            .append(DateUtils.getLast2Month())
            .append(".html");
            
            localReportFile.append(directory).append(DateUtils.getLast2Month()).append("/")
                .append("monthlyReport-")
                .append(currentUser.getLevel());
            
            if( !LsAttributes.USER_LEVEL_BM.equalsIgnoreCase(currentUser.getLevel()) ){
            	localReportFile.append("-")
            	.append(currentUserTel);
            }
            
            localReportFile.append("-")
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
    
    /**
     * 周报数据查询
     * @param request 周报数据查询
     * @param response 周报数据查询
     * @return
     * @throws IOException IOException
     */
    @RequestMapping("/doDownloadWeeklyData")
    public String doDownloadWeeklyData(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	logger.info("download the weekly data..");
    	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		String localPath = request.getRealPath("/");
		StringBuffer remoteWeeklyReportFile = new StringBuffer(basePath).append("weeklyReport2Download/");
		StringBuffer weeklyReportFile2Download = new StringBuffer(localPath).append("weeklyReport2Download/");
		StringBuffer localWeeklyReportFile = new StringBuffer(localPath).append("weeklyReport/");
		
		StringBuffer remoteRoomWeeklyReportFile = new StringBuffer(basePath).append("weeklyReport2Download/");
		StringBuffer weeklyRoomReportFile2Download = new StringBuffer(localPath).append("weeklyReport2Download/");
		StringBuffer localRoomWeeklyReportFile = new StringBuffer(localPath).append("weeklyReport/");
		
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
				    	populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "儿科门急诊周报-DSM-", dsm.getName(),directoryName);
				    	populateWeeklyReportFile(remoteRoomWeeklyReportFile, weeklyRoomReportFile2Download, localRoomWeeklyReportFile, chooseDate_d, "儿科住院周报-DSM-", dsm.getName(),directoryName);
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
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "儿科门急诊周报-RSM-", selectedRSM,directoryName);
						populateWeeklyReportFile(remoteRoomWeeklyReportFile, weeklyRoomReportFile2Download, localRoomWeeklyReportFile, chooseDate_d, "儿科住院周报-RSM-", selectedRSM,directoryName);
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
						populateWeeklyReportFile(remoteWeeklyReportFile, weeklyReportFile2Download, localWeeklyReportFile, chooseDate_d, "儿科门急诊周报-RSD-", selectedRSD,directoryName);
						populateWeeklyReportFile(remoteRoomWeeklyReportFile, weeklyRoomReportFile2Download, localRoomWeeklyReportFile, chooseDate_d, "儿科住院周报-RSD-", selectedRSD,directoryName);
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
							populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath
									, chooseDate_d
									, weeklyReportFile2Download
									, localWeeklyReportFile
									, remoteWeeklyReportFile
									, "呼吸科周报", "",directoryName);
							break;
						case "2":
							populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath
									, chooseDate_d
									, weeklyReportFile2Download
									, localWeeklyReportFile
									, remoteWeeklyReportFile
									, "儿科门急诊周报", "",directoryName);
							populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath
									, chooseDate_d
									, weeklyReportFile2Download
									, localWeeklyReportFile
									, remoteWeeklyReportFile
									, "儿科住院周报", "",directoryName);
							break;
						case "3":
							populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath
									, chooseDate_d
									, weeklyReportFile2Download
									, localWeeklyReportFile
									, remoteWeeklyReportFile
									, "胸外科周报", "",directoryName);
							break;
						case "4":
							directoryName = DateUtils.getDirectoryNameOfCurrentDuration(new Date(chooseDate_d.getTime() + 7*24*60*60*1000));
							populateWeeklyReportAttachedFiles(filePaths,reportFiles, localPath, basePath
									, chooseDate_d
									, weeklyReportFile2Download
									, localWeeklyReportFile
									, remoteWeeklyReportFile
									, "家庭雾化周报", "",directoryName);
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
			
			File weeklyReportFile = new File(localWeeklyReportFile.toString());
	        File targetReportFile = new File(weeklyReportFile2Download.toString());
	        
	        File weeklyRoomReportFile = new File(localRoomWeeklyReportFile.toString());
	        File targetRoomReportFile = new File(weeklyRoomReportFile2Download.toString());
	        
	        if( !weeklyReportFile.exists() ){
	            logger.error("fail to generate the daily report to export, no file is found");
	            
	            request.getSession().setAttribute(LsAttributes.WEEKLY_PDF_REPORT_MESSAGE, LsAttributes.NO_WEEKLY_PDF_FOUND);
	            if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
	                return "redirect:showWebUploadData";
	            }else{
	                return "redirect:showUploadData";
	            }
	        }else{
	            FileUtils.copySourceFile2TargetFile(weeklyReportFile, targetReportFile);
	            
	            FileUtils.copySourceFile2TargetFile(weeklyRoomReportFile, targetRoomReportFile);
	            
	            ReportFileObject rfo = new ReportFileObject();
	            rfo.setFileName(remoteWeeklyReportFile.toString().substring(remoteWeeklyReportFile.toString().lastIndexOf("/")+1));
	            rfo.setFilePath(remoteWeeklyReportFile.toString());
	            
	            ReportFileObject roomRfo = new ReportFileObject();
	            roomRfo.setFileName(remoteRoomWeeklyReportFile.toString().substring(remoteRoomWeeklyReportFile.toString().lastIndexOf("/")+1));
	            roomRfo.setFilePath(remoteRoomWeeklyReportFile.toString());
	            
	            reportFiles.add(rfo);
	            reportFiles.add(roomRfo);
	            
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

    /**
     * 每月汇总数据统计
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
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
                    
                    HSSFCellStyle percentCellStyle = workbook.createCellStyle();
                    percentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
                    percentCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    percentCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    percentCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                    percentCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                    percentCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    percentCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    percentCellStyle.setTopBorderColor(HSSFColor.BLACK.index);
                    percentCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                    
                    HSSFCellStyle numberCellStyle = workbook.createCellStyle();
                    numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
                    numberCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    numberCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    numberCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                    numberCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                    numberCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    numberCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    numberCellStyle.setTopBorderColor(HSSFColor.BLACK.index);
                    numberCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                    
                    HSSFCellStyle averageDoseCellStyle = workbook.createCellStyle();
                    averageDoseCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
                    averageDoseCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    averageDoseCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                    averageDoseCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    averageDoseCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    averageDoseCellStyle.setTopBorderColor(HSSFColor.BLACK.index);
                    averageDoseCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                    
                    
                    HSSFCellStyle stringCellStyle = workbook.createCellStyle();
                    stringCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    stringCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                    stringCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    stringCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    stringCellStyle.setTopBorderColor(HSSFColor.BLACK.index);
                    stringCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                    
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
                    topStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    topStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    topStyle.setTopBorderColor(HSSFColor.BLACK.index);
                    topStyle.setBottomBorderColor(HSSFColor.BLACK.index);
                    
                    HSSFCellStyle top2Style=workbook.createCellStyle();
                    top2Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    top2Style.setFont(top1FontStyle);
                    top2Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    top2Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
                    top2Style.setLeftBorderColor(HSSFColor.BLACK.index);
                    top2Style.setRightBorderColor(HSSFColor.BLACK.index);
                    top2Style.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    top2Style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
                    top2Style.setTopBorderColor(HSSFColor.BLACK.index);
                    top2Style.setBottomBorderColor(HSSFColor.BLACK.index);
                    
                    /**
                     * 数据总表
                     */
                    String title = monthName+level+"汇总数据";
                    workbook.createSheet(title);
                    HSSFSheet sheet = workbook.getSheetAt(0);
                    
                    populateMainSheet(sheet, title, level, department, inRateDate, topStyle, top2Style, percentCellStyle, numberCellStyle, averageDoseCellStyle, stringCellStyle);
                    
                    if( "1".equalsIgnoreCase(department) ){
                    	/**
                    	 * 剂量表
                    	 */
                    	String subTitle = monthName+level+"剂型汇总数据";
                    	workbook.createSheet(subTitle);
                    	HSSFSheet sheet1 = workbook.getSheetAt(1);
                    	populateSubSheet(sheet1, subTitle, level, inRateDate, topStyle, top2Style, percentCellStyle, averageDoseCellStyle, stringCellStyle);
                    	
                    	String childLevel = "";
                    	if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(level) ){
                    		childLevel = LsAttributes.USER_LEVEL_RSM;
                    	}else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(level) ){
                    		childLevel = LsAttributes.USER_LEVEL_DSM;
                    	}
                    	
                    	if( !LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(level) ){
                    		/**
                    		 * 下一级数据总表
                    		 */
                    		String childTitle = monthName+childLevel+"汇总数据";
                    		workbook.createSheet(childTitle);
                    		HSSFSheet childSheet1 = workbook.getSheetAt(2);
                    		
                    		populateMainSheet(childSheet1, childTitle, childLevel, department, inRateDate, topStyle, top2Style, percentCellStyle, numberCellStyle, averageDoseCellStyle, stringCellStyle);
                    		/**
                    		 * 下一级剂量表
                    		 */
                    		String childTitle2 = monthName+childLevel+"剂型汇总数据";
                    		workbook.createSheet(childTitle2);
                    		HSSFSheet childSheet2 = workbook.getSheetAt(3);
                    		populateSubSheet(childSheet2, childTitle2, childLevel, inRateDate, topStyle, top2Style, percentCellStyle, averageDoseCellStyle, stringCellStyle);
                    	}
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
    
    private void populateMainSheet(HSSFSheet sheet, String title, String level, String department, Date inRateDate 
    		,HSSFCellStyle topStyle, HSSFCellStyle top2Style, HSSFCellStyle percentCellStyle
    		, HSSFCellStyle numberCellStyle, HSSFCellStyle averageDoseCellStyle, HSSFCellStyle stringCellStyle) throws Exception{
    	
    	HSSFRow row = sheet.createRow(0);
        row.createCell(0, XSSFCell.CELL_TYPE_STRING).setCellValue(title);
        row.createCell(1, XSSFCell.CELL_TYPE_STRING).setCellValue("");
        row.createCell(2, XSSFCell.CELL_TYPE_STRING).setCellValue("");
        row.createCell(3, XSSFCell.CELL_TYPE_STRING).setCellValue("");
        sheet.addMergedRegion(new Region(0, (short)0, 0, (short)3));
        row.getCell(0).setCellStyle(topStyle);
        
        //build the header
        row = sheet.createRow(1);
        HSSFRow top2Row = sheet.createRow(2);
        
        int columnNum = 0;
        HSSFCell userCell = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        userCell.setCellValue(level+"名单");
        userCell.setCellStyle(top2Style);
        sheet.setColumnWidth(columnNum-1, 15*256);
        
        HSSFCell emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
        emptyMergeCell.setCellValue("");
        emptyMergeCell.setCellStyle(top2Style);
        
        sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        
        if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(level) ){
        	HSSFCell rsmTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        	rsmTitle.setCellValue("RSM");
        	rsmTitle.setCellStyle(top2Style);
        	
        	emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
            emptyMergeCell.setCellValue("");
            emptyMergeCell.setCellStyle(top2Style);
        	
        	sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        }
        
        HSSFCell inRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        inRateTitle.setCellValue("上报率");
        inRateTitle.setCellStyle(top2Style);
        
        emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
        emptyMergeCell.setCellValue("");
        emptyMergeCell.setCellStyle(top2Style);
        
        sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        
        HSSFCell coreInRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        coreInRateTitle.setCellValue("Core医院上报率");
        coreInRateTitle.setCellStyle(top2Style);
        
        emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
        emptyMergeCell.setCellValue("");
        emptyMergeCell.setCellStyle(top2Style);
        
        sheet.setColumnWidth(columnNum-1, 20*256);
        sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        
        HSSFCell emergingInRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        emergingInRateTitle.setCellValue("Emerging医院上报率");
        emergingInRateTitle.setCellStyle(top2Style);
        
        emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
        emptyMergeCell.setCellValue("");
        emptyMergeCell.setCellStyle(top2Style);
        
        sheet.setColumnWidth(columnNum-1, 20*256);
        sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        
        HSSFCell whRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        whRateTitle.setCellValue("雾化率");
        
        emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
        emptyMergeCell.setCellValue("");
        emptyMergeCell.setCellStyle(top2Style);
        
        whRateTitle.setCellStyle(top2Style);
        sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        
        HSSFCell coreWhRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        coreWhRateTitle.setCellValue("Core医院雾化率");
        coreWhRateTitle.setCellStyle(top2Style);
        
        emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
        emptyMergeCell.setCellValue("");
        emptyMergeCell.setCellStyle(top2Style);
        
        sheet.setColumnWidth(columnNum-1, 20*256);
        sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        
        HSSFCell emergingWhRateTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        emergingWhRateTitle.setCellValue("Emerging医院雾化率");
        emergingWhRateTitle.setCellStyle(top2Style);
        
        emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
        emptyMergeCell.setCellValue("");
        emptyMergeCell.setCellStyle(top2Style);
        
        sheet.setColumnWidth(columnNum-1, 20*256);
        sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        
        switch(department){
            case "1":
            	HSSFCell pnumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
            	pnumTitle.setCellValue("病房病人数");
            	pnumTitle.setCellStyle(top2Style);
            	
            	emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
                emptyMergeCell.setCellValue("");
                emptyMergeCell.setCellStyle(top2Style);
            	
            	sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
            	
            	HSSFCell aenumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
            	aenumTitle.setCellValue("病房内AECOPD人数");
            	aenumTitle.setCellStyle(top2Style);
            	
            	emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
                emptyMergeCell.setCellValue("");
                emptyMergeCell.setCellStyle(top2Style);
            	
            	sheet.setColumnWidth(columnNum-1, 15*256);
            	sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
            	break;
            case "3":
            	HSSFCell chenumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
            	chenumTitle.setCellValue("病房病人数");
            	chenumTitle.setCellStyle(top2Style);
            	
            	emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
                emptyMergeCell.setCellValue("");
                emptyMergeCell.setCellStyle(top2Style);
            	
            	sheet.setColumnWidth(columnNum-1, 10*256);
            	sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
            	
            	HSSFCell risknumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
            	risknumTitle.setCellValue("病房内合并COPD或哮喘的手术病人数");
            	risknumTitle.setCellStyle(top2Style);
            	
            	emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
                emptyMergeCell.setCellValue("");
                emptyMergeCell.setCellStyle(top2Style);
            	
            	sheet.setColumnWidth(columnNum-1, 20*256);
            	sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
            	break;
            case "2":
            	HSSFCell pednumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
            	pednumTitle.setCellValue("门诊人次");
            	
            	emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
                emptyMergeCell.setCellValue("");
                emptyMergeCell.setCellStyle(top2Style);
            	
            	pednumTitle.setCellStyle(top2Style);
            	sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
            	break;
        	default:
            		break;
        }
        
        HSSFCell lsnumTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        lsnumTitle.setCellValue("雾化令舒人数");
        lsnumTitle.setCellStyle(top2Style);
        
        emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
        emptyMergeCell.setCellValue("");
        emptyMergeCell.setCellStyle(top2Style);
        
        sheet.setColumnWidth(columnNum-1, 10*256);
        sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        
        HSSFCell averageDoseTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        averageDoseTitle.setCellValue("平均计量");
        
        emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
        emptyMergeCell.setCellValue("");
        emptyMergeCell.setCellStyle(top2Style);
        
        averageDoseTitle.setCellStyle(top2Style);
        sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        
        if( "1".equalsIgnoreCase(department) ){
        	HSSFCell coreAverageTopTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        	coreAverageTopTitle.setCellValue("Core医院计量");
        	coreAverageTopTitle.setCellStyle(top2Style);
        	sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 1, (short)(columnNum+1)));
        	
        	columnNum--;
        	
        	HSSFCell coreAverageDoseTitle = top2Row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        	coreAverageDoseTitle.setCellValue("Core医院平均计量");
        	coreAverageDoseTitle.setCellStyle(top2Style);
        	
        	emptyMergeCell = row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
            emptyMergeCell.setCellValue("Core医院计量");
            emptyMergeCell.setCellStyle(top2Style);
            
        	sheet.addMergedRegion(new Region(2, (short)(columnNum-1), 2, (short)(columnNum-1)));
        	
        	HSSFCell coreAverageDose3Title = top2Row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        	coreAverageDose3Title.setCellValue("Core医院三级医院平均计量");
        	coreAverageDose3Title.setCellStyle(top2Style);
        	
        	emptyMergeCell = row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
            emptyMergeCell.setCellValue("Core医院计量");
            emptyMergeCell.setCellStyle(top2Style);
        	
        	sheet.addMergedRegion(new Region(2, (short)(columnNum-1), 2, (short)(columnNum-1)));
        	
        	HSSFCell coreAverageDose2Title = top2Row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        	coreAverageDose2Title.setCellValue("Core医院二级医院平均计量");
        	coreAverageDose2Title.setCellStyle(top2Style);
        	
        	emptyMergeCell = row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
            emptyMergeCell.setCellValue("Core医院计量");
            emptyMergeCell.setCellStyle(top2Style);
        	
        	sheet.addMergedRegion(new Region(2, (short)(columnNum-1), 2, (short)(columnNum-1)));
        	
        	HSSFCell emergingAverageDoseTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
        	emergingAverageDoseTitle.setCellValue("Emerging医院平均计量");
        	emergingAverageDoseTitle.setCellStyle(top2Style);
        	
        	emptyMergeCell = top2Row.createCell(columnNum-1, XSSFCell.CELL_TYPE_STRING);
            emptyMergeCell.setCellValue("");
            emptyMergeCell.setCellStyle(top2Style);
        	
        	sheet.addMergedRegion(new Region(1, (short)(columnNum-1), 2, (short)(columnNum-1)));
        }
        
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
        
        logger.info("monthlyStatistics size is " + monthlyStatistics.size());
        
        int currentRowNum = 3;
        for( MonthlyStatisticsData sData : monthlyStatistics){
        	
        	row = sheet.createRow(currentRowNum++);
        	int column = 0;
        	String name = "";
        	String displayName = "";
        	
        	switch(level){
            	case LsAttributes.USER_LEVEL_RSD:
            		name = sData.getRsd();
            		displayName = sData.getRsd();
            		break;
            	case LsAttributes.USER_LEVEL_RSM:
            		name = sData.getRsm();
            		displayName = sData.getRsm();
            		break;
            	case LsAttributes.USER_LEVEL_DSM:
            		name = sData.getRsd()+sData.getRsm()+sData.getDsmCode();
            		displayName = sData.getDsmName();
            		break;
        	}
        	
        	if( name == null|| "".equalsIgnoreCase(name) ){
        		displayName = "全国";
        	}
        	
        	HSSFCell nameCell = row.createCell(column++, XSSFCell.CELL_TYPE_STRING);
        	nameCell.setCellValue(displayName);
        	nameCell.setCellStyle(stringCellStyle);
        	
        	if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(level) ){
        		HSSFCell rsmNameCell = row.createCell(column++, XSSFCell.CELL_TYPE_STRING);
        		rsmNameCell.setCellValue(sData.getRsm());
        		rsmNameCell.setCellStyle(stringCellStyle);
        	}
        	
        	/**
        	 * 上报率
        	 */
        	HSSFCell inRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	inRateCell.setCellValue(sData.getInRate());
        	inRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * Core上报率
        	 */
        	HSSFCell coreRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	if( null != coreMonthlyStatistics && null != coreMonthlyStatistics.get(name) ){
        		coreRateCell.setCellValue(coreMonthlyStatistics.get(name).getCoreInRate());
        	}else if(null==name||"".equalsIgnoreCase(name)){
        		coreRateCell.setCellValue(coreMonthlyCountryStatistics.getCoreInRate());
        	}else{
        		coreRateCell.setCellValue(0);
        	}
        	coreRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * Emerging上报率
        	 */
        	HSSFCell emergingRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	if( null != emergingMonthlyStatistics && null != emergingMonthlyStatistics.get(name) ){
        		emergingRateCell.setCellValue(emergingMonthlyStatistics.get(name).getEmergingInRate());
        	}else if(null==name||"".equalsIgnoreCase(name)){
        		emergingRateCell.setCellValue(emergingMonthlyCountryStatistics.getEmergingInRate());
        	}else{
        		emergingRateCell.setCellValue(0);
        	}
        	emergingRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 雾化率
        	 */
        	HSSFCell whRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	whRateCell.setCellValue(sData.getWhRate());
        	whRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * Core雾化率
        	 */
        	HSSFCell coreWhRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	if( null != coreMonthlyStatistics && null != coreMonthlyStatistics.get(name) ){
        		coreWhRateCell.setCellValue(coreMonthlyStatistics.get(name).getCoreWhRate());
        	}else if(null==name||"".equalsIgnoreCase(name)){
        		coreWhRateCell.setCellValue(coreMonthlyCountryStatistics.getCoreWhRate());
        	}else{
        		coreWhRateCell.setCellValue(0);
        	}
        	coreWhRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * Emerging雾化率
        	 */
        	HSSFCell emergingwhRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	if( null != emergingMonthlyStatistics && null != emergingMonthlyStatistics.get(name) ){
        		emergingwhRateCell.setCellValue(emergingMonthlyStatistics.get(name).getEmergingWhRate());
        	}else if(null==name||"".equalsIgnoreCase(name)){
        		emergingwhRateCell.setCellValue(emergingMonthlyCountryStatistics.getEmergingWhRate());
        	}else{
        		emergingwhRateCell.setCellValue(0);
        	}
        	emergingwhRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 病房病人数
        	 */
        	HSSFCell pnumCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	pnumCell.setCellValue(sData.getPnum());
        	pnumCell.setCellStyle(numberCellStyle);
        	
        	/**
        	 * AE人数
        	 */
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
            
        	/**
        	 * 雾化令舒人数
        	 */
            HSSFCell lsnumCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
            lsnumCell.setCellValue(sData.getLsnum());
            lsnumCell.setCellStyle(numberCellStyle);
            
            /**
             * 平均剂量
             */
            HSSFCell averageDoseCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
            averageDoseCell.setCellValue(sData.getAverageDose());
            averageDoseCell.setCellStyle(averageDoseCellStyle);

            if( "1".equalsIgnoreCase(department) ){
            	/**
            	 * Core医院平均剂量
            	 */
            	HSSFCell coreAverageDoseCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
            	if( null != coreMonthlyStatistics && null != coreMonthlyStatistics.get(name) ){
            		coreAverageDoseCell.setCellValue(coreMonthlyStatistics.get(name).getCoreAverageDose());
            	}else if(null==name||"".equalsIgnoreCase(name)){
            		coreAverageDoseCell.setCellValue(coreMonthlyCountryStatistics.getCoreAverageDose());
            	}else{
            		coreAverageDoseCell.setCellValue("#N/A");
            	}
            	coreAverageDoseCell.setCellStyle(averageDoseCellStyle);
            	
            	/**
            	 * Core医院三级医院平均剂量
            	 */
            	HSSFCell coreAverageDose3Cell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
            	if( null != coreMonthlyStatistics && null != coreMonthlyStatistics.get(name) && coreMonthlyStatistics.get(name).getCoreAverageDose3() != -1){
            		coreAverageDose3Cell.setCellValue(coreMonthlyStatistics.get(name).getCoreAverageDose3());
            	}else if(null==name||"".equalsIgnoreCase(name)){
            		coreAverageDose3Cell.setCellValue(coreMonthlyCountryStatistics.getCoreAverageDose3());
            	}else{
            		coreAverageDose3Cell.setCellValue("#N/A");
            	}
            	coreAverageDose3Cell.setCellStyle(averageDoseCellStyle);
            	
            	/**
            	 * Core医院二级医院平均剂量
            	 */
            	HSSFCell coreAverageDose2Cell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
            	if( null != coreMonthlyStatistics && null != coreMonthlyStatistics.get(name) && coreMonthlyStatistics.get(name).getCoreAverageDose2() != -1 ){
            		coreAverageDose2Cell.setCellValue(coreMonthlyStatistics.get(name).getCoreAverageDose2());
            	}else if(null==name||"".equalsIgnoreCase(name)){
            		coreAverageDose2Cell.setCellValue(coreMonthlyCountryStatistics.getCoreAverageDose2());
            	}else{
            		coreAverageDose2Cell.setCellValue("#N/A");
            	}
            	coreAverageDose2Cell.setCellStyle(averageDoseCellStyle);
            	
            	/**
            	 * Emerging平均剂量
            	 */
            	HSSFCell emergingAverageDoseCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
            	if( null != emergingMonthlyStatistics && null != emergingMonthlyStatistics.get(name) ){
            		emergingAverageDoseCell.setCellValue(emergingMonthlyStatistics.get(name).getEmergingAverageDose());
            	}else if(null==name||"".equalsIgnoreCase(name)){
            		emergingAverageDoseCell.setCellValue(emergingMonthlyCountryStatistics.getEmergingAverageDose());
            	}else{
            		emergingAverageDoseCell.setCellValue("#N/A");
            	}
            	emergingAverageDoseCell.setCellStyle(averageDoseCellStyle);
            }
        }
    }
    
    private void populateSubSheet(HSSFSheet sheet, String title, String level, Date inRateDate
    		,HSSFCellStyle topStyle,HSSFCellStyle top2Style, HSSFCellStyle percentCellStyle, HSSFCellStyle averageDoseCellStyle, HSSFCellStyle stringCellStyle) throws Exception{
		int currentRowNum = 0;
	    //build the header
		HSSFRow row = sheet.createRow(currentRowNum++);
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
	    
	    HSSFCell coreAverageDoseTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    coreAverageDoseTitle.setCellValue("Core医院平均剂量");
	    coreAverageDoseTitle.setCellStyle(top2Style);
	    sheet.setColumnWidth(columnNum-1, 20*256);
	    
	    HSSFCell core3AverageDoseTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core3AverageDoseTitle.setCellValue("三级医院平均剂量");
	    core3AverageDoseTitle.setCellStyle(top2Style);
	    sheet.setColumnWidth(columnNum-1, 20*256);
	    
	    HSSFCell core3Ad1Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core3Ad1Title.setCellValue("1mg");
	    core3Ad1Title.setCellStyle(top2Style);
	    
	    HSSFCell core3Ad2Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core3Ad2Title.setCellValue("2mg");
	    core3Ad2Title.setCellStyle(top2Style);
	    
	    HSSFCell core3Ad3Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core3Ad3Title.setCellValue("3mg");
	    core3Ad3Title.setCellStyle(top2Style);
	    
	    HSSFCell core3Ad4Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core3Ad4Title.setCellValue("4mg");
	    core3Ad4Title.setCellStyle(top2Style);
	    
	    HSSFCell core3Ad6Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core3Ad6Title.setCellValue("6mg");
	    core3Ad6Title.setCellStyle(top2Style);
	    
	    HSSFCell core3Ad8Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core3Ad8Title.setCellValue("8mg");
	    core3Ad8Title.setCellStyle(top2Style);

	    HSSFCell core3Ad4upTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core3Ad4upTitle.setCellValue("4mg及以上");
	    core3Ad4upTitle.setCellStyle(top2Style);

	    HSSFCell core2AverageDoseTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core2AverageDoseTitle.setCellValue("二级医院平均剂量");
	    core2AverageDoseTitle.setCellStyle(top2Style);
	    sheet.setColumnWidth(columnNum-1, 20*256);
	    
	    HSSFCell core2Ad1Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core2Ad1Title.setCellValue("1mg");
	    core2Ad1Title.setCellStyle(top2Style);
	    
	    HSSFCell core2Ad2Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core2Ad2Title.setCellValue("2mg");
	    core2Ad2Title.setCellStyle(top2Style);
	    
	    HSSFCell core2Ad3Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core2Ad3Title.setCellValue("3mg");
	    core2Ad3Title.setCellStyle(top2Style);
	    
	    HSSFCell core2Ad4Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core2Ad4Title.setCellValue("4mg");
	    core2Ad4Title.setCellStyle(top2Style);
	    
	    HSSFCell core2Ad6Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core2Ad6Title.setCellValue("6mg");
	    core2Ad6Title.setCellStyle(top2Style);
	    
	    HSSFCell core2Ad8Title = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core2Ad8Title.setCellValue("8mg");
	    core2Ad8Title.setCellStyle(top2Style);

	    HSSFCell core2Ad4upTitle = row.createCell(columnNum++, XSSFCell.CELL_TYPE_STRING);
	    core2Ad4upTitle.setCellValue("4mg及以上");
	    core2Ad4upTitle.setCellStyle(top2Style);
	 
        String beginDuraion = DateUtils.getMonthInRateBeginDuration(inRateDate);
        String endDuraion = DateUtils.getMonthInRateEndDuration(inRateDate);
        
	    logger.info(String.format("begin to get monthly inRate during %s and %s", beginDuraion,endDuraion));
        List<MonthlyStatisticsData> coreMonthlyStatistics = new ArrayList<MonthlyStatisticsData>();
        MonthlyStatisticsData coreMonthlyCountryStatistics = new MonthlyStatisticsData();
        
		coreMonthlyStatistics = respirologyService.getCoreAverageDoseMonthlyStatisticsData(beginDuraion,endDuraion,level);
		coreMonthlyCountryStatistics = respirologyService.getCoreAverageDoseMonthlyStatisticsCountryData(beginDuraion, endDuraion);
        logger.info("get monthly statistics data end...");
        
        coreMonthlyStatistics.add(coreMonthlyCountryStatistics);
        for( MonthlyStatisticsData sData : coreMonthlyStatistics){
        	
        	row = sheet.createRow(currentRowNum++);
        	int column = 0;
        	String name = "";
        	String displayName = "";
        	
        	switch(level){
            	case LsAttributes.USER_LEVEL_RSD:
            		name = sData.getRsd();
            		displayName = sData.getRsd();
            		break;
            	case LsAttributes.USER_LEVEL_RSM:
            		name = sData.getRsm();
            		displayName = sData.getRsm();
            		break;
            	case LsAttributes.USER_LEVEL_DSM:
            		name = sData.getRsd()+sData.getRsm()+sData.getDsmCode();
            		displayName = sData.getDsmName();
            		break;
        	}
        	
        	if( name == null|| "".equalsIgnoreCase(name) ){
        		displayName = "全国";
        	}
        	
        	HSSFCell nameCell = row.createCell(column++, XSSFCell.CELL_TYPE_STRING);
        	nameCell.setCellValue(displayName);
        	nameCell.setCellStyle(stringCellStyle);
        	
        	if( LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(level) ){
        		HSSFCell rsmNameCell = row.createCell(column++, XSSFCell.CELL_TYPE_STRING);
        		rsmNameCell.setCellValue(sData.getRsm());
        		rsmNameCell.setCellStyle(stringCellStyle);
        	}
        	
        	/**
        	 * Core医院平均剂量
        	 */
        	HSSFCell coreAverageDoseCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(coreAverageDoseCell, sData.getAverageDose());
        	coreAverageDoseCell.setCellStyle(averageDoseCellStyle);
        	
        	/**
        	 * Core三级医院平均剂量
        	 */
        	HSSFCell coreLevel3ADCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(coreLevel3ADCell, sData.getCoreAverageDose3());
        	coreLevel3ADCell.setCellStyle(averageDoseCellStyle);
        	
        	/**
        	 * 1mg
        	 */
        	HSSFCell level31mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level31mgRateCell, sData.getCoreLevel3AD1mgRate());
        	level31mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 2mg
        	 */
        	HSSFCell level32mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level32mgRateCell, sData.getCoreLevel3AD2mgRate());
        	level32mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 3mg
        	 */
        	HSSFCell level33mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level33mgRateCell, sData.getCoreLevel3AD3mgRate());
        	level33mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 4mg
        	 */
        	HSSFCell level34mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level34mgRateCell, sData.getCoreLevel3AD4mgRate());
        	level34mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 6mg
        	 */
        	HSSFCell level36mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level36mgRateCell, sData.getCoreLevel3AD6mgRate());
        	level36mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 8mg
        	 */
        	HSSFCell level38mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level38mgRateCell, sData.getCoreLevel3AD8mgRate());
        	level38mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 4mg及以上
        	 */
        	HSSFCell level34mgUpRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level34mgUpRateCell, sData.getCoreLevel3AD4mgUpRate());
        	level34mgUpRateCell.setCellStyle(percentCellStyle);

        	/**
        	 * Core二级医院平均剂量
        	 */
        	HSSFCell coreLevel2ADCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(coreLevel2ADCell, sData.getCoreAverageDose2());
        	coreLevel2ADCell.setCellStyle(averageDoseCellStyle);
        	
        	/**
        	 * 1mg
        	 */
        	HSSFCell level21mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level21mgRateCell, sData.getCoreLevel2AD1mgRate());
        	level21mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 2mg
        	 */
        	HSSFCell level22mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level22mgRateCell, sData.getCoreLevel2AD2mgRate());
        	level22mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 3mg
        	 */
        	HSSFCell level23mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level23mgRateCell, sData.getCoreLevel2AD3mgRate());
        	level23mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 4mg
        	 */
        	HSSFCell level24mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level24mgRateCell, sData.getCoreLevel2AD4mgRate());
        	level24mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 6mg
        	 */
        	HSSFCell level26mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level26mgRateCell, sData.getCoreLevel2AD6mgRate());
        	level26mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 8mg
        	 */
        	HSSFCell level28mgRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level28mgRateCell, sData.getCoreLevel2AD8mgRate());
        	level28mgRateCell.setCellStyle(percentCellStyle);
        	
        	/**
        	 * 4mg及以上
        	 */
        	HSSFCell level24mgUpRateCell = row.createCell(column++, XSSFCell.CELL_TYPE_NUMERIC);
        	populateCellDoubleValue(level24mgUpRateCell, sData.getCoreLevel2AD4mgUpRate());
        	level24mgUpRateCell.setCellStyle(percentCellStyle);
        }
    }
    
    private void populateCellDoubleValue(HSSFCell cell, double value){
    	if(value!=-1){
    		cell.setCellValue(value);
    	}else{
    		cell.setCellValue("#N/A");
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
    				
    				int columnCount = 0;
    				
    				HSSFCell userCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				userCell.setCellValue("RSM");
    				userCell.setCellStyle(topStyle);
    				
    				HSSFCell pedRoomDtWhNumTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				pedRoomDtWhNumTitle.setCellValue("儿科病房");
    				pedRoomDtWhNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell pedRoomDtWhRateTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				pedRoomDtWhRateTitle.setCellValue("儿科病房占比");
    				pedRoomDtWhRateTitle.setCellStyle(topStyle);
    				
    				HSSFCell pedEmerWhNumTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				pedEmerWhNumTitle.setCellValue("儿科门急诊");
    				pedEmerWhNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell pedEmerWhRateTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				pedEmerWhRateTitle.setCellValue("儿科门急诊占比");
    				pedEmerWhRateTitle.setCellStyle(topStyle);
    				
    				
    				HSSFCell homeWhNumTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				homeWhNumTitle.setCellValue("家庭雾化");
    				homeWhNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell homeWhRateTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				homeWhRateTitle.setCellValue("家庭雾化占比");
    				homeWhRateTitle.setCellStyle(topStyle);
    				
    				HSSFCell resRoomNumTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				resRoomNumTitle.setCellValue("呼吸科病房");
    				resRoomNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell resRoomRateTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				resRoomRateTitle.setCellValue("呼吸科病房占比");
    				resRoomRateTitle.setCellStyle(topStyle);
    				
    				HSSFCell resClinicNumTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				resClinicNumTitle.setCellValue("呼吸科门急诊");
    				resClinicNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell resClinicRateTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				resClinicRateTitle.setCellValue("呼吸科门急诊占比");
    				resClinicRateTitle.setCellStyle(topStyle);
    				
    				HSSFCell otherNumTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				otherNumTitle.setCellValue("其他科室");
    				otherNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell otherRateTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				otherRateTitle.setCellValue("其他科室占比");
    				otherRateTitle.setCellStyle(topStyle);
    				
    				HSSFCell sumNumTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				sumNumTitle.setCellValue("总袋数");
    				sumNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell inHosNumTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				inHosNumTitle.setCellValue("上报医院数量");
    				inHosNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell hosNumTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				hosNumTitle.setCellValue("负责医院数量");
    				hosNumTitle.setCellStyle(topStyle);
    				
    				HSSFCell inRateTitle = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    				inRateTitle.setCellValue("上报率");
    				inRateTitle.setCellStyle(topStyle);
    				
    				for( int i = 0; i < 19; i++ ){
    					sheet.setColumnWidth(i, 16*256);
    				}
    				
    				List<MonthlyRatioData> monthlyCollectionData = hospitalService.getMonthlyCollectionData(collectionDate);
    				
    				for( MonthlyRatioData data : monthlyCollectionData ){
    					
    					int sumColumnCount = 0;
    					
    					row = sheet.createRow(currentRowNum++);
    					HSSFCell rsmCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_STRING);
    					rsmCell.setCellValue(data.getRsmRegion());
    					if( currentRowNum%2 == 0 ){
    						rsmCell.setCellStyle(evenNumCellStyle);
    					}else{
    						rsmCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell pedRoomDtWhCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					pedRoomDtWhCell.setCellValue(data.getPedRoomDrugStoreWh());
    					if( currentRowNum%2 == 0 ){
    						pedRoomDtWhCell.setCellStyle(evenNumCellStyle);
    					}else{
    						pedRoomDtWhCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell pedRoomDtWhRateCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					pedRoomDtWhRateCell.setCellValue(data.getPedRoomDrugStoreWhRate());
    					if( currentRowNum%2 == 0 ){
    						pedRoomDtWhRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						pedRoomDtWhRateCell.setCellStyle(percentCellStyle);
    					}
    					
    					HSSFCell pedEmerWhCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					pedEmerWhCell.setCellValue(data.getPedEmerWh());
    					if( currentRowNum%2 == 0 ){
    						pedEmerWhCell.setCellStyle(evenNumCellStyle);
    					}else{
    						pedEmerWhCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell pedEmerWhRateCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					pedEmerWhRateCell.setCellValue(data.getPedEmerWhRate());
    					if( currentRowNum%2 == 0 ){
    						pedEmerWhRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						pedEmerWhRateCell.setCellStyle(percentCellStyle);
    					}
    					
    					HSSFCell homeWhCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					homeWhCell.setCellValue(data.getHomeWh());
    					if( currentRowNum%2 == 0 ){
    						homeWhCell.setCellStyle(evenNumCellStyle);
    					}else{
    						homeWhCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell homeWhRateCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					homeWhRateCell.setCellValue(data.getHomeWhRate());
    					if( currentRowNum%2 == 0 ){
    						homeWhRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						homeWhRateCell.setCellStyle(percentCellStyle);
    					}
    					
    					HSSFCell resRoomCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					resRoomCell.setCellValue(data.getResRoom());
    					if( currentRowNum%2 == 0 ){
    						resRoomCell.setCellStyle(evenNumCellStyle);
    					}else{
    						resRoomCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell resRoomRateCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					resRoomRateCell.setCellValue(data.getResRoomRate());
    					if( currentRowNum%2 == 0 ){
    						resRoomRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						resRoomRateCell.setCellStyle(percentCellStyle);
    					}
    					
    					HSSFCell resClinicCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					resClinicCell.setCellValue(data.getResClinic());
    					if( currentRowNum%2 == 0 ){
    						resClinicCell.setCellStyle(evenNumCellStyle);
    					}else{
    						resClinicCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell resClinicRateCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					resClinicRateCell.setCellValue(data.getResClinicRate());
    					if( currentRowNum%2 == 0 ){
    						resClinicRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						resClinicRateCell.setCellStyle(percentCellStyle);
    					}
    					
    					HSSFCell otherCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					otherCell.setCellValue(data.getOthernum());
    					if( currentRowNum%2 == 0 ){
    						otherCell.setCellStyle(evenNumCellStyle);
    					}else{
    						otherCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell otherRateCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					otherRateCell.setCellValue(data.getOthernumrate());
    					if( currentRowNum%2 == 0 ){
    						otherRateCell.setCellStyle(evenPercentCellStyle);
    					}else{
    						otherRateCell.setCellStyle(percentCellStyle);
    					}

    					HSSFCell sumCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					sumCell.setCellValue(data.getTotalnum());
    					if( currentRowNum%2 == 0 ){
    						sumCell.setCellStyle(evenNumCellStyle);
    					}else{
    						sumCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell inHosNumCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					inHosNumCell.setCellValue(data.getInnum());
    					if( currentRowNum%2 == 0 ){
    						inHosNumCell.setCellStyle(evenNumCellStyle);
    					}else{
    						inHosNumCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell hosNumCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
    					hosNumCell.setCellValue(data.getHosnum());
    					if( currentRowNum%2 == 0 ){
    						hosNumCell.setCellStyle(evenNumCellStyle);
    					}else{
    						hosNumCell.setCellStyle(numCellStyle);
    					}
    					
    					HSSFCell inRateCell = row.createCell(sumColumnCount++, XSSFCell.CELL_TYPE_NUMERIC);
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
    				
    				columnCount = 0;
    				
					HSSFCell rsmCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
					rsmCell.setCellValue("总计");
					if( rsmDataCount%2 == 0 ){
						rsmCell.setCellStyle(evenNumCellStyle);
					}else{
						rsmCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell pedRoomDtWhCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					pedRoomDtWhCell.setCellValue(monthlySumData.getPedRoomDrugStoreWh());
					if( currentRowNum%2 == 0 ){
						pedRoomDtWhCell.setCellStyle(evenNumCellStyle);
					}else{
						pedRoomDtWhCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell pedRoomDtWhRateCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					pedRoomDtWhRateCell.setCellValue(monthlySumData.getPedRoomDrugStoreWhRate());
					if( currentRowNum%2 == 0 ){
						pedRoomDtWhRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						pedRoomDtWhRateCell.setCellStyle(percentCellStyle);
					}
					
					HSSFCell pedEmerWhCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					pedEmerWhCell.setCellValue(monthlySumData.getPedEmerWh());
					if( currentRowNum%2 == 0 ){
						pedEmerWhCell.setCellStyle(evenNumCellStyle);
					}else{
						pedEmerWhCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell pedEmerWhRateCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					pedEmerWhRateCell.setCellValue(monthlySumData.getPedEmerWhRate());
					if( currentRowNum%2 == 0 ){
						pedEmerWhRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						pedEmerWhRateCell.setCellStyle(percentCellStyle);
					}
					
					HSSFCell homeWhCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					homeWhCell.setCellValue(monthlySumData.getHomeWh());
					if( currentRowNum%2 == 0 ){
						homeWhCell.setCellStyle(evenNumCellStyle);
					}else{
						homeWhCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell homeWhRateCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					homeWhRateCell.setCellValue(monthlySumData.getHomeWhRate());
					if( currentRowNum%2 == 0 ){
						homeWhRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						homeWhRateCell.setCellStyle(percentCellStyle);
					}
					
					HSSFCell resRoomCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					resRoomCell.setCellValue(monthlySumData.getResRoom());
					if( currentRowNum%2 == 0 ){
						resRoomCell.setCellStyle(evenNumCellStyle);
					}else{
						resRoomCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell resRoomRateCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					resRoomRateCell.setCellValue(monthlySumData.getResRoomRate());
					if( currentRowNum%2 == 0 ){
						resRoomRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						resRoomRateCell.setCellStyle(percentCellStyle);
					}
					
					HSSFCell resClinicCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					resClinicCell.setCellValue(monthlySumData.getResClinic());
					if( currentRowNum%2 == 0 ){
						resClinicCell.setCellStyle(evenNumCellStyle);
					}else{
						resClinicCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell resClinicRateCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					resClinicRateCell.setCellValue(monthlySumData.getResClinicRate());
					if( currentRowNum%2 == 0 ){
						resClinicRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						resClinicRateCell.setCellStyle(percentCellStyle);
					}
					
					HSSFCell otherCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					otherCell.setCellValue(monthlySumData.getOthernum());
					if( rsmDataCount%2 == 0 ){
						otherCell.setCellStyle(evenNumCellStyle);
					}else{
						otherCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell otherRateCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					otherRateCell.setCellValue(monthlySumData.getOthernumrate());
					if( rsmDataCount%2 == 0 ){
						otherRateCell.setCellStyle(evenPercentCellStyle);
					}else{
						otherRateCell.setCellStyle(percentCellStyle);
					}

					HSSFCell sumCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					sumCell.setCellValue(monthlySumData.getTotalnum());
					if( rsmDataCount%2 == 0 ){
						sumCell.setCellStyle(evenNumCellStyle);
					}else{
						sumCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell inHosNumCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					inHosNumCell.setCellValue(monthlySumData.getInnum());
					if( rsmDataCount%2 == 0 ){
						inHosNumCell.setCellStyle(evenNumCellStyle);
					}else{
						inHosNumCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell hosNumCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
					hosNumCell.setCellValue(monthlySumData.getHosnum());
					if( rsmDataCount%2 == 0 ){
						hosNumCell.setCellStyle(evenNumCellStyle);
					}else{
						hosNumCell.setCellStyle(numCellStyle);
					}
					
					HSSFCell inRateCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_NUMERIC);
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
    
    /**
     * 呼吸科周周报
     * @param request request
     * @param response response
     * @return 
     * @throws IOException
     */
    @RequestMapping("/doDownloadResMonthData")
    public String doDownloadResMonthData(HttpServletRequest request, HttpServletResponse response) throws IOException{
        logger.info("download the res month data..");
        FileOutputStream fOut = null;
        String fileName = null;
        String fromWeb = request.getParameter("fromWeb");
        String isRe2 = request.getParameter("isRe2");
        String selfLevel = request.getParameter("level");
        
        List<ReportFileObject> reportFiles = new ArrayList<ReportFileObject>();
        List<String> keys = new ArrayList<String>();
        String lastWeekDuration = DateUtils.getWeeklyDurationStr(new Date(),"MM.dd");
        try{
        	long systemTime = System.currentTimeMillis();
                File resMonthData = new File(request.getRealPath("/") + "resMonthData/"+systemTime+"/");
                if( !resMonthData.exists() ){
                    resMonthData.mkdir();
                }
                
                HSSFWorkbook workbook = new HSSFWorkbook();
                
                HSSFCellStyle top1Style=workbook.createCellStyle();
                HSSFCellStyle rsmTitleStyle=workbook.createCellStyle();
                HSSFCellStyle rsmTitleBorderStyle=workbook.createCellStyle();
                HSSFCellStyle rsmValueStyle=workbook.createCellStyle();
                HSSFCellStyle rsmValueBorderStyle=workbook.createCellStyle();
                HSSFCellStyle top2Style=workbook.createCellStyle();
                HSSFCellStyle numberCellStyle = workbook.createCellStyle();
                HSSFCellStyle numberCellRightBorderStyle = workbook.createCellStyle();
                HSSFCellStyle percentCellStyle = workbook.createCellStyle();
                HSSFCellStyle percentCellRightBorderStyle = workbook.createCellStyle();
                HSSFCellStyle averageDoseCellStyle = workbook.createCellStyle();
                HSSFCellStyle averageDoseRightCellStyle = workbook.createCellStyle();
                
                populateWeekMonthSheet1CellStyle(top1Style, rsmTitleStyle, rsmTitleBorderStyle, rsmValueStyle, rsmValueBorderStyle
                		, top2Style, numberCellStyle, numberCellRightBorderStyle
                		, percentCellStyle, percentCellRightBorderStyle
                		, averageDoseCellStyle, averageDoseRightCellStyle);
                
                HSSFCellStyle month_week_top1Style=workbook.createCellStyle();
                HSSFCellStyle month_week_top2Style=workbook.createCellStyle();
                HSSFCellStyle month_week_top2LeftStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_valueLeftStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_valueRightStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_valueStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_valueBottomStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_valueBottomRightStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_top2RightStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_valueBottomLeftStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_numberBottomStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_numberBottomRightStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_percentBottomStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_percentBottomRightStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_averageDoseBottomStyle=workbook.createCellStyle();
                HSSFCellStyle month_week_averageDoseBottomRightStyle=workbook.createCellStyle();
                
                populateWeekMonthSheet2CellStyle(month_week_top1Style, month_week_top2Style
                		, month_week_top2LeftStyle, month_week_top2RightStyle
                		, month_week_valueLeftStyle, month_week_valueRightStyle, month_week_valueStyle, month_week_valueBottomStyle
                		, month_week_valueBottomRightStyle, month_week_valueBottomLeftStyle
                		, month_week_numberBottomStyle, month_week_numberBottomRightStyle
                		, month_week_percentBottomStyle, month_week_percentBottomRightStyle
                		, month_week_averageDoseBottomStyle, month_week_averageDoseBottomRightStyle);
                
                String childLevel = "";
                if( LsAttributes.USER_LEVEL_RSD.equalsIgnoreCase(selfLevel) ){
                	childLevel = LsAttributes.USER_LEVEL_RSM;
                }else if( LsAttributes.USER_LEVEL_RSM.equalsIgnoreCase(selfLevel) ){
                	childLevel = LsAttributes.USER_LEVEL_DSM;
                }
                
                int sheetNum = 0;
                
                List<RespirologyExportData> resExportData = respirologyService.getResMonthExportData(isRe2, selfLevel, false);
                List<RespirologyExportData> resExportDataIn2Months = respirologyService.getResMonthExportData(isRe2, selfLevel, true);
                keys = new ArrayList<String>(resExportData.get(0).getInRateMap().keySet());
                
                fileName = new StringBuffer("resMonthData/").append(systemTime).append("/").append("呼吸科上报数据汇总-")
                .append(selfLevel)
                .append("(")
                .append(keys.get(0)).append("到").append(lastWeekDuration)
                .append(").xls")
                .toString();
                
                File tmpFile = new File(request.getRealPath("/") + fileName);
                if( !tmpFile.exists() ){
                    tmpFile.createNewFile();
                }
                
                fOut = new FileOutputStream(tmpFile);
                
                workbook.createSheet("呼吸科周周报");
                HSSFSheet sheet = workbook.getSheetAt(sheetNum++);
                int currentRowNum = 1;
                
                //build the header
                HSSFRow row = sheet.createRow(currentRowNum++);
                
                if( null != resExportDataIn2Months && resExportDataIn2Months.size() > 0 ){
                	int columnCount = 1;
                	
                	currentRowNum = populateSheet1Title(row, sheet, resExportDataIn2Months, isRe2, currentRowNum, columnCount
                			, month_week_top1Style, month_week_top2LeftStyle, month_week_top2RightStyle, month_week_top2Style);
                	
                    sheet.setColumnWidth(0, 2*256);
                    for( int columnNum = 1; columnNum < columnCount; columnNum++ ){
                		sheet.setColumnWidth(columnNum, 16*256);
                	}
                    
                    int resExportDataCount = 0;
                    
                    populateSheet1Content(resExportDataIn2Months, row, sheet, currentRowNum, resExportDataCount, isRe2
                    		, month_week_valueBottomLeftStyle, month_week_valueLeftStyle, month_week_valueBottomRightStyle, month_week_valueRightStyle
                    		, month_week_numberBottomRightStyle, numberCellRightBorderStyle
                    		, month_week_percentBottomRightStyle, percentCellRightBorderStyle, month_week_percentBottomStyle, percentCellStyle
                    		, month_week_numberBottomStyle, numberCellStyle
                    		, month_week_averageDoseBottomRightStyle, averageDoseRightCellStyle
                    		, month_week_averageDoseBottomStyle, averageDoseCellStyle);
                }
                
                workbook.createSheet("分析总表");
                sheet = workbook.getSheetAt(sheetNum++);
                currentRowNum = 0;
                
                //build the header
                row = sheet.createRow(currentRowNum++);
                
                if( null != resExportData && resExportData.size() > 0 ){
                	populateSheet2Title(resExportData, isRe2, row, sheet, rsmTitleStyle, rsmTitleBorderStyle, month_week_top2Style, month_week_top1Style);
                	
                	populateSheet2Content(resExportData, row, sheet, currentRowNum
                			, rsmValueStyle, rsmValueBorderStyle, numberCellRightBorderStyle, percentCellRightBorderStyle, percentCellStyle
                			, numberCellStyle, averageDoseRightCellStyle, averageDoseCellStyle, isRe2);
                }
                
                /**
                 * 下级的周周报
                 */
                if( null != childLevel && !"".equals(childLevel) ){
                	resExportData = respirologyService.getResMonthExportData(isRe2, childLevel, false);
                    
                    workbook.createSheet("下级呼吸科周周报");
                    sheet = workbook.getSheetAt(sheetNum++);
                    currentRowNum = 1;
                    
                    //build the header
                    row = sheet.createRow(currentRowNum++);
                    
                    if( null != resExportData && resExportData.size() > 0 ){
                    	int columnCount = 1;
                    	
                    	currentRowNum = populateSheet1Title(row, sheet, resExportData, isRe2, currentRowNum, columnCount
                    			, month_week_top1Style, month_week_top2LeftStyle, month_week_top2RightStyle, month_week_top2Style);
                    	
                        sheet.setColumnWidth(0, 2*256);
                        for( int columnNum = 1; columnNum < columnCount; columnNum++ ){
                    		sheet.setColumnWidth(columnNum, 16*256);
                    	}
                        
                        int resExportDataCount = 0;
                        
                        populateSheet1Content(resExportData, row, sheet, currentRowNum, resExportDataCount, isRe2
                        		, month_week_valueBottomLeftStyle, month_week_valueLeftStyle, month_week_valueBottomRightStyle, month_week_valueRightStyle
                        		, month_week_numberBottomRightStyle, numberCellRightBorderStyle
                        		, month_week_percentBottomRightStyle, percentCellRightBorderStyle, month_week_percentBottomStyle, percentCellStyle
                        		, month_week_numberBottomStyle, numberCellStyle
                        		, month_week_averageDoseBottomRightStyle, averageDoseRightCellStyle
                        		, month_week_averageDoseBottomStyle, averageDoseCellStyle);
                    }
                    
                    workbook.createSheet("下级分析总表");
                    sheet = workbook.getSheetAt(sheetNum++);
                    currentRowNum = 0;
                    
                    //build the header
                    row = sheet.createRow(currentRowNum++);
                    
                    if( null != resExportData && resExportData.size() > 0 ){
                    	populateSheet2Title(resExportData, isRe2, row, sheet, rsmTitleStyle, rsmTitleBorderStyle, month_week_top2Style, month_week_top1Style);
                    	
                    	populateSheet2Content(resExportData, row, sheet, currentRowNum
                    			, rsmValueStyle, rsmValueBorderStyle, numberCellRightBorderStyle, percentCellRightBorderStyle, percentCellStyle
                    			, numberCellStyle, averageDoseRightCellStyle, averageDoseCellStyle, isRe2);
                    }
                }
                
                workbook.createSheet("上周呼吸科KPI医院原始数据");
                sheet = workbook.getSheetAt(sheetNum);
                currentRowNum = 0;
                Date lastWeekEndDate = DateUtils.getGenerateWeeklyReportDate();
                Date lastWeekStartDate = new Date(lastWeekEndDate.getTime() - 6*24*60*60*1000);
                
                List<RespirologyData> dbResData = respirologyService.getRespirologyDataByDate(lastWeekStartDate,lastWeekEndDate);
                
                //build the header
                row = sheet.createRow(currentRowNum++);
                populateSheet3Content(row, isRe2, dbResData, sheet, currentRowNum);
                
                workbook.write(fOut);
        }catch(Exception e){
            logger.error("fail to export the res month data file,",e);
        }finally{
            if( fOut != null ){
                fOut.close();
            }
        }
        
        if( "1".equals(isRe2) && !LsAttributes.USER_LEVEL_DSM.equalsIgnoreCase(selfLevel) ){
        	try{
        		String pdfFileName = "";
        		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
            	String localPath = request.getRealPath("/");
            	StringBuffer remoteReportFile = new StringBuffer(basePath);
            	StringBuffer localReportFile = new StringBuffer(localPath);
            	long systemTime = System.currentTimeMillis();
            	
            	String folderName = new StringBuffer("re2Report/").append(systemTime).append("/").toString();
            	
            	String fileFullName = new StringBuffer("RE2医院呼吸科周周报-")
            							.append(selfLevel)
						                .append("(")
						                .append(keys.get(0)).append("到").append(lastWeekDuration)
						            	.append(").pdf").toString();
            	
            	checkAndCreateFileFolder(basePath+folderName);
            	
            	remoteReportFile.append(folderName).append(fileFullName);
            	
            	localReportFile.append(folderName).append(fileFullName);
            	
            	File reportFile = new File(localReportFile.toString());
            	
            	if( !reportFile.exists() ){
            		BirtReportUtils html = new BirtReportUtils();
            		logger.info("begin to generate RE2 report");
            		html.startPlatform();
            		createRe2Report(html, localPath, systemTime, selfLevel, folderName+fileFullName);
            		html.stopPlatform();
            		logger.info("end to generate RE2 report");
            		
            		if( !reportFile.exists() ){
            			logger.error("fail to generate the RE2 report to export, no file is found");
            		}else{
            			ReportFileObject rfo = new ReportFileObject();
            			rfo.setFileName(remoteReportFile.toString().substring(remoteReportFile.toString().lastIndexOf("/")+1));
            			rfo.setFilePath(remoteReportFile.toString());
            			reportFiles.add(rfo);
            		}
            	}
        	}catch(Exception e){
        		logger.error("re2 周周报 pdf生成失败",e);
        	}
        }
        
        ReportFileObject rfo = new ReportFileObject();
        rfo.setFileName(fileName.substring(fileName.lastIndexOf("/")+1));
        rfo.setFilePath(fileName);
        reportFiles.add(rfo);
        
        if( "1".equals(isRe2) ){
        	request.getSession().setAttribute("re2ReportFiles", reportFiles);
        }else{
        	request.getSession().setAttribute("resReportFiles", reportFiles);
        }
        if( null != fromWeb && "Y".equalsIgnoreCase(fromWeb) ){
            return "redirect:showWebUploadData";
        }else{
            return "redirect:showUploadData";
        }
    }
    

    private int populateSheet1Title(HSSFRow row, HSSFSheet sheet, List<RespirologyExportData> resExportData, String isRe2
    		,int currentRowNum, int columnCount
    		,HSSFCellStyle month_week_top1Style, HSSFCellStyle month_week_top2LeftStyle, HSSFCellStyle month_week_top2RightStyle
    		,HSSFCellStyle month_week_top2Style){
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
        
        Map<String, Double> averageDoseMap = resData.getAverageDoseMap();
        Iterator<String> averageDoseIte = averageDoseMap.keySet().iterator();
        
        Map<String, Double> currentWeekAENumMap = resData.getCurrentWeekAENum();
        Iterator<String> currentWeekAENumIte = currentWeekAENumMap.keySet().iterator();
        
        Map<String, Double> currentWeekLsAERateMap = resData.getCurrentWeekLsAERate();
        Iterator<String> currentWeekLsAERateIte = currentWeekLsAERateMap.keySet().iterator();
        
    	Map<String, Double> aePNumMap = resData.getAePNumMap();
    	Iterator<String> aePNumIte = null;
    	if( "1".equals(isRe2) ){
    		aePNumIte = aePNumMap.keySet().iterator();
    	}

    	Map<String, Double> xbkAeNumMap = resData.getXbkAeNumMap();
    	Iterator<String> xbkAeNumIte = null;
    	if( "1".equals(isRe2) ){
    		xbkAeNumIte = xbkAeNumMap.keySet().iterator();
    	}
        
        int i = 0;
        int inRateStartCount = columnCount;
        while( inRateIte.hasNext() ){
            inRateIte.next();
            HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
            titleCell.setCellValue("上报率");
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
            titleCell.setCellValue("雾化令舒人数(周平均)");
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
            titleCell.setCellValue("雾化率");
            titleCell.setCellStyle(month_week_top1Style);
            i++;
        }
        columnCount += whRateMap.size();
        
        sheet.addMergedRegion(new Region(1, (short)whRateStartCount, 1, (short)(columnCount-1)));
        row.getCell(whRateStartCount).setCellStyle(month_week_top1Style);
        
        i = 0;
        int averageDoseStartCount = columnCount;
        while( averageDoseIte.hasNext() ){
        	averageDoseIte.next();
            HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
            titleCell.setCellValue("平均剂量");
            titleCell.setCellStyle(month_week_top1Style);
            i++;
        }
        columnCount += averageDoseMap.size();
        
        sheet.addMergedRegion(new Region(1, (short)averageDoseStartCount, 1, (short)(columnCount-1)));
        row.getCell(averageDoseStartCount).setCellStyle(month_week_top1Style);
        
        if( "1".equals(isRe2) ){
        	i = 0;
        	int aePNumStartCount = columnCount;
        	while( aePNumIte.hasNext() ){
        		aePNumIte.next();
        		HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
        		titleCell.setCellValue("AE人数/住院人数");
        		titleCell.setCellStyle(month_week_top1Style);
        		i++;
        	}
        	columnCount += aePNumMap.size();
        	
        	sheet.addMergedRegion(new Region(1, (short)aePNumStartCount, 1, (short)(columnCount-1)));
        	row.getCell(aePNumStartCount).setCellStyle(month_week_top1Style);
        	
        	i = 0;
        	int xbkAeNumStartCount = columnCount;
        	while( xbkAeNumIte.hasNext() ){
        		xbkAeNumIte.next();
        		HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
        		titleCell.setCellValue("信必可带药率(信必可带药人数/AECOPD人数)");
        		titleCell.setCellStyle(month_week_top1Style);
        		i++;
        	}
        	columnCount += xbkAeNumMap.size();
        	
        	sheet.addMergedRegion(new Region(1, (short)xbkAeNumStartCount, 1, (short)(columnCount-1)));
        	row.getCell(xbkAeNumStartCount).setCellStyle(month_week_top1Style);
        }
        
        int lsAERateStartCount = columnCount;
        HSSFCell lsAERateTitleCell = row.createCell(columnCount, XSSFCell.CELL_TYPE_STRING);
        lsAERateTitleCell.setCellValue("当周雾化令舒人数/AE人数");
        lsAERateTitleCell.setCellStyle(month_week_top1Style);
        columnCount++;
        
        HSSFCell lsAERateTitle2Cell = row.createCell(columnCount, XSSFCell.CELL_TYPE_STRING);
        lsAERateTitle2Cell.setCellValue("当周雾化令舒人数/AE人数");
        lsAERateTitle2Cell.setCellStyle(month_week_top1Style);
        columnCount++;
        
        sheet.addMergedRegion(new Region(1, (short)lsAERateStartCount, 1, (short)(columnCount-1)));
        row.getCell(lsAERateStartCount).setCellStyle(month_week_top1Style);
        
        if( null != whDaysMap && whDaysMap.size() > 0 ){
            i = 0;
            int daysStartCount = columnCount;
            Iterator<String> daysIte = whDaysMap.keySet().iterator();
            while( daysIte.hasNext() ){
                daysIte.next();
                HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
                titleCell.setCellValue("雾化天数");
                titleCell.setCellStyle(month_week_top1Style);
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
        rsmNameTitleCell.setCellValue("姓名");
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
        averageDoseIte = averageDoseMap.keySet().iterator();
        while( averageDoseIte.hasNext() ){
            String monthName = averageDoseIte.next();
            HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
            titleCell.setCellValue(monthName);
            if( i == averageDoseMap.size()-1 ){
            	titleCell.setCellStyle(month_week_top2RightStyle);
            }else{
            	titleCell.setCellStyle(month_week_top2Style);
            }
            i++;
        }
        columnCount += averageDoseMap.size();
        
        if( "1".equals(isRe2) ){
        	i = 0;
        	aePNumIte = aePNumMap.keySet().iterator();
        	while( aePNumIte.hasNext() ){
        		String monthName = aePNumIte.next();
        		HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
        		titleCell.setCellValue(monthName);
        		if( i == aePNumMap.size()-1 ){
        			titleCell.setCellStyle(month_week_top2RightStyle);
        		}else{
        			titleCell.setCellStyle(month_week_top2Style);
        		}
        		i++;
        	}
        	columnCount += aePNumMap.size();
        	
        	i = 0;
        	xbkAeNumIte = xbkAeNumMap.keySet().iterator();
        	while( xbkAeNumIte.hasNext() ){
        		String monthName = xbkAeNumIte.next();
        		HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
        		titleCell.setCellValue(monthName);
        		if( i == xbkAeNumMap.size()-1 ){
        			titleCell.setCellStyle(month_week_top2RightStyle);
        		}else{
        			titleCell.setCellStyle(month_week_top2Style);
        		}
        		i++;
        	}
        	columnCount += xbkAeNumMap.size();
        }
        
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
        
        return currentRowNum;
    }
    
    private void populateSheet1Content(List<RespirologyExportData> resExportData, HSSFRow row, HSSFSheet sheet, int currentRowNum, int resExportDataCount
    		,String isRe2
    		,HSSFCellStyle month_week_valueBottomLeftStyle,HSSFCellStyle month_week_valueLeftStyle
    		,HSSFCellStyle month_week_valueBottomRightStyle,HSSFCellStyle month_week_valueRightStyle
    		,HSSFCellStyle month_week_numberBottomRightStyle,HSSFCellStyle numberCellRightBorderStyle
    		,HSSFCellStyle month_week_percentBottomRightStyle,HSSFCellStyle percentCellRightBorderStyle
    		,HSSFCellStyle month_week_percentBottomStyle,HSSFCellStyle percentCellStyle
    		,HSSFCellStyle month_week_numberBottomStyle,HSSFCellStyle numberCellStyle
    		,HSSFCellStyle month_week_averageDoseBottomRightStyle,HSSFCellStyle averageDoseRightCellStyle
    		,HSSFCellStyle month_week_averageDoseBottomStyle,HSSFCellStyle averageDoseCellStyle
    		){
    	int columnCount = 1;
    	
    	Map<String, Double> pNumMap = null;
    	Iterator<String> pNumIte = null;
    	
    	Map<String, Double> lsNumMap = null;
    	Iterator<String> lsNumIte = null;
    	
    	Map<String, Double> aeNumMap = null;
    	Iterator<String> aeNumIte = null;
    	
    	Map<String, Double> inRateMap = null;
    	Iterator<String> inRateIte = null;
    	
    	Map<String, Double> whRateMap = null;
    	Iterator<String> whRateIte = null;
    	
    	Map<String, Double> averageDoseMap = null;
    	Iterator<String> averageDoseIte = null;
    	
    	Map<String, Double> whDaysMap = null;
    	Iterator<String> whDaysIte = null;
    	
    	Map<String, Double> dValueMap = null;
    	Iterator<String> dValueIte = null;
    	
		Map<String, Double> aePNumMap = null;
		Iterator<String> aePNumIte = null;
		
		Map<String, Double> xbkAeNumMap = null;
		Iterator<String> xbkAeNumIte = null;
		
		Map<String, Double> currentWeekAENumMap = null;
		Iterator<String> currentWeekAENumIte = null;
		
		Map<String, Double> currentWeekLsAERateMap = null;
		Iterator<String> currentWeekLsAERateIte = null;
		
    	for( RespirologyExportData res : resExportData ){
            
        	if( !"N/A".equals(res.getRsmName()) && 0 != res.getHosNum() ){
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
                
                averageDoseMap = res.getAverageDoseMap();
                averageDoseIte = averageDoseMap.keySet().iterator();
                
                if( "1".equals(isRe2) ){
                	aePNumMap = res.getAePNumMap();
                	aePNumIte = aePNumMap.keySet().iterator();
                	
                	xbkAeNumMap = res.getXbkAeNumMap();
                	xbkAeNumIte = xbkAeNumMap.keySet().iterator();
                }
                
                whDaysMap = res.getWhDaysMap();
                whDaysIte = whDaysMap.keySet().iterator();
                
                inRateMap = res.getInRateMap();
                inRateIte = inRateMap.keySet().iterator();
                
                currentWeekAENumMap = res.getCurrentWeekAENum();
            	currentWeekAENumIte = currentWeekAENumMap.keySet().iterator();
                
                currentWeekLsAERateMap = res.getCurrentWeekLsAERate();
            	currentWeekLsAERateIte = currentWeekLsAERateMap.keySet().iterator();
                
                int i = 0;
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
                        	lsNumValueCell.setCellStyle(numberCellRightBorderStyle);
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
                while( averageDoseIte.hasNext() ){
                    columnName = averageDoseIte.next();
                    HSSFCell valueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                    valueCell.setCellValue(averageDoseMap.get(columnName));
                    if( i == averageDoseMap.size() - 1 ){
                        if( resExportDataCount == resExportData.size()-1 ){
                        	valueCell.setCellStyle(month_week_averageDoseBottomRightStyle);
                        }else{
                        	valueCell.setCellStyle(averageDoseRightCellStyle);
                        }
                    }else{
                        if( resExportDataCount == resExportData.size()-1 ){
                        	valueCell.setCellStyle(month_week_averageDoseBottomStyle);
                        }else{
                        	valueCell.setCellStyle(averageDoseCellStyle);
                        }
                    }
                    i++;
                }
                columnCount += averageDoseMap.size();
                
                if( "1".equals(isRe2) ){
                	i = 0;
                	while( aePNumIte.hasNext() ){
                		columnName = aePNumIte.next();
                		HSSFCell valueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                		valueCell.setCellValue(aePNumMap.get(columnName));
                		if( i == aePNumMap.size() -1 ){
                			if( resExportDataCount == resExportData.size()-1 ){
                				valueCell.setCellStyle(month_week_percentBottomRightStyle);
                			}else{
                				valueCell.setCellStyle(percentCellRightBorderStyle);
                			}
                		}else{
                			if( resExportDataCount == resExportData.size()-1 ){
                				valueCell.setCellStyle(month_week_percentBottomStyle);
                			}else{
                				valueCell.setCellStyle(percentCellStyle);
                			}
                		}
                		i++;
                	}
                	columnCount += aePNumMap.size();
                	
                	i = 0;
                	while( xbkAeNumIte.hasNext() ){
                		columnName = xbkAeNumIte.next();
                		HSSFCell valueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
                		valueCell.setCellValue(xbkAeNumMap.get(columnName));
                		if( i == xbkAeNumMap.size() -1 ){
                			if( resExportDataCount == resExportData.size()-1 ){
                				valueCell.setCellStyle(month_week_percentBottomRightStyle);
                			}else{
                				valueCell.setCellStyle(percentCellRightBorderStyle);
                			}
                		}else{
                			if( resExportDataCount == resExportData.size()-1 ){
                				valueCell.setCellStyle(month_week_percentBottomStyle);
                			}else{
                				valueCell.setCellStyle(percentCellStyle);
                			}
                		}
                		i++;
                	}
                	columnCount += xbkAeNumMap.size();
                }
                
                if( null != currentWeekAENumIte ){
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
                }
                
                if( null != currentWeekLsAERateIte ){
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
                }
                
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
            }
            resExportDataCount++;
        }
    }
    
    private void populateSheet2Title(List<RespirologyExportData> resExportData,String isRe2,HSSFRow row, HSSFSheet sheet
    		,HSSFCellStyle rsmTitleStyle,HSSFCellStyle rsmTitleBorderStyle,HSSFCellStyle top2Style,HSSFCellStyle top1Style){
    	int columnCount = 0;
    	int i = 0;
    	
    	RespirologyExportData resData = resExportData.get(0);
    	
    	Map<String, Double> pNumMap = resData.getpNumMap();
    	Iterator<String> pNumIte = null;
    	
    	Map<String, Double> lsNumMap = resData.getLsNumMap();
    	Iterator<String> lsNumIte = null;
    	
    	Map<String, Double> aeNumMap = resData.getAeNumMap();
    	Iterator<String> aeNumIte = null;
    	
    	Map<String, Double> inRateMap = resData.getInRateMap();
    	Iterator<String> inRateIte = null;
    	
    	Map<String, Double> whRateMap = resData.getWhRateMap();
    	Iterator<String> whRateIte = null;
    	
    	Map<String, Double> averageDoseMap = resData.getAverageDoseMap();
    	Iterator<String> averageDoseIte = null;
    	
    	Map<String, Double> whDaysMap = resData.getWhDaysMap();
    	Iterator<String> whDaysIte = null;
    	
    	Map<String, Double> dValueMap = resData.getdValueMap();
    	Iterator<String> dValueIte = null;
    	
		Map<String, Double> aePNumMap = resData.getAePNumMap();
		Iterator<String> aePNumIte = null;
		
		Map<String, Double> xbkAeNumMap = resData.getXbkAeNumMap();
		Iterator<String> xbkAeNumIte = null;
    	
    	HSSFCell rsmRegionTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    	rsmRegionTitleCell.setCellValue("区域");
    	rsmRegionTitleCell.setCellStyle(rsmTitleStyle);
    	
    	HSSFCell rsmNameTitleCell = row.createCell(columnCount++, XSSFCell.CELL_TYPE_STRING);
    	rsmNameTitleCell.setCellValue("姓名");
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
    	
    	if( "1".equals(isRe2) ){
    		i = 0;
    		aePNumIte = aePNumMap.keySet().iterator();
    		while( aePNumIte.hasNext() ){
    			String monthName = aePNumIte.next();
    			HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
    			titleCell.setCellValue(monthName+"AE人数/住院人数");
    			if( i == aePNumMap.size()-1 ){
    				titleCell.setCellStyle(top2Style);
    			}else{
    				titleCell.setCellStyle(top1Style);
    			}
    			i++;
    		}
    		columnCount += aePNumMap.size();
    		
    		i = 0;
    		xbkAeNumIte = xbkAeNumMap.keySet().iterator();
    		while( xbkAeNumIte.hasNext() ){
    			String monthName = xbkAeNumIte.next();
    			HSSFCell titleCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_STRING);
    			titleCell.setCellValue(monthName+"信必可带药率");
    			if( i == xbkAeNumMap.size()-1 ){
    				titleCell.setCellStyle(top2Style);
    			}else{
    				titleCell.setCellStyle(top1Style);
    			}
    			i++;
    		}
    		columnCount += xbkAeNumMap.size();
    	}
    	
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
    }
    
    private void populateSheet2Content(List<RespirologyExportData> resExportData, HSSFRow row, HSSFSheet sheet, int currentRowNum
    		,HSSFCellStyle rsmValueStyle,HSSFCellStyle rsmValueBorderStyle,HSSFCellStyle numberCellRightBorderStyle
    		,HSSFCellStyle percentCellRightBorderStyle,HSSFCellStyle percentCellStyle,HSSFCellStyle numberCellStyle
    		,HSSFCellStyle averageDoseRightCellStyle,HSSFCellStyle averageDoseCellStyle
    		,String isRe2){
    	
    	Map<String, Double> pNumMap = null;
    	Iterator<String> pNumIte = null;
    	Map<String, Double> lsNumMap = null;
    	Iterator<String> lsNumIte = null;
    	Map<String, Double> aeNumMap = null;
    	Iterator<String> aeNumIte = null;
    	Map<String, Double> inRateMap = null;
    	Iterator<String> inRateIte = null;
    	Map<String, Double> whRateMap = null;
    	Iterator<String> whRateIte = null;
    	Map<String, Double> averageDoseMap = null;
    	Iterator<String> averageDoseIte = null;
    	Map<String, Double> whDaysMap = null;
    	Iterator<String> whDaysIte = null;
    	Map<String, Double> dValueMap = null;
    	Iterator<String> dValueIte = null;
    	Map<String, Double> aePNumMap = null;
    	Iterator<String> aePNumIte = null;
    	Map<String, Double> xbkAeNumMap = null;
    	Iterator<String> xbkAeNumIte = null;
    	
    	int columnCount = 0;
    	for( RespirologyExportData res : resExportData ){
    		if( !"N/A".equals(res.getRsmName()) && 0 != res.getHosNum() ){
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
    			
    			if( "1".equals(isRe2) ){
    				aePNumMap = res.getAePNumMap();
    				aePNumIte = aePNumMap.keySet().iterator();
    				
    				xbkAeNumMap = res.getXbkAeNumMap();
    				xbkAeNumIte = xbkAeNumMap.keySet().iterator();
    			}
    			
    			int i = 0;
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

    			if( "1".equals(isRe2) ){
    				i = 0;
    				while( aePNumIte.hasNext() ){
    					columnName = aePNumIte.next();
    					HSSFCell aePNumValueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
    					aePNumValueCell.setCellValue(aePNumMap.get(columnName));
    					if( i == aePNumMap.size()-1 ){
    						aePNumValueCell.setCellStyle(percentCellRightBorderStyle);
    					}else{
    						aePNumValueCell.setCellStyle(percentCellStyle);
    					}
    					i++;
    				}
    				columnCount += aePNumMap.size();
    				
    				i = 0;
    				while( xbkAeNumIte.hasNext() ){
    					columnName = xbkAeNumIte.next();
    					HSSFCell valueCell = row.createCell(columnCount+i, XSSFCell.CELL_TYPE_NUMERIC);
    					valueCell.setCellValue(xbkAeNumMap.get(columnName));
    					if( i == xbkAeNumMap.size()-1 ){
    						valueCell.setCellStyle(percentCellRightBorderStyle);
    					}else{
    						valueCell.setCellStyle(percentCellStyle);
    					}
    					i++;
    				}
    				columnCount += xbkAeNumMap.size();
    			}
    			
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
    }
    
    private void populateSheet3Content(HSSFRow row, String isRe2, List<RespirologyData> dbResData, HSSFSheet sheet, int currentRowNum){
    	SimpleDateFormat exportdateformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	
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
        if( "0".equals(isRe2) ){
        	row.createCell(20, XSSFCell.CELL_TYPE_STRING).setCellValue("是否为KPI医院（在=1，不在=0）");
        }else if( "1".equals(isRe2) ){
        	row.createCell(20, XSSFCell.CELL_TYPE_STRING).setCellValue("出院信必可带药人数");
        	row.createCell(21, XSSFCell.CELL_TYPE_STRING).setCellValue("处方1支人数");
        	row.createCell(22, XSSFCell.CELL_TYPE_STRING).setCellValue("处方2支人数");
        	row.createCell(23, XSSFCell.CELL_TYPE_STRING).setCellValue("处方3支及以上人数");
        	row.createCell(24, XSSFCell.CELL_TYPE_STRING).setCellValue("是否为KPI医院（在=1，不在=0）");
        }
        
        for( RespirologyData resData : dbResData ){
            if( "0".equals(isRe2) && "1".equalsIgnoreCase(resData.getIsResAssessed()) ){
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
            
            if( "1".equals(isRe2) && "1".equalsIgnoreCase(resData.getIsRe2()) ){
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
                row.createCell(20, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getXbknum());
            	row.createCell(21, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getXbk1num());
            	row.createCell(22, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getXbk2num());
            	row.createCell(23, XSSFCell.CELL_TYPE_NUMERIC).setCellValue(resData.getXbk3num());
            	row.createCell(24, XSSFCell.CELL_TYPE_STRING).setCellValue(resData.getIsRe2());
            }
        }
    }
    
    private void populateWeekMonthSheet1CellStyle(HSSFCellStyle top1Style,HSSFCellStyle rsmTitleStyle,HSSFCellStyle rsmTitleBorderStyle
    		,HSSFCellStyle rsmValueStyle,HSSFCellStyle rsmValueBorderStyle,HSSFCellStyle top2Style,HSSFCellStyle numberCellStyle
    		,HSSFCellStyle numberCellRightBorderStyle,HSSFCellStyle percentCellStyle,HSSFCellStyle percentCellRightBorderStyle
    		,HSSFCellStyle averageDoseCellStyle,HSSFCellStyle averageDoseRightCellStyle){
        top1Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        top1Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        top1Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        top1Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        top1Style.setLeftBorderColor(HSSFColor.BLACK.index);
        top1Style.setRightBorderColor(HSSFColor.BLACK.index);
        top1Style.setWrapText(true);
        
        rsmTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        rsmTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        rsmTitleStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        rsmTitleStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        rsmTitleStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        rsmTitleStyle.setRightBorderColor(HSSFColor.BLACK.index);
        
        rsmTitleBorderStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        rsmTitleBorderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        rsmTitleBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        rsmTitleBorderStyle.setRightBorderColor(HSSFColor.BLACK.index);
        
        rsmValueStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        rsmValueStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        rsmValueStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        rsmValueStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        rsmValueStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        rsmValueStyle.setRightBorderColor(HSSFColor.BLACK.index);
        
        rsmValueBorderStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        rsmValueBorderStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        rsmValueBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        rsmValueBorderStyle.setRightBorderColor(HSSFColor.BLACK.index);
        
        top2Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        top2Style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        top2Style.setRightBorderColor(HSSFColor.BLACK.index);
        top2Style.setWrapText(true);
        
        numberCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        numberCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        numberCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
        numberCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        numberCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        numberCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        numberCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        
        numberCellRightBorderStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        numberCellRightBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        numberCellRightBorderStyle.setRightBorderColor(HSSFColor.BLACK.index);
        numberCellRightBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        numberCellRightBorderStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        
        percentCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
        percentCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        percentCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        percentCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        percentCellStyle.setRightBorderColor(HSSFColor.BLACK.index);

        percentCellRightBorderStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
        percentCellRightBorderStyle.setRightBorderColor(HSSFColor.BLACK.index);
        percentCellRightBorderStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        percentCellRightBorderStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        percentCellRightBorderStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        
        averageDoseCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        averageDoseCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        averageDoseCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        averageDoseCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        averageDoseCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
        
        averageDoseRightCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        averageDoseRightCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
        averageDoseRightCellStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        averageDoseRightCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        averageDoseRightCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
    }
    
    private void populateWeekMonthSheet2CellStyle(HSSFCellStyle top1Style, HSSFCellStyle top2Style,HSSFCellStyle top2LeftStyle,HSSFCellStyle top2RightStyle
    		,HSSFCellStyle valueLeftStyle,HSSFCellStyle valueRightStyle,HSSFCellStyle valueStyle
    		,HSSFCellStyle valueBottomStyle, HSSFCellStyle valueBottomRightStyle,HSSFCellStyle valueBottomLeftStyle
    		,HSSFCellStyle numberBottomStyle,HSSFCellStyle numberBottomRightStyle,HSSFCellStyle percentBottomStyle
    		,HSSFCellStyle percentBottomRightStyle,HSSFCellStyle averageDoseBottomStyle,HSSFCellStyle averageDoseBottomRightStyle){
    	top1Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        top1Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        top1Style.setBorderTop(HSSFCellStyle.BORDER_THICK);
        top1Style.setTopBorderColor(HSSFColor.BLACK.index);
        top1Style.setBorderRight(HSSFCellStyle.BORDER_THICK);
        top1Style.setRightBorderColor(HSSFColor.BLACK.index);
        top1Style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        top1Style.setBottomBorderColor(HSSFColor.BLACK.index);
        top1Style.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        top1Style.setLeftBorderColor(HSSFColor.BLACK.index);
        top1Style.setWrapText(true);
        
        top2Style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        top2Style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        top2Style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        top2Style.setRightBorderColor(HSSFColor.BLACK.index);
        top2Style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        top2Style.setBottomBorderColor(HSSFColor.BLACK.index);
        top2Style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        top2Style.setLeftBorderColor(HSSFColor.BLACK.index);
        top2Style.setWrapText(true);
        
        top2LeftStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        top2LeftStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        top2LeftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        top2LeftStyle.setRightBorderColor(HSSFColor.BLACK.index);
        top2LeftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        top2LeftStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        top2LeftStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        top2LeftStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        top2LeftStyle.setWrapText(true);
        
        top2RightStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        top2RightStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        top2RightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        top2RightStyle.setRightBorderColor(HSSFColor.BLACK.index);
        top2RightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        top2RightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        top2RightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        top2RightStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        top2RightStyle.setWrapText(true);
        
        valueLeftStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        valueLeftStyle.setTopBorderColor(HSSFColor.BLACK.index);
        valueLeftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        valueLeftStyle.setRightBorderColor(HSSFColor.BLACK.index);
        valueLeftStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        valueLeftStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        valueLeftStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        valueLeftStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        
        valueRightStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        valueRightStyle.setTopBorderColor(HSSFColor.BLACK.index);
        valueRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        valueRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
        valueRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        valueRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        valueRightStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        valueRightStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        
        valueStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        valueStyle.setTopBorderColor(HSSFColor.BLACK.index);
        valueStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        valueStyle.setRightBorderColor(HSSFColor.BLACK.index);
        valueStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        valueStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        valueStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        valueStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        
        valueBottomStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        valueBottomStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        valueBottomStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        valueBottomStyle.setRightBorderColor(HSSFColor.BLACK.index);
        
        valueBottomRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        valueBottomRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        valueBottomRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        valueBottomRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
        
        valueBottomLeftStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        valueBottomLeftStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        valueBottomLeftStyle.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        valueBottomLeftStyle.setLeftBorderColor(HSSFColor.BLACK.index);
        valueBottomLeftStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        valueBottomLeftStyle.setRightBorderColor(HSSFColor.BLACK.index);
        
        numberBottomStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        numberBottomStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        numberBottomStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        numberBottomStyle.setRightBorderColor(HSSFColor.BLACK.index);
        numberBottomStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        
        numberBottomRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        numberBottomRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        numberBottomRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        numberBottomRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
        numberBottomRightStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
        
        percentBottomStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        percentBottomStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        percentBottomStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        percentBottomStyle.setRightBorderColor(HSSFColor.BLACK.index);
        percentBottomStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
        
        percentBottomRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        percentBottomRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        percentBottomRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        percentBottomRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
        percentBottomRightStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0%"));
        
        averageDoseBottomStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        averageDoseBottomStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        averageDoseBottomStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
        
        averageDoseBottomRightStyle.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        averageDoseBottomRightStyle.setBottomBorderColor(HSSFColor.BLACK.index);
        averageDoseBottomRightStyle.setBorderRight(HSSFCellStyle.BORDER_THICK);
        averageDoseBottomRightStyle.setRightBorderColor(HSSFColor.BLACK.index);
        averageDoseBottomRightStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
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
