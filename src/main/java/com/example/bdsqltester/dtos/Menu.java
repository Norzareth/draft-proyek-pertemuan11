package com.example.bdsqltester.dtos;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu {
    private int id;
    private String nama;
    private String jenis;
    private int idPenjual;
    private String namaPenjual;
    private int harga;
    private int cabangId;
    private String namaCabang;
    
    public Menu() {}
    
    public Menu(int id, String nama, String jenis, int idPenjual, String namaPenjual, int harga, int cabangId, String namaCabang) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
        this.idPenjual = idPenjual;
        this.namaPenjual = namaPenjual;
        this.harga = harga;
        this.cabangId = cabangId;
        this.namaCabang = namaCabang;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    
    public int getIdPenjual() { return idPenjual; }
    public void setIdPenjual(int idPenjual) { this.idPenjual = idPenjual; }
    
    public String getNamaPenjual() { return namaPenjual; }
    public void setNamaPenjual(String namaPenjual) { this.namaPenjual = namaPenjual; }
    
    public int getHarga() { return harga; }
    public void setHarga(int harga) { this.harga = harga; }
    
    public int getCabangId() { return cabangId; }
    public void setCabangId(int cabangId) { this.cabangId = cabangId; }
    
    public String getNamaCabang() { return namaCabang; }
    public void setNamaCabang(String namaCabang) { this.namaCabang = namaCabang; }
    
    @Override
    public String toString() {
        return nama;
    }
}
