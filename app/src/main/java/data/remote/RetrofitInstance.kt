package data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://api.exchangerate.host/"
    private const val ACCESS_KEY = "5b8239e77b94065ff4d94f0f6494e470"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val originalUrl = original.url()

            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("access_key", ACCESS_KEY)
                .build()

            val requestBuilder = original.newBuilder().url(newUrl)
            val request = requestBuilder.build()
            chain.proceed(request)
        })
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ExchangeRateApi by lazy {
        retrofit.create(ExchangeRateApi::class.java)
    }
}

