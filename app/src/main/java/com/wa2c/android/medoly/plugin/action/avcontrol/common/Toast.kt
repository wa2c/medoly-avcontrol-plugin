package com.wa2c.android.medoly.plugin.action.avcontrol.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.pranavpandey.android.dynamic.toasts.DynamicToast
import com.wa2c.android.medoly.plugin.action.avcontrol.R

/**
 * Toast receiver.
 */
class ToastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        DynamicToast.make(
            context.applicationContext,
            intent.getStringExtra(MESSAGE_TOAST),
            ContextCompat.getDrawable(context, R.drawable.ic_notification)
        ).show()
    }
}

private const val MESSAGE_TOAST = "message"

fun Context.showToast(@StringRes stringId: Int) {
    showToast(getString(stringId))
}

fun Context.showToast(text: String) {
    val intent = Intent(this, ToastReceiver::class.java)
    intent.putExtra(MESSAGE_TOAST, text)
    this.sendBroadcast(intent)
    logD(text)
}
