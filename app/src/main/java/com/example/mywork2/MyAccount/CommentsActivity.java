package com.example.mywork2.MyAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mywork2.R;
import com.example.mywork2.adapter.CommentsAdapter;
import com.example.mywork2.dao.CommentDao;
import com.example.mywork2.domain.Comment;
import com.example.mywork2.domain.DepartureTime;
import com.example.mywork2.domain.Journey;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class CommentsActivity extends AppCompatActivity {
    private InputMethodManager imm;
    private CommentsAdapter commentsAdapter;
    private RatingBar inputRating;
    private ArrayList<Comment> comments = new ArrayList<>();
    private ListView commentsListView;
    private LinearLayout commentLoading;
    private EditText inputContent;
    private String username;
    private int rating;
    private String content;

    //receive the data from the database
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0x11:
                    //my comments has been placed in the comments list
                    //then get other comments into the comments list
                    getOtherComments();
                    break;
                case 0x22:
                    //get all the comments
                    //then show the comments in the listView
                    showComments();
                    break;
                case 0x33:
                    //init the page
                    //after adding/removing a new comment
                    initPage();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentsListView = findViewById(R.id.commentListView);
        commentLoading = findViewById(R.id.commentLoading);
        inputContent = findViewById(R.id.commentInputContent);
        imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputRating = findViewById(R.id.commentInputRating);
        commentsAdapter = (CommentsAdapter) commentsListView.getAdapter();
        username = getIntent().getExtras().getString("username");


        //set action bar
        TextView title = findViewById(R.id.header_title);
        title.setText(R.string.comment);
        title.setTextColor(Color.BLACK);
        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});
        //set the send button listener
        findViewById(R.id.commentSendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the input
                getInput();
                addComment();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //the comments will be shown in the end
        initPage();
    }

    //init the page
    public void initPage(){
        //show the loading page until the info has been loaded
        commentLoading.setVisibility(View.VISIBLE);
        commentsListView.setVisibility(View.INVISIBLE);
        //hide the virtual keyboard
        imm.hideSoftInputFromWindow(inputContent.getWindowToken(), 0);
        //clear the input text
        inputContent.setText("");
        //clear all the comments
        for(int i = comments.size() - 1; i >= 0; i--){
            comments.remove(i);
        }
        //and get the new ones
        getMyComments();
    }

    //get all user's comments
    //the user's comments will shown at the top
    public void getMyComments(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommentDao commentDao = new CommentDao();
                ArrayList<Comment> myComments = commentDao.getCommentsByUsername(username);
                if(myComments != null && myComments.size() > 0){
                    comments.addAll(myComments);
                }
                Message message = handler.obtainMessage();
                message.what = 0x11;
                handler.sendMessage(message);
            }
        }).start();
    }

    //get all the comments
    public void getOtherComments(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommentDao commentDao = new CommentDao();
                ArrayList<Comment> otherComments = commentDao.getCommentsExceptUsername(username);
                if(otherComments != null && otherComments.size() > 0){
                    comments.addAll(otherComments);
                }
                Message message = handler.obtainMessage();
                message.what = 0x22;
                handler.sendMessage(message);
            }
        }).start();
    }

    //show the comments in the list view
    public void showComments(){
        commentsListView.setAdapter(new CommentsAdapter(comments, this, username));
        //show the loading page until the info has been loaded
        commentLoading.setVisibility(View.GONE);
        commentsListView.setVisibility(View.VISIBLE);
    }

    //get the input comment
    //rating and content
    public void getInput(){
        rating = (int)inputRating.getRating();
        content = inputContent.getText().toString();
    }

    //add this new comment
    public void addComment(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommentDao commentDao = new CommentDao();
                Date date = new Date(System.currentTimeMillis());
                //transfer the date format
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                String dateStr = format.format(date);
                Comment comment = new Comment(System.currentTimeMillis() + username, username, rating, content, dateStr);
                commentDao.addComment(comment);
                Message message = handler.obtainMessage();
                message.what = 0x33;
                handler.sendMessage(message);
            }
        }).start();
    }

    //remove a comment
    public void removeCommentById(String commentId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommentDao commentDao = new CommentDao();
                commentDao.removeCommentById(commentId);
                //inform the main thread to init the page
                Message message = handler.obtainMessage();
                message.what = 0x33;
                handler.sendMessage(message);
            }
        }).start();
    }
}