package com.example.funday.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget (
    @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val userId: Int,
        val categoryId: Int,
        val minimumGoal: Double,
        val maximumGoal: Double,
        val period: String // monthly, weekly, etc
    )