package au.id.colby.nfcquicksettings

import android.Manifest.permission.WRITE_SECURE_SETTINGS
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log

private const val SCHEME_PACKAGE = "package" ///< @see android.content.IntentFilter.SCHEME_PACKAGE
private const val TAG = "NfcTilePrefsActivity"

/**
 * A custom Activity for NFC.
 *
 * The tile shows the current NFC status (On, Off, or Unavailable), and when clicked, starts the
 * device's NFC Settings activity.
 */
class NfcTilePreferencesActivity : Activity() {

    /**
     * Called when this activity is being created.
     *
     * This override simply starts either the NFC Settings, Application Details activity, depending
     * on whether or not the WRITE_SECURE_SETTINGS permission has been granted.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        val intent = if (checkSelfPermission(WRITE_SECURE_SETTINGS) == PERMISSION_GRANTED)
            Intent(Settings.ACTION_NFC_SETTINGS)
        else Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            .setData(Uri.fromParts(SCHEME_PACKAGE, componentName.packageName, null))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        Log.d(TAG, "$intent ${intent.data}")
        Log.i(TAG, "Starting the ${intent.action} activity")
        startActivity(intent)
    }

    /**
     * Called when this activity is about to be shown to the user.
     *
     * This override simply calls finish() to have this activity closed (since onCreate() will have
     * already launched a different activity).
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart; Finishing")
        finish()
    }
}
