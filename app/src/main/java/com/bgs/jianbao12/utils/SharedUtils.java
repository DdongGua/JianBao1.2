package com.bgs.jianbao12.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.bgs.jianbao12.activity.Activity_FirstStart;
import com.bgs.jianbao12.activity.Activity_Guide;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



/**
 *本类为sharedpreferences保存类    xml本地保存类
 *	 sharedpreferences：android 五大存储方式之一，存储数据类型为： K  V  文件已xml形式保存
 *					项目应用：1.导航页是否第一次进入
 *						    2.用户信息，登陆信息
 * 好处：只要不卸载软件，或者不手动清除，基本上不会被清除
 * */
public class SharedUtils extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String str=new SharedUtils().getShared("tag", SharedUtils.this);
		if(str.equals("")){
			Intent intent=new Intent(SharedUtils.this, Activity_Guide.class);
			startActivity(intent);
		}else {
			Intent intent=new Intent(SharedUtils.this, Activity_FirstStart.class);
			startActivity(intent);
		}
		finish();
	}

	private String name="jianbao";
	private String touxiang = "touxiang";
	/*
	 * 保存数据的方法
	 * */
	public void saveShared(String key,String value,Context ctx){
		SharedPreferences shared=ctx.getSharedPreferences(name,0);
		Editor edit = shared.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	/*
	 * 从本地获取数据
	 * */
	public String getShared(String key,Context ctx){
		String str=null;
		SharedPreferences shared = ctx.getSharedPreferences(name, 0);
		str = shared.getString(key, "");
		return str;
	}
	/*
	* 保存对象
	* */
	public boolean saveObject(String key,Object object,Context context ) {
// TODO Auto-generated method stub
		SharedPreferences share = PreferenceManager
				.getDefaultSharedPreferences(context);
		if (object == null) {
			Editor editor = share.edit().remove(key);
			return editor.commit();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
// 将对象放到OutputStream中
// 将对象转换成byte数组，并将其进行base64编码
		String objectStr = new String(Base64.encode(baos.toByteArray(),
				Base64.DEFAULT));
		try {
			baos.close();
			oos.close();
		} catch (IOException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Editor editor = share.edit();
// 将编码后的字符串写到base64.xml文件中
		editor.putString(key, objectStr);
		return editor.commit();
	}
	/*
	* 获取对象
	* */
	public static Object getObject(String key,Context context ) {
		SharedPreferences sharePre = PreferenceManager
				.getDefaultSharedPreferences(context);
		try {
			String wordBase64 = sharePre.getString(key, "");
// 将base64格式字符串还原成byte数组
			if (wordBase64 == null || wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
				return null;
			}
			byte[] objBytes = Base64.decode(wordBase64.getBytes(),
					Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
// 将byte数组转换成product对象
			Object obj = ois.readObject();
			bais.close();
			ois.close();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void saveBoolean(String key,boolean value,Context ctx){
		SharedPreferences shared=ctx.getSharedPreferences(name,0);
		Editor edit = shared.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}
	public boolean getBoolean(String key,Context ctx){
		boolean b = false;
		SharedPreferences shared = ctx.getSharedPreferences(name, 0);
		b = shared.getBoolean(key, b);
		return b;
	}
}
