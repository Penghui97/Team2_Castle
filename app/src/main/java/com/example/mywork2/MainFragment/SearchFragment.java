package com.example.mywork2.MainFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mywork2.MainActivity;
import com.example.mywork2.R;
import com.example.mywork2.SearchPlanDetailsActivity;
import com.example.mywork2.SearchPlans;
import com.example.mywork2.domain.User;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class SearchFragment extends Fragment {

    private TextView mDisplayDate;
    private TextView mDisplayTime;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private View view;
    private User user;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            user = mainActivity.user;
        }

        setDepartureTime();

        Button findPlans = view.findViewById(R.id.find_plans);
        findPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SearchPlans.startSearchPlansActivity(getActivity());
                toPlanDetails();
            }
        });

        return view;

    }

    public void setDepartureTime(){
        mDisplayDate = view.findViewById(R.id.departure_date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        com.google.android.material.R.style.Theme_AppCompat_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month+1;
                String m = "";
                if(month<10){
                    m+= 0;
                }
                m +=month;

                String d = "";
                if(day<10){
                    d+= 0;
                }
                d +=day;

                String date = year+"/"+m+"/"+d;
                mDisplayDate.setText(date);
            }
        };

        mDisplayTime = view.findViewById(R.id.departure_time);

        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int min = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        getActivity(),
                        com.google.android.material.R.style.Theme_AppCompat_Dialog_MinWidth,
                        onTimeSetListener,
                        hour,min,true
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }

        });

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String time = i+":" +i1;
                mDisplayTime.setText( time);
            }
        };

    }

    //jump to the details of the different routes
    public void toPlanDetails(){
        Intent intent = new Intent(getActivity(), SearchPlanDetailsActivity.class);
        Spinner departure = this.view.findViewById(R.id.departure);
        Spinner destination = this.view.findViewById(R.id.destination);
        TextView departureDate = this.view.findViewById(R.id.departure_date);
        TextView departureTime = this.view.findViewById(R.id.departure_time);
        TabLayout ticketNumLayout = this.view.findViewById(R.id.ticket_num);
        int ticketNum = ticketNumLayout.getSelectedTabPosition() + 1;
        //check if the user has selected date and time
        String strDate = (String) departureDate.getText();
        if(!strDate.contains("/")) {
            alert("Please select a date");
            return;
        }
        String strTime = (String) departureTime.getText();
        if(!strTime.contains(":")){
            alert("Please select a time");
            return;
        }
        intent.putExtra("departure", departure.getSelectedItem().toString());
        intent.putExtra("destination", destination.getSelectedItem().toString());
        //transfer the format of the time
        if(strTime.length() == 4){
            strTime = "0" + strTime;
        }
        intent.putExtra("time", strTime);
        intent.putExtra("date", strDate);
        intent.putExtra("ticketNum", ticketNum);
        if(user == null) intent.putExtra("username", "root");
        else intent.putExtra("username", user.getUsername());

        startActivity(intent);
    }

    //alert a friendly message to the user
    public void alert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //no actions
                        //just inform the user to select the time and date
                    }
                })
                .create()
                .show();
    }
}