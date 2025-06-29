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

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class BranchAdminController {

    @FXML private ComboBox<String> cabangComboBox;
    @FXML private TableView<MenuData> menuTable;
    @FXML private TableColumn<MenuData, Integer> colMenuId;
    @FXML private TableColumn<MenuData, String> colMenuName;
    @FXML private TableColumn<MenuData, String> colMenuType;
    @FXML private TableColumn<MenuData, Integer> colMenuPrice;
    @FXML private TableColumn<MenuData, String> colMenuSeller;
    @FXML private TableColumn<MenuData, Integer> colStock;
    @FXML private TableColumn<MenuData, String> colStatus;
    
    @FXML private TextField newMenuName;
    @FXML private ComboBox<String> newMenuType;
    @FXML private ComboBox<String> newMenuSeller;
    @FXML private TextField newMenuPrice;
    @FXML private TextField newMenuStock;
    
    private final ObservableList<MenuData> menuItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        createStokHarianTableIfNotExists();
        setupMenuTable();
        loadCabangData();
        loadMenuTypes();
        loadSellers();
        
        // Set default cabang
        if (!cabangComboBox.getItems().isEmpty()) {
            cabangComboBox.setValue(cabangComboBox.getItems().get(0));
            loadMenuData();
        }
        
        showInfo("Info", "Admin Cabang Dashboard berhasil dimuat!");
    }
    
    private void createStokHarianTableIfNotExists() {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
                CREATE TABLE IF NOT EXISTS stok_harian (
                    id_stok_harian SERIAL PRIMARY KEY,
                    id_menu INT REFERENCES daftar_menu(id_menu),
                    tanggal DATE NOT NULL,
                    stok_harian INT NOT NULL DEFAULT 0,
                    UNIQUE(id_menu, tanggal)
                )
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            showError("Database Error", "Gagal membuat tabel stok_harian: " + e.getMessage());
        }
    }
    
    private void setupMenuTable() {
        colMenuId.setCellValueFactory(new PropertyValueFactory<>("idMenu"));
        colMenuName.setCellValueFactory(new PropertyValueFactory<>("namaMenu"));
        colMenuType.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colMenuPrice.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colMenuSeller.setCellValueFactory(new PropertyValueFactory<>("namaPenjual"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        menuTable.setItems(menuItems);
    }
    
    private void loadCabangData() {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = "SELECT lokasi FROM cabang ORDER BY lokasi";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                cabangComboBox.getItems().add(rs.getString("lokasi"));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data cabang: " + e.getMessage());
        }
    }
    
    private void loadMenuTypes() {
        newMenuType.getItems().addAll("Makanan Utama", "Minuman", "Snack", "Dessert");
    }
    
    private void loadSellers() {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = "SELECT nama_penjual FROM penjual_sampingan ORDER BY nama_penjual";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                newMenuSeller.getItems().add(rs.getString("nama_penjual"));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data penjual: " + e.getMessage());
        }
    }
    
    @FXML
    void onRefreshMenu() {
        loadMenuData();
    }
    
    private void loadMenuData() {
        String selectedCabang = cabangComboBox.getValue();
        if (selectedCabang == null) return;
        
        menuItems.clear();
        
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
                SELECT m.id_menu, m.nama_menu, m.jenis, m.harga, p.nama_penjual,
                       COALESCE(s.stok_harian, 0) as stok_harian,
                       CASE 
                           WHEN COALESCE(s.stok_harian, 0) > 0 THEN 'Tersedia'
                           ELSE 'Habis'
                       END as status
                FROM daftar_menu m
                JOIN penjual_sampingan p ON m.id_penjual = p.id_penjual
                JOIN cabang c ON m.cabang_id = c.cabang_id
                LEFT JOIN stok_harian s ON m.id_menu = s.id_menu AND s.tanggal = CURRENT_DATE
                WHERE c.lokasi = ?
                ORDER BY m.nama_menu
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, selectedCabang);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                menuItems.add(new MenuData(
                    rs.getInt("id_menu"),
                    rs.getString("nama_menu"),
                    rs.getString("jenis"),
                    rs.getInt("harga"),
                    rs.getString("nama_penjual"),
                    rs.getInt("stok_harian"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data menu: " + e.getMessage());
        }
    }
    
    @FXML
    void onAddMenu() {
        String namaMenu = newMenuName.getText().trim();
        String jenis = newMenuType.getValue();
        String namaPenjual = newMenuSeller.getValue();
        String hargaStr = newMenuPrice.getText().trim();
        String stokStr = newMenuStock.getText().trim();
        
        if (namaMenu.isEmpty() || jenis == null || namaPenjual == null || 
            hargaStr.isEmpty() || stokStr.isEmpty()) {
            showError("Error", "Semua field harus diisi");
            return;
        }
        
        try {
            int harga = Integer.parseInt(hargaStr);
            int stok = Integer.parseInt(stokStr);
            
            if (harga <= 0 || stok < 0) {
                showError("Error", "Harga harus > 0 dan stok harus >= 0");
                return;
            }
            
            addMenuToDatabase(namaMenu, jenis, namaPenjual, harga, stok);
            
        } catch (NumberFormatException e) {
            showError("Error", "Harga dan stok harus berupa angka");
        }
    }
    
    private void addMenuToDatabase(String namaMenu, String jenis, String namaPenjual, int harga, int stok) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            c.setAutoCommit(false);
            
            try {
                // Get cabang_id
                String cabangSql = "SELECT cabang_id FROM cabang WHERE lokasi = ?";
                PreparedStatement cabangStmt = c.prepareStatement(cabangSql);
                cabangStmt.setString(1, cabangComboBox.getValue());
                ResultSet cabangRs = cabangStmt.executeQuery();
                
                if (!cabangRs.next()) {
                    throw new SQLException("Cabang tidak ditemukan");
                }
                int cabangId = cabangRs.getInt("cabang_id");
                
                // Get penjual_id
                String penjualSql = "SELECT id_penjual FROM penjual_sampingan WHERE nama_penjual = ?";
                PreparedStatement penjualStmt = c.prepareStatement(penjualSql);
                penjualStmt.setString(1, namaPenjual);
                ResultSet penjualRs = penjualStmt.executeQuery();
                
                if (!penjualRs.next()) {
                    throw new SQLException("Penjual tidak ditemukan");
                }
                int penjualId = penjualRs.getInt("id_penjual");
                
                // Insert menu
                String menuSql = """
                    INSERT INTO daftar_menu (nama_menu, jenis, id_penjual, harga, cabang_id)
                    VALUES (?, ?, ?, ?, ?)
                    RETURNING id_menu
                    """;
                PreparedStatement menuStmt = c.prepareStatement(menuSql);
                menuStmt.setString(1, namaMenu);
                menuStmt.setString(2, jenis);
                menuStmt.setInt(3, penjualId);
                menuStmt.setInt(4, harga);
                menuStmt.setInt(5, cabangId);
                
                ResultSet menuRs = menuStmt.executeQuery();
                if (!menuRs.next()) {
                    throw new SQLException("Gagal insert menu");
                }
                int menuId = menuRs.getInt("id_menu");
                
                // Insert stok harian
                if (stok > 0) {
                    String stokSql = """
                        INSERT INTO stok_harian (id_menu, tanggal, stok_harian)
                        VALUES (?, CURRENT_DATE, ?)
                        """;
                    PreparedStatement stokStmt = c.prepareStatement(stokSql);
                    stokStmt.setInt(1, menuId);
                    stokStmt.setInt(2, stok);
                    stokStmt.executeUpdate();
                }
                
                c.commit();
                showInfo("Sukses", "Menu berhasil ditambahkan");
                clearForm();
                loadMenuData();
                
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            showError("Database Error", "Gagal menambahkan menu: " + e.getMessage());
        }
    }
    
    @FXML
    void onEditMenu() {
        MenuData selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih menu yang akan diedit");
            return;
        }
        
        // Populate form with selected data
        newMenuName.setText(selected.getNamaMenu());
        newMenuType.setValue(selected.getJenis());
        newMenuSeller.setValue(selected.getNamaPenjual());
        newMenuPrice.setText(String.valueOf(selected.getHarga()));
        newMenuStock.setText(String.valueOf(selected.getStok()));
        
        showInfo("Info", "Data menu telah dimuat ke form. Klik 'Update Menu' untuk menyimpan perubahan.");
    }
    
    @FXML
    void onUpdateMenu() {
        MenuData selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih menu yang akan diupdate");
            return;
        }
        
        String namaMenu = newMenuName.getText().trim();
        String jenis = newMenuType.getValue();
        String namaPenjual = newMenuSeller.getValue();
        String hargaStr = newMenuPrice.getText().trim();
        String stokStr = newMenuStock.getText().trim();
        
        if (namaMenu.isEmpty() || jenis == null || namaPenjual == null || 
            hargaStr.isEmpty() || stokStr.isEmpty()) {
            showError("Error", "Semua field harus diisi");
            return;
        }
        
        try {
            int harga = Integer.parseInt(hargaStr);
            int stok = Integer.parseInt(stokStr);
            
            if (harga <= 0 || stok < 0) {
                showError("Error", "Harga harus > 0 dan stok harus >= 0");
                return;
            }
            
            updateMenuInDatabase(selected.getIdMenu(), namaMenu, jenis, namaPenjual, harga, stok);
            
        } catch (NumberFormatException e) {
            showError("Error", "Harga dan stok harus berupa angka");
        }
    }
    
    private void updateMenuInDatabase(int menuId, String namaMenu, String jenis, String namaPenjual, int harga, int stok) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            c.setAutoCommit(false);
            
            try {
                // Get penjual_id
                String penjualSql = "SELECT id_penjual FROM penjual_sampingan WHERE nama_penjual = ?";
                PreparedStatement penjualStmt = c.prepareStatement(penjualSql);
                penjualStmt.setString(1, namaPenjual);
                ResultSet penjualRs = penjualStmt.executeQuery();
                
                if (!penjualRs.next()) {
                    throw new SQLException("Penjual tidak ditemukan");
                }
                int penjualId = penjualRs.getInt("id_penjual");
                
                // Update menu
                String menuSql = """
                    UPDATE daftar_menu 
                    SET nama_menu = ?, jenis = ?, id_penjual = ?, harga = ?
                    WHERE id_menu = ?
                    """;
                PreparedStatement menuStmt = c.prepareStatement(menuSql);
                menuStmt.setString(1, namaMenu);
                menuStmt.setString(2, jenis);
                menuStmt.setInt(3, penjualId);
                menuStmt.setInt(4, harga);
                menuStmt.setInt(5, menuId);
                
                int affected = menuStmt.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Menu tidak ditemukan");
                }
                
                // Update stok harian
                String stokSql = """
                    INSERT INTO stok_harian (id_menu, tanggal, stok_harian)
                    VALUES (?, CURRENT_DATE, ?)
                    ON CONFLICT (id_menu, tanggal) 
                    DO UPDATE SET stok_harian = EXCLUDED.stok_harian
                    """;
                PreparedStatement stokStmt = c.prepareStatement(stokSql);
                stokStmt.setInt(1, menuId);
                stokStmt.setInt(2, stok);
                stokStmt.executeUpdate();
                
                c.commit();
                showInfo("Sukses", "Menu berhasil diupdate");
                clearForm();
                loadMenuData();
                
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            showError("Database Error", "Gagal update menu: " + e.getMessage());
        }
    }
    
    @FXML
    void onDeleteMenu() {
        MenuData selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih menu yang akan dihapus");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin menghapus menu '" + selected.getNamaMenu() + "'?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteMenuFromDatabase(selected.getIdMenu());
            }
        });
    }
    
    private void deleteMenuFromDatabase(int menuId) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            // Delete stok harian first
            String stokSql = "DELETE FROM stok_harian WHERE id_menu = ?";
            PreparedStatement stokStmt = c.prepareStatement(stokSql);
            stokStmt.setInt(1, menuId);
            stokStmt.executeUpdate();
            
            // Delete menu
            String menuSql = "DELETE FROM daftar_menu WHERE id_menu = ?";
            PreparedStatement menuStmt = c.prepareStatement(menuSql);
            menuStmt.setInt(1, menuId);
            
            int affected = menuStmt.executeUpdate();
            if (affected > 0) {
                showInfo("Sukses", "Menu berhasil dihapus");
                loadMenuData();
            } else {
                showError("Error", "Gagal menghapus menu");
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal menghapus menu: " + e.getMessage());
        }
    }
    
    @FXML
    void onSetStock() {
        MenuData selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih menu yang akan diatur stoknya");
            return;
        }
        
        TextInputDialog dialog = new TextInputDialog(String.valueOf(selected.getStok()));
        dialog.setTitle("Set Stok Harian");
        dialog.setHeaderText(null);
        dialog.setContentText("Masukkan jumlah stok untuk menu '" + selected.getNamaMenu() + "':");
        
        dialog.showAndWait().ifPresent(stockStr -> {
            try {
                int stock = Integer.parseInt(stockStr);
                if (stock < 0) {
                    showError("Error", "Stok tidak boleh negatif");
                    return;
                }
                setMenuStock(selected.getIdMenu(), stock);
            } catch (NumberFormatException e) {
                showError("Error", "Masukkan angka yang valid");
            }
        });
    }
    
    private void setMenuStock(int menuId, int stock) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
                INSERT INTO stok_harian (id_menu, tanggal, stok_harian)
                VALUES (?, CURRENT_DATE, ?)
                ON CONFLICT (id_menu, tanggal) 
                DO UPDATE SET stok_harian = EXCLUDED.stok_harian
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, menuId);
            stmt.setInt(2, stock);
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                showInfo("Sukses", "Stok berhasil diatur");
                loadMenuData();
            } else {
                showError("Error", "Gagal mengatur stok");
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal mengatur stok: " + e.getMessage());
        }
    }
    
    private void clearForm() {
        newMenuName.clear();
        newMenuType.setValue(null);
        newMenuSeller.setValue(null);
        newMenuPrice.clear();
        newMenuStock.clear();
    }
    
    @FXML
    void onLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load());
            HelloApplication.getApplicationInstance().getPrimaryStage().setScene(scene);
            HelloApplication.getApplicationInstance().getPrimaryStage().setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
    
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }
    
    // Data class for menu table
    public static class MenuData {
        private final int idMenu;
        private final String namaMenu;
        private final String jenis;
        private final int harga;
        private final String namaPenjual;
        private final int stok;
        private final String status;
        
        public MenuData(int idMenu, String namaMenu, String jenis, int harga, String namaPenjual, int stok, String status) {
            this.idMenu = idMenu;
            this.namaMenu = namaMenu;
            this.jenis = jenis;
            this.harga = harga;
            this.namaPenjual = namaPenjual;
            this.stok = stok;
            this.status = status;
        }
        
        public int getIdMenu() { return idMenu; }
        public String getNamaMenu() { return namaMenu; }
        public String getJenis() { return jenis; }
        public int getHarga() { return harga; }
        public String getNamaPenjual() { return namaPenjual; }
        public int getStok() { return stok; }
        public String getStatus() { return status; }
    }
} 