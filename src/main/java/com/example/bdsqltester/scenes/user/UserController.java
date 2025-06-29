package com.example.bdsqltester.scenes.user;

import com.example.bdsqltester.HelloApplication;
import com.example.bdsqltester.datasources.DataSourceManager;
import com.example.bdsqltester.dtos.CartItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;

public class UserController {

    @FXML private ComboBox<String> cabangFilter;
    @FXML private ComboBox<String> kategoriFilter;
    @FXML private TableView<MenuItem> menuTable;
    @FXML private TableColumn<MenuItem, String> colMenuName;
    @FXML private TableColumn<MenuItem, String> colJenis;
    @FXML private TableColumn<MenuItem, Integer> colHarga;
    @FXML private TableColumn<MenuItem, String> colLokasi;
    @FXML private TableColumn<MenuItem, String> colDiskon;
    @FXML private TableColumn<MenuItem, Void> colAction;
    
    // Cart components
    @FXML private TableView<CartItem> cartTable;
    @FXML private TableColumn<CartItem, String> colCartMenu;
    @FXML private TableColumn<CartItem, String> colCartJenis;
    @FXML private TableColumn<CartItem, Integer> colCartHarga;
    @FXML private TableColumn<CartItem, Integer> colCartQuantity;
    @FXML private TableColumn<CartItem, Integer> colCartTotal;
    @FXML private TableColumn<CartItem, Void> colCartAction;
    @FXML private Label totalLabel;
    @FXML private DatePicker deliveryDatePicker;

