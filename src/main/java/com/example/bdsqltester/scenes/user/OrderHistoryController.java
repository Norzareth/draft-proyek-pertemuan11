package com.example.bdsqltester.scenes.user;

import com.example.bdsqltester.HelloApplication;
import com.example.bdsqltester.datasources.DataSourceManager;
import com.example.bdsqltester.dtos.OrderHistoryItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderHistoryController {

    @FXML private TableView<OrderHistoryItem> orderTable;
    @FXML private TableColumn<OrderHistoryItem, String> colTanggal;
    @FXML private TableColumn<OrderHistoryItem, String> colMenu;
    @FXML private TableColumn <OrderHistoryItem, String> colStatus;
    @FXML private TableColumn <OrderHistoryItem, String> colJadwal;


    @FXML
    public void initialize (){
        colTanggal.setCellValueFactory(data -> data.getValue().tanggalProperty());
        colMenu.setCellValueFactory(data -> data.getValue().menuProperty());
        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colJadwal.setCellValueFactory(data -> data.getValue().jadwalProperty());

        loadOrderHistory();

    }

    private void loadOrderHistory(){
        ObservableList<OrderHistoryItem> items = FXCollections.observableArrayList();
        String sql = """
                select p.tanggal_pesanan, m.nama_menu, p.status, g.jadwal_kirim
                from pemesanan p
                join pengiriman g on p.pengiriman_id = g.pengiriman_id
                join pesanan_menu pm on p.pesanan_id = pm.pesanan_id
                join daftar_menu m on pm.id_menu = m.id_menu
                where p.user_id = ?
                order by p.tanggal_pesanan desc
                """;


        try (Connection conn = DataSourceManager.getUserConnection()){

            PreparedStatement stmt = conn.prepareStatement(sql);

            int userId = HelloApplication.getApplicationInstance().getUserId();
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                String tanggal = rs.getDate("tanggal_pesanan").toString();
                String namaMenu = rs.getString("nama_menu");
                String status = rs.getString("status");
                String jadwal = rs.getDate("jadwal_kirim").toString();

                items.add(new OrderHistoryItem(tanggal, namaMenu, status, jadwal));
            }

            orderTable.setItems(items);

        }catch (SQLException e){
            e.printStackTrace();
            showError("ERROR", "error history");


        }




    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    @FXML
    void onBackClick (){
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("user-view.fxml"));
            Scene scene = new Scene(loader.load());
            HelloApplication.getApplicationInstance().getPrimaryStage().setScene(scene);

        }catch (IOException e) {
            e.printStackTrace();

        }


    }





}
