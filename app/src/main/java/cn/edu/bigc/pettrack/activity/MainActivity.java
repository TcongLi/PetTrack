package cn.edu.bigc.pettrack.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.avos.avoscloud.AVUser;
import com.roughike.bottombar.BottomBar;

import cn.edu.bigc.pettrack.R;
import cn.edu.bigc.pettrack.Utils.RecordsUtils;
import cn.edu.bigc.pettrack.Utils.StatusUtils;
import cn.edu.bigc.pettrack.fragment.ActivitiesFragment;
import cn.edu.bigc.pettrack.fragment.StatusFragment;
import cn.edu.bigc.pettrack.fragment.MineFragment;
import cn.edu.bigc.pettrack.fragment.RecordsFragment;

public class MainActivity extends AppCompatActivity {
    BottomBar bottomBar;
    ActivitiesFragment activitiesFragment;
    StatusFragment statusFragment;
    RecordsFragment recordsFragment;
    MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.i("Username:Main", AVUser.getCurrentUser().getObjectId());

        //leancloud test

        //^leancloud test

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //toolbar.setNavigationOnClickListener((v -> onBackPressed()));

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        activitiesFragment = new ActivitiesFragment();
        statusFragment = new StatusFragment();
        recordsFragment = new RecordsFragment();
        mineFragment = new MineFragment();
        bottomBar.setOnTabSelectListener((tabId -> {
            switch (tabId) {
                case R.id.tab_activity:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_main, activitiesFragment)
                            .commit();
                    break;
                case R.id.tab_dynamics:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_main, statusFragment)
                            .commit();
                    break;
                case R.id.tab_records:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_main, recordsFragment)
                            .commit();
                    break;
                case R.id.tab_mine:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_main, mineFragment)
                            .commit();
                    break;
            }
        }));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            AVUser.logOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return true;
        }
        if(id==R.id.action_search){
            startActivity(new Intent(this,FindPersonActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        //EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    boolean exitFlg = false;

    @Override
    public void onBackPressed() {
        if (exitFlg) {
            finish();
        }
        exitFlg = true;
        new Handler().postDelayed(() -> exitFlg = false, 2000);
        Snackbar.make(bottomBar, "再按一次退出", Snackbar.LENGTH_SHORT).show();
    }
}
