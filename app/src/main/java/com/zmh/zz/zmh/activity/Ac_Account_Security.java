package com.zmh.zz.zmh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zmh.zz.zmh.BaseActivity;
import com.zmh.zz.zmh.R;


/**
 * Created by Administrator
 * 账号与安全
 */

public class Ac_Account_Security extends BaseActivity implements View.OnClickListener {
    private RelativeLayout mPhone_number, mMailbox, mChange_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("账号与安全");
        FindViewById();
    }
    @Override
    protected int getContentView() {
        return R.layout.ac_account_security;//任意非空布局
    }

    private void FindViewById() {
        mPhone_number = (RelativeLayout) findViewById(R.id.phone_number);
        mMailbox = (RelativeLayout) findViewById(R.id.mailbox);
        mChange_password = (RelativeLayout) findViewById(R.id.change_password);
        mPhone_number.setOnClickListener(this);
        mMailbox.setOnClickListener(this);
        mChange_password.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.phone_number:
                startActivity(new Intent(Ac_Account_Security.this, Ac_Verify_Phone_Number_Phone.class));
                break;
            case R.id.mailbox:
                startActivity(new Intent(Ac_Account_Security.this, Ac_Verify_Phone_Number_Mailbox.class));
                break;
            case R.id.change_password:
                startActivity(new Intent(Ac_Account_Security.this, Ac_Change_Password.class));
                break;
        }
    }
}
