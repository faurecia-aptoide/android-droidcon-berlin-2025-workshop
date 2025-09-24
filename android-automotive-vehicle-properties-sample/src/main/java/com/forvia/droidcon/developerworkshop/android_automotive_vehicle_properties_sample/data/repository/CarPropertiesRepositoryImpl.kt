package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.repository

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyConfig
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.ArraySet
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.CarConnectState
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.CarPropertiesModel
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.CarPropertiesRepository
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.VehiclePropertyStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Implementation of [com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.CarPropertiesRepository] that interacts with the Android Car API to fetch
 * and manage vehicle properties.
 *
 * @property context The application context.
 * @property vehiclePropertyStore A store for vehicle property definitions.
 */
class CarPropertiesRepositoryImpl(
    private val context: Context,
    private val vehiclePropertyStore: VehiclePropertyStore,
) : CarPropertiesRepository {

    private lateinit var car: Car
    private lateinit var cachedCarProperties: List<CarPropertiesModel>

    /**
     * Retrieves a list of car properties, optionally filtered by a string.
     *
     * If the properties have not been fetched yet, it connects to the car, reads all available
     * properties, and caches them. Subsequent calls will use the cached data and apply the filter.
     *
     * @param filter An optional string to filter properties by name or identifier.
     * @return A list of [CarPropertiesModel] matching the filter, or all properties if no filter is provided.
     */
    override fun getCarProperties(
        filter: String?,
        showOnlyImplemented: Boolean?
    ): List<CarPropertiesModel> {
        vehiclePropertyStore.ensureLoaded()
        if (!::cachedCarProperties.isInitialized) {
            val listOfInfo: MutableList<CarPropertiesModel?> = mutableListOf()
            if (::car.isInitialized && car.isConnected) {
                val carPropertyManager =
                    car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
                val propsStore = vehiclePropertyStore.getList()
                val availableProps = getAvailableVehicleProperties(carPropertyManager)
                val props = propsStore.map { each ->
                    val find = availableProps.find { it.propertyId == each.id?.toInt() }
                    val value = find?.let { readValue(carPropertyManager, it) }
                    val flatArea = find?.areaIds?.firstOrNull().toString()
                    CarPropertiesModel(
                        propertyName = vehiclePropertyStore.displayNameOf(each),
                        propertyDescription = each.description,
                        propertyIdentifier = each.id,
                        area = flatArea,
                        value = value,
                        isImplemented = find != null
                    )
                }
                listOfInfo.addAll(props)
            }
            cachedCarProperties = listOfInfo.filterNotNull()
            return cachedCarProperties
        }

        val filteredList = if (showOnlyImplemented != null) {
            cachedCarProperties.filter {
                (it.propertyName?.contains(filter ?: "", true) == true ||
                        it.propertyIdentifier?.contains(
                            filter ?: "",
                            true
                        ) == true) && it.isImplemented == showOnlyImplemented
            }
        } else {
            cachedCarProperties.filter {
                it.propertyName?.contains(filter ?: "", true) == true ||
                        it.propertyIdentifier?.contains(filter ?: "", true) == true
            }
        }

        return filteredList
    }

    /**
     * Disconnects from the Android Car service.
     * Updates the [carConnectionStatus] to [com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.CarConnectState.DISCONNECTED].
     */
    override fun disconnectCar() {
        if (::car.isInitialized && car.isConnected) {
            car.disconnect()
            carConnectionStatus.value = CarConnectState.DISCONNECTED
        }
    }

    /**
     * Connects to the Android Car service.
     *
     * Updates the [carConnectionStatus] based on the connection state:
     * - [CarConnectState.CONNECTING] while connecting.
     * - [CarConnectState.CONNECTED] when successfully connected.
     * - [CarConnectState.DISCONNECTED] if the connection fails or is lost.
     * - [CarConnectState.NOT_AVAILABLE] if the Car service is not available (e.g., on a non-automotive device).
     */
    override fun connectCar() {
        try {
            car = Car.createCar(
                context,
                Handler.createAsync(Looper.getMainLooper()),
                Car.CAR_WAIT_TIMEOUT_WAIT_FOREVER
            ) { mCar: Car, ready: Boolean ->
                if (ready) {
                    if (mCar.isConnecting) {
                        carConnectionStatus.update { CarConnectState.CONNECTING }
                    } else if (mCar.isConnected) {
                        carConnectionStatus.update { CarConnectState.CONNECTED }
                    } else {
                        carConnectionStatus.update { CarConnectState.DISCONNECTED }
                    }
                }
            }
        } catch (e: NoClassDefFoundError) {
            carConnectionStatus.update { CarConnectState.NOT_AVAILABLE }
        }

    }

    /**
     * A [kotlinx.coroutines.flow.MutableStateFlow] that emits the current connection state to the Android Car service.
     * Defaults to [CarConnectState.DISCONNECTED].
     */
    override val carConnectionStatus: MutableStateFlow<CarConnectState> =
        MutableStateFlow(CarConnectState.DISCONNECTED)

    /**
     * Tries to fetch all available property configurations from the [CarPropertyManager].
     * @param mgr The [CarPropertyManager] instance.
     * @return A list of [android.car.hardware.CarPropertyConfig] objects, or an empty list if an error occurs.
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

    /**
     * Reads a single property value (best-effort) for a given [CarPropertyConfig].
     * It attempts to read the value based on its data type. If the property is zoned,
     * it reads from the first available area ID, otherwise it defaults to area 0.
     * @param mgr The [CarPropertyManager] instance.
     * @param cfg The [CarPropertyConfig] for the property to read.
     * @return A string representation of the property value, "unsupported type" if the type is unknown, or an error message if reading fails.
     */
    fun readValue(mgr: CarPropertyManager, cfg: CarPropertyConfig<*>): String {
        val id = cfg.propertyId
        val area = cfg.areaIds.firstOrNull() ?: 0
        return runCatching {
            val type = cfg.propertyType

            if (type?.name?.lowercase()?.contains("boolean") == true) {
                mgr.getBooleanProperty(id, area).toString()
            } else if (type?.name?.lowercase()?.contains("float") == true) {
                mgr.getFloatProperty(id, area).toString()
            } else if (type?.name == "java.lang.Integer") {
                mgr.getIntProperty(id, area).toString()
            } else if (type?.name?.lowercase()?.contains("long") == true) {
                (mgr.getProperty(
                    Long::class.java,
                    id,
                    area
                ) as CarPropertyValue<Long>).value.toString()
            } else if (type?.name?.lowercase()?.contains("string") == true) {
                (mgr.getProperty(String::class.java, id, area) as CarPropertyValue<String>).value
            } else if (type?.name == "[Ljava.lang.Integer;") {
                var finalValue: String = ""
                mgr.getIntArrayProperty(id, area).onEach {
                    finalValue += "$it "
                }
                finalValue
            } else {
                "unsupported type=$type"
            }
        }.getOrElse { e ->
            // Permission or not available
            "— (${e::class.simpleName})"
        }
    }

    /**
     * Converts a property ID (integer) to its readable string name using [Any.toString].
     * @param propId The integer ID of the vehicle property.
     * @return The string name of the property, or its hexadecimal representation if the name cannot be resolved.
     */
    fun nameOf(propId: Int): String =
        try {
            VehiclePropertyIds.toString(propId)
        } catch (_: Throwable) {
            "0x${propId.toString(16)}"
        }
}