package com.zmh.zz.zmh.mainfragment;

import android.view.View;

import com.zmh.zz.zmh.BaseFragment;
import com.zmh.zz.zmh.R;

/**
 * 业务
 */
public class FragmentBusiness extends BaseFragment {
    @Override
    public View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_business, null);
        setNotificationBar(R.color.babyblue);
        initData();
        return view;
    }

    public void initData() {
    }
}