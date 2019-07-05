package com.example.countries

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.countries.model.CountriesService
import com.example.countries.model.Country
import com.example.countries.viewModel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

/**
 * Created by Rasa Cosmin on 05/07/2019.
 */

class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var countriesService: CountriesService

    @InjectMocks
    var listViewModel = ListViewModel()

    private var testSingle : Single<List<Country>>? = null

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getCountriesSuccess(){
        val country = Country("name", "capital", "url")
        val countryList = arrayListOf(country)
        testSingle = Single.just(countryList)

        `when`(countriesService.getCountries()).thenReturn(testSingle)

        listViewModel.refresh()

        assertEquals(1, listViewModel.countries.value?.size)
        assertEquals(false, listViewModel.countryLoadError.value)
        assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getCountriesFail(){
        testSingle = Single.error(Throwable())

        `when`(countriesService.getCountries()).thenReturn(testSingle)
        listViewModel.refresh()

        assertEquals(true, listViewModel.countryLoadError.value)
        assertEquals(false, listViewModel.loading.value)
    }

    @Before
    fun setupRXSchedulers(){
        val immediate = object : Scheduler(){
            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, unit)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }

        }

        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }
    }




}