package com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.SetupManager
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.SetupManagerImpl
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.pushNotificationsDataStore
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.datastore.sharedPreferencesDataStore
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.PushServiceImpl
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.FakeApplicationServer
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.MyPushManager
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.data.push.pushmanager.MyPushManagerImpl
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.GenerateCURLUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.GetDistributorsUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.GetSetupStateUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.RegisterPushServiceUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.SaveSetupStateUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.SendPushNotificationUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.domain.usecases.SubscribeToSavedPushNotificationsUseCase
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.navigation.NavigationViewModel
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.notifications.NotificationsManager
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.notifications.NotificationsManagerImpl
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.permissions.NotificationPermissionHandler
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.permissions.NotificationPermissionHandlerImpl
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.main.MainViewModel
import com.forvia.droidcon.developerworkshop.android_automotive_covesa_push_notifications_sample.presentation.ui.screens.setup.SetupViewModel
import global.covesa.sdk.api.client.push.PushService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

object PushNotificationsModule {
    val dataModule = module {
        single<DataStore<Preferences>> { androidContext().pushNotificationsDataStore }
        single<DataStore<Preferences>> { androidContext().sharedPreferencesDataStore }
        singleOf(::SetupManagerImpl) { bind<SetupManager>() }
        single { FakeApplicationServer(androidContext(), get()) }
        singleOf(::NotificationsManagerImpl) { bind<NotificationsManager>() }
        singleOf(::MyPushManagerImpl) { bind<MyPushManager>() }
        singleOf(::PushServiceImpl) { bind<PushService>() }
    }

    val domainModule = module {
        factory { GetDistributorsUseCase(get()) }
        factory { RegisterPushServiceUseCase(get()) }
        factory { SendPushNotificationUseCase(get()) }
        factory { SubscribeToSavedPushNotificationsUseCase(get()) }
        factory { GetSetupStateUseCase(get()) }
        factory { SaveSetupStateUseCase(get()) }
        factory { GenerateCURLUseCase(get()) }
    }

    val presentationModule = module {
        singleOf(::NotificationPermissionHandlerImpl) { bind<NotificationPermissionHandler>() }
        viewModelOf(::NavigationViewModel)
        viewModelOf(::MainViewModel)
        viewModelOf(::SetupViewModel)
    }

    val modules = listOf(
        dataModule,
        domainModule,
        presentationModule
    )
}
