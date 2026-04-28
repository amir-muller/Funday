package com.example.funday.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.funday.Data.DatabaseInstance
import com.example.funday.R
import kotlinx.coroutines.launch
import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.EditText
import com.example.funday.Data.Transaction
import com.example.funday.Data.AppDatabase

class MainActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null

    private val imagePicker =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ){ uri: Uri? ->

            if(uri != null){
                selectedImageUri = uri

                findViewById<ImageView>(R.id.expenseImage)
                    .setImageURI(uri)
            }
        }

    private lateinit var welcomeText: TextView
    private lateinit var balanceText: TextView
    private lateinit var amountInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var addTransactionButton: Button

    private lateinit var viewTransactionsButton: Button
    private lateinit var categoriesButton: Button

    private lateinit var goalsButton: Button

    private lateinit var logoutButton: Button

    private var userId: Int = -1
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get user data from intent
        userId = intent.getIntExtra("USER_ID", -1)
        userName = intent.getStringExtra("USER_NAME") ?: "User"

        // Initialize views
        welcomeText = findViewById(R.id.welcomeText)
        balanceText = findViewById(R.id.balanceText)
        amountInput = findViewById(R.id.expenseAmount)
        descriptionInput = findViewById(R.id.expenseDescription)
        addTransactionButton = findViewById(R.id.addTransactionButton)
        viewTransactionsButton = findViewById(R.id.viewTransactionsButton)
        categoriesButton = findViewById(R.id.categoriesButton)
        goalsButton = findViewById(R.id.goalsButton)
        logoutButton = findViewById(R.id.logoutButton)
        val photoButton = findViewById<Button>(R.id.btnPhoto)

        // Set welcome message
        welcomeText.text = "Welcome, $userName!"

        goalsButton.setOnClickListener{
            Toast.makeText(
                this,
                "Set Min/Max goals screen next",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Load balance
        loadBalance()

        // Button clicks
        addTransactionButton.setOnClickListener {

            val amountText = amountInput.text.toString()
            val descriptionText = descriptionInput.text.toString()

            if(amountText.isEmpty()){
                Toast.makeText(
                    this,
                    "Enter amount",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val transaction = Transaction(
                amount = amountText.toDouble(),
                userId = 1,
                CategoryId = 1,
                date = System.currentTimeMillis(),
                description = descriptionText,
                photoUri = selectedImageUri?.toString()
            )

            lifecycleScope.launch {

                val db = AppDatabase.getDatabase(applicationContext)

                db.transactionDao().insert(transaction)

                runOnUiThread {
                    Toast.makeText(
                        this@MainActivity,
                        "Expense Saved!",
                        Toast.LENGTH_SHORT
                    ).show()

                    amountInput.text.clear()
                    descriptionInput.text.clear()
                }
            }
        }
        photoButton.setOnClickListener {
            imagePicker.launch("image/*")
        }

        viewTransactionsButton.setOnClickListener {
            Toast.makeText(this, "View Transactions - Coming soon!", Toast.LENGTH_SHORT).show()
        }

        categoriesButton.setOnClickListener {
            Toast.makeText(this, "Manage Categories - Coming soon!", Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun loadBalance() {
        lifecycleScope.launch {
            val db = DatabaseInstance.getDatabase(this@MainActivity)
            val transactions = db.transactionDao().getTransactionsForUser(userId)

            var balance = 0.0
            for (transaction in transactions) {
                // For now, just sum all amounts
                // Later you'll add income/expense logic with categories
                balance += transaction.amount
            }

            balanceText.text = String.format("$%.2f", balance)
        }
    }

    private fun logout() {
        Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
        finish()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
    }
}