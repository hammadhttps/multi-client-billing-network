package com.example.Client.model;

public class Customer {
    private String customerId; // 4-digit ID
    private String cnic; // 13-digit CNIC
    private String name;
    private String address;
    private String phoneNumber;
    private String customerType; // "Commercial" or "Domestic"
    private String meterType; // "Single Phase" or "Three Phase"
    private String connectionDate; // Format: DD/MM/YYYY
    private int regularUnitsConsumed;
    private int peakHourUnitsConsumed;

    public Customer(String customerId, String cnic, String name, String address, String phoneNumber,
                    String customerType, String meterType, String connectionDate) {
        this.customerId = customerId;
        this.cnic = cnic;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.customerType = customerType;
        this.meterType = meterType;
        this.connectionDate = connectionDate;
        this.regularUnitsConsumed = 0; // Default value
        this.peakHourUnitsConsumed = meterType.equals("Three Phase") ? 0 : -1; // -1 for Single Phase
    }

    // Getters
    public String getCustomerId() {
        return customerId;
    }

    public String getCnic() {
        return cnic;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getMeterType() {
        return meterType;
    }

    public String getConnectionDate() {
        return connectionDate;
    }

    public int getRegularUnitsConsumed() {
        return regularUnitsConsumed;
    }

    public int getPeakHourUnitsConsumed() {
        return peakHourUnitsConsumed;
    }

    // Setters
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType;
    }

    public void setConnectionDate(String connectionDate) {
        this.connectionDate = connectionDate;
    }

    public void setRegularUnitsConsumed(int regularUnitsConsumed) {
        this.regularUnitsConsumed = regularUnitsConsumed;
    }

    public void setPeakHourUnitsConsumed(int peakHourUnitsConsumed) {
        this.peakHourUnitsConsumed = peakHourUnitsConsumed;
    }

    // Methods to update unit consumption
    public void addRegularUnits(int units) {
        this.regularUnitsConsumed += units;
    }

    public void addPeakHourUnits(int units) {
        if (this.peakHourUnitsConsumed != -1) {
            this.peakHourUnitsConsumed += units;
        }
    }

}
