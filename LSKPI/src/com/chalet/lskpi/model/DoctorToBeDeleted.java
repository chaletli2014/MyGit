package com.chalet.lskpi.model;

public class DoctorToBeDeleted {
	
	private int drId;
	private String drName;
	private String hospitalCode;
	private String hospitalName;
	private String deleteReason;
	
	public int getDrId() {
		return drId;
	}
	public void setDrId(int drId) {
		this.drId = drId;
	}
	public String getDrName() {
		return drName;
	}
	public void setDrName(String drName) {
		this.drName = drName;
	}
	public String getHospitalCode() {
		return hospitalCode;
	}
	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getDeleteReason() {
		return deleteReason;
	}
	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

}
