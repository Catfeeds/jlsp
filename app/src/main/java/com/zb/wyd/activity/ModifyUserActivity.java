package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.ResultHandler;
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
public class ModifyUserActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView  tvTitle;
    @BindView(R.id.tv_submit)
    TextView  tvSubmit;
    @BindView(R.id.et_content)
    EditText  etContent;
    String name;

    private static final String SAVE_NICK       = "save_nick";
    private static final int    REQUEST_SUCCESS = 0x01;
    private static final int    REQUEST_FAIL    = 0x02;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(ModifyUserActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    ToastUtil.show(ModifyUserActivity.this, "修改成功");
                    setResult(Activity.RESULT_OK, new Intent().putExtra("NAME", name));
                    finish();
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(ModifyUserActivity.this, msg.obj.toString());
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
        setContentView(R.layout.activity_modify_user);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.yellow));
        StatusBarUtil.StatusBarLightMode(ModifyUserActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        tvSubmit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("编辑昵称");
        tvSubmit.setText("完成");
        tvSubmit.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == tvSubmit)
        {
            name = etContent.getText().toString();

            if (StringUtils.stringIsEmpty(name))
            {
                ToastUtil.show(this, "请输入昵称");
                return;
            }
            showProgressDialog();
            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("nick", name);
            DataRequest.instance().request(this, Urls.getEditUnickUrl(), this, HttpRequest.POST, SAVE_NICK, valuePairs,
                    new ResultHandler());


        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (SAVE_NICK.equals(action))
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
