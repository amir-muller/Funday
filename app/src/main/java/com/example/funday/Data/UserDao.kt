package com.example.funday.Data

import androidx.room.*

@Dao
interface UserDao {

    // CREATE USER (REGISTER)
    @Insert
    suspend fun insert(user: User)

    // LOGIN CHECK
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User?

    // CHECK IF EMAIL EXISTS (REGISTER VALIDATION)
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    // OPTIONAL (you already had this)
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?
}