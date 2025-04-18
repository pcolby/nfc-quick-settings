// SPDX-FileCopyrightText: 2023-2025 Paul Colby <git@colby.id.au>
// SPDX-License-Identifier: GPL-3.0-or-later

package au.id.colby.nfcquicksettings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat

private const val TITLE_TAG = "settingsActivityTitle"

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
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        } else {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setTitle(R.string.title)
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, title)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.popBackStackImmediate()) {
            return true
        }
        return super.onSupportNavigateUp()
    }

//    override fun onPreferenceStartFragment(
//        caller: PreferenceFragmentCompat,
//        pref: Preference
//    ): Boolean {
//        // Instantiate the new Fragment
//        val args = pref.extras
//        val fragment = supportFragmentManager.fragmentFactory.instantiate(
//            classLoader,
//            pref.fragment ?: return false
//        ).apply {
//            arguments = args
//            setTargetFragment(caller, 0)
//        }
//        // Replace the existing Fragment with the new Fragment
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.settings, fragment)
//            .addToBackStack(null)
//            .commit()
//        title = pref.title
//        return true
//    }

    /**
     * The root preference fragment that displays preferences that link to the other preference
     * fragments below.
     */
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root, rootKey)
        }
    }

    /**
     * A preference fragment that demonstrates commonly used preference attributes.
     */
    class BasicPreferencesFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.basic_preferences, rootKey)
        }
    }

    /**
     * A preference fragment that demonstrates preferences which contain dynamic widgets.
     */
    class WidgetPreferencesFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.widgets, rootKey)
        }
    }

    /**
     * A preference fragment that demonstrates preferences that launch a dialog when tapped.
     */
    class DialogPreferencesFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.dialogs, rootKey)
        }
    }

    /**
     * A preference fragment that demonstrates more advanced attributes and functionality.
     */
    class AdvancedPreferencesFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.advanced, rootKey)
        }
    }
}
