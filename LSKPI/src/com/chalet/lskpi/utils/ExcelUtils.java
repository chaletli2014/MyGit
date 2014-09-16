package com.chalet.lskpi.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.DDIData;
import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.HospitalUserRefer;
import com.chalet.lskpi.model.MonthlyData;
import com.chalet.lskpi.model.PediatricsData;
import com.chalet.lskpi.model.Property;
import com.chalet.lskpi.model.RespirologyData;
import com.chalet.lskpi.model.UserCode;
import com.chalet.lskpi.model.UserInfo;

public class ExcelUtils {
    
    private static Logger logger = Logger.getLogger(ExcelUtils.class);
    
    private static String populateRecipeTypeValue(String recipeType){
        String returnValue = recipeType;
        if( null != recipeType ){
            if( "一次门急诊处方1天的雾化量".equalsIgnoreCase(recipeType) ){
                returnValue = "1";
            }else if( "一次门急诊处方2天的雾化量".equalsIgnoreCase(recipeType) ){
                returnValue = "2";
            }else if( "一次门急诊处方3天的雾化量".equalsIgnoreCase(recipeType) ){
                returnValue = "3";
            }else if( "一次门急诊处方4天的雾化量".equalsIgnoreCase(recipeType) ){
                returnValue = "4";
            }else if( "一次门急诊处方5天的雾化量".equalsIgnoreCase(recipeType) ){
                returnValue = "5";
            }else if( "一次门急诊处方6天的雾化量".equalsIgnoreCase(recipeType) ){
                returnValue = "6";
            }else if( "一次门急诊处方7天的雾化量".equalsIgnoreCase(recipeType) ){
                returnValue = "7";
            }
         }
        return returnValue;
    }
    

    public static List<Hospital> getHospitalsFromFile(String filePath,List<String> headers) throws Exception{
        List<Hospital> hospitals = new ArrayList<Hospital>();
        FileInputStream is = null;
        try{
            is = new FileInputStream(filePath);
            Workbook book = createCommonWorkbook(is);
            Sheet sheet = book.getSheetAt(0);
            
            int header_count = 0;
            Map<String, Integer> headerColumn = new HashMap<String, Integer>();
            
            Row row = sheet.getRow(sheet.getFirstRowNum());
            for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
                if( headers.contains(row.getCell(i).toString()) ){
                    headerColumn.put(row.getCell(i).toString(), i);
                    header_count++;
                }
            }
            System.out.println("header_count is " + header_count + ", but " + headers.size() + " is needed");
            if( header_count != headers.size() ){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            
            //get the data
            for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
                row = sheet.getRow(i);
                String codeCell = row.getCell(headerColumn.get(headers.get(0))).toString();
                String nameCell = row.getCell(headerColumn.get(headers.get(1))).toString();
                String provinceCell = row.getCell(headerColumn.get(headers.get(2))).toString();
                String cityCell = row.getCell(headerColumn.get(headers.get(3))).toString();
                
                Cell dsmCodeCell = row.getCell(headerColumn.get(headers.get(4)));
                dsmCodeCell.setCellType(Cell.CELL_TYPE_STRING);
                String dsmCode = dsmCodeCell.toString();
                
                String dsmNameCell = row.getCell(headerColumn.get(headers.get(5))).toString();
                String rsmRegionCell = row.getCell(headerColumn.get(headers.get(6))).toString();
                String saleNameCell = row.getCell(headerColumn.get(headers.get(7))).toString();
                String regionCell = row.getCell(headerColumn.get(headers.get(8))).toString();
                
                
                if( null != nameCell && !"".equalsIgnoreCase(nameCell) ){
                    Hospital hospital = new Hospital();
                    hospital.setCode(codeCell);
                    hospital.setName(nameCell);
                    hospital.setProvince(provinceCell);
                    hospital.setCity(cityCell);
                    hospital.setDsmCode(dsmCode);
                    hospital.setDsmName(dsmNameCell);
                    hospital.setRsmRegion(rsmRegionCell);
                    hospital.setSaleName(saleNameCell);
                    hospital.setRegion(regionCell);
                    
                    hospitals.add(hospital);
                }
            }
            
        }catch(Exception e){
            logger.error("fail to get hospitals from the excel file.",e);
            throw new Exception(e.getMessage());
        }finally{
            if(null!=is){
                is.close();
            }
        }
        
