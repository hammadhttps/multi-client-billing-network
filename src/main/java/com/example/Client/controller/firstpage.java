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


    @FXML
    private void goToCustomer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/Client/customer.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        try {
            output.writeObject("Customer");
        } catch (IOException e) {
            System.err.println("Failed to send data to server: " + e.getMessage());
        }
        stage.show();
    }

    @FXML
    private void goToEmployee(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/Client/emp.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        try {
            output.writeObject("Employee");
        } catch (IOException e) {
            System.err.println("Failed to send data to server: " + e.getMessage());
        }
        stage.show();
    }

    public void setconnection(ActionEvent actionEvent)
    {
        try {
            socket = new Socket("localhost", 5000);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}