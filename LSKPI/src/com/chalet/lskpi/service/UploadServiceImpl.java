package com.chalet.lskpi.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chalet.lskpi.dao.HospitalDAO;
import com.chalet.lskpi.dao.UserDAO;

@Service("uploadService")
public class UploadServiceImpl implements UploadService {

    @Autowired
    @Qualifier("userDAO")
    private UserDAO userDAO;
    
    @Autowired
    @Qualifier("hospitalDAO")
    private HospitalDAO hospitalDAO;
    
    Logger logger = Logger.getLogger(UploadServiceImpl.class);

    @Transactional(rollbackFor=Exception.class)
    public void uploadAllData(Map<String, List> allInfos) throws Exception {
        try{
            long end = System.currentTimeMillis();
            logger.info("remove the old user infos firstly");
            userDAO.delete();
            userDAO.insert(allInfos.get("users"));
            long finish = System.currentTimeMillis();
            logger.info("time spent to insert the user infos into DB is " + (finish-end) + " ms");
            
            logger.info("begin to handle the hospital");
            hospitalDAO.delete();
            hospitalDAO.insert(allInfos.get("hospitals"));
            long hosFinish = System.currentTimeMillis();
            logger.info("time spent to insert the hospital infos into DB is " + (hosFinish-finish) + " ms");
            
            logger.info("begin to handle the hospital user reference");
            userDAO.deleteHosUsers();
            userDAO.insertHosUsers(allInfos.get("hosUsers"));
            long hosUserFinish = System.currentTimeMillis();
            logger.info("time spent to insert the hospital infos into DB is " + (hosUserFinish - hosFinish) + " ms");
        }catch(Exception e){
            logger.error("fail to update the all data,",e);
            throw new Exception("更新层级失败");
        }
    }
    
    
}
