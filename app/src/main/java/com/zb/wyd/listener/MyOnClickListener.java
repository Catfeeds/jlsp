package com.zb.wyd.listener;

/**
 * DESC: 一句话简单描述
 */
public class MyOnClickListener
{
    public interface OnSuccessListener
    {
        public abstract void onSuccess();
    }


    public interface OnSubmitListener
    {
        public abstract void onSubmit(String content);
    }
    public interface OnFloatSubmitListener
    {
        public abstract void onSubmit(float content);
    }
    public interface OnCallBackListener
    {
        public abstract void onSubmit(int p, String content);
    }

    public interface OnEditCallBackListener
    {
        public abstract void onSubmit(String id, String content);
    }

    public interface OnClickCallBackListener
    {
        public abstract void onSubmit(int p, int i);
    }


}
