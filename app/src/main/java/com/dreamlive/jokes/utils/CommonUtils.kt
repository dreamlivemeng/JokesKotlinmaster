package com.dreamlive.jokes.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 一些比较常用的utils
 * @author 2017/6/23 14:10 / mengwei
 */
class CommonUtils {

    companion object {
        /**
         *  formatType格式：yyyy-MM-dd HH:mm:ss
         *
         */
        fun dateToString(data: Date?, formatType: String): String {
            if (data == null) {
                return ""
            }
            return SimpleDateFormat(formatType).format(data)
        }

    }
}