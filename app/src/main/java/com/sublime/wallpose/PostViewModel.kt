package com.sublime.wallpose

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.sublime.wallpose.models.UnsplashImageDetails
import com.sublime.wallpose.repository.PopularDataSource
import com.sublime.wallpose.repository.PopularDataSourceFactory
import com.sublime.wallpose.repository.PostDataSource
import com.sublime.wallpose.repository.PostDataSourceFactory

class PostViewModel : ViewModel() {

    var popularPagedList: LiveData<PagedList<UnsplashImageDetails>>? = null
    var postPagedList: LiveData<PagedList<UnsplashImageDetails>>? = null
    private var popularLiveDataSource: LiveData<PageKeyedDataSource<Int, UnsplashImageDetails>>? =
        null
    private var postLiveDataSource: LiveData<PageKeyedDataSource<Int, UnsplashImageDetails>>? = null

    init {
        val popularDataSourceFactory = PopularDataSourceFactory(viewModelScope)
        popularLiveDataSource = popularDataSourceFactory.getPopularLiveDataSource()
        val popularImageConfig: PagedList.Config =
            (PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setPageSize(PopularDataSource(viewModelScope).PAGE_SIZE).build()
        popularPagedList =
            LivePagedListBuilder(popularDataSourceFactory, popularImageConfig).build()


        val postDataSourceFactory = PostDataSourceFactory(viewModelScope)
        postLiveDataSource = postDataSourceFactory.getPostLiveDataSource()

        val postImageConfig: PagedList.Config =
            (PagedList.Config.Builder()).setEnablePlaceholders(false)
                .setPageSize(PostDataSource(viewModelScope).PAGE_SIZE).build()

        postPagedList = LivePagedListBuilder(postDataSourceFactory, postImageConfig).build()
    }

}
