package com.example.bdsqltester.scenes.admin;

import com.example.bdsqltester.HelloApplication;
import com.example.bdsqltester.dtos.Menu;
import com.example.bdsqltester.dtos.OrderHistoryItem;
import com.example.bdsqltester.dtos.DeliverySchedule;
import com.example.bdsqltester.datasources.DataSourceManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class BranchAdminController {

    // Menu Management
    @FXML private TableView<Menu> menuTableView;
    @FXML private TableColumn<Menu, Integer> idColumn;
    @FXML private TableColumn<Menu, String> namaColumn;
    @FXML private TableColumn<Menu, String> jenisColumn;
    @FXML private TableColumn<Menu, Integer> hargaColumn;
    @FXML private TableColumn<Menu, String> penjualColumn;
    @FXML private TableColumn<Menu, String> cabangColumn;
    @FXML private TableColumn<Menu, Void> actionColumn;
    @FXML private ComboBox<String> filterComboBox;
    
    // Order Management
    @FXML private TableView<OrderHistoryItem> orderTableView;
    @FXML private TableColumn<OrderHistoryItem, Integer> orderIdColumn;
    @FXML private TableColumn<OrderHistoryItem, String> customerColumn;
    @FXML private TableColumn<OrderHistoryItem, LocalDate> orderDateColumn;
    @FXML private TableColumn<OrderHistoryItem, String> orderStatusColumn;
    @FXML private TableColumn<OrderHistoryItem, String> deliveryStatusColumn;
    @FXML private TableColumn<OrderHistoryItem, LocalDate> deliveryDateColumn;
    @FXML private TableColumn<OrderHistoryItem, Void> orderActionColumn;
    @FXML private ComboBox<String> orderStatusFilterComboBox;
    
    private ObservableList<Menu> menuList = FXCollections.observableArrayList();
    private FilteredList<Menu> filteredMenuList;
    
    private ObservableList<OrderHistoryItem> orderList = FXCollections.observableArrayList();
    private FilteredList<OrderHistoryItem> filteredOrderList;
    
    @FXML
    public void initialize() {
        setupMenuManagement();
        setupOrderManagement();
    }
    
    private void setupMenuManagement() {
        setupTableColumns();
        setupFilterComboBox();
        loadMenuData();
        setupActionColumn();
    }
    
    private void setupOrderManagement() {
        setupOrderTableColumns();
        setupOrderFilterComboBox();
        loadOrderData();
        setupOrderActionColumn();
    }
    
    private void setupTableColumns() {
        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        namaColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNama()));
        jenisColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJenis()));
        hargaColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getHarga()).asObject());
        penjualColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaPenjual()));
        cabangColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaCabang()));
        
        // Format harga column
        hargaColumn.setCellFactory(column -> new TableCell<Menu, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Rp " + String.format("%,d", item));
                }
            }
        });
    }
    
    private void setupFilterComboBox() {
        filterComboBox.getItems().addAll("Semua Jenis", "Makanan Utama", "Minuman", "Snack", "Dessert");
        filterComboBox.setValue("Semua Jenis");
        
        filteredMenuList = new FilteredList<>(menuList, p -> true);
        menuTableView.setItems(filteredMenuList);
    }
    
    private void setupActionColumn() {
        actionColumn.setCellFactory(new Callback<TableColumn<Menu, Void>, TableCell<Menu, Void>>() {
            @Override
            public TableCell<Menu, Void> call(TableColumn<Menu, Void> param) {
                return new TableCell<Menu, Void>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Hapus");
                    private final HBox buttonBox = new HBox(5, editButton, deleteButton);
                    
                    {
                        editButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                        deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                        
                        editButton.setOnAction(event -> {
                            Menu menu = getTableView().getItems().get(getIndex());
                            showEditMenuDialog(menu);
                        });
                        
                        deleteButton.setOnAction(event -> {
                            Menu menu = getTableView().getItems().get(getIndex());
                            deleteMenu(menu);
                        });
                    }
                    
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : buttonBox);
                    }
                };
            }
        });
    }
    
    @FXML
    void loadMenuData() {
        menuList.clear();
        String sql = """
            SELECT dm.id_menu, dm.nama_menu, dm.jenis, dm.id_penjual, dm.harga, dm.cabang_id,
                   ps.nama_penjual, c.lokasi as nama_cabang
            FROM daftar_menu dm
            LEFT JOIN penjual_sampingan ps ON dm.id_penjual = ps.id_penjual
            LEFT JOIN cabang c ON dm.cabang_id = c.cabang_id
            ORDER BY dm.id_menu
            """;
        
        try (Connection conn = DataSourceManager.getUserConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Menu menu = new Menu(
                    rs.getInt("id_menu"),
                    rs.getString("nama_menu"),
                    rs.getString("jenis"),
                    rs.getInt("id_penjual"),
                    rs.getString("nama_penjual"),
                    rs.getInt("harga"),
                    rs.getInt("cabang_id"),
                    rs.getString("nama_cabang")
                );
                menuList.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error", "Gagal memuat data menu: " + e.getMessage());
        }
    }
    
    @FXML
    void refreshMenuData() {
        loadMenuData();
        showInfo("Sukses", "Data menu berhasil diperbarui!");
    }
    
    @FXML
    void filterMenuData() {
        String selectedFilter = filterComboBox.getValue();
        if ("Semua Jenis".equals(selectedFilter)) {
            filteredMenuList.setPredicate(menu -> true);
        } else {
            filteredMenuList.setPredicate(menu -> selectedFilter.equals(menu.getJenis()));
        }
    }
    
    @FXML
    void showAddMenuDialog() {
        Dialog<Menu> dialog = new Dialog<>();
        dialog.setTitle("Tambah Menu Baru");
        dialog.setHeaderText("Masukkan detail menu baru");
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        // Set up dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        TextField namaField = new TextField();
        namaField.setPromptText("Nama Menu");
        ComboBox<String> jenisCombo = new ComboBox<>();
        jenisCombo.getItems().addAll("Makanan Utama", "Minuman", "Snack", "Dessert");
        TextField hargaField = new TextField();
        hargaField.setPromptText("Harga");
        ComboBox<String> penjualCombo = new ComboBox<>();
        ComboBox<String> cabangCombo = new ComboBox<>();
        
        // Load penjual and cabang data
        loadPenjualData(penjualCombo);
        loadCabangData(cabangCombo);
        
        grid.add(new Label("Nama Menu:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Jenis:"), 0, 1);
        grid.add(jenisCombo, 1, 1);
        grid.add(new Label("Harga:"), 0, 2);
        grid.add(hargaField, 1, 2);
        grid.add(new Label("Penjual:"), 0, 3);
        grid.add(penjualCombo, 1, 3);
        grid.add(new Label("Cabang:"), 0, 4);
        grid.add(cabangCombo, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String nama = namaField.getText();
                    String jenis = jenisCombo.getValue();
                    int harga = Integer.parseInt(hargaField.getText());
                    String penjualInfo = penjualCombo.getValue();
                    String cabangInfo = cabangCombo.getValue();
                    
                    if (nama.isEmpty() || jenis == null || penjualInfo == null || cabangInfo == null) {
                        showError("Error", "Semua field harus diisi!");
                        return null;
                    }
                    
                    int idPenjual = Integer.parseInt(penjualInfo.split(" - ")[0]);
                    int idCabang = Integer.parseInt(cabangInfo.split(" - ")[0]);
                    
                    return new Menu(0, nama, jenis, idPenjual, "", harga, idCabang, "");
                } catch (NumberFormatException e) {
                    showError("Error", "Harga harus berupa angka!");
                    return null;
                }
            }
            return null;
        });
        
        Optional<Menu> result = dialog.showAndWait();
        result.ifPresent(this::addMenu);
    }
    
    private void showEditMenuDialog(Menu menu) {
        Dialog<Menu> dialog = new Dialog<>();
        dialog.setTitle("Edit Menu");
        dialog.setHeaderText("Edit detail menu: " + menu.getNama());
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        TextField namaField = new TextField(menu.getNama());
        ComboBox<String> jenisCombo = new ComboBox<>();
        jenisCombo.getItems().addAll("Makanan Utama", "Minuman", "Snack", "Dessert");
        jenisCombo.setValue(menu.getJenis());
        TextField hargaField = new TextField(String.valueOf(menu.getHarga()));
        ComboBox<String> penjualCombo = new ComboBox<>();
        ComboBox<String> cabangCombo = new ComboBox<>();
        
        loadPenjualData(penjualCombo);
        loadCabangData(cabangCombo);
        penjualCombo.setValue(menu.getIdPenjual() + " - " + menu.getNamaPenjual());
        cabangCombo.setValue(menu.getCabangId() + " - " + menu.getNamaCabang());
        
        grid.add(new Label("Nama Menu:"), 0, 0);
        grid.add(namaField, 1, 0);
        grid.add(new Label("Jenis:"), 0, 1);
        grid.add(jenisCombo, 1, 1);
        grid.add(new Label("Harga:"), 0, 2);
        grid.add(hargaField, 1, 2);
        grid.add(new Label("Penjual:"), 0, 3);
        grid.add(penjualCombo, 1, 3);
        grid.add(new Label("Cabang:"), 0, 4);
        grid.add(cabangCombo, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String nama = namaField.getText();
                    String jenis = jenisCombo.getValue();
                    int harga = Integer.parseInt(hargaField.getText());
                    String penjualInfo = penjualCombo.getValue();
                    String cabangInfo = cabangCombo.getValue();
                    
                    if (nama.isEmpty() || jenis == null || penjualInfo == null || cabangInfo == null) {
                        showError("Error", "Semua field harus diisi!");
                        return null;
                    }
                    
                    int idPenjual = Integer.parseInt(penjualInfo.split(" - ")[0]);
                    int idCabang = Integer.parseInt(cabangInfo.split(" - ")[0]);
                    
                    Menu updatedMenu = new Menu(menu.getId(), nama, jenis, idPenjual, "", harga, idCabang, "");
                    return updatedMenu;
                } catch (NumberFormatException e) {
                    showError("Error", "Harga harus berupa angka!");
                    return null;
                }
            }
            return null;
        });
        
        Optional<Menu> result = dialog.showAndWait();
        result.ifPresent(this::updateMenu);
    }
    
    private void loadPenjualData(ComboBox<String> comboBox) {
        String sql = "SELECT id_penjual, nama_penjual FROM penjual_sampingan ORDER BY nama_penjual";
        try (Connection conn = DataSourceManager.getUserConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String item = rs.getInt("id_penjual") + " - " + rs.getString("nama_penjual");
                comboBox.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadCabangData(ComboBox<String> comboBox) {
        String sql = "SELECT cabang_id, lokasi FROM cabang ORDER BY lokasi";
        try (Connection conn = DataSourceManager.getUserConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                String item = rs.getInt("cabang_id") + " - " + rs.getString("lokasi");
                comboBox.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void addMenu(Menu menu) {
        String sql = "INSERT INTO daftar_menu (nama_menu, jenis, id_penjual, harga, cabang_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DataSourceManager.getUserConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, menu.getNama());
            stmt.setString(2, menu.getJenis());
            stmt.setInt(3, menu.getIdPenjual());
            stmt.setInt(4, menu.getHarga());
            stmt.setInt(5, menu.getCabangId());
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                showInfo("Sukses", "Menu berhasil ditambahkan!");
                loadMenuData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error", "Gagal menambahkan menu: " + e.getMessage());
        }
    }
    
    private void updateMenu(Menu menu) {
        String sql = "UPDATE daftar_menu SET nama_menu = ?, jenis = ?, id_penjual = ?, harga = ?, cabang_id = ? WHERE id_menu = ?";
        try (Connection conn = DataSourceManager.getUserConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, menu.getNama());
            stmt.setString(2, menu.getJenis());
            stmt.setInt(3, menu.getIdPenjual());
            stmt.setInt(4, menu.getHarga());
            stmt.setInt(5, menu.getCabangId());
            stmt.setInt(6, menu.getId());
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                showInfo("Sukses", "Menu berhasil diperbarui!");
                loadMenuData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error", "Gagal memperbarui menu: " + e.getMessage());
        }
    }
    
    private void deleteMenu(Menu menu) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Menu");
        alert.setContentText("Apakah Anda yakin ingin menghapus menu '" + menu.getNama() + "'?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sql = "DELETE FROM daftar_menu WHERE id_menu = ?";
            try (Connection conn = DataSourceManager.getUserConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setInt(1, menu.getId());
                int deleteResult = stmt.executeUpdate();
                
                if (deleteResult > 0) {
                    showInfo("Sukses", "Menu berhasil dihapus!");
                    loadMenuData();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showError("Error", "Gagal menghapus menu: " + e.getMessage());
            }
        }
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
    
    private void setupOrderTableColumns() {
        orderIdColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getPesananId()).asObject());
        customerColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCustomerName()));
        orderDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTanggalPesanan()));
        orderStatusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));
        deliveryStatusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatusPengiriman()));
        deliveryDateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getJadwalKirim()));
    }
    
    private void setupOrderFilterComboBox() {
        orderStatusFilterComboBox.getItems().addAll("Semua Status", "Menunggu", "Diproses", "Siap", "Selesai");
        orderStatusFilterComboBox.setValue("Semua Status");
        
        filteredOrderList = new FilteredList<>(orderList, p -> true);
        orderTableView.setItems(filteredOrderList);
    }
    
    private void setupOrderActionColumn() {
        orderActionColumn.setCellFactory(new Callback<TableColumn<OrderHistoryItem, Void>, TableCell<OrderHistoryItem, Void>>() {
            @Override
            public TableCell<OrderHistoryItem, Void> call(TableColumn<OrderHistoryItem, Void> param) {
                return new TableCell<OrderHistoryItem, Void>() {
                    private final Button viewDetailsButton = new Button("Lihat Detail");
                    private final Button updateStatusButton = new Button("Update Status");
                    private final Button scheduleDeliveryButton = new Button("Jadwalkan Kirim");
                    private final HBox buttonBox = new HBox(5, viewDetailsButton, updateStatusButton, scheduleDeliveryButton);
                    
                    {
                        viewDetailsButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
                        updateStatusButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                        scheduleDeliveryButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                        
                        viewDetailsButton.setOnAction(event -> {
                            OrderHistoryItem order = getTableView().getItems().get(getIndex());
                            showOrderDetailsDialog(order);
                        });
                        
                        updateStatusButton.setOnAction(event -> {
                            OrderHistoryItem order = getTableView().getItems().get(getIndex());
                            showUpdateOrderStatusDialog(order);
                        });
                        
                        scheduleDeliveryButton.setOnAction(event -> {
                            OrderHistoryItem order = getTableView().getItems().get(getIndex());
                            showScheduleDeliveryDialog(order);
                        });
                    }
                    
                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : buttonBox);
                    }
                };
            }
        });
    }
    
    @FXML
    void loadOrderData() {
        orderList.clear();
        String sql = """
            SELECT p.pesanan_id, u.username as customer_name, u.email as customer_email,
                   p.tanggal_pesanan, p.status, p.user_id,
                   COALESCE(pg.status_pengiriman, 'Belum Dijadwalkan') as status_pengiriman,
                   pg.jadwal_kirim, pg.estimasi_sampai, p.pengiriman_id,
                   COALESCE(SUM(dm.harga), 0) as total_harga
            FROM pemesanan p
            JOIN users u ON p.user_id = u.user_id
            LEFT JOIN pengiriman pg ON p.pengiriman_id = pg.pengiriman_id
            LEFT JOIN pesanan_menu pm ON p.pesanan_id = pm.pesanan_id
            LEFT JOIN daftar_menu dm ON pm.id_menu = dm.id_menu
            GROUP BY p.pesanan_id, u.username, u.email, p.tanggal_pesanan, p.status, p.user_id,
                     pg.status_pengiriman, pg.jadwal_kirim, pg.estimasi_sampai, p.pengiriman_id
            ORDER BY p.tanggal_pesanan DESC
            """;
        
        try (Connection conn = DataSourceManager.getUserConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                OrderHistoryItem order = new OrderHistoryItem(
                    rs.getInt("pesanan_id"),
                    rs.getString("customer_name"),
                    rs.getString("customer_email"),
                    rs.getDate("tanggal_pesanan") != null ? rs.getDate("tanggal_pesanan").toLocalDate() : null,
                    rs.getString("status"),
                    rs.getInt("total_harga"),
                    rs.getInt("user_id"),
                    rs.getString("status_pengiriman"),
                    rs.getDate("jadwal_kirim") != null ? rs.getDate("jadwal_kirim").toLocalDate() : null,
                    rs.getDate("estimasi_sampai") != null ? rs.getDate("estimasi_sampai").toLocalDate() : null,
                    rs.getInt("pengiriman_id")
                );
                orderList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error", "Gagal memuat data pesanan: " + e.getMessage());
        }
    }
    
    @FXML
    void refreshOrderData() {
        loadOrderData();
        showInfo("Sukses", "Data pesanan berhasil diperbarui!");
    }
    
    @FXML
    void filterOrderData() {
        String selectedFilter = orderStatusFilterComboBox.getValue();
        if ("Semua Status".equals(selectedFilter)) {
            filteredOrderList.setPredicate(order -> true);
        } else {
            filteredOrderList.setPredicate(order -> selectedFilter.equals(order.getStatus()));
        }
    }
    
    @FXML
    void showScheduleDeliveryDialog() {
        // Show dialog to select order for delivery scheduling
        Dialog<OrderHistoryItem> dialog = new Dialog<>();
        dialog.setTitle("Jadwalkan Pengiriman");
        dialog.setHeaderText("Pilih pesanan untuk dijadwalkan pengirimannya");
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        // Filter orders that are ready for delivery
        ObservableList<OrderHistoryItem> readyOrders = orderList.filtered(order -> 
            "Siap".equals(order.getStatus()) && order.getPengirimanId() == 0);
        
        if (readyOrders.isEmpty()) {
            showInfo("Info", "Tidak ada pesanan yang siap untuk dijadwalkan pengirimannya.");
            return;
        }
        
        ComboBox<OrderHistoryItem> orderComboBox = new ComboBox<>(readyOrders);
        orderComboBox.setCellFactory(param -> new ListCell<OrderHistoryItem>() {
            @Override
            protected void updateItem(OrderHistoryItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("Pesanan #" + item.getPesananId() + " - " + item.getCustomerName());
                }
            }
        });
        orderComboBox.setButtonCell(orderComboBox.getCellFactory().call(null));
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        grid.add(new Label("Pilih Pesanan:"), 0, 0);
        grid.add(orderComboBox, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType scheduleButtonType = new ButtonType("Jadwalkan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(scheduleButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == scheduleButtonType) {
                return orderComboBox.getValue();
            }
            return null;
        });
        
        Optional<OrderHistoryItem> result = dialog.showAndWait();
        result.ifPresent(this::showScheduleDeliveryDialog);
    }
    
    private void showScheduleDeliveryDialog(OrderHistoryItem order) {
        Dialog<DeliverySchedule> dialog = new Dialog<>();
        dialog.setTitle("Jadwalkan Pengiriman");
        dialog.setHeaderText("Jadwalkan pengiriman untuk Pesanan #" + order.getPesananId());
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        DatePicker jadwalKirimPicker = new DatePicker();
        jadwalKirimPicker.setValue(LocalDate.now().plusDays(1));
        
        DatePicker estimasiSampaiPicker = new DatePicker();
        estimasiSampaiPicker.setValue(LocalDate.now().plusDays(2));
        
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Menunggu", "Dijadwalkan", "Dalam Pengiriman", "Selesai");
        statusComboBox.setValue("Dijadwalkan");
        
        grid.add(new Label("Customer:"), 0, 0);
        grid.add(new Label(order.getCustomerName()), 1, 0);
        grid.add(new Label("Jadwal Kirim:"), 0, 1);
        grid.add(jadwalKirimPicker, 1, 1);
        grid.add(new Label("Estimasi Sampai:"), 0, 2);
        grid.add(estimasiSampaiPicker, 1, 2);
        grid.add(new Label("Status:"), 0, 3);
        grid.add(statusComboBox, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                LocalDate jadwalKirim = jadwalKirimPicker.getValue();
                LocalDate estimasiSampai = estimasiSampaiPicker.getValue();
                String status = statusComboBox.getValue();
                
                if (jadwalKirim == null || estimasiSampai == null || status == null) {
                    showError("Error", "Semua field harus diisi!");
                    return null;
                }
                
                if (estimasiSampai.isBefore(jadwalKirim)) {
                    showError("Error", "Estimasi sampai tidak boleh sebelum jadwal kirim!");
                    return null;
                }
                
                return new DeliverySchedule(0, order.getPesananId(), order.getCustomerName(), 
                                          order.getCustomerEmail(), status, jadwalKirim, estimasiSampai, order.getUserId());
            }
            return null;
        });
        
        Optional<DeliverySchedule> result = dialog.showAndWait();
        result.ifPresent(this::addDeliverySchedule);
    }
    
    private void addDeliverySchedule(DeliverySchedule delivery) {
        // First, create new pengiriman record
        String insertPengirimanSql = """
            INSERT INTO pengiriman (status_pengiriman, jadwal_kirim, estimasi_sampai) 
            VALUES (?, ?, ?)
            """;
        
        try (Connection conn = DataSourceManager.getUserConnection();
             PreparedStatement stmt = conn.prepareStatement(insertPengirimanSql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, delivery.getStatusPengiriman());
            stmt.setDate(2, java.sql.Date.valueOf(delivery.getJadwalKirim()));
            stmt.setDate(3, java.sql.Date.valueOf(delivery.getEstimasiSampai()));
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                // Get the generated pengiriman_id
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int pengirimanId = rs.getInt(1);
                    
                    // Update pemesanan to link with pengiriman
                    String updatePemesananSql = "UPDATE pemesanan SET pengiriman_id = ? WHERE pesanan_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updatePemesananSql)) {
                        updateStmt.setInt(1, pengirimanId);
                        updateStmt.setInt(2, delivery.getPesananId());
                        
                        int updateResult = updateStmt.executeUpdate();
                        if (updateResult > 0) {
                            showInfo("Sukses", "Jadwal pengiriman berhasil ditambahkan!");
                            loadOrderData(); // Refresh order data
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error", "Gagal menambahkan jadwal pengiriman: " + e.getMessage());
        }
    }
    
    private void showOrderDetailsDialog(OrderHistoryItem order) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detail Pesanan");
        dialog.setHeaderText("Detail pesanan #" + order.getPesananId());
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        grid.add(new Label("ID Pesanan:"), 0, 0);
        grid.add(new Label(String.valueOf(order.getPesananId())), 1, 0);
        
        grid.add(new Label("Customer:"), 0, 1);
        grid.add(new Label(order.getCustomerName()), 1, 1);
        
        grid.add(new Label("Email:"), 0, 2);
        grid.add(new Label(order.getCustomerEmail()), 1, 2);
        
        grid.add(new Label("Tanggal Pesanan:"), 0, 3);
        grid.add(new Label(order.getTanggalPesanan().toString()), 1, 3);
        
        grid.add(new Label("Status:"), 0, 4);
        grid.add(new Label(order.getStatus()), 1, 4);
        
        grid.add(new Label("Status Pengiriman:"), 0, 5);
        grid.add(new Label(order.getStatusPengiriman() != null ? order.getStatusPengiriman() : "N/A"), 1, 5);
        
        grid.add(new Label("Jadwal Kirim:"), 0, 6);
        grid.add(new Label(order.getJadwalKirim() != null ? order.getJadwalKirim().toString() : "N/A"), 1, 6);
        
        grid.add(new Label("Estimasi Sampai:"), 0, 7);
        grid.add(new Label(order.getEstimasiSampai() != null ? order.getEstimasiSampai().toString() : "N/A"), 1, 7);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType closeButtonType = new ButtonType("Tutup", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(closeButtonType);
        
        dialog.showAndWait();
    }
    
    private void showUpdateOrderStatusDialog(OrderHistoryItem order) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update Status Pesanan");
        dialog.setHeaderText("Update status untuk pesanan #" + order.getPesananId());
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Pending", "Diproses", "Siap", "Dikirim", "Selesai");
        statusComboBox.setValue(order.getStatus());
        
        grid.add(new Label("Status Baru:"), 0, 0);
        grid.add(statusComboBox, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return statusComboBox.getValue();
            }
            return null;
        });
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newStatus -> updateOrderStatus(order.getPesananId(), newStatus));
    }
    
    private void updateOrderStatus(int pesananId, String newStatus) {
        String sql = "UPDATE pemesanan SET status = ? WHERE pesanan_id = ?";
        try (Connection conn = DataSourceManager.getUserConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus);
            stmt.setInt(2, pesananId);
            
            int result = stmt.executeUpdate();
            if (result > 0) {
                showInfo("Sukses", "Status pesanan berhasil diperbarui!");
                loadOrderData(); // Refresh data
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error", "Gagal memperbarui status pesanan: " + e.getMessage());
        }
    }
} 