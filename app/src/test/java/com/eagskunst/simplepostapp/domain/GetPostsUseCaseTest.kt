package com.eagskunst.simplepostapp.domain

import com.eagskunst.simplepostapp.data.PostsRemoteDataSource
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

class GetPostsUseCaseTest : BaseUseCaseTests<GetPostsUseCase>() {
    @MockK(relaxed = true)
    lateinit var remoteDataSource: PostsRemoteDataSource

    @Test
    fun when_getPosts_then_verifyRemoteDataSourceGetsCalled() {
        every {
            remoteDataSource.getPosts()
        } returns Observable.just(listOf(PostEntity("", "")))
        useCase.createObservable(null, {}, {}).test()
            .await()
        verify { remoteDataSource.getPosts() }
    }
}
