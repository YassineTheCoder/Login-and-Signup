package com.example.loginbyapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameEditText = findViewById<EditText>(R.id.editTextNom)
        val firstNameEditText = findViewById<EditText>(R.id.editTextPrenom)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextMotDePasse) // Corrected ID
        val buttonSignUp = findViewById<Button>(R.id.buttonSignUp)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiyes.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        buttonSignUp.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val firstName = firstNameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            Log.d("MainActivity3", "Signing up: $name, $firstName, $email, $password")

            if (name.isEmpty() || firstName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please add all the required information", Toast.LENGTH_SHORT).show()
            } else {
                val account = Accounts(
                    id = 0,
                    nom = name,
                    prenom = firstName,
                    mail = email,
                    motDePasse = password
                )

                apiService.signup(account).enqueue(object : Callback<getResponse> {
                    override fun onResponse(call: Call<getResponse>, response: Response<getResponse>) {
                        if (response.isSuccessful) {
                            val signupResponse = response.body()
                            if (signupResponse != null && signupResponse.code == 1) {
                                Toast.makeText(this@MainActivity3, "Sign up successful!", Toast.LENGTH_LONG).show()
                                val intent = Intent(this@MainActivity3, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@MainActivity3, "Failed to sign up", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity3, "Error signing up: ${response.message()}", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<getResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity3, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}
