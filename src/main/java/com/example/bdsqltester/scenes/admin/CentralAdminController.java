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
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

public class CentralAdminController {

    // Performance tab
    @FXML private DatePicker performanceStartDate;
    @FXML private DatePicker performanceEndDate;
    @FXML private TableView<PerformanceData> performanceTable;
    @FXML private TableColumn<PerformanceData, String> colBranchName;
    @FXML private TableColumn<PerformanceData, Integer> colTotalOrders;
    @FXML private TableColumn<PerformanceData, Integer> colTotalRevenue;
    @FXML private TableColumn<PerformanceData, Double> colAvgOrderValue;
    @FXML private TableColumn<PerformanceData, Integer> colPerformanceScore;
    @FXML private TableColumn<PerformanceData, Double> colGrowthRate;
    @FXML private TableColumn<PerformanceData, String> colStatus;
    
    // Promotion tab
    @FXML private ComboBox<String> promotionType;
    @FXML private ComboBox<String> promotionBranch;
    @FXML private TableView<PromotionData> promotionTable;
    @FXML private TableColumn<PromotionData, Integer> colPromotionId;
    @FXML private TableColumn<PromotionData, String> colPromotionName;
    @FXML private TableColumn<PromotionData, String> colPromotionType;
    @FXML private TableColumn<PromotionData, Integer> colDiscountPercent;
    @FXML private TableColumn<PromotionData, Integer> colMinPurchase;
    @FXML private TableColumn<PromotionData, String> colStartDate;
    @FXML private TableColumn<PromotionData, String> colEndDate;
    @FXML private TableColumn<PromotionData, String> colPromotionStatus;
    
    // Branch management tab
    @FXML private TableView<BranchData> branchTable;
    @FXML private TableColumn<BranchData, Integer> colBranchId;
    @FXML private TableColumn<BranchData, String> colBranchLocation;
    @FXML private TableColumn<BranchData, Integer> colBranchPerformance;
    @FXML private TableColumn<BranchData, Integer> colBranchIncome;
    @FXML private TableColumn<BranchData, Integer> colBranchStaff;
    
    // Staff management tab
    @FXML private ComboBox<String> staffBranchFilter;
    @FXML private TableView<StaffData> staffTable;
    @FXML private TableColumn<StaffData, Integer> colStaffId;
    @FXML private TableColumn<StaffData, String> colStaffName;
    @FXML private TableColumn<StaffData, String> colStaffJob;
    @FXML private TableColumn<StaffData, String> colStaffBranch;
    
    // Report tab
    @FXML private DatePicker reportStartDate;
    @FXML private DatePicker reportEndDate;
    @FXML private ComboBox<String> reportType;
    @FXML private TextArea overallReportArea;
    
    private final ObservableList<PerformanceData> performanceItems = FXCollections.observableArrayList();
    private final ObservableList<PromotionData> promotionItems = FXCollections.observableArrayList();
    private final ObservableList<BranchData> branchItems = FXCollections.observableArrayList();
    private final ObservableList<StaffData> staffItems = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        setupPerformanceTable();
        setupPromotionTable();
        setupBranchTable();
        setupStaffTable();
        loadPromotionTypes();
        loadReportTypes();
        
        // Set default dates
        performanceStartDate.setValue(LocalDate.now().minusDays(30));
        performanceEndDate.setValue(LocalDate.now());
        reportStartDate.setValue(LocalDate.now().minusDays(30));
        reportEndDate.setValue(LocalDate.now());
        
        // Load initial data
        onRefreshPerformance();
        onRefreshPromotions();
        onRefreshBranches();
        onRefreshStaff();
        
