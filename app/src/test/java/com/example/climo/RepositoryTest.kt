package com.example.climo

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.work.WorkManager
import com.example.climo.data.Repository
import com.example.climo.data.RepositoryImp
import com.example.climo.data.local.FakeAlertsLocalDataSource
import com.example.climo.data.local.FakeFavouritesLocalDataSource
import com.example.climo.data.local.FakeOptionsLocalDataSource
import com.example.climo.data.local.FakeWeatherLocalDataSource
import com.example.climo.data.remote.FakeWeatherRemoteDataSource
import com.example.climo.model.Favourites
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    private lateinit var favouritesLocalDataSource: FakeFavouritesLocalDataSource
    private lateinit var weatherRemoteDataSource: FakeWeatherRemoteDataSource
    private lateinit var weatherLocalDataSource: FakeWeatherLocalDataSource
    private lateinit var alertsLocalDataSource : FakeAlertsLocalDataSource
    private lateinit var optionsLocalDataSource: FakeOptionsLocalDataSource
    private lateinit var repository: Repository

   @Before
   fun setup(){
       favouritesLocalDataSource = FakeFavouritesLocalDataSource()
       weatherRemoteDataSource = FakeWeatherRemoteDataSource()
       weatherLocalDataSource = mockk(relaxed = true)
       alertsLocalDataSource = mockk(relaxed = true)
       optionsLocalDataSource = mockk(relaxed = true)
       repository = RepositoryImp.getInstance(weatherRemoteDataSource,favouritesLocalDataSource,weatherLocalDataSource,alertsLocalDataSource,optionsLocalDataSource)
   }

    @Test
    fun getFavourites_retrieveFreshListOfFav() = runTest{
        //Given
        val fav = Favourites(1,45.0,32.5,"address")
        repository.addFavourite(fav)

        //When
        val result = repository.getFavourites().first()

        //Then
        assertThat(result.isNotEmpty(),`is`(true))
        assertThat(result.contains(fav),`is`(true))
    }

    @Test
    fun deleteFavourites_retrieveFreshListWithoutFavourite() = runTest{
        //Given
        val fav = Favourites(1,45.0,32.5,"address")
        repository.addFavourite(fav)

        //When
        repository.deleteFavourite(fav)
        val result = repository.getFavourites().first()

        //Then
        assertThat(result.isEmpty(),`is`(true))
        assertThat(result.contains(fav),`is`(false))
    }

    @Test
    fun getCurrentWeather_getCurrentWeatherDetails() = runTest{
        //Given
        val lat = 30.1
        val lon = 32.5

        //When
        val result = repository.getCurrentWeather(lat,lon,"metric","en").first()

        //Then
        assertThat(result.clouds.all,`is`(30))
        assertThat(result.coord.lon,equalTo(30.1))

    }

    @Test
    fun getCurrentForecast_getListOfForecast() = runTest{
        //Given
        val lat = 30.1
        val lon = 32.5

        //When
        val result = repository.getCurrentForecast(lat,lon,"metric ").first()

        //Then
        assertThat(result.list.isNotEmpty(),`is`(true))
        assertThat(result.list.size, equalTo((2)))
    }
}