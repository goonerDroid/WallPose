package com.sublime.wallpose.repository

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.sublime.wallpose.data.JsonApi
import com.sublime.wallpose.data.RetrofitService
import com.sublime.wallpose.models.UnsplashImageDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PopularDataSource(private val scope: CoroutineScope) :
    PageKeyedDataSource<Int, UnsplashImageDetails>() {

    @Suppress("PrivatePropertyName")
    public val PAGE_SIZE = 20

    @Suppress("PrivatePropertyName")
    private val FIRST_PAGE = 1
    private val accessKey = "e3bc7bf237473a863b587b27220ec9b4a0a6f25e8b1514053c91d212a312b777"
    private val orderBy = "popular"
    private var jsonApi: JsonApi = RetrofitService.createService(JsonApi::class.java)

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UnsplashImageDetails>
    ) {

        scope.launch {
            try {
                val response = jsonApi.getPopularImages(accessKey, params.key, PAGE_SIZE, orderBy)

                when {
                    response.isSuccessful -> {
                        val key: Int? = if (response.body()!!.isNotEmpty()) {
                            params.key + 1
                        } else {
                            null
                        }
                        callback.onResult(response.body()!!, key)
                    }
                }

            } catch (e: Exception) {
                Log.w("PopularDataSource->Load After->Get Popular Images", "" + e.message)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UnsplashImageDetails>
    ) {

        scope.launch {
            try {

                val response = jsonApi.getPopularImages(accessKey, params.key, PAGE_SIZE, orderBy)
                val key: Int? = if (params.key > 1) {
                    params.key - 1
                } else {
                    null
                }

                when {
                    response.isSuccessful -> {
                        callback.onResult(response.body()!!, key)
                    }
                }

            } catch (e: Exception) {
                Log.w("PopularDataSource->Load Before->Get Popular Images", "" + e.message)
            }
        }
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UnsplashImageDetails>
    ) {
        scope.launch {

            try {
                val response = jsonApi.getPopularImages(accessKey, FIRST_PAGE, PAGE_SIZE, orderBy)
                when {
                    response.isSuccessful -> {
                        callback.onResult(response.body()!!, null, FIRST_PAGE + 1)
                    }
                }
            } catch (exception: Exception) {
                Log.w("PopularDataSource->LoadInitial->Get Popular Images", "" + exception.message)
            }
        }
    }

    override fun invalidate() {
        super.invalidate()
        scope.cancel()
    }


}
