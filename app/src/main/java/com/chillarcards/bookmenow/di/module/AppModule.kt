package com.chillarcards.bookmenow.di.module

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.chillarcards.bookmenow.data.api.ApiHelper
import com.chillarcards.bookmenow.data.api.ApiHelperImpl
import com.chillarcards.bookmenow.data.api.ApiService
import com.chillarcards.bookmenow.utills.NetworkHelper
import com.chillarcards.bookmenow.utills.PrefManager
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val CONNECT_TIMEOUT: Long = 60

/**
 * @Author: Sherin Jaison
 * @Date: 01-11-2023
 * Chillar
 */

val appModule = module {
    single { provideOkHttpClient(get()) }
    single { provideApiService(get()) }
    single { provideRetrofit(get(), ConfigBuild.BASE_URL) }
    single { provideNetworkHelper(androidContext()) }
    single<ApiHelper> {
        return@single ApiHelperImpl(get())
    }
    single { PrefManager(androidContext()) } // Provide PrefManager instance

}

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)

private fun provideOkHttpClient(prefManager: PrefManager) = if (ConfigBuild.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(getHeaderInterceptor(prefManager))
        .protocols(listOf(Protocol.HTTP_1_1))
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
        .build()
} else OkHttpClient
    .Builder()
    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
    .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
    .writeTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
    .connectionPool(ConnectionPool(0, 5, TimeUnit.MINUTES))
    .protocols(listOf(Protocol.HTTP_1_1))
    .build()

private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
) =
    Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create().asLenient())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

private fun provideApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)

private fun getHeaderInterceptor(prefManager: PrefManager): Interceptor {
    return Interceptor { chain ->
        val token = setAuthHeader(prefManager)
        val request = chain.request().newBuilder()
            .header("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }
}

// Modify setAuthHeader function to accept PrefManager instance as parameter
fun setAuthHeader(prefManager: PrefManager): String {
    var token=""
    if(prefManager.getRefresh() == "0"){
        token = prefManager.getToken()
        Log.e("AppModelAuth token",token)
    }else{
        token = prefManager.getRefToken()
        Log.e("AppModelAuth retoken",token)

    }
    return token
}
