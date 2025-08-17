// SPDX-FileCopyrightText: 2023-2025 Paul Colby <git@colby.id.au>
// SPDX-License-Identifier: GPL-3.0-or-later

package au.id.colby.nfcquicksettings

import android.app.StatusBarManager
import android.app.StatusBarManager.TILE_ADD_REQUEST_ERROR_APP_NOT_IN_FOREGROUND
import android.app.StatusBarManager.TILE_ADD_REQUEST_ERROR_BAD_COMPONENT
import android.app.StatusBarManager.TILE_ADD_REQUEST_ERROR_MISMATCHED_PACKAGE
import android.app.StatusBarManager.TILE_ADD_REQUEST_ERROR_NOT_CURRENT_USER
import android.app.StatusBarManager.TILE_ADD_REQUEST_ERROR_NO_STATUS_BAR_SERVICE
import android.app.StatusBarManager.TILE_ADD_REQUEST_ERROR_REQUEST_IN_PROGRESS
import android.app.StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ADDED
import android.app.StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED
import android.app.StatusBarManager.TILE_ADD_REQUEST_RESULT_TILE_NOT_ADDED
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
import com.google.android.material.snackbar.Snackbar

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

    /**
     * Called when this activity is being created.
     *
     * This override sets up the UI.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings_activity)

        val qsLabel = getText(R.string.android_quick_settings_label)
        findViewById<TextView>(R.id.settingsIntro).text =
            getString(R.string.settings_intro, qsLabel)

        findViewById<Button>(R.id.addTileButton).apply {
            if (SDK_INT < VERSION_CODES.TIRAMISU) visibility = View.GONE
            else {
                text = getString(R.string.settings_add_tile_button_label, qsLabel)
                setOnClickListener { onTileButtonClick() }
            }
        }

        val settingsVersion = findViewById<TextView>(R.id.settingsVersion)
        settingsVersion.text = getString(
            R.string.settings_version_text,
            getText(R.string.build_version)
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Called when the user taps the "Add Tile" button. Requests the system to prompt the user to
     * install the tile.
     *
     * @see onAddTileServiceResponse
     */
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

    /**
     * Handles the system's response to our request to add the tile.
     *
     * @param result The result of the add-tile request.
     *
     * @see onTileButtonClick
     * @see StatusBarManager.requestAddTileService
     */
    private fun onAddTileServiceResponse(result: Int?) {
        Log.d(TAG, "requestAddTileService result: $result")
        val message = getString(
            when (result) {
                TILE_ADD_REQUEST_ERROR_APP_NOT_IN_FOREGROUND -> R.string.settings_app_not_in_foreground
                TILE_ADD_REQUEST_ERROR_BAD_COMPONENT -> R.string.settings_bad_component
                TILE_ADD_REQUEST_ERROR_MISMATCHED_PACKAGE -> R.string.settings_mismatched_package
                TILE_ADD_REQUEST_ERROR_NOT_CURRENT_USER -> R.string.settings_no_current_user
                TILE_ADD_REQUEST_ERROR_NO_STATUS_BAR_SERVICE -> R.string.settings_no_status_bar_service
                TILE_ADD_REQUEST_ERROR_REQUEST_IN_PROGRESS -> R.string.settings_request_in_progress
                TILE_ADD_REQUEST_RESULT_TILE_ADDED -> R.string.settings_tile_added
                TILE_ADD_REQUEST_RESULT_TILE_ALREADY_ADDED -> R.string.settings_tile_already_added
                TILE_ADD_REQUEST_RESULT_TILE_NOT_ADDED -> R.string.settings_tile_not_added
                else -> R.string.settings_unknown_add_response
            }
        )
        Log.d(TAG, "requestAddTileService message: $message")
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }
}
