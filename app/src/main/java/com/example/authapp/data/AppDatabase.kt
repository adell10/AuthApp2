//package com.example.authapp.data
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//// app database
//// digunakan untuk source of database dari aplikasi
//
//@Database(entities = [Contact::class], version = 1, exportSchema = true)
//abstract class AppDatabase: RoomDatabase() {
//
//    abstract fun contactDao(): ContactDao
//
//    companion object {
//
//        @Volatile private var INSTANCE: AppDatabase? = null
//
//        // get instance dari database
//        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
//            INSTANCE ?: Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase::class.java,
//                name = "contacts.db"
//            ).build().also { INSTANCE = it }
//        }
//    }
//
//}

package com.example.authapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(
//    entities = [Contact::class, LoginHistory::class], // daftar tabel
//    version = 2,
//    exportSchema = false
//)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun contactDao(): ContactDao
//    abstract fun loginHistoryDao(): LoginHistoryDao
//
//    companion object {
//        @Volatile private var INSTANCE: AppDatabase? = null
//
//        fun get(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "contacts.db" // nama file database-nya
//                )
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}
@Database(entities = [Contact::class, User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun userDao(): UserDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                ).build()
            }
            return INSTANCE!!
        }
    }
}
