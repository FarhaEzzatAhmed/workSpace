package com.example.workspace.api.model

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {
    companion object{
        private var retrofit: Retrofit?=null
        private @Synchronized  fun getInstance(): Retrofit {
            if(retrofit == null){
                val logginInterceptor = HttpLoggingInterceptor(
                    HttpLoggingInterceptor.Logger {
                        Log.e("api",it)
                    }
                )

                logginInterceptor.level = HttpLoggingInterceptor.Level.BODY

                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(logginInterceptor)
                    .build()
                retrofit = Retrofit.Builder()
                    .baseUrl("https://newsapi.org/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
        fun getApis(): WebServices {
            return  getInstance().create(WebServices::class.java)
        }
    }
}