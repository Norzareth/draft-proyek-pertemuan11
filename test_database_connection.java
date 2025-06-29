import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class test_database_connection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/bd_project";
        String username = "squire";
        String password = "schoolstuff";
        
        try {
            System.out.println("Testing database connection...");
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Database connection successful!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 