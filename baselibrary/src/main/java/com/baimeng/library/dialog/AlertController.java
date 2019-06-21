package com.baimeng.library.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/11.
 */

class AlertController {
    private AlertDialog mAlertDialog;
    private Window mWindow;

    public AlertController(AlertDialog alertDialog, Window window) {
        this.mAlertDialog = alertDialog;
        this.mWindow = window;
    }

    public AlertDialog getmAlertDialog() {
        return mAlertDialog;
    }

    /**
     * 获取dialog的window
     *
     * @return
     */
    public Window getmWindow() {
        return mWindow;
    }

    public static class AlterParams {

        public Context mContext;
        public int mTheme;
        public boolean mCancelable = true; //点击空白是否能取消
        //Dialog取消监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //Dialog消失监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //Dialog按键监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        public View mView;
        public int mViewLayoutId;
        //存放字体的修改
        public SparseArray<CharSequence> mTextArray = new SparseArray<>();

        //存放点击事件
        public SparseArray<WeakReference<View.OnClickListener>> mClickArray = new SparseArray<>();
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        public int mAnimation;
        public int mGravity = Gravity.CENTER;
        public int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        public AlterParams(Context context, int theme) {
            this.mContext = context;
            this.mTheme = theme;
        }

        public void apply(AlertController mAlert) {
            //1.设置布局 DialogViewHelper
            DialogViewHelper viewHelper = null;
            if (mViewLayoutId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewLayoutId);
            }

            if (mView != null) {
                viewHelper = new DialogViewHelper(mView);
            }

            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView");
            }

            //给Dialog设置布局
            mAlert.getmAlertDialog().setContentView(viewHelper.getContentView());

            //2.设置文本
            for (int i = 0; i < mTextArray.size(); i++) {
                viewHelper.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }

            //3.设置点击
            for (int i = 0; i < mClickArray.size(); i++) {
                viewHelper.setOnClickListener(mClickArray.keyAt(i), mClickArray.valueAt(i).get());
            }

            //4.配置自定义效果  （全屏，从底部弹出，默认动画）
            Window window = mAlert.getmWindow();
            window.setGravity(mGravity);
            if (mAnimation != 0) {
                window.setWindowAnimations(mAnimation);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }
}
