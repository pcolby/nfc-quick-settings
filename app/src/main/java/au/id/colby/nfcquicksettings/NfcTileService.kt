// SPDX-FileCopyrightText: 2023-2024 Paul Colby <git@colby.id.au>
// SPDX-License-Identifier: GPL-3.0-or-later

package au.id.colby.nfcquicksettings

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
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
            qsTile?.apply {
                Log.d(TAG, "Updating tile")
                state = if (adapter == null) Tile.STATE_INACTIVE else
                    if (adapter.isEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
                if (SDK_INT >= Build.VERSION_CODES.Q) subtitle = getText(
                    if (adapter == null) string.tile_subtitle_unavailable else
                        if (adapter.isEnabled) string.tile_subtitle_active else string.tile_subtitle_inactive
                )
                updateTile()
            }
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
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //noinspection StartActivityAndCollapseDeprecated
        if (SDK_INT < UPSIDE_DOWN_CAKE) @Suppress("DEPRECATION") startActivityAndCollapse(intent)
        else startActivityAndCollapse(PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE))
    }
}
