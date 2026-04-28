package com.example.funday.Data

import androidx.room.*

@Dao
interface BudgetDao {

    @Insert
    suspend fun insert(budget: Budget)

    @Update
    suspend fun update(budget: Budget)

    @Delete
    suspend fun delete(budget: Budget)

    @Query("SELECT * FROM budgets WHERE userId = :userId")
    suspend fun getBudgetsForUser(userId: Int): List<Budget>

    @Query("SELECT * FROM budgets WHERE categoryId = :categoryId")
    suspend fun getBudgetsForCategory(categoryId: Int): List<Budget>
}