// SPDX-FileCopyrightText: 2023 Paul Colby <git@colby.id.au>
// SPDX-License-Identifier: GPL-3.0-or-later

package au.id.colby.nfcquicksettings

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Build
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import au.id.colby.nfcquicksettings.R.*

/**
 * A custom Quick Settings tile for NFC.
 *
 * The tile shows the current NFC status (On, Off, or Unavailable), and when clicked, starts the
 * device's NFC Settings activity.
 */
class NfcTileService : TileService() {

    /**
     * Called when this tile moves into a listening state.
     *
     * This override updates the tile's state and title to indicate the device's current NFC status
     * (On, Off, or Unavailable).
     */
    override fun onStartListening() {
        super.onStartListening()
        val adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)
        qsTile.state = if (adapter == null) Tile.STATE_UNAVAILABLE else
            if (adapter.isEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) qsTile.subtitle = getText(
            if (adapter == null) string.tile_subtitle_unavailable else
                if (adapter.isEnabled) string.tile_subtitle_active else string.tile_subtitle_inactive
        )
        qsTile.updateTile()
    }

    /**
     * Called when the user clicks on this tile.
     *
     * This override takes the user to the NFC settings, by starting the `ACTION_NFC_SETTINGS`
     * activity, and collapsing the Quick Settings menu.
     */
    override fun onClick() {
        super.onClick()
        val intent = Intent(Settings.ACTION_NFC_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityAndCollapse(intent)
        /// \todo When Android 14 is officially released:
        //if (SDK_INT < UPSIDE_DOWN_CAKE) @Suppress("DEPRECATION") startActivityAndCollapse(intent)
        //else startActivityAndCollapse(PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
    }
}
