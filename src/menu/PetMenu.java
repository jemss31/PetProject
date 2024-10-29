package menu;

import java.util.Scanner;

public class PetMenu {
    private Scanner scanner = new Scanner(System.in);
    private config dbConfig = new config();

    public void petMenu() {
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
        System.out.print("Enter your choice:              |\n");
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

    private void addPet() {
        String name = getValidStringInput("Enter Pet Name: ");
        String breed = getValidStringInput("Enter Pet Breed: ");
        String ownerName = getValidStringInput("Enter Owner Name: ");
        String ownerPhone = getValidPhoneNumber("Enter Owner Phone Number (11 digits): ");

        String sql = "INSERT INTO tbl_breed (p_name, p_breed, p_Oname, p_phone) VALUES (?, ?, ?, ?)";
        try {
            dbConfig.addRecord(sql, name, breed, ownerName, ownerPhone);
            System.out.println("Pet added successfully.");
        } catch (Exception e) {
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

    private void viewPets() {
        String sqlQuery = "SELECT * FROM tbl_breed";
        String[] columnHeaders = {"Pet ID", "Name", "Breed", "Owner Name", "Owner Phone"};
        String[] columnNames = {"p_id", "p_name", "p_breed", "p_Oname", "p_phone"};
        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    private void updatePet() {
        int petId = getValidPetId();
        String newName = getValidStringInput("Enter new name: ");
        String newBreed = getValidStringInput("Enter new breed: ");
        String newOwnerName = getValidStringInput("Enter new owner name: ");
        String newOwnerPhone = getValidPhoneNumber("Enter new owner phone number (11 digits): ");

        String sql = "UPDATE tbl_breed SET p_name = ?, p_breed = ?, p_Oname = ?, p_phone = ? WHERE p_id = ?";
        try {
            dbConfig.addRecord(sql, newName, newBreed, newOwnerName, newOwnerPhone, petId);
            System.out.println("Pet updated successfully.");
        } catch (Exception e) {
            System.out.println("Error updating pet: " + e.getMessage());
        }
    }

    private int getValidPetId() {
        while (true) {
            System.out.print("Enter Pet ID: ");
            String input = scanner.nextLine().trim();
            try {
                int petId = Integer.parseInt(input);
                return petId; 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for Pet ID.");
            }
        }
    }

    private void deletePet() {
        int petId = getValidPetId();
        String sql = "DELETE FROM tbl_breed WHERE p_id = ?";
        try {
            dbConfig.addRecord(sql, petId);
            System.out.println("Pet deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting pet: " + e.getMessage());
        }
    }

    private void appointmentMenu() {
        int choice;
        do {
            displayAppointmentMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1:
                    viewPets();
                    viewAppointments();
                    addAppointment();
                    break;
                case 2:
                    viewPets();
                    viewAppointments();
                    break;
                case 3:
                    viewPets();
                    viewAppointments();
                    updateAppointment();
                    break;
                case 4:
                    viewAppointments();
                    deleteAppointment();
                    viewAppointments();
                    break;
            }
        } while (choice != 5);
    }

    private void displayAppointmentMenu() {
        System.out.println("----------- Appointment Menu -----------");
        System.out.println("1. Add Appointment                  |");
        System.out.println("2. View Appointments                |");
        System.out.println("3. Update Appointment               |");
        System.out.println("4. Delete Appointment               |");
        System.out.println("5. Back to Main Menu                |");
        System.out.println("---------------------------------------");
        System.out.print("Enter your choice:                  |\n");
    }

    private void addAppointment() {
        int petId = getValidPetId();
        scanner.nextLine();

        String description = getValidStringInput("Enter Appointment Description: ");
        double cost = getValidCost();

        String sql = "INSERT INTO tbl_appointments (ap_id, a_des, a_cost) VALUES (?, ?, ?)";
        try {
            dbConfig.addRecord(sql, petId, description, cost);
            System.out.println("Appointment added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding appointment: " + e.getMessage());
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
        String[] columnHeaders = {"Appointment ID", "Pet ID", "Date", "Description", "Cost"};
        String[] columnNames = {"a_id", "ap_id", "a_date", "a_des", "a_cost"};
        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    private void updateAppointment() {
        int appointmentId = getValidAppointmentId();
        int newPetId = getValidPetId();
        scanner.nextLine(); 

        String newDate = getValidDate("Enter new Appointment Date (YYYY-MM-DD): ");
        String newTime = getValidTime("Enter new Appointment Time (HH:MM): ");
        String newDescription = getValidStringInput("Enter new Appointment Description: ");
        double newCost = getValidCost();

        String sql = "UPDATE tbl_appointments SET ap_id = ?, a_date = ?, a_time = ?, a_des = ?, a_cost = ? WHERE a_id = ?";
        try {
            dbConfig.addRecord(sql, newPetId, newDate, newTime, newDescription, newCost, appointmentId);
            System.out.println("Appointment updated successfully.");
        } catch (Exception e) {
            System.out.println("Error updating appointment: " + e.getMessage());
        }
    }

    private int getValidAppointmentId() {
        while (true) {
            System.out.print("Enter Appointment ID: ");
            String input = scanner.nextLine().trim();
            try {
                int appointmentId = Integer.parseInt(input);
                return appointmentId; 
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer for Appointment ID.");
            }
        }
    }

    private String getValidDate(String prompt) {
        while (true) {
            System.out.print(prompt);
            String date = scanner.nextLine().trim();
            if (date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return date; 
            } else {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
    }

    private String getValidTime(String prompt) {
        while (true) {
            System.out.print(prompt);
            String time = scanner.nextLine().trim();
            if (time.matches("\\d{2}:\\d{2}")) {
                return time; 
            } else {
                System.out.println("Invalid time format. Please enter the time in HH:MM format.");
            }
        }
    }

    private void deleteAppointment() {
        int appointmentId = getValidAppointmentId();
        String sql = "DELETE FROM tbl_appointments WHERE a_id = ?";
        try {
            dbConfig.addRecord(sql, appointmentId);
            System.out.println("Appointment deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting appointment: " + e.getMessage());
        }
    }

    public void mainMenu() {
        int choice;
        do {
            displayMainMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1: petMenu(); break;
                case 2: appointmentMenu(); break; 
                case 3: viewAppointmentReport(); break;
                case 4: 
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 4);

        System.out.println("Exiting... Thank you for using the system!");
        scanner.close();
    }
        
    private void displayMainMenu() {
        System.out.println("----------- Main Menu -----------");
        System.out.println("1. Manage Pets                  |");
        System.out.println("2. Manage Appointments          |");
        System.out.println("3. View Appointment Report      |");
        System.out.println("4. Exit                         |");
        System.out.println("---------------------------------");
        System.out.print("Enter your choice:              |\n");
    }

    private void viewAppointmentReport() {
        String sqlQuery = "SELECT a.a_id AS Appointment_ID, a.ap_id AS Pet_ID, b.p_name AS Pet_Name, "
                        + "b.p_breed AS Pet_Breed, a.a_date AS Appointment_Date, "
                        + "a.a_des AS Appointment_Description, a.a_cost AS Appointment_Cost "
                        + "FROM tbl_appointments a "
                        + "JOIN tbl_breed b ON a.ap_id = b.p_id "
                        + "ORDER BY a.a_date";
        
        String[] columnHeaders = {"Appointment ID", "Pet ID", "Pet Name", "Pet Breed", "Appointment Date", "Description", "Cost"};
        String[] columnNames = {"a_id", "p_id", "p_name", "p_breed", "a_date", "a_des", "a_cost"};

        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }
}