package cn.edu.bigc.pettrack.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import cn.edu.bigc.pettrack.Event.PersonTypeEvent;
import cn.edu.bigc.pettrack.Event.UserEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.activity.FindPersonActivity;
import cn.edu.bigc.pettrack.activity.LoginActivity;
import cn.edu.bigc.pettrack.activity.MainActivity;
import cn.edu.bigc.pettrack.activity.MyActivitiesActivity;
import cn.edu.bigc.pettrack.activity.MyHomePageActivity;
import cn.edu.bigc.pettrack.activity.PersonListActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {
    Button btnMine;
    Button btnFollower;
    Button btnFollowee;
    Button btnFindUser;
    Button btnLogout;
    Button btnMyActivity;
    ImageView imgMyAvatar;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mine, container, false);

        btnMine = (Button) v.findViewById(R.id.btn_my_homepage);
        btnFollowee = (Button) v.findViewById(R.id.btn_my_followee);
        btnFollower = (Button) v.findViewById(R.id.btn_my_follower);
        imgMyAvatar = (ImageView) v.findViewById(R.id.img_my_avater);
        btnFindUser = (Button) v.findViewById(R.id.btn_find_user);
        btnLogout = (Button) v.findViewById(R.id.btn_logout);
        btnMyActivity = (Button) v.findViewById(R.id.btn_my_activity);

        btnLogout.setOnClickListener((view) -> {
            AVUser.logOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        });
        btnMine.setOnClickListener((view) -> {
            EventBus.getDefault().postSticky(new UserEvent(AVUser.getCurrentUser()));
            startActivity(new Intent(getActivity(), MyHomePageActivity.class),
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            imgMyAvatar, "homepage").toBundle());
        });
        btnFollowee.setOnClickListener((view) -> {
            EventBus.getDefault().postSticky(new PersonTypeEvent(PersonTypeEvent.FOLLOWEE));
            EventBus.getDefault().postSticky(new UserEvent(AVUser.getCurrentUser()));
            startActivity(new Intent(getActivity(), PersonListActivity.class));
        });
        btnFollower.setOnClickListener((view) -> {
            EventBus.getDefault().postSticky(new PersonTypeEvent(PersonTypeEvent.FOLLOWER));
            EventBus.getDefault().postSticky(new UserEvent(AVUser.getCurrentUser()));
            startActivity(new Intent(getActivity(), PersonListActivity.class));
        });
        btnFindUser.setOnClickListener((view) -> {
            startActivity(new Intent(getActivity(), FindPersonActivity.class));
        });

        btnMyActivity.setOnClickListener((view) -> {
            startActivity(new Intent(getActivity(), MyActivitiesActivity.class));
        });

        setAvatar();

        return v;
    }

    private void setAvatar() {
        AVFile file = AVUser.getCurrentUser().getAVFile("avatar");
        if (file != null) {
            Glide.with(getActivity()).load(file.getUrl()).crossFade().into(imgMyAvatar);
        }
        btnMine.setText(AVUser.getCurrentUser().getUsername()+"\nÎÒµÄÖ÷Ò³");
    }

}
