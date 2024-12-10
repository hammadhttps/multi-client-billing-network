package com.example.Client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class StatusUpdate {
    @FXML
    private TextField cus_id;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;

    // Set connection to server
    public void setConnection(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.socket = socket;
        this.output = output;
        this.input = input;
    }

    // Method triggered by the button
    public void update(ActionEvent event) {
        try {
            // Retrieve the customer ID from the text field
            String customerId = cus_id.getText().trim();

            if (customerId.isEmpty()) {
                showAlert("Error", "Customer ID cannot be empty!", Alert.AlertType.ERROR);
                return;
            }

            output.writeObject(customerId);      // Send customer ID
            output.flush();

            // Wait for the server's response
            String response = (String) input.readObject();

            // Display the response to the user
            showAlert("Response", response, Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update status: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public String updateCustomerId(String customerId) {
        if (customerId == null || customerId.isEmpty()) {
            return "Customer ID cannot be empty!";
        }

        try {
            output.writeObject(customerId);  // Send customer ID
            output.flush();

            // Wait for the server's response
            String response = (String) input.readObject();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to update status: " + e.getMessage();
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
