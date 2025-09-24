package com.forvia.droidcon.developerworkshop.domain.usecases

import com.forvia.droidcon.developerworkshop.domain.repository.AppListRepository

class GetAppListUseCase(
    private val appListRepository: AppListRepository
) {
    operator fun invoke() = appListRepository.getAppList()
}
