package com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.di

import com.forvia.droidcon.common.di.CommonModules
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.data.datasource.PoiDataSource
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.data.datasource.PoiDataSourceImpl
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.mappers.PoiMapper
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.mappers.PoiMapperImpl
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.usecases.GetPOIsUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.domain.usecases.GetPOIsUseCaseImpl
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.mappers.PoiModelMapper
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.mappers.PoiModelMapperImpl
import com.forvia.droidcon.developerworkshop.android_automotive_poi_sample.presentation.ui.listscreen.PoiListViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object PoiModules {
    private val appModule = module {
        singleOf(::PoiMapperImpl) { bind<PoiMapper>() }
        singleOf(::PoiModelMapperImpl) { bind<PoiModelMapper>() }
        singleOf(::PoiDataSourceImpl) { bind<PoiDataSource>() }
        factory<GetPOIsUseCase> {
            GetPOIsUseCaseImpl(
                dataSource = get(),
                poiMapper = get(),
                locationInteractor = get()
            )
        }
        viewModelOf(::PoiListViewModel)
    }

    val modules = listOf(
        appModule,
    ) + CommonModules.modules
}
