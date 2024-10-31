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
                          "a_date AS date, l.a_des AS description, l.a_cost AS cost " +
                          "FROM tbl_appointments l " +
                          "JOIN tbl_customer c ON l.c_id = c.c_id";

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
}