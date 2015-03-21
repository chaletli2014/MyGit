package com.chalet.lskpi.model;
/**
 * @author Chalet
 * @version 创建时间：2013年12月24日 上午2:19:50
 * 类说明
 */

public class MonthlyRatioData {

    private String hospitalCode;
	private String saleName;
	private String dsmName;
	private String rsmRegion;
	private String region;
	
	private int pedemernum;
	private int pedroomnum;
	private int resnum;
	private int othernum;
	
	private double pedemernumrate;
	private double pedroomnumrate;
    private double resnumrate;
    private double othernumrate;
    
    private double pedemernumrateratio;
    private double pedroomnumrateratio;
    private double resnumrateratio;
    private double othernumrateratio;
	
	private double pedemernumratio;
	private double pedroomnumratio;
	private double resnumratio;
	private double othernumratio;
	
	private int hosnum;
	private int innum;
	private double innumratio;
	private double hosnumratio;
	private int totalnum;
	private double totalnumratio;
	
	private double inrate;
	private double inrateratio;
	
	/**
	 * 儿科门急诊药房
	 */
	private double pedEmerDrugStore;
	private double pedEmerDrugStoreRate;
	private double pedEmerDrugStoreRatio;
	private double pedEmerDrugStoreRateRatio;
	
	/**
	 * 儿科门急诊雾化室
	 */
	private double pedEmerWh;
	private double pedEmerWhRate;
	private double pedEmerWhRatio;
	private double pedEmerWhRateRatio;
	
	/**
	 * 儿科病房药房
	 */
	private double pedRoomDrugStore;
	private double pedRoomDrugStoreRate;
	private double pedRoomDrugStoreRatio;
	private double pedRoomDrugStoreRateRatio;
	
	/**
	 * 儿科病房药房雾化
	 */
	private double pedRoomDrugStoreWh;
	private double pedRoomDrugStoreWhRate;
	private double pedRoomDrugStoreWhRatio;
	private double pedRoomDrugStoreWhRateRatio;
	
	/**
	 * 家庭雾化
	 */
	private double homeWh;
	private double homeWhRate;
	private double homeWhRatio;
	private double homeWhRateRatio;
	
	/**
	 * 呼吸科门诊
	 */
	private double resClinic;
	private double resClinicRate;
	private double resClinicRatio;
	private double resClinicRateRatio;
	
	/**
	 * 呼吸科病房
	 */
	private double resRoom;
	private double resRoomRate;
	private double resRoomRatio;
	private double resRoomRateRatio;
	
