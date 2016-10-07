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
import android.widget.Button;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.edu.bigc.pettrack.Event.ActivitiesEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.ActivitiesUtils;
import cn.edu.bigc.pettrack.fragment.ActivitiesDetailFragment;
import cn.edu.bigc.pettrack.fragment.ActivitiesPeopleFragment;

public class ActivitiesDetailActivity extends AppCompatActivity {
    ViewPager vp;
    TabLayout tab;
    PagerAdapter pagerAdapter;
    AVObject act;
    AVUser user;
    ImageView img;
    Button btnSubscribe;
    boolean isSubscribed;
    CollapsingToolbarLayout ct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener((v -> onBackPressed()));

        EventBus.getDefault().register(this);

        tab= (TabLayout) findViewById(R.id.activities_detail_tab);
        vp= (ViewPager) findViewById(R.id.activities_detail_vp);
        img= (ImageView) findViewById(R.id.activities_detail_img);
        btnSubscribe= (Button) findViewById(R.id.btn_subscribe);
        ct= (CollapsingToolbarLayout) findViewById(R.id.activities_detail_ct);

        ct.setTitle(" ");

        String imgURL=act.getString("imgURL");
        if(imgURL==null||imgURL.isEmpty()){
            Glide.clear(img);
        }else{
            Glide.with(this).load(imgURL).crossFade().fitCenter().into(img);
        }

        pagerAdapter=new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFrag(new ActivitiesDetailFragment(),"详情");
        pagerAdapter.addFrag(new ActivitiesPeopleFragment(),"已报名");
        vp.setAdapter(pagerAdapter);
        tab.setupWithViewPager(vp);

        queryIsSubscribed();
        if(isSubscribed){
            btnSubscribe.setText("取消报名");
        }


        btnSubscribe.setOnClickListener((v)->{
            queryIsSubscribed();
            if(!isSubscribed){
                ActivitiesUtils.subscribe(act);
                Snackbar.make(v,"报名成功",Snackbar.LENGTH_SHORT).show();
                btnSubscribe.setText("取消报名");
            }else {
                btnSubscribe.setText("取消报名");
            }
        });

    }

    public void queryIsSubscribed(){
        ActivitiesUtils.queryPeople(act, new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if(list==null||list.size()==0){
                    return;
                }
                for(Object o:list){
                    if(o.equals(AVUser.getCurrentUser())){
                        isSubscribed=true;
                    }
                }
            }

        });
    }

    class PagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onEvent(ActivitiesEvent e){
        act=e.getActivites();
        user=e.getUser();
    }
}
