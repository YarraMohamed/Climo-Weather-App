package com.example.climo.home.viewmodel

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.climo.data.Repository
import com.example.climo.data.RepositoryImp
import com.example.climo.data.local.FavouritesLocalDataSource
import com.example.climo.data.remote.WeatherRemoteDataSource
import com.example.climo.model.Clouds
import com.example.climo.model.Coord
import com.example.climo.model.CurrentWeather
import com.example.climo.model.Main
import com.example.climo.model.Response
import com.example.climo.model.Weather
import com.example.climo.model.WeatherStatus
import com.example.climo.model.Wind
import com.example.climo.utilities.ConnectivityListener
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.isNotNull

@RunWith(AndroidJUnit4::class)
class HomeViewModelTest{
    private lateinit var repository: Repository
    private lateinit var viewModel: HomeViewModel

    private val fakeCurrentWeather = CurrentWeather(
        Coord(30.1,32.5),
        listOf(
            Weather(
                2,
                "rain",
                "Description",
                "01d")
        ),
        Main(17.5,35,60),
        Wind(35.4),
        Clouds(30)
    )

    private val fakeWeatherStatus = WeatherStatus(
        30.1,32.5, fakeCurrentWeather)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() = runTest{
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mockk()

        coEvery {
            repository.getCurrentWeather(31.0,32.0,"metric","en")
        }returns flowOf(fakeCurrentWeather)

        coEvery {
            repository.getWeatherStatus(31.0,32.0)
        }returns flowOf(fakeWeatherStatus)

        coEvery {
            repository.getTempUnit()
        }returns flowOf("metric")

        coEvery {
            repository.getLanguage()
        }returns flowOf("en")
        viewModel = HomeViewModel(repository, ConnectivityListener(ApplicationProvider.getApplicationContext()))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun cleanup(){
        Dispatchers.resetMain()
    }

    @Test
    fun getCurrentWeather_onlineStatus_retrieveObjectOfCurrentWeather() = runTest {
        // Given
        val lat = 31.0
        val lon = 32.0

        // When
        viewModel.getCurrentWeather(lat, lon,true)
        advanceUntilIdle()
        val result = viewModel.currentWeather.first{
            it is Response.Success
        }


        // Then
        assertThat(result, instanceOf(Response.Success::class.java))
        val successResponse = result as Response.Success
        assertThat(successResponse.data.clouds.all, equalTo(30))
    }

    @Test
    fun getCurrentWeather_offlineStatus_retrieveObjectOfCurrentWeather() = runTest {
        // Given
        val lat = 31.0
        val lon = 32.0

        // When
        viewModel.getCurrentWeather(lat, lon,false)
        advanceUntilIdle()
        val result = viewModel.currentWeather.first{
            it is Response.Success
        }


        // Then
        assertThat(result, instanceOf(Response.Success::class.java))
        val successResponse = result as Response.Success
        assertThat(successResponse.data.clouds.all, equalTo(30))
    }


}