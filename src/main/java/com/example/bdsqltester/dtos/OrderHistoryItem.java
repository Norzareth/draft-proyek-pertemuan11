package com.example.bdsqltester.dtos;

import java.time.LocalDate;

public class OrderHistoryItem {
    private int pesananId;
    private String customerName;
    private String customerEmail;
    private LocalDate tanggalPesanan;
    private String status;
    private int totalHarga;
    private String statusPengiriman;
    private LocalDate jadwalKirim;
    private LocalDate estimasiSampai;
    private int userId;
    private int pengirimanId;
    
    public OrderHistoryItem() {}
    
    public OrderHistoryItem(int pesananId, String customerName, String customerEmail, 
                          LocalDate tanggalPesanan, String status, int totalHarga, int userId,
                          String statusPengiriman, LocalDate jadwalKirim, LocalDate estimasiSampai, int pengirimanId) {
        this.pesananId = pesananId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.tanggalPesanan = tanggalPesanan;
        this.status = status;
        this.totalHarga = totalHarga;
        this.statusPengiriman = statusPengiriman;
        this.jadwalKirim = jadwalKirim;
        this.estimasiSampai = estimasiSampai;
        this.userId = userId;
        this.pengirimanId = pengirimanId;
    }
    
    // Getters and Setters
    public int getPesananId() { return pesananId; }
    public void setPesananId(int pesananId) { this.pesananId = pesananId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    
    public LocalDate getTanggalPesanan() { return tanggalPesanan; }
    public void setTanggalPesanan(LocalDate tanggalPesanan) { this.tanggalPesanan = tanggalPesanan; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public int getTotalHarga() { return totalHarga; }
    public void setTotalHarga(int totalHarga) { this.totalHarga = totalHarga; }
    
    public String getStatusPengiriman() { return statusPengiriman; }
    public void setStatusPengiriman(String statusPengiriman) { this.statusPengiriman = statusPengiriman; }
    
    public LocalDate getJadwalKirim() { return jadwalKirim; }
    public void setJadwalKirim(LocalDate jadwalKirim) { this.jadwalKirim = jadwalKirim; }
    
    public LocalDate getEstimasiSampai() { return estimasiSampai; }
    public void setEstimasiSampai(LocalDate estimasiSampai) { this.estimasiSampai = estimasiSampai; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getPengirimanId() { return pengirimanId; }
    public void setPengirimanId(int pengirimanId) { this.pengirimanId = pengirimanId; }
    
    @Override
    public String toString() {
        return "Pesanan #" + pesananId + " - " + customerName;
    }
}
