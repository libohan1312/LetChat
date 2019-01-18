package com.ltc.letchat.account;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ltc.letchat.Constant;
import com.ltc.letchat.R;
import com.ltc.letchat.base.BaseActivity;
import com.ltc.letchat.util.OKUtil;

import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class AccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account1);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OKUtil.OKRequestEntity okRequestEntity = new OKUtil.OKRequestEntity();
                okRequestEntity.url = Constant.http2Uri_test +"/test/abc?name=abc&age=13";
                okRequestEntity.method = "POST";
                okRequestEntity.params.put("name","abc");
                okRequestEntity.params.put("age",12);
                Log.e("oktest","start time :"+System.currentTimeMillis());
                long startTime = System.currentTimeMillis();
                Disposable disposable = OKUtil.doRequest(okRequestEntity)
                        .repeat(100)
                        .subscribe(new Consumer<JSONObject>() {
                            @Override
                            public void accept(JSONObject jsonObject) throws Exception {
                                Log.e("oktest", jsonObject.toString());
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {
                                Log.e("oktest","end time:"+System.currentTimeMillis());
                                Log.e("oktest","user time:"+(System.currentTimeMillis()-startTime));
                            }
                        });
            }
        });
    }
}
