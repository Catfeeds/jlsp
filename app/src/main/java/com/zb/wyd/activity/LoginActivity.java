package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.LoginHandler;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：一句话简单描述
 */
public class LoginActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.ll_register)
    LinearLayout mRegisterLayout;
    @BindView(R.id.tv_recovery_pwd_phone)
    TextView tvRecoveryPwdPhone;

    private String account;
    private String pwd;
    private static final String USER_LOGIN = "user_login";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(LoginActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    ToastUtil.show(LoginActivity.this, "登录成功");
                    ConfigManager.instance().setUserName(account);
                    ConfigManager.instance().setUserPwd(pwd);
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(LoginActivity.this, msg.obj.toString());
                    break;

            }
        }
    };

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_login);
        StatusBarUtil.transparencyBar(LoginActivity.this);
        StatusBarUtil.StatusBarLightMode(LoginActivity.this, false);
    }

    @Override
    protected void initEvent()
    {

        btnLogin.setOnClickListener(this);
        mRegisterLayout.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvRecoveryPwdPhone.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        //        ImageLoader.getInstance().displayImage(ConfigManager.instance().getBgLogin(),
        // ivBg);
        //        tvEmail.setText("防丢邮箱,发邮件到" + ConfigManager.instance().getSystemEmail() +
        // "获取最新地址");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        etAccount.setText(ConfigManager.instance().getUserName());
        etPwd.setText(ConfigManager.instance().getUserPwd());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);

        if (v == btnLogin)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            account = etAccount.getText().toString();
            pwd = etPwd.getText().toString();


            if (TextUtils.isEmpty(account) || account.length() < 5)
            {
                ToastUtil.show(LoginActivity.this, "请输入5-16位账号");
                return;
            }
            //            String str = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
            //            if (!str.contains(String.valueOf(account.charAt(0))))
            //            {
            //                ToastUtil.show(LoginActivity.this, "账号必须以字母开头");
            //                return;
            //            }


            if (TextUtils.isEmpty(pwd) || pwd.length() < 6)
            {
                ToastUtil.show(LoginActivity.this, "请输入6-18位密码");
                return;
            }

            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("user_name", account);
            valuePairs.put("password", pwd);
            DataRequest.instance().request(LoginActivity.this, Urls.getLoginUrl(), this,
                    HttpRequest.POST, USER_LOGIN, valuePairs, new LoginHandler());


        }
        else if (v == mRegisterLayout)
        {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            //startActivity(new Intent(LoginActivity.this, PhoneRegisterActivity.class));

        }

        else if (v == ivBack)
        {
            finish();
        }
        else if (v == tvRecoveryPwdPhone)
        {
            startActivity(new Intent(LoginActivity.this, FindPwdActivity.class));
        }
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (USER_LOGIN.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }


}
