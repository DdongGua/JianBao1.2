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


/**
 * Created by 醇色 on 2016/11/28.
 */

public class Adapter_gridview extends BaseAdapter {
    private List<Info_splb_2> list;
    private Context context;


    public Adapter_gridview(List<Info_splb_2> list, Context context) {
        this.list = list;
        this.context = context;
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
        ViewHolder holder;
        if (view==null){
            holder=new ViewHolder();
            view=View.inflate(context, R.layout.gridview,null);
            holder.mGrid_img= (SimpleDraweeView) view.findViewById(R.id.mGrid_img);
            holder.mGrid_price= (TextView) view.findViewById(R.id.mGrid_price);
            holder.mGrid_title= (TextView) view.findViewById(R.id.mGrid_title);
            holder.mGrid_tag= (ImageView) view.findViewById(R.id.mGrid_tag);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }
        if (list.get(i).getState()==0){
            holder.mGrid_tag.setImageResource(R.mipmap.normal);
        }else if (list.get(i).getState()==1){
            holder.mGrid_tag.setImageResource(R.mipmap.sold);
        }else if (list.get(i).getState()==2){
            holder.mGrid_tag.setImageResource(R.mipmap.out);
        }
        Uri uri = Uri.parse(list.get(i).getImage());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true)
                .build();

        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .build();
        holder.mGrid_img.setController(controller);
        holder.mGrid_title.setText(list.get(i).getTitle());
        holder.mGrid_price.setText(list.get(i).getPrice());
        return view;
    }
    private class ViewHolder{
        private TextView mGrid_price,mGrid_title;
        private ImageView mGrid_tag;
        private SimpleDraweeView mGrid_img;
    }
}
