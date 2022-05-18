package com.midterm.proj.warehousemanagement.model;

public class Employee {
    private int ID_Employee;    // primary key
    private String name;
    private String phone;

    public Employee(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Employee(int ID_Employee, String name, String phone) {
        this.ID_Employee = ID_Employee;
        this.name = name;
        this.phone = phone;
    }

    public Employee() {

    }

    public int getID_Employee() {
        return ID_Employee;
    }

    public void setID_Employee(int ID_Employee) {
        this.ID_Employee = ID_Employee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
