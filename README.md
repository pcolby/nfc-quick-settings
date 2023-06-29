# Quick NFC Settings

Quick NFC Setting is a really basic Android app with no GUI of it's own, that simply adds an NFC
tile to the tiles available to the [Quick Settings panel].

*Screenshots go here...*

## Internal Details

Just for the curious, the application implements a basic [TileService], that overrides:

* [onStartListening()] to check the current NFC adapter's status, and update the tile state; and
* [onClick()] to show the device's NFC Settings by starting the [NFC Settings] activity.

A really basic app the implements a [Quick Settings tile] that shows the current NFC adapter
status, and when clicked, invokes the current device's [NFC Settings action] (which shows a UI
that allows NFC to be turned on or off, if supported).

[NFC Settings]: https://developer.android.com/reference/android/provider/Settings#ACTION_NFC_SETTINGS android.settings.NFC_SETTINGS
[onClick()]: https://developer.android.com/reference/android/service/quicksettings/TileService#onClick()
[onStartListening()]: https://developer.android.com/reference/android/service/quicksettings/TileService#onStartListening()
[Quick Settings panel]: https://support.google.com/android/answer/9083864
[TileService]: https://developer.android.com/reference/android/service/quicksettings/TileService
