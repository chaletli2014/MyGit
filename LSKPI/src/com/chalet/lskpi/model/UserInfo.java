package com.chalet.lskpi.model;
/**
 * @author Chalet
 * @version 创建时间：2013年11月24日 下午5:16:58
 * 类说明
 */

public class UserInfo {

	private int id;
	private String name;
	private String telephone;
	private String etmsCode;
	private String userCode;
	private String bu;
	private String regionCenter;
	private String region; //RSM
	private String teamCode;
	private String team;
	private String level;
	private String superior;
	private String email;
	
	private String realLevel;
	private String regionCenterCN;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEtmsCode() {
		return etmsCode;
	}
	public void setEtmsCode(String etmsCode) {
		this.etmsCode = etmsCode;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getSuperior() {
		return superior;
	}
	public void setSuperior(String superior) {
		this.superior = superior;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getBu() {
		return bu;
	}
	public void setBu(String bu) {
		this.bu = bu;
	}
	public String getRegionCenter() {
		return regionCenter;
	}
	public void setRegionCenter(String regionCenter) {
		this.regionCenter = regionCenter;
	}
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String teamCode) {
		this.teamCode = teamCode;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    public String getRealLevel() {
        return realLevel;
    }
    public void setRealLevel(String realLevel) {
        this.realLevel = realLevel;
    }
    public String getRegionCenterCN() {
        return regionCenterCN;
    }
    public void setRegionCenterCN(String regionCenterCN) {
        this.regionCenterCN = regionCenterCN;
    }
}
