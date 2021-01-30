package com.fasdev.devloperlife.ui.adapter.vpFragment.fabric

import androidx.fragment.app.Fragment
import com.fasdev.devlife.core.common.model.TypeSection
import com.fasdev.devloperlife.ui.adapter.vpFragment.VpFragment
import com.fasdev.devloperlife.ui.fragment.PostFragment
import java.lang.IllegalArgumentException

class MainFabric: VpFragment.Fabric
{
    companion object {
        const val LATEST_POS = 0
        const val TOP_POS = 1
        const val HOT_POS = 2
    }
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment
    {
        return when (position) {
            LATEST_POS -> PostFragment.newInstance(TypeSection.LATEST)
            TOP_POS -> PostFragment.newInstance(TypeSection.TOP)
            HOT_POS -> PostFragment.newInstance(TypeSection.HOT)
            else -> throw IllegalArgumentException("Don't support this position")
        }
    }
}