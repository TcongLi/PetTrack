package cn.edu.bigc.pettrack.fragment;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.edu.bigc.pettrack.Event.ActivitiesEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.ActivitiesUtils;
import cn.edu.bigc.pettrack.adapter.PersonlistAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesPeopleFragment extends Fragment {
    RecyclerView recyclerView;
    AVObject act;
    PersonlistAdapter adapter;
    List<AVObject> users;
    List<AVUser> usersList;
    public ActivitiesPeopleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activities_people, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_activities_people);

        EventBus.getDefault().register(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        findPeople();
        return v;
    }

    public void findPeople(){
        ActivitiesUtils.queryPeople(act, new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if(list==null||list.size()==0){
                    Snackbar.make(recyclerView,"还没有人报名哦,快来报名吧",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                users=list;
                usersList=new ArrayList<AVUser>();
                for(AVObject o:users){
                    usersList.add(o.getAVUser("user"));
                }
                adapter=new PersonlistAdapter(usersList,getContext(),getActivity());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe(sticky = true)
    public void onEvent(ActivitiesEvent e) {
        act = e.getActivites();
    }

}
