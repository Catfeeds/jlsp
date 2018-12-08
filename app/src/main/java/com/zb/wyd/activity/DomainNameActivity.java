package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.DomainHandler;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.StringUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：一句话简单描述
 */
public class DomainNameActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    @BindView(R.id.ll_content)
    LinearLayout llContent;

    private static final String TEST_DOMAINNAME = "test_domainname1";
    private static final int TEST_DOMAINNAME_FAIL = 0x06;
    private static final int TEST_DOMAINNAME_SUCCESS = 0x07;


    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(DomainNameActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case TEST_DOMAINNAME_SUCCESS:

                    DomainHandler mResultHandler = (DomainHandler) msg.obj;

                    tvContent.setText(mResultHandler.getHeader());
                    tvMsg.setText(mResultHandler.getResultMsg());
                    llContent.setVisibility(View.VISIBLE);
                    ToastUtil.show(DomainNameActivity.this, mResultHandler.getResultMsg());
                    break;

                case TEST_DOMAINNAME_FAIL:
                    ToastUtil.show(DomainNameActivity.this, "该域名不可用");
                    llContent.setVisibility(View.GONE);
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
        setContentView(R.layout.activity_domian_name);
        StatusBarUtil.setStatusBarColor(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(DomainNameActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String name = etName.getText().toString();
                if (StringUtils.stringIsEmpty(name))
                {
                    ToastUtil.show(DomainNameActivity.this, "请输入正确的域名");
                    return;
                }

                ConfigManager.instance().setZdyDomainName(name);
                ToastUtil.show(DomainNameActivity.this, "保存成功,重启APP后生效");

            }
        });
        btnTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String name = etName.getText().toString();
                if (StringUtils.stringIsEmpty(name))
                {
                    ToastUtil.show(DomainNameActivity.this, "请输入正确的域名");
                    return;
                }
                String url = name + "/ping.js?_=+" + getRandomString();
                Map<String, String> valuePairs = new HashMap<>();
                DataRequest.instance().request(DomainNameActivity.this, url, DomainNameActivity.this, HttpRequest.GET, TEST_DOMAINNAME, valuePairs, new DomainHandler());
            }
        });
    }

    public static String getRandomString()
    {
        Random rand = new Random();

        int length = rand.nextInt(8) + 3;
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiop1234567890";
        //由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        //长度为几就循环几次
        for (int i = 0; i < length; ++i)
        {
            //产生0-61的数字
            int number = random.nextInt(36);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    @Override
    protected void initViewData()
    {
        etName.setText(ConfigManager.instance().getDomainName());
        tvTitle.setText("域名设置");
        tvEmail.setText("防丢邮箱,发邮件到" + ConfigManager.instance().getSystemEmail() + "获取最新地址");
    }


    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        if (TEST_DOMAINNAME.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(TEST_DOMAINNAME_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(TEST_DOMAINNAME_FAIL, resultMsg));
            }
        }
    }


}
