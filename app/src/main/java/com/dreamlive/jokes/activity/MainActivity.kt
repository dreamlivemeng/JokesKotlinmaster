package com.dreamlive.jokes.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.ViewConfiguration
import com.dreamlive.jokes.R
import com.dreamlive.jokes.adapter.TabPageAdapter
import com.dreamlive.jokes.fragment.*
import com.jakewharton.rxbinding.view.RxView
import java.util.concurrent.TimeUnit

/**
 * 主界面
 * @author 2017/6/21 17:04 / mengwei
 */
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var touchSlop: Int = 10

    lateinit var mToolbar: Toolbar //定义变量，lateinit 延迟初始化

    lateinit var mViewPager: ViewPager

    lateinit var mTabLayout: TabLayout

    lateinit var mDrawerLayout: DrawerLayout

    lateinit var mNavigationView: NavigationView

    lateinit var mFab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initParam()
        initView()
        initAction()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_camera -> {
            }
            else -> {
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 初始化控件
     */
    private fun initParam() {
        mToolbar = findViewById(R.id.main_toolbar) as Toolbar
        mViewPager = findViewById(R.id.main_vp) as ViewPager
        mTabLayout = findViewById(R.id.main_tabl) as TabLayout
        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        mNavigationView = findViewById(R.id.nav_view) as NavigationView
        mFab = findViewById(R.id.main_fab) as FloatingActionButton
    }

    /**
     * 界面初始化
     */
    private fun initView() {
        touchSlop = (ViewConfiguration.get(this).scaledTouchSlop * 0.9).toInt()
        mToolbar.title = resources.getString(R.string.toolbartitle)
        setSupportActionBar(mToolbar)
        val toggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        initTablayout()
    }

    /**
     * 事件监听
     */
    private fun initAction() {
        RxView.clicks(mFab).throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe {
                    println("fab点击事件")
                }
        mNavigationView.setNavigationItemSelectedListener(this)
    }

    /**
     * 初始化TabLayout
     */
    fun initTablayout() {
        var tabList: MutableList<String> = ArrayList<String>()
        tabList.add(getString(R.string.joke))
        tabList.add(getString(R.string.welfare))
        tabList.add(getString(R.string.random))
        //设置滚动模式
        mTabLayout.tabMode = TabLayout.MODE_FIXED

        mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(0)))
        mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(1)))
        mTabLayout.addTab(mTabLayout.newTab().setText(tabList.get(2)))

        var listFragment: MutableList<Fragment> = ArrayList<Fragment>()
        listFragment.add(AndroidArticleFragment())
        listFragment.add(WelfareFragment())
        listFragment.add(SearchFragment())
        //设置预加载页面
        mViewPager.offscreenPageLimit = 2

        var fragmentAdapter: TabPageAdapter = TabPageAdapter(supportFragmentManager, listFragment, tabList)
        mViewPager.adapter = fragmentAdapter
        mTabLayout.setupWithViewPager(mViewPager)
    }
}