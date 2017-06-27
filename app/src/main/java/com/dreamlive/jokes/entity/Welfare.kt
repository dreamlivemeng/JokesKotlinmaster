package com.dreamlive.jokes.entity

import java.util.*

/**
 * @author 2017/6/23 14:02 / mengwei
 */
class Welfare {
    /*
        "_id": "5941db7b421aa92c794633cd",
        "createdAt": "2017-06-15T08:57:31.47Z",
        "desc": "6-15",
        "publishedAt": "2017-06-15T13:55:57.947Z",
        "source": "chrome",
        "type": "\u798f\u5229",
        "url": "https://ws1.sinaimg.cn/large/610dc034ly1fgllsthvu1j20u011in1p.jpg",
        "used": true,
        "who": "\u4ee3\u7801\u5bb6"*/
    var _id: String? = null
    var createdAt: Date? = null
    var desc: String? = null
    var publishedAt: Date? = null
    var source: String? = null
    var type: String? = null
    var url: String? = null
    var used: Boolean = true
    var who: String? = null

    constructor()
    constructor(_id: String?, createdAt: Date?, desc: String?, publishedAt: Date?, source: String?, type: String?, url: String?, used: Boolean, who: String?) {
        this._id = _id
        this.createdAt = createdAt
        this.desc = desc
        this.publishedAt = publishedAt
        this.source = source
        this.type = type
        this.url = url
        this.used = used
        this.who = who
    }

}