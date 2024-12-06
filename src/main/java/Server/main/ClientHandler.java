package Server.main;

import com.example.Client.model.BillingInfo;
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
                System.out.println("Received command: " + command);

                switch (command) {
                    case "Customer":
                        handleCustomer();
                        break;
                    case "Employee":
                        handleEmployeeLogin();
                        break;
                    case "View Bill":
                        viewBill();
                        break;
                    case "View Reports":
                       view_reports();
                        break;
                    case "Update Status":
                        update_status();
                        break;
                    default:
                        sendResponse("Unknown command: " + command);
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

    public void update_status() throws IOException, ClassNotFoundException {
        // Receive the customer ID from the client
        String customerId = (String) input.readObject();

        if (customerId == null || customerId.isEmpty()) {
            sendResponse("Customer ID is invalid!");
            return;
        }

        // SQL query to update billing info
        String query = "UPDATE billinginfo " +
                "SET billPaidStatus = 'Paid', billPaymentDate = CURRENT_DATE " +
                "WHERE customerId = ?";

        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, customerId);

            // Execute the update query
            int rowsUpdated = stmt.executeUpdate();

            // Respond to the client
            if (rowsUpdated > 0) {
                sendResponse("Status updated successfully for Customer ID: " + customerId);
            } else {
                sendResponse("No billing record found for Customer ID: " + customerId);
            }
        } catch (SQLException e) {
            System.err.println("Database error during status update: " + e.getMessage());
            sendResponse("An error occurred while updating the status. Please try again.");
        }
    }


    private void view_reports()
    {

    }

    private void handleEmployeeLogin() throws IOException, ClassNotFoundException {
        Employee emp = (Employee) input.readObject();
        String username = emp.getUsername();
        String password = emp.getPassword();

        if (password == null || username == null || username.isEmpty()) {
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
            System.err.println("Database error during login: " + e.getMessage());
            sendResponse("An error occurred while processing your request.");
        }
    }

    private void viewBill() throws IOException, ClassNotFoundException {
        String cusId = (String) input.readObject();
        if (cusId == null || cusId.isEmpty()) {
            sendResponse("Customer ID cannot be empty.");
            return;
        }

        String query = "SELECT * FROM billinginfo WHERE customerId = ?";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            stmt.setString(1, cusId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    BillingInfo bill = new BillingInfo(
                            rs.getString("customerId"),
                            rs.getString("billingMonth"),
                            rs.getInt("currentMeterReadingRegular"),
                            rs.getInt("currentMeterReadingPeak"),
                            rs.getString("readingEntryDate"),
                            rs.getDouble("costOfElectricity"),
                            rs.getDouble("salesTaxAmount"),
                            rs.getDouble("fixedCharges"),
                            rs.getDouble("totalBillingAmount"),
                            rs.getString("billPaidStatus"),
                            rs.getString("billPaymentDate") // Handles NULL for unpaid bills
                    );
                    output.writeObject(bill); // Send the BillingInfo object
                    output.flush();
                } else {
                    output.writeObject("No billing information found for Customer ID: " + cusId);
                    output.flush();
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during bill retrieval: " + e.getMessage());
            output.writeObject("Error while fetching billing info.");
            output.flush();
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
        // Placeholder for customer-specific logic
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
