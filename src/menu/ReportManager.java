package menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ReportManager {
    private config dbConfig;
    private Scanner scanner;

    public ReportManager(config dbConfig, Scanner scanner) {
        this.dbConfig = dbConfig;
        this.scanner = scanner;
    }

    public void viewAppointmentReport() {
        String sqlQuery = "SELECT l.a_id AS appointment_id, l.ap_id AS pet_id, " +
                          "c.c_fname || ' ' || c.c_lname AS customer_name, " +
                          "l.a_date AS date, l.a_des AS description, l.a_cost AS cost, " +
                          "l.l_status AS status " + 
                          "FROM tbl_appointments l " +
                          "JOIN tbl_customer c ON l.c_id = c.c_id";
        
        String[] columnHeaders = {"Appointment ID", "Pet ID", "Customer Name", "Date", "Description", "Cost", "Status"};
        int[] columnWidths = {15, 10, 20, 12, 25, 10, 10}; 

        displayTable(sqlQuery, columnHeaders, columnWidths);
    }

    public void viewIndividualReport() {
        System.out.print("Enter Customer ID: ");
        int customerId = scanner.nextInt();
        
        String sqlQuery = "SELECT l.a_id AS appointment_id, l.ap_id AS pet_id, " +
                          "c.c_fname || ' ' || c.c_lname AS customer_name, " +
                          "l.a_date AS date, l.a_des AS description, l.a_cost AS cost, " +
                          "l.l_status AS status " +
                          "FROM tbl_appointments l " +
                          "INNER JOIN tbl_customer c ON l.c_id = c.c_id " +
                          "WHERE c.c_id = ?";
        
        String[] columnHeaders = {"Appointment ID", "Pet ID", "Customer Name", "Date", "Description", "Cost", "Status"};
        int[] columnWidths = {15, 10, 20, 12, 25, 10, 10}; // Define column widths for alignment

        displayTable(sqlQuery, columnHeaders, columnWidths, customerId);
    }

    private void displayTable(String sqlQuery, String[] headers, int[] widths, Object... params) {
        // Print header separator
        printSeparator(widths);
        
        // Print headers
        for (int i = 0; i < headers.length; i++) {
            System.out.printf("| %-"+ widths[i] +"s ", headers[i]);
        }
        System.out.println("|");
        
        // Print header separator
        printSeparator(widths);

        // Execute query and display rows
        try (Connection conn = dbConfig.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sqlQuery)) {

            // Bind parameters if any
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                boolean hasResults = false;
                while (rs.next()) {
                    hasResults = true;
                    for (int i = 0; i < headers.length; i++) {
                        System.out.printf("| %-"+ widths[i] +"s ", rs.getString(headers[i].toLowerCase().replace(" ", "_")));
                    }
                    System.out.println("|");
                }
                if (!hasResults) {
                    System.out.println("| No results found.                                          |");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error displaying table: " + e.getMessage());
        }

        // Print footer separator
        printSeparator(widths);
    }

    private void printSeparator(int[] widths) {
        for (int width : widths) {
            System.out.print("+");
            for (int i = 0; i < width + 2; i++) { // +2 for padding spaces
                System.out.print("-");
            }
        }
        System.out.println("+");
    }
}
