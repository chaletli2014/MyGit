package com.chalet.lskpi.model;

import java.util.Date;

public class HomeData {

    private int id;
    private String region;
    private String rsmRegion;
    private String dsmName;
    private String salesName;
    private String hospitalName;
    private String hospitalCode;
    private int doctorId;
    private String drName;
    /**
     * 每周新病人人次.
     */
    private int salenum;
    /**
     * 哮喘*患者人次.
     */
    private int asthmanum;
    /**
     * 处方>=8天的哮喘维持期病人次.
     */
    private int ltenum;
    /**
     * 维持期病人中推荐使用令舒的人次.
     */
    private int lsnum;
    /**
     * 8<=DOT<15天，病人次.
     */
    private int efnum;
    /**
     * 15<=DOT<30天，病人次.
     */
    private int ftnum;
    /**
     * DOT>=30天,病人次.
     */
    private int lttnum;
    
    private Date createDate;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getDoctorId() {
        return doctorId;
    }
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    
    /**
     * 每周新病人人次.
     * @return int
     */
    public int getSalenum() {
        return salenum;
    }
    
    /**
     * 每周新病人人次.
     * @param salenum int
     */
    public void setSalenum(int salenum) {
        this.salenum = salenum;
    }
    
    /**
     * 哮喘*患者人次.
     * @return int
     */
    public int getAsthmanum() {
        return asthmanum;
    }
    /**
     * 哮喘*患者人次.
     * @param asthmanum int
     */
    public void setAsthmanum(int asthmanum) {
        this.asthmanum = asthmanum;
    }
    /**
     * 处方>=8天的哮喘维持期病人次.
     * @return int
     */
    public int getLtenum() {
        return ltenum;
    }
    /**
     * 处方>=8天的哮喘维持期病人次.
     * @param ltenum int
     */
    public void setLtenum(int ltenum) {
        this.ltenum = ltenum;
    }
    /**
     * 维持期病人中推荐使用令舒的人次.
     * @return int
     */
    public int getLsnum() {
        return lsnum;
    }
    /**
     * 维持期病人中推荐使用令舒的人次.
     * @param lsnum int
     */
    public void setLsnum(int lsnum) {
        this.lsnum = lsnum;
    }
    
    /**
     * 8<=DOT<15天，病人次.
     * @return int
     */
    public int getEfnum() {
        return efnum;
    }
    /**
     * 8<=DOT<15天，病人次.
     * @param efnum int
     */
    public void setEfnum(int efnum) {
        this.efnum = efnum;
    }
    
    /**
     * 15<=DOT<30天，病人次.
     * @return int
     */
    public int getFtnum() {
        return ftnum;
    }
    /**
     * 15<=DOT<30天，病人次.
     * @param ftnum int
     */
    public void setFtnum(int ftnum) {
        this.ftnum = ftnum;
    }
    
    /**
     * DOT>=30天,病人次.
     * @return int
     */
    public int getLttnum() {
        return lttnum;
    }
    /**
     * DOT>=30天,病人次.
     * @param lttnum int
     */
    public void setLttnum(int lttnum) {
        this.lttnum = lttnum;
    }
    
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getRsmRegion() {
        return rsmRegion;
    }
    public void setRsmRegion(String rsmRegion) {
        this.rsmRegion = rsmRegion;
    }
    public String getDsmName() {
        return dsmName;
    }
    public void setDsmName(String dsmName) {
        this.dsmName = dsmName;
    }
    public String getSalesName() {
        return salesName;
    }
    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }
    public String getHospitalName() {
        return hospitalName;
    }
    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }
    public String getDrName() {
        return drName;
    }
    public void setDrName(String drName) {
        this.drName = drName;
    }
    public String getHospitalCode() {
        return hospitalCode;
    }
    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
