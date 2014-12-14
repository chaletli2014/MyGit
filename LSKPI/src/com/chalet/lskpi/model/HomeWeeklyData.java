package com.chalet.lskpi.model;

public class HomeWeeklyData {

    private String userName;
    private int totalDrNum;
    private int newDrNum;
    /**
     * 上周家庭雾化新病人次量.
     * “每周新病人人次”统加
     */
    private double newWhNum;
    
    /**
     * 维持期治疗率.
     * “处方>=8天的哮喘维持期病人次”统加/“哮喘*患者人次”统加
     */
    private double cureRate;
    
    /**
     * 维持期使用令舒的人次.
     * “维持期病人中推荐使用令舒的人次”统加
     */
    private double lsnum;
    
    /**
     * 维持期令舒比例.
     * “维持期病人中推荐使用令舒的人次”统加/“处方>=8天的哮喘维持期病人次” 统加
     */
    private double lsRate;
    
    /**
     * 家庭雾化疗程达标人次（DOT>=30天）.
     * “DOT>=30天,病人次”统加/“维持期病人中推荐使用令舒的人次”统加
     */
    private double reachRate;
    
    private String regionCenterCN;
    
    private int reportNum;
    
    private double inRate;

    public int getTotalDrNum() {
        return totalDrNum;
    }

    public void setTotalDrNum(int totalDrNum) {
        this.totalDrNum = totalDrNum;
    }

    public int getNewDrNum() {
        return newDrNum;
    }

    public void setNewDrNum(int newDrNum) {
        this.newDrNum = newDrNum;
    }

    /**
     * 上周家庭雾化新病人次量.
     * “每周新病人人次”统加
     * @return double
     */
    public double getNewWhNum() {
        return newWhNum;
    }

    /**
     * 上周家庭雾化新病人次量.
     * “每周新病人人次”统加
     * @param newWhNum double
     */
    public void setNewWhNum(double newWhNum) {
        this.newWhNum = newWhNum;
    }

    /**
     * 维持期治疗率.
     * “处方>=8天的哮喘维持期病人次”统加/“哮喘*患者人次”统加
     * @return double
     */
    public double getCureRate() {
        return cureRate;
    }

    /**
     * 维持期治疗率.
     * “处方>=8天的哮喘维持期病人次”统加/“哮喘*患者人次”统加
     * @param cureRate double
     */
    public void setCureRate(double cureRate) {
        this.cureRate = cureRate;
    }

    /**
     * 维持期使用令舒的人次.
     * “维持期病人中推荐使用令舒的人次”统加
     * @return double
     */
    public double getLsnum() {
        return lsnum;
    }

    /**
     * 维持期使用令舒的人次.
     * @param lsnum
     */
    public void setLsnum(double lsnum) {
        this.lsnum = lsnum;
    }

    public double getLsRate() {
        return lsRate;
    }

    public void setLsRate(double lsRate) {
        this.lsRate = lsRate;
    }

    public double getReachRate() {
        return reachRate;
    }

    public void setReachRate(double reachRate) {
        this.reachRate = reachRate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

	public String getRegionCenterCN() {
		return regionCenterCN;
	}

	public void setRegionCenterCN(String regionCenterCN) {
		this.regionCenterCN = regionCenterCN;
	}

	public int getReportNum() {
		return reportNum;
	}

	public void setReportNum(int reportNum) {
		this.reportNum = reportNum;
	}

	public double getInRate() {
		return inRate;
	}

	public void setInRate(double inRate) {
		this.inRate = inRate;
	}
}
