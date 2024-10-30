package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PetMenu {
    private Scanner scanner = new Scanner(System.in);
    private config dbConfig = new config();

    public void mainMenu() {
        int choice;
        do {
            displayMainMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1:
                    petMenu();
                    break;
                case 2:
                    appointmentMenu(); 
                    break; 
                case 3:
                    customerMenu();
                    break;
                case 4:
                    viewAppointmentReport();
                    break;
                case 5:
                    System.out.println("Exiting... Thank you for using the system!"); break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
        scanner.close();
    }

    private void displayMainMenu() {
        System.out.println("----------- Main Menu -----------");
        System.out.println("1. Manage Pets                  |");
        System.out.println("2. Manage Appointments          |");
        System.out.println("3. Manage Customers             |");
        System.out.println("4. View Appointment Report      |");
        System.out.println("5. Exit                         |");
        System.out.println("---------------------------------");
        System.out.print("Enter your choice:              ");
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
                    System.out.println("Choice must be between 1 and 5. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    // Pet Management
    private void petMenu() {
        int choice;
        do {
            displayPetMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1:
                    viewPets();
                    addPet(); 
                    viewPets();
                    break;
                case 2: 
                    viewPets(); 
                    break;
                case 3: 
                    viewPets();
                    updatePet(); 
                    viewPets();
                    break;
                case 4: 
                    viewPets();
                    deletePet();
                    viewPets();
                    break;
            }
        } while (choice != 5);
    }

    private void displayPetMenu() {
        System.out.println("----------- Pet Menu -----------");
        System.out.println("1. Add Pet                      |");
        System.out.println("2. View Pets                    |");
        System.out.println("3. Update Pet                   |");
        System.out.println("4. Delete Pet                   |");
        System.out.println("5. Back to Main Menu            |");
        System.out.println("---------------------------------");
        System.out.print("Enter your choice:              ");
    }

    private void addPet() {
        String name = getValidStringInput("Enter Pet Name: ");
        String breed = getValidStringInput("Enter Pet Breed: ");

        String sql = "INSERT INTO tbl_breed (p_name, p_breed) VALUES (?, ?)";
        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, breed);
            stmt.executeUpdate();
            System.out.println("Pet added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding pet: " + e.getMessage());
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

    private void viewPets() {
        String sqlQuery = "SELECT * FROM tbl_breed";
        String[] columnHeaders = {"Pet ID", "Name", "Breed"};
        String[] columnNames = {"p_id", "p_name", "p_breed"};
        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    private void updatePet() {
        int petId = getValidPetId();
        String newName = getValidStringInput("Enter new name: ");
        String newBreed = getValidStringInput("Enter new breed: ");

        String sql = "UPDATE tbl_breed SET p_name = ?, p_breed = ? WHERE p_id = ?";
        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newName);
            stmt.setString(2, newBreed);
            stmt.setInt(3, petId);
            stmt.executeUpdate();
            System.out.println("Pet updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating pet: " + e.getMessage());
        }
    }

    private int getValidPetId() {
        while (true) {
            System.out.print("Enter Pet ID: ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input); 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for Pet ID.");
            }
        }
    }

    private void deletePet() {
        int petId = getValidPetId();
        String sql = "DELETE FROM tbl_breed WHERE p_id = ?";
        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, petId);
            stmt.executeUpdate();
            System.out.println("Pet deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting pet: " + e.getMessage());
        }
    }

    // Customer Management
    private void customerMenu() {
        int choice;
        do {
            displayCustomerMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1: 
                    viewCustomers();
                    addCustomer();
                    viewCustomers();
                    break;
                case 2: 
                    viewCustomers(); 
                    break;
                case 3: 
                    viewCustomers();
                    updateCustomer(); 
                    viewCustomers();
                    break;
                case 4:
                    viewCustomers();
                    deleteCustomer(); 
                    viewCustomers();
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
        String customerId = getValidStringInput("Enter Customer ID to update: ");
        String newFirstName = getValidStringInput("Enter new First Name: ");
        String newMiddleName = getValidStringInput("Enter new Middle Name: ");
        String newLastName = getValidStringInput("Enter new Last Name: ");
        String newEmail = getValidStringInput("Enter new Email Address: ");
        String newPhone = getValidPhoneNumber("Enter new Phone Number (11 digits): ");

        String sql = "UPDATE tbl_customer SET c_fname = ?, c_mname = ?, c_lname = ?, c_email = ?, c_phone = ? WHERE c_id = ?";
        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newFirstName);
            stmt.setString(2, newMiddleName);
            stmt.setString(3, newLastName);
            stmt.setString(4, newEmail);
            stmt.setString(5, newPhone);
            stmt.setString(6, customerId);
            stmt.executeUpdate();
            System.out.println("Customer updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }

    private void deleteCustomer() {
        String customerId = getValidStringInput("Enter Customer ID to delete: ");
        String sql = "DELETE FROM tbl_customer WHERE c_id = ?";
        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customerId);
            stmt.executeUpdate();
            System.out.println("Customer deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
        }
    }

    // Appointment Management
    private void appointmentMenu() {
        int choice;
        do {
            displayAppointmentMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1: 
                    viewAppointments();
                    addAppointment();
                    viewAppointments();
                    break;
                case 2:
                    viewAppointments();
                    break;
                case 3: 
                    viewAppointments();
                    updateAppointment(); 
                    viewAppointments();
                    break;
                case 4: 
                    viewAppointments();
                    deleteAppointment();
                    viewAppointments();
                    break;
                case 5: 
                    viewAppointmentReport();
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
        System.out.println("5. View Appointment Report          |");
        System.out.println("6. Back to Main Menu                |");
        System.out.println("---------------------------------------");
        System.out.print("Enter your choice:                  ");
    }

    private void addAppointment() {
        int petId = getValidPetId();
        int customerId = getValidCustomerId();
        scanner.nextLine(); 

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

    private int getValidCustomerId() {
        while (true) {
            System.out.print("Enter Customer ID: ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for Customer ID.");
            }
        }
    }

    private double getValidCost() {
        while (true) {
            System.out.print("Cost: ");
            String input = scanner.nextLine().trim();
            try {
                double cost = Double.parseDouble(input);
                if (cost >= 0) {
                    return cost; 
                } else {
                    System.out.println("Cost cannot be negative. Please enter a valid cost.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for cost.");
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
        int appointmentId = getValidAppointmentId();
        scanner.nextLine(); 

        String newDescription = getValidStringInput("Enter new Appointment Description: ");
        double newCost = getValidCost();
        String newDate = getValidStringInput("Enter new Appointment Date (YYYY-MM-DD): ");

        String sql = "UPDATE tbl_appointments SET a_des = ?, a_cost = ?, a_date = ? WHERE a_id = ?";
        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newDescription);
            stmt.setDouble(2, newCost);
            stmt.setString(3, newDate);
            stmt.setInt(4, appointmentId);
            stmt.executeUpdate();
            System.out.println("Appointment updated successfully.");
        } catch (SQLException e) {
            System.out.println("Error updating appointment: " + e.getMessage());
        }
    }

    private int getValidAppointmentId() {
        while (true) {
            System.out.print("Enter Appointment ID: ");
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input); 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for Appointment ID.");
            }
        }
    }

    private void deleteAppointment() {
        int appointmentId = getValidAppointmentId();
        String sql = "DELETE FROM tbl_appointments WHERE a_id = ?";
        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
            System.out.println("Appointment deleted successfully.");
        } catch (SQLException e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
        }
    }

    private void viewAppointmentReport() {
        String sqlQuery = "SELECT l.a_id AS appointment_id, l.ap_id AS pet_id, " +
                          "c.c_fname || ' ' || c.c_lname AS customer_name, " +
                          "a_date AS date, l.a_des AS description, l.a_cost AS cost " +
                          "FROM tbl_appointments l " +
                          "JOIN tbl_customer c ON c_id = c.c_id"; // Join on customer ID

        try (Connection conn = dbConfig.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sqlQuery);
             ResultSet rs = stmt.executeQuery()) {

            // Print the report header
            System.out.println("----------- Appointment Report -----------");
            System.out.printf("%-15s %-10s %-25s %-15s %-30s %-10s\n", 
                              "Appointment ID", "Pet ID", "Customer Name", "Date", "Description", "Cost");
            System.out.println("------------------------------------------------------------------------------------------");

            // Loop through the result set and print each record
            while (rs.next()) {
                int appointmentId = rs.getInt("appointment_id");
                int petId = rs.getInt("pet_id");
                String customerName = rs.getString("customer_name");
                String date = rs.getString("date");
                String description = rs.getString("description");
                double cost = rs.getDouble("cost");

                // Print each row of the report
                System.out.printf("%-15d %-10d %-25s %-15s %-30s $%.2f\n", 
                                  appointmentId, petId, customerName, date, description, cost);
            }
            System.out.println("------------------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error retrieving appointment report: " + e.getMessage());
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