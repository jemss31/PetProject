package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AppointmentManager {
    private config dbConfig;
    private Scanner scanner;

    public AppointmentManager(config dbConfig, Scanner scanner) {
        this.dbConfig = dbConfig;
        this.scanner = scanner;
    }
    private void displayAppointmentMenu() {
        System.out.println("----------- Appointment Menu -----------");
        System.out.println("1. Add Appointment                  |");
        System.out.println("2. View Appointments                |");
        System.out.println("3. Update Appointment               |");
        System.out.println("4. Delete Appointment               |");
        System.out.println("5. Update Transaction Status        |"); 
        System.out.println("6. Back to Main Menu                |");
        System.out.println("---------------------------------------");
        System.out.print("Enter your choice:                  ");
    }

    public void manageAppointments() {
        int choice;
        do {
            displayAppointmentMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1:
                    viewAppointments();
                    addAppointment();
                    break;
                case 2:
                    viewAppointments();
                    break;
                case 3:
                    viewAppointments();
                    updateAppointment();
                    break;
                case 4:
                    viewAppointments();
                    deleteAppointment();
                    viewAppointments();
                    break;
                case 5:
                    viewAppointments();
                    updateTransactionStatus();
                    viewAppointments();
                    break;
            }
        } while (choice != 6);
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
                if (choice >= 1 && choice <= 6) {
                    return choice;
                } else {
                    System.out.println("Choice must be between 1 and 6. Please try again.");
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
    String status = getValidStringInput("Enter Appointment Status (paid/unpaid): "); 
    String date = getValidStringInput("Enter Appointment Date (YYYY-MM-DD): "); 

    
    String sql = "INSERT INTO tbl_appointments (a_des, a_cost, c_id, a_date, l_status) VALUES (?, ?, ?, ?, ?)";
    
    try (Connection conn = dbConfig.connectDB();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, description); 
        stmt.setDouble(2, cost);
        stmt.setInt(3, customerId); 
        stmt.setString(4, date); 
        stmt.setString(5, status); 

        stmt.executeUpdate(); 
        System.out.println("Appointment added successfully.");
    } catch (SQLException e) {
        System.out.println("Error adding appointment: " + e.getMessage());
    }
}

   


    private int getValidPetId() {
        while (true) {
            System.out.print("Enter Pet ID: ");
            String input = scanner.nextLine().trim();
            try {
                int petId = Integer.parseInt(input);
                if (petExists(petId)) {
                    return petId;
                } else {
                    System.out.println("No pet found with ID: " + petId);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid Pet ID.");
            }
        }
    }

    private int getValidCustomerId() {
        while (true) {
            System.out.print("Enter Customer ID: ");
            String input = scanner.nextLine().trim();
            try {
                int customerId = Integer.parseInt(input);
                if (customerExists(customerId)) {
                    return customerId;
                } else {
                    System.out.println("No customer found with ID: " + customerId);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid Customer ID.");
            }
        }
    }

    private double getValidCost() {
        while (true) {
            System.out.print("Enter Appointment Cost: ");
            String input = scanner.nextLine().trim();
            try {
                double cost = Double.parseDouble(input);
                if (cost >= 0) {
                    return cost;
                } else {
                    System.out.println("Cost must be a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid cost.");
            }
        }
    }

    private boolean petExists(int petId) {
        String sql = "SELECT COUNT(*) FROM tbl_breed WHERE p_id = ?";
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, petId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking pet existence: " + e.getMessage());
            return false;
        }
    }
    private boolean customerExists(int customerId) {
        String sql = "SELECT COUNT(*) FROM tbl_customer WHERE c_id = ?";
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking customer existence: " + e.getMessage());
            return false;
        }
    }
    
    private void viewAppointments() {
        String sqlQuery = "SELECT * FROM tbl_appointments";
        String[] columnHeaders = {"Appointment ID", "Pet ID", "Description", "Cost", "Date"};
        String[] columnNames = {"a_id", "ap_id", "a_des", "a_cost", "a_date"};
        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    private void updateAppointment() {
        int appointmentId = getValidAppointmentId();

        String description = getValidStringInput("Enter new Appointment Description: ");
        double cost = getValidCost();
        String date = getValidStringInput("Enter new Appointment Date (YYYY-MM-DD): ");

        String sql = "UPDATE tbl_appointments SET a_des = ?, a_cost = ?, a_date = ? WHERE a_id = ?";
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, description);
            stmt.setDouble(2, cost);
            stmt.setString(3, date);
            stmt.setInt(4, appointmentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment updated successfully.");
            } else {
                System.out.println("No appointment found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating appointment: " + e.getMessage());
        }
    }

     private void deleteAppointment() {
        int appointmentId = getValidAppointmentId();

        String sql = "DELETE FROM tbl_appointments WHERE a_id = ?";
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment deleted successfully.");
            } else {
                System.out.println("No appointment found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
        }
    }
 private void updateTransactionStatus() {
        int appointmentId = getValidAppointmentId();
        String newStatus = getValidStringInput("Enter new status (paid/unpaid): ");

        String sql = "UPDATE tbl_appointments SET l_status = ? WHERE a_id = ?";
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, appointmentId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction status updated successfully.");
            } else {
                System.out.println("No appointment found with the given ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating transaction status: " + e.getMessage());
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
    private int getValidAppointmentId() {
        while (true) {
            System.out.print("Enter Appointment ID: ");
            String input = scanner.nextLine().trim();
            try {
                int appointmentId = Integer.parseInt(input);
                if (appointmentExists(appointmentId)) {
                    return appointmentId;
                } else {
                    System.out.println("No appointment found with ID: " + appointmentId);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid Appointment ID.");
            }
        }
    }
     private boolean appointmentExists(int appointmentId) {
        String sql = "SELECT COUNT(*) FROM tbl_appointments WHERE a_id = ?";
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking appointment existence: " + e.getMessage());
            return false;
        }
    }
}