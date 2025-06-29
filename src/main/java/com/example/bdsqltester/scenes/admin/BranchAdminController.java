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
import java.time.format.DateTimeFormatter;

public class BranchAdminController {

    @FXML private ComboBox<String> cabangComboBox;
    @FXML private TableView<MenuData> menuTable;
    @FXML private TableColumn<MenuData, Integer> colMenuId;
    @FXML private TableColumn<MenuData, String> colMenuName;
    @FXML private TableColumn<MenuData, String> colMenuType;
    @FXML private TableColumn<MenuData, Integer> colMenuPrice;
    @FXML private TableColumn<MenuData, String> colMenuSeller;
    
    @FXML private ComboBox<String> statusFilter;
    @FXML private TableView<OrderData> orderTable;
    @FXML private TableColumn<OrderData, Integer> colOrderId;
    @FXML private TableColumn<OrderData, String> colOrderDate;
    @FXML private TableColumn<OrderData, String> colOrderStatus;
    @FXML private TableColumn<OrderData, String> colCustomerName;
    @FXML private TableColumn<OrderData, String> colDeliveryStatus;
    @FXML private TableColumn<OrderData, Integer> colTotalAmount;
    
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private TextArea reportArea;
    
    private final ObservableList<MenuData> menuItems = FXCollections.observableArrayList();
    private final ObservableList<OrderData> orderItems = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        setupMenuTable();
        setupOrderTable();
        loadCabangData();
        loadStatusFilter();
        
