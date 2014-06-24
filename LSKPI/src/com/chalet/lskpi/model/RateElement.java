package com.chalet.lskpi.model;
/**
 * @author Chalet
 * @version 创建时间：2013年12月12日 下午10:53:52
 * 类说明
 */

public class RateElement {
	
	public RateElement(String typeName){
		this.typeName = typeName;
	}
	
	private int rateType;
	private double rateNum;
	
	private String typeName;
	private String rateName;
	
	private double rateRatio;
	
	public int getRateType() {
		return rateType;
	}
	public void setRateType(int rateType) {
		this.rateType = rateType;
	}
	public double getRateNum() {
		return rateNum;
	}
	public void setRateNum(double rateNum) {
		this.rateNum = rateNum;
	}
	public String getRateName() {
		String name = "1mg/d";
		if( this.typeName == "ped" ){
			if( this.rateType == 1 ){
				name = "0.5mg/d";
			}else if( this.rateType == 2){
				name = "1mg/d";
			}else if( this.rateType == 3){
				name = "2mg/d";
			}else{
				name = rateType+"mg/d";
			}
			return name;
		}else{
			return this.rateType+"mg/d";
		}
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public double getRateRatio() {
		return rateRatio;
	}
	public void setRateRatio(double rateRatio) {
		this.rateRatio = rateRatio;
	}
}
