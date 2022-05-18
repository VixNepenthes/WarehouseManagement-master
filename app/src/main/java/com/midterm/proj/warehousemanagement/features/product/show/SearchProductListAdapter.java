package com.midterm.proj.warehousemanagement.features.product.show;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.midterm.proj.warehousemanagement.util.BitmapHelper;
import com.midterm.proj.warehousemanagement.util.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchProductListAdapter extends ArrayAdapter<Product> {
    private LayoutInflater mInflater;
    private List<Product> mProducts = null;
    private ArrayList<Product> arrayList; //used for the search bar
    private int layoutResource;
    private Context mContext;
    private String mAppend;

    public SearchProductListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Product> products) {
        super(context, resource, products);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        //mAppend = append;
        this.mProducts = products;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mProducts);
    }

    private static class ViewHolder {
        TextView name;
        ImageView productImage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.productName);
            holder.productImage = convertView.findViewById(R.id.productImage);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        String productName = getItem(position).getName();
        holder.name.setText(productName);
        holder.productImage.setImageBitmap(BitmapFactory.decodeByteArray(getItem(position).getBytesImage(),0,getItem(position).getBytesImage().length));
        return convertView;
    }

    // filter name in Search Bar
    public void filter(String characterText) {
        characterText = characterText.toLowerCase(Locale.getDefault());
        mProducts.clear();
        if (characterText.length() == 0) {
            mProducts.addAll(arrayList);
        } else {
            mProducts.clear();
            for (Product product: arrayList) {
                if (product.getName().toLowerCase(Locale.getDefault()).contains(characterText)) {
                    mProducts.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }
}
