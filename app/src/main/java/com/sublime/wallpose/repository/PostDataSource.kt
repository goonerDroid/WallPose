package com.sublime.wallpose.repository

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.sublime.wallpose.data.JsonApi
import com.sublime.wallpose.data.RetrofitService
import com.sublime.wallpose.models.UnsplashImageDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PostDataSource(private val scope: CoroutineScope) :
    PageKeyedDataSource<Int, UnsplashImageDetails>() {

    @Suppress("PrivatePropertyName")
    val PAGE_SIZE = 20

    @Suppress("PrivatePropertyName")
    private val FIRST_PAGE = 1
    private val accessKey = "e3bc7bf237473a863b587b27220ec9b4a0a6f25e8b1514053c91d212a312b777"
    private var jsonApi: JsonApi = RetrofitService.createService(JsonApi::class.java)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UnsplashImageDetails>
    ) {
        scope.launch {
            try {
                val response = jsonApi.getImages(accessKey, FIRST_PAGE, PAGE_SIZE)
                when {
                    response.isSuccessful -> {
                        callback.onResult(response.body()!!, null, FIRST_PAGE + 1)
                    }
                }

            } catch (e: Exception) {
                Log.w("PostDataSource->Load Initial->Get Post Images", "" + e.message)
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UnsplashImageDetails>
    ) {
        scope.launch {
            val response = jsonApi.getImages(accessKey, FIRST_PAGE, PAGE_SIZE)

            try {
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
                Log.w("PostDataSource->Load After->Get Post Images", "" + e.message)
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, UnsplashImageDetails>
    ) {
        scope.launch {
            try {

                val response = jsonApi.getImages(accessKey, params.key, PAGE_SIZE)
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
                Log.w("PostDataSource->Load Before->Get Post Images", "" + e.message)
            }
        }
    }


}
