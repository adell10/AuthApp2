package com.example.authapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoginHistoryDao {

    // Menyimpan satu riwayat login
    @Insert
    suspend fun insert(history: LoginHistory)

    // Mengambil semua riwayat login (urut terbaru dulu)
    @Query("SELECT * FROM login_history ORDER BY timestamp DESC")
    suspend fun getAll(): List<LoginHistory>

    // Menghapus semua riwayat login (kalau mau reset)
    @Query("DELETE FROM login_history")
    suspend fun clear()
}
