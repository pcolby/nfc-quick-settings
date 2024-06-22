// SPDX-FileCopyrightText: 2023-2024 Paul Colby <git@colby.id.au>
// SPDX-License-Identifier: GPL-3.0-or-later

package au.id.colby.nfcquicksettings

import android.Manifest.permission.WRITE_SECURE_SETTINGS
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.nfc.NfcAdapter
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import androidx.core.content.ContextCompat
import au.id.colby.nfcquicksettings.R.string

private const val TAG = "NfcTileService"

/**
 * A custom Quick Settings tile for NFC.
 *
 * The tile shows the current NFC status (On, Off, or Unavailable), and when clicked, starts the
 * device's NFC Settings activity.
 */
class NfcTileService : TileService() {
    private val nfcBroadcastReceiver = NfcBroadcastReceiver()

    /**
     * Called when this tile moves into a listening state.
     *
     * This override registers a broadcast receiver to listen for NFC adapter state changes, then
     * updates the tile according the default adapter's current state.
     */
    override fun onStartListening() {
        super.onStartListening()
        Log.d(TAG, "onStartListening; Registering broadcast receiver")
        ContextCompat.registerReceiver(
            this,
            nfcBroadcastReceiver,
            IntentFilter(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED),
            ContextCompat.RECEIVER_EXPORTED
        )
        updateTile()
    }

    /**
     * Called when this tile moves out of the listening state.
     *
     * This override simply unregisters the broadcast receiver.
     */
    override fun onStopListening() {
        Log.d(TAG, "onStopListening; Unregistering broadcast receiver")
        unregisterReceiver(nfcBroadcastReceiver)
        super.onStopListening()
    }

    /**
     * Called when the user clicks on this tile.
     *
     * This override attempts to invert the default NFC adapter's state, and if that cannot be done,
     * launches the NFC Settings Action, where the user can toggle it themselves.
     */
    override fun onClick() {
        super.onClick()
        Log.d(TAG, "onClick")
        if (!invertNfcState()) startNfcSettingsActivity()
    }

    /**
     * Inverts the NFC [adapter]'s current state.
     *
     * That is, if [adapter] is currently enabled, then disable it, and vice versa. This calls
     * [setNfcAdapterState], which in turn, requires WRITE_SECURE_SETTINGS permission.
     *
     * @return true if the [adapter]'s new state was successfully requested.
     */
    private fun invertNfcState(adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)): Boolean {
        return adapter?.run { setNfcAdapterState(this, !isEnabled) } ?: false
    }

    /**
     * Checks if [permission] has been granted to us.
     *
     * @return true if we have [permission], otherwise false.
     */
    private fun permissionGranted(permission: String): Boolean {
        val status = checkSelfPermission(permission)
        Log.d(TAG, "$permission status: $status")
        return status == PERMISSION_GRANTED
    }

    /**
     * Sets the [adapter] state to [enable].
     *
     * This uses introspection to execute either the NfcAdapter::enable() or NfcAdapter::disable()
     * function as appropriate. These functions both require WRITE_SECURE_SETTINGS permission.
     *
     * @return true if the state change was successfully requested, otherwise false.
     */
    private fun setNfcAdapterState(adapter: NfcAdapter, enable: Boolean): Boolean {
        if (!permissionGranted(WRITE_SECURE_SETTINGS)) return false
        val methodName = if (enable) "enable" else "disable"
        Log.i(TAG, "Setting NFC adapter's status to ${methodName}d")
        val success = try {
            Log.d(TAG, "Invoking NfcAdapter::$methodName()")
            val result = NfcAdapter::class.java.getMethod(methodName).invoke(adapter)
            Log.d(TAG, "NfcAdapter::$methodName() returned $result")
            result is Boolean && result
        } catch (e: Exception) {
            Log.e(TAG, "Failed to invoke NfcAdapter::$methodName()", e)
            false
        }
        return success
    }

    /**
     * Starts the NFC Settings activity, then collapses the Quick Settings panel behind it.
     */
    private fun startNfcSettingsActivity() {
        Log.i(TAG, "Starting the ACTION_NFC_SETTINGS activity")
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //noinspection StartActivityAndCollapseDeprecated
        if (SDK_INT < UPSIDE_DOWN_CAKE) @Suppress("DEPRECATION") startActivityAndCollapse(intent)
        else startActivityAndCollapse(PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE))
    }

    /**
     * Updates the Quick Settings tile with the [newState] and (if supported) [newSubTitleResId].
     *
     * Note [newSubTitleResId] will be ignored on devices running Android versions earlier than Q.
     *
     * @param newState The next state for the tile. Should be one of the Tile.STATE_* constants.
     * @param newSubTitleResId Resource ID for the new subtitle text.
     */
    private fun updateTile(newState: Int, newSubTitleResId: Int) {
        qsTile?.apply {
            Log.d(TAG, "Updating tile with state $newState")
            this.state = newState
            if (SDK_INT >= VERSION_CODES.Q) this.subtitle = getText(newSubTitleResId)
            updateTile()
        }
    }

    /**
     * Updates the Quick Settings tile to show as active or not.
     *
     * @param active If true show the tile as active, otherwise show as inactive.
     */
    private fun updateTile(active: Boolean) {
        if (active) updateTile(Tile.STATE_ACTIVE, string.tile_subtitle_active)
        else updateTile(Tile.STATE_INACTIVE, string.tile_subtitle_inactive)
    }

    /**
     * Updates the Quick Settings tile to reflect the [adapter]'s current state.
     *
     * @param adapter The adapter to reflect the state of.
     */
    private fun updateTile(adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)) {
        adapter?.apply { updateTile(isEnabled) } ?:
            updateTile(Tile.STATE_INACTIVE, string.tile_subtitle_unavailable)
    }

    /**
     * Provides static functions for the NfcTileService class.
     */
    companion object {
        /**
         * Updates the Quick Settings tile owned by [context], which is an NfcTileService instance.
         */
        fun updateTile(context: Context) {
            (context as? NfcTileService)?.run { updateTile(); }
        }
    }

    /**
     * A custom Broadcast Receiver for updating an NfcTileService on NFC adapter state changes.
     */
    inner class NfcBroadcastReceiver : BroadcastReceiver() {
        /**
         * Called when a broadcast message is received.
         *
         * This override simply reflects the event back to the [context] for which it the
         * receiver was registered.
         */
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "onReceive $context $intent")
            if (intent.action == NfcAdapter.ACTION_ADAPTER_STATE_CHANGED) updateTile(context)
            else Log.w(TAG, "Received unexpected broadcast message: $intent")
        }
    }
}
