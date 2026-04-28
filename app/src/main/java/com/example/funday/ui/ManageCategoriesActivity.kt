package com.example.funday.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.funday.Data.AppDatabase
import com.example.funday.Data.Category
import com.example.funday.Data.CategoryType
import com.example.funday.R
import kotlinx.coroutines.launch

class ManageCategoriesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var etCategoryName: EditText
    private lateinit var rgType: RadioGroup
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_categories)

        recyclerView = findViewById(R.id.rvCategories)
        etCategoryName = findViewById(R.id.etCategoryName)
        rgType = findViewById(R.id.rgCategoryType)
        btnAdd = findViewById(R.id.btnAddCategory)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadCategories()

        btnAdd.setOnClickListener {
            saveCategory()
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@ManageCategoriesActivity)
            val categories = db.categoryDao().getAll()
            runOnUiThread {
                recyclerView.adapter = CategoryAdapter(categories)
            }
        }
    }

    private fun saveCategory() {
        val name = etCategoryName.text.toString()
        val type = if (rgType.checkedRadioButtonId == R.id.rbIncome)
            CategoryType.INCOME else CategoryType.EXPENSE

        if (name.isNotEmpty()) {
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(this@ManageCategoriesActivity)
                db.categoryDao().insert(Category(name = name, type = type))
                loadCategories() // Refresh list
                runOnUiThread { etCategoryName.text.clear() }
            }
        }
    }
}