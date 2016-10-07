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
                    Snackbar.make(signUp, "ע��ɹ�,��֤��Ϣ�ѷ���������", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> finish(), 2000);
                } else {
                    switch (e.getCode()) {
                        case 202:
                            Snackbar.make(signUp, "�û����Ѵ���", Snackbar.LENGTH_SHORT)
                                    .show();
                            break;
                        case 203:
                            Snackbar.make(signUp, "��������ע��", Snackbar.LENGTH_SHORT)
                                    .show();
                            break;
                        default:
                            Snackbar.make(signUp, "�������,��������", Snackbar.LENGTH_SHORT)
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
            userName.setError("�û�������Ϊ���Ҳ��ܳ���10���ַ�");
            valid = false;
        } else {
            userName.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("��������ȷ�������ʽ");
            valid = false;
        } else {
            userEmail.setError(null);
        }


        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            userPsw.setError("������4~10λ��ĸ������");
            valid = false;
        } else {
            userPsw.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            userPswAgain.setError("��������벻һ��");
            valid = false;
        } else {
            userPswAgain.setError(null);
        }

        return valid;
    }
}
