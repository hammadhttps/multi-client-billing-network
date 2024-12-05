package com.example.Client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class changepass {
    @FXML
    private TextField usnmae; // For username input
    @FXML
    private PasswordField pass; // For current password input
    @FXML
    private PasswordField newpass; // For new password input

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;

    // Set connection from the main application
    public void setConnection(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.socket = socket;
        this.output = output;
        this.input = input;
    }

    @FXML
    public void changepass(ActionEvent actionEvent) {
        String username = usnmae.getText();
        String newPassword = newpass.getText();

        if (username.isEmpty() || newPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all fields.");
            return;
        }

        try {

            output.writeObject(newPassword);
            output.flush();

            System.out.println("Change password request sent to the server.");

            // Handle server response
            Object response = input.readObject();
            if (response instanceof String) {
                String serverResponse = (String) response;
                if ("Password updated successfully".equals(serverResponse)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Password updated successfully!");
                } else if ("Failed".equals(serverResponse)) {
                    showAlert(Alert.AlertType.ERROR, "Failure", "Failed to update the password.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Unexpected server response: " + serverResponse);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error during password change: " + e.getMessage());
            System.err.println("Error during password change: " + e.getMessage());
        }
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
