package com.bgs.jianbao12.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.adapter.Adapter_Collection;
import com.bgs.jianbao12.bean.Info_Collection;
import com.bgs.jianbao12.utils.SharedUtils;
import com.bgs.jianbao12.view.MyListView;
import com.bgs.jianbao12.view.PullToRefreshLayout;

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

import static com.bgs.jianbao12.Constant.FOLLOWLISTURL;
import static com.bgs.jianbao12.Constant.FOLLOWURL;
import static com.bgs.jianbao12.Constant.IMGURL;
import static java.lang.String.valueOf;

/**
 * Created by HaiBo on 2016/12/11.
 * 个人关注
 */

public class Activity_Collection extends Activity {
    //上下拉刷新
    private PullToRefreshLayout mColl_PullView;
    //放动态布局的view
    private LinearLayout mColl_sel_center;
    //返回键
    private RelativeLayout mFaBu_coll_out;
    //自定义listview
    private MyListView mFrag_coll_Lv;
    //listview适配器
    private Adapter_Collection adapter;
    //存放数据请求的map值
    private Map<String, String> map = new HashMap<>();
    //
    private Map<String, String> map2 = new HashMap<>();
    //
    private Map<String, Object> map_guanzhu = new HashMap<>();
    //Java   bean
    private List<Info_Collection> list = new ArrayList<Info_Collection>();
    //
    private SharedUtils utils;
    //
    private int index = 1;
    //
    private String token;

    //
    private boolean isFirst = true;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        utils = ((MyAppalication) this.getApplicationContext()).utils;
        token = utils.getShared("token", this);
        initView();
        initMap();
        post_file(FOLLOWLISTURL, map);
    }

    //
    private void initMap() {
        map.clear();
        map.put("curPage", "1");
        map.put("token", token);
    }

    //
    private void initMap2() {
        index++;
        map2.clear();
        map2.put("curPage", String.valueOf(index));
        map2.put("token", token);
    }

    //
    private void initView() {
        mColl_sel_center = (LinearLayout) findViewById(R.id.mColl_sel_center);
        mColl_PullView = (PullToRefreshLayout) findViewById(R.id.mColl_PullView);

        mFaBu_coll_out = (RelativeLayout) findViewById(R.id.mFaBu_coll_out);
        mFaBu_coll_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //
        mColl_PullView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            //
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            handler.sendEmptyMessage(900);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            //
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            handler.sendEmptyMessage(909);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        //主布局
        View mylistview = View.inflate(Activity_Collection.this, R.layout.mylistview, null);
        mFrag_coll_Lv = (MyListView) mylistview.findViewById(R.id.mFrag_coll_Lv);
        mColl_sel_center.addView(mylistview);
    }

    //
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //
            if (msg.what == index) {
                final List<Info_Collection> list2 = (List<Info_Collection>) msg.obj;
                list.addAll(list2);
                Log.e("sdfsdf", "" + list2.size());
            }
            //
            if (adapter == null) {
                adapter = new Adapter_Collection(Activity_Collection.this, list);
                mFrag_coll_Lv.setAdapter(adapter);
                adapter.setOnDelListener(new Adapter_Collection.onSwipeListener() {
                    @Override
                    public void onDelete(int pos) {
                        map_guanzhu.put("id", list.get(pos).getId());
                        map_guanzhu.put("act", 1);
                        map_guanzhu.put("token", token);
                        post_file_guanzhu(FOLLOWURL, map_guanzhu);
                        adapter.getList().remove(pos);
                        adapter.notifyDataSetChanged();

                    }
                });
                //
                adapter.setOnClickListener(new Adapter_Collection.setOnClickListener() {
                    @Override
                    public void onItemClick(int postion) {
                        Intent intent = new Intent(Activity_Collection.this, Activity_GoodsDetail.class);
                        intent.putExtra("id", postion);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                    }
                });

            }

            if (msg.what == 900) {
                //刷新
                index = 1;
                list.clear();
                isFirst = false;
                initMap();
                post_file(FOLLOWLISTURL, map);

            }
            if (msg.what == 909) {
                //加载更多
                isFirst = false;
                initMap2();
                post_file(FOLLOWLISTURL, map2);
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
                try {
                    JSONObject object = new JSONObject(str);
                    JSONObject data = object.getJSONObject("data");
                    String zhuangtai = object.getString("status");
                    int size = data.getInt("size");
                    if (!isFirst) {
                        if (curPage.equals("1")) {
                            //刷新是否成功
                            if (zhuangtai.equals("200")) {
                                //成功
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mColl_PullView.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    }
                                });

                            } else {
                                //失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mColl_PullView.refreshFinish(PullToRefreshLayout.FAIL);
                                    }
                                });
                            }
                        } else {
                            //加载是否成功
                            if (zhuangtai.equals("200")) {
                                if (size > 0) {
//                                    //成功
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mColl_PullView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                        }
                                    });
                                    //有
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(Activity_Collection.this, "没有更多了", Toast.LENGTH_SHORT).show();
                                            mColl_PullView.loadmoreFinish(PullToRefreshLayout.FAIL);
                                        }
                                    });
                                }
                            } else {
                                //失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mColl_PullView.loadmoreFinish(PullToRefreshLayout.FAIL);
                                    }
                                });
                            }
                        }
                    }
                    JSONArray list = data.getJSONArray("list");
                    List<Info_Collection> list1 = new ArrayList<Info_Collection>();
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject jsonObject = list.getJSONObject(i);
                        int id1 = jsonObject.getInt("id");
                        String title1 = jsonObject.getString("title");
                        String image1 = IMGURL + jsonObject.getString("image");
                        String price1 = jsonObject.getString("price");
                        String issue_time1 = jsonObject.getString("issue_time");
                        int state1 = jsonObject.getInt("state");
                        Info_Collection info_collection = new Info_Collection(id1, title1, image1, price1, issue_time1, state1);
                        list1.add(info_collection);
                    }
                    Message msg = handler.obtainMessage();
                    msg.what = Integer.valueOf(curPage);
                    msg.obj = list1;
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }

    protected void post_file_guanzhu(final String url, final Map<String, Object> map) {
        int act = (int) map.get("act");
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
                final String str1 = response.body().string();

            }
        });

    }
}

