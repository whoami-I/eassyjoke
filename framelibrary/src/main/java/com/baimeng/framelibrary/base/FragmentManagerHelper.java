package com.baimeng.framelibrary.base;

import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Created by Administrator on 2017/8/5.
 */

public class FragmentManagerHelper {
    //管理类FragmentManager
    private FragmentManager mFragmentManager ;
    //容器布局Id containerViewId ;
    private int mContainerViewId ;

    /**
     * 构造函数
     * @param fragmentManager 管理类FragmentManager
     * @param containerViewId 容器布局id containerViewId
     */
    public FragmentManagerHelper(@Nullable FragmentManager fragmentManager , @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager ;
        this.mContainerViewId = containerViewId ;
    }

    public void add(Fragment fragment){
        //开启事务
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        //第一个参数是fragment的容器如viewpager，第二个是需要添加的Fragment
        fragmentTransaction.add(mContainerViewId,fragment);
        //必须commit
        fragmentTransaction.commit() ;
    }

    /**
     * 切换显示Fragment
     * @param fragment  需要显示的Fragment
     */
    public void switchFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        for (Fragment childFragment : fragments) {
            fragmentTransaction.hide(childFragment);
        }
        //如果集合里没有这个fragemnt就添加，有就显示
        if(!fragments.contains(fragment)){
            fragmentTransaction.add(mContainerViewId,fragment);
        }else {
            fragmentTransaction.show(fragment);
        }
        //必须commit
        fragmentTransaction.commit() ;

    }

}