    private final ObservableList<MenuItem> menuItems = FXCollections.observableArrayList();
    private final ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupMenuTable();
        setupCartTable();
        setupDatePicker();
        loadFilters();
        loadMenu(null, null);
        updateTotalLabel();
    }

    private void setupMenuTable() {
        colMenuName.setCellValueFactory(data -> data.getValue().namaMenuProperty());
        colJenis.setCellValueFactory(data -> data.getValue().jenisProperty());
        colHarga.setCellValueFactory(data -> data.getValue().hargaProperty().asObject());
        colLokasi.setCellValueFactory(data -> data.getValue().lokasiProperty());
        colDiskon.setCellValueFactory(data -> data.getValue().infoDiskonProperty());
        
        setupActionColumn();
        menuTable.setItems(menuItems);
    }

    private void setupActionColumn() {
        colAction.setCellFactory(new Callback<TableColumn<MenuItem, Void>, TableCell<MenuItem, Void>>() {
            @Override
            public TableCell<MenuItem, Void> call(TableColumn<MenuItem, Void> param) {
                return new TableCell<MenuItem, Void>() {
                    private final Button addButton = new Button("Tambah ke Keranjang");
                    private final HBox buttonBox = new HBox(5, addButton);
                    
                    {
                        addButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
                        addButton.setOnAction(event -> {
                            MenuItem menuItem = getTableView().getItems().get(getIndex());
                            addToCart(menuItem);
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

    private void setupCartTable() {
        colCartMenu.setCellValueFactory(data -> data.getValue().namaMenuProperty());
        colCartJenis.setCellValueFactory(data -> data.getValue().jenisProperty());
        colCartHarga.setCellValueFactory(data -> data.getValue().hargaProperty().asObject());
        colCartQuantity.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        colCartTotal.setCellValueFactory(data -> data.getValue().totalHargaProperty().asObject());
        
        setupCartActionColumn();
        cartTable.setItems(cartItems);
    }

    private void setupCartActionColumn() {
        colCartAction.setCellFactory(new Callback<TableColumn<CartItem, Void>, TableCell<CartItem, Void>>() {
            @Override
            public TableCell<CartItem, Void> call(TableColumn<CartItem, Void> param) {
                return new TableCell<CartItem, Void>() {
                    private final Button incrementButton = new Button("+");
                    private final Button decrementButton = new Button("-");
                    private final Button removeButton = new Button("Hapus");
                    private final HBox buttonBox = new HBox(3, incrementButton, decrementButton, removeButton);
                    
                    {
                        incrementButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white;");
                        decrementButton.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");
                        removeButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
                        
                        incrementButton.setOnAction(event -> {
                            CartItem cartItem = getTableView().getItems().get(getIndex());
                            cartItem.incrementQuantity();
                            updateTotalLabel();
                        });
                        
                        decrementButton.setOnAction(event -> {
                            CartItem cartItem = getTableView().getItems().get(getIndex());
                            cartItem.decrementQuantity();
                            updateTotalLabel();
                        });
                        
                        removeButton.setOnAction(event -> {
                            CartItem cartItem = getTableView().getItems().get(getIndex());
                            removeFromCart(cartItem);
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

    private void setupDatePicker() {
        // Set DatePicker untuk mencegah pemilihan tanggal di masa lalu
        deliveryDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date != null && date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffebee;");
                }
            }
        });
        
        // Set default value ke hari ini
        deliveryDatePicker.setValue(LocalDate.now());
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
            SELECT 
                m.id_menu, 
                m.nama_menu, 
                m.jenis, 
                m.harga, 
                c.lokasi,
                COALESCE(d.persentase_diskon, 0) as persentase_diskon,
                d.syarat_diskon,
                CASE 
                    WHEN d.persentase_diskon IS NOT NULL 
                    THEN m.harga - (m.harga * d.persentase_diskon / 100)
                    ELSE m.harga 
                END as harga_diskon,
                CASE 
                    WHEN d.persentase_diskon IS NOT NULL 
                    THEN 'Harga dari Rp ' || m.harga || ' menjadi Rp ' || 
                         (m.harga - (m.harga * d.persentase_diskon / 100)) || 
                         ', ' || COALESCE(d.syarat_diskon, 'Promo Spesial')
                    ELSE ''
                END as info_diskon
            FROM daftar_menu m
            JOIN cabang c on c.cabang_id = m.cabang_id
            LEFT JOIN diskon d on d.id_menu = m.id_menu
            WHERE (? IS NULL OR c.lokasi = ?)
              AND (? IS NULL OR m.jenis = ?)
            ORDER BY m.nama_menu
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
                        rs.getInt("id_menu"),
                        rs.getString("nama_menu"),
                        rs.getString("jenis"),
                        rs.getInt("harga"),
                        rs.getString("lokasi"),
                        rs.getInt("harga_diskon"),
                        rs.getString("info_diskon")
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
    void onViewHistoryClick() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("order-history-view.fxml"));
            Scene scene = new Scene(loader.load());
            HelloApplication.getApplicationInstance().getPrimaryStage().setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Konfirmasi Logout");
        alert.setContentText("Apakah Anda yakin ingin keluar dari aplikasi?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Reset user session
                HelloApplication.getApplicationInstance().setUserId(0);
                
                // Navigate back to login screen
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
                Scene scene = new Scene(loader.load());
                HelloApplication.getApplicationInstance().getPrimaryStage().setScene(scene);
                
                showInfo("Logout Berhasil", "Anda telah berhasil keluar dari aplikasi.");
            } catch (IOException e) {
                e.printStackTrace();
                showError("Error", "Gagal melakukan logout: " + e.getMessage());
            }
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

    private int getCurrentUserId() {
        return HelloApplication.getApplicationInstance().getUserId();
    }

    private void addToCart(MenuItem menuItem) {
        // Check if item already exists in cart
        for (CartItem cartItem : cartItems) {
            if (cartItem.getIdMenu() == menuItem.getIdMenu()) {
                cartItem.incrementQuantity();
                updateTotalLabel();
                showInfo("Keranjang", "Jumlah " + menuItem.getNamaMenu() + " ditambah ke keranjang");
                return;
            }
        }
        
        // Add new item to cart - use discounted price if available
        int hargaYangDigunakan = menuItem.hasDiscount() ? menuItem.getHargaDiskon() : menuItem.getHarga();
        
        CartItem newCartItem = new CartItem(
            menuItem.getIdMenu(),
            menuItem.getNamaMenu(),
            menuItem.getJenis(),
            hargaYangDigunakan,
            1,
            menuItem.getLokasi()
        );
        
        cartItems.add(newCartItem);
        updateTotalLabel();
        
        String message = menuItem.getNamaMenu() + " ditambahkan ke keranjang";
        if (menuItem.hasDiscount()) {
            message += " (dengan diskon)";
        }
        showInfo("Keranjang", message);
    }

    private void removeFromCart(CartItem cartItem) {
        cartItems.remove(cartItem);
        updateTotalLabel();
        showInfo("Keranjang", cartItem.getNamaMenu() + " dihapus dari keranjang");
    }

    private void updateTotalLabel() {
        int total = cartItems.stream()
                .mapToInt(CartItem::getTotalHarga)
                .sum();
        totalLabel.setText("Rp " + String.format("%,d", total));
    }

    @FXML
    void onClearCartClick() {
        if (cartItems.isEmpty()) {
            showInfo("Keranjang", "Keranjang sudah kosong");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Kosongkan Keranjang");
        alert.setHeaderText("Konfirmasi");
        alert.setContentText("Apakah Anda yakin ingin mengosongkan keranjang?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            cartItems.clear();
            updateTotalLabel();
            showInfo("Keranjang", "Keranjang berhasil dikosongkan");
        }
    }

    @FXML
    void onCheckoutClick() {
        if (cartItems.isEmpty()) {
            showError("Keranjang Kosong", "Keranjang belanja masih kosong. Silakan pilih menu terlebih dahulu.");
            return;
        }

        LocalDate deliveryDate = deliveryDatePicker.getValue();
        if (deliveryDate == null) {
            showError("Jadwal Kirim", "Pilih jadwal kirim terlebih dahulu.");
            return;
        }

        // Validasi jadwal kirim tidak boleh di masa lalu
        LocalDate today = LocalDate.now();
        if (deliveryDate.isBefore(today)) {
            showError("Invalid Delivery Date", "Jadwal kirim tidak boleh di masa lalu. Pilih tanggal hari ini atau masa depan.");
            return;
        }

        // Konfirmasi checkout
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Checkout");
        alert.setHeaderText("Konfirmasi Pesanan");
        alert.setContentText("Total belanja: " + totalLabel.getText() + "\nJadwal kirim: " + deliveryDate.toString() + "\n\nLanjutkan checkout?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            processCheckout(deliveryDate);
        }
    }

    private void processCheckout(LocalDate deliveryDate) {
        try (Connection conn = DataSourceManager.getUserConnection()) {
            conn.setAutoCommit(false);
            
            // Calculate total before clearing cart
            int totalAmount = cartItems.stream()
                    .mapToInt(CartItem::getTotalHarga)
                    .sum();
            String totalText = "Rp " + String.format("%,d", totalAmount);
            
            // Insert pengiriman
            String insertDeliverySQL = """
                    INSERT INTO pengiriman (status_pengiriman, jadwal_kirim, estimasi_sampai)
                    VALUES (?, ?, ?)
                    RETURNING pengiriman_id
                    """;

            PreparedStatement stmtDelivery = conn.prepareStatement(insertDeliverySQL);
            stmtDelivery.setString(1, "Belum Dikirim");
            stmtDelivery.setDate(2, Date.valueOf(deliveryDate));
            stmtDelivery.setDate(3, Date.valueOf(deliveryDate.plusDays(2)));

            ResultSet rsDelivery = stmtDelivery.executeQuery();
            rsDelivery.next();
            int pengirimanId = rsDelivery.getInt("pengiriman_id");

            // Insert pemesanan
            String insertOrderSQL = """
                    INSERT INTO pemesanan (tanggal_pesanan, status, user_id, pengiriman_id)
                    VALUES (current_date, ?, ?, ?)
                    RETURNING pesanan_id
                    """;

            PreparedStatement stmtOrder = conn.prepareStatement(insertOrderSQL);
            stmtOrder.setString(1, "Belum Diproses");
            stmtOrder.setInt(2, HelloApplication.getApplicationInstance().getUserId());
            stmtOrder.setInt(3, pengirimanId);
            ResultSet rsOrder = stmtOrder.executeQuery();
            rsOrder.next();
            int pesananId = rsOrder.getInt("pesanan_id");

            // Insert pesanan_menu untuk setiap item di keranjang
            String insertLinkSQL = "INSERT INTO pesanan_menu (pesanan_id, id_menu) VALUES (?, ?)";
            PreparedStatement stmtLink = conn.prepareStatement(insertLinkSQL);
            
            for (CartItem cartItem : cartItems) {
                for (int i = 0; i < cartItem.getQuantity(); i++) {
                    stmtLink.setInt(1, pesananId);
                    stmtLink.setInt(2, cartItem.getIdMenu());
                    stmtLink.executeUpdate();
                }
            }

            conn.commit();

            // Clear cart after successful checkout
            cartItems.clear();
            updateTotalLabel();

            showInfo("Checkout Berhasil", "Pesanan berhasil dibuat!\nTotal: " + totalText + "\nJadwal kirim: " + deliveryDate.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Gagal melakukan checkout: " + e.getMessage());
        }
    }
}
