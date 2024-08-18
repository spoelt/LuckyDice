package com.spoelt.luckydice.di

import com.spoelt.luckydice.data.repository.GameRepositoryImpl
import com.spoelt.luckydice.domain.repository.GameRepository
import com.spoelt.luckydice.presentation.home.HomeViewModel
import com.spoelt.luckydice.presentation.selectgameoptions.SelectGameOptionsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    // Repository
    singleOf(::GameRepositoryImpl).bind<GameRepository>()

    // ViewModel
    viewModelOf(::HomeViewModel)
    viewModelOf(::SelectGameOptionsViewModel)
}