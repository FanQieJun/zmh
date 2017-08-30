package com.zmh.zz.zmh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zmh.zz.zmh.R;
import com.zmh.zz.zmh.utlis.ToastUtils;

/**
 * Created by Administrator on .
 */

public class Ac_Im_Head_Portrait extends Activity implements View.OnClickListener {
    private RelativeLayout mPhotograph, mPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_im_head_portrait);
        mPhotograph = (RelativeLayout) findViewById(R.id.photograph);
        mPhoto = (RelativeLayout) findViewById(R.id.photo);
        mPhotograph.setOnClickListener(this);
        mPhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.photograph:
                ToastUtils.showToast(Ac_Im_Head_Portrait.this, "拍照");
                break;
            case R.id.photo:
                ToastUtils.showToast(Ac_Im_Head_Portrait.this, "相册");
                break;
        }
    }
}
