package com.bgs.jianbao12;

import android.app.Application;

import com.bgs.jianbao12.utils.SharedUtils;
import com.bgs.jianbao12.utils.TimeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;


public class MyAppalication extends Application{
	public SharedUtils utils;
	public TimeUtils timeUtils;

	@Override
	public void onCreate() {
		super.onCreate();
		Fresco.initialize(this);
		utils = new SharedUtils();
		timeUtils=new TimeUtils();
	}
}
