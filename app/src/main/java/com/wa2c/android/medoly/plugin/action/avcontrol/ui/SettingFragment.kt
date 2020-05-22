package com.wa2c.android.medoly.plugin.action.avcontrol.ui

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.wa2c.android.medoly.plugin.action.avcontrol.R
import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import com.wa2c.android.medoly.plugin.action.avcontrol.ui.component.setListener
import com.wa2c.android.medoly.plugin.action.avcontrol.ui.component.updatePrefSummary
import com.wa2c.android.medoly.plugin.action.avcontrol.ui.dialog.HostInputDialog

/**
 * Setting fragment
 */
class SettingFragment : PreferenceFragmentCompat() {

    /** On change settings. */
    private val listener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key -> updatePrefSummary(key) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = AppPreferences.FILE_NAME
        addPreferencesFromResource(R.xml.pref_setting)
        setClickListener()
    }

    private fun setClickListener() {

        setListener(R.string.pref_key_set_host) {
            HostInputDialog.newInstance().let {
                it.clickListener = { _, which, _ ->
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        updateSummary()
                    }
                }
                it.show(requireActivity())
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(true)
            it.setIcon(null)
            it.setTitle(R.string.screen_title_setting)
        }

        updateSummary()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Update summary
     */
    private fun updateSummary() {
        updatePrefSummary(R.string.pref_key_set_host, true)
    }

}
