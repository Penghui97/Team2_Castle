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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.mywork2.MainActivity;
import com.example.mywork2.R;
import com.example.mywork2.SearchPlanDetailsActivity;
import com.example.mywork2.domain.User;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
/**
 * @author Jiacheng(UI behavior), Jing()
 * function: the fragment for finding plans
 * modification date and description can be found in github repository history
 */
public class SearchFragment extends Fragment {
    // the textview of date selected displayed
    private TextView mDisplayDate;
    // the textview of time selected displayed
    private TextView mDisplayTime;
    // the date picker dialog used
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    // the time picker dialog used
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    // fragment view
    private View view;
    private User user;
    //the selected date
    private Calendar dateOfDeparture;
    // the textview of ticket nums
    private TextView  ticketNumLayout;

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
        ticketNumLayout = view.findViewById(R.id.ticket_num);

        ImageView add = view.findViewById(R.id.add_count);
        add.setOnClickListener(view1 -> {
            if(Integer.parseInt(ticketNumLayout.getText().toString())!=5){
                int i = Integer.parseInt(ticketNumLayout.getText().toString())+1;
                ticketNumLayout.setText(""+i);
            }
        });

        ImageView min = view.findViewById(R.id.remove_count);
        min.setOnClickListener(view1 -> {
            if(Integer.parseInt(ticketNumLayout.getText().toString())!=1){
                int i = Integer.parseInt(ticketNumLayout.getText().toString())-1;
                ticketNumLayout.setText(""+i);
            }
        });
        //get the current date
        dateOfDeparture = Calendar.getInstance();
        return view;

    }
    //set departure date
    public void setDepartureTime(){
        mDisplayDate = view.findViewById(R.id.departure_date);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DATE);

                //use the date dialog to select the date
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

                //check if the date is before the current date
                dateOfDeparture.set(year,month,day+1);
                if(dateOfDeparture.before(Calendar.getInstance())){
                   alert("please select a date before current date!");
                   return;
                }
                dateOfDeparture.set(year,month,day);

                // the month is 0-11, so add 1
                month = month+1;
                String m = "";
                //if month <10, it should display add a 0
                if(month<10){
                    m+= 0;
                }
                m +=month;
                //if date <10 , it should display add a 0
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
        //set departure time
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
                String iString = "";
                //if hour <10, it should display add a 0
                if(i<10){
                    iString += "0";
                }
                iString += i;
                //if min <10, it should display add a 0
                String i1String ="";
                if(i1<10){
                    i1String += "0";
                }
                i1String += i1;
                String time = iString+":" +i1String;
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
        int ticketNum = Integer.parseInt(ticketNumLayout.getText().toString());

        //Log.d("debu",""+dateOfDeparture.getTime()+"po:"+departure.getSelectedItemPosition());
        //one castle is closed in monday and Tuesday, check it in this if statement
        if(departure.getSelectedItemPosition() ==0){
            if(dateOfDeparture.getTime().toString().contains("Tue")||
                    dateOfDeparture.getTime().toString().contains("Mon")){
                alert("Auckland Castle is closed on Mondays and Tuesday. Sorry!");
                return;
            }
        }

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
        //canceled this function
//        intent.putExtra("departure", departure.getSelectedItem().toString());
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
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
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