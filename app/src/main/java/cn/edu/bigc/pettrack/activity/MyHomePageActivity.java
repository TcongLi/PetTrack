package cn.edu.bigc.pettrack.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cn.edu.bigc.pettrack.Event.PersonTypeEvent;
import cn.edu.bigc.pettrack.Event.UserEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.StatusUtils;
import cn.edu.bigc.pettrack.Utils.UserUtils;
import cn.edu.bigc.pettrack.adapter.StatusAdapter;

public class MyHomePageActivity extends AppCompatActivity {
    CollapsingToolbarLayout ct;
    Context mContext = this;
    Activity activity = this;
    AVUser avUser;
    TextView txtFollower;
    TextView txtFollowee;
    ImageView imgAvatar;
    ImageView imgBg;
    Button btnFollow;
    RecyclerView recyclerView;
    StatusAdapter adapter;
    List<AVObject> statusList;
    boolean isMine;
    boolean isMyFollowee;
    boolean setAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener((v -> onBackPressed()));

        ct = (CollapsingToolbarLayout) findViewById(R.id.my_homepage_detail_ct);
        txtFollowee = (TextView) findViewById(R.id.num_followee);
        txtFollower = (TextView) findViewById(R.id.num_follower);
        imgAvatar = (ImageView) findViewById(R.id.img_avater);
        imgBg = (ImageView) findViewById(R.id.home_bg);
        btnFollow = (Button) findViewById(R.id.btn_follow);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_homepage);
        //CAUTION! The location of register of eventbus,or you'll get a NULLPOINTER exception.
        EventBus.getDefault().register(this);
        //
        if (avUser != null) {
            ct.setTitle(avUser.getUsername());
        }

        setNum();
        setAavtar();
        setHomeBg();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        StatusUtils.queryStatus(avUser, new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list == null || list.size() == 0) {
                    return;
                }
                statusList = list;
                adapter = new StatusAdapter(statusList, mContext, activity);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        txtFollower.setOnClickListener((v) -> {
            EventBus.getDefault().postSticky(new PersonTypeEvent(PersonTypeEvent.FOLLOWER));
            EventBus.getDefault().postSticky(new UserEvent(avUser));
            startActivity(new Intent(MyHomePageActivity.this, PersonListActivity.class));
        });
        txtFollowee.setOnClickListener((v) -> {
            EventBus.getDefault().postSticky(new PersonTypeEvent(PersonTypeEvent.FOLLOWEE));
            EventBus.getDefault().postSticky(new UserEvent(avUser));
            startActivity(new Intent(MyHomePageActivity.this, PersonListActivity.class));
        });
        if (avUser.equals(AVUser.getCurrentUser())) {
            imgAvatar.setOnClickListener((v) ->{
                showDialog("要更换头像吗");
                setAvatar=true;
            });

            imgBg.setOnClickListener((v)->{
                showDialog("要更换照片墙吗");
                setAvatar=false;
            });
            btnFollow.setVisibility(View.GONE);
        }
        UserUtils.getUserList(AVUser.getCurrentUser(), UserUtils.FOLLOWEE, new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                for (AVUser u : list) {
                    if (u.getObjectId().equals(avUser.getObjectId())) {
                        isMyFollowee = true;
                        btnFollow.setText("取消关注");
                        return;
                    }
                }
            }
        });
        btnFollow.setOnClickListener((view) -> {
            if (isMyFollowee) {
                UserUtils.unfollow(avUser, new FollowCallback() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        Snackbar.make(view, "取消关注成功", Snackbar.LENGTH_SHORT).show();
                        btnFollow.setText("关注");
                        isMyFollowee = false;
                    }
                });
            } else {
                UserUtils.follow(avUser, new FollowCallback() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        Snackbar.make(view, "关注成功", Snackbar.LENGTH_SHORT).show();
                        btnFollow.setText("取消关注");
                        isMyFollowee = true;
                    }
                });
            }

        });
    }

    private void setAavtar() {
        if (avUser == null) {
            return;
        }
        AVFile file = avUser.getAVFile("avatar");
        if (file != null) {
            Glide.with(this).load(file.getUrl()).crossFade().into(imgAvatar);
        } else {
            Glide.with(this).load(R.drawable.icon).crossFade().into(imgAvatar);
        }
    }
    private void setHomeBg(){
        if (avUser == null) {
            return;
        }
        AVFile file = avUser.getAVFile("homeBg");
        if (file != null) {
            Glide.with(this).load(file.getUrl()).crossFade().centerCrop().into(imgBg);
        } else {
            Glide.clear(imgBg);
        }
    }
    private void setNum() {
        if (avUser == null) {
            return;
        }
        UserUtils.getUserList(avUser, UserUtils.FOLLOWER, new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                txtFollower.setText(list.size() + "");
            }
        });
        UserUtils.getUserList(avUser, UserUtils.FOLLOWEE, new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                txtFollowee.setText(list.size() + "");
            }
        });
    }

    public void showDialog(String title) {
        new AlertDialog.Builder(MyHomePageActivity.this)
                .setTitle(title)
                .setPositiveButton("是", (dialogInterface, i) -> {
                    Intent intent = new Intent();
                        /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*");
                        /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                        /* 取得相片后返回本画面 */
                    startActivityForResult(intent, 1);
                })
                .setNegativeButton("否", null)
                .create()
                .show();
    }

    @Subscribe(sticky = true)
    public void onEvent(UserEvent ue) {
        avUser = ue.getUser();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                AVFile file = new AVFile(uri.getLastPathSegment() + "", UserUtils.readBytes(uri, getContentResolver()));
                if(setAvatar){
                    avUser.put("avatar", file);
                    avUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            setAavtar();
                        }
                    });

                }else {
                    avUser.put("homeBg", file);
                    avUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            setHomeBg();
                        }
                    });

                }
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(), e);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
