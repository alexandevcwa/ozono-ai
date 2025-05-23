package com.ozono.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ozono.R

class KDialog(private val context: Context) {
    private lateinit var icon: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var button: Button
    private var view: View? = null

    fun inflate(): KDialog {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_k, null)
        icon = view!!.findViewById(R.id.icon)
        title = view!!.findViewById(R.id.title)
        description = view!!.findViewById(R.id.description)
        button = view!!.findViewById(R.id.button)
        return this
    }

    fun show(kProperties: KProperties) {
        Dialog(context, R.style.KDialog).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(kProperties.cancelable)
            icon.setImageResource(kProperties.icon!!)
            icon.setColorFilter(context.resources.getColor(kProperties.iconColor!!))
            title.text = kProperties.title
            description.text = kProperties.description

            if (null == kProperties.textButton) {
                button.text = "Ok"
            } else {
                button.text = kProperties.textButton
            }

            button.setOnClickListener {
                hide()
                val function = kProperties.onAction
                if (null != function) {

                    function.invoke()
                }
                dismiss()
            }

            setContentView(view!!)

            setOnCancelListener {
                dismiss()
            }

        }.show()
    }
}