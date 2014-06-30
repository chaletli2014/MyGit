package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.UserInfo;

public interface ChestSurgeryDAO {

    public ChestSurgeryData getChestSurgeryDataByHospital(String hospitalCode) throws Exception;
    public List<ChestSurgeryData> getChestSurgeryDataByDate(Date createdatebegin, Date createdateend) throws Exception;
    public ChestSurgeryData getChestSurgeryDataByHospitalAndDate(String hospitalCode, Date createdate) throws Exception;
    public ChestSurgeryData getChestSurgeryDataById(int id) throws Exception;
    public void insert(ChestSurgeryData chestSurgeryData, UserInfo operator, Hospital hospital) throws Exception;
    public void insert(ChestSurgeryData chestSurgeryData,String dsmCode) throws Exception;
    public void update(ChestSurgeryData chestSurgeryData) throws Exception;
}
