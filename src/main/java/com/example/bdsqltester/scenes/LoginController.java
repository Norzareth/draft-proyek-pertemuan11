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

    boolean verifyCredentials(String username, String password, String selectedRole) throws SQLException {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
            SELECT u.user_id, u.password, r.jenis_user
            FROM users u
            JOIN role r ON u.id_role = r.id_role
            WHERE (u.username = ? OR u.email = ?)
        """;

            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                String dbRole = rs.getString("jenis_user");

                if (dbPassword.equals(password) && dbRole.equalsIgnoreCase(selectedRole)) {
                    this.userId = rs.getInt("user_id");
                    System.out.println("Login success as " + dbRole);
                    return true;
                } else {
                    System.out.println("Role mismatch or wrong password.");
                }
            }
        }
        return false;
    }



    @FXML
    void initialize() {
        selectRole.getItems().addAll("Customer", "Admin Cabang", "Admin Pusat");
        selectRole.setValue("Customer"); // default value
    }

    @FXML
    void onLoginClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = selectRole.getValue(); // get selected role from UI

        try {
            if (verifyCredentials(username, password, role)) {
                HelloApplication app = HelloApplication.getApplicationInstance();
                app.setUserId(this.userId);

                FXMLLoader loader;

                if (role.equalsIgnoreCase("Customer")) {
                    app.getPrimaryStage().setTitle("Customer View");
                    loader = new FXMLLoader(HelloApplication.class.getResource("user-view.fxml"));
                } else if (role.equalsIgnoreCase("Admin Cabang")) {
                    app.getPrimaryStage().setTitle("Branch Admin View");
                    loader = new FXMLLoader(HelloApplication.class.getResource("branch-admin-view.fxml"));
                } else if (role.equalsIgnoreCase("Admin Pusat")) {
                    app.getPrimaryStage().setTitle("Central Admin View");
                    loader = new FXMLLoader(HelloApplication.class.getResource("central-admin-view.fxml"));
                } else {
                    throw new RuntimeException("Unknown role: " + role);
                }

                Scene scene = new Scene(loader.load());
                app.getPrimaryStage().setScene(scene);
                app.getPrimaryStage().sizeToScene();
            } else {
                showError("Login Failed", "Invalid credentials or mismatched role.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Could not connect to the database.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
