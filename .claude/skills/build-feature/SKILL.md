# Build Feature Skill

Use this skill when implementing a new feature or extending existing functionality in the SPD-Manager app. Follow the established patterns exactly.

## Feature Checklist

When building a feature, create files in this order:

1. **Model** (`model/{domain}/`)
2. **DeviceManager update methods** (`service/DeviceManager.kt`)
3. **ViewModel** (`viewmodel/`)
4. **Components** (`ui/components/{domain}/`)
5. **Screen** (`ui/screens/`)
6. **Navigation** (if new screen — `App.kt`)

All source goes under `composeApp/src/desktopMain/kotlin/org/xebia/spdmanager/`.

---

## 1. Model Layer

All models are immutable `data class` with `companion object` factory methods:

```kotlin
// model/feature/Feature.kt
data class Feature(
    val name: String,
    val volume: Int,
    val type: FeatureType
) {
    companion object {
        fun fromValues(rawName: Int, rawVolume: Int, rawType: Int): Feature {
            return Feature(
                name = NameConverter.decode(rawName),
                volume = rawVolume,
                type = FeatureType.fromValue(rawType)
            )
        }
    }
}
```

Enums use `fromValue()` with a fallback default:

```kotlin
enum class FeatureType(val value: Int) {
    TYPE_A(0), TYPE_B(1), TYPE_C(2);
    companion object {
        fun fromValue(value: Int): FeatureType = entries.find { it.value == value } ?: TYPE_A
    }
}
```

**Rules:**
- Never use `var` in models — all properties are `val`
- Use `data class` for entities, `enum class` for fixed sets
- Factory methods transform raw integer values into domain types
- Use `Map<EnumKey, Entity>` for indexed collections (e.g., pads by PadNumber)

---

## 2. DeviceManager Updates

Add update methods to `service/DeviceManager.kt` using the immutable copy pattern with transform lambdas:

```kotlin
// Top-level entity update
fun updateFeature(featureIndex: Int, transform: (Feature) -> Feature) {
    device?.let { currentDevice ->
        val updated = currentDevice.features.toMutableList()
        updated[featureIndex] = transform(updated[featureIndex])
        device = currentDevice.copy(features = updated)
    }
}

// Nested entity update — navigate hierarchy, transform leaf, reassign root
fun updateSubFeature(featureIndex: Int, subKey: SubKey, transform: (Sub) -> Sub) {
    device?.let { currentDevice ->
        val updatedFeatures = currentDevice.features.toMutableList()
        val feature = updatedFeatures[featureIndex]
        val updatedSubs = feature.subs.toMutableMap()
        updatedSubs[subKey]?.let { sub ->
            updatedSubs[subKey] = transform(sub)
        }
        updatedFeatures[featureIndex] = feature.copy(subs = updatedSubs)
        device = currentDevice.copy(features = updatedFeatures)
    }
}
```

**Rules:**
- `device` is `mutableStateOf<Device?>` — the single source of truth
- Always use `?.let { }` for null safety
- Always use `.toMutableList()` / `.toMutableMap()`, modify, then `.copy()` back
- Never mutate the existing data classes directly

---

## 3. ViewModel Layer

Two patterns exist — pick based on need:

### Pattern A: Derived State (for entity detail views)

Use when the ViewModel just reads and updates a slice of DeviceManager state:

```kotlin
// viewmodel/FeatureViewModel.kt
class FeatureViewModel(
    private val featureIndex: Int,
    private val deviceManager: DeviceManager
) {
    val feature by derivedStateOf {
        deviceManager.device?.features?.getOrNull(featureIndex)
    }

    fun updateName(name: String) {
        deviceManager.updateFeature(featureIndex) { it.copy(name = name) }
    }

    fun updateVolume(volume: Int) {
        deviceManager.updateFeature(featureIndex) { it.copy(volume = volume) }
    }
}
```

### Pattern B: StateFlow (for selection/coordination state)

Use when the ViewModel manages its own selection or UI-coordination state:

```kotlin
// viewmodel/FeatureViewModel.kt
class FeatureViewModel(val deviceManager: DeviceManager) {
    private val _selectedIndex = MutableStateFlow<Int?>(null)
    val selectedIndex: StateFlow<Int?> = _selectedIndex.asStateFlow()

    private val _activeTab = MutableStateFlow(0)
    val activeTab: StateFlow<Int> = _activeTab.asStateFlow()

    fun selectFeature(feature: Feature) {
        val index = deviceManager.device?.features?.indexOf(feature)
        if (index != null && index >= 0) {
            _selectedIndex.value = index
        }
    }
}
```

**Rules:**
- Do NOT extend `ViewModel()` — just use plain classes with `derivedStateOf` or `MutableStateFlow`
- Private `_mutableFlow` + public `.asStateFlow()` for encapsulation
- Update methods named `update{Property}(value)`, selection methods named `select{Entity}(entity)`
- Use `derivedStateOf` when you only need a computed view of DeviceManager state
- Use `MutableStateFlow` when you own independent state (selections, toggles)

---

## 4. Components

Components live in `ui/components/{domain}/` and are stateless Composable functions with callback lambdas:

