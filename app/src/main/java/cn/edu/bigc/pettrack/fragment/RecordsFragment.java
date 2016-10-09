package cn.edu.bigc.pettrack.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.bigc.pettrack.Event.RefreshEvent;
import cn.edu.bigc.pettrack.PtApplication;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.RecordsUtils;
import cn.edu.bigc.pettrack.Utils.UserUtils;
import cn.edu.bigc.pettrack.activity.MyHomePageActivity;
import cn.edu.bigc.pettrack.activity.RecordDetailActivity;
import cn.edu.bigc.pettrack.activity.RecordEditActivity;
import cn.edu.bigc.pettrack.adapter.RecordsAdapter;

import static android.app.Activity.RESULT_OK;
import static org.greenrobot.eventbus.ThreadMode.MAIN;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordsFragment extends Fragment {
    List<AVObject> recordList;
    RecyclerView recyclerView;
    RecordsAdapter adapter;
    FloatingActionButton fab;
    ImageView imgBg;

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
        imgBg = (ImageView) v.findViewById(R.id.pet_bg);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        fab.setOnClickListener((view) -> startActivity(new Intent(getActivity(), RecordEditActivity.class)));
        EventBus.getDefault().register(this);

        imgBg.setOnClickListener((view) -> {
            new AlertDialog.Builder(getActivity())
                    .setTitle("要更换宠物照片墙吗?")
                    .setPositiveButton("是", (dialogInterface, i) -> {
                        Intent intent = new Intent();
                        /* 开启Pictures画面Type设定为image */
                        intent.setType("image/*");
                        /* 使用Intent.ACTION_GET_CONTENT这个Action */
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        /* 取得相片后返回本画面 */
                        startActivityForResult(intent, Activity.RESULT_FIRST_USER);
                    })
                    .setNegativeButton("否", null)
                    .create()
                    .show();
        });

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

    public void setPetBg(){
        AVFile file=AVUser.getCurrentUser().getAVFile("petBg");
        if(file!=null){
            Glide.with(this).load(file.getUrl()).crossFade().centerCrop().into(imgBg);
        }else{
            Glide.clear(imgBg);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                AVFile file = new AVFile(uri.getLastPathSegment() + "", UserUtils.readBytes(uri, PtApplication.getContext().getContentResolver()));
                AVUser avUser = AVUser.getCurrentUser();
                avUser.put("petBg", file);
                avUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        setPetBg();
                    }
                });

            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
