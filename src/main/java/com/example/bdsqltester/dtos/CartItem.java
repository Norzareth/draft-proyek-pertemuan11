package com.example.bdsqltester.dtos;

import javafx.beans.property.*;

public class CartItem {
    private final IntegerProperty idMenu;
    private final StringProperty namaMenu;
    private final StringProperty jenis;
    private final IntegerProperty harga;
    private final IntegerProperty quantity;
    private final IntegerProperty totalHarga;
    private final StringProperty lokasi;

    public CartItem(int idMenu, String namaMenu, String jenis, int harga, int quantity, String lokasi) {
        this.idMenu = new SimpleIntegerProperty(idMenu);
        this.namaMenu = new SimpleStringProperty(namaMenu);
        this.jenis = new SimpleStringProperty(jenis);
        this.harga = new SimpleIntegerProperty(harga);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.totalHarga = new SimpleIntegerProperty(harga * quantity);
        this.lokasi = new SimpleStringProperty(lokasi);
    }

    // Getters and Setters
    public int getIdMenu() { return idMenu.get(); }
    public IntegerProperty idMenuProperty() { return idMenu; }
    public void setIdMenu(int idMenu) { this.idMenu.set(idMenu); }

    public String getNamaMenu() { return namaMenu.get(); }
    public StringProperty namaMenuProperty() { return namaMenu; }
    public void setNamaMenu(String namaMenu) { this.namaMenu.set(namaMenu); }

    public String getJenis() { return jenis.get(); }
    public StringProperty jenisProperty() { return jenis; }
    public void setJenis(String jenis) { this.jenis.set(jenis); }

    public int getHarga() { return harga.get(); }
    public IntegerProperty hargaProperty() { return harga; }
    public void setHarga(int harga) { 
        this.harga.set(harga); 
        updateTotalHarga();
    }

    public int getQuantity() { return quantity.get(); }
    public IntegerProperty quantityProperty() { return quantity; }
    public void setQuantity(int quantity) { 
        this.quantity.set(quantity); 
        updateTotalHarga();
    }

    public int getTotalHarga() { return totalHarga.get(); }
    public IntegerProperty totalHargaProperty() { return totalHarga; }

    public String getLokasi() { return lokasi.get(); }
    public StringProperty lokasiProperty() { return lokasi; }
    public void setLokasi(String lokasi) { this.lokasi.set(lokasi); }

    private void updateTotalHarga() {
        totalHarga.set(harga.get() * quantity.get());
    }

    public void incrementQuantity() {
        setQuantity(getQuantity() + 1);
    }

    public void decrementQuantity() {
        if (getQuantity() > 1) {
            setQuantity(getQuantity() - 1);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CartItem cartItem = (CartItem) obj;
        return getIdMenu() == cartItem.getIdMenu();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getIdMenu());
    }

    @Override
    public String toString() {
        return getNamaMenu() + " (x" + getQuantity() + ")";
    }
} 