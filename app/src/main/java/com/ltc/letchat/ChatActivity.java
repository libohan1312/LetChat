package com.ltc.letchat;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chatlist)
    RecyclerView recyclerView;
    @BindView(R.id.chattoolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatListAdapter chatListAdapter = new ChatListAdapter(this);
        recyclerView.setAdapter(chatListAdapter);

        for(int i=0;i<100;i++) {
            ChatItem chatItem = new ChatItem();
            if(i%2==0){
                chatItem.isMe = true;
                chatItem.content = "fdadfadfasdffadsfasfafdafafasdfasfasfasfasfasfdasfasdff";
            }else {
                chatItem.isMe = false;
                chatItem.content = "法拉伐风景饿啦放假fadsfasf啊了放假啊ffadsfasdfsfdafadfasf了";
            }

            chatListAdapter.addChat(chatItem);
        }
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(name);
        }

    }
}
