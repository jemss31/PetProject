package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        
        String firstName = getValidStringInput("First Name: ");
        String middleName = getValidStringInput("Middle Name: ");
        String lastName = getValidStringInput("Last Name: ");
        String email = getValidStringInput("Email Address: ");
        String phone = getValidPhoneNumber("Phone Number (11 digits): ");

        String sql = "INSERT INTO tbl_customer ( c_fname, c_mname, c_lname, c_email, c_phone) VALUES ( ?, ?, ?, ?, ?)";
        executeUpdate(sql, firstName, middleName, lastName, email, phone);
    }

    private void viewCustomers() {
        String sqlQuery = "SELECT * FROM tbl_customer";
        String[] columnHeaders = {"Customer ID", "First Name", "Middle Name", "Last Name", "Email", "Phone"};
        String[] columnNames = {"c_id", "c_fname", "c_mname", "c_lname", "c_email", "c_phone"};
        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    private void updateCustomer() {
        String customerId = getValidStringInput("Enter Customer ID to update: ");
        if (!customerExists(customerId)) {
            System.out.println("No customer found with ID: " + customerId);
            return;
        }

        String newFirstName = getValidStringInput("New First Name: ");
        String newMiddleName = getValidStringInput("New Middle Name: ");
        String newLastName = getValidStringInput("New Last Name: ");
        String newEmail = getValidStringInput("New Email Address: ");
        String newPhone = getValidPhoneNumber("New Phone Number (11 digits): ");

        String sql = "UPDATE tbl_customer SET c_fname = ?, c_mname = ?, c_lname = ?, c_email = ?, c_phone = ? WHERE c_id = ?";
        executeUpdate(sql, newFirstName, newMiddleName, newLastName, newEmail, newPhone, customerId);
    }

    private void deleteCustomer() {
        String customerId = getValidStringInput("Enter Customer ID to delete: ");
        if (!customerExists(customerId)) {
            System.out.println("No customer found with ID: " + customerId);
            return;
        }

        String sql = "DELETE FROM tbl_customer WHERE c_id = ?";
        executeUpdate(sql, customerId);
    }

    private void executeUpdate(String sql, String... params) {
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }
            stmt.executeUpdate();
            System.out.println("Operation completed successfully.");
        } catch (SQLException e) {
            System.out.println("Error executing operation: " + e.getMessage());
        }
    }

    private boolean customerExists(String customerId) {
        String sql = "SELECT COUNT(*) FROM tbl_customer WHERE c_id = ?";
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking customer existence: " + e.getMessage());
            return false;
        }
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
