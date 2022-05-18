package com.midterm.proj.warehousemanagement.database.daoImplementation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.widget.Toast;

import com.midterm.proj.warehousemanagement.constant.Constants;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.SqliteDatabaseHelper;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.model.ExportTicket;
import com.midterm.proj.warehousemanagement.model.ImportTicket;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.util.ArrayList;
import java.util.List;

public class ExportTicketQuery implements DAO.ExportTicketQuery {
    private final SqliteDatabaseHelper databaseHelper = SqliteDatabaseHelper.getInstance();

    public void createExportTicket(ExportTicket exportTicket, QueryResponse<Boolean> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.EXPORT_TICKET_CREATION_DATE, exportTicket.getCreateDate());
        contentValues.put(Constants.EMPLOYEE_ID_FK, exportTicket.getEmployeeID());
        contentValues.put(Constants.CUSTOMER_ID_FK, exportTicket.getCustomerID());
        contentValues.put(Constants.WAREHOUSE_ID_FK, exportTicket.getWarehouseID());
        try{
            long rowCount = sqLiteDatabase.insertOrThrow(Constants.EXPORT_TICKET_TABLE, null,contentValues);

            if (rowCount > 0){
                response.onSuccess(true);
                String info =  "Xác nhận phiếu xuất kho lúc: " + exportTicket.getCreateDate();
                Toast.makeText(MyApp.context, info, Toast.LENGTH_LONG).show();
            }
            else
                response.onFailure("Không tạo được phiếu xuất kho!");

        }catch (SQLiteException e){
            response.onFailure(e.getMessage());
        }finally {
            sqLiteDatabase.close();
        }
    }
    public void readExportTicket(int ExportTicketID, QueryResponse<ExportTicket> response){}
    public void readAllExportTicket(int WarehouseID, QueryResponse<List<ExportTicket>> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        //  select * from import_ticket_table where warehouse_id = 1
        String QUERY = "SELECT * FROM "
                +Constants.EXPORT_TICKET_TABLE + " WHERE "
                +Constants.WAREHOUSE_ID_FK + " = "
                +WarehouseID;
        Cursor cursor = null;
        try{
            List<ExportTicket> exportTickets= new ArrayList<>();
            cursor = sqLiteDatabase.rawQuery(QUERY,null);

            if(cursor.moveToFirst()) {
                do {
                    ExportTicket exportTicket = new ExportTicket();

                    exportTicket.setID_ExportTicket(cursor.getInt(cursor.getColumnIndex(Constants.EXPORT_TICKET_ID)));;
                    exportTicket.setEmployeeID(cursor.getInt(cursor.getColumnIndex(Constants.EMPLOYEE_ID_FK)));
                    exportTicket.setWarehouseID(cursor.getInt(cursor.getColumnIndex(Constants.WAREHOUSE_ID_FK)));
                    exportTicket.setCreateDate(cursor.getString(cursor.getColumnIndex(Constants.EXPORT_TICKET_CREATION_DATE)));
                    exportTicket.setCustomerID(cursor.getInt(cursor.getColumnIndex(Constants.CUSTOMER_ID_FK)));

                    exportTickets.add(exportTicket);
                } while (cursor.moveToNext());

                response.onSuccess(exportTickets);
            }
        }catch (Exception e){
            response.onFailure(e.getMessage());
        }finally {
            sqLiteDatabase.close();
            if(cursor != null)
                cursor.close();
        }
    }

    public void getRowCount(QueryResponse<Integer> response){
        String countQuery = "SELECT  * FROM " + Constants.EXPORT_TICKET_TABLE;
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        if (count > 0)
            response.onSuccess(count);
        else
            response.onFailure("rowcount = -1");
    }
}
