package com.zb.wyd.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kevin.crop.UCrop;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.entity.PhotoInfo;
import com.zb.wyd.entity.UserInfo;
import com.zb.wyd.http.DataRequest;
import com.zb.wyd.http.HttpRequest;
import com.zb.wyd.http.IRequestListener;
import com.zb.wyd.json.PhotoInfoHandler;
import com.zb.wyd.json.ResultHandler;
import com.zb.wyd.json.UserInfoHandler;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.ConstantUtil;
import com.zb.wyd.utils.ImageUtils;
import com.zb.wyd.utils.LogUtil;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.utils.Urls;
import com.zb.wyd.widget.CircleImageView;
import com.zb.wyd.widget.SelectPicturePopupWindow;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述：一句话简单描述
 */
public class UserDetailActivity extends BaseActivity implements IRequestListener
{
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_user_pic)
    CircleImageView ivUserPic;
    @BindView(R.id.ll_user_pic)
    RelativeLayout llUserPic;
    @BindView(R.id.tv_user_nick)
    TextView tvUserNick;
    @BindView(R.id.ll_user_nick)
    RelativeLayout llUserNick;
    @BindView(R.id.ll_user_pwd)
    RelativeLayout llUserPwd;
    @BindView(R.id.tv_phone_status)
    TextView tvPhoneStatus;
    @BindView(R.id.ll_bind_phone)
    RelativeLayout llBindPhone;

    private UserInfo userInfo;

    private static final String GET_USER_DETAIL = "get_user_detail";
    private static final String SAVE_USER_INFO = "save_user_info";
    private static final String UPLOAD_USER_PIC = "upload_user_pic";
    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;
    private static final int UPLOAD_PIC_SUCCESS = 0x03;
    private static final int GET_USER_SUCCESS = 0x04;

    private SelectPicturePopupWindow mSelectPicturePopupWindow;
    private Bitmap bitmap = null;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    protected static final int CAMERA_PERMISSIONS_REQUEST_CODE = 102;
    private static final int GALLERY_REQUEST_CODE = 9001;    // 相册选图标记
    private static final int CAMERA_REQUEST_CODE = 9002;    // 相机拍照标记
    private static final int MODIFY_USER_NICK_CODE = 9003;    // 修改昵称
    // 拍照临时图片
    private String mTempPhotoPath;
    // 剪切后图像文件
    private Uri mDestinationUri;

    private String userPic;
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(UserDetailActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case REQUEST_SUCCESS:
                    ToastUtil.show(UserDetailActivity.this, "头像保存成功");
                    ImageLoader.getInstance().displayImage(userPic, ivUserPic);
                    break;


                case REQUEST_FAIL:
                    ToastUtil.show(UserDetailActivity.this, msg.obj.toString());
                    break;

                case UPLOAD_PIC_SUCCESS:
                    PhotoInfoHandler mPhotoInfoHandler = (PhotoInfoHandler) msg.obj;
                    PhotoInfo photoInfo = mPhotoInfoHandler.getPhotoInfo();

                    if (null != photoInfo)
                    {
                        userPic = photoInfo.getHost() + photoInfo.getSavepath() + photoInfo.getSavename();
                        saveUser("face", userPic);
                    }

                    break;

                case GET_USER_SUCCESS:
                    UserInfoHandler mUserInfoHandler = (UserInfoHandler) msg.obj;
                    userInfo = mUserInfoHandler.getUserInfo();

                    if (null != userInfo)
                    {
                        if (null != ivUserPic && !TextUtils.isEmpty(userInfo.getUface()))
                            ImageLoader.getInstance().displayImage(userInfo.getUface(), ivUserPic);

                        if (null != tvUserNick) tvUserNick.setText(userInfo.getUnick());


                        //                        if (userInfo.getEmail().equals(userInfo.getUname()))
                        //                        {
                        //                            tvPhoneStatus.setText("已绑定手机" + userInfo.getUname() + "不支持换不绑");
                        //                            tvPhoneStatus.setTextColor(ContextCompat.getColor(UserDetailActivity.this, R.color.hint_edit));
                        //                            llBindPhone.setEnabled(false);
                        //                            ConfigManager.instance().setUserName(userInfo.getUname());
                        //                        }
                        //                        else
                        //                        {
                        tvPhoneStatus.setTextColor(ContextCompat.getColor(UserDetailActivity.this, R.color.blackC));
                        tvPhoneStatus.setText("安全设置");
                        llBindPhone.setEnabled(true);
                        //                        }
                    }
                    break;

            }
        }
    };

    @Override
    protected void initData()
    {
        userInfo = (UserInfo) getIntent().getSerializableExtra("USER_INFO");
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_user_detail);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(UserDetailActivity.this, false);
    }

    @Override
    protected void initEvent()
    {
        ivBack.setOnClickListener(this);
        llUserPic.setOnClickListener(this);
        llUserNick.setOnClickListener(this);
        llUserPwd.setOnClickListener(this);
        llBindPhone.setOnClickListener(this);
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("个人信息");
        ivBack.setOnClickListener(this);

        mSelectPicturePopupWindow = new SelectPicturePopupWindow(UserDetailActivity.this);
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

        mDestinationUri = Uri.fromFile(new File(getCacheDir(), "cropImage.jpeg"));
        mTempPhotoPath = Environment.getExternalStorageDirectory() + File.separator + "photo.jpeg";
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(UserDetailActivity.this, Urls.getUserInfoUrl(), this, HttpRequest.GET, GET_USER_DETAIL, valuePairs, new
                UserInfoHandler());
    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v == ivBack)
        {
            finish();
        }
        else if (v == llUserPic)
        {
            mSelectPicturePopupWindow.showPopupWindow(UserDetailActivity.this);
        }
        else if (v == llUserNick)
        {
            startActivityForResult(new Intent(UserDetailActivity.this, ModifyUserActivity.class), MODIFY_USER_NICK_CODE);
        }
        else if (v == llUserPwd)
        {
            startActivity(new Intent(UserDetailActivity.this, ModifyPwdActivity.class));

        }
        else if (v == llBindPhone)
        {
            startActivity(new Intent(UserDetailActivity.this, SafeSettingActivity.class));
        }
    }

    private void uploadPic(Uri uri)
    {
        String filePath = ImageUtils.compressImage(getRealPathFromURI(uri));

        LogUtil.e("TAG", "filePath--->" + filePath);

        showProgressDialog();
        File mFile = new File(filePath);
        Map<String, String> valuePairs = new HashMap<>();
        DataRequest.instance().request(UserDetailActivity.this, Urls.getPhotoUploadUrl(), this, HttpRequest.UPLOAD, UPLOAD_USER_PIC, valuePairs,
                mFile, new PhotoInfoHandler());
    }


    private void saveUser(String key, String value)
    {
        showProgressDialog();
        Map<String, String> valuePairs = new HashMap<>();
        valuePairs.put(key, value);
        DataRequest.instance().request(this, Urls.getTaskprofileUrl(), this, HttpRequest.POST, SAVE_USER_INFO, valuePairs, new ResultHandler());
    }

    private void takePhoto()
    {
        if (ContextCompat.checkSelfPermission(UserDetailActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(UserDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                        .PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(UserDetailActivity.this, Manifest.permission.CAMERA))
            {
                ToastUtil.show(UserDetailActivity.this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(UserDetailActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission
                    .READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        }
        else
        {
            mSelectPicturePopupWindow.dismissPopupWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                doTakePhotoIn7(new File(mTempPhotoPath).getAbsolutePath());
            }
            else
            {
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //下面这句指定调用相机拍照后的照片存储的路径
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mTempPhotoPath)));
                startActivityForResult(takeIntent, CAMERA_REQUEST_CODE);
            }
        }
    }


    private void doTakePhotoIn7(String path)
    {
        Uri mCameraTempUri;
        try
        {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.Images.Media.DATA, path);
            mCameraTempUri = UserDetailActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (mCameraTempUri != null)
            {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempUri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            }
            startActivityForResult(intent, CAMERA_REQUEST_CODE);


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void pickFromGallery()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN // Permission was added in API Level 16
                && ActivityCompat.checkSelfPermission(UserDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED)
        {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getString(R.string.permission_read_storage_rationale),
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

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startCropActivity(Uri uri)
    {
        UCrop.of(uri, mDestinationUri).withAspectRatio(1, 1).withMaxResultSize(200, 200).withTargetActivity(CropActivity.class).start
                (UserDetailActivity.this);
    }

    //将URI文件转化为FILE文件
    public String getRealPathFromURI(Uri uri)
    {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme))
        {
            data = uri.getPath();
        }
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme))
        {
            Cursor cursor = UserDetailActivity.this.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null,
                    null);
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

    /**
     * 处理剪切成功的返回值
     *
     * @param result
     */
    private void handleCropResult(Intent result)
    {
        //  deleteTempPhotoFile();
        final Uri resultUri = UCrop.getOutput(result);
        if (null != resultUri)
        {

            //TODO 这个地方处理图片上传操作
            try
            {

                uploadPic(resultUri);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            ToastUtil.show(UserDetailActivity.this, "无法剪切选择图片");
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
            case CAMERA_PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    takePhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /**
     * 处理剪切失败的返回值
     *
     * @param result
     */
    private void handleCropError(Intent result)
    {
        //  deleteTempPhotoFile();
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null)
        {
            ToastUtil.show(UserDetailActivity.this, cropError.getMessage());
        }
        else
        {
            ToastUtil.show(UserDetailActivity.this, "无法剪切选择图片");
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
                    startCropActivity(Uri.fromFile(temp));
                    break;
                case GALLERY_REQUEST_CODE:  // 直接从相册获取
                    startCropActivity(data.getData());
                    break;
                case UCrop.REQUEST_CROP:    // 裁剪图片结果
                    handleCropResult(data);
                    break;
                case UCrop.RESULT_ERROR:    // 裁剪图片错误
                    handleCropError(data);

                case MODIFY_USER_NICK_CODE:
                    String name = data.getStringExtra("NAME");
                    tvUserNick.setText(name);

                    break;
            }
        }
    }

    @Override
    public void notify(String action, String resultCode, String resultMsg, Object obj)
    {
        hideProgressDialog();
        if (UPLOAD_USER_PIC.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(UPLOAD_PIC_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
        else if (SAVE_USER_INFO.equals(action))
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
        else if (GET_USER_DETAIL.equals(action))
        {
            if (ConstantUtil.RESULT_SUCCESS.equals(resultCode))
            {
                mHandler.sendMessage(mHandler.obtainMessage(GET_USER_SUCCESS, obj));
            }
            else
            {
                mHandler.sendMessage(mHandler.obtainMessage(REQUEST_FAIL, resultMsg));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