        return hospitals;
    }
    
    public static List<UserInfo> getUserInfosFromFile(String filePath,List<String> headers) throws Exception{
    	List<UserInfo> userInfos = new ArrayList<UserInfo>();
    	FileInputStream is = null;
    	try{
    	    is = new FileInputStream(filePath);
    		Workbook book = createCommonWorkbook(is);
    		Sheet sheet = book.getSheetAt(0);
    		
    		int header_count = 0;
    		Map<String, Integer> headerColumn = new HashMap<String, Integer>();
    		
    		Row row = sheet.getRow(sheet.getFirstRowNum());
    		for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
    			if( headers.contains(row.getCell(i).toString()) ){
    				headerColumn.put(row.getCell(i).toString(), i);
    				header_count++;
    			}
    		}
    		logger.info("header_count is " + header_count);
    		if( header_count != headers.size() ){
    			throw new Exception("文件格式不正确，无法导入数据");
    		}
    		//get the data
    		for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
    			row = sheet.getRow(i);
    			
    			Cell userCodeCell = row.getCell(headerColumn.get(headers.get(0)));
    			userCodeCell.setCellType(Cell.CELL_TYPE_STRING);
    			String userCode = userCodeCell.toString();
    			
    			String nameCell = row.getCell(headerColumn.get(headers.get(1))).toString();
    			String buCell = row.getCell(headerColumn.get(headers.get(2))).toString();
    			String regionCenterCell = row.getCell(headerColumn.get(headers.get(3))).toString();
    			String regionCell = row.getCell(headerColumn.get(headers.get(4))).toString();
    			String teamCodeCell = row.getCell(headerColumn.get(headers.get(5))).toString();
    			String teamCell = row.getCell(headerColumn.get(headers.get(6))).toString();
    			String levelCell = row.getCell(headerColumn.get(headers.get(7))).toString();
    			
    			Cell telCell = row.getCell(headerColumn.get(headers.get(8)));
    			telCell.setCellType(Cell.CELL_TYPE_STRING);
    			String tel = telCell.toString();
    			
    			if( null != nameCell && !"".equalsIgnoreCase(nameCell) ){
    				UserInfo userInfo = new UserInfo();
    				userInfo.setUserCode(userCode);
    				userInfo.setName(nameCell);
    				userInfo.setBu(buCell);
    				userInfo.setRegionCenter(regionCenterCell);
    				userInfo.setRegion(regionCell);
    				userInfo.setTeamCode(teamCodeCell);
    				userInfo.setTeam(teamCell);
    				userInfo.setLevel(levelCell);
    				userInfo.setTelephone(tel);
    				userInfos.add(userInfo);
    			}
    		}
    		
    	}catch(Exception e){
    		logger.error("fail to get users from the excel file.",e);
    		throw new Exception(e.getMessage());
    	}finally{
            if(null!=is){
                is.close();
            }
        }
    	
    	return userInfos;
    }
    
    public static List<Doctor> getDoctorDataFromFile(String filePath,List<String> headers) throws Exception{
        List<Doctor> doctors = new ArrayList<Doctor>();
        FileInputStream is = null;
        try{
            is = new FileInputStream(filePath);
            Workbook book = createCommonWorkbook(is);
            Sheet sheet = book.getSheetAt(0);
            
            int header_count = 0;
            Map<String, Integer> headerColumn = new HashMap<String, Integer>();
            
            Row row = sheet.getRow(sheet.getFirstRowNum());
            for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
                if( headers.contains(row.getCell(i).toString()) ){
                    headerColumn.put(row.getCell(i).toString(), i);
                    header_count++;
                }
            }
            logger.info("doctor header_count is " + header_count);
            if( header_count != headers.size() ){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            //get the data
            String hospitalCode_tmp = "";
            int count = 0;
            NumberFormat nf = NumberFormat.getInstance();
            //设置是否使用分组
            nf.setGroupingUsed(false);
            //设置最大整数位数
            nf.setMaximumIntegerDigits(4);
            //设置最小整数位数    
            nf.setMinimumIntegerDigits(4);
            
            for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
                row = sheet.getRow(i);
                
                Cell hospitalCodeCell = row.getCell(headerColumn.get(headers.get(0)));
                hospitalCodeCell.setCellType(Cell.CELL_TYPE_STRING);
                String hospitalCode = hospitalCodeCell.toString();
                
                Cell doctorNameCell = row.getCell(headerColumn.get(headers.get(1)));
                doctorNameCell.setCellType(Cell.CELL_TYPE_STRING);
                String doctorName = doctorNameCell.toString();
                
                Cell salesCodeCell = row.getCell(headerColumn.get(headers.get(2)));
                salesCodeCell.setCellType(Cell.CELL_TYPE_STRING);
                String salesCode = salesCodeCell.toString();
                
                if( null != hospitalCode && !"#N/A".equalsIgnoreCase(hospitalCode) ){
                	
                	if( !hospitalCode.equalsIgnoreCase(hospitalCode_tmp) ){
                    	hospitalCode_tmp = hospitalCode;
                    	count = 1;
                    }else{
                    	count++;
                    }
                	
                    Doctor doctor = new Doctor();
                    doctor.setHospitalCode(hospitalCode);
                    doctor.setName(doctorName);
                    doctor.setSalesCode(salesCode);
                    doctor.setCode(nf.format(count));
                    doctors.add(doctor);
                }
            }
        }catch(Exception e){
            logger.error("fail to get doctors from the excel file.",e);
            throw new Exception(e.getMessage());
        }finally{
            if(null!=is){
                is.close();
            }
        }
        
        return doctors;
    }
    
    public static List<Hospital> getPortNumDataFromFile(String filePath,List<String> headers) throws Exception{
    	List<Hospital> portNums = new ArrayList<Hospital>();
    	FileInputStream is = null;
    	try{
    		is = new FileInputStream(filePath);
    		Workbook book = createCommonWorkbook(is);
    		Sheet sheet = book.getSheetAt(0);
    		
    		int header_count = 0;
    		Map<String, Integer> headerColumn = new HashMap<String, Integer>();
    		
    		Row row = sheet.getRow(sheet.getFirstRowNum());
    		for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
    			if( headers.contains(row.getCell(i).toString()) ){
    				headerColumn.put(row.getCell(i).toString(), i);
    				header_count++;
    			}
    		}
    		logger.info("port num header_count is " + header_count);
    		if( header_count != headers.size() ){
    			throw new Exception("文件格式不正确，无法导入数据");
    		}
    		//get the data
    		for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
    			row = sheet.getRow(i);
    			
    			Cell hospitalCodeCell = row.getCell(headerColumn.get(headers.get(0)));
    			hospitalCodeCell.setCellType(Cell.CELL_TYPE_STRING);
    			String hospitalCode = hospitalCodeCell.toString();
    			
    			Cell portNumCell = row.getCell(headerColumn.get(headers.get(1)));
    			portNumCell.setCellType(Cell.CELL_TYPE_STRING);
    			int portNum = 0;
    			try{
    				portNum = Integer.parseInt(portNumCell.toString());
    			}catch(Exception e){
    				logger.warn("fail to parse the port num,",e);
    			}
    			
    			Hospital hospitalPortNum = new Hospital();
    			hospitalPortNum.setCode(hospitalCode);
    			hospitalPortNum.setPortNum(portNum);
    			
    			portNums.add(hospitalPortNum);
    		}
    	}catch(Exception e){
    		logger.error("fail to get doctors from the excel file.",e);
    		throw new Exception(e.getMessage());
    	}finally{
    		if(null!=is){
    			is.close();
    		}
    	}
    	
    	return portNums;
    }
    
    public static Map<String, List<RespirologyData>> getdailyRESDataFromFile(String filePath,List<String> headers) throws Exception{
        List<RespirologyData> resDatas = new ArrayList<RespirologyData>();
        List<RespirologyData> invalidResDatas = new ArrayList<RespirologyData>();
        Map<String, List<RespirologyData>> allResData = new HashMap<String, List<RespirologyData>>();
        InputStream is = null;
        try{
        	is = new FileInputStream(filePath);
            Workbook book = createCommonWorkbook(is);
            Sheet sheet = book.getSheetAt(0);
            
            int header_count = 0;
            Map<String, Integer> headerColumn = new HashMap<String, Integer>();
            
            Row row = sheet.getRow(sheet.getFirstRowNum());
            if( row == null ){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
                if( headers.contains(row.getCell(i).toString()) ){
                    headerColumn.put(row.getCell(i).toString(), i);
                    header_count++;
                }
            }
            logger.info("header_count is " + header_count + ", should be " + headers.size());
            if( header_count != headers.size() ){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            //get the data
            for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
                RespirologyData resData = new RespirologyData();
                boolean isValidData = true;
                try{
                    row = sheet.getRow(i);
                    String hosCode = row.getCell(headerColumn.get(headers.get(1))).toString();
                    resData.setHospitalCode(hosCode);
                    
                    String hosName = row.getCell(headerColumn.get(headers.get(2))).toString();
                    resData.setHospitalName(hosName);
                    
                    Date createdate = null;
                    if(  Cell.CELL_TYPE_NUMERIC == row.getCell(headerColumn.get(headers.get(0))).getCellType() ){
                        createdate = row.getCell(headerColumn.get(headers.get(0))).getDateCellValue();
                    }else{
                        String dateStr = row.getCell(headerColumn.get(headers.get(0))).toString();
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
                        createdate = sf.parse(dateStr);
                    }
                    
    				Cell pnumCell = row.getCell(headerColumn.get(headers.get(3)));
    				pnumCell.setCellType(Cell.CELL_TYPE_STRING);
    				String pnum = pnumCell.toString();
    				
    				Cell aenumCell = row.getCell(headerColumn.get(headers.get(4)));
    				aenumCell.setCellType(Cell.CELL_TYPE_STRING);
    				String aenum = aenumCell.toString();
    				
    				Cell whnumCell = row.getCell(headerColumn.get(headers.get(5)));
    				whnumCell.setCellType(Cell.CELL_TYPE_STRING);
    				String whnum = whnumCell.toString();
    				
    				Cell lsnumCell = row.getCell(headerColumn.get(headers.get(6)));
    				lsnumCell.setCellType(Cell.CELL_TYPE_STRING);
    				String lsnum = lsnumCell.toString();
                    
                    if( Integer.parseInt(pnum)*1.5 < Integer.parseInt(lsnum) ){
                        isValidData = false;
                    }
                    
                    Cell etmsCodeCell = row.getCell(headerColumn.get(headers.get(7)));
                    etmsCodeCell.setCellType(Cell.CELL_TYPE_STRING);
                    String code = etmsCodeCell.toString();
                    
                    String salesName = row.getCell(headerColumn.get(headers.get(8))).toString();                    
                    String region = row.getCell(headerColumn.get(headers.get(9))).toString();
                    String rsmRegion = row.getCell(headerColumn.get(headers.get(10))).toString();
                    
                    if( null == region || "".equalsIgnoreCase(region)
                            || null == rsmRegion || "".equalsIgnoreCase(rsmRegion)){
                        isValidData = false;
                    }
                    
                    double oqd = 0;
                    double tqd = 0;
                    double otid = 0;
                    double tbid = 0;
                    double ttid = 0;
                    double thbid = 0;
                    double fbid = 0;
                    
                    try{
                        oqd = Double.parseDouble(row.getCell(headerColumn.get(headers.get(11))).toString());
                    }catch(Exception e){
                        logger.error("ignore the parse of double format for oqd",e);
                    }
                    try{
                        tqd = Double.parseDouble(row.getCell(headerColumn.get(headers.get(12))).toString());
                    }catch(Exception e){
                        logger.error("ignore the parse of double format for tqd",e);
                    }
                    try{
                        otid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(13))).toString());
                    }catch(Exception e){
                        logger.error("ignore the parse of double format for otid",e);
                    }
                    try{
                        tbid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(14))).toString());
                    }catch(Exception e){
                        logger.error("ignore the parse of double format for tbid",e);
                    }
                    try{
                        ttid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(15))).toString());
                    }catch(Exception e){
                        logger.error("ignore the parse of double format for ttid",e);
                    }
                    try{
                        thbid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(16))).toString());
                    }catch(Exception e){
                        logger.error("ignore the parse of double format for thid",e);
                    }
                    try{
                        fbid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(17))).toString());
                    }catch(Exception e){
                        logger.error("ignore the parse of double format for fbid",e);
                    }
                    
                    if( DoubleUtils.round((DoubleUtils.add(oqd, DoubleUtils.add(tqd,DoubleUtils.add(otid,DoubleUtils.add(tbid,DoubleUtils.add(ttid,DoubleUtils.add(thbid,fbid)))))) ),1) != 1.0 && Integer.parseInt(lsnum) > 0 ){
                        isValidData = false;
                    }
                    
