# NFC Quick Settings

[![Build Status](https://github.com/pcolby/nfc-quick-settings/actions/workflows/build.yaml/badge.svg?branch=main)](https://github.com/pcolby/nfc-quick-settings/actions/workflows/build.yaml?query=branch%3Amain)
[![CodeQL Analysis](https://github.com/pcolby/nfc-quick-settings/actions/workflows/codeql.yaml/badge.svg?branch=main)](https://github.com/pcolby/nfc-quick-settings/actions/workflows/codeql.yaml?query=branch%3Amain)
[![Codacy Grade](https://img.shields.io/codacy/grade/8f8bea5e700d4c64bf24b4a4297cc995?label=Code%20Quality&logo=codacy)](https://app.codacy.com/gh/pcolby/nfc-quick-settings/dashboard)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=pcolby_nfc-quick-settings&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=pcolby_nfc-quick-settings)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=pcolby_nfc-quick-settings&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=pcolby_nfc-quick-settings)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=pcolby_nfc-quick-settings&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=pcolby_nfc-quick-settings)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=pcolby_nfc-quick-settings&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=pcolby_nfc-quick-settings)

NFC Quick Settings is a really basic Android app with no GUI of it's own, that simply adds an NFC tile to the tiles
available to the [Quick Settings panel][].

The Quick Settings tile indicates the current NFC status (enabled, or disabled), but the action it takes when tapped
varies a little depending on the permissions available. See [Basic](#basic-mode) and [Advanced](#advanced-mode) modes
below.

[![Get it](assets/Play_Store_badge.svg)](https://play.google.com/store/apps/details?id=au.id.colby.nfcquicksettings)&ensp;
[![Get it](assets/F-Droid_badge.svg)](https://f-droid.org/packages/au.id.colby.nfcquicksettings)

## Basic Mode

In the _basic_ mode, which requires no special permissions, tapping the tile will simply open the device's NFC Settings
page (assuming the device has one). This is actually the best that can be done using official Android APIs.

## Advanced Mode

The advanced mode requires special permissions (see below), but once enabled, tapping the NFC Quick Settings tile will
turn the NFC service on or off directly, without having to open the NFC settings page at all. Unfortunately, to acheive
this _advanced_ mode, NFC Quick Settings needs to use APIs not intended for third-party applications (specifically
[`NfcAdapter::enable()`][] and [`NfcAdapter::disable()`][]), and to use those methods the tile needs the special
[`WRITE_SECURE_SETTINGS`][] permission.

### Granting `WRITE_SECURE_SETTINGS` Permission

As the [`WRITE_SECURE_SETTINGS`][] permission is not intended to be used by third-party applications, it needs to be
granted via the [Android Debug Bridge (adb)][] tool.

1. Install [Android Debug Bridge (adb)][]:

   > `adb` is included in the Android SDK Platform Tools package. Download this package with the [SDK Manager][], which
   > installs it at `android_sdk/platform-tools/`. If you want the standalone Android SDK Platform Tools package,
   > [download it here](https://developer.android.com/tools/releases/platform-tools).

2. Install NFC Quick Settings, via [Google Play], [F-Droid], or [GitHub Releases].

3. [Enable adb debugging on your device](https://developer.android.com/tools/adb#Enabling).

4. [Connect your device via Wi-Fi](https://developer.android.com/tools/adb#connect-to-a-device-over-wi-fi).

5. Grant the permission to NFC Quick Settings via `adb`:

   ```sh
   adb shell pm grant au.id.colby.nfcquicksettings android.permission.WRITE_SECURE_SETTINGS
   ```

If for some reason you want to revoke the [`WRITE_SECURE_SETTINGS`][] permission, and restore the _basic_ mode, then
simply follow the same steps as above, but replace `grant` with `revoke`. That is, run:

```sh
adb shell pm revoke au.id.colby.nfcquicksettings android.permission.WRITE_SECURE_SETTINGS
```

[`NfcAdapter::disable()`]: https://cs.android.com/android/platform/superproject/+/main:frameworks/base/core/java/android/nfc/NfcAdapter.java;l=986?q=NfcAdapter "android.nfc.NfcAdapter::disable()"
[`NfcAdapter::enable()`]: https://cs.android.com/android/platform/superproject/+/main:frameworks/base/core/java/android/nfc/NfcAdapter.java;l=947?q=NfcAdapter "android.nfc.NfcAdapter::enable()"
[`WRITE_SECURE_SETTINGS`]: https://developer.android.com/reference/android/Manifest.permission#WRITE_SECURE_SETTINGS "android.permission.WRITE_SECURE_SETTINGS"
[Android Debug Bridge (adb)]: https://developer.android.com/tools/adb "Android Debug Bridge (adb)"
[F-Droid]: https://f-droid.org/packages/au.id.colby.nfcquicksettings "NFC Quick Settings on F-Droid"
[GitHub Releases]: https://github.com/pcolby/nfc-quick-settings/releases "NFC Quick Settings releases"
[Google Play]: https://play.google.com/store/apps/details?id=au.id.colby.nfcquicksettings "NFC Quick Settings on Google Play"
[SDK Manager]: https://developer.android.com/studio/intro/update#sdk-manager "Update your tools with the SDK Manager"
[Quick Settings panel]: https://support.google.com/android/answer/9083864 "Change settings quickly on your Android phone"
