package com.midterm.proj.warehousemanagement.features.supplier.show;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.CustomerQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.SupplierQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.features.customer.show.ShowCustomerListAdapter;
import com.midterm.proj.warehousemanagement.features.employee.update.UpdateEmployeeDialogFragment;
import com.midterm.proj.warehousemanagement.features.supplier.SupplierCrudListener;
import com.midterm.proj.warehousemanagement.features.supplier.update.UpdateSupplierDialogFragment;
import com.midterm.proj.warehousemanagement.features.warehouse.show.ShowWarehouseFragment;
import com.midterm.proj.warehousemanagement.model.Customer;
import com.midterm.proj.warehousemanagement.model.Supplier;
import com.midterm.proj.warehousemanagement.model.Warehouse;
import com.midterm.proj.warehousemanagement.util.MyApp;
import com.midterm.proj.warehousemanagement.util.ThirdPartyApp;

import java.util.ArrayList;
import java.util.List;

public class ShowSupplierListFragment extends Fragment implements SupplierCrudListener {
    private ListView lvSupplierList;
    private ShowSupplierListAdapter adapter;
    private ArrayList<Supplier> supplierArrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_supplier_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
    }

    private void setControl() {
        lvSupplierList = getView().findViewById(R.id.lv_supplier_list);
        fetchSupplierList();
    }

    private void setEvent() {
        lvSupplierList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        builder.setIcon(R.drawable._warehouse);
        String[] options = {"Chỉnh sửa thông tin", "Tìm kiếm trên Google Map", "Xóa"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Chỉnh sửa thông tin
                        editSupplierInfo(pos);
                        break;
                    case 1: // Google Map
                        String address = adapter.getItem(pos).getAddress();
                        ThirdPartyApp.googlemapSearchForAddress(address);
                        break;
                    case 2: // Xóa
                        deleteSupplier(pos);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteSupplier(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("CẢNH BÁO");
        builder.setIcon(R.drawable.ic_dangerous);
        builder.setMessage("Bạn chắc muốn xóa Nhà cung cấp này chứ?");
        int id = adapter.getItem(pos).getID_Supplier();
        // add the buttons
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DAO.SupplierQuery supplierQuery = new SupplierQuery();
                supplierQuery.deleteSupplier(id, new QueryResponse<Boolean>() {
                    @Override
                    public void onSuccess(Boolean data) {
                        updateSupplierList();
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

    private void editSupplierInfo(int pos) {
        int id = adapter.getItem(pos).getID_Supplier();
        UpdateSupplierDialogFragment updateSupplierDialogFragment = UpdateSupplierDialogFragment.newInstance("Chỉnh sửa thông tin", id, ShowSupplierListFragment.this);
        updateSupplierDialogFragment.show(getFragmentManager(), "update_supplier");

    }

    public void fetchSupplierList(){
        DAO.SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.readAllSupplier(new QueryResponse<List<Supplier>>() {
            @Override
            public void onSuccess(List<Supplier> data) {
                supplierArrayList.addAll(data);
                adapter = new ShowSupplierListAdapter(getContext(), supplierArrayList);
                lvSupplierList.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }

    @Override
    public void onSupplierListUpdate(boolean isUpdated){
        if(isUpdated)
            updateSupplierList();
    }

    private void updateSupplierList() {
        DAO.SupplierQuery supplierQuery = new SupplierQuery();
        supplierQuery.readAllSupplier(new QueryResponse<List<Supplier>>() {
            @Override
            public void onSuccess(List<Supplier> data) {
                supplierArrayList.clear();
                supplierArrayList.addAll(data);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}
