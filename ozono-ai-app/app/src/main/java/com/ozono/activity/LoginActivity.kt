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

    @Inject
    lateinit var authService: AuthService

    private lateinit var emailEditText: AppCompatEditText
    private lateinit var passwordEditText: AppCompatEditText
    private lateinit var loginButton: Button
    private lateinit var singUpButton: Button

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
        singUpButton = findViewById(R.id.sing_up_button)
    }

    private fun initListeners() {
        loginButton.setOnClickListener {
            login()
        }

        singUpButton.setOnClickListener {
            Intent(this, SignUpActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun login() {
        val emailEditable = emailEditText.text
        val passwordEditable = passwordEditText.text

        if (emailEditable!!.isEmpty() || passwordEditable!!.isEmpty()) {
            return
        }

        val email = emailEditable.toString()
        val password = passwordEditable.toString()

        val result = authService.login(email, password)

        if (result.isSuccess) {
            print("Login successful")
        } else {
            val error = result.exceptionOrNull()
            KDialog(this).inflate().show(
                KProperties.Builder()
                    .title("Failed authentication")
                    .description(error!!.message!!)
                    .icon(KIcon.WARNING)
                    .build()
            )
        }
    }

}