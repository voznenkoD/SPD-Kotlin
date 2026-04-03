
# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

SPD-Manager is a Kotlin Multiplatform Desktop application for managing Roland SPD-Pro drum pad device configurations. It reads/writes device configuration files (XML-based) and provides a UI for editing kits, pads, setup, and system settings.

**Package:** `org.xebia.spdmanager`  
**Main class:** `org.xebia.spdmanager.AppKt`

## Build Commands

```bash
./gradlew build                # Build the project
./gradlew desktopRun           # Run the desktop application
./gradlew desktopTest          # Run tests
./gradlew allTests             # Run all platform tests
./gradlew packageDmg           # Create macOS DMG installer
./gradlew packageMsi           # Create Windows MSI installer
./gradlew packageDeb           # Create Linux DEB package
./gradlew clean                # Clean build artifacts
```

## Tech Stack

- **Kotlin 2.1.0** with Kotlin Multiplatform
- **Jetpack Compose Multiplatform 1.7.0** (Material 3) for UI
- **AndroidX Lifecycle** for ViewModel and lifecycle management
- **Kotlinx Coroutines** (with Swing dispatcher for desktop)
- **Jackson** for XML parsing (jackson-dataformat-xml, jackson-module-kotlin)
- **JUnit 4** for testing
- Dependency versions managed via `gradle/libs.versions.toml`

## Architecture

MVVM pattern with clear layer separation:

- **ViewModels** (`viewmodel/`): `MainViewModel`, `SetupViewModel`, `SystemViewModel`, `KitViewModel`, `PadViewModel` — manage UI state and business logic
- **Screens** (`ui/screens/`): `MainScreen`, `SetupScreen`, `SystemScreen`, `WaveDetailsScreen` — full-screen Compose views
- **Components** (`ui/components/`): Reusable UI pieces organized by domain (pad, kit, fx, setup, system, masterfx, common)
- **Models** (`model/`): Domain data classes — `Device`, `Kit`, `Pad`, `Wave`, `KitChain`, plus sub-packages for pad modes, MIDI, FX, setup, and system config
- **Services** (`service/`): `DeviceManager` (device I/O), `XmlParser` (XML parsing), `FolderChooser` (file dialogs)
- **Data** (`data/`): Parsers and converters (`NameConverter`, `Coordinate`)

All source lives under `composeApp/src/desktopMain/kotlin/org/xebia/spdmanager/`.

**Dependency injection** uses Compose `LocalProviders` (see `LocalProviders.kt`) — `DeviceManager` is provided via `LocalDeviceManager`.

**App flow:** User opens a folder containing SPD device export files → `DeviceManager` loads and parses XML config → ViewModels expose state → Compose screens render and allow editing.

## Device Mapping Documentation

`Map_Kit.md`, `Map_Pad.md`, `Map_Setup.md`, `Map_System.md` in the project root document the Roland SPD-Pro data model — parameter names, value ranges, and effect types. Reference these when working on model classes or UI components for specific device features.