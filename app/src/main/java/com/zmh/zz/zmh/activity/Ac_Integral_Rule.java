package com.zmh.zz.zmh.activity;

import android.os.Bundle;

import com.zmh.zz.zmh.BaseActivity;
import com.zmh.zz.zmh.R;

/**
 * Created by Administrator
 * 积分规则
 */
public class Ac_Integral_Rule extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("积分说明");
        FindViewById();
    }

    @Override
    protected int getContentView() {
        return R.layout.ac_integral_rule;
    }

    private void FindViewById() {
    }


}
