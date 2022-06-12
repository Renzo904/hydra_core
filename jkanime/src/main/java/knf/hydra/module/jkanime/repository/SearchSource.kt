/*
 * Created by @UnbarredStream on 11/06/22 21:32
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 11/06/22 21:32
 */

package knf.hydra.module.jkanime.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import knf.hydra.core.models.BypassModel
import knf.hydra.core.models.DirectoryModel
import knf.hydra.core.models.data.FilterRequest
import knf.hydra.module.jkanime.retrofit.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchSource(private val query: String?, private val bypassModel: BypassModel, private val filters: FilterRequest?) : PagingSource<Int, DirectoryModel>() {
    override fun getRefreshKey(state: PagingState<Int, DirectoryModel>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DirectoryModel> {
        return try {
            var filteredQuery = query?.replace("_", "-")
            if (filteredQuery != null) {
                filteredQuery = filteredQuery.replace(" ", "_")
                filteredQuery = filteredQuery.filter { c ->  Character.isLetterOrDigit(c) || c=='_'}
            }
            val list = withContext(Dispatchers.IO) { NetworkRepository.getSearchPage(filteredQuery, params.key?:1,bypassModel) }
            LoadResult.Page(
                list,
                null,
                if (list.size < 12) null else (params.key?:1) + 1
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}