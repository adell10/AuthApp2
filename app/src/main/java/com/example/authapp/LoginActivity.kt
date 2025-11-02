package com.example.authapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.authapp.data.AppDatabase
import com.example.mycontact.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var prefManager: PrefManager
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        db = AppDatabase.get(this)

        with(binding) {
            btnLogin.setOnClickListener {
                val username = edtUsername.text.toString().trim()
                val password = edtPassword.text.toString().trim()

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Semua field harus diisi",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // 🔹 cek user dari database Room
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = db.userDao().getUser(username, password)
                        runOnUiThread {
                            if (user != null) {
                                prefManager.setLoggedIn(true)
                                prefManager.setUsername(username)
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login berhasil",
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Username atau password salah",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }

            txtRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
