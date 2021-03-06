package com.zmh.zz.zmh.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.zmh.zz.zmh.BaseActivity;
import com.zmh.zz.zmh.LoadingDialog.ShapeLoadingDialog;
import com.zmh.zz.zmh.R;
import com.zmh.zz.zmh.httpurls.HttpURLs;
import com.zmh.zz.zmh.modeljson.LoginJson;
import com.zmh.zz.zmh.uploaImage.GlideImageLoader;
import com.zmh.zz.zmh.uploaImage.ImagePickerAdapter;
import com.zmh.zz.zmh.uploaImage.SelectPortraitDialog;
import com.zmh.zz.zmh.utils.Base64Util;
import com.zmh.zz.zmh.utils.MyStringCallBack;
import com.zmh.zz.zmh.utils.OkHttpUtil;
import com.zmh.zz.zmh.utils.RegularUtil;
import com.zmh.zz.zmh.utils.SharedPreferencesUtil;
import com.zmh.zz.zmh.utils.ToastUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;


/**
 * Created by Administrator
 * 实名认证_false
 */

public class RealNameAuthenticationFalse extends BaseActivity implements View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener {
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private ImagePickerAdapter adapter;
    private ArrayList<ImageItem> selImageList; //当前选择的所有图片
    private int maxImgCount = 3;               //允许选择图片最大数

