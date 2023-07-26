package com.digiventure.fido_exploration.module

import android.content.Context
import com.digiventure.fido_exploration.api.AuthAPI
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.Fido2ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityComponent::class)
class AuthModule {
    @Provides
    fun authApi(retrofit: Retrofit) : AuthAPI = retrofit.create(AuthAPI::class.java)

    @Provides
    fun retrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://bda0-43-245-190-132.ngrok-free.app/api/ssi/issuer/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun fido2Client(@ApplicationContext context: Context): Fido2ApiClient {
        return Fido.getFido2ApiClient(context)
    }
}