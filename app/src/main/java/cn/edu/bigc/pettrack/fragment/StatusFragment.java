package cn.edu.bigc.pettrack.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.edu.bigc.pettrack.Event.RefreshEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.StatusUtils;
import cn.edu.bigc.pettrack.activity.StatusEditActivity;
import cn.edu.bigc.pettrack.adapter.StatusAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    StatusAdapter adapter;
    List<AVObject> statusList;

    public StatusFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_status, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_status);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fab = (FloatingActionButton) v.findViewById(R.id.status_fab);
        fab.setOnClickListener((view) -> getActivity().startActivity(new Intent(getActivity(), StatusEditActivity.class)));

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
        if (!e.isAdded()) {
            adapter.notifyDataSetChanged();
        } else {
            refresh();
            Log.i("status refresh", "onEvent: done");
        }
    }

    private void refresh() {
        StatusUtils.queryAllStatus(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list == null || list.size() == 0) {
                    return;
                }
                StatusFragment.this.statusList = list;
                adapter = new StatusAdapter(statusList, getContext(), getActivity());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        });
    }

}
