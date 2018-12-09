package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.StringUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 描述：一句话简单描述
 */
public class BindPhoneActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView  tvTitle;
    @BindView(R.id.et_phone)
    EditText  etPhone;
    @BindView(R.id.tv_code)
    TextView  tvCode;
    @BindView(R.id.et_code)
    EditText  etCode;
    @BindView(R.id.btn_submit)
    Button    btnSubmit;

    private int time = 60;
    private int    count;
    private String token;

    private static final String BIND_PHONE             = "bind_phone";
    private static final String GET_PHONE_CODE         = "get_phone_code";
    private static final int    REQUEST_SUCCESS        = 0x01;
    private static final int    REQUEST_FAIL           = 0x02;
    private static final int    UPDATE_CODE_VIEW       = 0X03;
    private static final int    GET_EMAIL_CODE_SUCCESS = 0X04;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(BindPhoneActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    ToastUtil.show(BindPhoneActivity.this, "绑定成功");

                    if (mHandler.hasMessages(UPDATE_CODE_VIEW))
                        mHandler.removeMessages(UPDATE_CODE_VIEW);
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(BindPhoneActivity.this, msg.obj.toString());
                    break;


                case UPDATE_CODE_VIEW:
                    count++;

                    int result = time - count;

                    if (result >= 0)
                    {
                        tvCode.setText(result + "S后发送");
                        mHandler.sendEmptyMessageDelayed(UPDATE_CODE_VIEW, 1000);
                    }
                    else
                    {
                        etPhone.setEnabled(true);
                        tvCode.setEnabled(true);
                        tvCode.setText("发送验证码");
                    }

                    break;

                case GET_EMAIL_CODE_SUCCESS:
                    ToastUtil.show(BindPhoneActivity.this, "验证码已发");
                    ResultHandler resultHandler = (ResultHandler) msg.obj;
                    token = resultHandler.getContent();
                    etPhone.setEnabled(false);
                    mHandler.sendEmptyMessage(UPDATE_CODE_VIEW);
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
        setContentView(R.layout.activity_bind_phone);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.yellow));
        StatusBarUtil.StatusBarLightMode(BindPhoneActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        btnSubmit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        tvCode.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("绑定手机");
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == btnSubmit)
        {
            String phone = etPhone.getText().toString();
            String code = etCode.getText().toString();

            if (TextUtils.isEmpty(phone)||phone.length()<11)
            {
                ToastUtil.show(this, "请输入正确的手机");
                return;
            }


            if (StringUtils.stringIsEmpty(phone))
            {
                ToastUtil.show(this, "请输入验证码");
                return;
            }

            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("phone", phone);
            valuePairs.put("scode", code);
            valuePairs.put("token", token);
            DataRequest.instance().request(this, Urls.getTaskprofileUrl(), this, HttpRequest.POST, BIND_PHONE, valuePairs,
                    new ResultHandler());
        }
        else if (v == tvCode)
        {
            final String phone = etPhone.getText().toString();

            if (TextUtils.isEmpty(phone)||phone.length()<11)
            {
                ToastUtil.show(this, "请输入正确的手机");
                return;
            }


            DialogUtils.showToastDialog2Button(this, "绑定手机后，将只能使用手机号码和密码登录", new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    tvCode.setEnabled(false);

                    showProgressDialog();
                    Map<String, String> valuePairs = new HashMap<>();
                    valuePairs.put("phone", phone);
                    valuePairs.put("action", "bind");
                    DataRequest.instance().request(BindPhoneActivity.this, Urls.getPhoneCodeUrl(), BindPhoneActivity.this, HttpRequest.POST, GET_PHONE_CODE, valuePairs,
                            new ResultHandler());
                }
            });



        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
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

        else if (GET_PHONE_CODE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_EMAIL_CODE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (mHandler.hasMessages(UPDATE_CODE_VIEW))
            mHandler.removeMessages(UPDATE_CODE_VIEW);
    }
}
