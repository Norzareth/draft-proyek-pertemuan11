package com.example.bdsqltester.scenes.user;
// test justin commit


import javafx.beans.property.*;

public class MenuItem {
    private final IntegerProperty idMenu;
    private final StringProperty namaMenu;
    private final StringProperty jenis;
    private final IntegerProperty harga;

    public MenuItem(int idMenu, String namaMenu, String jenis, int harga) {
        this.idMenu = new SimpleIntegerProperty(idMenu);
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

    public int getIdMenu(){return idMenu.get();}
    public IntegerProperty idMenuProperty(){return idMenu; }
}
///oiiiiiiii haloooo

