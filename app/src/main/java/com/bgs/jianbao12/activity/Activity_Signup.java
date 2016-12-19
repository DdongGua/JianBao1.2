package com.bgs.jianbao12.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bgs.jianbao12.R;
import com.bgs.jianbao12.utils.PhotoUtils;
import com.bgs.jianbao12.utils.RoundBitmapUtils;

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

import static com.bgs.jianbao12.R.id.mImg_idcard;
import static java.lang.String.valueOf;


/**
 * Created by 毛毛 on 2016/11/30.
 * 注册界面
 */

public class Activity_Signup extends Activity {
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;


    @InjectView(mImg_idcard)
    ImageView mImgIdcard;
    private ProgressDialog progressDialog;

    @InjectView(R.id.mTil_QQ)
    TextInputLayout mTilQQ;
    @InjectView(R.id.mTil_wechat)
    TextInputLayout mTilWechat;
    @InjectView(R.id.mTil_email)
    TextInputLayout mTilEmail;
    private boolean isMore;
    @InjectView(R.id.input_code)
    EditText inputcode;
    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;
    @InjectView(R.id.input_phone1)
    EditText inputPhone;
    @InjectView(R.id.mRbtn_man)
    RadioButton mRbtnMan;
    @InjectView(R.id.mRbtn_woman)
    RadioButton mRbtnWoman;
    @InjectView(R.id.input_more)
    TextView inputMore;
    @InjectView(R.id.input_QQ)
    EditText inputQQ;
    @InjectView(R.id.input_wechat)
    EditText inputWechat;
    private String url = "http://192.168.4.188/Goods/app/common/register.json";
    private File file;
    private Map map = new HashMap<>();
    private Map map1 = new HashMap<>();
    private boolean valid;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.inject(this);
        Bitmap bitmap = RoundBitmapUtils.getBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.iiiiiii));
        mImgIdcard.setImageBitmap(bitmap);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
                post_file(url, map, file);
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    private void initMap() {
        map1.put("code",inputcode.getText());
    }

    private void initData() {
        validate();
        if (!valid) {
            Toast.makeText(this, "请检查您输入的信息", Toast.LENGTH_SHORT).show();
            return;
        }
        map.put("mobile", inputPhone.getText().toString());
        map.put("qq", inputQQ.getText().toString());
        map.put("wechat", inputWechat.getText().toString());
        map.put("name", _nameText.getText().toString());
        map.put("code", inputcode.getText().toString());
        map.put("password", _passwordText.getText().toString());
        if (mRbtnMan.isChecked()) {
            map.put("gender", "男");
        }
        if (mRbtnWoman.isChecked()) {
            map.put("gender", "女");
        }
    }


    protected void post_file(final String url, final Map<String, Object> map, File file) {

        OkHttpClient client = new OkHttpClient();
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("card", file.getName(), body);
        }
        if (map != null && !map.isEmpty()) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                if (entry.getValue() != null && !"".equals(entry.getValue())) {
                    requestBody.addFormDataPart(valueOf(entry.getKey()), valueOf(entry.getValue()));
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
                    if (response.code() == 200) {
                        String str = response.body().string();

                        try {
                            JSONObject jo = new JSONObject(str);
                            String statusCode = (String) jo.get("status");
                            if (!"200".equals(statusCode)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Activity_Signup.this, "填写信息有误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Activity_Signup.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        signup();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                _signupButton.setEnabled(false);

                                                progressDialog = new ProgressDialog(Activity_Signup.this,
                                                        R.style.AppTheme_Dark_Dialog);
                                                progressDialog.setIndeterminate(true);
                                                progressDialog.setMessage("账号创建中...");
                                                progressDialog.show();
                                            }
                                        });
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Activity_Signup.this, "1212131212123", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });
        }

    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }
        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "注册失败", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);

    }

    public boolean validate() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                valid = true;
                String code = inputcode.getText().toString().trim();
                String name = _nameText.getText().toString().trim();
                String password = _passwordText.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();


                if (code == null || "".equals(code)) {
                    inputcode.setError("输入的邀请码有误");
                    valid = false;
                } else {
                    inputcode.setError(null);
                }
                if (!isMobileNO(phone)) {
                    inputPhone.setError("输入的手机号有误");
                    valid = false;
                } else {
                    inputPhone.setError(null);
                }
                if (name.isEmpty() || name.length() > 6) {
                    _nameText.setError("名字长度不能大于三个字");
                    valid = false;
                } else {
                    _nameText.setError(null);
                }

                if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
                    _passwordText.setError("密码为4 - 20个字母数字字符");
                    valid = false;
                } else {
                    _passwordText.setError(null);
                }
            }
        });


        return valid;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
		移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        String telRegex = "[1][358]\\d{9}";
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    @OnClick({mImg_idcard, R.id.input_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case mImg_idcard:
                showChoosePicDialog();
                break;
            case R.id.input_more:
                if (!isMore) {
                    mTilEmail.setVisibility(View.VISIBLE);
                    mTilQQ.setVisibility(View.VISIBLE);
                    mTilWechat.setVisibility(View.VISIBLE);
                    isMore = !isMore;
                } else {
                    mTilEmail.setVisibility(View.GONE);
                    mTilQQ.setVisibility(View.GONE);
                    mTilWechat.setVisibility(View.GONE);
                    isMore = !isMore;
                }
                break;
        }
    }
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Signup.this);
        builder.setTitle("选择图片");
        String[] items = { "选择本地照片","拍照" };
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {

                    case TAKE_PICTURE: // 拍照
                        Intent openCameraIntent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        tempUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "image.jpg"));
                        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                        startActivityForResult(openCameraIntent, TAKE_PICTURE);
                        break;
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        // aspectX aspectY 是宽高的比例);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = PhotoUtils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            mImgIdcard.setImageBitmap(photo);
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
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            file = new File(imagePath);
            post_file(url, map, file);
        }
    }

}
