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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;

public class OrderHistoryController {

    @FXML private TableView<OrderHistoryItem> orderTable;
    @FXML private TableColumn<OrderHistoryItem, String> colTanggal;
    @FXML private TableColumn<OrderHistoryItem, String> colMenu;
    @FXML private TableColumn <OrderHistoryItem, String> colStatus;
    @FXML private TableColumn <OrderHistoryItem, String> colJadwal;


    @FXML
    public void initialize (){
        colTanggal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggalPesanan().toString()));
        colMenu.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomerName()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        colJadwal.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJadwalKirim() != null ? data.getValue().getJadwalKirim().toString() : "N/A"));

        loadOrderHistory();

    }

    private void openReviewDialog(int pesananId) throws IOException {
       try {
           FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("review-screen.fxml"));
           Scene scene = new Scene(loader.load());

           Stage dialog = new Stage();
           dialog.setTitle("Review/rating");
           dialog.setScene(scene);
           dialog.setResizable(false);
           dialog.initOwner(HelloApplication.getApplicationInstance().getPrimaryStage());
           dialog.show();

           ReviewDialogController controller = loader.getController();
           controller.setPesananId(pesananId);

       }catch(IOException e){
           e.printStackTrace();
           showError("error", "error membuka ulasan");
       }
    }

    private void loadOrderHistory(){
        ObservableList<OrderHistoryItem> items = FXCollections.observableArrayList();
        String sql = """
                SELECT p.pesanan_id, u.username as customer_name, u.email as customer_email,
                       p.tanggal_pesanan, p.status, p.user_id,
                       COALESCE(pg.status_pengiriman, 'Belum Dijadwalkan') as status_pengiriman,
                       pg.jadwal_kirim, pg.estimasi_sampai, p.pengiriman_id,
                       COALESCE(SUM(dm.harga), 0) as total_harga
                FROM pemesanan p
                JOIN users u ON p.user_id = u.user_id
                LEFT JOIN pengiriman pg ON p.pengiriman_id = pg.pengiriman_id
                LEFT JOIN pesanan_menu pm ON p.pesanan_id = pm.pesanan_id
                LEFT JOIN daftar_menu dm ON pm.id_menu = dm.id_menu
                GROUP BY p.pesanan_id, u.username, u.email, p.tanggal_pesanan, p.status, p.user_id,
                         pg.status_pengiriman, pg.jadwal_kirim, pg.estimasi_sampai, p.pengiriman_id
                ORDER BY p.tanggal_pesanan DESC
                """;

        try (Connection conn = DataSourceManager.getUserConnection()){
            PreparedStatement stmt = conn.prepareStatement(sql);

            int userId = HelloApplication.getApplicationInstance().getUserId();
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                OrderHistoryItem order = new OrderHistoryItem(
                    rs.getInt("pesanan_id"),
                    rs.getString("customer_name"),
                    rs.getString("customer_email"),
                    rs.getDate("tanggal_pesanan") != null ? rs.getDate("tanggal_pesanan").toLocalDate() : null,
                    rs.getString("status"),
                    rs.getInt("total_harga"),
                    rs.getInt("user_id"),
                    rs.getString("status_pengiriman"),
                    rs.getDate("jadwal_kirim") != null ? rs.getDate("jadwal_kirim").toLocalDate() : null,
                    rs.getDate("estimasi_sampai") != null ? rs.getDate("estimasi_sampai").toLocalDate() : null,
                    rs.getInt("pengiriman_id")
                );
                items.add(order);
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

    @FXML
    void onReviewClick () throws IOException {
        OrderHistoryItem selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null){
            showError("Tidak ada pesanan", "Pilihlah pesanan terlebih dahulu ;)");
            return;
        }

        int pesananId = selected.getPesananId();

        if (isAlreadyReviewed(pesananId)){
            showError("Sudah diulas", "pesanan sudah diulas");
        }
        else {
            openReviewDialog(pesananId);
        }
    }

    private boolean isAlreadyReviewed (int pesananId){
        String sql = "select 1 from rating where pesanan_id = ?";

        try (Connection conn = DataSourceManager.getUserConnection(); PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, pesananId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
}
