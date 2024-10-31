package customermanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import menu.config;

public class CustomerManager {
    private config dbConfig;
    private Scanner scanner;

    public CustomerManager(config dbConfig, Scanner scanner) {
        this.dbConfig = dbConfig;
        this.scanner = scanner;
    }

    public void manageCustomers() {
        int choice;
        do {
            displayCustomerMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1:
                    addCustomer();
                    break;
                case 2:
                    viewCustomers();
                    break;
                case 3:
                    updateCustomer();
                    break;
                case 4:
                    deleteCustomer();
                    break;
            }
        } while (choice != 5);
    }

    private void displayCustomerMenu() {
        System.out.println("----------- Customer Menu -----------");
        System.out.println("1. Register Customer              |");
        System.out.println("2. View Customers                 |");
        System.out.println("3. Edit Customer                  |");
        System.out.println("4. Delete Customer                |");
        System.out.println("5. Back to Main Menu              |");
        System.out.println("-------------------------------------");
        System.out.print("Enter your choice:                ");
    }

    private int getValidChoice() {
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue; 
            }
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 5) {
                    return choice; 
                } else {
                    System.out.println("Choice must be between 1 and 5. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private void addCustomer() {
        String customerId = getValidStringInput("Customer ID: ");
        String firstName = getValidStringInput("First Name: ");
        String middleName = getValidStringInput("Middle Name: ");
        String lastName = getValidStringInput("Last Name: ");
        String email = getValidStringInput("Email Address: ");
        String phone = getValidPhoneNumber("Phone Number (11 digits): ");

        String sql = "INSERT INTO tbl_customer (c_id, c_fname, c_mname, c_lname, c_email, c_phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            stmt.setString(2, firstName);
            stmt.setString(3, middleName);
            stmt.setString(4, lastName);
            stmt.setString(5, email);
            stmt.setString(6, phone);
            stmt.executeUpdate();
            System.out.println("Customer registered successfully.");
        } catch (SQLException e) {
            System.out.println("Error registering customer: " + e.getMessage());
        }
    }

    private void viewCustomers() {
        String sqlQuery = "SELECT * FROM tbl_customer";
        String[] columnHeaders = {"Customer ID", "First Name", "Middle Name", "Last Name", "Email", "Phone"};
        String[] columnNames = {"c_id", "c_fname", "c_mname", "c_lname", "c_email", "c_phone"};
        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    private void updateCustomer() {
        // implementation to update customer
    }

    private void deleteCustomer() {
        // implementation to delete customer
    }

    private String getValidStringInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input; 
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }

    private String getValidPhoneNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String phoneNumber = scanner.nextLine().trim();
            if (phoneNumber.matches("\\d{11}")) {
                return phoneNumber; 
            } else {
                System.out.println("Invalid phone number. Please enter exactly 11 digits.");
            }
        }
    }
}