        showInfo("Info", "Admin Pusat Dashboard berhasil dimuat!");
    }
    
    private void setupPerformanceTable() {
        colBranchName.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        colTotalOrders.setCellValueFactory(new PropertyValueFactory<>("totalOrders"));
        colTotalRevenue.setCellValueFactory(new PropertyValueFactory<>("totalRevenue"));
        colAvgOrderValue.setCellValueFactory(new PropertyValueFactory<>("avgOrderValue"));
        colPerformanceScore.setCellValueFactory(new PropertyValueFactory<>("performanceScore"));
        colGrowthRate.setCellValueFactory(new PropertyValueFactory<>("growthRate"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        performanceTable.setItems(performanceItems);
    }
    
    private void setupPromotionTable() {
        colPromotionId.setCellValueFactory(new PropertyValueFactory<>("promotionId"));
        colPromotionName.setCellValueFactory(new PropertyValueFactory<>("promotionName"));
        colPromotionType.setCellValueFactory(new PropertyValueFactory<>("promotionType"));
        colDiscountPercent.setCellValueFactory(new PropertyValueFactory<>("discountPercent"));
        colMinPurchase.setCellValueFactory(new PropertyValueFactory<>("minPurchase"));
        colStartDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        colPromotionStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        promotionTable.setItems(promotionItems);
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
    
    private void loadPromotionTypes() {
        promotionType.getItems().addAll("Diskon Persentase", "Diskon Nominal", "Buy 1 Get 1", "Free Delivery");
        promotionType.setValue("Diskon Persentase");
        
        // Load branch data for promotion
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = "SELECT lokasi FROM cabang ORDER BY lokasi";
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                promotionBranch.getItems().add(rs.getString("lokasi"));
            }
            
            if (!promotionBranch.getItems().isEmpty()) {
                promotionBranch.setValue("Semua Cabang");
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data cabang: " + e.getMessage());
        }
    }
    
    private void loadReportTypes() {
        reportType.getItems().addAll("Laporan Penjualan", "Laporan Performa Cabang", "Laporan Staff", "Laporan Keuangan");
        reportType.setValue("Laporan Penjualan");
    }
    
    // Performance Management
    @FXML
    void onAnalyzeBranchPerformance() {
        LocalDate start = performanceStartDate.getValue();
        LocalDate end = performanceEndDate.getValue();
        
        if (start == null || end == null) {
            showError("Error", "Pilih periode analisis");
            return;
        }
        
        if (start.isAfter(end)) {
            showError("Error", "Tanggal mulai tidak boleh setelah tanggal akhir");
            return;
        }
        
        analyzeBranchPerformance(start, end);
    }
    
    @FXML
    void onRefreshPerformance() {
        LocalDate start = performanceStartDate.getValue();
        LocalDate end = performanceEndDate.getValue();
        if (start != null && end != null) {
            analyzeBranchPerformance(start, end);
        }
    }
    
    private void analyzeBranchPerformance(LocalDate start, LocalDate end) {
        performanceItems.clear();
        
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
                SELECT 
                    c.lokasi as branch_name,
                    COUNT(p.pesanan_id) as total_orders,
                    SUM(COALESCE(dm.harga, 0)) as total_revenue,
                    AVG(COALESCE(dm.harga, 0)) as avg_order_value,
                    pc.tingkat_performa as performance_score,
                    CASE 
                        WHEN COUNT(p.pesanan_id) > 10 THEN 'Excellent'
                        WHEN COUNT(p.pesanan_id) > 5 THEN 'Good'
                        ELSE 'Needs Improvement'
                    END as status
                FROM cabang c
                LEFT JOIN performa_cabang pc ON c.id_performa_cabang = pc.id_performa_cabang
                LEFT JOIN daftar_menu dm ON c.cabang_id = dm.cabang_id
                LEFT JOIN pesanan_menu pm ON dm.id_menu = pm.id_menu
                LEFT JOIN pemesanan p ON pm.pesanan_id = p.pesanan_id
                WHERE p.tanggal_pesanan BETWEEN ? AND ? OR p.tanggal_pesanan IS NULL
                GROUP BY c.cabang_id, c.lokasi, pc.tingkat_performa
                ORDER BY total_revenue DESC
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2, Date.valueOf(end));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                performanceItems.add(new PerformanceData(
                    rs.getString("branch_name"),
                    rs.getInt("total_orders"),
                    rs.getInt("total_revenue"),
                    rs.getDouble("avg_order_value"),
                    rs.getInt("performance_score"),
                    0.0, // Growth rate placeholder
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal menganalisis performa: " + e.getMessage());
        }
    }
    
    @FXML
    void onViewPerformanceDetail() {
        PerformanceData selected = performanceTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih cabang yang akan dilihat detailnya");
            return;
        }
        showInfo("Info", "Detail performa untuk " + selected.getBranchName() + " akan diimplementasikan");
    }
    
    @FXML
    void onExportPerformanceReport() {
        showInfo("Info", "Fitur export laporan performa akan diimplementasikan");
    }
    
    @FXML
    void onShowPerformanceChart() {
        showInfo("Info", "Fitur grafik performa akan diimplementasikan");
    }
    
    // Promotion Management
    @FXML
    void onAddPromotion() {
        showInfo("Info", "Fitur tambah promosi akan diimplementasikan");
    }
    
    @FXML
    void onRefreshPromotions() {
        loadPromotionData();
    }
    
    private void loadPromotionData() {
        promotionItems.clear();
        
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = """
                SELECT id_promosi, nama_promosi, jenis_promosi, persentase_diskon,
                       minimal_pembelian, tanggal_mulai, tanggal_berakhir, status
                FROM promosi
                ORDER BY tanggal_mulai DESC
                """;
            
            PreparedStatement stmt = c.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                promotionItems.add(new PromotionData(
                    rs.getInt("id_promosi"),
                    rs.getString("nama_promosi"),
                    rs.getString("jenis_promosi"),
                    rs.getInt("persentase_diskon"),
                    rs.getInt("minimal_pembelian"),
                    rs.getDate("tanggal_mulai").toString(),
                    rs.getDate("tanggal_berakhir").toString(),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal memuat data promosi: " + e.getMessage());
        }
    }
    
    @FXML
    void onEditPromotion() {
        PromotionData selected = promotionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih promosi yang akan diedit");
            return;
        }
        showInfo("Info", "Fitur edit promosi akan diimplementasikan");
    }
    
    @FXML
    void onDeletePromotion() {
        PromotionData selected = promotionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih promosi yang akan dihapus");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin menghapus promosi '" + selected.getPromotionName() + "'?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                deletePromotion(selected.getPromotionId());
            }
        });
    }
    
    private void deletePromotion(int promotionId) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = "DELETE FROM promosi WHERE id_promosi = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setInt(1, promotionId);
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                showInfo("Sukses", "Promosi berhasil dihapus");
                onRefreshPromotions();
            } else {
                showError("Error", "Gagal menghapus promosi");
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal menghapus promosi: " + e.getMessage());
        }
    }
    
    @FXML
    void onTogglePromotion() {
        PromotionData selected = promotionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Pilih promosi yang akan diaktifkan/nonaktifkan");
            return;
        }
        
        String newStatus = "Aktif".equals(selected.getStatus()) ? "Nonaktif" : "Aktif";
        togglePromotionStatus(selected.getPromotionId(), newStatus);
    }
    
    private void togglePromotionStatus(int promotionId, String newStatus) {
        try (Connection c = DataSourceManager.getUserConnection()) {
            String sql = "UPDATE promosi SET status = ? WHERE id_promosi = ?";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.setString(1, newStatus);
            stmt.setInt(2, promotionId);
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                showInfo("Sukses", "Status promosi berhasil diubah menjadi '" + newStatus + "'");
                onRefreshPromotions();
            } else {
                showError("Error", "Gagal mengubah status promosi");
            }
        } catch (SQLException e) {
            showError("Database Error", "Gagal mengubah status promosi: " + e.getMessage());
        }
    }
    
    // Branch Management
    @FXML
    void onAddBranch() {
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
        showInfo("Info", "Fitur lihat detail cabang akan diimplementasikan");
    }
    
    // Staff Management
    @FXML
    void onAddStaff() {
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
    
    // Report Generation
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
        showInfo("Info", "Fitur export PDF akan diimplementasikan");
    }
    
    @FXML
    void onExportOverallExcel() {
        showInfo("Info", "Fitur export Excel akan diimplementasikan");
    }
    
    @FXML
    void onSendEmailReport() {
        showInfo("Info", "Fitur kirim email akan diimplementasikan");
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
    public static class PerformanceData {
        private final String branchName;
        private final int totalOrders;
        private final int totalRevenue;
        private final double avgOrderValue;
        private final int performanceScore;
        private final double growthRate;
        private final String status;
        
        public PerformanceData(String branchName, int totalOrders, int totalRevenue, double avgOrderValue, int performanceScore, double growthRate, String status) {
            this.branchName = branchName;
            this.totalOrders = totalOrders;
            this.totalRevenue = totalRevenue;
            this.avgOrderValue = avgOrderValue;
            this.performanceScore = performanceScore;
            this.growthRate = growthRate;
            this.status = status;
        }
        
        public String getBranchName() { return branchName; }
        public int getTotalOrders() { return totalOrders; }
        public int getTotalRevenue() { return totalRevenue; }
        public double getAvgOrderValue() { return avgOrderValue; }
        public int getPerformanceScore() { return performanceScore; }
        public double getGrowthRate() { return growthRate; }
        public String getStatus() { return status; }
    }
    
    public static class PromotionData {
        private final int promotionId;
        private final String promotionName;
        private final String promotionType;
        private final int discountPercent;
        private final int minPurchase;
        private final String startDate;
        private final String endDate;
        private final String status;
        
        public PromotionData(int promotionId, String promotionName, String promotionType, int discountPercent, int minPurchase, String startDate, String endDate, String status) {
            this.promotionId = promotionId;
            this.promotionName = promotionName;
            this.promotionType = promotionType;
            this.discountPercent = discountPercent;
            this.minPurchase = minPurchase;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = status;
        }
        
        public int getPromotionId() { return promotionId; }
        public String getPromotionName() { return promotionName; }
        public String getPromotionType() { return promotionType; }
        public int getDiscountPercent() { return discountPercent; }
        public int getMinPurchase() { return minPurchase; }
        public String getStartDate() { return startDate; }
        public String getEndDate() { return endDate; }
        public String getStatus() { return status; }
    }
    
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