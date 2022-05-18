package com.midterm.proj.warehousemanagement.database.daoImplementation;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.midterm.proj.warehousemanagement.constant.Constants;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.SqliteDatabaseHelper;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.model.Customer;
import com.midterm.proj.warehousemanagement.model.ExportTicketDetail;
import com.midterm.proj.warehousemanagement.model.ImportTicket;

import java.util.ArrayList;
import java.util.List;

public class ExportTicketDetailQuery implements DAO.ExportTicketDetailQuery {
    private final SqliteDatabaseHelper databaseHelper = SqliteDatabaseHelper.getInstance();

    @Override
    public void createExportTicketDetail(ExportTicketDetail exportTicketDetail, QueryResponse<Boolean> response){
        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            ContentValues contentValues = getContentValuesForExportTicketDetail(exportTicketDetail);
            long id = sqLiteDatabase.insertOrThrow(Constants.EXPORT_TICKET_DETAIL_TABLE, null, contentValues);
            if (id > 0) {
                response.onSuccess(true);
            } else
                response.onFailure("Không tạo được chi tiết phiếu xuất kho mới.");
        } catch (SQLiteException e) {
            response.onFailure(e.getMessage());
        }

    }

    private ContentValues getContentValuesForExportTicketDetail(ExportTicketDetail exportTicketDetail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.EXPORT_TICKET_ID, exportTicketDetail.getID_ExportTicket());
        contentValues.put(Constants.PRODUCT_ID, exportTicketDetail.getID_Product());
        contentValues.put(Constants.EXPORT_TICKET_DETAIL_PRODUCT_NUMBER, exportTicketDetail.getNumber());
        contentValues.put(Constants.EXPORT_TICKET_DETAIL_PRODUCT_PRICE, exportTicketDetail.getPricePerUnit());
        return contentValues;

    }

    @Override
    public void readExportTicketDetail(int ExportTicketDetailID, QueryResponse<ExportTicketDetail> response){}

    @Override
    public void readAllExportTicketDetail(int ExportTicketID, QueryResponse<List <ExportTicketDetail> > response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        //  select * from import_ticket_table where warehouse_id = 1
        String QUERY = "SELECT * FROM "
                +Constants.EXPORT_TICKET_DETAIL_TABLE + " WHERE "
                +Constants.EXPORT_TICKET_ID + " = "
                +ExportTicketID;
        Cursor cursor = null;
        try{
            List<ExportTicketDetail> exportTicketDetails= new ArrayList<>();
            cursor = sqLiteDatabase.rawQuery(QUERY,null);

            if(cursor.moveToFirst()) {
                do {
                    ExportTicketDetail exportTicketDetail = new ExportTicketDetail();
                    exportTicketDetail.setID_ExportTicket
                            (cursor.getInt(cursor.getColumnIndex(Constants.EXPORT_TICKET_ID)));
                    exportTicketDetail.setID_Product
                            (cursor.getInt(cursor.getColumnIndex(Constants.PRODUCT_ID)));
                    exportTicketDetail.setNumber(cursor.getInt
                            (cursor.getColumnIndex(Constants.EXPORT_TICKET_DETAIL_PRODUCT_NUMBER)));
                    exportTicketDetail.setPricePerUnit(cursor.getInt
                            (cursor.getColumnIndex(Constants.EXPORT_TICKET_DETAIL_PRODUCT_PRICE)));
                    exportTicketDetails.add(exportTicketDetail);
                } while (cursor.moveToNext());

                response.onSuccess(exportTicketDetails);
            }
        }catch (Exception e){
            response.onFailure(e.getMessage());
        }finally {
            sqLiteDatabase.close();
            if(cursor != null)
                cursor.close();
        }
    }

    private ExportTicketDetail getExportTicketDetailFromCursor(Cursor cursor) {
        ExportTicketDetail exportTicketDetail = new ExportTicketDetail();
        exportTicketDetail.setID_ExportTicket(cursor.getInt(cursor.getColumnIndex(Constants.EXPORT_TICKET_ID)));
        exportTicketDetail.setID_ExportTicket(cursor.getInt(cursor.getColumnIndex(Constants.PRODUCT_ID)));
        exportTicketDetail.setNumber(cursor.getInt(cursor.getColumnIndex(Constants.EXPORT_TICKET_DETAIL_PRODUCT_NUMBER)));
        exportTicketDetail.setPricePerUnit(cursor.getInt(cursor.getColumnIndex(Constants.EXPORT_TICKET_DETAIL_PRODUCT_PRICE)));
        return exportTicketDetail;
    }

}
