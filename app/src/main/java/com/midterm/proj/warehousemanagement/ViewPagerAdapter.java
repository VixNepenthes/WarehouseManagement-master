package com.midterm.proj.warehousemanagement;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.midterm.proj.warehousemanagement.features.dashboard.DashboardFragment;
import com.midterm.proj.warehousemanagement.features.export_ticket.create.CreateExportTicketFragment;
import com.midterm.proj.warehousemanagement.features.import_ticket.create.CreateImportTicketFragment;
import com.midterm.proj.warehousemanagement.features.login.LogOutFragment;
import com.midterm.proj.warehousemanagement.features.product.create.CreateProductFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DashboardFragment();
            case 1:
                return new CreateImportTicketFragment();
            case 2:
                return new CreateExportTicketFragment();
            case 3:
                return new CreateProductFragment();
//            case 4:
//                return new LogOutFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

}
