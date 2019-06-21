package com.baimeng.library.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/7/11.
 * Dialog的View辅助处理类
 */

class DialogViewHelper {
    private View mContentView = null;
    private SparseArray<WeakReference<View>> mViews;

    public DialogViewHelper(Context mContext, int mViewLayoutId) {
        mContentView = LayoutInflater.from(mContext).inflate(mViewLayoutId, null);
        mViews = new SparseArray<>();
    }

    public DialogViewHelper(View mView) {
        this.mContentView = mView;
        mViews = new SparseArray<>();
    }

    /**
     * 设置文本
     *
     * @param viewId
     * @param charSequence
     */
    public void setText(int viewId, CharSequence charSequence) {
        //减少findViewById的次数
        TextView tv = getView(viewId);
        if (tv != null) {
            tv.setText(charSequence);
        }
    }

    private <T extends View> T getView(int viewId) {
        WeakReference<View> viewWeakReference = mViews.get(viewId);
        View view = null;
        if (viewWeakReference != null) {
            view = viewWeakReference.get();
        }
        if (view == null) {
            view = mContentView.findViewById(viewId);
            if (view != null)
                mViews.put(viewId, new WeakReference<View>(view));
        }
        return (T) view;
    }

    /**
     * 设置点击事件
     *
     * @param viewId
     * @param listener
     */
    public void setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
    }

    public View getContentView() {
        return mContentView;
    }
}
