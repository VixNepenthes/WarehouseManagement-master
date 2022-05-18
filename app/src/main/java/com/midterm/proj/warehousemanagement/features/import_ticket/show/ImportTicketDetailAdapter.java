package com.midterm.proj.warehousemanagement.features.import_ticket.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ProductQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.model.ExportTicketDetail;
import com.midterm.proj.warehousemanagement.model.ImportTicket;
import com.midterm.proj.warehousemanagement.model.Product;

import java.util.ArrayList;


public class ImportTicketDetailAdapter extends ArrayAdapter<ImportTicket> {

    private Context mContext;
    TextView tvProductName, tvNumber, tvPriceUnit, tvTotal;
    private ArrayList<ImportTicket> importTicketsDetailArrayList = new ArrayList<>();
    private ArrayList<Product> product = new ArrayList<>();


    public ImportTicketDetailAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<ImportTicket> list){
        super(context, 0 , list);
        mContext = context;
        importTicketsDetailArrayList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.import_ticket_detail_list_item, parent, false);
        }
        ImportTicket importTicketDetail = importTicketsDetailArrayList.get(position);
        tvProductName = listItem.findViewById(R.id.tv_product_name);
        tvNumber = listItem.findViewById(R.id.tv_product_number);
        tvPriceUnit = listItem.findViewById(R.id.tv_price_unit);
        tvTotal = listItem.findViewById(R.id.tv_total);

        fetchProduct(importTicketDetail.getProductID());

        tvProductName.setText(product.get(0).getName());
        tvNumber.setText(String.valueOf(importTicketDetail.getNumber()));
        tvPriceUnit.setText(String.valueOf(product.get(0).getPrice()));
        tvTotal.setText(String.valueOf(product.get(0).getPrice()*importTicketDetail.getNumber()));

        return listItem;
    }

    private void fetchProduct(int productID){
        DAO.ProductQuery productQuery = new ProductQuery();
        productQuery.readProduct(productID, new QueryResponse<Product>() {
            @Override
            public void onSuccess(Product data) {
                product.clear();
                product.add(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });
    }
}