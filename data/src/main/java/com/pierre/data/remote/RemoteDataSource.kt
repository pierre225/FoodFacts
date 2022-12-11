package com.pierre.data.remote

import com.pierre.data.remote.models.RemoteProductResponse
import com.pierre.data.remote.service.RemoteService

interface RemoteDataSource {

    suspend fun product(code: String, countryCode: String, languageCode: String): RemoteProductResponse?

}

internal class RemoteDataSourceImpl(
    private val service: RemoteService
) : RemoteDataSource {

    override suspend fun product(code: String, countryCode: String, languageCode: String) : RemoteProductResponse? =
        service.product(code, countryCode, languageCode).body()

}