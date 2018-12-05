package com.zb.wyd.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.MyApplication;
import com.zb.wyd.R;
import com.zb.wyd.fragment.BoxFragment;
import com.zb.wyd.fragment.DouyinFragment;
import com.zb.wyd.fragment.LiveIndexFragment;
import com.zb.wyd.fragment.MemberFragment;
import com.zb.wyd.fragment.SelfieFragment;
import com.zb.wyd.fragment.VideoFragment;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.VersionManager;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity
{

    @BindView(android.R.id.tabhost)
    FragmentTabHost fragmentTabHost;
    public static final String TAB_LIVE = "tab_live";
    public static final String TAB_VIDEO = "tab_video";
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.tv_sign_in)
    TextView tvSignIn;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;

    private String texts[] = {"直播", "抖音", "视频", "自拍", "宝盒"};
    private int imageButton[] = {R.drawable.ic_live_selector, R.drawable.ic_dy_selector, R.drawable.ic_video_selector, R.drawable
            .ic_photo_selector, R.drawable.ic_box_selector};


    private Class fragmentArray[] = {LiveIndexFragment.class, DouyinFragment.class, VideoFragment.class, SelfieFragment.class,BoxFragment.class};

    @Override
    protected void initData()
    {
        mMyBroadCastReceiver = new MyBroadCastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TAB_LIVE);
        intentFilter.addAction(TAB_VIDEO);
        registerReceiver(mMyBroadCastReceiver, intentFilter);
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_main);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(MainActivity.this, false);
    }


    @Override
    protected void initEvent()
    {
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            @Override
            public void onTabChanged(String tabId)
            {
            }
        });

        tvSignIn.setOnClickListener(this);
        rlSearch.setOnClickListener(this);
    }

    public void setTab(int p)
    {
        fragmentTabHost.setCurrentTab(p);
    }

    protected static final int READ_PHONE_STATE_PERMISSIONS_REQUEST_CODE = 9002;

    @Override
    protected void initViewData()
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                        .PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_PHONE_STATE))
            {
                ToastUtil.show(MainActivity.this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission
                    .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_PHONE_STATE_PERMISSIONS_REQUEST_CODE);
        }
        else
        {
            initMain();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case READ_PHONE_STATE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager
                        .PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED)
                {
                    initMain();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initMain()
    {
        fragmentTabHost.setup(this, getSupportFragmentManager(), R.id.main_layout);

        for (int i = 0; i < texts.length; i++)
        {
            TabHost.TabSpec spec = fragmentTabHost.newTabSpec(texts[i]).setIndicator(getView(i));

            fragmentTabHost.addTab(spec, fragmentArray[i], null);

            //设置背景(必须在addTab之后，由于需要子节点（底部菜单按钮）否则会出现空指针异常)
            // fragmentTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable
            // .main_tab_selector);
        }
        fragmentTabHost.getTabWidget().setDividerDrawable(R.color.transparent);
        new VersionManager(this).init();

        ImageLoader.getInstance().displayImage(ConfigManager.instance().getUserPic(),ivUserPic);
    }

    private View getView(int i)
    {
        //取得布局实例
        View view = View.inflate(MainActivity.this, R.layout.tabcontent, null);
        //取得布局对象
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.text);

        //设置图标
        imageView.setImageResource(imageButton[i]);
        //设置标题
        textView.setText(texts[i]);
        return view;
    }


    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            DialogUtils.showToastDialog2Button(MainActivity.this, "是否退出APP", new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    sendBroadcast(new Intent("EXIT_APP"));
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                Thread.sleep(500);
                                System.exit(0);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            });

            return false;
        }
        else
        {
            return super.onKeyDown(keyCode, event);
        }

    }

    private MyBroadCastReceiver mMyBroadCastReceiver;


    class MyBroadCastReceiver extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {

            if (TAB_LIVE.contentEquals(intent.getAction()))
            {
                fragmentTabHost.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fragmentTabHost.setCurrentTab(0);

                    }
                }, 100);

            }
            else if (TAB_VIDEO.contentEquals(intent.getAction()))
            {
                fragmentTabHost.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        fragmentTabHost.setCurrentTab(1);

                    }
                }, 100);

            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (null != mMyBroadCastReceiver)
        {
            unregisterReceiver(mMyBroadCastReceiver);
        }

    }


    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == tvSignIn)
        {
           // startActivity(new Intent(MainActivity.this, AuthorPhotoListActivity.class));
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        }
        else if (v == rlSearch)
        {
            startActivity(new Intent(MainActivity.this, SearchListActivity.class));

        }
    }
}
