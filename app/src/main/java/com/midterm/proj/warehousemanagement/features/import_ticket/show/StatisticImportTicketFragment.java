package com.midterm.proj.warehousemanagement.features.import_ticket.show;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ImportTicketQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ProductQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.model.ExportTicket;
import com.midterm.proj.warehousemanagement.model.ImportTicket;
import com.midterm.proj.warehousemanagement.model.Product;
import com.midterm.proj.warehousemanagement.model.Warehouse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatisticImportTicketFragment extends Fragment {
    BarChart chart;
    private ArrayList<Warehouse> allWarehouse = new ArrayList<>();
    private ArrayList<ImportTicket> allImportTicket = new ArrayList<>();
    private ArrayList<Product> product = new ArrayList<>();
    private static int start;
    private static int end;
    private static int month;
    //    private ArrayList<Integer> month = new ArrayList<>();
    private ArrayList<Integer> totalMoneyMoth = new ArrayList<>();

    LinearLayout linearChartImport;
    Bitmap bitmap;
    Button btnPrintPDF;

    private void fetchStatistics(){
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);
        totalMoneyMoth.add(0);


        allWarehouse.clear();
        allImportTicket.clear();

        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        DAO.ImportTicketQuery importTicketQuery = new ImportTicketQuery();
        DAO.ProductQuery productQuery = new ProductQuery();

        warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
            @Override
            public void onSuccess(List<Warehouse> data) {
                allWarehouse.addAll(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });

        for (Warehouse w: allWarehouse){
            importTicketQuery.readAllImportTicketFromWarehouse(w.getID_Warehouse(), new QueryResponse<List<ImportTicket>>() {
                @Override
                public void onSuccess(List<ImportTicket> data) {
                    allImportTicket.addAll(data);
                }

                @Override
                public void onFailure(String message) {

                }
            });
        }

        for(ImportTicket i: allImportTicket){
            start = i.getCreateDate().indexOf("/");
            end = i.getCreateDate().indexOf("/", start +1);
            month = Integer.parseInt(i.getCreateDate().substring(start+1, end)) -1;
            Log.d("TAG", "month: " + String.valueOf(month+1));
//            month.add(Integer.parseInt(i.getCreateDate().substring(start+1,end)));
            productQuery.readProduct(i.getProductID(), new QueryResponse<Product>() {
                @Override
                public void onSuccess(Product data) {
                    product.clear();
                    product.add(data);
                    totalMoneyMoth.set(month, (int) (product.get(0).getPrice()*i.getNumber() + totalMoneyMoth.get(month)));

                }

                @Override
                public void onFailure(String message) {

                }
            });
        }
        Log.d("TAG", "fetchAllImportTicket: "+ totalMoneyMoth);


    }

    private void showBarChart(){
        ArrayList noOfEmp = new ArrayList();
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(0),0));     //1
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(1),1));     //2
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(2),2));     //3
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(3),3));     //4
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(4),4));     //5
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(05),5));     //6
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(6),6));     //7
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(7),7));     //8
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(8),8));     //9
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(9),9));     //10
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(10),10));     //11
        noOfEmp.add(new BarEntry(totalMoneyMoth.get(11),11));     //12

        ArrayList month = new ArrayList();

        month.add("1");month.add("2");month.add("3");month.add("4");month.add("5");month.add("6");month.add("7");month.add("8");month.add("9");month.add("10");month.add("11");month.add("12");

        BarDataSet barDataSet = new BarDataSet(noOfEmp, "No Of product");
        chart.animateY(5000);
        BarData data = new BarData(month, barDataSet);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistic_import_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
    }

    private void setEvent() {
        fetchStatistics();
        showBarChart();

        btnPrintPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("size", ""+linearChartImport.getHeight() + " " + linearChartImport.getWidth());
                bitmap = LoadBitmap(linearChartImport, linearChartImport.getWidth(), linearChartImport.getHeight());
                createPDF();
            }
        });
    }

    private Bitmap LoadBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); //tạo 1 bức ảnh với mỗi pixel lưu 4 byte
        Canvas canvas = new Canvas(bitmap);//nhu 1 to giay co the ve bat cu doi tuong len
        v.draw(canvas);
        return  bitmap;
    }

    private void createPDF(){
        //WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);//lay mat do diem anh
        float width = displayMetrics.widthPixels;
        float height = displayMetrics.heightPixels;
        int convertWidth = (int)width, convertHeight = (int)height;

        PdfDocument document = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHeight, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            Paint paint = new Paint();// pain dinh nghia mau, kieu, kich co
            canvas.drawPaint(paint);
            bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHeight, true);

            canvas.drawBitmap(bitmap, 0, 0, null);
            document.finishPage(page);

            //String targetPDF = "/sdcard/importtTicket.pdf";


            File file;
            file = new File(getActivity().getExternalFilesDir(null), "importtTicket.pdf");
            //file = new File(targetPDF);
            try {
                document.writeTo(new FileOutputStream(file));
            }catch (IOException e){
                e.printStackTrace();
                Toast.makeText(getActivity(), "sai o day1", Toast.LENGTH_SHORT).show();
            }
            document.close();
            Toast.makeText(getActivity(), "success save to Storage", Toast.LENGTH_SHORT).show();
        }

    }

    private void setControl() {
        chart = getView().findViewById(R.id.barchart);
        linearChartImport = getView().findViewById(R.id.linearChartImport);
        btnPrintPDF = getView().findViewById(R.id.btnPrintPDF);
    }


}