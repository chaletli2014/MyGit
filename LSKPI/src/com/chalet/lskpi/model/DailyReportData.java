package com.chalet.lskpi.model;
/**
 * @author Chalet
 * @version 创建时间：2013年12月19日 下午11:01:13
 * 类说明
 */

public class DailyReportData {
	
	private String rsmName;
	private String rsmCode;
	private double inRate;
	private double whRate;
	private double averageDose;
	
	public String getRsmName() {
		return rsmName;
	}
	public void setRsmName(String rsmName) {
		this.rsmName = rsmName;
	}
	public String getRsmCode() {
		return rsmCode;
	}
	public void setRsmCode(String rsmCode) {
		this.rsmCode = rsmCode;
	}
	public double getAverageDose() {
		return averageDose;
	}
	public void setAverageDose(double averageDose) {
		this.averageDose = averageDose;
	}
	public double getInRate() {
		return inRate;
	}
	public void setInRate(double inRate) {
		this.inRate = inRate;
	}
	public double getWhRate() {
		return whRate;
	}
	public void setWhRate(double whRate) {
		this.whRate = whRate;
	}

}
