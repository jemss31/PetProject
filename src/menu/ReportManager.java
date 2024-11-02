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
    String[] columnNames = {"appointment_id", "pet_id", "customer_name", "date", "description", "cost", "status"};
    
    try (Connection conn = dbConfig.connectDB();
         PreparedStatement stmt = conn.prepareStatement(sqlQuery);
         ResultSet rs = stmt.executeQuery()) {
        
        
        for (String header : columnHeaders) {
            System.out.printf("%-20s", header);
        }
        System.out.println();
        
        
        while (rs.next()) {
            for (String columnName : columnNames) {
                System.out.printf("%-20s", rs.getString(columnName));
            }
            System.out.println();
        }
    } catch (SQLException e) {
        System.out.println("Error viewing appointment report: " + e.getMessage());
    }
   }
   
}