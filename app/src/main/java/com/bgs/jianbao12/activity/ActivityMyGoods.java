package com.bgs.jianbao12.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.adapter.Adapter_Collection;
import com.bgs.jianbao12.adapter.MyGoodsAdapter;
import com.bgs.jianbao12.bean.Info_Collection;
import com.bgs.jianbao12.utils.SharedUtils;
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

import static com.bgs.jianbao12.Constant.IMGURL;
import static com.bgs.jianbao12.Constant.ISSUELISTURL;
import static com.bgs.jianbao12.Constant.MODIFYURL;
import static java.lang.String.valueOf;

/**
 * Created by Administrator on 2016/12/29.
 * 个人发布
 */

public class ActivityMyGoods extends Activity {
    private ListView mFrag_coll_Lv;

    //上下拉刷新
    private PullToRefreshLayout mPullView;
    private LinearLayout mFrag_sel_center;

    private View view;
    private MyGoodsAdapter adapter;
    private Map<String, String> map = new HashMap<>();
    private Map<String, Object> map_state = new HashMap<>();
    private Map<String, String> map2 = new HashMap<>();
    private int id;
    private boolean flag = true;
    private List<Info_Collection> list = new ArrayList<Info_Collection>();
    private SharedUtils utils;
    private int index = 1;
    private String token;
    private View mylistview;
    private boolean isFirst = true;
    //    private CustomProgressDialog dialog;

    //区分两次请求数据 删除前和删除后
    private int index1 = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == index) {
                List<Info_Collection> list1 = (List<Info_Collection>) msg.obj;
                for (int i = 0; i < list1.size(); i++) {
//                    if (list1.get(i).getState() == 1) {
                        list.add(list1.get(i));
//                    }
                }
                if (adapter == null) {
                    adapter = new MyGoodsAdapter(ActivityMyGoods.this, list);
                    mFrag_coll_Lv.setAdapter(adapter);
                    adapter.setOnDelListener(new Adapter_Collection.onSwipeListener() {
                        @Override
                        public void onDelete(int pos) {
                            map_state.put("id", list.get(pos).getId());
                            map_state.put("state", 9 + "");
                            map_state.put("token", token);
                            post_file_state(MODIFYURL, map_state);
                            adapter.getList().remove(pos);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    adapter.setOnClickListener(new Adapter_Collection.setOnClickListener() {
                        @Override
                        public void onItemClick(int postion) {
//                        int id = list.get(postion).getId();
                            Intent intent = new Intent(ActivityMyGoods.this, Activity_GoodsDetail.class);
                            intent.putExtra("id", postion);
                            startActivity(intent);
                        }
                    });
                } else {
                    adapter.notifyDataSetChanged();
                }

            }
            if (msg.what == 900) {
                //刷新
                isFirst = false;
                index = 1;
                list.clear();
                initMap();
                post_file(ISSUELISTURL, map);
            }
            if (msg.what == 909) {
                //加载更多
                isFirst = false;
                initMap2();
                post_file(ISSUELISTURL, map2);
            }
        }
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_goods);
        utils = ((MyAppalication) this.getApplicationContext()).utils;
        token = utils.getShared("token", this);
        if (token == null || token == "") {
            Intent intent = new Intent(this, Activity_Login.class);
            startActivity(intent);
        }
        initView();
        initMap();
        post_file(ISSUELISTURL, map);
    }

    /*
    *  *Dialog对话框提示用户
   */
    protected void Dialog(final int postion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("再次编辑，并从新发布宝贝！");
        builder.setTitle("提示");
        builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(ActivityMyGoods.this, Activity_PushGoods.class);
                intent.putExtra("id", postion);
                startActivity(intent);
                ActivityMyGoods.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            }
        });
        builder.create().show();
    }
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
                            if (("200").equals(zhuangtai)) {
                                //成功
                                ActivityMyGoods.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPullView.refreshFinish(PullToRefreshLayout.SUCCEED);
                                    }
                                });

                            } else {
                                //失败
                                ActivityMyGoods.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPullView.refreshFinish(PullToRefreshLayout.FAIL);
                                    }
                                });
                            }
                        } else {
                            //加载是否成功
                            if (zhuangtai.equals("200")) {
                                if (size > 0) {
                                    //成功
                                    ActivityMyGoods.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mPullView.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                                        }
                                    });
                                } else {
                                    ActivityMyGoods.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(ActivityMyGoods.this, "没有更多了", Toast.LENGTH_SHORT).show();
                                            mPullView.loadmoreFinish(PullToRefreshLayout.FAIL);
                                        }
                                    });
                                }
                            } else {
                                //失败
                                ActivityMyGoods.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPullView.loadmoreFinish(PullToRefreshLayout.FAIL);
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
    private void initMap() {
        token = utils.getShared("token", ActivityMyGoods.this);
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
        mFrag_sel_center = (LinearLayout)findViewById(R.id.mFrag_sel_center);
        mPullView = (PullToRefreshLayout)findViewById(R.id.mPullView);
        mPullView.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
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
        mylistview = View.inflate(this, R.layout.mylistview, null);
        mFrag_coll_Lv = (ListView) mylistview.findViewById(R.id.mFrag_coll_Lv);
        mFrag_sel_center.addView(mylistview);

    }
    protected void post_file_state(final String url, final Map<String, Object> map) {
        String state = (String) map.get("state");
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
                if (response.isSuccessful()) {
                    //String str = response.body().string();
                } else {

                }

            }
        });

    }
}
