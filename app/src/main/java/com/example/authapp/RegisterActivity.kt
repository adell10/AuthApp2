//package com.example.authapp
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.mycontact.databinding.ActivityRegisterBinding
//
//class RegisterActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityRegisterBinding
//    private  lateinit var prefManager: PrefManager
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        //assign binding
//        binding = ActivityRegisterBinding.inflate(layoutInflater)
//
//        //set content view
//        setContentView(binding.root)
//
//        prefManager = PrefManager.getInstance(this)
//
//
//        //handle UI
//        with(binding){
//            btnRegister.setOnClickListener {
//                val username = edtUsername.text.toString()
//                val password = edtPassword.text.toString()
//                val confirmPassword = edtPasswordConfirm.text.toString()
//
//                if(username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//                    Toast.makeText(
//                        this@RegisterActivity,
//                        "Semua field harus diisi",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }else if (password != confirmPassword) {
//                    Toast.makeText(
//                        this@RegisterActivity,
//                        "Password dan confirm password tidak sesuai.",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }else {
//                    prefManager.saveUsername(username)
//                    prefManager.savePassword(password)
//                    prefManager.setLoggedIn(true)
//                    checkLoginStatus()
//                }
//            }
//            txtLogin.setOnClickListener {
//                startActivity(Intent(this@RegisterActivity,
//                    LoginActivity::class.java))
//            }
//        }
//    }
//
//    private fun checkLoginStatus() {
//        var isLoggedIn = prefManager.isLoggedIn()
//        if (isLoggedIn) {
//            Toast.makeText(
//                this@RegisterActivity,
//                "Register berhasil",
//                Toast.LENGTH_SHORT
//            ).show()
//
//            var intent = Intent(this@RegisterActivity, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }else {
//            Toast.makeText(
//                this@RegisterActivity, "Registrasi gagal", Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
//}

package com.example.authapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.authapp.data.AppDatabase
import com.example.authapp.data.User
import com.example.mycontact.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var prefManager: PrefManager
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefManager = PrefManager.getInstance(this)
        db = AppDatabase.get(this)

        with(binding) {
            btnRegister.setOnClickListener {
                val username = edtUsername.text.toString().trim()
                val password = edtPassword.text.toString().trim()
                val confirmPassword = edtPasswordConfirm.text.toString().trim()

                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Semua field harus diisi",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (password != confirmPassword) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Password dan konfirmasi tidak sesuai",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Simpan user ke database
                    CoroutineScope(Dispatchers.IO).launch {
                        val user = User(username = username, password = password)
                        db.userDao().insert(user)

                        runOnUiThread {
                            prefManager.saveUsername(username)
                            prefManager.savePassword(password)
                            prefManager.setLoggedIn(true)
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi berhasil!",
                                Toast.LENGTH_SHORT
                            ).show()

                            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            }

            txtLogin.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }
}


