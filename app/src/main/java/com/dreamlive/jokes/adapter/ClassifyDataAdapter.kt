package com.dreamlive.jokes.adapter


import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.dreamlive.jokes.R
import com.dreamlive.jokes.entity.ClassifyData
import com.dreamlive.jokes.utils.CommonUtils.Companion.dateToString


/**
 * @author 2017/6/22 16:12 / mengwei
 */
class ClassifyDataAdapter(data: List<ClassifyData>) : BaseQuickAdapter<ClassifyData, BaseViewHolder>(R.layout.item_classify_data_card, data) {

    override fun convert(helper: BaseViewHolder, item: ClassifyData) {
        Log.e("TAG", "position=" + helper.layoutPosition.toString() + "====" + item.desc)
        var imageIv: ImageView = helper.getView(R.id.item_welfare_img)

        if (item.type == "福利") {
            helper.setVisible(R.id.item_type_welfare_rl, true)
            helper.setVisible(R.id.item_type_not_welfare_rl, false)
            Glide.with(mContext).load(item.url).into(imageIv)
        } else {
            helper.setVisible(R.id.item_type_welfare_rl, false)
            helper.setVisible(R.id.item_type_not_welfare_rl, true)
            var imageIv: ImageView = helper.getView(R.id.item_img)
            if (item != null && item.images != null && item.images!!.size > 0) {
                imageIv!!.visibility = View.VISIBLE
                Glide.with(mContext).load(item.images!!.get(0)).into(imageIv)
            } else {
                imageIv!!.visibility = View.GONE
            }
            helper.setText(R.id.item_content, item.desc)//描述
            helper.setText(R.id.item_type, item.type)//类型

            helper.setText(R.id.item_date, dateToString(item.createdAt, "yyyy-MM-dd HH:mm"))//时间
        }

    }
}