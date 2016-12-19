package com.bgs.jianbao12.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.activity.Activity_GoodsDetail;
import com.bgs.jianbao12.activity.Activity_Login;
import com.bgs.jianbao12.bean.Info_splb_2;
import com.bgs.jianbao12.utils.SharedUtils;
import com.bgs.jianbao12.utils.ViewPagerScroller;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的广告轮播类
 * */
public class View_Advers implements OnPageChangeListener {
	private Context ctx;
	private RelativeLayout relat;//放置vp和点的的容器
	private MyViewPager mVp;//viewpager
	private LinearLayout mLinear;//存放底部显示点的容器
	private boolean touchFlag=false;//触摸锁
	private Bitmap pointS;//选中的点
	private Bitmap pointN;//没有选中的点
	private List<View> Vplist=new ArrayList<View>();
	private List<Info_splb_2> list1;
	private VpAdapter adapter;
	private int index=0;//当前vp的索引页
	private boolean ThreadFlag=true;
	private View [] views;
	private TimeThread tiemThread;
	private int [] arr=new int [5];
	private TextView mTv;
	private LinearLayout Blinear;

	private SharedUtils utils;
	private String token;



	public View_Advers(Context ctx, List<Info_splb_2> list1) {
		this.ctx=ctx;
		this.list1=list1;
		utils=((MyAppalication)ctx.getApplicationContext()).utils;
		token = utils.getShared("token", ctx);
		views=new View[list1.size()];
		initList();
		initView();
		initPoint();
		tiemThread=new TimeThread();
		tiemThread.start();
	}
	/*
	 * 获取当前父容器的方法
	 * */
	public RelativeLayout getView(){
		return relat;
	}
	/*
	 * 关闭线程的方法
	 * */
	public void stopTime(){
		ThreadFlag=false;
	}
	//初始化数据
	private void initList() {

		for (int i = 0; i < arr.length; i++) {
			Log.e("aaaaaaa",""+arr.length);
			Log.e("bbbbbb",""+list1.size());
			if (list1.get(i).getImage().equals("http://192.168.4.188/Goods/uploads/")){

				ImageView mImg=new ImageView(ctx);
				mImg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				mImg.setImageResource(R.mipmap.nopicture);
				mImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
				Vplist.add(mImg);
				views[i]=mImg;
			}else{
				ImageView mImg=new ImageView(ctx);
				mImg.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
				mImg.setImageResource(arr[i]);
				mImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
				//图片的异步加载
				Glide.with(ctx).load(list1.get(i).getImage()).into(mImg);
				Vplist.add(mImg);
				views[i]=mImg;
			}

		}
	}


	/*
	 * 初始化底部圆点的方法
	 * */
	private void initPoint() {
		for (int i = 0; i < 5; i++) {
			ImageView img=new ImageView(ctx);
			LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(20, 20);
			lp.rightMargin=10;
			lp.bottomMargin=5;
			img.setLayoutParams(lp);
			if(i==0){
				img.setImageBitmap(pointS);
			}else{
				img.setImageBitmap(pointN);
			}
			mLinear.addView(img, i);
		}
	}

	private void initView() {
		relat=new RelativeLayout(ctx);
		mVp=new MyViewPager(ctx);
		mTv=new TextView(ctx);
		//存放vp显示的字体与小圆点
		Blinear=new LinearLayout(ctx);
		RelativeLayout.LayoutParams lp_blinear=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp_blinear.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lp_blinear.addRule(RelativeLayout.CENTER_HORIZONTAL);
		Blinear.setOrientation(LinearLayout.VERTICAL);
		Blinear.setGravity(Gravity.CENTER_HORIZONTAL);
		Blinear.setLayoutParams(lp_blinear);
		mVp.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		mLinear=new LinearLayout(ctx);
		mLinear.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		mLinear.setLayoutParams(lp);
		//textview
		LinearLayout.LayoutParams lp1=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		mTv.setTextSize(16);
		mTv.setLayoutParams(lp1);
		mTv.setTextColor(Color.WHITE);
		mTv.setSingleLine(true);
		//将字体与小圆点加入到blinear中
		//字体的赋值在小圆点初始化中设置了
		Blinear.addView(mTv);
		Blinear.addView(mLinear);
		//设置adapter
		adapter=new VpAdapter();
		//设置viewpager页面切换的速度
		ViewPagerScroller scroll=new ViewPagerScroller(ctx);
		scroll.initViewPagerScroll(mVp);
		mVp.setAdapter(adapter);


		relat.addView(mVp);
		relat.addView(Blinear);
		//读取圆点图片
//		pointN=BitmapFactory.decodeResource(ctx.getResources(), R.drawable.community_ad_banner_point_nor);
//		pointS=BitmapFactory.decodeResource(ctx.getResources(), R.drawable.community_ad_banner_point_sel);

		//给vp设置滑动监听
		mVp.setOnPageChangeListener(this);

	}
	Handler hand=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0){
				if(touchFlag){
					return;
				}
				if(!ThreadFlag){
					return;
				}
				index++;
				mVp.setCurrentItem(index);
			}
		}
	};
	private class VpAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			try {
				((ViewPager)container).addView(Vplist.get(position%Vplist.size()));

				views[position%Vplist.size()].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (token==""||token==null){
							Intent intent=new Intent(ctx, Activity_Login.class);
							ctx.startActivity(intent);
						}else{
							int id = list1.get(position%list1.size()).getId();
							Log.e("","**********************"+id);
							Intent intent=new Intent(ctx, Activity_GoodsDetail.class);
							intent.putExtra("id",id);
							ctx.startActivity(intent);
						}

					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Vplist.get(position%Vplist.size());
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			try {
				((ViewPager)container).removeView(Vplist.get(position%Vplist.size()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * vp的滑动监听
	 * */
	@Override
	public void onPageScrollStateChanged(int arg0) {
		if(arg0== ViewPager.SCROLL_STATE_IDLE){
			touchFlag=false;
		}else{
			touchFlag=true;
		}
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
	@Override
	public void onPageSelected(int arg0) {
		index=arg0;
		//根据当前viewpager页卡切换底部对应点
		for (int i = 0; i < Vplist.size(); i++) {
			ImageView mImg = (ImageView) mLinear.getChildAt(i);

			if(i!=(arg0%Vplist.size())){
				mImg.setImageBitmap(pointN);
			}else{
				mImg.setImageBitmap(pointS);
				mTv.setText(list1.get(i).getTitle());
			}
		}
	}
	/**
	 * 子线程内部类 
	 * */
	public class TimeThread extends Thread{
		@Override
		public void run() {
			super.run();
			while(ThreadFlag){
				try {
					Thread.sleep(3000);
					hand.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
	

}
