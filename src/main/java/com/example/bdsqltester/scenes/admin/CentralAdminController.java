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

public class CentralAdminController {

    @FXML private TableView<BranchData> branchTable;
    @FXML private TableColumn<BranchData, Integer> colBranchId;
    @FXML private TableColumn<BranchData, String> colBranchLocation;
    @FXML private TableColumn<BranchData, Integer> colBranchPerformance;
    @FXML private TableColumn<BranchData, Integer> colBranchIncome;
    @FXML private TableColumn<BranchData, Integer> colBranchStaff;
    
    @FXML private ComboBox<String> staffBranchFilter;
    @FXML private TableView<StaffData> staffTable;
    @FXML private TableColumn<StaffData, Integer> colStaffId;
    @FXML private TableColumn<StaffData, String> colStaffName;
    @FXML private TableColumn<StaffData, String> colStaffJob;
    @FXML private TableColumn<StaffData, String> colStaffBranch;
    
    @FXML private DatePicker reportStartDate;
    @FXML private DatePicker reportEndDate;
    @FXML private ComboBox<String> reportType;
    @FXML private TextArea overallReportArea;
    
    @FXML private ComboBox<String> performanceMetric;
    @FXML private TextArea analysisArea;
    
    private final ObservableList<BranchData> branchItems = FXCollections.observableArrayList();
    private final ObservableList<StaffData> staffItems = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        setupBranchTable();
        setupStaffTable();
        loadReportTypes();
        loadPerformanceMetrics();
        
        // Set default dates
        reportStartDate.setValue(LocalDate.now().minusDays(30));
        reportEndDate.setValue(LocalDate.now());
        
