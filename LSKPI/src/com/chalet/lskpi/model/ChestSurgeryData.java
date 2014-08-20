package com.chalet.lskpi.model;

import java.util.Date;


/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午3:37:38
 * 类说明
 */

public class ChestSurgeryData {
	
    private int dataId;
	private String hospitalName;
	private String hospitalCode;
	private int pnum;
	private int risknum;
	private int whnum;
	private int lsnum;
	private double oqd;
	private double tqd;
	private double otid;
	private double tbid;
	private double ttid;
	private double thbid;
	private double fbid;
	
	private String isChestSurgeryAssessed;
	
	//below five are used in the upload daily res data feature.
	private String region;
	private String rsmRegion;
	private String salesName;
	private String salesCode;
	private Date createdate;
	private String dsmName;
	
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
	public int getRisknum() {
		return risknum;
	}
	public void setRisknum(int risknum) {
		this.risknum = risknum;
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
    public String getSalesCode() {
        return salesCode;
    }
    public void setSalesCode(String salesCode) {
        this.salesCode = salesCode;
    }
    public String getIsChestSurgeryAssessed() {
        return isChestSurgeryAssessed;
    }
    public void setIsChestSurgeryAssessed(String isChestSurgeryAssessed) {
        this.isChestSurgeryAssessed = isChestSurgeryAssessed;
    }
}
