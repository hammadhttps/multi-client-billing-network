package Server.main;

import com.example.Client.model.Employee;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                        Employee emp = (Employee) input.readObject();
                        handleLogin(emp);
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

    // Handle login logic
    private void handleLogin(Employee emp) throws IOException, ClassNotFoundException {
        String username = emp.getUsername();
        String password =  emp.getPassword();

        if (password == null) {
            sendResponse("Invalid credentials. Please try again.");
            return;
        }

        String query = "SELECT * FROM employee WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    sendResponse("Login successful!");
                    System.out.println("Login successful for user: " + username);
                } else {
                    sendResponse("Invalid credentials. Please try again.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            sendResponse("An error occurred while processing your request.");
        }

        String command = (String) input.readObject();
        switch (command) {
            case "Change Password":
                changePassword(emp);
                break;
            case "Employee":

                break;
            default:
                break;
        }

    }

    private void changePassword(Employee emp) throws IOException, ClassNotFoundException {
        // Receive username
        String username = emp.getUsername();
        // Receive new password
        String newPassword = (String) input.readObject();

        if (newPassword == null || newPassword.isEmpty()) {
            sendResponse("Password cannot be empty. Please try again.");
            return;
        }

        String updateQuery = "UPDATE employee SET password = ? WHERE username = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(updateQuery)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                sendResponse("Password updated successfully");
                System.out.println("Password updated for user: " + username);
            } else {
                sendResponse("Failed");
            }
        } catch (SQLException e) {
            System.err.println("Database error while updating password: " + e.getMessage());
            sendResponse("An error occurred while updating your password. Please try again later.");
        }
    }



    private void sendResponse(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            System.err.println("Error sending response: " + e.getMessage());
        }
    }

    private void handleCustomer() {
        System.out.println("Handling customer...");
        // Add logic to handle customer operations
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
