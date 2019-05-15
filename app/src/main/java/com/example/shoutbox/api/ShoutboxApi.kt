package com.example.shoutbox.api

import androidx.lifecycle.LiveData
import com.example.shoutbox.db.MessageEntry
import com.example.shoutbox.db.MessageInput
import com.example.shoutbox.util.LiveDataCallAdapterFactory
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.cert.X509Certificate
import javax.net.ssl.*
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.http.*


const val BASE_URL = "https://tgryl.pl/shoutbox/"

interface ShoutboxApi {

    @GET("messages")
    fun getMessages(): Deferred<List<MessageEntry>>

    @POST("message")
    fun sendMessage(@Body message: MessageInput): Deferred<MessageEntry>

    @PUT("message/{id}")
    fun updateMessage(@Body message: MessageInput,
                      @Path("id") messageId: String
    ): Deferred<MessageEntry>

    @DELETE("message/{id}")
    fun deleteMessage(@Path("id") messageId: String): Deferred<MessageEntry>


    companion object {
        operator fun invoke(): ShoutboxApi {

            return Retrofit.Builder()
                .client(getUnsafeOkHttpClient())
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(ShoutboxApi::class.java)
        }

        private fun getUnsafeOkHttpClient(): OkHttpClient {
            val trustAllCerts = getUnvalidatedTrustManager()
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            return OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
                .build()
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