//                    String recipeType = row.getCell(headerColumn.get(headers.get(18))).toString();
//                    
//                    recipeType = populateRecipeTypeValue(recipeType);
                    
                    if( null != hosName && !"".equalsIgnoreCase(hosName) && isValidData ){
                        resData.setPnum(Integer.parseInt(pnum));
                        resData.setAenum(Integer.parseInt(aenum));
                        resData.setWhnum(Integer.parseInt(whnum));
                        resData.setLsnum(Integer.parseInt(lsnum));
                        resData.setSalesETMSCode(code);
                        resData.setSalesName(salesName);
                        resData.setRegion(region);
                        resData.setRsmRegion(rsmRegion);
                        resData.setOqd(oqd);
                        resData.setTqd(tqd);
                        resData.setOtid(otid);
                        resData.setTbid(tbid);
                        resData.setTtid(ttid);
                        resData.setThbid(thbid);
                        resData.setFbid(fbid);
//                        resData.setRecipeType(recipeType);
                        resData.setCreatedate(createdate);
                        
                        resDatas.add(resData);
                    }else{
                        invalidResDatas.add(resData);
                    }
                }catch(Exception e){
                    logger.error("fail to parse the data",e);
                    invalidResDatas.add(resData);
                }
            }
            
            allResData.put("validResData", resDatas);
            allResData.put("invalidResData", invalidResDatas);
            
        }catch(Exception e){
            logger.error("fail to get the daily res data from the excel file.",e);
            throw new Exception(e.getMessage());
        }finally{
            if(null!=is){
                is.close();
            }
        }
        
        return allResData;
    }
    
    public static Map<String, List<ChestSurgeryData>> getdailyCHEDataFromFile(String filePath,List<String> headers) throws Exception{
        List<ChestSurgeryData> datas = new ArrayList<ChestSurgeryData>();
        List<ChestSurgeryData> invalidDatas = new ArrayList<ChestSurgeryData>();
        Map<String, List<ChestSurgeryData>> allData = new HashMap<String, List<ChestSurgeryData>>();
        InputStream is = null;
        try{
            is = new FileInputStream(filePath);
            Workbook book = createCommonWorkbook(is);
            Sheet sheet = book.getSheetAt(0);
            
            int header_count = 0;
            Map<String, Integer> headerColumn = new HashMap<String, Integer>();
            
            Row row = sheet.getRow(sheet.getFirstRowNum());
            if( row == null ){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
                if( headers.contains(row.getCell(i).toString()) ){
                    headerColumn.put(row.getCell(i).toString(), i);
                    header_count++;
                }
            }
            logger.info("header_count is " + header_count + ", should be " + headers.size());
            if( header_count != headers.size() ){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            //get the data
            for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
                ChestSurgeryData data = new ChestSurgeryData();
                boolean isValidData = true;
                try{
                    row = sheet.getRow(i);
                    String hosCode = row.getCell(headerColumn.get(headers.get(1))).toString();
                    data.setHospitalCode(hosCode);
                    
                    String hosName = row.getCell(headerColumn.get(headers.get(2))).toString();
                    data.setHospitalName(hosName);
                    
                    Date createdate = null;
                    if(  Cell.CELL_TYPE_NUMERIC == row.getCell(headerColumn.get(headers.get(0))).getCellType() ){
                        createdate = row.getCell(headerColumn.get(headers.get(0))).getDateCellValue();
                    }else{
                        String dateStr = row.getCell(headerColumn.get(headers.get(0))).toString();
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
                        createdate = sf.parse(dateStr);
                    }
                    
                    Cell pnumCell = row.getCell(headerColumn.get(headers.get(3)));
                    pnumCell.setCellType(Cell.CELL_TYPE_STRING);
                    String pnum = pnumCell.toString();
                    
                    Cell aenumCell = row.getCell(headerColumn.get(headers.get(4)));
                    aenumCell.setCellType(Cell.CELL_TYPE_STRING);
                    String aenum = aenumCell.toString();
                    
                    Cell whnumCell = row.getCell(headerColumn.get(headers.get(5)));
                    whnumCell.setCellType(Cell.CELL_TYPE_STRING);
                    String whnum = whnumCell.toString();
                    
                    Cell lsnumCell = row.getCell(headerColumn.get(headers.get(6)));
                    lsnumCell.setCellType(Cell.CELL_TYPE_STRING);
                    String lsnum = lsnumCell.toString();
                    
                    if( Integer.parseInt(pnum)*1.5 < Integer.parseInt(lsnum) ){
                        isValidData = false;
                    }
                    
                    double oqd = 0;
                    double tqd = 0;
                    double otid = 0;
                    double tbid = 0;
                    double ttid = 0;
                    double thbid = 0;
                    double fbid = 0;
                    
                    try{
                        if( null != row.getCell(headerColumn.get(headers.get(7))) ){
                            oqd = Double.parseDouble(row.getCell(headerColumn.get(headers.get(7))).toString());
                        }
                    }catch(Exception e){
                        logger.warn("ignore the parse of double format for oqd",e);
                    }
                    try{
                        if( null != row.getCell(headerColumn.get(headers.get(8))) ){
                            tqd = Double.parseDouble(row.getCell(headerColumn.get(headers.get(8))).toString());
                        }
                    }catch(Exception e){
                        logger.warn("ignore the parse of double format for tqd",e);
                    }
                    try{
                        if( null != row.getCell(headerColumn.get(headers.get(9))) ){
                            otid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(9))).toString());
                        }
                    }catch(Exception e){
                        logger.warn("ignore the parse of double format for otid",e);
                    }
                    try{
                        if( null != row.getCell(headerColumn.get(headers.get(10))) ){
                            tbid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(10))).toString());
                        }
                    }catch(Exception e){
                        logger.warn("ignore the parse of double format for tbid",e);
                    }
                    try{
                        if( null != row.getCell(headerColumn.get(headers.get(11))) ){
                            ttid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(11))).toString());
                        }
                    }catch(Exception e){
                        logger.warn("ignore the parse of double format for ttid",e);
                    }
                    try{
                        if( null != row.getCell(headerColumn.get(headers.get(12))) ){
                            thbid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(12))).toString());
                        }
                    }catch(Exception e){
                        logger.warn("ignore the parse of double format for thid",e);
                    }
                    try{
                        if( null != row.getCell(headerColumn.get(headers.get(13))) ){
                            fbid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(13))).toString());
                        }
                    }catch(Exception e){
                        logger.warn("ignore the parse of double format for fbid",e);
                    }
                    
                    if( DoubleUtils.round((DoubleUtils.add(oqd, DoubleUtils.add(tqd,DoubleUtils.add(otid,DoubleUtils.add(tbid,DoubleUtils.add(ttid,DoubleUtils.add(thbid,fbid)))))) ),1) != 1.0 && Integer.parseInt(lsnum) > 0 ){
                        isValidData = false;
                    }
                    
                    if( null != hosName && !"".equalsIgnoreCase(hosName) && isValidData ){
                        data.setPnum(Integer.parseInt(pnum));
                        data.setRisknum(Integer.parseInt(aenum));
                        data.setWhnum(Integer.parseInt(whnum));
                        data.setLsnum(Integer.parseInt(lsnum));
                        data.setOqd(oqd);
                        data.setTqd(tqd);
                        data.setOtid(otid);
                        data.setTbid(tbid);
                        data.setTtid(ttid);
                        data.setThbid(thbid);
                        data.setFbid(fbid);
                        data.setCreatedate(createdate);
                        
                        datas.add(data);
                    }else{
                        invalidDatas.add(data);
                    }
                }catch(Exception e){
                    logger.error("fail to parse the chest surgery data",e);
                    invalidDatas.add(data);
                }
            }
            
            allData.put("validData", datas);
            allData.put("invalidData", invalidDatas);
            
        }catch(Exception e){
            logger.error("fail to get the daily res data from the excel file.",e);
            throw new Exception(e.getMessage());
        }finally{
            if(null!=is){
                is.close();
            }
        }
        
        return allData;
    }
    
    public static Map<String, List<PediatricsData>> getdailyPEDDataFromFile(String filePath,List<String> headers) throws Exception{
    	List<PediatricsData> pedDatas = new ArrayList<PediatricsData>();
    	List<PediatricsData> invalidPedDatas = new ArrayList<PediatricsData>();
    	Map<String, List<PediatricsData>> allPedData = new HashMap<String, List<PediatricsData>>();
    	InputStream is = null;
    	try{
    		is = new FileInputStream(filePath);
    		Workbook book = createCommonWorkbook(is);
    		Sheet sheet = book.getSheetAt(0);
    		
    		int header_count = 0;
    		Map<String, Integer> headerColumn = new HashMap<String, Integer>();
    		
    		Row row = sheet.getRow(sheet.getFirstRowNum());
            if( row == null ){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            
    		for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
    			if( headers.contains(row.getCell(i).toString()) ){
    				headerColumn.put(row.getCell(i).toString(), i);
    				header_count++;
    			}
    		}
    		logger.info("header_count is " + header_count  + ", should be " + headers.size());
    		if( header_count != headers.size() ){
    			throw new Exception("文件格式不正确，无法导入数据");
    		}
    		//get the data
    		
    		for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
    			PediatricsData pedData = new PediatricsData();
    			boolean isValidData = true;
    			int columnNum = 0;
    			try{
    				row = sheet.getRow(i);
    				Date createdate = null;
    				if(  Cell.CELL_TYPE_NUMERIC == row.getCell(headerColumn.get(headers.get(columnNum))).getCellType() ){
    					createdate = row.getCell(headerColumn.get(headers.get(columnNum++))).getDateCellValue();
    				}else{
    					String dateStr = row.getCell(headerColumn.get(headers.get(columnNum++))).toString();
    					SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
    					createdate = sf.parse(dateStr);
    				}
    				
    				String hosCode = row.getCell(headerColumn.get(headers.get(columnNum++))).toString();
    				pedData.setHospitalCode(hosCode);
    				
    				String hosName = row.getCell(headerColumn.get(headers.get(columnNum++))).toString();
    				pedData.setHospitalName(hosName);
    				
    				Cell pnumCell = row.getCell(headerColumn.get(headers.get(columnNum++)));
    				pnumCell.setCellType(Cell.CELL_TYPE_STRING);
    				String pnum = pnumCell.toString();
    				
    				Cell whnumCell = row.getCell(headerColumn.get(headers.get(columnNum++)));
    				whnumCell.setCellType(Cell.CELL_TYPE_STRING);
    				String whnum = whnumCell.toString();
    				
    				Cell lsnumCell = row.getCell(headerColumn.get(headers.get(columnNum++)));
    				lsnumCell.setCellType(Cell.CELL_TYPE_STRING);
    				String lsnum = lsnumCell.toString();
    				
    				Cell portnumCell = row.getCell(headerColumn.get(headers.get(columnNum++)));
    				portnumCell.setCellType(Cell.CELL_TYPE_STRING);
    				String portnum = portnumCell.toString();
    				
    				if( Integer.parseInt(pnum)*1.5 < Integer.parseInt(lsnum) ){
    					logger.warn(String.format("pnum*1.5<lsnum,current hospital is %s", hosCode));
    				    isValidData = false;
    				}
    				
    				Cell etmsCodeCell = row.getCell(headerColumn.get(headers.get(columnNum++)));
    				etmsCodeCell.setCellType(Cell.CELL_TYPE_STRING);
    				String code = etmsCodeCell.toString();
    				
    				String salesName = row.getCell(headerColumn.get(headers.get(columnNum++))).toString();                    
    				String region = row.getCell(headerColumn.get(headers.get(columnNum++))).toString();
    				String rsmRegion = row.getCell(headerColumn.get(headers.get(columnNum++))).toString();
    				
    				if( null == region || "".equalsIgnoreCase(region)
    				        || null == rsmRegion || "".equalsIgnoreCase(rsmRegion)){
    					logger.warn(String.format("region or rsmRegion is null ,current hospital is %s", hosCode));
                        isValidData = false;
                    }
    				
    				double hqd = 0;
    				double hbid = 0;
    				double oqd = 0;
    				double obid = 0;
    				double tqd = 0;
    				double tbid = 0;
    				try{
    				    hqd = Double.parseDouble(row.getCell(headerColumn.get(headers.get(columnNum++))).toString());
    				}catch(Exception e){
    				    logger.error("ignore the parse of double format for hqd",e);
    				}
    				try{
    				    hbid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(columnNum++))).toString());
    				}catch(Exception e){
    				    logger.error("ignore the parse of double format for hbid",e);
    				}
    				try{
    				    oqd = Double.parseDouble(row.getCell(headerColumn.get(headers.get(columnNum++))).toString());
    				}catch(Exception e){
    				    logger.error("ignore the parse of double format for oqd",e);
    				}
    				try{
    				    obid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(columnNum++))).toString());
    				}catch(Exception e){
    				    logger.error("ignore the parse of double format for obid",e);
    				}
    				try{
    				    tqd = Double.parseDouble(row.getCell(headerColumn.get(headers.get(columnNum++))).toString());
    				}catch(Exception e){
    				    logger.error("ignore the parse of double format for tqd",e);
    				}
    				try{
    				    tbid = Double.parseDouble(row.getCell(headerColumn.get(headers.get(columnNum++))).toString());
    				}catch(Exception e){
    				    logger.error("ignore the parse of double format for tbid",e);
    				}
    				
    				String recipeType = row.getCell(headerColumn.get(headers.get(columnNum++))).toString();
    				
    				recipeType = populateRecipeTypeValue(recipeType);
    				
    				if( DoubleUtils.round((DoubleUtils.add(hqd, DoubleUtils.add(hbid,DoubleUtils.add(oqd,DoubleUtils.add(obid,DoubleUtils.add(tqd,tbid)))))),1) != 1.0 && Integer.parseInt(lsnum) > 0 ){
    					logger.warn(String.format("(hqd + hbid + oqd + obid + tqd + tbid) != 1.0 && Integer.parseInt(lsnum) > 0 ,current hospital is %s", hosCode));
    				    isValidData = false;
    				}
    				
    				if( null != hosName && !"".equalsIgnoreCase(hosName) && isValidData ){
    					pedData.setPnum(Integer.parseInt(pnum));
    					pedData.setWhnum(Integer.parseInt(whnum));
    					pedData.setLsnum(Integer.parseInt(lsnum));
    					pedData.setPortNum(Integer.parseInt(portnum));
    					pedData.setSalesETMSCode(code);
    					pedData.setSalesName(salesName);
    					pedData.setRegion(region);
    					pedData.setRsmRegion(rsmRegion);
    					pedData.setHqd(hqd);
    					pedData.setHbid(hbid);
    					pedData.setOqd(oqd);
    					pedData.setObid(obid);
    					pedData.setTqd(tqd);
    					pedData.setTbid(tbid);
    					pedData.setRecipeType(recipeType);
    					pedData.setCreatedate(createdate);
    					
    					pedDatas.add(pedData);
    				}else{
    					invalidPedDatas.add(pedData);
    				}
    			}catch(Exception e){
    				logger.error("fail to parse the data",e);
    				invalidPedDatas.add(pedData);
    			}
    		}
    		
    		allPedData.put("validPedData", pedDatas);
    		allPedData.put("invalidPedData", invalidPedDatas);
    		
    	}catch(Exception e){
    		logger.error("fail to get the daily res data from the excel file.",e);
    		throw new Exception(e.getMessage());
    	}finally{
    	    if( null!= is ){
    	        is.close();
    	    }
    	}
    	
    	return allPedData;
    }
    
    public static Map<String, List<MonthlyData>> getMonthlyDataFromFile(String filePath,List<String> headers) throws Exception{
    	Map<String, List<MonthlyData>> resultData = new HashMap<String, List<MonthlyData>>();
    	List<MonthlyData> monthlyDataList = new ArrayList<MonthlyData>();
    	InputStream is = null;
    	try{
    		is = new FileInputStream(filePath);
    		Workbook book = createCommonWorkbook(is);
    		Sheet sheet = book.getSheetAt(0);
    		
    		int header_count = 0;
    		Map<String, Integer> headerColumn = new HashMap<String, Integer>();
    		
    		Row row = sheet.getRow(sheet.getFirstRowNum());
    		if( row == null ){
    			throw new Exception("文件格式不正确，无法导入数据");
    		}
    		
    		for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
    			if( headers.contains(row.getCell(i).toString()) ){
    				headerColumn.put(row.getCell(i).toString(), i);
    				header_count++;
    			}
    		}
    		logger.info("header_count is " + header_count  + ", should be " + headers.size());
    		if( header_count != headers.size() ){
    			throw new Exception("文件格式不正确，无法导入数据");
    		}
    		//get the data
    		
    		for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
    			MonthlyData monthlyData = new MonthlyData();
    			try{
    				row = sheet.getRow(i);
    				Date createdate = null;
    				if(  Cell.CELL_TYPE_NUMERIC == row.getCell(headerColumn.get(headers.get(0))).getCellType() ){
    					createdate = row.getCell(headerColumn.get(headers.get(0))).getDateCellValue();
    				}else{
    					String dateStr = row.getCell(headerColumn.get(headers.get(0))).toString();
    					SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
    					createdate = sf.parse(dateStr);
    				}
    				monthlyData.setCreateDate(createdate);
    				
    				String hosCode = row.getCell(headerColumn.get(headers.get(1))).toString();
    				monthlyData.setHospitalCode(hosCode);
    				
    				double pedemernum = 0;
    				double pedroomnum = 0;
    				double resnum = 0;
    				double othernum = 0;
    				try{
    					pedemernum = Double.parseDouble(row.getCell(headerColumn.get(headers.get(2))).toString());
    				}catch(Exception e){
    					logger.error("ignore the parse of double format for pedemernum",e);
    				}
    				try{
    					pedroomnum = Double.parseDouble(row.getCell(headerColumn.get(headers.get(3))).toString());
    				}catch(Exception e){
    					logger.error("ignore the parse of double format for pedroomnum",e);
    				}
    				try{
    					resnum = Double.parseDouble(row.getCell(headerColumn.get(headers.get(4))).toString());
    				}catch(Exception e){
    					logger.error("ignore the parse of double format for resnum",e);
    				}
    				try{
    					othernum = Double.parseDouble(row.getCell(headerColumn.get(headers.get(5))).toString());
    				}catch(Exception e){
    					logger.error("ignore the parse of double format for othernum",e);
    				}
    				
    				monthlyData.setPedemernum(pedemernum);
    				monthlyData.setPedroomnum(pedroomnum);
    				monthlyData.setResnum(resnum);
    				monthlyData.setOthernum(othernum);
    					
					monthlyDataList.add(monthlyData);
					
					resultData.put("validData", monthlyDataList);
    			}catch(Exception e){
    				logger.error("fail to parse the data",e);
    			}
    		}
    	}catch(Exception e){
    		logger.error("fail to get the daily res data from the excel file.",e);
    		throw new Exception(e.getMessage());
    	}finally{
    		if( null!= is ){
    			is.close();
    		}
    	}
    	
    	return resultData;
    }
    
    public static List<DDIData> getDDIDataFromFile(String filePath,List<String> headers) throws Exception{
    	List<DDIData> ddiDatas = new ArrayList<DDIData>();
    	InputStream is = null;
    	try{
    		is = new FileInputStream(filePath);
    		Workbook book = createCommonWorkbook(is);
    		Sheet sheet = book.getSheetAt(0);
    		
    		int header_count = 0;
    		Map<String, Integer> headerColumn = new HashMap<String, Integer>();
    		
    		Row row = sheet.getRow(sheet.getFirstRowNum());
    		if( row == null ){
    			throw new Exception("文件格式不正确，无法导入数据");
    		}
    		
    		for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
    			if( headers.contains(row.getCell(i).toString()) ){
    				headerColumn.put(row.getCell(i).toString(), i);
    				header_count++;
    			}
    		}
    		logger.info("header_count is " + header_count  + ", should be " + headers.size());
    		if( header_count != headers.size() ){
    			throw new Exception("文件格式不正确，无法导入数据");
    		}
    		//get the data
    		
    		for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
    			try{
    				row = sheet.getRow(i);
    				
    				Cell durationCell = row.getCell(0);
    				durationCell.setCellType(Cell.CELL_TYPE_STRING);
    				String duration = durationCell.toString();
    				
    				for( int j = 0; j < headers.size(); j++ ){
    				    Cell numCell = row.getCell(headerColumn.get(headers.get(j)));
    				    numCell.setCellType(Cell.CELL_TYPE_STRING);
    				    String num = numCell.toString();
    				    
    				    DDIData ddiData = new DDIData();
    				    ddiData.setDuration(duration);
    				    ddiData.setNum(Double.parseDouble(num));
    				    ddiData.setRegion(headers.get(j));
    				    ddiDatas.add(ddiData);
    				}
    			}catch(Exception e){
    				logger.error("fail to parse the ddi data",e);
    			}
    		}
    	}catch(Exception e){
    		logger.error("fail to get the ddi data from the excel file.",e);
    		throw new Exception(e.getMessage());
    	}finally{
    	    if( null!= is ){
    	        is.close();
    	    }
    	}
    	return ddiDatas;
    }
    
    public static Map<String,List> getAllInfosFromFile(String filePath,List<String> regionHeaders, 
            List<String> hospitalHeaders, List<String> repHeaders, List<String> dsmHeaders, List<String> rsmHeaders, List<String> rsdHeaders) throws Exception{
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        Map<String,List> allInfos = new HashMap<String,List>();
        InputStream is = null;
        try{
        	is = new FileInputStream(filePath);
            Workbook book = createCommonWorkbook(is);
            Sheet sheet = book.getSheetAt(0);
            
            int region_header_count = 0;
            int hos_header_count = 0;
            int rep_header_count = 0;
            int dsm_header_count = 0;
            int rsm_header_count = 0;
            int rsd_header_count = 0;
            Map<String, Integer> regionHeaderColumn = new HashMap<String, Integer>();
            Map<String, Integer> hospitalHeaderColumn = new HashMap<String, Integer>();
            Map<String, Integer> repHeaderColumn = new HashMap<String, Integer>();
            Map<String, Integer> dsmHeaderColumn = new HashMap<String, Integer>();
            Map<String, Integer> rsmHeaderColumn = new HashMap<String, Integer>();
            Map<String, Integer> rsdHeaderColumn = new HashMap<String, Integer>();
            
            Row row = sheet.getRow(sheet.getFirstRowNum());
            if( row == null ){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            
            for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
                if( null == row.getCell(i).toString() || "".equalsIgnoreCase(row.getCell(i).toString()) ){
                    logger.info("current column is " + i + ", the header is null, so break the loop");
                    break;
                }
                logger.info("header row : " + row.getCell(i).toString());
                if( regionHeaders.contains(row.getCell(i).toString()) ){
                    regionHeaderColumn.put(row.getCell(i).toString(), i);
                    region_header_count++;
                    continue;
                }
                if( hospitalHeaders.contains(row.getCell(i).toString()) ){
                    hospitalHeaderColumn.put(row.getCell(i).toString(), i);
                    hos_header_count++;
                    continue;
                }
                if( repHeaders.contains(row.getCell(i).toString()) ){
                    repHeaderColumn.put(row.getCell(i).toString(), i);
                    rep_header_count++;
                    continue;
                }
                if( dsmHeaders.contains(row.getCell(i).toString()) ){
                    dsmHeaderColumn.put(row.getCell(i).toString(), i);
                    dsm_header_count++;
                    continue;
                }
                if( rsmHeaders.contains(row.getCell(i).toString()) ){
                    rsmHeaderColumn.put(row.getCell(i).toString(), i);
                    rsm_header_count++;
                    continue;
                }
                if( rsdHeaders.contains(row.getCell(i).toString()) ){
                    rsdHeaderColumn.put(row.getCell(i).toString(), i);
                    rsd_header_count++;
                }
            }
            logger.info("hospitalHeaders num is " + hos_header_count + ",regionHeader num is " + region_header_count
                    + ", repHeaders num is " + rep_header_count + ", dsmHeaders num is " + dsm_header_count 
                    + ", rsmHeaders num is " + rsm_header_count + ", rsdHeaders num is " + rsd_header_count );
            if( hos_header_count != hospitalHeaders.size() 
                    || region_header_count != regionHeaders.size() 
                    || rep_header_count != repHeaders.size() 
                    || dsm_header_count != dsmHeaders.size() 
                    || rsm_header_count != rsmHeaders.size() 
                    || rsd_header_count != rsdHeaders.size()){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            
            DecimalFormat df = new DecimalFormat();
            //get the data
            List<String> userCodes = new ArrayList<String>();
            List<String> hospitalCodes = new ArrayList<String>();
            Map<String,Hospital> hospitalMap = new HashMap<String, Hospital>();
            List<HospitalUserRefer> hosUsers = new ArrayList<HospitalUserRefer>();
            List<Property> regionNameList = new ArrayList<Property>();
            
            for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
                row = sheet.getRow(i);
                logger.info(" read row " + i);
                if( null == row || null == row.getCell(0) || "".equalsIgnoreCase(row.getCell(0).toString()) ){
                    break;
                }
                //collect region info
                String brName = row.getCell(regionHeaderColumn.get(regionHeaders.get(0))).toString();
                String distName = row.getCell(regionHeaderColumn.get(regionHeaders.get(1))).toString();
                String brCNName = row.getCell(regionHeaderColumn.get(regionHeaders.get(2))).toString();
                
                regionNameList.add(new Property(brName,brCNName));
                
                // collect hospital info
                String hospitalCode = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(0))).toString();
                String hospitalName = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(1))).toString();
                String province = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(2))).toString();
                String city = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(3))).toString();
                
                Cell hospitalLevelCell = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(4)));
                hospitalLevelCell.setCellType(Cell.CELL_TYPE_STRING);
                String hospitalLevel = hospitalLevelCell.toString();
                
                String dragonType = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(5))).toString();
                
                Cell isResAssessedCell = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(6)));
                isResAssessedCell.setCellType(Cell.CELL_TYPE_STRING);
                String isResAssessed = isResAssessedCell.toString();
                
                Cell isPedAssessedCell = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(7)));
                isPedAssessedCell.setCellType(Cell.CELL_TYPE_STRING);
                String isPedAssessed = isPedAssessedCell.toString();
                
                Cell isMonthlyAssessedCell = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(8)));
                isMonthlyAssessedCell.setCellType(Cell.CELL_TYPE_STRING);
                String isMonthlyAssessed = isMonthlyAssessedCell.toString();
                
                Cell isChestSurgeryAssessedCell = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(9)));
                isChestSurgeryAssessedCell.setCellType(Cell.CELL_TYPE_STRING);
                String isChestSurgeryAssessed = isChestSurgeryAssessedCell.toString();
                
                Cell isTop100Cell = row.getCell(hospitalHeaderColumn.get(hospitalHeaders.get(10)));
                isTop100Cell.setCellType(Cell.CELL_TYPE_STRING);
                String isTop100 = isTop100Cell.toString();
                
                //collect rep info
                Cell repCodeCell = row.getCell(repHeaderColumn.get(repHeaders.get(0)));
                repCodeCell.setCellType(Cell.CELL_TYPE_STRING);
                String repCode = repCodeCell.toString();
                
                String repName = row.getCell(repHeaderColumn.get(repHeaders.get(1))).toString();
                
                Cell repTelCell = row.getCell(repHeaderColumn.get(repHeaders.get(2)));
                repTelCell.setCellType(Cell.CELL_TYPE_STRING);
                String repTel = repTelCell.toString();
                
                Cell isPrimaryCell = row.getCell(repHeaderColumn.get(repHeaders.get(3)));
                isPrimaryCell.setCellType(Cell.CELL_TYPE_STRING);
                String isPrimary = isPrimaryCell.toString();
                
                Cell repEmailCell = row.getCell(repHeaderColumn.get(repHeaders.get(4)));
                repEmailCell.setCellType(Cell.CELL_TYPE_STRING);
                String repEmail = repEmailCell.toString();
                
                //collect dsm info
                Cell dsmCodeCell = row.getCell(dsmHeaderColumn.get(dsmHeaders.get(0)));
                dsmCodeCell.setCellType(Cell.CELL_TYPE_STRING);
                String dsmCode = dsmCodeCell.toString();
                
                String dsmName = row.getCell(dsmHeaderColumn.get(dsmHeaders.get(1))).toString();
                
                Cell dsmTelCell = row.getCell(dsmHeaderColumn.get(dsmHeaders.get(2)));
                dsmTelCell.setCellType(Cell.CELL_TYPE_STRING);
                String dsmTel = dsmTelCell.toString();
                
                Cell dsmEmailCell = row.getCell(dsmHeaderColumn.get(dsmHeaders.get(3)));
                dsmEmailCell.setCellType(Cell.CELL_TYPE_STRING);
                String dsmEmail = dsmEmailCell.toString();
                
                //collect rsm info
                Cell rsmCodeCell = row.getCell(rsmHeaderColumn.get(rsmHeaders.get(0)));
                rsmCodeCell.setCellType(Cell.CELL_TYPE_STRING);
                String rsmCode = rsmCodeCell.toString();
                
                String rsmName = row.getCell(rsmHeaderColumn.get(rsmHeaders.get(1))).toString();
                
                Cell rsmTelCell = row.getCell(rsmHeaderColumn.get(rsmHeaders.get(2)));
                rsmTelCell.setCellType(Cell.CELL_TYPE_STRING);
                String rsmTel = rsmTelCell.toString();
                
                Cell rsmEmailCell = row.getCell(rsmHeaderColumn.get(rsmHeaders.get(3)));
                rsmEmailCell.setCellType(Cell.CELL_TYPE_STRING);
                String rsmEmail = rsmEmailCell.toString();
                
                //collect rsd info
                Cell rsdCodeCell = row.getCell(rsdHeaderColumn.get(rsdHeaders.get(0)));
                rsdCodeCell.setCellType(Cell.CELL_TYPE_STRING);
                String rsdCode = rsdCodeCell.toString();
                
                String rsdName = row.getCell(rsdHeaderColumn.get(rsdHeaders.get(1))).toString();
                
                Cell rsdTelCell = row.getCell(rsdHeaderColumn.get(rsdHeaders.get(2)));
                rsdTelCell.setCellType(Cell.CELL_TYPE_STRING);
                String rsdTel = rsdTelCell.toString();
                
                Cell rsdEmailCell = row.getCell(rsdHeaderColumn.get(rsdHeaders.get(3)));
                rsdEmailCell.setCellType(Cell.CELL_TYPE_STRING);
                String rsdEmail = rsdEmailCell.toString();
                
                if( null != hospitalCodes && !hospitalCodes.contains(hospitalCode) 
                        && null != hospitalName && !"".equalsIgnoreCase(hospitalName) ){
                    Hospital hospital = new Hospital();
                    hospital.setCode(hospitalCode);
                    hospital.setName(hospitalName);
                    hospital.setProvince(province);
                    hospital.setCity(city);
                    hospital.setDsmCode(dsmCode);
                    hospital.setDsmName(dsmName);
                    hospital.setRsmRegion(distName);
                    hospital.setSaleName(repName);
                    hospital.setSaleCode(repCode);
                    hospital.setRegion(brName);
                    hospital.setLevel(hospitalLevel);
                    hospital.setDragonType(dragonType);
                    hospital.setIsResAssessed(isResAssessed);
                    hospital.setIsPedAssessed(isPedAssessed);
                    hospital.setIsMonthlyAssessed(isMonthlyAssessed);
                    hospital.setIsChestSurgeryAssessed(isChestSurgeryAssessed);
                    hospital.setIsTop100(isTop100);
                    
                    hospitalMap.put(hospitalCode, hospital);
                    hospitalCodes.add(hospitalCode);
                }
                
                if( null!=isPrimary && "1".equalsIgnoreCase(isPrimary) ){
                    Hospital existHospital = hospitalMap.get(hospitalCode);
                    existHospital.setSaleCode(repCode);
                    existHospital.setSaleName(repName);
                    hospitalMap.put(hospitalCode, existHospital);
                }
                
                if( null != userCodes && !userCodes.contains(distName+dsmName+repCode+repName) 
                        && null != repName && !"".equalsIgnoreCase(repName) ){
                    UserInfo repInfo = new UserInfo();
                    repInfo.setUserCode(repCode);
                    repInfo.setName(repName);
                    repInfo.setRegionCenter(brName);
                    repInfo.setRegion(distName);
                    repInfo.setLevel("REP");
                    repInfo.setTelephone(repTel);
                    repInfo.setSuperior(dsmCode);
                    repInfo.setEmail(repEmail);
                    userInfos.add(repInfo);
                    
                    userCodes.add(distName+dsmName+repCode+repName);
                }
                
                if( null != userCodes && !userCodes.contains(distName+dsmCode+dsmName) 
                        && null != dsmName && !"".equalsIgnoreCase(dsmName) ){
                    UserInfo dsmInfo = new UserInfo();
                    dsmInfo.setUserCode(dsmCode);
                    dsmInfo.setName(dsmName);
                    dsmInfo.setTelephone(dsmTel);
                    dsmInfo.setRegionCenter(brName);
                    dsmInfo.setRegion(distName);
                    dsmInfo.setLevel("DSM");
                    dsmInfo.setEmail(dsmEmail);
                    userInfos.add(dsmInfo);
                    
                    userCodes.add(distName+dsmCode+dsmName);
                }
                
                if( null != userCodes && !userCodes.contains(distName+rsmCode+rsmName) 
                        && null != rsmName && !"".equalsIgnoreCase(rsmName) ){
                    UserInfo rsmInfo = new UserInfo();
                    rsmInfo.setUserCode(rsmCode);
                    rsmInfo.setName(rsmName);
                    rsmInfo.setTelephone(rsmTel);
                    rsmInfo.setRegionCenter(brName);
                    rsmInfo.setRegion(distName);
                    rsmInfo.setLevel("RSM");
                    rsmInfo.setEmail(rsmEmail);
                    userInfos.add(rsmInfo);
                    
                    userCodes.add(distName+rsmCode+rsmName);
                }
                
                if( null != userCodes && !userCodes.contains(rsdCode+rsdName) 
                        && null != rsdName && !"".equalsIgnoreCase(rsdName) ){
                    UserInfo rsdInfo = new UserInfo();
                    rsdInfo.setUserCode(rsdCode);
                    rsdInfo.setName(rsdName);
                    rsdInfo.setTelephone(rsdTel);
                    rsdInfo.setRegionCenter(brName);
                    rsdInfo.setRegion(distName);
                    rsdInfo.setLevel("RSD");
                    rsdInfo.setEmail(rsdEmail);
                    userInfos.add(rsdInfo);
                    
                    userCodes.add(rsdCode+rsdName);
                }
                
                //hos users reference
                if( null != repCode && !"".equalsIgnoreCase(repCode) && !"#N/A".equalsIgnoreCase(repCode)
                        && null != hospitalCode && !"".equalsIgnoreCase(hospitalCode)){
                    HospitalUserRefer hosUserRefer = new HospitalUserRefer();
                    hosUserRefer.setHospitalCode(hospitalCode);
                    hosUserRefer.setUserCode(repCode);
                    hosUserRefer.setIsPrimary(isPrimary);
                    
                    hosUsers.add(hosUserRefer);
                }
            }
            
            List<Hospital> hospitalInfos = new ArrayList<Hospital>();
            for( Hospital hos : hospitalMap.values()){
                hospitalInfos.add(hos);
            }
            logger.info(String.format("hospital list size is %s", hospitalInfos==null?0:hospitalInfos.size()) );
            
            allInfos.put("hospitals", hospitalInfos);
            allInfos.put("users", userInfos);
            allInfos.put("hosUsers", hosUsers);
            allInfos.put("regionNames", regionNameList);
            
        }catch(Exception e){
            logger.error("fail to get users from the excel file.",e);
            throw new Exception(e.getMessage());
        }finally{
            if( null!= is ){
                is.close();
            }
        }
        
        return allInfos;
    }
    
    public static List<UserInfo> getBMUserInfosFromFile(String filePath,List<String> headers) throws Exception{
        List<UserInfo> userInfos = new ArrayList<UserInfo>();
        FileInputStream is = null;
        try{
            is = new FileInputStream(filePath);
            Workbook book = createCommonWorkbook(is);
            Sheet sheet = book.getSheetAt(0);
            
            int header_count = 0;
            Map<String, Integer> headerColumn = new HashMap<String, Integer>();
            
            Row row = sheet.getRow(sheet.getFirstRowNum());
            for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
                if( headers.contains(row.getCell(i).toString()) ){
                    logger.info(String.format("row.getCell(i).toString() is %s, i is %s", row.getCell(i).toString(),i));
                    headerColumn.put(row.getCell(i).toString(), i);
                    header_count++;
                }
            }
            logger.info("header_count is " + header_count);
            if( header_count != headers.size() ){
                throw new Exception("文件格式不正确，无法导入数据");
            }
            //get the data
            logger.info(String.format("sheet.getPhysicalNumberOfRows() is %s", sheet.getPhysicalNumberOfRows()));
            for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
                row = sheet.getRow(i);
                logger.info(String.format("read the row from BM excel %s",i));
                
                String userCode = "";
                String name = "";
                String type = "";
                String tel = "";
                String email = "";
                try{
                    Cell userCodeCell = row.getCell(headerColumn.get(headers.get(0)));
                    Cell nameCell = row.getCell(headerColumn.get(headers.get(1)));
                    Cell typeCell = row.getCell(headerColumn.get(headers.get(2)));
                    Cell telCell = row.getCell(headerColumn.get(headers.get(3)));
                    Cell emailCell = row.getCell(headerColumn.get(headers.get(4)));
                    if( userCodeCell == null && nameCell == null
                            && typeCell == null
                            && telCell == null
                            && emailCell == null ){
                        logger.info("there is no new info, break now");
                        break;
                    }
                    
                    userCodeCell.setCellType(Cell.CELL_TYPE_STRING);
                    if( null != userCodeCell ){
                        userCode = userCodeCell.toString();
                    }
                    logger.info(String.format("insert BM user whose code is %s",userCode));
                    
                    name = nameCell.toString();
                    
                    type = typeCell.toString();
                    
                    telCell.setCellType(Cell.CELL_TYPE_STRING);
                    tel = telCell.toString();
                    
                    if( null != emailCell ){
                        email = emailCell.toString();
                    }
                }catch(Exception e){
                    logger.error("fail to get the user from the excel,",e);
                }
                
                if( null != tel && !"".equalsIgnoreCase(tel) ){
                    UserInfo userInfo = new UserInfo();
                    userInfo.setUserCode(userCode);
                    userInfo.setName(name);
                    userInfo.setLevel(type);
                    userInfo.setTelephone(tel);
                    userInfo.setEmail(email);
                    userInfos.add(userInfo);
                }
            }
            
        }catch(Exception e){
            logger.error("fail to get users from the excel file.",e);
            throw new Exception(e.getMessage());
        }finally{
            if(null!= is){
                is.close();
            }
        }
        
        return userInfos;
    }
    
    public static List<UserCode> getUserCodesFromFile(String filePath,List<String> headers) throws Exception{
    	List<UserCode> userCodes = new ArrayList<UserCode>();
    	FileInputStream is = null;
    	try{
    	    is = new FileInputStream(filePath);
    		Workbook book = createCommonWorkbook(is);
    		Sheet sheet = book.getSheetAt(0);
    		
    		int header_count = 0;
    		Map<String, Integer> headerColumn = new HashMap<String, Integer>();
    		
    		Row row = sheet.getRow(sheet.getFirstRowNum());
    		for( int i = row.getFirstCellNum(); i < row.getPhysicalNumberOfCells(); i++ ){
    			if( headers.contains(row.getCell(i).toString()) ){
    				headerColumn.put(row.getCell(i).toString(), i);
    				header_count++;
    			}
    		}
    		logger.info("header_count is " + header_count);
    		if( header_count != headers.size() ){
    			throw new Exception("文件格式不正确，无法导入数据");
    		}
    		//get the data
    		for( int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++ ){
    			row = sheet.getRow(i);
    			
    			Cell oldUserCodeCell = row.getCell(headerColumn.get(headers.get(0)));
    			oldUserCodeCell.setCellType(Cell.CELL_TYPE_STRING);
    			String oldUserCode = oldUserCodeCell.toString();
    			
    			Cell newUserCodeCell = row.getCell(headerColumn.get(headers.get(1)));
    			newUserCodeCell.setCellType(Cell.CELL_TYPE_STRING);
    			String newUserCode = newUserCodeCell.toString();
    			
    			if( null != oldUserCode && !"".equalsIgnoreCase(oldUserCode) 
    					&& null != newUserCode && !"".equalsIgnoreCase(newUserCode) ){
    				UserCode userCode = new UserCode();
    				userCode.setNewCode(newUserCode);
    				userCode.setOldCode(oldUserCode);
    				userCodes.add(userCode);
    			}
    		}
    		
    	}catch(Exception e){
    		logger.error("fail to get user codes from the excel file.",e);
    		throw new Exception(e.getMessage());
    	}finally{
            if(null!= is){
                is.close();
            }
        }
    	
    	return userCodes;
    }

    public static Workbook createCommonWorkbook(InputStream inp) throws IOException, InvalidFormatException { 
        if (!inp.markSupported()) {
            inp = new PushbackInputStream(inp,8);
        } 
        if (POIFSFileSystem.hasPOIFSHeader(inp)) { 
            return new HSSFWorkbook(inp); 
        } 
        if (POIXMLDocument.hasOOXMLHeader(inp)) { 
            return new XSSFWorkbook(OPCPackage.open(inp)); 
        } 
        throw new IOException("不能解析的excel版本"); 
    }
    
    public static void main(String[] args){
    }
}
