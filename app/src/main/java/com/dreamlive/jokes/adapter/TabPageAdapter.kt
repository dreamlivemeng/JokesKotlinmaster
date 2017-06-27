package com.dreamlive.jokes.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author 2017/6/20 15:04 / mengwei
 */
class TabPageAdapter(fm: FragmentManager, private val listFragment: List<Fragment>, private val titles: List<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titles[position]
    }
}