package com.chalet.lskpi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.chalet.lskpi.dao.DoctorDAO;
import com.chalet.lskpi.dao.HospitalDAO;
import com.chalet.lskpi.dao.PropertyDAO;
import com.chalet.lskpi.dao.UserDAO;
import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.Property;

@Service("uploadService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
public class UploadServiceImpl implements UploadService {

    @Autowired
    @Qualifier("userDAO")
    private UserDAO userDAO;
    
    @Autowired
    @Qualifier("hospitalDAO")
    private HospitalDAO hospitalDAO;
    
    @Autowired
    @Qualifier("doctorDAO")
    private DoctorDAO doctorDAO;
    
    @Autowired
    @Qualifier("propertyDAO")
    private PropertyDAO propertyDAO;
    
    Logger logger = Logger.getLogger(UploadServiceImpl.class);

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
            
            List<Property> properties = allInfos.get("regionNames");
            for( Property property : properties ){
                try{
                    propertyDAO.getPropertyValueByName(property.getPropertyName());
                    
                    propertyDAO.update(property);
                }catch(EmptyResultDataAccessException eda){
                    propertyDAO.insert(property);
                }
            }
            long propertyFinish = System.currentTimeMillis();
            logger.info("time spent to insert the property infos into DB is " + (propertyFinish - hosUserFinish) + " ms");
            
        }catch(Exception e){
            logger.error("fail to update the all data,",e);
            throw new Exception("更新层级失败");
        }
    }

    public List<Doctor> uploadDoctorData(List<Doctor> doctors) throws Exception {
    	List<Doctor> duplicateDoctors = new ArrayList<Doctor>();
        try{
            long start = System.currentTimeMillis();
//            doctorDAO.cleanDoctor();
            int drCount = 0;
            String hospitalCode = "";
            
            for( Doctor doctor : doctors ){
            	boolean doctorExists = false;
            	hospitalCode = doctor.getHospitalCode();
            	doctorExists = doctorDAO.isDrExists(hospitalCode,doctor.getName());
            	if( !doctorExists ){
            		drCount = doctorDAO.getTotalDrNumOfHospital(hospitalCode)+doctorDAO.getTotalRemovedDrNumOfHospital(hospitalCode);
            		doctor.setCode(String.valueOf(drCount+1));
            		doctorDAO.insertDoctor(doctor);
            	}else{
            		duplicateDoctors.add(doctor);
            	}
            }
            
            long finish = System.currentTimeMillis();
            logger.info("time spent to insert the doctors infos into DB is " + (finish-start) + " ms");
        }catch(Exception e){
            logger.error("fail to update the doctor data,",e);
            throw new Exception("更新医生失败");
        }
        return duplicateDoctors;
    }
    
    
}
