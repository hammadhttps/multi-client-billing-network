package com.example.Client.model;



public class NadraRecord {
    private String cnic;
    private String issuanceDate; // Format: DD/MM/YYYY
    private String expiryDate; // Format: DD/MM/YYYY

    public NadraRecord(String cnic, String issuanceDate, String expiryDate) {
        this.cnic = cnic;
        this.issuanceDate = issuanceDate;
        this.expiryDate = expiryDate;
    }

    // Getters
    public String getCnic() {
        return cnic;
    }

    public String getIssuanceDate() {
        return issuanceDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    // Setters
    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public void setIssuanceDate(String issuanceDate) {
        this.issuanceDate = issuanceDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }




}
