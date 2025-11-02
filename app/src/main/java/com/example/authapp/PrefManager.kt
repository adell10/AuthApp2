package com.example.authapp

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context){

    private val  sharedPreferences: SharedPreferences

    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_LAST_LOGIN = "lastLoginTime"
        private var instance: PrefManager? = null

        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREFS_FILENAME,
            Context.MODE_PRIVATE)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }

    fun setUsername(username: String) {
        saveUsername(username)
    }

    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }


    // wrapper agar kode lama tetap kompatibe
    fun setPassword(password: String) {
        savePassword(password)
    }

    fun getUsername(): String{
        return sharedPreferences.getString(KEY_USERNAME, "") ?: ""
    }

    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
    }

    fun saveLastLoginTime(time: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_LAST_LOGIN, time)
        editor.apply()
    }

    fun getLastLoginTime(): String {
        return sharedPreferences.getString(KEY_LAST_LOGIN, "Belum pernah login") ?: "Belum pernah login"
    }

    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
