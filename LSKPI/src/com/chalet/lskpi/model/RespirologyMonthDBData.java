package com.chalet.lskpi.model;

public class RespirologyMonthDBData {

    private String rsmRegion;
    private String rsmName;
    private String dataMonth;
    private String dataYear;
    private String duration;
    private double pnum;
    private double lsnum;
    private double aenum;
    private double inRate;
    private double whRate;
    private double averageDose;
    private int weeklyCount;
    private double whDays;
    private double xbknum;
    
    public String getRsmRegion() {
        return rsmRegion;
    }
    public void setRsmRegion(String rsmRegion) {
        this.rsmRegion = rsmRegion;
    }
    public String getDataMonth() {
        return dataMonth;
    }
    public void setDataMonth(String dataMonth) {
        this.dataMonth = dataMonth;
    }
    public String getDataYear() {
        return dataYear;
    }
    public void setDataYear(String dataYear) {
        this.dataYear = dataYear;
    }
    public double getPnum() {
        return pnum;
    }
    public void setPnum(double pnum) {
        this.pnum = pnum;
    }
    public double getLsnum() {
        return lsnum;
    }
    public void setLsnum(double lsnum) {
        this.lsnum = lsnum;
    }
    public double getAenum() {
        return aenum;
    }
    public void setAenum(double aenum) {
        this.aenum = aenum;
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
    public double getAverageDose() {
        return averageDose;
    }
    public void setAverageDose(double averageDose) {
        this.averageDose = averageDose;
    }
    public int getWeeklyCount() {
        return weeklyCount;
    }
    public void setWeeklyCount(int weeklyCount) {
        this.weeklyCount = weeklyCount;
    }
    public String getRsmName() {
        return rsmName;
    }
    public void setRsmName(String rsmName) {
        this.rsmName = rsmName;
    }
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public double getWhDays() {
		return whDays;
	}
	public void setWhDays(double whDays) {
		this.whDays = whDays;
	}
	public double getXbknum() {
		return xbknum;
	}
	public void setXbknum(double xbknum) {
		this.xbknum = xbknum;
	}
}
