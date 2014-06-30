package com.chalet.lskpi.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.chalet.lskpi.dao.ChestSurgeryDAO;
import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.UserInfo;

@Service("chestSurgeryService")
public class ChestSurgeryServiceImpl implements ChestSurgeryService {
    
    @Autowired
    @Qualifier("chestSurgeryDAO")
    private ChestSurgeryDAO chestSurgeryDAO;
    
    @Autowired
    @Qualifier("userService")
    private UserService userService;
    
    @Autowired
    @Qualifier("hospitalService")
    private HospitalService hospitalService;
    
    private Logger logger = Logger.getLogger(ChestSurgeryServiceImpl.class);

    public ChestSurgeryData getChestSurgeryDataByHospital(String hospitalCode) throws Exception {
        try{
            return chestSurgeryDAO.getChestSurgeryDataByHospital(hospitalCode);
        } catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(Exception e){
            logger.error("fail to get the chest surgery data by hospital - " + hospitalCode,e);
            return null;
        }
    }

    public List<ChestSurgeryData> getChestSurgeryDataByDate(Date createdatebegin, Date createdateend) throws Exception {
        try{
            createdateend = new Date(createdateend.getTime() + 1 * 24 * 60 * 60 * 1000);
            return chestSurgeryDAO.getChestSurgeryDataByDate(createdatebegin, createdateend);
        } catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(IncorrectResultSizeDataAccessException ire){
            logger.error(ire.getMessage());
            return new ArrayList<ChestSurgeryData>();
        }catch(Exception e){
            logger.error(String.format("fail to get the chest surgery data by data from %s to %s", createdatebegin,createdateend),e);
            return new ArrayList<ChestSurgeryData>();
        }
    }

    public ChestSurgeryData getChestSurgeryDataByHospitalAndDate(String hospitalCode, Date createdate) throws Exception {
        try{
            return chestSurgeryDAO.getChestSurgeryDataByHospitalAndDate(hospitalCode, createdate);
        } catch(EmptyResultDataAccessException erd){
            logger.info("there is no record found.");
            return null;
        } catch(IncorrectResultSizeDataAccessException ire){
            logger.error(ire.getMessage());
            ChestSurgeryData chestData = new ChestSurgeryData();
            chestData.setDataId(0);
            return chestData;
        }catch(Exception e){
            logger.error(String.format("fail to get the chest surgery data by hospital - ", hospitalCode),e);
            ChestSurgeryData chestData = new ChestSurgeryData();
            chestData.setDataId(0);
            return chestData;
        }
    }

    public ChestSurgeryData getChestSurgeryDataById(int id) throws Exception {
        try{
            return chestSurgeryDAO.getChestSurgeryDataById(id);
        }catch(Exception e){
            logger.error(String.format("fail to get the respirology data by ID - ", id),e);
            return null;
        }
    }

    public void insert(ChestSurgeryData chestSurgeryData, UserInfo operator, Hospital hospital) throws Exception {
        chestSurgeryDAO.insert(chestSurgeryData, operator, hospital);
    }

    public void insert(ChestSurgeryData chestSurgeryData) throws Exception {
        String dsmCode = "";
        try{
            UserInfo primarySales = hospitalService.getPrimarySalesOfHospital(chestSurgeryData.getHospitalCode());
            if( null != primarySales ){
                chestSurgeryData.setSalesCode(primarySales.getUserCode());
                dsmCode = (primarySales.getSuperior()==null||"".equalsIgnoreCase(primarySales.getSuperior()))?primarySales.getUserCode():primarySales.getSuperior();
            }
        }catch(EmptyResultDataAccessException erd){
            logger.info("there is no user found whose code is " + chestSurgeryData.getSalesCode());
        }
        chestSurgeryDAO.insert(chestSurgeryData,dsmCode);
    }

    public void update(ChestSurgeryData chestSurgeryData, UserInfo operator) throws Exception {
        chestSurgeryDAO.update(chestSurgeryData);
    }

    
}
