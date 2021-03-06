package com.phonemate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.phonemate.R;
import com.phonemate.adapter.PanelSettingBgAdapter;
import com.phonemate.base.BaseFragment;
import com.phonemate.global.GlobalUtils;
import com.phonemate.model.PanelBgColorEntity;
import com.phonemate.utils.SettingUtils;
import com.phonemate.utils.WindowUtils;
import com.rxx.fast.adapter.BaseRecyclerView;
import com.rxx.fast.view.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：PhoneMate
 * 类描述：
 * 创建人：colorful
 * 创建时间：15/11/18 15:56
 * 修改人：colorful
 * 修改时间：15/11/18 15:56
 * 修改备注：
 */
public class PanelSettingBgFragment extends BaseFragment implements BaseRecyclerView.OnItemClickListener,SeekBar.OnSeekBarChangeListener {

    @ViewInject(id = R.id.mLayoutPanel)
    private LinearLayout mLayoutPanel;


    @ViewInject(id = R.id.mRecyclerView)
    private RecyclerView mRecyclerView;

    @ViewInject(id = R.id.mTvSize)
    TextView mTvSize;

    @ViewInject(id = R.id.mTvRadius)
    TextView mTvRadius;

    @ViewInject(id = R.id.mTvTrans)
    TextView mTvTrans;

    @ViewInject(id = R.id.mSbRadius)
    SeekBar mSbRadius;

    @ViewInject(id = R.id.mSbSize)
    SeekBar mSbSize;

    @ViewInject(id = R.id.mSbTrans)
    SeekBar mSbTrans;

    private List<PanelBgColorEntity> mList;
    private PanelSettingBgAdapter mAdapter;
    private Intent mIntentUpdateBroadCastReceiver;

    @Override
    public View init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pannelsetting_bg, container, false);
    }

    @Override
    public void bingViewFinish() {
        ViewGroup.LayoutParams layout = mLayoutPanel.getLayoutParams();
        layout.width = (int) (WindowUtils.getWIndowWidth(mActivity) * 0.8);
        layout.height = (int) (WindowUtils.getWIndowWidth(mActivity) * 0.8);

        mIntentUpdateBroadCastReceiver=new Intent(GlobalUtils.BROADCAST_RECEIVER_UPDATE_PANEL_VIEW);

        mSbTrans.setOnSeekBarChangeListener(this);
        mSbRadius.setOnSeekBarChangeListener(this);
        mSbSize.setOnSeekBarChangeListener(this);

        mSbTrans.setProgress((int) (SettingUtils.getPanelTrans(mActivity) * 50));
        mSbRadius.setProgress((int) (SettingUtils.getPanelRadius(mActivity)*50));
        mSbSize.setProgress((int) (SettingUtils.getPanelSize(mActivity)*50));

        mList = new ArrayList<>();
        for (int i = 0; i < PanelBgColorEntity.mMaxNum; i++) {
            PanelBgColorEntity entity = new PanelBgColorEntity();
            if (i == SettingUtils.getPanelColor(mActivity)) {
                entity.isSelect = true;
            } else {
                entity.isSelect = false;
            }
            mList.add(entity);
        }
        mAdapter = new PanelSettingBgAdapter(mList, mActivity);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(RecyclerView recyclerView, View view, int position, Object o) {
        for (PanelBgColorEntity entity : mList) {
            entity.isSelect = false;
        }
        mList.get(position).isSelect = true;
        SettingUtils.setPanelColor(mActivity, position);
        mAdapter.notifyDataSetChanged();
        mActivity.sendBroadcast(mIntentUpdateBroadCastReceiver);
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(seekBar==mSbRadius){
            if(progress>=50) {
                mTvRadius.setText(progress*2 + "%");
                SettingUtils.setPanelRadius(mActivity, progress / 50f);
            }else{
                mTvRadius.setText((progress-100)*2 + "%");
                SettingUtils.setPanelRadius(mActivity, progress / 50f);
            }
        }else if(seekBar==mSbSize){
            if(progress>=50) {
                mTvSize.setText(progress * 2 + "%");
                SettingUtils.setPanelSize(mActivity, progress / 50f);
            }else{
                mTvSize.setText((progress-100)*2 + "%");
                SettingUtils.setPanelSize(mActivity, progress / 50f);
            }
        }else if(seekBar==mSbTrans){
            if(progress>=50) {
                mTvTrans.setText(progress*2 + "%");
                SettingUtils.setPanelTrans(mActivity, (progress) / 50f);
            }else{
                mTvTrans.setText((progress-100)*2 + "%");
                SettingUtils.setPanelTrans(mActivity, progress / 50f);
            }
        }
        mActivity.sendBroadcast(mIntentUpdateBroadCastReceiver);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
