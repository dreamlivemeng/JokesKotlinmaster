package com.dreamlive.jokes.fragment;

import com.chad.library.adapter.base.BaseViewHolder;
import com.dreamlive.jokes.adapter.QuickAdapter;
import com.dreamlive.jokes.entity.AndroidAricle;
import com.dreamlive.jokes.entity.JokeContent;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dreamlive.jokes.R;

import java.util.ArrayList;

/**
 * Created by dreamlive on 2016/7/27.
 */
public class JokeFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    List<AndroidAricle> mJokeData;
    BaseQuickAdapter<AndroidAricle, BaseViewHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.content_main, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.main_srl);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_rv);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mJokeData = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mJokeData.add(new AndroidAricle("这是第" + i + "个item"));
        }
  /*      mAdapter = new BaseQuickAdapter<AndroidAricle, BaseViewHolder>(R.layout.item_joke_card, mJokeData) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, AndroidAricle androidAricle) {
                baseViewHolder.setText(R.id.item_content, baseViewHolder.getLayoutPosition() + "         " + androidAricle.getDesc());
            }
        };*/
        mAdapter = new QuickAdapter(getListData());
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Toast.makeText(getActivity(), Integer.toString(i) + "==" + mJokeData.get(i).getDesc(), Toast.LENGTH_LONG).show();

            }
        });
    }
    public static List<AndroidAricle> getListData() {
        List<AndroidAricle> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new AndroidAricle("这是第" + i + "个item"));
        }
        Log.e("TAG",list.size()+"总共有x个");
        return list;
    }
}
