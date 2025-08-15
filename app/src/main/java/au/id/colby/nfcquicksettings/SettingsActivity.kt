// SPDX-FileCopyrightText: 2023-2025 Paul Colby <git@colby.id.au>
// SPDX-License-Identifier: GPL-3.0-or-later

package au.id.colby.nfcquicksettings

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
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

        val addTileButton = findViewById<Button?>(R.id.addTileButton)
        addTileButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.d(TAG, "addTileButton::onClick")
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
