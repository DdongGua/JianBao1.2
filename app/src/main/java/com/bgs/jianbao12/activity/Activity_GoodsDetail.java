package com.bgs.jianbao12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.bean.Info_spxq_2;
import com.bgs.jianbao12.utils.SharedUtils;
import com.bgs.jianbao12.utils.TimeUtils;
import com.bgs.jianbao12.view.View_Advers_details;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
import uk.co.senab.photoview.PhotoViewAttacher;

import static java.lang.String.valueOf;


/**
 * Created by 醇色 on 2016/11/28.
 * 商品详细列表
 */

public class Activity_GoodsDetail extends Activity implements View.OnClickListener {
    private boolean autoChange=true;
    private boolean touchFlag=false;
    private ImageView Gshoucang_mimg;
    private TextView Gguan,Gowned,Gtitle,Gprice,Gdescription,Gmobile,Gwechat,Gqq,Gmail,Tv_conncet,Gissue_time,Gfollow;
    private ImageView Gtag;
    private LinearLayout Gcollection,Gcall;
    private boolean isCollection=true;
    private Map<String,Object> map=new HashMap<>();
    private Map<String,Object> map_guanzhu=new HashMap<>();
    private Map<String,Object> map_quxiao=new HashMap<>();
    private int id;
    private String url="http://192.168.4.188/Goods/app/item/detail.json";
    private String guanzhu_url="http://192.168.4.188/Goods/app/item/follow.json";
    private List<String> list_url=new ArrayList<>();
    private List<String> stringList=new ArrayList<>();
    private Info_spxq_2 info_spxq_2;
    private int [] array;
    private View_Advers_details view_advers_details;
    private LinearLayout linearLayout;
    private String mobilenum;
    private MyDialog myDialog;
    private SharedUtils utils;
    private TimeUtils timeUtils;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetail);
        utils=((MyAppalication)this.getApplicationContext()).utils;
        timeUtils=((MyAppalication)this.getApplicationContext()).timeUtils;
        id=getIntent().getIntExtra("id",0);
        initView();
        initMap();
        post_file(url,map);

    }

    private void initMap() {
        token = utils.getShared("token", this);
        map.put("id",id);
        map.put("token", token);
    }

    private void initView() {
        linearLayout= (LinearLayout) findViewById(R.id.viewpager);

        Gtag= (ImageView) findViewById(R.id.Gtag);
        Gtitle= (TextView) findViewById(R.id.Gtitle);
        Gprice= (TextView) findViewById(R.id.Gprice);
        Gdescription= (TextView) findViewById(R.id.Gdiscription);
        Gmobile= (TextView) findViewById(R.id.Gmobile);
        Gwechat= (TextView) findViewById(R.id.Gwechat);
        Gqq= (TextView) findViewById(R.id.Gqq);
        Gmail= (TextView) findViewById(R.id.Gemail);
        Gcollection= (LinearLayout) findViewById(R.id.Gcollection);
        Gcall= (LinearLayout) findViewById(R.id.Gcall);
        Gshoucang_mimg= (ImageView) findViewById(R.id.Gshoucang_mImg);
        Gissue_time= (TextView) findViewById(R.id.Gissue_time);
        Tv_conncet= (TextView) findViewById(R.id.Tv_connect);
        Gfollow= (TextView) findViewById(R.id.Gfollow);
        Gowned= (TextView) findViewById(R.id.Gowned);
        Gguan= (TextView) findViewById(R.id.Gguan);

        Gcollection.setOnClickListener(this);
        Gcall.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Gcollection:
                if (isCollection){
                    Gshoucang_mimg.setImageResource(R.mipmap.yishoucang);
                    Gguan.setText("已关注");
                    Toast.makeText(this,"已添加到关注",Toast.LENGTH_SHORT).show();
                    //TODO 传值到收藏列表
                    map_guanzhu.put("id",id);
                    map_guanzhu.put("act",0);
                    map_guanzhu.put("token",token);
                    post_file_guanzhu(guanzhu_url,map_guanzhu);
                    isCollection=false;
                }else{
                    Gshoucang_mimg.setImageResource(R.mipmap.shoucang);
                    Gguan.setText("关注");
                    Toast.makeText(this,"已取消关注",Toast.LENGTH_SHORT).show();
                    map_guanzhu.put("id",id);
                    map_guanzhu.put("act",1);
                    map_guanzhu.put("token",token);
                    post_file_guanzhu(guanzhu_url,map_guanzhu);
                    isCollection=true;
                }
                break;
            case R.id.Gcall:
                String num=mobilenum;
                //意图：想干什么事
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                //url:统一资源定位符
                //uri:统一资源标示符（更广）
                intent.setData(Uri.parse("tel:" + num));
                //开启系统拨号器
                startActivity(intent);
                break;
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==99){
                info_spxq_2 = (Info_spxq_2) msg.obj;
                Gtitle.setText(info_spxq_2.getTitle());
                Gprice.setText(info_spxq_2.getPrice());
                Gdescription.setText(info_spxq_2.getDescription());
                Tv_conncet.setText(info_spxq_2.getContact());
               //时间转换
                String s = timeUtils.progressDate(info_spxq_2.getIssue_time());
                Gissue_time.setText(s);
                Gfollow.setText(info_spxq_2.getFollow());
                Gmobile.setText(info_spxq_2.getMobile());
                if (info_spxq_2.getQq().equals("")){
                    Gqq.setText("暂未公布");
                }else{
                    Gqq.setText(info_spxq_2.getQq());
                }
                if (info_spxq_2.getWechat().equals("")){
                    Gwechat.setText("暂未公布");
                }else{
                    Gwechat.setText(info_spxq_2.getWechat());
                }
                if (info_spxq_2.getEmail().equals("")){
                    Gmail.setText("暂未公布");
                }else{
                    Gmail.setText(info_spxq_2.getEmail());
                }
                if (Integer.valueOf(info_spxq_2.getState())==0){
                    //正常
                    Gtag.setImageResource(R.mipmap.normal);
                }else if (Integer.valueOf(info_spxq_2.getState())==1){
                    //售出
                    Gtag.setImageResource(R.mipmap.sold);
                }else if (Integer.valueOf(info_spxq_2.getState())==2) {
                    Gtag.setImageResource(R.mipmap.out);
                    //下架
                }
                if (info_spxq_2.isFollowed()){
                    Gshoucang_mimg.setImageResource(R.mipmap.yishoucang);
                    Gguan.setText("已关注");
                }else{
                    Gshoucang_mimg.setImageResource(R.mipmap.shoucang);
                    Gguan.setText("关注");
                }
                if (info_spxq_2.isOwner()){
                    Gowned.setText("是");
                }else{
                    Gowned.setText("否");
                }
                final List<String> list = info_spxq_2.getList();
                if (list.size()==0){
                    ImageView imageView=new ImageView(Activity_GoodsDetail.this);
                    imageView.setImageResource(R.mipmap.nopicture);
                    linearLayout.addView(imageView);
                }else if (list.size()==2){
                    SimpleDraweeView imageView=new SimpleDraweeView(Activity_GoodsDetail.this);
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Uri uri = Uri.parse(list.get(0));
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                            .setAutoRotateEnabled(true)
                            .build();

                    PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .build();
                    imageView.setController(controller);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadB(list.get(0));
                        }
                    });
                    SimpleDraweeView imageView1=new SimpleDraweeView(Activity_GoodsDetail.this);
                    imageView1.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
                    imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    Uri uri1 = Uri.parse(list.get(0));
                    ImageRequest request1 = ImageRequestBuilder.newBuilderWithSource(uri1)
                            .setAutoRotateEnabled(true)
                            .build();

                    PipelineDraweeController controller1 = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request1)
                            .build();
                    imageView1.setController(controller1);

                    imageView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadB(list.get(1));
                        }
                    });
                    //居中
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setWeightSum(2);
                    linearLayout.addView(imageView);
                    linearLayout.addView(imageView1);

                }
                else{
                    array=new int[list.size()];
                    view_advers_details=new View_Advers_details(Activity_GoodsDetail.this,list,array);
                    linearLayout.addView(view_advers_details.getView());
                }
                //获取电话号码
                mobilenum=info_spxq_2.getMobile();
            }
            if (msg.what==44){
                Bitmap bitmap= (Bitmap) msg.obj;
                myDialog = new MyDialog(Activity_GoodsDetail.this,bitmap);
                myDialog.show();
            }
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        autoChange=false;
    }
    protected void post_file(final String url, final Map<String, Object> map) {
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getValue()!=null&&!"".equals(entry.getValue()))
                {  requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));}
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
               /* if (response.isSuccessful()) {
                    //String str = response.body().string();
                    Log.e("111111", response.message() + " , body " + str1);

                } else {
                    Log.e("22222" ,response.message() + " error : body " + response.body().string());
                }*/
                try {
                    Info_spxq_2 info_spxq_2=new Info_spxq_2();
                    JSONObject object=new JSONObject(str1);
                    JSONObject data = object.getJSONObject("data");
                    int id = data.getInt("id");
                    info_spxq_2.setId(id);
                    int user_id = data.getInt("user_id");
                    info_spxq_2.setUser_id(user_id);
                    String title = data.getString("title");
                    info_spxq_2.setTitle(title);
                    String description = data.getString("description");
                    info_spxq_2.setDescription(description);
                    String price = data.getString("price");
                    info_spxq_2.setPrice(price);
                    String contact = data.getString("contact");
                    info_spxq_2.setContact(contact);
                    String mobile = data.getString("mobile");
                    info_spxq_2.setMobile(mobile);
                    if(data.has("qq")){
                        String qq = data.getString("qq");
                        info_spxq_2.setQq(qq);
                    }else{
                        info_spxq_2.setQq("");
                    }
                    if(data.has("wechat")){
                        String wechat = data.getString("wechat");
                        info_spxq_2.setWechat(wechat);
                    }else{
                        info_spxq_2.setWechat("");
                    }
                    if(data.has("email")) {
                        String email = data.getString("email");
                        info_spxq_2.setEmail(email);
                    }else {
                        info_spxq_2.setEmail("");
                    }
                    String issue_time = data.getString("issue_time");
                    info_spxq_2.setIssue_time(issue_time);
                    if(data.has("follow")) {
                        String follow = data.getString("follow");
                        info_spxq_2.setFollow(follow);
                    }else{
                        info_spxq_2.setFollow("");
                    }
                    String state = data.getString("state");
                    info_spxq_2.setState(state);
                    boolean owner = data.getBoolean("owned");
                    info_spxq_2.setOwner(owner);
                    boolean followed=data.getBoolean("followed");
                    info_spxq_2.setFollowed(followed);
                    if(data.has("photos")) {
                        JSONArray photos = data.getJSONArray("photos");
                        for (int i=0;i<photos.length();i++){
                            String string = "http://192.168.4.188/Goods/uploads/"+photos.getString(i);
                            list_url.add(string);
                        }
                        info_spxq_2.setList(list_url);
                    }else{
                        info_spxq_2.setList(null);
                    }
                    Message msg=handler.obtainMessage();
                    msg.what=99;
                    msg.obj=info_spxq_2;
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

   /* @Override
    protected void onStop() {
        super.onStop();
        if (info_spxq_2.getList().size()!=0&&info_spxq_2.getList().size()!=2){
            view_advers_details.stopTime();
        }

    }*/

    protected void post_file_guanzhu(final String url, final Map<String, Object> map) {
        int act = (int) map.get("act");
        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getValue()!=null&&!"".equals(entry.getValue()))
                {  requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));}
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
                    Log.e("111111", response.message() + " , body------------------------------- " + str1);

                } else {
                    Log.e("22222" ,response.message() + " error : body " + response.body().string());
                }

            }
        });

    }
    private class MyDialog extends AlertDialog {
        private ImageView mImg_dialog;
        private Bitmap bitmap;
        private Context context;
        protected MyDialog(Context context,Bitmap bitmap) {
            super(context);
            this.context=context;
            this.bitmap=bitmap;
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_xml);
            mImg_dialog=(ImageView) findViewById(R.id.mImg_dialog);
            mImg_dialog.setImageBitmap(bitmap);
            PhotoViewAttacher attacher=new PhotoViewAttacher(mImg_dialog);
        }
    }
    //下载图片的方法
    public void loadB(final String string){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url=new URL(string);
                    URLConnection connection = url.openConnection();
                    InputStream is = connection.getInputStream();
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 2;//这个的值压缩的倍数（2的整数倍），数值越小，压缩率越小，图片越清晰
                    //返回原图解码之后的bitmap对象
                    Bitmap bitmap = BitmapFactory.decodeStream(is,null,opts);
                    Message msg=handler.obtainMessage();
                    msg.what=44;
                    msg.obj=bitmap;
                    handler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                }
            }
        }).start();
    }
}
