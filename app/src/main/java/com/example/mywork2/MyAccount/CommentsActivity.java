package com.example.mywork2.MyAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mywork2.R;
import com.example.mywork2.adapter.CommentsAdapter;
import com.example.mywork2.dao.CommentDao;
import com.example.mywork2.domain.Comment;
import com.example.mywork2.domain.DepartureTime;
import com.example.mywork2.domain.Journey;

import java.util.ArrayList;

public class CommentsActivity extends AppCompatActivity {
    private ArrayList<Comment> comments = new ArrayList<>();
    private ListView commentsListView;
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
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        commentsListView = findViewById(R.id.commentListView);
        inputContent = findViewById(R.id.commentInputContent);
        username = getIntent().getExtras().getString("username");


        //set action bar
        TextView title = findViewById(R.id.header_title);
        title.setText(R.string.comment);
        title.setTextColor(Color.BLACK);
        findViewById(R.id.back_button).setOnClickListener(view -> {
            this.finish()
            ;});

        findViewById(R.id.commentSendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the input
                getInput();
                addComment();
            }
        });

        //get my comments firstly
        //the comments will be shown in the end
        getMyComments();
    }

    //get all my comments
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
        commentsListView.setAdapter(new CommentsAdapter(comments, this));
    }

    //get the input comment
    //rating and content
    public void getInput(){
//        rating =
        content = (String) inputContent.getHint();
    }

    //add this new comment
    public void addComment(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommentDao commentDao = new CommentDao();
                Comment comment = new Comment(System.currentTimeMillis() + "comment", username, rating, content, System.currentTimeMillis() + "");
                commentDao.addComment(comment);
            }
        }).start();
    }
}