package cn.edu.bigc.pettrack.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.UserUtils;
import cn.edu.bigc.pettrack.adapter.PersonlistAdapter;

public class FindPersonActivity extends AppCompatActivity {
    EditText txtUserName;
    Button btnSearch;
    List<AVUser> users;
    Context mContext = this;
    RecyclerView recyclerView;
    PersonlistAdapter adapter;
    Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener((v -> onBackPressed()));

        txtUserName = (EditText) findViewById(R.id.user_name);
        btnSearch = (Button) findViewById(R.id.btn_search);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_find_user_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        btnSearch.setOnClickListener((v) -> {
            String nameToFind = txtUserName.getText().toString();
            if (nameToFind.isEmpty()) {
                Log.i("username", "onCreate: 用户名空");
                Snackbar.make(v, "用户名不能为空", Snackbar.LENGTH_SHORT).show();
            } else {
                UserUtils.findUser(nameToFind, new FindCallback() {
                    @Override
                    public void done(List list, AVException e) {
                        if (list == null || list.size() == 0) {
                            Snackbar.make(v, "未找到该用户", Snackbar.LENGTH_SHORT).show();
                        } else {
                            users = list;
                            adapter = new PersonlistAdapter(users, mContext, activity);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        });
    }
}
