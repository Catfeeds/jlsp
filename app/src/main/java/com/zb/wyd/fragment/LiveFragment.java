package com.zb.wyd.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.activity.SearchActivity;
import com.zb.wyd.adapter.MyViewPagerAdapter;
import com.zb.wyd.widget.AutoFitTextView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zb.wyd.utils.APPUtils.dip2px;

/**
 * 描述：一句话简单描述
 */
public class LiveFragment extends BaseFragment
{

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    private View rootView = null;
    private Unbinder   unbinder;
    private ViewHolder holder;
    private List<String> tabs = new ArrayList<>(); //标签名称

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (rootView == null)
        {
            rootView = inflater.inflate(R.layout.fragment_live, null);
            unbinder = ButterKnife.bind(this, rootView);
            initData();
            initViews();
            initViewData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null)
        {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    protected void initData()
    {
        tabs.add("直播首页");
        tabs.add("主播列表");
        tabs.add("成人抖音");
    }

    @Override
    protected void initViews()
    {

    }

    @Override
    protected void initEvent()
    {
        ivSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
    }

    @Override
    protected void initViewData()
    {
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(getChildFragmentManager());
//        viewPagerAdapter.addFragment(LiveIndexFragment.newInstance(), "直播首页");//添加Fragment
        viewPagerAdapter.addFragment(AnchorListFragment.newInstance(), "主播列表");
        viewPagerAdapter.addFragment(DouyinFragment.newInstance(), "成人抖音");
        mViewPager.setAdapter(viewPagerAdapter);//设置适配器
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.addTab(mTabLayout.newTab().setText("直播首页"));//给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText("主播列表"));
        mTabLayout.addTab(mTabLayout.newTab().setText("成人抖音"));
        mTabLayout.setupWithViewPager(mViewPager);//给TabLayout设置关联ViewPager，如果设置了ViewPager，那么ViewPagerAdapter中的getPageTitle()方法返回的就是Tab上的标题
        setTabView();
        reflex(mTabLayout);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                holder = new ViewHolder(tab.getCustomView());
                holder.tvTabName.setSelected(true);
                //选中后字体变大
                holder.tvTabName.setTextSize(18);
                TextPaint tp = holder.tvTabName.getPaint();
                tp.setFakeBoldText(true);
                //让Viewpager跟随TabLayout的标签切换
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                holder = new ViewHolder(tab.getCustomView());
                holder.tvTabName.setSelected(false);
                //恢复为默认字体大小
                holder.tvTabName.setTextSize(16);
                TextPaint tp = holder.tvTabName.getPaint();
                tp.setFakeBoldText(false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });


    }

    /**
     * 设置Tab的样式
     */
    private void setTabView()
    {
        holder = null;
        for (int i = 0; i < 3; i++)
        {
            //依次获取标签
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            //为每个标签设置布局
            tab.setCustomView(R.layout.item_tab);
            holder = new ViewHolder(tab.getCustomView());
            //为标签填充数据
            holder.tvTabName.setText(tabs.get(i));
            //默认选择第一项
            if (i == 0)
            {
                holder.tvTabName.setSelected(true);
                holder.tvTabName.setTextSize(18);
                TextPaint tp = holder.tvTabName.getPaint();
                tp.setFakeBoldText(true);
            }
        }
    }

    public void setTabIndex(int p)
    {
        mViewPager.setCurrentItem(p);
    }


    class ViewHolder
    {
        AutoFitTextView tvTabName;

        public ViewHolder(View tabView)
        {
            tvTabName = (AutoFitTextView) tabView.findViewById(R.id.tv_tab_name);
        }
    }


    public void reflex(final TabLayout tabLayout)
    {
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dip2px(tabLayout.getContext(), 10);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++)
                    {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0)
                        {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e)
                {
                    e.printStackTrace();
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        });

    }

//    @Override
//    public void onDestroy()
//    {
//        super.onDestroy();
//        if (null != unbinder)
//        {
//            unbinder.unbind();
//            unbinder = null;
//        }
//    }
}
