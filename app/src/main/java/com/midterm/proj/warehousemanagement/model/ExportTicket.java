package com.midterm.proj.warehousemanagement.model;

public class ExportTicket {
    int ID_ExportTicket;    // primary key

    int employeeID;
    int customerID;
    int warehouseID;
    String createDate;

    public ExportTicket(){}

    public ExportTicket(int employeeID, int customerID, int warehouseID, String createDate) {
        this.employeeID = employeeID;
        this.customerID = customerID;
        this.warehouseID = warehouseID;
        this.createDate = createDate;
    }

    public ExportTicket(int ID_ExportTicket, String createDate) {
        this.ID_ExportTicket = ID_ExportTicket;
        this.createDate = createDate;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public int getID_ExportTicket() {
        return ID_ExportTicket;
    }

    public void setID_ExportTicket(int ID_ExportTicket) {
        this.ID_ExportTicket = ID_ExportTicket;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
