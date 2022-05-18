package com.midterm.proj.warehousemanagement.model;

public class Warehouse {
    private int ID_Warehouse;
    private String name;
    private String address;

    public Warehouse(int ID_Warehouse, String name, String address) {
        this.ID_Warehouse = ID_Warehouse;
        this.name = name;
        this.address = address;
    }

    public Warehouse(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Warehouse() {

    }

    public int getID_Warehouse() {
        return ID_Warehouse;
    }

    public void setID_Warehouse(int ID_Warehouse) {
        this.ID_Warehouse = ID_Warehouse;
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
