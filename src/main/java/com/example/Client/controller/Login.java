package com.example.Client.controller;

import com.example.Client.model.Employee;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Login {
    @FXML
    private TextField usname; // For username input
    @FXML
    private PasswordField pass; // For password input

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
    public void login(ActionEvent actionEvent) {
        String username = usname.getText();
        String password = pass.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in both fields.");
            return;
        }

        // Create Employee object with user input
        Employee employee = new Employee(username, password);

        try {
            // Send Employee object to the server
            output.writeObject(employee);
            output.flush();
            System.out.println("Employee object sent to the server.");

            // Handle server response
            Object response = input.readObject();
            if (response instanceof String) {
                String serverResponse = (String) response;
                if ("Login successful!".equals(serverResponse)) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");

                    // Load emp.fxml after successful login
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/emp.fxml"));
                    Parent root = loader.load();

                    // Pass the connection to emp controller
                    emp empController = loader.getController();
                    empController.setConnection(socket, output, input);

                    // Switch the scene
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else if ("Invalid credentials. Please try again.".equals(serverResponse)) {
                    showAlert(Alert.AlertType.ERROR, "Failure", "Invalid credentials. Please try again.");
                } else {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Unexpected server response: " + serverResponse);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error during login: " + e.getMessage());
            System.err.println("Error during login: " + e.getMessage());
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
