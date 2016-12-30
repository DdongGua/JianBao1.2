package com.bgs.jianbao12.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgs.jianbao12.R;
import com.bgs.jianbao12.bean.Info_Collection;
import com.bgs.jianbao12.utils.TimeUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import static com.bgs.jianbao12.Constant.IMGURL;
import static com.bgs.jianbao12.utils.TimeUtil.TIME_FORMAT;

/**
 * Created by Administrator on 2016/12/25.
 */

public class Adapter_failure extends BaseAdapter {
    private Context ctx;
    private List<Info_Collection> list;
    private Adapter_Collection.onSwipeListener mOnSwipeListener;
    private Adapter_Collection.setOnClickListener setOnClickListener;

    public Adapter_failure(Context ctx, List<Info_Collection> list) {
        this.ctx = ctx;
        this.list = list;


    }

    public List getList() {
        return list;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = View.inflate(ctx, R.layout.adapter_failure, null);
            holder.mFail_time = (TextView) view.findViewById(R.id.mFail_time);
            holder.mFail_title = (TextView) view.findViewById(R.id.mFail_title);
            holder.mFail_price = (TextView) view.findViewById(R.id.mFail_price);
            holder.mFail_state = (ImageView) view.findViewById(R.id.mFail_state);
            holder.mFail_photo = (SimpleDraweeView) view.findViewById(R.id.mFail_photo);
            holder.contentView = view.findViewById(R.id.mFail_swipe_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String s = TimeUtil.convertTime(TIME_FORMAT, list.get(i).getIssue_time());
        holder.mFail_time.setText(s);
        holder.mFail_title.setText(list.get(i).getTitle());
        holder.mFail_price.setText(list.get(i).getPrice());
        holder.mFail_title.setText(list.get(i).getTitle());
        if (list.get(i).getState() == 0) {
            holder.mFail_state.setImageResource(R.mipmap.off_shenhezhong);
        } else if (list.get(i).getState() == 1) {
            holder.mFail_state.setImageResource(R.mipmap.off_zhengchang);
        } else if (list.get(i).getState() == 3) {
            holder.mFail_state.setImageResource(R.mipmap.off_weitongguo);
        } else if (list.get(i).getState() == 9) {
            holder.mFail_state.setImageResource(R.mipmap.off_yixiajia);
        }

        if (list.get(i).getImage().equals(IMGURL)) {
            holder.mFail_photo.setImageResource(R.mipmap.nopicture);
        } else {
            Uri uri = Uri.parse(list.get(i).getImage());
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setAutoRotateEnabled(true)
                    .build();

            PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .build();
            holder.mFail_photo.setController(controller);
        }
//        if (mOnSwipeListener != null) {
//            final View finalView = view;
//            (holder.mFail_Delete).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (null != mOnSwipeListener) {
//                        //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
//                        mOnSwipeListener.onDelete(i);
//                        //且如果想让侧滑菜单同时关闭，需要同时调用
//                        ((SwipeMenuLayout) finalView).quickClose();
//
//                    }
//                }
//
//            });
//
//        }
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        if (setOnClickListener != null) {
            (holder.contentView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setOnClickListener.onItemClick(list.get(i).getId());
//                int id = list.get(i).getId();
//                Intent intent = new Intent(ctx, Activity_GoodsDetail.class);
//                intent.putExtra("id", id);
//                ctx.startActivity(intent);
                }
            });
        }
        return view;
    }


    private class ViewHolder {
        View contentView;
        private TextView mFail_time, mFail_title;
        private TextView mFail_price;
        private ImageView mFail_state;
        private SimpleDraweeView mFail_photo;
        private Button mFail_Delete;


    }

    /**
     * 删除的接口回调
     */
    public static interface onSwipeListener {
        void onDelete(int pos);

    }

    public void setOnDelListener(Adapter_Collection.onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    /**
     * 条目点击事件的回调
     */

    public interface setOnClickListener {
        void onItemClick(int postion);
    }

    public void setOnClickListener(Adapter_Collection.setOnClickListener listener) {
        setOnClickListener = listener;
    }


}
