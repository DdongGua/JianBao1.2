package com.bgs.jianbao12.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.bgs.jianbao12.R;
import com.bgs.jianbao12.bean.Info_Grid;
import com.bgs.jianbao12.utils.RecordSQLiteOpenHelper;
import com.bgs.jianbao12.view.MyListView;

import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */

public class Activity_Search extends Activity implements View.OnClickListener {
    private TextView serchgoods_tvtip, serchgoods_tvclear;
    private ImageView serchgoods_ivback, serchgoods_ivdelete;
    private EditText serchgoods_etinput;
    private MyListView serchgoods_listview;
    private RelativeLayout serchgoods_tvserch;
    private List<Info_Grid> list;

    private RecordSQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();

    }

    private void initView() {
        helper = new RecordSQLiteOpenHelper(this);
        serchgoods_tvserch = (RelativeLayout) findViewById(R.id.serchgoods_tvserch);
        serchgoods_tvtip = (TextView) findViewById(R.id.serchgoods_tvtip);
        serchgoods_tvclear = (TextView) findViewById(R.id.serchgoods_tvclear);

        serchgoods_ivback = (ImageView) findViewById(R.id.serchgoods_ivback);
        serchgoods_ivdelete = (ImageView) findViewById(R.id.serchgoods_ivdelete);
        serchgoods_etinput = (EditText) findViewById(R.id.serchgoods_etinput);
        serchgoods_listview = (MyListView) findViewById(R.id.serchgoods_listview);
        serchgoods_tvclear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteData();
                queryData("");
            }
        });
        serchgoods_tvserch.setOnClickListener(this);
        serchgoods_ivdelete.setOnClickListener(this);
        serchgoods_ivback.setOnClickListener(this);

        // 搜索框的键盘搜索键点击回调
        serchgoods_etinput.setOnKeyListener(new View.OnKeyListener() {// 输入完后按键盘上的搜索键

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = hasData(serchgoods_etinput.getText().toString().trim());
                    if (!hasData) {
                        insertData(serchgoods_etinput.getText().toString().trim());
                        queryData("");
                    }
                    Intent intent = new Intent(Activity_Search.this, Activity_result.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("input", serchgoods_etinput.getText().toString().trim());
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);

                }
                return false;
            }
        });
        // 搜索框的文本变化实时监听
        serchgoods_etinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
//                    serchgoods_etinput.setText("搜索历史");
                } else {
//                    serchgoods_etinput.setText("搜索结果");
                }
                String tempName = serchgoods_etinput.getText().toString();
                // 根据tempName去模糊查询数据库中有没有数据
                queryData(tempName);

            }
        });
        serchgoods_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                serchgoods_etinput.setText(name);
                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询，由你自己去实现
                Intent intent = new Intent(Activity_Search.this, Activity_result.class);
                Bundle bundle = new Bundle();
                bundle.putString("input", serchgoods_etinput.getText().toString().trim());
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        if (serchgoods_etinput.getText() != null) {
            serchgoods_ivdelete.setVisibility(View.VISIBLE);
        } else {
            serchgoods_ivdelete.setVisibility(View.GONE);
        }
        queryData("");
    }

    /**
     * 插入数据
     */
    private void insertData(String tempName) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into records(name) values('" + tempName + "')");
        db.close();
    }

    /**
     * 模糊查询数据
     */
    private void queryData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);
        // 创建adapter适配器对象
        adapter = new SimpleCursorAdapter(Activity_Search.this, android.R.layout.simple_list_item_1, cursor, new String[]{"name"},
                new int[]{android.R.id.text1}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        serchgoods_listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    /**
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String tempName) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(
                "select id as _id,name from records where name =?", new String[]{tempName});
        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        db.close();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.serchgoods_ivback:
                //跳转到之前的界面
                this.finish();
                break;
            case R.id.serchgoods_tvserch:
                // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                boolean hasData = hasData(serchgoods_etinput.getText().toString().trim());
                if (!hasData) {
                    insertData(serchgoods_etinput.getText().toString().trim());
                    queryData("");
                }
                Intent intent = new Intent(Activity_Search.this, Activity_result.class);
                Bundle bundle = new Bundle();
                bundle.putString("input", serchgoods_etinput.getText().toString().trim());
                intent.putExtra("bundle", bundle);
                startActivity(intent);

                break;
            case R.id.serchgoods_ivdelete:
                serchgoods_etinput.setText("");
                break;

        }
    }
}


