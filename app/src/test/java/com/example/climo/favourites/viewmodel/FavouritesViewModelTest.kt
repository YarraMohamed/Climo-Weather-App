package com.example.climo.favourites.viewmodel

import com.example.climo.data.Repository
import com.example.climo.model.Favourites
import com.example.climo.model.Response
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.isNotNull


class FavouritesViewModelTest{
    private lateinit var repository: Repository
    private lateinit var viewModel: FavouritesViewModel
    private val fakeList = mutableListOf(
        Favourites(1, 3.4, 5.5, "address1"),
        Favourites(2, 3.5, 5.7, "address2"),
        Favourites(3, 3.7, 5.0, "address3"),

    )

    @Before
    fun setup(){
        Dispatchers.setMain(StandardTestDispatcher())
        repository = mockk()
        viewModel = FavouritesViewModel(repository)
        coEvery {
            repository.getFavourites()
        }returns flowOf(fakeList)
    }

    @Test
    fun getFav_retrieveFreshListOfFav() = runTest{

        //When
        viewModel.getFav()
        val result = viewModel.favList.first{
            it is Response.Success
        }

        //Then
        assertThat(result, instanceOf(Response.Success::class.java))
        val successResponse = result as Response.Success
        assertThat(successResponse.data.size, equalTo(3))
    }

    @Test
    fun deleteFav_retrieveFreshListWithoutItem() = runTest {
        //Given
        val fav = fakeList.get(0)

        //When
        viewModel.deleteFav(fav)
        fakeList.remove(fav)
        viewModel.getFav()
        val result = viewModel.favList.first{
            it is Response.Success
        }

        //Then
        assertThat(result, instanceOf(Response.Success::class.java))
        val successResponse = result as Response.Success
        assertThat(successResponse.data.size, equalTo(2))

    }
}