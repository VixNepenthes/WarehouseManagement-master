package com.midterm.proj.warehousemanagement.features.employee.show;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.database.daoImplementation.EmployeeQuery;
import com.midterm.proj.warehousemanagement.features.employee.EmployeeCrudListener;
import com.midterm.proj.warehousemanagement.features.employee.create.CreateEmployeeDialogFragment;
import com.midterm.proj.warehousemanagement.features.employee.update.UpdateEmployeeDialogFragment;
import com.midterm.proj.warehousemanagement.features.observer.Message;
import com.midterm.proj.warehousemanagement.features.observer.Observer;
import com.midterm.proj.warehousemanagement.model.Employee;
import com.midterm.proj.warehousemanagement.util.MyApp;
import com.midterm.proj.warehousemanagement.util.ThirdPartyApp;

import java.util.ArrayList;
import java.util.List;

public class ShowEmployeeFragment extends Fragment implements EmployeeCrudListener {
    private ListView lvEmployeeList;
    public EmployeeAdapter employeeAdapter;
    private FragmentActivity myContext;
    private Button btnAddEmployee;
    private ArrayList<Employee> employees = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employee_list, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
    }

    private void setControl() {
        lvEmployeeList = getView().findViewById(R.id.lv_employee_list);
        btnAddEmployee = getView().findViewById(R.id.btn_create_employee);
        fetchEmployeeList();
    }

    private void fetchEmployeeList() {
        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.readAllEmployee(new QueryResponse<List<Employee>>() {
            @Override
            public void onSuccess(List<Employee> data) {
                employees.addAll(data);
                employeeAdapter = new EmployeeAdapter(getContext(), employees);
                lvEmployeeList.setAdapter(employeeAdapter);
                employeeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void setEvent() {
        btnAddEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEmployeeDialogFragment createEmployeeDialogFragment = CreateEmployeeDialogFragment.newInstance("Thêm nhân viên mới", ShowEmployeeFragment.this);
                createEmployeeDialogFragment.show(getFragmentManager(), "create_warehouse");
            }
        });

        lvEmployeeList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                showOptions(pos);
                return true;
            }
        });

    }

    private void showOptions(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tùy chọn");
        builder.setIcon(R.drawable.staff);
        String[] animals = {"Chỉnh sửa thông tin", "Gọi điện", "Xóa"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Chỉnh sửa thông tin
                        editEmployeeInfo(pos);
                        break;
                    case 1: // Gọi điện
                        String phone = employeeAdapter.getItem(pos).getPhone();
                        ThirdPartyApp.dialPhoneNumber(phone);
                        break;
                    case 2: // Xóa
                        deleteEmployee(pos);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteEmployee(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("CẢNH BÁO");
        builder.setIcon(R.drawable.ic_dangerous);
        builder.setMessage("Bạn chắc muốn xóa nhân viên này chứ?");
        int id = employeeAdapter.getItem(pos).getID_Employee();
        // add the buttons
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
                employeeQuery.deleteEmployee(id, new QueryResponse<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        updateEmployeeList();
                        //crudAllEventListener.onCrudCallback("Đã xóa nhân viên. ID: "+id);
                    }

                    @Override
                    public void onFailure(String message) {
                        Toast.makeText(MyApp.context, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void editEmployeeInfo(int pos) {
        int id = employeeAdapter.getItem(pos).getID_Employee();
        UpdateEmployeeDialogFragment updateEmployeeDialogFragment = UpdateEmployeeDialogFragment.newInstance("Chỉnh sửa thông tin", id, ShowEmployeeFragment.this);
        updateEmployeeDialogFragment.show(getFragmentManager(), "update_employee");

    }

    @Override
    public void onEmployeeListUpdate(boolean isUpdated){
        if(isUpdated)
            updateEmployeeList();
    }

    private void updateEmployeeList() {
        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        employeeQuery.readAllEmployee(new QueryResponse<List<Employee>>() {
            @Override
            public void onSuccess(List<Employee> data) {
                employees.clear();
                employees.addAll(data);
                employeeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }


}
