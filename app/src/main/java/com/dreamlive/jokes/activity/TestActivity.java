package com.dreamlive.jokes.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dreamlive.jokes.R;
import com.dreamlive.jokes.adapter.QuickAdapter;
import com.dreamlive.jokes.entity.AndroidAricle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 2017/6/23 10:25 / mengwei
 */
@Deprecated
public class TestActivity extends AppCompatActivity {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    List<AndroidAricle> mJokeData;
    BaseQuickAdapter<AndroidAricle, BaseViewHolder> mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.main_srl);
        mRecyclerView = (RecyclerView) findViewById(R.id.main_rv);
        initView();
    }

    private void initView() {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mJokeData = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mJokeData.add(new AndroidAricle("这是第" + i + "个item"));
        }
        mAdapter = new QuickAdapter(getListData());

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Toast.makeText(TestActivity.this, Integer.toString(i) + "==" + mJokeData.get(i).getDesc(), Toast.LENGTH_LONG).show();

            }
        });

    }

    public static List<AndroidAricle> getListData() {
        List<AndroidAricle> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(new AndroidAricle("这是第" + i + "个item"));
        }
        Log.e("TAG", list.size() + "总共有x个");
        return list;
    }
}
