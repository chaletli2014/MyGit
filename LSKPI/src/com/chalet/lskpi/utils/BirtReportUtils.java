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
            
            if( null != telephone && !"".equalsIgnoreCase(telephone) ){
                IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
                Collection parameters = paramTask.getParameterDefns(false);
                Map paramValues = new HashMap();
                paramValues.put("userTel", telephone);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            if( null != userCode && !"".equalsIgnoreCase(userCode) ){
                IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
                Collection parameters = paramTask.getParameterDefns(false);
                Map paramValues = new HashMap();
                paramValues.put("userCode", userCode);
                evaluateParameterValues(parameterMap,parameters,paramValues);
            }
            
            if( null != hospitalCode && !"".equalsIgnoreCase(hospitalCode) ){
            	IGetParameterDefinitionTask paramTask = engine.createGetParameterDefinitionTask(design);
            	Collection parameters = paramTask.getParameterDefns(false);
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
    public void runRefreshReport(String designPath, String telephone, String startDate, String endDate, String reportFileName, String fileType, String reportImgPath, String baseImgPath){
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
                    || ( null != endDate && !"".equalsIgnoreCase(endDate) ) ){
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
    //用于启动报表平台  
    public void startPlatform(){  
        try{
            config = new EngineConfig( );  
            config.setBIRTHome("");//
            config.setLogConfig(CustomizedProperty.getContextProperty("birt_log_path", "d:/chalet/birt/logs"), Level.FINE);
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
    		config.setLogConfig(CustomizedProperty.getContextProperty("birt_log_path", "d:/chalet/birt/logs"), Level.FINE);
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
//        BirtReportUtils html = new BirtReportUtils();  
//        html.startPlatform();  
//        System.out.println("Started");
//        html.runReport("D:\\workspace_birt\\LSKPI\\dsmPEDDaily.rptdesign","13137437096","d:\\test.xls","","");  
//        html.stopPlatform();  
//        System.out.println("Finished");  
    }
}
