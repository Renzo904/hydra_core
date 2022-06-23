/*
 * Created by @UnbarredStream on 12/06/22 18:40
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 12/06/22 12:39
 */

package knf.hydra.module.cuevana.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import knf.hydra.core.models.BypassModel
import knf.hydra.core.models.RecentModel
import knf.hydra.module.cuevana.retrofit.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecentsSource(private val bypassModel: BypassModel): PagingSource<Int,RecentModel>() {
    override fun getRefreshKey(state: PagingState<Int, RecentModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecentModel> {
        return try {
            val list = withContext(Dispatchers.IO) { NetworkRepository.getRecentPage(bypassModel) }
            LoadResult.Page(
                list,
                null,
                null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}