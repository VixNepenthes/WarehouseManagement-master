package com.midterm.proj.warehousemanagement.database.daoImplementation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.midterm.proj.warehousemanagement.constant.Constants;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.SqliteDatabaseHelper;
import com.midterm.proj.warehousemanagement.model.Warehouse;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.util.ArrayList;
import java.util.List;

public class WarehouseQuery implements DAO.WarehouseQuery {
    private final SqliteDatabaseHelper databaseHelper = SqliteDatabaseHelper.getInstance();

    @Override
    public void createWarehouse(Warehouse warehouse, QueryResponse<Boolean> response) {
        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            ContentValues contentValues = getContentValuesForWarehouse(warehouse);
            long id = sqLiteDatabase.insertOrThrow(Constants.WAREHOUSE_TABLE, null, contentValues);
            if (id > 0) {
                response.onSuccess(true);
                warehouse.setID_Warehouse((int) id);
                Toast.makeText(MyApp.context, "Tạo kho thành công. Mã kho: "+warehouse.getID_Warehouse(), Toast.LENGTH_LONG).show();
            } else
                response.onFailure("Không thể tạo được nhà kho mới. Vui lòng kiểm tra lại thông tin");
        } catch (SQLiteException e) {
            response.onFailure(e.getMessage());
        }
    }

    private ContentValues getContentValuesForWarehouse(Warehouse warehouse) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.WAREHOUSE_NAME, warehouse.getName());
        contentValues.put(Constants.WAREHOUSE_ADDRESS, warehouse.getAddress());

        return contentValues;
    }

    @Override
    public void readWarehouse(int WarehouseID, QueryResponse<Warehouse> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.query(Constants.WAREHOUSE_TABLE, null,
                    Constants.__WAREHOUSE_ID + " =? ", new String[]{String.valueOf(WarehouseID)},
                    null, null, null,null);

            if(cursor!=null && cursor.moveToFirst()) {
                Warehouse warehouse = getWarehouseFromCursor(cursor);
                response.onSuccess(warehouse);
            }
            else
                response.onFailure("Không tìm thấy nhà kho này trong database");

        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void readAllWarehouse(QueryResponse<List<Warehouse>> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        List<Warehouse> warehousesList = new ArrayList<>();

        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Constants.WAREHOUSE_TABLE, null,null,null,null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                do {
                    Warehouse warehouse = getWarehouseFromCursor(cursor);
                    warehousesList.add(warehouse);
                } while (cursor.moveToNext());

                response.onSuccess(warehousesList);
            } else
                response.onFailure("Không có nhà kho nào cả");
        }catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void anyWarehouseCreated(QueryResponse<Boolean> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        boolean empty = true;
        Cursor cur = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM "+Constants.WAREHOUSE_TABLE, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        if(empty){
            response.onSuccess(empty);
        }else{
            response.onFailure("Chưa có nhà kho nào được tạo");
        }
    }

    @Override
    public void updateWarehouse(Warehouse warehouse, QueryResponse<Boolean> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        String id = String.valueOf(warehouse.getID_Warehouse());
        ContentValues contentValues = getContentValuesForWarehouse(warehouse);
        int row = sqLiteDatabase.update(Constants.WAREHOUSE_TABLE,
                contentValues,
                Constants.__WAREHOUSE_ID + " = ?",
                new String[]{id});
        if(row>0){
            response.onSuccess(true);
            Toast.makeText(MyApp.context, "Xác nhận thay đổi thông tin nhà kho", Toast.LENGTH_LONG).show();
        }
        else{
            response.onFailure("Không thể thay đổi thông tin nhà kho");
        }
    }

    @Override
    public void deleteWarehouse(int WarehouseID, QueryResponse<Boolean> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        int row = sqLiteDatabase.delete(Constants.WAREHOUSE_TABLE, Constants.__WAREHOUSE_ID+" = ?",new String[]{String.valueOf(WarehouseID)});

        if(row>0){
            response.onSuccess(true);
            Toast.makeText(MyApp.context, "Đã xóa kho", Toast.LENGTH_LONG).show();
        }
        else{
            response.onFailure("Không thể xóa kho này");
        }
    }

    private Warehouse getWarehouseFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(Constants.__WAREHOUSE_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.WAREHOUSE_NAME));
        String address = cursor.getString(cursor.getColumnIndex(Constants.WAREHOUSE_ADDRESS));

        return new Warehouse(id, name, address);
    }
}
