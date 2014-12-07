package com.chalet.lskpi.model;
/**
 * @author Chalet
 * @version 创建时间：2014年5月18日 下午3:53:03
 * 该类用于每月上报率统计
 */

public class MonthlyStatisticsData {

	private String duration;
	private String rsd;
	private String rsm;
	private String dsmName;
	private String dsmCode;
	private double resInRate;
	private double pedInRate;
	
	private double inRate;
	private double coreInRate;
	private double emergingInRate;
	private double whRate;
	private double coreWhRate;
	private double emergingWhRate;
	private double pnum;
	private double aenum;
	private double risknum;
	private double lsnum;
	private double averageDose;
	
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
	public double getInRate() {
		return inRate;
	}
	public void setInRate(double inRate) {
		this.inRate = inRate;
	}
	public double getCoreInRate() {
		return coreInRate;
	}
	public void setCoreInRate(double coreInRate) {
		this.coreInRate = coreInRate;
	}
	public double getEmergingInRate() {
		return emergingInRate;
	}
	public void setEmergingInRate(double emergingInRate) {
		this.emergingInRate = emergingInRate;
	}
	public double getWhRate() {
		return whRate;
	}
	public void setWhRate(double whRate) {
		this.whRate = whRate;
	}
	public double getCoreWhRate() {
		return coreWhRate;
	}
	public void setCoreWhRate(double coreWhRate) {
		this.coreWhRate = coreWhRate;
	}
	public double getEmergingWhRate() {
		return emergingWhRate;
	}
	public void setEmergingWhRate(double emergingWhRate) {
		this.emergingWhRate = emergingWhRate;
	}
	public double getPnum() {
		return pnum;
	}
	public void setPnum(double pnum) {
		this.pnum = pnum;
	}
	public double getAenum() {
		return aenum;
	}
	public void setAenum(double aenum) {
		this.aenum = aenum;
	}
	public double getRisknum() {
		return risknum;
	}
	public void setRisknum(double risknum) {
		this.risknum = risknum;
	}
	public double getLsnum() {
		return lsnum;
	}
	public void setLsnum(double lsnum) {
		this.lsnum = lsnum;
	}
	public double getAverageDose() {
		return averageDose;
	}
	public void setAverageDose(double averageDose) {
		this.averageDose = averageDose;
	}
	public String getDsmName() {
		return dsmName;
	}
	public void setDsmName(String dsmName) {
		this.dsmName = dsmName;
	}
	public String getDsmCode() {
		return dsmCode;
	}
	public void setDsmCode(String dsmCode) {
		this.dsmCode = dsmCode;
	}
}
