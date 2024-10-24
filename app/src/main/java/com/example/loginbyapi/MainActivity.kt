package com.example.loginbyapi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        const val BASE_URL = "https://apiyes.net/"
        const val LOGIN_FAILED_MSG = "Login failed"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val maildata = findViewById<EditText>(R.id.editTextEmail)
        val passworddata = findViewById<EditText>(R.id.editTextPassword)
        val errorTextView = findViewById<TextView>(R.id.error)
        val login = findViewById<Button>(R.id.buttonLogin)
        val singup = findViewById<Button>(R.id.buttonSignup)

        singup.setOnClickListener {
            startActivity(Intent(this, MainActivity3::class.java))
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        login.setOnClickListener {
            val mail = maildata.text.toString().trim()
            val password = passworddata.text.toString().trim()

            if (mail.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please add the Login information", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val account = Accounts(0, "name", "", mail, password)
            apiService.login(account).enqueue(object : Callback<getResponse> {
                override fun onResponse(call: Call<getResponse>, response: Response<getResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null && loginResponse.code == 1) {
                            val intent = Intent(this@MainActivity, MainActivity2::class.java).apply {
                                putExtra("FULLNAME", loginResponse.fullName)
                            }
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, LOGIN_FAILED_MSG, Toast.LENGTH_LONG).show()
                        }
                    } else {
                        errorTextView.text = "$LOGIN_FAILED_MSG: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<getResponse>, t: Throwable) {
                    errorTextView.text = "Error: ${t.message}"
                }
            })
        }
    }
}
