package com.eagskunst.simplepostapp.domain.usecase

import com.eagskunst.simplepostapp.data.PostsRemoteDataSource
import com.eagskunst.simplepostapp.domain.BackgroundScheduler
import com.eagskunst.simplepostapp.domain.MainScheduler
import com.eagskunst.simplepostapp.domain.entity.PostEntity
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
