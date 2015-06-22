package com.chalet.lskpi.model;

public class MobilePEDDailyData {

    //日报手机端显示关键词为，上报率、雾化率、平均剂量、最大剂量占比，其中最大剂量占比最多取前两项，并以剂量的大小为排序（3mg/d 30% 2mg/d 30% 1mg/d 30%只取前两个剂量显示）。
    private String userName;
    private String userCode;
    
    private int hosNum;
    private int inNum;
    private int patNum;
    private int patNumRoom;
    private int whNum;
    private int whNumRoom;
    private int lsNum;
    private int lsNumRoom;
    private double averageDose;
    private double averageDoseRoom;
    private double hmgRate;
    private double omgRate;
    private double tmgRate;
    private double fmgRate;
    
    private RateElement firstRate;
    private RateElement secondRate;
    
    private double inRate;
    private double whRate;
    private double whRateRoom;
    
    private String regionCenterCN;
    private String region;
    private double coreInRate;
    private double whPortRate;
    
    private double coreWhRate;
    private double emergingWhRate;
    
    private int whbwnum;
    private int whbwnumRoom;
    private double blRate;
    private double blRateRoom;
    
    private double whdays;
    private double whdaysRoom;
    
    private int homeWhEmergingNum1;
    private int homeWhEmergingNum2;
    private int homeWhEmergingNum3;
    private int homeWhEmergingNum4;
    private int homeWhRoomNum1;
    private int homeWhRoomNum2;
    private int homeWhRoomNum3;
    private int homeWhRoomNum4;
    
    private double homeWhEmergingAverDays;
    private double homeWhRoomAverDays;
    
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
    public double getHmgRate() {
        return hmgRate;
    }
    public void setHmgRate(double hmgRate) {
        this.hmgRate = hmgRate;
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
    public double getFmgRate() {
        return fmgRate;
    }
    public void setFmgRate(double fmgRate) {
        this.fmgRate = fmgRate;
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
    public String getRegionCenterCN() {
        return regionCenterCN;
    }
    public void setRegionCenterCN(String regionCenterCN) {
        this.regionCenterCN = regionCenterCN;
    }
	public double getCoreInRate() {
		return coreInRate;
	}
	public void setCoreInRate(double coreInRate) {
		this.coreInRate = coreInRate;
	}
	public double getWhPortRate() {
		return whPortRate;
	}
	public void setWhPortRate(double whPortRate) {
		this.whPortRate = whPortRate;
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
	public int getWhbwnum() {
		return whbwnum;
	}
	public void setWhbwnum(int whbwnum) {
		this.whbwnum = whbwnum;
	}
	public double getBlRate() {
		return blRate;
	}
	public void setBlRate(double blRate) {
		this.blRate = blRate;
	}
	public int getPatNumRoom() {
		return patNumRoom;
	}
	public void setPatNumRoom(int patNumRoom) {
		this.patNumRoom = patNumRoom;
	}
	public int getWhNumRoom() {
		return whNumRoom;
	}
	public void setWhNumRoom(int whNumRoom) {
		this.whNumRoom = whNumRoom;
	}
	public int getLsNumRoom() {
		return lsNumRoom;
	}
	public void setLsNumRoom(int lsNumRoom) {
		this.lsNumRoom = lsNumRoom;
	}
	public double getAverageDoseRoom() {
		return averageDoseRoom;
	}
	public void setAverageDoseRoom(double averageDoseRoom) {
		this.averageDoseRoom = averageDoseRoom;
	}
	public double getWhRateRoom() {
		return whRateRoom;
	}
	public void setWhRateRoom(double whRateRoom) {
		this.whRateRoom = whRateRoom;
	}
	public int getWhbwnumRoom() {
		return whbwnumRoom;
	}
	public void setWhbwnumRoom(int whbwnumRoom) {
		this.whbwnumRoom = whbwnumRoom;
	}
	public double getBlRateRoom() {
		return blRateRoom;
	}
	public void setBlRateRoom(double blRateRoom) {
		this.blRateRoom = blRateRoom;
	}
	public double getWhdays() {
		return whdays;
	}
	public void setWhdays(double whdays) {
		this.whdays = whdays;
	}
	public double getWhdaysRoom() {
		return whdaysRoom;
	}
	public void setWhdaysRoom(double whdaysRoom) {
		this.whdaysRoom = whdaysRoom;
	}
	public int getHomeWhEmergingNum1() {
		return homeWhEmergingNum1;
	}
	public void setHomeWhEmergingNum1(int homeWhEmergingNum1) {
		this.homeWhEmergingNum1 = homeWhEmergingNum1;
	}
	public int getHomeWhEmergingNum2() {
		return homeWhEmergingNum2;
	}
	public void setHomeWhEmergingNum2(int homeWhEmergingNum2) {
		this.homeWhEmergingNum2 = homeWhEmergingNum2;
	}
	public int getHomeWhEmergingNum3() {
		return homeWhEmergingNum3;
	}
	public void setHomeWhEmergingNum3(int homeWhEmergingNum3) {
		this.homeWhEmergingNum3 = homeWhEmergingNum3;
	}
	public int getHomeWhEmergingNum4() {
		return homeWhEmergingNum4;
	}
	public void setHomeWhEmergingNum4(int homeWhEmergingNum4) {
		this.homeWhEmergingNum4 = homeWhEmergingNum4;
	}
	public int getHomeWhRoomNum1() {
		return homeWhRoomNum1;
	}
	public void setHomeWhRoomNum1(int homeWhRoomNum1) {
		this.homeWhRoomNum1 = homeWhRoomNum1;
	}
	public int getHomeWhRoomNum2() {
		return homeWhRoomNum2;
	}
	public void setHomeWhRoomNum2(int homeWhRoomNum2) {
		this.homeWhRoomNum2 = homeWhRoomNum2;
	}
	public int getHomeWhRoomNum3() {
		return homeWhRoomNum3;
	}
	public void setHomeWhRoomNum3(int homeWhRoomNum3) {
		this.homeWhRoomNum3 = homeWhRoomNum3;
	}
	public int getHomeWhRoomNum4() {
		return homeWhRoomNum4;
	}
	public void setHomeWhRoomNum4(int homeWhRoomNum4) {
		this.homeWhRoomNum4 = homeWhRoomNum4;
	}
	public double getHomeWhEmergingAverDays() {
		return homeWhEmergingAverDays;
	}
	public void setHomeWhEmergingAverDays(double homeWhEmergingAverDays) {
		this.homeWhEmergingAverDays = homeWhEmergingAverDays;
	}
	public double getHomeWhRoomAverDays() {
		return homeWhRoomAverDays;
	}
	public void setHomeWhRoomAverDays(double homeWhRoomAverDays) {
		this.homeWhRoomAverDays = homeWhRoomAverDays;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	
}
