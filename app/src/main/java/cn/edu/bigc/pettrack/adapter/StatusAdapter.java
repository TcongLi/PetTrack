package cn.edu.bigc.pettrack.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.edu.bigc.pettrack.Event.StatusEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.activity.StatusDetailActivity;

/**
 * Created by L T on 2016/10/2.
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.MyViewHolder> {
    List<AVObject> statusList;
    Context mContext;
    Activity activity;

    public StatusAdapter(List<AVObject> statusList, Context mContext, Activity activity) {
        this.statusList = statusList;
        this.mContext = mContext;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.status_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AVObject status = statusList.get(position);
        AVUser user = (AVUser) status.get("user");
        String url = status.getString("imgURL");
        if (!(url == null || url.equals(""))) {
            Glide.with(mContext).load(url).crossFade().fitCenter().into(holder.imgStatus);
        } else {
            // make sure Glide doesn't load anything into this view until told otherwise
            Glide.clear(holder.imgStatus);
            // remove the placeholder (optional); read comments below
            //holder.imgStatus.setVisibility(View.GONE);

        }
        AVFile avatar = user.getAVFile("avatar");
        if (avatar != null) {
            Glide.with(mContext).load(avatar.getUrl()).crossFade().into(holder.imgAvatar);
        }
        holder.msgStatus.setText(status.getString("msg"));
        holder.userName.setText(user.getString("username"));
        holder.likeNum.setText(status.getInt("likeNum") + "");
        holder.commentNum.setText(status.getInt("commentNum") + "");

        holder.itemView.setOnClickListener((v)->{
            EventBus.getDefault().postSticky(new StatusEvent(status,user));
            mContext.startActivity(new Intent(activity, StatusDetailActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity,holder.imgStatus,"status_img_card_to_detail")
                    .toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return statusList == null ? 0 : statusList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgStatus;
        ImageView imgAvatar;
        TextView msgStatus;
        TextView userName;
        TextView likeNum;
        TextView commentNum;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgStatus = (ImageView) itemView.findViewById(R.id.img_status_card);
            imgAvatar = (ImageView) itemView.findViewById(R.id.status_list_avater);
            msgStatus = (TextView) itemView.findViewById(R.id.txt_msg_status_card);
            userName = (TextView) itemView.findViewById(R.id.txt_username_status_card);
            likeNum = (TextView) itemView.findViewById(R.id.ststus_like_num);
            commentNum = (TextView) itemView.findViewById(R.id.ststus_conmment_num);
        }
    }
}
