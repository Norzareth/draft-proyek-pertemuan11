package com.example.bdsqltester.scenes.admin;

import com.example.bdsqltester.datasources.DataSourceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PerformaController {

    @FXML private TableView<PerformaCabang> performaTable;
    @FXML private TableColumn<PerformaCabang, String> colCabang;
    @FXML private TableColumn<PerformaCabang, Integer> colPerforma;
    @FXML private TableColumn<PerformaCabang, Integer> colIncome;

    @FXML
    public void initialize() {
        colCabang.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        colPerforma.setCellValueFactory(new PropertyValueFactory<>("performa"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));

        loadPerformaCabang();
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

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
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
