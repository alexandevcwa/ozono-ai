package com.ozono.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.ozono.R
import kotlinx.coroutines.*

class KWaitingDialog(
    private val context: Context,
    private val title: String,
    private val cancelable: Boolean,
    private val function: suspend () -> Unit
) {
    private var view: View? = null
    private lateinit var titleTextView: AppCompatTextView
    private lateinit var progressBar: ProgressBar
    private lateinit var dialog: Dialog
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
        titleTextView = view!!.findViewById(R.id.title_text_view)
        progressBar = view!!.findViewById(R.id.progress_bar)
        initDialog()
    }

    private fun initDialog() {
        dialog = Dialog(context, R.style.KDialog).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(cancelable)
            setContentView(view!!)
        }

        titleTextView.text = title

    }

    fun show(){
        dialog.show()


        scope.launch {
            progressBar.visibility = View.VISIBLE
            try {
                withContext(Dispatchers.IO) {
                    function.invoke()
                }
            } finally {
                progressBar.visibility = View.GONE
                dialog.dismiss()
            }
        }
    }
}
