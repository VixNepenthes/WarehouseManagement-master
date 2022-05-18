package com.midterm.proj.warehousemanagement.constant;

public class CreateTable {
    public static String CREATE_IMPORT_TICKET_TABLE =
            "CREATE TABLE " + Constants.IMPORT_TICKET_TABLE + "("
            +Constants.IMPORT_TICKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Constants._EMPLOYEE_ID + " INTEGER NOT NULL, "
            +Constants._WAREHOUSE_ID + " INTEGER NOT NULL, "
            +Constants.IMPORT_TICKET_CREATION_DATE + " TEXT NOT NULL, "
            +Constants.IMPORT_TICKET_NUMBER_OF_PRODUCTS + " INTEGER NOT NULL, "
            +Constants.IMPORT_TICKET_SUPPLIER_ID_FK+ " INTEGER NOT NULL, "
            +Constants.IMPORT_TICKET_PRODUCTS_ID_FK+ " INTEGER NOT NULL, "
            +"FOREIGN KEY (" + Constants._EMPLOYEE_ID + ") REFERENCES " + Constants.EMPLOYEE_TABLE + "(" + Constants.__EMPLOYEE_ID + ") ON UPDATE CASCADE, "
            +"FOREIGN KEY (" + Constants._WAREHOUSE_ID + ") REFERENCES " + Constants.WAREHOUSE_TABLE + "(" + Constants.__WAREHOUSE_ID + ") ON UPDATE CASCADE, "
            +"FOREIGN KEY (" + Constants.IMPORT_TICKET_PRODUCTS_ID_FK + ") REFERENCES " + Constants.PRODUCT_TABLE + "(" + Constants._PRODUCT_ID + ") ON UPDATE CASCADE, "
            +"FOREIGN KEY (" + Constants.IMPORT_TICKET_SUPPLIER_ID_FK + ") REFERENCES " + Constants.SUPPLIER_TABLE + "(" + Constants._SUPPLIER_ID + ") ON UPDATE CASCADE "
            +")";

    public static String CREATE_SUPPLIER_TABLE =
            "CREATE TABLE " + Constants.SUPPLIER_TABLE + "("
            +Constants._SUPPLIER_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Constants.SUPPLIER_NAME+" TEXT NOT NULL, "
            +Constants.SUPPLIER_ADDRESS+ " TEXT "
            +")";

    public static String CREATE_EMPLOYEE_TABLE=
            "CREATE TABLE " + Constants.EMPLOYEE_TABLE + "("
            +Constants.__EMPLOYEE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Constants.EMPLOYEE_NAME+" TEXT NOT NULL, "
            +Constants.EMPLOYEE_PHONE+ " TEXT NOT NULL"
            +")";

    public static String CREATE_PRODUCT_TABLE=
            "CREATE TABLE " + Constants.PRODUCT_TABLE + "("
            +Constants._PRODUCT_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Constants.PRODUCT_NAME+" TEXT NOT NULL, "
            +Constants.PRODUCT_UNIT+ " TEXT NOT NULL, "
            +Constants.PRODUCT_INSTOCK_NUMBER+ " INT, "
            +Constants.PRODUCT_PRICE+ " INT NOT NULL, "
            +Constants.PRODUCT_IMAGE+ " BLOB"
            +")";

    public static String CREATE_WAREHOUSE_TABLE=
            "CREATE TABLE " + Constants.WAREHOUSE_TABLE + "("
            +Constants.__WAREHOUSE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Constants.WAREHOUSE_NAME+" TEXT NOT NULL, "
            +Constants.WAREHOUSE_ADDRESS+ " TEXT "
            +")";

    public static String CREATE_EXPORT_TICKET_TABLE=
            "CREATE TABLE " + Constants.EXPORT_TICKET_TABLE + "("
            +Constants._EXPORT_TICKET_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Constants.EXPORT_TICKET_CREATION_DATE+" TEXT NOT NULL, "
            +Constants.EMPLOYEE_ID_FK+" INTEGER NOT NULL, "
            +Constants.CUSTOMER_ID_FK+" INTEGER NOT NULL, "
            +Constants.WAREHOUSE_ID_FK+" INTEGER NOT NULL, "
            +"FOREIGN KEY (" + Constants.EMPLOYEE_ID_FK + ") REFERENCES " + Constants.EMPLOYEE_TABLE + "(" + Constants.__EMPLOYEE_ID + ") ON UPDATE CASCADE, "
            +"FOREIGN KEY (" + Constants.CUSTOMER_ID_FK + ") REFERENCES " + Constants.CUSTOMER_TABLE + "(" + Constants.CUSTOMER_ID + ") ON UPDATE CASCADE, "
            +"FOREIGN KEY (" + Constants.WAREHOUSE_ID_FK + ") REFERENCES " + Constants.WAREHOUSE_TABLE + "(" + Constants._WAREHOUSE_ID + ") ON UPDATE CASCADE "
            +")";

    public static String CREATE_EXPORT_TICKET_DETAIL_TABLE =
            "CREATE TABLE " + Constants.EXPORT_TICKET_DETAIL_TABLE + "("
            +Constants.EXPORT_TICKET_ID+ " INTEGER, "
            +Constants.PRODUCT_ID+ " INTEGER, "
            +Constants.EXPORT_TICKET_DETAIL_PRODUCT_NUMBER+ " INTEGER NOT NULL, "
            +Constants.EXPORT_TICKET_DETAIL_PRODUCT_PRICE+ " INTEGER NOTE NULL, "
            +"PRIMARY KEY("+Constants.EXPORT_TICKET_ID+", "+Constants.PRODUCT_ID+") "
            +")";

    public static String CREATE_CUSTOMER_TABLE =
            "CREATE TABLE " + Constants.CUSTOMER_TABLE + "("
            +Constants.CUSTOMER_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Constants.CUSTOMER_NAME+" TEXT NOT NULL, "
            +Constants.CUSTOMER_PHONE+" TEXT  "
            +")";

}
