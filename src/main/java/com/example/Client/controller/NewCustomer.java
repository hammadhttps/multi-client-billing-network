package com.example.Client.controller;

import com.example.Client.model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NewCustomer {
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField cnicField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<String> customerTypeCombo;
    @FXML
    private ComboBox<String> meterTypeCombo;
    @FXML
    private DatePicker connectionDatePicker;
    @FXML
    private Button submitButton;
    @FXML
    private Button backButton;

    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;

    public void initialize() {
        // Initialize ComboBoxes
        customerTypeCombo.getItems().addAll("Domestic", "Commercial");
        meterTypeCombo.getItems().addAll("Single Phase", "Three Phase");
        
        // Set default values
        customerTypeCombo.setValue("Domestic");
        meterTypeCombo.setValue("Single Phase");
        connectionDatePicker.setValue(LocalDate.now());
    }

    public void setConnection(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.socket = socket;
        this.output = output;
        this.input = input;
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        if (!validateForm()) {
            return;
        }

        try {
            // Create customer object
            Customer customer = new Customer(
                customerIdField.getText(),
                cnicField.getText(),
                nameField.getText(),
                addressField.getText(),
                phoneField.getText(),
                customerTypeCombo.getValue(),
                meterTypeCombo.getValue(),
                connectionDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            // Send to server
            output.writeObject("Add Customer");
            output.writeObject(customer);
            output.flush();

            // Get response
            String response = (String) input.readObject();
            
            if (response.contains("successfully")) {
                showAlert(Alert.AlertType.INFORMATION, "Success", response);
                clearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", response);
            }

        } catch (IOException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Connection Error", 
                     "Failed to communicate with server: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/emp.fxml"));
            Parent root = loader.load();
            
            emp controller = loader.getController();
            controller.setConnection(socket, output, input);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", 
                     "Failed to go back: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        // Validate Customer ID (4 digits)
        if (customerIdField.getText().isEmpty() || !customerIdField.getText().matches("\\d{4}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                     "Customer ID must be exactly 4 digits.");
            return false;
        }

        // Validate CNIC (13 digits)
        if (cnicField.getText().isEmpty() || !cnicField.getText().matches("\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                     "CNIC must be exactly 13 digits.");
            return false;
        }

        // Validate Name
        if (nameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                     "Name cannot be empty.");
            return false;
        }

        // Validate Address
        if (addressField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                     "Address cannot be empty.");
            return false;
        }

        // Validate Phone (basic validation)
        if (phoneField.getText().isEmpty() || !phoneField.getText().matches("\\d{10,11}")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                     "Phone number must be 10-11 digits.");
            return false;
        }

        // Validate ComboBoxes
        if (customerTypeCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                     "Please select a customer type.");
            return false;
        }

        if (meterTypeCombo.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                     "Please select a meter type.");
            return false;
        }

        // Validate Date
        if (connectionDatePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", 
                     "Please select a connection date.");
            return false;
        }

        return true;
    }

    private void clearForm() {
        customerIdField.clear();
        cnicField.clear();
        nameField.clear();
        addressField.clear();
        phoneField.clear();
        customerTypeCombo.setValue("Domestic");
        meterTypeCombo.setValue("Single Phase");
        connectionDatePicker.setValue(LocalDate.now());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
