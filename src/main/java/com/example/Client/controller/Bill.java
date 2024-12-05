package com.example.Client.controller;

import com.example.Client.model.BillingInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Bill {

    @FXML
    private TextField cus_id;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;

    // Method to set connection to server
    public void setConnection(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.socket = socket;
        this.output = output;
        this.input = input;
    }

    public void view(ActionEvent actionEvent) {
        try {
            // Get the customer ID from the TextField
            String customerId = cus_id.getText();

            if (customerId.isEmpty()) {
                showAlert("Error", "Customer ID is required!", AlertType.ERROR);
                return;
            }

            // Send the customer ID to the server
            output.writeObject("View Bill"); // Command to server
            output.flush();
            output.writeObject(customerId); // Customer ID
            output.flush();

            // Wait for a response from the server
            Object response = input.readObject();

            if (response instanceof BillingInfo) {
                // Handle BillingInfo object
                showBillDetails((BillingInfo) response);
            } else if (response instanceof String) {
                // Handle String message (e.g., no data found or error)
                showAlert("Response", (String) response, AlertType.INFORMATION);
            } else {
                showAlert("Error", "Unexpected response from the server.", AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to retrieve the bill: " + e.getMessage(), AlertType.ERROR);
        }
    }


    // Method to display the bill details
    private void showBillDetails(BillingInfo bill) {
        // Display the bill details in an alert
        Alert billAlert = new Alert(AlertType.INFORMATION);
        billAlert.setTitle("Bill Details");
        billAlert.setHeaderText("Bill for Customer: " + bill.getCustomerId());
        billAlert.setContentText(
                "Billing Month: " + bill.getBillingMonth() + "\n" +
                        "Current Meter Reading (Regular): " + bill.getCurrentMeterReadingRegular() + "\n" +
                        "Current Meter Reading (Peak): " + (bill.getCurrentMeterReadingPeak() == -1 ? "N/A (Single Phase)" : bill.getCurrentMeterReadingPeak()) + "\n" +
                        "Cost of Electricity: " + bill.getCostOfElectricity() + "\n" +
                        "Sales Tax Amount: " + bill.getSalesTaxAmount() + "\n" +
                        "Fixed Charges: " + bill.getFixedCharges() + "\n" +
                        "Total Billing Amount: " + bill.getTotalBillingAmount() + "\n" +
                        "Due Date: " + bill.getDueDate() + "\n" +
                        "Bill Paid Status: " + bill.getBillPaidStatus() + "\n" +
                        "Bill Payment Date: " + (bill.getBillPaymentDate() != null ? bill.getBillPaymentDate() : "Not Paid Yet")
        );
        billAlert.showAndWait();
    }

    // Method to show alerts
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
