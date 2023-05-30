package com.eagskunst.simplepostapp.domain

import com.eagskunst.simplepostapp.data.PostsRemoteDataSource
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchPostsUseCase @Inject constructor(
    backgroundScheduler: BackgroundScheduler,
    mainScheduler: MainScheduler,
    private val remoteDataSource: PostsRemoteDataSource,
) : BaseUseCase<Posts, String>(backgroundScheduler, mainScheduler) {

    override fun createObservable(
        params: String?,
        onSuccess: (Posts) -> Unit,
        onError: (Throwable?) -> Unit,
    ): Observable<Posts> {
        return remoteDataSource.searchPosts(params ?: "")
    }
}
