package com.ozono.util

import com.ozono.R


data class KProperties(
    val icon: Int?,
    val title: String?,
    val description: String?,
    val onAction: () -> Unit?,
    val cancelable: Boolean = true,
    val iconColor: Int? = null,
    val textButton: String? = null
) {

    class Builder {
        private var icon: Int? = null
        private var title: String? = null
        private var description: String? = null
        private var onAction: () -> Unit? = {}
        private var cancelable: Boolean = true
        private var iconColor: Int? = null
        private var textButton: String? = null

        fun icon(icon: KIcon): Builder = apply {
            when (icon) {
                KIcon.SUCCESS -> {
                    iconColor = R.color.alert_info
                    this.icon = R.drawable.ic_success
                }

                KIcon.ERROR -> {
                    iconColor = R.color.alert_error
                    this.icon = R.drawable.ic_error
                }

                KIcon.WARNING -> {
                    iconColor = R.color.alert_warning
                    this.icon = R.drawable.ic_warning
                }

                KIcon.INFO -> {
                    iconColor = R.color.alert_info
                    this.icon = R.drawable.ic_info
                }

                KIcon.ALERT -> {
                    iconColor = R.color.alert_info
                    this.icon = R.drawable.ic_alert
                }

                KIcon.QUESTION -> {
                    iconColor = R.color.alert_info
                    this.icon = R.drawable.ic_question
                }
            }
        }

        fun title(title: String) = apply { this.title = title }
        fun description(description: String) = apply { this.description = description }
        fun onAction(onAction: () -> Unit?) = apply { this.onAction = onAction }
        fun cancelable(cancelable: Boolean) = apply { this.cancelable = cancelable }
        fun textButton(textButton: String) = apply { this.textButton = textButton }

        fun build() =
            KProperties(icon, title, description, onAction, cancelable, iconColor, textButton)
    }
}