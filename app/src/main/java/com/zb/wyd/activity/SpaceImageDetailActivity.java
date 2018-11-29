package com.zb.wyd.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zb.wyd.R;
import com.zb.wyd.adapter.MyViewPagerAdapter;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.utils.ConfigManager;
import com.zb.wyd.utils.DialogUtils;
import com.zb.wyd.utils.ToastUtil;
import com.zb.wyd.widget.SmoothImageView;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 描述：一句话简单描述
 */
public class SpaceImageDetailActivity extends BaseActivity
{

    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    // private SmoothImageView mSmoothImageView = null;

    private ImageView mBackIv;
    private ImageView mDownloadIv;
    // private String mImgUrl;
    private ViewPager mViewPager;

    private static final int SHOW_PROGRESSDIALOG = 0x01;
    private static final int HIDE_PROGRESSDIALOG = 0x02;


    private int selectPosition;
    private int startPosition;


    private List<String> picList = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private BaseHandler mHandler = new BaseHandler(SpaceImageDetailActivity.this)
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case SHOW_PROGRESSDIALOG:
                    showProgressDialog();
                    break;

                case HIDE_PROGRESSDIALOG:
                    hideProgressDialog();
                    ToastUtil.show(SpaceImageDetailActivity.this, msg.obj.toString());
                    break;


            }
        }
    };

    class SamplePagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            return picList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position)
        {
            PhotoView photoView = new PhotoView(container.getContext());
            selectPosition = position;
            ImageLoader.getInstance().displayImage(picList.get(position), photoView);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

    }


    @Override
    protected void initData()
    {
        picList = getIntent().getStringArrayListExtra("PIC_LIST");
        startPosition = getIntent().getIntExtra("startPosition", 0);
        selectPosition = startPosition;
    }


    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_img_detail);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.yellow));
        StatusBarUtil.StatusBarLightMode(SpaceImageDetailActivity.this, false);


        mViewPager = (ViewPager) findViewById(R.id.mViewPager);
        mBackIv = (ImageView) findViewById(R.id.iv_back);

        //        mImgUrl = getIntent().getStringExtra("url");
        //        mLocationX = getIntent().getIntExtra("locationX", 0);
        //        mLocationY = getIntent().getIntExtra("locationY", 0);
        //        mWidth = getIntent().getIntExtra("width", 0);
        //        mHeight = getIntent().getIntExtra("height", 0);
        mDownloadIv = (ImageView) findViewById(R.id.iv_download);
        //        mSmoothImageView = (SmoothImageView) findViewById(R.id.smoothImageView);
        //        mSmoothImageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        //        mSmoothImageView.transformIn();
        //        // mSmoothImageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        //        mSmoothImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        //        //        setContentView(mSmoothImageView);
        //        //        mSmoothImageView.setTag(mImgUrl);
        //        Log.e("Tag", "url:" + mImgUrl);
        //        ImageLoader.getInstance().displayImage(mImgUrl, mSmoothImageView);
        //
        //
        //        mSmoothImageView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener()
        //        {
        //            @Override
        //            public void onViewTap(View view, float x, float y)
        //            {
        //                mSmoothImageView.setOnTransformListener(new SmoothImageView.TransformListener()
        //                {
        //                    @Override
        //                    public void onTransformComplete(int mode)
        //                    {
        //                        if (mode == 2)
        //                        {
        //                            finish();
        //                        }
        //                    }
        //                });
        //                mSmoothImageView.transformOut();
        //            }
        //        });


        mViewPager.setAdapter(new SamplePagerAdapter());
        mViewPager.setCurrentItem(startPosition);

        mBackIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        mDownloadIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (ConfigManager.instance().getVipLevelVIE() <= 0 && !ConfigManager.instance().getValid_vip())
                {
                    DialogUtils.showPromptDialog(SpaceImageDetailActivity.this, "VIP可下载", new MyItemClickListener()
                    {
                        @Override
                        public void onItemClick(View view, int position)
                        {

                        }
                    });
                    return;
                }
                DialogUtils.showToastDialog2Button(SpaceImageDetailActivity.this, "是否保存此图片至相册", new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        showProgressDialog();
                        new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {


                                try
                                {

                                    Bitmap mBitmap = getBitmap(picList.get(selectPosition));

                                    if (null != mBitmap)
                                    {
                                        saveImageToGallery(mBitmap);
                                    }
                                    else
                                    {
                                        Message msg = new Message();
                                        msg.obj = "图片保存失败！";
                                        msg.what = HIDE_PROGRESSDIALOG;
                                        mHandler.sendMessage(msg);
                                    }
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                    Message msg = new Message();
                                    msg.obj = "图片保存失败！";
                                    msg.what = HIDE_PROGRESSDIALOG;
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }).start();
                    }
                });
            }
        });

    }

    @Override
    protected void initEvent()
    {

    }

    @Override
    protected void initViewData()
    {

    }

//    @Override
//    public void onBackPressed()
//    {
//        //        mSmoothImageView.setOnTransformListener(new SmoothImageView.TransformListener()
//        //        {
//        //            @Override
//        //            public void onTransformComplete(int mode)
//        //            {
//        //                if (mode == 2)
//        //                {
//        //                    finish();
//        //                }
//        //            }
//        //        });
//        //        mSmoothImageView.transformOut();
//    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (isFinishing())
        {
            overridePendingTransition(0, 0);
        }
    }


    private void saveImageToGallery(Bitmap bmp)
    {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists())
        {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try
        {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
        Message msg = new Message();
        msg.obj = "图片保存成功！";
        msg.what = HIDE_PROGRESSDIALOG;
        mHandler.sendMessage(msg);
    }

    private Bitmap getBitmap(String path) throws IOException
    {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200)
        {
            InputStream inputStream = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }
        return null;

    }
}