package com.youarelaunched.challenge

import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.youarelaunched.challenge.data.repository.model.Vendor
import com.youarelaunched.challenge.data.repository.model.VendorCategory
import com.youarelaunched.challenge.ui.screen.state.VendorsScreenUiState
import com.youarelaunched.challenge.ui.screen.view.VendorsScreen
import com.youarelaunched.challenge.ui.theme.VendorAppTheme
import org.junit.Rule
import org.junit.Test


class VendorsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testVisibleVendorsItems() {
        val uiState = VendorsScreenUiState(
            vendors = listOf(),
            true
        )

        composeTestRule.activity.setContent {
            VendorAppTheme {
                VendorsScreen(
                    uiState = uiState,
                    onClickButtonSearch = {},
                    onSearchAfter3Letter = { _, _ -> }) {

                }
            }
        }

        composeTestRule.onNodeWithText("Sorry! No results found…").assertIsDisplayed()
    }

    @Test
    fun testInvisibleVendorsItems() {

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
        val uiState = VendorsScreenUiState(
            listVendors
        )

        composeTestRule.activity.setContent {
            VendorAppTheme {
                VendorsScreen(
                    uiState = uiState,
                    onClickButtonSearch = {},
                    onSearchAfter3Letter = { _, _ -> }) {
                }
            }
        }

        composeTestRule.onNodeWithText("Big Brain").assertExists()
    }
}