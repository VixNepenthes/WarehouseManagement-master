package com.midterm.proj.warehousemanagement.features.product.search;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.midterm.proj.warehousemanagement.database.daoImplementation.ProductQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.features.product.SearchProductItemListener;
import com.midterm.proj.warehousemanagement.features.product.show.SearchProductListAdapter;
import com.midterm.proj.warehousemanagement.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductSearchDialogFragment extends DialogFragment {
    private static SearchProductItemListener chooseProductListener;
    private static String TAG = "ProductSearchDialogFragment";
    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;

    private int mAppBarState;
    private ListView productsList;

    EditText mSearchProducts;
    private AppBarLayout viewProductsBar, searchBar;
    private SearchProductListAdapter productListAdapter;

    public static ProductSearchDialogFragment newInstance(SearchProductItemListener listener){
        chooseProductListener = listener;
        return new ProductSearchDialogFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_product, container, false);
        viewProductsBar = view.findViewById(R.id.viewProductsToolbar);
        searchBar= view.findViewById(R.id.searchToolbar);
        productsList = view.findViewById(R.id.lv_productsList);
        mSearchProducts = view.findViewById(R.id.etSearchProducts);

        setAppBaeState(STANDARD_APPBAR);
        ImageView ivSearchProduct = (ImageView) view.findViewById(R.id.ivSearchIcon);
        ivSearchProduct.setOnClickListener(new View.OnClickListener() {
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

        setupProductList();
        return view;
    }

    private void setupProductList() {
        DAO.ProductQuery productQuery = new ProductQuery();
        ArrayList<Product> products = new ArrayList<>();
        productQuery.readAllProduct(new QueryResponse<List<Product>>() {
            @Override
            public void onSuccess(List<Product> data) {
                products.addAll(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });
        productListAdapter = new SearchProductListAdapter(getActivity(), R.layout.layout_product_item, products);
        mSearchProducts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mSearchProducts.getText().toString().toLowerCase(Locale.getDefault());
                productListAdapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        productsList.setAdapter(productListAdapter);
        productsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = products.get(position).getName();
                chooseProductListener.setProductNameCallback(name);
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
            viewProductsBar.setVisibility(View.VISIBLE);

            View view = getView();
            InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                im.hideSoftInputFromWindow(view.getWindowToken(), 0); // make keyboard hide
            } catch (NullPointerException e) {
                //Log.d(TAG, "setAppBaeState: NullPointerException: " + e);
            }
        } else if (mAppBarState == SEARCH_APPBAR) {
            viewProductsBar.setVisibility(View.GONE);
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
