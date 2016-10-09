package cn.edu.bigc.pettrack.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Locale;

import cn.edu.bigc.pettrack.Event.RecordEvent;
import cn.edu.bigc.pettrack.R;

public class RecordDetailActivity extends AppCompatActivity {
    AVObject record;
    ImageView img;
    TextView msg;
    String date;
    CollapsingToolbarLayout ct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener((v -> onBackPressed()));

        img= (ImageView) findViewById(R.id.record_detail_img);
        msg= (TextView) findViewById(R.id.record_detail_msg);
        ct= (CollapsingToolbarLayout) findViewById(R.id.record_detail_ct);

        EventBus.getDefault().register(this);

        ct.setTitle(new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH).format(record.getCreatedAt()));

        String url=record.getString("imgURL");
        if(url==null){
            Glide.clear(img);
        }else {
            Glide.with(this).load(url).crossFade().centerCrop().into(img);
        }

        msg.setText(record.getString("msg"));

    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(sticky = true)
    public void onEvent(RecordEvent e) {
        record = e.getRecord();
    }

}
