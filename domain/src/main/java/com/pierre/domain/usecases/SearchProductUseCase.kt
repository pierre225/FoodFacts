package com.pierre.domain.usecases

import com.pierre.data.repository.SongsRepository

interface PreloadSongsUseCase : suspend () -> Unit

internal class PreloadSongsUseCaseImpl(
    private val songsRepository: SongsRepository,
) : PreloadSongsUseCase {

    override suspend fun invoke() = songsRepository.preloadSongs()

}