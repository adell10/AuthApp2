package com.example.authapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.authapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //assign binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        //set content view
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        //handle UI
        with(binding) {
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString()
                val password = edtPassword.text.toString()

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Semua field harus diisi",
                        Toast.LENGTH_SHORT
                    ).show()
                }else {
                    if (isValidCredential()) {
                        prefManager.setLoggedIn(true)
                        checkLoginStatus()
                    } else {
                        //text = Username atau password salah
                        Toast.makeText(
                            this@LoginActivity,
                            "Username atau password salah",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            txtRegister.setOnClickListener {
                var intent = Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
                startActivity(intent)
            }


        }
    }

    private fun isValidCredential(): Boolean{
        val username = prefManager.getUsername()
        val password = prefManager.getPassword()
        val inputUsername = binding.edtUsername.text.toString()
        val inputPassword = binding.edtPassword.text.toString()

        return username == inputUsername && password == inputPassword
    }

    private fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()
        if (isLoggedIn) {
            Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }else {
            Toast.makeText(this@LoginActivity, "Login gagal", Toast.LENGTH_SHORT
            ).show()
        }
    }
}