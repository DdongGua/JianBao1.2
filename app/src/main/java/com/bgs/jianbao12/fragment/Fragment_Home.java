package com.bgs.jianbao12.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.activity.Activity_GoodsDetail;
import com.bgs.jianbao12.activity.Activity_Login;
import com.bgs.jianbao12.activity.Activity_Search;
import com.bgs.jianbao12.adapter.Adapter_fragment;
import com.bgs.jianbao12.adapter.Adapter_gridview;
import com.bgs.jianbao12.bean.Info_shouyel;
import com.bgs.jianbao12.bean.Info_splb_2;
import com.bgs.jianbao12.utils.SharedUtils;
import com.bgs.jianbao12.view.PullToRefrushView;
import com.bgs.jianbao12.view.View_Advers;

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
 * Created by 醇色 on 2016/11/25.
 * 首页
 */

public class Fragment_Home extends Fragment implements PullToRefrushView.Pull_To_Load {
    private View v;
    private PullToRefrushView mPullToRefrushView;
    private View centerLinear;
    private LinearLayout myViewPager;
    private View_Advers viewAdvers;
    private List<Info_shouyel> list = new ArrayList<Info_shouyel>();
    private ListView mFrag_home_lv;
    private Adapter_fragment adapter_fragment;
    private GridView mGridView;
    private ImageView mFrag_home_search;
    private Adapter_gridview adapter_grid;
    //vp
    private String url = "http://192.168.4.188/Goods/app/item/list.json";
    private Map<String, String> map = new HashMap<>();
    private List<Info_splb_2> info_splb2vp = new ArrayList<>();
    //listview 资源
    private Map<String, String> map1 = new HashMap<>();
    private List<Info_splb_2> info_splblist = new ArrayList<>();
    //gridview 资源
    Map<String, String> map2 = new HashMap<>();
    private int index = 2;
    private List<Info_splb_2> list1 = new ArrayList<>();

    private SharedUtils utils;
    private String token;

    private TextView CTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = View.inflate(getActivity(), R.layout.fragment_home, null);
        utils = ((MyAppalication) getActivity().getApplicationContext()).utils;
        token = utils.getShared("token", getActivity());
//        netWorkHelper.isNetworkAvailable(getActivity());
        initMap();
        initMap1();
        post_file(url, map);
        post_file(url, map1);
        initView();
        return v;
    }

    private void initMap2() {
        index++;
        map2.clear();
        map2.put("curPage", String.valueOf(index));
    }

    private void initMap1() {
        map1.put("curPage", "2");
    }

    private void initMap() {
        map.put("curPage", "1");
    }

    private void initView() {
        mPullToRefrushView = (PullToRefrushView) v.findViewById(R.id.mPullToRefrushView);

        mPullToRefrushView.setCall(this);
        //viewpager的主布局
        centerLinear = View.inflate(getActivity(), R.layout.centerlinear, null);
        mPullToRefrushView.setCenterView(centerLinear);
        //顶部vp
        myViewPager = (LinearLayout) centerLinear.findViewById(R.id.mAdver);

        CTv = (TextView) centerLinear.findViewById(R.id.CTv);
        //listview
        mFrag_home_lv = (ListView) centerLinear.findViewById(R.id.mFrag_home_lv);
        mFrag_home_lv.setFocusable(false);
        //gridview
        mGridView = (GridView) centerLinear.findViewById(R.id.mGrid);
        mGridView.setNumColumns(2);
        mGridView.setColumnWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        mGridView.setVerticalSpacing(6);
        mGridView.setHorizontalSpacing(5);
        mFrag_home_search= (ImageView) v.findViewById(R.id.mFrag_home_search);
        mFrag_home_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity().getApplicationContext(), Activity_Search.class);
                startActivity(intent);
            }
        });

    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 66) {
                //刷新
                mPullToRefrushView.complate();
                CTv.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
                info_splblist.clear();
                list1.clear();
                initMap1();
                post_file(url, map1);
                index = 2;
                mGridView.setVisibility(View.GONE);
            }
            if (msg.what == 88) {
                if (list1.size() >= 60) {
                    mPullToRefrushView.complate();
                    CTv.setVisibility(View.VISIBLE);
                } else {
                    //加载更多
                    mPullToRefrushView.complate();
                    mGridView.setVisibility(View.VISIBLE);
                    initMap2();
                    post_file(url, map2);
                }

            }
            if (msg.what == 1) {
                info_splb2vp = (List<Info_splb_2>) msg.obj;
                viewAdvers = new View_Advers(getActivity(), info_splb2vp);
                myViewPager.addView(viewAdvers.getView());
            } else if (msg.what == 2) {
                info_splblist = (List<Info_splb_2>) msg.obj;
                adapter_fragment = new Adapter_fragment(info_splblist, getActivity());
                mFrag_home_lv.setAdapter(adapter_fragment);
                mPullToRefrushView.MesureListHeight(mFrag_home_lv);
                mFrag_home_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (token == null || token == "") {
                            Intent intent = new Intent(getActivity(), Activity_Login.class);
                            startActivity(intent);
                        } else {
                            int id = info_splblist.get(i).getId();
                            Intent intent = new Intent(getActivity(), Activity_GoodsDetail.class);
                            intent.putExtra("id", id);
                            getActivity().startActivity(intent);
                        }

                    }
                });
            } else if (msg.what == index) {
                final List<Info_splb_2> list = (List<Info_splb_2>) msg.obj;
                for (int i = 0; i < list.size(); i++) {
                    list1.add(list.get(i));
                }
                Toast.makeText(getActivity(), "加载成功", Toast.LENGTH_SHORT).show();
                adapter_grid = new Adapter_gridview(list1, getActivity());
                mGridView.setAdapter(adapter_grid);
                mPullToRefrushView.calGridViewWidthAndHeigh(2, mGridView);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if (token == null || token == "") {
                            Intent intent = new Intent(getActivity(), Activity_Login.class);
                            startActivity(intent);
                        } else {
                            int id = list1.get(i).getId();
                            Intent intent = new Intent(getActivity(), Activity_GoodsDetail.class);
                            intent.putExtra("id", id);
                            getActivity().startActivity(intent);
                        }

                    }
                });

            }
        }
    };

    @Override
    public void Load() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    handler.sendEmptyMessage(88);
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
                    handler.sendEmptyMessage(66);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
                final String str1 = response.body().string();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject object = new JSONObject(str1);
                            JSONObject data = object.getJSONObject("data");
                            int size = data.getInt("size");
                            JSONArray list = data.getJSONArray("list");
                            List<Info_splb_2> list1 = new ArrayList<Info_splb_2>();
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject jsonObject = list.getJSONObject(i);
                                int id1 = jsonObject.getInt("id");
                                String title1 = jsonObject.getString("title");
                                String image1 = "http://192.168.4.188/Goods/uploads/" + jsonObject.getString("image");
                                String price1 = jsonObject.getString("price");
                                String issue_time1 = jsonObject.getString("issue_time");
                                int state1 = jsonObject.getInt("state");
                                Info_splb_2 info_splb2 = new Info_splb_2(id1, title1, image1, issue_time1, state1, price1);
                                list1.add(info_splb2);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGridView.setVisibility(View.GONE);
//        viewAdvers.stopTime();
    }


}
