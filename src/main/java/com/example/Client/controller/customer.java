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
        output.writeObject("view Current Bill");


    }

    public void setConnection(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.socket = socket;
        this.output = output;
        this.input = input;
    }

    @FXML
    private void updateCnicExpiryDate(ActionEvent event) throws IOException {
        output.writeObject("Update Expiry");


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
