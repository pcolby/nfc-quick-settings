# NFC Quick Settings

[![Build Status](https://github.com/pcolby/nfc-quick-settings/actions/workflows/build.yaml/badge.svg?branch=main)](https://github.com/pcolby/nfc-quick-settings/actions/workflows/build.yaml?query=branch%3Amain)
[![CodeQL Analysis](https://github.com/pcolby/nfc-quick-settings/actions/workflows/codeql.yaml/badge.svg?branch=main)](https://github.com/pcolby/nfc-quick-settings/actions/workflows/codeql.yaml?query=branch%3Amain)
[![Codacy Grade](https://img.shields.io/codacy/grade/8f8bea5e700d4c64bf24b4a4297cc995?label=Code%20Quality&logo=codacy)](https://app.codacy.com/gh/pcolby/nfc-quick-settings/dashboard)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=pcolby_nfc-quick-settings&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=pcolby_nfc-quick-settings)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=pcolby_nfc-quick-settings&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=pcolby_nfc-quick-settings)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pcolby_nfc-quick-settings&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=pcolby_nfc-quick-settings)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=pcolby_nfc-quick-settings&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=pcolby_nfc-quick-settings)

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
