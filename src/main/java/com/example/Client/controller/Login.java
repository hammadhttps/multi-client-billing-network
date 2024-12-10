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

        // Handle business logic
        String response = processLogin(username, password);

        // GUI behavior based on response
        if ("Login successful!".equals(response)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Login successful!");
            switchToEmployeePage(actionEvent);
        } else if ("Invalid credentials. Please try again.".equals(response)) {
            showAlert(Alert.AlertType.ERROR, "Failure", "Invalid credentials. Please try again.");
        } else if (response.startsWith("Error")) {
            showAlert(Alert.AlertType.ERROR, "Error", response);
        } else {
            showAlert(Alert.AlertType.WARNING, "Warning", "Unexpected server response: " + response);
        }
    }

    // Extracted business logic for login processing
    public String processLogin(String username, String password) {
        try {
            Employee employee = new Employee(username, password);
            output.writeObject(employee);
            output.flush();

            System.out.println("Employee object sent to the server.");

            Object response = input.readObject();
            if (response instanceof String) {
                return (String) response;
            } else {
                return "Error: Invalid response from server.";
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "Error during login: " + e.getMessage();
        }
    }

    // GUI method to switch to emp.fxml
    private void switchToEmployeePage(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/emp.fxml"));
            Parent root = loader.load();

            emp empController = loader.getController();
            empController.setConnection(socket, output, input);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load employee page: " + e.getMessage());
            e.printStackTrace();
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

