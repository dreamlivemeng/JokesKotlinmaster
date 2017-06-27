package com.dreamlive.jokes.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dreamlive.jokes.R
import com.dreamlive.jokes.adapter.AndroidArticleAdapter
import com.dreamlive.jokes.entity.AndroidAricle
import com.dreamlive.jokes.retrofit.RetrofitFactory
import com.dreamlive.jokes.retrofit.api.CommonApi
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * @author 2017/6/20 14:59 / mengwei
 */
class OtherArticleFragment : Fragment(), BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    override fun onLoadMoreRequested() {
        ++mPage
        getJoke()
    }

    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = true
        mData.clear()
        mPage = 1
        getJoke()
    }

    lateinit var rootView: View
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager

    var mData = mutableListOf<AndroidAricle>()


    var mPage: Int = 1//当前页码
    var mPageSize: Int = 8//默认返回条数，最大20条
    internal var mAdapter: AndroidArticleAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.content_main, container, false)
        initParam()
        initView()
        initAction()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        getJoke()
    }

    /**
     * 初始化控件
     */
    private fun initParam() {
        mSwipeRefreshLayout = rootView.findViewById(R.id.main_srl) as SwipeRefreshLayout
        mRecyclerView = rootView.findViewById(R.id.main_rv) as RecyclerView
    }

    private fun initView() {
        mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = mLayoutManager
        mAdapter = AndroidArticleAdapter(mData)
        mRecyclerView.adapter = mAdapter
    }

    private fun initAction() {
        mSwipeRefreshLayout.setOnRefreshListener(this)
//        mAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
        mAdapter!!.setOnLoadMoreListener(this, mRecyclerView)
    }


    private fun getJoke() {
        RetrofitFactory.getControllerSingleTonOperation(CommonApi::class.java)
                .getAndroidArticle(mPageSize, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread() as Scheduler)
                .subscribe({ result: List<AndroidAricle> ->
                    mAdapter!!.loadMoreComplete()
                    mSwipeRefreshLayout.isRefreshing = false
                    if (result != null && result!!.size > 0) {
                        mData.addAll(result)
                        mAdapter!!.notifyDataSetChanged()
                        for (item in mData) {
                            println(item.desc)
                        }
                    }
                }, { t: Throwable? -> }, {
                    mAdapter!!.loadMoreComplete()
                    mSwipeRefreshLayout.isRefreshing = false
                })
    }


}