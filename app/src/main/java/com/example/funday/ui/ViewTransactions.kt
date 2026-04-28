package com.example.funday.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.funday.Data.AppDatabase
import com.example.funday.R
import kotlinx.coroutines.launch

class ViewTransactions : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvTotalAmount: TextView
    private lateinit var tvEmptyMessage: TextView
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_transactions)

        userId = intent.getIntExtra("User_ID", -1)

        recyclerView = findViewById(R.id.recyclerViewTransactions)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage)

        recyclerView.layoutManager = LinearLayoutManager(this)

        loadTransactions()

    }

    private fun loadTransactions() {
        lifecycleScope.launch {
            //get the database instance
            val db = AppDatabase.getDatabase(this@ViewTransactions)

            //fetch transactions for the specific user
            val transactions = db.transactionDao().getTransactionsForUser(userId)

            //switch back to the UI thread, update the views
            runOnUiThread {
                if (transactions.isEmpty()) {
                    tvEmptyMessage.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    tvEmptyMessage.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE

                    // 4. Calculate total amount and update the header [cite: 29]
                    val total = transactions.sumOf { it.amount }
                    tvTotalAmount.text = String.format("R %.2f", total)

                    // 5. Connect the Adapter to the RecyclerView [cite: 31]
                    recyclerView.adapter = TransactionAdapter(transactions)
                }
            }
        }
    }

}