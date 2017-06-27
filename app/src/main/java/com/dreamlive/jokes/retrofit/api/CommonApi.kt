package com.dreamlive.jokes.retrofit.api

import com.dreamlive.jokes.entity.AndroidAricle
import com.dreamlive.jokes.entity.ClassifyData
import com.dreamlive.jokes.entity.Welfare
import retrofit2.http.GET
import retrofit2.http.Path
import rx.Observable


/**
 * 接口API
 * @author 2017/6/21 14:57 / mengwei
 */

interface CommonApi {

    /**
     * 获取Android相关文章
     * http://gank.io/api/data/Android/10/1
     */
    @GET("data/Android/{pageSize}/{page}")
    fun getAndroidArticle(@Path("pageSize") pageSize: Int, @Path("page") page: Int): Observable<List<AndroidAricle>>

    /**
     * http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/1
     */
    @GET("data/福利/{pageSize}/{page}")
    fun getWelfare(@Path("pageSize") pageSize: Int, @Path("page") page: Int): Observable<List<Welfare>>

    /**
     * http://gank.io/api/search/query/listview/category/Android/count/10/page/1
     * 注：
    category 后面可接受参数 all | Android | iOS | 休息视频 | 福利 | 拓展资源 | 前端 | 瞎推荐 | App
    count 最大 50
     */
    @GET("search/query/listview/category/{type}/count/{pageSize}/page/{page}")
    fun searchData(@Path("type") type: String, @Path("pageSize") pageSize: Int, @Path("page") page: Int): Observable<String>

    /**
     * 随机数据：http://gank.io/api/random/data/分类/个数

    数据类型：福利 | Android | iOS | 休息视频 | 拓展资源 | 前端
    个数： 数字，大于0
    例：
    http://gank.io/api/random/data/Android/20
     */
    @GET("http://gank.io/api/random/data/{type}/20")
    fun randomData(@Path("type") type: String): Observable<String>

    /**
     * http://gank.io/api/data/数据类型/请求个数/第几页
     *  福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
     */
    @GET("http://gank.io/api/data/{type}/{pageSize}/{page}")
    fun classifyData(@Path("type") type: String, @Path("pageSize") pageSize: Int, @Path("page") page: Int): Observable<List<ClassifyData>>

}
