package menu;

import customermanager.CustomerManager;
import java.util.Scanner;

public class Menu{
    private Scanner scanner = new Scanner(System.in);
    private config dbConfig = new config(); // Make sure Config class is defined
    private PetManager petManager;
    private AppointmentManager appointmentManager;
    private CustomerManager customerManager;
    private ReportManager reportManager;

    public Menu() {
        petManager = new PetManager(dbConfig, scanner);
        appointmentManager = new AppointmentManager(dbConfig, scanner);
        customerManager = new CustomerManager(dbConfig, scanner);
        reportManager = new ReportManager(dbConfig, scanner);
    }

    public void mainMenu() {
        int choice;
        do {
            displayMainMenu();
            choice = getValidChoice();

            switch (choice) {
                case 1:
                    petManager.managePets();
                    break;
                case 2:
                    appointmentManager.manageAppointments();
                    break;
                case 3:
                    customerManager.manageCustomers();
                    break;
                case 4:
                    reportManager.viewAppointmentReport();
                    break;
                case 5:
                    System.out.println("Exiting... Thank you for using the system!");
                    break;
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
}