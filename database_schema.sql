-- Multi-Client Billing Network Database Schema
-- Run this script in MySQL to create the required database and tables

CREATE DATABASE IF NOT EXISTS billing_db;
USE billing_db;

-- Employee table for login authentication
CREATE TABLE IF NOT EXISTS employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Customer table
CREATE TABLE IF NOT EXISTS customer (
    customerId VARCHAR(4) PRIMARY KEY,
    cnic VARCHAR(13) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phoneNumber VARCHAR(15) NOT NULL,
    customerType VARCHAR(20) NOT NULL CHECK (customerType IN ('Domestic', 'Commercial')),
    meterType VARCHAR(20) NOT NULL CHECK (meterType IN ('Single Phase', 'Three Phase')),
    connectionDate VARCHAR(10) NOT NULL,
    regularUnitsConsumed INT DEFAULT 0,
    peakHourUnitsConsumed INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- NADRA record table for CNIC expiry tracking
CREATE TABLE IF NOT EXISTS nadrarecord (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customerId VARCHAR(4) NOT NULL,
    cnicIssueDate VARCHAR(10),
    cnicExpiryDate VARCHAR(10),
    FOREIGN KEY (customerId) REFERENCES customer(customerId) ON DELETE CASCADE
);

-- Billing information table
CREATE TABLE IF NOT EXISTS billinginfo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customerId VARCHAR(4) NOT NULL,
    billingMonth VARCHAR(7) NOT NULL, -- Format: MM/YYYY
    currentMeterReadingRegular INT NOT NULL,
    currentMeterReadingPeak INT DEFAULT 0,
    readingEntryDate VARCHAR(10) NOT NULL,
    costOfElectricity DECIMAL(10,2) NOT NULL,
    salesTaxAmount DECIMAL(10,2) NOT NULL,
    fixedCharges DECIMAL(10,2) NOT NULL,
    totalBillingAmount DECIMAL(10,2) NOT NULL,
    billPaidStatus VARCHAR(20) DEFAULT 'Unpaid' CHECK (billPaidStatus IN ('Paid', 'Unpaid')),
    billPaymentDate VARCHAR(10) DEFAULT NULL,
    FOREIGN KEY (customerId) REFERENCES customer(customerId) ON DELETE CASCADE,
    UNIQUE KEY unique_customer_month (customerId, billingMonth)
);

-- Tariff tax information table
CREATE TABLE IF NOT EXISTS tarifftaxinfo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customerType VARCHAR(20) NOT NULL,
    meterType VARCHAR(20) NOT NULL,
    regularUnitPrice DECIMAL(6,3) NOT NULL,
    peakHourUnitPrice DECIMAL(6,3) DEFAULT 0,
    taxPercentage DECIMAL(5,2) NOT NULL,
    fixedCharges DECIMAL(8,2) NOT NULL,
    effectiveDate DATE NOT NULL,
    INDEX idx_customer_meter_type (customerType, meterType)
);

-- Insert sample employee for testing
INSERT IGNORE INTO employee (username, password, full_name, email) VALUES 
('admin', 'admin123', 'System Administrator', 'admin@billing.com'),
('operator', 'op123', 'Billing Operator', 'operator@billing.com');

-- Insert sample tariff data
INSERT IGNORE INTO tarifftaxinfo (customerType, meterType, regularUnitPrice, peakHourUnitPrice, taxPercentage, fixedCharges, effectiveDate) VALUES 
('Domestic', 'Single Phase', 16.480, 0.000, 17.00, 500.00, '2024-01-01'),
('Domestic', 'Three Phase', 18.500, 20.500, 17.00, 1000.00, '2024-01-01'),
('Commercial', 'Single Phase', 22.950, 0.000, 20.00, 800.00, '2024-01-01'),
('Commercial', 'Three Phase', 25.000, 30.000, 20.00, 1500.00, '2024-01-01');

-- Insert sample customer for testing
INSERT IGNORE INTO customer (customerId, cnic, name, address, phoneNumber, customerType, meterType, connectionDate, regularUnitsConsumed, peakHourUnitsConsumed) VALUES 
('1001', '1234567890123', 'John Doe', '123 Main Street, Lahore', '03001234567', 'Domestic', 'Single Phase', '01/01/2024', 250, 0),
('1002', '9876543210987', 'ABC Company', '456 Business Avenue, Karachi', '03219876543', 'Commercial', 'Three Phase', '15/02/2024', 800, 300);

-- Insert corresponding NADRA records
INSERT IGNORE INTO nadrarecord (customerId, cnicIssueDate, cnicExpiryDate) VALUES 
('1001', '01/01/2020', '01/01/2030'),
('1002', '15/06/2019', '15/06/2029');

-- Insert sample billing information
INSERT IGNORE INTO billinginfo (customerId, billingMonth, currentMeterReadingRegular, currentMeterReadingPeak, readingEntryDate, costOfElectricity, salesTaxAmount, fixedCharges, totalBillingAmount, billPaidStatus, billPaymentDate) VALUES 
('1001', '12/2024', 250, 0, '01/12/2024', 4120.00, 700.40, 500.00, 5320.40, 'Unpaid', NULL),
('1002', '12/2024', 800, 300, '02/12/2024', 29000.00, 5800.00, 1500.00, 36300.00, 'Paid', '15/12/2024');

COMMIT;

-- Display created tables
SHOW TABLES;

-- Display table structures
DESCRIBE employee;
DESCRIBE customer;
DESCRIBE nadrarecord;
DESCRIBE billinginfo;
DESCRIBE tarifftaxinfo;