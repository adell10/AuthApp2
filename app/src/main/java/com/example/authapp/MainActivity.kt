package com.example.authapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.authapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //assign binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        //set content view
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()

        //handle UI with binding
        with(binding) {

            val loggedInUsername = prefManager.getUsername()
            txtUsername.text = "Login sebagai: ${loggedInUsername}"

            btnLogout.setOnClickListener {
                prefManager.setLoggedIn(false)
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }
            btnClear.setOnClickListener {
                prefManager.clear()
                finish()
            }
        }
    }

    fun checkLoginStatus() {
        val isLoggedIn = prefManager.isLoggedIn()

        if (!isLoggedIn) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}