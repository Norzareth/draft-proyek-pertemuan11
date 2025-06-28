package com.example.bdsqltester.dtos;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OrderHistoryItem {
    private final StringProperty tanggal;
    private final StringProperty menu;
    private final StringProperty status;
    private final StringProperty jadwal;

    private final IntegerProperty pesananId = new SimpleIntegerProperty();


    public OrderHistoryItem(int pesananId, String tanggal, String menu, String status, String jadwal){

        this.pesananId.set(pesananId);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.menu = new SimpleStringProperty(menu);
        this.status = new SimpleStringProperty(status);
        this.jadwal = new SimpleStringProperty(jadwal);
    }
    public int getPesananId (){
        return pesananId.get();
    }
    public IntegerProperty pesananIdProperty(){
        return pesananId;
    }
    public StringProperty tanggalProperty (){return tanggal; }
    public StringProperty menuProperty() {return menu; }
    public StringProperty statusProperty (){return status; }
    public StringProperty jadwalProperty() {return jadwal; }









}
