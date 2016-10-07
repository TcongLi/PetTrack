package cn.edu.bigc.pettrack.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.bigc.pettrack.Event.RefreshEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.RecordsUtils;
import cn.edu.bigc.pettrack.activity.RecordDetailActivity;
import cn.edu.bigc.pettrack.activity.RecordEditActivity;
import cn.edu.bigc.pettrack.adapter.RecordsAdapter;

import static org.greenrobot.eventbus.ThreadMode.MAIN;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordsFragment extends Fragment {
    List<AVObject> recordList;
    RecyclerView recyclerView;
    RecordsAdapter adapter;
    FloatingActionButton fab;

    public RecordsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_records, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_records);
        fab = (FloatingActionButton) v.findViewById(R.id.record_fab);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fab.setOnClickListener((view) -> startActivity(new Intent(getActivity(), RecordEditActivity.class)));
        EventBus.getDefault().register(this);
        refresh();

        return v;


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RefreshEvent e) {
        if (e.isAdded()) {
            refresh();
            Log.i("record refresh", "onEvent: done");
        }
    }

    public void refresh() {

        RecordsUtils.queryRecord(AVUser.getCurrentUser(), new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                recordList = list;
                if (list == null || list.size() == 0) {
                    return;
                }
                adapter = new RecordsAdapter(recordList, getContext(), getActivity());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
