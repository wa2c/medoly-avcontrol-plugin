package com.wa2c.android.medoly.plugin.action.avcontrol.source.local

import android.content.Context
import com.wa2c.android.medoly.plugin.action.avcontrol.R
import com.wa2c.android.prefs.Prefs
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * App preferences.
 */
@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs: Prefs = Prefs(context, FILE_NAME)

    /** Host address of AV receiver. */
    var hostAddress: String?
        get() = prefs.getStringOrNull(R.string.pref_key_set_host)
        set(value) {
            prefs.putString(R.string.pref_key_set_host, value)
        }

    /** Previous host of AV receiver. */
    var previousAddress: String?
        get() = prefs.getStringOrNull(R.string.pref_key_previous_host)
        set(value) {
            prefs.putString(R.string.pref_key_previous_host, value)
        }

    /** Show success message. */
    val showSuccessMessage: Boolean
        get() = prefs.getBoolean(
            R.string.pref_key_show_success_message,
            defRes = R.bool.pref_default_show_success_message
        )

    /** Show failure message. */
    val showFailureMessage: Boolean
        get() = prefs.getBoolean(
            R.string.pref_key_show_failure_message,
            defRes = R.bool.pref_default_show_failure_message
        )

    companion object {
        const val FILE_NAME = "app-preferences"
    }
}
