package com.chalet.lskpi.model;

public class WeeklyDataOfHospital {
    
    private String duration;
    private String hospitalCode;
    private double pnum;
    private double lsnum;
    private double averageDose;
    
    public String getHospitalCode() {
        return hospitalCode;
    }
    public void setHospitalCode(String hospitalCode) {
        this.hospitalCode = hospitalCode;
    }
    public double getPnum() {
        return pnum;
    }
    public void setPnum(double pnum) {
        this.pnum = pnum;
    }
    public double getLsnum() {
        return lsnum;
    }
    public void setLsnum(double lsnum) {
        this.lsnum = lsnum;
    }
    public double getAverageDose() {
        return averageDose;
    }
    public void setAverageDose(double averageDose) {
        this.averageDose = averageDose;
    }
    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

}
