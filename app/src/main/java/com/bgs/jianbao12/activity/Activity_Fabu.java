package com.bgs.jianbao12.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.adapter.Adapter_collection;
import com.bgs.jianbao12.bean.Info_Collection;
import com.bgs.jianbao12.utils.SharedUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.bgs.jianbao12.view.PullToRefrushView;

import static java.lang.String.valueOf;

/**
 * Created by HaiBo on 2016/12/11.
 * 个人发布
 */

public class Activity_Fabu extends Activity implements PullToRefrushView.Pull_To_Load {
    private ListView mFrag_coll_Lv;
    private PullToRefrushView mfb_PullView;
    private Adapter_collection adapter;
    private String url = "http://192.168.4.188/Goods/app/user/issue_list.json";
    private Map<String, String> map = new HashMap<>();
    private List<Info_Collection> list = new ArrayList<Info_Collection>();
    private SharedUtils utils;
    private int index = 1;
    private Map<String, String> map2 = new HashMap<>();
    private String token;
    private View footerview;
    private ImageView mFaBu_out;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabu);
        utils = ((MyAppalication) this.getApplicationContext()).utils;
        initView();
        initMap();
        post_file(url, map);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initMap() {
        token = utils.getShared("token", this);
        map.clear();
        map.put("curPage", "1");
        map.put("token", token);
    }

    private void initMap2() {
        index++;
        map2.clear();
        map2.put("curPage", String.valueOf(index));
        map2.put("token", token);
    }

    private void initView() {
        mfb_PullView = (PullToRefrushView) findViewById(R.id.mfb_PullView);
        mfb_PullView.setCall(this);
        //viewpager的主布局
        footerview = View.inflate(this, R.layout.listviewfabu, null);
        mfb_PullView.setCenterView(footerview);
        mFrag_coll_Lv = (ListView) footerview.findViewById(R.id.mFrag_coll_Lv);
        mFrag_coll_Lv.setFocusable(false);
        mFaBu_out = (ImageView) footerview.findViewById(R.id.mFaBu_out);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == index) {
                final List<Info_Collection> list1 = (List<Info_Collection>) msg.obj;
                Log.e("aaaaaaaaaa", "lss" + list1.size());
                for (int i = 0; i < list1.size(); i++) {
                    list.add(list1.get(i));
                }
                adapter = new Adapter_collection(Activity_Fabu.this, list);
                mFrag_coll_Lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                mFrag_coll_Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (token == null || token == "") {
                            Intent intent = new Intent(Activity_Fabu.this, Activity_Login.class);
                            startActivity(intent);
                        } else {
                            int id = list.get(i).getId();
                            Intent intent = new Intent(Activity_Fabu.this, Activity_GoodsDetail.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }

                    }
                });
            }
            if (msg.what == 201) {
                //刷新
                mfb_PullView.complate();
                list.clear();
                initMap();
                post_file(url, map);
            }
            if (msg.what == 202) {
                //加载更多
                mfb_PullView.complate();
                initMap2();
                post_file(url, map2);
            }
        }
    };

    protected void post_file(final String url, final Map<String, String> map) {
        final String curPage = map.get("curPage");
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
                final String str = response.body().string();
                Log.e("11212111", str);

                try {
                    JSONObject object = new JSONObject(str);
                    JSONObject data = object.getJSONObject("data");
                    int size = data.getInt("size");
                    if (size == 0) {

                    } else {
                        JSONArray list = data.getJSONArray("list");
                        List<Info_Collection> list1 = new ArrayList<Info_Collection>();
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject jsonObject = list.getJSONObject(i);
                            int id1 = jsonObject.getInt("id");
                            String title1 = jsonObject.getString("title");
                            String image1 = "http://192.168.4.188/Goods/uploads/" + jsonObject.getString("image");
                            String price1 = jsonObject.getString("price");
                            String issue_time1 = jsonObject.getString("issue_time");
                            int state1 = jsonObject.getInt("state");
                            Info_Collection info_collection = new Info_Collection(id1, title1, image1, price1, issue_time1, state1);
                            list1.add(info_collection);
                            Log.e("456789", "list1" + list1.size());
                        }
                        Message msg = handler.obtainMessage();
                        msg.what = Integer.valueOf(curPage);
                        msg.obj = list1;
                        handler.sendMessage(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public void Load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    handler.sendEmptyMessage(202);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void Refrash() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    handler.sendEmptyMessage(201);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Activity_Fabu Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        AppIndex.AppIndexApi.start(client2, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client2, getIndexApiAction());
        client2.disconnect();
    }
}
