package com.covidtracker.di

import android.content.Context
import com.covidtracker.BuildConfig
import com.covidtracker.repository.MainRepository
import com.covidtracker.util.isNetworkAvailable
import com.covidtracker.webservices.CovidWebservices
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
val restServiceModule = module {

    single {
        GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(getOkHttpClient(androidContext()))
            .build()
            .create(CovidWebservices::class.java)
    }

    single {
        MainRepository(get())
    }
}

fun getOkHttpClient(context: Context): OkHttpClient {
    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(context.cacheDir, cacheSize)

    return OkHttpClient.Builder()
        .cache(myCache)
        .addInterceptor { chain ->
            var request = chain.request()
            request = if (isNetworkAvailable(context)!!)
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
}

