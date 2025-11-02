package com.example.authapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_history")
data class LoginHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val timestamp: Long
)
