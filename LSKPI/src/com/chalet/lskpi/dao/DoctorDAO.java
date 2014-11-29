package com.chalet.lskpi.dao;

import java.util.Date;
import java.util.List;

import com.chalet.lskpi.model.Doctor;
import com.chalet.lskpi.model.DoctorToBeDeleted;

public interface DoctorDAO {

    public List<Doctor> getDoctorsByDsmCode(String dsmCode) throws Exception;
    public List<Doctor> getDoctorsBySalesCode(String salesCode) throws Exception;
    public List<Doctor> getDoctorsByRegion(String region) throws Exception;
    public List<Doctor> getDoctorsByRegionCenter(String regionCenter) throws Exception;
    public int getExistedDrNumByHospitalCode( String hospitalCode, String drName ) throws Exception;
    public int getExistedDrNumByHospitalCodeExcludeSelf( long dataId, String hospitalCode, String drName ) throws Exception;
    public int getTotalDrNumOfHospital(String hospitalCode) throws Exception;
    public int getTotalRemovedDrNumOfHospital(String hospitalCode) throws Exception;
    public int insertDoctor(Doctor doctor) throws Exception;
    public void updateDoctor(Doctor doctor) throws Exception;
    public void deleteDoctor(Doctor doctor) throws Exception;
    public void cleanDoctor() throws Exception;
    public void insertDoctors(List<Doctor> doctors) throws Exception;
    public void backupDoctor(Doctor doctor) throws Exception;
    public void updateDoctorRelationship(int doctorId, String salesCode) throws Exception;
    public Doctor getDoctorById(int doctorId) throws Exception;
    
    public boolean drHasLastWeekData(int doctorId, Date beginDate, Date endDate) throws Exception;
    
    public boolean isDrExists(String hospitalCode,String drName) throws Exception;
    
    public List<DoctorToBeDeleted> getAllDoctorsToBeDeleted() throws Exception;
    public void storeToBeDeletedDoctor(DoctorToBeDeleted doctor, String currentUserTel) throws Exception;
    public String getDeleteReasonByDrId(int drId) throws Exception;
    public void updateApprovalStatus(Doctor doctor, String currentUserTel, String status) throws Exception;
}
