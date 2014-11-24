package com.chalet.lskpi.service;

import java.util.List;
import java.util.Map;

import com.chalet.lskpi.model.Doctor;

public interface UploadService {

    public void uploadAllData(Map<String, List> allInfos) throws Exception;
    
    public List<Doctor> uploadDoctorData(List<Doctor> doctors) throws Exception;
}
