package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen

import android.text.Spannable
import android.text.SpannableString
import androidx.car.app.CarContext
import androidx.car.app.HostException
import androidx.car.app.Screen
import androidx.car.app.model.CarLocation
import androidx.car.app.model.Distance
import androidx.car.app.model.DistanceSpan
import androidx.car.app.model.ItemList
import androidx.car.app.model.Metadata
import androidx.car.app.model.OnClickListener
import androidx.car.app.model.Place
import androidx.car.app.model.PlaceListMapTemplate
import androidx.car.app.model.PlaceMarker
import androidx.car.app.model.Row
import androidx.car.app.model.Template
import androidx.lifecycle.lifecycleScope
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.model.PoiModel
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.errorscreen.ErrorScreen
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen.actions.MainAction
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen.actions.MainEvent
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.utils.launchMapChooser
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.utils.launchMapChooserNoCarApi
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin

class PoiListScreen(
    private val carContext: CarContext,
    private val viewModel: PoiListViewModel = getKoin().get()
) : Screen(carContext) {

    init {
        lifecycleScope.launch {
            viewModel.onAction(MainAction.FetchList(carContext))
            viewModel.poiList.collect {
                invalidate()
            }
        }
    }

    override fun onGetTemplate(): Template {
        val currentPois = viewModel.poiList.value

        lifecycleScope.launch {
            viewModel.event.collect {
                when (it) {
                    is MainEvent.ShowMessage -> {
                        screenManager.push(
                            ErrorScreen(
                                carContext = carContext,
                                message = it.message,
                                title = it.title,
                                actionTitle = null,
                                onAction = {
                                    screenManager.pop()
                                }
                            )
                        )
                    }

                    null -> {

                    }
                }
            }
        }

        return getRows(currentPois)
    }

    private fun clickListener(poi: PoiModel): OnClickListener = OnClickListener {
        carContext.launchMapChooser(poi) { exception ->
            val baseMessage = when (exception) {
                is SecurityException -> "Security Exception"
                is HostException -> "HostException"
                is NullPointerException -> "Null Pointer Exception"
                else -> "Navigation Error"
            }
            val firstTryScreen = ErrorScreen(
                carContext = carContext,
                message = exception.cause?.message.toString(),
                title = "$baseMessage ${exception.message.toString()}",
                actionTitle = "Retry without startCarApp"
            ) {
                carContext.launchMapChooserNoCarApi(poi) { exception ->
                    val secondTryScreen = ErrorScreen(
                        carContext = carContext,
                        message = exception.message.toString(),
                        title = "Error launching navigation app",
                        actionTitle = null,
                        onAction = {
                            screenManager.popToRoot()
                        }
                    )
                    screenManager.remove(it)
                    screenManager.push(
                        secondTryScreen
                    )
                }
            }
            screenManager.push(
                firstTryScreen
            )
        }
    }

    private fun getRows(currentPois: List<PoiModel>): Template {
        val itemList = ItemList.Builder().apply {
            for (poi in currentPois) {
                addItem(
                    Row.Builder()
                        .setTitle("${poi.name} (${poi.latitude}, ${poi.longitude})")
                        .addText(SpannableString(" ").apply {
                            setSpan(
                                DistanceSpan.create(
                                    Distance.create(
                                        Math.random()*100,
                                        Distance.UNIT_KILOMETERS
                                    )
                                ),
                                0,1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                        })
                        .setOnClickListener(clickListener(poi))
                        .setMetadata(
                            Metadata.Builder()
                                .setPlace(
                                    Place.Builder(
                                        CarLocation.create(
                                            poi.latitude,
                                            poi.longitude
                                        )
                                    )
                                    .setMarker(PlaceMarker.Builder().build())
                                    .build()
                                ).build()
                        )
                        .build()
                )
            }
        }.build()

        return PlaceListMapTemplate.Builder()
            .setTitle("Nearby POIs")
            .setItemList(itemList)
            .build()
    }
}
