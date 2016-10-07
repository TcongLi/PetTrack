package cn.edu.bigc.pettrack.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import org.greenrobot.eventbus.Subscribe;

import cn.edu.bigc.pettrack.Event.StatusEvent;
import cn.edu.bigc.pettrack.Event.UserEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.activity.MyHomePageActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusDatailFragment extends Fragment {
    AVObject status;
    AVUser user;
    ImageView avatar;
    TextView userName;
    TextView msg;
    public StatusDatailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_status_datail, container, false);
        avatar= (ImageView) v.findViewById(R.id.person_list_avater);
        userName= (TextView) v.findViewById(R.id.person_list_username);
        msg= (TextView) v.findViewById(R.id.status_detail_msg);

        EventBus.getDefault().register(this);

        AVFile file=user.getAVFile("avatar");
        if(file!=null){
            Glide.with(getContext()).load(file.getUrl()).into(avatar);
        }
        userName.setText(user.getUsername());
        userName.setOnClickListener((view)->{
            EventBus.getDefault().postSticky(new UserEvent(user));
            startActivity(new Intent(getActivity(), MyHomePageActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), avatar, "homepage")
                            .toBundle());
        });

        msg.setText(status.getString("msg"));

        return v;
    }
    @Subscribe(sticky = true)
    public void onEvent(StatusEvent e){
        status=e.getStatus();
        user=e.getUser();
    }

}
