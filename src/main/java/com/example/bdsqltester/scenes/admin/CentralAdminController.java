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
    @FXML private Button lihatPerformaButton;

    @FXML
    public void initialize() {
        colNamaMenu.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));

        loadMenuItems();
    }

    private void loadMenuItems() {
        ObservableList<MenuItem> list = FXCollections.observableArrayList();

        String sql = """
            SELECT m.id_menu, m.nama_menu, m.jenis, m.harga, c.lokasi
            FROM daftar_menu m
            JOIN cabang c ON m.cabang_id = c.cabang_id
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

    @FXML
    void onSaveDiskon() {
        MenuItem selected = performanceTable.getSelectionModel().getSelectedItem();
        String syarat = syaratField.getText();
        int persen;

        try {
            persen = Integer.parseInt(diskonField.getText());
        } catch (NumberFormatException e) {
            showError("Masukkan angka valid untuk diskon.", "");
            return;
        }

        if (selected == null || syarat.isEmpty()) {
            showError("Lengkapi semua input dan pilih menu.", "");
            return;
        }

        try (Connection conn = DataSourceManager.getUserConnection()) {
            String sql = "INSERT INTO diskon (persentase_diskon, syarat_diskon, id_menu) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setInt(1, persen);
            stmt.setString(2, syarat);
            stmt.setInt(3, selected.getId());

            stmt.executeUpdate();
            showInfo("Diskon berhasil disimpan untuk menu: " + selected.getNama());

        } catch (SQLException e) {
            showError("Gagal menyimpan diskon.", e.getMessage());
        }
    }

    @FXML
    void onLihatPerformaClick() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("performa-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Performa Cabang");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Gagal membuka tampilan performa cabang", e.getMessage());
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
}
