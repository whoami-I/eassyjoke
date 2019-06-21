package com.baimeng.library.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baimeng.library.ioc.ViewUtils;

/**
 * Created by Administrator on 2017/8/5.
 */

public abstract class BaseFragment extends Fragment {
    public Context mContext ;
    public View rootView ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mContext = getActivity();
        rootView = View.inflate(mContext,getLayoutId(),null);
        ViewUtils.inject(rootView,this);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void initListener();

    public abstract int getLayoutId() ;
}
