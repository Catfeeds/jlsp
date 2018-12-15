package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
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
public class ModifyPwdActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_aging_pwd)
    EditText etAgingPwd;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    private static final String SAVE_PWD = "save_pwd";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(ModifyPwdActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    ToastUtil.show(ModifyPwdActivity.this, "修改成功");
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(ModifyPwdActivity.this, msg.obj.toString());
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
        setContentView(R.layout.activity_modify_pwd);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(ModifyPwdActivity.this, false);

    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("密码修改");
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
            String oldPwd = etOldPwd.getText().toString();
            String newPwd = etNewPwd.getText().toString();
            String agingPwd = etAgingPwd.getText().toString();

            if (StringUtils.stringIsEmpty(oldPwd))
            {
                ToastUtil.show(ModifyPwdActivity.this, "请输入原密码");
                return;
            }

            if (StringUtils.stringIsEmpty(newPwd))
            {
                ToastUtil.show(ModifyPwdActivity.this, "请输入新密码");
                return;
            }


            if (!newPwd.equals(agingPwd))
            {
                ToastUtil.show(ModifyPwdActivity.this, "新密码不一致");
                return;
            }


            //            if (!ConfigManager.instance().getUserPwd().equals(oldPwd))
            //            {
            //                ToastUtil.show(ModifyPwdActivity.this, "原输入不正确");
            //                return;
            //            }

            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("passwd", newPwd);
            valuePairs.put("repasswd", agingPwd);
            valuePairs.put("oldpasswd", oldPwd);
            DataRequest.instance().request(this, Urls.getEditPwdUrl(), this, HttpRequest.POST, SAVE_PWD, valuePairs, new ResultHandler());
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (SAVE_PWD.equals(action))
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
