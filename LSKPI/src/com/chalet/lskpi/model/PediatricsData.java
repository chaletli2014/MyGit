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
	private int pnum_room;
	private int whnum;
	private int whnum_room;
	private int lsnum;
	private int lsnum_room;
	private double hqd;
	private double hbid;
	private double oqd;
	private double obid;
	private double tqd;
	private double tbid;
	private double hqd_room;
	private double hbid_room;
	private double oqd_room;
	private double obid_room;
	private double tqd_room;
	private double tbid_room;
	private String recipeType;
	
	//below four are used in the upload daily res data feature.
    private String region;
    private String rsmRegion;
    private String salesName;
    private String salesETMSCode;
    private Date createdate;
    
    private String dsmName;
    
    private String isPedAssessed;
    
    private String dragonType;
    
    private int portNum;
    
    /**
     * 雾化博雾人次
     */
    private int whbwnum;
    
    private int whbwnum_room;
    /**
     * 门急诊雾化天数1天占比(%)
     */
    private double whdaysEmerging1Rate;
    
    /**
     * 门急诊雾化天数2天占比(%)
     */
    private double whdaysEmerging2Rate;
    
    /**
     * 门急诊雾化天数3天占比(%)
     */
    private double whdaysEmerging3Rate;
    
    /**
     * 门急诊雾化天数4天占比(%)
     */
    private double whdaysEmerging4Rate;
    
    /**
     * 门急诊雾化天数5天占比(%)
     */
    private double whdaysEmerging5Rate;
    
    /**
     * 门急诊雾化天数6天占比(%)
     */
    private double whdaysEmerging6Rate;
    
    /**
     * 门急诊雾化天数7天及以上占比(%)
     */
    private double whdaysEmerging7Rate;
    
    /**
     * 门急诊赠卖泵数量
     */
    private int homeWhEmergingNum1;
    
    /**
     * 门急诊带药人数
     */
    private int homeWhEmergingNum2;
    
    /**
     * 门急诊平均带药天数
     */
    private int homeWhEmergingNum3;
    
    /**
     * 门急诊总带药支数
     */
    private int homeWhEmergingNum4;
    
    
    /**
     * 病房雾化天数1天占比(%)
     */
    private double whdaysRoom1Rate;
    
    /**
     * 病房雾化天数2天占比(%)
     */
    private double whdaysRoom2Rate;
    
    /**
     * 病房雾化天数3天占比(%)
     */
    private double whdaysRoom3Rate;
    
    /**
     * 病房雾化天数4天占比(%)
     */
    private double whdaysRoom4Rate;
    
    /**
     * 病房雾化天数5天占比(%)
     */
    private double whdaysRoom5Rate;
    
    /**
     * 病房雾化天数6天占比(%)
     */
    private double whdaysRoom6Rate;
    
    /**
     * 病房雾化天数7天占比(%)
     */
    private double whdaysRoom7Rate;
    
    /**
     * 病房雾化天数8天占比(%)
     */
    private double whdaysRoom8Rate;
    
    /**
     * 病房雾化天数9天占比(%)
     */
    private double whdaysRoom9Rate;
    
    /**
     * 病房雾化天数10天及以上占比(%)
     */
    private double whdaysRoom10Rate;
    
    /**
     * 病房赠卖泵数量
     */
    private int homeWhRoomNum1;
    
    /**
     * 病房带药人数
     */
    private int homeWhRoomNum2;
    
    /**
     * 病房平均带药天数
     */
    private int homeWhRoomNum3;
    
    /**
     * 病房总带药支数
     */
    private int homeWhRoomNum4;
    
    /**
     * 门急诊DOT>=30天,病人次.
     */
    private int lttEmergingNum;
    
    /**
     * 住院DOT>=30天,病人次.
     */
    private int lttRoomNum;
	
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
    public String getDragonType() {
        return dragonType;
    }
    public void setDragonType(String dragonType) {
        this.dragonType = dragonType;
    }
	public int getPortNum() {
		return portNum;
	}
	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}
	public int getWhbwnum() {
		return whbwnum;
	}
	public void setWhbwnum(int whbwnum) {
		this.whbwnum = whbwnum;
	}
	public double getWhdaysEmerging1Rate() {
		return whdaysEmerging1Rate;
	}
	public void setWhdaysEmerging1Rate(double whdaysEmerging1Rate) {
		this.whdaysEmerging1Rate = whdaysEmerging1Rate;
	}
	public double getWhdaysEmerging2Rate() {
		return whdaysEmerging2Rate;
	}
	public void setWhdaysEmerging2Rate(double whdaysEmerging2Rate) {
		this.whdaysEmerging2Rate = whdaysEmerging2Rate;
	}
	public double getWhdaysEmerging3Rate() {
		return whdaysEmerging3Rate;
	}
	public void setWhdaysEmerging3Rate(double whdaysEmerging3Rate) {
		this.whdaysEmerging3Rate = whdaysEmerging3Rate;
	}
	public double getWhdaysEmerging4Rate() {
		return whdaysEmerging4Rate;
	}
	public void setWhdaysEmerging4Rate(double whdaysEmerging4Rate) {
		this.whdaysEmerging4Rate = whdaysEmerging4Rate;
	}
	public double getWhdaysEmerging5Rate() {
		return whdaysEmerging5Rate;
	}
	public void setWhdaysEmerging5Rate(double whdaysEmerging5Rate) {
		this.whdaysEmerging5Rate = whdaysEmerging5Rate;
	}
	public double getWhdaysEmerging6Rate() {
		return whdaysEmerging6Rate;
	}
	public void setWhdaysEmerging6Rate(double whdaysEmerging6Rate) {
		this.whdaysEmerging6Rate = whdaysEmerging6Rate;
	}
	public double getWhdaysEmerging7Rate() {
		return whdaysEmerging7Rate;
	}
	public void setWhdaysEmerging7Rate(double whdaysEmerging7Rate) {
		this.whdaysEmerging7Rate = whdaysEmerging7Rate;
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
	public double getWhdaysRoom1Rate() {
		return whdaysRoom1Rate;
	}
	public void setWhdaysRoom1Rate(double whdaysRoom1Rate) {
		this.whdaysRoom1Rate = whdaysRoom1Rate;
	}
	public double getWhdaysRoom2Rate() {
		return whdaysRoom2Rate;
	}
	public void setWhdaysRoom2Rate(double whdaysRoom2Rate) {
		this.whdaysRoom2Rate = whdaysRoom2Rate;
	}
	public double getWhdaysRoom3Rate() {
		return whdaysRoom3Rate;
	}
	public void setWhdaysRoom3Rate(double whdaysRoom3Rate) {
		this.whdaysRoom3Rate = whdaysRoom3Rate;
	}
	public double getWhdaysRoom4Rate() {
		return whdaysRoom4Rate;
	}
	public void setWhdaysRoom4Rate(double whdaysRoom4Rate) {
		this.whdaysRoom4Rate = whdaysRoom4Rate;
	}
	public double getWhdaysRoom5Rate() {
		return whdaysRoom5Rate;
	}
	public void setWhdaysRoom5Rate(double whdaysRoom5Rate) {
		this.whdaysRoom5Rate = whdaysRoom5Rate;
	}
	public double getWhdaysRoom6Rate() {
		return whdaysRoom6Rate;
	}
	public void setWhdaysRoom6Rate(double whdaysRoom6Rate) {
		this.whdaysRoom6Rate = whdaysRoom6Rate;
	}
	public double getWhdaysRoom7Rate() {
		return whdaysRoom7Rate;
	}
	public void setWhdaysRoom7Rate(double whdaysRoom7Rate) {
		this.whdaysRoom7Rate = whdaysRoom7Rate;
	}
	public double getWhdaysRoom8Rate() {
		return whdaysRoom8Rate;
	}
	public void setWhdaysRoom8Rate(double whdaysRoom8Rate) {
		this.whdaysRoom8Rate = whdaysRoom8Rate;
	}
	public double getWhdaysRoom9Rate() {
		return whdaysRoom9Rate;
	}
	public void setWhdaysRoom9Rate(double whdaysRoom9Rate) {
		this.whdaysRoom9Rate = whdaysRoom9Rate;
	}
	public double getWhdaysRoom10Rate() {
		return whdaysRoom10Rate;
	}
	public void setWhdaysRoom10Rate(double whdaysRoom10Rate) {
		this.whdaysRoom10Rate = whdaysRoom10Rate;
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
	public int getPnum_room() {
		return pnum_room;
	}
	public void setPnum_room(int pnum_room) {
		this.pnum_room = pnum_room;
	}
	public int getWhnum_room() {
		return whnum_room;
	}
	public void setWhnum_room(int whnum_room) {
		this.whnum_room = whnum_room;
	}
	public int getLsnum_room() {
		return lsnum_room;
	}
	public void setLsnum_room(int lsnum_room) {
		this.lsnum_room = lsnum_room;
	}
	public double getHqd_room() {
		return hqd_room;
	}
	public void setHqd_room(double hqd_room) {
		this.hqd_room = hqd_room;
	}
	public double getHbid_room() {
		return hbid_room;
	}
	public void setHbid_room(double hbid_room) {
		this.hbid_room = hbid_room;
	}
	public double getOqd_room() {
		return oqd_room;
	}
	public void setOqd_room(double oqd_room) {
		this.oqd_room = oqd_room;
	}
	public double getObid_room() {
		return obid_room;
	}
	public void setObid_room(double obid_room) {
		this.obid_room = obid_room;
	}
	public double getTqd_room() {
		return tqd_room;
	}
	public void setTqd_room(double tqd_room) {
		this.tqd_room = tqd_room;
	}
	public double getTbid_room() {
		return tbid_room;
	}
	public void setTbid_room(double tbid_room) {
		this.tbid_room = tbid_room;
	}
	public int getWhbwnum_room() {
		return whbwnum_room;
	}
	public void setWhbwnum_room(int whbwnum_room) {
		this.whbwnum_room = whbwnum_room;
	}
	public int getLttEmergingNum() {
		return lttEmergingNum;
	}
	public void setLttEmergingNum(int lttEmergingNum) {
		this.lttEmergingNum = lttEmergingNum;
	}
	public int getLttRoomNum() {
		return lttRoomNum;
	}
	public void setLttRoomNum(int lttRoomNum) {
		this.lttRoomNum = lttRoomNum;
	}
	
	
	
}
