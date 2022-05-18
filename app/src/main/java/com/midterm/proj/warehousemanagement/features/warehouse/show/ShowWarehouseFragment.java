package com.midterm.proj.warehousemanagement.features.warehouse.show;

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
import androidx.fragment.app.FragmentManager;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.daoImplementation.EmployeeQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.features.employee.show.ShowEmployeeFragment;
import com.midterm.proj.warehousemanagement.features.employee.update.UpdateEmployeeDialogFragment;
import com.midterm.proj.warehousemanagement.features.warehouse.WarehouseCrudListener;
import com.midterm.proj.warehousemanagement.features.warehouse.create.CreateWarehouseDialogFragment;
import com.midterm.proj.warehousemanagement.features.warehouse.update.UpdateWarehouseDialogFragment;
import com.midterm.proj.warehousemanagement.model.Warehouse;
import com.midterm.proj.warehousemanagement.util.MyApp;
import com.midterm.proj.warehousemanagement.util.ThirdPartyApp;

import java.util.ArrayList;
import java.util.List;

public class ShowWarehouseFragment extends Fragment implements WarehouseCrudListener {
    private ListView lvWarehouseList;
    public WarehouseAdapter warehouseAdapter;
    private FragmentManager fragmentManager;
    private FragmentActivity myContext;
    private Button btnAddWarehouse;
    private ArrayList<Warehouse> warehouses = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_warehouse_list, container, false);
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
        lvWarehouseList = getView().findViewById(R.id.lv_warehouse_list);
        btnAddWarehouse = getView().findViewById(R.id.btn_create_warehouse);
        fragmentManager = myContext.getSupportFragmentManager();

        fetchWarehouseList();
    }

    private void setEvent() {
        btnAddWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateWarehouseDialogFragment createWarehouseDialogFragment = CreateWarehouseDialogFragment.newInstance("Tạo kho mới", ShowWarehouseFragment.this);
                createWarehouseDialogFragment.show(getFragmentManager(), "create_warehouse");
            }
        });

        lvWarehouseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                showOptions(pos);
                return true;
            }
        });
    }

    public void fetchWarehouseList(){
        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
            @Override
            public void onSuccess(List<Warehouse> data) {
                warehouses.addAll(data);
                warehouseAdapter = new WarehouseAdapter(getContext(), warehouses);
                lvWarehouseList.setAdapter(warehouseAdapter);
                warehouseAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    private void showOptions(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tùy chọn");
        builder.setIcon(R.drawable._warehouse);
        String[] options = {"Chỉnh sửa thông tin", "Tìm kiếm trên Google Map", "Xóa"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Chỉnh sửa thông tin
                        editWarehouseInfo(pos);
                        break;
                    case 1: // Google Map
                        String address = warehouseAdapter.getItem(pos).getAddress();
                        ThirdPartyApp.googlemapSearchForAddress(address);
                        break;
                    case 2: // Xóa
                        deleteWarehouse(pos);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteWarehouse(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("CẢNH BÁO");
        builder.setIcon(R.drawable.ic_dangerous);
        builder.setMessage("Bạn chắc muốn xóa kho này chứ?");
        int id = warehouseAdapter.getItem(pos).getID_Warehouse();
        // add the buttons
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
                warehouseQuery.deleteWarehouse(id, new QueryResponse<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        updateWarehouseList();
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

    private void editWarehouseInfo(int pos) {
        int id = warehouseAdapter.getItem(pos).getID_Warehouse();
        UpdateWarehouseDialogFragment updateWarehouseDialogFragment = UpdateWarehouseDialogFragment.newInstance("Chỉnh sửa thông tin", id, ShowWarehouseFragment.this);
        updateWarehouseDialogFragment.show(getFragmentManager(), "update_warehouse");

    }

    private void updateWarehouseList(){
        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
            @Override
            public void onSuccess(List<Warehouse> data) {
                warehouses.clear();
                warehouses.addAll(data);
                warehouseAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @Override
    public void onWarehouseListUpdate(boolean isUpdated){
        if(isUpdated)
            updateWarehouseList();
    }
}
