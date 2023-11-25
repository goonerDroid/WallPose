package com.sublime.wallpose.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.sublime.wallpose.models.UnsplashImageDetails
import kotlinx.coroutines.CoroutineScope

class PostDataSourceFactory(private val scope: CoroutineScope) :
    DataSource.Factory<Int, UnsplashImageDetails>() {

    private val postLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, UnsplashImageDetails>> =
        MutableLiveData()

    override fun create(): DataSource<Int, UnsplashImageDetails> {
        val postDataSource = PostDataSource(scope)
        postLiveDataSource.postValue(postDataSource)
        return postDataSource
    }

    fun getPostLiveDataSource(): MutableLiveData<PageKeyedDataSource<Int, UnsplashImageDetails>> {
        return postLiveDataSource
    }

}
