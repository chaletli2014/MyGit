package com.chalet.lskpi.model;
/**
 * @author Chalet
 * @version 创建时间：2014年1月2日 下午9:45:30
 * 类说明
 */

public class ReportProcessDataDetail {

	private String hospitalName;
	private String hospitalCode;
	private int inNum;
	private String saleCode;
	private String saleName;
	private String isAssessed;
	
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public int getInNum() {
		return inNum;
	}
	public void setInNum(int inNum) {
		this.inNum = inNum;
	}
	public String getSaleCode() {
		return saleCode;
	}
	public void setSaleCode(String saleCode) {
		this.saleCode = saleCode;
	}
	public String getSaleName() {
		return saleName;
	}
	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}
    public String getIsAssessed() {
        return isAssessed;
    }
    public void setIsAssessed(String isAssessed) {
        this.isAssessed = isAssessed;
    }
}
