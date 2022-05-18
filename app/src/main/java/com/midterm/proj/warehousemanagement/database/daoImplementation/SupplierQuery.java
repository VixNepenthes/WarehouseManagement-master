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
import com.midterm.proj.warehousemanagement.model.Supplier;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.util.ArrayList;
import java.util.List;

public class SupplierQuery implements DAO.SupplierQuery {

    private final SqliteDatabaseHelper databaseHelper = SqliteDatabaseHelper.getInstance();

    @Override
    public void createSupplier(Supplier supplier, QueryResponse<Boolean> response){
        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            ContentValues contentValues = getContentValuesForSupplier(supplier);
            long id = sqLiteDatabase.insertOrThrow(Constants.SUPPLIER_TABLE, null, contentValues);
            if (id > 0) {
                response.onSuccess(true);
                supplier.setID_Supplier((int) id);
            } else
                response.onFailure("Không tạo được nhà cung cấp");
        } catch (SQLiteException e) {
            response.onFailure(e.getMessage());
        }
    }

    @Override
    public void readSupplier(int SupplierID, QueryResponse<Supplier> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.query(Constants.SUPPLIER_TABLE, null,
                    Constants._SUPPLIER_ID + " =? ", new String[]{String.valueOf(SupplierID)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()) {
                Supplier supplier = getSupplierFromCursor(cursor);
                response.onSuccess(supplier);
            }
            else
                response.onFailure("Không tìm thấy");

        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void readAllSupplier(QueryResponse<List<Supplier>> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        List<Supplier> supplierList = new ArrayList<>();

        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Constants.SUPPLIER_TABLE, null,null,null,null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                do {
                    Supplier supplier = getSupplierFromCursor(cursor);
                    supplierList.add(supplier);
                } while (cursor.moveToNext());

                response.onSuccess(supplierList);
            } else
                response.onFailure("Chưa có nhà cung cấp nào");
        }catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void updateSupplier(Supplier supplier, QueryResponse<Boolean> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        String id = String.valueOf(supplier.getID_Supplier());
        ContentValues contentValues = getContentValuesForSupplier(supplier);
        int row = sqLiteDatabase.update(Constants.SUPPLIER_TABLE,
                contentValues,
                Constants._SUPPLIER_ID + " = ?",
                new String[]{id});
        if(row>0){
            response.onSuccess(true);
            Toast.makeText(MyApp.context, "Xác nhận thay đổi thông tin nhà cung cấp", Toast.LENGTH_LONG).show();
        }
        else{
            response.onFailure("Không thể thay đổi thông tin nhà cung cấp");
        }
    }

    @Override
    public void deleteSupplier(int supplierID, QueryResponse<Boolean> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        int row = sqLiteDatabase.delete(Constants.SUPPLIER_TABLE, Constants._SUPPLIER_ID+" = ?",new String[]{String.valueOf(supplierID)});

        if(row>0){
            response.onSuccess(true);
            Toast.makeText(MyApp.context, "Đã xóa nhà cung cấp", Toast.LENGTH_LONG).show();
        }
        else{
            response.onFailure("Không thể xóa cung cấp này");
        }
    }

    private ContentValues getContentValuesForSupplier(Supplier supplier){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.SUPPLIER_NAME, supplier.getName());
        contentValues.put(Constants.SUPPLIER_ADDRESS, supplier.getAddress());

        return contentValues;
    }

    private Supplier getSupplierFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(Constants._SUPPLIER_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.SUPPLIER_NAME));
        String address = cursor.getString(cursor.getColumnIndex(Constants.SUPPLIER_ADDRESS));

        return new Supplier(id, name, address);
    }
}
