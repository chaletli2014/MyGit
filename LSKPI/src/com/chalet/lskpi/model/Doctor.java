package com.chalet.lskpi.model;

public class Doctor {

    private int id;
    private String name;
    private String code;
    private String hospitalCode;
    private String salesCode;
    /**
     * 0 active 1 delete.
     */
    private String status;
    private String reason;
    
    private String hospitalName;
    private String salesName;
    
    private String nameWithHosName;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getHospitalCode() {
        return hospitalCode;
    }
    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }
    public String getSalesCode() {
        return salesCode;
    }
    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getHospitalName() {
        return hospitalName;
    }
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
    public String getSalesName() {
        return salesName;
    }
    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }
    public String getNameWithHosName() {
        return this.name+"("+this.hospitalName+")";
    }
}
