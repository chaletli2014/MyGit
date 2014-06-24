package com.chalet.lskpi.model;
/**
 * @author Chalet
 * @version 创建时间：2014年5月18日 下午3:53:03
 * 该类用于每月上报率统计
 */

public class MonthlyInRateData {

	private String duration;
	private String rsd;
	private String rsm;
	private double resInRate;
	private double pedInRate;
	
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getRsd() {
		return rsd;
	}
	public void setRsd(String rsd) {
		this.rsd = rsd;
	}
	public String getRsm() {
		return rsm;
	}
	public void setRsm(String rsm) {
		this.rsm = rsm;
	}
	public double getResInRate() {
		return resInRate;
	}
	public void setResInRate(double resInRate) {
		this.resInRate = resInRate;
	}
	public double getPedInRate() {
		return pedInRate;
	}
	public void setPedInRate(double pedInRate) {
		this.pedInRate = pedInRate;
	}
}
