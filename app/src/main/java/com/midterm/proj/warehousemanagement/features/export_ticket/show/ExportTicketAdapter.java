package com.midterm.proj.warehousemanagement.features.export_ticket.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import com.midterm.proj.warehousemanagement.database.daoImplementation.CustomerQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.EmployeeQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.model.Customer;
import com.midterm.proj.warehousemanagement.model.Employee;
import com.midterm.proj.warehousemanagement.model.ExportTicket;
import com.midterm.proj.warehousemanagement.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class ExportTicketAdapter extends ArrayAdapter<ExportTicket> {
    private Context mContext;
    private ArrayList<ExportTicket> exportTicketsArrayList = new ArrayList<>();
    private ArrayList<Warehouse> warehouses = new ArrayList<>();
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<Customer> customers = new ArrayList<>();

    public ExportTicketAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<ExportTicket> list){
        super(context, 0 , list);
        mContext = context;
        exportTicketsArrayList = list;
    }

    public String getTextItem(ExportTicket exportTicket){
        ArrayList<ExportTicket> exportTicketsArrayList2 = new ArrayList<>();
        ArrayList<Warehouse> warehouses2 = new ArrayList<>();
        ArrayList<Employee> employees2 = new ArrayList<>();
        ArrayList<Customer> customers2 = new ArrayList<>();
        String result = "";
        String idExportTicket, WarehouseAddress="", employeeName, customerName, customerPhone, tvCreateDate;
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
            if(w.getID_Warehouse() == exportTicket.getWarehouseID()){
                WarehouseAddress = w.getAddress();
            }
        }

        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.readEmployee(exportTicket.getEmployeeID(), new QueryResponse<Employee>() {
            @Override
            public void onSuccess(Employee data) {
                employees2.clear();
                employees2.add(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        DAO.CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.readCustomer(exportTicket.getCustomerID(), new QueryResponse<Customer>() {
            @Override
            public void onSuccess(Customer data) {
                customers2.clear();
                customers2.add(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        idExportTicket = String.valueOf(exportTicket.getID_ExportTicket());
        try{
            employeeName = employees2.get(0).getName();
        }catch (Exception e){
            employeeName = "(?) đã xóa";
        }
        try{
            customerName = customers2.get(0).getName();
        }catch (Exception e){
            customerName = "(?) đã xóa";
        }
        try{
            customerPhone = customers.get(0).getPhone();
        }catch (Exception e){
            customerPhone = "(?) đã xóa";
        }
        tvCreateDate = exportTicket.getCreateDate();

        result += "Ngày xuất kho: " + tvCreateDate + "\n";
        result += "Mã hóa đơn: " + idExportTicket + "\n";
        result += "Địa chỉ kho: " + WarehouseAddress + "\n";
        result += "Nhân viên: " + employeeName + "\n";
        result += "Khách hàng: " + customerName + "\n";
        result += "SĐT: " + customerPhone + "\n";

        return  result;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.export_ticket_list_item, parent, false);
        }
        ExportTicket exportTicket = exportTicketsArrayList.get(position);
        TextView idExportTicket = listItem.findViewById(R.id.tv_id_export_ticket);
        TextView WarehouseAddress = listItem.findViewById(R.id.tv_warehouse_address);
        TextView employeeName = listItem.findViewById(R.id.tv_employee_name);
        TextView customerName = listItem.findViewById(R.id.tv_customer_name);
        TextView customerPhone = listItem.findViewById(R.id.tv_customer_phone);
        TextView tvCreateDate = listItem.findViewById(R.id.tv_create_date);
        fetchWarehouseList();
        for(Warehouse w: warehouses){
            if(w.getID_Warehouse() == exportTicket.getWarehouseID()){
                WarehouseAddress.setText(w.getAddress());
            }
        }

        fetchEmployee(exportTicket.getEmployeeID());
        fetchCustomer(exportTicket.getCustomerID());

        idExportTicket.setText(String.valueOf(exportTicket.getID_ExportTicket()));
        try{
            employeeName.setText(employees.get(0).getName());
        }catch (Exception e){
            employeeName.setText("(?) đã xóa");
        }
        try{
            customerName.setText(customers.get(0).getName());
        }catch (Exception e){
            customerName.setText("(?) đã xóa");
        }
        try{
            customerPhone.setText(customers.get(0).getPhone());
        }catch (Exception e){
            customerPhone.setText("(?) đã xóa");
        }
        tvCreateDate.setText(exportTicket.getCreateDate());

        return listItem;
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

    private void fetchCustomer(int customerID){
        DAO.CustomerQuery customerQuery = new CustomerQuery();
        customerQuery.readCustomer(customerID, new QueryResponse<Customer>() {
            @Override
            public void onSuccess(Customer data) {
                customers.clear();
                customers.add(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
