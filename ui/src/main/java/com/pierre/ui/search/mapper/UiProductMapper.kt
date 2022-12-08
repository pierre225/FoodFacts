package com.pierre.songs.ui.songslist.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.pierre.domain.model.DomainSong
import com.pierre.songs.ui.songslist.model.UiSong

class SongsUiMapper {

    private fun toUi(domainSong: DomainSong) = UiSong(
        id = domainSong.id,
        title = domainSong.title,
        image = domainSong.imageUrl,
        preview = domainSong.thumbnailUrl
    )

    fun mapPagingDataToDomain(pagingData: PagingData<DomainSong>) =
        pagingData.map { toUi(it) }
}