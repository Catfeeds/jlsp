package com.zb.wyd.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.adapter.AddPhotoAdapter;
import com.zb.wyd.adapter.LabelChooseAdapter;
import com.zb.wyd.entity.CategoryInfo;
import com.zb.wyd.entity.LocationInfo;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.entity.PicInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.LocationInfoHandler;
import com.zb.wyd.json.PhotoInfoHandler;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.ImageUtils;
import com.zb.wyd.utils.LogUtil;
import com.zb.wyd.utils.StringUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.FullyGridLayoutManager;
import com.zb.wyd.widget.MaxRecyclerView;
import com.zb.wyd.widget.SelectPicturePopupWindow;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 描述：一句话简单描述
 */
public class AddPhotoActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_location)
    ImageView       ivLocation;
    @BindView(R.id.iv_back)
    ImageView       ivBack;
    @BindView(R.id.tv_title)
    TextView        tvTitle;
    @BindView(R.id.tv_submit)
    TextView        tvSubmit;
    @BindView(R.id.tv_name)
    EditText        tvName;
    @BindView(R.id.tv_num)
    TextView        tvNum;
    @BindView(R.id.et_desc)
    EditText        etDesc;
    @BindView(R.id.tv_free)
    TextView        tvFree;
    @BindView(R.id.tv_charge)
    TextView        tvCharge;
    @BindView(R.id.rv_free)
    MaxRecyclerView rvFree;
    @BindView(R.id.rv_charge)
    MaxRecyclerView rvCharge;
    @BindView(R.id.et_price)
    EditText        etPrice;
    @BindView(R.id.ll_price)
    LinearLayout    llPrice;
    @BindView(R.id.ll_tags)
    LinearLayout    llTags;
    @BindView(R.id.rv_label)
    MaxRecyclerView rvLabel;


    private int     index;
    private String  host;
    private List<PicInfo>  freePicList     = new ArrayList<>();
    private List<PicInfo>  chargePicList   = new ArrayList<>();
    private List<CategoryInfo> labelChooseList = new ArrayList<>();

    private AddPhotoAdapter    freeAdapter;
    private AddPhotoAdapter    chargeAdapter;
    private LabelChooseAdapter mLabelChooseAdapter;

//    private   LocationInfo locationInfo;
    // 拍照临时图片
    private String                   mTempPhotoPath;
    private SelectPicturePopupWindow mSelectPicturePopupWindow;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION  = 101;
    protected static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;
    private static final   int GALLERY_REQUEST_CODE                    = 9001;    // 相册选图标记
    private static final   int CAMERA_REQUEST_CODE                     = 9002;    // 相机拍照标记
    private static final   int GET_LABEL_CODE                          = 9003;    // 相机拍照标记

//    private static final String GET_LOCATION         = "get_location";
    private static final String ADD_PHOTO            = "add_photo";
    private static final String UPLOAD_USER_PIC      = "upload_user_pic";
    private static final int    REQUEST_SUCCESS      = 0x01;
    private static final int    REQUEST_FAIL         = 0x02;
    private static final int    ADD_PHOTO_SUCCESS    = 0x03;
    private static final int    GET_LOCATION_SUCCESS = 0x04;
    private static final int    GET_LOCATION_CODE    = 0X05;

    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(AddPhotoActivity.this)
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
                        host = photoInfo.getHost();
                        if (index == 0)
                        {
                            PicInfo picInfo = new PicInfo();
                            picInfo.setRelativePath(photoInfo.getSavepath() + photoInfo.getSavename());
                            picInfo.setAbsolutelyPath(photoInfo.getHost() + photoInfo.getSavepath() + photoInfo.getSavename());
                            freePicList.add(freePicList.size() - 1, picInfo);
                            freeAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            PicInfo picInfo = new PicInfo();
                            picInfo.setRelativePath(photoInfo.getSavepath() + photoInfo.getSavename());
                            picInfo.setAbsolutelyPath(photoInfo.getHost() + photoInfo.getSavepath() + photoInfo.getSavename());
                            chargePicList.add(chargePicList.size() - 1, picInfo);
                            chargeAdapter.notifyDataSetChanged();
                        }
                    }

                    break;

                case REQUEST_FAIL:
                    ToastUtil.show(AddPhotoActivity.this, msg.obj.toString());

                    break;

                case ADD_PHOTO_SUCCESS:
                    ToastUtil.show(AddPhotoActivity.this, "图集上传成功");
                    finish();
                    break;

