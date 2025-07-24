# Multi-Client Billing Network System

A comprehensive JavaFX-based billing management system designed for electricity billing with support for multiple clients, employee management, and real-time billing operations.

## Features

### Employee Features
- **Secure Login System**: Username/password authentication for employees
- **Customer Management**: Add new customers with comprehensive validation
- **Billing Operations**: Generate and manage billing information
- **Reports Generation**: View comprehensive billing reports
- **Payment Status Updates**: Update payment status for customer bills
- **Password Management**: Change employee passwords

### Customer Features
- **Bill Viewing**: View current and historical billing information
- **CNIC Expiry Updates**: Update CNIC expiry dates
- **Payment Status Tracking**: Track payment status

### System Features
- **Multi-threaded Server**: Handles multiple concurrent client connections
- **Database Integration**: MySQL database for persistent data storage
- **Real-time Communication**: Socket-based client-server architecture
- **Input Validation**: Comprehensive form validation and error handling
- **Modern UI**: JavaFX-based user interface with custom styling

## Technology Stack

- **Frontend**: JavaFX 17 with FXML
- **Backend**: Java 17 with Socket Programming
- **Database**: MySQL 8.0+
- **Build Tool**: Maven 3.6+
- **UI Libraries**: ControlsFX, FormsFX, ValidatorFX, BootstrapFX

## Prerequisites

1. **Java Development Kit (JDK) 17 or higher**
2. **MySQL Server 8.0 or higher**
3. **Maven 3.6 or higher**
4. **Git** (for cloning the repository)

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd multi-client-billing-network
```

### 2. Database Setup
1. Install and start MySQL server
2. Run the database schema script:
```bash
mysql -u root -p < database_schema.sql
```

This will create:
- Database: `billing_db`
- Tables: `employee`, `customer`, `nadrarecord`, `billinginfo`, `tarifftaxinfo`
- Sample data for testing

### 3. Configure Database Connection
Update the database connection details in `src/main/java/Server/main/DB/DB_connection.java`:
```java
private static final String URL = "jdbc:mysql://localhost:3306/billing_db";
private static final String USER = "your_username";
private static final String PASSWORD = "your_password";
```

### 4. Build the Project
```bash
# Make maven wrapper executable (Linux/Mac)
chmod +x mvnw

# Compile the project
./mvnw clean compile

# Or if you have Maven installed globally
mvn clean compile
```

## Running the Application

### 1. Start the Server
```bash
# Run the server
./mvnw exec:java -Dexec.mainClass="Server.main.Server"
```
The server will start on port 5000 and display connection logs.

### 2. Start the Client Application
In a new terminal:
```bash
# Run the JavaFX client
./mvnw javafx:run
```

## Default Login Credentials

### Employee Login
- **Username**: `admin` | **Password**: `admin123`
- **Username**: `operator` | **Password**: `op123`

### Sample Customer Data
- **Customer ID**: `1001` (John Doe - Domestic, Single Phase)
- **Customer ID**: `1002` (ABC Company - Commercial, Three Phase)

## Usage Guide

### For Employees

1. **Login**: Start with the login screen using employee credentials
2. **Dashboard**: Access various functions from the employee dashboard:
   - **Add New Customer**: Complete form with validation
   - **View Bills**: Search and view customer billing information
   - **Generate Reports**: View comprehensive billing reports
   - **Update Payment Status**: Modify payment status for bills
   - **Change Password**: Update employee password

### For Customers

1. **Customer Portal**: Access customer-specific functions:
   - **View Current Bill**: Enter customer ID to view latest bill
   - **Update CNIC Expiry**: Update CNIC expiry date

## Database Schema

The system uses the following main tables:

- **employee**: Employee authentication and details
- **customer**: Customer information and connection details
- **billinginfo**: Monthly billing records and payment status
- **nadrarecord**: CNIC tracking and expiry information
- **tarifftaxinfo**: Tariff rates and tax information

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   ├── com/example/Client/
│   │   │   ├── controller/     # JavaFX Controllers
│   │   │   ├── model/         # Data Models
│   │   │   └── Client.java    # Main Client Application
│   │   ├── Server/main/
│   │   │   ├── DB/           # Database Connection
│   │   │   ├── ClientHandler.java  # Server Request Handler
│   │   │   └── Main.java     # Server Main Class
│   │   └── module-info.java  # Java Module Configuration
│   └── resources/
│       └── com/example/Client/
│           ├── *.fxml        # JavaFX UI Layouts
│           └── *.css         # Stylesheets
```

## Key Features Implementation

### Client-Server Architecture
- Multi-threaded server handling concurrent connections
- Socket-based communication between client and server
- Automatic resource cleanup and connection management

### Data Validation
- Customer ID: 4-digit validation
- CNIC: 13-digit validation
- Phone numbers: 10-11 digit validation
- Form field validation with user feedback

### Database Operations
- CRUD operations for all entities
- Transaction management
- Foreign key constraints and data integrity
- Prepared statements for SQL injection prevention

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL server is running
   - Check database credentials in `DB_connection.java`
   - Ensure `billing_db` database exists

2. **Port Already in Use**
   - Change server port in `Server.java` if 5000 is occupied
   - Update client connection accordingly

3. **JavaFX Runtime Error**
   - Ensure Java 17+ with JavaFX support
   - Verify module path configuration

4. **Maven Build Issues**
   - Run `./mvnw clean` to clean build artifacts
   - Check internet connection for dependency downloads

## Development

### Adding New Features
1. Create model classes in `com.example.Client.model`
2. Add corresponding database tables
3. Implement server-side handlers in `ClientHandler.java`
4. Create JavaFX controllers and FXML files
5. Update module-info.java if needed

### Testing
- Use provided sample data for testing
- Employee login: admin/admin123
- Sample customers: 1001, 1002

## Contributing

1. Fork the repository
2. Create a feature branch
3. Implement changes with proper testing
4. Submit a pull request

## License

This project is developed for educational purposes. Please refer to the license file for usage terms.

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review server and client logs
3. Verify database connectivity
4. Ensure all dependencies are properly installed