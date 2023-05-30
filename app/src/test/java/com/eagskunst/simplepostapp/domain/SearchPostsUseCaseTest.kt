package com.eagskunst.simplepostapp.domain

import com.eagskunst.simplepostapp.data.PostsRemoteDataSource
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

class SearchPostsUseCaseTest : BaseUseCaseTests<SearchPostsUseCase>() {
    @MockK(relaxed = true)
    lateinit var postsRemoteDataSource: PostsRemoteDataSource

    @Test
    fun given_anyNotNullEmptyString_when_search_then_verifyRemoteDataSourceDoSearch() {
        val words = listOf("lorem", "ipsum", "dolor", "sit", "")
        words.forEach { useCase.createObservable(it, {}, {}).test().await() }
        verify(exactly = words.size) { postsRemoteDataSource.searchPosts(any()) }
    }

    @Test
    fun given_nullString_when_search_then_verifyRemoteDataSourceDoSearchWithEmptyString() {
        val wordSlot = slot<String>()
        val dummyPosts = listOf(PostEntity("", ""))
        every {
            postsRemoteDataSource.searchPosts(capture(wordSlot))
        } returns Observable.just(dummyPosts)

        useCase.createObservable(null, {}, {}).test().await()
        verify { postsRemoteDataSource.searchPosts(any()) }
        assertThat(wordSlot.captured).isNotNull()
    }
}
