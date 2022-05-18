package com.midterm.proj.warehousemanagement.features.employee.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchEmployeeAdapter extends ArrayAdapter<Employee> {
    private LayoutInflater mInflater;
    private Context mContext;
    private int layoutResource;
    private List<Employee> mEmployees = null;
    private ArrayList<Employee> arrayList;

    public SearchEmployeeAdapter(@NonNull Context context, int resource , List<Employee> employees) {
        super(context, resource , employees);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.mEmployees = employees;

        arrayList = new ArrayList<>();
        this.arrayList.addAll(mEmployees);
    }
    private static class ViewHolder {
        TextView name;
        TextView employeePhone;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new SearchEmployeeAdapter.ViewHolder();
            holder.name = convertView.findViewById(R.id.EmployeeName);
            holder.employeePhone = convertView.findViewById(R.id.EmployeePhone);
            convertView.setTag(holder);
        }
        else{
            holder = (SearchEmployeeAdapter.ViewHolder)convertView.getTag();
        }

        String EmployeePhone = getItem(position).getPhone();
        holder.employeePhone.setText(EmployeePhone);
        String EmployeeName = getItem(position).getName();
        holder.name.setText(EmployeeName);
        Log.d("TAG", String.valueOf(position));
        Log.d("TAG", "phone: "+ EmployeePhone);
        Log.d("TAG", "phone: "+ EmployeeName);


        return convertView;
    }

    public void filter(String characterText) {
        characterText = characterText.toLowerCase(Locale.getDefault());
        mEmployees.clear();
        if (characterText.length() == 0) {
            mEmployees.addAll(arrayList);
        } else {
            mEmployees.clear();
            for (Employee employee: arrayList) {
                if (employee.getName().toLowerCase(Locale.getDefault()).contains(characterText)) {
                    mEmployees.add(employee);
                }
            }
        }
        notifyDataSetChanged();
    }
}
