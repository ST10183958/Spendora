package com.menak.login.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.menak.login.data.Dao.CategoryDao
import com.menak.login.data.Dao.ExpenseDao
import com.menak.login.data.Dao.UserDao
import com.menak.login.data.Entity.CategoryEntity
import com.menak.login.data.Entity.ExpenseEntity
import com.menak.login.data.Entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CategoryEntity::class,
        ExpenseEntity::class
    ],
    version = 3,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}