```kotlin
// ui/components/feature/FeatureView.kt
@Composable
fun FeatureView(
    feature: Feature,
    onFeatureChange: (Feature) -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(5.dp)) {
        TextField(
            value = feature.name,
            onValueChange = { onFeatureChange(feature.copy(name = it)) }
        )
        SliderWithLabel(
            label = "Volume",
            value = feature.volume.toFloat(),
            onValueChange = { onFeatureChange(feature.copy(volume = it.toInt())) },
            valueRange = 0f..127f
        )
        DropdownSelector(
            label = "Type",
            selectedItem = feature.type,
            onItemSelected = { onFeatureChange(feature.copy(type = it)) },
            items = FeatureType.entries.toList()
        )
    }
}
```

**Reusable common components** (already exist in `ui/components/common/`):
- `SliderWithLabel` — labeled slider with value display
- `IntStepSliderWithLabel` — integer-step slider
- `DropdownSelector<T>` — generic dropdown with label
- `SwitchWithLabel` — toggle switch
- `GenericListView<T>` / `GenericListItemView<T>` — generic lazy list

**Rules:**
- Components receive data and emit changes via lambdas — no direct ViewModel access
- Exception: accessing `LocalDeviceManager.current` for read-only reference data (e.g., wave lists) is acceptable
- Use `remember { mutableStateOf() }` only for local UI state (expanded dropdown, selected tab)
- Tab switching uses `TabRow` + `when (selectedTab)` pattern
- Layout uses `Row`/`Column` with `Modifier.weight()` for proportional sizing

---

## 5. Screen Layer

Screens live in `ui/screens/` and wire ViewModel to components:

```kotlin
// ui/screens/FeatureScreen.kt
@Composable
fun FeatureScreen() {
    val deviceManager = LocalDeviceManager.current

    val viewModel = remember(deviceManager) {
        FeatureViewModel(deviceManager)
    }

    val selectedIndex by viewModel.selectedIndex.collectAsState()
    val device = deviceManager.device

    // Early return for null/empty state
    if (device == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            SelectFolderButton()
        }
        return
    }

    // Multi-column layout with weights
    Row(Modifier.fillMaxSize()) {
        Column(Modifier.weight(0.3f).fillMaxHeight()) {
            GenericListView(device.features, onItemSelected = viewModel::selectFeature) { feature ->
                Text(feature.name, fontSize = 18.sp)
            }
        }
        Column(Modifier.weight(0.7f).fillMaxHeight()) {
            selectedIndex?.let { index ->
                FeatureDetailScreen(featureIndex = index, deviceManager = deviceManager)
            }
        }
    }
}
```

Detail screens instantiate their own ViewModel with entity-specific keys:

```kotlin
@Composable
fun FeatureDetailScreen(featureIndex: Int, deviceManager: DeviceManager) {
    val viewModel = remember(featureIndex) {
        FeatureViewModel(featureIndex, deviceManager)
    }
    val feature = viewModel.feature

    if (feature == null) {
        Text("Feature not found", Modifier.padding(16.dp))
        return
    }

    FeatureView(
        feature = feature,
        onFeatureChange = { updated ->
            viewModel.updateName(updated.name)
            viewModel.updateVolume(updated.volume)
        }
    )
}
```

**Rules:**
- Instantiate ViewModel with `remember(key) { }` — key ensures recreation when selection changes
- Collect StateFlow with `by viewModel.flow.collectAsState()`
- Early `return` when data is null — show placeholder or empty state
- Pass `deviceManager` explicitly to detail sub-screens
- Callback propagation: bubble callbacks up through lambda parameters

---

## 6. Navigation

If the feature needs a new top-level screen, update `App.kt`:

```kotlin
// Add to sealed class
sealed class Screen {
    object Main : Screen()
    object Setup : Screen()
    object System : Screen()
    object Feature : Screen()  // Add new screen
}

// Add menu item in main() Window MenuBar
Menu("View", mnemonic = 'V') {
    Item("Feature", onClick = { AppState.currentScreen = Screen.Feature })
}

// Add to when block in App()
@Composable
fun App() {
    when (AppState.currentScreen) {
        Screen.Main -> MainScreen()
        Screen.Setup -> SetupScreen()
        Screen.System -> SystemScreen()
        Screen.Feature -> FeatureScreen()
    }
}
```

---

## Naming Conventions

| Type | Convention | Example |
|------|-----------|---------|
| Model | `{Name}.kt` | `Feature.kt` |
| Enum | `{Name}.kt` | `FeatureType.kt` |
| ViewModel | `{Name}ViewModel.kt` | `FeatureViewModel.kt` |
| Screen | `{Name}Screen.kt` | `FeatureScreen.kt` |
| Component | `{Name}View.kt` or `{Name}Section.kt` | `FeatureView.kt` |
| Update methods | `update{Property}` | `updateVolume` |
| Selection methods | `select{Entity}` | `selectFeature` |
| Callbacks | `on{Action}` | `onFeatureChange` |
| Factory methods | `fromValue` / `fromValues` | `FeatureType.fromValue(0)` |

## Mapping Documentation

Refer to `Map_Kit.md`, `Map_Pad.md`, `Map_Setup.md`, `Map_System.md` in the project root for Roland SPD-Pro parameter names, value ranges, and effect type definitions when implementing model classes or UI for specific device features.

## Suggested Test Plan

Create test plan with detailed instructions and suggestions on tests and save it in ./test-plans/<the same name as feature file>.md .

## UI guidelines

Follow the design system defined in /ui-guidelines skill for all UI work.