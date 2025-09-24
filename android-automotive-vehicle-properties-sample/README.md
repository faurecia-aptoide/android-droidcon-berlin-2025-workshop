# Android Droidcon Berlin 2025 Workshop - Vehicle Properties

## Description

The application downloads the source code of `VehiclePropertiesIds.java` from the official repository and lists all the existing properties:
```
https://android.googlesource.com/platform/packages/services/Car/+/refs/heads/master/car-lib/src/android/car/VehiclePropertyIds.java
```

The goal of this sample application is to showcase that there are multiple vehicle properties defined in the car, but not all of them are accessible:
1. OEMs may not define all the properties.
2. Some properties might not be accessible due to Android's permission scheme. For example:
   1. [Fuel Type](https://developer.android.com/reference/android/car/VehiclePropertyIds#INFO_FUEL_TYPE) can be read by any application.
   2. [Vehicle Speed](https://developer.android.com/reference/android/car/VehiclePropertyIds#PERF_VEHICLE_SPEED) can be read by any application, as long as the user explicitly gives access to it.
   3. [VIN](https://developer.android.com/reference/android/car/VehiclePropertyIds#INFO_VIN) cannot be read by Third-Party applications.


Overall Vehicle Properties' documentation: https://developer.android.com/reference/android/car/VehiclePropertyIds

## Reading vehicle properties

Properties are read under the [CarPropertiesRepositoryImpl](src/main/java/com/forvia/droidcon/developerworkshop/android_automotive_vehicle_properties_sample/data/repository/CarPropertiesRepositoryImpl.kt) class.

```kotlin
    /**
     * Tries to fetch all available property configurations from the [CarPropertyManager].
     * @param mgr The [CarPropertyManager] instance.
     * @return A list of [CarPropertyConfig] objects, or an empty list if an error occurs.
     */
    fun getAvailableVehicleProperties(mgr: CarPropertyManager): List<CarPropertyConfig<*>> {
        // Best effort: ask for *all* IDs we know from VehiclePropertyIds via reflection.
        val ids = VehiclePropertyIds::class.java.fields
            .filter { it.type == Int::class.javaPrimitiveType }
            .map { it.getInt(null) }
            .toSet()

        val set = ArraySet<Int>().apply { addAll(ids) }
        return try {
            @Suppress("UNCHECKED_CAST")
            mgr.getPropertyList(set) as List<CarPropertyConfig<*>>
        } catch (t: Throwable) {
            emptyList()
        }
    }
```
Additional documentation: https://developer.android.com/reference/android/car/hardware/property/CarPropertyManager#getPropertyList(android.util.ArraySet%3Cjava.lang.Integer%3E)
