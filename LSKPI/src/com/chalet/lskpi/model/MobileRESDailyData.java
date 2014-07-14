package com.chalet.lskpi.model;

public class MobileRESDailyData {

    //日报手机端显示关键词为，上报率、雾化率、平均剂量、最大剂量占比，其中最大剂量占比最多取前两项，并以剂量的大小为排序（3mg/d 30% 2mg/d 30% 1mg/d 30%只取前两个剂量显示）。
    private String userName;
    private String userCode;
    
    private int hosNum;
    private int inNum;
    private int patNum;
    private int whNum;
    private int lsNum;
    private double averageDose;
    private double omgRate;
	private double tmgRate;
	private double thmgRate;
	private double fmgRate;
	private double smgRate;
	private double emgRate;
    
    private RateElement firstRate;
    private RateElement secondRate;
    
    private double inRate;
    private double whRate;
    
    private String regionCenterCN;
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    
    public int getHosNum() {
        return hosNum;
    }
    public void setHosNum(int hosNum) {
        this.hosNum = hosNum;
    }
    public int getInNum() {
        return inNum;
    }
    public void setInNum(int inNum) {
        this.inNum = inNum;
    }
    public int getWhNum() {
        return whNum;
    }
    public void setWhNum(int whNum) {
        this.whNum = whNum;
    }
    public int getLsNum() {
        return lsNum;
    }
    public void setLsNum(int lsNum) {
        this.lsNum = lsNum;
    }
    public double getAverageDose() {
        return averageDose;
    }
    public void setAverageDose(double averageDose) {
        this.averageDose = averageDose;
    }
    public int getPatNum() {
        return patNum;
    }
    public void setPatNum(int patNum) {
        this.patNum = patNum;
    }
    public RateElement getFirstRate() {
        return firstRate;
    }
    public void setFirstRate(RateElement firstRate) {
        this.firstRate = firstRate;
    }
    public RateElement getSecondRate() {
        return secondRate;
    }
    public void setSecondRate(RateElement secondRate) {
        this.secondRate = secondRate;
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
	public double getOmgRate() {
		return omgRate;
	}
	public void setOmgRate(double omgRate) {
		this.omgRate = omgRate;
	}
	public double getTmgRate() {
		return tmgRate;
	}
	public void setTmgRate(double tmgRate) {
		this.tmgRate = tmgRate;
	}
	public double getThmgRate() {
		return thmgRate;
	}
	public void setThmgRate(double thmgRate) {
		this.thmgRate = thmgRate;
	}
	public double getFmgRate() {
		return fmgRate;
	}
	public void setFmgRate(double fmgRate) {
		this.fmgRate = fmgRate;
	}
	public double getSmgRate() {
		return smgRate;
	}
	public void setSmgRate(double smgRate) {
		this.smgRate = smgRate;
	}
	public double getEmgRate() {
		return emgRate;
	}
	public void setEmgRate(double emgRate) {
		this.emgRate = emgRate;
	}
    public String getRegionCenterCN() {
        return regionCenterCN;
    }
    public void setRegionCenterCN(String regionCenterCN) {
        this.regionCenterCN = regionCenterCN;
    }
}
