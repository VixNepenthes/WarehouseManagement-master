package com.midterm.proj.warehousemanagement.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.midterm.proj.warehousemanagement.constant.CreateTable;
import com.midterm.proj.warehousemanagement.constant.DropTable;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.util.logging.Logger;

public class SqliteDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WarehouseDB";
    private static final int DATABASE_VERSION = 2;
    private static SqliteDatabaseHelper databaseHelper;

    private SqliteDatabaseHelper() {
        super(MyApp.context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SqliteDatabaseHelper getInstance() {

        if (databaseHelper == null) {
            synchronized (SqliteDatabaseHelper.class){ //thread safe singleton
                if (databaseHelper == null)
                    databaseHelper = new SqliteDatabaseHelper();
            }
        }

        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable.CREATE_SUPPLIER_TABLE);
        db.execSQL(CreateTable.CREATE_EMPLOYEE_TABLE);
        db.execSQL(CreateTable.CREATE_PRODUCT_TABLE);
        db.execSQL(CreateTable.CREATE_WAREHOUSE_TABLE);
        db.execSQL(CreateTable.CREATE_CUSTOMER_TABLE);
        db.execSQL(CreateTable.CREATE_IMPORT_TICKET_TABLE);
        db.execSQL(CreateTable.CREATE_EXPORT_TICKET_TABLE);
        db.execSQL(CreateTable.CREATE_EXPORT_TICKET_DETAIL_TABLE);
        Log.d("DEBUG-MSG", "CREATE DATABASE SUCCESSFULLY");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DropTable.DROP_CUSTOMER_TABLE);
        db.execSQL(DropTable.DROP_EMPLOYEE_TABLE);
        db.execSQL(DropTable.DROP_EXPORT_TICKET_DETAIL_TABLE);
        db.execSQL(DropTable.DROP_EXPORT_TICKET_TABLE);
        db.execSQL(DropTable.DROP_IMPORT_TICKET_TABLE);
        db.execSQL(DropTable.DROP_PRODUCT_TABLE);
        db.execSQL(DropTable.DROP_SUPPLIER_TABLE);
        db.execSQL(DropTable.DROP_WAREHOUSE_TABLE);
        onCreate(db);
        Log.d("DEBUG-MSG", "DROP DATABASE SUCCESSFULLY");
    }
}
