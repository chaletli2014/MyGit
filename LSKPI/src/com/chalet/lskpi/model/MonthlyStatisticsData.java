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
	
	private double coreAverageDose;
	private double coreAverageDose2;
	private double coreAverageDose3;
	private double emergingAverageDose;
	
	private double coreLevel2AD1mgRate;
	private double coreLevel2AD2mgRate;
	private double coreLevel2AD3mgRate;
	private double coreLevel2AD4mgRate;
	private double coreLevel2AD6mgRate;
	private double coreLevel2AD8mgRate;
	private double coreLevel2AD4mgUpRate;
	
	private double coreLevel3AD1mgRate;
	private double coreLevel3AD2mgRate;
	private double coreLevel3AD3mgRate;
	private double coreLevel3AD4mgRate;
	private double coreLevel3AD6mgRate;
	private double coreLevel3AD8mgRate;
	private double coreLevel3AD4mgUpRate;
	
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
	public double getCoreAverageDose() {
		return coreAverageDose;
	}
	public void setCoreAverageDose(double coreAverageDose) {
		this.coreAverageDose = coreAverageDose;
	}
	public double getEmergingAverageDose() {
		return emergingAverageDose;
	}
	public void setEmergingAverageDose(double emergingAverageDose) {
		this.emergingAverageDose = emergingAverageDose;
	}
	public double getCoreAverageDose2() {
		return coreAverageDose2;
	}
	public void setCoreAverageDose2(double coreAverageDose2) {
		this.coreAverageDose2 = coreAverageDose2;
	}
	public double getCoreAverageDose3() {
		return coreAverageDose3;
	}
	public void setCoreAverageDose3(double coreAverageDose3) {
		this.coreAverageDose3 = coreAverageDose3;
	}
	public double getCoreLevel2AD1mgRate() {
		return coreLevel2AD1mgRate;
	}
	public void setCoreLevel2AD1mgRate(double coreLevel2AD1mgRate) {
		this.coreLevel2AD1mgRate = coreLevel2AD1mgRate;
	}
	public double getCoreLevel2AD2mgRate() {
		return coreLevel2AD2mgRate;
	}
	public void setCoreLevel2AD2mgRate(double coreLevel2AD2mgRate) {
		this.coreLevel2AD2mgRate = coreLevel2AD2mgRate;
	}
	public double getCoreLevel2AD3mgRate() {
		return coreLevel2AD3mgRate;
	}
	public void setCoreLevel2AD3mgRate(double coreLevel2AD3mgRate) {
		this.coreLevel2AD3mgRate = coreLevel2AD3mgRate;
	}
	public double getCoreLevel2AD4mgRate() {
		return coreLevel2AD4mgRate;
	}
	public void setCoreLevel2AD4mgRate(double coreLevel2AD4mgRate) {
		this.coreLevel2AD4mgRate = coreLevel2AD4mgRate;
	}
	public double getCoreLevel2AD6mgRate() {
		return coreLevel2AD6mgRate;
	}
	public void setCoreLevel2AD6mgRate(double coreLevel2AD6mgRate) {
		this.coreLevel2AD6mgRate = coreLevel2AD6mgRate;
	}
	public double getCoreLevel2AD8mgRate() {
		return coreLevel2AD8mgRate;
	}
	public void setCoreLevel2AD8mgRate(double coreLevel2AD8mgRate) {
		this.coreLevel2AD8mgRate = coreLevel2AD8mgRate;
	}
	public double getCoreLevel2AD4mgUpRate() {
		return coreLevel2AD4mgUpRate;
	}
	public void setCoreLevel2AD4mgUpRate(double coreLevel2AD4mgUpRate) {
		this.coreLevel2AD4mgUpRate = coreLevel2AD4mgUpRate;
	}
	public double getCoreLevel3AD1mgRate() {
		return coreLevel3AD1mgRate;
	}
	public void setCoreLevel3AD1mgRate(double coreLevel3AD1mgRate) {
		this.coreLevel3AD1mgRate = coreLevel3AD1mgRate;
	}
	public double getCoreLevel3AD2mgRate() {
		return coreLevel3AD2mgRate;
	}
	public void setCoreLevel3AD2mgRate(double coreLevel3AD2mgRate) {
		this.coreLevel3AD2mgRate = coreLevel3AD2mgRate;
	}
	public double getCoreLevel3AD3mgRate() {
		return coreLevel3AD3mgRate;
	}
	public void setCoreLevel3AD3mgRate(double coreLevel3AD3mgRate) {
		this.coreLevel3AD3mgRate = coreLevel3AD3mgRate;
	}
	public double getCoreLevel3AD4mgRate() {
		return coreLevel3AD4mgRate;
	}
	public void setCoreLevel3AD4mgRate(double coreLevel3AD4mgRate) {
		this.coreLevel3AD4mgRate = coreLevel3AD4mgRate;
	}
	public double getCoreLevel3AD6mgRate() {
		return coreLevel3AD6mgRate;
	}
	public void setCoreLevel3AD6mgRate(double coreLevel3AD6mgRate) {
		this.coreLevel3AD6mgRate = coreLevel3AD6mgRate;
	}
	public double getCoreLevel3AD8mgRate() {
		return coreLevel3AD8mgRate;
	}
	public void setCoreLevel3AD8mgRate(double coreLevel3AD8mgRate) {
		this.coreLevel3AD8mgRate = coreLevel3AD8mgRate;
	}
	public double getCoreLevel3AD4mgUpRate() {
		return coreLevel3AD4mgUpRate;
	}
	public void setCoreLevel3AD4mgUpRate(double coreLevel3AD4mgUpRate) {
		this.coreLevel3AD4mgUpRate = coreLevel3AD4mgUpRate;
	}
}
