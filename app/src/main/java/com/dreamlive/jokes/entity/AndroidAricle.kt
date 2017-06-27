package com.dreamlive.jokes.entity

import java.util.*

/**
 * android文章
 * @author 2017/6/22 17:14 / mengwei
 */
class AndroidAricle {

/*    "_id": "593f2091421aa92c769a8c6a",
    "createdAt": "2017-06-13T07:15:29.423Z",
    "desc": "Android\u4e4b\u81ea\u5b9a\u4e49View\uff1a\u4fa7\u6ed1\u5220\u9664",
    "publishedAt": "2017-06-15T13:55:57.947Z",
    "source": "web",
    "type": "Android",
    "url": "https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA==&mid=2247484934&idx=1&sn=f2a40261efe8ebee45804e9df93c1cce&chksm=96cda74ba1ba2e5dbbac15a9e57b5329176d1fe43478e5c63f7bc502a6ca50e4dfa6c0a9041e#rd",
    "used": true,
    "who": "\u9648\u5b87\u660e"*/

    var _id: String? = null
    var createdAt: Date? = null
    var desc: String? = null
    var publishedAt: Date? = null
    var source: String? = null
    var type: String? = null
    var url: String? = null
    var used: Boolean = true
    var who: String? = null
    var images: List<String>? = null


    constructor()

    constructor(_id: String?, createdAt: Date?, desc: String?, publishedAt: Date?, source: String?, type: String?, url: String?, used: Boolean, who: String?, images: List<String>?) {
        this._id = _id
        this.createdAt = createdAt
        this.desc = desc
        this.publishedAt = publishedAt
        this.source = source
        this.type = type
        this.url = url
        this.used = used
        this.who = who
        this.images = images
    }

    constructor(desc: String?) {
        this.desc = desc
    }

}