	public int getPedemernum() {
		return pedemernum;
	}
	public void setPedemernum(int pedemernum) {
		this.pedemernum = pedemernum;
	}
	public int getPedroomnum() {
		return pedroomnum;
	}
	public void setPedroomnum(int pedroomnum) {
		this.pedroomnum = pedroomnum;
	}
	public int getResnum() {
		return resnum;
	}
	public void setResnum(int resnum) {
		this.resnum = resnum;
	}
	public int getOthernum() {
		return othernum;
	}
	public void setOthernum(int othernum) {
		this.othernum = othernum;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
    public String getRsmRegion() {
        return rsmRegion;
    }
    public void setRsmRegion(String rsmRegion) {
        this.rsmRegion = rsmRegion;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getSaleName() {
        return saleName;
    }
    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }
    public String getDsmName() {
        return dsmName;
    }
    public void setDsmName(String dsmName) {
        this.dsmName = dsmName;
    }
    public double getPedemernumrate() {
        return pedemernumrate;
    }
    public void setPedemernumrate(double pedemernumrate) {
        this.pedemernumrate = pedemernumrate;
    }
    public double getPedroomnumrate() {
        return pedroomnumrate;
    }
    public void setPedroomnumrate(double pedroomnumrate) {
        this.pedroomnumrate = pedroomnumrate;
    }
    public double getResnumrate() {
        return resnumrate;
    }
    public void setResnumrate(double resnumrate) {
        this.resnumrate = resnumrate;
    }
    public double getOthernumrate() {
        return othernumrate;
    }
    public void setOthernumrate(double othernumrate) {
        this.othernumrate = othernumrate;
    }
    public double getPedemernumratio() {
        return pedemernumratio;
    }
    public void setPedemernumratio(double pedemernumratio) {
        this.pedemernumratio = pedemernumratio;
    }
    public double getPedroomnumratio() {
        return pedroomnumratio;
    }
    public void setPedroomnumratio(double pedroomnumratio) {
        this.pedroomnumratio = pedroomnumratio;
    }
    public double getResnumratio() {
        return resnumratio;
    }
    public void setResnumratio(double resnumratio) {
        this.resnumratio = resnumratio;
    }
    public double getOthernumratio() {
        return othernumratio;
    }
    public void setOthernumratio(double othernumratio) {
        this.othernumratio = othernumratio;
    }
    public double getPedemernumrateratio() {
        return pedemernumrateratio;
    }
    public void setPedemernumrateratio(double pedemernumrateratio) {
        this.pedemernumrateratio = pedemernumrateratio;
    }
    public double getPedroomnumrateratio() {
        return pedroomnumrateratio;
    }
    public void setPedroomnumrateratio(double pedroomnumrateratio) {
        this.pedroomnumrateratio = pedroomnumrateratio;
    }
    public double getResnumrateratio() {
        return resnumrateratio;
    }
    public void setResnumrateratio(double resnumrateratio) {
        this.resnumrateratio = resnumrateratio;
    }
    public double getOthernumrateratio() {
        return othernumrateratio;
    }
    public void setOthernumrateratio(double othernumrateratio) {
        this.othernumrateratio = othernumrateratio;
    }
	public int getHosnum() {
		return hosnum;
	}
	public void setHosnum(int hosnum) {
		this.hosnum = hosnum;
	}
	public int getInnum() {
		return innum;
	}
	public void setInnum(int innum) {
		this.innum = innum;
	}
	public int getTotalnum() {
		return totalnum;
	}
	public void setTotalnum(int totalnum) {
		this.totalnum = totalnum;
	}
	public double getTotalnumratio() {
		return totalnumratio;
	}
	public void setTotalnumratio(double totalnumratio) {
		this.totalnumratio = totalnumratio;
	}
	public double getInnumratio() {
		return innumratio;
	}
	public void setInnumratio(double innumratio) {
		this.innumratio = innumratio;
	}
	public double getHosnumratio() {
		return hosnumratio;
	}
	public void setHosnumratio(double hosnumratio) {
		this.hosnumratio = hosnumratio;
	}
    public double getInrate() {
        return inrate;
    }
    public void setInrate(double inrate) {
        this.inrate = inrate;
    }
    public double getInrateratio() {
        return inrateratio;
    }
    public void setInrateratio(double inrateratio) {
        this.inrateratio = inrateratio;
    }
	public double getPedEmerDrugStore() {
		return pedEmerDrugStore;
	}
	public void setPedEmerDrugStore(double pedEmerDrugStore) {
		this.pedEmerDrugStore = pedEmerDrugStore;
	}
	public double getPedEmerDrugStoreRate() {
		return pedEmerDrugStoreRate;
	}
	public void setPedEmerDrugStoreRate(double pedEmerDrugStoreRate) {
		this.pedEmerDrugStoreRate = pedEmerDrugStoreRate;
	}
	public double getPedEmerDrugStoreRatio() {
		return pedEmerDrugStoreRatio;
	}
	public void setPedEmerDrugStoreRatio(double pedEmerDrugStoreRatio) {
		this.pedEmerDrugStoreRatio = pedEmerDrugStoreRatio;
	}
	public double getPedEmerDrugStoreRateRatio() {
		return pedEmerDrugStoreRateRatio;
	}
	public void setPedEmerDrugStoreRateRatio(double pedEmerDrugStoreRateRatio) {
		this.pedEmerDrugStoreRateRatio = pedEmerDrugStoreRateRatio;
	}
	public double getPedEmerWh() {
		return pedEmerWh;
	}
	public void setPedEmerWh(double pedEmerWh) {
		this.pedEmerWh = pedEmerWh;
	}
	public double getPedEmerWhRate() {
		return pedEmerWhRate;
	}
	public void setPedEmerWhRate(double pedEmerWhRate) {
		this.pedEmerWhRate = pedEmerWhRate;
	}
	public double getPedEmerWhRatio() {
		return pedEmerWhRatio;
	}
	public void setPedEmerWhRatio(double pedEmerWhRatio) {
		this.pedEmerWhRatio = pedEmerWhRatio;
	}
	public double getPedEmerWhRateRatio() {
		return pedEmerWhRateRatio;
	}
	public void setPedEmerWhRateRatio(double pedEmerWhRateRatio) {
		this.pedEmerWhRateRatio = pedEmerWhRateRatio;
	}
	public double getPedRoomDrugStore() {
		return pedRoomDrugStore;
	}
	public void setPedRoomDrugStore(double pedRoomDrugStore) {
		this.pedRoomDrugStore = pedRoomDrugStore;
	}
	public double getPedRoomDrugStoreRate() {
		return pedRoomDrugStoreRate;
	}
	public void setPedRoomDrugStoreRate(double pedRoomDrugStoreRate) {
		this.pedRoomDrugStoreRate = pedRoomDrugStoreRate;
	}
	public double getPedRoomDrugStoreRatio() {
		return pedRoomDrugStoreRatio;
	}
	public void setPedRoomDrugStoreRatio(double pedRoomDrugStoreRatio) {
		this.pedRoomDrugStoreRatio = pedRoomDrugStoreRatio;
	}
	public double getPedRoomDrugStoreRateRatio() {
		return pedRoomDrugStoreRateRatio;
	}
	public void setPedRoomDrugStoreRateRatio(double pedRoomDrugStoreRateRatio) {
		this.pedRoomDrugStoreRateRatio = pedRoomDrugStoreRateRatio;
	}
	public double getPedRoomDrugStoreWh() {
		return pedRoomDrugStoreWh;
	}
	public void setPedRoomDrugStoreWh(double pedRoomDrugStoreWh) {
		this.pedRoomDrugStoreWh = pedRoomDrugStoreWh;
	}
	public double getPedRoomDrugStoreWhRate() {
		return pedRoomDrugStoreWhRate;
	}
	public void setPedRoomDrugStoreWhRate(double pedRoomDrugStoreWhRate) {
		this.pedRoomDrugStoreWhRate = pedRoomDrugStoreWhRate;
	}
	public double getPedRoomDrugStoreWhRatio() {
		return pedRoomDrugStoreWhRatio;
	}
	public void setPedRoomDrugStoreWhRatio(double pedRoomDrugStoreWhRatio) {
		this.pedRoomDrugStoreWhRatio = pedRoomDrugStoreWhRatio;
	}
	public double getPedRoomDrugStoreWhRateRatio() {
		return pedRoomDrugStoreWhRateRatio;
	}
	public void setPedRoomDrugStoreWhRateRatio(double pedRoomDrugStoreWhRateRatio) {
		this.pedRoomDrugStoreWhRateRatio = pedRoomDrugStoreWhRateRatio;
	}
	public double getResClinic() {
		return resClinic;
	}
	public void setResClinic(double resClinic) {
		this.resClinic = resClinic;
	}
	public double getResClinicRate() {
		return resClinicRate;
	}
	public void setResClinicRate(double resClinicRate) {
		this.resClinicRate = resClinicRate;
	}
	public double getResClinicRatio() {
		return resClinicRatio;
	}
	public void setResClinicRatio(double resClinicRatio) {
		this.resClinicRatio = resClinicRatio;
	}
	public double getResClinicRateRatio() {
		return resClinicRateRatio;
	}
	public void setResClinicRateRatio(double resClinicRateRatio) {
		this.resClinicRateRatio = resClinicRateRatio;
	}
	public double getResRoom() {
		return resRoom;
	}
	public void setResRoom(double resRoom) {
		this.resRoom = resRoom;
	}
	public double getResRoomRate() {
		return resRoomRate;
	}
	public void setResRoomRate(double resRoomRate) {
		this.resRoomRate = resRoomRate;
	}
	public double getResRoomRatio() {
		return resRoomRatio;
	}
	public void setResRoomRatio(double resRoomRatio) {
		this.resRoomRatio = resRoomRatio;
	}
	public double getResRoomRateRatio() {
		return resRoomRateRatio;
	}
	public void setResRoomRateRatio(double resRoomRateRatio) {
		this.resRoomRateRatio = resRoomRateRatio;
	}
	public double getHomeWh() {
		return homeWh;
	}
	public void setHomeWh(double homeWh) {
		this.homeWh = homeWh;
	}
	public double getHomeWhRate() {
		return homeWhRate;
	}
	public void setHomeWhRate(double homeWhRate) {
		this.homeWhRate = homeWhRate;
	}
	public double getHomeWhRatio() {
		return homeWhRatio;
	}
	public void setHomeWhRatio(double homeWhRatio) {
		this.homeWhRatio = homeWhRatio;
	}
	public double getHomeWhRateRatio() {
		return homeWhRateRatio;
	}
	public void setHomeWhRateRatio(double homeWhRateRatio) {
		this.homeWhRateRatio = homeWhRateRatio;
	}
}
