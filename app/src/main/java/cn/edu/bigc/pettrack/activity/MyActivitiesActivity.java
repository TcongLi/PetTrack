package cn.edu.bigc.pettrack.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.ArrayList;
import java.util.List;

import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.ActivitiesUtils;
import cn.edu.bigc.pettrack.adapter.ActivitiesAdapter;

public class MyActivitiesActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ActivitiesAdapter adapter;
    List<AVObject> activities;
    List<AVObject> activitiesList;
    Context context=this;
    Activity activity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activities);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener((v -> onBackPressed()));

        recyclerView= (RecyclerView) findViewById(R.id.recyclerview_my_activities);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ActivitiesUtils.queryActivities(AVUser.getCurrentUser(), new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if(list==null||list.size()==0){
                    Snackbar.make(recyclerView,"你还没有发布或报名任何活动哦",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                activities=list;
                activitiesList=new ArrayList<AVObject>();
                for(AVObject o:activities){
                    activitiesList.add(o.getAVObject("activity"));
                }
                adapter=new ActivitiesAdapter(activitiesList,context,activity);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        });

    }

}
