package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.json.VideoInfoListHandler;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

//安全设置
public class SafeSettingActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_bind)
    Button btnBind;


    private static final String BIND_PHONE = "BIND_PHONE";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(SafeSettingActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_FAIL:
                    ToastUtil.show(SafeSettingActivity.this, msg.obj.toString());

                    break;

                case REQUEST_SUCCESS:
                    ToastUtil.show(SafeSettingActivity.this,"绑定成功");
                    finish();
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
        setContentView(R.layout.activity_safe_setting);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(SafeSettingActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnBind.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("安全设置");
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == btnBind)
        {


            String account = etPhone.getText().toString();
            String pwd = etPwd.getText().toString();


            if (TextUtils.isEmpty(account) || account.length() < 11)
            {
                ToastUtil.show(SafeSettingActivity.this, "请输入正确的手机号码");
                return;
            }


            if (TextUtils.isEmpty(pwd) || pwd.length() < 6)
            {
                ToastUtil.show(SafeSettingActivity.this, "请输入6-18位密码");
                return;
            }
            Map<String, String> valuePairs = new HashMap<>();

            valuePairs.put("phone", account);
            valuePairs.put("passwd", pwd);
            DataRequest.instance().request(this, Urls.getSafeUrl(), this, HttpRequest.POST, BIND_PHONE, valuePairs, new ResultHandler());
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (BIND_PHONE.equals(action))
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
