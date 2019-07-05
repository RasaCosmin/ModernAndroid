package com.example.countries.di

import com.example.countries.model.CountriesService
import com.example.countries.viewModel.ListViewModel
import dagger.Component

/**
 * Created by Rasa Cosmin on 05/07/2019.
 */

@Component(modules = [ApiModule::class])
interface ApiComponent{
    fun inject(service: CountriesService)

    fun inject(viewModel: ListViewModel)
}