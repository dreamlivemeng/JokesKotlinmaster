package com.dreamlive.jokes.adapter


import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dreamlive.jokes.R
import com.dreamlive.jokes.entity.Welfare


/**
 * 福利页面适配器
 * @author 2017/6/22 16:12 / mengwei
 */
class WelfareAdapter(data: List<Welfare>) : BaseQuickAdapter<Welfare, BaseViewHolder>(R.layout.item_welfare, data) {

    override fun convert(helper: BaseViewHolder, item: Welfare) {

        var imageIv: ImageView = helper.getView(R.id.item_img)

        if (item != null && !TextUtils.isEmpty(item.url)) {
            imageIv!!.visibility = View.VISIBLE
            Glide.with(mContext).load(item.url).into(imageIv)
        } else {
            imageIv!!.visibility = View.GONE
        }

    }
}