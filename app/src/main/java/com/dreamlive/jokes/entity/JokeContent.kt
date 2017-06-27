package com.dreamlive.jokes.entity

import java.io.Serializable

/**
 * 目录
 * @author 2017/6/21 15:09 / mengwei
 */
class JokeContent : Serializable {

    var content: String? = null
    var hashId: String? = null
    var unixtime: Long = 0
    var updatetime: String? = null

    constructor(content: String, hashId: String, unixtime: Long, updatetime: String) {
        this.content = content
        this.hashId = hashId
        this.unixtime = unixtime
        this.updatetime = updatetime
    }

    constructor() {}
}