package com.midterm.proj.warehousemanagement.model;

public class Product {
    private int ID_Product;     // primary key
    private String name;
    private String unit;
    private int number;
    private long price;
    private byte[] bytesImage;

    public Product(String name){
        this.name= name;
    }

    public Product(int ID_Product, String name, String unit, int number, long price, byte[] bytesImage) {
        this.ID_Product = ID_Product;
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.bytesImage = bytesImage;
        this.number = number;
    }

    public Product(String name, String unit, int number ,long price, byte[] bytesImage) {
        this.name = name;
        this.unit = unit;
        this.number = number;
        this.price = price;
        this.bytesImage = bytesImage;
    }

    public int getID_Product() {
        return ID_Product;
    }

    public void setID_Product(int ID_Product) {
        this.ID_Product = ID_Product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public byte[] getBytesImage() {
        return bytesImage;
    }

    public void setBytesImage(byte[] bytesImage) {
        this.bytesImage = bytesImage;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
