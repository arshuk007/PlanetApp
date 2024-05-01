package com.myapp.planetapp.di

import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.test.planetapp.network.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule  = module{


    //Network dependency
    factory { provideRetrofit(get(), "") }
    factory { provideHttpClient(get()) }
    factory { provideLoggingInterceptor(get()) }
    factory { provideApiInterface(get()) }

}

private fun provideRetrofit(okHttpClient: OkHttpClient, appUrl: String): Retrofit{
    return Retrofit.Builder().baseUrl(appUrl).client(okHttpClient)
        .addConverterFactory(provideGsonConverterFactory()).build()
}

private fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
    return OkHttpClient().newBuilder()
        .connectTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)
        .addInterceptor(loggingInterceptor)
        .build()
}

private fun provideLoggingInterceptor(context: Context): HttpLoggingInterceptor{
    val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
        override fun log(message: String) {
            if(BuildConfig.DEBUG){
                Log.d("PlanetAppLog", message)
            }
        }
    })

    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}

private fun provideApiInterface(retrofit: Retrofit): ApiInterface = retrofit.create(ApiInterface::class.java)

private fun provideGsonConverterFactory(): GsonConverterFactory{
    return GsonConverterFactory.create(GsonBuilder().create())
}