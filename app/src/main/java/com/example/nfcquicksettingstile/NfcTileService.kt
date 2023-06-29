package com.example.nfcquicksettingstile

import android.content.Intent
import android.nfc.NfcAdapter
import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class NfcTileService: TileService() {

    // Called when the user adds your tile.
    override fun onTileAdded() {
        super.onTileAdded()
    }
    // Called when your app can update your tile.
    override fun onStartListening() {
        super.onStartListening()
        var adapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(this)
        qsTile.state = if (adapter == null) Tile.STATE_UNAVAILABLE else
            if (adapter.isEnabled) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.subtitle = if (adapter == null) null else
            getText(if (adapter.isEnabled) R.string.on else R.string.off)
        qsTile.updateTile()
    }

    // Called when your app can no longer update your tile.
    override fun onStopListening() {
        super.onStopListening()
    }

    // Called when the user taps on your tile in an active or inactive state.
    override fun onClick() {
        super.onClick()
        var intent = Intent(Settings.ACTION_NFC_SETTINGS);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityAndCollapse(Intent(intent))
    }
    // Called when the user removes your tile.
    override fun onTileRemoved() {
        super.onTileRemoved()
    }
}