        // Set default dates
        startDate.setValue(LocalDate.now().minusDays(30));
        endDate.setValue(LocalDate.now());
    }
    
    private void setupMenuTable() {
        colMenuId.setCellValueFactory(new PropertyValueFactory<>("idMenu"));
        colMenuName.setCellValueFactory(new PropertyValueFactory<>("namaMenu"));
        colMenuType.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        colMenuPrice.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colMenuSeller.setCellValueFactory(new PropertyValueFactory<>("namaPenjual"));
        
        menuTable.setItems(menuItems);
    }
    
    private void setupOrderTable() {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("pesananId"));
        colOrderDate.setCellValueFactory(new PropertyValueFactory<>("tanggalPesanan"));
        colOrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colDeliveryStatus.setCellValueFactory(new PropertyValueFactory<>("statusPengiriman"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        
        orderTable.setItems(orderItems);
    }
    
    private void loadCabangData() {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = "SELECT lokasi FROM cabang ORDER BY lokasi";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                cabangComboBox.getItems().add(rs.getString("lokasi"));
            }
            
            if (!cabangComboBox.getItems().isEmpty()) {
                cabangComboBox.setValue(cabangComboBox.getItems().get(0));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data cabang: " + e.getMessage());
        }
    }
    
    private void loadStatusFilter() {
        statusFilter.getItems().addAll("Semua", "Belum Diproses", "Dalam Proses", "Selesai");
        statusFilter.setValue("Semua");
    }
    
    @FXML
    void onRefreshMenu() {
        String selectedCabang = cabangComboBox.getValue();
        if (selectedCabang != null) {
            loadMenuData(selectedCabang);
        }
    }
    
    private void loadMenuData(String cabang) {
        menuItems.clear();
        
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
                SELECT m.id_menu, m.nama_menu, m.jenis, m.harga, p.nama_penjual
                FROM daftar_menu m
                JOIN penjual_sampingan p ON m.id_penjual = p.id_penjual
                JOIN cabang c ON m.cabang_id = c.cabang_id
                WHERE c.lokasi = ?
                ORDER BY m.nama_menu
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, cabang);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                menuItems.add(new MenuData(
                    rs.getInt("id_menu"),
                    rs.getString("nama_menu"),
                    rs.getString("jenis"),
                    rs.getInt("harga"),
                    rs.getString("nama_penjual")
                ));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data menu: " + e.getMessage());
        }
    }
    
    @FXML
    void onAddMenu() {
        // TODO: Implement add menu dialog
        showInfo("Info", "Fitur tambah menu akan diimplementasikan");
    }
    
    @FXML
    void onEditMenu() {
        MenuData selected = menuTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih menu yang akan diedit");
            return;
        }
        // TODO: Implement edit menu dialog
        showInfo("Info", "Fitur edit menu akan diimplementasikan");
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
                deleteMenu(selected.getIdMenu());
            }
        });
    }
    
    private void deleteMenu(int menuId) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = "DELETE FROM daftar_menu WHERE id_menu = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, menuId);
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                showInfo("Sukses", "Menu berhasil dihapus");
                onRefreshMenu();
            } else {
                showError("Error", "Gagal menghapus menu");
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal menghapus menu: " + e.getMessage());
        }
    }
    
    @FXML
    void onRefreshOrders() {
        String selectedStatus = statusFilter.getValue();
        loadOrderData(selectedStatus);
    }
    
    private void loadOrderData(String status) {
        orderItems.clear();
        
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
                SELECT p.pesanan_id, p.tanggal_pesanan, p.status, 
                       u.username as customer_name, pg.status_pengiriman,
                       COALESCE(SUM(dm.harga), 0) as total_amount
                FROM pemesanan p
                JOIN users u ON p.user_id = u.user_id
                JOIN pengiriman pg ON p.pengiriman_id = pg.pengiriman_id
                LEFT JOIN pesanan_menu pm ON p.pesanan_id = pm.pesanan_id
                LEFT JOIN daftar_menu dm ON pm.id_menu = dm.id_menu
                WHERE (? = 'Semua' OR p.status = ?)
                GROUP BY p.pesanan_id, p.tanggal_pesanan, p.status, u.username, pg.status_pengiriman
                ORDER BY p.tanggal_pesanan DESC
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setString(2, status.equals("Semua") ? "" : status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                orderItems.add(new OrderData(
                    rs.getInt("pesanan_id"),
                    rs.getDate("tanggal_pesanan").toString(),
                    rs.getString("status"),
                    rs.getString("customer_name"),
                    rs.getString("status_pengiriman"),
                    rs.getInt("total_amount")
                ));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data pesanan: " + e.getMessage());
        }
    }
    
    @FXML
    void onUpdateOrderStatus() {
        OrderData selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih pesanan yang akan diupdate");
            return;
        }
        // TODO: Implement status update dialog
        showInfo("Info", "Fitur update status akan diimplementasikan");
    }
    
    @FXML
    void onViewOrderDetail() {
        OrderData selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih pesanan yang akan dilihat detailnya");
            return;
        }
        // TODO: Implement order detail dialog
        showInfo("Info", "Fitur lihat detail akan diimplementasikan");
    }
    
    @FXML
    void onGenerateReport() {
        LocalDate start = startDate.getValue();
        LocalDate end = endDate.getValue();
        
        if (start == null || end == null) {
            showError("Error", "Pilih periode laporan");
            return;
        }
        
        if (start.isAfter(end)) {
            showError("Error", "Tanggal mulai tidak boleh setelah tanggal akhir");
            return;
        }
        
        generateBranchReport(start, end);
    }
    
    private void generateBranchReport(LocalDate start, LocalDate end) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
                SELECT 
                    p.tanggal_pesanan,
                    COUNT(p.pesanan_id) as total_orders,
                    SUM(COALESCE(dm.harga, 0)) as total_revenue,
                    AVG(COALESCE(dm.harga, 0)) as avg_order_value
                FROM pemesanan p
                LEFT JOIN pesanan_menu pm ON p.pesanan_id = pm.pesanan_id
                LEFT JOIN daftar_menu dm ON pm.id_menu = dm.id_menu
                WHERE p.tanggal_pesanan BETWEEN ? AND ?
                GROUP BY p.tanggal_pesanan
                ORDER BY p.tanggal_pesanan
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2, Date.valueOf(end));
            ResultSet rs = stmt.executeQuery();
            
            StringBuilder report = new StringBuilder();
            report.append("LAPORAN CABANG\n");
            report.append("Periode: ").append(start).append(" s/d ").append(end).append("\n");
            report.append("=".repeat(50)).append("\n\n");
            
            int totalOrders = 0;
            int totalRevenue = 0;
            
            while (rs.next()) {
                String date = rs.getDate("tanggal_pesanan").toString();
                int orders = rs.getInt("total_orders");
                int revenue = rs.getInt("total_revenue");
                double avgValue = rs.getDouble("avg_order_value");
                
                report.append(String.format("Tanggal: %s\n", date));
                report.append(String.format("  Total Pesanan: %d\n", orders));
                report.append(String.format("  Total Pendapatan: Rp %,d\n", revenue));
                report.append(String.format("  Rata-rata Nilai Pesanan: Rp %.0f\n\n", avgValue));
                
                totalOrders += orders;
                totalRevenue += revenue;
            }
            
            report.append("=".repeat(50)).append("\n");
            report.append(String.format("TOTAL KESELURUHAN:\n"));
            report.append(String.format("Total Pesanan: %d\n", totalOrders));
            report.append(String.format("Total Pendapatan: Rp %,d\n", totalRevenue));
            
            reportArea.setText(report.toString());
            
        } catch (SQLException e) {
            showError("Database Error", "Gagal generate laporan: " + e.getMessage());
        }
    }
    
    @FXML
    void onExportPDF() {
        // TODO: Implement PDF export
        showInfo("Info", "Fitur export PDF akan diimplementasikan");
    }
    
    @FXML
    void onExportExcel() {
        // TODO: Implement Excel export
        showInfo("Info", "Fitur export Excel akan diimplementasikan");
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
    
    // Data classes for table
    public static class MenuData {
        private final int idMenu;
        private final String namaMenu;
        private final String jenis;
        private final int harga;
        private final String namaPenjual;
        
        public MenuData(int idMenu, String namaMenu, String jenis, int harga, String namaPenjual) {
            this.idMenu = idMenu;
            this.namaMenu = namaMenu;
            this.jenis = jenis;
            this.harga = harga;
            this.namaPenjual = namaPenjual;
        }
        
        public int getIdMenu() { return idMenu; }
        public String getNamaMenu() { return namaMenu; }
        public String getJenis() { return jenis; }
        public int getHarga() { return harga; }
        public String getNamaPenjual() { return namaPenjual; }
    }
    
    public static class OrderData {
        private final int pesananId;
        private final String tanggalPesanan;
        private final String status;
        private final String customerName;
        private final String statusPengiriman;
        private final int totalAmount;
        
        public OrderData(int pesananId, String tanggalPesanan, String status, String customerName, String statusPengiriman, int totalAmount) {
            this.pesananId = pesananId;
            this.tanggalPesanan = tanggalPesanan;
            this.status = status;
            this.customerName = customerName;
            this.statusPengiriman = statusPengiriman;
            this.totalAmount = totalAmount;
        }
        
        public int getPesananId() { return pesananId; }
        public String getTanggalPesanan() { return tanggalPesanan; }
        public String getStatus() { return status; }
        public String getCustomerName() { return customerName; }
        public String getStatusPengiriman() { return statusPengiriman; }
        public int getTotalAmount() { return totalAmount; }
    }
} 