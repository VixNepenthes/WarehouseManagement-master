package com.midterm.proj.warehousemanagement.features.import_ticket.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.EmployeeQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.SupplierQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.model.Employee;
import com.midterm.proj.warehousemanagement.model.ImportTicket;
import com.midterm.proj.warehousemanagement.model.Supplier;
import com.midterm.proj.warehousemanagement.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class ImportTicketAdapter extends ArrayAdapter<ImportTicket> {
    private Context mContext;
    private ArrayList<ImportTicket> importTicketsArrayList = new ArrayList<>();
    private ArrayList<Warehouse> warehouses = new ArrayList<>();
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<Supplier> suppliers = new ArrayList<>();

    public ImportTicketAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<ImportTicket> list){
        super(context, 0 , list);
        mContext = context;
        importTicketsArrayList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.import_ticket_list_item, parent, false);
        }
        ImportTicket importTicket = importTicketsArrayList.get(position);
        TextView idImportTicket = listItem.findViewById(R.id.tv_id_import_ticket);
        TextView tvCreateDate = listItem.findViewById(R.id.tv_create_date);
        TextView WarehouseAddress = listItem.findViewById(R.id.tv_warehouse_address);
        TextView employeeName = listItem.findViewById(R.id.tv_employee_name);
        TextView supplierName = listItem.findViewById(R.id.tv_supplier_name);
        TextView supplierAddress = listItem.findViewById(R.id.tv_supplier_address);

        fetchWarehouseList();
        for(Warehouse w: warehouses){
            if(w.getID_Warehouse() == importTicket.getID_Warehouse()){
                WarehouseAddress.setText(w.getAddress());
            }
        }

        fetchEmployee(importTicket.getID_Employee());
        fetchSupplier(importTicket.getSupplierID());

        idImportTicket.setText(String.valueOf(importTicket.getImportTicketID()));
        try{
            employeeName.setText(employees.get(0).getName());
        }catch (Exception e){
            employeeName.setText("(?) đã xóa");
        }
        try{
            supplierName.setText(suppliers.get(0).getName());
        }catch (Exception e){
            supplierName.setText("(?) đã xóa");
        }
        try{
            supplierAddress.setText(suppliers.get(0).getAddress());
        }catch (Exception e){
            supplierAddress.setText("(?) đã xóa");
        }
        tvCreateDate.setText(importTicket.getCreateDate());

        return listItem;
    }

    public String getContentItem(ImportTicket importTicket){
        String result ="";
        ArrayList<Warehouse> warehouses2 = new ArrayList<>();
        ArrayList<Employee> employees2 = new ArrayList<>();
        ArrayList<Supplier> suppliers2 = new ArrayList<>();
        String idImportTicket2, tvCreateDate2, WarehouseAddress2 ="", employeeName2, supplierName2, supplierAddress2;

        //fetchWarehouseList();
        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
            @Override
            public void onSuccess(List<Warehouse> data) {
                warehouses2.clear();
                warehouses2.addAll(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });


        for(Warehouse w: warehouses2){
            if(w.getID_Warehouse() == importTicket.getID_Warehouse()){
                WarehouseAddress2 = w.getAddress();
            }
        }

        //fetchEmployee(importTicket.getID_Employee());
        int employeeID = importTicket.getID_Employee();
        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.readEmployee(employeeID, new QueryResponse<Employee>() {
            @Override
            public void onSuccess(Employee data) {
                employees2.clear();
                employees2.add(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });


        //fetchSupplier(importTicket.getSupplierID());
        int supplierID = importTicket.getSupplierID();
        DAO.SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.readSupplier(supplierID, new QueryResponse<Supplier>() {
            @Override
            public void onSuccess(Supplier data) {
                suppliers2.clear();
                suppliers2.add(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        idImportTicket2 = String.valueOf(importTicket.getImportTicketID());
        try{
            employeeName2 = employees2.get(0).getName();
        }catch (Exception e){
            employeeName2 = "(?) đã xóa";
        }
        try{
            supplierName2 = suppliers2.get(0).getName();
        }catch (Exception e){
            supplierName2 = "(?) đã xóa";
        }
        try{
            supplierAddress2 = suppliers2.get(0).getAddress();
        }catch (Exception e){
            supplierAddress2 = "(?) đã xóa";
        }
        tvCreateDate2 = importTicket.getCreateDate();

        result += "Ngày nhập kho: " + tvCreateDate2 + "\n";
        result += "Mã hóa đơn: " + idImportTicket2 + "\n";
        result += "Địa chỉ kho: " + WarehouseAddress2 + "\n";
        result += "Nhân viên: " + employeeName2 + "\n";
        result += "Tên nhà cung cấp: " + supplierName2 + "\n";
        result += "Địa chỉ: " + supplierAddress2 + "\n";

        return result;
    }

    private void fetchEmployee(int employeeID){
        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.readEmployee(employeeID, new QueryResponse<Employee>() {
            @Override
            public void onSuccess(Employee data) {
                employees.clear();
                employees.add(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void fetchWarehouseList(){
        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
            @Override
            public void onSuccess(List<Warehouse> data) {
                warehouses.clear();
                warehouses.addAll(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void fetchSupplier(int supplierID){
        DAO.SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.readSupplier(supplierID, new QueryResponse<Supplier>() {
            @Override
            public void onSuccess(Supplier data) {
                suppliers.clear();
                suppliers.add(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
