package com.eagskunst.simplepostapp.data

import androidx.annotation.VisibleForTesting
import com.eagskunst.simplepostapp.commons.GenericException
import com.eagskunst.simplepostapp.domain.PostEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PostsRemoteDataSource @Inject constructor() {

    companion object {
        const val GENERIC_ERROR_MSG = "Something wrong happened. Try again later"
        const val DEFAULT_DELAY_TIME = 2L
    }

    private var firstTime = true
    val posts = mutableListOf(
        PostEntity("Hello World", "Lorem ipsum dolor sit"),
        PostEntity("Hi", "This is the first post"),
    )
        @VisibleForTesting get
    private val postsSubject = BehaviorSubject.create<List<PostEntity>>()

    fun getPosts(): Observable<List<PostEntity>> {
        // Simulates API Call
        if (firstTime) {
            firstTime = false
            return Observable.error<List<PostEntity>>(GenericException(GENERIC_ERROR_MSG))
                .delay(DEFAULT_DELAY_TIME, TimeUnit.SECONDS)
        }

        postsSubject.onNext(posts)
        return postsSubject.delay(DEFAULT_DELAY_TIME, TimeUnit.SECONDS)
    }

    fun removePost(post: PostEntity): Observable<List<PostEntity>> {
        postsSubject.onNext(posts.apply { remove(post) })
        return postsSubject.delay(DEFAULT_DELAY_TIME, TimeUnit.SECONDS)
    }

    fun addPost(post: PostEntity): Observable<List<PostEntity>> {
        postsSubject.onNext(
            posts.apply {
                add(post.copy(added = true))
            },
        )
        return postsSubject.delay(DEFAULT_DELAY_TIME, TimeUnit.SECONDS)
    }

    fun searchPosts(input: String): Observable<List<PostEntity>> {
        val filteredPosts = if (input.isEmpty()) {
            posts
        } else {
            posts.filter { post -> post.name.contains(input) }
        }
        postsSubject.onNext(filteredPosts)
        return postsSubject
    }
}
