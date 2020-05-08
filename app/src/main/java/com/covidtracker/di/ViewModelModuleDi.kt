package com.covidtracker.di

import com.covidtracker.ui.main.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
val mainViewModelModule = module {
    viewModel {
        MainViewModel(get())
    }
}