        // Load initial data
        onRefreshBranches();
        onRefreshStaff();
    }
    
    private void setupBranchTable() {
        colBranchId.setCellValueFactory(new PropertyValueFactory<>("cabangId"));
        colBranchLocation.setCellValueFactory(new PropertyValueFactory<>("lokasi"));
        colBranchPerformance.setCellValueFactory(new PropertyValueFactory<>("tingkatPerforma"));
        colBranchIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
        colBranchStaff.setCellValueFactory(new PropertyValueFactory<>("jumlahStaff"));
        
        branchTable.setItems(branchItems);
    }
    
    private void setupStaffTable() {
        colStaffId.setCellValueFactory(new PropertyValueFactory<>("idStaff"));
        colStaffName.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colStaffJob.setCellValueFactory(new PropertyValueFactory<>("jobDesc"));
        colStaffBranch.setCellValueFactory(new PropertyValueFactory<>("cabang"));
        
        staffTable.setItems(staffItems);
    }
    
    private void loadReportTypes() {
        reportType.getItems().addAll("Laporan Penjualan", "Laporan Performa Cabang", "Laporan Staff", "Laporan Keuangan");
        reportType.setValue("Laporan Penjualan");
    }
    
    private void loadPerformanceMetrics() {
        performanceMetric.getItems().addAll("Pendapatan", "Jumlah Pesanan", "Rata-rata Nilai Pesanan", "Performa Staff");
        performanceMetric.setValue("Pendapatan");
    }
    
    @FXML
    void onAddBranch() {
        // TODO: Implement add branch dialog
        showInfo("Info", "Fitur tambah cabang akan diimplementasikan");
    }
    
    @FXML
    void onRefreshBranches() {
        loadBranchData();
    }
    
    private void loadBranchData() {
        branchItems.clear();
        
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
                SELECT c.cabang_id, c.lokasi, pc.tingkat_performa, pc.income,
                       COUNT(s.id_staff) as jumlah_staff
                FROM cabang c
                JOIN performa_cabang pc ON c.id_performa_cabang = pc.id_performa_cabang
                LEFT JOIN staff_katering s ON c.cabang_id = s.cabang_id
                GROUP BY c.cabang_id, c.lokasi, pc.tingkat_performa, pc.income
                ORDER BY c.lokasi
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                branchItems.add(new BranchData(
                    rs.getInt("cabang_id"),
                    rs.getString("lokasi"),
                    rs.getInt("tingkat_performa"),
                    rs.getInt("income"),
                    rs.getInt("jumlah_staff")
                ));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data cabang: " + e.getMessage());
        }
    }
    
    @FXML
    void onEditBranch() {
        BranchData selected = branchTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih cabang yang akan diedit");
            return;
        }
        // TODO: Implement edit branch dialog
        showInfo("Info", "Fitur edit cabang akan diimplementasikan");
    }
    
    @FXML
    void onDeleteBranch() {
        BranchData selected = branchTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih cabang yang akan dihapus");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin menghapus cabang '" + selected.getLokasi() + "'?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteBranch(selected.getCabangId());
            }
        });
    }
    
    private void deleteBranch(int cabangId) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = "DELETE FROM cabang WHERE cabang_id = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, cabangId);
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                showInfo("Sukses", "Cabang berhasil dihapus");
                onRefreshBranches();
            } else {
                showError("Error", "Gagal menghapus cabang");
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal menghapus cabang: " + e.getMessage());
        }
    }
    
    @FXML
    void onViewBranchDetail() {
        BranchData selected = branchTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih cabang yang akan dilihat detailnya");
            return;
        }
        // TODO: Implement branch detail dialog
        showInfo("Info", "Fitur lihat detail cabang akan diimplementasikan");
    }
    
    @FXML
    void onAddStaff() {
        // TODO: Implement add staff dialog
        showInfo("Info", "Fitur tambah staff akan diimplementasikan");
    }
    
    @FXML
    void onRefreshStaff() {
        String selectedBranch = staffBranchFilter.getValue();
        loadStaffData(selectedBranch);
    }
    
    private void loadStaffData(String branch) {
        staffItems.clear();
        
        try (Connection c = DataSourceManager.getUserConnection()) {
            // Load branch filter if not loaded
            if (staffBranchFilter.getItems().isEmpty()) {
                String branchSql = "SELECT lokasi FROM cabang ORDER BY lokasi";
                PreparedStatement branchStmt = c.prepareStatement(branchSql);
                ResultSet branchRs = branchStmt.executeQuery();
                
                while (branchRs.next()) {
                    staffBranchFilter.getItems().add(branchRs.getString("lokasi"));
                }
                
                if (!staffBranchFilter.getItems().isEmpty()) {
                    staffBranchFilter.setValue(staffBranchFilter.getItems().get(0));
                }
            }
            
            String sql = """
                SELECT s.id_staff, s.nama, s.job_desc, c.lokasi
                FROM staff_katering s
                JOIN cabang c ON s.cabang_id = c.cabang_id
                WHERE (? IS NULL OR c.lokasi = ?)
                ORDER BY s.nama
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, branch);
            stmt.setString(2, branch);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                staffItems.add(new StaffData(
                    rs.getInt("id_staff"),
                    rs.getString("nama"),
                    rs.getString("job_desc"),
                    rs.getString("lokasi")
                ));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data staff: " + e.getMessage());
        }
    }
    
    @FXML
    void onEditStaff() {
        StaffData selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih staff yang akan diedit");
            return;
        }
        // TODO: Implement edit staff dialog
        showInfo("Info", "Fitur edit staff akan diimplementasikan");
    }
    
    @FXML
    void onDeleteStaff() {
        StaffData selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih staff yang akan dihapus");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin menghapus staff '" + selected.getNama() + "'?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deleteStaff(selected.getIdStaff());
            }
        });
    }
    
    private void deleteStaff(int staffId) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = "DELETE FROM staff_katering WHERE id_staff = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, staffId);
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                showInfo("Sukses", "Staff berhasil dihapus");
                onRefreshStaff();
            } else {
                showError("Error", "Gagal menghapus staff");
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal menghapus staff: " + e.getMessage());
        }
    }
    
    @FXML
    void onGenerateOverallReport() {
        LocalDate start = reportStartDate.getValue();
        LocalDate end = reportEndDate.getValue();
        String type = reportType.getValue();
        
        if (start == null || end == null) {
            showError("Error", "Pilih periode laporan");
            return;
        }
        
        if (start.isAfter(end)) {
            showError("Error", "Tanggal mulai tidak boleh setelah tanggal akhir");
            return;
        }
        
        generateOverallReport(start, end, type);
    }
    
    private void generateOverallReport(LocalDate start, LocalDate end, String type) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            StringBuilder report = new StringBuilder();
            report.append("LAPORAN KESELURUHAN\n");
            report.append("Jenis: ").append(type).append("\n");
            report.append("Periode: ").append(start).append(" s/d ").append(end).append("\n");
            report.append("=".repeat(60)).append("\n\n");
            
            switch (type) {
                case "Laporan Penjualan":
                    generateSalesReport(c, start, end, report);
                    break;
                case "Laporan Performa Cabang":
                    generateBranchPerformanceReport(c, report);
                    break;
                case "Laporan Staff":
                    generateStaffReport(c, report);
                    break;
                case "Laporan Keuangan":
                    generateFinancialReport(c, start, end, report);
                    break;
            }
            
            overallReportArea.setText(report.toString());
            
        } catch (SQLException e) {
            showError("Database Error", "Gagal generate laporan: " + e.getMessage());
        }
    }
    
    private void generateSalesReport(Connection c, LocalDate start, LocalDate end, StringBuilder report) throws SQLException {
        String sql = """
            SELECT 
                c.lokasi,
                COUNT(p.pesanan_id) as total_orders,
                SUM(COALESCE(dm.harga, 0)) as total_revenue
            FROM cabang c
            LEFT JOIN daftar_menu dm ON c.cabang_id = dm.cabang_id
            LEFT JOIN pesanan_menu pm ON dm.id_menu = pm.id_menu
            LEFT JOIN pemesanan p ON pm.pesanan_id = p.pesanan_id
            WHERE p.tanggal_pesanan BETWEEN ? AND ?
            GROUP BY c.cabang_id, c.lokasi
            ORDER BY total_revenue DESC
            """;
        
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setDate(1, Date.valueOf(start));
        stmt.setDate(2, Date.valueOf(end));
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String lokasi = rs.getString("lokasi");
            int orders = rs.getInt("total_orders");
            int revenue = rs.getInt("total_revenue");
            
            report.append(String.format("Cabang: %s\n", lokasi));
            report.append(String.format("  Total Pesanan: %d\n", orders));
            report.append(String.format("  Total Pendapatan: Rp %,d\n\n", revenue));
        }
    }
    
    private void generateBranchPerformanceReport(Connection c, StringBuilder report) throws SQLException {
        String sql = """
            SELECT c.lokasi, pc.tingkat_performa, pc.income
            FROM cabang c
            JOIN performa_cabang pc ON c.id_performa_cabang = pc.id_performa_cabang
            ORDER BY pc.tingkat_performa DESC
            """;
        
        PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String lokasi = rs.getString("lokasi");
            int performa = rs.getInt("tingkat_performa");
            int income = rs.getInt("income");
            
            report.append(String.format("Cabang: %s\n", lokasi));
            report.append(String.format("  Performa: %d%%\n", performa));
            report.append(String.format("  Income: Rp %,d\n\n", income));
        }
    }
    
    private void generateStaffReport(Connection c, StringBuilder report) throws SQLException {
        String sql = """
            SELECT c.lokasi, COUNT(s.id_staff) as jumlah_staff
            FROM cabang c
            LEFT JOIN staff_katering s ON c.cabang_id = s.cabang_id
            GROUP BY c.cabang_id, c.lokasi
            ORDER BY jumlah_staff DESC
            """;
        
        PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            String lokasi = rs.getString("lokasi");
            int staff = rs.getInt("jumlah_staff");
            
            report.append(String.format("Cabang: %s\n", lokasi));
            report.append(String.format("  Jumlah Staff: %d\n\n", staff));
        }
    }
    
    private void generateFinancialReport(Connection c, LocalDate start, LocalDate end, StringBuilder report) throws SQLException {
        String sql = """
            SELECT 
                SUM(COALESCE(dm.harga, 0)) as total_revenue,
                COUNT(DISTINCT p.pesanan_id) as total_orders,
                AVG(COALESCE(dm.harga, 0)) as avg_order_value
            FROM pemesanan p
            LEFT JOIN pesanan_menu pm ON p.pesanan_id = pm.pesanan_id
            LEFT JOIN daftar_menu dm ON pm.id_menu = dm.id_menu
            WHERE p.tanggal_pesanan BETWEEN ? AND ?
            """;
        
        PreparedStatement stmt = c.prepareStatement(sql);
        stmt.setDate(1, Date.valueOf(start));
        stmt.setDate(2, Date.valueOf(end));
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            int revenue = rs.getInt("total_revenue");
            int orders = rs.getInt("total_orders");
            double avgValue = rs.getDouble("avg_order_value");
            
            report.append("RINGKASAN KEUANGAN\n");
            report.append(String.format("Total Pendapatan: Rp %,d\n", revenue));
            report.append(String.format("Total Pesanan: %d\n", orders));
            report.append(String.format("Rata-rata Nilai Pesanan: Rp %.0f\n", avgValue));
        }
    }
    
    @FXML
    void onExportOverallPDF() {
        // TODO: Implement PDF export
        showInfo("Info", "Fitur export PDF akan diimplementasikan");
    }
    
    @FXML
    void onExportOverallExcel() {
        // TODO: Implement Excel export
        showInfo("Info", "Fitur export Excel akan diimplementasikan");
    }
    
    @FXML
    void onSendEmailReport() {
        // TODO: Implement email sending
        showInfo("Info", "Fitur kirim email akan diimplementasikan");
    }
    
    @FXML
    void onAnalyzePerformance() {
        String metric = performanceMetric.getValue();
        if (metric == null) {
            showError("Error", "Pilih metrik performa");
            return;
        }
        
        analyzePerformance(metric);
    }
    
    private void analyzePerformance(String metric) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            StringBuilder analysis = new StringBuilder();
            analysis.append("ANALISIS PERFORMA\n");
            analysis.append("Metrik: ").append(metric).append("\n");
            analysis.append("=".repeat(50)).append("\n\n");
            
            switch (metric) {
                case "Pendapatan":
                    analyzeRevenue(c, analysis);
                    break;
                case "Jumlah Pesanan":
                    analyzeOrders(c, analysis);
                    break;
                case "Rata-rata Nilai Pesanan":
                    analyzeAverageOrderValue(c, analysis);
                    break;
                case "Performa Staff":
                    analyzeStaffPerformance(c, analysis);
                    break;
            }
            
            analysisArea.setText(analysis.toString());
            
        } catch (SQLException e) {
            showError("Database Error", "Gagal menganalisis performa: " + e.getMessage());
        }
    }
    
    private void analyzeRevenue(Connection c, StringBuilder analysis) throws SQLException {
        String sql = """
            SELECT c.lokasi, SUM(COALESCE(dm.harga, 0)) as revenue
            FROM cabang c
            LEFT JOIN daftar_menu dm ON c.cabang_id = dm.cabang_id
            LEFT JOIN pesanan_menu pm ON dm.id_menu = pm.id_menu
            LEFT JOIN pemesanan p ON pm.pesanan_id = p.pesanan_id
            GROUP BY c.cabang_id, c.lokasi
            ORDER BY revenue DESC
            """;
        
        PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        analysis.append("ANALISIS PENDAPATAN PER CABANG:\n\n");
        
        while (rs.next()) {
            String lokasi = rs.getString("lokasi");
            int revenue = rs.getInt("revenue");
            
            analysis.append(String.format("• %s: Rp %,d\n", lokasi, revenue));
        }
    }
    
    private void analyzeOrders(Connection c, StringBuilder analysis) throws SQLException {
        String sql = """
            SELECT c.lokasi, COUNT(p.pesanan_id) as orders
            FROM cabang c
            LEFT JOIN daftar_menu dm ON c.cabang_id = dm.cabang_id
            LEFT JOIN pesanan_menu pm ON dm.id_menu = pm.id_menu
            LEFT JOIN pemesanan p ON pm.pesanan_id = p.pesanan_id
            GROUP BY c.cabang_id, c.lokasi
            ORDER BY orders DESC
            """;
        
        PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        analysis.append("ANALISIS JUMLAH PESANAN PER CABANG:\n\n");
        
        while (rs.next()) {
            String lokasi = rs.getString("lokasi");
            int orders = rs.getInt("orders");
            
            analysis.append(String.format("• %s: %d pesanan\n", lokasi, orders));
        }
    }
    
    private void analyzeAverageOrderValue(Connection c, StringBuilder analysis) throws SQLException {
        String sql = """
            SELECT c.lokasi, AVG(COALESCE(dm.harga, 0)) as avg_value
            FROM cabang c
            LEFT JOIN daftar_menu dm ON c.cabang_id = dm.cabang_id
            LEFT JOIN pesanan_menu pm ON dm.id_menu = pm.id_menu
            LEFT JOIN pemesanan p ON pm.pesanan_id = p.pesanan_id
            GROUP BY c.cabang_id, c.lokasi
            ORDER BY avg_value DESC
            """;
        
        PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        analysis.append("ANALISIS RATA-RATA NILAI PESANAN PER CABANG:\n\n");
        
        while (rs.next()) {
            String lokasi = rs.getString("lokasi");
            double avgValue = rs.getDouble("avg_value");
            
            analysis.append(String.format("• %s: Rp %.0f\n", lokasi, avgValue));
        }
    }
    
    private void analyzeStaffPerformance(Connection c, StringBuilder analysis) throws SQLException {
        String sql = """
            SELECT c.lokasi, COUNT(s.id_staff) as staff_count
            FROM cabang c
            LEFT JOIN staff_katering s ON c.cabang_id = s.cabang_id
            GROUP BY c.cabang_id, c.lokasi
            ORDER BY staff_count DESC
            """;
        
        PreparedStatement stmt = c.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        
        analysis.append("ANALISIS JUMLAH STAFF PER CABANG:\n\n");
        
        while (rs.next()) {
            String lokasi = rs.getString("lokasi");
            int staffCount = rs.getInt("staff_count");
            
            analysis.append(String.format("• %s: %d staff\n", lokasi, staffCount));
        }
    }
    
    @FXML
    void onShowPerformanceChart() {
        // TODO: Implement performance chart
        showInfo("Info", "Fitur grafik performa akan diimplementasikan");
    }
    
    @FXML
    void onShowRecommendations() {
        // TODO: Implement recommendations
        showInfo("Info", "Fitur rekomendasi akan diimplementasikan");
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
    public static class BranchData {
        private final int cabangId;
        private final String lokasi;
        private final int tingkatPerforma;
        private final int income;
        private final int jumlahStaff;
        
        public BranchData(int cabangId, String lokasi, int tingkatPerforma, int income, int jumlahStaff) {
            this.cabangId = cabangId;
            this.lokasi = lokasi;
            this.tingkatPerforma = tingkatPerforma;
            this.income = income;
            this.jumlahStaff = jumlahStaff;
        }
        
        public int getCabangId() { return cabangId; }
        public String getLokasi() { return lokasi; }
        public int getTingkatPerforma() { return tingkatPerforma; }
        public int getIncome() { return income; }
        public int getJumlahStaff() { return jumlahStaff; }
    }
    
    public static class StaffData {
        private final int idStaff;
        private final String nama;
        private final String jobDesc;
        private final String cabang;
        
        public StaffData(int idStaff, String nama, String jobDesc, String cabang) {
            this.idStaff = idStaff;
            this.nama = nama;
            this.jobDesc = jobDesc;
            this.cabang = cabang;
        }
        
        public int getIdStaff() { return idStaff; }
        public String getNama() { return nama; }
        public String getJobDesc() { return jobDesc; }
        public String getCabang() { return cabang; }
    }
} 