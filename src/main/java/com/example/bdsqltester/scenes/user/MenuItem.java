package com.example.bdsqltester.scenes.user;
// test justin commit


import javafx.beans.property.*;

public class MenuItem {
    private final IntegerProperty idMenu;
    private final StringProperty namaMenu;
    private final StringProperty jenis;
    private final IntegerProperty harga;
    private final StringProperty lokasi;
    private final IntegerProperty hargaDiskon;
    private final StringProperty infoDiskon;

    public MenuItem(int idMenu, String namaMenu, String jenis, int harga, String lokasi) {
        this.idMenu = new SimpleIntegerProperty(idMenu);
        this.namaMenu = new SimpleStringProperty(namaMenu);
        this.jenis = new SimpleStringProperty(jenis);
        this.harga = new SimpleIntegerProperty(harga);
        this.lokasi = new SimpleStringProperty(lokasi);
        this.hargaDiskon = new SimpleIntegerProperty(harga); // Default to original price
        this.infoDiskon = new SimpleStringProperty(""); // Default empty
    }

    public MenuItem(int idMenu, String namaMenu, String jenis, int harga, String lokasi, int hargaDiskon, String infoDiskon) {
        this.idMenu = new SimpleIntegerProperty(idMenu);
        this.namaMenu = new SimpleStringProperty(namaMenu);
        this.jenis = new SimpleStringProperty(jenis);
        this.harga = new SimpleIntegerProperty(harga);
        this.lokasi = new SimpleStringProperty(lokasi);
        this.hargaDiskon = new SimpleIntegerProperty(hargaDiskon);
        this.infoDiskon = new SimpleStringProperty(infoDiskon != null ? infoDiskon : "");
    }

    public String getNamaMenu() { return namaMenu.get(); }
    public StringProperty namaMenuProperty() { return namaMenu; }

    public String getJenis() { return jenis.get(); }
    public StringProperty jenisProperty() { return jenis; }

    public int getHarga() { return harga.get(); }
    public IntegerProperty hargaProperty() { return harga; }

    public int getIdMenu(){return idMenu.get();}
    public IntegerProperty idMenuProperty(){return idMenu; }

    public String getLokasi() { return lokasi.get(); }
    public StringProperty lokasiProperty() { return lokasi; }

    public int getHargaDiskon() { return hargaDiskon.get(); }
    public IntegerProperty hargaDiskonProperty() { return hargaDiskon; }

    public String getInfoDiskon() { return infoDiskon.get(); }
    public StringProperty infoDiskonProperty() { return infoDiskon; }

    public boolean hasDiscount() {
        return hargaDiskon.get() < harga.get();
    }
}
///oiiiiiiii haloooo

