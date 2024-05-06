package com.myapp.planetapp.di

import android.util.Log
import com.google.gson.GsonBuilder
import com.test.planetapp.db.PlanetAppDB
import com.test.planetapp.network.ApiInterface
import com.test.planetapp.network.ResponseHandler
import com.test.planetapp.repository.HomeRepository
import com.test.planetapp.repository.PlanetDetailsRepository
import com.test.planetapp.usecase.HomeUsecase
import com.test.planetapp.usecase.PlanetDetailsUsecase
import com.test.planetapp.viewmodel.HomeViewModel
import com.test.planetapp.viewmodel.PlanetDetailsViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory

val appModule  = module{

    //ViewModel dependency
    viewModel {HomeViewModel(get())}
    viewModel {PlanetDetailsViewModel(get())}

    //Usecase dependency
    factory { HomeUsecase(get(), get(), get()) }
    factory { PlanetDetailsUsecase(get(), get()) }

    //Repository dependency
    single { HomeRepository(get()) }
    single { PlanetDetailsRepository(get()) }

    //Database dependency
    single { PlanetAppDB.getDatabase(get()) }
    single(createdAtStart = false) { get<PlanetAppDB>().planetAppDao() }

    //Network dependency
    factory { provideHttpClient(get()) }
    factory { provideApiInterface(get()) }
    factory { provideLoggingInterceptor() }
    factory { ResponseHandler() }
    single { provideRetrofit(get(), "https://swapi.dev/api/") }

}

private fun provideRetrofit(okHttpClient: OkHttpClient, appUrl: String): Retrofit{
    return Retrofit.Builder().baseUrl(appUrl).client(okHttpClient)
        .addConverterFactory(provideGsonConverterFactory())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build()
}

private fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
    return OkHttpClient().newBuilder()
        .connectTimeout(2, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)
        .addInterceptor(loggingInterceptor)
        .build()
}

private fun provideLoggingInterceptor(): HttpLoggingInterceptor{
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