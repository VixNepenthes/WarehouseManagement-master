package com.midterm.proj.warehousemanagement.model;

public class Supplier {
    private int ID_Supplier;     // primary key
    private String name;
    private String address;

    public Supplier(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Supplier(int ID_Supplier, String name, String address) {
        this.ID_Supplier = ID_Supplier;
        this.name = name;
        this.address = address;
    }

    public Supplier() {

    }

    public int getID_Supplier() {
        return ID_Supplier;
    }

    public void setID_Supplier(int ID_Supplier) {
        this.ID_Supplier = ID_Supplier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
