package com.ltc.letchat.chat;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ltc.letchat.MyApplication;
import com.ltc.letchat.R;
import com.ltc.letchat.net.api.IChat;
import com.ltc.letchat.net.request.Talk;
import com.ltc.letchat.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.chatlist)
    RecyclerView recyclerView;
    @BindView(R.id.chattoolbar)
    Toolbar toolbar;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.include)
    LinearLayout linearLayout;

    @BindView(R.id.bt_send)
    Button bt_send;

    String chatUserId;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        userName = intent.getStringExtra("name");
        chatUserId = intent.getStringExtra("userId");
        toolbar.setTitle(chatUserId);
        setSupportActionBar(toolbar);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ChatListAdapter chatListAdapter = new ChatListAdapter(this);
        recyclerView.setAdapter(chatListAdapter);

        recyclerView.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(isKeyboardShown(recyclerView.getRootView())){
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount()-1);
//question here                    hh = getKeyboardHight(recyclerView.getRootView());
//                    recyclerView.scrollBy(0,hh-linearLayout.getMeasuredHeight());
                }
            }
        });
        try {
            MyApplication.getChatManager().receiveMsg(new IChat.OnReceiveMsgListener() {
                @Override
                public void onReceive(String uri,String msg) {
                    ChatItem chatItem = new ChatItem();
                    chatItem.content = msg;
                    chatItem.isMe = false;
                    chatListAdapter.addChat(chatItem);
                }
            });
            bt_send.setOnClickListener(v -> {
                Talk talk = new Talk();
                talk.setToHow(chatUserId);
                talk.setContent(editText.getText().toString());
                String json = Utils.objectToJson(talk);
                MyApplication.getChatManager().sendMsg(json);
                ChatItem chatItem = new ChatItem();
                chatItem.content = editText.getText().toString();
                chatItem.isMe = true;
                chatItem.fromHow = "me";
                chatListAdapter.addChat(chatItem);
                editText.setText(null);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    private int getKeyboardHight(View rootView){
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff;
    }
}
