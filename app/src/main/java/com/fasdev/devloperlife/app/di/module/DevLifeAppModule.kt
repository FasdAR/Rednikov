package com.fasdev.devloperlife.app.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.fasdev.devlife.data.repository.local.LocalRepository
import com.fasdev.devlife.data.repository.local.LocalRepositoryImpl
import com.fasdev.devlife.data.repository.network.NetworkRepository
import com.fasdev.devlife.data.repository.network.NetworkRepositoryImpl
import com.fasdev.devlife.data.source.retrofit.api.DevLifeApi
import com.fasdev.devlife.data.source.room.DevLifeDB
import com.fasdev.devlife.data.source.room.dao.PostDao
import com.fasdev.devlife.data.source.room.dao.PostQueueDao
import com.fasdev.devlife.data.source.room.dao.QueueDao
import com.fasdev.devlife.data.source.shareData.SharedData
import com.fasdev.devloperlife.BuildConfig
import com.fasdev.devloperlife.app.util.bindViewModel
import com.fasdev.devloperlife.ui.fragment.post.ui.PostFragment
import com.fasdev.devloperlife.ui.fragment.post.useCase.PostUseCase
import com.fasdev.devloperlife.ui.fragment.post.viewModel.PostViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.*
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val mainModule = DI.Module("devLifeApp") {
    import(viewModelModule)
    import(networkModule)
    import(dbModule)
    import(repositoryModule)
}

val viewModelModule = DI.Module("viewModel") {
    import(viewModelFactoryModule)

    bindViewModel<PostFragment, PostViewModel, PostViewModel.Factory>()
}

val viewModelFactoryModule = DI.Module("vmFactory") {
    bind() from provider {
        PostViewModel.Factory(instance())
    }
}

val networkModule = DI.Module("network") {
    bind<Converter.Factory>() with  singleton {
        GsonConverterFactory.create()
    }

    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(instance<HttpLoggingInterceptor>())
            }
        }.build()
    }

    bind<HttpLoggingInterceptor>() with provider {
        HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
    }

    bind() from singleton {
        Retrofit.Builder()
            .baseUrl("https://developerslife.ru")
            .addConverterFactory(instance())
            .client(instance())
            .build()
    }

    bind<DevLifeApi>() with singleton {
        instance<Retrofit>().create(DevLifeApi::class.java)
    }
}

val dbModule = DI.Module("db") {
    bind<DevLifeDB>() with singleton {
        Room.databaseBuilder(instance(), DevLifeDB::class.java, DevLifeDB.DB_NAME)
            .build()
    }

    bind<QueueDao>() with singleton {
        instance<DevLifeDB>().getQueueDao()
    }

    bind<PostDao>() with singleton {
        instance<DevLifeDB>().getPostDao()
    }

    bind<PostQueueDao>() with singleton {
        instance<DevLifeDB>().getPostQueueDao()
    }
}

val repositoryModule = DI.Module("repository") {
    bind<LocalRepository>() with singleton {
        LocalRepositoryImpl(instance(), instance(), instance(), instance())
    }

    bind<NetworkRepository>() with singleton {
        NetworkRepositoryImpl(instance())
    }

    bind() from provider {
        PostUseCase(instance(), instance())
    }

    constant(tag = "name_settings") with SharedData.NAME_SETTINGS

    bind() from provider {
        instance<Context>()
                .getSharedPreferences(instance(tag = "name_settings"), Context.MODE_PRIVATE)
    }

    bind() from provider {
        SharedData(instance())
    }
}