package com.dreamlive.jokes.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.dreamlive.jokes.R
import com.dreamlive.jokes.activity.PicturePreviewActivity
import com.dreamlive.jokes.activity.WebViewActivity
import com.dreamlive.jokes.adapter.ClassifyDataAdapter
import com.dreamlive.jokes.entity.ClassifyData
import com.dreamlive.jokes.entity.Welfare
import com.dreamlive.jokes.retrofit.RetrofitFactory
import com.dreamlive.jokes.retrofit.api.CommonApi
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 分类数据
 * @author 2017/6/26 10:04 / mengwei
 */
class SearchFragment : Fragment(), BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        //刷新
        mSwipeRefreshLayout.isRefreshing = true
        mData.clear()
        mPage = 1
        requestData()
    }

    override fun onLoadMoreRequested() {
        //加载更多
        ++mPage
        requestData()
    }


    var type: String = "福利"
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    lateinit var mRecyclerView: RecyclerView
    lateinit var mLayoutManager: LinearLayoutManager

    var mData = mutableListOf<ClassifyData>()


    var mPage: Int = 1//当前页码
    var mPageSize: Int = 8//默认返回条数，最大20条
    internal var mAdapter: ClassifyDataAdapter? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater!!.inflate(R.layout.fragment_search, container, false)
        initParam(rootView)
        initView()
        initAction()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.main, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item!!.itemId) {
            R.id.action_settings_welfare -> {
                type = "福利"
                resetPage()
                return true
            }
            R.id.action_settings_android -> {
                type = "Android"
                resetPage()
                return true
            }
            R.id.action_settings_ios -> {
                type = "iOS"
                resetPage()
                return true
            }
            R.id.action_settings_video -> {
                type = "休息视频"
                resetPage()
                return true
            }
            R.id.action_settings_expand_resoureces -> {
                type = "拓展资源"
                resetPage()
                return true
            }
            R.id.action_settings_qd -> {
                type = "前端"
                resetPage()
                return true
            }
            R.id.action_settings_all -> {
                type = "all"
                resetPage()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * 重新根据分类请求
     */
    private fun resetPage() {
        mData.clear()
        mPage = 1
        requestData()
    }

    /**
     * 初始化控件
     */
    private fun initParam(rootView: View) {
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

        mAdapter = ClassifyDataAdapter(mData)
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
                val item = mAdapter!!.data.get(position)
                Log.e("Tag", "url=" + item.url)
                if (item.type == "福利") {
                    val intent = Intent(context, PicturePreviewActivity::class.java)
                    intent.putExtra("url", item.url)
                    startActivity(intent)
                } else if (item.type == "休息视频") {
                    //直接跳转到第三方浏览器
                    val uri = Uri.parse(item.url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                } else {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("title", item.desc)
                    intent.putExtra("url", item.url)
                    startActivity(intent)
                }
            } catch (e: NullPointerException) {

            } catch (e: IndexOutOfBoundsException) {
                //
            }
        }
    }

    /**
     *
     * 请求数据
     */
    private fun requestData() {
        RetrofitFactory.getControllerSingleTonOperation(CommonApi::class.java)
                .classifyData(type, mPageSize, mPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result: List<ClassifyData>? ->
                    mAdapter!!.loadMoreComplete()
                    mSwipeRefreshLayout.isRefreshing = false
                    if (result != null && result!!.size > 0) {
                        mData.addAll(result)
                        mAdapter!!.notifyDataSetChanged()
                    }
                }, { t: Throwable? ->
                    mAdapter!!.loadMoreComplete()
                    mSwipeRefreshLayout.isRefreshing = false
                }, {
                    mAdapter!!.loadMoreComplete()
                    mSwipeRefreshLayout.isRefreshing = false
                })

    }
}