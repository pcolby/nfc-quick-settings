# Changelog[^1]

## [1.4.5][] (2025-08-15)

Support for Android 16.

Updated translations for `fr-CA`, `kk`, and `kn`.

## [1.4.4][] (2024-10-17)

Support for Android 15.

## [1.4.3][] (2024-08-12)

Redirected long-tap to NFC Settings when granted `WRITE_SECURE_SETTINGS` permission.

## [1.4.2][] (2024-07-30)

Handle a rare exception when unregistering the broadcast listener.

## [1.4.1][] (2024-06-29)

Added metadata for F-Droid.

## [1.4.0][] (2024-06-22)

Replaced timer with broadcast listener for more responsive tile updates.

Added support for direct NFC toggle if granted `WRITE_SECURE_SETTINGS` permission.

## [1.3.1][] (2023-12-04)

Enabled code minification and resource shrinkage.

## [1.3.0][] (2023-10-21)

Added tile update timer to solve race condition.

## [1.2.2][] (2023-10-20)

Fixed potential crash when tile is added to Quick Settings menu.

## [1.2.1][] (2023-10-20)

Exclude devices without NFC hardware.

## [1.2.0][] (2023-10-14)

Support for Android 14.

## [1.1.0][] (2023-08-13)

Added translations for 86 languages.

## [1.0.0][] (2023-07-15)

Initial release.

[Unreleased]: https://github.com/pcolby/nfc-quick-settings/compare/v1.4.5...HEAD
[1.4.5]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.4.5
[1.4.4]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.4.4
[1.4.3]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.4.3
[1.4.2]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.4.2
[1.4.1]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.4.1
[1.4.0]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.4.0
[1.3.1]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.3.1
[1.3.0]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.3.0
[1.2.2]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.2.2
[1.2.1]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.2.1
[1.2.0]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.2.0
[1.1.0]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.1.0
[1.0.0]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.0.0

[^1]: Format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and
  project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
