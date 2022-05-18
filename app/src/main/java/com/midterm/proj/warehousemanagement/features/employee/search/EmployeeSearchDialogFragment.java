package com.midterm.proj.warehousemanagement.features.employee.search;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.appbar.AppBarLayout;
import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.EmployeeQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.features.employee.SearchEmployeeItemListener;
import com.midterm.proj.warehousemanagement.model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.midterm.proj.warehousemanagement.R.id.viewEmployeesToolbar;

public class EmployeeSearchDialogFragment extends DialogFragment {
    private static SearchEmployeeItemListener chooseEmloyeeListener;
    private static String TAG = "EmployeeSearchDialogFragment";
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;

    private int mAppBarState;
    private ListView employeesList;

    EditText mSearchEmployees;
    private AppBarLayout viewEmployeesBar, searchBar;
    private SearchEmployeeAdapter employeesListAdapter;

    public static EmployeeSearchDialogFragment newInstance(SearchEmployeeItemListener listener){
        chooseEmloyeeListener = listener;
        return new EmployeeSearchDialogFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_employee, container, false);
        viewEmployeesBar = view.findViewById(viewEmployeesToolbar);
        searchBar= view.findViewById(R.id.searchToolbar);
        employeesList = view.findViewById(R.id.lv_employeesList);
        mSearchEmployees = view.findViewById(R.id.etSearchProducts);

        setAppBaeState(STANDARD_APPBAR);
        ImageView ivSearchEmployee = (ImageView) view.findViewById(R.id.ivSearchIcon);
        ivSearchEmployee.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: clicked searched icon");
                toggleToolBarState();
            }
        });
        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: clicked back arrow.");
                toggleToolBarState();
            }
        });

        setupEmployeeList();
        return view;
    }

    private void setupEmployeeList() {
        DAO.EmployeeQuery employeeQuery = new EmployeeQuery();
        ArrayList<Employee> employees = new ArrayList<>();
        employeeQuery.readAllEmployee(new QueryResponse<List<Employee>>() {
            @Override
            public void onSuccess(List<Employee> data) {
                employees.addAll(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        employeesListAdapter = new SearchEmployeeAdapter(getContext(),R.layout.layout_employee_item, employees);
        mSearchEmployees.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mSearchEmployees.getText().toString().toLowerCase(Locale.getDefault());
                employeesListAdapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        employeesList.setAdapter(employeesListAdapter);
        employeesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = employees.get(position).getName();
                chooseEmloyeeListener.setEmployeeNameCallback(name);
                getDialog().dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void toggleToolBarState() {
        if (mAppBarState == STANDARD_APPBAR) {
            setAppBaeState(SEARCH_APPBAR);
        } else {
            setAppBaeState(STANDARD_APPBAR);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setAppBaeState(int state) {
        mAppBarState = state;
        if (mAppBarState == STANDARD_APPBAR) {
            searchBar.setVisibility(View.GONE);
            viewEmployeesBar.setVisibility(View.VISIBLE);

            View view = getView();
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                im.hideSoftInputFromWindow(view.getWindowToken(), 0); // make keyboard hide
            } catch (NullPointerException e) {
                //Log.d(TAG, "setAppBaeState: NullPointerException: " + e);
            }
        } else if (mAppBarState == SEARCH_APPBAR) {
            viewEmployeesBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            im.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0); // make keyboard popup

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        setAppBaeState(STANDARD_APPBAR);
    }


}
