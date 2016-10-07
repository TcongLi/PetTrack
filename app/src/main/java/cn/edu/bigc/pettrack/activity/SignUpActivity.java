package cn.edu.bigc.pettrack.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

import cn.edu.bigc.pettrack.R;

public class SignUpActivity extends AppCompatActivity {
    EditText userName;
    EditText userEmail;
    EditText userPsw;
    EditText userPswAgain;
    TextView linkLogin;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        findview();
        setListener();
    }

    private void setListener() {
        linkLogin.setOnClickListener((v) -> finish());
        signUp.setOnClickListener((v) -> signUp());
    }

    private void findview() {
        userName = (EditText) findViewById(R.id.user_name);
        userEmail = (EditText) findViewById(R.id.user_email);
        userPsw = (EditText) findViewById(R.id.user_psw);
        userPswAgain = (EditText) findViewById(R.id.user_psw_again);
        linkLogin = (TextView) findViewById(R.id.link_login);
        signUp = (Button) findViewById(R.id.btn_signup);
    }

    void signUp() {
        if (!validate()) {
            return;
        }
        AVUser user = new AVUser();
        user.setUsername(userName.getText().toString());
        user.setEmail(userEmail.getText().toString());
        user.setPassword(userPsw.getText().toString());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Snackbar.make(signUp, "注册成功,验证信息已发送至邮箱", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> finish(), 2000);
                } else {
                    switch (e.getCode()) {
                        case 202:
                            Snackbar.make(signUp, "用户名已存在", Snackbar.LENGTH_SHORT)
                                    .show();
                            break;
                        case 203:
                            Snackbar.make(signUp, "该邮箱已注册", Snackbar.LENGTH_SHORT)
                                    .show();
                            break;
                        default:
                            Snackbar.make(signUp, "网络错误,请检查网络", Snackbar.LENGTH_SHORT)
                                    .show();
                            break;
                    }
                }
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPsw.getText().toString();
        String reEnterPassword = userPswAgain.getText().toString();

        if (name.isEmpty() || name.length() > 10) {
            userName.setError("用户名不能为空且不能超过10个字符");
            valid = false;
        } else {
            userName.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("请输入正确的邮箱格式");
            valid = false;
        } else {
            userEmail.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            userPsw.setError("请输入4~10位字母或数字");
            valid = false;
        } else {
            userPsw.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            userPswAgain.setError("输入的密码不一致");
            valid = false;
        } else {
            userPswAgain.setError(null);
        }

        return valid;
    }
}
