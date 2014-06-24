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
}
