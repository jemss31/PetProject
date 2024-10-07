
package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PetMenu {
    private Scanner scanner = new Scanner(System.in);
    private config dbConfig = new config(); // Assuming Config is your database configuration class

    public void petMenu() {
        int choice;
        do {
            System.out.println("----------- Pet Menu -----------");
            System.out.println("1. Add Pet                      |");
            System.out.println("2. View Pets                    |");
            System.out.println("3. Update Pet                   |");
            System.out.println("4. Delete Pet                   |");
            System.out.println("5. Back to Main Menu            |");
            System.out.println("---------------------------------");
            System.out.print("Enter your choice:              |\n");

            choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1: addPet(); break;
                case 2: viewPets(); break;
                case 3: updatePet(); break;
                case 4: deletePet(); break;
            }
        } while (choice != 5);
    }

    private void addPet() {
        System.out.print("Enter Pet ID: ");
        int petId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Pet Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Pet Breed: ");
        String breed = scanner.nextLine();
        System.out.print("Enter Owner Name: ");
        String ownerName = scanner.nextLine();
        System.out.print("Enter Owner Phone Number: ");
        String ownerPhone = scanner.nextLine();

        String sql = "INSERT INTO tbl_breed (p_id, p_name, p_breed, p_Oname, p_phone) VALUES (?, ?, ?, ?, ?)";
        dbConfig.addRecord(sql, petId, name, breed, ownerName, ownerPhone);
        System.out.println("Pet added successfully.");
    }

    private void viewPets() {
        String sqlQuery = "SELECT * FROM tbl_breed";
        String[] columnHeaders = {"Pet ID", "Name", "Breed", "Owner Name", "Owner Phone"};
        String[] columnNames = {"p_id", "p_name", "p_breed", "p_Oname", "p_phone"};
        dbConfig.viewRecords(sqlQuery, columnHeaders, columnNames);
    }

    private void updatePet() {
        System.out.print("Enter Pet ID to edit: ");
        int petId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.print("Enter new name: ");
        String newName = scanner.nextLine();
        System.out.print("Enter new breed: ");
        String newBreed = scanner.nextLine();
        System.out.print("Enter new owner name: ");
        String newOwnerName = scanner.nextLine();
        System.out.print("Enter new owner phone number: ");
        String newOwnerPhone = scanner.nextLine();

        String sql = "UPDATE tbl_breed SET p_name = ?, p_breed = ?, p_name = ?, p_Oname = ? WHERE p_id = ?";
        dbConfig.addRecord(sql, newName, newBreed, newOwnerName, newOwnerPhone, petId);
        System.out.println("Pet updated successfully.");
    }



    private void deletePet() {
        System.out.print("Enter Pet ID to delete: ");
        int petId = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM tbl_breed WHERE p_id = ?";
        dbConfig.addRecord(sql, petId);
        System.out.println("Pet deleted successfully.");
    }

    public void mainMenu() {
        int choice;
        do {
            System.out.println("----------- Main Menu -----------");
            System.out.println("1. Manage Pets                  |");
            System.out.println("2. Exit                         |");
            System.out.println("---------------------------------");
            System.out.print("Enter your choice:              |\n");
            choice = scanner.nextInt();

            switch (choice) {
                case 1: petMenu(); break;
            }
        } while (choice != 2);

        System.out.println("Exiting... Thank you for using the system!");
        scanner.close();
    }
}