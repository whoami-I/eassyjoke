package com.baimeng.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import com.baimeng.library.R;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/7/11.
 */

public class AlertDialog extends Dialog {

    private AlertController mAlert;

    public AlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        mAlert = new AlertController(this, getWindow());
    }

    public static class Builder {
        private final AlertController.AlterParams P;

        public Builder(Context context) {
            this(context, R.style.dialog);
        }

        public Builder(Context context, int theme) {
            P = new AlertController.AlterParams(context, theme);
        }

        /**
         * Creates an {@link android.app.AlertDialog} with the arguments supplied to this
         * builder.
         * <p>
         * Calling this method does not display the dialog. If no additional
         * processing is needed, {@link #show()} may be called instead to both
         * create and display the dialog.
         */
        public AlertDialog create() {
            // Context has already been wrapped with the appropriate theme.
            final AlertDialog dialog = new AlertDialog(P.mContext, P.mTheme);
            P.apply(dialog.mAlert);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        /**
         * Creates an {@link android.app.AlertDialog} with the arguments supplied to this
         * builder and immediately displays the dialog.
         * <p>
         * Calling this method is functionally identical to:
         * <pre>
         *     AlertDialog dialog = builder.create();
         *     dialog.show();
         * </pre>
         */
        public AlertDialog show() {
            final AlertDialog dialog = create();
            dialog.show();
            return dialog;
        }

        /**
         * Set the title displayed in the {@link Dialog}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutId = 0;
            return this;
        }

        public Builder setContentView(int layoutId) {
            P.mView = null;
            P.mViewLayoutId = layoutId;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         * <p>
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(android.content.DialogInterface.OnDismissListener) setOnDismissListener}.</p>
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        //设置文本
        public Builder setText(int viewId, CharSequence text) {
            P.mTextArray.put(viewId, text);
            return this;
        }

        //设置点击事件
        public Builder setOnClickListener(int viewId, View.OnClickListener listener) {
            P.mClickArray.put(viewId, new WeakReference<View.OnClickListener>(listener));
            return this;
        }

        /**
         * 设置全屏
         *
         * @return
         */
        public Builder fullWidth() {
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        /**
         * 设置从底部弹出
         *
         * @param isAnimation
         * @return
         */
        public Builder fromBottom(boolean isAnimation) {
            if (isAnimation) {
                P.mAnimation = R.style.dialog_from_bottom_anim;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        /**
         * 设置Dialog的宽高
         *
         * @param width
         * @param height
         * @return
         */
        public Builder setWidthAndHeight(int width, int height) {
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        /**
         * 添加默认的动画
         *
         * @return
         */
        public Builder addDefaultAnimation() {
            P.mAnimation = R.style.dialog_from_bottom_anim;
            return this;
        }

        public Builder addAnimation(@StyleRes int styleAnimation) {
            P.mAnimation = styleAnimation;
            return this;
        }
    }
}
