package com.chalet.lskpi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import org.apache.log4j.Logger;

public class FileUtils {
    
    private static Logger logger = Logger.getLogger(FileUtils.class);

    public static boolean copySourceFile2TargetFile(File sourceFile, File targetFile){
        try{
            File parent = targetFile.getParentFile();
            if(!parent.exists() && !parent.mkdirs()){
                parent.mkdirs();
            }
            
            int length=2097152;
            FileInputStream in=new FileInputStream(sourceFile);
            FileOutputStream out=new FileOutputStream(targetFile);
            FileChannel inC=in.getChannel();
            FileChannel outC=out.getChannel();
            int i=0;
            while(true){
                if(inC.position()==inC.size()){
                    inC.close();
                    outC.close();
                    return true;
                }
                if((inC.size()-inC.position())<20971520)
                    length=(int)(inC.size()-inC.position());
                else
                    length=20971520;
                inC.transferTo(inC.position(),length,outC);
                inC.position(inC.position()+length);
                i++;
            }
        }catch(Exception e){
            logger.error("fail to copy files,",e);
            return false;
        }
        
    }
}
