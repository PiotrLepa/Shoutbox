package com.example.shoutbox.api

import com.example.shoutbox.db.model.Message
import com.example.shoutbox.db.model.MessagePost
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


const val BASE_URL = "https://tgryl.pl/shoutbox/"

interface ShoutboxApi {

    @GET("messages")
    fun getMessages(): Deferred<List<Message>>

    @POST("message")
    fun sendMessage(@Body message: MessagePost): Deferred<Message>

    @PUT("message/{id}")
    fun updateMessage(@Body message: MessagePost,
                      @Path("id") messageId: String
    ): Deferred<Message>

    @DELETE("message/{id}")
    fun deleteMessage(@Path("id") messageId: String): Deferred<Message>


    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ShoutboxApi {

            val okHttpClient = getUnsafeOkHttpClientBuilder()
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(ShoutboxApi::class.java)
        }

        private fun getUnsafeOkHttpClientBuilder(): OkHttpClient.Builder {
            val trustAllCerts = getUnvalidatedTrustManager()
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
        }

        private fun getUnvalidatedTrustManager(): Array<TrustManager> {
            return arrayOf(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) = Unit
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) = Unit
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })
        }
    }
}