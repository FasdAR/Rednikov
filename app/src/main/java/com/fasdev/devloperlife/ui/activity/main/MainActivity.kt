package com.fasdev.devloperlife.ui.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fasdev.devloperlife.R
import com.fasdev.devloperlife.databinding.ActivityMainBinding
import com.fasdev.devloperlife.ui.adapter.vpFragment.VpFragment
import com.fasdev.devloperlife.ui.adapter.vpFragment.fabric.MainFabric
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    private val vpAdapter by lazy {
        return@lazy VpFragment(supportFragmentManager, lifecycle, MainFabric())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = vpAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab: TabLayout.Tab, pos: Int ->
            val text: String = when (pos) {
                MainFabric.LATEST_POS -> resources.getString(R.string.latest)
                MainFabric.TOP_POS -> resources.getString(R.string.top)
                MainFabric.HOT_POS -> resources.getString(R.string.hot)
                else -> throw IllegalArgumentException("Don't support pos")
            }

            tab.text = text
        }.attach()
    }
}