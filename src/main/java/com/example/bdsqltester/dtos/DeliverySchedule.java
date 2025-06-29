package com.example.bdsqltester.dtos;

import java.time.LocalDate;

public class DeliverySchedule {
    private int pengirimanId;
    private int pesananId;
    private String customerName;
    private String customerEmail;
    private String statusPengiriman;
    private LocalDate jadwalKirim;
    private LocalDate estimasiSampai;
    private int userId;
    
    public DeliverySchedule() {}
    
    public DeliverySchedule(int pengirimanId, int pesananId, String customerName, String customerEmail,
                          String statusPengiriman, LocalDate jadwalKirim, LocalDate estimasiSampai, int userId) {
        this.pengirimanId = pengirimanId;
        this.pesananId = pesananId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.statusPengiriman = statusPengiriman;
        this.jadwalKirim = jadwalKirim;
        this.estimasiSampai = estimasiSampai;
        this.userId = userId;
    }
    
    // Getters and Setters
    public int getPengirimanId() { return pengirimanId; }
    public void setPengirimanId(int pengirimanId) { this.pengirimanId = pengirimanId; }
    
    public int getPesananId() { return pesananId; }
    public void setPesananId(int pesananId) { this.pesananId = pesananId; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    
    public String getStatusPengiriman() { return statusPengiriman; }
    public void setStatusPengiriman(String statusPengiriman) { this.statusPengiriman = statusPengiriman; }
    
    public LocalDate getJadwalKirim() { return jadwalKirim; }
    public void setJadwalKirim(LocalDate jadwalKirim) { this.jadwalKirim = jadwalKirim; }
    
    public LocalDate getEstimasiSampai() { return estimasiSampai; }
    public void setEstimasiSampai(LocalDate estimasiSampai) { this.estimasiSampai = estimasiSampai; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    @Override
    public String toString() {
        return "Pengiriman #" + pengirimanId + " - " + customerName;
    }
} 