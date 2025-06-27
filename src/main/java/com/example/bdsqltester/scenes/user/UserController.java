package com.example.bdsqltester.scenes.user;

import com.example.bdsqltester.HelloApplication;
import com.example.bdsqltester.datasources.DataSourceManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class UserController {

    @FXML private ComboBox<String> cabangFilter;
    @FXML private ComboBox<String> kategoriFilter;
    @FXML private TableView<MenuItem> menuTable;
    @FXML private TableColumn<MenuItem, String> colMenuName;
    @FXML private TableColumn<MenuItem, String> colJenis;
    @FXML private TableColumn<MenuItem, Integer> colHarga;
    @FXML private DatePicker deliveryDatePicker;

    private final ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colMenuName.setCellValueFactory(data -> data.getValue().namaMenuProperty());
        colJenis.setCellValueFactory(data -> data.getValue().jenisProperty());
        colHarga.setCellValueFactory(data -> data.getValue().hargaProperty().asObject());

        menuTable.setItems(menuItems);

        loadFilters();
        loadMenu(null, null);
    }

    private void loadFilters() {
        try (Connection c = DataSourceManager.getUserConnection()) {
            // Cabang
            Statement stmt = c.createStatement();
            ResultSet rsCabang = stmt.executeQuery("SELECT lokasi FROM cabang");
            while (rsCabang.next()) {
                cabangFilter.getItems().add(rsCabang.getString("lokasi"));
            }

            // Kategori (jenis makanan)
            ResultSet rsJenis = stmt.executeQuery("SELECT DISTINCT jenis FROM daftar_menu");
            while (rsJenis.next()) {
                kategoriFilter.getItems().add(rsJenis.getString("jenis"));
            }

        } catch (SQLException e) {
            showError("Database error", e.getMessage());
        }
    }

    private void loadMenu(String cabang, String kategori) {
        menuItems.clear();

        String sql = """
            SELECT m.id_menu, m.nama_menu, m.jenis, m.harga, c.lokasi
            FROM daftar_menu m
            JOIN cabang c on c.cabang_id = m.cabang_id

            WHERE (? IS NULL OR c.lokasi = ?)
              AND (? IS NULL OR m.jenis = ?)
              
              
        """;

        try (Connection c = DataSourceManager.getUserConnection()) {
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, cabang);
            stmt.setString(2, cabang);
            stmt.setString(3, kategori);
            stmt.setString(4, kategori);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                menuItems.add(new MenuItem(
                        rs.getInt("id_menu"),
                        rs.getString("nama_menu"),
                        rs.getString("jenis"),
                        rs.getInt("harga")
                ));
            }

        } catch (SQLException e) {
            showError("Error loading menu", e.getMessage());
        }
    }

    @FXML
    void onFilterClick() {
        String cabang = cabangFilter.getValue();
        String kategori = kategoriFilter.getValue();
        loadMenu(cabang, kategori);
    }

    @FXML
    void onOrderClick() {
        MenuItem selected = menuTable.getSelectionModel().getSelectedItem();
        LocalDate deliveryDate = deliveryDatePicker.getValue();

        if (selected == null || deliveryDate == null) {
            showError("Input Incomplete", "Select a menu item and date.");
            return;
        }


        try (Connection conn = DataSourceManager.getUserConnection()){
            conn.setAutoCommit(false);
            // masukkan tabel pengiriman
            String insertDeliverySQL = """
                    insert into pengiriman (status_pengiriman, jadwal_kirim, estimasi_sampai)
                    values (?, ?, ?)
                    returning pengiriman_id
                    """;

            PreparedStatement stmtDelivery = conn.prepareStatement(insertDeliverySQL);
            stmtDelivery.setString(1, "Belum Dikirim");
            stmtDelivery.setDate(2, Date.valueOf(deliveryDate));
            stmtDelivery.setDate(3, Date.valueOf(deliveryDate.plusDays(2)));

            ResultSet rsDelivery = stmtDelivery.executeQuery();
            rsDelivery.next();
            int pengirimanId = rsDelivery.getInt("pengiriman_id");


            //masuk di tabel pemesanan
            String insertOrderSQL = """
                    insert into pemesanan (tanggal_pesanan, status, user_id, pengiriman_id)
                    values (current_date, ?, ?, ?)
                    returning pesanan_id
                    """;


            PreparedStatement stmtOrder = conn.prepareStatement(insertOrderSQL);


            stmtOrder.setString(1, "Belum Diproses");
            stmtOrder.setInt(2, HelloApplication.getApplicationInstance().getUserId());
            stmtOrder.setInt(3, pengirimanId);
            ResultSet rsOrder = stmtOrder.executeQuery();
            rsOrder.next();
            int pesananId = rsOrder.getInt("pesanan_id");


            // masukkan di tabel m2m pesanan_menu
            String insertLinkSQL = "insert into pesanan_menu  (pesanan_id, id_menu) values (?, ?)";
            PreparedStatement stmtLink = conn.prepareStatement(insertLinkSQL);
            stmtLink.setInt(1, pesananId);
            stmtLink.setInt(2, selected.getIdMenu());
            stmtLink.executeUpdate();

            conn.commit();

            showInfo("Pesanan Berhasil", "Pesanan untuk \"" + selected.getNamaMenu() + "\" telah dibuat");




        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "error bruh");



        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    private int getCurrentUserId() {
        return HelloApplication.getApplicationInstance().getUserId();
    }
}
