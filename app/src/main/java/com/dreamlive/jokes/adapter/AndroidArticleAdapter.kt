package com.dreamlive.jokes.adapter


import android.content.Context
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dreamlive.jokes.R
import com.dreamlive.jokes.entity.AndroidAricle
import com.dreamlive.jokes.utils.CommonUtils.Companion.dateToString


/**
 * @author 2017/6/22 16:12 / mengwei
 */
class AndroidArticleAdapter(data: List<AndroidAricle>) : BaseQuickAdapter<AndroidAricle, BaseViewHolder>(R.layout.item_joke_card, data) {

    override fun convert(helper: BaseViewHolder, item: AndroidAricle) {
        Log.e("TAG", "position=" + helper.layoutPosition.toString() + "====" + item.desc)

        var imageIv: ImageView = helper.getView(R.id.item_img)

        if (item != null && item.images != null && item.images!!.size > 0) {
            imageIv!!.visibility = View.VISIBLE
            Glide.with(mContext).load(item.images!!.get(0)).into(imageIv)
        } else {
            imageIv!!.visibility = View.GONE
        }
        helper.setText(R.id.item_content, item.desc)

        helper.setText(R.id.item_date, dateToString(item.createdAt, "yyyy-MM-dd HH:mm"))
    }
}