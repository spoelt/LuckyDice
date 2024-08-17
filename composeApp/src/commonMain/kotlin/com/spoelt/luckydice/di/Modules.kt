package com.spoelt.luckydice.di

import com.spoelt.luckydice.presentation.home.HomeViewModel
import com.spoelt.luckydice.presentation.selectgameoptions.SelectGameOptionsViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::SelectGameOptionsViewModel)
}