package com.example.Client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class customer {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;



    @FXML
    private void viewCurrentBill(ActionEvent event) throws IOException {
        // Get customer ID from user
        javafx.scene.control.TextInputDialog dialog = new javafx.scene.control.TextInputDialog();
        dialog.setTitle("View Current Bill");
        dialog.setHeaderText("Enter Customer ID");
        dialog.setContentText("Customer ID (4 digits):");
        
        java.util.Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().trim().isEmpty()) {
            String customerId = result.get().trim();
            
            if (!customerId.matches("\\d{4}")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Customer ID must be exactly 4 digits.");
                return;
            }
            
            try {
                output.writeObject("view Current Bill");
                output.writeObject(customerId);
                output.flush();
                
                // Get response
                Object response = input.readObject();
                if (response instanceof com.example.Client.model.BillingInfo) {
                    com.example.Client.model.BillingInfo bill = (com.example.Client.model.BillingInfo) response;
                    showBillDetails(bill);
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Bill Information", response.toString());
                }
            } catch (ClassNotFoundException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to receive response: " + e.getMessage());
            }
        }
    }

    public void setConnection(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.socket = socket;
        this.output = output;
        this.input = input;
    }

    @FXML
    private void updateCnicExpiryDate(ActionEvent event) throws IOException {
        // Get customer ID from user
        javafx.scene.control.TextInputDialog customerDialog = new javafx.scene.control.TextInputDialog();
        customerDialog.setTitle("Update CNIC Expiry Date");
        customerDialog.setHeaderText("Enter Customer ID");
        customerDialog.setContentText("Customer ID (4 digits):");
        
        java.util.Optional<String> customerResult = customerDialog.showAndWait();
        if (customerResult.isPresent() && !customerResult.get().trim().isEmpty()) {
            String customerId = customerResult.get().trim();
            
            if (!customerId.matches("\\d{4}")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Customer ID must be exactly 4 digits.");
                return;
            }
            
            // Get new expiry date
            javafx.scene.control.TextInputDialog dateDialog = new javafx.scene.control.TextInputDialog();
            dateDialog.setTitle("Update CNIC Expiry Date");
            dateDialog.setHeaderText("Enter New Expiry Date");
            dateDialog.setContentText("Date (DD/MM/YYYY):");
            
            java.util.Optional<String> dateResult = dateDialog.showAndWait();
            if (dateResult.isPresent() && !dateResult.get().trim().isEmpty()) {
                String expiryDate = dateResult.get().trim();
                
                try {
                    output.writeObject("Update Expiry");
                    output.writeObject(customerId);
                    output.writeObject(expiryDate);
                    output.flush();
                    
                    // Get response
                    String response = (String) input.readObject();
                    showAlert(Alert.AlertType.INFORMATION, "Update Result", response);
                } catch (ClassNotFoundException e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to receive response: " + e.getMessage());
                }
            }
        }
    }

    // Helper method to display bill details
    private void showBillDetails(com.example.Client.model.BillingInfo bill) {
        StringBuilder details = new StringBuilder();
        details.append("Customer ID: ").append(bill.getCustomerId()).append("\n");
        details.append("Billing Month: ").append(bill.getBillingMonth()).append("\n");
        details.append("Regular Units: ").append(bill.getCurrentMeterReadingRegular()).append("\n");
        details.append("Peak Units: ").append(bill.getCurrentMeterReadingPeak()).append("\n");
        details.append("Entry Date: ").append(bill.getReadingEntryDate()).append("\n");
        details.append("Cost of Electricity: ").append(bill.getCostOfElectricity()).append("\n");
        details.append("Sales Tax: ").append(bill.getSalesTaxAmount()).append("\n");
        details.append("Fixed Charges: ").append(bill.getFixedCharges()).append("\n");
        details.append("Total Amount: ").append(bill.getTotalBillingAmount()).append("\n");
        details.append("Payment Status: ").append(bill.getBillPaidStatus()).append("\n");
        details.append("Payment Date: ").append(bill.getBillPaymentDate()).append("\n");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Current Bill Details");
        alert.setHeaderText("Billing Information");
        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    // Helper method to display alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
