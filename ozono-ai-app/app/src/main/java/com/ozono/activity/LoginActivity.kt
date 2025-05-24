package com.ozono.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ozono.R
import com.ozono.service.AuthService
import com.ozono.util.KDialog
import com.ozono.util.KIcon
import com.ozono.util.KProperties
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    @Inject lateinit var authService: AuthService

    private lateinit var emailEditText: AppCompatEditText
    private lateinit var passwordEditText: AppCompatEditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initListeners()
    }

    private fun initComponents() {
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.password_edit_text)
        loginButton = findViewById(R.id.login_button)
        signUpButton = findViewById(R.id.sing_up_button)
    }

    private fun initListeners() {
        loginButton.setOnClickListener { login() }
        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun login() {
        val email = emailEditText.text?.toString()?.trim()
        val password = passwordEditText.text?.toString()?.trim()

        if (email.isNullOrEmpty()) {
            emailEditText.error = "Email is required"
            return
        }

        if (password.isNullOrEmpty()) {
            passwordEditText.error = "Password is required"
            return
        }

        runCatching {
            authService.login(email, password)
        }.onSuccess { result ->
            if (result.isSuccess) {
                goToHomeActivity()
            } else {
                showErrorDialog(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }.onFailure {
            showErrorDialog(it.message ?: "Unexpected error")
        }
    }

    private fun showErrorDialog(message: String) {
        KDialog(this).inflate().show(
            KProperties.Builder()
                .title("Failed Authentication")
                .description(message)
                .icon(KIcon.WARNING)
                .build()
        )
    }

    private fun goToHomeActivity() {
        Intent(this, HomeActivity::class.java).apply {
            startActivity(this)
        }

        finish()
    }
}
