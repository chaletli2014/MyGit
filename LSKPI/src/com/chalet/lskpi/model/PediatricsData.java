package com.chalet.lskpi.model;

import java.util.Date;

/**
 * @author Chalet
 * @version 创建时间：2013年11月27日 下午11:24:22
 * 类说明
 */

public class PediatricsData {

	private int dataId;
	private String hospitalName;
	private String hospitalCode;
	private int pnum;
	private int whnum;
	private int lsnum;
	private double hqd;
	private double hbid;
	private double oqd;
	private double obid;
	private double tqd;
	private double tbid;
	private String recipeType;
	
	//below four are used in the upload daily res data feature.
    private String region;
    private String rsmRegion;
    private String salesName;
    private String salesETMSCode;
    private Date createdate;
    
    private String dsmName;
    
    private String isPedAssessed;
	
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}
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
	public double getHqd() {
		return hqd;
	}
	public void setHqd(double hqd) {
		this.hqd = hqd;
	}
	public double getHbid() {
		return hbid;
	}
	public void setHbid(double hbid) {
		this.hbid = hbid;
	}
	public double getOqd() {
		return oqd;
	}
	public void setOqd(double oqd) {
		this.oqd = oqd;
	}
	public double getObid() {
		return obid;
	}
	public void setObid(double obid) {
		this.obid = obid;
	}
	public double getTqd() {
		return tqd;
	}
	public void setTqd(double tqd) {
		this.tqd = tqd;
	}
	public double getTbid() {
		return tbid;
	}
	public void setTbid(double tbid) {
		this.tbid = tbid;
	}
	public String getRecipeType() {
		return recipeType;
	}
	public void setRecipeType(String recipeType) {
		this.recipeType = recipeType;
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
    public String getIsPedAssessed() {
        return isPedAssessed;
    }
    public void setIsPedAssessed(String isPedAssessed) {
        this.isPedAssessed = isPedAssessed;
    }
}
