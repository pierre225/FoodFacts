package com.pierre.domain.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.pierre.data.repository.model.DataSong
import com.pierre.domain.model.DomainSong

class SongsDomainMapper {

    fun toDomain(dataSong: DataSong) = DomainSong(
        id = dataSong.id,
        title = dataSong.title,
        imageUrl = dataSong.imageUrl,
        thumbnailUrl = dataSong.thumbnailUrl
    )

    fun mapPagingDataToDomain(pagingData: PagingData<out DataSong>) =
        pagingData.map { toDomain(it) }

}