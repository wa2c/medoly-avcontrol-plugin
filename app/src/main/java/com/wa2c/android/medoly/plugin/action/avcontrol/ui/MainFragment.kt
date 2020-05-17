package com.wa2c.android.medoly.plugin.action.avcontrol.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.wa2c.android.medoly.library.MedolyEnvironment
import com.wa2c.android.medoly.plugin.action.avcontrol.R
import com.wa2c.android.medoly.plugin.action.avcontrol.common.showToast
import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import com.wa2c.android.medoly.plugin.action.avcontrol.ui.component.navigateSafe
import com.wa2c.android.medoly.plugin.action.avcontrol.ui.component.setListener


/**
 * Main fragment
 */
class MainFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = AppPreferences.FILE_NAME
        addPreferencesFromResource(R.xml.pref_main)
        setClickListener()
    }

    private fun setClickListener() {

        setListener(R.string.pref_key_open_setting) {
            navigateSafe(MainFragmentDirections.actionMainFragmentToSettingFragment())
        }

        setListener(R.string.pref_key_open_info) {
            navigateSafe(MainFragmentDirections.actionMainFragmentToInformationFragment())
        }

        setListener(R.string.pref_key_launch_medoly) {
            activity?.packageManager?.getLaunchIntentForPackage(MedolyEnvironment.MEDOLY_PACKAGE)
                ?.let { intent ->
                    startActivity(intent)
                } ?: run {
                context?.showToast(R.string.medoly_launch_failed_message)
            }
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(false)
            it.setDisplayShowTitleEnabled(true)
            it.setIcon(R.mipmap.ic_launcher)
            it.setTitle(R.string.screen_title_main)
        }
    }

}
