package com.fasdev.devloperlife.app

import android.app.Application
import com.fasdev.devloperlife.app.di.module.mainModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

class DevLifeApp: Application(), DIAware
{
    override val di: DI by DI.lazy {
        import(androidXModule(this@DevLifeApp))
        import(mainModule)
    }
}