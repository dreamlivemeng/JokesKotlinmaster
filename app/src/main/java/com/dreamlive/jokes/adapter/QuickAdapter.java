package com.dreamlive.jokes.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dreamlive.jokes.R;
import com.dreamlive.jokes.entity.AndroidAricle;

import java.util.List;

@Deprecated
public class QuickAdapter extends BaseQuickAdapter<AndroidAricle, BaseViewHolder> {


    public QuickAdapter(List<AndroidAricle> listdata) {
        super(R.layout.item_joke_card, listdata);
    }

    @Override
    protected void convert(BaseViewHolder helper, AndroidAricle item) {
        helper.setText(R.id.item_content, item.getDesc());
        ImageView imgIV = helper.getView(R.id.item_img);
    }


}
