package com.wa2c.android.medoly.plugin.action.avcontrol.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Toast receiver.
 */
class ToastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context.applicationContext, intent.getStringExtra(MESSAGE_TOAST), Toast.LENGTH_SHORT).show()
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
