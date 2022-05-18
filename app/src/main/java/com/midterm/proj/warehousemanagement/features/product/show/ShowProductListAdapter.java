package com.midterm.proj.warehousemanagement.features.product.show;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.model.Product;
import com.midterm.proj.warehousemanagement.model.Warehouse;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowProductListAdapter extends ArrayAdapter<Product> {
    private Context mContext;
    private ArrayList<Product> productArrayList = new ArrayList<>();

    public ShowProductListAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Product> list){
        super(context, 0 , list);
        mContext = context;
        productArrayList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.product_list_item, parent, false);
        }
        Product product = productArrayList.get(position);
        TextView name = listItem.findViewById(R.id.tv_product_name);
        TextView unit = listItem.findViewById(R.id.tv_product_unit);
        TextView number=listItem.findViewById(R.id.tv_product_number);
        ImageView image=listItem.findViewById(R.id.imgv_product_image);
        name.setText(product.getName());
        unit.setText(product.getUnit());
        number.setText(String.valueOf(product.getNumber()));
        byte[] imgBytes = product.getBytesImage();
        image.setImageBitmap(BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.length));
        return listItem;
    }
}
