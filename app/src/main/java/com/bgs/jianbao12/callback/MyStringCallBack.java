package com.bgs.jianbao12.callback;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by 毛毛 on 2016/12/23.
 */

public class MyStringCallBack extends StringCallback {
    Activity act;
    Fragment fra;

    public MyStringCallBack(Activity act) {
        this.act = act;
    }

    public MyStringCallBack(Fragment fra) {
        this.fra = fra;
    }

    private static final String TAG = "MyMyStringCallBack";

    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(String response, int id) {
        checkToken(response);

    }

    public void checkToken(String response) {
        try {
            JSONObject object = new JSONObject(response);
            String status = (String) object.get("status");
            if ("301".equals(status)) {
                Log.e(TAG, "onResponse: 未登录");
                Toast.makeText(act == null ? fra.getActivity() : act, "亲，请先登录。", Toast.LENGTH_LONG).show();
//                (act == null ? fra.getActivity() : act).startActivity(new Intent((act == null ? fra.getActivity() : act), Activity_Login.class));
            } else if (status.equals("300") || status.equals("500")) {
                Toast.makeText(act == null ? fra.getActivity() : act,  "网络错误，请稍后重试", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
