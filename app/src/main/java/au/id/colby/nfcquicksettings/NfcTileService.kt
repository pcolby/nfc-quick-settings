// SPDX-FileCopyrightText: 2023-2024 Paul Colby <git@colby.id.au>
// SPDX-License-Identifier: GPL-3.0-or-later

package au.id.colby.nfcquicksettings

import android.Manifest.permission.WRITE_SECURE_SETTINGS
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import android.util.Log
import au.id.colby.nfcquicksettings.R.string
import java.util.Timer
import kotlin.concurrent.fixedRateTimer

private const val TAG = "NfcTileService"

/**
 * A custom Quick Settings tile for NFC.
 *
 * The tile shows the current NFC status (On, Off, or Unavailable), and when clicked, starts the
 * device's NFC Settings activity.
 */
class NfcTileService : TileService() {
    private var updateTimer: Timer? = null

    /**
     * Called when this tile moves into a listening state.
     *
     * This override updates the tile's state and title to indicate the device's current NFC status
     * (On, Off, or Unavailable).
     */
    override fun onStartListening() {
        super.onStartListening()
        Log.d(TAG, "onStartListening")
        val adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)
        updateTimer = fixedRateTimer("default", false, 0L, 500) {
            Log.d(TAG, "updateTimer")
            updateTile(adapter)
        }
    }

    /**
     * Called when this tile moves out of the listening state.
     *
     * This override cancels the update timer, if any is running.
     */
    override fun onStopListening() {
        Log.d(TAG, "onStopListening")
        updateTimer?.apply {
            Log.d(TAG, "Cancelling update timer")
            cancel()
        }
        super.onStopListening()
    }

    /**
     * Called when the user clicks on this tile.
     *
     * This override takes the user to the NFC settings, by starting the `ACTION_NFC_SETTINGS`
     * activity, and collapsing the Quick Settings menu.
     */
    override fun onClick() {
        super.onClick()
        Log.d(TAG, "onClick")
        if (!invertNfcState()) startNfcSettingsActivity()
    }

    /**
     * Checks if [permission] has been granted to us.
     *
     * @return [true] if we have [permission], otherwise [false].
     */
    private fun permissionGranted(permission: String): Boolean {
        val status = checkSelfPermission(permission)
        Log.d(TAG, "$permission status: $status")
        return status == PERMISSION_GRANTED
    }

    /**
     * Inverts the NFC [adapter]'s current state.
     *
     * That is, if [adapter] is currently enbled, then disable it, and vice versa. This calls
     * [setNfcAdapterState], which in turn, requires WRITE_SECURE_SETTINGS permission.
     *
     * @return [true] if the [adapter]'s new state was successfully requested.
     */
    private fun invertNfcState(adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)): Boolean {
        return adapter?.run { setNfcAdapterState(this, !isEnabled) } ?: false
    }

    /**
     * Sets the [adapter] state to [enable].
     *
     * This uses introspection to execute either the NfcAdapter::enable() or NfcAdapter::disable()
     * function as appropriate. These functions both require WRITE_SECURE_SETTINGS permission.
     *
     * @return [true] if the state change was successfully requested, otherwise [false].
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
        if (success) updateTile(enable)
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
            Log.d(TAG, "Updating tile")
            this.state = newState
            if (SDK_INT >= Build.VERSION_CODES.Q) this.subtitle = getText(newSubTitleResId)
            updateTile()
        }
    }

    /**
     * Updates the Quick Settings tile to show as active or not.
     *
     * @param active If [true] show the tile as active, otherwise show as inactive.
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
    private fun updateTile(adapter: NfcAdapter?) {
        adapter?.apply { updateTile(isEnabled) } ?:
            updateTile(Tile.STATE_INACTIVE, string.tile_subtitle_unavailable)
    }
}
