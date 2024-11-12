package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PetManager {
    private config dbConfig;
    private Scanner scanner;

    public PetManager(config dbConfig, Scanner scanner) {
        this.dbConfig = dbConfig;
        this.scanner = scanner;
    }

    public void managePets() {
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

    public void viewPets() {
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
}