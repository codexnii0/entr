import java.sql.*;

public class entr {
    public static void main(String[] args) {
        // Database credentials
        String url = "jdbc:mysql://127.0.0.1:3306/entr_db"; // Change 'event_tracker' if needed
        String user = "root";  // Change if you set a different MySQL user
        String password = "codexnii";  // Set your MySQL password

        try {

            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to MySQL successfully!");
            
            conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }
}
