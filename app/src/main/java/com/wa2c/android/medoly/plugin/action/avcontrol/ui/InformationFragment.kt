package com.wa2c.android.medoly.plugin.action.avcontrol.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.wa2c.android.medoly.library.MedolyEnvironment
import com.wa2c.android.medoly.plugin.action.avcontrol.R
import com.wa2c.android.medoly.plugin.action.avcontrol.common.showToast
import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import com.wa2c.android.medoly.plugin.action.avcontrol.ui.component.navigateSafe
import com.wa2c.android.medoly.plugin.action.avcontrol.ui.component.preference
import com.wa2c.android.medoly.plugin.action.avcontrol.ui.component.setListener


/**
 * Information fragment.
 */
class InformationFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = AppPreferences.FILE_NAME
        addPreferencesFromResource(R.xml.pref_information)
        setClickListener()
        updateSummary()

//    /**
//     * Device auto start.
//     */
//    private val deviceAutoStartPreferenceClickListener = Preference.OnPreferenceClickListener {
//        activity?.let {
//            if (!KillerManager.doAction(it, managerAction)) {
//                it.showToast(R.string.message_unsupported_device)
//            }
//        }
//        true
//    }
//
//    /**
//     * Privacy policy
//     */
//    private val privacyPolicyPreferenceClickListener = Preference.OnPreferenceClickListener {
//        val url = Uri.parse(getString(R.string.app_privacy_policy_url))
//        startActivity(Intent(Intent.ACTION_VIEW, url))
//        true
//    }
//
//    /**
//     * App info.
//     */
//    private val applicationDetailsPreferenceClickListener = Preference.OnPreferenceClickListener {
//        activity?.let {
//            val intent = Intent()
//            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//            intent.data = Uri.parse("package:" + it.packageName)
//            startActivity(intent)
//        }
//        true
//    }
//
//    /**
//     * About.
//     */
//    private val aboutPreferenceClickListener = Preference.OnPreferenceClickListener {
//        AboutDialogFragment.newInstance().show(activity)
//        true
//    }

//        KillerManager.init(activity)
//        managerAction = when {
//            KillerManager.isActionAvailable(activity, KillerManager.Actions.ACTION_POWERSAVING) -> KillerManager.Actions.ACTION_POWERSAVING
//            KillerManager.isActionAvailable(activity, KillerManager.Actions.ACTION_AUTOSTART) -> KillerManager.Actions.ACTION_AUTOSTART
//            KillerManager.isActionAvailable(activity, KillerManager.Actions.ACTION_NOTIFICATIONS) -> KillerManager.Actions.ACTION_NOTIFICATIONS
//            else -> null
//        }

//        // Device auto start
//        if (managerAction != null) {
//            (findPreference(getString(R.string.pref_key_device_auto_start)) as? Preference)?.onPreferenceClickListener = deviceAutoStartPreferenceClickListener
//        } else {
//            (findPreference(getString(R.string.pref_key_device_auto_start)) as? Preference)?.isEnabled = false
//        }
//        // Privacy Policy
//        (findPreference(getString(R.string.pref_key_open_privacy_policy)) as? Preference)?.onPreferenceClickListener = privacyPolicyPreferenceClickListener
//        // App info
//        (findPreference(getString(R.string.pref_key_application_details)) as? Preference)?.onPreferenceClickListener = applicationDetailsPreferenceClickListener
//        // About
//        (findPreference(getString(R.string.pref_key_about)) as? Preference)?.onPreferenceClickListener = aboutPreferenceClickListener
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        (activity as? AppCompatActivity)?.supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(true)
            it.setIcon(null)
            it.setTitle(R.string.screen_title_info)
        }

        updateSummary()
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


    private fun setClickListener() {
        setListener(R.string.pref_key_open_author) {
            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_author_url))).let {
                startActivity(it)
            }
        }

        setListener(R.string.pref_key_open_app_license) {
            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_license_url))).let {
                it.putExtra("title", getString(R.string.pref_title_open_library_license))
                startActivity(it)
            }
        }

        setListener(R.string.pref_key_open_library_license) {
            startActivity(Intent(context, OssLicensesMenuActivity::class.java))
        }

        setListener(R.string.pref_key_open_info_app) {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + requireContext().packageName)).let {
                it.putExtra("title", getString(R.string.pref_title_open_library_license))
                startActivity(it)
            }
        }

        setListener(R.string.pref_key_open_store) {
            val url = getString(R.string.app_store_url)
            Intent(Intent.ACTION_VIEW, Uri.parse(url)).let {
                startActivity(it)
            }
        }
    }

    /**
     * Update summary
     */
    private fun updateSummary() {
        preference<Preference>(R.string.pref_key_info_app_version)?.summary = "1.0"
    }
}
