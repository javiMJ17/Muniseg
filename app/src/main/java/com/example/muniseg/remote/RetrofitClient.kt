package com.example.muniseg.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Direccion de nuestra API de MuniSeg en Xano
    private const val BASE_URL =
        "https://x8ki-letl-twmt.n7.xano.io/api:6yaLESbh/"

    fun crearApi(baseUrl: String = BASE_URL): MunisegApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MunisegApi::class.java)
    }
}
