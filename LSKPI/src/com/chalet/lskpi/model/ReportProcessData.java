package com.chalet.lskpi.model;
/**
 * @author Chalet
 * @version 创建时间：2014年1月2日 下午9:42:06
 * 类说明
 */

public class ReportProcessData {
	private String telephone;
	private String name;
	private int hosNum;
	private int validInNum;
	private double currentInRate;
	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public int getHosNum() {
		return hosNum;
	}
	public void setHosNum(int hosNum) {
		this.hosNum = hosNum;
	}
	public int getValidInNum() {
		return validInNum;
	}
	public void setValidInNum(int validInNum) {
		this.validInNum = validInNum;
	}
	public double getCurrentInRate() {
		return currentInRate;
	}
	public void setCurrentInRate(double currentInRate) {
		this.currentInRate = currentInRate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
