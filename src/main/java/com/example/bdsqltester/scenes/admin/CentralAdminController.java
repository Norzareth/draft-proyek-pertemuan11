package com.example.bdsqltester.scenes.admin;

import com.example.bdsqltester.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;

public class CentralAdminController {

    @FXML
    public void initialize() {
        // Simple initialization
    }
    
    @FXML
    void onLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load());
            HelloApplication.getApplicationInstance().getPrimaryStage().setScene(scene);
            HelloApplication.getApplicationInstance().getPrimaryStage().setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
} 