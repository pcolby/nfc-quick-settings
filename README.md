# NFC Quick Settings

[![Build Status](https://github.com/pcolby/nfc-quick-settings/actions/workflows/build.yaml/badge.svg?branch=main)](https://github.com/pcolby/nfc-quick-settings/actions/workflows/build.yaml?query=branch%3Amain)
[![CodeQL Analysis](https://github.com/pcolby/nfc-quick-settings/actions/workflows/codeql.yaml/badge.svg?branch=main)](https://github.com/pcolby/nfc-quick-settings/actions/workflows/codeql.yaml?query=branch%3Amain)

NFC Quick Settings is a really basic Android app with no GUI of it's own, that simply adds an NFC
tile to the tiles available to the [Quick Settings panel][].

*Screenshots go here...*

## Internal Details

Just for the curious, the application implements a basic [TileService][], that overrides:

* [onStartListening()][] to check the default NFC adapter's current status, and update the tile state
  accordingly; and
* [onClick()][] to show the device's NFC Settings by starting the [NFC Settings][] activity.

[NFC Settings]: https://developer.android.com/reference/android/provider/Settings#ACTION_NFC_SETTINGS
[onClick()]: https://developer.android.com/reference/android/service/quicksettings/TileService#onClick()
[onStartListening()]: https://developer.android.com/reference/android/service/quicksettings/TileService#onStartListening()
[Quick Settings panel]: https://support.google.com/android/answer/9083864
[TileService]: https://developer.android.com/reference/android/service/quicksettings/TileService
