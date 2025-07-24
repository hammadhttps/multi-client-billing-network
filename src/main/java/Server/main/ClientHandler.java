package Server.main;

import com.example.Client.model.BillingInfo;
import com.example.Client.model.Customer;
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
                    case "Add Customer":
                        addCustomer();
                        break;
                    case"view Current Bill":
                        viewCurrentBill();
                        break;

                    case"Update Expiry":
                        updateExpiry();
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


    private void view_reports() throws IOException, ClassNotFoundException {
        // Get all billing information for reporting
        String query = "SELECT * FROM billinginfo ORDER BY billingMonth DESC";
        try (PreparedStatement stmt = dbConnection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                StringBuilder report = new StringBuilder();
                report.append("=== BILLING REPORTS ===\n\n");
                
                int count = 0;
                while (rs.next() && count < 50) { // Limit to 50 records
                    report.append("Customer ID: ").append(rs.getString("customerId")).append("\n");
                    report.append("Billing Month: ").append(rs.getString("billingMonth")).append("\n");
                    report.append("Regular Units: ").append(rs.getInt("currentMeterReadingRegular")).append("\n");
                    report.append("Peak Units: ").append(rs.getInt("currentMeterReadingPeak")).append("\n");
                    report.append("Total Amount: ").append(rs.getDouble("totalBillingAmount")).append("\n");
                    report.append("Status: ").append(rs.getString("billPaidStatus")).append("\n");
                    report.append("Payment Date: ").append(rs.getString("billPaymentDate")).append("\n");
                    report.append("-".repeat(50)).append("\n");
                    count++;
                }
                
                if (count == 0) {
                    report.append("No billing records found.");
                }
                
                sendResponse(report.toString());
            }
        } catch (SQLException e) {
            System.err.println("Database error during report generation: " + e.getMessage());
            sendResponse("Error generating reports.");
        }
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

    private void viewCurrentBill() throws IOException, ClassNotFoundException {
        String cusId = (String) input.readObject();
        if (cusId == null || cusId.isEmpty()) {
            sendResponse("Customer ID cannot be empty.");
            return;
        }

        // Get the most recent billing information for this customer
        String query = "SELECT * FROM billinginfo WHERE customerId = ? ORDER BY billingMonth DESC LIMIT 1";
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
                            rs.getString("billPaymentDate")
                    );
                    output.writeObject(bill);
                    output.flush();
                } else {
                    output.writeObject("No current billing information found for Customer ID: " + cusId);
                    output.flush();
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during current bill retrieval: " + e.getMessage());
            output.writeObject("Error while fetching current billing info.");
            output.flush();
        }
    }

    private void updateExpiry() throws IOException, ClassNotFoundException {
        String cusId = (String) input.readObject();
        String newExpiryDate = (String) input.readObject();
        
        if (cusId == null || cusId.isEmpty() || newExpiryDate == null || newExpiryDate.isEmpty()) {
            sendResponse("Customer ID and expiry date cannot be empty.");
            return;
        }

        // Check if customer exists
        String checkQuery = "SELECT customerId FROM customer WHERE customerId = ?";
        try (PreparedStatement checkStmt = dbConnection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, cusId);
            
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (!rs.next()) {
                    sendResponse("Customer with ID " + cusId + " not found.");
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during customer check: " + e.getMessage());
            sendResponse("Error checking customer existence.");
            return;
        }

        // Update CNIC expiry date (assuming we have a nadrarecord table)
        String updateQuery = "UPDATE nadrarecord SET cnicExpiryDate = ? WHERE customerId = ?";
        try (PreparedStatement updateStmt = dbConnection.prepareStatement(updateQuery)) {
            updateStmt.setString(1, newExpiryDate);
            updateStmt.setString(2, cusId);

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                sendResponse("CNIC expiry date updated successfully for Customer ID: " + cusId);
                System.out.println("CNIC expiry updated for customer: " + cusId);
            } else {
                sendResponse("No NADRA record found for Customer ID: " + cusId);
            }
        } catch (SQLException e) {
            System.err.println("Database error during expiry update: " + e.getMessage());
            sendResponse("Error updating CNIC expiry date.");
        }
    }

    private void addCustomer() throws IOException, ClassNotFoundException {
        Customer customer = (Customer) input.readObject();
        String customerId = customer.getCustomerId();
        String cnic = customer.getCnic();

        if (customerId == null || customerId.isEmpty() || cnic == null || cnic.isEmpty()) {
            sendResponse("Customer ID and CNIC cannot be empty.");
            return;
        }

        // Check if customer already exists
        String checkQuery = "SELECT customerId FROM customer WHERE customerId = ? OR cnic = ?";
        try (PreparedStatement checkStmt = dbConnection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, customerId);
            checkStmt.setString(2, cnic);
            
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    sendResponse("Customer with ID " + customerId + " or CNIC " + cnic + " already exists.");
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during customer check: " + e.getMessage());
            sendResponse("Error checking customer existence.");
            return;
        }

        // Insert new customer
        String insertQuery = "INSERT INTO customer (customerId, cnic, name, address, phoneNumber, customerType, meterType, connectionDate, regularUnitsConsumed, peakHourUnitsConsumed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = dbConnection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, customer.getCustomerId());
            insertStmt.setString(2, customer.getCnic());
            insertStmt.setString(3, customer.getName());
            insertStmt.setString(4, customer.getAddress());
            insertStmt.setString(5, customer.getPhoneNumber());
            insertStmt.setString(6, customer.getCustomerType());
            insertStmt.setString(7, customer.getMeterType());
            insertStmt.setString(8, customer.getConnectionDate());
            insertStmt.setInt(9, customer.getRegularUnitsConsumed());
            insertStmt.setInt(10, customer.getPeakHourUnitsConsumed());

            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                sendResponse("Customer added successfully! Customer ID: " + customerId);
                System.out.println("New customer added: " + customerId);
            } else {
                sendResponse("Failed to add customer. Please try again.");
            }
        } catch (SQLException e) {
            System.err.println("Database error during customer insertion: " + e.getMessage());
            sendResponse("Error adding customer to database.");
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
