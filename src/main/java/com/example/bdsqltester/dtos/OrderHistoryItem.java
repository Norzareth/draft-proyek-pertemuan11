package com.example.bdsqltester.dtos;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OrderHistoryItem {
    private final StringProperty tanggal;
    private final StringProperty menu;
    private final StringProperty status;
    private final StringProperty jadwal;


    public OrderHistoryItem(String tanggal, String menu, String status, String jadwal){
        this.tanggal = new SimpleStringProperty(tanggal);
        this.menu = new SimpleStringProperty(menu);
        this.status = new SimpleStringProperty(status);
        this.jadwal = new SimpleStringProperty(jadwal);
    }
    public StringProperty tanggalProperty (){return tanggal; }
    public StringProperty menuProperty() {return menu; }
    public StringProperty statusProperty (){return status; }
    public StringProperty jadwalProperty() {return jadwal; }









}
