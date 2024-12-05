package com.example.Client.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BillingInfo implements Serializable {
    private String customerId; // Linked to Customer
    private String billingMonth; // E.g., "November 2024"
    private int currentMeterReadingRegular;
    private int currentMeterReadingPeak; // -1 for Single Phase
    private String readingEntryDate; // Format: DD/MM/YYYY
    private double costOfElectricity;
    private double salesTaxAmount;
    private double fixedCharges;
    private double totalBillingAmount;
    private String dueDate; // 7 days after readingEntryDate
    private String billPaidStatus; // "Paid" or "Unpaid"
    private String billPaymentDate; // Format: DD/MM/YYYY or null

    // Constructor
    public BillingInfo(String customerId, String billingMonth, int currentMeterReadingRegular, int currentMeterReadingPeak,
                       String readingEntryDate, double costOfElectricity, double salesTaxAmount, double fixedCharges,
                       double totalBillingAmount, String billPaidStatus, String billPaymentDate) {
        this.customerId = customerId;
        this.billingMonth = billingMonth;
        this.currentMeterReadingRegular = currentMeterReadingRegular;
        this.currentMeterReadingPeak = currentMeterReadingPeak;
        this.readingEntryDate = readingEntryDate;
        this.costOfElectricity = costOfElectricity;
        this.salesTaxAmount = salesTaxAmount;
        this.fixedCharges = fixedCharges;
        this.totalBillingAmount = totalBillingAmount;
        this.billPaidStatus = billPaidStatus;
        this.billPaymentDate = billPaymentDate != null ? billPaymentDate : "Not Paid Yet"; // Handle NULL values
    }


    // Getters
    public String getCustomerId() {
        return customerId;
    }

    public String getBillingMonth() {
        return billingMonth;
    }

    public int getCurrentMeterReadingRegular() {
        return currentMeterReadingRegular;
    }

    public int getCurrentMeterReadingPeak() {
        return currentMeterReadingPeak;
    }

    public String getReadingEntryDate() {
        return readingEntryDate;
    }

    public double getCostOfElectricity() {
        return costOfElectricity;
    }

    public double getSalesTaxAmount() {
        return salesTaxAmount;
    }

    public double getFixedCharges() {
        return fixedCharges;
    }

    public double getTotalBillingAmount() {
        return totalBillingAmount;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getBillPaidStatus() {
        return billPaidStatus;
    }

    public String getBillPaymentDate() {
        return billPaymentDate;
    }

    // Setters
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setBillingMonth(String billingMonth) {
        this.billingMonth = billingMonth;
    }

    public void setCurrentMeterReadingRegular(int currentMeterReadingRegular) {
        this.currentMeterReadingRegular = currentMeterReadingRegular;
    }

    public void setCurrentMeterReadingPeak(int currentMeterReadingPeak) {
        this.currentMeterReadingPeak = currentMeterReadingPeak;
    }

    public void setReadingEntryDate(String readingEntryDate) {
        this.readingEntryDate = readingEntryDate;
    }

    public void setCostOfElectricity(double costOfElectricity) {
        this.costOfElectricity = costOfElectricity;
    }

    public void setSalesTaxAmount(double salesTaxAmount) {
        this.salesTaxAmount = salesTaxAmount;
    }

    public void setFixedCharges(double fixedCharges) {
        this.fixedCharges = fixedCharges;
    }

    public void setTotalBillingAmount(double totalBillingAmount) {
        this.totalBillingAmount = totalBillingAmount;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setBillPaidStatus(String billPaidStatus) {
        this.billPaidStatus = billPaidStatus;
    }

    public void setBillPaymentDate(String billPaymentDate) {
        this.billPaymentDate = billPaymentDate;
    }

    // Method to calculate the due date (7 days after reading entry date)
    private String calculateDueDate(String readingEntryDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = sdf.parse(readingEntryDate);
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, 7); // Adding 7 days for the due date
            return sdf.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to calculate total billing amount (example: simple sum of components)
    public void calculateTotalBillingAmount() {
        this.totalBillingAmount = costOfElectricity + salesTaxAmount + fixedCharges;
    }
}
