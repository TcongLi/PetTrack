package cn.edu.bigc.pettrack.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.edu.bigc.pettrack.Event.RefreshEvent;
import cn.edu.bigc.pettrack.Event.StatusEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.StatusUtils;
import cn.edu.bigc.pettrack.fragment.StatusCommentFragment;
import cn.edu.bigc.pettrack.fragment.StatusDatailFragment;

public class StatusDetailActivity extends AppCompatActivity {
    AVObject status;
    AVUser user;
    ImageView imgStatus;
    CollapsingToolbarLayout ct;
    TabLayout tab;
    ViewPager vp;
    VpAdapter vpAdapter;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener((v -> onBackPressed()));

        imgStatus = (ImageView) findViewById(R.id.status_detail_img);
        ct = (CollapsingToolbarLayout) findViewById(R.id.status_detail_ct);
        tab = (TabLayout) findViewById(R.id.status_detail_tab);
        vp = (ViewPager) findViewById(R.id.status_detail_vp);
        fab= (FloatingActionButton) findViewById(R.id.status_like_fab);

        //EventBus--------------------------
        EventBus.getDefault().register(this);
        //EventBus--------------------------
        tab.setupWithViewPager(vp);
        vpAdapter=new VpAdapter(getSupportFragmentManager());
        vpAdapter.addFrag(new StatusDatailFragment(),"详情");
        vpAdapter.addFrag(new StatusCommentFragment(),"评论");
        vp.setAdapter(vpAdapter);

        ct.setTitle(" ");
        String url = status.getString("imgURL");
        if (url == null || url.isEmpty()) {
            Glide.clear(imgStatus);
        } else {
            Glide.with(this).load(url).crossFade().fitCenter().into(imgStatus);
        }

        fab.setOnClickListener((view)->{
            StatusUtils.likeStatus(status);
            Snackbar.make(view,"点赞成功",Snackbar.LENGTH_SHORT).show();
        });

    }


    @Subscribe(sticky = true)
    public void onEvent(StatusEvent e) {
        status = e.getStatus();
        user = e.getUser();

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    class VpAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFrag(Fragment frag, String title) {
            fragments.add(frag);
            titles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
