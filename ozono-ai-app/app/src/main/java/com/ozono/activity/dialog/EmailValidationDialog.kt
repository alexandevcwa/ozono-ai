package com.ozono.activity.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import com.ozono.R
import com.ozono.service.AuthService
import com.ozono.util.KDialog
import com.ozono.util.KIcon
import com.ozono.util.KProperties

class EmailValidationDialog(private val context: Context, private val authService: AuthService) {

    private lateinit var codeEditText: AppCompatEditText
    private lateinit var validateButton: Button
    private lateinit var cancelButton: Button

    private lateinit var dialog: Dialog

    init {
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_email_validation, null)
        codeEditText = view.findViewById(R.id.code)
        validateButton = view.findViewById(R.id.validate)
        cancelButton = view.findViewById(R.id.cancel_button)

        dialog = Dialog(context, R.style.KDialog).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            setContentView(view)
        }
    }

    private fun initListeners() {
        validateButton.setOnClickListener {
            if (!codeEditText.text!!.isEmpty()) {
                val code = codeEditText.text.toString().toIntOrNull()
                if (null == code) {
                    codeEditText.error = "Code is invalid"
                } else {
                    validateEmail(code)
                }
            } else {
                codeEditText.error = "Code is required"
            }
        }

        dialog.setOnCancelListener {
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun validateEmail(code: Int) {

        val result = authService.emailConfirmation(code)

        if (result.isSuccess) {
            dialog.hide()
            KDialog(context).inflate().show(
                KProperties.Builder()
                    .title("Email Validation")
                    .description(result.getOrNull()!!.message)
                    .icon(KIcon.SUCCESS)
                    .cancelable(false)
                    .onAction {
                        dialog.dismiss()
                    }
                    .build()
            )
        } else {
            dialog.hide()
            KDialog(context).inflate().show(
                KProperties.Builder()
                    .title("Email Validation Failed")
                    .description(result.exceptionOrNull()!!.message!!)
                    .icon(KIcon.WARNING)
                    .cancelable(false)
                    .onAction {
                        dialog.show()
                    }
                    .build()
            )
        }
    }

    fun show() {
        dialog.show()
    }
}