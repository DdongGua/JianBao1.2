package com.bgs.jianbao12.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.activity.Activity_Fabu;
import com.bgs.jianbao12.activity.Activity_KeFu;
import com.bgs.jianbao12.activity.Activity_VerSion;
import com.bgs.jianbao12.activity.Activity_XinXi;
import com.bgs.jianbao12.bean.Info_xinxi;
import com.bgs.jianbao12.utils.PhotoUtils;
import com.bgs.jianbao12.utils.SharedUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static java.lang.String.valueOf;


/**
 * Created by 醇色 on 2016/11/25.
 * 我的
 */

public class FragmentMine extends Fragment {
    private String url = "http://192.168.4.188/Goods/app/user/upload.json";
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    /* private static final int TAKE_PICTURE = 0;
     private static final int CHOOSE_PICTURE = 1;*/
    //private static final int SCALE = 5;//照片缩小比例
    private Map map = new HashMap();
    private SharedUtils utils;
    @InjectView(R.id.mImg_head)
    ImageView mImgHead;
    @InjectView(R.id.mTv_login_no)
    TextView mTvLoginNo;
    LinearLayout mLinFavorite;
    @InjectView(R.id.mLin_info)
    LinearLayout mLinInfo;
    @InjectView(R.id.mLin_renzheng)
    LinearLayout mLinYijian;
    @InjectView(R.id.mLin_kefu)
    LinearLayout mLinKefu;
    @InjectView(R.id.mLin_fabu)
    LinearLayout mLinFaBu;
    @InjectView(R.id.mLin_updata)
    LinearLayout mLinUpData;
    private View v;
    private String xinxi_url = "http://192.168.4.188/Goods/app/user/info.json";
    private Info_xinxi info = new Info_xinxi();
    private File file;
    private String path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = View.inflate(getActivity(), R.layout.fragment_mine, null);
        utils = ((MyAppalication) getActivity().getApplicationContext()).utils;
        ButterKnife.inject(this, v);
        initMap();
        boolean b = utils.getBoolean("boolean", getActivity());
        if (b) {
            post_headImg(xinxi_url, map);
        } else {
            String headPath = utils.getShared("headPath", getActivity());

            //file:///sdcard/en
            Uri uri = Uri.fromFile(new File(headPath));
            // 清除Fresco对这条验证码的缓存
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromMemoryCache(uri);
            imagePipeline.evictFromDiskCache(uri);
            // combines above two lines
            imagePipeline.evictFromCache(uri);
            imagePipeline.clearCaches();
            mImgHead.setImageURI(uri);
        }
        return v;
    }

    private void initMap() {
        String token = utils.getShared("token", getActivity());
        map.put("token", token);
        if (token != null) {
            mTvLoginNo.setText("已登录");
            mTvLoginNo.setTextColor(getResources().getColor(R.color.primary));
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Info_xinxi info = (Info_xinxi) msg.obj;
                String imgurl = info.getPhoto();
                if (imgurl.equals("http://192.168.4.188/Goods/uploads/")) {
                    mImgHead.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Uri uri = Uri.parse(imgurl);
                    // 清除Fresco对这条验证码的缓存
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    imagePipeline.evictFromMemoryCache(uri);
                    imagePipeline.evictFromDiskCache(uri);
                    // combines above two lines
                    imagePipeline.evictFromCache(uri);
                    imagePipeline.clearCaches();
                    mImgHead.setImageURI(uri);
                    mImgHead.setDrawingCacheEnabled(true);
                    Bitmap bm = mImgHead.getDrawingCache();
                    utils.saveBoolean("boolean", false, getActivity());
                }
            }
        }
    };

    protected void post_headImg(final String url, final Map<String, Object> map) {
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
                        JSONObject data = object.getJSONObject("data");
                        if (!data.getString("photo").equals("")) {
                            info.setPhoto("http://192.168.4.188/Goods/uploads/" + data.getString("photo"));

                            Message msg = handler.obtainMessage();
                            msg.obj = info;
                            msg.what = 1;
                            Log.e("photo", info.getPhoto());
                            handler.sendMessage(msg);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("22222", response.message() + " error : body " + response.body().string());
                }
            }
        });

    }

    protected void post_photo(final String url, final Map<String, Object> map, File file) {
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
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("photo", file.getName(), body);
            MultipartBody build = requestBody.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(build)
                    .build();
            // readTimeout("请求超时时间" , 时间单位);
            client.newBuilder()
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .build()
                    .newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.code() == 200) {
                                String str = response.body().string();
                                try {
                                    JSONObject object = new JSONObject(str);
                                    String jsonCode = object.getString("status");
                                    if (jsonCode.equals("200")) {
                                        Log.e("111111", response.message() + " , body " + str);
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getActivity(), "上传失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                Log.e("22222", response.message() + " error : body " + response.body().string());
                            }


                        }
                    });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({R.id.mImg_head, R.id.mLin_info, R.id.mLin_renzheng, R.id.mLin_kefu, R.id.mLin_fabu, R.id.mLin_updata})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mLin_kefu:
                startActivity(new Intent(getActivity(), Activity_KeFu.class));
                break;
            case R.id.mImg_head:
                showChoosePicDialog();
                break;
            case R.id.mLin_info:
                startActivity(new Intent(getActivity(), Activity_XinXi.class));
                break;
//            case R.id.mLin_renzheng:
//                startActivity(new Intent(getActivity(), Activity_RenZheng.class));
//                break;
            case R.id.mLin_fabu:
                startActivity(new Intent(getActivity(), Activity_Fabu.class));
                break;
            case R.id.mLin_updata:
                startActivity(new Intent(getActivity(), Activity_VerSion.class));
                break;
        }
    }

    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = PhotoUtils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            mImgHead.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

        String imagePath = PhotoUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath + "");
        if (imagePath != null) {
            utils.saveShared("headPath", imagePath, getActivity());
            file = new File(imagePath);
            post_photo(url, map, file);
        }
    }
}
