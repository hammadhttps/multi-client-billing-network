package com.example.Client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class firstpage {
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
    private void goToCustomer(ActionEvent event) throws IOException {
        // Correct way to load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/customer.fxml"));
        Parent root = loader.load();  // This loads the FXML file into 'root'

        // Get the controller from the loaded FXML
        customer controller = loader.getController();
        controller.setConnection(socket, output, input);

        // Set the scene on the current stage
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        // Send the command to the server
        try {
            output.writeObject("Customer");
        } catch (IOException e) {
            System.err.println("Failed to send data to server: " + e.getMessage());
        }

        stage.show();
    }


    @FXML
    private void goToEmployee(ActionEvent event) throws IOException {
        // Load Employee FXML and set its controller manually
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/login.fxml"));
        Parent root = loader.load();

        // Set connection for the Employee controller
        Login controller = loader.getController();
        controller.setConnection(socket, output, input);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));

        // Send command to server
        try {
            output.writeObject("Employee");
        } catch (IOException e) {
            System.err.println("Failed to send data to server: " + e.getMessage());
        }

        stage.show();
    }
}
