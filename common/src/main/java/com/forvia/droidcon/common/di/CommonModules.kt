package com.forvia.droidcon.common.di

import com.forvia.droidcon.common.location.LocationInteractor
import com.forvia.droidcon.common.location.LocationInteractorImpl
import com.forvia.droidcon.common.permissions.PermissionsManager
import com.forvia.droidcon.common.permissions.PermissionsManagerImpl
import com.forvia.droidcon.common.ui.ImmersiveModeController
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object CommonModules {
    private val common = module {
        singleOf(::LocationInteractorImpl) { bind<LocationInteractor>() }
        singleOf(::PermissionsManagerImpl) { bind<PermissionsManager>() }
        single { ImmersiveModeController() }
    }
    val modules = listOf(common)
}
