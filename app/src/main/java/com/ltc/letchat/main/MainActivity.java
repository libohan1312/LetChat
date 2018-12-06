package com.ltc.letchat.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ltc.letchat.MyApplication;
import com.ltc.letchat.R;
import com.ltc.letchat.contacts.ContactFragment;
import com.ltc.letchat.news.NewsFragment;
import com.ltc.letchat.recentchat.RecentChatFragment;
import com.ltc.letchat.recentchat.RecentChatPresenter;
import com.ltc.letchat.recentchat.RecentListAdapter;

import java.util.ArrayList;
import java.util.List;

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
        initView();
    }

    private void initView() {

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);



        ArrayList<String> tabs = new ArrayList<>();
        tabs.add("聊天界面");
        tabs.add("在线朋友");
        tabs.add("朋友动态");

        List<Fragment> fragments = new ArrayList<>();
        RecentChatFragment recentChatFragment = RecentChatFragment.newInstance();
        new RecentChatPresenter(this,recentChatFragment);
        fragments.add(recentChatFragment);
        fragments.add(ContactFragment.newInstance());
        fragments.add(NewsFragment.newInstance("",""));

        MainFragementPagerAdapter fragementPagerAdapter = new MainFragementPagerAdapter(getSupportFragmentManager(),fragments,tabs);

        viewPager.setAdapter(fragementPagerAdapter);
        TabLayout tableLayout = (TabLayout) findViewById(R.id.tablayout);
        tableLayout.setTabMode(TabLayout.MODE_FIXED);
        tableLayout.setupWithViewPager(viewPager);

        tableLayout.getTabAt(0).setIcon(R.drawable.ic_insert_comment_black_24dp);
        tableLayout.getTabAt(1).setIcon(R.drawable.ic_account_circle_black_24dp);
        tableLayout.getTabAt(2).setIcon(R.drawable.ic_star_black_24dp);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("退出");
        builder.setMessage("是否退出软件?");
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyApplication.getChatManager().close();
                finish();
                System.exit(0);
            }
        });
        builder.setPositiveButton("不退",null);
        builder.create().show();
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
