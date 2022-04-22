package com.example.mywork2.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.mywork2.R;
import com.google.android.material.tabs.TabLayout;

public class MyPlansDialoge extends AppCompatDialogFragment {
    private int ticketNums;
    private String dialogTitle;
    private String removeOrRefund;
    private View view;
    private String dialogText;
    public TabLayout myPlanInfoRemoveNum;
    public TextView titleDia;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        //dialog context view
        view = layoutInflater.inflate(R.layout.dialog_layout,null);
        //tab item view
        myPlanInfoRemoveNum = view.findViewById(R.id.ticket_dia_count);
        int labelNum = myPlanInfoRemoveNum.getTabCount();
        //show all the labels
        for(int i = labelNum; i < 5; i++){
            TabLayout.Tab tab = myPlanInfoRemoveNum.newTab().setText((i + 1) + "");
            myPlanInfoRemoveNum.addTab(tab, i, false);
        }
        //remove unnecessary labels
        for(int i = 4; i >= getTicketNums(); i--){
            myPlanInfoRemoveNum.removeTabAt(i);
        }
        //textView in dialog
        titleDia = view.findViewById(R.id.dia_title);
        titleDia.setText(getDialogText());
        //build the dialog
        builder.setView(view).setTitle(getDialogTitle())
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .setPositiveButton(getRemoveOrRefund(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("asd",titleDia.getText().toString());
                    }
                });

        return builder.create();
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public void setDialogTitle(String dialogTitle) {
        this.dialogTitle = dialogTitle;
    }

    public String getRemoveOrRefund() {
        return removeOrRefund;
    }

    public void setRemoveOrRefund(String removeOrRefund) {
        this.removeOrRefund = removeOrRefund;
    }

    public int getTicketNums() {
        return ticketNums;
    }

    public void setTicketNums(int ticketNums) {
        this.ticketNums = ticketNums;
    }

    public String getDialogText() {
        return dialogText;
    }

    public void setDialogText(String dialogText) {
        this.dialogText = dialogText;
    }
}
