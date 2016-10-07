package cn.edu.bigc.pettrack.fragment;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerClickListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cn.edu.bigc.pettrack.Event.ActivitiesEvent;
import cn.edu.bigc.pettrack.Event.RefreshEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.ActivitiesUtils;
import cn.edu.bigc.pettrack.activity.ActivitiesDetailActivity;
import cn.edu.bigc.pettrack.activity.ActivitiesPublishActivity;
import cn.edu.bigc.pettrack.adapter.ActivitiesAdapter;

import static org.greenrobot.eventbus.ThreadMode.MAIN;

/**
 * A placeholder fragment containing a simple view.
 */
public class ActivitiesFragment extends Fragment {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    Banner banner;
    ActivitiesAdapter adapter;
    List<AVObject> activites;

    List<AVObject> bannerActivities;

    public ActivitiesFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activity, container, false);

        fab = (FloatingActionButton) v.findViewById(R.id.activities_fab);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_activities);
        banner = (Banner) v.findViewById(R.id.banner);

        EventBus.getDefault().register(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fab.setOnClickListener((view) -> startActivity(new Intent(getActivity(), ActivitiesPublishActivity.class)));


        refresh();
        return v;
    }

    private void refresh() {
        ActivitiesUtils.queryAllActivities(new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (list == null || list.size() == 0) {
                    return;
                }
                activites = list;
                adapter = new ActivitiesAdapter(activites, getContext(), getActivity());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                int size = list.size();
                int bannerNum = size >= 5 ? 5 : size;

                bannerActivities = activites.subList(0, bannerNum);

                List<String> bannerImgURLs=new ArrayList<String>();
                List<String> bannerTitles=new ArrayList<String>();

                for(AVObject o:bannerActivities){
                    bannerImgURLs.add(o.getString("imgURL"));
                }
                for(AVObject o:bannerActivities){
                    bannerTitles.add(o.getString("info"));
                }

                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                        .setImages(bannerImgURLs)
                        .setImageLoader((c, p, i) -> Glide.with(c).load(p).into(i))
                        .setBannerAnimation(Transformer.DepthPage)
                        .setBannerTitles(bannerTitles)
                        .start();
                banner.setOnBannerClickListener((pos) -> {
                    EventBus.getDefault().postSticky(new ActivitiesEvent(bannerActivities.get(pos-2),bannerActivities.get(pos-2).getAVUser("user")));
                    startActivity(new Intent(getActivity(), ActivitiesDetailActivity.class),
                            ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),banner,"activities_card_to_detail").toBundle());
                });
            }

        });
    }

    @Subscribe(threadMode = MAIN)
    public void onEvent(RefreshEvent e) {
        refresh();
    }
}
