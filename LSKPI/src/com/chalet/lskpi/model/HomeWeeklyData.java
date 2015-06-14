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
    
    /**
     * 卖赠泵数量
     */
    private int homeWhNum1;
    
    /**
     * 平均带药天数
     */
    private double averDays;
    
    /**
     * 总带药支数
     */
    private int homeWhNum4;
    
    /**
     * 儿科家庭雾化上报医院数
     */
    private int inNum;
    
    /**
     * 儿科病房卖赠泵数量
     */
    private int homeRoomWhNum1;
    
    /**
     * 儿科病房平均带药天数
     */
    private double roomAverDays;
    
    /**
     * 儿科病房总带药支数
     */
    private int homeRoomWhNum4;
    
    /**
     * 儿科病房家庭雾化上报医院数
     */
    private int roomInNum;
    
    /**
     * 儿科病房家庭雾化上报率
     */
    private double roomInRate;

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

	public int getHomeWhNum1() {
		return homeWhNum1;
	}

	public void setHomeWhNum1(int homeWhNum1) {
		this.homeWhNum1 = homeWhNum1;
	}

	public double getAverDays() {
		return averDays;
	}

	public void setAverDays(double averDays) {
		this.averDays = averDays;
	}

	public int getHomeWhNum4() {
		return homeWhNum4;
	}

	public void setHomeWhNum4(int homeWhNum4) {
		this.homeWhNum4 = homeWhNum4;
	}

	public int getInNum() {
		return inNum;
	}

	public void setInNum(int inNum) {
		this.inNum = inNum;
	}

	public int getHomeRoomWhNum1() {
		return homeRoomWhNum1;
	}

	public void setHomeRoomWhNum1(int homeRoomWhNum1) {
		this.homeRoomWhNum1 = homeRoomWhNum1;
	}

	public double getRoomAverDays() {
		return roomAverDays;
	}

	public void setRoomAverDays(double roomAverDays) {
		this.roomAverDays = roomAverDays;
	}

	public int getHomeRoomWhNum4() {
		return homeRoomWhNum4;
	}

	public void setHomeRoomWhNum4(int homeRoomWhNum4) {
		this.homeRoomWhNum4 = homeRoomWhNum4;
	}

	public int getRoomInNum() {
		return roomInNum;
	}

	public void setRoomInNum(int roomInNum) {
		this.roomInNum = roomInNum;
	}

	public double getRoomInRate() {
		return roomInRate;
	}

	public void setRoomInRate(double roomInRate) {
		this.roomInRate = roomInRate;
	}
}
