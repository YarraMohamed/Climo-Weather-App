package com.example.climo.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.climo.data.db.AppDatabase
import com.example.climo.data.db.FavouritesDAO
import com.example.climo.model.Favourites
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class FavouritesLocalDataSourceTest {
    private lateinit var favouritesLocalDataSource: FavouritesLocalDataSourceImp
    private lateinit var favouritesDAO: FavouritesDAO
    private lateinit var database: AppDatabase

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        favouritesDAO = database.getFavouritesDAO()
        favouritesLocalDataSource = FavouritesLocalDataSourceImp(favouritesDAO)
    }

    @After
    fun cleanup(){
        database.close()
    }

    @Test
    fun getFavouriteList_retrieveFreshListWithItems() = runTest {
        //Given
        val list = listOf(
            Favourites(1,20.5,13.6,"address"),
            Favourites(2,30.5,40.6,"address"),
            Favourites(3,25.5,43.6,"address")
        )

        list.forEach { fav ->
            favouritesLocalDataSource.addFav(fav)
        }

        //When
        val result = favouritesLocalDataSource.getFavouriteList().first()

        //Then
        assertThat(result.isNotEmpty(),`is`(true))
        assertThat(result.size,equalTo(3))


    }

    @Test
    fun addFav_retrieveFreshListWithNewFav() = runTest {
        //Given
        val fav = Favourites(1,5.0,30.1,"address")
        favouritesLocalDataSource.addFav(fav)

        //When
        val result = favouritesLocalDataSource.getFavouriteList().first()

        //Then
        assertThat(result.isNotEmpty(),`is`(true))
        assertThat(result.get(0).id, equalTo(1))
        assertThat(result.get(0).latitude, equalTo(5.0))
        assertThat(result.get(0).longitude, equalTo(30.1))
        assertThat(result.get(0).address, equalTo("address"))

    }

    @Test
    fun deleteFav_retrieveFreshListWithoutFav() = runBlocking {
        //Given
        val fav = Favourites(1,5.0,30.1,"address")
        favouritesLocalDataSource.deleteFav(fav)

        //When
        val result = favouritesLocalDataSource.getFavouriteList().first()

        //Then
        assertThat(result.isEmpty(),`is`(true))
        assertThat(result.size, equalTo(0))
    }
}