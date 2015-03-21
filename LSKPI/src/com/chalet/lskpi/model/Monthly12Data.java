package com.chalet.lskpi.model;

/**
 * @author Chalet
 * @version 创建时间：2013年12月24日 上午2:19:50
 * 类说明
 */

public class Monthly12Data {

    private String dataMonth;
	private int pedemernum;
	private int pedroomnum;
	private int resnum;
	private int othernum;
	private int hosNum;
	private int totalnum;
	private int inNum;
	
	/**
	 * 儿科门急诊药房
	 */
	private double pedEmerDrugStore;
	
	/**
	 * 儿科门急诊雾化室
	 */
	private double pedEmerWh;
	
	/**
	 * 儿科病房药房
	 */
	private double pedRoomDrugStore;
	
	/**
	 * 儿科病房药房雾化
	 */
	private double pedRoomDrugStoreWh;
	
	/**
	 * 家庭雾化
	 */
	private double homeWh;
	
	/**
	 * 呼吸科门诊
	 */
	private double resClinic;
	
	/**
	 * 呼吸科病房
	 */
	private double resRoom;
	
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
    public int getHosNum() {
        return hosNum;
    }
    public void setHosNum(int hosNum) {
        this.hosNum = hosNum;
    }
    public String getDataMonth() {
        return dataMonth;
    }
    public void setDataMonth(String dataMonth) {
        this.dataMonth = dataMonth;
    }
    public int getTotalnum() {
        return totalnum;
    }
    public void setTotalnum(int totalnum) {
        this.totalnum = totalnum;
    }
	public int getInNum() {
		return inNum;
	}
	public void setInNum(int inNum) {
		this.inNum = inNum;
	}
	public double getPedEmerDrugStore() {
		return pedEmerDrugStore;
	}
	public void setPedEmerDrugStore(double pedEmerDrugStore) {
		this.pedEmerDrugStore = pedEmerDrugStore;
	}
	public double getPedEmerWh() {
		return pedEmerWh;
	}
	public void setPedEmerWh(double pedEmerWh) {
		this.pedEmerWh = pedEmerWh;
	}
	public double getPedRoomDrugStore() {
		return pedRoomDrugStore;
	}
	public void setPedRoomDrugStore(double pedRoomDrugStore) {
		this.pedRoomDrugStore = pedRoomDrugStore;
	}
	public double getPedRoomDrugStoreWh() {
		return pedRoomDrugStoreWh;
	}
	public void setPedRoomDrugStoreWh(double pedRoomDrugStoreWh) {
		this.pedRoomDrugStoreWh = pedRoomDrugStoreWh;
	}
	public double getResClinic() {
		return resClinic;
	}
	public void setResClinic(double resClinic) {
		this.resClinic = resClinic;
	}
	public double getResRoom() {
		return resRoom;
	}
	public void setResRoom(double resRoom) {
		this.resRoom = resRoom;
	}
	public double getHomeWh() {
		return homeWh;
	}
	public void setHomeWh(double homeWh) {
		this.homeWh = homeWh;
	}
}
