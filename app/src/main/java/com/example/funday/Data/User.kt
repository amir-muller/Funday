package com.example.funday.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // The local db will auto generate this
    val name: String,
    val surname: String,
    val email: String,
    val password: String
)