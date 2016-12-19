package com.bgs.jianbao12.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgs.jianbao12.R;
import com.bgs.jianbao12.bean.Info_splb_2;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

public class Adapter_fragment extends BaseAdapter {
	private List<Info_splb_2> list;
	private Context context;

	public Adapter_fragment(List<Info_splb_2> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	public void refrush(List<Info_splb_2> list){
		this.list=list;
		notifyDataSetChanged();
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder;
		if (convertView==null){
			vHolder=new ViewHolder();
			convertView=View.inflate(context, R.layout.adapter_fragment_1,null);
			vHolder.Ltag= (ImageView) convertView.findViewById(R.id.Ltag);
			vHolder.Limg= (SimpleDraweeView) convertView.findViewById(R.id.Limg);
			//vHolder.user_icon= (ImageView) convertView.findViewById(R.id.user_icon);
			vHolder.Ltitle= (TextView) convertView.findViewById(R.id.Ltitle);
			vHolder.Lprice= (TextView) convertView.findViewById(R.id.Lprice);
		}else{
			vHolder= (ViewHolder) convertView.getTag();
		}
		Uri uri = Uri.parse(list.get(position).getImage());
		ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
				.setAutoRotateEnabled(true)
				.build();

		PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
				.setImageRequest(request)
				.build();
		vHolder.Limg.setController(controller);
		vHolder.Ltitle.setText(list.get(position).getTitle());
		vHolder.Lprice.setText(list.get(position).getPrice());
		if (list.get(position).getState()==0){
			vHolder.Ltag.setImageResource(R.mipmap.normal);
		}else if (list.get(position).getState()==1){
			vHolder.Ltag.setImageResource(R.mipmap.sold);
		}else if (list.get(position).getState()==2){
			vHolder.Ltag.setImageResource(R.mipmap.out);
		}
		return convertView;
	}
	private class ViewHolder{
		private ImageView user_icon,Ltag;
		private SimpleDraweeView Limg;
		private TextView Ltitle,Lprice;
	}
	
}
