package cn.edu.bigc.pettrack.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cn.edu.bigc.pettrack.Event.ActivitiesEvent;
import cn.edu.bigc.pettrack.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesDetailFragment extends Fragment {
    AVObject act;
    AVUser user;
    TextView txtLocation;
    TextView txtTime;
    TextView txtDate;
    TextView txtInfo;
    TextView txtMsg;
    ImageView avatar;
    TextView userName;

    public ActivitiesDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_activities_detail, container, false);
        txtLocation= (TextView) v.findViewById(R.id.txt_activities_location);
        txtTime= (TextView) v.findViewById(R.id.txt_activities_time);
        txtDate= (TextView) v.findViewById(R.id.txt_activities_date);
        txtInfo= (TextView) v.findViewById(R.id.txt_activities_info);
        txtMsg= (TextView) v.findViewById(R.id.txt_activities_msg);
        avatar= (ImageView) v.findViewById(R.id.person_list_avater);
        userName= (TextView) v.findViewById(R.id.person_list_username);

        EventBus.getDefault().register(this);

        AVFile file=user.getAVFile("avatar");
        if(file!=null){
            Glide.with(this).load(file.getUrl()).crossFade().into(avatar);
        }
        userName.setText(user.getUsername());

        txtLocation.setText(act.getString("location"));
        txtTime.setText(act.getString("time"));
        txtDate.setText(act.getString("date"));
        txtInfo.setText(act.getString("info"));
        txtMsg.setText(act.getString("msg"));

        return v;
    }

    @Subscribe(sticky = true)
    public void onEvent(ActivitiesEvent e){
        act=e.getActivites();
        user=e.getUser();
    }
}
