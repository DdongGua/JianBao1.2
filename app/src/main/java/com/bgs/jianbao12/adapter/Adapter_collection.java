package com.bgs.jianbao12.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgs.jianbao12.MyAppalication;
import com.bgs.jianbao12.R;
import com.bgs.jianbao12.bean.Info_Collection;
import com.bgs.jianbao12.utils.TimeUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */

public class Adapter_collection extends BaseAdapter {
    private Context ctx;
    private List <Info_Collection>list;
    private Map<String,Object> map=new HashMap<>();
    private TimeUtils timeUtils;

    public Adapter_collection(Context ctx, List<Info_Collection> list) {
        this.ctx = ctx;
        this.list = list;
        timeUtils = ((MyAppalication)ctx.getApplicationContext()).timeUtils;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=new ViewHolder();
        if (view==null) {
            view=View.inflate(ctx, R.layout.adapter_collection, null);
            holder.mLv_time=(TextView) view.findViewById(R.id.mLv_time);
            holder.mLv_title=(TextView) view.findViewById(R.id.mLv_title);
            holder.mLv_price= (TextView) view.findViewById(R.id.mLv_price);
            holder.mLv_state= (ImageView) view.findViewById(R.id.mLv_state);
            holder.mLv_photo= (SimpleDraweeView) view.findViewById(R.id.mLv_photo);
//            holder.mLv_cat= (ImageView) view.findViewById(R.id.mLv_cat);
            view.setTag(holder);
        }else {
            holder=(ViewHolder) view.getTag();
        }
        String s = timeUtils.progressDate(list.get(i).getIssue_time());
        holder.mLv_time.setText(s);
        holder.mLv_title.setText(list.get(i).getTitle());
        holder.mLv_price.setText(list.get(i).getPrice());
        holder.mLv_title.setText(list.get(i).getTitle());
        if (list.get(i).getState()==0){
            holder.mLv_state.setImageResource(R.mipmap.normal);
        }else if (list.get(i).getState()==1){
            holder.mLv_state.setImageResource(R.mipmap.sold);
        }else if (list.get(i).getState()==2){
            holder.mLv_state.setImageResource(R.mipmap.out);
        }

        if (list.get(i).getImage().equals("http://192.168.4.188/Goods/uploads/")) {
            holder.mLv_photo.setImageResource(R.mipmap.nopicture);
        }else {
            Uri uri = Uri.parse(list.get(i).getImage());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setAutoRotateEnabled(true)
                    .build();

            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            holder.mLv_photo.setController(controller);
        }
        return view;
    }


    private class ViewHolder{
        private TextView mLv_time,mLv_title;
        private TextView mLv_price;
        private ImageView mLv_state;
        private SimpleDraweeView mLv_photo;

    }

}
