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
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.edu.bigc.pettrack.Event.RefreshEvent;
import cn.edu.bigc.pettrack.Event.StatusEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.CommentUtils;
import cn.edu.bigc.pettrack.adapter.CommentAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusCommentFragment extends Fragment {
    List<AVObject> commentList;
    CommentAdapter adapter;
    RecyclerView recyclerView;
    AVObject status;
    EditText input;
    ImageView send;

    public StatusCommentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status_comment, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview_comment);
        input = (EditText) v.findViewById(R.id.comment_input);
        send = (ImageView) v.findViewById(R.id.comment_send);

        EventBus.getDefault().register(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        refresh();
        send.setOnClickListener((view) -> {
            String msg = input.getText().toString();
            if (msg.isEmpty()) {
                Snackbar.make(view, "评论不能为空", Snackbar.LENGTH_SHORT).show();
            } else {
                CommentUtils.sendComment(status, AVUser.getCurrentUser(),msg);
                Snackbar.make(view, "评论成功", Snackbar.LENGTH_SHORT).show();
                refresh();
            }
        });

        return v;
    }

    public void refresh(){
        CommentUtils.queryComment(status, new FindCallback() {
            @Override
            public void done(List list, AVException e) {
                if (list == null || list.size() == 0) {
                    return;
                }
                commentList = list;
                adapter = new CommentAdapter(list, getContext(), getActivity());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true)
    public void onEvent(StatusEvent e) {
        status = e.getStatus();
    }
}
