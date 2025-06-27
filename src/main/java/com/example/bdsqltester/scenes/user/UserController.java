package com.example.bdsqltester.scenes.user;

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
            SELECT m.nama_menu, m.jenis, m.harga, c.lokasi
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

        // TODO: insert order into pemesanan + pengiriman table
        showInfo("Success", "Order placed for " + selected.getNamaMenu() + " on " + deliveryDate);
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
}
