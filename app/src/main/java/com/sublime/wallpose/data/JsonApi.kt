package com.sublime.wallpose.data

import com.sublime.wallpose.models.UnsplashImageDetails
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonApi {

    @GET("/photos/curated")
    suspend fun getPopularImages(
        @Query("client_id") accessKey: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): Response<List<UnsplashImageDetails>>


    @GET("/photos/")
    suspend fun getImages(
        @Query("client_id") accessKey: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ):Response<List<UnsplashImageDetails>>

}
