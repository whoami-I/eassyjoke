package com.baimeng.library.ioc;

import android.app.Activity;
import android.view.View;

import com.baimeng.library.R;

/**
 * Created by Administrator on 2017/7/2.
 */

public class ViewFinder {
    private Activity mActivity;
    private View mView;

    public ViewFinder(View view) {
        this.mView = view;
    }

    public ViewFinder(Activity activity) {
        this.mActivity = activity;
    }

    public View findViewById(int id) {
        return mActivity != null ? mActivity.findViewById(id) : mView.findViewById(id);
    }
}
