package com.chalet.lskpi.model;

public class Hospital {

    private int id;
    private String name;
    private String city;
    private String province;
    private String region;
    private String rsmRegion;
    private String level;
    private String code;
    private String dsmCode;
    private String dsmName;
    private String saleName;
    private String saleCode;
    
    private String dragonType;
    private String isResAssessed;
    private String isPedAssessed;
    private String isMonthlyAssessed;
    private String isChestSurgeryAssessed;
    private String isTop100;//是否在胸外科Top 100名单中（在=1，不在=0）
    
    private String isDailyReport;
    
    /**
     * 是否在RE2名单中（在=1，不在=0）
     */
    private String isRe2;
    
    private int portNum;
    
    private String isWHBW;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRsmRegion() {
		return rsmRegion;
	}
	public void setRsmRegion(String rsmRegion) {
		this.rsmRegion = rsmRegion;
	}
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getDsmCode() {
        return dsmCode;
    }
    public void setDsmCode(String dsmCode) {
        this.dsmCode = dsmCode;
    }
    public String getDsmName() {
        return dsmName;
    }
    public void setDsmName(String dsmName) {
        this.dsmName = dsmName;
    }
    public String getSaleName() {
        return saleName;
    }
    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }
    public String getDragonType() {
        return dragonType;
    }
    public void setDragonType(String dragonType) {
        this.dragonType = dragonType;
    }
	public String getIsResAssessed() {
		return isResAssessed;
	}
	public void setIsResAssessed(String isResAssessed) {
		this.isResAssessed = isResAssessed;
	}
	public String getIsPedAssessed() {
		return isPedAssessed;
	}
	public void setIsPedAssessed(String isPedAssessed) {
		this.isPedAssessed = isPedAssessed;
	}
    public String getSaleCode() {
        return saleCode;
    }
    public void setSaleCode(String saleCode) {
        this.saleCode = saleCode;
    }
    public String getIsMonthlyAssessed() {
        return isMonthlyAssessed;
    }
    public void setIsMonthlyAssessed(String isMonthlyAssessed) {
        this.isMonthlyAssessed = isMonthlyAssessed;
    }
    public String getIsChestSurgeryAssessed() {
        return isChestSurgeryAssessed;
    }
    public void setIsChestSurgeryAssessed(String isChestSurgeryAssessed) {
        this.isChestSurgeryAssessed = isChestSurgeryAssessed;
    }
    public String getIsTop100() {
        return isTop100;
    }
    public void setIsTop100(String isTop100) {
        this.isTop100 = isTop100;
    }
	public int getPortNum() {
		return portNum;
	}
	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
	public String getIsDailyReport() {
		return isDailyReport;
	}
	public void setIsDailyReport(String isDailyReport) {
		this.isDailyReport = isDailyReport;
	}
	public String getIsRe2() {
		return isRe2;
	}
	public void setIsRe2(String isRe2) {
		this.isRe2 = isRe2;
	}
	public String getIsWHBW() {
		return isWHBW;
	}
	public void setIsWHBW(String isWHBW) {
		this.isWHBW = isWHBW;
	}
}