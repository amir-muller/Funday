package com.example.funday.Data

import androidx.room.*

@Dao
interface CategoryDao {

    @Insert
    suspend fun insert(category: Category)

    @Insert
    suspend fun insertAll(categories: List<Category>)

    @Query("SELECT * FROM categories")
    suspend fun getAll(): List<Category>

    @Query("SELECT * FROM categories WHERE type = :type")
    suspend fun getByType(type: CategoryType): List<Category>
}