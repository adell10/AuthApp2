package com.example.authapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mycontact.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)

        // ambil username dari PrefManager
        val username = prefManager.getUsername()
        binding.txtProfileName.text = username ?: "Guest"
        binding.txtProfileEmail.text = "${username?.lowercase() ?: "guest"}@example.com"

        // tombol kembali
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
