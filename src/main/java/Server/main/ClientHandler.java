package Server.main;

import java.io.*;
import java.net.*;
import java.sql.*;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Connection dbConnection;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientHandler(Socket socket, Connection dbConnection) {
        this.clientSocket = socket;
        this.dbConnection = dbConnection;
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String command = (String) input.readObject();
                switch (command) {
                    case "Customer":
                        System.out.println("Customer");
                        break;
                    case "Employee":
                        System.out.println("Employee");
                        break;
                    // Add more commands as needed
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}