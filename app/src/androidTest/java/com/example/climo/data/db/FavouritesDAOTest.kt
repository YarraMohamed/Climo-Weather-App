package com.example.climo.data.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.climo.model.Favourites
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class FavouritesDAOTest {
    private lateinit var database: AppDatabase
    private lateinit var favouritesDAO: FavouritesDAO

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java).build()
        favouritesDAO = database.getFavouritesDAO()
    }

    @After
    fun cleanup(){
        database.close()
    }

    @Test
    fun addNewFavPlace_retrieveListWithNewItem() = runTest{
        //Given
        val fav  = Favourites(1,31.5,32.7,"Address")
        favouritesDAO.addFav(fav)

        //When
        val result = favouritesDAO.getFavouriteList().first()

        //Then
        assertThat(result.isNotEmpty(),`is`(true))
        assertThat(result.last(),`is`(fav))
    }

    @Test
    fun deleteFavPlace_retrieveListWithoutTheItem() = runTest {
        // Given
        val firstFavourite = Favourites(1, 31.5, 32.7, "Address")
        val secondFavourite = Favourites(2, 30.5, 20.0, "address")
        favouritesDAO.addFav(firstFavourite)
        favouritesDAO.addFav(secondFavourite)

        // When
        favouritesDAO.deleteFavourite(firstFavourite)
        val result = favouritesDAO.getFavouriteList().first()

        // Then
        assertThat(result.isNotEmpty(), `is`(true))
        assertThat(result.contains(firstFavourite), not(`is`(true)))
        assertThat(result.first(), `is`(secondFavourite))
    }
}