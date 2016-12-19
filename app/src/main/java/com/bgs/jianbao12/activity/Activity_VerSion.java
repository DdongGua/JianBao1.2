package com.bgs.jianbao12.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bgs.jianbao12.R;
import com.bgs.jianbao12.bean.Info_Version;
import com.bgs.jianbao12.utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HaiBo on 2016/12/8.
 * 更新
 */

public class Activity_VerSion extends Activity {
    private LinearLayout mVer_url_lin, mVer_date_lin, mVer_notes_lin;
    private TextView mVer_Tv, mVer_url, mVer_date, mVer_notes;
    private List<Info_Version> list = new ArrayList<>();
    private String url = "http://192.168.4.188/Goods/app/common/version.json";
    private TimeUtils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version);
        utils=new TimeUtils();
        initView();
        getUrlfromJson();
    }

    private void initView() {
        mVer_url_lin = (LinearLayout) findViewById(R.id.mVer_url_lin);
        mVer_date_lin = (LinearLayout) findViewById(R.id.mVer_date_lin);
        mVer_notes_lin = (LinearLayout) findViewById(R.id.mVer_notes_lin);
        mVer_Tv = (TextView) findViewById(R.id.mVer_Tv);
        mVer_url = (TextView) findViewById(R.id.mVer_url);
        mVer_date = (TextView) findViewById(R.id.mVer_date);
        mVer_notes = (TextView) findViewById(R.id.mVer_notes);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                final List<Info_Version> list1 = (List<Info_Version>) msg.obj;
                Log.e("ddddddd",""+list1.size());
                mVer_Tv.setText(list1.get(0).getVer());
                String s = utils.secondToTime(list1.get(0).getDate() + "");
                mVer_date.setText(s);
                mVer_notes.setText(list1.get(0).getNotes());
                mVer_url.setText(list1.get(0).getUrl());
            }
        }
    };

    public void getUrlfromJson() {

        OkHttpClient mHttpClient = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //加载失败
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功获取数据
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.e("wwwwwwwww",json);
                    try {
                        JSONObject object = new JSONObject(json);
                        JSONObject data = object.getJSONObject("data");
                        List<Info_Version> list1 = new ArrayList<Info_Version>();
                        String ver1 = data.getString("ver");
                        String url1 = data.getString("url");
                        String notes1 = data.getString("notes");
                        int date1 = data.getInt("date");
                        Info_Version info_version = new Info_Version(ver1, url1, date1, notes1);
                        list1.add(info_version);
                        Log.e("wwwwwwwww",""+list1.size());
                        Message msg = handler.obtainMessage();
                        msg.what = 1;
                        msg.obj = list1;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
