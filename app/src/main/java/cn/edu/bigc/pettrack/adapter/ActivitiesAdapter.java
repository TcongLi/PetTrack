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

import cn.edu.bigc.pettrack.Event.ActivitiesEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.activity.ActivitiesDetailActivity;

/**
 * Created by L T on 2016/10/3.
 */

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> {
    List<AVObject> activities;
    Context mContext;
    Activity activity;

    public ActivitiesAdapter(List<AVObject> activities, Context mContext, Activity activity) {
        this.activities = activities;
        this.mContext = mContext;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activities_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AVObject act = activities.get(position);
        AVUser user = act.getAVUser("user");

        AVFile fileAvatar = user.getAVFile("avatar");
        if (fileAvatar != null) {
            Glide.with(mContext).load(fileAvatar.getUrl()).crossFade().into(holder.avatar);
        } else {
            Glide.clear(holder.avatar);
        }

        String imgURL = act.getString("imgURL");
        if (imgURL == null || imgURL.isEmpty()) {
            Glide.clear(holder.img);
        } else {
            Glide.with(mContext).load(imgURL).crossFade().into(holder.img);
        }

        holder.info.setText(act.getString("info"));
        holder.userName.setText(user.getUsername());
        holder.peopleNum.setText(act.getInt("people")+"");
        holder.itemView.setOnClickListener(view -> {
            EventBus.getDefault().postSticky(new ActivitiesEvent(act,user));
            mContext.startActivity(new Intent(activity, ActivitiesDetailActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity,holder.img,"activities_card_to_detail").toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return activities == null ? 0 : activities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView info;
        ImageView avatar;
        TextView userName;
        TextView peopleNum;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.activities_img);
            info = (TextView) itemView.findViewById(R.id.activities_info);
            avatar = (ImageView) itemView.findViewById(R.id.activities_list_avater);
            userName = (TextView) itemView.findViewById(R.id.txt_username_activities_card);
            peopleNum = (TextView) itemView.findViewById(R.id.activities_people_num);
        }
    }
}
