package com.zb.wyd.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.adapter.VideoGridAdapter;
import com.zb.wyd.entity.LocalVideoModel;
import com.zb.wyd.utils.video.VideoUtil;
import com.zb.wyd.video.view.DividerGridItemDecoration;
import com.zb.wyd.widget.statusbar.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class VideoAlbumActivity extends BaseActivity implements VideoGridAdapter.OnItemClickListener
{

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private List<LocalVideoModel> mLocalVideoModels = new ArrayList<>();
    private VideoGridAdapter mAdapter;

    public static void startActivity(Context context)
    {
        Intent intent = new Intent(context, VideoAlbumActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initData()
    {
        VideoUtil.getLocalVideoFiles(this).subscribe(new Observer<ArrayList<LocalVideoModel>>()
        {
            @Override
            public void onSubscribe(Disposable d)
            {
                subscribe(d);
            }

            @Override
            public void onNext(ArrayList<LocalVideoModel> localVideoModels)
            {
                mLocalVideoModels = localVideoModels;
                mAdapter.setData(mLocalVideoModels);
            }

            @Override
            public void onError(Throwable e)
            {
                e.printStackTrace();
            }

            @Override
            public void onComplete()
            {

            }
        });
    }

    @Override
    protected void initViews(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_video_album);
        StatusBarUtil.setStatusBarBackground(this, R.drawable.main_bg);
        StatusBarUtil.StatusBarLightMode(VideoAlbumActivity.this, false);
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
    }

    @Override
    protected void initViewData()
    {
        tvTitle.setText("本地视频");
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));
        mAdapter = new VideoGridAdapter(this, mLocalVideoModels);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position, LocalVideoModel model)
    {
        Intent intent = new Intent(this, TrimVideoActivity.class);
        intent.putExtra("videoPath", model.getVideoPath());
        startActivityForResult(intent, 100);
        finish();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mLocalVideoModels = null;
    }


}
