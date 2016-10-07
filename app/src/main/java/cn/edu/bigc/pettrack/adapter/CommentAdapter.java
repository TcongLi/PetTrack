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
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.edu.bigc.pettrack.Event.UserEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.activity.MyHomePageActivity;

/**
 * Created by L T on 2016/10/5.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    List<AVObject> comments;
    Context mContext;
    Activity activity;

    public CommentAdapter(List<AVObject> comments, Context mContext, Activity activity) {
        this.comments = comments;
        this.mContext = mContext;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AVObject comment=comments.get(position);
        AVFile avatar = comment.getAVUser("user").getAVFile("avatar");
        if (avatar != null) {
            Glide.with(mContext).load(avatar.getUrl()).crossFade().into(holder.imgAvatar);
        }
        holder.userName.setText(comment.getAVUser("user").getUsername());
        holder.msgComment.setText(comment.getString("msg"));
        holder.userName.setOnClickListener((view)->{
            EventBus.getDefault().postSticky(new UserEvent(comment.getAVUser("user")));
            mContext.startActivity(new Intent(activity, MyHomePageActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity,holder.imgAvatar, "homepage")
                            .toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return comments==null?0:comments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView  userName;
        TextView  msgComment;
        public MyViewHolder(View itemView) {
            super(itemView);
            imgAvatar= (ImageView) itemView.findViewById(R.id.comment_avatar);
            userName= (TextView) itemView.findViewById(R.id.comment_username);
            msgComment= (TextView) itemView.findViewById(R.id.comment_msg);
        }
    }
}
