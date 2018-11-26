package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.adapter.CommentAdapter;
import com.zb.wyd.adapter.PhotoAdapter;
import com.zb.wyd.entity.CommentInfo;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.entity.PriceInfo;
import com.zb.wyd.entity.ShareInfo;
import com.zb.wyd.entity.SignInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.CommentInfoListHandler;
import com.zb.wyd.json.PhotoInfoHandler;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.json.ShareInfoHandler;
import com.zb.wyd.json.SignInfoHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.listener.MyOnClickListener;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.StringUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.DividerDecoration;
import com.zb.wyd.widget.FullyGridLayoutManager;
import com.zb.wyd.widget.MaxRecyclerView;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：自拍详情
 */
public class PhotoDetailActivity extends BaseActivity implements IRequestListener
{

    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    TextView        tvTitle;
    @BindView(R.id.tv_update_time)
    TextView        tvAddTime;
    @BindView(R.id.rv_photo)
    MaxRecyclerView rvPhoto;
    @BindView(R.id.rv_comment)
    MaxRecyclerView rvComment;
    @BindView(R.id.tv_send)
    TextView        tvSend;
    @BindView(R.id.tv_collection_num)
    TextView        tvCollectionNum;
    @BindView(R.id.tv_more)
    TextView        tvMore;

