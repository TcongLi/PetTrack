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

import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.vipul.hp_hp.timelineview.TimelineView;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.edu.bigc.pettrack.Event.RecordEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.activity.RecordDetailActivity;

/**
 * Created by L T on 2016/10/3.
 */

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.MyViewHolder> {
    List<AVObject> recordsList;
    Context mContext;
    Activity activity;

    public RecordsAdapter(List<AVObject> recordsList, Context context, Activity activity) {
        this.recordsList = recordsList;
        this.mContext = context;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        return new MyViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AVObject record = recordsList.get(position);

        String url = record.getString("imgURL");
        if (!(url == null || url.equals(""))) {
            Glide.with(mContext).load(url).crossFade().fitCenter().into(holder.imgRecord);
        } else {
            //Glide.clear(holder.imgRecord);
            Glide.with(mContext).load(R.drawable.icon).into(holder.imgRecord);
        }
        holder.msgRecord.setText(record.getString("msg"));

        holder.MMM__dd.setText(new SimpleDateFormat("MMM  dd", Locale.ENGLISH).format(record.getCreatedAt()));
        holder.yyyy.setText(new SimpleDateFormat("yyyy").format(record.getCreatedAt()));
        holder.itemView.setOnClickListener((v)->{
            EventBus.getDefault().postSticky(new RecordEvent(record));
            mContext.startActivity(new Intent(activity, RecordDetailActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity,holder.imgRecord,"record_to_detail").toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return recordsList == null ? 0 : recordsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TimelineView mTimelineView;
        ImageView imgRecord;
        TextView msgRecord;
        TextView MMM__dd;
        TextView yyyy;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.time_marker);
            mTimelineView.initLine(viewType);
            msgRecord = (TextView) itemView.findViewById(R.id.txt_msg_record_cord);
            imgRecord = (ImageView) itemView.findViewById(R.id.img_record_cord);
            MMM__dd = (TextView) itemView.findViewById(R.id.record_MMM__dd);
            yyyy = (TextView) itemView.findViewById(R.id.record_yyyy);
        }
    }
}
