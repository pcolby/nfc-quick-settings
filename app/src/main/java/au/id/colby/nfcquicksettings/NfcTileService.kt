// SPDX-FileCopyrightText: 2023 Paul Colby <git@colby.id.au>
// SPDX-License-Identifier: GPL-3.0-or-later

package au.id.colby.nfcquicksettings

import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class NfcTileService : TileService() {
    override fun onStartListening() {
        super.onStartListening()
        var adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)
        qsTile.state = if (adapter == null) Tile.STATE_UNAVAILABLE else
            if (adapter.isEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.subtitle = if (adapter == null) null else
            getText(if (adapter.isEnabled) R.string.on else R.string.off)
        qsTile.updateTile()
    }

    // Called when the user taps on your tile in an active or inactive state.
    override fun onClick() {
        super.onClick()
        var intent = Intent(Settings.ACTION_NFC_SETTINGS);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityAndCollapse(intent)
        /// \todo When Android 14 is officially released:
        //if (SDK_INT < UPSIDE_DOWN_CAKE) @Suppress("DEPRECATION") startActivityAndCollapse(intent)
        //else startActivityAndCollapse(PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
    }
}
