package com.zb.wyd.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zb.wyd.R;
import com.zb.wyd.entity.PriceInfo;
import com.zb.wyd.listener.MyItemClickListener;
import com.zb.wyd.listener.MyOnClickListener;
import com.zb.wyd.widget.StarBar;

/**
 * 描述：一句话简单描述
 */
public class DialogUtils
{


    public static void showVersionUpdateDialog(Context mContext, String content, final
    MyOnClickListener.OnSubmitListener listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_version, null);
        dialog.setContentView(view);
        TextView mContent = (TextView) view.findViewById(R.id.tv_content);
        Button mSubmitBtn = (Button) view.findViewById(R.id.btn_submit);
        Button mCancelBtn = (Button) view.findViewById(R.id.btn_cancel);
        TextView mLinkTv = (TextView) view.findViewById(R.id.tv_link);
        mLinkTv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mLinkTv.getPaint().setAntiAlias(true);//抗锯齿


        mContent.setText(content);
        mSubmitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                dialog.dismiss();
                listener.onSubmit("1");
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onSubmit("2");
            }
        });

        mLinkTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onSubmit("3");
            }
        });

        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels * 0.75);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }


    /**
     * 提示框
     *
     * @return
     */
    public static void showPromptDialog(Context mContext, String title, final MyItemClickListener
            listener)
    {
        if (!((Activity) mContext).isFinishing())
        {
            final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
            dialog.setCancelable(true);
            View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_prompt, null);
            dialog.setContentView(v);
            ((TextView) v.findViewById(R.id.tv_content)).setText(title);
            v.findViewById(R.id.tv_submit).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onItemClick(v, 0);
                    dialog.dismiss();
                }
            });

            //Dialog部分
            Window mWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.width = APPUtils.getScreenWidth(mContext) * 3 / 4;
            mWindow.setAttributes(lp);
            dialog.setCancelable(false);
            dialog.show();
        }
    }


    public static Dialog createLoadingDialog(Context context, String msg)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading1, null);// 得到加载view
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_loading_view);// 加载布局
        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        tipTextView.setText(msg);// 设置加载信息

        Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        // 设置布局
        /**
         *将显示Dialog的方法封装在这里面
         */
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        return loadingDialog;
    }

    /**
     * 温馨提示
     *
     * @return
     */
    public static void showToastDialog2Button(Context mContext, String str, final View
            .OnClickListener onClickListener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_toast_2_button, null);
        dialog.setContentView(v);
        TextView mTitle = (TextView) v.findViewById(R.id.tv_title);
        mTitle.setText(str);
        ((RelativeLayout) v.findViewById(R.id.rl_confirm)).setOnClickListener(new View
                .OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });

        v.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * 温馨提示
     *
     * @return
     */
    public static Dialog showToastDialog2Button(Context mContext, String str, String buttonStr,
                                                final View.OnClickListener onClickListener, final
                                                View.OnClickListener onClickListener1)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_toast_2_button, null);
        dialog.setContentView(v);
        TextView mTitle = (TextView) v.findViewById(R.id.tv_title);
        mTitle.setText(str);

        TextView mSubmitTv = (TextView) v.findViewById(R.id.tv_submit);
        mSubmitTv.setText(buttonStr);
        ((RelativeLayout) v.findViewById(R.id.rl_confirm)).setOnClickListener(new View
                .OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                onClickListener.onClick(v);
            }
        });

        v.findViewById(R.id.rl_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                onClickListener1.onClick(v);
            }
        });
        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        return dialog;
    }


    /**
     * 温馨提示
     *
     * @return
     */
    public static Dialog showLivePriceDialog(Context mContext, PriceInfo mLivePriceInfo, final
    View.OnClickListener cancelListener, final MyOnClickListener.OnSubmitListener listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_live_price, null);
        dialog.setContentView(v);
        TextView priceTv = (TextView) v.findViewById(R.id.tv_price);
        TextView userMoneyTv = (TextView) v.findViewById(R.id.tv_user_money);
        TextView msgTv = (TextView) v.findViewById(R.id.tv_msg);
        ImageView mClosedIv = (ImageView) v.findViewById(R.id.iv_closed);
        TextView cancelTv = (TextView) v.findViewById(R.id.tv_cancel);
        TextView submitTv = (TextView) v.findViewById(R.id.tv_submit);
        Double userMoney = Double.parseDouble(mLivePriceInfo.getUser_money());
        int price = mLivePriceInfo.getOff_amount();
        if (TextUtils.isEmpty(mLivePriceInfo.getMsg()))
        {
            msgTv.setVisibility(View.GONE);
        }
        else
        {
            msgTv.setVisibility(View.VISIBLE);
            msgTv.setText(mLivePriceInfo.getMsg());
        }

        priceTv.setText(price + "积分");

        if (userMoney >= price)
        {
            userMoneyTv.setText("剩余积分:" + mLivePriceInfo.getUser_money());
            submitTv.setText("购买VIP");
            cancelTv.setText("积分兑换");
        }
        else
        {
            userMoneyTv.setText("您的积分不足!");
            submitTv.setText("购买VIP");
            cancelTv.setText("去做任务");
        }


        cancelTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                // cancelListener.onClick(v);
                if (userMoney >= price)
                {
                    listener.onSubmit("1");
                }
                else
                {
                    listener.onSubmit("2");
                }
            }
        });

        mClosedIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                cancelListener.onClick(v);
            }
        });
        submitTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onSubmit("3");
            }
        });


        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        return dialog;
    }


    /**
     * 温馨提示
     *
     * @return
     */
    public static Dialog showVideoPriceDialog(Context mContext, PriceInfo mLivePriceInfo, final
    View.OnClickListener cancelListener, final MyOnClickListener.OnSubmitListener listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_live_price, null);
        dialog.setContentView(v);
        TextView priceTv = (TextView) v.findViewById(R.id.tv_price);
        TextView userMoneyTv = (TextView) v.findViewById(R.id.tv_user_money);
        TextView msgTv = (TextView) v.findViewById(R.id.tv_msg);
        ImageView mClosedIv = (ImageView) v.findViewById(R.id.iv_closed);
        TextView cancelTv = (TextView) v.findViewById(R.id.tv_cancel);
        TextView submitTv = (TextView) v.findViewById(R.id.tv_submit);
        Double userMoney = Double.parseDouble(mLivePriceInfo.getUser_money());
        int price = mLivePriceInfo.getOff_amount();
        if (TextUtils.isEmpty(mLivePriceInfo.getMsg()))
        {
            msgTv.setVisibility(View.GONE);
        }
        else
        {
            msgTv.setVisibility(View.VISIBLE);
            msgTv.setText(mLivePriceInfo.getMsg());
        }

        priceTv.setText(price + "积分");

        if (userMoney >= price)
        {
            userMoneyTv.setText("剩余积分:" + mLivePriceInfo.getUser_money());
            submitTv.setText("购买VIP");
            cancelTv.setText("积分兑换");
        }
        else
        {
            userMoneyTv.setText("您的积分不足!");
            submitTv.setText("购买VIP");
            cancelTv.setText("去做任务");
        }


        cancelTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                // cancelListener.onClick(v);
                if (userMoney >= price)
                {
                    listener.onSubmit("1");
                }
                else
                {
                    listener.onSubmit("2");
                }
            }
        });

        mClosedIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                cancelListener.onClick(v);
            }
        });
        submitTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onSubmit("3");
            }
        });


        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 1 / 2;
        mWindow.setAttributes(lp);
        return dialog;
    }

    /**
     * 提示框
     *
     * @return
     */
    public static void showChannelDialog(Context mContext, final MyItemClickListener listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(true);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_change_channel, null);
        dialog.setContentView(v);


        Button btnYd = (Button) v.findViewById(R.id.btn_yd);
        Button btnDx = (Button) v.findViewById(R.id.btn_dx);
        Button btnCm = (Button) v.findViewById(R.id.btn_cm);
        Button btnOut = (Button) v.findViewById(R.id.btn_out);

        btnYd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onItemClick(v, 1);
            }
        });


        btnDx.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onItemClick(v, 2);
            }
        });


        btnCm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onItemClick(v, 3);
            }
        });


        btnOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onItemClick(v, 4);
            }
        });


        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 1 / 2;
        mWindow.setAttributes(lp);
        dialog.setCancelable(false);
        dialog.show();
    }


    /**
     * 温馨提示
     *
     * @return
     */
    public static void showTaskDialog(Context mContext, String title, String desc, String task)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_task, null);
        dialog.setContentView(v);
        TextView mTitleTv = (TextView) v.findViewById(R.id.tv_title);
        TextView mDescTv = (TextView) v.findViewById(R.id.tv_desc);
        TextView mTaskTv = (TextView) v.findViewById(R.id.tv_task);
        Button mSubmitBtn = (Button) v.findViewById(R.id.btn_submit);
        mTitleTv.setText(title);
        mDescTv.setText(desc);
        mTaskTv.setText(task);
        mSubmitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });


        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * 温馨提示
     *
     * @return
     */
    public static void showReportDialog(Context mContext, MyOnClickListener.OnSubmitListener
            listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_repot_msg, null);
        dialog.setContentView(v);
        v.findViewById(R.id.tv_msg1).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onSubmit("1");
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.tv_msg2).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onSubmit("2");
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.tv_msg3).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onSubmit("3");
                dialog.dismiss();
            }
        });
        v.findViewById(R.id.tv_msg4).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listener.onSubmit("4");
                dialog.dismiss();
            }
        });

        v.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });


        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 3 / 5;
        mWindow.setAttributes(lp);
        dialog.show();
    }

    /**
     * 温馨提示
     *
     * @return
     */
    public static Dialog showDyTipsDialog(Context mContext, final View.OnClickListener
            buyListener, final View.OnClickListener memberListener, final View.OnClickListener
            closedListener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_dy_live_price, null);
        dialog.setContentView(v);

        TextView cancelTv = (TextView) v.findViewById(R.id.tv_cancel);
        TextView submitTv = (TextView) v.findViewById(R.id.tv_submit);
        ImageView closedIv = (ImageView) v.findViewById(R.id.iv_closed);
        cancelTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                buyListener.onClick(v);
            }
        });

        submitTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                memberListener.onClick(v);
            }
        });


        closedIv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
                closedListener.onClick(v);

            }
        });

        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        return dialog;
    }


    /**
     * 温馨提示
     *
     * @return
     */
    public static void showScoreDialog(Context mContext, String title, String content,
                                         MyOnClickListener.OnFloatSubmitListener listener)
    {
        final Dialog dialog = new Dialog(mContext, R.style.dialogNoAnimation);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_score, null);
        dialog.setContentView(v);

        TextView cancelTv = (TextView) v.findViewById(R.id.tv_cancel);
        TextView submitTv = (TextView) v.findViewById(R.id.tv_submit);
        TextView titleTv = (TextView) v.findViewById(R.id.tv_title);
        TextView contentTv = (TextView) v.findViewById(R.id.tv_content);
        StarBar mStarBar = (StarBar) v.findViewById(R.id.starBar);


        titleTv.setText(title);
        contentTv.setText(content);
        cancelTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        submitTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                listener.onSubmit(mStarBar.getStarMark());

            }
        });


        //Dialog部分
        Window mWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.gravity = Gravity.CENTER;
        lp.width = APPUtils.getScreenWidth(mContext) * 7 / 8;
        mWindow.setAttributes(lp);
        dialog.show();
    }


}
