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
import com.midterm.proj.warehousemanagement.model.ImportTicket;
import com.midterm.proj.warehousemanagement.model.Product;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.util.ArrayList;
import java.util.List;

public class ProductQuery implements DAO.ProductQuery {
    private final SqliteDatabaseHelper databaseHelper = SqliteDatabaseHelper.getInstance();
    @Override
    public void createProduct(Product product, QueryResponse<Boolean> response) {
        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            ContentValues contentValues = getContentValuesForProduct(product);
            long id = sqLiteDatabase.insertOrThrow(Constants.PRODUCT_TABLE, null, contentValues);
            if (id > 0) {
                response.onSuccess(true);
                product.setID_Product((int) id);
                Toast.makeText(MyApp.context, "Tạo sản phẩm thành công. Mã sản phẩm: "+product.getID_Product(), Toast.LENGTH_LONG).show();
            } else
                response.onFailure("Không tạo được sản phẩm mới! Vui lòng kiểm tra lại thông tin");
        } catch (SQLiteException e) {
            response.onFailure(e.getMessage());
        }
    }

    private ContentValues getContentValuesForProduct(Product product) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.PRODUCT_NAME, product.getName());
        contentValues.put(Constants.PRODUCT_UNIT, product.getUnit());
        contentValues.put(Constants.PRODUCT_INSTOCK_NUMBER, product.getNumber());
        contentValues.put(Constants.PRODUCT_PRICE, product.getPrice());
        contentValues.put(Constants.PRODUCT_IMAGE, product.getBytesImage());

        return contentValues;
    }

    @Override
    public void readProduct(int ProductID, QueryResponse<Product> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.query(Constants.PRODUCT_TABLE, null,
                    Constants.PRODUCT_ID + " =? ", new String[]{String.valueOf(ProductID)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()) {
                Product product = getProductFromCursor(cursor);
                response.onSuccess(product);
            }
            else
                response.onFailure("Không tìm thấy sản phẩm này trong database");

        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void readAllProduct(QueryResponse<List<Product>> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        List<Product> productsList = new ArrayList<>();

        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Constants.PRODUCT_TABLE, null,null,null,null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                do {
                    Product product = getProductFromCursor(cursor);
                    productsList.add(product);
                } while (cursor.moveToNext());

                response.onSuccess(productsList);
            } else
                response.onFailure("Không có sản phẩm nào trong database");
        }catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void updateProduct(Product product, QueryResponse<Boolean> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = getContentValuesForProduct(product);

        try {
            long rowCount = sqLiteDatabase.update(Constants.PRODUCT_TABLE, contentValues,
                    Constants.PRODUCT_ID + " =? ", new String[]{String.valueOf(product.getID_Product())});
            if(rowCount>0){
                response.onSuccess(true);
            }
            else
                response.onFailure("Lỗi. Dữ liệu chưa được cập nhật");
        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
        }
    }

    @Override
    public void updateInstock(int ProductID, int ProductNumber, QueryResponse<Boolean> response){
        String QUERY = "UPDATE " + Constants.PRODUCT_TABLE
                + " SET " + Constants.PRODUCT_INSTOCK_NUMBER +" = " + String.valueOf(ProductNumber)
                + " WHERE " + Constants.PRODUCT_ID + " = "+ String.valueOf(ProductID);
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.rawQuery(QUERY,null);
            if(cursor.moveToFirst()) {
                response.onSuccess(true);
            }
        }catch (Exception e){
            response.onFailure(e.getMessage());
        }finally {
            sqLiteDatabase.close();
            if(cursor != null)
                cursor.close();
        }

    }

    @Override
    public void deleteProduct(int ProductID, QueryResponse<Boolean> response) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        int row = sqLiteDatabase.delete(Constants.PRODUCT_TABLE, Constants.PRODUCT_ID+" = ?",new String[]{String.valueOf(ProductID)});

        if(row>0){
            response.onSuccess(true);
            Toast.makeText(MyApp.context, "Đã xóa thông tin sản phẩm ", Toast.LENGTH_LONG).show();
        }
        else{
            response.onFailure("Không thể xóa thông tin sản phẩm");
        }
    }
    private Product getProductFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(Constants.PRODUCT_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.PRODUCT_NAME));
        String unit= cursor.getString(cursor.getColumnIndex(Constants.PRODUCT_UNIT));
        int number= cursor.getInt(cursor.getColumnIndex(Constants.PRODUCT_INSTOCK_NUMBER));
        int price= cursor.getInt(cursor.getColumnIndex(Constants.PRODUCT_PRICE));
        byte[] bytesImage = cursor.getBlob(cursor.getColumnIndex(Constants.PRODUCT_IMAGE));
        return new Product(id,name,unit,number,price,bytesImage);
    }
}
