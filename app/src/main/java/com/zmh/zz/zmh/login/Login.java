package com.zmh.zz.zmh.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.zmh.zz.zmh.LoadingDialog.ShapeLoadingDialog;
import com.zmh.zz.zmh.MainActivity;
import com.zmh.zz.zmh.R;
import com.zmh.zz.zmh.httpurls.HttpURLs;
import com.zmh.zz.zmh.modeljson.LoginJson;
import com.zmh.zz.zmh.utils.MD5Util;
import com.zmh.zz.zmh.utils.MyStringCallBack;
import com.zmh.zz.zmh.utils.OkHttpUtil;
import com.zmh.zz.zmh.utils.RegularUtil;
import com.zmh.zz.zmh.utils.SharedPreferencesUtil;
import com.zmh.zz.zmh.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by Administrator
 * 登录
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences sp;
    private OkHttpUtil okHttp = new OkHttpUtil();
    private String mUserNameValue, mPasswordValue;
    private TextView Tv_TitleName;
    private EditText Et_UserName, Et_Password;// 账号,密码
    private Button But_Login, But_ForgetPassword, But_Register;//登录,忘记密码,注册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransparentTitleBar();
        setContentView(R.layout.ac_login);
        Tv_TitleName = (TextView) findViewById(R.id.tv_title_name);
        Tv_TitleName.setText("登录");
        TextPaint tp = Tv_TitleName.getPaint();
        tp.setFakeBoldText(true);
        Et_UserName = (EditText) findViewById(R.id.et_username);
        Et_Password = (EditText) findViewById(R.id.et_password);
        But_Login = (Button) findViewById(R.id.but_login);
        But_ForgetPassword = (Button) findViewById(R.id.but_forget_password);
        But_Register = (Button) findViewById(R.id.but_register);
        Et_UserName.setFilters(new InputFilter[]{RegularUtil.filter});
        Et_Password.setFilters(new InputFilter[]{RegularUtil.filter});
        But_Login.setOnClickListener(this);
        But_ForgetPassword.setOnClickListener(this);
        But_Register.setOnClickListener(this);
        //第二次无需输入账号和密码直接登录
        sp = getSharedPreferences("UserInfo", 0);
        String name = sp.getString("USERNAME", "");
        String pass = sp.getString("PASSWORD", "");
        Et_UserName.setText(name);
        Et_Password.setText(pass);
        if (!name.equals("") && !pass.equals("")) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        mUserNameValue = Et_UserName.getText().toString();
        mPasswordValue = Et_Password.getText().toString();
        switch (v.getId()) {
            case R.id.but_login:
//                startActivity(new Intent(Login.this, MainActivity.class));
//                finish();
                if (mUserNameValue.equals("")) {
                    ToastUtils.showToast(Login.this, "账号不能为空");
                } else if (mPasswordValue.equals("")) {
                    ToastUtils.showToast(Login.this, "密码不能为空");
                } else {
                    Login();// 登录
                }
                break;
            case R.id.but_forget_password:
                startActivity(new Intent(Login.this, ForgetPassword.class));//忘记密码
                break;
            case R.id.but_register:
                startActivity(new Intent(Login.this, Register.class));//注册
                break;
        }
    }

    /**
     * 登录
     */
    private void Login() {
        final ShapeLoadingDialog shapeLoadingDialog = new ShapeLoadingDialog(Login.this);
        shapeLoadingDialog.setCancelable(false);
        shapeLoadingDialog.setLoadingText("登录中,请稍等...");
        shapeLoadingDialog.show();
        mUserNameValue = Et_UserName.getText().toString();
        mPasswordValue = Et_Password.getText().toString();
        final SharedPreferences.Editor editor = sp.edit();
        String url = HttpURLs.LOGIN;
        Map<String, String> params = new HashMap<>();
        params.put("loginname", mUserNameValue);
        params.put("password", MD5Util.MD5(mPasswordValue, 32));
        okHttp.postRequest(url, params, new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                Log.e("sssss>>>", response);
//              JsonResult<JSONObject> jsonRet = new JsonResult<>();
//              JSONObject Result = jsonRet.getData();
//              String email = Result.getString("email");
//              JSONObject jsonObject = new JSONObject(response);
//              String resultCode = jsonObject.getString("resultCode");
                LoginJson login = JSONObject.parseObject(response, LoginJson.class);
                int code = login.getCode();
                String desc = login.getDesc();//Token值
                switch (code) {
                    case 200:
                        shapeLoadingDialog.dismiss();
                        ToastUtils.showToast(Login.this, "登录成功");
                        SharedPreferencesUtil.setParam(Login.this, "Token", desc);
                        //保存用户名和密码
                        editor.putString("USERNAME", mUserNameValue);
                        editor.putString("PASSWORD", mPasswordValue);
                        editor.commit();
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                        break;
                    case 400:
                        shapeLoadingDialog.dismiss();
                        ToastUtils.showToast(Login.this, desc);
                        break;
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                shapeLoadingDialog.dismiss();
                Toast.makeText(Login.this, R.string.ConnectionError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("提示！")
                        .setMessage("确定退出程序？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                break;
        }
        return false;
    }

    /**
     * 透明标题栏
     */
    private void TransparentTitleBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
}