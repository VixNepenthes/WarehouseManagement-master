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
import com.midterm.proj.warehousemanagement.model.Employee;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.util.ArrayList;
import java.util.List;

public class EmployeeQuery implements DAO.EmployeeQuery {

    private final SqliteDatabaseHelper databaseHelper = SqliteDatabaseHelper.getInstance();

    @Override
    public void createEmployee(Employee employee, QueryResponse<Boolean> response){
        try (SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase()) {
            ContentValues contentValues = getContentValuesForEmployee(employee);
            long id = sqLiteDatabase.insertOrThrow(Constants.EMPLOYEE_TABLE, null, contentValues);
            if (id > 0) {
                response.onSuccess(true);
                employee.setID_Employee((int) id);
                Toast.makeText(MyApp.context, "Thêm mới nhân viên thành công. Mã nhân viên: "+id, Toast.LENGTH_LONG).show();

            } else
                response.onFailure("Failed to create employee. Unknown Reason!");
        } catch (SQLiteException e) {
            response.onFailure(e.getMessage());
        }
    }

    @Override
    public void readEmployee(int EmployeeID, QueryResponse<Employee> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.query(Constants.EMPLOYEE_TABLE, null,
                    Constants.__EMPLOYEE_ID + " =? ", new String[]{String.valueOf(EmployeeID)},
                    null, null, null);

            if(cursor!=null && cursor.moveToFirst()) {
                Employee employee = getEmployeeFromCursor(cursor);
                response.onSuccess(employee);
            }
            else
                response.onFailure("Không tìm thấy nhân viên này trong database.");

        } catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void readAllEmployee(QueryResponse<List<Employee>> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        List<Employee> employeeList = new ArrayList<>();

        Cursor cursor = null;
        try{
            cursor = sqLiteDatabase.query(Constants.EMPLOYEE_TABLE, null,null,null,null,null,null);
            if(cursor!=null && cursor.moveToFirst()){
                do {
                    Employee employee = getEmployeeFromCursor(cursor);
                    employeeList.add(employee);
                } while (cursor.moveToNext());

                response.onSuccess(employeeList);
            } else
                response.onFailure("Chưa có nhân viên nào");
        }catch (Exception e){
            response.onFailure(e.getMessage());
        } finally {
            sqLiteDatabase.close();
            if(cursor!=null)
                cursor.close();
        }
    }

    @Override
    public void anyEmployeeCreated(QueryResponse<Boolean> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getReadableDatabase();
        boolean empty = true;
        Cursor cur = sqLiteDatabase.rawQuery("SELECT COUNT(*) FROM "+Constants.EMPLOYEE_TABLE, null);
        if (cur != null && cur.moveToFirst()) {
            empty = (cur.getInt (0) == 0);
        }
        cur.close();

        if(empty){
            response.onSuccess(empty);
        }else{
            response.onFailure("Chưa có nhân viên nào");
        }
    }

    @Override
    public void updateEmployee(Employee employee, QueryResponse<Boolean> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();

        String id = String.valueOf(employee.getID_Employee());
        ContentValues contentValues = getContentValuesForEmployee(employee);
        int row = sqLiteDatabase.update(Constants.EMPLOYEE_TABLE,
                contentValues,
                Constants.__EMPLOYEE_ID + " = ?",
                new String[]{id});
        if(row>0){
            response.onSuccess(true);
            Toast.makeText(MyApp.context, "Xác nhận thay đổi thông tin nhân viên", Toast.LENGTH_LONG).show();
        }
        else{
            response.onFailure("Không thể thay đổi thông tin nhân viên");
        }
    }

    @Override
    public void deleteEmployee(int employeeID, QueryResponse<Boolean> response){
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        int row = sqLiteDatabase.delete(Constants.EMPLOYEE_TABLE, Constants.__EMPLOYEE_ID+" = ?",new String[]{String.valueOf(employeeID)});

        if(row>0){
            response.onSuccess(true);
            Toast.makeText(MyApp.context, "Đã xóa thông tin nhân viên", Toast.LENGTH_LONG).show();
        }
        else{
            response.onFailure("Không thể xóa thông tin nhân viên");
        }
    }

    private ContentValues getContentValuesForEmployee(Employee employee){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.EMPLOYEE_NAME, employee.getName());
        contentValues.put(Constants.EMPLOYEE_PHONE, employee.getPhone());

        return contentValues;
    }

    private Employee getEmployeeFromCursor(Cursor cursor){
        int id = cursor.getInt(cursor.getColumnIndex(Constants.__EMPLOYEE_ID));
        String name = cursor.getString(cursor.getColumnIndex(Constants.EMPLOYEE_NAME));
        String phone = cursor.getString(cursor.getColumnIndex(Constants.EMPLOYEE_PHONE));

        return new Employee(id, name, phone);
    }
}
