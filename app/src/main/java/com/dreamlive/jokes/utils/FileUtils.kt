package com.dreamlive.jokes.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable


/**
 * 文件帮助类
 * @author 2017/6/26 16:48 / mengwei
 */
class FileUtils {
    companion object {

        /**
         * 将drawable转为Bitmap
         */
        fun drawableToBitmap(drawable: Drawable): Bitmap {
            val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    if (drawable.opacity != PixelFormat.OPAQUE)
                        Bitmap.Config.ARGB_8888
                    else
                        Bitmap.Config.RGB_565)
            val canvas = Canvas(bitmap)
            //canvas.setBitmap(bitmap);
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)
            return bitmap
        }
    }
}