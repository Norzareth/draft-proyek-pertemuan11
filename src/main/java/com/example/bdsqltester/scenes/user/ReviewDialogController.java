package com.example.bdsqltester.scenes.user;

import com.example.bdsqltester.HelloApplication;
import com.example.bdsqltester.datasources.DataSourceManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReviewDialogController {

    @FXML private TextArea commentArea;
    @FXML private Spinner<Integer> ratingSpinner;


    private int pesananId;


    public void initialize(){
        ratingSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 5));
    }


    public void setPesananId (int id){
        this.pesananId = id;
    }

    @FXML
    void onSubmit() {

        String comment = commentArea.getText().trim();
        int rating = ratingSpinner.getValue();
        if (comment.isEmpty()){
            showError("Belum ada komentar");

        }


        try(Connection conn = DataSourceManager.getUserConnection(); ){
            String sql = """
                    insert into rating (comment, nilai_rating, user_id, pesanan_id)
                    values (?, ?, ?, ?)
                    
                    
                    
                    """;
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, comment);
            stmt.setInt(2, rating);
            stmt.setInt(3, HelloApplication.getApplicationInstance().getUserId());
            stmt.setInt(4, pesananId);

            stmt.executeUpdate();

            showInfo("Terimakasih sudah memberi ulasan ;D");
            closeWindow();





        } catch (SQLException e) {

            e.printStackTrace();
            showError("ulasan error");
        }


    }

    @FXML
    void onCancel(){
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) commentArea.getScene().getWindow();
        stage.close();
    }

    private void showError(String message){
        new Alert(Alert.AlertType.ERROR, message, ButtonType.OK).showAndWait();
    }

    private void showInfo(String message){
        new Alert (Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();


    }







}





