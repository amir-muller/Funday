package com.example.funday.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.funday.Data.DatabaseInstance
import com.example.funday.Data.User
import com.example.funday.R
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = DatabaseInstance.getDatabase(this@LoginActivity)
                val user = db.userDao().login(email, password)

                if (user != null) {
                    Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("USER_ID", user.id)
                    intent.putExtra("USER_NAME", user.name)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@LoginActivity, "Wrong email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerButton.setOnClickListener {
            showRegisterDialog()
        }
    }

    private fun showRegisterDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_register, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.dialogNameInput)
        val surnameInput = dialogView.findViewById<EditText>(R.id.dialogSurnameInput)
        val emailInput = dialogView.findViewById<EditText>(R.id.dialogEmailInput)
        val passwordInput = dialogView.findViewById<EditText>(R.id.dialogPasswordInput)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Register")
            .setView(dialogView)
            .setPositiveButton("Register") { _, _ ->
                val name = nameInput.text.toString().trim()
                val surname = surnameInput.text.toString().trim()
                val email = emailInput.text.toString().trim()
                val password = passwordInput.text.toString()

                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                lifecycleScope.launch {
                    val db = DatabaseInstance.getDatabase(this@LoginActivity)
                    val existingUser = db.userDao().getUserByEmail(email)

                    if (existingUser == null) {
                        val newUser = User(
                            name = name,
                            surname = surname,
                            email = email,
                            password = password
                        )
                        db.userDao().insert(newUser)
                        Toast.makeText(this@LoginActivity, "Registration successful! Please login.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Email already exists!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }
}