package com.example.Client;

import com.example.Client.controller.firstpage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Application {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    @Override
    public void start(Stage stage) {
        try {
            // Establish connection to the server
            socket = new Socket("localhost", 5000);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());

            // Load the firstpage and set the connection
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Client/firstpage.fxml"));
            Scene scene = new Scene(loader.load());
            firstpage controller = loader.getController();
            controller.setConnection(socket, output, input);

            stage.setScene(scene);
            stage.setTitle("LESCO Billing System");
            stage.show();
        } catch (Exception e) {
            System.err.println("Error starting client: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        // Close resources on application exit
        if (output != null) output.close();
        if (input != null) input.close();
        if (socket != null) socket.close();
        System.out.println("Client resources closed.");
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}