    @BindView(R.id.et_content)
    EditText        etContent;
    @BindView(R.id.iv_share)
    ImageView       ivShare;
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.tv_user_name)
    TextView        tvUserName;
    @BindView(R.id.tv_desc)
    TextView        tvDesc;
    private List<String>      freePic         = new ArrayList<>();
    private List<String>      chargePic       = new ArrayList<>();
    private List<String>      allPic          = new ArrayList<>();
    private List<CommentInfo> commentInfoList = new ArrayList<>();
    private PhotoAdapter   mPhotoAdapter;
    private CommentAdapter mCommentAdapter;

    private int labelBgArr[] = {
            R.drawable.lable1_3dp, R.drawable.lable2_3dp, R.drawable.lable3_3dp, R.drawable.lable4_3dp, R.drawable.lable5_3dp, R.drawable
            .lable6_3dp,};

    private String biz_id;
    private int pn = 1;
    private PriceInfo priceInfo;
    private String    shareCnontent;

    private String contact;
    private              String has_favorite             = "0";
    private static final String GET_TASK_SHARE           = "GET_TASK_SHARE";
    private static final String GET_SHARE                = "GET_SHARE";
    private static final String BUY_PHOTO                = "buy_photo";
    private static final String FAVORITE_LIKE            = "favorite_like";
    private static final String SEND_COMMENT             = "send_comment";
    private static final String GET_PH0TO_DETAIL         = "get_photo_detail";
    private static final String GET_COMMENT_LIST         = " get_comment_list";
    private final        String UN_FAVORITE_LIKE         = "un_favorite_like";
    private static final int    REQUEST_SUCCESS          = 0x01;
    private static final int    REQUEST_FAIL             = 0x02;
    private static final int    BUY_PHOTO_SUCCESS        = 0x05;
    private static final int    GET_COMMENT_LIST_CODE    = 0x06;
    private static final int    GET_COMMENT_LIST_SUCCESS = 0x07;
    private static final int    GET_PHOTO_LIST_CODE      = 0x08;
    private static final int    FAVORITE_LIKE_SUCCESS    = 0x09;
    private final        int    UN_FAVORITE_LIKE_SUCCESS = 0x15;
    private static final int    SEND_COMMENT_SUCCESS     = 0x10;
    private static final int    GET_SHARE_CODE           = 0x11;
    private static final int    GET_SHARE_SUCCESS        = 0x12;
    private static final int    GET_TASK_SHARE_CODE      = 0x13;
    private static final int    GET_TASK_SHARE_SUCCESS   = 0x14;


    private static final int         SHARE_PHOTO_REQUEST_CODE = 0x91;
    @SuppressLint("HandlerLeak")
    private              BaseHandler mHandler                 = new BaseHandler(PhotoDetailActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    PhotoInfoHandler mPhotoInfoHandler = (PhotoInfoHandler) msg.obj;
                    PhotoInfo photoInfo = mPhotoInfoHandler.getPhotoInfo();

                    if (null != photoInfo)
                    {
                        priceInfo = photoInfo.getPriceInfo();
                     //   tvName.setText(photoInfo.getPname());
                        tvAddTime.setText(photoInfo.getAdd_time());
                        tvDesc.setText(photoInfo.getDesc());

                        has_favorite = photoInfo.getHas_favorite();
//                        if ("1".equals(photoInfo.getHas_favorite()))
//                        {
//                            ivCollection.setEnabled(false);
//                        }
//                        else
//                        {
//                            ivCollection.setEnabled(true);
//                        }


                        if (!TextUtils.isEmpty(photoInfo.getTags()))
                        {
                            String labeArr[] = photoInfo.getTags().split(",");


                            for (int i = 0; i < labeArr.length; i++)
                            {
                                TextView tvLabel = new TextView(PhotoDetailActivity.this);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout
                                        .LayoutParams.WRAP_CONTENT);
                                params.leftMargin = 10;
                                tvLabel.setPadding(10, 3, 10, 3);
                                tvLabel.setText(labeArr[i]);
                                tvLabel.setTextSize(12);
                                tvLabel.setTextColor(ContextCompat.getColor(PhotoDetailActivity.this, R.color.white));
                                tvLabel.setBackgroundResource(labelBgArr[i]);
                                tvLabel.setLayoutParams(params);
                             //   llLabel.addView(tvLabel);

                            }
                        }


                        UserInfo userInfo = photoInfo.getUserInfo();

                        if (null != userInfo)
                        {
                            ImageLoader.getInstance().displayImage(userInfo.getUface(), ivUserPic);
                            tvUserName.setText(userInfo.getUnick());
                        }

                        freePic.clear();
                        freePic.addAll(photoInfo.getFreePic());
                        tvCollectionNum.setText(photoInfo.getFavour_count());
                        chargePic.clear();
                        chargePic.addAll(photoInfo.getChargePic());


//                        if (null != priceInfo)
//                        {
//                            btnBuy.setVisibility(View.VISIBLE);
//                        }
//                        else
//                        {
//                            btnBuy.setVisibility(View.GONE);
//
//
//                        }
//
//                        contact = photoInfo.getContact();
//                        if (TextUtils.isEmpty(contact))
//                        {
//                            tvContact.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            tvContact.setVisibility(View.VISIBLE);
//                            tvContact.setText("联系作者");
//                        }
//
//                        if (TextUtils.isEmpty(photoInfo.getLocation()))
//                        {
//                            tvLocation.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            tvLocation.setVisibility(View.VISIBLE);
//                            tvLocation.setText(photoInfo.getLocation());
//
//                        }

                        if(chargePic.isEmpty())
                        {
                            allPic.addAll(freePic);
                        }
                        allPic.addAll(chargePic);
                        mPhotoAdapter.notifyDataSetChanged();

                    }


                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(PhotoDetailActivity.this, msg.obj.toString());

                    break;

                case BUY_PHOTO_SUCCESS:
                    loadData();
                    break;

                case GET_COMMENT_LIST_CODE:
                    getCommentList();
                    break;

                case GET_COMMENT_LIST_SUCCESS:
                    CommentInfoListHandler mCommentInfoListHandler = (CommentInfoListHandler) msg.obj;

                    if (mCommentInfoListHandler.getCommentInfoList().size() < 20)
                    {
                        if(null !=tvMore)
                        tvMore.setVisibility(View.GONE);
                    }
                    else
                    {
                        if(null !=tvMore)
                        tvMore.setVisibility(View.VISIBLE);
                    }
                    commentInfoList.addAll(mCommentInfoListHandler.getCommentInfoList());
                    mCommentAdapter.notifyDataSetChanged();


                    break;

                case GET_PHOTO_LIST_CODE:
                    getPhotoList();
                    break;
                case FAVORITE_LIKE_SUCCESS:
                    has_favorite = "1";
                    ToastUtil.show(PhotoDetailActivity.this, "收藏成功");
                   // ivCollection.setSelected(true);
                    break;
                case UN_FAVORITE_LIKE_SUCCESS:
                    has_favorite = "0";
                    ToastUtil.show(PhotoDetailActivity.this, "取消收藏成功");
                   // ivCollection.setSelected(false);
                    break;
                case SEND_COMMENT_SUCCESS:
                    etContent.setText("");
                    ToastUtil.show(PhotoDetailActivity.this, "评论成功");
                    commentInfoList.clear();
                    getCommentList();
                    break;

                case GET_SHARE_CODE:
                    getShareUrl();
                    break;

                case GET_SHARE_SUCCESS:
                    ShareInfoHandler mShareInfoHandler = (ShareInfoHandler) msg.obj;
                    ShareInfo shareInfo = mShareInfoHandler.getShareInfo();
                    if (null != shareInfo)
                    {
                        shareCnontent = shareInfo.getTitle() + ":" + shareInfo.getUrl();
                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                        intent1.putExtra(Intent.EXTRA_TEXT, shareCnontent);
                        intent1.setType("text/plain");
                        startActivityForResult(Intent.createChooser(intent1, "分享"), SHARE_PHOTO_REQUEST_CODE);

                    }
                    break;

                case GET_TASK_SHARE_CODE:
                    getTaskShareUrl();
                    break;

                case GET_TASK_SHARE_SUCCESS:
                    SignInfoHandler mSignInfoHandler = (SignInfoHandler) msg.obj;
                    SignInfo signInfo = mSignInfoHandler.getSignInfo();

                    if (null != signInfo)
                    {
                        String title = "分享成功";
                        String desc = signInfo.getVal() + "积分";
                        String task = "连续分享" + signInfo.getSeries() + "天";
                        DialogUtils.showTaskDialog(PhotoDetailActivity.this, title, desc, task);
                    }
                    break;
            }
        }
    };

    @Override
    protected void initData()
    {
        biz_id = getIntent().getStringExtra("biz_id");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_photo_detail);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.yellow));
        StatusBarUtil.StatusBarLightMode(PhotoDetailActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
//        btnBuy.setOnClickListener(this);
      //  ivCollection.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        tvMore.setOnClickListener(this);
//        tvContact.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("照片详情");
        rvPhoto.setLayoutManager(new FullyGridLayoutManager(PhotoDetailActivity.this,3));
        rvPhoto.setNestedScrollingEnabled(false);

        mPhotoAdapter = new PhotoAdapter(allPic, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {

            }
        });

        rvPhoto.setAdapter(mPhotoAdapter);

        rvComment.setLayoutManager(new LinearLayoutManager(PhotoDetailActivity.this, LinearLayoutManager.VERTICAL, false));
        rvComment.addItemDecoration(new DividerDecoration(this));
        mCommentAdapter = new CommentAdapter(commentInfoList);
        rvComment.setAdapter(mCommentAdapter);
        loadData();
    }

    private void loadData()
    {
        mHandler.sendEmptyMessage(GET_PHOTO_LIST_CODE);
        mHandler.sendEmptyMessage(GET_COMMENT_LIST_CODE);
    }

    private void showBuyDialog()
    {
        if (null != priceInfo)
        {
            DialogUtils.showLivePriceDialog(PhotoDetailActivity.this, priceInfo, new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                }
            }, new MyOnClickListener.OnSubmitListener()
            {
                @Override
                public void onSubmit(String content)
                {

                    if ("1".equals(content))//兑换
                    {

                        buyPhoto(priceInfo.getFinger());


                    }
                    //购买VIP
                    else if ("3".equals(content))
                    {
                        startActivity(new Intent(PhotoDetailActivity.this, MemberActivity
                                .class));
                    }
                    else//去做任务
                    {

                        // sendBroadcast(new Intent(MainActivity.TAB_TASK));
                        startActivity(new Intent(PhotoDetailActivity.this, TaskActivity
                                .class));
                        finish();
                    }

                }
            }).show();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
