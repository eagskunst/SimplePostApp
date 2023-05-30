package com.eagskunst.simplepostapp.domain

import com.eagskunst.simplepostapp.data.PostsRemoteDataSource
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

typealias Posts = List<PostEntity>

class GetPostsUseCase @Inject constructor(
    backgroundScheduler: BackgroundScheduler,
    mainScheduler: MainScheduler,
    private val remoteDataSource: PostsRemoteDataSource,
) : BaseUseCase<Posts, Unit>(backgroundScheduler, mainScheduler) {
    override fun createObservable(
        params: Unit?,
        onSuccess: (Posts) -> Unit,
        onError: (Throwable?) -> Unit,
    ): Observable<Posts> {
        return remoteDataSource.getPosts()
    }
}
