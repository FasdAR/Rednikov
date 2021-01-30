package com.fasdev.devloperlife.ui.adapter.vpFragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class VpFragment(
    private val fragmentManager: FragmentManager, private val lifecycle: Lifecycle,
    private val fabricFragment: VpFragment.Fabric): FragmentStateAdapter(fragmentManager, lifecycle)
{
    interface Fabric {
        fun getItemCount(): Int
        fun createFragment(position: Int): Fragment
    }

    override fun getItemCount(): Int = fabricFragment.getItemCount()

    override fun createFragment(position: Int): Fragment = fabricFragment.createFragment(position)
}