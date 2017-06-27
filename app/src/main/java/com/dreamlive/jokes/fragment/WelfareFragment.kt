package com.dreamlive.jokes.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dreamlive.jokes.R
import com.dreamlive.jokes.activity.PicturePreviewActivity
import com.dreamlive.jokes.adapter.WelfareAdapter
import com.dreamlive.jokes.entity.Welfare
import com.dreamlive.jokes.retrofit.RetrofitFactory
import com.dreamlive.jokes.retrofit.api.CommonApi
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


/**
 * 福利
 * @author 2017/6/20 14:59 / mengwei
 */
class WelfareFragment : Fragment(), BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    override fun onLoadMoreRequested() {
        ++mPage
        requestData()
    }

    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = true
        mData.clear()
        mPage = 1
        requestData()
    }

    lateinit var rootView: View
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager

    var mData = mutableListOf<Welfare>()


    var mPage: Int = 1//当前页码
    var mPageSize: Int = 20//默认返回条数，最大20条
    internal var mAdapter: WelfareAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.content_main, container, false)
        initParam()
        initView()
        initAction()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestData()
    }

    /**
     * 初始化控件
     */
    private fun initParam() {
        mSwipeRefreshLayout = rootView.findViewById(R.id.main_srl) as SwipeRefreshLayout
        mRecyclerView = rootView.findViewById(R.id.main_rv) as RecyclerView
    }

    /**
     * 初始化界面
     */
    private fun initView() {
        mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.VERTICAL
        mRecyclerView.layoutManager = mLayoutManager

        mAdapter = WelfareAdapter(mData)
        mRecyclerView.adapter = mAdapter
        mAdapter!!.notifyDataSetChanged()
    }

    /**
     * 事件监听
     */
    private fun initAction() {
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mAdapter!!.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT)
        mAdapter!!.setOnLoadMoreListener(this, mRecyclerView)
        mAdapter!!.setOnItemClickListener { adapter, view, position ->
            try {
                val intent = Intent(context, PicturePreviewActivity::class.java)
                val item: Welfare = adapter!!.data.get(position) as Welfare
                intent.putExtra("url", item.url)
                startActivity(intent)
            } catch (e: NullPointerException) {

            } catch (e: IndexOutOfBoundsException) {
            }

        }
    }

    /**
     * 数据请求
     */
    private fun requestData() {
        RetrofitFactory.getControllerSingleTonOperation(CommonApi::class.java)
                .getWelfare(mPageSize, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread() as Scheduler)
                .subscribe({ result: List<Welfare> ->
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