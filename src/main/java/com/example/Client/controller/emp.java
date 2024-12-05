package com.example.Client.controller;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class emp {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket socket;

    // Reuse the connection set from the firstpage controller
    public void setConnection(Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.socket = socket;
        this.output = output;
        this.input = input;
    }

    public void click(ActionEvent actionEvent) {
        try {
            output.writeObject("Employee");
            output.flush();
        } catch (IOException e) {
            System.err.println("Failed to send data to server: " + e.getMessage());
        }
    }
}
