package au.id.colby.nfcquicksettings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log

private const val TAG = "NfcTilePrefsActivity"

/**
 * A custom "preferences" activity for the NFC Quick Settings tile.
 *
 * The activity has no display, but simply starts the NFC Settings activity, then finishes. By
 * default, this activity is not enabled (ie via the AndroidManifest.xml) however, if the
 * `WRITE_SECURE_SETTINGS` permission has been granted, then the NfcTileService will enable this
 * activity. See NfcTileService::onCreate().
 */
class NfcTilePreferencesActivity : Activity() {

    /**
     * Called when this activity is being created.
     *
     * This override simply starts the NFC Settings activity, then finishes this activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        Log.i(TAG, "Starting the ACTION_NFC_SETTINGS activity")
        startActivity(
            Intent(Settings.ACTION_NFC_SETTINGS).setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            )
        )
        finish()
    }
}
