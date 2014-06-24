package com.chalet.lskpi.model;

public class HomeWeeklyData {

    private String userName;
    private int totalDrNum;
    private int newDrNum;
    /**
     * 上周家庭雾化新病人次量.
     * “卖/赠泵数量”统加
     */
    private double newWhNum;
    
    /**
     * 持续期治疗率.
     * “处方>=8天的哮喘持续期病人次”统加/“哮喘*患者人次”统加
     */
    private double cureRate;
    
    /**
     * 推荐使用令舒的人次.
     * “持续期病人中推荐使用令舒的人次”统加
     */
    private double lsnum;
    
    /**
     * 持续期令舒比例.
     * “持续期病人中推荐使用令舒的人次”统加/“处方>=8天的哮喘持续期病人次” 统加
     */
    private double lsRate;
    
    /**
     * 家庭雾化疗程达标率（DOT>=30天）.
     * “DOT>=30天,病人次”统加/“持续期病人中推荐使用令舒的人次”统加
     */
    private double reachRate;

    public int getTotalDrNum() {
        return totalDrNum;
    }

    public void setTotalDrNum(int totalDrNum) {
        this.totalDrNum = totalDrNum;
    }

    public int getNewDrNum() {
        return newDrNum;
    }

    public void setNewDrNum(int newDrNum) {
        this.newDrNum = newDrNum;
    }

    /**
     * 上周家庭雾化新病人次量.
     * “卖/赠泵数量”统加
     * @return double
     */
    public double getNewWhNum() {
        return newWhNum;
    }

    /**
     * 上周家庭雾化新病人次量.
     * “卖/赠泵数量”统加
     * @param newWhNum double
     */
    public void setNewWhNum(double newWhNum) {
        this.newWhNum = newWhNum;
    }

    /**
     * 持续期治疗率.
     * “处方>=8天的哮喘持续期病人次”统加/“哮喘*患者人次”统加
     * @return double
     */
    public double getCureRate() {
        return cureRate;
    }

    /**
     * 持续期治疗率.
     * “处方>=8天的哮喘持续期病人次”统加/“哮喘*患者人次”统加
     * @param cureRate double
     */
    public void setCureRate(double cureRate) {
        this.cureRate = cureRate;
    }

    /**
     * 推荐使用令舒的人次.
     * “持续期病人中推荐使用令舒的人次”统加
     * @return double
     */
    public double getLsnum() {
        return lsnum;
    }

    /**
     * 推荐使用令舒的人次.
     * @param lsnum
     */
    public void setLsnum(double lsnum) {
        this.lsnum = lsnum;
    }

    public double getLsRate() {
        return lsRate;
    }

    public void setLsRate(double lsRate) {
        this.lsRate = lsRate;
    }

    public double getReachRate() {
        return reachRate;
    }

    public void setReachRate(double reachRate) {
        this.reachRate = reachRate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
