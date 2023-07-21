package com.youarelaunched.challenge.data.network.api

import com.youarelaunched.challenge.data.network.models.NetworkVendor

interface ApiVendors {

    suspend fun getVendors(): List<NetworkVendor>
    suspend fun getFilterVendorsByCompanyName(companyName : String): List<NetworkVendor>
}