package com.youarelaunched.challenge.data.network.api

import android.content.Context
import android.util.Log
import androidx.compose.ui.text.toUpperCase
import com.google.gson.Gson
import com.youarelaunched.challenge.data.network.models.NetworkVendor
import com.youarelaunched.challenge.data.network.models.NetworkVendorsData
import com.youarelaunched.challenge.di.DispatcherIo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class NetworkDataProvider @Inject constructor(
    @DispatcherIo private val workDispatcher: CoroutineDispatcher,
    @ApplicationContext private val appContext: Context
) : ApiVendors {

    override suspend fun getVendors(): List<NetworkVendor> = withContext(workDispatcher) {
        val json = appContext.assets
            .open("vendors.json")
            .bufferedReader()
            .use {
                it.readText()
            }

        val vendors = Gson()
            .fromJson(json, NetworkVendorsData::class.java)
            .vendors

        Log.d("NetworkDataProvider", "getVendors: $vendors")
        return@withContext vendors
    }

    override suspend fun getFilterVendorsByCompanyName(companyName: String): List<NetworkVendor> {
        val allVendors = getVendors()
        return allVendors.filter {
            val upperCaseCompanyName = it.companyName.uppercase()
            upperCaseCompanyName.startsWith(companyName.uppercase())
        }

    }

}