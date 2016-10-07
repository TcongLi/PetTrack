package cn.edu.bigc.pettrack.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.ActivitiesUtils;

public class ActivitiesPublishActivity extends AppCompatActivity {
    EditText editTime;
    EditText editDate;
    EditText editLocation;
    EditText editInfo;
    EditText editMsg;
    Button btnPublish;
    FloatingActionButton fab;
    ImageView img;
    Uri uri;
    boolean haveImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_publish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener((v -> onBackPressed()));
        //find view
        editTime = (EditText) findViewById(R.id.edit_time);
        editDate = (EditText) findViewById(R.id.edit_date);
        img = (ImageView) findViewById(R.id.activities_edit_img);
        editLocation = (EditText) findViewById(R.id.edit_location);
        editInfo = (EditText) findViewById(R.id.edit_info);
        editMsg = (EditText) findViewById(R.id.edit_msg);
        btnPublish = (Button) findViewById(R.id.activities_publish);
        fab = (FloatingActionButton) findViewById(R.id.activities_add_img_fab);

        //set listener
        editTime.setOnClickListener((v) ->
                new TimePickerDialog(this,
                        (tp, i, i1) -> editTime.setText(i + ":" + i1),
                        0,
                        0,
                        true).show()
        );
        editDate.setOnClickListener((v) ->
                new DatePickerDialog(this,
                        (dp, i, i1, i2) -> editDate.setText(i + "年" + i1 + "月" + i2 + "日"),
                        2016,
                        0,
                        0).show()
        );
        fab.setOnClickListener((view) -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        });
        btnPublish.setOnClickListener(v -> publish());
    }

    public void publish() {
        if (haveImg) {
            ActivitiesUtils.publish(AVUser.getCurrentUser(),
                    editLocation.getText().toString(),
                    editTime.getText().toString(),
                    editDate.getText().toString(),
                    editInfo.getText().toString(),
                    editMsg.getText().toString(),
                    uri);
        } else {
            ActivitiesUtils.publish(AVUser.getCurrentUser(),
                    editLocation.getText().toString(),
                    editTime.getText().toString(),
                    editDate.getText().toString(),
                    editInfo.getText().toString(),
                    editMsg.getText().toString(),
                    null);
        }
        Snackbar.make(btnPublish,"发布成功",Snackbar.LENGTH_SHORT).show();
        new Handler().postDelayed(()->finish(),2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            if (uri != null) {
                Glide.with(this).load(uri).crossFade().into(img);
                haveImg = true;
            }
        } else {
            haveImg = false;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
