package com.baimeng.eassyjoke.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baimeng.eassyjoke.R;

/**
 * Created by Administrator on 2017/8/6.
 */

public class ItemFragment extends Fragment {
    public static ItemFragment newInstance(String item){
        ItemFragment itemFragment = new ItemFragment() ;
        Bundle bundle = new Bundle();
        bundle.putString("title",item);
        itemFragment.setArguments(bundle);
        return itemFragment ;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itme, null);
        TextView tv = (TextView) view.findViewById(R.id.text);
        Bundle bundle = getArguments() ;
        String title = bundle.getString("title");
        tv.setText(title);
        return view;
    }
}
