package cn.edu.bigc.pettrack.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.edu.bigc.pettrack.Event.PersonTypeEvent;
import cn.edu.bigc.pettrack.Event.UserEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.UserUtils;
import cn.edu.bigc.pettrack.adapter.PersonlistAdapter;

public class PersonListActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public PersonlistAdapter personlistAdapter;
    public List<AVUser> userlist;
    public Context context = this;
    public int personType;
    public AVUser currentUser;
    Activity activity=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener((v -> onBackPressed()));

        EventBus.getDefault().register(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_personlist);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FindCallback<AVUser> callback = new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                userlist = new ArrayList<>(list);
                personlistAdapter = new PersonlistAdapter(userlist, context,activity);
                recyclerView.setAdapter(personlistAdapter);
                personlistAdapter.notifyDataSetChanged();
            }
        };

        if (personType == PersonTypeEvent.FOLLOWER) {
            UserUtils.getUserList(currentUser, UserUtils.FOLLOWER, callback);
        } else {
            UserUtils.getUserList(currentUser, UserUtils.FOLLOWEE, callback);
        }
    }

    @Subscribe(sticky = true)
    public void onEvent(UserEvent ue) {
        currentUser = ue.getUser();
        Log.i("PersonList:currentUser", "onEvent: " + currentUser.getUsername());
    }

    @Subscribe(sticky = true)
    public void onEvent(PersonTypeEvent pt) {
        personType = pt.getPersonType();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
