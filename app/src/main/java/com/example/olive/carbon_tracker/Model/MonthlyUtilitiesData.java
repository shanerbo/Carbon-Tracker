package com.example.olive.carbon_tracker.Model;

import com.example.olive.carbon_tracker.R;

public class MonthlyUtilitiesData {
    private String startDate;
    private String endDate;
    private long totalDays;
    private long IndElecUsage;
    private long IndGasUsage;
    private long IndCO2;

    public MonthlyUtilitiesData(String startDate, String endDate, long totalDays, long indElecUsage, long indGasUsage, long indCO2) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
        this.IndElecUsage = indElecUsage;
        this.IndGasUsage = indGasUsage;
        this.IndCO2 = indCO2;
    }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public long getTotalDays() { return totalDays; }

    public void setTotalDays(long totalDays) { this.totalDays = totalDays; }

    public long getIndElecUsage() { return IndElecUsage; }

    public void setIndElecUsage(long indElecUsage) { IndElecUsage = indElecUsage; }

    public long getIndGasUsage() { return IndGasUsage; }

    public void setIndGasUsage(long indGasUsage) { IndGasUsage = indGasUsage; }

    public long getIndCO2() { return IndCO2; }

    public void setIndCO2(long indCO2) { IndCO2 = indCO2; }
}
