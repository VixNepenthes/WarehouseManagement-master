package com.midterm.proj.warehousemanagement.features.employee.manager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.database.daoImplementation.EmployeeQuery;
import com.midterm.proj.warehousemanagement.features.employee.create.CreateEmployeeFragment;
import com.midterm.proj.warehousemanagement.features.employee.show.ShowEmployeeFragment;

public class EmployeeManagerFragment extends Fragment {
    private static int numOfEmployee;
    private FragmentManager fragmentManager;
    private FragmentActivity myContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employee_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    private void setControl() {
        fragmentManager = myContext.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        Bundle args = new Bundle();

        employeeQuery.anyEmployeeCreated(new QueryResponse<Boolean>() {
            @Override
            public void onSuccess(Boolean data) {
                args.putInt("number_of_employee", 0);
                Fragment fragment = new CreateEmployeeFragment();
                fragment.setArguments(args);
                fragmentTransaction.add(R.id.framelayout_employee_manager_container, fragment);
                fragmentTransaction.addToBackStack(null);
            }
            @Override
            public void onFailure(String message) {
                fragmentTransaction.add(R.id.framelayout_employee_manager_container, new ShowEmployeeFragment());
            }
        });
        fragmentTransaction.commit();
    }
}
