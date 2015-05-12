package com.chalet.lskpi.model;

public class WeeklyRatioData {
    private String userCode;
    private String name;
	
    private int pnum;
    private int lsnum;
    private double inRate;
    private double whRate;
    private double averageDose;
    
    private double patNumRatio;
    private double lsNumRatio;
    private double averageDoseRatio;
    private double inRateRatio;
    private double whRateRatio;
    
    private double hmgRate;
    private double omgRate;
    private double tmgRate;
    private double thmgRate;
    private double fmgRate;
    private double smgRate;
    private double emgRate;
    
    private double hmgRateRatio;
    private double omgRateRatio;
    private double tmgRateRatio;
    private double thmgRateRatio;
    private double fmgRateRatio;
    private double smgRateRatio;
    private double emgRateRatio;
    
    private RateElement firstRate;
    private RateElement secondRate;
    
    private int whbwnum;
    private double whbwNumRatio;
    private double blRate;
    private double blRateRatio;
    
    public double getPatNumRatio() {
        return patNumRatio;
    }
    public void setPatNumRatio(double patNumRatio) {
        this.patNumRatio = patNumRatio;
    }
    public double getLsNumRatio() {
        return lsNumRatio;
    }
    public void setLsNumRatio(double lsNumRatio) {
        this.lsNumRatio = lsNumRatio;
    }
    public double getAverageDoseRatio() {
        return averageDoseRatio;
    }
    public void setAverageDoseRatio(double averageDoseRatio) {
        this.averageDoseRatio = averageDoseRatio;
    }
    public double getInRateRatio() {
        return inRateRatio;
    }
    public void setInRateRatio(double inRateRatio) {
        this.inRateRatio = inRateRatio;
    }
    public double getWhRateRatio() {
        return whRateRatio;
    }
    public void setWhRateRatio(double whRateRatio) {
        this.whRateRatio = whRateRatio;
    }
    public int getPnum() {
        return pnum;
    }
    public void setPnum(int pnum) {
        this.pnum = pnum;
    }
    public int getLsnum() {
        return lsnum;
    }
    public void setLsnum(int lsnum) {
        this.lsnum = lsnum;
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
	public double getHmgRateRatio() {
		return hmgRateRatio;
	}
	public void setHmgRateRatio(double hmgRateRatio) {
		this.hmgRateRatio = hmgRateRatio;
	}
	public double getOmgRateRatio() {
		return omgRateRatio;
	}
	public void setOmgRateRatio(double omgRateRatio) {
		this.omgRateRatio = omgRateRatio;
	}
	public double getTmgRateRatio() {
		return tmgRateRatio;
	}
	public void setTmgRateRatio(double tmgRateRatio) {
		this.tmgRateRatio = tmgRateRatio;
	}
	public double getFmgRateRatio() {
		return fmgRateRatio;
	}
	public void setFmgRateRatio(double fmgRateRatio) {
		this.fmgRateRatio = fmgRateRatio;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
	public double getThmgRate() {
		return thmgRate;
	}
	public void setThmgRate(double thmgRate) {
		this.thmgRate = thmgRate;
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
	public double getThmgRateRatio() {
		return thmgRateRatio;
	}
	public void setThmgRateRatio(double thmgRateRatio) {
		this.thmgRateRatio = thmgRateRatio;
	}
	public double getSmgRateRatio() {
		return smgRateRatio;
	}
	public void setSmgRateRatio(double smgRateRatio) {
		this.smgRateRatio = smgRateRatio;
	}
	public double getEmgRateRatio() {
		return emgRateRatio;
	}
	public void setEmgRateRatio(double emgRateRatio) {
		this.emgRateRatio = emgRateRatio;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWhbwnum() {
		return whbwnum;
	}
	public void setWhbwnum(int whbwnum) {
		this.whbwnum = whbwnum;
	}
	public double getWhbwNumRatio() {
		return whbwNumRatio;
	}
	public void setWhbwNumRatio(double whbwNumRatio) {
		this.whbwNumRatio = whbwNumRatio;
	}
	public double getBlRate() {
		return blRate;
	}
	public void setBlRate(double blRate) {
		this.blRate = blRate;
	}
	public double getBlRateRatio() {
		return blRateRatio;
	}
	public void setBlRateRatio(double blRateRatio) {
		this.blRateRatio = blRateRatio;
	}
}
