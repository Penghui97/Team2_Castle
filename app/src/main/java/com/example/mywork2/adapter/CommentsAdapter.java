package com.example.mywork2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mywork2.R;
import com.example.mywork2.domain.Comment;

import java.util.ArrayList;

public class CommentsAdapter extends BaseAdapter {
    private ArrayList<Comment> comments;
    private Context context;

    public CommentsAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        //first time
        if(view == null){
            viewHolder = new ViewHolder();
            //set the list_item.xml as the view
            view = LayoutInflater.from(context).inflate(R.layout.comment_item, viewGroup, false);
            //put the textViews of list_item to the viewHolder
            viewHolder.username = view.findViewById(R.id.account_name_comment);
            viewHolder.content = view.findViewById(R.id.comments);
            viewHolder.ratingBar = view.findViewById(R.id.rating);
            viewHolder.time = view.findViewById(R.id.comment_date_time);
            //put the holder into the view's tag
            //in case to use next time
            view.setTag(viewHolder);
        }else{
            //next time
            viewHolder = (ViewHolder) view.getTag();
        }
        //set the content of the textView in the holder
        viewHolder.username.setText(comments.get(i).getUsername() + "");
        viewHolder.content.setText(comments.get(i).getContent() + "");
        viewHolder.ratingBar.setRating(comments.get(i).getRating());
        viewHolder.time.setText(comments.get(i).getTime() + "");
        return view;
    }

    private final class ViewHolder{
        ImageView avatar;
        TextView username;
        TextView content;
        RatingBar ratingBar;
        TextView time;
    }
}
