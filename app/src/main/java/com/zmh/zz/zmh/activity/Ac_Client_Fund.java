package com.zmh.zz.zmh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zmh.zz.zmh.BaseActivity;
import com.zmh.zz.zmh.R;

/**
 * Created by Administrator
 * 客户资金
 */

public class Ac_Client_Fund extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mClient_top_up, mClient_bonus, mClient_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("客户资金");
        FindViewById();
    }
    @Override
    protected int getContentView() {
        return R.layout.ac_client_fund;
    }

    private void FindViewById() {
        mClient_top_up = (RelativeLayout) findViewById(R.id.client_top_up);
        mClient_bonus = (RelativeLayout) findViewById(R.id.client_bonus);
        mClient_sum = (RelativeLayout) findViewById(R.id.client_sum);
        mClient_top_up.setOnClickListener(this);
        mClient_bonus.setOnClickListener(this);
        mClient_sum.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.client_top_up:
                startActivity(new Intent(Ac_Client_Fund.this, Ac_Client_Top_Up.class));
                break;
            case R.id.client_bonus:
                startActivity(new Intent(Ac_Client_Fund.this, Ac_Client_Bonus.class));
                break;
            case R.id.client_sum:
                startActivity(new Intent(Ac_Client_Fund.this, Ac_Client_Sum.class));
                break;
        }
    }
}
