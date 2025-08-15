// SPDX-FileCopyrightText: 2023-2025 Paul Colby <git@colby.id.au>
// SPDX-License-Identifier: GPL-3.0-or-later

package au.id.colby.nfcquicksettings

import android.app.StatusBarManager
import android.content.ComponentName
import android.graphics.drawable.Icon
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "SettingsActivity"

/**
 * An activity for managing user preferences for NFC Quick Settings.
 *
 * Currently this is really just an initial placeholder (because Google Play Support has begun
 * rejecting updates because they "can't see the app" 🙄), but eventually will extend to support a
 * range of preferences, such as:
 *  - what actions to take when tapping / long-tapping the tile
 *  - auto-disable NFC after a timer
 *  - etc.
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings_activity)

        val addTileButton = findViewById<Button>(R.id.addTileButton)
        if (SDK_INT < VERSION_CODES.TIRAMISU) addTileButton.visibility = View.GONE
        else addTileButton.setOnClickListener { onTileButtonClick() }

        val settingsVersion = findViewById<TextView>(R.id.settingsVersion)
        settingsVersion.text = getString(R.string.settings_version_text,
            getText(R.string.build_version))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onTileButtonClick() {
        Log.d(TAG, "onTileButtonClick")
        assert(SDK_INT >= VERSION_CODES.TIRAMISU)
        if (SDK_INT < VERSION_CODES.TIRAMISU) return
        getSystemService(StatusBarManager::class.java).requestAddTileService(
            ComponentName(this, NfcTileService::class.java),
            getString(R.string.tile_label),
            Icon.createWithResource(this, R.drawable.round_nfc_24),
            mainExecutor
        ) { result -> onAddTileServiceResponse(result) }
    }

    private fun onAddTileServiceResponse(result: Int?) {
        Log.i(TAG, "requestAddTileService result: $result")
        // \todo Handle result codes. eg added, vs already-added, etc.
    }
}
