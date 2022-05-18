package com.midterm.proj.warehousemanagement.features.export_ticket.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ProductQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.model.ExportTicket;
import com.midterm.proj.warehousemanagement.model.ExportTicketDetail;
import com.midterm.proj.warehousemanagement.model.Product;

import java.util.ArrayList;

public class ExportTicketDetailAdapter extends ArrayAdapter<ExportTicketDetail> {
    private Context mContext;
    TextView tvIdExportTicket, tvProductName, tvNumber, tvPriceUnit, tvTotal;
    private ArrayList<ExportTicketDetail> exportTicketsDetailArrayList = new ArrayList<>();
    private ArrayList<Product> products = new ArrayList<>();

    public ExportTicketDetailAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<ExportTicketDetail> list){
        super(context, 0 , list);
        mContext = context;
        exportTicketsDetailArrayList = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.export_ticket_detail_list_item, parent, false);
        }
        ExportTicketDetail exportTicketDetail = exportTicketsDetailArrayList.get(position);

        tvIdExportTicket = listItem.findViewById(R.id.tv_id_export_ticket);
        tvProductName = listItem.findViewById(R.id.tv_product_name);
        tvNumber = listItem.findViewById(R.id.tv_product_number);
        tvPriceUnit = listItem.findViewById(R.id.tv_price_unit);
        tvTotal = listItem.findViewById(R.id.tv_total);
        fetchProduct(exportTicketDetail.getID_Product());
        tvIdExportTicket.setText(String.valueOf(exportTicketDetail.getID_ExportTicket()));
        tvProductName.setText(products.get(0).getName());
        tvNumber.setText(String.valueOf(exportTicketDetail.getNumber()));
        tvPriceUnit.setText(String.valueOf(exportTicketDetail.getPricePerUnit()));
        tvTotal.setText(String.valueOf(exportTicketDetail.getNumber()*exportTicketDetail.getPricePerUnit()));

        return listItem;
    }

    private void fetchProduct(int productId){
        DAO.ProductQuery productQuery = new ProductQuery();
        productQuery.readProduct(productId, new QueryResponse<Product>() {
            @Override
            public void onSuccess(Product data) {
                products.clear();
                products.add(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });

    }
}
