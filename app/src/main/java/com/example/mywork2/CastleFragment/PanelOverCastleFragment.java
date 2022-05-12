package com.example.mywork2.CastleFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mywork2.R;
/**
 * @author Jiacheng
 * function: castle info fragment, displayed the information about the castle
 * modification date and description can be found in github repository history
 */

public class PanelOverCastleFragment extends Fragment {
    //castle name to determine which castle
    private String castleName;
    // the links that to the castles website
    private String links;

    public PanelOverCastleFragment(String castleName, String links) {
        this.castleName = castleName;
        this.links = links;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_panel_over_castle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = view.findViewById(R.id.castle_picture);
        TextView castleTitle = view.findViewById(R.id.castle_Name);
        TextView castleLocation = view.findViewById(R.id.castleLocation);
        TextView info = view.findViewById(R.id.brief_intro);
        TextView openTime = view.findViewById(R.id.Open_time);
        TextView entranceTime = view.findViewById(R.id.last_entrance_time);
        TextView closeTime = view.findViewById(R.id.close_time);
        TextView ticketValue = view.findViewById(R.id.ticket_value);
        TextView moreAbout = view.findViewById(R.id.more_about_castle);

        moreAbout.setOnClickListener(view1 ->{
            Uri uri = Uri.parse(links);
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        } );

        //display different page for different castles
        switch (castleName){
            case "Alnwic":
                imageView.setImageResource(R.drawable.alnwic1);
                castleTitle.setText(R.string.alnwic_castle);
                castleLocation.setText("Northumberland NE66 1NQ");
                info.setText("Alnwick Castle has over 950 years of history to discover" +
                        ", and the origins of the Castle date back to the Norman period. " +
                        "Since 1309, its story has been intertwined with that of the Percy family" +
                        ", a family with a history as illustrious as the castle’s own.\n" +
                        " \n" +
                        "The second largest inhabited castle in the UK, Alnwick has served as a military outpost" +
                        ", a teaching college, a refuge for evacuees, a film set, and not least as a family home." +
                        " Delve deeper into this extraordinary history and travel through the centuries of this living" +
                        ", evolving castle.");
                openTime.setText("10:00 am");
                entranceTime.setText("03:45 pm");
                closeTime.setText("05:30 pm");
                ticketValue.setText("￡ 19.5    ");
                break;
            case "Auckland":
                imageView.setImageResource(R.drawable.auckland2);
                castleTitle.setText(R.string.auckland);
                castleLocation.setText("Bishop Auckland DL14 7NR");
                info.setText("Auckland Castle is one of the best-preserved bishops’ palaces in the whole of Europe and is at the centre of The Auckland Project." +
                        " The Castle recently reopened after undergoing major conservation work, " +
                        "which has transformed its state rooms to their original Georgian Gothic splendour, " +
                        "as designed by the renowned English architect James Wyatt.\n" +
                        "\n" +
                        "A visit here can help us understand, perhaps for the first time," +
                        " the fascinating history of the men who helped shape the country we live in today.");
                openTime.setText("11:00 am");
                entranceTime.setText("04:00 pm");
                closeTime.setText("05:00 pm");
                ticketValue.setText("￡ 14.00   ");
                break;
            case "Barnard":
                imageView.setImageResource(R.drawable.barnard);
                castleTitle.setText(R.string.barnard);
                castleLocation.setText("Barnard Castle DL12 8PR");
                info.setText("Set on a high rock above the River Tees, Barnard Castle takes its name from its 12th century founder, Bernard de Balliol. It was later developed by the Beauchamp family and then passed into the hands of Richard III.\n" +
                        "\n" +
                        "With fantastic views over the Tees Gorge this fortress sits on the fringe of an attractive working" +
                                " market town also known as ‘Barney’ so there is plenty to do for families on a day out. " +
                                "Try and spot Richard's boar emblem carved above a window in the inner ward or visit the 'sensory garden' of scented plants and tactile objects.\n"
                        );
                openTime.setText("10:00 am");
                entranceTime.setText("04:30 pm");
                closeTime.setText("05:00 pm");
                ticketValue.setText("￡ 10.00   ");
                break;
            case "Bamburgh":
                imageView.setImageResource(R.drawable.bamburgh2);
                castleTitle.setText(R.string.bamburgh);
                castleLocation.setText("Bamburgh NE69 7DF");
                info.setText("Welcome to Bamburgh Castle, the Kingdom of Northumbria’s epicentre and home of the Armstrong family since 1894.\n" +
                        "\n" +
                        "Crowning nine acres of the Great Whin Sill, Bamburgh Castle has stood guard above the Northumberland coast for thousands of years.\n" +
                        "\n" +
                        "Discover a castle like no other and find out why Bamburgh Castle truly is England’s finest coastal fortress.");
                openTime.setText("10:00 am");
                entranceTime.setText("04:00 pm");
                closeTime.setText("05:00 pm");
                ticketValue.setText("￡ 14.10    ");
                break;
            default:
                break;
        }


    }
}