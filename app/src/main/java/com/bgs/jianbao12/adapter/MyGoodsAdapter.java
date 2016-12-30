package com.bgs.jianbao12.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bgs.jianbao12.R;
import com.bgs.jianbao12.activity.Activity_PushGoods;
import com.bgs.jianbao12.bean.Info_Collection;
import com.bgs.jianbao12.utils.NativeUtil;
import com.bgs.jianbao12.utils.TimeUtil;
import com.bgs.jianbao12.view.SwipeMenuLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import static com.bgs.jianbao12.Constant.IMGURL;
import static com.bgs.jianbao12.utils.TimeUtil.TIME_FORMAT;

/**
 * Created by Administrator on 2016/12/29.
 */

public class MyGoodsAdapter extends BaseAdapter {

    private Context ctx;
    private List<Info_Collection> list;
    //private Map<String, Object> map = new HashMap<>();


    private Adapter_Collection.onSwipeListener mOnSwipeListener;
    private Adapter_Collection.setOnClickListener setOnClickListener;

    public MyGoodsAdapter(Context ctx, List<Info_Collection> list) {
        this.ctx = ctx;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (getItemViewType(i)==0){
            //说明是成功的列表

            return getSuccessView(i, view);

        }else{
            //说明是失败的
            return getFailureView(i, view);

        }
    }

    @NonNull
    private View getFailureView(final int i, View view) {
        ViewHolderFailure holder = new ViewHolderFailure();
        if (view == null) {
            view = View.inflate(ctx, R.layout.adapter_failure, null);
            holder.mFail_time = (TextView) view.findViewById(R.id.mFail_time);
            holder.mFail_title = (TextView) view.findViewById(R.id.mFail_title);
            holder.mFail_price = (TextView) view.findViewById(R.id.mFail_price);
            holder.mFail_state = (ImageView) view.findViewById(R.id.mFail_state);
            holder.mFail_photo = (SimpleDraweeView) view.findViewById(R.id.mFail_photo);
//            holder.mFail_Delete = (Button) view.findViewById(R.id.mFail_Delete);
            holder.contentView = view.findViewById(R.id.mFail_swipe_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolderFailure) view.getTag();
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
            //根据控件大小压缩控件
            String image = list.get(i).getImage();
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            holder.mFail_photo.measure(w, h);
            int width =   holder.mFail_photo.getMeasuredHeight();
            int height =   holder.mFail_photo.getMeasuredWidth();
            NativeUtil.load(Uri.parse(image),   holder.mFail_photo, width, height);

        }

        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        if (setOnClickListener != null) {
            (holder.contentView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //setOnClickListener.onItemClick(list.get(i).getId());
                    int id = list.get(i).getId();
                    Dialog(id);
                }
            });
        }
        return view;
    }

    @NonNull
    private View getSuccessView(final int i, View view) {
        ViewHolderSuccess holder = new ViewHolderSuccess();
        if (view == null) {
            view = View.inflate(ctx, R.layout.adapter_seling, null);
            holder.mSel_time = (TextView) view.findViewById(R.id.mSel_time);
            holder.mSel_title = (TextView) view.findViewById(R.id.mSel_title);
            holder.mSel_price = (TextView) view.findViewById(R.id.mSel_price);
            holder.mSel_state = (ImageView) view.findViewById(R.id.mSel_state);
            holder.mSel_photo = (SimpleDraweeView) view.findViewById(R.id.mSel_photo);
            holder.mSel_Delete = (Button) view.findViewById(R.id.mSel_Delete);
            holder.contentView = view.findViewById(R.id.mSel_swipe_content);
            view.setTag(holder);
        } else {
            holder = (ViewHolderSuccess) view.getTag();
        }
        String s = TimeUtil.convertTime(TIME_FORMAT, list.get(i).getIssue_time());
        holder.mSel_time.setText(s);
        holder.mSel_title.setText(list.get(i).getTitle());
        holder.mSel_price.setText(list.get(i).getPrice());
        holder.mSel_title.setText(list.get(i).getTitle());

        if (list.get(i).getState() == 0) {
            holder.mSel_state.setImageResource(R.mipmap.off_shenhezhong);
        } else if (list.get(i).getState() == 1) {
            holder.mSel_state.setImageResource(R.mipmap.off_zhengchang);
        } else if (list.get(i).getState() == 3) {
            holder.mSel_state.setImageResource(R.mipmap.off_weitongguo);
        } else if (list.get(i).getState() == 9) {
            holder.mSel_state.setImageResource(R.mipmap.off_yixiajia);
        }

        if (list.get(i).getImage().equals(IMGURL)) {
            holder.mSel_photo.setImageResource(R.mipmap.nopicture);
        } else {
            //根据控件大小压缩控件
            String image = list.get(i).getImage();
            int w = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            holder.mSel_photo.measure(w, h);
            int width =  holder.mSel_photo.getMeasuredHeight();
            int height =  holder.mSel_photo.getMeasuredWidth();
            NativeUtil.load(Uri.parse(image),  holder.mSel_photo, width, height);
        }
        if (mOnSwipeListener != null) {
            final View finalView = view;
            (holder.mSel_Delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mOnSwipeListener) {
                        //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                        mOnSwipeListener.onDelete(i);
                        //且如果想让侧滑菜单同时关闭，需要同时调用
                        ((SwipeMenuLayout) finalView).quickClose();

                    }
                }

            });

        }
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

    //拿到item条目类型的数量
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    //拿到那个类型
    @Override
    public int getItemViewType(int position) {
        Info_Collection info_collection = list.get(position);
        int state = info_collection.getState();
        if (1==state){
            return 0;
        }
        else{
            return 1;
        }
    }


    private class ViewHolderFailure {
        View contentView;
        private TextView mFail_time, mFail_title;
        private TextView mFail_price;
        private ImageView mFail_state;
        private SimpleDraweeView mFail_photo;
        private Button mFail_Delete;
    }
    private class ViewHolderSuccess{
        View contentView;
        private TextView mSel_time, mSel_title;
        private TextView mSel_price;
        private ImageView mSel_state;
        private SimpleDraweeView mSel_photo;
        private Button mSel_Delete;


    }





    /**
     * 删除的接口回调
     */
    public static interface onSwipeListener {
        void onDelete(int pos);

    }
    public List getList() {
        return list;
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

    protected void Dialog(final int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage("再次编辑，并从新发布宝贝！");
        builder.setTitle("提示");
        builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
               // notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(ctx, Activity_PushGoods.class);
                intent.putExtra("id", id);
                Activity ctx = (Activity) MyGoodsAdapter.this.ctx;
                ctx.startActivity(intent);
                ctx.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
            }
        });
        builder.create().show();
    }


}
