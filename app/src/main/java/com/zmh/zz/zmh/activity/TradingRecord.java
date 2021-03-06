package com.zmh.zz.zmh.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.zmh.zz.zmh.BaseActivity;
import com.zmh.zz.zmh.R;
import com.zmh.zz.zmh.adapter.TradingRecordAdapter;
import com.zmh.zz.zmh.modelinfo.TradingRecordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator
 * 交易记录
 */

public class TradingRecord extends BaseActivity {
    private RecyclerView mRv_trading_record;
    private TradingRecordAdapter tradingRecordAdapter;
    private List<TradingRecordInfo> tradingrecordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLtTitle("交易记录");
        FindViewById();
        initData();
    }

    @Override
    protected int getContentView() {
        return R.layout.ac_trading_record;
    }

    private void FindViewById() {
    }

    private void initData() {
        mRv_trading_record = (RecyclerView) findViewById(R.id.rv_trading_record);
        tradingrecordList = new ArrayList<>();
        tradingRecordAdapter = new TradingRecordAdapter(tradingrecordList, TradingRecord.this);
        mRv_trading_record.setLayoutManager(new LinearLayoutManager(this));
        mRv_trading_record.setAdapter(tradingRecordAdapter);
        tradingRecordAdapter.notifyDataSetChanged();
    }


}
