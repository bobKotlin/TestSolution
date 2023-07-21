package com.youarelaunched.challenge.ui.screen.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.youarelaunched.challenge.middle.R
import com.youarelaunched.challenge.ui.screen.state.VendorsScreenUiState
import com.youarelaunched.challenge.ui.screen.view.components.ChatsumerSnackbar
import com.youarelaunched.challenge.ui.screen.view.components.VendorItem
import com.youarelaunched.challenge.ui.theme.Shapes
import com.youarelaunched.challenge.ui.theme.VendorAppTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun VendorsRoute(
    viewModel: VendorsVM
) {
    val uiState by viewModel.uiState.collectAsState()


    VendorsScreen(
        uiState = uiState,
        onClickButtonSearch = {
            viewModel.onClickButtonSearch(it)
        },
        onSearchAfter3Letter = { textFieldValue, oldText ->
            viewModel.onSearchAfter3Letter(textFieldValue, oldText)
        },
        onSearchingFieldIsEmpty = {
            viewModel.getVendors()
        }
    )
}

@Composable
fun VendorsScreen(
    uiState: VendorsScreenUiState,
    onClickButtonSearch: (textSearching: String) -> Unit,
    onSearchAfter3Letter: (companyName: String, isFasterThenLoad: () -> Boolean) -> Unit,
    onSearchingFieldIsEmpty: () -> Unit
    ) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = VendorAppTheme.colors.background,
        snackbarHost = { ChatsumerSnackbar(it) }
    ) { paddings ->


        Column(modifier = Modifier.fillMaxSize()) {
            SearchField(onClickButtonSearch, onSearchAfter3Letter, onSearchingFieldIsEmpty)

            if (!uiState.vendors.isNullOrEmpty()) {

                LazyColumn(
                    modifier = Modifier
                        .padding(paddings)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(
                        vertical = 24.dp,
                        horizontal = 16.dp
                    )
                ) {
                    items(uiState.vendors) { vendor ->
                        VendorItem(
                            vendor = vendor
                        )
                    }
                }
            } else {
                ResultNotFound(uiState.isSearching)
            }

        }
    }
}

@Composable
fun SearchField(
    onClickButtonSearch: (textSearching: String) -> Unit,
    onSearchAfter3Letter: (companyName: String, isFasterThenLoad: () -> Boolean) -> Unit,
    onSearchingFieldIsEmpty: () -> Unit
) {
    var textSearching by remember { mutableStateOf(TextFieldValue()) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            shape = Shapes.large,
            color = Color.White,
            elevation = 5.dp
        ) {


            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .wrapContentHeight()
                        .weight(1f),
                    value = textSearching,
                    onValueChange = {
                        textSearching = it

                        if (it.text.length >= 3)
                            onSearchAfter3Letter(it.text) {
                                return@onSearchAfter3Letter textSearching.text != it.text
                            }
                        if (it.text.isEmpty()){
                            onSearchingFieldIsEmpty()
                        }
                    },
                    placeholder = {
                        Text(stringResource(R.string.search))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = VendorAppTheme.colors.text,
                        placeholderColor = VendorAppTheme.colors.text,
                        textColor = VendorAppTheme.colors.text
                    )
                )

                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            onClickButtonSearch.invoke(textSearching.text)
                        },
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,

                    )
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

@Composable
fun ResultNotFound(isSearching: Boolean) {
    if (isSearching) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.sorry_no_results_found),
                color = VendorAppTheme.colors.textDarkGreen,
                style = VendorAppTheme.typography.h2,
                textAlign = TextAlign.Center,

                )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.please_try_a_different_search_request),
                color = VendorAppTheme.colors.textDark,
                style = VendorAppTheme.typography.subtitle1,
                textAlign = TextAlign.Center,

                )
        }

    }
}
