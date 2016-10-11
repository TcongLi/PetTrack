package cn.edu.bigc.pettrack.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.bumptech.glide.Glide;

import cn.edu.bigc.pettrack.PtApplication;
import cn.edu.bigc.pettrack.R;

public class LoginActivity extends AppCompatActivity {
    EditText acnt;
    EditText psw;
    TextView signUpLink;
    Button login;
    ImageView imgLaunch;
    ImageView icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        AVAnalytics.trackAppOpened(getIntent());
        findView();
        setListener();
        Glide.with(this).load(R.drawable.ic_launch).crossFade().into(imgLaunch);
        acnt.setVisibility(View.GONE);
        psw.setVisibility(View.GONE);
        signUpLink.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        icon.setVisibility(View.GONE);
        new Handler().postDelayed(()->{
            if(AVUser.getCurrentUser()!=null){
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }else{
                imgLaunch.setVisibility(View.GONE);
                acnt.setVisibility(View.VISIBLE);
                psw.setVisibility(View.VISIBLE);
                signUpLink.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
                icon.setVisibility(View.VISIBLE);
            }
        },2000);

    }

    private void findView() {
        acnt= (EditText) findViewById(R.id.intput_account);
        psw= (EditText) findViewById(R.id.intput_password);
        signUpLink= (TextView) findViewById(R.id.link_signup);
        login= (Button) findViewById(R.id.btn_login);
        imgLaunch= (ImageView) findViewById(R.id.img_launch);
        icon= (ImageView) findViewById(R.id.img_icon);
    }

    private void setListener() {
        signUpLink.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,SignUpActivity.class)));
        login.setOnClickListener((v)->login());
    }
    void login(){
        if(!validate()){
            return;
        }
        AVUser.logInInBackground(acnt.getText().toString(), psw.getText().toString(),
                new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if(avUser==null){
                            Snackbar.make(login,"登陆失败",Snackbar.LENGTH_SHORT).show();
                        }else{
                            /*if(!avUser.getBoolean("emailVerified")){
                                Snackbar.make(login,"该邮箱未验证",Snackbar.LENGTH_SHORT).show();
                                avUser.logOut();
                            }else {*/
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            //}
                        }
                    }
                });

    }

    public boolean validate() {
        boolean valid = true;

        String email = acnt.getText().toString();
        String password =psw.getText().toString();

        /*if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            acnt.setError("请输入您的邮箱地址");
            valid = false;
        } else {
            acnt.setError(null);
        }*/
        if(email.isEmpty()){
            acnt.setError("用户名不能为空");
            valid = false;
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            psw.setError("4~10个英文字母或数字");
            valid = false;
        } else {
            psw.setError(null);
        }

        return valid;
    }

}
