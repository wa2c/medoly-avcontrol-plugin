package com.wa2c.android.medoly.plugin.action.avcontrol.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.wa2c.android.medoly.plugin.action.avcontrol.R
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logD
import com.wa2c.android.medoly.plugin.action.avcontrol.common.logE
import com.wa2c.android.medoly.plugin.action.avcontrol.common.showToast
import com.wa2c.android.medoly.plugin.action.avcontrol.databinding.DialogHostInputBinding
import com.wa2c.android.medoly.plugin.action.avcontrol.source.local.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import io.resourcepool.ssdp.client.SsdpClient
import io.resourcepool.ssdp.model.DiscoveryListener
import io.resourcepool.ssdp.model.SsdpRequest
import io.resourcepool.ssdp.model.SsdpService
import io.resourcepool.ssdp.model.SsdpServiceAnnouncement
import javax.inject.Inject

/**
 * Host input dialog
 */
@AndroidEntryPoint
class HostInputDialog : AbstractDialogFragment() {

    private val binding: DialogHostInputBinding by lazy {
        DataBindingUtil.inflate<DialogHostInputBinding>(
            LayoutInflater.from(activity),
            R.layout.dialog_host_input,
            null,
            false
        )
    }

    @Inject
    lateinit var preferences: AppPreferences

    /** SSDP client. */
    private val client: SsdpClient = SsdpClient.create()

    /** Handler */
    private val handler = Handler(Looper.getMainLooper())

    /** Adapter */
    private val adapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            mutableListOf()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    /** Device map [Key: IP address, Value: device name]  */
    private val deviceMap: LinkedHashMap<String, String> = LinkedHashMap()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)

        binding.dialogHostInputDeviceSpinner.adapter = adapter
        binding.dialogHostInputDeviceSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val host =
                        deviceMap.keys.elementAtOrNull(binding.dialogHostInputDeviceSpinner.selectedItemPosition)
                    binding.dialogHostInputCopyButton.isEnabled != host.isNullOrEmpty()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }
        binding.dialogHostInputCopyButton.setOnClickListener {
            deviceMap.keys.elementAtOrNull(binding.dialogHostInputDeviceSpinner.selectedItemPosition)
                ?.let { host ->
                    binding.dialogHostInputText.setText(host)
                }
        }

        // Build
        return AlertDialog.Builder(requireContext()).apply {
            setTitle(R.string.dialog_host_input_title)
            setView(binding.root)
            setPositiveButton(android.R.string.ok, null)
            setNeutralButton(android.R.string.cancel, null)
        }.create()
    }

    override fun onStart() {
        super.onStart()

        binding.dialogHostInputText.setText(preferences.hostAddress)
        discoverDevices()
    }

    override fun onStop() {
        super.onStop()
        try {
            client.stopDiscovery()
        } catch (e: Exception) {
            logE(e)
        }
    }

    override fun invokeListener(which: Int, bundle: Bundle?, close: Boolean) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            preferences.hostAddress = binding.dialogHostInputText.text.toString()
        }
        super.invokeListener(which, bundle, close)
    }

    private fun discoverDevices() {
        deviceMap.clear()

        val networkStorageDevice = SsdpRequest.builder()
            .serviceType(SSDP_SERVICE_TYPE)
            .build()
        client.discoverServices(networkStorageDevice, object : DiscoveryListener {
            override fun onFailed(e: Exception) {
                logE(e)
                context?.showToast(R.string.detect_device_failed)
            }

            override fun onServiceDiscovered(service: SsdpService) {
                logD(service)
                val names = service.originalResponse.headers[X_MODELNAME]?.split(":") ?: return
                val name = names.getOrNull(2) ?: names.getOrNull(0) ?: return
                val address = service.remoteIp.hostAddress ?: return
                handler.post {
                    addItem(address, name)
                }
            }

            override fun onServiceAnnouncement(announcement: SsdpServiceAnnouncement) {
                logD(announcement)
            }
        })
    }

    private fun addItem(address: String, name: String) {
        deviceMap[address] = name
        adapter.add("$name ($address)")
        adapter.notifyDataSetChanged()
    }


    companion object {

        private const val SSDP_SERVICE_TYPE = "urn:schemas-upnp-org:device:MediaRenderer:1"
        private const val X_MODELNAME = "X-MODELNAME"

        /**
         * Create dialog instance.
         * @return Dialog instance.
         */
        fun newInstance(): HostInputDialog {
            return HostInputDialog().apply {
                arguments = Bundle()
            }
        }
    }

}
