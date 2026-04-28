package com.example.funday.Data

import androidx.room.*

@Dao
interface TransactionDao {

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE userId = :userId")
    suspend fun getTransactionsForUser(userId: Int): List<Transaction>
}