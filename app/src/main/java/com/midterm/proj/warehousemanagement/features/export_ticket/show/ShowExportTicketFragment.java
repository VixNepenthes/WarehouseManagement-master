package com.midterm.proj.warehousemanagement.features.export_ticket.show;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ExportTicketDetailQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ExportTicketQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ImportTicketQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ProductQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.model.ExportTicket;
import com.midterm.proj.warehousemanagement.model.ExportTicketDetail;
import com.midterm.proj.warehousemanagement.model.ImportTicket;
import com.midterm.proj.warehousemanagement.model.Product;
import com.midterm.proj.warehousemanagement.model.Warehouse;
import com.midterm.proj.warehousemanagement.util.ThirdPartyApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class ShowExportTicketFragment extends Fragment {
    private ListView lvExportTicketListView;
    private Spinner spnWarehouse;
    private ExportTicketAdapter adapter;
    private ArrayList<Warehouse> warehouses = new ArrayList<>();
    private ArrayList<ExportTicket> exportTicketsArrayList = new ArrayList<>();
    private static int warehouseId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_export_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
    }

    private void setEvent() {
        fetchWarehouseList();
        final int[] WarehouseId = {-1};
        ArrayList<String> warehouseNameList = new ArrayList<>();
        for(Warehouse w:warehouses){
            warehouseNameList.add(w.getName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getView().getContext(), R.layout.spiner_item,warehouseNameList);
        dataAdapter.setDropDownViewResource(R.layout.custom_spiner_item);

        spnWarehouse.setAdapter(dataAdapter);
        spnWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                warehouseId = (warehouses.get(i).getID_Warehouse());
                fetchExportTicket();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setControl() {
        spnWarehouse = getView().findViewById(R.id.spiner_choose_warehouse);
        lvExportTicketListView = getView().findViewById(R.id.lv_export_ticket_list);
    }

    private void fetchWarehouseList(){
        DAO.WarehouseQuery warehouseQuery = new WarehouseQuery();
        warehouseQuery.readAllWarehouse(new QueryResponse<List<Warehouse>>() {
            @Override
            public void onSuccess(List<Warehouse> data) {
                warehouses.clear();
                warehouses.addAll(data);
            }

            @Override
            public void onFailure(String message) {

            }
        });


    }

    private void showOptions(int idExportTicket, String content1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tùy chọn");
        builder.setIcon(R.drawable._warehouse);
        String[] options = {"Chi tiết", "Print Docx"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Chỉnh sửa thông tin
                        ShowDetailExportTicketFragment showDetailExportTicketFragment = ShowDetailExportTicketFragment.newInstance(idExportTicket);
                        showDetailExportTicketFragment.show(getFragmentManager(),"show_detail_export_ticket");
                        break;
                    case 1:
                        String content2 = "";
                        //private ListView lvExportTicketDetailListView;
                        //private ExportTicketDetailAdapter adapter;
                        ArrayList<Product> products3 = new ArrayList<>();
                        ArrayList<ExportTicketDetail> exportTicketsDetailArrayList3 = new ArrayList<>();
                        DAO.ExportTicketDetailQuery exportTicketDetailQuery3 = new ExportTicketDetailQuery();
                        exportTicketDetailQuery3.readAllExportTicketDetail(idExportTicket, new QueryResponse<List<ExportTicketDetail>>() {
                            @Override
                            public void onSuccess(List<ExportTicketDetail> data) {
                                exportTicketsDetailArrayList3.clear();
                                exportTicketsDetailArrayList3.addAll(data);
                            }

                            @Override
                            public void onFailure(String message) {
                            }
                        });

                        for(ExportTicketDetail exportTicketDetail:exportTicketsDetailArrayList3){
                            DAO.ProductQuery productQuery = new ProductQuery();
                            productQuery.readProduct(exportTicketDetail.getID_Product(), new QueryResponse<Product>() {
                                @Override
                                public void onSuccess(Product data) {
                                    products3.clear();
                                    products3.add(data);
                                }

                                @Override
                                public void onFailure(String message) {

                                }
                            });

                            content2 += "Id phiếu xuất: " + String.valueOf(exportTicketDetail.getID_ExportTicket()) +"\n";
                            content2 += "Tên sản phẩm: " + products3.get(0).getName() +"\n";
                            content2 += "Đơn giá: " + String.valueOf(exportTicketDetail.getPricePerUnit()) +"\n";
                            content2 += "Số lượng: "+ String.valueOf(exportTicketDetail.getNumber()) + "\n";
                            content2 += "Thành tiền: "+ String.valueOf(exportTicketDetail.getNumber()*exportTicketDetail.getPricePerUnit()) + "\n";

                        }

                        String content = content1 + content2;

                        File filePath = null;
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PackageManager.PERMISSION_GRANTED);

                        //String targetDOCX = "/sdcard/showImportTicketDOCX.docx";
                        filePath = new File(getActivity().getExternalFilesDir(null), "showExportTicketDOCX"+idExportTicket+".docx");
                        //filePath = new File(targetDOCX);

                        try {
                            if (!filePath.exists()){
                                filePath.createNewFile();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        try {
                            XWPFDocument xwpfDocument = new XWPFDocument();
                            XWPFParagraph xwpfParagraph = xwpfDocument.createParagraph();
                            XWPFRun xwpfRun = xwpfParagraph.createRun();

                            if(content.contains("\n")){
                                String[] lines = content.split("\n");
                                xwpfRun.setText(lines[0], 0);
                                for(int i = 1; i < lines.length; i++){
                                    xwpfRun.addBreak();
                                    xwpfRun.setText(lines[i]);
                                }
                            }else{
                                xwpfRun.setText(content, 0);
                            }

//                            xwpfRun.setText(editTextInput.getText().toString() + "\n" +"hehehehe");
//                            xwpfRun.addBreak();
//                            xwpfRun.setText("ba may");
                            xwpfRun.setFontSize(24);

                            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                            xwpfDocument.write(fileOutputStream);

                            if (fileOutputStream!=null){
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            }
                            xwpfDocument.close();
                            Toast.makeText(getActivity(), "success save to Storage", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void fetchExportTicket() {
        DAO.ExportTicketQuery exportTicketQuery = new ExportTicketQuery();
        exportTicketQuery.readAllExportTicket(warehouseId , new QueryResponse<List<ExportTicket>>() {
            @Override
            public void onSuccess(List<ExportTicket> data) {
                exportTicketsArrayList.clear();
                exportTicketsArrayList.addAll(data);
                if(!exportTicketsArrayList.isEmpty()){
                    adapter = new ExportTicketAdapter(getContext(), exportTicketsArrayList);
                    lvExportTicketListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(String message) {
            }
        });

        lvExportTicketListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selec = adapter.getTextItem(exportTicketsArrayList.get(position));
                showOptions(exportTicketsArrayList.get(position).getID_ExportTicket(), selec);
                return false;
            }
        });
    }
}