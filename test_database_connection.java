import java.sql.*;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/bd_project";
        String username = "squire";
        String password = "schoolstuff";
        
        System.out.println("Testing database connection...");
        System.out.println("URL: " + url);
        System.out.println("Username: " + username);
        System.out.println("========================================");
        
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("✅ Database connection successful!");
            
            // Test admin data
            testAdminData(conn);
            
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed!");
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void testAdminData(Connection conn) throws SQLException {
        System.out.println("\nTesting admin data...");
        
        // Test roles
        String roleQuery = "SELECT * FROM role ORDER BY id_role";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(roleQuery)) {
            
            System.out.println("\n📋 ROLES:");
            while (rs.next()) {
                System.out.printf("ID: %d, Role: %s, Access: %s%n", 
                    rs.getInt("id_role"), 
                    rs.getString("jenis_user"), 
                    rs.getString("jenis_akses"));
            }
        }
        
        // Test admin users
        String adminQuery = """
            SELECT u.user_id, u.username, u.email, u.password, r.jenis_user
            FROM users u
            JOIN role r ON u.id_role = r.id_role
            WHERE r.jenis_user IN ('AdminCab', 'AdminPus')
            ORDER BY r.jenis_user, u.username
            """;
            
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(adminQuery)) {
            
            System.out.println("\n👥 ADMIN USERS:");
            while (rs.next()) {
                System.out.printf("ID: %d, Username: %s, Email: %s, Password: %s, Role: %s%n", 
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("jenis_user"));
            }
        }
        
        // Test specific admin login
        System.out.println("\n🔐 TESTING SPECIFIC LOGINS:");
        
        // Test admin cabang
        testSpecificLogin(conn, "admin_jak", "AdminCab");
        testSpecificLogin(conn, "admin_ban", "AdminCab");
        testSpecificLogin(conn, "admin_sur", "AdminCab");
        
        // Test admin pusat
        testSpecificLogin(conn, "super_adm", "AdminPus");
    }
    
    private static void testSpecificLogin(Connection conn, String username, String expectedRole) throws SQLException {
        String query = """
            SELECT u.user_id, u.username, u.password, r.jenis_user
            FROM users u
            JOIN role r ON u.id_role = r.id_role
            WHERE u.username = ? AND r.jenis_user = ?
            """;
            
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, expectedRole);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.printf("✅ %s (%s): Found - ID: %d, Password: %s%n", 
                        username, expectedRole, 
                        rs.getInt("user_id"), 
                        rs.getString("password"));
                } else {
                    System.out.printf("❌ %s (%s): Not found%n", username, expectedRole);
                }
            }
        }
    }
} 