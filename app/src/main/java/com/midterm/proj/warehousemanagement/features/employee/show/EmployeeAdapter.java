package com.midterm.proj.warehousemanagement.features.employee.show;

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
import com.midterm.proj.warehousemanagement.model.Employee;
import com.midterm.proj.warehousemanagement.model.Warehouse;

import java.util.ArrayList;

public class EmployeeAdapter extends ArrayAdapter<Employee> {
    private Context mContext;
    private ArrayList<Employee> employees = new ArrayList<>();

    public EmployeeAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Employee> list) {
        super(context, 0 , list);
        mContext = context;
        employees = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.employee_list_item, parent, false);
        Employee employee = employees.get(position);
        TextView employeeName = listItem.findViewById(R.id.tv_employee_name);
        TextView employeePhone = listItem.findViewById(R.id.tv_employee_phonenum);
        TextView employeeID = listItem.findViewById(R.id.tv_employee_id);
        employeeName.setText(employee.getName());
        employeePhone.setText(employee.getPhone());
        employeeID.setText( String.valueOf(employee.getID_Employee()));
        return listItem;
    }
}