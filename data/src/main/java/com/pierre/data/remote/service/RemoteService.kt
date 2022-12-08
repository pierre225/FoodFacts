package com.pierre.data.remote.service

import com.pierre.data.repository.model.DataSong
import retrofit2.Response
import retrofit2.http.GET

interface RemoteService {

    @GET("/img/shared/technical-test.json")
    suspend fun songs(): Response<List<DataSong.RemoteSong>>

}