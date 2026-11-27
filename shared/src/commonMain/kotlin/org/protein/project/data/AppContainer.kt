package org.protein.project.data

import org.protein.project.data.remote.UniProtProteinRepositoryImpl
import org.protein.project.data.remote.createUniProtHttpClient
import org.protein.project.domain.ProteinRepository

object AppContainer {
    private val httpClient by lazy { createUniProtHttpClient() }

    val proteinRepository: ProteinRepository by lazy {
        UniProtProteinRepositoryImpl(httpClient = httpClient)
    }
}
