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
import com.midterm.proj.warehousemanagement.model.Customer;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.util.ArrayList;
import java.util.List;

public class CustomerQuery implements DAO.CustomerQuery {

    private final SqliteDatabaseHelper databaseHelper = SqliteDatabaseHelper.getInstance();

    @Override
    public void createCustomer(Customer customer, QueryResponse<Boolean> response){
        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            ContentValues contentValues = getContentValuesForCustomer(customer);
            long id = sqLiteDatabase.insertOrThrow(Constants.CUSTOMER_TABLE, null, contentValues);
            if (id > 0) {
                response.onSuccess(true);
                customer.setCustomerID((int) id);
            } else
                response.onFailure("Không tạo được khách hàng mới.");
        } catch (SQLiteException e) {
            response.onFailure(e.getMessage());
        }
    }

    @Override
    public void readCustomer(int CustomerID, QueryResponse<Customer> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.query(Constants.CUSTOMER_TABLE, null,
                    Constants.CUSTOMER_ID + " =? ",
                    new String[]{String.valueOf(CustomerID)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()) {
                Customer customer = getCustomerFromCursor(cursor);
                response.onSuccess(customer);
            }
            else
                response.onFailure("Không thể tìm thấy khách hàng với ID được cung cấp");

        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void readAllCustomer(QueryResponse<List<Customer>> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        List<Customer> customerList = new ArrayList<>();

        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Constants.CUSTOMER_TABLE, null,null,null,null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                do {
                    Customer customer = getCustomerFromCursor(cursor);
                    customerList.add(customer);
                } while (cursor.moveToNext());

                response.onSuccess(customerList);
            } else
                response.onFailure("Không có khách hàng nào trong database");
        }catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void updateCustomer(Customer customer, QueryResponse<Boolean> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        String id = String.valueOf(customer.getCustomerID());
        ContentValues contentValues = getContentValuesForCustomer(customer);
        int row = sqLiteDatabase.update(Constants.CUSTOMER_TABLE,
                contentValues,
                Constants.CUSTOMER_ID + " = ?",
                new String[]{id});
        if(row>0){
            response.onSuccess(true);
            Toast.makeText(MyApp.context, "Xác nhận thay đổi thông tin khách hàng", Toast.LENGTH_LONG).show();
        }
        else{
            response.onFailure("Không thể thay đổi thông tin khách hàng");
        }
    }

    @Override
    public void deleteCustomer(int customerID, QueryResponse<Boolean> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        int row = sqLiteDatabase.delete(Constants.CUSTOMER_TABLE, Constants.CUSTOMER_ID+" = ?",new String[]{String.valueOf(customerID)});

        if(row>0){
            response.onSuccess(true);
            Toast.makeText(MyApp.context, "Đã xóa thông tin khách hàng", Toast.LENGTH_LONG).show();
        }
        else{
            response.onFailure("Không thể xóa thông tin khách hàng");
        }
    }

    private ContentValues getContentValuesForCustomer(Customer customer){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.CUSTOMER_NAME, customer.getName());
        contentValues.put(Constants.CUSTOMER_PHONE, customer.getPhone());

        return contentValues;
    }

    private Customer getCustomerFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(Constants.CUSTOMER_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_NAME));
        String phone = cursor.getString(cursor.getColumnIndex(Constants.CUSTOMER_PHONE));

        return new Customer(id, name, phone);
    }
}
