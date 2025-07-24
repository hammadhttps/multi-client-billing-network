package com.example.Client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class emp {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;

    // Reuse the connection set from the Login controller
    public void setConnection(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.socket = socket;
        this.output = output;
        this.input = input;
    }

    public void changepass(ActionEvent actionEvent) throws IOException {

        output.writeObject("Change Password");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/changepass.fxml"));
        Parent root = loader.load();

        // Pass the connection to emp controller
        changepass empController = loader.getController();
        empController.setConnection(socket, output, input);

        // Switch the scene
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void add_new_customer(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/new_customer.fxml"));
        Parent root = loader.load();

        // Pass the connection to new customer controller
        NewCustomer controller = loader.getController();
        controller.setConnection(socket, output, input);

        // Switch the scene
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void update_customer_data(ActionEvent actionEvent)
    {

    }

    public void generate_report(ActionEvent event)
            throws IOException {

        output.writeObject("View Bill");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/bill.fxml"));
        Parent root = loader.load();

        // Pass the connection to emp controller
        Bill empController = loader.getController();
        empController.setConnection(socket, output, input);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();


    }

    public void update_tax_tariff(ActionEvent actionEvent) {
    }

    public void add_Billing(ActionEvent actionEvent)
    {
    }

    public void update_status(ActionEvent event) throws IOException {

        output.writeObject("Update Status");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/status_update.fxml"));
        Parent root = loader.load();

        // Pass the connection to emp controller
        StatusUpdate empController = loader.getController();
        empController.setConnection(socket, output, input);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void view_bill(ActionEvent event) throws IOException {

        output.writeObject("View Bill");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/bill.fxml"));
        Parent root = loader.load();

        // Pass the connection to emp controller
        Bill empController = loader.getController();
        empController.setConnection(socket, output, input);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();


    }
}
