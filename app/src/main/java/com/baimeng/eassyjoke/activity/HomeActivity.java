package com.baimeng.eassyjoke.activity;

import android.app.NativeActivity;
import android.app.VoiceInteractor;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.RadioGroup;

import com.baimeng.eassyjoke.R;
import com.baimeng.eassyjoke.fragment.FindFragment;
import com.baimeng.eassyjoke.fragment.HomeFragment;
import com.baimeng.eassyjoke.fragment.MessageFragment;
import com.baimeng.eassyjoke.fragment.NewFragment;
import com.baimeng.eassyjoke.service.MessageService;
import com.baimeng.framelibrary.base.BaseSkinActivity;
import com.baimeng.framelibrary.base.DefaultNavigationBar;
import com.baimeng.framelibrary.base.FragmentManagerHelper;
import com.baimeng.framelibrary.skin.SkinResources;
import com.baimeng.library.ioc.OnClick;

import static android.R.attr.handle;

/**
 * Created by Administrator on 2017/8/5.
 */

public class HomeActivity extends BaseSkinActivity {

    private HomeFragment mHomeFragment;
    private FindFragment mFindFragment;
    private MessageFragment mMessageFragment;
    private NewFragment mNewFragment;
    private FragmentManagerHelper mFragmentHelper;


    @Override
    public void changeSkin(SkinResources skinResources) {

    }

    @Override
    protected void initData() {
        mFragmentHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.main_tab_fl);
        mHomeFragment = new HomeFragment();
        mFragmentHelper.add(mHomeFragment);
    }

    @Override
    protected void initView() {

        findViewById(R.id.home_rb).getBackground().setState(
                new int[]{android.R.attr.state_pressed});
        //findViewById(R.id.home_rb).setSelected(true);
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar =
                new DefaultNavigationBar.Builder(this)
                        .setTitle("首页")
                        .hideLeftIcon()
                        .build();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @OnClick(R.id.home_rb)
    private void homeRbClick(View v) {
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
        }
        mFragmentHelper.switchFragment(mHomeFragment);


    }

    @OnClick(R.id.find_rb)
    private void findRbClick(View v) {
        if (mFindFragment == null) {
            mFindFragment = new FindFragment();
        }
        mFragmentHelper.switchFragment(mFindFragment);
    }

    @OnClick(R.id.new_rb)
    private void newRbClick(View v) {
        if (mNewFragment == null) {
            mNewFragment = new NewFragment();
        }
        mFragmentHelper.switchFragment(mNewFragment);
    }

    @OnClick(R.id.message_rb)
    private void messageRbClick(View v) {
        if (mMessageFragment == null) {
            mMessageFragment = new MessageFragment();
        }
        mFragmentHelper.switchFragment(mMessageFragment);
    }
}
