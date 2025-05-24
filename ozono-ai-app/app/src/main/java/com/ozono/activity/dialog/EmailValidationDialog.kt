package com.ozono.activity.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import com.ozono.R
import com.ozono.activity.HomeActivity
import com.ozono.service.AuthService
import com.ozono.util.KDialog
import com.ozono.util.KIcon
import com.ozono.util.KProperties

class EmailValidationDialog(
    private val context: Context,
    private val authService: AuthService
) {

    private val dialog: Dialog = Dialog(context, R.style.KDialog)
    private lateinit var codeEditText: AppCompatEditText
    private lateinit var validateButton: Button
    private lateinit var cancelButton: Button

    init {
        setupDialog()
        setupListeners()
    }

    private fun setupDialog() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_email_validation, null)

        codeEditText = view.findViewById(R.id.code)
        validateButton = view.findViewById(R.id.validate)
        cancelButton = view.findViewById(R.id.cancel_button)

        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(view)
        }
    }

    private fun setupListeners() {
        validateButton.setOnClickListener {
            validateInputCode()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnCancelListener {
            dialog.dismiss()
        }
    }

    private fun validateInputCode() {
        val codeText = codeEditText.text?.toString()

        if (codeText.isNullOrBlank()) {
            codeEditText.error = "Code is required"
            return
        }

        val code = codeText.toIntOrNull()
        if (code == null) {
            codeEditText.error = "Code is invalid"
            return
        }

        validateEmail(code)
    }

    private fun validateEmail(code: Int) {
        runCatching {
            authService.emailConfirmation(code)
        }.onSuccess { result ->
            if (result.isSuccess) {
                showSuccessDialog(result.getOrNull()?.message.orEmpty())
            } else {
                showErrorDialog(result.exceptionOrNull()?.message.orEmpty())
            }
        }.onFailure {
            showErrorDialog(it.message.orEmpty())
        }
    }

    private fun showSuccessDialog(message: String) {
        dialog.hide()
        KDialog(context).inflate().show(
            KProperties.Builder()
                .title("Email Validation")
                .description(message)
                .icon(KIcon.SUCCESS)
                .cancelable(false)
                .onAction {
                    navigateToHome()
                }
                .build()
        )
    }

    private fun showErrorDialog(message: String) {
        dialog.hide()
        KDialog(context).inflate().show(
            KProperties.Builder()
                .title("Email Validation Failed")
                .description(message)
                .icon(KIcon.WARNING)
                .cancelable(false)
                .onAction {
                    dialog.show()
                }
                .build()
        )
    }

    private fun navigateToHome() {
        dialog.hide()
        Intent(context, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(this)
        }
        dialog.dismiss()
    }

    fun show() {
        dialog.show()
    }
}
