package com.midterm.proj.warehousemanagement.features.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.midterm.proj.warehousemanagement.MainActivity;
import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.features.customer.show.ShowCustomerListFragment;
import com.midterm.proj.warehousemanagement.features.employee.manager.EmployeeManagerFragment;
import com.midterm.proj.warehousemanagement.features.product.show.ShowInstockFragment;
import com.midterm.proj.warehousemanagement.features.supplier.show.ShowSupplierListFragment;
import com.midterm.proj.warehousemanagement.features.warehouse.manager.WarehouseManagerFragment;
import com.midterm.proj.warehousemanagement.model.Warehouse;

import java.util.List;

public class DashboardFragment extends Fragment{
    Button btn_instock, btn_tickets_list, btn_warehouse_management, btn_employee_info, btn_customer_info, btn_supplier_info;
    private FragmentActivity myContext;
    LinearLayout dashboardMenu;
    private ActionBar actionBar;
    // Animation
    Animation ani1, ani2, ani3, ani4, ani5, ani6;
    LinearLayout item1, item2, item3, item4, item5, item6;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkEmptyWarehouseList();
        setControl();
        setEvent();

        handleAnimation();
        changeFontDancing();
        changeFontOpensan();
        changeFontItim();
    }

    private void changeFontDancing(){
        Button btn_Dancing = getView().findViewById(R.id.btn_dancing);

        btn_Dancing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface font = ResourcesCompat.getFont(getContext(), R.font.dancing);
                Button product = getView().findViewById(R.id.btn_instock);
                Button tickets_list = getView().findViewById(R.id.btn_tickets_list);
                Button btn_warehouse_management = getView().findViewById(R.id.btn_warehouse_management);
                Button btn_employee_info = getView().findViewById(R.id.btn_employee_info);
                Button btn_customer_info = getView().findViewById(R.id.btn_customer_info);
                Button btn_supplier_info = getView().findViewById(R.id.btn_supplier_info);

                product.setTypeface(font);
                tickets_list.setTypeface(font);
                btn_warehouse_management.setTypeface(font);
                btn_employee_info.setTypeface(font);
                btn_customer_info.setTypeface(font);
                btn_supplier_info.setTypeface(font);

            }
        });
    }

    private void changeFontOpensan() {
        Button btn_opensan = getView().findViewById(R.id.btn_opensan);

        btn_opensan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface font = ResourcesCompat.getFont(getContext(), R.font.opensan);

                Button product = getView().findViewById(R.id.btn_instock);
                Button tickets_list = getView().findViewById(R.id.btn_tickets_list);
                Button btn_warehouse_management = getView().findViewById(R.id.btn_warehouse_management);
                Button btn_employee_info = getView().findViewById(R.id.btn_employee_info);
                Button btn_customer_info = getView().findViewById(R.id.btn_customer_info);
                Button btn_supplier_info = getView().findViewById(R.id.btn_supplier_info);

                product.setTypeface(font);
                tickets_list.setTypeface(font);
                btn_warehouse_management.setTypeface(font);
                btn_employee_info.setTypeface(font);
                btn_customer_info.setTypeface(font);
                btn_supplier_info.setTypeface(font);

            }
        });
    }

    private void changeFontItim() {
        Button btn_itim = getView().findViewById(R.id.btn_itim);

        btn_itim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Typeface font = ResourcesCompat.getFont(getContext(), R.font.itim);

                Button product = getView().findViewById(R.id.btn_instock);
                Button tickets_list = getView().findViewById(R.id.btn_tickets_list);
                Button btn_warehouse_management = getView().findViewById(R.id.btn_warehouse_management);
                Button btn_employee_info = getView().findViewById(R.id.btn_employee_info);
                Button btn_customer_info = getView().findViewById(R.id.btn_customer_info);
                Button btn_supplier_info = getView().findViewById(R.id.btn_supplier_info);

                product.setTypeface(font);
                tickets_list.setTypeface(font);
                btn_warehouse_management.setTypeface(font);
                btn_employee_info.setTypeface(font);
                btn_customer_info.setTypeface(font);
                btn_supplier_info.setTypeface(font);

            }
        });
    }

    private void handleAnimation(){
        ani1 = AnimationUtils.loadAnimation(myContext, R.anim.ani1);
        ani2 = AnimationUtils.loadAnimation(myContext, R.anim.ani2);
        ani3 = AnimationUtils.loadAnimation(myContext, R.anim.ani3);
        ani4 = AnimationUtils.loadAnimation(myContext, R.anim.ani4);
        ani5 = AnimationUtils.loadAnimation(myContext, R.anim.ani5);
        ani6 = AnimationUtils.loadAnimation(myContext, R.anim.ani6);

        item1 = getView().findViewById(R.id.item_main_1);
        item2 = getView().findViewById(R.id.item_main_2);
        item3 = getView().findViewById(R.id.item_main_3);
        item4 = getView().findViewById(R.id.item_main_4);
        item5 = getView().findViewById(R.id.item_main_5);
        item6 = getView().findViewById(R.id.item_main_6);

        ani1.setStartTime(200);
        ani1.setStartOffset(200);
        ani2.setStartOffset(400);
        ani3.setStartOffset(600);
        ani4.setStartOffset(800);
        ani5.setStartOffset(1000);
        ani6.setStartOffset(1200);

        item1.startAnimation(ani1);
        item2.startAnimation(ani2);
        item3.startAnimation(ani3);
        item4.startAnimation(ani4);
        item5.startAnimation(ani5);
        item6.startAnimation(ani6);
    }

    private void checkEmptyWarehouseList() {
        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
            @Override
            public void onSuccess(List<Warehouse> data) {
                if(data.isEmpty()){
                    askForFirstWarehouse();
                }
            }

            @Override
            public void onFailure(String message) {
                askForFirstWarehouse();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        myContext=(FragmentActivity) context;
        super.onAttach(context);
    }

    private void setControl() {
        btn_instock = getView().findViewById(R.id.btn_instock);
        btn_tickets_list = getView().findViewById(R.id.btn_tickets_list);
        btn_warehouse_management = getView().findViewById(R.id.btn_warehouse_management);
        btn_employee_info = getView().findViewById(R.id.btn_employee_info);
        btn_customer_info = getView().findViewById(R.id.btn_customer_info);
        btn_supplier_info = getView().findViewById(R.id.btn_supplier_info);
        dashboardMenu = getView().findViewById(R.id.dashboard_menu);
        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();


    }

    private void setEvent() {
        btn_instock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setTitle("QUẢN LÝ SẢN PHẨM");
                dashboardMenu.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_dashboard_container, new ShowInstockFragment(),"product");
                fragmentTransaction.addToBackStack("product");
                fragmentTransaction.commit();
            }
        });
        btn_tickets_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setTitle("QUẢN LÝ PHIẾU NHẬP/XUẤT");
                dashboardMenu.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_dashboard_container, new ImportExportFragment(),"tickets");
                fragmentTransaction.addToBackStack("tickets");
                fragmentTransaction.commit();
            }
        });
        btn_warehouse_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setTitle("DANH SÁCH NHÀ KHO");
                dashboardMenu.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_dashboard_container, new WarehouseManagerFragment(),"warehouses");
                fragmentTransaction.addToBackStack("warehouses");
                fragmentTransaction.commit();
            }
        });

        btn_employee_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setTitle("QUẢN LÝ NHÂN VIÊN");
                dashboardMenu.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_dashboard_container, new EmployeeManagerFragment(),"employee");
                fragmentTransaction.addToBackStack("employee");
                fragmentTransaction.commit();
            }
        });

        btn_customer_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setTitle("DANH SÁCH KHÁCH HÀNG");
                dashboardMenu.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_dashboard_container, new ShowCustomerListFragment(),"customer");
                fragmentTransaction.addToBackStack("customer");
                fragmentTransaction.commit();
            }
        });

        btn_supplier_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionBar.setTitle("DANH SÁCH NHÀ CUNG CẤP");
                dashboardMenu.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_dashboard_container, new ShowSupplierListFragment(), "supplier");
                fragmentTransaction.addToBackStack("supplier");
                fragmentTransaction.commit();
            }
        });
    }

    private void askForFirstWarehouse(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Danh sách nhà kho trống!");
        builder.setMessage("Hãy bắt đầu bằng cách thêm một nhà kho mới...");
        builder.setIcon(R.drawable._warehouse);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dashboardMenu.setVisibility(View.INVISIBLE);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_dashboard_container, new WarehouseManagerFragment());
                fragmentTransaction.commit();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//
//    @Override
//    public void onCrudCallback(String message){
//        Log.d("onCrudCallback: ", message);
//    }



}
