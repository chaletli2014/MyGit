package com.chalet.lskpi.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class CustomizedProperty extends
		PropertyPlaceholderConfigurer {

	private static Map<String, String> ctxPropertiesMap; 
	  
    @Override 
    protected void processProperties( 
            ConfigurableListableBeanFactory beanFactoryToProcess, 
            Properties props) throws BeansException { 
        super.processProperties(beanFactoryToProcess, props); 
        ctxPropertiesMap = new HashMap<String, String>(); 
        for (Object key : props.keySet()) { 
            String keyStr = key.toString(); 
            String value = props.getProperty(keyStr); 
            ctxPropertiesMap.put(keyStr, value); 
        }   
    } 
 
    public static String getContextProperty(String name) { 
        return ctxPropertiesMap.get(name); 
    }
    
    public static String getContextProperty(String name,String defaultValue) { 
    	String value = ctxPropertiesMap.get(name);
    	return (value==null||value.equalsIgnoreCase(""))?defaultValue:value; 
    }
    
    public static List<Object> getContextProperites(String prefix){
    	List<Object> properties = new ArrayList<Object>();
    	List<String> keys = new ArrayList<String>();
    	Iterator<String> ite = ctxPropertiesMap.keySet().iterator();
    	while( ite.hasNext() ){
    		String key = ite.next();
    		if( key.indexOf(prefix) > -1 ){
    			keys.add(key);
    		}
    	}
    	Collections.sort(keys);
    	for( String key : keys ){
    		properties.add(ctxPropertiesMap.get(key));
    	}
    	return properties;
    }
}
