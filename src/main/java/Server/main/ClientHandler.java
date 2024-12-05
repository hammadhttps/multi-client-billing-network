package Server.main;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Connection dbConnection;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientHandler(Socket socket, Connection dbConnection) {
        this.clientSocket = socket;
        this.dbConnection = dbConnection;
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            input = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error initializing streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Read and handle commands from the client
                String command = (String) input.readObject();
                System.out.println("Received: " + command);

                switch (command) {
                    case "Customer":
                        handleCustomer();
                        break;
                    case "Employee":
                        handleEmployee();
                        break;
                    default:
                        System.out.println("Unknown command: " + command);
                        break;
                }
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected.");
        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            closeResources();
        }
    }

    private void handleCustomer() {
        System.out.println("Handling customer...");
        // Add logic to handle customer operations
    }

    private void handleEmployee() {
        System.out.println("Handling employee...");
        // Add logic to handle employee operations
    }

    private void closeResources() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Resources closed for client.");
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}
