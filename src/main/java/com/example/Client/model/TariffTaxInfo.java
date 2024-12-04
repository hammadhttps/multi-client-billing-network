package com.example.Client.model;

public class TariffTaxInfo {
    private String meterType; // "1Phase" or "3Phase"
    private double regularUnitPrice;
    private double peakHourUnitPrice; // 0 for Single Phase
    private double taxPercentage;
    private double fixedCharges;

    public TariffTaxInfo(String meterType, double regularUnitPrice, double peakHourUnitPrice,
                         double taxPercentage, double fixedCharges) {
        this.meterType = meterType;
        this.regularUnitPrice = regularUnitPrice;
        this.peakHourUnitPrice = peakHourUnitPrice;
        this.taxPercentage = taxPercentage;
        this.fixedCharges = fixedCharges;
    }

    // Getters
    public String getMeterType() {
        return meterType;
    }

    public double getRegularUnitPrice() {
        return regularUnitPrice;
    }

    public double getPeakHourUnitPrice() {
        return peakHourUnitPrice;
    }

    public double getTaxPercentage() {
        return taxPercentage;
    }

    public double getFixedCharges() {
        return fixedCharges;
    }

    // Setters
    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public void setRegularUnitPrice(double regularUnitPrice) {
        this.regularUnitPrice = regularUnitPrice;
    }

    public void setPeakHourUnitPrice(double peakHourUnitPrice) {
        this.peakHourUnitPrice = peakHourUnitPrice;
    }

    public void setTaxPercentage(double taxPercentage) {
        this.taxPercentage = taxPercentage;
    }

    public void setFixedCharges(double fixedCharges) {
        this.fixedCharges = fixedCharges;
    }

    // Method to calculate tax amount based on a given cost
    public double calculateTaxAmount(double cost) {
        return cost * (taxPercentage / 100);
    }


}
