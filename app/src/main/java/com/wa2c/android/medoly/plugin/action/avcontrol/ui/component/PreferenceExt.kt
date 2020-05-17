package com.wa2c.android.medoly.plugin.action.avcontrol.ui.component

import androidx.annotation.StringRes
import androidx.preference.*
import com.wa2c.android.medoly.plugin.action.avcontrol.R


fun PreferenceFragmentCompat.setListener(@StringRes prefKeyRes: Int, listener: (() -> Unit)) {
    (findPreference(getString(prefKeyRes)) as? Preference)?.setOnPreferenceClickListener {
        listener.invoke()
        true
    }
}

fun <T : Preference> PreferenceFragmentCompat.preference(@StringRes prefKeyRes: Int): T? {
    return preference(getString(prefKeyRes))
}

fun <T : Preference> PreferenceFragmentCompat.preference(prefKey: String): T? {
    return findPreference(prefKey)
}

fun PreferenceFragmentCompat.updatePrefSummary(@StringRes prefKeyRes: Int, force: Boolean = false) {
    this.updatePrefSummary(requireContext().getString(prefKeyRes), force)
}

fun PreferenceFragmentCompat.updatePrefSummary(prefKey: String, force: Boolean = false) {
    val p = preference(prefKey) as? Preference ?: return

    // for instance type
    when (p) {
        is ListPreference -> {
            // ListPreference
            p.value = p.sharedPreferences.getString(p.key, "")
            setSummaryValue(p, p.entry)
        }
        is MultiSelectListPreference -> {
            // MultiSelectListPreference
            val stringSet = p.sharedPreferences.getStringSet(p.key, null)
            var text = ""
            if (stringSet != null && stringSet.size > 0) {
                p.values = stringSet // update value once
                val builder = StringBuilder()
                (p.entries.indices)
                    .filter { stringSet.contains(p.entryValues[it]) }
                    .forEach { builder.append(p.entries[it]).append(",") }
                if (builder.isNotEmpty()) {
                    text = builder.substring(0, builder.length - 1) // remove end comma
                }
            }
            setSummaryValue(p, text)
        }
        is EditTextPreference -> {
            // EditTextPreference
            val text = p.sharedPreferences.getString(p.key, "")
            p.text = text // update once
            setSummaryValue(p, text)
        }
        else -> {
            if (force) {
                val text = p.sharedPreferences.getString(p.key, "")
                setSummaryValue(p, text)
            }
        }
    }
}

/**
 * Add value on preference summary.
 */
private fun setSummaryValue(p: Preference, value: CharSequence?) {
    val mask = "********"
    val escapeMask = """\*\*\*\*\*\*\*\*"""
    val literal = (p.context.getString(R.string.pref_summary_current_value, mask)).let {
        val from = Regex("(?!\\\\)(\\W)").replace(it, "\\\\\$1") // NOTE: Escape regex characters
        from.replace(escapeMask, ".*")
    }
    val plainSummary = Regex("\n$literal$").replace(p.summary, "")
    val valueSummary = p.context.getString(R.string.pref_summary_current_value, value ?: "")
    p.summary = plainSummary + "\n" + valueSummary
}
