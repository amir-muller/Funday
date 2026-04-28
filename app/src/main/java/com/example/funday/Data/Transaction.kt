package com.example.funday.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val userId: Int,  // ADD THIS - it was missing
    val CategoryId: Int,
    val date: Long,
    val description: String,
    val photoUri: String? = null
)