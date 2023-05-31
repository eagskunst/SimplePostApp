package com.eagskunst.simplepostapp.domain.usecase

import com.eagskunst.simplepostapp.data.PostsRemoteDataSource
import com.eagskunst.simplepostapp.domain.BackgroundScheduler
import com.eagskunst.simplepostapp.domain.MainScheduler
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
