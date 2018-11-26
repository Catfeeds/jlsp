package com.zb.wyd.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//作者
public class AuthorDetailActivity extends BaseActivity
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.iv_appointment)
    ImageView ivAppointment;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_sex)
    ImageView ivSex;
    @BindView(R.id.tv_user_location)
    TextView tvUserLocation;
    @BindView(R.id.tv_invitation_code)
    TextView tvInvitationCode;
    @BindView(R.id.ll_photo)
    LinearLayout llPhoto;
    @BindView(R.id.ll_dy)
    LinearLayout llDy;
    @BindView(R.id.ll_block_one)
    LinearLayout llBlockOne;
    @BindView(R.id.ll_msg)
    LinearLayout llMsg;
    @BindView(R.id.ll_collection)
    LinearLayout llCollection;
    @BindView(R.id.ll_share_friend)
    LinearLayout llShareFriend;
    @BindView(R.id.ll_block_two)
    LinearLayout llBlockTwo;
    @BindView(R.id.ll_yumin)
    LinearLayout llYumin;
    @BindView(R.id.ll_safe)
    LinearLayout llSafe;
    @BindView(R.id.ll_block_three)
    LinearLayout llBlockThree;

    @Override
    protected void initData()
    {

    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_author_detail);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(AuthorDetailActivity.this, false);
    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {

    }



    @OnClick({R.id.iv_back, R.id.iv_share, R.id.ll_photo, R.id.ll_dy, R.id.ll_block_one, R.id
            .ll_msg, R.id.ll_collection, R.id.ll_share_friend, R.id.ll_block_two, R.id.ll_yumin,
            R.id.ll_safe, R.id.ll_block_three})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back:
                break;
            case R.id.iv_share:
                break;
            case R.id.ll_photo:
                break;
            case R.id.ll_dy:
                break;
            case R.id.ll_block_one:
                break;
            case R.id.ll_msg:
                break;
            case R.id.ll_collection:
                break;
            case R.id.ll_share_friend:
                break;
            case R.id.ll_block_two:
                break;
            case R.id.ll_yumin:
                break;
            case R.id.ll_safe:
                break;
            case R.id.ll_block_three:
                break;
        }
    }
}
