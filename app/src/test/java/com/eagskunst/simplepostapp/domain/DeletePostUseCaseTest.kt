package com.eagskunst.simplepostapp.domain

import com.eagskunst.simplepostapp.data.PostsRemoteDataSource
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

class DeletePostUseCaseTest : BaseUseCaseTests<DeletePostUseCase>() {
    @MockK
    lateinit var remoteDataSource: PostsRemoteDataSource

    @Test
    fun given_notNullPost_when_createObservable_then_verifyRemoteDataSourceIsInvoked() {
        val posts = listOf(
            PostEntity("", ""),
        )
        every { remoteDataSource.removePost(any()) } returns Observable.just(posts)

        useCase.createObservable(PostEntity("", ""), {}, {}).test()
            .assertValue(posts)

        verify { remoteDataSource.removePost(any()) }
    }

    @Test
    fun given_nullPost_when_createObservable_then_verifyUseCaseEmitsError() {
        useCase.createObservable(null, {}, {}).test()
            .assertFailure(KotlinNullPointerException::class.java)
        verify(exactly = 0) { remoteDataSource.removePost(any()) }
    }
}