//                case GET_LOCATION_SUCCESS:
//                    LocationInfoHandler mLocationInfoHandler = (LocationInfoHandler) msg.obj;
//                     locationInfo = mLocationInfoHandler.getLocationInfo();
//
//                    if (null != locationInfo)
//                    {
//                        isShowLocation = true;
//                        ivLocation.setVisibility(View.VISIBLE);
//                        location = locationInfo.getProv() + "," + locationInfo.getCity() + "," + locationInfo.getDistrict();
//                    }
//                    break;
//
//                case GET_LOCATION_CODE:
//                    Map<String, String> valuePairs = new HashMap<>();
//                    DataRequest.instance().request(AddPhotoActivity.this, ConfigManager.instance().getIpLookUp(), AddPhotoActivity.this, HttpRequest.GET,
//                            GET_LOCATION,
//                            valuePairs,
//                            new LocationInfoHandler());
//                    break;
            }
        }
    };

    @Override
    protected void initData()
    {
        freePicList.add(new PicInfo());
        chargePicList.add(new PicInfo());
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {

        setContentView(R.layout.activity_add_photo);
        StatusBarUtil.setStatusBarBackground(this,R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(AddPhotoActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        tvFree.setOnClickListener(this);
        tvCharge.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        llTags.setOnClickListener(this);
        etDesc.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                tvNum.setText(s.length() + "/300");
            }
        });
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("发布自拍图集");
        tvSubmit.setText("发布");
        tvSubmit.setVisibility(View.VISIBLE);
        setTab(0);


        rvFree.setLayoutManager(new FullyGridLayoutManager(AddPhotoActivity.this, 4));
        freeAdapter = new AddPhotoAdapter(freePicList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, final int position)
            {
                if (position == (freePicList.size() - 1))
                {
                    mSelectPicturePopupWindow.showPopupWindow(AddPhotoActivity.this);
                }
                else
                {
                    DialogUtils.showToastDialog2Button(AddPhotoActivity.this, "是否删除该图片", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            freePicList.remove(position);
                            freeAdapter.notifyDataSetChanged();
                        }
                    });


                }
            }
        });
        rvFree.setAdapter(freeAdapter);


        rvCharge.setLayoutManager(new FullyGridLayoutManager(AddPhotoActivity.this, 4));
        chargeAdapter = new AddPhotoAdapter(chargePicList, new MyItemClickListener()
        {
            @Override
            public void onItemClick(View view, final int position)
            {
                if (position == (chargePicList.size() - 1))
                {
                    mSelectPicturePopupWindow.showPopupWindow(AddPhotoActivity.this);
                }
                else
                {
                    DialogUtils.showToastDialog2Button(AddPhotoActivity.this, "是否删除该图片", new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            chargePicList.remove(position);
                            chargeAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        });
        rvCharge.setAdapter(chargeAdapter);


        mSelectPicturePopupWindow = new SelectPicturePopupWindow(AddPhotoActivity.this);
        mSelectPicturePopupWindow.setOnSelectedListener(new SelectPicturePopupWindow.OnSelectedListener()
        {
            @Override
            public void OnSelected(View v, int position)
            {
                switch (position)
                {
                    case 0:
                        // "拍照"按钮被点击了
                        takePhoto();
                        break;
                    case 1:
                        // "从相册选择"按钮被点击了
                        pickFromGallery();
                        break;
                    case 2:
                        // "取消"按钮被点击了
                        mSelectPicturePopupWindow.dismissPopupWindow();
                        break;
                }
            }
        });


        rvLabel.setLayoutManager(new FullyGridLayoutManager(AddPhotoActivity.this, 3));
        mLabelChooseAdapter = new LabelChooseAdapter(labelChooseList);
        rvLabel.setAdapter(mLabelChooseAdapter);


        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";


        mHandler.sendEmptyMessage(GET_LOCATION_CODE);
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

            String title = tvName.getText().toString();

            if (StringUtils.stringIsEmpty(title))
            {
                ToastUtil.show(this, "请输入图集名称");
                return;
            }

            if (chargePicList.size() < 2)
            {
                if (freePicList.size() < 4)
                {
                    ToastUtil.show(this, "免费图片数量不少于3张");
                    return;
                }
            }
            else
            {
                if (freePicList.size() < 4)
                {
                    ToastUtil.show(this, "免费图片数量不少于3张");
                    return;
                }

                if (chargePicList.size() <4)
                {
                    ToastUtil.show(this, "收费图片数量不少于3张");
                    return;
                }

                String price = etPrice.getText().toString();

                if (StringUtils.stringIsEmpty(price))
                {
                    ToastUtil.show(this, "请输入收费图片价格");
                    return;
                }
                if (Integer.parseInt(price) < 5 || Integer.parseInt(price) > 50)
                {
                    ToastUtil.show(this, "收费图片价格为5-50=0积分");
                    return;
                }

            }


            StringBuffer freeSb = new StringBuffer();

            for (int i = 0; i < freePicList.size(); i++)
            {
                if (!StringUtils.stringIsEmpty(freePicList.get(i).getAbsolutelyPath()))
                {
                    freeSb.append(freePicList.get(i).getRelativePath());

                    if (i < freePicList.size())
                    {
                        freeSb.append("$$");
                    }
                }
            }


            StringBuffer chargeSb = new StringBuffer();
            for (int i = 0; i < chargePicList.size(); i++)
            {
                if (!StringUtils.stringIsEmpty(chargePicList.get(i).getAbsolutelyPath()))
                {
                    chargeSb.append(chargePicList.get(i).getRelativePath());

                    if (i < chargePicList.size())
                    {
                        chargeSb.append("$$");
                    }
                }
            }


            if(labelChooseList.isEmpty())
            {
                ToastUtil.show(AddPhotoActivity.this,"请选择标签");
                return;
            }
            showProgressDialog();

            Map<String, String> valuePairs = new HashMap<>();
            valuePairs.put("title", title);
            valuePairs.put("desc", etDesc.getText().toString());
            valuePairs.put("tags", getLabel());
            valuePairs.put("free_album", freeSb.toString());
            valuePairs.put("charge_album", chargeSb.toString());
            valuePairs.put("host", host);
            valuePairs.put("cash", etPrice.getText().toString());
            valuePairs.put("coupon", "100");
            DataRequest.instance().request(AddPhotoActivity.this, Urls.getAddPhotoUrl(), this, HttpRequest.POST, ADD_PHOTO, valuePairs,
                    new ResultHandler());


        }
        else if (v == tvFree)
        {
            setTab(0);
        }
        else if (v == tvCharge)
        {
            setTab(1);
        }
//        else if (v == llLocation)
//        {
//            //            showProgressDialog();
//            //            Map<String, String> valuePairs = new HashMap<>();
//            //            DataRequest.instance().request(AddPhotoActivity.this, Urls.getIplookupUrl(), this, HttpRequest.POST, GET_LOCATION, valuePairs,
//            //                    new LocationInfoHandler());
//
//            if (isShowLocation)
//            {
//                isShowLocation = false;
//                ivLocation.setVisibility(View.GONE);
//                tvLocation.setText("不显示地址");
//                tvLocation.setTextColor(ContextCompat.getColor(AddPhotoActivity.this, R.color.blackA));
//            }
//            else
//            {
//                if (null != locationInfo)
//                {
//                    tvLocation.setText(locationInfo.getCity());
//                }
//                isShowLocation = false;
//                ivLocation.setVisibility(View.VISIBLE);
//                tvLocation.setTextColor(ContextCompat.getColor(AddPhotoActivity.this, R.color.yellow));
//            }
//
//        }
        else if (v == llTags)
        {
            startActivityForResult(new Intent(AddPhotoActivity.this, LabelActivity.class), GET_LABEL_CODE);

        }
    }

    private void setTab(int p)
    {
        index = p;
        if (p == 0)
        {
            tvFree.setSelected(true);
            tvCharge.setSelected(false);

            rvFree.setVisibility(View.VISIBLE);
            rvCharge.setVisibility(View.GONE);
            llPrice.setVisibility(View.GONE);
        }
        else
        {
            tvFree.setSelected(false);
            tvCharge.setSelected(true);

            rvFree.setVisibility(View.GONE);
            rvCharge.setVisibility(View.VISIBLE);
            llPrice.setVisibility(View.VISIBLE);
        }
    }


    private String getLabel()
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < labelChooseList.size(); i++)
        {
            sb.append(labelChooseList.get(i).getName());
            if (i < labelChooseList.size() - 1)
            {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    private void pickFromGallery()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(AddPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        }
        else
        {
            mSelectPicturePopupWindow.dismissPopupWindow();
            Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
            // 如果限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }
    }

    private void takePhoto()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(AddPhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        }
        else
        {
            mSelectPicturePopupWindow.dismissPopupWindow();
            Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //下面这句指定调用相机拍照后的照片存储的路径
            takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
            startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
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
            case REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    pickFromGallery();
                }
                break;
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    takePhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case CAMERA_REQUEST_CODE:   // 调用相机拍照
                    File temp = new File(mTempPhotoPath);
                    Uri uri1 = Uri.fromFile(temp);
                    uploadPic(uri1);
                    LogUtil.e("TAG", "URI--->" + uri1);
                    break;
                case GALLERY_REQUEST_CODE:  // 直接从相册获取
                    Uri uri = data.getData();
                    LogUtil.e("TAG", "URI--->" + uri);
                    uploadPic(uri);
                    break;


                case GET_LABEL_CODE:
                    labelChooseList.clear();
                    labelChooseList.addAll((List<CategoryInfo>) data.getSerializableExtra("LABEL_LIST"));
                    mLabelChooseAdapter.notifyDataSetChanged();
                    break;

            }
        }
    }


    private void uploadPic(Uri uri)
    {
        String filePath = ImageUtils.compressImage(getRealPathFromURI(uri));

        LogUtil.e("TAG", "filePath--->" + filePath);

        showProgressDialog();
        File mFile = new File(filePath);
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(AddPhotoActivity.this, ConfigManager.instance().getUploadImgUrl(), this, HttpRequest.UPLOAD, UPLOAD_USER_PIC, valuePairs, mFile,
                new PhotoInfoHandler());
    }


    //将URI文件转化为FILE文件
    public String getRealPathFromURI(Uri uri)
    {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme))
        {
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
        {
            Cursor cursor = AddPhotoActivity.this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor)
            {
                if (cursor.moveToFirst())
                {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1)
                    {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (UPLOAD_USER_PIC.equals(action))
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
        else if (ADD_PHOTO.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(ADD_PHOTO_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }

    }


}
