/*
 * Created by @UnbarredStream on 13/04/22 11:59
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/04/22 2:59
 */

package knf.hydra.module.jkanime.extras

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import knf.hydra.core.HeadRepository
import knf.hydra.core.models.*
import knf.hydra.core.models.data.FilterRequest
import knf.hydra.core.models.data.SourceData
import knf.hydra.module.jkanime.repository.DirectorySource
import knf.hydra.module.jkanime.repository.RecentsSource
import knf.hydra.module.jkanime.repository.SearchSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Repository : HeadRepository(){

    override fun infoPage(link: String, bypassModel: BypassModel): Flow<InfoModel?> {
        return flow {
            emit(null)
        }
    }

    override suspend fun sourceData(content: ContentItemMin, bypassModel: BypassModel): SourceData<*>? = null

    override suspend fun recentsPager(bypassModel: BypassModel): Flow<PagingData<RecentModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RecentsSource() }
        ).flow
    }

    override suspend fun directoryPager(bypassModel: BypassModel, filters: FilterRequest?): Flow<PagingData<DirectoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 24,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { DirectorySource(bypassModel) }
        ).flow
    }

    override suspend fun searchPager(
        query: String?,
        bypassModel: BypassModel,
        filters: FilterRequest?
    ): Flow<PagingData<DirectoryModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 12,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchSource(query, bypassModel, filters) }
        ).flow
    }
}