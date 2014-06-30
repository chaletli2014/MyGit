package com.chalet.lskpi.service;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.Hospital;
import com.chalet.lskpi.model.ChestSurgeryData;
import com.chalet.lskpi.model.UserInfo;

public interface ChestSurgeryService {

    public ChestSurgeryData getChestSurgeryDataByHospital(String hospitalCode) throws Exception;
    public List<ChestSurgeryData> getChestSurgeryDataByDate(Date createdatebegin, Date createdateend) throws Exception;
    public ChestSurgeryData getChestSurgeryDataByHospitalAndDate(String hospitalCode, Date createdate) throws Exception;
    public ChestSurgeryData getChestSurgeryDataById(int id) throws Exception;
    public void insert(ChestSurgeryData chestSurgeryData, UserInfo operator, Hospital hospital) throws Exception;
    public void insert(ChestSurgeryData chestSurgeryData) throws Exception;
    public void update(ChestSurgeryData chestSurgeryData, UserInfo operator) throws Exception;
}
