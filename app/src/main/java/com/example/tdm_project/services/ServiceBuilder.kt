package com.example.tdm_project.services


import com.example.tdm_project.view.activities.CustomBaseActivity
import com.facebook.FacebookSdk.getCacheDir
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object ServiceBuilder {


    // the Base adresse
    private const val URL = "https://dznews-heroku.herokuapp.com"
    val cacheSize = (5 * 1024 * 1024).toLong()
    val myCache = Cache(CustomBaseActivity.cache!!, cacheSize)

    val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    //Create Okhttp Client
    private val okHttp: OkHttpClient = OkHttpClient.Builder()
        .cache(myCache)
        .addInterceptor{
                chain : Interceptor.Chain ->

            // Get the request from the chain.
            var request = chain.request()

            /*
            *  Leveraging the advantage of using Kotlin,
            *  we initialize the request and change its header depending on whether
            *  the device is connected to Internet or not.
            */
            request = if (CustomBaseActivity.isOnline)
            /*
            *  If there is Internet, get the cache that was stored 5 seconds ago.
            *  If the cache is older than 5 seconds, then discard it,
            *  and indicate an error in fetching the response.
            *  The 'max-age' attribute is responsible for this behavior.
            */
                request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
            else
            /*
            *  If there is no Internet, get the cache that was stored 7 days ago.
            *  If the cache is older than 7 days, then discard it,
            *  and indicate an error in fetching the response.
            *  The 'max-stale' attribute is responsible for this behavior.
            *  The 'only-if-cached' attribute indicates to not retrieve new data; fetch the cache only instead.
            */
                request.newBuilder().header("Cache-Control", "public, max-stale=" + 60 * 60 * 24 * 7).build()
            // End of if-else statement

            // Add the modified request to the chain.
            chain.proceed(request)
        }
        .readTimeout(5, TimeUnit.MINUTES)
        .writeTimeout(5, TimeUnit.MINUTES)
        .build()

    // Create Retrofit Builder
    private val builder: Retrofit.Builder = Retrofit.Builder()
        .client(okHttp)
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())

    //create Retrofit Instance
    private val retrofit = builder.build()


    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }

}