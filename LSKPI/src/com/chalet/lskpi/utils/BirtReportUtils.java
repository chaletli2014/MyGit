package com.chalet.lskpi.utils;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

public class BirtReportUtils {

    private IReportEngine engine = null;
    private EngineConfig config = null;
    private Logger logger = Logger.getLogger(BirtReportUtils.class);
    
    public BirtReportUtils(){
        
    }
    
  //用于运行报表  
    public void runReport(String designPath, String telephone, String userCode,String hospitalCode, String reportFileName, String fileType, String reportImgPath, String baseImgPath){
        try{
        	logger.info(String.format("run the birt report, the file name is %s",reportFileName));
            IReportRunnable design = null;  
            HashMap parameterMap = new HashMap();
            //Open the report design  
            design = engine.openReportDesign(designPath);  
            
            Date refreshDate = DateUtils.getGenerateWeeklyReportDate();
            String startDate = DateUtils.getTheBeginDateOfRefreshDate(refreshDate);
            String endDate = DateUtils.getTheEndDateOfRefreshDate(refreshDate);
            
            String startDuration = startDate+"-"+endDate;
            String last12WeekDuration = DateUtils.getEndDurationByStartDate(startDate);
            
            IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
            Collection parameters = paramTask.getParameterDefns(false);
            
            if( null != startDuration && !"".equalsIgnoreCase(startDuration) ){
                Map paramValues = new HashMap();
                paramValues.put("startDuration", startDuration);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            if( null != last12WeekDuration && !"".equalsIgnoreCase(last12WeekDuration) ){
                Map paramValues = new HashMap();
                paramValues.put("endDuration", last12WeekDuration);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            if( null != telephone && !"".equalsIgnoreCase(telephone) ){
                Map paramValues = new HashMap();
                paramValues.put("userTel", telephone);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            if( null != userCode && !"".equalsIgnoreCase(userCode) ){
                Map paramValues = new HashMap();
                paramValues.put("userCode", userCode);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            if( null != hospitalCode && !"".equalsIgnoreCase(hospitalCode) ){
            	Map paramValues = new HashMap();
            	paramValues.put("hospitalCode", hospitalCode);
            	evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            IRunAndRenderTask task = engine.createRunAndRenderTask(design);  
            logger.info("create and render the task");
            IRenderOption options = null;
            if( null == fileType || "".equalsIgnoreCase(fileType) || "excel".equalsIgnoreCase(fileType)){
            	options = new EXCELRenderOption();  
            	options.setOutputFormat("xlsx");
            	options.setOutputFileName(reportFileName);
            	options.setOption(IExcelRenderOption.OFFICE_VERSION, "office2007");
            }else if( "html".equalsIgnoreCase(fileType) ){
            	HTMLRenderContext renderContext = new HTMLRenderContext();
                renderContext.setImageDirectory(reportImgPath);
                renderContext.setBaseImageURL(baseImgPath);
                HashMap contextMap = new HashMap();
                contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
                task.setAppContext(contextMap);
                
	            options = new HTMLRenderOption();
	            options.setOutputFileName(reportFileName);
	            options.setOutputFormat("html");
	            ((HTMLRenderOption)options).setEmbeddable(true);
	            options.setSupportedImageFormats("jpg");
            }else if( "pdf".equalsIgnoreCase(fileType) ){
            	options = new PDFRenderOption();
            	options.setOutputFormat("pdf");
            	options.setOutputFileName(reportFileName);
            }
            
            task.setRenderOption(options);
            if( ( null != telephone && !"".equalsIgnoreCase(telephone) ) 
                    || (null != userCode && !"".equalsIgnoreCase(userCode)) 
                    || (null != hospitalCode && !"".equalsIgnoreCase(hospitalCode))){
                task.setParameterValues(parameterMap);
            }
            logger.info("start to run the task");
            task.run();
            task.close();  
            logger.info("the taks is closed.");
        }catch( Exception ex){
            logger.error("fail to generate the report",ex);
        }  
    }  
    //用于运行报表  
    public void runHomeReport(String designPath, String telephone, String startDuration,String last12WeekDuration, String reportFileName, String fileType, String reportImgPath, String baseImgPath){
    	try{
    		logger.info(String.format("run the birt report, the file name is %s",reportFileName));
    		IReportRunnable design = null;  
    		HashMap parameterMap = new HashMap();
    		//Open the report design  
    		design = engine.openReportDesign(designPath);  
    		IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    		Collection parameters = paramTask.getParameterDefns(false);
    		
    		if( null != startDuration && !"".equalsIgnoreCase(startDuration) ){
    			Map paramValues = new HashMap();
    			paramValues.put("startDuration", startDuration);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		if( null != last12WeekDuration && !"".equalsIgnoreCase(last12WeekDuration) ){
    			Map paramValues = new HashMap();
    			paramValues.put("endDuration", last12WeekDuration);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		if( null != telephone && !"".equalsIgnoreCase(telephone) ){
    			Map paramValues = new HashMap();
    			paramValues.put("userTel", telephone);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		IRunAndRenderTask task = engine.createRunAndRenderTask(design);  
    		logger.info("create and render the task");
    		IRenderOption options = null;
    		if( null == fileType || "".equalsIgnoreCase(fileType) || "excel".equalsIgnoreCase(fileType)){
    			options = new EXCELRenderOption();  
    			options.setOutputFormat("xlsx");
    			options.setOutputFileName(reportFileName);
    			options.setOption(IExcelRenderOption.OFFICE_VERSION, "office2007");
    		}else if( "html".equalsIgnoreCase(fileType) ){
    			HTMLRenderContext renderContext = new HTMLRenderContext();
    			renderContext.setImageDirectory(reportImgPath);
    			renderContext.setBaseImageURL(baseImgPath);
    			HashMap contextMap = new HashMap();
    			contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
    			task.setAppContext(contextMap);
    			
    			options = new HTMLRenderOption();
    			options.setOutputFileName(reportFileName);
    			options.setOutputFormat("html");
    			((HTMLRenderOption)options).setEmbeddable(true);
    			options.setSupportedImageFormats("jpg");
    		}else if( "pdf".equalsIgnoreCase(fileType) ){
    			options = new PDFRenderOption();
    			options.setOutputFormat("pdf");
    			options.setOutputFileName(reportFileName);
    		}
    		
    		task.setRenderOption(options);
			task.setParameterValues(parameterMap);
    		logger.info("start to run the task");
    		task.run();
    		task.close();  
    		logger.info("the taks is closed.");
    	}catch( Exception ex){
    		logger.error("fail to generate the home report",ex);
    	}  
    }  
    //用于运行报表  
    public void runRefreshReport(String designPath, String telephone, String startDate, String endDate, String reportFileName, String fileType, String reportImgPath, String baseImgPath, String region){
        try{
            logger.info(String.format("run the birt refresh report, the file name is %s",reportFileName));
            IReportRunnable design = null;  
            HashMap parameterMap = new HashMap();
            //Open the report design  
            design = engine.openReportDesign(designPath);  
            
            if( null != telephone && !"".equalsIgnoreCase(telephone) ){
                IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
                Collection parameters = paramTask.getParameterDefns(false);
                Map paramValues = new HashMap();
                paramValues.put("userTel", telephone);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            if( null != startDate ){
                logger.info(String.format("populdate the param startDate %s", startDate));
                IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
                Collection parameters = paramTask.getParameterDefns(false);
                Map paramValues = new HashMap();
                paramValues.put("startDate", startDate);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            if( null != endDate ){
                logger.info(String.format("populdate the param endDate %s", endDate));
                IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
                Collection parameters = paramTask.getParameterDefns(false);
                Map paramValues = new HashMap();
                paramValues.put("endDate", endDate);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            if( null != region && !"".equalsIgnoreCase(region) ){
                logger.info(String.format("populdate the param region %s", region));
                IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
                Collection parameters = paramTask.getParameterDefns(false);
                Map paramValues = new HashMap();
                paramValues.put("regionCenter", region);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            String startDuration = startDate+"-"+endDate;
            if( null != startDuration ){
    			logger.info(String.format("populdate the param startDuration %s", startDuration));
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("startDuration", startDuration);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
            String last12WeekDuration = DateUtils.getEndDurationByStartDate(startDate);
    		if( null != last12WeekDuration ){
    			logger.info(String.format("populdate the param last12WeekDuration %s", last12WeekDuration));
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("endDuration", last12WeekDuration);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
            
            IRunAndRenderTask task = engine.createRunAndRenderTask(design);  
            logger.info("create and render the refresh task");
            IRenderOption options = null;
            if( null == fileType || "".equalsIgnoreCase(fileType) || "excel".equalsIgnoreCase(fileType)){
                options = new EXCELRenderOption();  
                options.setOutputFormat("xlsx");
                options.setOutputFileName(reportFileName);
                options.setOption(IExcelRenderOption.OFFICE_VERSION, "office2007");
            }else if( "html".equalsIgnoreCase(fileType) ){
                HTMLRenderContext renderContext = new HTMLRenderContext();
                renderContext.setImageDirectory(reportImgPath);
                renderContext.setBaseImageURL(baseImgPath);
                HashMap contextMap = new HashMap();
                contextMap.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);
                task.setAppContext(contextMap);
                
                options = new HTMLRenderOption();
                options.setOutputFileName(reportFileName);
                options.setOutputFormat("html");
                ((HTMLRenderOption)options).setEmbeddable(true);
                options.setSupportedImageFormats("jpg");
            }else if( "pdf".equalsIgnoreCase(fileType) ){
                options = new PDFRenderOption();
                options.setOutputFormat("pdf");
                options.setOutputFileName(reportFileName);
            }
            
            task.setRenderOption(options);
            if( ( null != telephone && !"".equalsIgnoreCase(telephone) ) 
                    || ( null != startDate && !"".equalsIgnoreCase(startDate) )
                    || ( null != endDate && !"".equalsIgnoreCase(endDate) ) 
                    || ( null != startDuration && !"".equalsIgnoreCase(startDuration) ) 
                    || ( null != last12WeekDuration && !"".equalsIgnoreCase(last12WeekDuration) ) ){
                task.setParameterValues(parameterMap);
            }
            logger.info("start to run the refresh report task");
            task.run();
            task.close();  
            logger.info("the refresh taks is closed.");
        }catch( Exception ex){
            logger.error("fail to refresh the report",ex);
        }  
    }  
    
    /**
     * 用于运行家庭雾化报表 .
     * @param designPath
     * @param telephone
     * @param startDuration
     * @param endDuration
     * @param reportFileName
     * @param fileType
     * @param reportImgPath
     * @param baseImgPath
     */
    public void runHomePDFReport(String designPath, String telephone, String startDuration, String endDuration, String startDate, String endDate, String last2Duration, String reportFileName){
    	try{
    		logger.info(String.format("run the birt home pdf report, the file name is %s",reportFileName));
    		IReportRunnable design = null;  
    		HashMap parameterMap = new HashMap();
    		//Open the report design  
    		design = engine.openReportDesign(designPath);  
    		
    		if( null != telephone && !"".equalsIgnoreCase(telephone) ){
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("userTel", telephone);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		if( null != startDuration ){
    			logger.info(String.format("populdate the param startDuration %s", startDuration));
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("startDuration", startDuration);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		if( null != endDuration ){
    			logger.info(String.format("populdate the param endDuration %s", endDuration));
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("endDuration", endDuration);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		if( null != startDate ){
    			logger.info(String.format("populdate the param startDate %s", startDate));
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("startDate", startDate);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		if( null != endDate ){
    			logger.info(String.format("populdate the param endDate %s", endDate));
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("endDate", endDate);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		if( null != last2Duration ){
    			logger.info(String.format("populdate the param last2Duration %s", last2Duration));
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("last2Duration", last2Duration);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		IRunAndRenderTask task = engine.createRunAndRenderTask(design);  
    		logger.info("create and render the refresh task");
    		IRenderOption options = new PDFRenderOption();
			options.setOutputFormat("pdf");
			options.setOutputFileName(reportFileName);
    		
    		task.setRenderOption(options);
			task.setParameterValues(parameterMap);
    		logger.info("start to run the home pdf report task");
    		task.run();
    		task.close();  
    		logger.info("the home pdf report taks is closed.");
    	}catch( Exception ex){
    		logger.error("fail to generate the home pdf report",ex);
    	}  
    }  
    
    public void runAllDailyReport(String designPath, String paramDate, String reportFileName){
    	try{
    		logger.info(String.format("run the all dsm or rsm daily report, the file name is %s",reportFileName));
    		IReportRunnable design = null;  
    		HashMap parameterMap = new HashMap();
    		//Open the report design  
    		design = engine.openReportDesign(designPath);  
    		
    		if( null != paramDate && !"".equalsIgnoreCase(paramDate) ){
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("paramDate", paramDate);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
    		IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
			Collection parameters = paramTask.getParameterDefns(false);
			Map paramValues = new HashMap();
			paramValues.put("portRateBase", CustomizedProperty.getContextProperty("portRateBase", "24"));
			evaluateParameterValues(parameterMap,parameters,paramValues);
    		
    		IRunAndRenderTask task = engine.createRunAndRenderTask(design);  
    		logger.info("create and render the task");
    		IRenderOption options = null;
			options = new EXCELRenderOption();  
			options.setOutputFormat("xlsx");
			options.setOutputFileName(reportFileName);
			options.setOption(IExcelRenderOption.OFFICE_VERSION, "office2007");
    		
    		task.setRenderOption(options);
			task.setParameterValues(parameterMap);
    		logger.info("start to run the task");
    		task.run();
    		task.close();
    		logger.info("the taks is closed.");
    	}catch( Exception ex){
    		logger.error("fail to generate the all daily report",ex);
    	}  
    }
    

    public void runRe2Report(String designPath, String reportFileName){
    	try{
    		logger.info(String.format("run the re2 report, the file name is %s",reportFileName));
    		IReportRunnable design = null;  
            HashMap parameterMap = new HashMap();
            //Open the report design  
            design = engine.openReportDesign(designPath);  
            
            String startDate = DateUtils.getTheBeginDateOfRefreshDate(new Date());
            String endDate = DateUtils.getTheEndDateOfRefreshDate(new Date());
            
            String startDuration = startDate+"-"+endDate;
            if( null != startDuration ){
    			logger.info(String.format("populdate the param startDuration %s", startDuration));
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("startDuration", startDuration);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
    		
            String last12WeekDuration = DateUtils.getEndDurationByStartDate(startDate);
    		if( null != last12WeekDuration ){
    			logger.info(String.format("populdate the param last12WeekDuration %s", last12WeekDuration));
    			IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
    			Collection parameters = paramTask.getParameterDefns(false);
    			Map paramValues = new HashMap();
    			paramValues.put("endDuration", last12WeekDuration);
    			evaluateParameterValues(parameterMap,parameters,paramValues);
    		}
            
            IRunAndRenderTask task = engine.createRunAndRenderTask(design);  
            logger.info("create and render the re2 task");
            IRenderOption options = null;
            options = new PDFRenderOption();
            options.setOutputFormat("pdf");
            options.setOutputFileName(reportFileName);
            
            task.setRenderOption(options);
            task.setParameterValues(parameterMap);
            
    		logger.info("start to run the task");
    		task.run();
    		task.close();
    		logger.info("the taks is closed.");
    	}catch( Exception ex){
    		logger.error("fail to generate the all daily report",ex);
    	}  
    }
    
    //用于启动报表平台  
    public void startPlatform(){  
        try{
            config = new EngineConfig( );  
            config.setBIRTHome("");//
            config.setLogConfig(CustomizedProperty.getContextProperty("birt_log_path", "c:/chalet/birt/logs"), Level.FINE);
            Platform.startup( config );  
            IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );  
            engine = factory.createReportEngine( config );  
        }catch( Exception e){  
            e.printStackTrace();
        }  
    }
    public void startHtmlPlatform(){
    	logger.info("start html engine");
    	try{
    		config = new EngineConfig( );  
    		config.setBIRTHome("");//
    		config.setLogConfig(CustomizedProperty.getContextProperty("birt_log_path", "c:/chalet/birt/logs"), Level.FINE);
    		HTMLEmitterConfig emitterConfig = new HTMLEmitterConfig( ); 
    		emitterConfig.setActionHandler( new HTMLActionHandler( ) ); 
    		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler( ); 
    		emitterConfig.setImageHandler( imageHandler );
    		config.getEmitterConfigs( ).put( "html", emitterConfig );
    		
    		Platform.startup( config );  
    		IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );  
    		engine = factory.createReportEngine( config );  
    	}catch(Exception e){
    		logger.error("fail to start html platform",e);
    	}
    }
    //用于停止报表平台  
    public void stopPlatform(){  
        if( null != engine ){
            engine.destroy();  
        }
        Platform.shutdown();  
    }  
    
    private void evaluateParameterValues(HashMap parameterMap,Collection paramDefns, Map params) {
        Iterator iter = paramDefns.iterator();
        while (iter.hasNext()) {
            IParameterDefnBase pBase = (IParameterDefnBase) iter.next();
            if (pBase instanceof IScalarParameterDefn) {
                IScalarParameterDefn paramDefn = (IScalarParameterDefn) pBase;
                String paramName = paramDefn.getName();
                String inputValue = (String) params.get(paramName);
//                int paramType = paramDefn.getDataType();
                try {
//                    Object paramValue = stringToObject(paramType, inputValue);
                    if (inputValue != null) {
                        parameterMap.put(paramName, inputValue);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void main (String[] args) {  
        BirtReportUtils html = new BirtReportUtils();  
        html.startHtmlPlatform();
        System.out.println("Started");
        html.runHomeReport( "D://workspace_birt//MyGit//LSKPI//WebContent//reportDesigns/weeklyHomeReportForWebBU.rptdesign","18501622299","2014.09.08-2014.09.14","2014.06.23-2014.06.29","D://test//testHome.html","html","D://test//","D://test//"); 
        html.stopPlatform();  
        System.out.println("Finished");  
    }
}
