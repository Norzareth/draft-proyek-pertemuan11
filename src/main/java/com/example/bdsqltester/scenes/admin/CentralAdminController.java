package com.example.bdsqltester.scenes.admin;

import com.example.bdsqltester.HelloApplication;
import com.example.bdsqltester.datasources.DataSourceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CentralAdminController {

    @FXML private TableView<MenuItem> performanceTable;
    @FXML private TableColumn<MenuItem, String> colNamaMenu;
    @FXML private TableColumn<MenuItem, String> colJenis;
    @FXML private TableColumn<MenuItem, String> colLokasi;
    @FXML private TableColumn<MenuItem, Integer> colHarga;

    @FXML private TextField diskonField;
    @FXML private TextField syaratField;

    // Performance table fields
    @FXML private TableView<PerformaCabang> performaTable;
    @FXML private TableColumn<PerformaCabang, String> colCabang;
    @FXML private TableColumn<PerformaCabang, Integer> colPerforma;
    @FXML private TableColumn<PerformaCabang, Integer> colIncome;

    @FXML
    public void initialize() {
        // Setup menu table
        colNamaMenu.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));

        // Setup performance table
        colCabang.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        colPerforma.setCellValueFactory(new PropertyValueFactory<>("performa"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));

        loadMenuItems();
        loadPerformaCabang();
    }

    private void loadMenuItems() {
        ObservableList<MenuItem> list = FXCollections.observableArrayList();

        String sql = """
            SELECT m.id_menu, m.nama_menu, m.jenis, m.harga, c.lokasi
            FROM daftar_menu m
            JOIN cabang c ON m.cabang_id = c.cabang_id
            ORDER BY m.nama_menu
        """;

        try (Connection conn = DataSourceManager.getUserConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_menu");
                String nama = rs.getString("nama_menu");
                String jenis = rs.getString("jenis");
                int harga = rs.getInt("harga");
                String lokasi = rs.getString("lokasi");
                list.add(new MenuItem(id, nama, jenis, harga, lokasi));
            }

            performanceTable.setItems(list);

        } catch (SQLException e) {
            showError("Gagal memuat daftar menu", e.getMessage());
        }
    }

    private void loadPerformaCabang() {
        ObservableList<PerformaCabang> list = FXCollections.observableArrayList();

        String sql = """
            SELECT c.lokasi, p.tingkat_performa, p.income
            FROM cabang c
            JOIN performa_cabang p ON c.id_performa_cabang = p.id_performa_cabang
        """;

        try (Connection conn = DataSourceManager.getUserConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String lokasi = rs.getString("lokasi");
                int performa = rs.getInt("tingkat_performa");
                int income = rs.getInt("income");
                list.add(new PerformaCabang(lokasi, performa, income));
            }

            performaTable.setItems(list);

        } catch (SQLException e) {
            showError("Gagal memuat data performa cabang", e.getMessage());
        }
    }

    @FXML
    void onSaveDiskon() {
        MenuItem selected = performanceTable.getSelectionModel().getSelectedItem();
        String hargaDiskonText = diskonField.getText();
        String deskripsi = syaratField.getText();

        if (selected == null) {
            showError("Pilih Menu", "Silakan pilih menu terlebih dahulu.");
            return;
        }

        if (hargaDiskonText.isEmpty()) {
            showError("Harga Diskon", "Masukkan harga diskon.");
            return;
        }

        if (deskripsi.isEmpty()) {
            showError("Deskripsi Diskon", "Masukkan deskripsi diskon.");
            return;
        }

        try {
            int hargaDiskon = Integer.parseInt(hargaDiskonText);
            int hargaAsli = selected.getHarga();
            
            if (hargaDiskon >= hargaAsli) {
                showError("Harga Tidak Valid", "Harga diskon harus lebih rendah dari harga asli.");
                return;
            }

            try (Connection conn = DataSourceManager.getUserConnection()) {
                String sql = "INSERT INTO diskon (harga_baru, deskripsi_diskon, id_menu) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setInt(1, hargaDiskon);
                stmt.setString(2, deskripsi);
                stmt.setInt(3, selected.getId());

                stmt.executeUpdate();
                showInfo("Diskon berhasil disimpan untuk menu: " + selected.getNama());
                
                // Clear form after successful save
                diskonField.clear();
                syaratField.clear();

            } catch (SQLException e) {
                showError("Gagal menyimpan diskon.", e.getMessage());
            }

        } catch (NumberFormatException e) {
            showError("Harga Tidak Valid", "Masukkan angka yang valid untuk harga diskon.");
        }
    }

    @FXML
    void onRefreshPerforma() {
        loadPerformaCabang();
        showInfo("Data berhasil diperbarui");
    }

    @FXML
    void onLogout() {
        try {
            // Load login view
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load());
            
            // Get current stage and replace scene
            Stage currentStage = (Stage) performanceTable.getScene().getWindow();
            currentStage.setScene(scene);
            currentStage.setTitle("Login");
            
        } catch (IOException e) {
            e.printStackTrace();
            showError("Gagal logout", e.getMessage());
        }
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukses");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class MenuItem {
        private final int id;
        private final String nama;
        private final String jenis;
        private final int harga;
        private final String lokasi;

        public MenuItem(int id, String nama, String jenis, int harga, String lokasi) {
            this.id = id;
            this.nama = nama;
            this.jenis = jenis;
            this.harga = harga;
            this.lokasi = lokasi;
        }

        public int getId() { return id; }
        public String getNama() { return nama; }
        public String getJenis() { return jenis; }
        public int getHarga() { return harga; }
        public String getLokasi() { return lokasi; }
    }

    public static class PerformaCabang {
        private final String lokasi;
        private final int performa;
        private final int income;

        public PerformaCabang(String lokasi, int performa, int income) {
            this.lokasi = lokasi;
            this.performa = performa;
            this.income = income;
        }

        public String getLokasi() { return lokasi; }
        public int getPerforma() { return performa; }
        public int getIncome() { return income; }
    }
}
