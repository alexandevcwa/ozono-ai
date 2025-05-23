package com.ozono.activity

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ozono.R
import com.ozono.activity.dialog.EmailValidationDialog
import com.ozono.model.Register
import com.ozono.service.AuthService
import com.ozono.util.DatePickerFragment
import com.ozono.util.DateUtil
import com.ozono.util.KDialog
import com.ozono.util.KIcon
import com.ozono.util.KProperties
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    @Inject
    lateinit var authService: AuthService

    private lateinit var firstName: AppCompatEditText
    private lateinit var lastName: AppCompatEditText
    private lateinit var email: AppCompatEditText
    private lateinit var password: AppCompatEditText
    private lateinit var birthDate: AppCompatEditText
    private lateinit var register: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        firstName = findViewById(R.id.first_name)
        lastName = findViewById(R.id.last_name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        birthDate = findViewById(R.id.birth_date)
        register = findViewById(R.id.register)
    }

    private fun initListeners() {
        birthDate.setOnClickListener {
            val datePicket = DatePickerFragment(::onDateSelected)
            datePicket.show(this.supportFragmentManager, "datePicker")
        }

        register.setOnClickListener {
            register()
        }
    }

    private fun register() {
        if (!validations()) {
            return
        }
        val registerObj = buildRegisterObject()
        val result = authService.register(registerObj)

        if (result.isSuccess) {
            KDialog(this).inflate()
                .show(
                    KProperties.Builder()
                        .title("Success")
                        .description(result.getOrNull()!!.message)
                        .icon(KIcon.SUCCESS)
                        .textButton("Confirm")
                        .onAction {
                            confirmEmail()
                        }
                        .build()
                )
        } else {
            val error = result.exceptionOrNull()
            KDialog(this).inflate()
                .show(
                    KProperties.Builder()
                        .title("Registration failed")
                        .description(error!!.message!!)
                        .icon(KIcon.ERROR)
                        .build()
                )
        }
    }

    private fun confirmEmail() {
        EmailValidationDialog(this, authService).show()
    }

    private fun buildRegisterObject(): Register {
        return Register(
            email.text.toString(),
            password.text.toString(),
            firstName.text.toString(),
            lastName.text.toString(),
            DateUtil.parse(birthDate.text.toString())
        )
    }

    private fun onDateSelected(day: Int, month: Int, year: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val selectedDate = calendar.time
        val currentDate = Calendar.getInstance().time

        if (selectedDate.after(currentDate)) {
            birthDate.error = "La fecha de la factura no puede ser mayor a la fecha actual"
        } else {
            birthDate.setText(DateUtil.format(calendar))
        }
    }

    private fun validations(): Boolean {
        var isOk = true

        if (firstName.text!!.isEmpty()) {
            firstName.error = "Name is required"
            isOk = false
        }
        if (lastName.text!!.isEmpty()) {
            lastName.error = "Last name is required"
            isOk = false
        }
        if (email.text!!.isEmpty()) {
            email.error = "Email is required"
            isOk = false
        }
        if (password.text!!.isEmpty()) {
            password.error = "Password is required"
            isOk = false
        }
        if (birthDate.text!!.isEmpty()) {
            birthDate.error = "Birth date is required"
            isOk = false
        }
        return isOk
    }
}