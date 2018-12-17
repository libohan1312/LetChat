package com.ltc.letchat.chat;

import android.content.Intent;
import android.graphics.Rect;
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

import com.ltc.letchat.R;
import com.ltc.letchat.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity implements ChatContract.View{

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

    ChatContract.Presenter presenter;
    ChatListAdapter chatListAdapter;
    ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        userName = intent.getStringExtra("name");
        chatUserId = intent.getStringExtra("userId");
        toolbar.setTitle(userName);
        setSupportActionBar(toolbar);

        new ChatPresenter(this,this,chatUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatListAdapter = new ChatListAdapter(this);
        recyclerView.setAdapter(chatListAdapter);

        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(isKeyboardShown(recyclerView.getRootView())){
                    recyclerView.scrollToPosition(chatListAdapter.getItemCount()-1);
                }
            }
        };
        recyclerView.getRootView().getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        bt_send.setOnClickListener(v -> {
                String msg = editText.getText().toString();
                presenter.sendMessage(chatUserId,msg);
        });
        presenter.loadAllChat();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.getRootView().getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
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

    @Override
    public void showMessage(ChatItem chatItem) {
        if(chatItem == null){
            return;
        }
        chatListAdapter.addChat(chatItem);
        recyclerView.scrollToPosition(chatListAdapter.getItemCount()-1);
    }

    @Override
    public void afterSendMessage(boolean success) {
        editText.setText(null);
    }

    @Override
    public void showAllMassage(List<ChatItem> chatItemList) {
        chatListAdapter.loadAllItem(chatItemList);
    }

    @Override
    public void onSetPresenter(ChatContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
