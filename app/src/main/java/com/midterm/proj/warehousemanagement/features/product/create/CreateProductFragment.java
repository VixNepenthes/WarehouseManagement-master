package com.midterm.proj.warehousemanagement.features.product.create;

import androidx.core.content.FileProvider;
import androidx.core.graphics.BitmapCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.BitmapFactory;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ProductQuery;
import com.midterm.proj.warehousemanagement.model.Product;
import com.midterm.proj.warehousemanagement.util.BitmapHelper;
import com.midterm.proj.warehousemanagement.util.MyApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
public class CreateProductFragment extends Fragment {

    private ImageView imgvProductImage;
    private EditText edtProductName;
    private EditText edtProductUnit;
    private EditText edtProductPrice;
    private Button btnAddImage;
    private Button btnAddProduct;
    private String path;
    private static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private Bitmap mResultsBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_product, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setControl(View view) {
        imgvProductImage =  view.findViewById(R.id.imgv_product_image);
        edtProductName = view.findViewById(R.id.edt_create_product_name);
        edtProductUnit = view.findViewById(R.id.edt_create_product_unit);
        edtProductPrice = view.findViewById(R.id.edt_create_product_price);
        btnAddImage = view.findViewById(R.id.btn_add_image);
        btnAddProduct = view.findViewById(R.id.btn_confirm_new_image);
        btnAddProduct.setVisibility(View.INVISIBLE);
    }

    private void setEvent() {
        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_IMAGE_CAPTURE
                    );
                } else {
                    startImageUploadOptions();
                }
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewProduct();
            }
        });

    }

    private void startImageUploadOptions() {
        final CharSequence[] options = {"Chụp từ Camera", "Chọn từ thư viện", "Hủy"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chọn ảnh cho sản phẩm mới");
        builder.setIcon(R.drawable.camera);
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Chụp từ Camera")) {
                    launchCamera();
                } else if (options[item].equals("Chọn từ thư viện")) {
                    launchGalleryImagePicker();
                } else if (options[item].equals("Hủy")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void launchCamera() {
        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        //if (takePictureIntent.resolveActivity() != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapHelper.createTempImageFile(getContext());
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                this.path = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.midterm.proj.warehousemanagement",
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        //}
    }

    private void createNewProduct() {
        // add new product to database
        String name = edtProductName.getText().toString().trim();
        String unit = edtProductUnit.getText().toString().trim();
        String sPrice = edtProductPrice.getText().toString();
        Bitmap bitmap = BitmapFactory.decodeFile(this.path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0, byteArrayOutputStream);
        byte[] bytesImage = byteArrayOutputStream.toByteArray();

        if(productValid(name,unit,sPrice,path)){
            long price = Integer.parseInt(sPrice);
            Product product = new Product(name,unit,0,price,bytesImage);
            DAO.ProductQuery productQuery = new ProductQuery();
            productQuery.createProduct(product, new QueryResponse<Boolean>() {
                @Override
                public void onSuccess(Boolean data) {
                    Toast.makeText(MyApp.context, "Tạo sản phẩm mới thành công!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(MyApp.context, message, Toast.LENGTH_LONG).show();
                }
            });
            resetProductInputField();
        }
    }

    private boolean productValid(String name, String unit, String sPrice, String path) {
        if(name.length()==0){
            Toast.makeText(MyApp.context, "Tên sản phẩm không được trống", Toast.LENGTH_LONG).show();
            return false;
        }else if(unit.length() == 0){
            Toast.makeText(MyApp.context, "Đơn vị tính sản phẩm không được trống", Toast.LENGTH_LONG).show();
            return false;
        }else if (sPrice.length() == 0){
            Toast.makeText(MyApp.context, "Giá sản phẩm không được trống", Toast.LENGTH_LONG).show();
            return false;
        }else if (path.length() == 0){
            Toast.makeText(MyApp.context, "Vui lòng thêm hình ảnh của sản phẩm", Toast.LENGTH_LONG).show();
            return false;
        }else if(checkProductName(name)){
            Toast.makeText(MyApp.context, "Tên sản phẩm trùng, vui lòng kiểm tra lại", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean checkProductName(String name) {
        DAO.ProductQuery productQuery = new ProductQuery();
        ArrayList<Product> products = new ArrayList<>();
        productQuery.readAllProduct(new QueryResponse<List<Product>>() {
            @Override
            public void onSuccess(List<Product> data) {
                products.addAll(products);
            }

            @Override
            public void onFailure(String message) {

            }
        });
        if(products.isEmpty()) return false;
        for(Product p : products){
            if(p.getName().equals(name))
                return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    processAndSetImage();
                    break;
                case REQUEST_IMAGE_GALLERY:
                    if (resultCode == RESULT_OK && data != null) {
                        getImageFromGallery(data);
                    }
                    break;
            }

        }

    }

    private void resetProductInputField() {
        edtProductName.setText("");
        edtProductUnit.setText("");
        edtProductPrice.setText("");
        imgvProductImage.setImageResource(0);
        btnAddProduct.setVisibility(View.INVISIBLE);
    }

    private void processAndSetImage() {
        mResultsBitmap = BitmapHelper.resamplePic(getContext(), this.path);
        imgvProductImage.setImageBitmap(mResultsBitmap);
        btnAddProduct.setVisibility(View.VISIBLE);
    }

    private void getImageFromGallery(Intent data) {
        this.path = getPathFromCameraData(data, this.getActivity());
        if(checkImageSizeLimit(path)){
            imgvProductImage.setImageBitmap(BitmapFactory.decodeFile(this.path));
            btnAddProduct.setVisibility(View.VISIBLE);
        }
        else{
            Toast.makeText(getActivity(), "File quá lớn. Tối đa chỉ 1 MB", Toast.LENGTH_LONG).show();
        }
    }

    private static String getPathFromCameraData(Intent data, Context context) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        Log.d("hihi", "getPathFromCameraData: " + picturePath);

        cursor.close();
//        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        return picturePath;
    }

    private void launchGalleryImagePicker() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
    }

    boolean checkImageSizeLimit(String path){
        File file = new File(path);
        int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
        if(file_size < 1000)// smaller than 1mb, 24kb is saved for better future :D
            return true;
        else
            return false;
    }
}
