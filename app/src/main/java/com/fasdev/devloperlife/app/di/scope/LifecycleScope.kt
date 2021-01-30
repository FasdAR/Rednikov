package com.fasdev.devloperlife.app.di.scope

import android.app.Activity
import androidx.fragment.app.Fragment
import org.kodein.di.bindings.WeakContextScope

class LifecycleScope
{
    companion object {
        val activityScope = WeakContextScope.of<Activity>()
        val fragmentScope = WeakContextScope.of<Fragment>()
    }
}