//        else if (v == btnBuy)
//        {
//            showBuyDialog();
//        }
//        else if (v == ivCollection)
//        {
//            if("1".equals(has_favorite))
//            {
//                unFavoriteLike();
//            }
//            else
//            {
//                favoriteLike();
//            }
//
//        }
        else if (v == tvSend)
        {
            sendComment();
        }
        else if (v == ivShare)
        {
            showProgressDialog();
            mHandler.sendEmptyMessage(GET_SHARE_CODE);
        }
        else if (v == tvMore)
        {
            pn += 1;
            getCommentList();
        }
//        else if (v == tvContact)
//        {
//            if ("fee".equals(contact))
//            {
//                showBuyDialog();
//            }
//            else
//            {
//                tvContact.setText(contact);
//            }
//        }
    }


    private void getPhotoList()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        DataRequest.instance().request(PhotoDetailActivity.this, Urls.getPhotoDetailUrl(), this, HttpRequest.GET, GET_PH0TO_DETAIL, valuePairs,
                new PhotoInfoHandler());
    }

    private void getCommentList()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pid", biz_id);
        valuePairs.put("pn", pn + "");
        valuePairs.put("num", "20");
        DataRequest.instance().request(PhotoDetailActivity.this, Urls.getCommentlistUrl(), this, HttpRequest.GET, GET_COMMENT_LIST, valuePairs,
                new CommentInfoListHandler());
    }

    //兑换
    private void buyPhoto(String finger)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "photo");
        valuePairs.put("finger", finger);
        DataRequest.instance().request(PhotoDetailActivity.this, Urls.getBuyLiveUrl(), this, HttpRequest.POST, BUY_PHOTO, valuePairs,
                new ResultHandler());
    }


    private void favoriteLike()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "photo");
        DataRequest.instance().request(PhotoDetailActivity.this, Urls.getCollectionRequestUrl(), this, HttpRequest.POST, FAVORITE_LIKE, valuePairs,
                new ResultHandler());
    }
    private void unFavoriteLike()
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "photo");
        DataRequest.instance().request(PhotoDetailActivity.this, Urls.getFavoriteUnLikeUrl(), this, HttpRequest.POST, UN_FAVORITE_LIKE, valuePairs,
                new ResultHandler());
    }


    private void getShareUrl()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "photo");
        DataRequest.instance().request(PhotoDetailActivity.this, Urls.getShareUrl(), this, HttpRequest.GET, GET_SHARE, valuePairs,
                new ShareInfoHandler());
    }

    private void getTaskShareUrl()
    {
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("biz_id", biz_id);
        valuePairs.put("co_biz", "photo");
        DataRequest.instance().request(PhotoDetailActivity.this, Urls.getTaskShareUrl(), this, HttpRequest.GET, GET_TASK_SHARE, valuePairs,
                new SignInfoHandler());
    }

    private void sendComment()
    {
        String text = etContent.getText().toString();

        if (StringUtils.stringIsEmpty(text))
        {
            ToastUtil.show(this, "请输入评论内容");
            return;
        }
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put("pid", biz_id);
        valuePairs.put("text", text);
        DataRequest.instance().request(PhotoDetailActivity.this, Urls.getSendCommentUrl(), this, HttpRequest.POST, SEND_COMMENT, valuePairs,
                new ResultHandler());
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (GET_PH0TO_DETAIL.equals(action))
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
        else if (BUY_PHOTO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(BUY_PHOTO_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_COMMENT_LIST.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_COMMENT_LIST_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (FAVORITE_LIKE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(FAVORITE_LIKE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (SEND_COMMENT.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(SEND_COMMENT_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (GET_SHARE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_SHARE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }

        }
        else if (GET_TASK_SHARE.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_TASK_SHARE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (UN_FAVORITE_LIKE.equals(action))
        {
            hideProgressDialog();
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(UN_FAVORITE_LIKE_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("DemoActivity", "requestCode=" + requestCode + " resultCode=" + resultCode);
        if ((int) (Math.random() * 100) <= 80)
            mHandler.sendEmptyMessage(GET_TASK_SHARE_CODE);

    }


}
