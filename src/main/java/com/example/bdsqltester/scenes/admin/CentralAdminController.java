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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class CentralAdminController {

    @FXML private TableView<MenuItem> performanceTable;
    @FXML private TableColumn<MenuItem, String> colNamaMenu;
    @FXML private TableColumn<MenuItem, String> colJenis;
    @FXML private TableColumn<MenuItem, String> colLokasi;
    @FXML private TableColumn<MenuItem, Integer> colHarga;
    @FXML private TableColumn<MenuItem, Integer> colHargaDiskon;
    @FXML private TableColumn<MenuItem, String> colDeskripsiDiskon;
    @FXML private TableColumn<MenuItem, Void> colAksiDiskon;

    @FXML private TextField diskonField;
    @FXML private TextField syaratField;
    @FXML private Button lihatPerformaButton;

    // Performance table fields
    @FXML private TableView<PerformaCabang> performaTable;
    @FXML private TableColumn<PerformaCabang, String> colCabang;
    @FXML private TableColumn<PerformaCabang, Integer> colPerforma;
    @FXML private TableColumn<PerformaCabang, Integer> colIncome;

    // Discount management fields
    @FXML private ComboBox<MenuItem> menuComboBox;
    @FXML private TextField hargaBaruField;
    @FXML private TextField deskripsiDiskonField;

    @FXML
    public void initialize() {
        // Setup menu table
        colNamaMenu.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colJenis.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colLokasi.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colHargaDiskon.setCellValueFactory(new PropertyValueFactory<>("hargaDiskon"));
        colDeskripsiDiskon.setCellValueFactory(new PropertyValueFactory<>("deskripsiDiskon"));

        // Setup action column
        setupActionColumn();

        // Setup performance table
        colCabang.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        colPerforma.setCellValueFactory(new PropertyValueFactory<>("performa"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));

        loadMenuItems();
        loadPerformaCabang();
        setupMenuComboBox();
    }

    private void setupActionColumn() {
        colAksiDiskon.setCellFactory(param -> new TableCell<MenuItem, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Hapus");
            private final HBox buttonBox = new HBox(5, editButton, deleteButton);

            {
                editButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
                deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                
                editButton.setOnAction(event -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    editDiskon(item);
                });
                
                deleteButton.setOnAction(event -> {
                    MenuItem item = getTableView().getItems().get(getIndex());
                    deleteDiskon(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    MenuItem menuItem = getTableView().getItems().get(getIndex());
                    if (menuItem.hasDiskon()) {
                        setGraphic(buttonBox);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void loadMenuItems() {
        ObservableList<MenuItem> list = FXCollections.observableArrayList();

        // First, try to load menu items without discount info
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
                
                // Try to get discount info for this menu
                int hargaDiskon = 0;
                String deskripsiDiskon = "";
                
                try {
                    String discountSql = "SELECT persentase_diskon, syarat_diskon FROM diskon WHERE id_menu = ?";
                    PreparedStatement discountStmt = conn.prepareStatement(discountSql);
                    discountStmt.setInt(1, id);
                    ResultSet discountRs = discountStmt.executeQuery();
                    
                    if (discountRs.next()) {
                        int diskonPersen = discountRs.getInt("persentase_diskon");
                        deskripsiDiskon = discountRs.getString("syarat_diskon");
                        if (diskonPersen > 0) {
                            hargaDiskon = harga - (harga * diskonPersen / 100);
                        }
                    }
                    discountStmt.close();
                } catch (SQLException discountError) {
                    // If discount table doesn't exist or permission denied, continue without discount
                    System.out.println("Warning: Could not load discount info for menu " + id + ": " + discountError.getMessage());
                }
                
                list.add(new MenuItem(id, nama, jenis, harga, lokasi, hargaDiskon, deskripsiDiskon));
            }

            performanceTable.setItems(list);

        } catch (SQLException e) {
            showError("Gagal memuat daftar menu", e.getMessage());
            e.printStackTrace();
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

    private void setupMenuComboBox() {
        // Load menu items for combo box (without discount info)
        ObservableList<MenuItem> menuList = FXCollections.observableArrayList();
        
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
                menuList.add(new MenuItem(id, nama, jenis, harga, lokasi));
            }

            menuComboBox.setItems(menuList);
            menuComboBox.setCellFactory(param -> new ListCell<MenuItem>() {
                @Override
                protected void updateItem(MenuItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNama() + " - " + item.getLokasi() + " (Rp " + String.format("%,d", item.getHarga()) + ")");
                    }
                }
            });
            menuComboBox.setButtonCell(menuComboBox.getCellFactory().call(null));

        } catch (SQLException e) {
            showError("Gagal memuat daftar menu untuk combo box", e.getMessage());
        }
    }

    @FXML
    void onSaveDiskon() {
        MenuItem selected = menuComboBox.getValue();
        String hargaBaruText = hargaBaruField.getText();
        String deskripsi = deskripsiDiskonField.getText();

        if (selected == null) {
            showError("Pilih Menu", "Silakan pilih menu terlebih dahulu.");
            return;
        }

        if (hargaBaruText.isEmpty()) {
            showError("Harga Baru", "Masukkan harga baru.");
            return;
        }

        if (deskripsi.isEmpty()) {
            showError("Deskripsi Diskon", "Masukkan deskripsi diskon.");
            return;
        }

        try {
            int hargaBaru = Integer.parseInt(hargaBaruText);
            int hargaAsli = selected.getHarga();
            
            if (hargaBaru >= hargaAsli) {
                showError("Harga Tidak Valid", "Harga diskon harus lebih rendah dari harga asli.");
                return;
            }

            // Calculate discount percentage
            int diskonPersen = ((hargaAsli - hargaBaru) * 100) / hargaAsli;

            try (Connection conn = DataSourceManager.getUserConnection()) {
                // Check if discount already exists
                try {
                    String checkSql = "SELECT COUNT(*) FROM diskon WHERE id_menu = ?";
                    PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                    checkStmt.setInt(1, selected.getId());
                    ResultSet rs = checkStmt.executeQuery();
                    rs.next();
                    int count = rs.getInt(1);
                    checkStmt.close();

                    if (count > 0) {
                        // Update existing discount
                        String updateSql = "UPDATE diskon SET persentase_diskon = ?, syarat_diskon = ? WHERE id_menu = ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setInt(1, diskonPersen);
                        updateStmt.setString(2, deskripsi);
                        updateStmt.setInt(3, selected.getId());
                        int updatedRows = updateStmt.executeUpdate();
                        updateStmt.close();
                        
                        if (updatedRows > 0) {
                            showInfo("Diskon berhasil diperbarui untuk menu: " + selected.getNama());
                        } else {
                            showError("Gagal memperbarui diskon", "Tidak ada baris yang diperbarui");
                        }
                    } else {
                        // Insert new discount
                        String insertSql = "INSERT INTO diskon (persentase_diskon, syarat_diskon, id_menu) VALUES (?, ?, ?)";
                        PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setInt(1, diskonPersen);
                        insertStmt.setString(2, deskripsi);
                        insertStmt.setInt(3, selected.getId());
                        int insertedRows = insertStmt.executeUpdate();
                        insertStmt.close();
                        
                        if (insertedRows > 0) {
                            showInfo("Diskon berhasil ditambahkan untuk menu: " + selected.getNama());
                        } else {
                            showError("Gagal menambahkan diskon", "Tidak ada baris yang ditambahkan");
                        }
                    }

                    clearDiscountForm();
                    loadMenuItems(); // Refresh table

                } catch (SQLException operationError) {
                    if (operationError.getMessage().contains("permission denied")) {
                        showError("Permission Error", 
                            "Tidak memiliki permission untuk mengakses tabel diskon.\n" +
                            "Silakan jalankan script fix_discount_permissions.bat sebagai administrator.");
                    } else if (operationError.getMessage().contains("relation \"diskon\" does not exist")) {
                        showError("Tabel Tidak Ada", 
                            "Tabel diskon belum dibuat.\n" +
                            "Silakan jalankan script fix_discount_permissions.bat sebagai administrator.");
                    } else {
                        showError("Gagal menyimpan diskon", "Error detail: " + operationError.getMessage());
                    }
                    operationError.printStackTrace();
                }

            } catch (SQLException e) {
                showError("Gagal koneksi database", "Error: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            showError("Harga Tidak Valid", "Masukkan angka yang valid untuk harga.");
        }
    }

    @FXML
    void onCancelDiskon() {
        clearDiscountForm();
    }

    private void clearDiscountForm() {
        menuComboBox.setValue(null);
        hargaBaruField.clear();
        deskripsiDiskonField.clear();
    }

    private void editDiskon(MenuItem item) {
        menuComboBox.setValue(item);
        hargaBaruField.setText(String.valueOf(item.getHargaDiskon()));
        deskripsiDiskonField.setText(item.getDeskripsiDiskon());
    }

    private void deleteDiskon(MenuItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hapus Diskon");
        alert.setHeaderText("Konfirmasi Hapus");
        alert.setContentText("Apakah Anda yakin ingin menghapus diskon untuk menu: " + item.getNama() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection conn = DataSourceManager.getUserConnection()) {
                String sql = "DELETE FROM diskon WHERE id_menu = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, item.getId());
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    showInfo("Diskon berhasil dihapus untuk menu: " + item.getNama());
                } else {
                    showInfo("Tidak ada diskon yang ditemukan untuk menu: " + item.getNama());
                }
                
                loadMenuItems(); // Refresh table

            } catch (SQLException e) {
                showError("Gagal menghapus diskon.", e.getMessage());
                e.printStackTrace();
            }
        }
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

    @FXML
    void onRefreshPerforma() {
        loadPerformaCabang();
        showInfo("Data berhasil diperbarui");
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
        private final int hargaDiskon;
        private final String deskripsiDiskon;

        public MenuItem(int id, String nama, String jenis, int harga, String lokasi) {
            this.id = id;
            this.nama = nama;
            this.jenis = jenis;
            this.harga = harga;
            this.lokasi = lokasi;
            this.hargaDiskon = 0;
            this.deskripsiDiskon = "";
        }

        public MenuItem(int id, String nama, String jenis, int harga, String lokasi, int hargaDiskon, String deskripsiDiskon) {
            this.id = id;
            this.nama = nama;
            this.jenis = jenis;
            this.harga = harga;
            this.lokasi = lokasi;
            this.hargaDiskon = hargaDiskon;
            this.deskripsiDiskon = deskripsiDiskon;
        }

        public int getId() { return id; }
        public String getNama() { return nama; }
        public String getJenis() { return jenis; }
        public int getHarga() { return harga; }
        public String getLokasi() { return lokasi; }
        public int getHargaDiskon() { return hargaDiskon; }
        public String getDeskripsiDiskon() { return deskripsiDiskon; }

        public boolean hasDiskon() {
            return hargaDiskon > 0;
        }

        @Override
        public String toString() {
            return nama + " - " + lokasi;
        }
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
