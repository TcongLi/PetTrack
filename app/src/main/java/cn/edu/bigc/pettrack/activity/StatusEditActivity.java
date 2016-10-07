package cn.edu.bigc.pettrack.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.io.IOException;

import cn.edu.bigc.pettrack.Event.RefreshEvent;
import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.StatusUtils;
import cn.edu.bigc.pettrack.Utils.UserUtils;

public class StatusEditActivity extends AppCompatActivity {
    EditText input;
    FloatingActionButton fab;
    CollapsingToolbarLayout ct;
    ImageView img;
    boolean havaImg;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener((v -> onBackPressed()));

        input = (EditText) findViewById(R.id.status_edit_input);
        fab = (FloatingActionButton) findViewById(R.id.status_add_img_fab);
        ct = (CollapsingToolbarLayout) findViewById(R.id.status_edit_ct);
        img = (ImageView) findViewById(R.id.status_edit_img);

        ct.setTitle(" ");

        fab.setOnClickListener((view) -> {
            Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
            intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
            intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
            startActivityForResult(intent, 1);
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_status_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_status_send) {
            String msg = input.getText().toString();
            if (msg.isEmpty()) {
                Snackbar.make(input, "内容不能为空", Snackbar.LENGTH_SHORT).show();
            } else {
                if (havaImg) {
                    try {
                        AVFile file = new AVFile(uri.getLastPathSegment() + "", UserUtils.readBytes(uri, getContentResolver()));
                        file.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if(e==null){
                                    StatusUtils.sendStatus(msg,file.getUrl());
                                }
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    StatusUtils.sendStatus(msg,null);
                }
                Snackbar.make(input, "发送成功", Snackbar.LENGTH_SHORT).show();

                new Handler().postDelayed(()->finish(),2000);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            if(uri!=null){
                Glide.with(this).load(uri).crossFade().into(img);
                havaImg=true;
            }
        }else {
            havaImg=false;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
