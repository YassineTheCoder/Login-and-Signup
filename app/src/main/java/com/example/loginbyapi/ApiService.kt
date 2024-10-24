package com.example.loginbyapi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class Accounts(
    val id :Int,
    val nom: String,
    val prenom: String,

    val mail:String,
    val motDePasse: String
)
data class getResponse(
    val code:Int,
    val message: String,
    val fullName: String
)
interface ApiService {

    @POST("AccountAPI/login.php")
    fun login(@Body account: Accounts): Call<getResponse>




    @POST("AccountAPI/create.php")
    fun signup(@Body account: Accounts): Call<getResponse>
}