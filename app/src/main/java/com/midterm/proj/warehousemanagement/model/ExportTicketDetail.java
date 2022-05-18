package com.midterm.proj.warehousemanagement.model;

public class ExportTicketDetail {
    int ID_ExportTicket;    // primary key
    int ID_Product;    // primary key
    int number;
    long pricePerUnit;


    public ExportTicketDetail(int ID_ExportTicket, int ID_Product, int number, long pricePerUnit) {
        this.ID_ExportTicket = ID_ExportTicket;
        this.ID_Product = ID_Product;
        this.number = number;
        this.pricePerUnit = pricePerUnit;
    }

    public ExportTicketDetail() {

    }

    public int getID_ExportTicket() {
        return ID_ExportTicket;
    }

    public void setID_ExportTicket(int ID_ExportTicket) {
        this.ID_ExportTicket = ID_ExportTicket;
    }

    public int getID_Product() {
        return ID_Product;
    }

    public void setID_Product(int ID_Product) {
        this.ID_Product = ID_Product;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(int pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
