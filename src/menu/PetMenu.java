package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PetMenu {
    private Scanner scanner = new Scanner(System.in);
    private config dbConfig = new config();
    private PetManager petManager;
    private AppointmentManager appointmentManager;
    private CustomerManager customerManager;
    private ReportManager reportManager;

    public PetMenu() {
        petManager = new PetManager(dbConfig, scanner);
        appointmentManager = new AppointmentManager(dbConfig, scanner);
        customerManager = new CustomerManager(dbConfig, scanner);
        reportManager = new ReportManager(dbConfig, scanner);
    }
 private void displayMainMenu() {
        System.out.println("----------- Main Menu -----------       /|_/| ");
        System.out.println("1. Manage Customers             |      ( ^.^ )");              
        System.out.println("2. Manage Pets                  |       > ^ <");
        System.out.println("3. Manage Appointments          |");
        System.out.println("4. View Appointment Report      |       /|_/| ");
        System.out.println("5. View Individual Report       |      ( o.o )");
        System.out.println("6. Update Transaction Status    |       > ^ <");
        System.out.println("7. Exit                         |");
        System.out.println("---------------------------------");
        System.out.println("   @..@          ,___,              ,~,");
        System.out.println("  (----)         [O.o]           ,-'__ `-, ");
        System.out.println(" ( >__< )        /)__)          {,-'  `. }              ,') ");
        System.out.println("  ^^  ^^       --'--'--        ,( a )   `-.__         ,',')~,");
        System.out.println("                              <=.) (         `-.__,==' ' ' '}");
        System.out.println("                                (   )                      /)");
        System.out.println("                                 `-'|   ,                    )");
        System.out.println("                                     |  \\        `~.        /");
        System.out.println("                                     \\   `._        \\      /");
        System.out.println("                                       \\   `._____,'    ,'");
        System.out.println("                                          `-.________,-' ");
        System.out.print("Enter your choice:              ");
    }
    public void mainMenu() {
        int choice;
        do {
            displayMainMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1:
                    customerManager.manageCustomers();
                    break;
                case 2:
                    petManager.managePets();
                    break;
                case 3:
                    appointmentManager.manageAppointments();
                    break;
                case 4:
                    reportManager.viewAppointmentReport();
                    break;
                case 5:
                    customerManager.viewCustomers();
                    reportManager.viewIndividualReport(); 
                    break;
                case 6:
                    
                    appointmentManager.viewAppointments();
                    updateTransactionStatus();
                    break;
                case 7:
                    System.out.println("Exiting... Thank you for using the system!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 7);
        scanner.close();
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
                if (choice >= 1 && choice <= 7) { 
                    return choice;
                } else {
                    System.out.println("Choice must be between 1 and 7. Please try again."); 
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
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
