package com.bgs.jianbao12.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.adapter.Adapter_Serchh;
import com.bgs.jianbao12.bean.Info_Seach;
import com.bgs.jianbao12.utils.SharedUtils;
import com.bgs.jianbao12.view.LoadMoreRecyclerView;

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

import static java.lang.String.valueOf;

/**
 * Created by Administrator on 2016/12/19.
 * 搜索结果界面
 */

public class Activity_result extends Activity implements View.OnClickListener, LoadMoreRecyclerView.LoadMoreListener, SwipeRefreshLayout.OnRefreshListener, Adapter_Serchh.OnRecyclerViewItemClickListener {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private Adapter_Serchh myItemRecyclerViewAdapter;
    private LoadMoreRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String url = "http://192.168.4.188/Goods/app/item/list.json";
    private Map<String, String> map = new HashMap<>();
    private Map<String, String> map2 = new HashMap<>();
    private int index = 1;
    private List<Info_Seach> list1;
    private List<Info_Seach> list2 = new ArrayList<>();
    private Info_Seach info_seach;
    private boolean flag = true;
    private int size;
    private RelativeLayout mSech_relatt, mSech_out;
    private TextView mSerh_title;
    private String input;
    private String token;
    private SharedUtils utils;
//    private RecycleViewDivider recycleViewDivider;
    private ImageView mSech_handover;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serch);
        utils = ((MyAppalication) this.getApplicationContext()).utils;
        token = utils.getShared("token", this);

        initView();
    }

    private void initMap() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        input = (String) bundle.getString("input");
        mSerh_title.setText(input);
        map.put("title", input);
        map.put("curPage", "1");
    }

    private void initMap2() {
        index++;
        map2.put("title", input);
        map2.put("curPage", valueOf(index));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mSech_out:
                finish();
                break;
            case R.id.mSech_relatt:
                if (1 == mColumnCount) {
                    mColumnCount = 2;
                    mSech_handover.setImageResource(R.mipmap.cut1);
                    myItemRecyclerViewAdapter.switchMode(true);

                    recyclerView.switchLayoutManager(new StaggeredGridLayoutManager(mColumnCount, StaggeredGridLayoutManager.VERTICAL));
                } else {
                    mColumnCount = 1;
                    mSech_handover.setImageResource(R.mipmap.cut2);
                    myItemRecyclerViewAdapter.switchMode(false);

                    recyclerView.switchLayoutManager(new LinearLayoutManager(Activity_result.this));
                }
                break;
        }
    }

    private void initView() {
        mSech_relatt = (RelativeLayout) findViewById(R.id.mSech_relatt);
        mSech_out = (RelativeLayout) findViewById(R.id.mSech_out);
        mSerh_title = (TextView) findViewById(R.id.mSerh_title);
        mSech_handover= (ImageView) findViewById(R.id.mSech_handover);
        mSech_out.setOnClickListener(this);
        mSech_relatt.setOnClickListener(this);

        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.mSech_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark, android.R.color.holo_blue_dark, android.R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(this);

        if (1 >= mColumnCount) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recycleViewDivider=new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,3, Color.BLACK);
        } else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(mColumnCount, StaggeredGridLayoutManager.VERTICAL));
//            recycleViewDivider=new RecycleViewDivider(this,LinearLayoutManager.VERTICAL);
        }
        initMap();
        post_file(url, map);
        myItemRecyclerViewAdapter = new Adapter_Serchh(this, list2);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        myItemRecyclerViewAdapter.setCall(this);
        recyclerView.setAutoLoadMoreEnable(false);
        recyclerView.setLoadMoreListener(this);

        myItemRecyclerViewAdapter.notifyDataSetChanged();

    }

    //上拉加载
    @Override
    public void onLoadMore() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                initMap2();
                post_file(url, map2);
            }
        }, 500);
    }

    //下拉刷新
    @Override
    public void onRefresh() {
        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Info_Seach> list3 = new ArrayList<>();
                for (int i = 0; i < list2.size(); i++) {
                    list3.add(list2.get(i));
                }
                myItemRecyclerViewAdapter.setData(list3);
                Log.e("ccsdfsafasafadcccc", "" + list2.size());
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 500);

    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == index) {
                final List<Info_Seach> list = (List<Info_Seach>) msg.obj;
                for (int i = 0; i < list.size(); i++) {
                    list2.add(list.get(i));
                }
                if (list2.size() < 5) {
                    Log.e("ooooooo", "" + list2.size());
                } else {
                    recyclerView.notifyMoreFinish(flag);
                }
            }
        }
    };

    protected void post_file(final String url, final Map<String, String> map) {
        final String curPage = map.get("curPage");
        Log.e("ffffff", "" + curPage);
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
                Log.e("返回类型", str1);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(str1);
                            JSONObject data = object.getJSONObject("data");
                            size = data.getInt("size");
                            if (size == 0) {
                                flag = false;
                            }
                            JSONArray list = data.getJSONArray("list");
                            list1 = new ArrayList<Info_Seach>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject jsonObject = list.getJSONObject(i);
                                int id1 = jsonObject.getInt("id");
                                String title1 = jsonObject.getString("title");
                                String image1 = "http://192.168.4.188/Goods/uploads/" + jsonObject.getString("image");
                                String price1 = jsonObject.getString("price");
                                String issue_time1 = jsonObject.getString("issue_time");
                                int state1 = jsonObject.getInt("state");
                                info_seach = new Info_Seach(id1, title1, image1, price1, issue_time1, state1);
                                list1.add(info_seach);
                                Log.e("", "" + list1.size());
                            }
                            Message msg = handler.obtainMessage();
                            msg.what = Integer.valueOf(curPage);
                            msg.obj = list1;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    public boolean hasMore(int size) {
        if (size == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int postion) {
        if (token == null || token == "") {
            Intent intent = new Intent(this, Activity_Login.class);
            startActivity(intent);
        } else {
            int id = list2.get(postion).getId();
            Intent intent = new Intent(this, Activity_GoodsDetail.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }


}
