package com.example.funday.Data

import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromCategoryType(type: CategoryType): String {
        return type.name
    }

    @TypeConverter
    fun toCategoryType(value: String): CategoryType {
        return CategoryType.valueOf(value)
    }
}