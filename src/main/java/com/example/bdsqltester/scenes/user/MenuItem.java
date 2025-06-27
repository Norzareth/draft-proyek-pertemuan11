package com.example.bdsqltester.scenes.user;


import javafx.beans.property.*;

public class MenuItem {
    private final StringProperty namaMenu;
    private final StringProperty jenis;
    private final IntegerProperty harga;

    public MenuItem(String namaMenu, String jenis, int harga) {
        this.namaMenu = new SimpleStringProperty(namaMenu);
        this.jenis = new SimpleStringProperty(jenis);
        this.harga = new SimpleIntegerProperty(harga);
    }

    public String getNamaMenu() { return namaMenu.get(); }
    public StringProperty namaMenuProperty() { return namaMenu; }

    public String getJenis() { return jenis.get(); }
    public StringProperty jenisProperty() { return jenis; }

    public int getHarga() { return harga.get(); }
    public IntegerProperty hargaProperty() { return harga; }
}
