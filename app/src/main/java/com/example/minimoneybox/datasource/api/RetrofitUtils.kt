package com.example.minimoneybox.datasource.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtils {
    const val BASE_URL = "https://api-test01.moneyboxapp.com/"
    const val APP_ID = "3a97b932a9d449c981b595"
    const val CONTENT_TYPE = "application/json"
    const val APP_VERSION = "5.10.0"
    const val API_VERSION = "3.0.0"

    fun createService(): ApiService {

        val builder = Retrofit.Builder()
            .baseUrl(BASE_URL).addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(provideOkHttpClient())
            .build()

        return builder.create<ApiService>(ApiService::class.java)
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        httpClient
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("AppId", APP_ID)
                    .addHeader("Content-Type", CONTENT_TYPE)
                    .addHeader("appVersion", APP_VERSION)
                    .addHeader("apiVersion", API_VERSION)
                    .build()
                chain.proceed(newRequest)
            }
            .addInterceptor(provideLoggingInterceptor())

        return httpClient.build()
    }

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }
}