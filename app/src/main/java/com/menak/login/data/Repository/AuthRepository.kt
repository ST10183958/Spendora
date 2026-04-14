package com.menak.login.data.Repository

import com.menak.login.data.Dao.UserDao
import com.menak.login.data.Entity.UserEntity

class AuthRepository(private val userDao: UserDao) {

    suspend fun register(username: String, password: String): Result<String> {
        val existingUser = userDao.getUserByUsername(username)

        return if (existingUser != null) {
            Result.failure(Exception("Username already exists"))
        } else {
            userDao.insertUser(
                UserEntity(
                    username = username,
                    password = password
                )
            )
            Result.success("Registration successful")
        }
    }

    suspend fun login(username: String, password: String): Result<UserEntity> {
        val user = userDao.login(username, password)
        return if (user != null) {
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid username or password"))
        }
    }
}