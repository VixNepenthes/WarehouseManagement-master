package com.midterm.proj.warehousemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.midterm.proj.warehousemanagement.database.SqliteDatabaseHelper;
import com.midterm.proj.warehousemanagement.features.customer.show.ShowCustomerListFragment;
import com.midterm.proj.warehousemanagement.features.dashboard.DashboardFragment;
import com.midterm.proj.warehousemanagement.features.dashboard.ImportExportFragment;
import com.midterm.proj.warehousemanagement.features.dashboard.NotificationDisplayService;
import com.midterm.proj.warehousemanagement.features.employee.manager.EmployeeManagerFragment;
import com.midterm.proj.warehousemanagement.features.export_ticket.create.CreateExportTicketFragment;
import com.midterm.proj.warehousemanagement.features.import_ticket.create.CreateImportTicketFragment;
import com.midterm.proj.warehousemanagement.features.login.LogOutFragment;
import com.midterm.proj.warehousemanagement.features.product.create.CreateProductFragment;
import com.midterm.proj.warehousemanagement.features.product.show.ShowInstockFragment;
import com.midterm.proj.warehousemanagement.features.supplier.show.ShowSupplierListFragment;
import com.midterm.proj.warehousemanagement.features.warehouse.manager.WarehouseManagerFragment;
import com.midterm.proj.warehousemanagement.model.Warehouse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static ActionBar actionBar;
    public static List<Warehouse> warehouses = new ArrayList<>();
    BottomNavigationView bottomNavigationView;
    private ListView listView;
    private Activity mActivity;
    private Context mContext;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    LinearLayout dashboardMenu;
    // Animation
    private Animation topAnimation, bottomAnimation;

    private static final int MY_PERMISSIONS_REQUEST_CODE = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        mActivity = MainActivity.this;
        checkPermission();
        setDatabase();
        setControl();
        setEvent();

        // Animation
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.ani1);
    }

    private void setDatabase() {
        SqliteDatabaseHelper databaseHelper = SqliteDatabaseHelper.getInstance();
        String dbName = databaseHelper.getDatabaseName();
    }

    @Override
    public void onBackPressed() {
        actionBar.setTitle("TRANG CHỦ");
        int id = bottomNavigationView.getSelectedItemId();
        if(id != R.id.item_warehouses_management){
            viewPager.setCurrentItem(0);
        }
        else{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            String[] tags = {"product", "tickets","warehouses","employee","customer","supplier"};
            for(int i = 0 ; i < tags.length; i++){
                Fragment current = getSupportFragmentManager().findFragmentByTag(tags[i]);
                if(current instanceof ShowInstockFragment
                        || current instanceof ImportExportFragment
                        || current instanceof WarehouseManagerFragment
                        || current instanceof EmployeeManagerFragment
                        || current instanceof ShowCustomerListFragment
                        || current instanceof ShowSupplierListFragment)
                {
                    getSupportFragmentManager().popBackStackImmediate();
                    finish();
                    startActivity(getIntent());
                }
            }


        }
        //super.onBackPressed();
    }

    private void setEvent() {
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        actionBar.setTitle("TRANG CHỦ");
                        bottomNavigationView.getMenu().findItem(R.id.item_warehouses_management).setChecked(true);
                        break;
                    case 1:
                        actionBar.setTitle("TẠO PHIẾU NHẬP KHO");
                        bottomNavigationView.getMenu().findItem(R.id.item_import_ticket).setChecked(true);
                        break;
                    case 2:
                        actionBar.setTitle("TẠO PHIẾU XUẤT KHO");
                        bottomNavigationView.getMenu().findItem(R.id.item_export_ticket).setChecked(true);
                        break;
                    case 3:
                        actionBar.setTitle("THÊM SẢN PHẨM MỚI");
                        bottomNavigationView.getMenu().findItem(R.id.item_add_product).setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setControl() {
        actionBar = getSupportActionBar();
        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(0);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setItemIconTintList(null);
        dashboardMenu = (LinearLayout) findViewById(R.id.dashboard_menu);
        actionBar.setTitle("TRANG CHỦ");
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint({"ResourceType", "NonConstantResourceId"})
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.item_warehouses_management:
                            actionBar.setTitle("TRANG CHỦ");
                            Intent startNotificationServiceIntent = new Intent(MainActivity.this, NotificationDisplayService.class);
                            startService(startNotificationServiceIntent);
                            //selectedFragment = new DashboardFragment();
                            //viewPagerAdapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.item_import_ticket:
                            actionBar.setTitle("TẠO PHIẾU NHẬP KHO");
                            //selectedFragment = new CreateImportTicketFragment();
                            //viewPagerAdapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.item_export_ticket:
                            actionBar.setTitle("TẠO PHIẾU XUẤT KHO");
                            //selectedFragment = new CreateExportTicketFragment();
                            //viewPagerAdapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(2);
                            break;
                        case R.id.item_add_product:
                            actionBar.setTitle("THÊM SẢN PHẨM MỚI");
                            //selectedFragment = new CreateProductFragment();
                            //viewPagerAdapter.notifyDataSetChanged();
                            viewPager.setCurrentItem(3);
                            break;


                    }
                    //assert selectedFragment != null;
                    //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };



    protected void checkPermission(){
        if(ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(
                mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,Manifest.permission.CAMERA)
                    ||  ActivityCompat.shouldShowRequestPermissionRationale(
                    mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setMessage("Yêu cầu quyền truy cập Camera và bộ nhớ để" +
                        " thực thi chương trình.");
                builder.setTitle("Vui lòng cấp quyền cho ứng dụng");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(
                                mActivity,
                                new String[]{
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                                },
                                MY_PERMISSIONS_REQUEST_CODE
                        );
                    }
                });
                builder.setNeutralButton("Cancel",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                ActivityCompat.requestPermissions(
                        mActivity,
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MY_PERMISSIONS_REQUEST_CODE
                );
            }
        }else {
            //Toast.makeText(mContext,"Permissions already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CODE:{
                if(
                        (grantResults.length >0) &&
                                (grantResults[0]
                                        + grantResults[1]

                                        == PackageManager.PERMISSION_GRANTED
                                )
                ){
                    //Toast.makeText(mContext,"Permissions granted.",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(mContext,"Permissions denied.",Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.kho:
                actionBar.setTitle("TRANG CHỦ");
                Intent startNotificationServiceIntent = new Intent(MainActivity.this, NotificationDisplayService.class);
                startService(startNotificationServiceIntent);
                bottomNavigationView.getMenu().findItem(R.id.item_warehouses_management).setChecked(true);
                viewPager.setCurrentItem(0);
                break;
            case R.id.nhapKho:
                actionBar.setTitle("TẠO PHIẾU NHẬP KHO");
                bottomNavigationView.getMenu().findItem(R.id.item_warehouses_management).setChecked(true);
                viewPager.setCurrentItem(1);
                break;
            case R.id.xuatKho:
                bottomNavigationView.getMenu().findItem(R.id.item_export_ticket).setChecked(true);
                viewPager.setCurrentItem(2);
                break;
            case R.id.sanPham:
                bottomNavigationView.getMenu().findItem(R.id.item_add_product).setChecked(true);
                viewPager.setCurrentItem(3);
                break;
            case R.id.dangxuat:
                Intent i = new Intent(MainActivity.this, LogOutFragment.class);
                startActivity(i);
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}