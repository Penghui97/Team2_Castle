package com.example.mywork2.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.example.mywork2.MainFragment.MyPlansFragment;
import com.example.mywork2.R;
import com.google.android.material.tabs.TabLayout;
/**
 * @author Jiacheng
 * function: the remove ticket dialog
 * modification date and description can be found in github repository history
 */
public class MyPlansDialoge extends AppCompatDialogFragment {
    //ticket nums that has been saved
    private int ticketNums;
    //the title displayed in the dialog
    private String dialogTitle;
    //remove or refund depends on whether the user has bought the ticket
    private String removeOrRefund;
    private View view;
    // the text content displayed in dialog
    private String dialogText;
    //the nums that user want to remove
    public TextView myPlanInfoRemoveNum;
    // the content displayed textview
    public TextView titleDia;
    private Fragment myFragment;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        //dialog context view
        view = layoutInflater.inflate(R.layout.dialog_layout,null);
        //Textview num
        myPlanInfoRemoveNum = view.findViewById(R.id.ticket_dia_count);
        myPlanInfoRemoveNum.setText(""+getTicketNums());

        //int labelNum = Integer.parseInt(myPlanInfoRemoveNum.getText().toString());

        ImageView add = view.findViewById(R.id.add_count);
        add.setOnClickListener(view1 -> {
            if(Integer.parseInt(myPlanInfoRemoveNum.getText().toString())!=getTicketNums()){
                int i = Integer.parseInt(myPlanInfoRemoveNum.getText().toString())+1;
                myPlanInfoRemoveNum.setText(""+i);
            }
        });

        ImageView remove = view.findViewById(R.id.remove_count);
        remove.setOnClickListener(view1 -> {
            if(Integer.parseInt(myPlanInfoRemoveNum.getText().toString())!=1){
                int i = Integer.parseInt(myPlanInfoRemoveNum.getText().toString())-1;
                myPlanInfoRemoveNum.setText(""+i);
            }
        });

        //textView in dialog
        titleDia = view.findViewById(R.id.dia_title);
        titleDia.setText(getDialogText());
        //build the dialog
        builder.setView(view).setTitle(getDialogTitle())
                .setNegativeButton("cancel", (dialogInterface, i) ->
                        dismiss())
                .setPositiveButton(getRemoveOrRefund(),(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MyPlansFragment myPlansFragment = (MyPlansFragment) getMyFragment();
                        myPlansFragment.returnTicketsInDialog(Integer.parseInt(myPlanInfoRemoveNum.getText().toString()));
                    }
                }));

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

    public Fragment getMyFragment() {
        return myFragment;
    }

    public void setMyFragment(Fragment myFragment) {
        this.myFragment = myFragment;
    }
}
