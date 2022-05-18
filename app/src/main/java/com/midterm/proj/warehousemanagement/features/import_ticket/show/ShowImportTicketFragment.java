package com.midterm.proj.warehousemanagement.features.import_ticket.show;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ImportTicketQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ProductQuery;
import com.midterm.proj.warehousemanagement.database.daoImplementation.WarehouseQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.features.export_ticket.show.ExportTicketAdapter;
import com.midterm.proj.warehousemanagement.features.export_ticket.show.ShowDetailExportTicketFragment;
import com.midterm.proj.warehousemanagement.model.ImportTicket;
import com.midterm.proj.warehousemanagement.model.Product;
import com.midterm.proj.warehousemanagement.model.Warehouse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShowImportTicketFragment extends Fragment {

    private ListView lvImportTicketListView;
    private Spinner spnWarehouse;
    private ImportTicketAdapter adapter;
    private ArrayList<Warehouse> warehouses = new ArrayList<>();
    private ArrayList<ImportTicket> importTicketArrayList = new ArrayList<>();
    private static int warehouseId;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_import_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();
    }

    private void setEvent() {
        fetchWarehouseList();
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
                fetchImportTicket();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setControl() {
        spnWarehouse = getView().findViewById(R.id.spiner_choose_warehouse);
        lvImportTicketListView = getView().findViewById(R.id.lv_import_ticket_list);
    }
    private void fetchWarehouseList() {
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


    private void showOptions(int idImportTicket, int warehouseId, String content1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Tùy chọn");
        builder.setIcon(R.drawable._warehouse);
        String[] options = {"Chi tiết", "Print Docx"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Chỉnh sửa thông tin
                        ShowDetailImportTicketFragment showDetailImportTicketFragment = ShowDetailImportTicketFragment.newInstance(idImportTicket, warehouseId);
                        showDetailImportTicketFragment.show(getFragmentManager(), "show_detail_import_ticket");
                        break;
                    case 1:
                        String conten2 = "";
                        ArrayList<ImportTicket> importTicketsWarehouseArrayList = new ArrayList<>();
                        ArrayList<ImportTicket> importTicketsDetailArrayList = new ArrayList<>();
                        ArrayList<Product> product = new ArrayList<>();
                        int importTicketId = idImportTicket;
                        int warehouseId2 = warehouseId;
                        DAO.ImportTicketQuery importTicketQuery = new ImportTicketQuery();
                        importTicketQuery.readAllImportTicketFromWarehouse(warehouseId, new QueryResponse<List<ImportTicket>>() {
                            @Override
                            public void onSuccess(List<ImportTicket> data) {
                                importTicketsWarehouseArrayList.clear();
                                importTicketsWarehouseArrayList.addAll(data);

                                for(ImportTicket it: importTicketsWarehouseArrayList){
                                    if(it.getImportTicketID() == importTicketId){
                                        importTicketsDetailArrayList.add(it);
                                    }
                                }

                            }

                            @Override
                            public void onFailure(String message) {

                            }
                        });

                        for(ImportTicket importTicketDetail: importTicketsDetailArrayList){
                            DAO.ProductQuery productQuery = new ProductQuery();
                            productQuery.readProduct(importTicketDetail.getProductID(), new QueryResponse<Product>() {
                                @Override
                                public void onSuccess(Product data) {
                                    product.clear();
                                    product.add(data);
                                }

                                @Override
                                public void onFailure(String message) {

                                }
                            });
                            conten2 += "Tên sản phẩm: " + product.get(0).getName() + "\n";
                            conten2 += "Đơn giá: " + String.valueOf(importTicketDetail.getNumber()) + "\n";
                            conten2 += "Số lượng: " + String.valueOf(product.get(0).getPrice()) + "\n";
                            conten2 += "Thành tiền: " + String.valueOf(product.get(0).getPrice()*importTicketDetail.getNumber()) + "\n";

                        }

                        String content = content1 + conten2;

                        File filePath = null;
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PackageManager.PERMISSION_GRANTED);

                        //String targetDOCX = "/sdcard/showImportTicketDOCX.docx";
                        filePath = new File(getActivity().getExternalFilesDir(null), "showImportTicketDOCX"+idImportTicket+".docx");
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



    private  void fetchImportTicket(){
        DAO.ImportTicketQuery importTicketQuery = new ImportTicketQuery();
        importTicketQuery.readAllImportTicketFromWarehouse(warehouseId, new QueryResponse<List<ImportTicket>>() {
            @Override
            public void onSuccess(List<ImportTicket> data) {
                importTicketArrayList.clear();
                importTicketArrayList.addAll(data);
                if(!importTicketArrayList.isEmpty()){
                    adapter = new ImportTicketAdapter(getContext(), importTicketArrayList);
                    lvImportTicketListView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(String message) {
            }
        });

        lvImportTicketListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromlist = adapter.getContentItem(importTicketArrayList.get(position));
                //Toast.makeText(getActivity(), selectedFromlist, Toast.LENGTH_SHORT).show();
                showOptions(importTicketArrayList.get(position).getImportTicketID(), importTicketArrayList.get(position).getID_Warehouse(), selectedFromlist);
                return false;
            }
        });

    }

}
