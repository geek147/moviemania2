package com.envious.moviemania.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.envious.moviemania.ui.fragment.FavoriteFragment
import com.envious.moviemania.ui.fragment.PopularFragment
import com.envious.moviemania.ui.fragment.TopRatedFragment

private const val NUM_TABS = 3

public class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return PopularFragment()
            1 -> return TopRatedFragment()
        }
        return FavoriteFragment()
    }
}
