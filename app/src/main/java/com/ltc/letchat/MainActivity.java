package com.ltc.letchat;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;

    RecentListAdapter chatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);


        View view1 = View.inflate(this,R.layout.chatrecentlist,null);
        RecyclerView recyclerView = (RecyclerView) view1.findViewById(R.id.recentlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatListAdapter = new RecentListAdapter();
        recyclerView.setAdapter(chatListAdapter);

        for(int i=0;i<100;i++) {
            RecentItem chatItem = new RecentItem();
            if(i%2==0){
                chatItem.head = getDrawable(R.drawable.me);
                chatItem.recentName = "阿凡达";
                chatItem.recentTemp = "fdadfadfasdffadsfasfafdafafasdfasfasfasfasfasfdasfasdff";
            }else {
                chatItem.head = getDrawable(R.drawable.others);
                chatItem.recentName = "狗日阿灿";
                chatItem.recentTemp = "法拉伐风景饿啦放假fadsfasf啊了放假啊ffadsfasdfsfdafadfasf了";
            }

            chatListAdapter.addItem(chatItem);
        }
        chatListAdapter.listener = new RecentListAdapter.ViewHolder.OnItemClickListener() {
            @Override
            public void onClick(View v, RecentItem item) {
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                intent.putExtra("name",item.recentName);
                startActivity(intent);
            }
        };

        View view2 = LayoutInflater.from(this).inflate(R.layout.ltrecyclerview,null);

        ArrayList<View> views = new ArrayList<>();
        views.add(view1);
        views.add(view2);
        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("聊天界面");
        tabs.add("在线界面");
        ViewPagerAdapter adapter = new ViewPagerAdapter(views,tabs);
        viewPager.setAdapter(adapter);
        TabLayout tableLayout = (TabLayout) findViewById(R.id.tablayout);
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        tableLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_favorite:
                Toast.makeText(this,"one",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(this,"two",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