    private RelativeLayout Rl_CertificateEffective;
    private TextView Tv_MenAndWomen;
    private EditText Et_Name, Et_IdNumber, Et_Date;
    private int which = 0;
    private OkHttpUtil okHttp = new OkHttpUtil();
    private String mNameValue, mMenAndWomenValue, mIdNumberValue, mDateValue, UserName, UserID;
    DateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd");
    //获取一个日历对象
    Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
    //当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //修改日历控件的年，月，日
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //将页面TextView的显示更新为最新时间
            Et_Date.setText(fmtDate.format(dateAndTime.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLtTitle("实名认证");
        setRtTitle("提交");
        setRightBtnVisible(true);
        FindViewById();
        initImagePicker();
        initWidget();
    }


    @Override
    protected int getContentView() {
        return R.layout.ac_real_name_authentication_false;
    }

    private void FindViewById() {
        Rl_CertificateEffective = (RelativeLayout) findViewById(R.id.rl_certificate_effective);
        Et_Name = (EditText) findViewById(R.id.et_name);
        Et_IdNumber = (EditText) findViewById(R.id.et_id_number);
        Et_Date = (EditText) findViewById(R.id.et_date);
        Tv_MenAndWomen = (TextView) findViewById(R.id.tv_men_and_women);
        Et_Name.setFilters(new InputFilter[]{RegularUtil.filter});
        Et_IdNumber.setFilters(new InputFilter[]{RegularUtil.filter});
        Tv_MenAndWomen.setOnClickListener(this);
        Rl_CertificateEffective.setOnClickListener(this);
    }

    //右键点击
    @Override
    protected void onClickRight() {
        mNameValue = Et_Name.getText().toString();
        mMenAndWomenValue = Tv_MenAndWomen.getText().toString();
        mIdNumberValue = Et_IdNumber.getText().toString();
        mDateValue = Et_Date.getText().toString();
        if (mNameValue.equals("")) {
            ToastUtils.showToast(RealNameAuthenticationFalse.this, "姓名不能为空");
        } else if (mMenAndWomenValue.equals("")) {
            ToastUtils.showToast(RealNameAuthenticationFalse.this, "性别不能为空");
        } else if (mIdNumberValue.equals("")) {
            ToastUtils.showToast(RealNameAuthenticationFalse.this, "身份证号不能为空");
        } else if (!RegularUtil.isIDCard(mIdNumberValue)) {
            ToastUtils.showToast(RealNameAuthenticationFalse.this, "身份证号格式不正确");
        } else if (mDateValue.equals("")) {
            ToastUtils.showToast(RealNameAuthenticationFalse.this, "证件有效期不能为空");
        } else if (selImageList.size() < maxImgCount) {
            ToastUtils.showToast(RealNameAuthenticationFalse.this, "照片不能少于3张");
        } else {
            Submit();//提交
        }
    }

    private void Submit() {
        final ShapeLoadingDialog shapeLoadingDialog = new ShapeLoadingDialog(RealNameAuthenticationFalse.this);
        shapeLoadingDialog.setCancelable(false);
        shapeLoadingDialog.setLoadingText("提交中,请稍等...");
        shapeLoadingDialog.show();
        String Token = (String) SharedPreferencesUtil.getParam(RealNameAuthenticationFalse.this, "Token", "");
        mNameValue = Et_Name.getText().toString();
        mMenAndWomenValue = Tv_MenAndWomen.getText().toString();
        mIdNumberValue = Et_IdNumber.getText().toString();
        mDateValue = Et_Date.getText().toString();
        String TypeBase64 = "";
        for (int i = 0; i < selImageList.size(); i++) {
            String Path = selImageList.get(i).path;
            String imgBase64 = Base64Util.imageToBase64(Path);
            String imgType = Path.substring(Path.length() - 3);
            if (selImageList.size() - 1 == i) {
                TypeBase64 += imgType + " " + imgBase64;
            } else {
                TypeBase64 += imgType + " " + imgBase64 + "#";
            }
        }
        Log.e("s>>>", TypeBase64);
        String url = HttpURLs.USERIDENTITY;
        Map<String, String> params = new HashMap<>();
        params.put("token", Token);
        params.put("name", mNameValue);
        if (mMenAndWomenValue.equals("男")) {
            which = 1;
        } else if (mMenAndWomenValue.equals("女")) {
            which = 2;
        }
        params.put("gender", which + "");
        params.put("cardValidTime", mDateValue);
        params.put("cardNo", mIdNumberValue);
        params.put("imgStr", TypeBase64);
        okHttp.postRequest(url, params, new MyStringCallBack() {
            @Override
            public void onResponse(String response, int id) {
                Log.e("sssss>>>", response);
                LoginJson login = JSONObject.parseObject(response, LoginJson.class);
                int code = login.getCode();
                String desc = login.getDesc();
                switch (code) {
                    case 200:
                        ToastUtils.showToast(RealNameAuthenticationFalse.this, "添加成功");
                        shapeLoadingDialog.dismiss();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(RealNameAuthenticationFalse.this);
                        dialog.setTitle("提示")
                                .setMessage("\r\r\r\r\r\r\r\r您的资料已提交,请耐心等待,我们将在1-2个工作日人审核完成。")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {// 确定按钮的响应事件
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).setCancelable(false).show();

                        break;
                    case 400:
                        shapeLoadingDialog.dismiss();
                        ToastUtils.showToast(RealNameAuthenticationFalse.this, desc);
                        break;
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                shapeLoadingDialog.dismiss();
                Toast.makeText(RealNameAuthenticationFalse.this, R.string.ConnectionError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_men_and_women:
                AlertDialog.Builder dialog = new AlertDialog.Builder(RealNameAuthenticationFalse.this);
                dialog.setTitle("性别");
                if (Tv_MenAndWomen.getText().toString().equals("男")) {
                    which = 0;
                } else if (Tv_MenAndWomen.getText().toString().equals("女")) {
                    which = 1;
                }
                dialog.setSingleChoiceItems(new String[]{"男", "女"}, which, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Tv_MenAndWomen.setText("男");
                        } else if (which == 1) {
                            Tv_MenAndWomen.setText("女");
                        }
                        if (!Tv_MenAndWomen.getText().toString().equals("请选择")) {
                            Tv_MenAndWomen.setTextColor(getResources().getColor(R.color.absolute_black));//通过获得资源文件进行设置。
                        }
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.rl_certificate_effective:
                DatePickerDialog dateDlg = new DatePickerDialog(RealNameAuthenticationFalse.this, date,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH));
                dateDlg.show();
                break;
        }
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(false);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setMultiMode(true);                       //多选
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);
    }

    private void initWidget() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_add_photo);
        selImageList = new ArrayList<>();
        adapter = new ImagePickerAdapter(this, selImageList, maxImgCount);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));//一行显示个数
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                final SelectPortraitDialog selectDialog = new SelectPortraitDialog(this);
                selectDialog.show();
                selectDialog.setClicklistener(new SelectPortraitDialog.ClickListenerInterface() {
                    @Override
                    public void photograph() {
                        //相机
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                        Intent intent = new Intent(RealNameAuthenticationFalse.this, ImageGridActivity.class);
                        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        selectDialog.dismiss();
                    }

                    @Override
                    public void photo() {
                        //相册
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - selImageList.size());
                        Intent intent = new Intent(RealNameAuthenticationFalse.this, ImageGridActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        selectDialog.dismiss();
                    }
                });
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) adapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null) {
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
                    selImageList.clear();
                    selImageList.addAll(images);
                    adapter.setImages(selImageList);
                }
            }
        }
    }
}
