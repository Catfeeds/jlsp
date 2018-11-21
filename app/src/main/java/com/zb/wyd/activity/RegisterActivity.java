package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.utils.APPUtils;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.KeyBoardUtils;
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
public class RegisterActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_pwd1)
    EditText etPwd1;

    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.ll_login)
    LinearLayout mLoginLayout;

    private String account;
    private String pwd;
    private static final String USER_REGISTER = "user_register";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(RegisterActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    ToastUtil.show(RegisterActivity.this, "注册成功");
                    ConfigManager.instance().setUserName(account);
                    ConfigManager.instance().setUserPwd(pwd);
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(RegisterActivity.this, msg.obj.toString());
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
        setContentView(R.layout.activity_register);
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this, false);
    }

    @Override
    protected void initEvent()
    {
        btnRegister.setOnClickListener(this);
        mLoginLayout.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {

    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == mLoginLayout)
        {
            finish();
        }
        else if (v == btnRegister)
        {
            account = etAccount.getText().toString();
            pwd = etPwd.getText().toString();

            String pwd1 = etPwd1.getText().toString();
            if (TextUtils.isEmpty(account) || account.length() < 5)
            {
                ToastUtil.show(RegisterActivity.this, "请输入5-16位账号");
                return;
            }
            //            String str = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm";
            //            if (!str.contains(String.valueOf(account.charAt(0))))
            //            {
            //                ToastUtil.show(RegisterActivity.this, "账号必须以字母开头");
            //                return;
            //            }


            if (TextUtils.isEmpty(pwd) || pwd.length() < 6)
            {
                ToastUtil.show(RegisterActivity.this, "请输入6-18位密码");
                return;
            }

            if (!etPwd.equals(pwd1))
            {
                ToastUtil.show(this, "两次密码输入不一致");
            }
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("user_name", account);
            valuePairs.put("password", pwd);
            valuePairs.put("repassword", pwd);
            DataRequest.instance().request(RegisterActivity.this, Urls.getRegisterUrl(), this,
                    HttpRequest.POST, USER_REGISTER, valuePairs, new ResultHandler());
        }


    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (USER_REGISTER.equals(action))
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
