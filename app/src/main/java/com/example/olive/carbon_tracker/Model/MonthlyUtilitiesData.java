package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;

public class MonthlyUtilitiesData {
    private String startDate;
    private String endDate;
    private long totalDays;
    private double IndElecUsage;
    private double IndGasUsage;
    private double IndCO2;
    private long numOfPeople;
    private int iconId = R.drawable.bills;
    private long UtilityDBId;

    public MonthlyUtilitiesData(String startDate, String endDate, long totalDays, double indElecUsage,
                                double indGasUsage, long numOfPeople, double indCO2, long utilityDBId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.IndElecUsage = indElecUsage;
        this.IndGasUsage = indGasUsage;
        this.numOfPeople = numOfPeople;
        this.IndCO2 = indCO2;
        this.UtilityDBId = utilityDBId;
    }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public long getTotalDays() { return totalDays; }

    public void setTotalDays(long totalDays) { this.totalDays = totalDays; }

    public double getIndElecUsage() { return IndElecUsage; }

    public void setIndElecUsage(double indElecUsage) { IndElecUsage = indElecUsage; }

    public double getIndGasUsage() { return IndGasUsage; }

    public void setIndGasUsage(double indGasUsage) { IndGasUsage = indGasUsage; }

    public double getIndCO2() { return IndCO2; }

    public void setIndCO2(double indCO2) { IndCO2 = indCO2; }

    public long getNumOfPeople() { return numOfPeople; }

    public void setNumOfPeople(long numOfPeople) { this.numOfPeople = numOfPeople; }

    public int getIconID(){ return iconId; }

    public long getUtilityDBId() {
        return UtilityDBId;
    }
}
