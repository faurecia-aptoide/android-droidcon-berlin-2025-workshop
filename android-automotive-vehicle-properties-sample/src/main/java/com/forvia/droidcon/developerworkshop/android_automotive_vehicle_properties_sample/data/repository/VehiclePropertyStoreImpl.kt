package com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.repository

import android.car.VehiclePropertyIds
import android.content.Context
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.VehiclePropertiesBase
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.data.model.VehiclePropertiesDataModel
import com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.VehiclePropertyStore
import com.google.gson.Gson

/**
 * Implementation of [com.forvia.droidcon.developerworkshop.android_automotive_vehicle_properties_sample.domain.repository.VehiclePropertyStore] that loads vehicle property data from a JSON asset file.
 * Provides methods to access and retrieve information about vehicle properties.
 */

class VehiclePropertyStoreImpl(
    private val context: Context
) : VehiclePropertyStore {
    companion object VPMS {
        private const val ASSET_FILE = "vehicle_properties.json"
    }

    @Volatile
    private var loaded = false
    @Volatile
    private lateinit var propertyList: List<VehiclePropertiesDataModel>

    /**
     * Ensures that the vehicle property data is loaded from the asset file.
     * This method is safe to call multiple times; the data will only be loaded once.
     *
     * @throws Exception if an error occurs during loading.
     */
    @Throws(Exception::class)
    override fun ensureLoaded() {
        if (loaded) return
        synchronized(this) {
            if (loaded) return
            runCatching {
                context.assets.open(ASSET_FILE).bufferedReader().use { it.readText() }
            }.onSuccess {
                val parsed = Gson().fromJson(it, VehiclePropertiesBase::class.java)
                propertyList = parsed.vehicleProperties
                loaded = true
            }.onFailure { e ->
                throw e
            }
        }
    }

    override fun getList(): List<VehiclePropertiesDataModel> = propertyList

    /**
     * Finds a [VehiclePropertiesDataModel] by its ID.
     *
     * @param id The ID of the vehicle property to find.
     * @return The [VehiclePropertiesDataModel] if found, otherwise null.
     */
    override fun find(id: Int): VehiclePropertiesDataModel? =
        propertyList.find { it.id == id.toString() }

    /**
     * Gets the human-friendly display name for a given [VehiclePropertiesDataModel].
     * It prioritizes the name from the model, then tries to find the constant name
     * via reflection from [VehiclePropertyIds].
     *
     * @param model The [VehiclePropertiesDataModel] for which to get the display name.
     * @return The display name as a String, or null if not found.
     */
    override fun displayNameOf(model: VehiclePropertiesDataModel?): String? =
        model?.name ?: run {
            constantNameOf(model?.id?.toInt())?.let { name -> return name }
        }

    /**
     * Gets the description for a given [VehiclePropertiesDataModel].
     * If the model does not have a description, a fallback message is returned.
     *
     * @param model The [VehiclePropertiesDataModel] for which to get the description.
     * @return The description as a String.
     */
    override fun descriptionOf(model: VehiclePropertiesDataModel?): String? =
        model?.description ?: "No description available for this platform."

    /**
     * Reflects the constant name of a vehicle property ID from [VehiclePropertyIds].
     * This is used as a fallback if a display name is not available in the loaded data.
     *
     * @param id The integer ID of the vehicle property.
     * @return The constant name as a String if found, otherwise null.
     */
    override fun constantNameOf(id: Int?): String? = try {
        VehiclePropertyIds::class.java.fields
            .firstOrNull { f ->
                f.type == Int::class.javaPrimitiveType && f.getInt(null) == id
            }?.name
    } catch (_: Throwable) {
        null
    }
}
