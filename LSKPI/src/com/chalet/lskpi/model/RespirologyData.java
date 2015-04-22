package com.chalet.lskpi.model;

import java.util.Date;


/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:37:38
 * 类说明
 */

public class RespirologyData {
	
    private int dataId;
	private String hospitalName;
	private String hospitalCode;
	private int pnum;
	private int aenum;
	private int whnum;
	private int lsnum;
	private double oqd;
	private double tqd;
	private double otid;
	private double tbid;
	private double ttid;
	private double thbid;
	private double fbid;
	private String recipeType;
	
	//below five are used in the upload daily res data feature.
	private String region;
	private String rsmRegion;
	private String salesName;
	private String salesETMSCode;
	private Date createdate;
	private String dsmName;
	private String isResAssessed;
	private String dragonType;
	
	/**
	 * 以下录入项为信必可字段
	 */
	private int xbknum;
	private int xbk1num;
	private int xbk2num;
	private int xbk3num;
	
	private String isRe2;
	
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public int getPnum() {
		return pnum;
	}
	public void setPnum(int pnum) {
		this.pnum = pnum;
	}
	public int getAenum() {
		return aenum;
	}
	public void setAenum(int aenum) {
		this.aenum = aenum;
	}
	public int getWhnum() {
		return whnum;
	}
	public void setWhnum(int whnum) {
		this.whnum = whnum;
	}
	public int getLsnum() {
		return lsnum;
	}
	public void setLsnum(int lsnum) {
		this.lsnum = lsnum;
	}
	public double getOqd() {
		return oqd;
	}
	public void setOqd(double oqd) {
		this.oqd = oqd;
	}
	public double getTqd() {
		return tqd;
	}
	public void setTqd(double tqd) {
		this.tqd = tqd;
	}
	public double getOtid() {
		return otid;
	}
	public void setOtid(double otid) {
		this.otid = otid;
	}
	public double getTbid() {
		return tbid;
	}
	public void setTbid(double tbid) {
		this.tbid = tbid;
	}
	public double getTtid() {
		return ttid;
	}
	public void setTtid(double ttid) {
		this.ttid = ttid;
	}
	public double getThbid() {
		return thbid;
	}
	public void setThbid(double thbid) {
		this.thbid = thbid;
	}
	public double getFbid() {
		return fbid;
	}
	public void setFbid(double fbid) {
		this.fbid = fbid;
	}
	public String getRecipeType() {
		return recipeType;
	}
	public void setRecipeType(String recipeType) {
		this.recipeType = recipeType;
	}
    public int getDataId() {
        return dataId;
    }
    public void setDataId(int dataId) {
        this.dataId = dataId;
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
    public String getSalesName() {
        return salesName;
    }
    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }
    public String getSalesETMSCode() {
        return salesETMSCode;
    }
    public void setSalesETMSCode(String salesETMSCode) {
        this.salesETMSCode = salesETMSCode;
    }
    public Date getCreatedate() {
        return createdate;
    }
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
    public String getHospitalCode() {
        return hospitalCode;
    }
    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }
    public String getDsmName() {
        return dsmName;
    }
    public void setDsmName(String dsmName) {
        this.dsmName = dsmName;
    }
    public String getIsResAssessed() {
        return isResAssessed;
    }
    public void setIsResAssessed(String isResAssessed) {
        this.isResAssessed = isResAssessed;
    }
    public String getDragonType() {
        return dragonType;
    }
    public void setDragonType(String dragonType) {
        this.dragonType = dragonType;
    }
	public int getXbknum() {
		return xbknum;
	}
	public void setXbknum(int xbknum) {
		this.xbknum = xbknum;
	}
	public int getXbk1num() {
		return xbk1num;
	}
	public void setXbk1num(int xbk1num) {
		this.xbk1num = xbk1num;
	}
	public int getXbk2num() {
		return xbk2num;
	}
	public void setXbk2num(int xbk2num) {
		this.xbk2num = xbk2num;
	}
	public int getXbk3num() {
		return xbk3num;
	}
	public void setXbk3num(int xbk3num) {
		this.xbk3num = xbk3num;
	}
	public String getIsRe2() {
		return isRe2;
	}
	public void setIsRe2(String isRe2) {
		this.isRe2 = isRe2;
	}
}
