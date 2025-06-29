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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class OrderHistoryController {

    @FXML private TableView<OrderHistoryItem> orderTable;
    @FXML private TableColumn<OrderHistoryItem, String> colTanggal;
    @FXML private TableColumn<OrderHistoryItem, String> colMenu;
    @FXML private TableColumn <OrderHistoryItem, String> colStatus;
    @FXML private TableColumn <OrderHistoryItem, String> colJadwal;
    @FXML private TableColumn <OrderHistoryItem, Void> colDetail;


    @FXML
    public void initialize (){
        colTanggal.setCellValueFactory(data -> {
            LocalDate tanggal = data.getValue().getTanggalPesanan();
            return new SimpleStringProperty(tanggal != null ? tanggal.toString() : "N/A");
        });
        colMenu.setCellValueFactory(data -> new SimpleStringProperty("Pesanan #" + data.getValue().getPesananId()));
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        colJadwal.setCellValueFactory(data -> {
            LocalDate jadwal = data.getValue().getJadwalKirim();
            return new SimpleStringProperty(jadwal != null ? jadwal.toString() : "N/A");
        });

        setupDetailColumn();
        loadOrderHistory();
    }

    private void setupDetailColumn() {
        colDetail.setCellFactory(new Callback<TableColumn<OrderHistoryItem, Void>, TableCell<OrderHistoryItem, Void>>() {
            @Override
            public TableCell<OrderHistoryItem, Void> call(TableColumn<OrderHistoryItem, Void> param) {
                return new TableCell<OrderHistoryItem, Void>() {
                    private final Button detailButton = new Button("Detail");
                    private final HBox buttonBox = new HBox(5, detailButton);
                    
                    {
                        detailButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
                        detailButton.setOnAction(event -> {
                            OrderHistoryItem order = getTableView().getItems().get(getIndex());
                            showOrderDetail(order);
                        });
                    }
                    
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : buttonBox);
                    }
                };
            }
        });
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
                WHERE p.user_id = ?
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
            showError("ERROR", "Error loading order history: " + e.getMessage());
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

    @FXML
    void onLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Konfirmasi Logout");
        alert.setContentText("Apakah Anda yakin ingin keluar dari aplikasi?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Reset user session
                HelloApplication.getApplicationInstance().setUserId(0);
                
                // Navigate back to login screen
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
                Scene scene = new Scene(loader.load());
                HelloApplication.getApplicationInstance().getPrimaryStage().setScene(scene);
                
                showInfo("Logout Berhasil", "Anda telah berhasil keluar dari aplikasi.");
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error", "Gagal melakukan logout: " + e.getMessage());
            }
        }
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private void showOrderDetail(OrderHistoryItem order) {
        // Load menu items for this order
        String sql = """
            SELECT dm.nama_menu, dm.jenis, dm.harga, COUNT(*) as quantity
            FROM pesanan_menu pm
            JOIN daftar_menu dm ON pm.id_menu = dm.id_menu
            WHERE pm.pesanan_id = ?
            GROUP BY dm.id_menu, dm.nama_menu, dm.jenis, dm.harga
            ORDER BY dm.nama_menu
            """;
        
        StringBuilder detailText = new StringBuilder();
        detailText.append("Detail Pesanan #").append(order.getPesananId()).append("\n\n");
        detailText.append("Customer: ").append(order.getCustomerName()).append("\n");
        detailText.append("Email: ").append(order.getCustomerEmail()).append("\n");
        detailText.append("Tanggal Pesanan: ").append(order.getTanggalPesanan()).append("\n");
        detailText.append("Status: ").append(order.getStatus()).append("\n");
        detailText.append("Status Pengiriman: ").append(order.getStatusPengiriman()).append("\n");
        
        if (order.getJadwalKirim() != null) {
            detailText.append("Jadwal Kirim: ").append(order.getJadwalKirim()).append("\n");
        }
        if (order.getEstimasiSampai() != null) {
            detailText.append("Estimasi Sampai: ").append(order.getEstimasiSampai()).append("\n");
        }
        
        detailText.append("\nMenu yang Dipesan:\n");
        detailText.append("─────────────────\n");
        
        try (Connection conn = DataSourceManager.getUserConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, order.getPesananId());
            ResultSet rs = stmt.executeQuery();
            
            int totalHarga = 0;
            while (rs.next()) {
                String namaMenu = rs.getString("nama_menu");
                String jenis = rs.getString("jenis");
                int harga = rs.getInt("harga");
                int quantity = rs.getInt("quantity");
                int subtotal = harga * quantity;
                totalHarga += subtotal;
                
                detailText.append(String.format("• %s (%s)\n", namaMenu, jenis));
                detailText.append(String.format("  %d x Rp %,d = Rp %,d\n", quantity, harga, subtotal));
            }
            
            detailText.append("\n─────────────────\n");
            detailText.append(String.format("Total: Rp %,d", totalHarga));
            
        } catch (SQLException e) {
            e.printStackTrace();
            detailText.append("\nError loading menu details: ").append(e.getMessage());
        }
        
        // Show dialog with scrollable TextArea for details
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detail Pesanan");
        alert.setHeaderText("Detail Pesanan #" + order.getPesananId());
        alert.setContentText("Lihat detail lengkap di bawah ini.");

        TextArea textArea = new TextArea(detailText.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefWidth(480);
        textArea.setPrefHeight(350);

        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);
        alert.getDialogPane().setPrefWidth(520);
        alert.getDialogPane().setPrefHeight(420);

        alert.showAndWait();
    }
}
