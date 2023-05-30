package com.eagskunst.simplepostapp.domain

import com.eagskunst.simplepostapp.data.PostsRemoteDataSource
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class AddPostUseCase @Inject constructor(
    backgroundScheduler: BackgroundScheduler,
    mainScheduler: MainScheduler,
    private val remoteDataSource: PostsRemoteDataSource,
) : BaseUseCase<Posts, PostEntity>(backgroundScheduler, mainScheduler) {

    override fun createObservable(
        params: PostEntity?,
        onSuccess: (Posts) -> Unit,
        onError: (Throwable?) -> Unit,
    ): Observable<Posts> {
        if (params == null) {
            return Observable.error(KotlinNullPointerException("Params cant be null"))
        }
        return remoteDataSource.addPost(params)
    }
}
