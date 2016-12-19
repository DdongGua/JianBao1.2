package com.bgs.jianbao12.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.fragment.FragmentMine;
import com.bgs.jianbao12.fragment.Fragment_Home;
import com.bgs.jianbao12.utils.SharedUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.String.valueOf;


/**
 * 主页面
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private ImageView mFrag_home1, mFrag_push,mFrag_mine1;
    private TextView mFrag_home2, mFrag_mine2;
    private LinearLayout mFrag_home,mFrag_mine, mLinear_frag;
    private Fragment_Home fragmentHome;
    private FragmentMine fragmentMine;
    private FragmentManager manager;
    private boolean isExit;
    private SharedUtils utils;
    private String token;
    private Map map = new HashMap();
    private String status;
    private String url = "http://192.168.4.188/Goods/app/user/info.json";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getSupportFragmentManager();
        NetWorkHelpe();
        utils = ((MyAppalication) MainActivity.this.getApplicationContext()).utils;
        token = utils.getShared("token", this);
        map.put("token", token);
        if (token != null && !token.equals("")) {
            post_file(url, map);
        }
        initView();

    }

    private void initView() {
        //按钮
        mFrag_home = (LinearLayout) findViewById(R.id.mFrag_home);
        mFrag_mine = (LinearLayout) findViewById(R.id.mFrag_mine);
        mFrag_home.setOnClickListener(this);
        mFrag_mine.setOnClickListener(this);

        mFrag_home1 = (ImageView) findViewById(R.id.mFrag_home1);
        mFrag_push = (ImageView) findViewById(R.id.mFrag_push);
        mFrag_mine1 = (ImageView) findViewById(R.id.mFrag_mine1);
        mFrag_push.setOnClickListener(this);

        mFrag_home2 = (TextView) findViewById(R.id.mFrag_home2);
        mFrag_home2.setTextColor(Color.RED);
        mFrag_mine2 = (TextView) findViewById(R.id.mFrag_mine2);
        //放frag的Linear
        mLinear_frag = (LinearLayout) findViewById(R.id.mLinear_frag);
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragmentHome == null) {
            fragmentHome = new Fragment_Home();
            transaction.add(R.id.mLinear_frag, fragmentHome);
        } else {
            transaction.show(fragmentHome);
        }
        transaction.commit();
    }

    protected void post_file(final String url, final Map<String, Object> map) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);

        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getValue() != null && !"".equals(entry.getValue())) {
                    requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
                }
            }
        }
        MultipartBody build = requestBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(build)
                .build();
        // readTimeout("请求超时时间" , 时间单位);
        client.newBuilder().readTimeout(5000, TimeUnit.MILLISECONDS).build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.e("111111", response.message() + " , body " + str);
                    try {
                        JSONObject object = new JSONObject(str);
                        status = object.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("22222", response.message() + " error : body " + response.body().string());
                }
            }
        });

    }

    private void initBehind() {

    }

    /**
     * 清除所有的颜色
     */
    private void cleanAll() {
        mFrag_home1.setImageResource(R.mipmap.main_board_1);
        mFrag_mine1.setImageResource(R.mipmap.main_board_4);
        mFrag_home2.setTextColor(Color.GRAY);
        mFrag_mine2.setTextColor(Color.GRAY);
    }

    /**
     * 清除所有fragment
     */
    private void cleanFrag() {
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragmentHome != null) {
            transaction.hide(fragmentHome);
        }
        if (fragmentMine != null) {
            transaction.hide(fragmentMine);
        }
        transaction.commit();
    }

    /**
     * 点击事件 切换fragment
     */
    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = manager.beginTransaction();

        switch (view.getId()) {
            case R.id.mFrag_home:
                cleanAll();
                cleanFrag();
                mFrag_home1.setImageResource(R.mipmap.main_board_1_active);
                mFrag_home2.setTextColor(Color.RED);
                if (fragmentHome == null) {
                    fragmentHome = new Fragment_Home();
                    transaction.add(R.id.mLinear_frag, fragmentHome);
                } else {
                    transaction.show(fragmentHome);
                }
                break;

            case R.id.mFrag_push:
                startActivity(new Intent(MainActivity.this, Activity_PushGoods.class));
                break;
            case R.id.mFrag_mine:
                cleanAll();
                cleanFrag();
                mFrag_mine1.setImageResource(R.mipmap.main_board_4_active);
                mFrag_mine2.setTextColor(Color.RED);
                if (token == null || "".equals(token)) {
                    Intent intent = new Intent(MainActivity.this, Activity_Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (status .equals("301") ) {
                        Intent intent = new Intent(MainActivity.this, Activity_Login.class);
                        startActivity(intent);
                        finish();
                    } else if (status .equals("500") ) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(utils, "网络错误，请检查网络。", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (status .equals("200") ) {
                        if (fragmentMine == null) {
                            fragmentMine = new FragmentMine();
                            transaction.add(R.id.mLinear_frag, fragmentMine);
                        } else {
                            transaction.show(fragmentMine);
                        }
                    }

                }
                break;
        }
        transaction.commit();
    }

    /*
 * 手机按返回键的监听
 * */
    @Override
    public void onBackPressed() {
        if (isExit) {
            finish();
            System.exit(0);
        } else {
            isExit = true;
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            //postDelayed延时调用
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);

        }
    }
/*
 *网络判断类
 * */

    public void NetWorkHelpe() {
        ConnectivityManager manager = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            Toast.makeText(this, "网络已经连接", Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("开启网络服务");
            builder.setMessage("网络没有连接，请到设置进行网络设置！");
            builder.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (android.os.Build.VERSION.SDK_INT > 10) {
                                // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                                startActivity(new Intent(
                                        android.provider.Settings.ACTION_SETTINGS));
                            } else {
                                startActivity(new Intent(
                                        android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }
                            dialog.cancel();
                        }
                    });

            builder.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        }
        super.onStart();
    }

}

