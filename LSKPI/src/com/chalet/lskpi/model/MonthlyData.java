package com.chalet.lskpi.model;

import java.util.Date;

/**
 * @author Chalet
 * @version 创建时间：2013年12月24日 上午2:19:50
 * 类说明
 */

public class MonthlyData {

	private int id;
	private double pedemernum;
	private double pedroomnum;
	private double resnum;
	private double othernum;
	private String operatorName;
	private String operatorCode;
	private String hospitalCode;
	private String dsmCode;
	private String rsmRegion;
	private String region;
	
	private String dsmName;
	private String hospitalName;
	private Date createDate;
	
	private int hosNum;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public void setOthernum(int othernum) {
		this.othernum = othernum;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
    public String getDsmCode() {
        return dsmCode;
    }
    public void setDsmCode(String dsmCode) {
        this.dsmCode = dsmCode;
    }
    public String getRsmRegion() {
        return rsmRegion;
    }
    public void setRsmRegion(String rsmRegion) {
        this.rsmRegion = rsmRegion;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
	public String getDsmName() {
		return dsmName;
	}
	public void setDsmName(String dsmName) {
		this.dsmName = dsmName;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
    public int getHosNum() {
        return hosNum;
    }
    public void setHosNum(int hosNum) {
        this.hosNum = hosNum;
    }
    public double getPedemernum() {
        return pedemernum;
    }
    public void setPedemernum(double pedemernum) {
        this.pedemernum = pedemernum;
    }
    public double getPedroomnum() {
        return pedroomnum;
    }
    public void setPedroomnum(double pedroomnum) {
        this.pedroomnum = pedroomnum;
    }
    public double getResnum() {
        return resnum;
    }
    public void setResnum(double resnum) {
        this.resnum = resnum;
    }
    public double getOthernum() {
        return othernum;
    }
    public void setOthernum(double othernum) {
        this.othernum = othernum;
    }
}
