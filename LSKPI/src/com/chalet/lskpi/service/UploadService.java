package com.chalet.lskpi.service;

import java.util.List;
import java.util.Map;

import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.Hospital;

public interface UploadService {

    public void uploadAllData(Map<String, List> allInfos) throws Exception;
    
    public List<Doctor> uploadDoctorData(List<Doctor> doctors) throws Exception;
    
    public void uploadWhbwHospital(List<Hospital> whbwHospitals) throws Exception;
}
