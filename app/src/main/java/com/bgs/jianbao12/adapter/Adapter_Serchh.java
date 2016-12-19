package com.bgs.jianbao12.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgs.jianbao12.R;
import com.bgs.jianbao12.bean.Info_Seach;
import com.bgs.jianbao12.utils.TimeUtils;
import com.bgs.jianbao12.view.LoadMoreRecyclerView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;


/**
 */
public class Adapter_Serchh extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Info_Seach> list;
    private boolean mIsStagger;
    private Context ctx;
    private TimeUtils timeUtils;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public Adapter_Serchh(Context ctx, List<Info_Seach> items) {
        this.ctx = ctx;
        this.list = items;
    }

    public void switchMode(boolean mIsStagger) {
        this.mIsStagger = mIsStagger;
    }

    public void setData(List<Info_Seach> datas) {
        this.list = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LoadMoreRecyclerView.TYPE_STAGGER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_grid, parent, false);
            return new StaggerViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_linear, parent, false);
            return new LinearViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (mIsStagger) {
            //网格
            StaggerViewHolder staggerViewHolder = (StaggerViewHolder) holder;
            staggerViewHolder.tv_title.setText(list.get(position).getTitle());
            staggerViewHolder.tv_price.setText(list.get(position).getPrice());
            String s = timeUtils.progressDate(list.get(position).getTime());
            staggerViewHolder.tv_time.setText(s);
            if (list.get(position).getImgstr().equals("http://192.168.4.188/Goods/uploads/")) {
                staggerViewHolder.iv_photo.setImageResource(R.mipmap.nopicture);
            } else {
                Uri uri = Uri.parse(list.get(position).getImgstr());

                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                        .setAutoRotateEnabled(true)
                        .build();

                PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .build();
                staggerViewHolder.iv_photo.setController(controller);

            }

            holder.itemView.setTag(list.get(position));
        } else {
            //listview
            LinearViewHolder linearViewHolder = (LinearViewHolder) holder;
            linearViewHolder.tv_title.setText(list.get(position).getTitle());
            linearViewHolder.tv_price.setText(list.get(position).getPrice());
            String s = timeUtils.progressDate(list.get(position).getTime());
            linearViewHolder.tv_time.setText(s);
            if (list.get(position).getImgstr().equals("http://192.168.4.188/Goods/uploads/")) {
                linearViewHolder.iv_photo.setImageResource(R.mipmap.nopicture);
            } else {
                Uri uri = Uri.parse(list.get(position).getImgstr());
                linearViewHolder.iv_photo.setImageURI(uri);
            }
            //将数据保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(list.get(position));
        }
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                  从布局中获取条目位置
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    //网格
    public class StaggerViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_price, tv_time, tv_state;
        private SimpleDraweeView iv_photo;

        public StaggerViewHolder(View itemView) {
            super(itemView);
            iv_photo = (SimpleDraweeView) itemView.findViewById(R.id.iv_photo);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    //listview
    public class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_id, tv_title, tv_price, tv_time, tv_state;
        private ImageView iv_photo;

        public LinearViewHolder(View itemView) {
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }

    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int postion);
    }

    public void setCall(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
