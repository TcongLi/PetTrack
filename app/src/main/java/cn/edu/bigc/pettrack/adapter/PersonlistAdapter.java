package cn.edu.bigc.pettrack.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.edu.bigc.pettrack.Event.UserEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.activity.MyHomePageActivity;
import cn.edu.bigc.pettrack.activity.PersonListActivity;

/**
 * Created by L T on 2016/9/26.
 */

public class PersonlistAdapter extends RecyclerView.Adapter<PersonlistAdapter.TheViewHolder> {
    public List<AVUser> users;
    public Context context;
    //The variable activity is a necessary parameter of the shared elements transition.
    Activity activity;

    public PersonlistAdapter(List<AVUser> users, Context context, Activity activity) {
        this.users = users;
        this.context = context;
        this.activity = activity;
    }

    public class TheViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView username;

        public TheViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.person_list_avater);
            username = (TextView) itemView.findViewById(R.id.person_list_username);
        }
    }

    @Override
    public TheViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_item, parent, false);
        return new TheViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TheViewHolder holder, int position) {
        AVUser user = users.get(position);

        holder.username.setText(user.getUsername());
        AVFile file=user.getAVFile("avatar");
        if(file==null){
            Glide.clear(holder.img);
        }else {
            Glide.with(context).load(file.getUrl()).into(holder.img);
        }
        holder.itemView.setOnClickListener((v) -> {
            EventBus.getDefault().postSticky(new UserEvent(user));
            context.startActivity(new Intent(context, MyHomePageActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.img, "homepage")
                            .toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

}
