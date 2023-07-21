package com.youarelaunched.challenge

import com.youarelaunched.challenge.data.repository.VendorsRepository
import com.youarelaunched.challenge.data.repository.model.Vendor
import com.youarelaunched.challenge.data.repository.model.VendorCategory
import com.youarelaunched.challenge.ui.screen.view.VendorsVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class VendorsLoadTest {

    @Mock
    private lateinit var vendorsRepository: VendorsRepository

    private lateinit var vendorsViewModel: VendorsVM

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        vendorsViewModel = VendorsVM(vendorsRepository)
    }

    @Test
    fun getVendorsDataLoadedSuccessfully() = runTest {
        val listVendorCategory = listOf(
            VendorCategory(id = 4, title = "Tit", imgUrl = "url"),
            VendorCategory(id = 3, title = "Tit 2", imgUrl = "url2"),
            VendorCategory(id = 5, title = "Tit 3", imgUrl = "url3"),
        )

        val listVendors = listOf(
            Vendor(
                id = 2,
                companyName = "Big Brain",
                coverPhoto = "https://cdn-staging.chatsumer.app/eyJidWNrZXQiOiJjaGF0c3VtZXItZ2VuZXJhbC1zdGFnaW5nLXN0b3JhZ2UiLCJrZXkiOiIxMy84ZjMzZTgyNy0yNzIxLTQ3ZjctYjViNS0zM2Q5Y2E2MTM1OGQuanBlZyJ9",
                area = "Bells",
                favorite = true,
                categories = listVendorCategory,
                tags = null
            )
        )

        `when`(vendorsRepository.getVendors()).thenReturn(listVendors)
        delay(1)
        val vendors = vendorsViewModel.uiState.value.vendors

        assertEquals(vendors , listVendors)
    }

    @Test
    fun getVendorsDataWithError() = runTest {
        `when`(vendorsRepository.getVendors()).thenThrow(RuntimeException())
        delay(1)
        val vendors = vendorsViewModel.uiState.value.vendors

        assertEquals(vendors , null)
    }


}