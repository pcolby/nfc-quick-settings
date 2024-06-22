# Changelog[^1]

## [Unreleased][]

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

[Unreleased]: https://github.com/pcolby/nfc-quick-settings/compare/v1.3.1...HEAD
[1.3.1]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.3.1
[1.3.0]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.3.0
[1.2.2]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.2.2
[1.2.1]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.2.1
[1.2.0]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.2.0
[1.1.0]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.1.0
[1.0.0]: https://github.com/pcolby/nfc-quick-settings/releases/tag/v1.0.0

[^1]: Format follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and
  project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
