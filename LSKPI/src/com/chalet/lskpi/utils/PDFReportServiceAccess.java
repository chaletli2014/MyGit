package com.chalet.lskpi.utils;
/**
 * @author Chalet
 * @version 创建时间：2013年12月17日 下午9:08:08
 * 类说明
 */
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
  
public class PDFReportServiceAccess  
{  
    protected static boolean initStatus = false;  
    private static IReportEngine engine = null;  
    private static EngineConfig config = null;  
    private static IReportRunnable design = null;  
    private static PDFRenderOption ro = null;  
    
    private Logger logger = Logger.getLogger(PDFReportServiceAccess.class);
  
    public void initilize(){
	    if ( initStatus == true ){
	    	return;  
	    }
	    try{
	    	logger.info("initilize");
	        config = new EngineConfig();  
	        config.setEngineHome("");
	        config.setLogConfig( "D:/chalet/logs", Level.FINE );
	  
	        Platform.startup( config );
	        IReportEngineFactory factory = ( IReportEngineFactory ) Platform  
	            .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );  
	        engine = factory.createReportEngine( config );  
	        engine.changeLogLevel( Level.WARNING );  
	  
	        ro = new PDFRenderOption();
	        config.getEmitterConfigs().put( "pdf", ro );  
	        initStatus = true;  
	    }  
	    catch ( Exception ex ){  
	        ex.printStackTrace();  
	        initStatus = false;  
	    }  
    }
  
    public void release(){
    	logger.info("release the engine");
    	engine.shutdown();  
    	Platform.shutdown();  
    	initStatus = false;  
    }  
  
    protected OutputStream run( String filename, HashMap parameters ) throws EngineException{
    	logger.info("run the task, filename is " + filename);
	    design = engine.openReportDesign( filename );  
	    // Create task to run and render the report,  
	    IRunAndRenderTask task = engine.createRunAndRenderTask( design );  
	    HashMap contextMap = new HashMap();  
	    contextMap.put( EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT, ro);  
	    task.setAppContext( contextMap );  
	    if(null != parameters){
	    	task.setParameterValues( parameters );  
	    	task.validateParameters();  
	    }
	    OutputStream os = new ByteArrayOutputStream();  
	    ro.setOutputStream( os );  
	    ro.setOutputFormat( "pdf" );  
	    task.setRenderOption( ro );  
	    task.run();  
	    task.close();  
	    return os;  
    }
  
    public OutputStream call( String filename, HashMap parameters ) throws EngineException{  
    	initilize();  
    	OutputStream os = run( filename, parameters );  
    	release();  
    	return os;
    } 
    
    public static void main( String[] args ){
    	ByteArrayOutputStream bos = null;  
    	PDFReportServiceAccess ebr = new PDFReportServiceAccess();
//    	String filename = "D:/WorkSpace4.3/LSKPI/WebContent/reportDesigns/weeklyPEDReport.rptdesign";  
    	String pedfilename = "D:/chalet/workspace/LSKPI/WebContent/reportDesigns/weeklyPEDReport.rptdesign";
    	String resfilename = "D:/chalet/workspace/LSKPI/WebContent/reportDesigns/weeklyRESReport.rptdesign";
    	try {
    		
    		HashMap parameters = new HashMap();
    		parameters.put("userTel", "13764132416");
    		
    		bos = ( ByteArrayOutputStream ) ebr.call( pedfilename, parameters );  
    		OutputStream fis = new FileOutputStream( "d:/儿科周报样例.pdf" );  
    		bos.writeTo( fis );
    		
    		bos = ( ByteArrayOutputStream ) ebr.call( resfilename, parameters );  
    		OutputStream fis2 = new FileOutputStream( "d:/呼吸科周报样例.pdf" );  
    		bos.writeTo( fis2 );
    	}
	    catch ( Exception e ){
	        e.printStackTrace();
	    }
    } 
}  