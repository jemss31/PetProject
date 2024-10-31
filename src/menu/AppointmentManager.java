package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AppointmentManager {
    private config dbConfig;
    private Scanner scanner;

    public AppointmentManager(config dbConfig, Scanner scanner) {
        this.dbConfig = dbConfig;
        this.scanner = scanner;
    }

    public void manageAppointments() {
        int choice;
        do {
            displayAppointmentMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1:
                    addAppointment();
                    break;
                case 2:
                    viewAppointments();
                    break;
                case 3:
                    updateAppointment();
                    break;
                case 4:
                    deleteAppointment();
                    break;
                case 5:
                    // Option to view appointment report can be included here
                    break;
            }
        } while (choice != 6);
    }

    private void displayAppointmentMenu() {
        System.out.println("----------- Appointment Menu -----------");
        System.out.println("1. Add Appointment                  |");
        System.out.println("2. View Appointments                |");
        System.out.println("3. Update Appointment               |");
        System.out.println("4. Delete Appointment               |");
        System.out.println("5. Back to Main Menu                |");
        System.out.println("---------------------------------------");
        System.out.print("Enter your choice:                  ");
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

    private void addAppointment() {
        int petId = getValidPetId();
        int customerId = getValidCustomerId();

        String description = getValidStringInput("Enter Appointment Description: ");
        double cost = getValidCost();
        String date = getValidStringInput("Enter Appointment Date (YYYY-MM-DD): ");

        String sql = "INSERT INTO tbl_appointments (ap_id, a_des, a_cost, c_id, a_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, petId);
            stmt.setString(2, description);
            stmt.setDouble(3, cost);
            stmt.setInt(4, customerId);
            stmt.setString(5, date);
            stmt.executeUpdate();
            System.out.println("Appointment added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding appointment: " + e.getMessage());
        }
    }

    private int getValidPetId() {
        return 0;
        // implementation to get valid Pet ID
    }

    private int getValidCustomerId() {
        return 0;
        // implementation to get valid Customer ID
    }

    private double getValidCost() {
        return 0;
        // implementation to get valid cost
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

    private void viewAppointments() {
        String sqlQuery = "SELECT * FROM tbl_appointments";
        String[] columnHeaders = {"Appointment ID", "Pet ID", "Description", "Cost", "Date"};
        String[] columnNames = {"a_id", "ap_id", "a_des", "a_cost", "a_date"};
        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    private void updateAppointment() {
        // implementation to update appointment
    }

    private void deleteAppointment() {
        // implementation to delete appointment
    }
}