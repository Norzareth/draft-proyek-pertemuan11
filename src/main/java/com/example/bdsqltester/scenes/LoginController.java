package com.example.bdsqltester.scenes;

import com.example.bdsqltester.HelloApplication;
import com.example.bdsqltester.datasources.DataSourceManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private TextField passwordField;

    @FXML
    private ChoiceBox<String> selectRole;

    @FXML
    private TextField usernameField;

    private int userId;

    public int getUserId() {
        return userId;
    }

    boolean verifyCredentials(String username, String password, String role) throws SQLException {
        try (Connection c = DataSourceManager.getUserConnection()) {
            // Updated query: Join users with role table
            String sql = """
            SELECT u.user_id, u.password, r.jenis_user
            FROM users u
            JOIN role r ON u.id_role = r.id_role
            WHERE (u.username = ? OR u.email = ?) AND r.jenis_user = ?
        """;

            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, username);  // Allow login with username or email
            stmt.setString(3, role);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(password)) {
                    this.userId = rs.getInt("user_id");
                    return true;
                }
            }
        }

        return false;
    }


    @FXML
    void initialize() {
        selectRole.getItems().addAll("Customer", "Admin Cabang", "Admin Pusat");
        selectRole.setValue("Customer");
    }


    @FXML
    void onLoginClick(ActionEvent event) {
        // Get the username and password from the text fields
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = selectRole.getValue();

        // Verify the credentials
        try {
            if (verifyCredentials(username, password, role)) {
                HelloApplication app = HelloApplication.getApplicationInstance();
                app.setUserId(this.userId);// Store the user_id in HelloApplication
                app.getPrimaryStage().setTitle("User View");

                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("user-view.fxml"));
                Scene scene = new Scene(loader.load());
                app.getPrimaryStage().setScene(scene);
                app.getPrimaryStage().sizeToScene();
            } else {
                // Show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid Credentials");
                alert.setContentText("Please check your username and password.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Database Connection Failed");
            alert.setContentText("Could not connect to the database. Please try again later.");
            alert.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
