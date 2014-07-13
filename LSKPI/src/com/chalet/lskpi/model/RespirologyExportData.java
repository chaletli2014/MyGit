package com.chalet.lskpi.model;

import java.util.Map;

public class RespirologyExportData {

	private String rsmRegion;
	private String rsmName;
	
	private Map<String, Double> lsNumMap;
	private Map<String, Double> pNumMap;
	private Map<String, Double> aeNumMap;
	
	private Map<String, Double> whRateMap;
	private Map<String, Double> whDaysMap;
	private Map<String, Double> inRateMap;
	private Map<String, Double> averageDoseMap;
	
	public String getRsmRegion() {
		return rsmRegion;
	}
	public void setRsmRegion(String rsmRegion) {
		this.rsmRegion = rsmRegion;
	}
	public String getRsmName() {
		return rsmName;
	}
	public void setRsmName(String rsmName) {
		this.rsmName = rsmName;
	}
	public Map<String, Double> getLsNumMap() {
		return lsNumMap;
	}
	public void setLsNumMap(Map<String, Double> lsNumMap) {
		this.lsNumMap = lsNumMap;
	}
	public Map<String, Double> getpNumMap() {
		return pNumMap;
	}
	public void setpNumMap(Map<String, Double> pNumMap) {
		this.pNumMap = pNumMap;
	}
	public Map<String, Double> getAeNumMap() {
		return aeNumMap;
	}
	public void setAeNumMap(Map<String, Double> aeNumMap) {
		this.aeNumMap = aeNumMap;
	}
	public Map<String, Double> getWhRateMap() {
		return whRateMap;
	}
	public void setWhRateMap(Map<String, Double> whRateMap) {
		this.whRateMap = whRateMap;
	}
	public Map<String, Double> getWhDaysMap() {
		return whDaysMap;
	}
	public void setWhDaysMap(Map<String, Double> whDaysMap) {
		this.whDaysMap = whDaysMap;
	}
	public Map<String, Double> getInRateMap() {
		return inRateMap;
	}
	public void setInRateMap(Map<String, Double> inRateMap) {
		this.inRateMap = inRateMap;
	}
	public Map<String, Double> getAverageDoseMap() {
		return averageDoseMap;
	}
	public void setAverageDoseMap(Map<String, Double> averageDoseMap) {
		this.averageDoseMap = averageDoseMap;
	}
}
