package com.midterm.proj.warehousemanagement.features.export_ticket.show;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;

import com.midterm.proj.warehousemanagement.R;
import com.midterm.proj.warehousemanagement.database.QueryResponse;
import com.midterm.proj.warehousemanagement.database.daoImplementation.ExportTicketDetailQuery;
import com.midterm.proj.warehousemanagement.database.daoInterface.DAO;
import com.midterm.proj.warehousemanagement.model.ExportTicket;
import com.midterm.proj.warehousemanagement.model.ExportTicketDetail;

import java.util.ArrayList;
import java.util.List;

public class ShowDetailExportTicketFragment extends DialogFragment {
    private ListView lvExportTicketDetailListView;
    private ExportTicketDetailAdapter adapter;
    private static int idExportTicket;

    private ArrayList<ExportTicketDetail> exportTicketsDetailArrayList = new ArrayList<>();


    public ShowDetailExportTicketFragment() {
        // Required empty public constructor
    }


    public static ShowDetailExportTicketFragment newInstance(int pos) {
        ShowDetailExportTicketFragment fragment = new ShowDetailExportTicketFragment();
        idExportTicket = pos;
        Bundle args = new Bundle();
        args.putInt("pos:",(pos));
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_detail_export_ticket, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setControl();
        setEvent();


    }

    private void setEvent() {

        DAO.ExportTicketDetailQuery exportTicketDetailQuery = new ExportTicketDetailQuery();
        exportTicketDetailQuery.readAllExportTicketDetail(idExportTicket, new QueryResponse<List<ExportTicketDetail>>() {
            @Override
            public void onSuccess(List<ExportTicketDetail> data) {
                exportTicketsDetailArrayList.clear();
                exportTicketsDetailArrayList.addAll(data);
                adapter = new ExportTicketDetailAdapter(getContext(), exportTicketsDetailArrayList);
                lvExportTicketDetailListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(String message) {
            }
        });


    }

    private void setControl() {
        lvExportTicketDetailListView =getView().findViewById(R.id.lv_export_ticket_detail_list);
    }
}